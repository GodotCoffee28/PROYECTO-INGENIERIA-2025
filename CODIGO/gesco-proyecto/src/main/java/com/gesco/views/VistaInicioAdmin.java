package com.gesco.views;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.gesco.views.PlantillasViews.BotonNeon;
import com.gesco.views.PlantillasViews.PlantillaGesco;
public class VistaInicioAdmin extends PlantillaGesco {

    private BotonNeon btnGestión, btnSubirDatos, btnCambio, btnVerDatos;
    private JLabel titulo;

    public VistaInicioAdmin() {
        super();
        inicializarComponentes();
        construirCuerpo();
        revalidate();
        repaint();
        setVisible(true);
    }

    private void inicializarComponentes() {
        Dimension tamBoton = new Dimension(220, 60);

        titulo = new JLabel("Panel de control");
        titulo.setFont(new Font("Arial", Font.BOLD, 36));
        titulo.setForeground(new Color(240, 240, 240)); 
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        btnGestión = new BotonNeon("Gestión del menú");
        btnGestión.setPreferredSize(tamBoton);

        btnSubirDatos = new BotonNeon("Subir datos de CF y CV");
        btnSubirDatos.setPreferredSize(tamBoton);
        btnSubirDatos.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));

        btnVerDatos = new BotonNeon("Ver datos de CF y CV");
        btnVerDatos.setPreferredSize(tamBoton);
        btnVerDatos.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));

        btnCambio = new BotonNeon("Navegar como comensal");
        btnCambio.setPreferredSize(new Dimension(220, 50));
    }

    private void construirCuerpo() {
        JPanel panelCuerpo = new JPanel(new BorderLayout(0, 20));
        panelCuerpo.setOpaque(false);
        panelCuerpo.setBorder(BorderFactory.createEmptyBorder(20, 50, 40, 50));

        panelCuerpo.add(titulo, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new GridBagLayout()); 
        panelCentral.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 15, 20, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        JPanel cajaMenu = crearTarjeta("Respecto al menú", 420, 280);
        JPanel pnlInternoMenu = new JPanel(new GridBagLayout());
        pnlInternoMenu.setOpaque(false);
        
        GridBagConstraints gMenu = new GridBagConstraints();
        gMenu.gridx = 0;
        gMenu.gridy = 0;
        gMenu.fill = GridBagConstraints.HORIZONTAL; 
        gMenu.weightx = 1.0;
        gMenu.insets = new Insets(0, 50, 0, 50);
        
        pnlInternoMenu.add(btnGestión, gMenu);
        cajaMenu.add(pnlInternoMenu, BorderLayout.CENTER);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelCentral.add(cajaMenu, gbc);

        // --- COLUMNA DERECHA: CCB ---
        JPanel cajaCCB = crearTarjeta("Respecto al CCB", 420, 280);
        JPanel pnlInternoCCB = new JPanel(new GridBagLayout());
        pnlInternoCCB.setOpaque(false);
        
        GridBagConstraints gCCB = new GridBagConstraints();
        gCCB.gridx = 0;
        gCCB.fill = GridBagConstraints.HORIZONTAL; 
        gCCB.weightx = 1.0;
        gCCB.insets = new Insets(10, 50, 10, 50);
        gCCB.gridy = 0; pnlInternoCCB.add(btnSubirDatos, gCCB);
        gCCB.gridy = 1; pnlInternoCCB.add(btnVerDatos, gCCB);
        
        cajaCCB.add(pnlInternoCCB, BorderLayout.CENTER);
        
        gbc.gridx = 1;
        panelCentral.add(cajaCCB, gbc);

        JPanel cajaNavegacion = crearTarjeta("", 880, 120); 
        cajaNavegacion.setLayout(new BorderLayout());
        cajaNavegacion.setBorder(BorderFactory.createEmptyBorder(35, 80, 35, 80));
        cajaNavegacion.add(btnCambio, BorderLayout.CENTER);

        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 2; 
        gbc.weighty = 0.2;
        panelCentral.add(cajaNavegacion, gbc);

        panelCuerpo.add(panelCentral, BorderLayout.CENTER);
        contenedorPrincipal.add(panelCuerpo, BorderLayout.CENTER);
    }

    private JPanel crearTarjeta(String nombre, int ancho, int alto) {
        JPanel tarjeta = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 30)); 
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                g2.setColor(new Color(255, 255, 255, 50));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 40, 40);
                g2.dispose();
            }
        };
        
        tarjeta.setOpaque(false); 
        tarjeta.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

            JLabel lblTitulo = new JLabel(nombre);
            lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
            lblTitulo.setForeground(Color.WHITE);
            lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
            lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            tarjeta.add(lblTitulo, BorderLayout.NORTH);

        Dimension dim = new Dimension(ancho, alto);
        tarjeta.setPreferredSize(dim);
        tarjeta.setMinimumSize(dim);
        
        return tarjeta;
    }

    public BotonNeon getBtnGestion() {
        return btnGestión;
    }

    public BotonNeon getBtnSubirDatos() {
        return btnSubirDatos;
    }

    public BotonNeon getBtnVerDatos() {
        return btnVerDatos;
    }

    public BotonNeon getBtnCambio() {
        return btnCambio;
    }
}