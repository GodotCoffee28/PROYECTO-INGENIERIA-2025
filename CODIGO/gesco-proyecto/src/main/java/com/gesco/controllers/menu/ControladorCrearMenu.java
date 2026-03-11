package com.gesco.controllers.menu;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;

import com.gesco.controllers.gestion_principal.DataBase;
import com.gesco.models.menu.Insumo;
import com.gesco.models.menu.Menu;
import com.gesco.models.menu.Platillo;
import com.gesco.views.menu.VistaCrearMenu;

public class ControladorCrearMenu {

    private final VistaCrearMenu vista;
    private final Runnable onBack;
    private final Runnable onSaveSuccess;

    public ControladorCrearMenu(VistaCrearMenu vista, Runnable onBack) {
        this(vista, onBack, onBack);
    }

    public ControladorCrearMenu(VistaCrearMenu vista, Runnable onBack, Runnable onSaveSuccess) {
        this.vista = vista;
        this.onBack = onBack;
        this.onSaveSuccess = onSaveSuccess == null ? onBack : onSaveSuccess;
    }

    public void conectar() {
        List<Insumo> insumosDisponibles = filtrarInsumosConStock(DataBase.obtenerInsumos());
        vista.setInsumos(insumosDisponibles);
        conectarAccionesInsumos();
        actualizarTodosLosSpinners();
        vista.addFechaChangeListener(this::actualizarDiaSemanaSeleccionado);
        actualizarDiaSemanaSeleccionado();
        vista.getBackIcon().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onBack.run();
            }
        });
        vista.getBtnCrear().addActionListener(e -> crearMenu());
    }

    private void conectarAccionesInsumos() {
        configurarAccionesInsumos(
            vista.getComboInsumo1(),
            vista.getSpinnerCantidad1(),
            vista.getModeloInsumos1(),
            vista.getListaInsumos1(),
            vista.getBtnAgregarInsumo1(),
            vista.getBtnQuitarInsumo1()
        );
        configurarAccionesInsumos(
            vista.getComboInsumo2(),
            vista.getSpinnerCantidad2(),
            vista.getModeloInsumos2(),
            vista.getListaInsumos2(),
            vista.getBtnAgregarInsumo2(),
            vista.getBtnQuitarInsumo2()
        );
        configurarAccionesInsumos(
            vista.getComboInsumo3(),
            vista.getSpinnerCantidad3(),
            vista.getModeloInsumos3(),
            vista.getListaInsumos3(),
            vista.getBtnAgregarInsumo3(),
            vista.getBtnQuitarInsumo3()
        );
    }

    private void configurarAccionesInsumos(
        JComboBox<Insumo> combo,
        JSpinner spinner,
        DefaultListModel<Insumo> modelo,
        JList<Insumo> lista,
        JButton btnAgregar,
        JButton btnQuitar
    ) {
        combo.addActionListener(e -> actualizarSpinnerSegunDisponibilidad(combo, spinner, modelo));

        btnAgregar.addActionListener(e -> {
            Insumo base = (Insumo) combo.getSelectedItem();
            if (base == null) return;

            int cantidad = ((Number) spinner.getValue()).intValue();
            if (cantidad <= 0) return;

            int stock = base.getCantidad();
            int existente = obtenerCantidadExistente(modelo, base);
            if (cantidad + existente > stock) {
                JOptionPane.showMessageDialog(vista,
                    "La cantidad supera el stock disponible (" + stock + ").",
                    "Stock insuficiente",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            float unitario = base.getCostoUnitario();
            int nuevaCantidad = cantidad + existente;
            Insumo nuevo = new Insumo(base.getNombre(), nuevaCantidad, base.getTipoNutricional(), unitario);

            int index = buscarInsumo(modelo, base);
            if (index >= 0) {
                modelo.set(index, nuevo);
            } else {
                modelo.addElement(nuevo);
            }

            actualizarSpinnerSegunDisponibilidad(combo, spinner, modelo);
        });

        btnQuitar.addActionListener(e -> {
            int index = lista.getSelectedIndex();
            if (index >= 0) {
                modelo.remove(index);
                actualizarSpinnerSegunDisponibilidad(combo, spinner, modelo);
            }
        });
    }

    private List<Insumo> filtrarInsumosConStock(List<Insumo> insumos) {
        List<Insumo> disponibles = new ArrayList<>();
        if (insumos == null) {
            return disponibles;
        }
        for (Insumo insumo : insumos) {
            if (insumo != null && insumo.getCantidad() > 0) {
                disponibles.add(insumo);
            }
        }
        return disponibles;
    }

    private void actualizarTodosLosSpinners() {
        actualizarSpinnerSegunDisponibilidad(vista.getComboInsumo1(), vista.getSpinnerCantidad1(), vista.getModeloInsumos1());
        actualizarSpinnerSegunDisponibilidad(vista.getComboInsumo2(), vista.getSpinnerCantidad2(), vista.getModeloInsumos2());
        actualizarSpinnerSegunDisponibilidad(vista.getComboInsumo3(), vista.getSpinnerCantidad3(), vista.getModeloInsumos3());
    }

    private void actualizarSpinnerSegunDisponibilidad(
        JComboBox<Insumo> combo,
        JSpinner spinner,
        DefaultListModel<Insumo> modelo
    ) {
        Insumo seleccionado = (Insumo) combo.getSelectedItem();
        if (seleccionado == null) {
            spinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 0, 1));
            spinner.setEnabled(false);
            return;
        }

        int existente = obtenerCantidadExistente(modelo, seleccionado);
        int restante = seleccionado.getCantidad() - existente;
        if (restante <= 0) {
            spinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 0, 1));
            spinner.setEnabled(false);
            return;
        }

        spinner.setModel(new javax.swing.SpinnerNumberModel(1, 1, restante, 1));
        spinner.setEnabled(true);
    }

    private int buscarInsumo(DefaultListModel<Insumo> modelo, Insumo base) {
        for (int i = 0; i < modelo.size(); i++) {
            Insumo actual = modelo.getElementAt(i);
            if (actual.getNombre().equalsIgnoreCase(base.getNombre())
                && actual.getTipoNutricional().equalsIgnoreCase(base.getTipoNutricional())) {
                return i;
            }
        }
        return -1;
    }

    private int obtenerCantidadExistente(DefaultListModel<Insumo> modelo, Insumo base) {
        int index = buscarInsumo(modelo, base);
        if (index >= 0) {
            return modelo.getElementAt(index).getCantidad();
        }
        return 0;
    }

    private void crearMenu() {
        try {
            LocalDate fecha = obtenerFechaSeleccionada();
            if (!DataBase.esFechaValidaParaMenu(fecha)) {
                javax.swing.JOptionPane.showMessageDialog(vista,
                    "Solo se permiten días hábiles (lunes a viernes no feriados).",
                    "Fecha no permitida",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean noDisponible = vista.isMenuNoDisponibleSeleccionado();
            Menu menu = new Menu(fecha, noDisponible ? Menu.EstadoMenu.NO_DISPONIBLE : Menu.EstadoMenu.CON_MENU);
            Menu.TipoMenu tipoMenu = vista.getTipoMenu();
            if (tipoMenu != null) {
                menu.setTipoMenu(tipoMenu);
            }
        
            String p1 = vista.getPlatillo1();
            String p2 = vista.getPlatillo2();
            String p3 = vista.getPlatillo3();

            List<Insumo> insumos1 = vista.getInsumosPlatillo1();
            List<Insumo> insumos2 = vista.getInsumosPlatillo2();
            List<Insumo> insumos3 = vista.getInsumosPlatillo3();

            if (!noDisponible) {
                if (p1 != null && !p1.isBlank() && insumos1.isEmpty()) {
                    javax.swing.JOptionPane.showMessageDialog(vista,
                        "El elemento 1 debe tener al menos un insumo.",
                        "Elemento sin insumos",
                        javax.swing.JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (p2 != null && !p2.isBlank() && insumos2.isEmpty()) {
                    javax.swing.JOptionPane.showMessageDialog(vista,
                        "El elemento 2 debe tener al menos un insumo.",
                        "Elemento sin insumos",
                        javax.swing.JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (p3 != null && !p3.isBlank() && insumos3.isEmpty()) {
                    javax.swing.JOptionPane.showMessageDialog(vista,
                        "El elemento 3 debe tener al menos un insumo.",
                        "Elemento sin insumos",
                        javax.swing.JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            if (p1 != null && !p1.isBlank()) {
                if (!esNombrePlatilloValido(p1)) {
                    mostrarErrorNombrePlatillo(1);
                    return;
                }
                Platillo platillo = new Platillo(p1.trim());
                agregarInsumos(platillo, insumos1);
                menu.agregarPlatillo(platillo);
            }
            if (p2 != null && !p2.isBlank()) {
                if (!esNombrePlatilloValido(p2)) {
                    mostrarErrorNombrePlatillo(2);
                    return;
                }
                Platillo platillo = new Platillo(p2.trim());
                agregarInsumos(platillo, insumos2);
                menu.agregarPlatillo(platillo);
            }
            if (p3 != null && !p3.isBlank()) {
                if (!esNombrePlatilloValido(p3)) {
                    mostrarErrorNombrePlatillo(3);
                    return;
                }
                Platillo platillo = new Platillo(p3.trim());
                agregarInsumos(platillo, insumos3);
                menu.agregarPlatillo(platillo);
            }

            if (!noDisponible && !menu.tienePlatillos()) {
                javax.swing.JOptionPane.showMessageDialog(vista,
                    "Debe cargar al menos un elemento del menu o marcar la opción 'Menu no disponible'.",
                    "Menú vacío",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean ok = DataBase.actualizarMenu(menu);
            if (ok) {
                javax.swing.JOptionPane.showMessageDialog(vista,
                    noDisponible ? "Día marcado como menú no disponible." : "Menú creado.",
                    "OK",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
                onSaveSuccess.run();
            } else {
                javax.swing.JOptionPane.showMessageDialog(vista, "Error al crear menú.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException | java.time.format.DateTimeParseException | NullPointerException ex) {
            javax.swing.JOptionPane.showMessageDialog(vista, "Fecha inválida. Use formato DD MM AAAA.", "Error", javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }

    private void agregarInsumos(Platillo platillo, List<Insumo> insumos) {
        if (insumos == null) return;
        for (Insumo insumo : insumos) {
            platillo.agregarInsumo(insumo);
        }
    }

    private boolean esNombrePlatilloValido(String nombre) {
        String nombreLimpio = nombre == null ? "" : nombre.trim();
        return !nombreLimpio.isEmpty() && nombreLimpio.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+$");
    }

    private void mostrarErrorNombrePlatillo(int numeroPlatillo) {
        JOptionPane.showMessageDialog(
            vista,
            "El nombre del elemento " + numeroPlatillo + " solo puede contener letras y espacios.",
            "Nombre de elemento inválido",
            JOptionPane.WARNING_MESSAGE
        );
    }

    private void actualizarDiaSemanaSeleccionado() {
        try {
            LocalDate fecha = obtenerFechaSeleccionada();
            vista.setDiaSemanaTexto(capitalizar(fecha.getDayOfWeek().getDisplayName(java.time.format.TextStyle.FULL, new Locale("es", "ES"))));
        } catch (Exception ex) {
            vista.setDiaSemanaTexto("-");
        }
    }

    private LocalDate obtenerFechaSeleccionada() {
        String dia = vista.getDia();
        String mes = vista.getMes();
        String anio = vista.getAnio();
        String fechaStr = String.format("%s-%02d-%02d",
            anio.trim(),
            Integer.parseInt(mes.trim()),
            Integer.parseInt(dia.trim()));
        return LocalDate.parse(fechaStr);
    }

    private String capitalizar(String texto) {
        if (texto == null || texto.isBlank()) return texto;
        return texto.substring(0, 1).toUpperCase(new Locale("es", "ES")) + texto.substring(1);
    }
}





