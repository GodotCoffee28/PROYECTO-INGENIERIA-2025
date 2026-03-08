package com.gesco.views.otros;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.gesco.models.usuarios.Usuario.TipoUsuario;
import com.gesco.views.PlantillasViews.BotonNeon;
import com.gesco.views.PlantillasViews.PlantillaGesco;

public class VistaCambiarTipoEstudiante  extends PlantillaGesco {
    private BotonNeon btnCambiar;
    private JLabel titulo;
    private JTextField ciField;
    private JComboBox<String> comboTipoEstudiante;

    
    public VistaCambiarTipoEstudiante() {
        super();
        setImagenFondo("/FondoPrincipal2.png");
        ocultarIcono();
        ocultarlblSprAdmin();
        inicializarComponentes();
        construirCuerpo();
        revalidate();
        repaint();
        setVisible(true);
    }


    private void inicializarComponentes() {
        titulo = crearEtiquetaPersonalizada("Cambiar tipo de usuario", "Times New Roman", Font.BOLD, 45, new Color(240, 240, 240), "centro");
        titulo.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        Dimension tamBoton = new Dimension(400, 60);
        btnCambiar = new BotonNeon("Cambiar tipo");
        btnCambiar.setPreferredSize(tamBoton);
        btnCambiar.setMaximumSize(tamBoton);
        btnCambiar.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
    

    private void agregarCampos(JPanel panelFondoBase) {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setMaximumSize(new Dimension(450, 220));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        Dimension tamCaja = new Dimension(450, 40);

        formPanel.add(crearEtiquetaPersonalizada("Cédula", "Times New Roman", Font.PLAIN, 19, Color.WHITE, "izquierda"));
        formPanel.add(Box.createVerticalStrut(8));
        ciField = new JTextField();
        diseñarCaja(ciField, tamCaja);
        formPanel.add(ciField);
        formPanel.add(Box.createVerticalStrut(16));

        formPanel.add(crearEtiquetaPersonalizada("Tipo de usuario", "Times New Roman", Font.PLAIN, 19, Color.WHITE, "izquierda"));
        formPanel.add(Box.createVerticalStrut(8));
        comboTipoEstudiante = new JComboBox<>(new String[] {
            "Estudiante regular",
            "Estudiante becario",
            "Estudiante exonerado"
        });
        comboTipoEstudiante.setPreferredSize(tamCaja);
        comboTipoEstudiante.setMaximumSize(tamCaja);
        comboTipoEstudiante.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboTipoEstudiante.setFont(new Font("Arial", Font.PLAIN, 16));
        formPanel.add(comboTipoEstudiante);
        panelFondoBase.add(formPanel);
    }


    private void construirCuerpo() {
        JPanel panelFondo = crearPanel(250, 15, 500, 150, 50, 50);

        panelFondo.setLayout(new BoxLayout(panelFondo, BoxLayout.Y_AXIS));
        panelFondo.setOpaque(false);
        panelFondo.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        panelFondo.add(Box.createVerticalStrut(20));
        panelFondo.add(titulo);
        panelFondo.add(Box.createVerticalStrut(50));

        agregarCampos(panelFondo);

        panelFondo.add(Box.createVerticalStrut(18));
        panelFondo.add(btnCambiar);

        this.contenedorPrincipal.add(panelFondo, BorderLayout.CENTER);
    }


    public BotonNeon getBtnCambiar() { return btnCambiar; }
    public String getCedula() { return ciField.getText(); }
    public String getTipoEstudiante() {
        Object tipoSeleccionado = comboTipoEstudiante.getSelectedItem();
        return tipoSeleccionado == null ? "" : tipoSeleccionado.toString();
    }

    public TipoUsuario getTipoUsuarioSeleccionado() {
        String valor = getTipoEstudiante();
        return switch (valor) {
            case "Estudiante regular" -> TipoUsuario.ESTUDIANTE;
            case "Estudiante becario" -> TipoUsuario.BECARIO;
            case "Estudiante exonerado" -> TipoUsuario.EXONERADO;
            default -> TipoUsuario.ESTUDIANTE;
        };
    }

    public void setTipoUsuarioSeleccionado(TipoUsuario tipoUsuario) {
        if (tipoUsuario == null || comboTipoEstudiante == null) {
            return;
        }
        String etiqueta = switch (tipoUsuario) {
            case ESTUDIANTE -> "Estudiante regular";
            case BECARIO -> "Estudiante becario";
            case EXONERADO -> "Estudiante exonerado";
            default -> "Estudiante regular";
        };
        comboTipoEstudiante.setSelectedItem(etiqueta);
    }

}

