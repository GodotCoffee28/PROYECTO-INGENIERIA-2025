package com.gesco.views.PlantillasViews;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class VentanaEmergente extends JDialog {
    private JPanel panelPrincipal;
    private Color colorBorde = new Color(255,255,255); 

    public VentanaEmergente(Frame padre, String titulo) {
        super(padre, true); 
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        
        inicializarComponentes(titulo);
        setSize(400, 300);
        setLocationRelativeTo(padre); 
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
    }

    private void inicializarComponentes(String titulo) {
        panelPrincipal = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.setColor(new Color(230, 230, 230));
                g2.fillRoundRect(0, 0, getWidth(), 60, 30, 30);
                g2.fillRect(0, 30, getWidth(), 30);
                g2.setColor(colorBorde);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 30, 30);
                
                g2.dispose();
            }
        };
        panelPrincipal.setLayout(new BorderLayout());
        panelPrincipal.setOpaque(false); 

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(50, 50, 50)); 
        lblTitulo.setPreferredSize(new Dimension(getWidth(), 60));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);
        add(panelPrincipal);
    }

    public JPanel getCuerpo() {
        JPanel cuerpo = new JPanel();
        cuerpo.setOpaque(false);
        cuerpo.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        panelPrincipal.add(cuerpo, BorderLayout.CENTER);
        return cuerpo;
    }
    
    public void cerrar() {
        dispose();
    }
}