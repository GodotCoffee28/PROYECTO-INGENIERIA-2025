package com.gesco.views.Registros;

import javax.swing.*;
import java.awt.*;
import com.gesco.views.PlantillasViews.PlantillaGesco;

public class VistaHistorialSaldo extends PlantillaGesco {

    private JPanel contenedorVertical;

    public VistaHistorialSaldo() {
        super();
        setImagenFondo("/FondoPrincipal2.png");
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

        JLabel titulo = crearEtiquetaPersonalizada("HISTORIAL DE TRANSACCIONES", "Times New Roman", Font.BOLD, 35, Color.WHITE, "centro");
        contenedorVertical.add(titulo);
        contenedorVertical.add(Box.createVerticalStrut(10));

        JSeparator separadorTitulo = new JSeparator();
        separadorTitulo.setMaximumSize(new Dimension(500, 2)); 
        separadorTitulo.setForeground(Color.WHITE);
        separadorTitulo.setBackground(Color.WHITE); 
        
        contenedorVertical.add(separadorTitulo);
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

    public void agregarTransaccionALista(String fecha, String referencia, String monto, String banco, String cedula) {
        JPanel bloqueTexto = new JPanel();
        bloqueTexto.setLayout(new BoxLayout(bloqueTexto, BoxLayout.Y_AXIS));
        bloqueTexto.setOpaque(false);
        bloqueTexto.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblFecha = crearEtiquetaPersonalizada("FECHA: " + fecha, "Segoe UI", Font.BOLD, 16, new Color(130, 180, 255), "centro");
        bloqueTexto.add(lblFecha);
        bloqueTexto.add(Box.createVerticalStrut(15)); 

        bloqueTexto.add(crearFilaDetalle("Referencia:", referencia));
        bloqueTexto.add(crearFilaDetalle("Monto Recarga:", monto + " BS."));
        bloqueTexto.add(crearFilaDetalle("Banco:", banco));
        bloqueTexto.add(crearFilaDetalle("Cédula Cliente:", cedula));

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

    private JPanel crearFilaDetalle(String etiqueta, String valor) {
        JPanel division = new JPanel(new BorderLayout());
        division.setOpaque(false);
        division.setMaximumSize(new Dimension(750, 45));

        JLabel lblTitulo = crearEtiquetaPersonalizada(etiqueta.toUpperCase(), "Segoe UI", Font.BOLD, 12, Color.WHITE, "izquierda");

        JTextArea txtValor = new JTextArea(valor);
        txtValor.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtValor.setForeground(new Color(200, 200, 200));
        txtValor.setLineWrap(true);
        txtValor.setWrapStyleWord(true);
        txtValor.setEditable(false);
        txtValor.setOpaque(false);
        txtValor.setBorder(BorderFactory.createEmptyBorder(2, 15, 5, 0));

        division.add(lblTitulo, BorderLayout.NORTH);
        division.add(txtValor, BorderLayout.CENTER);
        
        return division;
    }

    public void limpiarHistorial() {
        contenedorVertical.removeAll();
        JLabel titulo = crearEtiquetaPersonalizada("Historial de transacciones", "Times New Roman", Font.BOLD, 35, Color.WHITE, "centro");
        contenedorVertical.add(titulo);
        contenedorVertical.add(Box.createVerticalStrut(25)); 
        contenedorVertical.revalidate();
        contenedorVertical.repaint();
    }

    public void mostrarMensajeSinMovimientos(String mensaje) {
        JLabel lblMensaje = crearEtiquetaPersonalizada(
            mensaje,
            "Segoe UI",
            Font.PLAIN,
            18,
            new Color(220, 220, 220),
            "centro"
        );

        contenedorVertical.add(Box.createVerticalStrut(10));
        contenedorVertical.add(lblMensaje);
        contenedorVertical.revalidate();
        contenedorVertical.repaint();
    }
}