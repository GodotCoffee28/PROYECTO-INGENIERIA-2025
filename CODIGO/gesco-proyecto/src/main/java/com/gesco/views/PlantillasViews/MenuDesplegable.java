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

        String[] etiquetas = {
            "Ir a Inicio", 
            "Ir a Inicio de sesión", 
            "Ir a Registro", 
            "Ir a la Fila", 
            "Ver Menu de la semana", 
            "Ver turnos"
        };

        String[] comandos = {
            "CMD_INICIO", 
            "CMD_SESION", 
            "CMD_REGISTRO", 
            "CMD_FILA", 
            "CMD_MENUSEMANA", 
            "CMD_TURNOS"
        };

        // 2. Iteramos una sola vez para crear, configurar y agregar todo
        for (int i = 0; i < etiquetas.length; i++) {
            // Creación
            JMenuItem item = new JMenuItem(etiquetas[i]);
            
            // Configuración
            item.setActionCommand(comandos[i]);
            
            // Diseño (Estilo)
            diseñarItem(item);
            
            // Agregado al menú
            menu.add(item);
        }

        return menu; 
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

        String etiquetasAdmin[] = {
        "Ir a carga del CCB ",
        "Ir a crear menu",
        "Ir a editar menu",
        "Ir a gestion de menu"
        };
        String comandosAdmin[] = {
        "CMD_CCB",
        "CMD_CREARMENU",
        "CMD_EDITARMENU",
        "CMD_GESTIONMENU"
        };

        for (int i= 0; i < etiquetasAdmin.length; i++) {
            JMenuItem item = new JMenuItem(etiquetasAdmin[i]);
            item.setActionCommand(comandosAdmin[i]);
            diseñarItem(item);
            menuDestino.add(item);
        }



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
