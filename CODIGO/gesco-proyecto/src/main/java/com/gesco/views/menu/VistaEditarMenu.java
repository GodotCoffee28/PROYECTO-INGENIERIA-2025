package com.gesco.views.menu;

import java.awt.*;

import java.util.ArrayList;
import java.util.List;

import javax.swing.*;


import com.gesco.models.menu.Insumo;
import com.gesco.models.menu.Menu;
import com.gesco.views.PlantillasViews.BotonNeon;
import com.gesco.views.PlantillasViews.CampoFecha;
import com.gesco.views.PlantillasViews.PlantillaGesco;

public class VistaEditarMenu extends PlantillaGesco {

    private BotonNeon btnEditar;
    private JTextField platillo1, platillo2, platillo3;
    private CampoFecha campoFecha;
    private JCheckBox chkNoDisponible;
    private JLabel Titulo, elementoMenu, lblDiaSemana;
    private JComboBox<Menu.TipoMenu> comboTipoMenu;
    private JComboBox<Insumo> comboInsumo1, comboInsumo2, comboInsumo3;
    private JSpinner spinnerCantidad1, spinnerCantidad2, spinnerCantidad3;
    private JButton btnAgregarInsumo1, btnAgregarInsumo2, btnAgregarInsumo3;
    private JButton btnQuitarInsumo1, btnQuitarInsumo2, btnQuitarInsumo3;
    private DefaultListModel<Insumo> modeloInsumos1, modeloInsumos2, modeloInsumos3;
    private JList<Insumo> listaInsumos1, listaInsumos2, listaInsumos3;
    
    public VistaEditarMenu() {
        super();
        ocultarIcono();
        setImagenFondo("/FondoPrincipal2.png");
        inicializarComponentes();
        construirCuerpo();
        revalidate();
        repaint(); 
        setVisible(true);
    }

    private void inicializarComponentes() {
        Dimension tamBoton = new Dimension(400, 60);
        btnEditar = new BotonNeon("Editar");
        btnEditar.setPreferredSize(tamBoton);
        btnEditar.setMaximumSize(tamBoton);
        btnEditar.setAlignmentX(Component.CENTER_ALIGNMENT);

        Titulo = crearEtiquetaPersonalizada("Editar menú", "Times New Roman", Font.BOLD, 45, new Color(240, 240, 240), "centro");

         lblDiaSemana = crearEtiquetaPersonalizada("Día seleccionado: -", "Times New Roman", Font.BOLD, 18, new Color(210, 210, 210), "centro");
        lblDiaSemana.setAlignmentX(Component.CENTER_ALIGNMENT);
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
        panelDerInsumos.setMaximumSize(new Dimension(600, 540)); //tamaño de la caja
        panelDerInsumos.setBorder(BorderFactory.createEmptyBorder(pading, pading, pading, pading)); //Pading 
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
        panelIzqPlatillos.add(Box.createVerticalStrut(8));
        lblDiaSemana.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelIzqPlatillos.add(lblDiaSemana);
        panelIzqPlatillos.add(Box.createVerticalStrut(8));

        Dimension tamCaja = new Dimension(450, 50);

        panelIzqPlatillos.add(crearEtiquetaPersonalizada("Tipo de menú", "Times New Roman", Font.PLAIN, 18, Color.WHITE, "izquierda"));
        panelIzqPlatillos.add(Box.createVerticalStrut(8));
        comboTipoMenu = new JComboBox<>(Menu.TipoMenu.values());
        comboTipoMenu.setMaximumSize(tamCaja);
        comboTipoMenu.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelIzqPlatillos.add(comboTipoMenu);
        panelIzqPlatillos.add(Box.createVerticalStrut(10));

        elementoMenu= crearEtiquetaPersonalizada("Elementos del menú", "Times New Roman", Font.BOLD, 18, Color.WHITE, "izquierda");
        panelIzqPlatillos.add(elementoMenu);
        panelIzqPlatillos.add(Box.createVerticalStrut(8));

        ImageIcon iconoPlatito = obtenerIcono("/Platos.png", 25);

        JLabel lbl1 = crearEtiquetaPersonalizada(" 1", "Times New Roman", Font.BOLD, 18, Color.WHITE, "izquierda");
        lbl1.setIcon(iconoPlatito);
        lbl1.setIconTextGap(10);
        panelIzqPlatillos.add(lbl1);
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
        agregarSeccionInsumos(panelDerInsumos, "Insumos del elemento 1", comboInsumo1, spinnerCantidad1,
            btnAgregarInsumo1, btnQuitarInsumo1, listaInsumos1);
        
        JLabel lbl2 = crearEtiquetaPersonalizada(" 2", "Times New Roman", Font.BOLD, 18, Color.WHITE, "izquierda");
        lbl2.setIcon(iconoPlatito);
        lbl2.setIconTextGap(10);
        panelIzqPlatillos.add(lbl2);
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
        agregarSeccionInsumos(panelDerInsumos, "Insumos del elemento 2", comboInsumo2, spinnerCantidad2,
            btnAgregarInsumo2, btnQuitarInsumo2, listaInsumos2);

        JLabel lbl3 = crearEtiquetaPersonalizada(" 3", "Times New Roman", Font.BOLD, 18, Color.WHITE, "izquierda");
        lbl3.setIcon(iconoPlatito);
        lbl3.setIconTextGap(10);
        panelIzqPlatillos.add(lbl3);
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
        agregarSeccionInsumos(panelDerInsumos, "Insumos del elemento 3", comboInsumo3, spinnerCantidad3,
            btnAgregarInsumo3, btnQuitarInsumo3, listaInsumos3);

        chkNoDisponible = new JCheckBox("Menú no disponible para este día");
        chkNoDisponible.setOpaque(false);
        chkNoDisponible.setForeground(Color.WHITE);
        chkNoDisponible.setFont(new Font("Arial", Font.BOLD, 14));
        chkNoDisponible.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelIzqPlatillos.add(chkNoDisponible);
    }

    private void construirCuerpo() {

    JPanel panelFondo = crearPanel(5, 5, 10, 10, 50, 50);
    panelFondo.setLayout(new BoxLayout(panelFondo, BoxLayout.Y_AXIS));
    panelFondo.setOpaque(true); 
    panelFondo.setBorder(BorderFactory.createEmptyBorder(10, 25, 20, 25));
    panelFondo.setOpaque(false);

    JPanel contenedorTitulo = crearPanel(0, 0, 5, 5, 30, 30);
    contenedorTitulo.setLayout(new GridBagLayout());
    contenedorTitulo.setOpaque(false);

    Dimension dimTitulo = new Dimension(600, 65);
    contenedorTitulo.setPreferredSize(dimTitulo);
    contenedorTitulo.setMaximumSize(dimTitulo);
    contenedorTitulo.add(Titulo);
    contenedorTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

    panelFondo.add(contenedorTitulo);
    panelFondo.add(Box.createVerticalStrut(20)); 

    JPanel panelContenido = new JPanel();
    panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.X_AXIS));
    panelContenido.setOpaque(false);
    panelContenido.setAlignmentX(Component.CENTER_ALIGNMENT);

    JPanel panelIzqPlatillo = crearPanel(0, 0, 5, 5, 50, 50);
    JPanel panelDerInsumo = crearPanel(0, 0, 5, 5, 50, 50);

    agregarCampos(panelIzqPlatillo, panelDerInsumo); 

    panelContenido.add(panelIzqPlatillo);
    panelContenido.add(Box.createHorizontalStrut(25));
    panelContenido.add(panelDerInsumo);

    panelFondo.add(panelContenido);

    panelFondo.add(Box.createVerticalStrut(25)); 
    btnEditar.setAlignmentX(Component.CENTER_ALIGNMENT);
    panelFondo.add(btnEditar); 
    panelFondo.add(Box.createVerticalGlue());

    this.contenedorPrincipal.add(panelFondo, BorderLayout.CENTER);
}

    public void setInsumos(List<Insumo> insumos) {
        cargarInsumosEnCombo(comboInsumo1, insumos);
        cargarInsumosEnCombo(comboInsumo2, insumos);
        cargarInsumosEnCombo(comboInsumo3, insumos);
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

    private void cargarInsumosEnCombo(
        JComboBox<Insumo> combo,
        List<Insumo> insumos
    ) {
        combo.removeAllItems();
        if (insumos == null) {
            return;
        }
        for (Insumo insumo : insumos) {
            if (insumo != null) {
                combo.addItem(insumo);
            }
        }
    }

    private List<Insumo> obtenerInsumosDesdeModelo(DefaultListModel<Insumo> modelo) {
        List<Insumo> resultado = new ArrayList<>();
        for (int i = 0; i < modelo.size(); i++) {
            resultado.add(modelo.getElementAt(i));
        }
        return resultado;
    }

    public BotonNeon getBtnEditar() { return btnEditar; }
    public String getPlatillo1(){ return platillo1.getText(); }
    public String getPlatillo2(){ return platillo2.getText(); }
    public String getPlatillo3(){ return platillo3.getText(); }
    public String getDia(){ return campoFecha.getDia(); }
    public String getMes(){ return campoFecha.getMes(); }
    public String getAnio(){ return campoFecha.getAnio(); }
    public String getFechaTexto(){ return campoFecha.getFechaTexto(); }
    public void setDiaSemanaTexto(String texto) {
        if (lblDiaSemana != null) {
            String valor = (texto == null || texto.isBlank()) ? "-" : texto;
            lblDiaSemana.setText("Día seleccionado: " + valor);
        }
    }

    public void addFechaChangeListener(Runnable listener) {
        if (campoFecha != null) {
            campoFecha.addFechaChangeListener(listener);
        }
    }
    public boolean isMenuNoDisponibleSeleccionado() { return chkNoDisponible.isSelected(); }
    public Menu.TipoMenu getTipoMenu() { return (Menu.TipoMenu) comboTipoMenu.getSelectedItem(); }
    public void setTipoMenu(Menu.TipoMenu tipoMenu) { comboTipoMenu.setSelectedItem(tipoMenu); }
    public JComboBox<Insumo> getComboInsumo1() { return comboInsumo1; }
    public JComboBox<Insumo> getComboInsumo2() { return comboInsumo2; }
    public JComboBox<Insumo> getComboInsumo3() { return comboInsumo3; }
    public JSpinner getSpinnerCantidad1() { return spinnerCantidad1; }
    public JSpinner getSpinnerCantidad2() { return spinnerCantidad2; }
    public JSpinner getSpinnerCantidad3() { return spinnerCantidad3; }
    public JButton getBtnAgregarInsumo1() { return btnAgregarInsumo1; }
    public JButton getBtnAgregarInsumo2() { return btnAgregarInsumo2; }
    public JButton getBtnAgregarInsumo3() { return btnAgregarInsumo3; }
    public JButton getBtnQuitarInsumo1() { return btnQuitarInsumo1; }
    public JButton getBtnQuitarInsumo2() { return btnQuitarInsumo2; }
    public JButton getBtnQuitarInsumo3() { return btnQuitarInsumo3; }
    public DefaultListModel<Insumo> getModeloInsumos1() { return modeloInsumos1; }
    public DefaultListModel<Insumo> getModeloInsumos2() { return modeloInsumos2; }
    public DefaultListModel<Insumo> getModeloInsumos3() { return modeloInsumos3; }
    public JList<Insumo> getListaInsumos1() { return listaInsumos1; }
    public JList<Insumo> getListaInsumos2() { return listaInsumos2; }
    public JList<Insumo> getListaInsumos3() { return listaInsumos3; }
    public List<Insumo> getInsumosPlatillo1() { return obtenerInsumosDesdeModelo(modeloInsumos1); }
    public List<Insumo> getInsumosPlatillo2() { return obtenerInsumosDesdeModelo(modeloInsumos2); }
    public List<Insumo> getInsumosPlatillo3() { return obtenerInsumosDesdeModelo(modeloInsumos3); }
}

