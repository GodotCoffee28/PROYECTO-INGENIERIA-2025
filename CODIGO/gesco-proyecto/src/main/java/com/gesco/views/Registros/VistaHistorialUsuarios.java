package com.gesco.views.Registros;

import javax.swing.*;
import java.awt.*;
import com.gesco.views.PlantillasViews.PlantillaGesco;

public class VistaHistorialUsuarios extends PlantillaGesco {

    private JPanel contenedorVertical;

    public VistaHistorialUsuarios() {
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
        if (contenedorVertical == null) {
            contenedorVertical = new JPanel();
            contenedorVertical.setLayout(new BoxLayout(contenedorVertical, BoxLayout.Y_AXIS));
            contenedorVertical.setOpaque(false);
            contenedorVertical.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        }
        agregarEncabezado();
    }

    private void agregarEncabezado() {
        JLabel titulo = crearEtiquetaPersonalizada("Usuarios Registrados", "Times New Roman", Font.BOLD, 35, Color.WHITE, "centro");
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
        panelGris.setPreferredSize(new Dimension(680, 600));
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

    public void agregarRegistroUsuario(String cedula, String nombre, String email, String tipo) {
        JPanel bloqueTexto = new JPanel();
        bloqueTexto.setLayout(new BoxLayout(bloqueTexto, BoxLayout.Y_AXIS));
        bloqueTexto.setOpaque(false);
        bloqueTexto.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblCedula = crearEtiquetaPersonalizada("CÉDULA: " + cedula, "Segoe UI", Font.BOLD, 18, new Color(130, 180, 255), "centro");
        JLabel lblNombre = crearEtiquetaPersonalizada("NOMBRE: " + nombre, "Segoe UI", Font.PLAIN, 14, new Color(220, 220, 220), "centro");
        JLabel lblEmail = crearEtiquetaPersonalizada("EMAIL: " + email, "Segoe UI", Font.ITALIC, 13, new Color(200, 200, 200), "centro");
        JLabel lblTipo = crearEtiquetaPersonalizada("TIPO: " + tipo, "Segoe UI", Font.ITALIC, 13, new Color(180, 230, 180), "centro");

        bloqueTexto.add(lblCedula);
        bloqueTexto.add(lblNombre);
        bloqueTexto.add(lblEmail);
        bloqueTexto.add(lblTipo);
        bloqueTexto.add(Box.createVerticalStrut(15));

        JSeparator separador = new JSeparator();
        separador.setMaximumSize(new Dimension(750, 1));
        separador.setForeground(new Color(150, 150, 150));
        separador.setBackground(new Color(150, 150, 150));

        bloqueTexto.add(Box.createVerticalStrut(10));
        bloqueTexto.add(separador);
        bloqueTexto.add(Box.createVerticalStrut(20));

        contenedorVertical.add(bloqueTexto);
        contenedorVertical.revalidate();
        contenedorVertical.repaint();
    }

    public void limpiarHistorial() {
        contenedorVertical.removeAll();
        agregarEncabezado();
        contenedorVertical.revalidate();
        contenedorVertical.repaint();
    }
}
