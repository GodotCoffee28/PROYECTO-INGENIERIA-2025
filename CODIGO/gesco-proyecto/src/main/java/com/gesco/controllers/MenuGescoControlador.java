package com.gesco.controllers;

import java.awt.Component;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.gesco.views.PlantillasViews.PlantillaGesco;

public class MenuGescoControlador {

    private final MenuAcciones acciones;
    private final MenuComandosControlador comandosControlador;

    public MenuGescoControlador(MenuAcciones acciones) {
        this.acciones = acciones;
        this.comandosControlador = new MenuComandosControlador(acciones);
    }

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
                    comandosControlador.procesar(e.getActionCommand(), esAdmin);
                });
            }
        }
    }
}
