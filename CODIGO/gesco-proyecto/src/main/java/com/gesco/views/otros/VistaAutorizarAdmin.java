package com.gesco.views.otros;

import java.awt.*;

import javax.swing.*;


import com.gesco.views.PlantillasViews.BotonNeon;
import com.gesco.views.PlantillasViews.PlantillaGesco;

public class VistaAutorizarAdmin  extends PlantillaGesco {
    private BotonNeon btnCrear;
    private JTextField cedula;
    private JTextField codigo;

    private JLabel Titulo;

    
    public VistaAutorizarAdmin() {
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
        btnCrear = new BotonNeon("Autorizar");
        btnCrear.setPreferredSize(tamBoton);
        btnCrear.setMaximumSize(tamBoton);
        btnCrear.setAlignmentX(Component.CENTER_ALIGNMENT);
        Titulo = crearEtiquetaPersonalizada("Autorizar administrador", "Times New Roman", Font.BOLD, 45, new Color(240, 240, 240), "centro");
        Titulo.setFont(new Font("Arial", Font.BOLD, 45));

    }
    


    private void agregarCampos(JPanel panelFondoBase) {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setMaximumSize(new Dimension(450, 260));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        Dimension tamCaja = new Dimension(450, 40);

        formPanel.add(crearEtiquetaPersonalizada("Cédula", "Times New Roman", Font.PLAIN, 19, Color.WHITE, "izquierda"));
        formPanel.add(Box.createVerticalStrut(8));
        cedula = new JTextField();
        diseñarCaja(cedula, tamCaja);
        formPanel.add(cedula);
        formPanel.add(Box.createVerticalStrut(16));

        formPanel.add(crearEtiquetaPersonalizada("Código", "Times New Roman", Font.PLAIN, 19, Color.WHITE, "izquierda"));
        formPanel.add(Box.createVerticalStrut(8));
        codigo = new JTextField();
        diseñarCaja(codigo, tamCaja);
        formPanel.add(codigo);
        formPanel.add(Box.createVerticalStrut(16));

        panelFondoBase.add(formPanel);
    }

    private void construirCuerpo() {
        JPanel panelFondo = crearPanel(250, 15, 500, 30, 50, 50);

        panelFondo.setLayout(new BoxLayout(panelFondo, BoxLayout.Y_AXIS));
        panelFondo.setOpaque(false);
        panelFondo.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        panelFondo.add(Box.createVerticalGlue());
        panelFondo.add(Titulo);
        panelFondo.add(Box.createVerticalStrut(20));

        agregarCampos(panelFondo);

        panelFondo.add(Box.createVerticalStrut(20));
        panelFondo.add(btnCrear);
        panelFondo.add(Box.createVerticalGlue());

        this.contenedorPrincipal.add(panelFondo, BorderLayout.CENTER);
    }

    public BotonNeon getBtnCrear() { return btnCrear; }
    public String getCedula() { return cedula.getText(); }
    public String getCodigo() { return codigo.getText(); }


}


