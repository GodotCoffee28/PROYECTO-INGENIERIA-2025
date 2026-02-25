package com.gesco.views;

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
        // Usamos blanco para mantener la transparencia y el borde neón
        super(new Color(255, 255, 255), new Color(255, 255, 255));
        
        actualizarDatos(titulo, horaInicio, horaFin);
    }

    @Override
    protected void construirContenido() {
        // Configuración de la Cabecera (Título del Turno)
        lblTitulo = new JLabel("", SwingConstants.CENTER);
        setTituloEstilo(lblTitulo); // Usa el método de la base para fuente Segoe UI y color blanco
        containerCabecera.add(lblTitulo, BorderLayout.CENTER);

        // Ajuste del margen interno para que respire igual que la de menú
        containerCuerpo.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Etiqueta de Inicio
        lblInicio = new JLabel("");
        configurarLabelTurno(lblInicio);
        
        // Etiqueta de Fin
        lblFin = new JLabel("");
        configurarLabelTurno(lblFin);

        containerCuerpo.add(lblInicio);
        containerCuerpo.add(Box.createVerticalStrut(10)); // Espacio entre horas
        containerCuerpo.add(lblFin);
    }

    /**
     * Aplica el estilo visual de los platillos a los datos del turno
     */
    private void configurarLabelTurno(JLabel label) {
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(Color.WHITE); // Cambiado de Negro a Blanco para el fondo oscuro
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void actualizarDatos(String titulo, String inicio, String fin) {
        if (lblTitulo != null) lblTitulo.setText(titulo.toUpperCase());
        if (lblInicio != null) lblInicio.setText("• INICIO: " + inicio);
        if (lblFin != null) lblFin.setText("• FIN: " + fin);
    }
}