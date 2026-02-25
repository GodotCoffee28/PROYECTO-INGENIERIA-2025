package com.gesco.views;
import java.awt.*;
import javax.swing.*;
import com.gesco.views.PlantillasViews.BotonNeon;
import com.gesco.views.PlantillasViews.PlantillaGesco;

public class VistaInicioComensal extends PlantillaGesco {

    private BotonNeon btnVerMenu, btnHorarios, btnAccesoFila, btnRecargar, btnRegistro;
    private JLabel bienvenida, infoTurnos, infoMenu, lblSaldo, infoFila;
    private String nombreUsuario;
    private double saldoDisponible = 0.0;

    public VistaInicioComensal(String nombre, double saldo) {
        super();
        this.nombreUsuario = nombre;
        this.saldoDisponible = saldo;
        setImagenFondo("/FondoPrincipal.png");
        inicializarComponentes();
        construirCuerpo();
        revalidate();
        repaint();
        setVisible(true);
    }

    private void inicializarComponentes() {
        Dimension tamBoton = new Dimension(220, 50);

        btnVerMenu = new BotonNeon("Ver menú semanal");
        btnVerMenu.setPreferredSize(tamBoton);

        btnHorarios = new BotonNeon("Verificar horarios");
        btnHorarios.setPreferredSize(tamBoton);
        
        btnAccesoFila = new BotonNeon("Ver estado de la fila");
        btnAccesoFila.setPreferredSize(tamBoton);

        btnRecargar = new BotonNeon("Recargar saldo");
        btnRecargar.setPreferredSize(tamBoton);

        btnRegistro = new BotonNeon("Registrar de transacciones");
        btnRegistro.setPreferredSize(tamBoton);

        bienvenida = new JLabel("¡Bienvenido, " + nombreUsuario + "!");
        bienvenida.setFont(new Font("Arial", Font.BOLD, 30));
        bienvenida.setForeground(new Color(240, 240, 240)); 
        bienvenida.setHorizontalAlignment(SwingConstants.CENTER);

        String estilo = "<html><body style='text-align: center; width: 280px;'>";
        
        infoMenu = new JLabel(estilo + "¡No te pierdas el menú de hoy!<br>"+ "Revisa la oferta completa de platos y organiza tu almuerzo con antelación."+ "</body></html>");
        infoMenu.setFont(new Font("Arial", Font.PLAIN, 20));
        infoMenu.setForeground(new Color(230, 230, 230));

        infoTurnos = new JLabel(estilo + "El acceso está habilitado únicamente durante los turnos programados.<br>"+ "Consulte aquí los horarios específicos."+ "</body></html>");
        infoTurnos.setFont(new Font("Arial", Font.PLAIN, 20));
        infoTurnos.setForeground(new Color(230, 230, 230));

        infoFila = new JLabel(estilo + "Asegura tu lugar en el comedor.<br>" + "Consulta los turnos disponibles y gestiona tu posición en la fila de forma eficiente." + "</body></html>");
        infoFila.setFont(new Font("Arial", Font.PLAIN, 20));
        infoFila.setForeground(new Color(230, 230, 230));

        lblSaldo = new JLabel();
        actualizarTextoSaldo();
        lblSaldo.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblSaldo.setForeground(Color.WHITE);
    }
        
    private void construirCuerpo() {
        JPanel panelCuerpo = new JPanel(new BorderLayout(0, 10));
        panelCuerpo.setOpaque(false);
        panelCuerpo.setBorder(BorderFactory.createEmptyBorder(10, 50, 20, 50));

        JPanel tarjetaBienvenida = crearTarjeta("", 0, 50, 20, 10, null);

        bienvenida.setHorizontalAlignment(SwingConstants.CENTER);
        bienvenida.setFont(new Font("Segoe UI", Font.ITALIC, 28));

        tarjetaBienvenida.add(bienvenida, BorderLayout.CENTER);

        panelCuerpo.add(tarjetaBienvenida, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new GridBagLayout()); 
        panelCentral.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.weightx = 1.0; 
        gbc.weighty = 0.5;

        gbc.gridy = 0;

        JPanel tarjetaSaldo = crearTarjeta("Tu saldo actual:", 400, 300, 40, 10, "/Billetera.png"); 
        
        JPanel contenedorSaldo = new JPanel(new GridBagLayout());
        contenedorSaldo.setOpaque(false); 
        GridBagConstraints gInt = new GridBagConstraints();
        gInt.gridx = 0;
        gInt.weightx = 1.0;

        gInt.gridy = 0;
        gInt.fill = GridBagConstraints.HORIZONTAL;
        gInt.insets = new Insets(0, 20, 5, 20);
        contenedorSaldo.add(lblSaldo, gInt);

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(250, 250, 255, 50));
        gInt.gridy = 1;
        gInt.insets = new Insets(2, 20, 10, 20);
        contenedorSaldo.add(sep, gInt);

        gInt.fill = GridBagConstraints.HORIZONTAL;
        
        btnRecargar.setText("Recargar saldo");
        gInt.gridy = 2;
        gInt.insets = new Insets(0, 40, 8, 40); 
        contenedorSaldo.add(btnRecargar, gInt);

        btnRegistro.setText("Ver movimientos"); 
        gInt.gridy = 3;
        gInt.insets = new Insets(0, 40, 5, 40);
        contenedorSaldo.add(btnRegistro, gInt);

        tarjetaSaldo.add(contenedorSaldo, BorderLayout.CENTER);
        gbc.gridx = 0; 
        panelCentral.add(tarjetaSaldo, gbc);

        JPanel menuTurnos = crearTarjeta("Turnos", 400, 300, 40, 10, "/Reloj.png");
        menuTurnos.add(crearContenedorInterno(infoFila, btnAccesoFila), BorderLayout.CENTER);
        gbc.gridx = 1; 
        panelCentral.add(menuTurnos, gbc);

        gbc.gridy = 1;

        JPanel cajaMenu = crearTarjeta("Menú del Día", 400, 300, 40, 10, "/Platos.png");
        cajaMenu.add(crearContenedorInterno(infoMenu, btnVerMenu), BorderLayout.CENTER);
        gbc.gridx = 0; 
        panelCentral.add(cajaMenu, gbc);

        JPanel cajaHorarios = crearTarjeta("Horarios de Servicio", 400, 300, 40, 10, "/personas.png");
        cajaHorarios.add(crearContenedorInterno(infoTurnos, btnHorarios), BorderLayout.CENTER);
        gbc.gridx = 1; 
        panelCentral.add(cajaHorarios, gbc);

        panelCuerpo.add(panelCentral, BorderLayout.CENTER);
        contenedorPrincipal.add(panelCuerpo, BorderLayout.CENTER);
    }

    private JPanel crearContenedorInterno(JLabel texto, JButton boton) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.fill = GridBagConstraints.HORIZONTAL;
        
        g.gridy = 0;
        g.weighty = 1.0;
        g.anchor = GridBagConstraints.CENTER;
        p.add(texto, g);
        
        g.gridy = 1;
        g.weighty = 0;
        g.insets = new Insets(15, 0, 0, 0);
        p.add(boton, g);
        return p;
    }


    public BotonNeon getBtnVerMenu() {
        return btnVerMenu;
    }

    public BotonNeon getBtnHorarios() {
        return btnHorarios;
    }

    public BotonNeon getBtnRecargar() {
        return btnRecargar;
    }

    public void setSaldoDisponible(double saldoDisponible) {
        this.saldoDisponible = saldoDisponible;
        actualizarTextoSaldo();
    }

    private void actualizarTextoSaldo() {
        if (lblSaldo != null) {
            lblSaldo.setText("Saldo Disponible: " + String.format("%.2f", saldoDisponible) + " Bs.");
        }
    }
}