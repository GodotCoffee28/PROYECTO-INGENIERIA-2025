package com.gesco.controllers.menu;


import com.gesco.controllers.gestion_principal.DataBase;
import com.gesco.views.menu.VistaEditarMenu;
import com.gesco.models.menu.Insumo;
import com.gesco.models.menu.Menu;
import com.gesco.models.menu.Platillo;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import java.util.List;

public class ControladorEditarMenu {

    private final VistaEditarMenu vista;
    private final Runnable onBack;
    public ControladorEditarMenu(VistaEditarMenu vista, Runnable onBack) {
        this.vista = vista;
        this.onBack = onBack;
    }

    public void conectar() {
        vista.setInsumos(DataBase.obtenerInsumos());
        conectarAccionesInsumos();
        vista.getBackIcon().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onBack.run();
            }
        });

        vista.getBtnEditar().addActionListener(e -> guardarEdicion());
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
        combo.addActionListener(e -> vista.actualizarSpinner(combo, spinner, modelo));

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

            vista.actualizarSpinner(combo, spinner, modelo);
        });

        btnQuitar.addActionListener(e -> {
            int index = lista.getSelectedIndex();
            if (index >= 0) {
                modelo.remove(index);
                vista.actualizarSpinner(combo, spinner, modelo);
            }
        });
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

    private void guardarEdicion() {
        try {
            String dia = vista.getDia();
            String mes = vista.getMes();
            String anio = vista.getAnio();
            String fechaStr = String.format("%s-%02d-%02d",
                anio.trim(),
                Integer.parseInt(mes.trim()),
                Integer.parseInt(dia.trim()));

            java.time.LocalDate fecha = java.time.LocalDate.parse(fechaStr);
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
                        "El platillo 1 debe tener al menos un insumo.",
                        "Platillo sin insumos",
                        javax.swing.JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (p2 != null && !p2.isBlank() && insumos2.isEmpty()) {
                    javax.swing.JOptionPane.showMessageDialog(vista,
                        "El platillo 2 debe tener al menos un insumo.",
                        "Platillo sin insumos",
                        javax.swing.JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (p3 != null && !p3.isBlank() && insumos3.isEmpty()) {
                    javax.swing.JOptionPane.showMessageDialog(vista,
                        "El platillo 3 debe tener al menos un insumo.",
                        "Platillo sin insumos",
                        javax.swing.JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            if (p1 != null && !p1.isBlank()) {
                Platillo platillo = new Platillo(p1.trim());
                agregarInsumos(platillo, insumos1);
                menu.agregarPlatillo(platillo);
            }
            if (p2 != null && !p2.isBlank()) {
                Platillo platillo = new Platillo(p2.trim());
                agregarInsumos(platillo, insumos2);
                menu.agregarPlatillo(platillo);
            }
            if (p3 != null && !p3.isBlank()) {
                Platillo platillo = new Platillo(p3.trim());
                agregarInsumos(platillo, insumos3);
                menu.agregarPlatillo(platillo);
            }

            if (!noDisponible && !menu.tienePlatillos()) {
                javax.swing.JOptionPane.showMessageDialog(vista,
                    "No se puede actualizar a menú vacío. Agregue un platillo o marque 'Menu no disponible'.",
                    "Menú inválido",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean ok = DataBase.actualizarMenu(menu);
            if (ok) {
                javax.swing.JOptionPane.showMessageDialog(vista,
                    noDisponible ? "Día actualizado a menú no disponible." : "Menú actualizado.",
                    "OK",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
                onBack.run();
            } else {
                javax.swing.JOptionPane.showMessageDialog(vista, "Error al actualizar menú.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(vista, "Fecha inválida. Use formato DD MM AAAA.", "Error", javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }

    private void agregarInsumos(Platillo platillo, List<Insumo> insumos) {
        if (insumos == null) return;
        for (Insumo insumo : insumos) {
            platillo.agregarInsumo(insumo);
        }
    }
}





