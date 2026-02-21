package com.gesco.controllers;

import com.gesco.views.VistaGestionMenu;

public class VistaGestionMenuControlador {

    private final VistaGestionMenu vista;
    private final Runnable onBack;
    private final Runnable onEditar;
    private final Runnable onCrear;
    private final Runnable onReiniciar;

    public VistaGestionMenuControlador(VistaGestionMenu vista, Runnable onBack, Runnable onEditar, Runnable onCrear, Runnable onReiniciar) {
        this.vista = vista;
        this.onBack = onBack;
        this.onEditar = onEditar;
        this.onCrear = onCrear;
        this.onReiniciar = onReiniciar;
    }

    public void conectar() {
        vista.getBackIcon().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onBack.run();
            }
        });

        vista.getBtnEditar().addActionListener(e -> onEditar.run());
        vista.getBtnCrear().addActionListener(e -> onCrear.run());
        vista.getBtnReiniciar().addActionListener(e -> onReiniciar.run());
    }
}
