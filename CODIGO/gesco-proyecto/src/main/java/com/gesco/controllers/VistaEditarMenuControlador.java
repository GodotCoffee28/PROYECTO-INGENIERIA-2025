package com.gesco.controllers;

import com.gesco.views.VistaEditarMenu;
import com.gesco.models.Menu;
import com.gesco.models.Platillo;

public class VistaEditarMenuControlador {

    private final VistaEditarMenu vista;
    private final Runnable onBack;
    public VistaEditarMenuControlador(VistaEditarMenu vista, Runnable onBack) {
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

            String p1 = vista.getPlatillo1();
            String p2 = vista.getPlatillo2();
            String p3 = vista.getPlatillo3();

            if (p1 != null && !p1.isBlank()) menu.agregarPlatillo(new Platillo(p1.trim()));
            if (p2 != null && !p2.isBlank()) menu.agregarPlatillo(new Platillo(p2.trim()));
            if (p3 != null && !p3.isBlank()) menu.agregarPlatillo(new Platillo(p3.trim()));

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
}