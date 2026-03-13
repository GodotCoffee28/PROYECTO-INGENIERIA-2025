package com.gesco.views.inicio;

import java.awt.*;
import javax.swing.*;
import com.gesco.views.PlantillasViews.BotonNeon;
import com.gesco.views.PlantillasViews.PlantillaGesco;

public class VistaInicioAdmin extends PlantillaGesco {

    private BotonNeon btnGestión, btnSubirDatos, btnCambio, btnVerCCB, btnVerMenu, btnCambiarTipoUsuario, btnVerHistorialAsistencia;
    private JLabel titulo;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public VistaInicioAdmin() {
        super();
        ocultarIcono();
        setImagenFondo("/FondoPrincipal2.png");
        inicializarComponentes();
        construirCuerpo();
        revalidate();
        repaint();
        setVisible(true);
    }

    private void inicializarComponentes() {
        Dimension tamBotonSuperior = new Dimension(220, 60);
        Dimension tamBotonNav = new Dimension(270, 70); 

        titulo = crearEtiquetaPersonalizada("Panel de control", "Times New Roman", Font.BOLD, 36, new Color(240, 240, 240), "centro");
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        btnGestión = new BotonNeon("Gestión del menú");
        btnGestión.setPreferredSize(tamBotonSuperior);

        btnVerMenu = new BotonNeon("Historial del menú");
        btnVerMenu.setPreferredSize(tamBotonSuperior);

        btnSubirDatos = new BotonNeon("Subir datos de CCB");
        btnSubirDatos.setPreferredSize(tamBotonSuperior);

        btnVerCCB = new BotonNeon("Historial datos CCB");
        btnVerCCB.setPreferredSize(tamBotonSuperior);

        btnCambio = new BotonNeon("Navegar como comensal");
        btnCambio.setPreferredSize(tamBotonNav);
        btnCambio.setFont(new Font("Arial", Font.BOLD, 20)); 

        btnCambiarTipoUsuario = new BotonNeon("Gestionar roles");
        btnCambiarTipoUsuario.setPreferredSize(tamBotonNav);
        btnCambiarTipoUsuario.setFont(new Font("Arial", Font.BOLD, 20));

        btnVerHistorialAsistencia = new BotonNeon("Historial fila");
        btnVerHistorialAsistencia.setPreferredSize(tamBotonNav);
        btnVerHistorialAsistencia.setFont(new Font("Arial", Font.BOLD, 20));
    }

    private void construirCuerpo() {
        JPanel panelCuerpo = new JPanel(new BorderLayout(0, 20));
        panelCuerpo.setOpaque(false);
        panelCuerpo.setBorder(BorderFactory.createEmptyBorder(20, 50, 40, 50));
        
        JPanel tarjetaTitulo = crearTarjeta("", 600, 70, 20, 0, "/Platos.png"); 
        tarjetaTitulo.setLayout(new BorderLayout());
        tarjetaTitulo.add(titulo, BorderLayout.CENTER);

        panelCuerpo.add(tarjetaTitulo, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new GridBagLayout()); 
        panelCentral.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 15, 20, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        JPanel cajaMenu = crearTarjeta("Respecto al menú", 420, 280, 40, 15, "/Platos.png");
        JPanel pnlInternoMenu = new JPanel(new GridBagLayout());
        pnlInternoMenu.setOpaque(false);
        GridBagConstraints gMenu = new GridBagConstraints();
        gMenu.gridx = 0;
        gMenu.fill = GridBagConstraints.HORIZONTAL; 
        gMenu.weightx = 1.0;
        gMenu.insets = new Insets(10, 50, 10, 50); 
        gMenu.gridy = 0; pnlInternoMenu.add(btnGestión, gMenu);
        gMenu.gridy = 1; pnlInternoMenu.add(btnVerMenu, gMenu);
        cajaMenu.add(pnlInternoMenu, BorderLayout.CENTER);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1; 
        panelCentral.add(cajaMenu, gbc);

        JPanel cajaCCB = crearTarjeta("Respecto al CCB", 420, 280, 40, 15, "/Billetera.png");
        JPanel pnlInternoCCB = new JPanel(new GridBagLayout());
        pnlInternoCCB.setOpaque(false);
        GridBagConstraints gCCB = new GridBagConstraints();
        gCCB.gridx = 0;
        gCCB.fill = GridBagConstraints.HORIZONTAL; 
        gCCB.weightx = 1.0;
        gCCB.insets = new Insets(10, 50, 10, 50);
        gCCB.gridy = 0; pnlInternoCCB.add(btnSubirDatos, gCCB);
        gCCB.gridy = 1; pnlInternoCCB.add(btnVerCCB, gCCB);
        cajaCCB.add(pnlInternoCCB, BorderLayout.CENTER);
        
        gbc.gridx = 1;
        panelCentral.add(cajaCCB, gbc);

        JPanel cajaNavegacion = crearTarjeta("", 880, 130, 40, 15, "/Billetera.png"); 
        cajaNavegacion.setLayout(new GridBagLayout()); 
        
        JPanel panelNavegacion = new JPanel(new GridBagLayout());
        panelNavegacion.setOpaque(false);
        
        GridBagConstraints gNav = new GridBagConstraints();
        gNav.gridy = 0;
        gNav.insets = new Insets(0, 10, 0, 10); 
        gNav.fill = GridBagConstraints.NONE;

        gNav.gridx = 0;
        panelNavegacion.add(btnCambio, gNav);

        gNav.gridx = 1;
        panelNavegacion.add(btnCambiarTipoUsuario, gNav);

        gNav.gridx = 2;
        panelNavegacion.add(btnVerHistorialAsistencia, gNav);

        cajaNavegacion.add(panelNavegacion);

        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 2; 
        gbc.weighty = 0.2;
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        panelCentral.add(cajaNavegacion, gbc);

        panelCuerpo.add(panelCentral, BorderLayout.CENTER);
        contenedorPrincipal.add(panelCuerpo, BorderLayout.CENTER);
    }

    public BotonNeon getBtnGestion() { return btnGestión; }
    public BotonNeon getBtnSubirDatos() { return btnSubirDatos; }
    public BotonNeon getBtnVerMenu() { return btnVerMenu; }
    public BotonNeon getBtnVerCCB() { return btnVerCCB; }
    public BotonNeon getBtnCambio() { return btnCambio; }
    public BotonNeon getBtnCambiarTipoUsuario() { return btnCambiarTipoUsuario; }
    public BotonNeon getBtnVerHistorialAsistencia() { return btnVerHistorialAsistencia; }
}