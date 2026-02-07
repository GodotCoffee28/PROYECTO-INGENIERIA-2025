package com.gesco.controllers;

import java.awt.Component;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.gesco.views.PlantillasViews.PlantillaGesco;

public class MenuGescoControlador {

    public void conectar(PlantillaGesco vista, boolean esAdmin) {
        JPopupMenu menu = esAdmin
            ? vista.getPopupMenu().getMenuAdmin()
            : vista.getPopupMenu().getMenuUsuario();

        vista.getMenuIcon().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                menu.show(e.getComponent(), 0, e.getComponent().getHeight());
            }
        });

        for (Component comp : menu.getComponents()) {
            if (comp instanceof JMenuItem) {
                ((JMenuItem) comp).addActionListener(e -> {
                    procesarAccionMenu(e.getActionCommand());
                    if (esAdmin) {
                        procesarAccionMenuAdmin(e.getActionCommand());
                    }
                });
            }
        }
    }

    private void procesarAccionMenu(String comando) {
        switch (comando) {
            case "CMD_INICIO"     -> System.out.println("Inicio");
            case "CMD_SESION"     -> System.out.println("Sesion");
            case "CMD_REGISTRO"   -> System.out.println("Registro");
            case "CMD_FILA"       -> System.out.println("Fila");
            case "CMD_MENUSEMANA" -> System.out.println("Menu");
            case "CMD_TURNOS"     -> System.out.println("Turnos");
            case "CMD_SALIR"      -> System.exit(0);
            default -> System.out.println("Comando: " + comando);
        }
    }

    private void procesarAccionMenuAdmin(String comando) {
        switch (comando) {
            case "CMD_CCB"         -> System.out.println("CCB");
            case "CMD_CREARMENU"   -> System.out.println("Crear");
            case "CMD_EDITARMENU"  -> System.out.println("Editar");
            case "CMD_GESTIONMENU" -> System.out.println("Gestion");
        }
    }
}
