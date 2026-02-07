package com.gesco.controllers;

import com.gesco.views.VistaGestionMenu;

public class VistaGestionMenuControlador {

    private final VistaGestionMenu vista;
    private final Runnable onBack;

    public VistaGestionMenuControlador(VistaGestionMenu vista, Runnable onBack) {
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
