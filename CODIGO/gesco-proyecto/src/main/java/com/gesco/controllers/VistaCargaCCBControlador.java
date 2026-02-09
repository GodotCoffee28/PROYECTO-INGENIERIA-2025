package com.gesco.controllers;

import javax.swing.JOptionPane;

import com.gesco.views.VistaCargaCCB;

public class VistaCargaCCBControlador {

    private final VistaCargaCCB vista;
    private final Runnable onBack;

    public VistaCargaCCBControlador(VistaCargaCCB vista, Runnable onBack) {
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

        vista.getBtnSubirDatos().addActionListener(e -> calcularCcb());
    }

    private void calcularCcb() {
        String tipoUsuario = vista.getUsuario();
        String nbTexto = vista.getNB();
        String mermaTexto = vista.getMERMA();
        String cfTexto = vista.getCF();
        String cvTexto = vista.getCV();

        if (tipoUsuario == null || tipoUsuario.isBlank()) {
            JOptionPane.showMessageDialog(
                vista,
                "Debe especificar el tipo de usuario.",
                "Datos incompletos",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String tipoNormalizado = tipoUsuario.trim();
        if (!tipoNormalizado.equalsIgnoreCase("Estudiante") && 
            !tipoNormalizado.equalsIgnoreCase("Profesor") && 
            !tipoNormalizado.equalsIgnoreCase("Empleado")) {
            JOptionPane.showMessageDialog(
                vista,
                "Tipo de usuario invalido. Debe ser: Estudiante, Profesor o Empleado.",
                "Datos invalidos",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        double nb = parsearNumero(nbTexto, "NB");
        double merma = parsearNumero(mermaTexto, "%merma");
        double cf = parsearNumero(cfTexto, "CF");
        double cv = parsearNumero(cvTexto, "CV");

        if (Double.isNaN(nb) || Double.isNaN(merma) || Double.isNaN(cf) || Double.isNaN(cv)) {
            return;
        }

        if (nb <= 0) {
            JOptionPane.showMessageDialog(
                vista,
                "NB debe ser mayor que 0.",
                "Datos invalidos",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (merma < 0) {
            JOptionPane.showMessageDialog(
                vista,
                "%merma no puede ser negativo.",
                "Datos invalidos",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (cf < 0) {
            JOptionPane.showMessageDialog(
                vista,
                "CF (Costos Fijos) no puede ser negativo.",
                "Datos invalidos",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (cv < 0) {
            JOptionPane.showMessageDialog(
                vista,
                "CV (Costos Variables) no puede ser negativo.",
                "Datos invalidos",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (merma > 1.0) {
            merma = merma / 100.0;
        }

        double ccb = ((cf + cv) / nb) * (1.0 + merma);
        vista.setResultado(String.format("%.2f", ccb));
    }

    private double parsearNumero(String texto, String nombreCampo) {
        if (texto == null || texto.isBlank()) {
            JOptionPane.showMessageDialog(
                vista,
                "Debe completar " + nombreCampo + ".",
                "Datos incompletos",
                JOptionPane.WARNING_MESSAGE
            );
            return Double.NaN;
        }

        try {
            return Double.parseDouble(texto.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                vista,
                "Valor invalido en " + nombreCampo + ".",
                "Datos invalidos",
                JOptionPane.WARNING_MESSAGE
            );
            return Double.NaN;
        }
    }
}
