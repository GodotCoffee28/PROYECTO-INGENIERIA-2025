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
    private JLabel lblDiaSemana;
    
    public VistaCrearMenu() {
        super(); 
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

        Titulo = crearEtiquetaSimple("Crear menú", 45, new Color(240, 240, 240));
        Titulo.setFont(new Font("Arial", Font.BOLD, 45));

        lblDiaSemana = new JLabel(" ");
        lblDiaSemana.setFont(new Font("Arial", Font.BOLD, 22));
        lblDiaSemana.setForeground(new java.awt.Color(180, 220, 255));
        lblDiaSemana.setAlignmentX(Component.LEFT_ALIGNMENT);
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
        formPanel.add(Box.createVerticalStrut(6));
        formPanel.add(lblDiaSemana);
        formPanel.add(Box.createVerticalStrut(14));

        javax.swing.event.DocumentListener actualizarDia = new javax.swing.event.DocumentListener() {
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { actualizarNombreDia(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { actualizarNombreDia(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { actualizarNombreDia(); }
        };
        diaField.getDocument().addDocumentListener(actualizarDia);
        mesField.getDocument().addDocumentListener(actualizarDia);
        anioField.getDocument().addDocumentListener(actualizarDia);

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

    private void actualizarNombreDia() {
        try {
            String d = diaField.getText().trim();
            String m = mesField.getText().trim();
            String a = anioField.getText().trim();
            if (d.isEmpty() || m.isEmpty() || a.length() < 4) {
                lblDiaSemana.setText(" ");
                return;
            }
            String fechaStr = String.format("%s-%02d-%02d",
                a, Integer.parseInt(m), Integer.parseInt(d));
            java.time.LocalDate fecha = java.time.LocalDate.parse(fechaStr);
            String nombre = switch (fecha.getDayOfWeek()) {
                case MONDAY    -> "Lunes";
                case TUESDAY   -> "Martes";
                case WEDNESDAY -> "Miércoles";
                case THURSDAY  -> "Jueves";
                case FRIDAY    -> "Viernes";
                case SATURDAY  -> "Sábado";
                case SUNDAY    -> "Domingo";
            };
            lblDiaSemana.setText(nombre);
        } catch (Exception ex) {
            lblDiaSemana.setText(" ");
        }
    }

    public void setFecha(String dia, String mes, String anio) {
        diaField.setText(dia);
        mesField.setText(mes);
        anioField.setText(anio);
        diaField.setEditable(false);
        mesField.setEditable(false);
        anioField.setEditable(false);
        diaField.setBackground(new java.awt.Color(220, 220, 220));
        mesField.setBackground(new java.awt.Color(220, 220, 220));
        anioField.setBackground(new java.awt.Color(220, 220, 220));
        actualizarNombreDia();
    }
}