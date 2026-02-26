package com.gesco.views.Registros;

import javax.swing.*;
import java.awt.*;
import com.gesco.views.PlantillasViews.PlantillaGesco;

public class VistaHistorialMenu extends PlantillaGesco {

    private JPanel contenedorVertical;

    public VistaHistorialMenu() {
        super();
        setImagenFondo("/FondoPrincipal2.png");
        ocultarIcono();
        inicializarComponentes();
        construirCuerpo();
        this.revalidate();
        this.repaint();
    }

    private void inicializarComponentes() {
        contenedorVertical = new JPanel();
        contenedorVertical.setLayout(new BoxLayout(contenedorVertical, BoxLayout.Y_AXIS));
        contenedorVertical.setOpaque(false);
        contenedorVertical.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titulo = crearEtiquetaPersonalizada("Historial de Menús", "Times New Roman", Font.BOLD, 35, Color.WHITE, "centro");

        contenedorVertical.add(titulo);
        contenedorVertical.add(Box.createVerticalStrut(25)); 
    }

    private void construirCuerpo() {
        JPanel panelGris = new JPanel(new BorderLayout());
        panelGris.setBackground(new Color(60, 60, 65));
        panelGris.setOpaque(true);
        panelGris.setPreferredSize(new Dimension(590, 600));
        
        panelGris.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));

        JScrollPane scroll = new JScrollPane(contenedorVertical);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(20);

        panelGris.add(scroll, BorderLayout.CENTER);

        JPanel capaCentradora = new JPanel(new GridBagLayout());
        capaCentradora.setOpaque(false);
        capaCentradora.add(panelGris); 

        if (this.contenedorPrincipal != null) {
            this.contenedorPrincipal.add(capaCentradora, BorderLayout.CENTER);
        }
    }

    public void agregarMenuALista(String fecha, String p1, String ins1, String p2, String ins2, String p3, String ins3) {
        JPanel bloqueTexto = new JPanel();
        bloqueTexto.setLayout(new BoxLayout(bloqueTexto, BoxLayout.Y_AXIS));
        bloqueTexto.setOpaque(false);
        bloqueTexto.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblFecha = crearEtiquetaPersonalizada("FECHA: " + fecha, "Segoe UI", Font.BOLD, 18, new Color(130, 180, 255), "centro");

        bloqueTexto.add(lblFecha);
        bloqueTexto.add(Box.createVerticalStrut(15)); 

        bloqueTexto.add(crearSeccion("PLATILLO 1: " + p1, ins1));
        bloqueTexto.add(crearSeccion("PLATILLO 2: " + p2, ins2));
        bloqueTexto.add(crearSeccion("PLATILLO 3: " + p3, ins3));

        JSeparator separador = new JSeparator();
        separador.setMaximumSize(new Dimension(750, 1));
        separador.setForeground(new Color(255, 255, 255, 50));
        
        bloqueTexto.add(Box.createVerticalStrut(20));
        bloqueTexto.add(separador);
        bloqueTexto.add(Box.createVerticalStrut(30));

        contenedorVertical.add(bloqueTexto);

        contenedorVertical.revalidate();
        contenedorVertical.repaint();
    }

    private JPanel crearSeccion(String titulo, String lista) {
        JPanel division = new JPanel(new BorderLayout());
        division.setOpaque(false);
        division.setMaximumSize(new Dimension(750, 70));

       JLabel tituloSeccion = crearEtiquetaPersonalizada(titulo.toUpperCase(), "Segoe UI", Font.BOLD, 13, Color.WHITE, "izquierda");

        JTextArea areaTexto = new JTextArea("Insumos: " + lista);
        areaTexto.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        areaTexto.setForeground(new Color(180, 180, 180));
        areaTexto.setLineWrap(true);
        areaTexto.setWrapStyleWord(true);
        areaTexto.setEditable(false);
        areaTexto.setOpaque(false);
        areaTexto.setBorder(BorderFactory.createEmptyBorder(2, 20, 10, 0));

        division.add(tituloSeccion, BorderLayout.NORTH);
        division.add(areaTexto, BorderLayout.CENTER);
        return division;
    }

    public void limpiarHistorial() {
    contenedorVertical.removeAll();

    JLabel titulo = crearEtiquetaPersonalizada("Historial de Menús", "Times New Roman", Font.BOLD, 35, Color.WHITE, "centro");
    contenedorVertical.add(titulo);
    contenedorVertical.add(Box.createVerticalStrut(25)); 

    contenedorVertical.revalidate();
    contenedorVertical.repaint();
    }
}
