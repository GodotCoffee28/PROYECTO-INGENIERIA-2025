package com.gesco.views;

import java.awt.*;
import javax.swing.*;

import com.gesco.views.PlantillasViews.BotonNeon;
import com.gesco.views.PlantillasViews.PlantillaGesco;

public class VistaEspera extends PlantillaGesco {
    private BotonNeon volver;
    private JLabel anuncio;
    private ImageIcon taza;

    public VistaEspera() {
        super(); 
        inicializarComponentes();
        construirCuerpo();
        revalidate();
        repaint();
        setVisible(true);
    }

    private void inicializarComponentes() {
        Dimension tamBoton = new Dimension(400, 60);

        volver = new BotonNeon("Volver al inicio");
        volver.setPreferredSize(tamBoton);
        volver.setMaximumSize(tamBoton);
        volver.setAlignmentX(Component.CENTER_ALIGNMENT);

        anuncio = crearEtiquetaSimple("Estamos verificando sus datos, intente ingresar en breve.", 32, new Color(240, 240, 240));
        anuncio.setFont(new Font("Arial", Font.BOLD, 32));
        
        taza = new ImageIcon("Taza_VistaEspera.png");
        if (taza.getIconWidth() > 0) {
            // Taza gigante
            Image imagenEscalada = taza.getImage().getScaledInstance(280, 280, Image.SCALE_SMOOTH);
            taza = new ImageIcon(imagenEscalada);
        }
    }

    private void construirCuerpo() {
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setOpaque(false); 
        
        panelContenido.add(Box.createVerticalGlue()); 

        if (taza != null && taza.getIconWidth() > 0) {
            JLabel lblTaza = new JLabel(taza);
            lblTaza.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelContenido.add(lblTaza);
        }

        // --- EL AJUSTE CLAVE ---
        panelContenido.add(Box.createVerticalStrut(10)); // Reducido a 10 para acercar el texto
        
        panelContenido.add(anuncio);
        
        panelContenido.add(Box.createVerticalStrut(80)); // Espacio mayor antes del botón para equilibrar
        
        panelContenido.add(volver); 

        panelContenido.add(Box.createVerticalGlue());

        this.contenedorPrincipal.add(panelContenido, BorderLayout.CENTER);
    }

    public BotonNeon getVolver() {
        return volver;
    }
}