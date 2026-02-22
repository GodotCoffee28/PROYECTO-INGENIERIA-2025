package com.gesco.controllers;

import com.gesco.models.CCB;
import com.gesco.views.VistaVerCCB;

public class VistaVerCCBControlador {

    private final VistaVerCCB vista;
    private final Runnable onBack;

    public VistaVerCCBControlador(VistaVerCCB vista, Runnable onBack) {
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

        vista.getBtnRefrescar().addActionListener(e -> cargar());
        cargar();
    }

    private void cargar() {
        CCB ccb = DataBase.obtenerUltimoCcb();
        vista.setCCB(ccb);
    }
}
