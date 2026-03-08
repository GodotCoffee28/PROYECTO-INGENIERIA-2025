package com.gesco.views.costos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.gesco.models.costos.CCB;
import com.gesco.views.PlantillasViews.BotonNeon;
import com.gesco.views.PlantillasViews.CampoFecha;
import com.gesco.views.PlantillasViews.PlantillaGesco;

public class VistaCargaCCB extends PlantillaGesco {
    
    private static final DateTimeFormatter FECHA_FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private BotonNeon btnSubirDatos;
    private CampoFecha campoFecha;
    private JComboBox<String> usuario;
    private JTextField NB, MERMA, CF, CV, porcentajeDescuento;
    private JLabel lblResultado;
    

    public VistaCargaCCB() {
        super();
        ocultarIcono();
        ocultarlblSprAdmin();
        setImagenFondo("/FondoPrincipal2.png");
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
        lblResultado = crearEtiquetaPersonalizada(" ", "Arial", Font.BOLD, 18, new Color(240, 240, 240),"centro");
    }


    private void agregarCampos(JPanel panelFondoBase) {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setMaximumSize(new Dimension(450, 420)); 
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT); 

        Dimension tamCaja = new Dimension(450, 35);

        campoFecha = new CampoFecha("Fecha (DD/MM/AAAA)", "Times New Roman", Font.PLAIN, 18, new Color(180, 180, 180), "izquierda");
        campoFecha.setLabelColor(new Color(180, 180, 180));
        campoFecha.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(campoFecha);
        formPanel.add(Box.createVerticalStrut(12));

        formPanel.add(crearEtiquetaPersonalizada("Tipo de usuario (Estudiante/Becario/Exonerado/Profesor/Empleado)", "Times New Roman", Font.PLAIN, 18, new Color(180, 180, 180), "izquierda"));
        formPanel.add(Box.createVerticalStrut(5));
        usuario = new JComboBox<>(new String[] {"Estudiante", "Becario", "Exonerado", "Profesor", "Empleado"});
        usuario.setPreferredSize(tamCaja);
        usuario.setMaximumSize(tamCaja);
        usuario.setAlignmentX(Component.LEFT_ALIGNMENT);
        usuario.setFont(new Font("Arial", Font.PLAIN, 15));
        usuario.setBackground(new Color(255, 255, 255, 240));
        formPanel.add(usuario);
        formPanel.add(Box.createVerticalStrut(12));

        formPanel.add(crearEtiquetaPersonalizada("% descuento a aplicar (ej: 25)", "Arial", Font.BOLD, 14, new Color(180, 180, 180), "izquierda"));
        formPanel.add(Box.createVerticalStrut(5));
        porcentajeDescuento = new JTextField();
        CajaCCB(porcentajeDescuento, tamCaja);
        formPanel.add(porcentajeDescuento);
        formPanel.add(Box.createVerticalStrut(12));

        formPanel.add(crearEtiquetaPersonalizada("NB (Número de bandejas servidas)", "Arial", Font.BOLD, 14, new Color(180, 180, 180), "izquierda"));
        formPanel.add(Box.createVerticalStrut(5));
        NB = new JTextField();
        CajaCCB(NB, tamCaja);
        formPanel.add(NB);
        formPanel.add(Box.createVerticalStrut(12)); 

        formPanel.add(crearEtiquetaPersonalizada("MERMA (% de desperdicio)", "Arial", Font.BOLD, 14, new Color(180, 180, 180), "izquierda"));
        formPanel.add(Box.createVerticalStrut(5));
        MERMA = new JTextField();
        CajaCCB(MERMA, tamCaja);
        formPanel.add(MERMA);
        formPanel.add(Box.createVerticalStrut(12)); 

        formPanel.add(crearEtiquetaPersonalizada("CF (Costos Fijos totales)", "Arial", Font.BOLD, 14, new Color(180, 180, 180), "izquierda"));
        formPanel.add(Box.createVerticalStrut(5));
        CF = new JTextField();
        CajaCCB(CF, tamCaja);
        formPanel.add(CF);
        formPanel.add(Box.createVerticalStrut(12)); 

        formPanel.add(crearEtiquetaPersonalizada("CV (Costos Variables totales)", "Arial", Font.BOLD, 14, new Color(180, 180, 180), "izquierda"));
        formPanel.add(Box.createVerticalStrut(5));
        CV = new JTextField();
        CajaCCB(CV, tamCaja);
        formPanel.add(CV);

        panelFondoBase.add(formPanel);
    }


    private void CajaCCB(JTextField c, Dimension d) {
        c.setPreferredSize(d);
        c.setMaximumSize(d);
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        c.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        c.setFont(new Font("Arial", Font.PLAIN, 15));
        c.setBackground(new Color(255, 255, 255, 240));
    }


    private void construirCuerpo() {
        JPanel panelFondo = crearPanel(250,15,500,30,50,50);
        
        panelFondo.setLayout(new BoxLayout(panelFondo, BoxLayout.Y_AXIS));
        panelFondo.setOpaque(false); 
        panelFondo.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        panelFondo.add(Box.createVerticalGlue()); 
        panelFondo.add(crearEtiquetaPersonalizada("Datos para el CCB", "Arial", Font.BOLD, 45, new Color(240, 240, 240), "centro"));
        panelFondo.add(Box.createVerticalStrut(15)); 

        agregarCampos(panelFondo); 

        panelFondo.add(Box.createVerticalStrut(20)); 
        panelFondo.add(lblResultado);
        panelFondo.add(Box.createVerticalStrut(1)); 
        
        panelFondo.add(btnSubirDatos); 
        panelFondo.add(Box.createVerticalGlue());

        this.contenedorPrincipal.add(panelFondo, BorderLayout.CENTER);
    }


    public String getUsuario(){
        Object seleccionado = usuario.getSelectedItem();
        return seleccionado == null ? "Estudiante" : seleccionado.toString();
    }
    public String getNB(){ return NB.getText();}
    public String getMERMA(){ return MERMA.getText();}
    public String getCF(){ return CF.getText();}
    public String getCV(){ return CV.getText();}
    public String getPorcentajeDescuento(){ return porcentajeDescuento.getText();}
    public BotonNeon getBtnSubirDatos() { return btnSubirDatos; }

    public void setCV(double valor) {
        CV.setText(String.format(java.util.Locale.US, "%.2f", valor));
    }

    public void setFecha(LocalDate fecha) {
        if (fecha == null) return;
        campoFecha.setFecha(
            String.valueOf(fecha.getDayOfMonth()),
            String.valueOf(fecha.getMonthValue()),
            String.valueOf(fecha.getYear())
        );
    }


    public LocalDate getFecha() {
        String texto = campoFecha.getFechaTexto();
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("La fecha es obligatoria.");
        }
        try {
            return LocalDate.parse(texto, FECHA_FORMATO);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Formato de fecha invalido. Use DD/MM/AAAA.");
        }
    }


    public CCB crearCCB() {
        LocalDate fecha = getFecha();
        double nb = parseDouble(getNB(), "NB");
        double merma = parseDouble(getMERMA(), "MERMA");
        double cf = parseDouble(getCF(), "CF");
        double cv = parseDouble(getCV(), "CV");
        double porcentaje = parseDouble(getPorcentajeDescuento(), "% descuento") / 100.0;
        return new CCB(fecha, getUsuario(), cf, cv, nb, merma, porcentaje);
    }


    public void setResultado(String resultado) {
        if (resultado == null || resultado.trim().isEmpty()) {
            lblResultado.setText(" ");
        } 
        else {
            lblResultado.setText("VALOR CCB: " + resultado);
        }
    }


    private double parseDouble(String valor, String campo) {
        try {
            return Double.parseDouble(valor);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("El campo " + campo + " debe ser numerico.");
        }
    }
}

