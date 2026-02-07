package com.gesco.views.PlantillasViews;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public abstract class TarjetaGeneral extends JPanel{
    protected JPanel containerCabecera;
    protected JPanel containerCuerpo;


    public TarjetaGeneral(Color colorFondo, Color colorCabecera) {
        setLayout(new BorderLayout());
        setBackground(colorFondo);
        
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1, true),
            new EmptyBorder(5, 5, 5, 5)
        ));

        containerCabecera = new JPanel(new BorderLayout());
        containerCabecera.setBackground(colorCabecera);
        containerCabecera.setOpaque(true);

        containerCuerpo = new JPanel();
        containerCuerpo.setLayout(new BoxLayout(containerCuerpo, BoxLayout.Y_AXIS));
        containerCuerpo.setBackground(colorFondo);
        containerCuerpo.setOpaque(true);

        // Añadir al layout principal
        add(containerCabecera, BorderLayout.NORTH);
        add(containerCuerpo, BorderLayout.CENTER);
        
        // Llamada al método que las hijas deben implementar
        construirContenido();
    }

    protected abstract void construirContenido();

    // Métodos de utilidad para personalizar la estética desde las hijas
    protected void setTituloEstilo(JLabel label) {
        label.setFont(new Font("Arial", Font.BOLD, 26));
        label.setForeground(Color.DARK_GRAY);
        label.setBorder(new EmptyBorder(5, 10, 5, 10));
    }
}
