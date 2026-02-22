package com.gesco.controllers;

import javax.swing.JOptionPane;

import com.gesco.models.CCB;
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

        boolean ok = DataBase.guardarCcb(ccb);
        if (ok) {
            vista.setResultado(String.valueOf(ccb.getCcb()));
            JOptionPane.showMessageDialog(vista, "CCB guardado en ccb.txt.", "Guardado", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(vista, "No se pudo guardar ccb.txt", "Error de archivo", JOptionPane.ERROR_MESSAGE);
        }
    }
}