package com.gesco.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.gesco.views.PlantillasViews.BotonNeon;
import com.gesco.views.PlantillasViews.CampoFecha;
import com.gesco.views.PlantillasViews.PlantillaGesco;

public class VistaEditarMenu extends PlantillaGesco {

    private BotonNeon btnEditar;
    private JTextField platillo1, platillo2, platillo3;
    private CampoFecha campoFecha;
    private JLabel Titulo;
    
    public VistaEditarMenu() {
        super();
        setImagenFondo("/FondoPrincipal2.png");
        inicializarComponentes();
        construirCuerpo();
        revalidate();
        repaint(); 
        setVisible(true);
    }

    private void inicializarComponentes() {
        Dimension tamBoton = new Dimension(400, 60);
        btnEditar = new BotonNeon("Editar");
        btnEditar.setPreferredSize(tamBoton);
        btnEditar.setMaximumSize(tamBoton);
        btnEditar.setAlignmentX(Component.CENTER_ALIGNMENT);

        Titulo = crearEtiquetaPersonalizada("Editar menú", "Times New Roman", Font.BOLD, 45, new Color(240, 240, 240), "centro");

    }

    private void agregarCampos(JPanel panelFondoBase) {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);

        formPanel.setMaximumSize(new Dimension(450, 480)); 
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT); 

        campoFecha = new CampoFecha("Fecha (DD/MM/AAAA)", "Times New Roman", Font.PLAIN, 18, Color.WHITE, "izquierda");
        campoFecha.setLabelColor(Color.WHITE);
        campoFecha.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(campoFecha);
        formPanel.add(Box.createVerticalStrut(20));

        Dimension tamCaja = new Dimension(450, 40);

        formPanel.add(crearEtiquetaPersonalizada("Platillo 1", "Times New Roman", Font.PLAIN, 18, Color.WHITE, "izquierda"));
        formPanel.add(Box.createVerticalStrut(8));
        platillo1 = new JTextField();
        diseñarCaja(platillo1, tamCaja);
        formPanel.add(platillo1);
        formPanel.add(Box.createVerticalStrut(20)); 
        
        formPanel.add(crearEtiquetaPersonalizada("Platillo 2", "Times New Roman", Font.PLAIN, 18, Color.WHITE, "izquierda"));
        formPanel.add(Box.createVerticalStrut(8));
        platillo2 = new JTextField();
        diseñarCaja(platillo2, tamCaja);
        formPanel.add(platillo2);
        formPanel.add(Box.createVerticalStrut(20));

        formPanel.add(crearEtiquetaPersonalizada("Platillo 3", "Times New Roman", Font.PLAIN, 18, Color.WHITE, "izquierda"));
        formPanel.add(Box.createVerticalStrut(8));
        platillo3 = new JTextField();
        diseñarCaja(platillo3, tamCaja);
        formPanel.add(platillo3);
        formPanel.add(Box.createVerticalStrut(14));

        chkNoDisponible = new JCheckBox("Menu no disponible para este día");
        chkNoDisponible.setOpaque(false);
        chkNoDisponible.setForeground(Color.WHITE);
        chkNoDisponible.setFont(new Font("Arial", Font.BOLD, 14));
        chkNoDisponible.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(chkNoDisponible);

        panelFondoBase.add(formPanel);
    }

    private void construirCuerpo() {
        JPanel panelFondo = crearPanel(250, 10, 500, 20, 50, 50);
        
        panelFondo.setLayout(new BoxLayout(panelFondo, BoxLayout.Y_AXIS));
        panelFondo.setOpaque(false); 
        panelFondo.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));

        panelFondo.add(Box.createVerticalGlue()); 
        panelFondo.add(Titulo);
        panelFondo.add(Box.createVerticalStrut(25)); 

        agregarCampos(panelFondo); 
        
        panelFondo.add(Box.createVerticalStrut(30)); 
        panelFondo.add(btnEditar); 
        panelFondo.add(Box.createVerticalGlue());

        this.contenedorPrincipal.add(panelFondo, BorderLayout.CENTER);
    }
    public BotonNeon getBtnEditar() { return btnEditar; }
    public String getPlatillo1(){ return platillo1.getText(); }
    public String getPlatillo2(){ return platillo2.getText(); }
    public String getPlatillo3(){ return platillo3.getText(); }
    public String getDia(){ return campoFecha.getDia(); }
    public String getMes(){ return campoFecha.getMes(); }
    public String getAnio(){ return campoFecha.getAnio(); }
    public String getFechaTexto(){ return campoFecha.getFechaTexto(); }
}