package com.gesco.controllers;

import com.gesco.views.VistaCrearMenu;
import com.gesco.views.VistaEditarMenu;

public class VistaCrearMenuControlador {

    private final VistaCrearMenu vista;
    private final Runnable onBack;

    public VistaCrearMenuControlador(VistaCrearMenu vista, Runnable onBack) {
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
        vista.getBtnCrear().addActionListener(e -> crearMenu());
    }

    private void crearMenu() {
        try {
            String dia = vista.getDia();
            String mes = vista.getMes();
            String anio = vista.getAnio();
            String fechaStr = String.format("%s-%s-%s", anio.trim(), mes.trim(), dia.trim());

            java.time.LocalDate fecha = java.time.LocalDate.parse(fechaStr);

            if (DataBase.esDiaNoDisponible(fechaStr)) {
                javax.swing.JOptionPane.showMessageDialog(vista, "El día seleccionado es un día festivo o fin de semana.", "Día no laborable", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            com.gesco.models.Menu menu = new com.gesco.models.Menu(fecha);
            String p1 = vista.getPlatillo1();
            String p2 = vista.getPlatillo2();
            String p3 = vista.getPlatillo3();

            boolean todosVacios = (p1 == null || p1.isBlank()) && (p2 == null || p2.isBlank()) && (p3 == null || p3.isBlank());
            if (todosVacios) {
                int confirmacion = javax.swing.JOptionPane.showConfirmDialog(
                    vista,
                    "No ingresó ningún platillo. El día quedará marcado como \"Dia fuera de servicio\". ¿Desea continuar?",
                    "Menú vacío",
                    javax.swing.JOptionPane.YES_NO_OPTION,
                    javax.swing.JOptionPane.WARNING_MESSAGE
                );
                if (confirmacion != javax.swing.JOptionPane.YES_OPTION) return;
                menu.agregarPlatillo(new com.gesco.models.Platillo("Dia fuera de servicio"));
            } else {
                if (p1 != null && !p1.isBlank()) menu.agregarPlatillo(new com.gesco.models.Platillo(p1.trim()));
                if (p2 != null && !p2.isBlank()) menu.agregarPlatillo(new com.gesco.models.Platillo(p2.trim()));
                if (p3 != null && !p3.isBlank()) menu.agregarPlatillo(new com.gesco.models.Platillo(p3.trim()));
            }

            boolean ok = DataBase.actualizarMenu(menu);
            if (ok) {
                javax.swing.JOptionPane.showMessageDialog(vista, "Menú creado.", "OK", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                onBack.run();
            } else {
                javax.swing.JOptionPane.showMessageDialog(vista, "Error al crear menú.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(vista, "Fecha inválida. Use formato DD MM AAAA.", "Error", javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }
}