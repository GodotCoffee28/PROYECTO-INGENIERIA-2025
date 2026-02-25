package com.gesco.controllers.costos;


import com.gesco.controllers.gestion_principal.DataBase;
import com.gesco.models.costos.CCB;
import com.gesco.views.costos.VistaVerCCB;

public class ControladorVerCCB {

    private final VistaVerCCB vista;
    private final Runnable onBack;

    public ControladorVerCCB(VistaVerCCB vista, Runnable onBack) {
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






