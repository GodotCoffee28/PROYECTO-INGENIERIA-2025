package com.gesco.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;


import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.gesco.views.PlantillasViews.BotonNeon;
import com.gesco.views.PlantillasViews.PlantillaGesco;
public class VistaGestionMenu extends PlantillaGesco {
     private BotonNeon btnEditar, btnCrear, btnReiniciar;
    private JLabel Titulo;

    public VistaGestionMenu() {
        super(); 
        inicializarComponentes();
        construirCuerpo();
        revalidate();
        repaint();
        setVisible(true);
    }

    private void inicializarComponentes() {
        Dimension tam = new Dimension(500, 80);

        btnEditar = new BotonNeon("Editar menú");
        btnEditar.setPreferredSize(tam);
        btnEditar.setMaximumSize(tam);
        btnEditar.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnCrear = new BotonNeon("Crear menú");
        btnCrear.setPreferredSize(tam);
        btnCrear.setMaximumSize(tam);
        btnCrear.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnReiniciar = new BotonNeon("Reiniciar menú");
        btnReiniciar.setPreferredSize(tam);
        btnReiniciar.setMaximumSize(tam);
        btnReiniciar.setAlignmentX(Component.CENTER_ALIGNMENT);

        Titulo = crearEtiquetaSimple("Gestión del menú", 32, new Color(240, 240, 240));
        Titulo.setFont(new Font("Arial", Font.BOLD, 32));
        
    }

    private void construirCuerpo() {
        JPanel panelFondo = crearPanel();
        
        panelFondo.setLayout(new BoxLayout(panelFondo, BoxLayout.Y_AXIS));
        panelFondo.setOpaque(false); 
        panelFondo.setBorder(BorderFactory.createEmptyBorder(60, 20, 60, 20));
        
        panelFondo.add(Box.createVerticalGlue());
        panelFondo.add(Titulo);
        panelFondo.add(Box.createVerticalStrut(80));

        panelFondo.add(btnEditar);
        panelFondo.add(Box.createVerticalStrut(50));

        panelFondo.add(btnCrear);
        panelFondo.add(Box.createVerticalStrut(50));
        
        panelFondo.add(btnReiniciar);
        panelFondo.add(Box.createVerticalGlue());

        this.contenedorPrincipal.add(panelFondo, BorderLayout.CENTER);
    }

    public BotonNeon getBtnEditar() { return btnEditar; }
    public BotonNeon getBtnCrear() { return btnCrear; }
    public BotonNeon getBtnReiniciar() { return btnReiniciar; }
}
