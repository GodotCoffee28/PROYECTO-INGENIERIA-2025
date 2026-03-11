package com.gesco.views.menu;

import java.awt.*;

import java.time.DayOfWeek;

import javax.swing.Box;
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
        /*Dimension tamañoFijo = new Dimension(300, 600);
    
    // IMPORTANTE: Para que el Layout de la vista principal no la ignore
    this.setPreferredSize(tamañoFijo);
    this.setMinimumSize(tamañoFijo); // Esto evita que se encoja
    this.setMaximumSize(tamañoFijo); // Esto evita que se estire raro */
    
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

        boolean soloDesayuno = menuDesayuno != null && menuAlmuerzo == null;
        boolean soloAlmuerzo = menuDesayuno == null && menuAlmuerzo != null;

        if (soloDesayuno) {
            dibujarSeccionMenu("Desayuno", menuDesayuno);
        } 
        else if (soloAlmuerzo) {
            dibujarSeccionMenu("Almuerzo", menuAlmuerzo);
        } 
        else {
            dibujarSeccionMenu("Desayuno", menuDesayuno);
            dibujarSeccionMenu("Almuerzo", menuAlmuerzo);
        }

        revalidate();
        repaint();
    }

    private void dibujarSeccionMenu(String titulo, Menu menu) {
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

        for (Platillo platillo : menu.getPlatillos()) {
          String textoHtml = "<html><p style='margin: 0; padding: 0; color: white; font-family: Segoe UI;'>" +
                   "• " + platillo.getNombre().toUpperCase() + "</p></html>";
    
            JLabel lblNombrePlatillo = new JLabel(textoHtml);
            configurarLabelPlatillo(lblNombrePlatillo);
            
            containerCuerpo.add(lblNombrePlatillo);
            
            // CONTROL TOTAL: Si quieres un espacio mínimo, usa este Strut. 
            // Ponle 0 si quieres que estén pegaditos.
            containerCuerpo.add(Box.createVerticalStrut(2));
        }
    }
}

