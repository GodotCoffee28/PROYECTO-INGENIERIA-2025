package com.gesco.controllers;

import com.gesco.views.VistaEditarMenu;
import com.gesco.models.Insumo;
import com.gesco.models.Menu;
import com.gesco.models.Platillo;
import java.util.List;

public class VistaEditarMenuControlador {

    private final VistaEditarMenu vista;
    private final Runnable onBack;
    public VistaEditarMenuControlador(VistaEditarMenu vista, Runnable onBack) {
        this.vista = vista;
        this.onBack = onBack;
    }

    public void conectar() {
        vista.setInsumos(DataBase.obtenerInsumos());
        vista.getBackIcon().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onBack.run();
            }
        });

        vista.getBtnEditar().addActionListener(e -> guardarEdicion());
    }

    private void guardarEdicion() {
        try {
            String dia = vista.getDia();
            String mes = vista.getMes();
            String anio = vista.getAnio();
            String fechaStr = String.format("%s-%02d-%02d",
                anio.trim(),
                Integer.parseInt(mes.trim()),
                Integer.parseInt(dia.trim()));

            java.time.LocalDate fecha = java.time.LocalDate.parse(fechaStr);
            if (!DataBase.esFechaValidaParaMenu(fecha)) {
                javax.swing.JOptionPane.showMessageDialog(vista,
                    "Solo se permiten días hábiles (lunes a viernes no feriados).",
                    "Fecha no permitida",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean noDisponible = vista.isMenuNoDisponibleSeleccionado();
            Menu menu = new Menu(fecha, noDisponible ? Menu.EstadoMenu.NO_DISPONIBLE : Menu.EstadoMenu.CON_MENU);
            Menu.TipoMenu tipoMenu = vista.getTipoMenu();
            if (tipoMenu != null) {
                menu.setTipoMenu(tipoMenu);
            }

            String p1 = vista.getPlatillo1();
            String p2 = vista.getPlatillo2();
            String p3 = vista.getPlatillo3();

            List<Insumo> insumos1 = vista.getInsumosPlatillo1();
            List<Insumo> insumos2 = vista.getInsumosPlatillo2();
            List<Insumo> insumos3 = vista.getInsumosPlatillo3();

            if (!noDisponible) {
                if (p1 != null && !p1.isBlank() && insumos1.isEmpty()) {
                    javax.swing.JOptionPane.showMessageDialog(vista,
                        "El platillo 1 debe tener al menos un insumo.",
                        "Platillo sin insumos",
                        javax.swing.JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (p2 != null && !p2.isBlank() && insumos2.isEmpty()) {
                    javax.swing.JOptionPane.showMessageDialog(vista,
                        "El platillo 2 debe tener al menos un insumo.",
                        "Platillo sin insumos",
                        javax.swing.JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (p3 != null && !p3.isBlank() && insumos3.isEmpty()) {
                    javax.swing.JOptionPane.showMessageDialog(vista,
                        "El platillo 3 debe tener al menos un insumo.",
                        "Platillo sin insumos",
                        javax.swing.JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            if (p1 != null && !p1.isBlank()) {
                Platillo platillo = new Platillo(p1.trim());
                agregarInsumos(platillo, insumos1);
                menu.agregarPlatillo(platillo);
            }
            if (p2 != null && !p2.isBlank()) {
                Platillo platillo = new Platillo(p2.trim());
                agregarInsumos(platillo, insumos2);
                menu.agregarPlatillo(platillo);
            }
            if (p3 != null && !p3.isBlank()) {
                Platillo platillo = new Platillo(p3.trim());
                agregarInsumos(platillo, insumos3);
                menu.agregarPlatillo(platillo);
            }

            if (!noDisponible && !menu.tienePlatillos()) {
                javax.swing.JOptionPane.showMessageDialog(vista,
                    "No se puede actualizar a menú vacío. Agregue un platillo o marque 'Menu no disponible'.",
                    "Menú inválido",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean ok = DataBase.actualizarMenu(menu);
            if (ok) {
                javax.swing.JOptionPane.showMessageDialog(vista,
                    noDisponible ? "Día actualizado a menú no disponible." : "Menú actualizado.",
                    "OK",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
                onBack.run();
            } else {
                javax.swing.JOptionPane.showMessageDialog(vista, "Error al actualizar menú.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(vista, "Fecha inválida. Use formato DD MM AAAA.", "Error", javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }

    private void agregarInsumos(Platillo platillo, List<Insumo> insumos) {
        if (insumos == null) return;
        for (Insumo insumo : insumos) {
            platillo.agregarInsumo(insumo);
        }
    }
}