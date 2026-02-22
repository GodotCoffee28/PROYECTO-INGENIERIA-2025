package com.gesco.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

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
        setImagenFondo("/FondoPrincipal2.png");
        inicializar();
        construirCuerpo();
        revalidate();
        repaint();
        setVisible(true);
    }

    private void inicializar() {
        lblCf = crearEtiquetaPersonalizada("CF: -", "Times New Roman", Font.PLAIN, 18, Color.WHITE, "centro");
        lblCf.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblCv = crearEtiquetaPersonalizada("CV: -", "Times New Roman", Font.PLAIN, 18, Color.WHITE, "centro");
        lblCv.setAlignmentX(Component.CENTER_ALIGNMENT);

        Dimension tam = new Dimension(400, 60);
        btnRefrescar = new BotonNeon("Refrescar");
        btnRefrescar.setPreferredSize(tam);
        btnRefrescar.setMaximumSize(tam);
        btnRefrescar.setAlignmentX(Component.CENTER_ALIGNMENT);

    }

    private void construirCuerpo() {
        JPanel panelFondo = crearPanel(250,10,500,20,50,50);

        panelFondo.setLayout(new BoxLayout(panelFondo, BoxLayout.Y_AXIS));
        panelFondo.setOpaque(false);
        panelFondo.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));

        panelFondo.add(Box.createVerticalGlue());
        panelFondo.add(crearEtiquetaPersonalizada("Datos actuales de CF y CV", "Times New Roman", Font.BOLD, 22, Color.WHITE, "centro"));
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
