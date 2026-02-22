package com.gesco.controllers;

import java.awt.event.MouseEvent;
import java.util.function.BiConsumer;

import javax.swing.JOptionPane;
import javax.swing.event.MouseInputAdapter;

import com.gesco.models.TipoUsuario;
import com.gesco.views.VistaInicioSesion;

public class VistaInicioSesionControlador {

    private final VistaInicioSesion vista;
    private final Runnable onBack;
    private final Runnable onRegistro;
    private final BiConsumer<TipoUsuario, String> onLoginSuccess;

    public VistaInicioSesionControlador(
        VistaInicioSesion vista,
        Runnable onBack,
        Runnable onRegistro,
        BiConsumer<TipoUsuario, String> onLoginSuccess
    ) {
        this.vista = vista;
        this.onBack = onBack;
        this.onRegistro = onRegistro;
        this.onLoginSuccess = onLoginSuccess;
    }

    public void conectar() {
        vista.getBtnInicioSesion().addActionListener(e -> procesarInicioSesion());
        vista.getRegistroLink().addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onRegistro.run();
            }
        });
        vista.getBackIcon().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onBack.run();
            }
        });
    }

    private void procesarInicioSesion() {
        String cedula = vista.getCedula();
        String clave = vista.getClave();

        if (cedula == null || cedula.isBlank() || clave == null || clave.isBlank()) {
            JOptionPane.showMessageDialog(
                vista,
                "Debe completar cedula y clave.",
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

        try {
            if (Long.parseLong(cedula) < 8_000_000L) {
                JOptionPane.showMessageDialog(
                    vista,
                    "La cédula debe ser mayor o igual a 8.000.000.",
                    "Cédula inválida",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                vista,
                "La cédula ingresada no es válida.",
                "Cédula inválida",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        boolean valido = DataBase.validarInicioSesion(cedula, clave);
        if (valido) {
            TipoUsuario tipoUsuario = DataBase.obtenerTipoUsuario(cedula);
            String nombre = DataBase.obtenerNombre(cedula);
            if (nombre == null || nombre.isBlank()) {
                nombre = "Usuario";
            }
            onLoginSuccess.accept(tipoUsuario, nombre);
        } else {
            JOptionPane.showMessageDialog(
                vista,
                "Usuario o clave incorrectos.",
                "Acceso denegado",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
