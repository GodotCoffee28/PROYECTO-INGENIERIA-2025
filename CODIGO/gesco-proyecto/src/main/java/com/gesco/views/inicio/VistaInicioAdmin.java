package com.gesco.views.inicio;
import java.awt.*;
import javax.swing.*;

import com.gesco.views.PlantillasViews.BotonNeon;
import com.gesco.views.PlantillasViews.PlantillaGesco;
public class VistaInicioAdmin extends PlantillaGesco {

    private BotonNeon btnGestión, btnSubirDatos, btnCambio, btnVerCCB, btnVerMenu, btnCambiarTipoUsuario;
    private JLabel titulo;

    public VistaInicioAdmin() {
        super();
        ocultarIcono();
        setEsSuperAdmin(false);
        setImagenFondo("/FondoPrincipal2.png");
        inicializarComponentes();
        construirCuerpo();
        revalidate();
        repaint();
        setVisible(true);
    }

    private void inicializarComponentes() {
        Dimension tamBoton = new Dimension(220, 60);

        titulo = crearEtiquetaPersonalizada("Panel de control", "Times New Roman", Font.BOLD, 36, new Color(240, 240, 240),"centro");
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        btnGestión = new BotonNeon("Gestión del menú");
        btnGestión.setPreferredSize(tamBoton);

        btnVerMenu = new BotonNeon("Historial del menú");
        btnVerMenu.setPreferredSize(tamBoton);

        btnSubirDatos = new BotonNeon("Subir datos de CCB");
        btnSubirDatos.setPreferredSize(tamBoton);

        btnVerCCB = new BotonNeon("Historial datos CCB");
        btnVerCCB.setPreferredSize(tamBoton);

        btnCambio = new BotonNeon("Navegar como comensal");
        btnCambio.setPreferredSize(new Dimension(220, 50));

        btnCambiarTipoUsuario = new BotonNeon("Cambiar tipo por cédula");
        btnCambiarTipoUsuario.setPreferredSize(new Dimension(220, 50));
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
        
        gMenu.gridy = 0; 
        pnlInternoMenu.add(btnGestión, gMenu);

        gMenu.gridy = 1; 
        pnlInternoMenu.add(btnVerMenu, gMenu);
        
        cajaMenu.add(pnlInternoMenu, BorderLayout.CENTER);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1; 
        panelCentral.add(cajaMenu, gbc);

        JPanel cajaCCB = crearTarjeta("Respecto al CCB", 420, 280,40,15,"/Billetera.png");
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

        JPanel cajaNavegacion = crearTarjeta("", 880, 120,40,15,"/Billetera.png"); 
        cajaNavegacion.setLayout(new BorderLayout());
        cajaNavegacion.setBorder(BorderFactory.createEmptyBorder(20, 80, 20, 80));

        JPanel panelNavegacion = new JPanel(new GridBagLayout());
        panelNavegacion.setOpaque(false);
        GridBagConstraints gNav = new GridBagConstraints();
        gNav.gridx = 0;
        gNav.insets = new Insets(6, 20, 6, 20);
        gNav.fill = GridBagConstraints.HORIZONTAL;
        gNav.weightx = 1.0;
        gNav.gridy = 0;
        panelNavegacion.add(btnCambio, gNav);
        gNav.gridy = 1;
        panelNavegacion.add(btnCambiarTipoUsuario, gNav);
        cajaNavegacion.add(panelNavegacion, BorderLayout.CENTER);

        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 2; 
        gbc.weighty = 0.2;
        panelCentral.add(cajaNavegacion, gbc);

        panelCuerpo.add(panelCentral, BorderLayout.CENTER);
        contenedorPrincipal.add(panelCuerpo, BorderLayout.CENTER);
    }

    public BotonNeon getBtnGestion() {
        return btnGestión;
    }

    public BotonNeon getBtnSubirDatos() {
        return btnSubirDatos;
    }

    public BotonNeon getBtnVerMenu() {
        return btnVerMenu;
    }

    public BotonNeon getBtnVerCCB() {
        return btnVerCCB;
    }

    public BotonNeon getBtnCambio() {
        return btnCambio;
    }

    public BotonNeon getBtnCambiarTipoUsuario() {
        return btnCambiarTipoUsuario;
    }

    public void setEsSuperAdmin(boolean esSuperAdmin) {
        JLabel iconoSuperAdmin = getIlblSprAdmin();
        if (iconoSuperAdmin != null) {
            iconoSuperAdmin.setVisible(esSuperAdmin);
            iconoSuperAdmin.setCursor(
                esSuperAdmin ? new Cursor(Cursor.HAND_CURSOR) : new Cursor(Cursor.DEFAULT_CURSOR)
            );
        }
    }
}
