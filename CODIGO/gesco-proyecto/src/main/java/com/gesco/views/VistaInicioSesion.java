package com.gesco.views;

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
    private JTextField cedula, clave;
    private JLabel Saludo;
    private JLabel registroLink;
    
    public VistaInicioSesion() {
        super(); 
        ocultarMenu();
        inicializarComponentes();
        construirCuerpo();
        revalidate();
        repaint();
        setVisible(true);
    }

    private void inicializarComponentes() {
        Dimension tam = new Dimension(400, 60);

        btnInicioSesion = new BotonNeon("Iniciar sesión");
        btnInicioSesion.setPreferredSize(tam);
        btnInicioSesion.setMaximumSize(tam);
        btnInicioSesion.setAlignmentX(Component.CENTER_ALIGNMENT);

        Saludo = crearEtiquetaSimple("¡Hola de nuevo!", 45, new Color(240, 240, 240));
        Saludo.setFont(new Font("Arial", Font.BOLD, 45)); 
    }
    
    private void agregarCampos(JPanel panelFondoBase) {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setMaximumSize(new Dimension(450, 200)); 
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT); 

        //Cédula de pepe xd
        JLabel lblCedula = crearEtiquetaForm("Cédula de identidad", 18, Color.WHITE);

        cedula = new JTextField();
        Dimension tamanoCaja = new Dimension(450, 40);
        cedula.setPreferredSize(tamanoCaja);
        cedula.setMaximumSize(tamanoCaja);
        cedula.setMinimumSize(tamanoCaja);
        cedula.setAlignmentX(Component.LEFT_ALIGNMENT);
        cedula.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        JLabel lblClave = crearEtiquetaForm("Contraseña", 18, Color.WHITE);

        clave = new JPasswordField();
        clave.setPreferredSize(tamanoCaja);
        clave.setMaximumSize(tamanoCaja);
        clave.setMinimumSize(tamanoCaja);
        clave.setAlignmentX(Component.LEFT_ALIGNMENT);
        clave.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

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
        JPanel panelFondo = crearPanel();
        
        panelFondo.setLayout(new BoxLayout(panelFondo, BoxLayout.Y_AXIS));
        panelFondo.setOpaque(false); 
        panelFondo.setBorder(BorderFactory.createEmptyBorder(60, 20, 60, 20));

        panelFondo.add(Box.createVerticalGlue()); 
        
        panelFondo.add(Saludo);
        panelFondo.add(Box.createVerticalStrut(10));
        
        JLabel subTitulo = crearEtiquetaSimple("Accede a la plataforma", 20, new Color(200, 200, 205));
        subTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelFondo.add(subTitulo);
        
        panelFondo.add(Box.createVerticalStrut(40)); 

        agregarCampos(panelFondo); 
        
        panelFondo.add(Box.createVerticalStrut(20));
        
        registroLink = crearEtiquetaSimple("¿Aún no tiene una cuenta? Registrarse", 15, new Color(220, 220, 220));
        registroLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelFondo.add(registroLink);

        panelFondo.add(Box.createVerticalStrut(30)); 
        panelFondo.add(btnInicioSesion);  
        
        panelFondo.add(Box.createVerticalGlue());

        this.contenedorPrincipal.add(panelFondo, BorderLayout.CENTER);
    }

    public String getCedula(){ return cedula.getText();}
    public String getClave(){ return clave.getText();}
    public BotonNeon getBtnInicioSesion() { return btnInicioSesion; }
    public JLabel getRegistroLink() { return registroLink; }
}