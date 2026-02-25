package com.gesco.views;

import java.awt.*;

import javax.swing.*;

import com.gesco.views.PlantillasViews.BotonNeon;
import com.gesco.views.PlantillasViews.CampoFecha;
import com.gesco.views.PlantillasViews.PlantillaGesco;

public class VistaRecargarSaldo extends PlantillaGesco {
    
    private JLabel Titulo;
    private CampoFecha campoFecha;
    private JTextField CeduField, BancoField, MontoField, RefenciaField;
    private BotonNeon botonRecargar;

    public VistaRecargarSaldo() {
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
        botonRecargar = new BotonNeon("Recargar");
        botonRecargar.setPreferredSize(new Dimension(400, 60));
        botonRecargar.setMaximumSize(tamBoton);
        botonRecargar.setAlignmentX(Component.CENTER_ALIGNMENT);
        Titulo = crearEtiquetaPersonalizada("Recargar saldo","Times New Roman", Font.BOLD, 45, new Color(240, 240, 240), "centro");
        Titulo.setFont(new Font("Arial", Font.BOLD, 45));
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
        panelFondo.add(botonRecargar); 
        panelFondo.add(Box.createVerticalGlue());

        this.contenedorPrincipal.add(panelFondo, BorderLayout.CENTER);
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

            formPanel.add(crearEtiquetaPersonalizada("Cedula de identidad", "Times New Roman", Font.PLAIN, 18, Color.WHITE, "izquierda"));
            formPanel.add(Box.createVerticalStrut(8));
            CeduField = new JTextField();
            diseñarCaja(CeduField, tamCaja);
            formPanel.add(CeduField);
            formPanel.add(Box.createVerticalStrut(20)); 

            formPanel.add(crearEtiquetaPersonalizada("Banco", "Times New Roman", Font.PLAIN, 18, Color.WHITE, "izquierda"));
            formPanel.add(Box.createVerticalStrut(8));
            BancoField = new JTextField();
            diseñarCaja(BancoField, tamCaja);
            formPanel.add(BancoField);

            formPanel.add(crearEtiquetaPersonalizada("Referencia", "Times New Roman", Font.PLAIN, 18, Color.WHITE, "izquierda"));
            formPanel.add(Box.createVerticalStrut(8));
            RefenciaField = new JTextField();
            diseñarCaja(RefenciaField, tamCaja);
            formPanel.add(RefenciaField);

            formPanel.add(crearEtiquetaPersonalizada("Monto Bs.", "Times New Roman", Font.PLAIN, 18, Color.WHITE, "izquierda"));
            formPanel.add(Box.createVerticalStrut(8));
            MontoField = new JTextField();
            diseñarCaja(MontoField, tamCaja);
            formPanel.add(MontoField);
            formPanel.add(Box.createVerticalStrut(20));

            panelFondoBase.add(formPanel);
    }

    //Getters para los campos de texto y el botón, para que el controlador pueda acceder a ellos
    public String getFecha() { return campoFecha.getFechaTexto(); }  
    public String getCedula() { return CeduField.getText();}
    public String getBanco() { return BancoField.getText();}
    public String getReferencia() { return RefenciaField.getText();}
    public String getMonto() { return MontoField.getText();}
    public BotonNeon getBotonRecargar() {return botonRecargar;}

    public void setCedula(String cedula) {
        CeduField.setText(cedula == null ? "" : cedula);
        CeduField.setEditable(false);
    }

}

