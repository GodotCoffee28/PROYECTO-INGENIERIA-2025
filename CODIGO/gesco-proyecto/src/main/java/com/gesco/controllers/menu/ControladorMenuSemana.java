package com.gesco.controllers.menu;

import com.gesco.views.menu.VistaMenuSemana;

public class ControladorMenuSemana {

    private final VistaMenuSemana vista;
    private final Runnable onBack;

    public ControladorMenuSemana(VistaMenuSemana vista, Runnable onBack) {
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



