package com.gesco.controllers.registros;

import java.util.List;

import com.gesco.controllers.gestion_principal.DataBase;
import com.gesco.views.Registros.VistaHistorialSaldo;

public class ControladorHistorialSaldo {

    private final VistaHistorialSaldo vista;
    private final Runnable onBack;

    public ControladorHistorialSaldo(VistaHistorialSaldo vista, Runnable onBack) {
        this.vista = vista;
        this.onBack = onBack;
    }

    public void conectar(String cedulaSesion) {
        vista.getBackIcon().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onBack.run();
            }
        });

        cargar(cedulaSesion);
    }

    public void cargar(String cedulaSesion) {
        vista.limpiarHistorial();
        List<String[]> recargas = DataBase.obtenerRecargasPorCedula(cedulaSesion);

        for (int i = recargas.size() - 1; i >= 0; i--) {
            String[] recarga = recargas.get(i);
            String referencia = recarga[0];
            String monto = recarga[1];
            String banco = recarga[2];
            String fecha = recarga[3];
            String cedula = recarga[4];
            vista.agregarTransaccionALista(fecha, referencia, monto, banco, cedula);
        }
    }
}
