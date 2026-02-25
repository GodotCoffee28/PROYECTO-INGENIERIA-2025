package com.gesco.controllers.auntentificacion;


import com.gesco.controllers.gestion_principal.DataBase;
import javax.swing.JOptionPane;

import com.gesco.models.usuarios.Usuario.TipoUsuario;
import com.gesco.views.auntentificacion.VistaRegistro;

public class ControladorRegistro {

    private final VistaRegistro vista;
    private final Runnable onBack;
    private final Runnable onLogin;
    private final Runnable onRegistroSuccess;

    public ControladorRegistro(
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
        String cedula = DataBase.normalizarCedula(vista.getCedula());
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
            if (cedulaNumero < 8_000_000L || cedulaNumero > 45_000_000L) {
                JOptionPane.showMessageDialog(
                    vista,
                    "La cédula debe estar entre 8.000.000 y 45.000.000.",
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
            if (!DataBase.adminAutorizadoPorSuperAdmin(cedula, codigoAdmin)) {
                JOptionPane.showMessageDialog(
                    vista,
                    "No está autorizado para registrarse como administrador con esa cédula/código.",
                    "Autorización inválida",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            guardado = DataBase.registrarAdministrador(cedula, clave, nombre, correo, codigoAdmin);
        } else {
            if (DataBase.cedulaYaRegistrada(cedula)) {
                JOptionPane.showMessageDialog(
                    vista,
                    "La cédula ya está registrada. Inicie sesión o use otra cédula.",
                    "Cédula duplicada",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            TipoUsuario tipoComensal = switch (tipoUsuario) {
                case "Profesor" -> TipoUsuario.PROFESOR;
                case "Empleado" -> TipoUsuario.EMPLEADO;
                default -> TipoUsuario.ESTUDIANTE;
            };
            guardado = DataBase.registrarUsuario(cedula, clave, nombre, correo, tipoComensal);
        }

        if (!guardado) {
            JOptionPane.showMessageDialog(
                vista,
                esAdmin
                    ? "No se pudo registrar como administrador. Revise datos y autorización."
                    : "No se pudo guardar el usuario por un error de persistencia. Intente nuevamente.",
                "Registro fallido",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        onRegistroSuccess.run();
    }
}






