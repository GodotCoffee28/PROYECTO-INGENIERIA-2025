package com.gesco.views.PlantillasViews;

import java.awt.*;

import  java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;


public class MenuDesplegable {
    private final JPopupMenu  menuOpciones;
    private final JPopupMenu menuOpcionesUsuario;
    private final JPopupMenu menuOpcionesAdmin;


    private final Color colorFondo = new Color(30, 30, 35);  
    private final Color colorHover = new Color(50, 50, 55);    
    private final Color colorTexto = new Color(220, 220, 220); 
    private final Color colorAdmin = new Color(130, 180, 255);
    private final Color colorSalir = new Color(255, 100, 100); 

    public MenuDesplegable() {

        menuOpciones = generarMenuBase();
        menuOpcionesUsuario = generarMenuBase();
        menuOpcionesAdmin = generarMenuBase();

        añadirSeparador(menuOpcionesAdmin);
        agregarTituloSeccion(menuOpcionesAdmin, "Administrador");
        añadirSeparador(menuOpcionesAdmin);
        ponerItemsAdmin(menuOpcionesAdmin);

        ponerItemSalir(menuOpciones);
        ponerItemSalir(menuOpcionesUsuario);
        ponerItemSalir(menuOpcionesAdmin);
    }

    private JPopupMenu generarMenuBase() {
        JPopupMenu menu = new JPopupMenu();
        menu.setBackground(colorFondo);

        menu.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 65), 1));

        agregarTituloSeccion(menu, "Comensal");
        añadirSeparador(menu);

        String[][] datos = {
            {"Ir a Inicio", "CMD_INICIO"},
            {"Ir a Inicio de sesión", "CMD_SESION"},
            {"Ir a Registro", "CMD_REGISTRO"},
            {"Ver Menú de la semana", "CMD_MENUSEMANA"},
            {"Ver turnos", "CMD_TURNOS"}
        };

        for (String[] fila : datos) {
            JMenuItem item = new JMenuItem(fila[0]);
            item.setActionCommand(fila[1]);
            diseñarItem(item, colorTexto);
            menu.add(item);
        }
        return menu;
    }

    private void diseñarItem(JMenuItem item, Color colorTexto) {
        item.setBackground(colorFondo);
        item.setForeground(colorTexto);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        item.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 20));
        item.setOpaque(true);


        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                item.setBackground(colorHover);
                item.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                item.setBackground(colorFondo);
            }
            }
        );
    }
    private void agregarTituloSeccion(JPopupMenu menu, String texto) {
        JLabel titulo = new JLabel("  " + texto);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16)); 
        titulo.setForeground(new Color(110, 110, 115));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
        menu.add(titulo);
    }
    private void ponerItemsAdmin(JPopupMenu menuDestino) {
        String[][] datosAdmin = {
            {"Panel de Control", "CMD_CONTROL"},
            {"Carga del CCB", "CMD_CCB"},
            {"Crear Menú", "CMD_CREARMENU"},
            {"Editar Menú", "CMD_EDITARMENU"},
            {"Gestión de Menú", "CMD_GESTIONMENU"}
        };

        for (String[] fila : datosAdmin) {
            JMenuItem item = new JMenuItem(fila[0]);
            item.setActionCommand(fila[1]);
            diseñarItem(item, colorAdmin);
            menuDestino.add(item);
        }
    }
    private void añadirSeparador(JPopupMenu menu){
        JSeparator separador = new JSeparator(SwingConstants.HORIZONTAL);
        separador.setForeground(new Color(250, 250, 250));
        separador.setBackground(colorFondo);       
        menu.add(separador);
    }
    private void ponerItemSalir(JPopupMenu jp) {
        añadirSeparador(jp);
        JMenuItem itemSalir = new JMenuItem("Cerrar Sesión");
        itemSalir.setActionCommand("CMD_SALIR");
        diseñarItem(itemSalir, colorSalir);
        jp.add(itemSalir);
    }

    public JPopupMenu getMenu() { return menuOpciones; }
    public JPopupMenu getMenuUsuario() { return menuOpcionesUsuario; }
    public JPopupMenu getMenuAdmin() { return menuOpcionesAdmin; }
}
