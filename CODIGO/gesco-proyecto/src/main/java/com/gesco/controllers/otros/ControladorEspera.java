package com.gesco.controllers.otros;

import com.gesco.views.otros.VistaEspera;

public class ControladorEspera {

    private final VistaEspera vista;
    private final Runnable onVolver;

    public ControladorEspera(VistaEspera vista, Runnable onVolver) {
        this.vista = vista;
        this.onVolver = onVolver;
    }

    public void conectar() {
        vista.getVolver().addActionListener(e -> onVolver.run());
    }
}



