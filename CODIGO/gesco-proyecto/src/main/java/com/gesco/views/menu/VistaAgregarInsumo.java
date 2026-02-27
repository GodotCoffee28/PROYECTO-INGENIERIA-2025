package com.gesco.views.menu;

import java.awt.*;
import javax.swing.*;

import com.gesco.views.PlantillasViews.BotonNeon;
import com.gesco.views.PlantillasViews.PlantillaGesco;

public class VistaAgregarInsumo  extends PlantillaGesco {
    private BotonNeon btnCrear;
    private JTextField insumoNombre, insumoCantidad, insumoPrecioUnitario;
    private JComboBox<String> comboTipoNutricional;

    
    public VistaAgregarInsumo() {
        super();
        setImagenFondo("/FondoPrincipal2.png");
        ocultarIcono();
        ocultarlblSprAdmin();
        inicializarComponentes();
        construirCuerpo();
        revalidate();
        repaint();
        setVisible(true);
    }


    private void inicializarComponentes() {
        Dimension tamBoton = new Dimension(400, 60);
        btnCrear = new BotonNeon("Agregar");
        btnCrear.setPreferredSize(tamBoton);
        btnCrear.setMaximumSize(tamBoton);
        btnCrear.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
    

    private void agregarCampos(JPanel panelFondoBase) {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setMaximumSize(new Dimension(450, 260));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        Dimension tamCaja = new Dimension(450, 40);

        formPanel.add(crearEtiquetaPersonalizada("Nombre", "Times New Roman", Font.PLAIN, 19, Color.WHITE, "izquierda"));
        formPanel.add(Box.createVerticalStrut(8));
        insumoNombre = new JTextField();
        diseñarCaja(insumoNombre, tamCaja);
        formPanel.add(insumoNombre);
        formPanel.add(Box.createVerticalStrut(16));

        formPanel.add(crearEtiquetaPersonalizada("Cantidad", "Times New Roman", Font.PLAIN, 19, Color.WHITE, "izquierda"));
        formPanel.add(Box.createVerticalStrut(8));
        insumoCantidad = new JTextField();
        diseñarCaja(insumoCantidad, tamCaja);
        formPanel.add(insumoCantidad);
        formPanel.add(Box.createVerticalStrut(16));

        formPanel.add(crearEtiquetaPersonalizada("Tipo nutricional", "Times New Roman", Font.PLAIN, 19, Color.WHITE, "izquierda"));
        formPanel.add(Box.createVerticalStrut(8));
        comboTipoNutricional = new JComboBox<>(new String[] {
            "Proteina",
            "Carbohidrato",
            "Grasa",
            "Fibra",
            "Vitamina",
            "Mineral",
            "Lacteo",
            "Fruta",
            "Verdura"
        });
        comboTipoNutricional.setPreferredSize(tamCaja);
        comboTipoNutricional.setMaximumSize(tamCaja);
        comboTipoNutricional.setMinimumSize(tamCaja);
        comboTipoNutricional.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboTipoNutricional.setFont(new Font("Arial", Font.PLAIN, 16));
        comboTipoNutricional.setBackground(new Color(255, 255, 255, 240));
        formPanel.add(comboTipoNutricional);
        formPanel.add(Box.createVerticalStrut(16));

        formPanel.add(crearEtiquetaPersonalizada("Precio unitario", "Times New Roman", Font.PLAIN, 19, Color.WHITE, "izquierda"));
        formPanel.add(Box.createVerticalStrut(8));
        insumoPrecioUnitario = new JTextField();
        diseñarCaja(insumoPrecioUnitario, tamCaja);
        formPanel.add(insumoPrecioUnitario);

        panelFondoBase.add(formPanel);
    }


    private void construirCuerpo() {
        JPanel panelFondo = crearPanel(250, 15, 500, 30, 50, 50);

        panelFondo.setLayout(new BoxLayout(panelFondo, BoxLayout.Y_AXIS));
        panelFondo.setOpaque(false);
        panelFondo.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        panelFondo.add(Box.createVerticalGlue());
        panelFondo.add(crearEtiquetaPersonalizada("Agregar insumo", "Times New Roman", Font.BOLD, 45, new Color(240, 240, 240), "centro"));
        panelFondo.add(Box.createVerticalStrut(20));

        agregarCampos(panelFondo);

        panelFondo.add(Box.createVerticalStrut(20));
        panelFondo.add(btnCrear);
        panelFondo.add(Box.createVerticalGlue());

        this.contenedorPrincipal.add(panelFondo, BorderLayout.CENTER);
    }


    public BotonNeon getBtnCrear() { return btnCrear; }
    public String getNombreInsumo() { return insumoNombre.getText(); }
    public String getCantidadInsumo() { return insumoCantidad.getText(); }
    public String getTipoNutricional() {
        Object tipoSeleccionado = comboTipoNutricional.getSelectedItem();
        return tipoSeleccionado == null ? "" : tipoSeleccionado.toString();
    }
    public String getPrecioUnitario() { return insumoPrecioUnitario.getText(); }
}


