package com.gesco.controllers;

import com.gesco.views.VistaCrearMenu;

public class VistaCrearMenuControlador {

    private final VistaCrearMenu vista;
    private final Runnable onBack;

    public VistaCrearMenuControlador(VistaCrearMenu vista, Runnable onBack) {
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
