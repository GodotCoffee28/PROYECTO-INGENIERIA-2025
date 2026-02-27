package com.gesco.views.Registros;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import com.gesco.models.costos.CCB;
import com.gesco.views.PlantillasViews.PlantillaGesco;

public class VistaHistorialCCB extends PlantillaGesco {

    private JPanel contenedorVertical;

    public VistaHistorialCCB() {
        super();
        setImagenFondo("/FondoPrincipal2.png");
        ocultarIcono();
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

        JLabel titulo = crearEtiquetaPersonalizada("Historial de Gestión CCB", "Times New Roman", Font.BOLD, 35, Color.WHITE, "centro");
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

    public void agregarRegistroCCB(CCB registro) {
        if (registro == null) return;
        String fecha = registro.getFecha().toString();
        String tipoUsuario = registro.getTipoUsuario();
        String cf = String.format(Locale.US, "%.2f", registro.getCf());
        String cv = String.format(Locale.US, "%.2f", registro.getCv());
        String nb = String.format(Locale.US, "%.2f", registro.getNb());
        String merma = String.format(Locale.US, "%.4f", registro.getMerma());
        String ccb = String.format(Locale.US, "%.4f", registro.getCcb());

        JPanel bloqueTexto = new JPanel();
        bloqueTexto.setLayout(new BoxLayout(bloqueTexto, BoxLayout.Y_AXIS));
        bloqueTexto.setOpaque(false);
        bloqueTexto.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblFecha = crearEtiquetaPersonalizada("FECHA: " + fecha, "Segoe UI", Font.BOLD, 16, new Color(130, 180, 255), "centro");
        String tipo = (tipoUsuario == null) ? "" : tipoUsuario.toUpperCase();
        JLabel lblUser = crearEtiquetaPersonalizada("TIPO DE USUARIO: " + tipo, "Segoe UI", Font.ITALIC, 13, new Color(200, 200, 200), "centro");
        
        bloqueTexto.add(lblFecha);
        bloqueTexto.add(lblUser);
        bloqueTexto.add(Box.createVerticalStrut(15)); 

        bloqueTexto.add(crearFilaDetalle("Costos Fijos (CF):", cf + " BS."));
        bloqueTexto.add(crearFilaDetalle("Costos Variables (CV):", cv + " BS."));
        bloqueTexto.add(crearFilaDetalle("Número de Bandejas (NB):", nb));
        bloqueTexto.add(crearFilaDetalle("Merma:", merma));

        bloqueTexto.add(crearFilaDetalle("COSTO COMEDOR/BANDEJA (CCB):", ccb + " BS."));

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

        JLabel lblTitulo = crearEtiquetaPersonalizada(etiqueta, "Segoe UI", Font.BOLD, 12, Color.WHITE, "izquierda");

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
        inicializarComponentes(); 
        contenedorVertical.revalidate();
        contenedorVertical.repaint();
    }
}