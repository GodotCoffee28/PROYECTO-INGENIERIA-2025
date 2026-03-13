package com.gesco.views.Registros;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import com.gesco.views.PlantillasViews.PlantillaGesco;

public class VistaHistorialFila extends PlantillaGesco {

    private JPanel contenedorVertical;
    private JLabel lblGananciaGeneral;
    private JLabel lblConteoGeneral;

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
        contenedorVertical.add(Box.createVerticalStrut(4));

        JSeparator sepInferior = new JSeparator();
        sepInferior.setMaximumSize(new Dimension(550, 2));
        sepInferior.setForeground(new Color(173, 216, 230));
        contenedorVertical.add(sepInferior);

        contenedorVertical.add(Box.createVerticalStrut(14));

        JPanel panelResumen = new JPanel();
        panelResumen.setLayout(new BoxLayout(panelResumen, BoxLayout.Y_AXIS));
        panelResumen.setOpaque(false);
        panelResumen.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblGananciaGeneral = crearEtiquetaPersonalizada("GANANCIA GENERAL: 0.00 Bs", "Segoe UI", Font.BOLD, 22, new Color(150, 255, 150), "centro");
        lblConteoGeneral = crearEtiquetaPersonalizada("Est: 0 | Bec: 0 | Exo: 0 | Emp: 0 | Prof: 0", "Segoe UI", Font.PLAIN, 15, new Color(200, 230, 255), "centro");

        panelResumen.add(lblGananciaGeneral);
        panelResumen.add(Box.createVerticalStrut(5));
        panelResumen.add(lblConteoGeneral);
        panelResumen.add(Box.createVerticalStrut(15));
        contenedorVertical.add(panelResumen);

    }

    public void mostrarMensajeVacio(String mensaje) {
        JLabel lblVacio = crearEtiquetaPersonalizada(mensaje,"Segoe UI", Font.ITALIC, 16,new Color(200, 200, 200), "centro");
        contenedorVertical.add(lblVacio);
        contenedorVertical.revalidate();
        contenedorVertical.repaint();
    }

    public void agregarRegistroALista(String fecha, String tipoServicio, String ci, String tipoUsuario, double cobro, double cobroTotal, int est, int estExo, int estBec, int emp, int prof) {
        
        JSeparator separador = new JSeparator();
        separador.setMaximumSize(new Dimension(750, 1));
        separador.setForeground(new Color(80, 80, 80));

        JPanel bloque = new JPanel();
        bloque.setLayout(new BoxLayout(bloque, BoxLayout.Y_AXIS));
        bloque.setOpaque(false);
        bloque.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblEncabezado = crearEtiquetaPersonalizada(fecha + "  |  " + tipoServicio.toUpperCase(), "Segoe UI", Font.BOLD, 16, new Color(130, 180, 255), "izquierda");

        String detalle = "CI: " + ci + "  -  ESTUDIANTE: " + tipoUsuario;
        JLabel lblDetalle = crearEtiquetaPersonalizada(detalle, "Segoe UI", Font.PLAIN, 14, Color.WHITE, "izquierda");

        JLabel lblCobro = crearEtiquetaPersonalizada(String.format(Locale.ROOT, "MONTO COBRADO: %.2f Bs", cobro),"Segoe UI",Font.BOLD,14,new Color(200, 200, 200),"izquierda");

        JLabel lblCobroTotal = crearEtiquetaPersonalizada(String.format(Locale.ROOT, "COBRO ACUMULADO DEL TURNO: %.2f Bs", cobroTotal),"Segoe UI",Font.BOLD,14,new Color(200, 200, 200),"izquierda");

        String resumenUsuarios = String.format("<html><b>USUARIOS QUE INGRESARON:</b><br>" +"Regulares: %d | Exonerados: %d | Becarios: %d | Empleados: %d | Profesores: %d</html>", est, estExo, estBec, emp, prof);

        JLabel lblTipos = crearEtiquetaPersonalizada(resumenUsuarios, "Segoe UI", Font.PLAIN, 13, new Color(220, 220, 220), "izquierda");

        bloque.add(lblEncabezado);
        bloque.add(Box.createVerticalStrut(5));
        bloque.add(lblDetalle);
        bloque.add(lblCobro);
        bloque.add(Box.createVerticalStrut(20));
        bloque.add(lblCobroTotal);
        bloque.add(Box.createVerticalStrut(5));
        bloque.add(lblTipos);
        bloque.add(Box.createVerticalStrut(5));
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
    public void actualizarSemana(double total, int est, int bec, int exo, int emp, int prof) {
        lblGananciaGeneral.setText(String.format(Locale.ROOT, "GANANCIA GENERAL: %.2f Bs", total));
        lblConteoGeneral.setText(String.format("Estudiantes: %d | Becarios: %d | Exonerados: %d | Empleados: %d | Profesores: %d", est, bec, exo, emp, prof));
    }

    public void agregarSeparadorTurno() {

    JPanel panelSeparador = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
    panelSeparador.setOpaque(false);
    panelSeparador.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

    JSeparator lineaIzq = new JSeparator();
    lineaIzq.setPreferredSize(new Dimension(150, 2));
    lineaIzq.setForeground(new Color(255, 200, 100));

    JLabel lblFin = new JLabel("FIN DE TURNO");
    lblFin.setFont(new Font("Segoe UI", Font.BOLD, 16));
    lblFin.setForeground(new Color(255, 200, 100)); 

    JSeparator lineaDer = new JSeparator();
    lineaDer.setPreferredSize(new Dimension(150, 2)); 
    lineaDer.setForeground(new Color(255, 200, 100));

    panelSeparador.add(lineaIzq);
    panelSeparador.add(lblFin);
    panelSeparador.add(lineaDer);

    contenedorVertical.add(panelSeparador);
}
}