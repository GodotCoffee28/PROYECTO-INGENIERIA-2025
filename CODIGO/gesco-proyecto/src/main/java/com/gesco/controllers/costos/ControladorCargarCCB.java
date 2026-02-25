package com.gesco.controllers.costos;


import com.gesco.controllers.gestion_principal.DataBase;
import javax.swing.JOptionPane;

import com.gesco.models.costos.CCB;
import com.gesco.views.costos.VistaCargaCCB;

public class ControladorCargarCCB {

    private final VistaCargaCCB vista;
    private final Runnable onBack;

    public ControladorCargarCCB(VistaCargaCCB vista, Runnable onBack) {
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

        vista.getBtnSubirDatos().addActionListener(e -> guardar());
    }

    private void guardar() {
        CCB ccb;
        try {
            ccb = vista.crearCCB();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(vista, ex.getMessage(), "Datos invalidos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean ok = DataBase.guardarCcb(ccb);
        if (ok) {
            vista.setResultado(String.valueOf(ccb.getCcb()));
            JOptionPane.showMessageDialog(vista, "CCB guardado en ccb.txt.", "Guardado", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(vista, "No se pudo guardar ccb.txt", "Error de archivo", JOptionPane.ERROR_MESSAGE);
        }
    }
}





