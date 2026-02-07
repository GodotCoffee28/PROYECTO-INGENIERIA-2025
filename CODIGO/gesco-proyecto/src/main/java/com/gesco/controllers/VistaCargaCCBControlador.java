package com.gesco.controllers;

import com.gesco.views.VistaCargaCCB;

public class VistaCargaCCBControlador {

    private final VistaCargaCCB vista;
    private final Runnable onBack;

    public VistaCargaCCBControlador(VistaCargaCCB vista, Runnable onBack) {
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
