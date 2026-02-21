package com.gesco.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.gesco.views.PlantillasViews.BotonNeon;
import com.gesco.views.PlantillasViews.PlantillaGesco;

public class VistaTurnos extends PlantillaGesco {

    private List<TarjetaTurno> listaTurnos;
    private JLabel titulo;
    private JPanel panelContenedorTarjetas;
    private BotonNeon btnVerMenu;

    public VistaTurnos() {
        super();       
        inicializarComponentes();
        construirCuerpo();
        cargarDatosPrueba(); 
        revalidate();
        repaint();
        setVisible(true);
    }

    private void inicializarComponentes() {
        titulo = crearEtiquetaSimple("Turnos de comida", 24, Color.WHITE);
        titulo.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        
        listaTurnos = new ArrayList<>();
        
        btnVerMenu = new BotonNeon("Ver menú del día");

        btnVerMenu.setFont(new Font("Arial", Font.BOLD, 20)); 
        btnVerMenu.setPreferredSize(new Dimension(250, 60));
    }

    private void construirCuerpo() {
        JPanel panelFondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int margenX = 100;
                int ancho = getWidth() - (margenX * 2);
                int alto = getHeight() - 40;
                
                g2.setColor(new Color(255, 255, 255, 25)); 
                g2.fillRoundRect(margenX, 20, ancho, alto, 50, 50);
                
                g2.setColor(new Color(255, 255, 255, 40));
                g2.drawRoundRect(margenX, 20, ancho, alto, 50, 50);
                g2.dispose();
            }
        };
        
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