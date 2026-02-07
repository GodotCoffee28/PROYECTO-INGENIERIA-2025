package com.gesco.controllers;

import com.gesco.views.VistaInicioComensal;

public class VistaInicioComensalControlador {

    private final VistaInicioComensal vista;
    private final Runnable onBack;

    public VistaInicioComensalControlador(VistaInicioComensal vista, Runnable onBack) {
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
    }
}
