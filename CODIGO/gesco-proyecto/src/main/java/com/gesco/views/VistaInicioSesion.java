package com.gesco.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Cursor;

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

        Saludo = new JLabel("¡Hola de nuevo!");
        Saludo.setFont(new Font("Arial", Font.BOLD, 45)); 
        Saludo.setForeground(new Color(240, 240, 240)); 
        Saludo.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
    
    private void agregarCampos(JPanel panelFondoBase) {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setMaximumSize(new Dimension(450, 200)); 
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT); 

        //Cédula
        JLabel lblCedula = new JLabel("Cédula de identidad");
        lblCedula.setFont(new Font("Arial", Font.BOLD, 18));
        lblCedula.setForeground(Color.WHITE);
        lblCedula.setAlignmentX(Component.LEFT_ALIGNMENT);
        //Caja de texto para la cédula
        cedula = new JTextField();
        Dimension tamanoCaja = new Dimension(450, 40);
        cedula.setPreferredSize(tamanoCaja);
        cedula.setMaximumSize(tamanoCaja);
        cedula.setMinimumSize(tamanoCaja);
        cedula.setAlignmentX(Component.LEFT_ALIGNMENT);
        cedula.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        //Contraseña
        JLabel lblClave = new JLabel("Contraseña");
        lblClave.setFont(new Font("Arial", Font.BOLD, 18));
        lblClave.setForeground(Color.WHITE);
        lblClave.setAlignmentX(Component.LEFT_ALIGNMENT);
        //Caja de texto contraseña
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
        JPanel panelFondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Color del vidrio
                g2.setColor(new Color(255, 255, 255, 25)); 
                g2.fillRoundRect(250, 10, getWidth() - 500, getHeight() - 20, 50, 50);
                
                g2.setColor(new Color(255, 255, 255, 40));
                g2.drawRoundRect(250, 10, getWidth() - 500, getHeight() - 20, 50, 50);
                g2.dispose();
            }
        };
        
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
        
        registroLink = new JLabel("¿Aún no tiene una cuenta? Registrarse");
        registroLink.setForeground(new Color(220, 220, 220));
        registroLink.setFont(new Font("Arial", Font.PLAIN, 15));
        registroLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registroLink.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelFondo.add(registroLink);

        panelFondo.add(Box.createVerticalStrut(30)); 
        panelFondo.add(btnInicioSesion);  
        
        panelFondo.add(Box.createVerticalGlue());

        this.contenedorPrincipal.add(panelFondo, BorderLayout.CENTER);
    }

   //Getters (Gabriel/Alejandro)
   public String getCedula(){ return cedula.getText();}
   public String getClave(){ return clave.getText();}
    public BotonNeon getBtnInicioSesion() { return btnInicioSesion; }
    public JLabel getRegistroLink() { return registroLink; }
}