package com.gesco.controllers.costos;

import com.gesco.controllers.gestion_principal.DataBase;
import com.gesco.models.costos.CCB;
import com.gesco.views.Registros.VistaHistorialCCB;

public class ControladorHistorialCCB {

    private final VistaHistorialCCB vista;
    private final Runnable onBack;

    public ControladorHistorialCCB(VistaHistorialCCB vista, Runnable onBack) {
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

        cargar();
    }

    public void cargar() {
        vista.limpiarHistorial();
        java.util.List<CCB> registros = DataBase.obtenerHistorialCcb();
        for (CCB ccb : registros) {
            vista.agregarRegistroCCB(ccb);
        }
    }
}





