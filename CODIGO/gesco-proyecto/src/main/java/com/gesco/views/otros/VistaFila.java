package com.gesco.views.otros;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

import com.gesco.models.menu.Menu;
import com.gesco.models.menu.Platillo;
import com.gesco.views.PlantillasViews.BotonNeon;
import com.gesco.views.PlantillasViews.PlantillaGesco;
import com.gesco.views.menu.TarjetaMenu;


public class VistaFila extends PlantillaGesco {
    private BotonNeon btnEntrar, btnSalir, btnVerMenu;
    private JLabel infoFila, infoBienvenida, iconoPersona;
    private int enFila = 0, Disponible = 100;

    public VistaFila() {
        super();
        setImagenFondo("/FondoPrincipal.png");
        inicializarComponentes();
        construirCuerpo();
        revalidate();
        repaint();
        setVisible(true);
    }

    private void inicializarComponentes() {
        Dimension tam = new Dimension(350, 70);

        btnEntrar = new BotonNeon("Entrar a la fila");
        btnEntrar.setPreferredSize(tam);
        btnEntrar.setMaximumSize(tam); 
        btnEntrar.setFont(new Font("Arial", Font.BOLD, 32));

        btnSalir = new BotonNeon("Salir de la fila");
        btnSalir.setPreferredSize(tam);
        btnSalir.setMaximumSize(tam);
        btnSalir.setFont(new Font("Arial", Font.BOLD, 32));

        btnVerMenu = new BotonNeon("Ver menú de la semana");
        btnVerMenu.setPreferredSize(tam);
        btnVerMenu.setMaximumSize(tam);
        btnVerMenu.setFont(new Font("Arial", Font.BOLD, 24));

        infoBienvenida = crearEtiquetaPersonalizada("Estado del Comedor Universitario", "Times New Roman", Font.BOLD, 40, new Color(240, 240, 240), "centro");

        infoFila = new JLabel(enFila + " / " + Disponible + " Personas en la fila");
        infoFila.setFont(new Font("Times New Roman", Font.BOLD, 35));
        infoFila.setForeground(new Color(240, 240, 240));

        iconoPersona = new JLabel(obtenerIcono("/personas.png", 250));
    }


    private void construirCuerpo() {

    JPanel panelPrincipal = crearPanel(100, 10, 200, 20, 50, 50);
    panelPrincipal.setLayout(new BorderLayout(0, 10));
    panelPrincipal.setOpaque(false);
    panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

    JPanel panelNorteContenedor = new JPanel();
    panelNorteContenedor.setLayout(new BoxLayout(panelNorteContenedor, BoxLayout.Y_AXIS));
    panelNorteContenedor.setOpaque(false);

    infoBienvenida.setAlignmentX(Component.CENTER_ALIGNMENT);
    panelNorteContenedor.add(infoBienvenida);

    JLabel lblDescripcion = crearEtiquetaPersonalizada("Consulta el estado de la fila y los menús disponibles para hoy", "Arial", Font.PLAIN, 16, new Color(200, 200, 200), "centro" );
    lblDescripcion.setAlignmentX(Component.CENTER_ALIGNMENT);
    panelNorteContenedor.add(Box.createVerticalStrut(2)); 
    panelNorteContenedor.add(lblDescripcion);

    JSeparator separador = new JSeparator(SwingConstants.HORIZONTAL);
    separador.setMaximumSize(new Dimension(800, 2)); 
    separador.setForeground(new Color(240, 240, 240, 180)); 
    
    panelNorteContenedor.add(Box.createVerticalStrut(10));
    panelNorteContenedor.add(separador);

    panelPrincipal.add(panelNorteContenedor, BorderLayout.NORTH);

    JPanel panelCentralContenedor = new JPanel(new GridBagLayout());
    panelCentralContenedor.setOpaque(false);
    GridBagConstraints gbc = new GridBagConstraints();

    gbc.gridx = 0;
    gbc.weightx = 0.0; 
    gbc.weighty = 1.0; 
    gbc.anchor = GridBagConstraints.CENTER; 
    gbc.insets = new Insets(0, 0, 0, 40); 
    panelCentralContenedor.add(iconoPersona, gbc);

    JPanel panelControles = new JPanel();
    panelControles.setLayout(new BoxLayout(panelControles, BoxLayout.Y_AXIS));
    panelControles.setOpaque(false);

    infoFila.setAlignmentX(Component.CENTER_ALIGNMENT);
    panelControles.add(infoFila);
    panelControles.add(Box.createVerticalStrut(15));
    
    btnEntrar.setAlignmentX(Component.CENTER_ALIGNMENT);
    panelControles.add(btnEntrar);
    panelControles.add(Box.createVerticalStrut(10));
    
    btnSalir.setAlignmentX(Component.CENTER_ALIGNMENT);
    panelControles.add(btnSalir);

    gbc.gridx = 1;
    gbc.anchor = GridBagConstraints.LINE_START; 
    gbc.insets = new Insets(0, 40, 0, 0); 
    panelCentralContenedor.add(panelControles, gbc);

    panelPrincipal.add(panelCentralContenedor, BorderLayout.CENTER);

    JPanel panelInferior = new JPanel();
    panelInferior.setLayout(new BoxLayout(panelInferior, BoxLayout.Y_AXIS));
    panelInferior.setOpaque(false);

    JLabel tituloMenu = crearEtiquetaPersonalizada("Menú del día", "Times New Roman", Font.BOLD, 24, Color.WHITE, "centro");
    tituloMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
    panelInferior.add(tituloMenu);
    panelInferior.add(Box.createVerticalStrut(8)); 

    JPanel panelTarjetas = new JPanel(new GridLayout(1, 2, 30, 0)); 
    panelTarjetas.setOpaque(false);
    panelTarjetas.setMaximumSize(new Dimension(750, 180));
    panelTarjetas.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40)); 
    
    Menu menuD = new Menu(LocalDate.now());
    menuD.agregarPlatillo(new Platillo("Arepa"));
    panelTarjetas.add(new TarjetaMenu(menuD));
    
    Menu menuA = new Menu(LocalDate.now());
    menuA.agregarPlatillo(new Platillo("Pasta"));
    menuA.agregarPlatillo(new Platillo("Patilla"));
    menuA.agregarPlatillo(new Platillo("Prueba"));
    panelTarjetas.add(new TarjetaMenu(menuA));
    
    panelInferior.add(panelTarjetas);
    
    panelInferior.add(Box.createVerticalStrut(15));
    btnVerMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
    panelInferior.add(btnVerMenu);
    panelInferior.add(Box.createVerticalStrut(10));

    panelPrincipal.add(panelInferior, BorderLayout.SOUTH);
    this.contenedorPrincipal.add(panelPrincipal, BorderLayout.CENTER);
}
}

