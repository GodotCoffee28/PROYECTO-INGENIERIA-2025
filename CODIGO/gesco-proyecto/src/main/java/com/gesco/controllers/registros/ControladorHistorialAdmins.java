package com.gesco.controllers.registros;

import com.gesco.views.Registros.VistaHistorialAdmins;
import com.gesco.controllers.gestion_principal.DataBase;
import java.util.List;

public class ControladorHistorialAdmins {

    private final VistaHistorialAdmins vista;
    private final Runnable onBack;

    public ControladorHistorialAdmins(VistaHistorialAdmins vista, Runnable onBack) {
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

        cargarDatos();
    }

    private void cargarDatos() {
        vista.limpiarHistorial();
        List<String> registros = DataBase.obtenerHistorialAdminsAutorizados();
        
        for (String linea : registros) {
            String[] partes = linea.split(":");
            if (partes.length >= 2) {
                vista.agregarRegistroAdmin(partes[0].trim(), partes[1].trim());
            } else if (partes.length == 1 && !partes[0].trim().isEmpty()) {
                vista.agregarRegistroAdmin(partes[0].trim(), "Sin código");
            }
        }
    }
}
