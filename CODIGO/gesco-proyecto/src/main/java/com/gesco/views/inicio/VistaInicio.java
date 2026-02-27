package com.gesco.views.inicio;
import java.awt.*;
import javax.swing.*;


import com.gesco.views.PlantillasViews.BotonNeon;
import com.gesco.views.PlantillasViews.PlantillaGesco;

public class VistaInicio extends PlantillaGesco {

    private BotonNeon btnRegistrarse;
    private BotonNeon btnInicioSesion;

    public VistaInicio() {
        super(); 
        setImagenFondo("/VISTAFONDO.png");
        ocultarMenu();
        ocultarBack();
        ocultarIcono();
        ocultarlblSprAdmin();
        inicializarComponentes();
        construirCuerpo();
        revalidate();
        repaint();
        setVisible(true);
    }

    private void inicializarComponentes() {
        Dimension tam = new Dimension(400, 70);

        btnInicioSesion = new BotonNeon("Inicio de sesión");
        btnInicioSesion.setPreferredSize(tam);
        btnInicioSesion.setMaximumSize(tam);
        btnInicioSesion.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnRegistrarse = new BotonNeon("Registrarse");
        btnRegistrarse.setPreferredSize(tam);
        btnRegistrarse.setMaximumSize(tam);
        btnRegistrarse.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void construirCuerpo() {
        JPanel panelFondo = crearPanel(100,10, 200, 20, 50,50);
        
        panelFondo.setLayout(new BoxLayout(panelFondo, BoxLayout.Y_AXIS));
        panelFondo.setOpaque(false); 
        panelFondo.setBorder(BorderFactory.createEmptyBorder(60, 20, 60, 20));

        panelFondo.add(Box.createVerticalGlue()); 
        panelFondo.add(crearEtiquetaPersonalizada("¡Hola, Ucevista!", "Times New Roman", Font.BOLD, 60, new Color(240, 240, 240), "centro"));
        panelFondo.add(Box.createVerticalStrut(30)); 
        panelFondo.add(crearEtiquetaPersonalizada("Accede a la plataforma del comedor", "Times New Roman", Font.PLAIN, 28, new Color(240, 240, 240), "centro"));
        
        panelFondo.add(Box.createVerticalStrut(40)); 
        panelFondo.add(crearEtiquetaPersonalizada("¿Ya posee una cuenta?", "Times New Roman", Font.PLAIN, 25, new Color(240, 240, 240), "centro"));
        panelFondo.add(Box.createVerticalStrut(25));
        panelFondo.add(btnInicioSesion); 

        panelFondo.add(Box.createVerticalStrut(40)); 
        panelFondo.add(crearEtiquetaPersonalizada("¿Primera vez accediendo?", "Times New Roman", Font.PLAIN, 25, new Color(240, 240, 240), "centro"));
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
