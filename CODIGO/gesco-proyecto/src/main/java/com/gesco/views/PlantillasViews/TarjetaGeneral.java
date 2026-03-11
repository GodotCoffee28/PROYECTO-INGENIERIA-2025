package com.gesco.views.PlantillasViews;

import java.awt.*;
import javax.swing.*;

public abstract class TarjetaGeneral extends JPanel {
    protected JPanel containerCabecera;
    protected JPanel containerCuerpo;

    private final Color colorBlanco = new Color(255, 255, 255); 

    public TarjetaGeneral(Color colorFondo, Color colorCabecera) {
        
        setLayout(new BorderLayout());
 
        setOpaque(false); 

        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(colorBlanco, 5, true), 
            BorderFactory.createEmptyBorder(0, 0, 0, 0) 
        ));

        containerCabecera = new JPanel(new BorderLayout());
        containerCabecera.setOpaque(false);
        containerCabecera.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 3, 0, colorBlanco),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        containerCuerpo = new JPanel();
        containerCuerpo.setLayout(new BoxLayout(containerCuerpo, BoxLayout.Y_AXIS));
        containerCuerpo.setOpaque(false);
        containerCuerpo.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        add(containerCabecera, BorderLayout.NORTH);
        add(containerCuerpo, BorderLayout.CENTER);
        
        construirContenido();
    }

    protected abstract void construirContenido();

    protected void setTituloEstilo(JLabel label) {
        label.setFont(new Font("Segoe UI", Font.BOLD, 17));
        label.setForeground(Color.WHITE); 
        label.setHorizontalAlignment(SwingConstants.CENTER);
    }

    protected void configurarLabelPlatillo(JLabel label) {
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(Color.WHITE); 
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
    }
}