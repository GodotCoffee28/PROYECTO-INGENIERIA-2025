package com.gesco.controllers;

import java.awt.event.MouseEvent;
import java.io.File;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.swing.JOptionPane;
import javax.swing.event.MouseInputAdapter;

import com.gesco.models.TipoUsuario;
import com.gesco.views.VistaInicioSesion;

public class VistaInicioSesionControlador {

    private final VistaInicioSesion vista;
    private final Runnable onBack;
    private final Runnable onRegistro;
    private final Consumer<String> onCedulaAutenticada;
    private final BiConsumer<TipoUsuario, String> onLoginSuccess;

    public VistaInicioSesionControlador(
        VistaInicioSesion vista,
        Runnable onBack,
        Runnable onRegistro,
        Consumer<String> onCedulaAutenticada,
        BiConsumer<TipoUsuario, String> onLoginSuccess
    ) {
        this.vista = vista;
        this.onBack = onBack;
        this.onRegistro = onRegistro;
        this.onCedulaAutenticada = onCedulaAutenticada;
        this.onLoginSuccess = onLoginSuccess;
    }

    public void conectar() {
        // Login normal con cédula y contraseña
        vista.getBtnInicioSesion().addActionListener(e -> procesarInicioSesion());

        // Acceso facial
        vista.getBtnAccesoFacial().addActionListener(e -> procesarAccesoFacial());

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
        String cedula = DataBase.normalizarCedula(vista.getCedula());
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

        boolean valido = DataBase.validarInicioSesion(cedula, clave);
        if (valido) {
            TipoUsuario tipoUsuario = DataBase.obtenerTipoUsuario(cedula);
            String nombre = DataBase.obtenerNombre(cedula);
            if (nombre == null || nombre.isBlank()) nombre = "Usuario";
            onCedulaAutenticada.accept(cedula);
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

    private void procesarAccesoFacial() {
        String cedula = DataBase.normalizarCedula(vista.getCedula());

        if (cedula == null || cedula.isBlank()) {
            JOptionPane.showMessageDialog(
                vista,
                "Debe ingresar su cédula antes de usar el Acceso Facial.",
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

        File imagenSecretaria = DataBase.obtenerImagenSecretaria(cedula);
        if (!imagenSecretaria.exists()) {
            JOptionPane.showMessageDialog(
                vista,
                "Usuario no encontrado en secretaria.",
                "Acceso denegado",
                JOptionPane.ERROR_MESSAGE
            );
            onBack.run();
            return;
        }

        String rutaIngresada = JOptionPane.showInputDialog(
            vista,
            "Ingrese la ruta completa de la imagen a comparar:\n(Ejemplo: C:\\Users\\usuario\\Downloads\\foto.jpg)",
            "Acceso Facial — Verificación",
            JOptionPane.PLAIN_MESSAGE
        );

        if (rutaIngresada == null) return;

        rutaIngresada = rutaIngresada.trim();
        if (rutaIngresada.isBlank()) {
            JOptionPane.showMessageDialog(
                vista,
                "Debe ingresar una ruta de imagen válida.",
                "Ruta vacía",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        File imagenUsuario = new File(rutaIngresada);
        if (!imagenUsuario.exists() || !imagenUsuario.isFile()) {
            JOptionPane.showMessageDialog(
                vista,
                "No se encontró el archivo de imagen en la ruta indicada.",
                "Archivo no encontrado",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            ValidadorIdentidad validador = new ValidadorIdentidad();
            boolean iguales = validador.compararImagenes(imagenSecretaria, imagenUsuario);

            if (iguales) {
                TipoUsuario tipoUsuario = DataBase.obtenerTipoUsuario(cedula);
                String nombre = DataBase.obtenerNombre(cedula);
                if (nombre == null || nombre.isBlank()) nombre = "Usuario";
                onCedulaAutenticada.accept(cedula);
                onLoginSuccess.accept(tipoUsuario, nombre);
            } else {
                JOptionPane.showMessageDialog(
                    vista,
                    "Acceso facial fallido.",
                    "Acceso denegado",
                    JOptionPane.ERROR_MESSAGE
                );
                onBack.run();
            }

        } catch (java.io.IOException ex) {
            JOptionPane.showMessageDialog(
                vista,
                "Error al leer las imágenes: " + ex.getMessage() + "\nVerifique que ambos archivos sean imágenes válidas.",
                "Error de imagen",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}