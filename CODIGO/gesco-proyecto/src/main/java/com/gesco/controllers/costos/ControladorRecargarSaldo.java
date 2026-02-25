package com.gesco.controllers.costos;

import javax.swing.JOptionPane;

import com.gesco.controllers.gestion_principal.DataBase;
import com.gesco.views.costos.VistaRecargarSaldo;

public class ControladorRecargarSaldo {

    private final VistaRecargarSaldo vista;
    private final String cedulaSesion;
    private final Runnable onBack;
    private final Runnable onRecargaSuccess;

    public ControladorRecargarSaldo(
        VistaRecargarSaldo vista,
        String cedulaSesion,
        Runnable onBack,
        Runnable onRecargaSuccess
    ) {
        this.vista = vista;
        this.cedulaSesion = DataBase.normalizarCedula(cedulaSesion);
        this.onBack = onBack;
        this.onRecargaSuccess = onRecargaSuccess;
    }

    public void conectar() {
        vista.setCedula(cedulaSesion);

        vista.getBackIcon().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onBack.run();
            }
        });

        vista.getBotonRecargar().addActionListener(e -> procesarRecarga());
    }

    private void procesarRecarga() {
        String fecha = valor(vista.getFecha());
        String banco = valor(vista.getBanco());
        String referencia = valor(vista.getReferencia());
        String montoStr = valor(vista.getMonto());

        if (cedulaSesion.isBlank()) {
            JOptionPane.showMessageDialog(
                vista,
                "No hay una sesión activa para recargar saldo.",
                "Sesión requerida",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (fecha.isBlank() || banco.isBlank() || referencia.isBlank() || montoStr.isBlank()) {
            JOptionPane.showMessageDialog(
                vista,
                "Debe completar fecha, banco, referencia y monto.",
                "Datos incompletos",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        double monto;
        try {
            monto = Double.parseDouble(montoStr.replace(',', '.'));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                vista,
                "El monto debe ser numérico.",
                "Monto inválido",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (monto <= 0) {
            JOptionPane.showMessageDialog(
                vista,
                "El monto debe ser mayor a 0.",
                "Monto inválido",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        double saldoActual = DataBase.obtenerSaldo(cedulaSesion);
        double nuevoSaldo = saldoActual + monto;
        boolean actualizado = DataBase.actualizarSaldo(cedulaSesion, nuevoSaldo);

        if (!actualizado) {
            JOptionPane.showMessageDialog(
                vista,
                "No se pudo actualizar el saldo. Intente nuevamente.",
                "Recarga fallida",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        JOptionPane.showMessageDialog(
            vista,
            "Recarga exitosa. Nuevo saldo: " + String.format("%.2f", nuevoSaldo) + " Bs.",
            "Recarga exitosa",
            JOptionPane.INFORMATION_MESSAGE
        );
        onRecargaSuccess.run();
    }

    private String valor(String texto) {
        return texto == null ? "" : texto.trim();
    }
}
