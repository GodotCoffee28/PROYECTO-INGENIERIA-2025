package com.gesco.views;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.gesco.views.PlantillasViews.BotonNeon;
import com.gesco.views.PlantillasViews.PlantillaGesco;

/*
Una interfaz hecha pero esta demasiado mal para montarla asi q dejalo asi
*/


public class VistaFila extends PlantillaGesco {

    // Componentes
    private BotonNeon btnVerMenuSemana, btnEntrarFila, btnSalirFila;
    private JLabel lblBienvenida;
    private JLabel lblEstadoFila;
    private JLabel lblContadorFila;
    
    // Variable para guardar el nombre
    private final String nombreUsuario;

    // 1. MODIFICAMOS EL CONSTRUCTOR PARA RECIBIR EL NOMBRE
    public VistaFila(String nombreUsuario) {
        super(); // Llama al constructor de PlantillaGesco (crea la ventana y encabezado)
        this.nombreUsuario = nombreUsuario;
        
        inicializarComponentes();
        construirCuerpo();
        
        // Refrescamos la ventana para asegurar que se pinten los cambios
        revalidate();
        repaint();
        setVisible(true);
    }

    private void inicializarComponentes() {
        // --- TÍTULO DE BIENVENIDA ---
        lblBienvenida = new JLabel("¡Hola, " + nombreUsuario + "!");
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 28));
        lblBienvenida.setForeground(Color.WHITE);
        lblBienvenida.setBorder(new EmptyBorder(20, 40, 10, 0)); // Márgenes

        // --- BOTONES ---
        // Aquí podrías usar tus 'BotonNeon' si ya los tienes listos
        btnEntrarFila = crearBotonEstilizado("Entrar a la Fila", new Color(0, 120, 215));
        btnSalirFila = crearBotonEstilizado("Salir de la Fila", new Color(220, 53, 69));
        btnVerMenuSemana = crearBotonEstilizado("Ver Menú Completo", new Color(40, 167, 69));
        
        // --- INFO DE LA FILA ---
        lblEstadoFila = new JLabel("Estado actual del comedor:");
        lblEstadoFila.setFont(new Font("Arial", Font.PLAIN, 16));
        lblEstadoFila.setForeground(new Color(200, 200, 200));
        
        lblContadorFila = new JLabel("14 Personas en espera"); // Esto vendría de la BD luego
        lblContadorFila.setFont(new Font("Arial", Font.BOLD, 40));
        lblContadorFila.setForeground(new Color(100, 255, 218)); // Color Cian Neon
    }

    private void construirCuerpo() {
        // Panel base transparente que ocupará el CENTRO de la Plantilla
        JPanel panelCuerpo = new JPanel(new BorderLayout());
        panelCuerpo.setOpaque(false); // Para ver el fondo de la plantilla

        // A. AGREGAMOS EL TÍTULO ARRIBA
        panelCuerpo.add(lblBienvenida, BorderLayout.NORTH);

        // B. CREAMOS EL PANEL DIVIDIDO (IZQUIERDA / DERECHA)
        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 20, 0)); // 1 Fila, 2 Cols, Gap 20px
        panelCentral.setOpaque(false);
        panelCentral.setBorder(new EmptyBorder(20, 40, 40, 40)); // Márgenes externos

        // --- COLUMNA IZQUIERDA: MENÚ DE LA SEMANA ---
        JPanel panelIzquierdo = crearPanelTarjeta("Menú de Hoy");
        
        // Contenido dummy del menú
        JTextArea txtMenu = new JTextArea("• Plato Principal: Pabellón Criollo\n\n• Opción Vegana: Berenjenas Guisadas\n\n• Bebida: Jugo de Papelón\n\n• Postre: Fruta de temporada");
        txtMenu.setFont(new Font("Arial", Font.PLAIN, 18));
        txtMenu.setForeground(Color.WHITE);
        txtMenu.setOpaque(false);
        txtMenu.setEditable(false);
        txtMenu.setLineWrap(true);
        txtMenu.setWrapStyleWord(true);
        
        panelIzquierdo.add(txtMenu, BorderLayout.CENTER);
        panelIzquierdo.add(btnVerMenuSemana, BorderLayout.SOUTH);

        // --- COLUMNA DERECHA: GESTIÓN DE FILA ---
        JPanel panelDerecho = crearPanelTarjeta("Gestión de Fila");
        
        // Panel interno para organizar los elementos de la derecha
        JPanel panelContenidoDer = new JPanel(new GridLayout(4, 1, 10, 10));
        panelContenidoDer.setOpaque(false);
        
        // Centrar los textos
        lblEstadoFila.setHorizontalAlignment(SwingConstants.CENTER);
        lblContadorFila.setHorizontalAlignment(SwingConstants.CENTER);

        panelContenidoDer.add(lblEstadoFila);
        panelContenidoDer.add(lblContadorFila);
        panelContenidoDer.add(btnEntrarFila);
        panelContenidoDer.add(btnSalirFila);

        // Usamos un GridBag para centrar el contenido verticalmente en la tarjeta derecha
        JPanel wrapperDerecho = new JPanel(new GridBagLayout());
        wrapperDerecho.setOpaque(false);
        wrapperDerecho.add(panelContenidoDer);
        
        panelDerecho.add(wrapperDerecho, BorderLayout.CENTER);

        // AGREGAMOS LOS PANELES AL GRID
        panelCentral.add(panelIzquierdo);
        panelCentral.add(panelDerecho);

        // AGREGAMOS todo  AL CUERPO
        panelCuerpo.add(panelCentral, BorderLayout.CENTER);

        // FINALMENTE: AGREGAMOS AL CONTENEDOR DE LA PLANTILLA PADRE
        // 'contenedorPrincipal' viene heredado de PlantillaGesco
        contenedorPrincipal.add(panelCuerpo, BorderLayout.CENTER);
    }

    // --- MÉTODOS AUXILIARES DE DISEÑO ---

    // Crea un panel con fondo semitransparente y bordes redondeados (simulado)
    private JPanel crearPanelTarjeta(String titulo) {
        JPanel tarjeta = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(50, 50, 60)); // Color de fondo de las tarjetas
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // Bordes redondeados
            }
        };
        tarjeta.setOpaque(false);
        tarjeta.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Título de la tarjeta
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(255, 193, 7)); // Amarillo estilo UCV o dorado
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        tarjeta.add(lblTitulo, BorderLayout.NORTH);
        
        return tarjeta;
    }

    private BotonNeon crearBotonEstilizado(String texto, Color colorFondo) {
        BotonNeon btn = new BotonNeon(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(colorFondo);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Tamaño preferido para que no se vean aplastados
        btn.setPreferredSize(new Dimension(200, 40)); 
        return btn;
    }
}