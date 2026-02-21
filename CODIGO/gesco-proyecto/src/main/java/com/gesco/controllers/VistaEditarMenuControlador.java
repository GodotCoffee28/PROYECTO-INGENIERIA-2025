package com.gesco.controllers;

import com.gesco.views.VistaEditarMenu;

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
            String fechaStr = String.format("%s-%s-%s", anio.trim(), mes.trim(), dia.trim());

            java.time.LocalDate fecha = java.time.LocalDate.parse(fechaStr);

            com.gesco.models.Menu menu = new com.gesco.models.Menu(fecha);
            String p1 = vista.getPlatillo1();
            String p2 = vista.getPlatillo2();
            String p3 = vista.getPlatillo3();
            if (p1 != null && !p1.isBlank()) menu.agregarPlatillo(new com.gesco.models.Platillo(p1.trim()));
            if (p2 != null && !p2.isBlank()) menu.agregarPlatillo(new com.gesco.models.Platillo(p2.trim()));
            if (p3 != null && !p3.isBlank()) menu.agregarPlatillo(new com.gesco.models.Platillo(p3.trim()));

            boolean ok = DataBase.actualizarMenu(menu);
            if (ok) {
                javax.swing.JOptionPane.showMessageDialog(vista, "Menú actualizado.", "OK", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                onBack.run();
            } else {
                javax.swing.JOptionPane.showMessageDialog(vista, "Error al actualizar menú.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(vista, "Fecha inválida. Use formato DD MM AAAA.", "Error", javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }
}
