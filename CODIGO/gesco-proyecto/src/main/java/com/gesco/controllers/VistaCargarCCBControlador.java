package com.gesco.controllers;

import java.io.IOException;
import java.time.LocalDate;

import javax.swing.JOptionPane;

import com.gesco.models.CCB;
import com.gesco.models.data.Calendario.CalendarioSemanal;
import com.gesco.views.VistaCargaCCB;

public class VistaCargarCCBControlador {

    private final VistaCargaCCB vista;
    private final Runnable onBack;

    public VistaCargarCCBControlador(VistaCargaCCB vista, Runnable onBack) {
        this.vista = vista;
        this.onBack = onBack;
    }

    public void conectar() {
        vista.getBackIcon().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onBack.run();
            }
        });

        vista.getBtnSubirDatos().addActionListener(e -> guardar());
    }

    private void guardar() {
        CCB ccb;
        try {
            ccb = vista.crearCCB();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(vista, ex.getMessage(), "Datos invalidos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate fecha = ccb.getFecha();
        CalendarioSemanal calendario = new CalendarioSemanal(fecha);

        try {
            calendario.agregarCCB(fecha, ccb);
            calendario.guardarEnArchivoPorDefecto();
            vista.setResultado(String.valueOf(ccb.getCcb()));
            JOptionPane.showMessageDialog(vista, "CCB guardado en calendario.", "Guardado", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(vista, "No se pudo guardar calendario.txt", "Error de archivo", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(vista, ex.getMessage(), "Datos invalidos", JOptionPane.WARNING_MESSAGE);
        }
    }
}