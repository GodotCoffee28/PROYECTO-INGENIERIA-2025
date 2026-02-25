package com.gesco.views.costos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.gesco.models.costos.CCB;
import com.gesco.views.PlantillasViews.BotonNeon;
import com.gesco.views.PlantillasViews.PlantillaGesco;

public class VistaVerCCB extends PlantillaGesco {
    private JLabel lblFecha;
    private JLabel lblUsuario;
    private JLabel lblNb;
    private JLabel lblMerma;
    private JLabel lblCf;
    private JLabel lblCv;
    private JLabel lblCcb;
    private BotonNeon btnRefrescar;

    public VistaVerCCB() {
        super();
        setImagenFondo("/FondoPrincipal2.png");
        inicializar();
        construirCuerpo();
        revalidate();
        repaint();
        setVisible(true);
    }

    private void inicializar() {
        lblFecha = crearEtiquetaPersonalizada("Fecha: -", "Times New Roman", Font.PLAIN, 18, Color.WHITE, "centro");
        lblFecha.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblUsuario = crearEtiquetaPersonalizada("Usuario: -", "Times New Roman", Font.PLAIN, 18, Color.WHITE, "centro");
        lblUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblNb = crearEtiquetaPersonalizada("NB: -", "Times New Roman", Font.PLAIN, 18, Color.WHITE, "centro");
        lblNb.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblMerma = crearEtiquetaPersonalizada("Merma: -", "Times New Roman", Font.PLAIN, 18, Color.WHITE, "centro");
        lblMerma.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblCf = crearEtiquetaPersonalizada("CF: -", "Times New Roman", Font.PLAIN, 18, Color.WHITE, "centro");
        lblCf.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblCv = crearEtiquetaPersonalizada("CV: -", "Times New Roman", Font.PLAIN, 18, Color.WHITE, "centro");
        lblCv.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblCcb = crearEtiquetaPersonalizada("CCB: -", "Times New Roman", Font.PLAIN, 18, Color.WHITE, "centro");
        lblCcb.setAlignmentX(Component.CENTER_ALIGNMENT);

        Dimension tam = new Dimension(400, 60);
        btnRefrescar = new BotonNeon("Refrescar");
        btnRefrescar.setPreferredSize(tam);
        btnRefrescar.setMaximumSize(tam);
        btnRefrescar.setAlignmentX(Component.CENTER_ALIGNMENT);

    }

    private void construirCuerpo() {
        JPanel panelFondo = crearPanel(250,10,500,20,50,50);

        panelFondo.setLayout(new BoxLayout(panelFondo, BoxLayout.Y_AXIS));
        panelFondo.setOpaque(false);
        panelFondo.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));

        panelFondo.add(Box.createVerticalGlue());
        panelFondo.add(crearEtiquetaPersonalizada("Datos actuales de CCB", "Times New Roman", Font.BOLD, 22, Color.WHITE, "centro"));
        panelFondo.add(Box.createVerticalStrut(20));
        panelFondo.add(lblFecha);
        panelFondo.add(Box.createVerticalStrut(8));
        panelFondo.add(lblUsuario);
        panelFondo.add(Box.createVerticalStrut(8));
        panelFondo.add(lblNb);
        panelFondo.add(Box.createVerticalStrut(8));
        panelFondo.add(lblMerma);
        panelFondo.add(Box.createVerticalStrut(8));
        panelFondo.add(lblCf);
        panelFondo.add(Box.createVerticalStrut(8));
        panelFondo.add(lblCv);
        panelFondo.add(Box.createVerticalStrut(8));
        panelFondo.add(lblCcb);
        panelFondo.add(Box.createVerticalStrut(20));
        panelFondo.add(btnRefrescar);
        panelFondo.add(Box.createVerticalGlue());

        this.contenedorPrincipal.add(panelFondo, BorderLayout.CENTER);
    }

    public void setCf(double cf) { lblCf.setText("CF: " + String.format("%.2f", cf)); }
    public void setCv(double cv) { lblCv.setText("CV: " + String.format("%.2f", cv)); }
    public void setNb(double nb) { lblNb.setText("NB: " + String.format("%.2f", nb)); }
    public void setMerma(double merma) { lblMerma.setText("Merma: " + String.format("%.4f", merma)); }
    public void setUsuario(String usuario) { lblUsuario.setText("Usuario: " + (usuario == null || usuario.isBlank() ? "-" : usuario)); }
    public void setFecha(String fecha) { lblFecha.setText("Fecha: " + (fecha == null || fecha.isBlank() ? "-" : fecha)); }
    public void setCcb(double ccb) { lblCcb.setText("CCB: " + String.format("%.4f", ccb)); }
    public JButton getBtnRefrescar() { return btnRefrescar; }

    public void setCCB(CCB ccb) {
        if (ccb == null) {
            setFecha("-");
            setUsuario("-");
            setNb(0);
            setMerma(0);
            setCf(0);
            setCv(0);
            setCcb(0);
            return;
        }
        setFecha(ccb.getFecha().toString());
        setUsuario(ccb.getTipoUsuario());
        setNb(ccb.getNb());
        setMerma(ccb.getMerma());
        setCf(ccb.getCf());
        setCv(ccb.getCv());
        setCcb(ccb.getCcb());
    }
}


