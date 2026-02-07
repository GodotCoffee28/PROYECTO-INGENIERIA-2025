package com.gesco.controllers;

import com.gesco.views.VistaEspera;

public class VistaEsperaControlador {

    private final VistaEspera vista;
    private final Runnable onVolver;

    public VistaEsperaControlador(VistaEspera vista, Runnable onVolver) {
        this.vista = vista;
        this.onVolver = onVolver;
    }

    public void conectar() {
        vista.getVolver().addActionListener(e -> onVolver.run());
    }
}
