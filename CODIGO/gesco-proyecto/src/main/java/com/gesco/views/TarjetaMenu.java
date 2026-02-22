package com.gesco.views;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.time.DayOfWeek;

import  javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.gesco.models.Menu;
import com.gesco.models.Platillo;
import com.gesco.views.PlantillasViews.TarjetaGeneral;


public class TarjetaMenu extends TarjetaGeneral {

    private final Menu menu;
    private JLabel lblDia;

    public TarjetaMenu(Menu menu) {
        super(new Color(230, 230, 230), new Color(180, 180, 185));
        this.menu = menu;
        
        actualizarInterfaz();
    }

    @Override
    protected final void construirContenido() {
        lblDia = new JLabel("", SwingConstants.CENTER);
        lblDia.setFont(new Font("Arial", Font.BOLD, 26));
        containerCabecera.add(lblDia, BorderLayout.CENTER);
        
    }

    private void actualizarInterfaz() {
        if (menu == null) return;
        containerCuerpo.removeAll();
        DayOfWeek dia = menu.getFecha().getDayOfWeek();
        String diaStr = switch(dia){
            case MONDAY    -> "Lunes";
            case TUESDAY   -> "Martes";
            case WEDNESDAY -> "Miércoles";
            case THURSDAY  -> "Jueves";
            case FRIDAY    -> "Viernes";
            case SATURDAY  -> "Sábado";
            case SUNDAY    -> "Domingo";
        };

        lblDia.setText(diaStr);

        if (menu.getEstado() == Menu.EstadoMenu.NO_DISPONIBLE) {
            JLabel lblNoDisponible = new JLabel("Menú no disponible");
            lblNoDisponible.setFont(new Font("Arial", Font.BOLD, 16));
            lblNoDisponible.setForeground(new Color(120, 45, 45));
            containerCuerpo.add(lblNoDisponible);
            revalidate();
            repaint();
            return;
        }

        if (!menu.tienePlatillos()) {
            JLabel lblVacio = new JLabel("Sin menú cargado");
            lblVacio.setFont(new Font("Arial", Font.BOLD, 16));
            lblVacio.setForeground(new Color(90, 90, 90));
            containerCuerpo.add(lblVacio);
            revalidate();
            repaint();
            return;
        }

        for (Platillo platillo : menu.getPlatillos()) {
            JLabel lblNombrePlatillo = new JLabel(" - " + platillo.getNombre().toUpperCase());
            lblNombrePlatillo.setFont(new Font("Arial", Font.PLAIN, 18));
            lblNombrePlatillo.setForeground(new Color(50, 50, 50));
            containerCuerpo.add(lblNombrePlatillo);
        }

        revalidate();
        repaint();
    }
}
