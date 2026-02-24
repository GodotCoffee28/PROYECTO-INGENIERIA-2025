package com.gesco.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import com.gesco.models.Insumo;
import com.gesco.models.Menu;
import com.gesco.views.PlantillasViews.BotonNeon;
import com.gesco.views.PlantillasViews.CampoFecha;
import com.gesco.views.PlantillasViews.PlantillaGesco;

public class VistaCrearMenu extends PlantillaGesco {
    private BotonNeon btnCrear;
    private JTextField platillo1, platillo2, platillo3;
    private CampoFecha campoFecha;
    private JLabel Titulo;
    private JCheckBox chkNoDisponible;
    private JComboBox<Menu.TipoMenu> comboTipoMenu;
    private JComboBox<Insumo> comboInsumo1, comboInsumo2, comboInsumo3;
    private JSpinner spinnerCantidad1, spinnerCantidad2, spinnerCantidad3;
    private JButton btnAgregarInsumo1, btnAgregarInsumo2, btnAgregarInsumo3;
    private JButton btnQuitarInsumo1, btnQuitarInsumo2, btnQuitarInsumo3;
    private DefaultListModel<Insumo> modeloInsumos1, modeloInsumos2, modeloInsumos3;
    private JList<Insumo> listaInsumos1, listaInsumos2, listaInsumos3;
    
    public VistaCrearMenu() {
        super();
        setImagenFondo("/FondoPrincipal2.png");
        inicializarComponentes();
        construirCuerpo();
        revalidate();
        repaint();
        setVisible(true);
    }

    private void inicializarComponentes() {
        Dimension tamBoton = new Dimension(400, 60);
        btnCrear = new BotonNeon("Crear");
        btnCrear.setPreferredSize(tamBoton);
        btnCrear.setMaximumSize(tamBoton);
        btnCrear.setAlignmentX(Component.CENTER_ALIGNMENT);
        Titulo = crearEtiquetaPersonalizada("Crear menú", "Times New Roman", Font.BOLD, 45, new Color(240, 240, 240), "centro");
        Titulo.setFont(new Font("Arial", Font.BOLD, 45));

    }
    
    private void agregarCampos(JPanel panelIzqPlatillos, JPanel panelDerInsumos) {
        int pading = 18;
        panelIzqPlatillos.setLayout(new BoxLayout(panelIzqPlatillos, BoxLayout.Y_AXIS));
        panelIzqPlatillos.setOpaque(true);
        panelIzqPlatillos.setPreferredSize(new Dimension(600, 540));
        panelIzqPlatillos.setMaximumSize(new Dimension(600, 540));
        panelIzqPlatillos.setBorder(BorderFactory.createEmptyBorder(pading, pading, pading, pading));
        panelIzqPlatillos.setAlignmentY(Component.TOP_ALIGNMENT);

        panelDerInsumos.setLayout(new BoxLayout(panelDerInsumos, BoxLayout.Y_AXIS));
        panelDerInsumos.setOpaque(true);
        panelDerInsumos.setPreferredSize(new Dimension(600, 540));
        panelDerInsumos.setMaximumSize(new Dimension(600, 540));
        panelDerInsumos.setBorder(BorderFactory.createEmptyBorder(pading, pading, pading, pading));
        panelDerInsumos.setAlignmentY(Component.TOP_ALIGNMENT);

        campoFecha = new CampoFecha("Fecha", "Times New Roman", Font.PLAIN, 18, Color.WHITE, "izquierda");
        campoFecha.setLabelColor(Color.WHITE);
        campoFecha.setAlignmentX(Component.LEFT_ALIGNMENT);
        java.time.LocalDate hoy = java.time.LocalDate.now();
        campoFecha.setFecha(
            String.valueOf(hoy.getDayOfMonth()),
            String.valueOf(hoy.getMonthValue()),
            String.valueOf(hoy.getYear())
        );
        panelIzqPlatillos.add(campoFecha);
        panelIzqPlatillos.add(Box.createVerticalStrut(20));

        Dimension tamCaja = new Dimension(450, 40);

        panelIzqPlatillos.add(crearEtiquetaPersonalizada("Tipo de menu", "Times New Roman", Font.PLAIN, 18, Color.WHITE, "izquierda"));
        panelIzqPlatillos.add(Box.createVerticalStrut(8));
        comboTipoMenu = new JComboBox<>(Menu.TipoMenu.values());
        comboTipoMenu.setMaximumSize(tamCaja);
        comboTipoMenu.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelIzqPlatillos.add(comboTipoMenu);
        panelIzqPlatillos.add(Box.createVerticalStrut(20));

        panelIzqPlatillos.add(crearEtiquetaPersonalizada("Platillo 1", "Times New Roman", Font.PLAIN, 18, Color.WHITE, "izquierda"));
        panelIzqPlatillos.add(Box.createVerticalStrut(8));
        platillo1 = new JTextField();
        diseñarCaja(platillo1, tamCaja);
        panelIzqPlatillos.add(platillo1);
        panelIzqPlatillos.add(Box.createVerticalStrut(10));

        modeloInsumos1 = new DefaultListModel<>();
        listaInsumos1 = new JList<>(modeloInsumos1);
        comboInsumo1 = new JComboBox<>();
        spinnerCantidad1 = new JSpinner();
        btnAgregarInsumo1 = new JButton("Agregar insumo");
        btnQuitarInsumo1 = new JButton("Eliminar insumo");
        agregarSeccionInsumos(panelDerInsumos, "Insumos platillo 1", comboInsumo1, spinnerCantidad1,
            btnAgregarInsumo1, btnQuitarInsumo1, listaInsumos1);

        panelIzqPlatillos.add(crearEtiquetaPersonalizada("Platillo 2", "Times New Roman", Font.PLAIN, 18, Color.WHITE, "izquierda"));
        panelIzqPlatillos.add(Box.createVerticalStrut(8));
        platillo2 = new JTextField();
        diseñarCaja(platillo2, tamCaja);
        panelIzqPlatillos.add(platillo2);
        panelIzqPlatillos.add(Box.createVerticalStrut(10));

        modeloInsumos2 = new DefaultListModel<>();
        listaInsumos2 = new JList<>(modeloInsumos2);
        comboInsumo2 = new JComboBox<>();
        spinnerCantidad2 = new JSpinner();
        btnAgregarInsumo2 = new JButton("Agregar insumo");
        btnQuitarInsumo2 = new JButton("Eliminar insumo");
        agregarSeccionInsumos(panelDerInsumos, "Insumos platillo 2", comboInsumo2, spinnerCantidad2,
            btnAgregarInsumo2, btnQuitarInsumo2, listaInsumos2);

        panelIzqPlatillos.add(crearEtiquetaPersonalizada("Platillo 3", "Times New Roman", Font.PLAIN, 18, Color.WHITE, "izquierda"));
        panelIzqPlatillos.add(Box.createVerticalStrut(8));
        platillo3 = new JTextField();
        diseñarCaja(platillo3, tamCaja);
        panelIzqPlatillos.add(platillo3);
        panelIzqPlatillos.add(Box.createVerticalStrut(10));

        modeloInsumos3 = new DefaultListModel<>();
        listaInsumos3 = new JList<>(modeloInsumos3);
        comboInsumo3 = new JComboBox<>();
        spinnerCantidad3 = new JSpinner();
        btnAgregarInsumo3 = new JButton("Agregar insumo");
        btnQuitarInsumo3 = new JButton("Eliminar insumo");
        agregarSeccionInsumos(panelDerInsumos, "Insumos platillo 3", comboInsumo3, spinnerCantidad3,
            btnAgregarInsumo3, btnQuitarInsumo3, listaInsumos3);

        configurarAccionesInsumos(comboInsumo1, spinnerCantidad1, modeloInsumos1, listaInsumos1,
            btnAgregarInsumo1, btnQuitarInsumo1);
        configurarAccionesInsumos(comboInsumo2, spinnerCantidad2, modeloInsumos2, listaInsumos2,
            btnAgregarInsumo2, btnQuitarInsumo2);
        configurarAccionesInsumos(comboInsumo3, spinnerCantidad3, modeloInsumos3, listaInsumos3,
            btnAgregarInsumo3, btnQuitarInsumo3);

        chkNoDisponible = new JCheckBox("Menu no disponible para este día");
        chkNoDisponible.setOpaque(false);
        chkNoDisponible.setForeground(Color.WHITE);
        chkNoDisponible.setFont(new Font("Arial", Font.BOLD, 14));
        chkNoDisponible.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelIzqPlatillos.add(chkNoDisponible);
    }

    private void construirCuerpo() {
        JPanel panelFondo = crearPanel(120, 10, 1100, 50, 50, 50);
        JPanel panelIzqPlatillo = crearPanel(10, 10, 20, 20, 50, 50);
        JPanel panelDerInsumo = crearPanel(10, 10, 20, 20, 50, 50);
        JPanel panelContenido = crearPanel(120, 100, 1100, 900, 50, 50);
        
        panelFondo.setLayout(new BoxLayout(panelFondo, BoxLayout.Y_AXIS));
        panelFondo.setOpaque(false);
        panelFondo.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));

        panelFondo.add(Box.createVerticalGlue());
        panelFondo.add(Titulo);
        panelFondo.add(Box.createVerticalStrut(25));

        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.X_AXIS));
        panelContenido.setOpaque(false);
        panelContenido.setAlignmentX(Component.CENTER_ALIGNMENT);

        agregarCampos(panelIzqPlatillo, panelDerInsumo);

        panelContenido.add(panelIzqPlatillo);
        panelContenido.add(Box.createHorizontalStrut(30));
        panelContenido.add(panelDerInsumo);

        panelFondo.add(panelContenido);
        
        panelFondo.add(Box.createVerticalStrut(30));
        panelFondo.add(btnCrear);
        panelFondo.add(Box.createVerticalGlue());

        this.contenedorPrincipal.add(panelFondo, BorderLayout.CENTER);
    }

    public String getPlatillo1(){ return platillo1.getText();}
    public String getPlatillo2(){ return platillo2.getText();}
    public String getPlatillo3(){ return platillo3.getText();}
    public BotonNeon getBtnCrear() { return btnCrear; }
    public String getDia(){ return campoFecha.getDia();}
    public String getMes(){ return campoFecha.getMes();}
    public String getAnio(){ return campoFecha.getAnio();}
    public String getFechaTexto(){ return campoFecha.getFechaTexto();}
    public boolean isMenuNoDisponibleSeleccionado() { return chkNoDisponible.isSelected(); }
    public Menu.TipoMenu getTipoMenu() { return (Menu.TipoMenu) comboTipoMenu.getSelectedItem(); }
    public List<Insumo> getInsumosPlatillo1() { return obtenerInsumosDesdeModelo(modeloInsumos1); }
    public List<Insumo> getInsumosPlatillo2() { return obtenerInsumosDesdeModelo(modeloInsumos2); }
    public List<Insumo> getInsumosPlatillo3() { return obtenerInsumosDesdeModelo(modeloInsumos3); }

    public void setFecha(String dia, String mes, String anio) {
        campoFecha.setFecha(dia, mes, anio);
        campoFecha.setEditable(false);
    }

    public void setInsumos(List<Insumo> insumos) {
        List<Insumo> disponibles = new ArrayList<>();
        if (insumos != null) {
            for (Insumo insumo : insumos) {
                if (insumo != null && insumo.getCantidad() > 0) {
                    disponibles.add(insumo);
                }
            }
        }

        cargarInsumosEnCombo(comboInsumo1, spinnerCantidad1, modeloInsumos1, disponibles);
        cargarInsumosEnCombo(comboInsumo2, spinnerCantidad2, modeloInsumos2, disponibles);
        cargarInsumosEnCombo(comboInsumo3, spinnerCantidad3, modeloInsumos3, disponibles);
    }

    private void agregarSeccionInsumos(
        JPanel formPanel,
        String titulo,
        JComboBox<Insumo> combo,
        JSpinner spinner,
        JButton btnAgregar,
        JButton btnQuitar,
        JList<Insumo> lista
    ) {
        formPanel.add(crearEtiquetaPersonalizada(titulo, "Times New Roman", Font.PLAIN, 18, Color.WHITE, "centro"));
        formPanel.add(Box.createVerticalStrut(6));

        JPanel filaCombo = new JPanel();
        filaCombo.setLayout(new BoxLayout(filaCombo, BoxLayout.X_AXIS));
        filaCombo.setOpaque(false);
        combo.setMaximumSize(new Dimension(340, 30));
        filaCombo.add(combo);
        filaCombo.add(Box.createHorizontalStrut(8));
        spinner.setMaximumSize(new Dimension(80, 30));
        filaCombo.add(spinner);
        formPanel.add(filaCombo);
        formPanel.add(Box.createVerticalStrut(6));

        JPanel filaBotones = new JPanel();
        filaBotones.setLayout(new BoxLayout(filaBotones, BoxLayout.X_AXIS));
        filaBotones.setOpaque(false);
        filaBotones.add(btnAgregar);
        filaBotones.add(Box.createHorizontalStrut(8));
        filaBotones.add(btnQuitar);
        formPanel.add(filaBotones);
        formPanel.add(Box.createVerticalStrut(6));

        JScrollPane scroll = new JScrollPane(lista);
        scroll.setPreferredSize(new Dimension(450, 80));
        formPanel.add(scroll);
        formPanel.add(Box.createVerticalStrut(16));
    }

    private void configurarAccionesInsumos(
        JComboBox<Insumo> combo,
        JSpinner spinner,
        DefaultListModel<Insumo> modelo,
        JList<Insumo> lista,
        JButton btnAgregar,
        JButton btnQuitar
    ) {
        combo.addActionListener(e -> actualizarSpinner(combo, spinner, modelo));

        btnAgregar.addActionListener(e -> {
            Insumo base = (Insumo) combo.getSelectedItem();
            if (base == null) return;

            int cantidad = ((Number) spinner.getValue()).intValue();
            if (cantidad <= 0) return;

            int stock = base.getCantidad();
            int existente = obtenerCantidadExistente(modelo, base);
            if (cantidad + existente > stock) {
                JOptionPane.showMessageDialog(this,
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

            actualizarSpinner(combo, spinner, modelo);
        });

        btnQuitar.addActionListener(e -> {
            int index = lista.getSelectedIndex();
            if (index >= 0) {
                modelo.remove(index);
                actualizarSpinner(combo, spinner, modelo);
            }
        });
    }

    private void cargarInsumosEnCombo(
        JComboBox<Insumo> combo,
        JSpinner spinner,
        DefaultListModel<Insumo> modelo,
        List<Insumo> insumos
    ) {
        combo.removeAllItems();
        for (Insumo insumo : insumos) {
            combo.addItem(insumo);
        }
        actualizarSpinner(combo, spinner, modelo);
    }

    private void actualizarSpinner(
        JComboBox<Insumo> combo,
        JSpinner spinner,
        DefaultListModel<Insumo> modelo
    ) {
        Insumo seleccionado = (Insumo) combo.getSelectedItem();
        if (seleccionado == null) {
            spinner.setModel(new SpinnerNumberModel(0, 0, 0, 1));
            spinner.setEnabled(false);
            return;
        }

        int existente = obtenerCantidadExistente(modelo, seleccionado);
        int restante = seleccionado.getCantidad() - existente;
        if (restante <= 0) {
            spinner.setModel(new SpinnerNumberModel(0, 0, 0, 1));
            spinner.setEnabled(false);
            return;
        }

        spinner.setModel(new SpinnerNumberModel(1, 1, restante, 1));
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

    private List<Insumo> obtenerInsumosDesdeModelo(DefaultListModel<Insumo> modelo) {
        List<Insumo> resultado = new ArrayList<>();
        for (int i = 0; i < modelo.size(); i++) {
            resultado.add(modelo.getElementAt(i));
        }
        return resultado;
    }
}
