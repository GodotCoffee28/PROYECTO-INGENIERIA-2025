package com.gesco.controllers.costos;


import com.gesco.controllers.gestion_principal.DataBase;
import com.gesco.models.costos.CFCV;
import com.gesco.views.costos.VistaVerCFCV;

public class ControladorVerCFCV {

    private final VistaVerCFCV vista;
    private final Runnable onBack;

    public ControladorVerCFCV(VistaVerCFCV vista, Runnable onBack) {
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






