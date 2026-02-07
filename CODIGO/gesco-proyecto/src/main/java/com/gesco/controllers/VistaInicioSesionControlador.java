package com.gesco.controllers;

import java.awt.event.MouseEvent;
import java.util.function.BiConsumer;

import javax.swing.JOptionPane;
import javax.swing.event.MouseInputAdapter;

import com.gesco.views.VistaInicioSesion;

public class VistaInicioSesionControlador {

    private final VistaInicioSesion vista;
    private final Runnable onBack;
    private final Runnable onRegistro;
    private final BiConsumer<Boolean, String> onLoginSuccess;

    public VistaInicioSesionControlador(
        VistaInicioSesion vista,
        Runnable onBack,
        Runnable onRegistro,
        BiConsumer<Boolean, String> onLoginSuccess
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

        boolean valido = DataBase.validarInicioSesion(cedula, clave);
        if (valido) {
            boolean esAdmin = DataBase.esAdmin(cedula);
            String nombre = DataBase.obtenerNombre(cedula);
            if (nombre == null || nombre.isBlank()) {
                nombre = "Usuario";
            }
            onLoginSuccess.accept(esAdmin, nombre);
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
