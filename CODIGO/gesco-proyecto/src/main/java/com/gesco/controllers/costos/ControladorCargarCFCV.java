package com.gesco.controllers.costos;


import com.gesco.controllers.gestion_principal.DataBase;
import javax.swing.JOptionPane;

import com.gesco.views.costos.VistaCargarCFCV;

public class ControladorCargarCFCV {

    private final VistaCargarCFCV vista;
    private final Runnable onBack;

    public ControladorCargarCFCV(VistaCargarCFCV vista, Runnable onBack) {
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

        vista.getBtnGuardar().addActionListener(e -> guardar());
    }

    private void guardar() {
        String cfTexto = vista.getCF();
        String cvTexto = vista.getCV();

        double cf = parsearNumero(cfTexto, "CF");
        double cv = parsearNumero(cvTexto, "CV");

        if (Double.isNaN(cf) || Double.isNaN(cv)) return;

        if (cf < 0) {
            JOptionPane.showMessageDialog(vista, "CF no puede ser negativo.", "Datos invalidos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (cv < 0) {
            JOptionPane.showMessageDialog(vista, "CV no puede ser negativo.", "Datos invalidos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        com.gesco.models.costos.CFCV modelo = new com.gesco.models.costos.CFCV(cf, cv);
        boolean ok = DataBase.guardarCfcv(modelo);
        if (ok) {
            vista.setResultado("Costos guardados correctamente.");
            JOptionPane.showMessageDialog(vista, "CF y CV guardados.", "Guardado", JOptionPane.INFORMATION_MESSAGE);
        } else {
            vista.setResultado("Error al guardar costos.");
            JOptionPane.showMessageDialog(vista, "No se pudo guardar CF/CV.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private double parsearNumero(String texto, String nombreCampo) {
        if (texto == null || texto.isBlank()) {
            JOptionPane.showMessageDialog(vista, "Debe completar " + nombreCampo + ".", "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return Double.NaN;
        }
        try {
            return Double.parseDouble(texto.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "Valor invalido en " + nombreCampo + ".", "Datos invalidos", JOptionPane.WARNING_MESSAGE);
            return Double.NaN;
        }
    }
}






