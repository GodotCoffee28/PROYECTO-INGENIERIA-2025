package com.gesco.views.otros;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.io.File;

import com.gesco.models.menu.Menu;
import com.gesco.models.menu.Platillo;
import com.gesco.views.PlantillasViews.BotonNeon;
import com.gesco.views.PlantillasViews.PlantillaGesco;
import com.gesco.views.menu.TarjetaMenu;
import com.gesco.views.PlantillasViews.VentanaEmergente;

public class VistaFila extends PlantillaGesco {
    private BotonNeon btnEntrar, btnSalir, btnVerMenu;
    private JLabel infoFila, infoBienvenida, iconoPersona, lblNombreArchivo;
    private int enFila = 0, Disponible = 100;
    private double costoMenu = 0.0;
    private double descuento = 0.0;
    private double costoTotal = 0.0;
    private JButton btnCobrar, btnCancelar, btnSeleccionar;
    private File archivoSeleccionado = null;
    private VentanaEmergente ventanaCobro;

    public VistaFila() {
        super();
        setImagenFondo("/FondoPrincipal.png");
        inicializarComponentes();
        construirCuerpo();
        revalidate();
        repaint();
        setVisible(true);
    }

    private void inicializarComponentes() {
        Dimension tam = new Dimension(350, 70);

        btnEntrar = new BotonNeon("Entrar a la fila");
        btnEntrar.setPreferredSize(tam);
        btnEntrar.setMaximumSize(tam);
        btnEntrar.setFont(new Font("Arial", Font.BOLD, 32));

        btnSalir = new BotonNeon("Salir de la fila");
        btnSalir.setPreferredSize(tam);
        btnSalir.setMaximumSize(tam);
        btnSalir.setFont(new Font("Arial", Font.BOLD, 32));

        btnVerMenu = new BotonNeon("Ver menú de la semana");
        btnVerMenu.setPreferredSize(tam);
        btnVerMenu.setMaximumSize(tam);
        btnVerMenu.setFont(new Font("Arial", Font.BOLD, 24));

        infoBienvenida = crearEtiquetaPersonalizada("Estado del Comedor Universitario", "Times New Roman", Font.BOLD, 40, new Color(240, 240, 240), "centro");

        infoFila = new JLabel();
        actualizarInfoFila();
        infoFila.setFont(new Font("Times New Roman", Font.BOLD, 35));
        infoFila.setForeground(new Color(240, 240, 240));

        iconoPersona = new JLabel(obtenerIcono("/personas.png", 250));

        btnCobrar = crearBotonventanaCobro("Cobrar", new Color(100, 100, 100));
        btnCancelar = crearBotonventanaCobro("Cancelar", new Color(150, 150, 150));
        btnSeleccionar = crearBotonventanaCobro("Seleccionar imagen", new Color(50,50,50));
        lblNombreArchivo = new JLabel("Ningún archivo seleccionado");

    }

    private void construirCuerpo() {
        JPanel panelPrincipal = crearPanel(100, 10, 200, 20, 50, 50);
        panelPrincipal.setLayout(new BorderLayout(0, 10));
        panelPrincipal.setOpaque(false);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        JPanel panelNorteContenedor = new JPanel();
        panelNorteContenedor.setLayout(new BoxLayout(panelNorteContenedor, BoxLayout.Y_AXIS));
        panelNorteContenedor.setOpaque(false);

        infoBienvenida.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelNorteContenedor.add(infoBienvenida);

        JLabel lblDescripcion = crearEtiquetaPersonalizada("Consulta el estado de la fila y los menús disponibles para hoy", "Arial", Font.PLAIN, 16, new Color(200, 200, 200), "centro");
        lblDescripcion.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelNorteContenedor.add(Box.createVerticalStrut(2));
        panelNorteContenedor.add(lblDescripcion);

        JSeparator separador = new JSeparator(SwingConstants.HORIZONTAL);
        separador.setMaximumSize(new Dimension(800, 2));
        separador.setForeground(new Color(240, 240, 240, 180));

        panelNorteContenedor.add(Box.createVerticalStrut(10));
        panelNorteContenedor.add(separador);

        panelPrincipal.add(panelNorteContenedor, BorderLayout.NORTH);

        JPanel panelCentralContenedor = new JPanel(new GridBagLayout());
        panelCentralContenedor.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 40);
        panelCentralContenedor.add(iconoPersona, gbc);

        JPanel panelControles = new JPanel();
        panelControles.setLayout(new BoxLayout(panelControles, BoxLayout.Y_AXIS));
        panelControles.setOpaque(false);

        infoFila.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelControles.add(infoFila);
        panelControles.add(Box.createVerticalStrut(15));

        btnEntrar.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelControles.add(btnEntrar);
        panelControles.add(Box.createVerticalStrut(10));
        btnSalir.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelControles.add(btnSalir);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 40, 0, 0);
        panelCentralContenedor.add(panelControles, gbc);

        panelPrincipal.add(panelCentralContenedor, BorderLayout.CENTER);

        // Sección Inferior: Menú del Día
        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new BoxLayout(panelInferior, BoxLayout.Y_AXIS));
        panelInferior.setOpaque(false);

        JLabel tituloMenu = crearEtiquetaPersonalizada("Menú del día", "Times New Roman", Font.BOLD, 24, Color.WHITE, "centro");
        tituloMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelInferior.add(tituloMenu);
        panelInferior.add(Box.createVerticalStrut(8));

        JPanel panelTarjetas = new JPanel(new GridLayout(1, 2, 30, 0));
        panelTarjetas.setOpaque(false);
        panelTarjetas.setMaximumSize(new Dimension(750, 160));
        panelTarjetas.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        Menu menuD = new Menu(LocalDate.now());
        menuD.agregarPlatillo(new Platillo("Arepa"));
        panelTarjetas.add(new TarjetaMenu(menuD));

        Menu menuA = new Menu(LocalDate.now());
        menuA.agregarPlatillo(new Platillo("Pasta"));
        menuA.agregarPlatillo(new Platillo("Patilla"));
        panelTarjetas.add(new TarjetaMenu(menuA));

        panelInferior.add(panelTarjetas);
        panelInferior.add(Box.createVerticalStrut(15));
        btnVerMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelInferior.add(btnVerMenu);
        panelInferior.add(Box.createVerticalStrut(10));

        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);
        this.contenedorPrincipal.add(panelPrincipal, BorderLayout.CENTER);
    }

    private JButton crearBotonventanaCobro(String texto, Color colorFondo) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(colorFondo);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public void crearVentanaEmergente(){
            Frame padre = (Frame) SwingUtilities.getWindowAncestor(this);

            ventanaCobro = new VentanaEmergente(padre, "PROCESAR ENTRADA Y COBRO");

            JPanel cuerpo = ventanaCobro.getCuerpo();
            cuerpo.setLayout(new BoxLayout(cuerpo, BoxLayout.Y_AXIS));
            cuerpo.setBorder(BorderFactory.createEmptyBorder(10, 25, 15, 25));

            Font fuenteLabel = new Font("Segoe UI", Font.BOLD, 14);
            Color colorTexto = new Color(50, 50, 50);

            JLabel lblCosto = new JLabel(String.format("Costo del menú: %.2f Bs.", costoMenu));
            JLabel lblDesc = new JLabel(String.format("Ajuste aplicado: %.2f%%", descuento));
            JLabel lblTotal = new JLabel(String.format("Costo final: %.2f Bs.", costoTotal));

            for (JLabel lbl : new JLabel[]{lblCosto, lblDesc, lblTotal}) {
                lbl.setFont(fuenteLabel);
                lbl.setForeground(colorTexto);
                lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
                cuerpo.add(lbl);
                cuerpo.add(Box.createVerticalStrut(5));
            }

            cuerpo.add(Box.createVerticalStrut(15));

            JLabel lblInstruccion = new JLabel("Seleccione su foto para comparar:");
            lblInstruccion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblInstruccion.setForeground(colorTexto);
            lblInstruccion.setAlignmentX(Component.CENTER_ALIGNMENT);
            cuerpo.add(lblInstruccion);

            btnSeleccionar.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblNombreArchivo = new JLabel("Ningún archivo seleccionado");
            lblNombreArchivo.setFont(new Font("Segoe UI", Font.ITALIC, 11));
            lblNombreArchivo.setAlignmentX(Component.CENTER_ALIGNMENT);

            cuerpo.add(Box.createVerticalStrut(5));
            cuerpo.add(btnSeleccionar);
            cuerpo.add(lblNombreArchivo);
            cuerpo.add(Box.createVerticalStrut(25));

            JPanel pnlBotonesventanaCobro = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
            pnlBotonesventanaCobro.setOpaque(false);

            pnlBotonesventanaCobro.add(btnCobrar);
            pnlBotonesventanaCobro.add(btnCancelar);
            cuerpo.add(pnlBotonesventanaCobro);

            ventanaCobro.setVisible(true);
}

    public void setNombreArchivoSeleccionado(String nombre) {
        if(lblNombreArchivo != null) lblNombreArchivo.setText(nombre);
    }
    public void setArchivoSeleccionado(File archivoSeleccionado) {
        this.archivoSeleccionado = archivoSeleccionado;
    }
    public int getDisponible() { return Disponible; }
    public BotonNeon getBtnEntrar() { return btnEntrar; }
    public BotonNeon getBtnSalir() { return btnSalir; }
    public BotonNeon getBtnVerMenu() { return btnVerMenu; }
    public JLabel getBtnBack() { return getBackIcon(); }
    public JButton getBtnCobrar() { return btnCobrar; }
    public JButton getBtnCancelar() { return btnCancelar; }
    public JButton getBtnSeleccionar(){ return btnSeleccionar;}
    public File getArchivoSeleccionado() { return archivoSeleccionado; }
    public double getCostoTotal() { return costoTotal; }
    public void cerrarEmergente() {
    if (ventanaCobro != null) {
        ventanaCobro.cerrar();
        }
    }
    public int getEnFila() { return enFila; }

    public void setEnFila(int enFila) {
        this.enFila = Math.max(0, enFila);
        actualizarInfoFila();
    }

    public void setDisponible(int disponible) {
        this.Disponible = Math.max(0, disponible);
        actualizarInfoFila();
    }

    public void setCobroInfo(double costoMenu, double descuento, double costoTotal) {
        this.costoMenu = Math.max(0.0, costoMenu);
        this.descuento = descuento;
        this.costoTotal = Math.max(0.0, costoTotal);
    }

    private void actualizarInfoFila() {
        if (infoFila != null) {
            infoFila.setText(enFila + " / " + Disponible + " Personas en la fila");
        }
    }

}
