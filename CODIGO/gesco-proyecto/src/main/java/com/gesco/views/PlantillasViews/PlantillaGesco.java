package com.gesco.views.PlantillasViews;


import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class PlantillaGesco extends JFrame {

    protected JPanel contenedorPrincipal; 
    private JLabel backIcon;  
    private JLabel menuIcon, lblIconoUsuario;
    private final MenuDesplegable menuDesplegableGeneral;
    private Image imagenFondo;
    
    public PlantillaGesco() {

        setTitle("Comedor estudiantil UCV");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750); 
        setLocationRelativeTo(null);
        setResizable(false);
        menuDesplegableGeneral = new MenuDesplegable();

        contenedorPrincipal = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagenFondo != null) {
                    g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        
        contenedorPrincipal.setBackground(new Color(33, 33, 39));

        contenedorPrincipal.add(Encabezado(), BorderLayout.NORTH);
        add(contenedorPrincipal);
    }

    private JPanel Encabezado() {
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(true);
        topPanel.setBackground(new Color(60, 60, 65));
        topPanel.setPreferredSize(new Dimension(1100, 100));
        
        
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(255, 255, 255)));
        
        menuIcon = new JLabel("☰");
        menuIcon.setFont(new Font("Dialog", Font.PLAIN, 45));
        menuIcon.setForeground(new Color(180, 180, 180));
        menuIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        menuIcon.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));

        menuIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                menuIcon.setForeground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                menuIcon.setForeground(new Color(180, 180, 180));
            }
        });
        JPanel panelIzquierdo = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        panelIzquierdo.setOpaque(false);

        panelIzquierdo.setPreferredSize(new Dimension(250, 100)); 
        panelIzquierdo.add(menuIcon);
        
        JPanel panelDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 30));
        panelDerecho.setOpaque(false);
        panelDerecho.setPreferredSize(new Dimension(250, 100));

        ImageIcon iconoBlanco = obtenerIcono("/iconoBlanco.png", 35);
        ImageIcon iconoGris = obtenerIcono("/iconoGris.png", 35);

        lblIconoUsuario = new JLabel(iconoGris);
        lblIconoUsuario.setCursor(new Cursor(Cursor.HAND_CURSOR));

        lblIconoUsuario.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                lblIconoUsuario.setIcon(iconoBlanco);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                lblIconoUsuario.setIcon(iconoGris);
            }
        });

        JLabel tituloEncabezado = new JLabel("Comedor estudiantil UCV", SwingConstants.CENTER);
        tituloEncabezado.setFont(new Font("Courier New", Font.BOLD, 30));
        tituloEncabezado.setForeground(Color.WHITE);

        JLabel relleno = new JLabel("☰");
        relleno.setFont(new Font("Dialog", Font.PLAIN, 45));
        relleno.setForeground(new Color(0,0,0,0)); 

        backIcon = new JLabel("🢀");  
        backIcon.setFont(new Font("Dialog", Font.PLAIN, 40));
        backIcon.setForeground(new Color(180, 180, 180));
        backIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelDerecho.add(lblIconoUsuario);
        panelDerecho.add(backIcon);
        
        backIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backIcon.setForeground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                backIcon.setForeground(new Color(180, 180, 180));
            }
        });

        topPanel.add(panelIzquierdo, BorderLayout.WEST);
        topPanel.add(tituloEncabezado, BorderLayout.CENTER);
        topPanel.add(panelDerecho, BorderLayout.EAST); 

        return topPanel;
    }


    protected JLabel crearEtiquetaPersonalizada(String texto, String fuente, int estilo, int size, Color color, String alineacion) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(new Font(fuente, estilo, size)); 
        etiqueta.setForeground(color);
        switch (alineacion) {
            case "izquierda" -> etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);
            case "derecha" -> etiqueta.setAlignmentX(Component.RIGHT_ALIGNMENT);
            default -> etiqueta.setAlignmentX(Component.CENTER_ALIGNMENT);
        }
        return etiqueta;
    }


    protected JPanel crearPanel(int x, int y, int ancho, int alto, int arcoAncho, int arcoAlto){
        JPanel panelFondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(new Color(30, 30, 35, 200));
                g2.fillRoundRect(x, y, getWidth() - ancho, getHeight() - alto, arcoAncho, arcoAlto);
                
                g2.setColor(new Color(100, 100, 105, 150));
                g2.drawRoundRect(x, y, getWidth() - ancho, getHeight() - alto, arcoAncho, arcoAlto);
                g2.dispose();
            }
        };
        return panelFondo;
    }


    protected JLabel crearSeparador() {
        JLabel sep = new JLabel("/");
        sep.setForeground(Color.WHITE);
        sep.setFont(new Font("Arial", Font.BOLD, 18));
        return sep;
    }


    protected void diseñarCaja(JTextField c, Dimension d) {
        c.setPreferredSize(d);
        c.setMaximumSize(d);
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        c.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        c.setFont(new Font("Arial", Font.PLAIN, 16));
        c.setBackground(new Color(255, 255, 255, 240));
    }

        
    protected JPanel crearTarjeta(String titulo, int ancho, int alto, int radio, int margen, String rutaIcono) {

        JPanel tarjeta = new JPanel(new BorderLayout(0, 10)) { 
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(30, 30, 35, 215)); 
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), radio, radio);
                g2.setColor(new Color(250, 250, 255)); 
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radio, radio);
                g2.dispose();
            }
        };

        tarjeta.setPreferredSize(new Dimension(ancho, alto));
        tarjeta.setOpaque(false);
        tarjeta.setBorder(BorderFactory.createEmptyBorder(margen, margen, margen, margen));

        if (titulo != null && !titulo.isEmpty()) {
            JPanel cabeceraCompleta = new JPanel(new BorderLayout(0, 5));
            cabeceraCompleta.setOpaque(false);

            JPanel panelIconoTexto = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
            panelIconoTexto.setOpaque(false);

            if (rutaIcono != null) {
                JLabel lblIcono = new JLabel(obtenerIcono(rutaIcono, 35)); 
                panelIconoTexto.add(lblIcono);
            }

            JLabel lblTitulo = new JLabel(titulo);
            lblTitulo.setFont(new Font("Times New Roman", Font.BOLD, 30)); 
            lblTitulo.setForeground(new Color(230, 230, 230));
            panelIconoTexto.add(lblTitulo);
            JSeparator linea = new JSeparator();
            linea.setForeground(new Color(250, 250, 255, 180)); 
            linea.setBackground(new Color(0, 0, 0, 0));

            JPanel contenedorLinea = new JPanel(new BorderLayout());
            contenedorLinea.setOpaque(false);
            contenedorLinea.setBorder(BorderFactory.createEmptyBorder(5, 40, 10, 40)); 
            contenedorLinea.add(linea, BorderLayout.CENTER);

            cabeceraCompleta.add(panelIconoTexto, BorderLayout.CENTER);
            cabeceraCompleta.add(contenedorLinea, BorderLayout.SOUTH);
            
            tarjeta.add(cabeceraCompleta, BorderLayout.NORTH);
        }

        return tarjeta;
    }


    protected ImageIcon obtenerIcono(String ruta, int size) {
    URL recurso = getClass().getResource(ruta);
    if (recurso != null) {
        ImageIcon original = new ImageIcon(recurso);
        Image escalada = original.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(escalada);
    }
    System.err.println("Error: No se encontró la imagen en " + ruta);
    return new ImageIcon();
}


    public void setImagenFondo(String ruta) {
        URL url = getClass().getResource(urlRuta(ruta) );
        if (url != null) {
            this.imagenFondo = new ImageIcon(url).getImage();
            contenedorPrincipal.repaint(); 
        } else {
            System.err.println("No se pudo cargar el fondo en: " + ruta);
        }
    }

    
    private String urlRuta(String r) {
        return r.startsWith("/") ? r : "/" + r;
    }


    public MenuDesplegable getPopupMenu() {
        return menuDesplegableGeneral;
    }


    public JLabel getMenuIcon(){
        return menuIcon;
    }


    public JLabel getBackIcon() {
        return backIcon;
    }


    protected void ocultarMenu() {
        if (menuIcon != null) {
        menuIcon.setForeground(new Color(0, 0, 0, 0)); 
        menuIcon.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        for (java.awt.event.MouseListener ml : menuIcon.getMouseListeners()) {
            menuIcon.removeMouseListener(ml);
            }
        }
    }


    protected void ocultarBack() {
        if (backIcon != null) {
            backIcon.setForeground(new Color(0, 0, 0, 0));
            backIcon.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            for (java.awt.event.MouseListener ml : backIcon.getMouseListeners()) {
                backIcon.removeMouseListener(ml);
            }
        }
    }

    protected void ocultarIcono() {
        if (lblIconoUsuario != null) {
            lblIconoUsuario.setVisible(false);
            lblIconoUsuario.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            for (java.awt.event.MouseListener ml : lblIconoUsuario.getMouseListeners()) {
                lblIconoUsuario.removeMouseListener(ml);
            }
        }
}

    public JLabel getIconoUsuario(){
        return  lblIconoUsuario;
    }
}