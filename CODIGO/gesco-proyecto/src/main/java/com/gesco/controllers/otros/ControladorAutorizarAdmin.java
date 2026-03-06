package com.gesco.controllers.otros;

import javax.swing.JOptionPane;

import com.gesco.controllers.gestion_principal.DataBase;
import com.gesco.views.otros.VistaAutorizarAdmin;

public class ControladorAutorizarAdmin {

    private final VistaAutorizarAdmin vista;
    private final Runnable onBack;

    public ControladorAutorizarAdmin(VistaAutorizarAdmin vista, Runnable onBack) {
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

        vista.getBtnCrear().addActionListener(e -> autorizarAdmin());
    }

    private void autorizarAdmin() {
        String cedula = DataBase.normalizarCedula(vista.getCedula());
        String codigo = vista.getCodigo() == null ? "" : vista.getCodigo().trim();

        if (cedula.isBlank() || codigo.isBlank()) {
            JOptionPane.showMessageDialog(
                vista,
                "Debe completar cédula y código de autorización.",
                "Datos incompletos",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!cedula.matches("\\d+")) {
            JOptionPane.showMessageDialog(
                vista,
                "La cédula debe contener solo números.",
                "Cédula inválida",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        boolean autorizado = DataBase.autorizarAdministrador(cedula, codigo);
        if (!autorizado) {
            JOptionPane.showMessageDialog(
                vista,
                "No se pudo autorizar al administrador. Verifique los datos.",
                "Autorización fallida",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        JOptionPane.showMessageDialog(
            vista,
            "Administrador autorizado correctamente.",
            "Autorización exitosa",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}
