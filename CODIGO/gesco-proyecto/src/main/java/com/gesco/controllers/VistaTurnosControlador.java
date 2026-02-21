package com.gesco.controllers;

import com.gesco.views.VistaTurnos;

public class VistaTurnosControlador {

    private final VistaTurnos vista;
    private final Runnable onBack;
    private final Runnable onVerMenu;

    public VistaTurnosControlador(VistaTurnos vista, Runnable onBack, Runnable onVerMenu) {
        this.vista = vista;
        this.onBack = onBack;
        this.onVerMenu = onVerMenu;
    }

    public void conectar() {
        vista.getBackIcon().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onBack.run();
            }
        });

        vista.getBtnVerMenu().addActionListener(e -> onVerMenu.run());
    }
}
