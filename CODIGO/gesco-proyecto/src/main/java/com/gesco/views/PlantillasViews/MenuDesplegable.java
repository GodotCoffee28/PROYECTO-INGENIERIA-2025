package com.gesco.views.PlantillasViews;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import com.gesco.controllers.gestion_principal.DataBase;
import com.gesco.models.usuarios.Usuario.TipoUsuario;


public class MenuDesplegable {
    private final JPopupMenu  menuOpciones;
    private final JPopupMenu menuOpcionesUsuario;
    private final JPopupMenu menuOpcionesAdmin;
    private final JPopupMenu menuSaldo;

    private final Color colorFondo = new Color(30, 30, 35);  
    private final Color colorHover = new Color(50, 50, 55);    
    private final Color colorTexto = new Color(220, 220, 220); 
    private final Color colorAdmin = new Color(130, 180, 255);
    private final Color colorSalir = new Color(255, 100, 100); 

    public MenuDesplegable() {

        menuOpciones = generarMenuBase();
        menuOpcionesUsuario = generarMenuBase();
        menuOpcionesAdmin = generarMenuBase();
        menuSaldo = generarMenuPerfil(null);

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
            {"Ver Menú de la semana", "CMD_MENUSEMANA"},
            {"Ver turnos", "CMD_TURNOS"},
            {"Historial de transacciones", "CMD_HISTORIAL_SALDO"},
            {"Ir a Fila", "CMD_FILA"},
            {"Recargar saldo", "CMD_RECARGAR_SALDO"}
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
            {"Gestión de Menú", "CMD_GESTIONMENU"},
            {"Agregar insumo", "CMD_AGREGAR_INSUMO"},
            {"Historial de menús", "CMD_HISTORIAL_MENU"},
            {"Historial de gestión CCB", "CMD_HISTORIAL_CCB"}
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
        JMenuItem itemSalir = new JMenuItem("Cerrar programa");
        itemSalir.setActionCommand("CMD_SALIR");
        diseñarItem(itemSalir, colorSalir);
        jp.add(itemSalir);
    }


    public JPopupMenu generarMenuPerfil(String cedulaSesion) {
        JPopupMenu menu = new JPopupMenu();
        menu.setBackground(colorFondo);
        menu.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 65), 1));

        agregarTituloSeccion(menu, "Mi Cuenta");

        String cedula = DataBase.normalizarCedula(cedulaSesion);
        boolean sinSesion = cedula == null || cedula.isBlank();

        String nombre = sinSesion ? "Sesion no iniciada" : DataBase.obtenerNombre(cedula);
        if (nombre == null || nombre.isBlank()) nombre = "Usuario";

        TipoUsuario tipo = sinSesion ? TipoUsuario.COMENSAL : DataBase.obtenerTipoUsuario(cedula);
        String tipoEtiqueta = tipo == null ? "Comensal" : tipo.toEtiqueta();

        double saldo = sinSesion ? 0.0 : DataBase.obtenerSaldo(cedula);
        String saldoTexto = String.format(Locale.ROOT, "%.2f", saldo);

        JMenuItem itemSaldo = new JMenuItem("Saldo: " + saldoTexto + " Bs.");
        diseñarItem(itemSaldo, colorAdmin); 
        itemSaldo.setEnabled(false); 
        menu.add(itemSaldo);

        añadirSeparador(menu);

        agregarTituloSeccion(menu, nombre);
        agregarTituloSeccion(menu, tipoEtiqueta);
    
        return menu;
    }
    public JPopupMenu getMenu() { return menuOpciones; }
    public JPopupMenu getMenuUsuario() { return menuOpcionesUsuario; }
    public JPopupMenu getMenuAdmin() { return menuOpcionesAdmin; }
    public JPopupMenu getMenuSaldo() { return menuSaldo; }
}
