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

    public ControladorHistorialMenu(VistaHistorialMenu vista) {
        this.vista = vista;
    }

    public void conectar() {
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
                        Insumo insumo = plat.getInsumos().get(0);
                        ins1 = insumo.getNombre();
                        cant1 = String.valueOf(insumo.getCantidad());
                        costo1 = String.format("%.2f", insumo.getCostoUnitario());
                    }
                }

                if (platillos.size() >= 2) {
                    Platillo plat = platillos.get(1);
                    p2 = plat.getNombre();
                    if (!plat.getInsumos().isEmpty()) {
                        Insumo insumo = plat.getInsumos().get(0);
                        ins2 = insumo.getNombre();
                        cant2 = String.valueOf(insumo.getCantidad());
                        costo2 = String.format("%.2f", insumo.getCostoUnitario());
                    }
                }

                if (platillos.size() >= 3) {
                    Platillo plat = platillos.get(2);
                    p3 = plat.getNombre();
                    if (!plat.getInsumos().isEmpty()) {
                        Insumo insumo = plat.getInsumos().get(0);
                        ins3 = insumo.getNombre();
                        cant3 = String.valueOf(insumo.getCantidad());
                        costo3 = String.format("%.2f", insumo.getCostoUnitario());
                    }
                }

                vista.agregarMenuALista(
                    fechaStr, tipo.name(),
                    p1, ins1, cant1, costo1,
                    p2, ins2, cant2, costo2,
                    p3, ins3, cant3, costo3
                );
                hayDatos = true;
            }
        }

        if (!hayDatos) {
            vista.mostrarMensajeSinDatos();
        }
    }
}