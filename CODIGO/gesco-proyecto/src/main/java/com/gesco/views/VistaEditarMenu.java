package com.gesco.views;

import java.awt.*;
import javax.swing.*;

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

        Titulo = new JLabel("Editar menú");
        Titulo.setFont(new Font("Arial", Font.BOLD, 45)); 
        Titulo.setForeground(new Color(240, 240, 240)); 
        Titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
    
    private void agregarCampos(JPanel panelFondoBase) {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);

        formPanel.setMaximumSize(new Dimension(450, 480)); 
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT); 

        JLabel lblFecha = crearEtiquetaForm("Fecha (DD/MM/AAAA)");
        
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

        formPanel.add(crearEtiquetaForm("Platillo 1"));
        formPanel.add(Box.createVerticalStrut(8));
        platillo1 = new JTextField();
        DiseñarCaja(platillo1, tamCaja);
        formPanel.add(platillo1);
        formPanel.add(Box.createVerticalStrut(20)); 
        
        formPanel.add(crearEtiquetaForm("Platillo 2"));
        formPanel.add(Box.createVerticalStrut(8));
        platillo2 = new JTextField();
        DiseñarCaja(platillo2, tamCaja);
        formPanel.add(platillo2);
        formPanel.add(Box.createVerticalStrut(20));

        formPanel.add(crearEtiquetaForm("Platillo 3"));
        formPanel.add(Box.createVerticalStrut(8));
        platillo3 = new JTextField();
        DiseñarCaja(platillo3, tamCaja);
        formPanel.add(platillo3);

        panelFondoBase.add(formPanel);
    }

    private JLabel crearEtiquetaForm(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setForeground(Color.WHITE);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JLabel crearSeparador() {
        JLabel sep = new JLabel("/");
        sep.setForeground(Color.WHITE);
        sep.setFont(new Font("Arial", Font.BOLD, 18));
        return sep;
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
        panelFondo.add(btnEditar); 
        panelFondo.add(Box.createVerticalGlue());

        this.contenedorPrincipal.add(panelFondo, BorderLayout.CENTER);
    }
}