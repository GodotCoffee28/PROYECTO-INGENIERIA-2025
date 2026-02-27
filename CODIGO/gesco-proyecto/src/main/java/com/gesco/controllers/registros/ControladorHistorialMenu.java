package com.gesco.controllers.registros;

import java.time.LocalDate;
import java.util.List;

import com.gesco.controllers.gestion_principal.DataBase;
import com.gesco.models.menu.Insumo;
import com.gesco.models.menu.Menu;
import com.gesco.models.menu.Platillo;
import com.gesco.views.Registros.VistaHistorialMenu;

public class ControladorHistorialMenu {

    private final VistaHistorialMenu vista;
    private final Runnable onBack;

    public ControladorHistorialMenu(VistaHistorialMenu vista, Runnable onBack) {
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
        List<LocalDate> diasHabiles = DataBase.obtenerUltimosCincoDiasHabiles(LocalDate.now());

        boolean hayDatos = false;

        for (LocalDate fecha : diasHabiles) {
            String fechaStr = fecha.toString();

            for (Menu.TipoMenu tipo : new Menu.TipoMenu[] { Menu.TipoMenu.DESAYUNO, Menu.TipoMenu.ALMUERZO }) {
                Menu menu = DataBase.obtenerMenuPorFechaYTipo(fechaStr, tipo);

                if (menu == null || menu.getEstado() == Menu.EstadoMenu.NO_DISPONIBLE || !menu.tienePlatillos()) {
                    continue;
                }

                List<Platillo> platillos = menu.getPlatillos();

                String p1 = "", ins1 = "-", cant1 = "-", costo1 = "-";
                String p2 = "", ins2 = "-", cant2 = "-", costo2 = "-";
                String p3 = "", ins3 = "-", cant3 = "-", costo3 = "-";

                if (platillos.size() >= 1) {
                    Platillo plat = platillos.get(0);
                    p1 = plat.getNombre();
                    if (!plat.getInsumos().isEmpty()) {
                        Insumo i = plat.getInsumos().get(0);
                        ins1 = i.getNombre();
                        cant1 = String.valueOf(i.getCantidad());
                        costo1 = String.format("%.2f", i.getCostoUnitario());
                    }
                }
                if (platillos.size() >= 2) {
                    Platillo plat = platillos.get(1);
                    p2 = plat.getNombre();
                    if (!plat.getInsumos().isEmpty()) {
                        Insumo i = plat.getInsumos().get(0);
                        ins2 = i.getNombre();
                        cant2 = String.valueOf(i.getCantidad());
                        costo2 = String.format("%.2f", i.getCostoUnitario());
                    }
                }
                if (platillos.size() >= 3) {
                    Platillo plat = platillos.get(2);
                    p3 = plat.getNombre();
                    if (!plat.getInsumos().isEmpty()) {
                        Insumo i = plat.getInsumos().get(0);
                        ins3 = i.getNombre();
                        cant3 = String.valueOf(i.getCantidad());
                        costo3 = String.format("%.2f", i.getCostoUnitario());
                    }
                }

                vista.agregarMenuALista(
                    fechaStr,
                    tipo.name(),
                    p1,
                    ins1,
                    cant1,
                    costo1,
                    p2,
                    ins2,
                    cant2,
                    costo2,
                    p3,
                    ins3,
                    cant3,
                    costo3
                );
                hayDatos = true;
            }
        }

        if (!hayDatos) {
            vista.mostrarMensajeVacio("No hay menús registrados para mostrar.");
        }
    }
}
