package com.gesco.views.auntentificacion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.gesco.views.PlantillasViews.BotonNeon;
import com.gesco.views.PlantillasViews.PlantillaGesco;

public class VistaInicioSesion extends PlantillaGesco {
    private BotonNeon btnInicioSesion;
    private BotonNeon btnAccesoFacial;
    private JTextField cedula, clave;
    private JLabel Saludo;
    private JLabel registroLink;
    
    public VistaInicioSesion() {
        super(); 
        setImagenFondo("/VISTAFONDO.png");
        ocultarMenu();
        inicializarComponentes();
        construirCuerpo();
        revalidate();
        repaint();
        setVisible(true);
    }

    private void inicializarComponentes() {
        Dimension tam = new Dimension(400, 60);
        Dimension tamFacial = new Dimension(400, 50);

        btnInicioSesion = new BotonNeon("Iniciar sesión");
        btnInicioSesion.setPreferredSize(tam);
        btnInicioSesion.setMaximumSize(tam);
        btnInicioSesion.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnAccesoFacial = new BotonNeon("Acceso Facial");
        btnAccesoFacial.setPreferredSize(tamFacial);
        btnAccesoFacial.setMaximumSize(tamFacial);
        btnAccesoFacial.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAccesoFacial.setFont(new Font("Arial", Font.BOLD, 22));

        Saludo = crearEtiquetaPersonalizada("¡Hola de nuevo!", "Times New Roman", Font.BOLD, 45, new Color(240, 240, 240), "centro");
    }

    private void agregarCampos(JPanel panelFondoBase) {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setMaximumSize(new Dimension(450, 250));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblCedula = crearEtiquetaPersonalizada("Cédula de identidad", "Times New Roman", Font.PLAIN, 18, Color.WHITE, "izquierda");
        lblCedula.setAlignmentX(Component.LEFT_ALIGNMENT);

        cedula = new JTextField();
        Dimension tamanoCaja = new Dimension(450, 40);
        cedula.setPreferredSize(tamanoCaja);
        cedula.setMaximumSize(tamanoCaja);
        cedula.setMinimumSize(tamanoCaja);
        cedula.setAlignmentX(Component.LEFT_ALIGNMENT);
        cedula.setBorder(BorderFactory.createCompoundBorder(
            cedula.getBorder(),
            BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));

        JLabel lblClave = crearEtiquetaPersonalizada("Contraseña", "Times New Roman", Font.PLAIN, 18, Color.WHITE, "izquierda");
        lblClave.setAlignmentX(Component.LEFT_ALIGNMENT);

        clave = new JPasswordField();
        clave.setPreferredSize(tamanoCaja);
        clave.setMaximumSize(tamanoCaja);
        clave.setMinimumSize(tamanoCaja);
        clave.setAlignmentX(Component.LEFT_ALIGNMENT);
        clave.setBorder(BorderFactory.createCompoundBorder(
            clave.getBorder(),
            BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));

        formPanel.add(lblCedula);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(cedula);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(lblClave);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(clave);

        panelFondoBase.add(formPanel);
    }

    private void construirCuerpo() {
        JPanel panelFondo = crearPanel(250, 10, 500, 20, 50, 50);

        panelFondo.setLayout(new BoxLayout(panelFondo, BoxLayout.Y_AXIS));
        panelFondo.setOpaque(false);
        panelFondo.setBorder(BorderFactory.createEmptyBorder(60, 20, 60, 20));

        panelFondo.add(Box.createVerticalGlue());

        panelFondo.add(Saludo);
        panelFondo.add(Box.createVerticalStrut(10));

        JLabel subTitulo = crearEtiquetaPersonalizada("Accede a la plataforma", "Times New Roman", Font.PLAIN, 20, new Color(200, 200, 205), "centro");
        subTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelFondo.add(subTitulo);

        panelFondo.add(Box.createVerticalStrut(40));

        agregarCampos(panelFondo);

        panelFondo.add(Box.createVerticalStrut(20));

        registroLink = crearEtiquetaPersonalizada("¿Aún no tiene una cuenta? Registrarse", "Times New Roman", Font.PLAIN, 15, new Color(220, 220, 220), "centro");
        registroLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelFondo.add(registroLink);

        panelFondo.add(Box.createVerticalStrut(30));
        panelFondo.add(btnInicioSesion);

        panelFondo.add(Box.createVerticalStrut(14));

        JLabel lblO = crearEtiquetaPersonalizada("── o también ──", "Times New Roman", Font.ITALIC, 14, new Color(180, 180, 185), "centro");
        lblO.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelFondo.add(lblO);

        panelFondo.add(Box.createVerticalStrut(10));
        panelFondo.add(btnAccesoFacial);

        panelFondo.add(Box.createVerticalGlue());

        this.contenedorPrincipal.add(panelFondo, BorderLayout.CENTER);
    }

    public String getCedula(){ return cedula.getText();}
    public String getClave(){ return clave.getText();}
    public BotonNeon getBtnInicioSesion() { return btnInicioSesion; }
    public BotonNeon getBtnAccesoFacial() { return btnAccesoFacial; }
    public JLabel getRegistroLink() { return registroLink; }
}
