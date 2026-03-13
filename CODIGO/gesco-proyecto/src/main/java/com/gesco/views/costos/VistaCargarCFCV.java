package com.gesco.views.costos;

import java.awt.*;
import javax.swing.*;

import com.gesco.views.PlantillasViews.BotonNeon;
import com.gesco.views.PlantillasViews.PlantillaGesco;

public class VistaCargarCFCV extends PlantillaGesco {
    private BotonNeon btnGuardar;
    private JTextField CF, CV;
    private JLabel lblResultado;

    public VistaCargarCFCV() {
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
        Dimension tamBoton = new Dimension(300, 50);
        btnGuardar = new BotonNeon("Guardar");
        btnGuardar.setPreferredSize(tamBoton);
        btnGuardar.setMaximumSize(tamBoton);
        btnGuardar.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblResultado = crearEtiquetaPersonalizada(" ", "Arial", Font.BOLD, 18, new Color(240, 240, 240), "centro");
        lblResultado.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void agregarCampos(JPanel panelFondoBase) {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setMaximumSize(new Dimension(450, 200));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        Dimension tamCaja = new Dimension(450, 40);

        formPanel.add(crearEtiquetaPersonalizada("CF (Costos Fijos)", "Times New Roman", Font.PLAIN, 19, Color.WHITE, "izquierda"));
        formPanel.add(Box.createVerticalStrut(8));
        CF = new JTextField();
        diseñarCaja(CF, tamCaja);
        formPanel.add(CF);
        formPanel.add(Box.createVerticalStrut(16));

        formPanel.add(crearEtiquetaPersonalizada("CV (Costos Variables)", "Times New Roman", Font.PLAIN, 19, Color.WHITE, "izquierda"));
        formPanel.add(Box.createVerticalStrut(8));
        CV = new JTextField();
        diseñarCaja(CV, tamCaja);
        formPanel.add(CV);

        panelFondoBase.add(formPanel);
    }

    private void construirCuerpo() {
        JPanel panelFondo = crearPanel(250,15,500,30,50,50);

        panelFondo.setLayout(new BoxLayout(panelFondo, BoxLayout.Y_AXIS));
        panelFondo.setOpaque(false);
        panelFondo.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        panelFondo.add(Box.createVerticalGlue());
        panelFondo.add(crearEtiquetaPersonalizada("Cargar Costos: Fijo y Variable", "Times New Roman", Font.BOLD, 36, new Color(240, 240, 240), "centro"));
        panelFondo.add(Box.createVerticalStrut(20));

        agregarCampos(panelFondo);

        panelFondo.add(Box.createVerticalStrut(16));
        panelFondo.add(lblResultado);
        panelFondo.add(Box.createVerticalStrut(16));
        panelFondo.add(btnGuardar);
        panelFondo.add(Box.createVerticalGlue());

        this.contenedorPrincipal.add(panelFondo, BorderLayout.CENTER);
    }

    public String getCF() { return CF.getText(); }
    public String getCV() { return CV.getText(); }
    public BotonNeon getBtnGuardar() { return btnGuardar; }
    public void setResultado(String texto) { this.lblResultado.setText(texto); }
}

