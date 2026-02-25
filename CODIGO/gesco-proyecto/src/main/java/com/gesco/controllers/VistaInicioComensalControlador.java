package com.gesco.controllers;

import com.gesco.views.VistaInicioComensal;

public class VistaInicioComensalControlador {

    private final VistaInicioComensal vista;
    private final Runnable onBack;
    private final Runnable onMenuSemana;
    private final Runnable onTurnos;
    private final Runnable onRecargarSaldo;
    private final Runnable onFila;

    public VistaInicioComensalControlador(
        VistaInicioComensal vista,
        Runnable onBack,
        Runnable onMenuSemana,
        Runnable onTurnos,
        Runnable onRecargarSaldo,
        Runnable onFila
    ) {
        this.vista = vista;
        this.onBack = onBack;
        this.onMenuSemana = onMenuSemana;
        this.onTurnos = onTurnos;
        this.onRecargarSaldo = onRecargarSaldo;
        this.onFila = onFila;
    }

    public void conectar() {
        vista.getBackIcon().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onBack.run();
            }
        });

        vista.getBtnVerMenu().addActionListener(e -> onMenuSemana.run());
        vista.getBtnHorarios().addActionListener(e -> onTurnos.run());
        vista.getBtnRecargar().addActionListener(e -> onRecargarSaldo.run());
        vista.getBtnAccesoFila().addActionListener(e -> onFila.run());
    }
}
