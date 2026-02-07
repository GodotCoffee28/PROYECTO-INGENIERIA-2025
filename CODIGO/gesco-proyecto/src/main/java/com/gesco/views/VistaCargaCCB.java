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

public class VistaCargaCCB extends PlantillaGesco {
    private BotonNeon btnSubirDatos;
    private JTextField usuario, NB, MERMA, CF, CV;
    private JLabel Titulo;
    private JLabel lblResultado;
    
    public VistaCargaCCB() {
        super(); 
        inicializarComponentes();
        construirCuerpo();
        revalidate();
        repaint();
        setVisible(true);
    }

    private void inicializarComponentes() {
        Dimension tamBoton = new Dimension(400, 60);
        btnSubirDatos = new BotonNeon("Subir datos");
        btnSubirDatos.setPreferredSize(tamBoton);
        btnSubirDatos.setMaximumSize(tamBoton);
        btnSubirDatos.setAlignmentX(Component.CENTER_ALIGNMENT);

        Titulo = new JLabel("Datos para el CCB");
        Titulo.setFont(new Font("Arial", Font.BOLD, 45)); 
        Titulo.setForeground(new Color(240, 240, 240)); 
        Titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblResultado = new JLabel(" ");
        lblResultado.setFont(new Font("Arial", Font.BOLD, 26));
        lblResultado.setForeground(new Color(0, 255, 150)); 
        lblResultado.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
    
    private void agregarCampos(JPanel panelFondoBase) {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setMaximumSize(new Dimension(450, 420)); 
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT); 

        Dimension tamCaja = new Dimension(450, 35);

        formPanel.add(crearEtiquetaForm("Tipo de usuario (Estudiante/Profesor/Empleado)"));
        formPanel.add(Box.createVerticalStrut(5));
        usuario = new JTextField();
        DiseñarCaja(usuario, tamCaja);
        formPanel.add(usuario);
        formPanel.add(Box.createVerticalStrut(12));

        formPanel.add(crearEtiquetaForm("NB (Número de bandejas servidas)"));
        formPanel.add(Box.createVerticalStrut(5));
        NB = new JTextField();
        DiseñarCaja(NB, tamCaja);
        formPanel.add(NB);
        formPanel.add(Box.createVerticalStrut(12)); 

        formPanel.add(crearEtiquetaForm("MERMA (% de desperdicio)"));
        formPanel.add(Box.createVerticalStrut(5));
        MERMA = new JTextField();
        DiseñarCaja(MERMA, tamCaja);
        formPanel.add(MERMA);
        formPanel.add(Box.createVerticalStrut(12)); 

        formPanel.add(crearEtiquetaForm("CF (Costos Fijos totales)"));
        formPanel.add(Box.createVerticalStrut(5));
        CF = new JTextField();
        DiseñarCaja(CF, tamCaja);
        formPanel.add(CF);
        formPanel.add(Box.createVerticalStrut(12)); 

        formPanel.add(crearEtiquetaForm("CV (Costos Variables totales)"));
        formPanel.add(Box.createVerticalStrut(5));
        CV = new JTextField();
        DiseñarCaja(CV, tamCaja);
        formPanel.add(CV);

        panelFondoBase.add(formPanel);
    }

    private JLabel crearEtiquetaForm(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(new Color(180, 180, 180));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void DiseñarCaja(JTextField c, Dimension d) {
        c.setPreferredSize(d);
        c.setMaximumSize(d);
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        c.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        c.setFont(new Font("Arial", Font.PLAIN, 15));
        c.setBackground(new Color(255, 255, 255, 240));
    }

    private void construirCuerpo() {
        JPanel panelFondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Dibujamos el panel translúcido
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

        // Estructura de apilado vertical
        panelFondo.add(Box.createVerticalGlue()); 
        panelFondo.add(Titulo);
        panelFondo.add(Box.createVerticalStrut(15)); 

        agregarCampos(panelFondo); 
        
        // Ubicamos el resultado DENTRO del flujo, antes del botón
        panelFondo.add(Box.createVerticalStrut(20)); 
        panelFondo.add(lblResultado);
        panelFondo.add(Box.createVerticalStrut(15)); 
        
        panelFondo.add(btnSubirDatos); 
        panelFondo.add(Box.createVerticalGlue());

        this.contenedorPrincipal.add(panelFondo, BorderLayout.CENTER);
    }

    public String getUsuario(){ return usuario.getText();}
    public String getNB(){ return NB.getText();}
    public String getMERMA(){ return MERMA.getText();}
    public String getCF(){ return CF.getText();}
    public String getCV(){ return CV.getText();}
    public BotonNeon getBtnSubirDatos() { return btnSubirDatos; }

    public void setResultado(String resultado) {
        if (resultado == null || resultado.trim().isEmpty()) {
            lblResultado.setText(" ");
        } else {
            lblResultado.setText("VALOR CCB: " + resultado);
        }
    }
}