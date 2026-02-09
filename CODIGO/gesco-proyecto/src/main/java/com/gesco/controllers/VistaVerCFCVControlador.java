package com.gesco.controllers;

import com.gesco.models.CFCV;
import com.gesco.views.VistaVerCFCV;

public class VistaVerCFCVControlador {

    private final VistaVerCFCV vista;
    private final Runnable onBack;

    public VistaVerCFCVControlador(VistaVerCFCV vista, Runnable onBack) {
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
        CFCV datos = DataBase.obtenerCfcv();
        vista.setCf(datos.getCf());
        vista.setCv(datos.getCv());
    }
}
