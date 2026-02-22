package com.gesco.controllers;

import javax.swing.JOptionPane;

import com.gesco.views.VistaRegistro;

public class VistaRegistroControlador {

    private final VistaRegistro vista;
    private final Runnable onBack;
    private final Runnable onLogin;
    private final Runnable onRegistroSuccess;

    public VistaRegistroControlador(
        VistaRegistro vista,
        Runnable onBack,
        Runnable onLogin,
        Runnable onRegistroSuccess
    ) {
        this.vista = vista;
        this.onBack = onBack;
        this.onLogin = onLogin;
        this.onRegistroSuccess = onRegistroSuccess;
    }

    public void conectar() {
        vista.getBackIcon().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onBack.run();
            }
        });
        vista.getLoginLink().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onLogin.run();
            }
        });
        vista.getBtnRegistrarse().addActionListener(e -> procesarRegistro());
    }

    private void procesarRegistro() {
        String nombre = vista.getNombreApellido();
        String cedula = vista.getCedula();
        String correo = vista.getCorreo();
        String clave = vista.getContra();
        String tipoUsuario = vista.getTipoUsuarioSeleccionado();
        String codigoAdmin = vista.getCodigoAdmin();

        if (nombre == null || nombre.isBlank()
            || cedula == null || cedula.isBlank()
            || correo == null || correo.isBlank()
            || clave == null || clave.isBlank()) {
            JOptionPane.showMessageDialog(
                vista,
                "Debe completar todos los campos.",
                "Datos incompletos",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!cedula.matches("\\d+")) {
            JOptionPane.showMessageDialog(
                vista,
                "La cedula debe contener solo numeros.",
                "Cedula invalida",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            long cedulaNumero = Long.parseLong(cedula);
            if (cedulaNumero < 8_000_000L) {
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

        if (!correo.contains("@")) {
            JOptionPane.showMessageDialog(
                vista,
                "El correo debe contener un '@'.",
                "Correo invalido",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        boolean guardado;
        boolean esAdmin = "Administrador".equalsIgnoreCase(tipoUsuario);
        if (esAdmin && (codigoAdmin == null || codigoAdmin.isBlank())) {
            JOptionPane.showMessageDialog(
                vista,
                "Para registrarse como administrador debe ingresar un código de autorización.",
                "Código requerido",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (esAdmin) {
            guardado = DataBase.registrarAdministrador(cedula, clave, nombre, correo, codigoAdmin);
        } else {
            guardado = DataBase.registrarUsuario(cedula, clave, nombre, correo);
        }

        if (!guardado) {
            JOptionPane.showMessageDialog(
                vista,
                esAdmin
                    ? "No se pudo registrar como administrador. Verifique autorización, cédula o duplicidad."
                    : "No se pudo guardar el usuario. La cédula ya está registrada o no cumple validación.",
                "Registro fallido",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        onRegistroSuccess.run();
    }
}
