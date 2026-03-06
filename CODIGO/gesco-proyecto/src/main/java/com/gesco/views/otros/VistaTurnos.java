package com.gesco.views.otros;

import java.awt.*;

import java.util.ArrayList;
import java.util.List;

import javax.swing.*;


import com.gesco.views.PlantillasViews.BotonNeon;
import com.gesco.views.PlantillasViews.PlantillaGesco;

public class VistaTurnos extends PlantillaGesco {

    private List<TarjetaTurno> listaTurnos;
    private JLabel titulo;
    private JPanel panelContenedorTarjetas;
    private BotonNeon btnVerMenu;

    public VistaTurnos() {
        super(); 
        setImagenFondo("/FondoPrincipal.png");     
        inicializarComponentes();
        ocultarlblSprAdmin();
        construirCuerpo();
        cargarDatosPrueba(); 
        revalidate();
        repaint();
        setVisible(true);
    }

    private void inicializarComponentes() {
        titulo = crearEtiquetaPersonalizada("Turnos de comida", "Times New Roman", Font.BOLD, 30, Color.WHITE,  "centro");
        titulo.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        
        listaTurnos = new ArrayList<>();
        
        btnVerMenu = new BotonNeon("Ver menú del día");

        btnVerMenu.setFont(new Font("Arial", Font.BOLD, 20)); 
        btnVerMenu.setPreferredSize(new Dimension(250, 60));
    }

    private void construirCuerpo() {
        JPanel panelFondo = crearPanel(100,20,200,40,50,50);
        
        panelFondo.setLayout(new BorderLayout());
        panelFondo.setOpaque(false);
        panelFondo.setBorder(BorderFactory.createEmptyBorder(40, 120, 40, 120));

        JPanel panelTitulo = new JPanel();
        panelTitulo.setLayout(new BoxLayout(panelTitulo, BoxLayout.Y_AXIS));
        panelTitulo.setOpaque(false);
        panelTitulo.add(titulo);
        panelTitulo.add(Box.createVerticalStrut(30));
        panelContenedorTarjetas = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        panelContenedorTarjetas.setOpaque(false);

        JScrollPane scroll = new JScrollPane(panelContenedorTarjetas);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.setOpaque(false);
        panelBoton.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0)); 
        panelBoton.add(btnVerMenu);

        panelFondo.add(panelTitulo, BorderLayout.NORTH);
        panelFondo.add(scroll, BorderLayout.CENTER);
        panelFondo.add(panelBoton, BorderLayout.SOUTH);

        this.contenedorPrincipal.add(panelFondo, BorderLayout.CENTER);
    }

    private void cargarDatosPrueba() {
        
        TarjetaTurno tMañana = new TarjetaTurno("Mañana", "7 AM", "9 AM");
        tMañana.setPreferredSize(new Dimension(280, 140));
        
        TarjetaTurno tTarde = new TarjetaTurno("Tarde", "12 PM", "2 PM");
        tTarde.setPreferredSize(new Dimension(280, 140));

        listaTurnos.add(tMañana);
        listaTurnos.add(tTarde);

        for (TarjetaTurno t : listaTurnos) {
            panelContenedorTarjetas.add(t);
        }
    }

    public BotonNeon getBtnVerMenu() {
        return btnVerMenu;
    }
}
