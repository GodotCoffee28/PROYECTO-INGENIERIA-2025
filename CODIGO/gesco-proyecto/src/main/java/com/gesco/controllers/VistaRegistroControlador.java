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

        if (!correo.contains("@")) {
            JOptionPane.showMessageDialog(
                vista,
                "El correo debe contener un '@'.",
                "Correo invalido",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        boolean guardado = DataBase.registrarUsuario(cedula, clave, nombre, correo);
        if (!guardado) {
            JOptionPane.showMessageDialog(
                vista,
                "No se pudo guardar el usuario. La cedula ya esta registrada.",
                "Registro fallido",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        JOptionPane.showMessageDialog(
            vista,
            "Registro exitoso. Estamos validando sus datos.",
            "Registro",
            JOptionPane.INFORMATION_MESSAGE
        );
        onRegistroSuccess.run();
    }
}
