package com.gesco.controllers;

import com.gesco.views.VistaFila;

public class VistaFilaControlador {

    private final VistaFila vista;
    private final Runnable onBack;
    private final Runnable onMenuSemana;

    public VistaFilaControlador(VistaFila vista, Runnable onBack, Runnable onMenuSemana) {
        this.vista = vista;
        this.onBack = onBack;
        this.onMenuSemana = onMenuSemana;
    }

    public void conectar() {
        vista.getBackIcon().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onBack.run();
            }
        });

        vista.getBtnVerMenuSemana().addActionListener(e -> onMenuSemana.run());
    }
}
