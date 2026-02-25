package com.gesco.controllers.inicio;

import com.gesco.views.inicio.VistaInicio;

public class ControladorInicio {

    private final VistaInicio vista;
    private final Runnable onInicioSesion;
    private final Runnable onRegistro;

    public ControladorInicio(VistaInicio vista, Runnable onInicioSesion, Runnable onRegistro) {
        this.vista = vista;
        this.onInicioSesion = onInicioSesion;
        this.onRegistro = onRegistro;
    }

    public void conectar() {
        vista.getBtnInicioSesion().addActionListener(e -> onInicioSesion.run());
        vista.getBtnRegistrarse().addActionListener(e -> onRegistro.run());
    }
}



