package com.gesco.views.Registros;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import com.gesco.views.PlantillasViews.PlantillaGesco;

public class VistaHistorialFila extends PlantillaGesco {

    private JPanel contenedorVertical;
    private JLabel lblGananciaTotal;
    private double ganancias = 0.0;

    public VistaHistorialFila() {
        super();
        setImagenFondo("/FondoPrincipal2.png");
        ocultarIcono();
        ocultarlblSprAdmin();
        inicializarComponentes();
        construirCuerpo();
        this.revalidate();
        this.repaint();
        setVisible(true);
    }

    private void inicializarComponentes() {

        contenedorVertical = new JPanel();
        contenedorVertical.setLayout(new BoxLayout(contenedorVertical, BoxLayout.Y_AXIS));
        contenedorVertical.setOpaque(false);
        contenedorVertical.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        agregarEncabezado();
    }

    private void agregarEncabezado() {
        JLabel titulo = crearEtiquetaPersonalizada("HISTORIAL DE ASISTENCIA Y COBROS", "Times New Roman", Font.BOLD, 30, Color.WHITE, "centro");
        contenedorVertical.add(titulo);

        contenedorVertical.add(Box.createVerticalStrut(10));
        JSeparator separadorTitulo = new JSeparator();
        separadorTitulo.setMaximumSize(new Dimension(650, 2));
        separadorTitulo.setForeground(Color.WHITE);
        contenedorVertical.add(separadorTitulo);

        contenedorVertical.add(Box.createVerticalStrut(20));

        JPanel panelGanancias = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelGanancias.setOpaque(false);
        lblGananciaTotal = crearEtiquetaPersonalizada(formatearTextoGanancia(ganancias), "Segoe UI", Font.BOLD, 22, new Color(150, 255, 150), "centro");
        panelGanancias.add(lblGananciaTotal);
        panelGanancias.setMaximumSize(new Dimension(Integer.MAX_VALUE, panelGanancias.getPreferredSize().height));

        contenedorVertical.add(panelGanancias);
        contenedorVertical.add(Box.createVerticalStrut(4));

        JSeparator sepInferior = new JSeparator();
        sepInferior.setMaximumSize(new Dimension(550, 2));
        sepInferior.setForeground(new Color(173, 216, 230));
        contenedorVertical.add(sepInferior);

        contenedorVertical.add(Box.createVerticalStrut(14));
    }

    public void setGananciasTotales(double total) {
        ganancias = total;
        if (lblGananciaTotal != null) {
            lblGananciaTotal.setText(formatearTextoGanancia(ganancias));
            lblGananciaTotal.revalidate();
            lblGananciaTotal.repaint();
        }
    }

    public void mostrarMensajeVacio(String mensaje) {
        JLabel lblVacio = crearEtiquetaPersonalizada(
            mensaje,
            "Segoe UI", Font.ITALIC, 16,
            new Color(200, 200, 200), "centro"
        );
        contenedorVertical.add(lblVacio);
        contenedorVertical.revalidate();
        contenedorVertical.repaint();
    }

    private String formatearTextoGanancia(double monto) {
        return String.format(Locale.ROOT, "GANANCIAS TOTALES: %.2f Bs", monto);
    }

    public void agregarRegistroALista(String fecha, String tipoServicio, String ci, String tipoUsuario, double cobro) {
        JPanel bloque = new JPanel();
        bloque.setLayout(new BoxLayout(bloque, BoxLayout.Y_AXIS));
        bloque.setOpaque(false);
        bloque.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblEncabezado = crearEtiquetaPersonalizada(fecha + "  |  " + tipoServicio.toUpperCase(), "Segoe UI", Font.BOLD, 16, new Color(130, 180, 255), "izquierda");

        String detalle = "CI: " + ci + "  -  TIPO: " + tipoUsuario;
        JLabel lblDetalle = crearEtiquetaPersonalizada(detalle, "Segoe UI", Font.PLAIN, 14, Color.WHITE, "izquierda");

        JLabel lblCobro = crearEtiquetaPersonalizada(
            String.format(Locale.ROOT, "MONTO COBRADO: %.2f Bs", cobro),
            "Segoe UI",
            Font.BOLD,
            14,
            new Color(200, 200, 200),
            "izquierda"
        );

        bloque.add(lblEncabezado);
        bloque.add(Box.createVerticalStrut(5));
        bloque.add(lblDetalle);
        bloque.add(lblCobro);

        JSeparator separador = new JSeparator();
        separador.setMaximumSize(new Dimension(750, 1));
        separador.setForeground(new Color(80, 80, 80));

        bloque.add(Box.createVerticalStrut(15));
        bloque.add(separador);
        bloque.add(Box.createVerticalStrut(20));

        contenedorVertical.add(bloque);
        contenedorVertical.revalidate();
        contenedorVertical.repaint();
    }

    private void construirCuerpo() {
        JPanel panelGris = new JPanel(new BorderLayout());
        panelGris.setBackground(new Color(60, 60, 65));
        panelGris.setOpaque(true);
        panelGris.setPreferredSize(new Dimension(700, 600));
        panelGris.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));

        JScrollPane scroll = new JScrollPane(contenedorVertical);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(25);

        panelGris.add(scroll, BorderLayout.CENTER);

        JPanel capaCentro = new JPanel(new GridBagLayout());
        capaCentro.setOpaque(false);
        capaCentro.add(panelGris); 

        this.contenedorPrincipal.add(capaCentro, BorderLayout.CENTER);

    }
}