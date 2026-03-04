package com.gesco.controllers.registros;

import com.gesco.views.Registros.VistaHistorialUsuarios;
import com.gesco.controllers.gestion_principal.DataBase;
import java.util.List;

public class ControladorHistorialUsuarios {

    private final VistaHistorialUsuarios vista;
    private final Runnable onBack;

    public ControladorHistorialUsuarios(VistaHistorialUsuarios vista, Runnable onBack) {
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
        List<String> lineas = DataBase.obtenerTodosLosUsuarios();

        for (String linea : lineas) {
            String lineaLimpia = linea.endsWith(";") ? linea.substring(0, linea.length() - 1) : linea;
            String[] partes = lineaLimpia.split(":");
            if (partes.length >= 5) {
                String cedula = partes[0].trim();
                String nombre = partes[2].trim();
                String email = partes[3].trim();
                String tipo = partes[4].trim();
                vista.agregarRegistroUsuario(cedula, nombre, email, tipo);
            } else if (partes.length >= 1 && !partes[0].trim().isEmpty()) {
                vista.agregarRegistroUsuario(partes[0].trim(), "—", "—", "—");
            }
        }
    }
}
