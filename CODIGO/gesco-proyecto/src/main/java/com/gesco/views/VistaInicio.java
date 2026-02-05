package com.gesco.views;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.gesco.views.PlantillasViews.BotonNeon;
import com.gesco.views.PlantillasViews.PlantillaGesco;

public class VistaInicio extends PlantillaGesco {

    private BotonNeon btnRegistrarse;
    private BotonNeon btnInicioSesion;
    private JLabel Saludo;

    public VistaInicio() {
        super(); 
        inicializarComponentes();
        construirCuerpo();
        revalidate();
        repaint();
        setVisible(true);
    }

    private void inicializarComponentes() {
        Dimension tam = new Dimension(400, 60);

        btnInicioSesion = new BotonNeon("Inicio de sesión");
        btnInicioSesion.setPreferredSize(tam);
        btnInicioSesion.setMaximumSize(tam);
        btnInicioSesion.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnRegistrarse = new BotonNeon("Registrarse");
        btnRegistrarse.setPreferredSize(tam);
        btnRegistrarse.setMaximumSize(tam);
        btnRegistrarse.setAlignmentX(Component.CENTER_ALIGNMENT);

        Saludo = new JLabel("¡Hola, Ucevista!");
        Saludo.setFont(new Font("Arial", Font.BOLD, 45)); 
        Saludo.setForeground(new Color(240, 240, 240)); 
        Saludo.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void construirCuerpo() {
        JPanel panelFondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Color translúcido
                g2.setColor(new Color(255, 255, 255, 10)); 
                g2.fillRoundRect(100, 10, getWidth() - 200, getHeight() - 20, 50, 50);
                g2.setColor(new Color(255, 255, 255, 25));
                g2.drawRoundRect(100, 10, getWidth() - 200, getHeight() - 20, 50, 50);
                g2.dispose();
            }
        };
        
        panelFondo.setLayout(new BoxLayout(panelFondo, BoxLayout.Y_AXIS));
        panelFondo.setOpaque(false); 
        panelFondo.setBorder(BorderFactory.createEmptyBorder(60, 20, 60, 20));

        //Elementos del panel
        panelFondo.add(Box.createVerticalGlue()); 
        panelFondo.add(Saludo);
        panelFondo.add(Box.createVerticalStrut(30)); 
        panelFondo.add(crearEtiquetaSimple("Accede a la plataforma del comedor", 24, new Color(180, 180, 185)));
        
        panelFondo.add(Box.createVerticalStrut(80)); 
        panelFondo.add(crearEtiquetaSimple("¿Ya posee una cuenta?", 25, new Color(150, 150, 155)));
        panelFondo.add(Box.createVerticalStrut(25));
        panelFondo.add(btnInicioSesion); 

        panelFondo.add(Box.createVerticalStrut(70)); 
        panelFondo.add(crearEtiquetaSimple("¿Primera vez accediendo?", 25, new Color(110, 110, 115)));
        panelFondo.add(Box.createVerticalStrut(20));
        panelFondo.add(btnRegistrarse); 
        
        panelFondo.add(Box.createVerticalGlue());

        this.contenedorPrincipal.add(panelFondo, BorderLayout.CENTER);
    }

    public BotonNeon getBtnRegistrarse() {
        return btnRegistrarse;
    }

    public BotonNeon getBtnInicioSesion() {
        return btnInicioSesion;
    }
    
}