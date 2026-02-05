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
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.gesco.views.PlantillasViews.BotonNeon;
import com.gesco.views.PlantillasViews.PlantillaGesco;

public class VistaRegistro extends PlantillaGesco{
    private JTextField TxtNombreApellido, TxtCedula, TxtCorreo;
    private JPasswordField TxtContra;
    private BotonNeon btnRegistrarse, btnIrLogin;
    private JLabel Saludo;

    public VistaRegistro() {
        super(); 
        inicializarComponentes();
        construirCuerpo();
        revalidate();
        repaint();
        setVisible(true);
    }

 private void inicializarComponentes() {
        Dimension tamBotonPrincipal = new Dimension(400, 55);
        Dimension tamBotonLogin = new Dimension(220, 45);

        btnRegistrarse = new BotonNeon("Registrarse");
        btnRegistrarse.setPreferredSize(tamBotonPrincipal);
        btnRegistrarse.setMaximumSize(tamBotonPrincipal);
        btnRegistrarse.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnIrLogin = new BotonNeon("Inicie sesión");
        btnIrLogin.setFont(new Font("Arial", Font.BOLD, 18)); 
        btnIrLogin.setPreferredSize(tamBotonLogin);
        btnIrLogin.setMaximumSize(tamBotonLogin);
        btnIrLogin.setAlignmentX(Component.CENTER_ALIGNMENT);

        Saludo = new JLabel("¡Únete, Ucevista!");
        Saludo.setFont(new Font("Arial", Font.BOLD, 40)); // Reducido un poco para ahorrar espacio
        Saludo.setForeground(new Color(240, 240, 240)); 
        Saludo.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void agregarCampos(JPanel panelFondoBase) {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        // Ajustamos la altura máxima para que no empuje lo de abajo
        formPanel.setMaximumSize(new Dimension(450, 320)); 
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT); 

        Dimension tamanoCaja = new Dimension(450, 35); // Altura de caja ligeramente menor
        Font labelFont = new Font("Arial", Font.BOLD, 14);

        formPanel.add(crearLabelForm("Nombre y apellido", labelFont));
        TxtNombreApellido = crearTextField(tamanoCaja);
        formPanel.add(TxtNombreApellido);
        formPanel.add(Box.createVerticalStrut(8)); // Espacio menor

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
        JPanel panelFondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Ajustamos el rectángulo para que cubra mejor el contenido centrado
                int x = 200;
                int w = getWidth() - 400;
                g2.setColor(new Color(255, 255, 255, 15)); 
                g2.fillRoundRect(x, 10, w, getHeight() - 25, 50, 50);
                
                g2.setColor(new Color(255, 255, 255, 30));
                g2.drawRoundRect(x, 10, w, getHeight() - 25, 50, 50);
                g2.dispose();
            }
        };
        
        panelFondo.setLayout(new BoxLayout(panelFondo, BoxLayout.Y_AXIS));
        panelFondo.setOpaque(false); 
        panelFondo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panelFondo.add(Box.createVerticalGlue()); 
        
        panelFondo.add(Saludo);
        panelFondo.add(Box.createVerticalStrut(5));
        
        JLabel subTitulo = crearEtiquetaSimple("Crea tu cuenta para el comedor", 18, new Color(180, 180, 185));
        panelFondo.add(subTitulo);
        
        panelFondo.add(Box.createVerticalStrut(20)); 

        agregarCampos(panelFondo); 
        
        panelFondo.add(Box.createVerticalStrut(20));
        
        // Sección Login integrada
        JLabel loginInfo = crearEtiquetaSimple("¿Ya tiene cuenta?", 16, new Color(160, 160, 165));
        panelFondo.add(loginInfo);
        panelFondo.add(Box.createVerticalStrut(5));
        panelFondo.add(btnIrLogin);

        panelFondo.add(Box.createVerticalStrut(25)); 
        
        // Botón Final
        panelFondo.add(btnRegistrarse);  
        
        panelFondo.add(Box.createVerticalGlue());

        this.contenedorPrincipal.add(panelFondo, BorderLayout.CENTER);
    }


    // Getters para los controladores
    public String getNombreApellido() { return TxtNombreApellido.getText(); }
    public String getCedula() { return TxtCedula.getText(); }
    public String getCorreo() { return TxtCorreo.getText(); }
    public String getContra() { return new String(TxtContra.getPassword()); }
}
