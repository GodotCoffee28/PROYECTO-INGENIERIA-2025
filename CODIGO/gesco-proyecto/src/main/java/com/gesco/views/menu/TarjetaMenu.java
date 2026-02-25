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

    private final Menu menuDesayuno;
    private final Menu menuAlmuerzo;
    private JLabel lblDia;

    public TarjetaMenu(Menu menu) {
        this(menu, null);
    }

    public TarjetaMenu(Menu menuDesayuno, Menu menuAlmuerzo) {
        super(new Color(255, 255, 255), new Color(255, 255, 255));
        this.menuDesayuno = menuDesayuno;
        this.menuAlmuerzo = menuAlmuerzo;
        
        actualizarInterfaz();
    }

    @Override
    protected final void construirContenido() {
        lblDia = new JLabel("", SwingConstants.CENTER);
        setTituloEstilo(lblDia); 
        containerCabecera.add(lblDia, BorderLayout.CENTER);
    }

    private void actualizarInterfaz() {
        containerCuerpo.removeAll();

        Menu menuBase = menuDesayuno != null ? menuDesayuno : menuAlmuerzo;
        if (menuBase == null) return;

        DayOfWeek dia = menuBase.getFecha().getDayOfWeek();
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

        renderMenuSection("Desayuno", menuDesayuno);
        renderMenuSection("Almuerzo", menuAlmuerzo);

        revalidate();
        repaint();
    }

    private void renderMenuSection(String titulo, Menu menu) {
        JLabel lblTipo = new JLabel(titulo);
        lblTipo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTipo.setForeground(Color.WHITE);
        containerCuerpo.add(lblTipo);

        if (menu == null || menu.getEstado() == Menu.EstadoMenu.NO_DISPONIBLE) {
            JLabel lblNoDisponible = new JLabel("MENÚ NO DISPONIBLE");
            configurarLabelPlatillo(lblNoDisponible);
            containerCuerpo.add(lblNoDisponible);
            return;
        }

        if (!menu.tienePlatillos()) {
            JLabel lblVacio = new JLabel("SIN MENÚ CARGADO");
            configurarLabelPlatillo(lblVacio);
            containerCuerpo.add(lblVacio);
            return;
        }

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
}

