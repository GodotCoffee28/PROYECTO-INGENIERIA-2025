package com.gesco.views;

import java.awt.*;
import javax.swing.*;

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

        formPanel.add(crearEtiquetaForm("Tipo de usuario"));
        formPanel.add(Box.createVerticalStrut(8));
        usuario = new JTextField();
        DiseñarCaja(usuario, tamCaja);
        formPanel.add(usuario);
        formPanel.add(Box.createVerticalStrut(20));

        formPanel.add(crearEtiquetaForm("NB"));
        formPanel.add(Box.createVerticalStrut(8));
        NB = new JTextField();
        DiseñarCaja(NB, tamCaja);
        formPanel.add(NB);
        formPanel.add(Box.createVerticalStrut(20)); 

        formPanel.add(crearEtiquetaForm("MERMA"));
        formPanel.add(Box.createVerticalStrut(8));
        MERMA = new JTextField();
        DiseñarCaja(MERMA, tamCaja);
        formPanel.add(MERMA);
        formPanel.add(Box.createVerticalStrut(20)); 

        formPanel.add(crearEtiquetaForm("CF"));
        formPanel.add(Box.createVerticalStrut(8));
        CF = new JTextField();
        DiseñarCaja(CF, tamCaja);
        formPanel.add(CF);
        formPanel.add(Box.createVerticalStrut(20)); 

        formPanel.add(crearEtiquetaForm("CV"));
        formPanel.add(Box.createVerticalStrut(8));
        CV = new JTextField();
        DiseñarCaja(CV, tamCaja);
        formPanel.add(CV);

        panelFondoBase.add(formPanel);
    }

    private JLabel crearEtiquetaForm(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setForeground(Color.WHITE);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void DiseñarCaja(JTextField c, Dimension d) {
        c.setPreferredSize(d);
        c.setMaximumSize(d);
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        c.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        c.setFont(new Font("Arial", Font.PLAIN, 16));
    }

    private void construirCuerpo() {
        JPanel panelFondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(new Color(255, 255, 255, 25)); 
                g2.fillRoundRect(250, 10, getWidth() - 500, getHeight() - 20, 50, 50);
                
                g2.setColor(new Color(255, 255, 255, 40));
                g2.drawRoundRect(250, 10, getWidth() - 500, getHeight() - 20, 50, 50);
                g2.dispose();
            }
        };
        
        panelFondo.setLayout(new BoxLayout(panelFondo, BoxLayout.Y_AXIS));
        panelFondo.setOpaque(false); 
        panelFondo.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));

        panelFondo.add(Box.createVerticalGlue()); 
        panelFondo.add(Titulo);
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

    public void setResultado(String resultado) {
        lblResultado.setText(resultado);
    }
}