package com.gesco.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.gesco.views.PlantillasViews.BotonNeon;
import com.gesco.views.PlantillasViews.PlantillaGesco;

public class VistaCrearMenu extends PlantillaGesco {
    private BotonNeon btnCrear;
    private JTextField platillo1, platillo2, platillo3;
    private JTextField diaField, mesField, anioField;
    private JLabel Titulo;
    
    public VistaCrearMenu() {
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
        btnCrear = new BotonNeon("Crear");
        btnCrear.setPreferredSize(tamBoton);
        btnCrear.setMaximumSize(tamBoton);
        btnCrear.setAlignmentX(Component.CENTER_ALIGNMENT);
        Titulo = crearEtiquetaPersonalizada("Crear menú", "Times New Roman", Font.BOLD, 45, new Color(240, 240, 240), "centro");
        Titulo.setFont(new Font("Arial", Font.BOLD, 45)); 
    }
    
    private void agregarCampos(JPanel panelFondoBase) {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);

        formPanel.setMaximumSize(new Dimension(450, 480)); 
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT); 

        JLabel lblFecha = crearEtiquetaPersonalizada("Fecha (DD/MM/AAAA)", "Times New Roman", Font.PLAIN, 18, Color.WHITE, "izquierda");
        
        diaField = new JTextField(2);
        mesField = new JTextField(2);
        anioField = new JTextField(4);
        
        JPanel fechaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        fechaPanel.setOpaque(false);
        fechaPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        fechaPanel.add(diaField); 
        fechaPanel.add(crearSeparador());
        fechaPanel.add(mesField); 
        fechaPanel.add(crearSeparador());
        fechaPanel.add(anioField);

        formPanel.add(lblFecha);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(fechaPanel);
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
        panelFondo.add(btnCrear); 
        panelFondo.add(Box.createVerticalGlue());

        this.contenedorPrincipal.add(panelFondo, BorderLayout.CENTER);
    }

    public String getPlatillo1(){ return platillo1.getText();}
    public String getPlatillo2(){ return platillo2.getText();}
    public String getPlatillo3(){ return platillo3.getText();}
    public BotonNeon getBtnCrear() { return btnCrear; }
    public String getDia(){ return diaField.getText();}
    public String getMes(){ return mesField.getText();}
    public String getAnio(){ return anioField.getText();}
}