package com.gesco.controllers;

import com.gesco.views.VistaEditarMenu;

public class VistaEditarMenuControlador {

    private final VistaEditarMenu vista;
    private final Runnable onBack;

    public VistaEditarMenuControlador(VistaEditarMenu vista, Runnable onBack) {
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
