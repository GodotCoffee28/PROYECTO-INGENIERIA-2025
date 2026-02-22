package com.gesco.views;

import java.awt.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import com.gesco.controllers.DataBase;
import com.gesco.models.Menu;
import com.gesco.views.PlantillasViews.PlantillaGesco;

public class VistaMenuSemana extends PlantillaGesco {
    
    private List<TarjetaMenu> tarjetasSemana;
    private JLabel titulo;
    private JPanel panelContenedorTarjetas;

    public VistaMenuSemana() {
        super();
        setImagenFondo("/FondoPrincipal.png");
        inicializarComponentes();
        construirCuerpo();
        
        
        cargarDatosReales();
        revalidate();
        repaint();
        setVisible(true);
    }

    private void inicializarComponentes() {
        titulo = crearEtiquetaPersonalizada("Menú de la semana", "Times New Roman", Font.BOLD, 24, Color.WHITE,"centro");
        titulo.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        tarjetasSemana = new ArrayList<>();
    }

    private void construirCuerpo() {
        JPanel panelFondo = crearPanel(100,20,200,40,50,50);

        panelFondo.setLayout(new BorderLayout());
        panelFondo.setOpaque(false);
        panelFondo.setBorder(BorderFactory.createEmptyBorder(40, 120, 40, 120));

        JPanel panelTitulo = new JPanel();
        panelTitulo.setLayout(new BoxLayout(panelTitulo, BoxLayout.Y_AXIS));
        panelTitulo.setOpaque(false);
        panelTitulo.add(titulo);
        panelTitulo.add(Box.createVerticalStrut(20));

        panelContenedorTarjetas = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20)) {
            

            @Override 
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                if (getParent() != null) {
                    d.width = getParent().getWidth(); 
                    
                    int nComponentes = getComponentCount();
                    if (nComponentes > 0 && d.width > 0) {
                        int anchoTarjeta = 200 + 20; 
                        int tarjetasPorFila = Math.max(1, (d.width - 20) / anchoTarjeta);
                        int filas = (int) Math.ceil((double) nComponentes / tarjetasPorFila);
                        int altoTarjeta = 180 + 20; 
                        d.height = filas * altoTarjeta + 40; 
                    }
                }
                return d;
            }
        };
        
        panelContenedorTarjetas.setOpaque(false);

        JScrollPane scroll = new JScrollPane(panelContenedorTarjetas);
        
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);

        panelFondo.add(panelTitulo, BorderLayout.NORTH);
        panelFondo.add(scroll, BorderLayout.CENTER);

        this.contenedorPrincipal.add(panelFondo, BorderLayout.CENTER);
    }
    
    private void cargarDatosReales() {
        LocalDate fechaInicio = LocalDate.now().with(java.time.DayOfWeek.MONDAY);

        tarjetasSemana.clear();
        panelContenedorTarjetas.removeAll();

        for (int i = 0; i < 5; i++) {
            LocalDate fechaDia = fechaInicio.plusDays(i);
            Menu menuDelDia = DataBase.obtenerMenuPorFecha(fechaDia.toString());

            TarjetaMenu tarjeta = new TarjetaMenu(menuDelDia);
            tarjeta.setPreferredSize(new Dimension(250, 160));

            tarjetasSemana.add(tarjeta);
            panelContenedorTarjetas.add(tarjeta);
        }

        panelContenedorTarjetas.revalidate();
        panelContenedorTarjetas.repaint();
    }
}