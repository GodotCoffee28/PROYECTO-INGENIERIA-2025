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

public class VistaEditarMenu extends PlantillaGesco {

    private BotonNeon btnEditar;
    private JTextField platillo1, platillo2, platillo3;
    private JTextField diaField, mesField, anioField;
    private JLabel Titulo;
    
    public VistaEditarMenu() {
        super(); 
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

        Titulo = crearEtiquetaSimple("Editar menú", 45, new Color(240, 240, 240));
        Titulo.setFont(new Font("Arial", Font.BOLD, 45));
    }

    private void agregarCampos(JPanel panelFondoBase) {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);

        formPanel.setMaximumSize(new Dimension(450, 480)); 
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT); 

        JLabel lblFecha = crearEtiquetaForm("Fecha (DD/MM/AAAA)", 18, Color.WHITE);
        
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

        formPanel.add(crearEtiquetaForm("Platillo 1", 18, Color.WHITE));
        formPanel.add(Box.createVerticalStrut(8));
        platillo1 = new JTextField();
        diseñarCaja(platillo1, tamCaja);
        formPanel.add(platillo1);
        formPanel.add(Box.createVerticalStrut(20)); 
        
        formPanel.add(crearEtiquetaForm("Platillo 2", 18, Color.WHITE));
        formPanel.add(Box.createVerticalStrut(8));
        platillo2 = new JTextField();
        diseñarCaja(platillo2, tamCaja);
        formPanel.add(platillo2);
        formPanel.add(Box.createVerticalStrut(20));

        formPanel.add(crearEtiquetaForm("Platillo 3", 18, Color.WHITE));
        formPanel.add(Box.createVerticalStrut(8));
        platillo3 = new JTextField();
        diseñarCaja(platillo3, tamCaja);
        formPanel.add(platillo3);

        panelFondoBase.add(formPanel);
    }

    private void construirCuerpo() {
        JPanel panelFondo = crearPanel();
        
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
    public String getDia(){ return diaField.getText(); }
    public String getMes(){ return mesField.getText(); }
    public String getAnio(){ return anioField.getText(); }
}