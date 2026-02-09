package com.gesco.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.gesco.views.PlantillasViews.BotonNeon;
import com.gesco.views.PlantillasViews.PlantillaGesco;

public class VistaVerCFCV extends PlantillaGesco {
    private JLabel lblCf;
    private JLabel lblCv;
    private BotonNeon btnRefrescar;

    public VistaVerCFCV() {
        super();
        inicializar();
        construirCuerpo();
        revalidate();
        repaint();
        setVisible(true);
    }

    private void inicializar() {
        lblCf = crearEtiquetaSimple("CF: -", 18, Color.WHITE);
        lblCf.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblCv = crearEtiquetaSimple("CV: -", 18, Color.WHITE);
        lblCv.setAlignmentX(Component.CENTER_ALIGNMENT);

        Dimension tam = new Dimension(400, 60);
        btnRefrescar = new BotonNeon("Refrescar");
        btnRefrescar.setPreferredSize(tam);
        btnRefrescar.setMaximumSize(tam);
        btnRefrescar.setAlignmentX(Component.CENTER_ALIGNMENT);

    }

    private void construirCuerpo() {
        JPanel panelFondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 25));
                g2.fillRoundRect(250, 10, getWidth() - 500, getHeight() - 20, 50, 50);
                g2.setColor(new Color(255, 255, 255, 40));
                g2.drawRoundRect(250, 10, getWidth() - 500, getHeight() - 20, 50, 50);
                g2.dispose();
            }
        };
        panelFondo.setLayout(new BoxLayout(panelFondo, BoxLayout.Y_AXIS));
        panelFondo.setOpaque(false);
        panelFondo.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));

        panelFondo.add(Box.createVerticalGlue());
        panelFondo.add(crearEtiquetaSimple("Datos actuales de CF y CV", 22, Color.WHITE));
        panelFondo.add(Box.createVerticalStrut(20));
        panelFondo.add(lblCf);
        panelFondo.add(Box.createVerticalStrut(10));
        panelFondo.add(lblCv);
        panelFondo.add(Box.createVerticalStrut(20));
        panelFondo.add(btnRefrescar);
        panelFondo.add(Box.createVerticalGlue());

        this.contenedorPrincipal.add(panelFondo, BorderLayout.CENTER);
    }

    public void setCf(double cf) { lblCf.setText("CF: " + String.format("%.2f", cf)); }
    public void setCv(double cv) { lblCv.setText("CV: " + String.format("%.2f", cv)); }
    public JButton getBtnRefrescar() { return btnRefrescar; }
}
