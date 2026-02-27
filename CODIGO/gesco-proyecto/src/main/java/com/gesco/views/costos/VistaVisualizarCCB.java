package com.gesco.views.costos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;


import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.gesco.models.costos.CCB;
import com.gesco.views.PlantillasViews.BotonNeon;
import com.gesco.views.PlantillasViews.PlantillaGesco;

public class VistaVisualizarCCB extends PlantillaGesco {
    private BotonNeon btnSubirDatos;
    private JTextField usuario, NB, MERMA, CF, CV;
    private JLabel TituloCcb;
    private JLabel lblResultado;
    
    public VistaVisualizarCCB() {
        super();
        setImagenFondo("/FondoPrincipal2.png");
        inicializarComponentes();
        ocultarlblSprAdmin();
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

        TituloCcb = new JLabel("Datos para el CCB");
        TituloCcb.setFont(new Font("Arial", Font.BOLD, 45)); 
        TituloCcb.setForeground(new Color(240, 240, 240)); 
        TituloCcb.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblResultado = new JLabel(" ");
        lblResultado.setFont(new Font("Arial", Font.BOLD, 22));
        lblResultado.setForeground(new Color(240, 240, 240));
        lblResultado.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
    
    private void agregarCampos(JPanel panelFondoBase) {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);

        formPanel.setMaximumSize(new Dimension(450, 480)); 
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT); 

        Dimension tamCaja = new Dimension(450, 40);

        formPanel.add(crearEtiquetaPersonalizada(("Tipo de usuario"), "Times New Roman", Font.PLAIN, 18, Color.WHITE,"centro"));
        formPanel.add(Box.createVerticalStrut(8));
        usuario = new JTextField();
        diseñarCaja(usuario, tamCaja);
        formPanel.add(usuario);
        formPanel.add(Box.createVerticalStrut(20));

        formPanel.add(crearEtiquetaPersonalizada("NB", "Times New Roman", Font.PLAIN, 18, Color.WHITE, "centro"));
        formPanel.add(Box.createVerticalStrut(8));
        NB = new JTextField();
        diseñarCaja(NB, tamCaja);
        formPanel.add(NB);
        formPanel.add(Box.createVerticalStrut(20)); 

        formPanel.add(crearEtiquetaPersonalizada("MERMA", "Times New Roman", Font.PLAIN, 18, Color.WHITE, "centro"));
        formPanel.add(Box.createVerticalStrut(8));
        MERMA = new JTextField();
        diseñarCaja(MERMA, tamCaja);
        formPanel.add(MERMA);
        formPanel.add(Box.createVerticalStrut(20)); 

        formPanel.add(crearEtiquetaPersonalizada("CF", "Times New Roman", Font.PLAIN, 18, Color.WHITE, "centro"));
        formPanel.add(Box.createVerticalStrut(8));
        CF = new JTextField();
        diseñarCaja(CF, tamCaja);
        formPanel.add(CF);
        formPanel.add(Box.createVerticalStrut(20)); 

        formPanel.add(crearEtiquetaPersonalizada("CV", "Times New Roman", Font.PLAIN, 18, Color.WHITE, "centro"));
        formPanel.add(Box.createVerticalStrut(8));
        CV = new JTextField();
        diseñarCaja(CV, tamCaja);
        formPanel.add(CV);

        panelFondoBase.add(formPanel);
    }


    private void construirCuerpo() {
        JPanel panelFondo = crearPanel(250,10,500,20,50,50);
        
        panelFondo.setLayout(new BoxLayout(panelFondo, BoxLayout.Y_AXIS));
        panelFondo.setOpaque(false); 
        panelFondo.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));

        panelFondo.add(Box.createVerticalGlue()); 
        panelFondo.add(TituloCcb);
        panelFondo.add(Box.createVerticalStrut(25)); 

        agregarCampos(panelFondo); 
        
        panelFondo.add(Box.createVerticalStrut(30)); 
        panelFondo.add(btnSubirDatos); 
        panelFondo.add(Box.createVerticalStrut(20));
        panelFondo.add(lblResultado);
        panelFondo.add(Box.createVerticalGlue());

        this.contenedorPrincipal.add(panelFondo, BorderLayout.CENTER);
    }
    public String getUsuario(){ return usuario.getText();}
    public String getNB(){ return NB.getText();}
    public String getMERMA(){ return MERMA.getText();}
    public String getCF(){ return CF.getText();}
    public String getCV(){ return CV.getText();}
    public BotonNeon getBtnSubirDatos() { return btnSubirDatos; }

    public void setCCB(CCB ccb) {
        if (ccb == null) {
            return;
        }
        usuario.setText(ccb.getTipoUsuario());
        NB.setText(String.valueOf(ccb.getNb()));
        MERMA.setText(String.valueOf(ccb.getMerma()));
        CF.setText(String.valueOf(ccb.getCf()));
        CV.setText(String.valueOf(ccb.getCv()));
        setResultado("VALOR CCB: " + ccb.getCcb());
    }

    public void setResultado(String resultado) {
        lblResultado.setText(resultado);
    }
}

