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
import javax.swing.JTextField;

import com.gesco.views.PlantillasViews.BotonNeon;
import com.gesco.views.PlantillasViews.PlantillaGesco;

public class VistaCargarCFCV extends PlantillaGesco {
    private BotonNeon btnGuardar;
    private JTextField CF, CV;
    private JLabel titulo;
    private JLabel lblResultado;

    public VistaCargarCFCV() {
        super();
        inicializarComponentes();
        construirCuerpo();
        revalidate();
        repaint();
        setVisible(true);
    }

    private void inicializarComponentes() {
        Dimension tamBoton = new Dimension(300, 50);
        btnGuardar = new BotonNeon("Guardar");
        btnGuardar.setPreferredSize(tamBoton);
        btnGuardar.setMaximumSize(tamBoton);
        btnGuardar.setAlignmentX(Component.CENTER_ALIGNMENT);

        titulo = new JLabel("Cargar Costos: Fijo y Variable");
        titulo.setFont(new Font("Arial", Font.BOLD, 36));
        titulo.setForeground(new Color(240, 240, 240));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblResultado = new JLabel(" ");
        lblResultado.setFont(new Font("Arial", Font.BOLD, 18));
        lblResultado.setForeground(new Color(240, 240, 240));
        lblResultado.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void agregarCampos(JPanel panelFondoBase) {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setMaximumSize(new Dimension(450, 200));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        Dimension tamCaja = new Dimension(450, 40);

        formPanel.add(crearEtiquetaForm("CF (Costos Fijos)"));
        formPanel.add(Box.createVerticalStrut(8));
        CF = new JTextField();
        diseñarCaja(CF, tamCaja);
        formPanel.add(CF);
        formPanel.add(Box.createVerticalStrut(16));

        formPanel.add(crearEtiquetaForm("CV (Costos Variables)"));
        formPanel.add(Box.createVerticalStrut(8));
        CV = new JTextField();
        diseñarCaja(CV, tamCaja);
        formPanel.add(CV);

        panelFondoBase.add(formPanel);
    }

    private JLabel crearEtiquetaForm(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(Color.WHITE);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void diseñarCaja(JTextField c, Dimension d) {
        c.setPreferredSize(d);
        c.setMaximumSize(d);
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        c.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        c.setFont(new Font("Arial", Font.PLAIN, 16));
        c.setBackground(new Color(255, 255, 255, 240));
    }

    private void construirCuerpo() {
        JPanel panelFondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int x = 250;
                int y = 15;
                int w = getWidth() - 500;
                int h = getHeight() - 30;
                g2.setColor(new Color(255, 255, 255, 25));
                g2.fillRoundRect(x, y, w, h, 50, 50);
                g2.setColor(new Color(255, 255, 255, 40));
                g2.drawRoundRect(x, y, w, h, 50, 50);
                g2.dispose();
            }
        };

        panelFondo.setLayout(new BoxLayout(panelFondo, BoxLayout.Y_AXIS));
        panelFondo.setOpaque(false);
        panelFondo.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        panelFondo.add(Box.createVerticalGlue());
        panelFondo.add(titulo);
        panelFondo.add(Box.createVerticalStrut(20));

        agregarCampos(panelFondo);

        panelFondo.add(Box.createVerticalStrut(16));
        panelFondo.add(lblResultado);
        panelFondo.add(Box.createVerticalStrut(16));
        panelFondo.add(btnGuardar);
        panelFondo.add(Box.createVerticalGlue());

        this.contenedorPrincipal.add(panelFondo, BorderLayout.CENTER);
    }

    public String getCF() { return CF.getText(); }
    public String getCV() { return CV.getText(); }
    public BotonNeon getBtnGuardar() { return btnGuardar; }
    public void setResultado(String texto) { this.lblResultado.setText(texto); }
}
