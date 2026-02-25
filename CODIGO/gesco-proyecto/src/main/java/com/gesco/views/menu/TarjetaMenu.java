package com.gesco.views.menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.time.DayOfWeek;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.gesco.models.menu.Menu;
import com.gesco.models.menu.Platillo;
import com.gesco.views.PlantillasViews.TarjetaGeneral;

public class TarjetaMenu extends TarjetaGeneral {

    private final Menu menu;
    private JLabel lblDia;
    private JLabel lblTipo;

    public TarjetaMenu(Menu menu) {
        super(new Color(255, 255, 255), new Color(255, 255, 255));
        this.menu = menu;
        
        actualizarInterfaz();
    }

    @Override
    protected final void construirContenido() {
        lblDia = new JLabel("", SwingConstants.CENTER);
        setTituloEstilo(lblDia); 
        containerCabecera.add(lblDia, BorderLayout.CENTER);
        
        lblTipo = new JLabel("", SwingConstants.CENTER);
        lblTipo.setFont(new Font("Arial", Font.PLAIN, 14));
        lblTipo.setForeground(Color.WHITE); 
        containerCabecera.add(lblTipo, BorderLayout.SOUTH);
    }

    private void actualizarInterfaz() {
        if (menu == null) return;
        containerCuerpo.removeAll();

        DayOfWeek dia = menu.getFecha().getDayOfWeek();
        String diaStr = switch(dia){
            case MONDAY    -> "LUNES";
            case TUESDAY   -> "MARTES";
            case WEDNESDAY -> "MIÉRCOLES";
            case THURSDAY  -> "JUEVES";
            case FRIDAY    -> "VIERNES";
            case SATURDAY  -> "SÁBADO";
            case SUNDAY    -> "DOMINGO";
        };

        lblDia.setText(diaStr);
        lblTipo.setText(formatearTipoMenu(menu.getTipoMenu()));

        if (menu.getEstado() == Menu.EstadoMenu.NO_DISPONIBLE) {
            JLabel lblNoDisponible = new JLabel("MENÚ NO DISPONIBLE");
            configurarLabelPlatillo(lblNoDisponible);
            containerCuerpo.add(lblNoDisponible);
        } 
        else if (!menu.tienePlatillos()) {
            JLabel lblVacio = new JLabel("SIN MENÚ CARGADO");
            configurarLabelPlatillo(lblVacio); 
            containerCuerpo.add(lblVacio);
        } 
        else {
            JLabel lblCosto = new JLabel(String.format("Costo: $%.2f", menu.getCostoMenu()));
            lblCosto.setFont(new Font("Arial", Font.PLAIN, 16));
            lblCosto.setForeground(Color.WHITE);
            containerCuerpo.add(lblCosto);

            for (Platillo platillo : menu.getPlatillos()) {
                JLabel lblNombrePlatillo = new JLabel(" • " + platillo.getNombre().toUpperCase());
                configurarLabelPlatillo(lblNombrePlatillo); 
                containerCuerpo.add(lblNombrePlatillo);
            }
        }

        revalidate();
        repaint();
    }

    private String formatearTipoMenu(Menu.TipoMenu tipoMenu) {
        if (tipoMenu == null || tipoMenu == Menu.TipoMenu.NO_DEFINIDO) {
            return "";
        }
        return switch (tipoMenu) {
            case DESAYUNO -> "Desayuno";
            case ALMUERZO -> "Almuerzo";
            case NO_DEFINIDO -> "";
        };
    }
}

