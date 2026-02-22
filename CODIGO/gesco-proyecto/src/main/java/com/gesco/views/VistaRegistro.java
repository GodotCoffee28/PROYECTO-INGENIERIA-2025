package com.gesco.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.gesco.views.PlantillasViews.BotonNeon;
import com.gesco.views.PlantillasViews.PlantillaGesco;

public class VistaRegistro extends PlantillaGesco {
    private JTextField TxtNombreApellido, TxtCedula, TxtCorreo;
    private JPasswordField TxtContra;
    private BotonNeon btnRegistrarse;
    private JLabel Saludo, loginLink;

    public VistaRegistro() {
        super(); 
        setImagenFondo("/VISTAFONDO.png");
        ocultarMenu();
        inicializarComponentes();
        construirCuerpo();
        revalidate();
        repaint();
        setVisible(true);
    }

    private void inicializarComponentes() {
        Dimension tamBotonPrincipal = new Dimension(400, 55);

        btnRegistrarse = new BotonNeon("Registrarse");
        btnRegistrarse.setPreferredSize(tamBotonPrincipal);
        btnRegistrarse.setMaximumSize(tamBotonPrincipal);
        btnRegistrarse.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginLink = crearEtiquetaPersonalizada("¿Ya tiene cuenta? Inicie sesión", "Times New Roman", Font.PLAIN, 16, new Color(220, 220, 220), "centro");
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Saludo = crearEtiquetaPersonalizada("¡Únete, Ucevista!", "Times New Roman", Font.BOLD, 40, new Color(240, 240, 240),"centro");
    }

    private void agregarCampos(JPanel panelFondoBase) {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setMaximumSize(new Dimension(450, 320)); 
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT); 

        Dimension tamanoCaja = new Dimension(450, 35); 
        Font labelFont = new Font("Arial", Font.BOLD, 14);

        formPanel.add(crearLabelForm("Nombre y apellido", labelFont));
        TxtNombreApellido = crearTextField(tamanoCaja);
        formPanel.add(TxtNombreApellido);
        formPanel.add(Box.createVerticalStrut(8)); 

        formPanel.add(crearLabelForm("Cédula de identidad", labelFont));
        TxtCedula = crearTextField(tamanoCaja);
        formPanel.add(TxtCedula);
        formPanel.add(Box.createVerticalStrut(8));

        formPanel.add(crearLabelForm("Correo electrónico", labelFont));
        TxtCorreo = crearTextField(tamanoCaja);
        formPanel.add(TxtCorreo);
        formPanel.add(Box.createVerticalStrut(8));

        formPanel.add(crearLabelForm("Contraseña", labelFont));
        TxtContra = new JPasswordField();
        estilizarComponente(TxtContra, tamanoCaja);
        formPanel.add(TxtContra);

        panelFondoBase.add(formPanel);
    }

    private JLabel crearLabelForm(String texto, Font fuente) {
        JLabel label = new JLabel(texto);
        label.setFont(fuente);
        label.setForeground(new Color(220, 220, 225));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JTextField crearTextField(Dimension dim) {
        JTextField field = new JTextField();
        estilizarComponente(field, dim);
        return field;
    }

    private void estilizarComponente(JComponent c, Dimension dim) {
        c.setPreferredSize(dim);
        c.setMaximumSize(dim);
        c.setMinimumSize(dim);
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        c.setBorder(BorderFactory.createEmptyBorder(2, 12, 2, 12));
        c.setBackground(Color.WHITE);
    }

    private void construirCuerpo() {
        JPanel panelFondo = crearPanel(200,10,400,25,50,50);
        
        panelFondo.setLayout(new BoxLayout(panelFondo, BoxLayout.Y_AXIS));
        panelFondo.setOpaque(false); 
        panelFondo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panelFondo.add(Box.createVerticalGlue()); 
        
        panelFondo.add(Saludo);
        panelFondo.add(Box.createVerticalStrut(5));
        
        JLabel subTitulo = crearEtiquetaPersonalizada("Crea tu cuenta para el comedor", "Times New Roman", Font.PLAIN, 18, new Color(180, 180, 185),"centro");
        panelFondo.add(subTitulo);
        
        panelFondo.add(Box.createVerticalStrut(20)); 

        agregarCampos(panelFondo); 
        
        panelFondo.add(Box.createVerticalStrut(30));
        
        panelFondo.add(loginLink);

        panelFondo.add(Box.createVerticalStrut(30)); 
        
        panelFondo.add(btnRegistrarse);  
        
        panelFondo.add(Box.createVerticalGlue());

        this.contenedorPrincipal.add(panelFondo, BorderLayout.CENTER);
    }

    public String getNombreApellido() { return TxtNombreApellido.getText(); }
    public String getCedula() { return TxtCedula.getText(); }
    public String getCorreo() { return TxtCorreo.getText(); }
    public String getContra() { return new String(TxtContra.getPassword()); }
    public JLabel getLoginLink() { return loginLink; }
    public BotonNeon getBtnRegistrarse() { return btnRegistrarse; }
}