package com.gesco.controllers.inicio;

import com.gesco.views.inicio.VistaInicioComensal;

public class ControladorInicioComensal {

    private final VistaInicioComensal vista;
    private final Runnable onBack;
    private final Runnable onMenuSemana;
    private final Runnable onTurnos;
    private final Runnable onRecargarSaldo;
    private final Runnable onFila;
    private final Runnable onVerMovimientos;

    public ControladorInicioComensal(
        VistaInicioComensal vista,
        Runnable onBack,
        Runnable onMenuSemana,
        Runnable onTurnos,
        Runnable onRecargarSaldo,
        Runnable onFila,
        Runnable onVerMovimientos
    ) {
        this.vista = vista;
        this.onBack = onBack;
        this.onMenuSemana = onMenuSemana;
        this.onTurnos = onTurnos;
        this.onRecargarSaldo = onRecargarSaldo;
        this.onFila = onFila;
        this.onVerMovimientos = onVerMovimientos;
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
        vista.getBtnRegistro().addActionListener(e -> onVerMovimientos.run());
    }
}



