package com.gesco.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.gesco.views.PlantillasViews.TarjetaGeneral;

public class TarjetaTurno extends TarjetaGeneral {

    private JLabel lblTitulo;
    private JLabel lblInicio;
    private JLabel lblFin;

    public TarjetaTurno(String titulo, String horaInicio, String horaFin) {
        super(new Color(225, 225, 225), new Color(160, 160, 160));
        
        actualizarDatos(titulo, horaInicio, horaFin);
    }

    @Override
    protected void construirContenido() {
        lblTitulo = new JLabel("", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(Color.DARK_GRAY);
        containerCabecera.add(lblTitulo, BorderLayout.CENTER);

        containerCuerpo.setBorder(new EmptyBorder(10, 15, 10, 15));

        lblInicio = new JLabel("");
        lblInicio.setFont(new Font("Arial", Font.PLAIN, 24));
        lblInicio.setForeground(Color.BLACK);
        lblInicio.setAlignmentX(JLabel.LEFT_ALIGNMENT);

        lblFin = new JLabel("");
        lblFin.setFont(new Font("Arial", Font.PLAIN, 24));
        lblFin.setForeground(Color.BLACK);
        lblFin.setAlignmentX(JLabel.LEFT_ALIGNMENT);

        containerCuerpo.add(lblInicio);
        containerCuerpo.add(javax.swing.Box.createVerticalStrut(5)); 
        containerCuerpo.add(lblFin);
    }

    private void actualizarDatos(String titulo, String inicio, String fin) {
        lblTitulo.setText(titulo);
        lblInicio.setText("Inicio: " + inicio);
        lblFin.setText("Finalización: " + fin);
    }
}