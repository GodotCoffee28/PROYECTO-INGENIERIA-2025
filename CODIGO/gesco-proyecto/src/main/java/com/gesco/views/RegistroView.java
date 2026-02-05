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
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.gesco.views.PlantillasViews.BotonNeon;
import com.gesco.views.PlantillasViews.PlantillaGesco;

public class RegistroView extends PlantillaGesco {

    private JTextField TxtNombreApellido, TxtCedula, TxtCorreo;
    private JPasswordField TxtContra;
    private BotonNeon btnRegistrarse;
    private JLabel loginLink;

    public RegistroView() {
        super(); 
        setTitle("Registro Comedor UCV");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);  // Mejor que EXIT_ON_CLOSE
        inicializarComponentes();
        construirCuerpo();
        revalidate();
        repaint();
        setVisible(true);
    }

    private void inicializarComponentes() {
        Dimension tamBoton = new Dimension(400, 55);

        btnRegistrarse = new BotonNeon("Registrarse");
        btnRegistrarse.setPreferredSize(tamBoton);
        btnRegistrarse.setMaximumSize(tamBoton);
        btnRegistrarse.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Link de login (estilo clickable)
        loginLink = new JLabel("¿Ya tienes una cuenta? Inicia sesión");
        loginLink.setFont(new Font("Arial", Font.PLAIN, 16));
        loginLink.setForeground(new Color(220, 220, 220));
        loginLink.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loginLink.setAlignmentX(Component.CENTER_ALIGNMENT);

        TxtNombreApellido = new JTextField();
        TxtCedula = new JTextField();
        TxtCorreo = new JTextField();
        TxtContra = new JPasswordField();
    }

    private void construirCuerpo() {
        JPanel panelFondo = new JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new java.awt.Color(255, 255, 255, 15));
                g2.fillRoundRect(200, 10, getWidth() - 400, getHeight() - 25, 50, 50);
                g2.setColor(new java.awt.Color(255, 255, 255, 30));
                g2.drawRoundRect(200, 10, getWidth() - 400, getHeight() - 25, 50, 50);
                g2.dispose();
            }
        };
        panelFondo.setLayout(new BoxLayout(panelFondo, BoxLayout.Y_AXIS));
        panelFondo.setOpaque(false);
        panelFondo.setBorder(BorderFactory.createEmptyBorder(60, 20, 60, 20));

        panelFondo.add(Box.createVerticalGlue());

        // Saludo grande (estilo de tu amigo)
        JLabel saludo = new JLabel("¡Únete, Ucevista!");
        saludo.setFont(new Font("Arial", Font.BOLD, 40));
        saludo.setForeground(new Color(240, 240, 240));
        saludo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelFondo.add(saludo);

        panelFondo.add(Box.createVerticalStrut(10));

        JLabel subTitulo = new JLabel("Crea tu cuenta para el comedor", JLabel.CENTER);
        subTitulo.setFont(new Font("Arial", Font.PLAIN, 18));
        subTitulo.setForeground(new Color(180, 180, 185));
        subTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelFondo.add(subTitulo);

        panelFondo.add(Box.createVerticalStrut(40));

        // Formulario de campos (tu diseño centrado)
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setMaximumSize(new Dimension(450, 300));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        Dimension tamCampo = new Dimension(450, 35);
        Font fontLabel = new Font("Arial", Font.BOLD, 14);

        agregarCampoForm(formPanel, "Nombre y Apellido", TxtNombreApellido, tamCampo, fontLabel);
        agregarCampoForm(formPanel, "Cédula de identidad", TxtCedula, tamCampo, fontLabel);
        agregarCampoForm(formPanel, "Correo electrónico", TxtCorreo, tamCampo, fontLabel);
        agregarCampoForm(formPanel, "Contraseña", TxtContra, tamCampo, fontLabel);

        panelFondo.add(formPanel);

        panelFondo.add(Box.createVerticalStrut(30));

        panelFondo.add(loginLink);

        panelFondo.add(Box.createVerticalStrut(30));

        panelFondo.add(btnRegistrarse);

        panelFondo.add(Box.createVerticalGlue());

        this.contenedorPrincipal.add(panelFondo, BorderLayout.CENTER);
    }

    private void agregarCampoForm(JPanel panel, String texto, JTextField campo, Dimension dim, Font font) {
        JLabel label = new JLabel(texto);
        label.setFont(font);
        label.setForeground(new Color(220, 220, 225));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        campo.setPreferredSize(dim);
        campo.setMaximumSize(dim);
        campo.setMinimumSize(dim);
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setBorder(BorderFactory.createEmptyBorder(2, 12, 2, 12));
        campo.setBackground(Color.WHITE);

        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(campo);
        panel.add(Box.createVerticalStrut(15));
    }

    // Getters para acceso desde el controlador
    public String getNombreApellido() { return TxtNombreApellido.getText(); }
    public String getCedula() { return TxtCedula.getText(); }
    public String getCorreo() { return TxtCorreo.getText(); }
    public String getContra() { return new String(TxtContra.getPassword()); }
    public JLabel getLoginLink() { return loginLink; }
    public BotonNeon getBtnRegistrarse() { return btnRegistrarse; }
}