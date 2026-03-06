package com.gesco.views.otros;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Box;
import javax.swing.border.EmptyBorder;

import com.gesco.views.PlantillasViews.TarjetaGeneral;

public class TarjetaTurno extends TarjetaGeneral {

    private JLabel lblTitulo;
    private JLabel lblInicio;
    private JLabel lblFin;

    public TarjetaTurno(String titulo, String horaInicio, String horaFin) {
        super(new Color(255, 255, 255), new Color(255, 255, 255));
        
        actualizarDatos(titulo, horaInicio, horaFin);
    }

    @Override
    protected void construirContenido() {
        lblTitulo = new JLabel("", SwingConstants.CENTER);
        setTituloEstilo(lblTitulo);
        containerCabecera.add(lblTitulo, BorderLayout.CENTER);

        containerCuerpo.setBorder(new EmptyBorder(15, 20, 15, 20));

        lblInicio = new JLabel("");
        configurarLabelTurno(lblInicio);

        lblFin = new JLabel("");
        configurarLabelTurno(lblFin);

        containerCuerpo.add(lblInicio);
        containerCuerpo.add(Box.createVerticalStrut(10));
        containerCuerpo.add(lblFin);
    }

    private void configurarLabelTurno(JLabel label) {
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(Color.WHITE);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void actualizarDatos(String titulo, String inicio, String fin) {
        if (lblTitulo != null) lblTitulo.setText(titulo.toUpperCase());
        if (lblInicio != null) lblInicio.setText("• INICIO: " + inicio);
        if (lblFin != null) lblFin.setText("• FIN: " + fin);
    }
}
