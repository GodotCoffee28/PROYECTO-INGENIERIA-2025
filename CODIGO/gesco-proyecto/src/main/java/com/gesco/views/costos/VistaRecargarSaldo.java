package com.gesco.views.costos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import com.gesco.views.PlantillasViews.BotonNeon;
import com.gesco.views.PlantillasViews.CampoFecha;
import com.gesco.views.PlantillasViews.PlantillaGesco;

public class VistaRecargarSaldo extends PlantillaGesco {
    private CampoFecha campoFecha;
    private JTextField cedulaField, montoField, referenciaField;
    private JComboBox<String> comboBanco;
    private BotonNeon botonRecargar;
    private JToggleButton switchPana;
    private String cedulaOriginal = "";
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
        Dimension tamCaja = new Dimension(450, 40);

        botonRecargar = new BotonNeon("Recargar");
        botonRecargar.setPreferredSize(new Dimension(400, 60));
        botonRecargar.setMaximumSize(tamBoton);
        botonRecargar.setAlignmentX(Component.CENTER_ALIGNMENT);
        comboBanco = new JComboBox<>(new String[]{
            "Seleccione un banco",
            "Banesco Banco Universal (0134)",
            "Banco Exterior (0115)",
            "Bancamiga Banco Universal (0172)",
            "Banco de Venezuela (0102)",
            "Banco del Tesoro (0163)",
            "Banco Mercantil (0105)",
            "Banco Nacional de Crédito (BNC) (0191)",
            "Banco Plaza (0138)",
            "Banco Provincial (BBVA) (0108)",
            "Banplus Banco Universal (0174)"
        });
        comboBanco.setPreferredSize(tamCaja);
        comboBanco.setMaximumSize(tamCaja);
        comboBanco.setMinimumSize(tamCaja);
        comboBanco.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboBanco.setFont(new Font("Arial", Font.PLAIN, 15));
        comboBanco.setBackground(new Color(255, 255, 255, 240));
        comboBanco.setSelectedIndex(0);

        switchPana = new JToggleButton("¿Recargar a un Pana?");
        switchPana.setPreferredSize(new Dimension(450, 35));
        switchPana.setMaximumSize(new Dimension(450, 35));
        switchPana.setFocusPainted(false);
        switchPana.setBackground(new Color(30, 30, 35));
        switchPana.setForeground(Color.WHITE);
        switchPana.setCursor(new Cursor(Cursor.HAND_CURSOR));
        switchPana.setFont(new Font("Arial", Font.BOLD, 14));
        switchPana.setAlignmentX(Component.LEFT_ALIGNMENT);

    }

    private void construirCuerpo() {
        JPanel panelFondo = crearPanel(250, 10, 500, 20, 50, 50);
        
        panelFondo.setLayout(new BoxLayout(panelFondo, BoxLayout.Y_AXIS));
        panelFondo.setOpaque(false); 
        panelFondo.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));

        panelFondo.add(Box.createVerticalGlue()); 
        panelFondo.add(crearEtiquetaPersonalizada("Recargar saldo","Times New Roman", Font.BOLD, 45, new Color(240, 240, 240), "centro"));
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
            
            formPanel.add(switchPana);
            formPanel.add(Box.createVerticalStrut(15));

            Dimension tamCaja = new Dimension(450, 40);

            formPanel.add(crearEtiquetaPersonalizada("Cedula de identidad", "Times New Roman", Font.PLAIN, 18, Color.WHITE, "izquierda"));
            formPanel.add(Box.createVerticalStrut(8));
            cedulaField = new JTextField();
            diseñarCaja(cedulaField, tamCaja);
            formPanel.add(cedulaField);
            formPanel.add(Box.createVerticalStrut(20)); 

            formPanel.add(crearEtiquetaPersonalizada("Banco", "Times New Roman", Font.PLAIN, 18, Color.WHITE, "izquierda"));
            formPanel.add(Box.createVerticalStrut(8));  
            formPanel.add(comboBanco);
            formPanel.add(Box.createVerticalStrut(8));

            formPanel.add(crearEtiquetaPersonalizada("Referencia", "Times New Roman", Font.PLAIN, 18, Color.WHITE, "izquierda"));
            formPanel.add(Box.createVerticalStrut(8));
            referenciaField = new JTextField();
            diseñarCaja(referenciaField, tamCaja);
            ((AbstractDocument) referenciaField.getDocument()).setDocumentFilter(new DocumentFilter() {
                @Override
                public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                        throws BadLocationException {
                    if (string == null) return;
                    String soloDigitos = string.replaceAll("[^0-9]", "");
                    int longitudActual = fb.getDocument().getLength();
                    int espacioDisponible = 20 - longitudActual;
                    if (espacioDisponible <= 0) return;
                    if (soloDigitos.length() > espacioDisponible) {
                        soloDigitos = soloDigitos.substring(0, espacioDisponible);
                    }
                    super.insertString(fb, offset, soloDigitos, attr);
                }

                @Override
                public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attr)
                        throws BadLocationException {
                    if (string == null) return;
                    String soloDigitos = string.replaceAll("[^0-9]", "");
                    int longitudActual = fb.getDocument().getLength() - length;
                    int espacioDisponible = 20 - longitudActual;
                    if (espacioDisponible <= 0) return;
                    if (soloDigitos.length() > espacioDisponible) {
                        soloDigitos = soloDigitos.substring(0, espacioDisponible);
                    }
                    super.replace(fb, offset, length, soloDigitos, attr);
                }
            });
            formPanel.add(referenciaField);

            formPanel.add(crearEtiquetaPersonalizada("Monto Bs.", "Times New Roman", Font.PLAIN, 18, Color.WHITE, "izquierda"));
            formPanel.add(Box.createVerticalStrut(8));
            montoField = new JTextField();
            diseñarCaja(montoField, tamCaja);
            formPanel.add(montoField);
            formPanel.add(Box.createVerticalStrut(20));

            panelFondoBase.add(formPanel);
    }


    public String getFecha() { return campoFecha.getFechaTexto(); }  
    public JTextField getCedula() { return cedulaField;}
    public String getBanco() { return (String) comboBanco.getSelectedItem();}
    public String getReferencia() { return referenciaField.getText();}
    public String getMonto() { return montoField.getText();}
    public BotonNeon getBotonRecargar() {return botonRecargar;}
    public JToggleButton getSwitchPana(){return switchPana;};
    public String getCedulaOriginal(){return cedulaOriginal;};

    public void setCedula(String cedula) {
    this.cedulaOriginal = (cedula == null) ? "" : cedula;
    this.cedulaField.setText(cedulaOriginal);          
    this.cedulaField.setEditable(false);            
}

}