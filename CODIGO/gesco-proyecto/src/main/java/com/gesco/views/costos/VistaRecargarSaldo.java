package com.gesco.views.costos;

import java.awt.*;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import com.gesco.views.PlantillasViews.BotonNeon;
import com.gesco.views.PlantillasViews.CampoFecha;
import com.gesco.views.PlantillasViews.PlantillaGesco;

public class VistaRecargarSaldo extends PlantillaGesco {
    
    private JLabel Titulo;
    private CampoFecha campoFecha;
    private JTextField CeduField, MontoField, RefenciaField;
    private JComboBox<String> comboBanco;
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
        Dimension tamCaja = new Dimension(450, 40);

        botonRecargar = new BotonNeon("Recargar");
        botonRecargar.setPreferredSize(new Dimension(400, 60));
        botonRecargar.setMaximumSize(tamBoton);
        botonRecargar.setAlignmentX(Component.CENTER_ALIGNMENT);
        Titulo = crearEtiquetaPersonalizada("Recargar saldo","Times New Roman", Font.BOLD, 45, new Color(240, 240, 240), "centro");
        Titulo.setFont(new Font("Arial", Font.BOLD, 45));
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
            formPanel.add(comboBanco);
            formPanel.add(Box.createVerticalStrut(8));

            formPanel.add(crearEtiquetaPersonalizada("Referencia", "Times New Roman", Font.PLAIN, 18, Color.WHITE, "izquierda"));
            formPanel.add(Box.createVerticalStrut(8));
            RefenciaField = new JTextField();
            diseñarCaja(RefenciaField, tamCaja);
            ((AbstractDocument) RefenciaField.getDocument()).setDocumentFilter(new DocumentFilter() {
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
    public String getBanco() { return (String) comboBanco.getSelectedItem();}
    public String getReferencia() { return RefenciaField.getText();}
    public String getMonto() { return MontoField.getText();}
    public BotonNeon getBotonRecargar() {return botonRecargar;}

    public void setCedula(String cedula) {
        CeduField.setText(cedula == null ? "" : cedula);
        CeduField.setEditable(false);
    }

}


