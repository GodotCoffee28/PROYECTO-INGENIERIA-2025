package com.gesco.views.PlantillasViews;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import  java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
public class MenuDesplegable {
    private final JPopupMenu  menuOpciones;
    private final JPopupMenu menuOpcionesUsuario;
    private final JPopupMenu menuOpcionesAdmin;

    public MenuDesplegable() {
        // Inicialización de las instancias
        menuOpciones = generarMenuBase();
        menuOpcionesUsuario = generarMenuBase();
        menuOpcionesAdmin = generarMenuBase();

        // Configuración de items específicos
        ponerItemSalir(menuOpciones);
        ponerItemSalir(menuOpcionesUsuario);
        
        ponerItemsAdmin(menuOpcionesAdmin);
        ponerItemSalir(menuOpcionesAdmin);
    }

    private JPopupMenu generarMenuBase() {
        JPopupMenu menu = new JPopupMenu();
        menu.setBackground(new Color(60, 60, 65));
        menu.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

        // Nota: "Ir a la Fila" se mantiene en el código (vista/controlador),
        // pero se elimina de la interfaz de usuario por ahora.
        String[] etiquetas = {
            "Ir a Inicio", "Ir a Inicio de sesión", "Ir a Registro",
            "Ver Menu de la semana", "Ver turnos"
        };

        String[] comandos = {
            "CMD_INICIO", "CMD_SESION", "CMD_REGISTRO",
            "CMD_MENUSEMANA", "CMD_TURNOS"
        };

        for (int i = 0; i < etiquetas.length; i++) {
            JMenuItem item = new JMenuItem(etiquetas[i]);
            item.setActionCommand(comandos[i]);
            diseñarItem(item);
            menu.add(item);
        }
        return menu;
    }

    private void diseñarItem(JMenuItem item) {
        item.setBackground(new Color(60, 60, 65));
        item.setForeground(Color.WHITE);
        item.setFont(new Font("Arial", Font.PLAIN, 14));
        item.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                item.setBackground(new Color(80, 80, 85));
                item.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                item.setBackground(new Color(60, 60, 65));
            }
        });
    }

    private void ponerItemsAdmin(JPopupMenu menuDestino) {
        String[] etiquetasAdmin = {
            "Ir a panel de control",
            "Ir a carga del CCB", "Ir a crear menu", 
            "Ir a editar menu", "Ir a gestion de menu"
        };
        String[] comandosAdmin = {
            "CMD_CONTROL",
            "CMD_CCB", "CMD_CREARMENU", "CMD_EDITARMENU", "CMD_GESTIONMENU"
        };

        for (int i = 0; i < etiquetasAdmin.length; i++) {
            JMenuItem item = new JMenuItem(etiquetasAdmin[i]);
            item.setActionCommand(comandosAdmin[i]);
            diseñarItem(item);
            menuDestino.add(item);
        }
    }

    private void ponerItemSalir(JPopupMenu jp) {
        jp.addSeparator(); // Separador visual
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.setActionCommand("CMD_SALIR");
        diseñarItem(itemSalir);
        jp.add(itemSalir);
    }

    // Getters
    public JPopupMenu getMenu() { return menuOpciones; }
    public JPopupMenu getMenuUsuario() { return menuOpcionesUsuario; }
    public JPopupMenu getMenuAdmin() { return menuOpcionesAdmin; }
}
