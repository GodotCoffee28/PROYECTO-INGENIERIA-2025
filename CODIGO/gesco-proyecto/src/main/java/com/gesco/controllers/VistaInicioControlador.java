package com.gesco.controllers;

import com.gesco.views.VistaInicio;

public class VistaInicioControlador {

    private final VistaInicio vista;
    private final Runnable onInicioSesion;
    private final Runnable onRegistro;

    public VistaInicioControlador(VistaInicio vista, Runnable onInicioSesion, Runnable onRegistro) {
        this.vista = vista;
        this.onInicioSesion = onInicioSesion;
        this.onRegistro = onRegistro;
    }

    public void conectar() {
        vista.getBtnInicioSesion().addActionListener(e -> onInicioSesion.run());
        vista.getBtnRegistrarse().addActionListener(e -> onRegistro.run());
    }
}
