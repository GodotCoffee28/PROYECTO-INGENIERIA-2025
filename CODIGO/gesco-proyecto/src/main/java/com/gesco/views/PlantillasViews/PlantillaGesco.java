package com.gesco.views.PlantillasViews;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PlantillaGesco extends JFrame {

    protected JPanel contenedorPrincipal; 
    private JLabel backIcon;  
    private JLabel menuIcon;
    private final MenuDesplegable menuDesplegableGeneral;
    public PlantillaGesco() {
        //Configuración de la ventana
        setTitle("Comedor estudiantil UCV");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750); 
        setLocationRelativeTo(null);
        setResizable(false);
        menuDesplegableGeneral = new MenuDesplegable();
        //Fondo con degradado
        contenedorPrincipal = new JPanel(new BorderLayout());
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

        
        JLabel tituloEncabezado = new JLabel("Comedor estudiantil UCV", SwingConstants.CENTER);
        tituloEncabezado.setFont(new Font("Arial", Font.BOLD, 30));
        tituloEncabezado.setForeground(Color.WHITE);

        JLabel relleno = new JLabel("☰");
        relleno.setFont(new Font("Dialog", Font.PLAIN, 45));
        relleno.setForeground(new Color(0,0,0,0)); 
        relleno.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));

        backIcon = new JLabel("🢀");  
        backIcon.setFont(new Font("Dialog", Font.PLAIN, 45));
        backIcon.setForeground(new Color(180, 180, 180));
        backIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backIcon.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));

        JLabel rellenoBack = new JLabel("🢀");
        rellenoBack.setFont(new Font("Dialog", Font.PLAIN, 45));
        rellenoBack.setForeground(new Color(0,0,0,0)); 
        rellenoBack.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));
        
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

        topPanel.add(menuIcon, BorderLayout.WEST);
        topPanel.add(tituloEncabezado, BorderLayout.CENTER);
        topPanel.add(relleno, BorderLayout.EAST);
        topPanel.add(backIcon, BorderLayout.EAST);

        return topPanel;
    }

     protected JLabel crearEtiquetaSimple(String texto, int size, Color color) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(new Font("Arial", Font.PLAIN, size));
        etiqueta.setForeground(color);
        etiqueta.setAlignmentX(Component.CENTER_ALIGNMENT);
        return etiqueta;
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
            menuIcon.setVisible(false);
            menuIcon.setEnabled(false);
        }
    }

    protected void ocultarBack() {
        if (backIcon != null) {
            backIcon.setVisible(false);
            backIcon.setEnabled(false);
        }
    }
}