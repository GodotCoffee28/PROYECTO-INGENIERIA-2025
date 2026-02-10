package com.gesco.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.net.URL;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.gesco.views.PlantillasViews.BotonNeon;
import com.gesco.views.PlantillasViews.PlantillaGesco;

public class VistaEspera extends PlantillaGesco {
    private BotonNeon volver;
    private JLabel anuncio;
    private ImageIcon taza;

    public VistaEspera() {
        super(); 
        inicializarComponentes();
        construirCuerpo();
        revalidate();
        repaint();
        setVisible(true);
    }

    private void inicializarComponentes() {
        Dimension tamBoton = new Dimension(400, 60);

        volver = new BotonNeon("Volver al inicio");
        volver.setPreferredSize(tamBoton);
        volver.setMaximumSize(tamBoton);
        volver.setAlignmentX(Component.CENTER_ALIGNMENT);

        anuncio = crearEtiquetaSimple("Estamos verificando sus datos, intente ingresar en breve.", 32, new Color(240, 240, 240));
        anuncio.setFont(new Font("Arial", Font.BOLD, 32));
        
        taza = cargarTaza();
        if (taza.getIconWidth() > 0) {
            Image imagenEscalada = taza.getImage().getScaledInstance(280, 280, Image.SCALE_SMOOTH);
            taza = new ImageIcon(imagenEscalada);
        }
    }

    private ImageIcon cargarTaza() {
        URL recurso = VistaEspera.class.getResource("src\\main\\resources\\Taza_VistaEspera.png");
        if (recurso != null) {
            return new ImageIcon(recurso);
        }

        File archivo = new File(System.getProperty("user.dir"),
            "src\\\\main\\\\resources\\\\Taza_VistaEspera.png");
        if (archivo.exists()) {
            return new ImageIcon(archivo.getAbsolutePath());
        }

        return new ImageIcon();
    }

    private void construirCuerpo() {
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setOpaque(false); 
        
        panelContenido.add(Box.createVerticalGlue()); 

        if (taza != null && taza.getIconWidth() > 0) {
            JLabel lblTaza = new JLabel(taza);
            lblTaza.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelContenido.add(lblTaza);
        }

        panelContenido.add(Box.createVerticalStrut(10));
        
        panelContenido.add(anuncio);
        
        panelContenido.add(Box.createVerticalStrut(80));
        
        panelContenido.add(volver); 

        panelContenido.add(Box.createVerticalGlue());

        this.contenedorPrincipal.add(panelContenido, BorderLayout.CENTER);
    }

    public BotonNeon getVolver() {
        return volver;
    }
}