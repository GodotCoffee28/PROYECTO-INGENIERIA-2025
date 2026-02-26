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

        JLabel titulo = crearEtiquetaPersonalizada("Historial de menús", "Times New Roman", Font.BOLD, 35, Color.WHITE, "centro");
        contenedorVertical.add(titulo);
        contenedorVertical.add(Box.createVerticalStrut(10));

        JSeparator separadorTitulo = new JSeparator();
        separadorTitulo.setMaximumSize(new Dimension(500, 2));
        separadorTitulo.setForeground(Color.WHITE);
        separadorTitulo.setBackground(Color.WHITE); 
        
        contenedorVertical.add(separadorTitulo);
        contenedorVertical.add(Box.createVerticalStrut(25)); 
    }

    private void construirCuerpo() {
        JPanel panelGris = new JPanel(new BorderLayout());
        panelGris.setBackground(new Color(60, 60, 65));
        panelGris.setOpaque(true);
        panelGris.setPreferredSize(new Dimension(650, 600));
        
        panelGris.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));

        JScrollPane scroll = new JScrollPane(contenedorVertical);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(20);

        panelGris.add(scroll, BorderLayout.CENTER);

        JPanel capaCentro = new JPanel(new GridBagLayout());
        capaCentro.setOpaque(false);
        capaCentro.add(panelGris); 

        if (this.contenedorPrincipal != null) {
            this.contenedorPrincipal.add(capaCentro, BorderLayout.CENTER);
        }
    }

    public void agregarMenuALista(String fecha, String tipo, String platillo1, String insumo1, String cantidad1, String costo1, String platillo2, String insumo2, String cantidad2, String costo2,String platillo3, String insumo3, String cantidad3, String costo3) {
        
        JPanel bloqueTexto = new JPanel();
        bloqueTexto.setLayout(new BoxLayout(bloqueTexto, BoxLayout.Y_AXIS));
        bloqueTexto.setOpaque(false);
        bloqueTexto.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblFecha = crearEtiquetaPersonalizada("FECHA: " + fecha, "Segoe UI", Font.BOLD, 18, new Color(130, 180, 255), "centro");
        JLabel lblTipo = crearEtiquetaPersonalizada("[" + tipo.toUpperCase() + "]", "Segoe UI", Font.ITALIC, 14, new Color(200, 200, 200), "centro");
        
        bloqueTexto.add(lblFecha);
        bloqueTexto.add(lblTipo);
        bloqueTexto.add(Box.createVerticalStrut(15)); 

        bloqueTexto.add(crearSeccion("PLATILLO 1: " + platillo1, formatoInsumo(insumo1, cantidad1, costo1)));
        bloqueTexto.add(crearSeccion("PLATILLO 2: " + platillo2, formatoInsumo(insumo2, cantidad2, costo2)));
        bloqueTexto.add(crearSeccion("PLATILLO 3: " + platillo3, formatoInsumo(insumo3, cantidad3, costo3)));

        JSeparator separador = new JSeparator();
        separador.setMaximumSize(new Dimension(750, 1));
        separador.setForeground(new Color(150, 150, 150)); 
        separador.setBackground(new Color(150, 150, 150));
        
        bloqueTexto.add(Box.createVerticalStrut(20));
        bloqueTexto.add(separador);
        bloqueTexto.add(Box.createVerticalStrut(30));

        contenedorVertical.add(bloqueTexto);
        contenedorVertical.revalidate();
        contenedorVertical.repaint();
    }

    private String formatoInsumo(String ins, String cant, String costo) {
        return ins + "  |  Cant: " + cant + "  |  Costo Unit: " + costo + "$";
    }

    private JPanel crearSeccion(String titulo, String infoProcesada) {
        JPanel division = new JPanel(new BorderLayout());
        division.setOpaque(false);
        division.setMaximumSize(new Dimension(750, 75));

        JLabel tituloSeccion = crearEtiquetaPersonalizada(titulo.toUpperCase(), "Segoe UI", Font.BOLD, 13, Color.WHITE, "izquierda");

        JTextArea areaTexto = new JTextArea(infoProcesada);
        areaTexto.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        areaTexto.setForeground(new Color(180, 180, 180));
        areaTexto.setEditable(false);
        areaTexto.setOpaque(false);
        areaTexto.setBorder(BorderFactory.createEmptyBorder(2, 20, 10, 0));

        division.add(tituloSeccion, BorderLayout.NORTH);
        division.add(areaTexto, BorderLayout.CENTER);
        return division;
    }

    public void limpiarHistorial() {
        contenedorVertical.removeAll();
        inicializarComponentes(); 
        contenedorVertical.revalidate();
        contenedorVertical.repaint();
    }
}