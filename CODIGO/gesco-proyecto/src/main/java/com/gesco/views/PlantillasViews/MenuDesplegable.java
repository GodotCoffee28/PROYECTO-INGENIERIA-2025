package com.gesco.views.PlantillasViews;

import java.awt.Color;
import  java.awt.Cursor;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
public class MenuDesplegable {
    private JPopupMenu menuOpciones, menuOpcionesUsuario,menuOpcionesAdmin;

    public MenuDesplegable() {
        menuOpciones = generarMenuBase();
        menuOpcionesUsuario = generarMenuBase();
        menuOpcionesAdmin = generarMenuBase();

        
        PonerItemSalir(menuOpciones);
        //menuOpciones ya tiene su cosito de salida
        PonerItemSalir(menuOpcionesUsuario);
        //a el de usuario se le pone
        PonerItemsAdmin(menuOpcionesAdmin);
        PonerItemSalir(menuOpcionesAdmin);
        //admin se le ponen sus items antes de poner el de salida
    }

    private JPopupMenu generarMenuBase() {
        JPopupMenu menu = new JPopupMenu(); 
        
        menu.setBackground(new Color(60, 60, 65));
        menu.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

        // Creamos los items comunes
        JMenuItem itemInicio = new JMenuItem("Ir a Inicio");
        JMenuItem itemInicioSesion = new JMenuItem("Ir a Inicio de sesión");
        JMenuItem itemRegistro = new JMenuItem("Ir a Registro");
        JMenuItem itemFila = new JMenuItem("Ir a la Fila");
        JMenuItem itemMenuSemana = new JMenuItem("Ver Menu de la semana");
        JMenuItem itemTurnos = new JMenuItem("Ver turnos");

        // Comandos
        itemInicio.setActionCommand("CMD_INICIO");
        itemInicioSesion.setActionCommand("CMD_SESION");
        itemRegistro.setActionCommand("CMD_REGISTRO");
        itemFila.setActionCommand("CMD_FILA");
        itemMenuSemana.setActionCommand("CMD_MENUSEMANA");
        itemTurnos.setActionCommand("CMD_TURNOS");

        // Diseño
        diseñarItem(itemInicio);
        diseñarItem(itemInicioSesion);
        diseñarItem(itemRegistro);
        diseñarItem(itemFila);
        diseñarItem(itemMenuSemana);
        diseñarItem(itemTurnos);

        // Agregamos al menú nuevo
        menu.add(itemInicio);
        menu.add(itemInicioSesion);
        menu.add(itemRegistro);
        menu.add(itemFila);
        menu.add(itemMenuSemana);
        menu.add(itemTurnos);

        return menu; // Devolvemos el menú "virgen" listo para usarse
    }

    private void diseñarItem(JMenuItem item) {
        item.setBackground(new Color(60, 60, 65)); 
        item.setForeground(Color.WHITE);           

        item.setFont(new Font("Arial", Font.PLAIN, 14));
        
        item.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        // Esto le da vida a la UI
        item.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                item.setBackground(new Color(80, 80, 85)); 
                item.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                item.setBackground(new Color(60, 60, 65));
            }
        });
    }
private void PonerItemsAdmin(JPopupMenu menuDestino){
        // Aquí agregas cosas SOLO al menú que te pasen (el de admin)
        
        // Ejemplo:
        // JMenuItem itemPanelAdmin = new JMenuItem("Panel Admin");
        // itemPanelAdmin.setActionCommand("CMD_ADMIN_PANEL");
        // diseñarItem(itemPanelAdmin);
        // menuDestino.add(itemPanelAdmin); 
    }
    private void PonerItemSalir(JPopupMenu Jp){
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.setActionCommand("CMD_SALIR");
        diseñarItem(itemSalir);
        JPopupMenu.Separator separador = new JPopupMenu.Separator();
        separador.setForeground(Color.WHITE);
        Jp.add(separador);
        Jp.add(itemSalir);
    }


    public JPopupMenu getMenuUsuario(){
        return menuOpcionesUsuario;
    }

    public JPopupMenu getMenuAdmin(){
        return menuOpcionesAdmin;
    }
    public JPopupMenu getMenu(){
        return menuOpciones;
    }
    
}
