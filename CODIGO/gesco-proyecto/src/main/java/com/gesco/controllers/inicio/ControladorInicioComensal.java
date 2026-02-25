package com.gesco.controllers.inicio;

import com.gesco.views.inicio.VistaInicioComensal;

public class ControladorInicioComensal {

    private final VistaInicioComensal vista;
    private final Runnable onBack;
    private final Runnable onMenuSemana;
    private final Runnable onTurnos;
    private final Runnable onRecargarSaldo;

    public ControladorInicioComensal(
        VistaInicioComensal vista,
        Runnable onBack,
        Runnable onMenuSemana,
        Runnable onTurnos,
        Runnable onRecargarSaldo
    ) {
        this.vista = vista;
        this.onBack = onBack;
        this.onMenuSemana = onMenuSemana;
        this.onTurnos = onTurnos;
        this.onRecargarSaldo = onRecargarSaldo;
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
    }
}



