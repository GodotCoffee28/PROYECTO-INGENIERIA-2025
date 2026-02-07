package com.gesco.views;
import java.awt.*;
import javax.swing.*;
import com.gesco.views.PlantillasViews.BotonNeon;
import com.gesco.views.PlantillasViews.PlantillaGesco;

public class VistaInicioComensal extends PlantillaGesco {

    private BotonNeon btnVerMenu, btnHorarios;
    private JLabel bienvenida, infoTurnos, infoMenu, lblSaldo;
    private String nombreUsuario;
    private int saldoDisponible = 0;

    public VistaInicioComensal(String nombre, int saldo) {
        super();
        this.nombreUsuario = nombre;
        this.saldoDisponible = saldo;
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

        bienvenida = new JLabel("¡Bienvenido, " + nombreUsuario + "!");
        bienvenida.setFont(new Font("Arial", Font.BOLD, 36));
        bienvenida.setForeground(new Color(240, 240, 240)); 
        bienvenida.setHorizontalAlignment(SwingConstants.CENTER);

        String estilo = "<html><body style='text-align: center; width: 280px;'>";
        
        infoMenu = new JLabel(estilo + "¡No te pierdas el menú de hoy!<br>"+ "Revisa la oferta completa de platos y organiza tu almuerzo con antelación."+ "</body></html>");
        infoMenu.setFont(new Font("Arial", Font.PLAIN, 20));
        infoMenu.setForeground(new Color(230, 230, 230));

        infoTurnos = new JLabel(estilo + "El acceso está habilitado únicamente durante los turnos programados.<br>"+ "Consulte aquí los horarios específicos."+ "</body></html>");
        infoTurnos.setFont(new Font("Arial", Font.PLAIN, 20));
        infoTurnos.setForeground(new Color(230, 230, 230));

        lblSaldo = new JLabel("Saldo Disponible: " + saldoDisponible + " Bs.");
        lblSaldo.setFont(new Font("Arial", Font.BOLD, 28));
        lblSaldo.setForeground(Color.WHITE);
    }

    private void construirCuerpo() {
        JPanel panelCuerpo = new JPanel(new BorderLayout(0, 30));
        panelCuerpo.setOpaque(false);
        panelCuerpo.setBorder(BorderFactory.createEmptyBorder(30, 50, 50, 50));

        panelCuerpo.add(bienvenida, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new GridBagLayout()); 
        panelCentral.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(0, 20, 30, 20);

        // Tarjeta Izquierda (Menú)
        JPanel cajaMenu = crearTarjeta("Menú del Día", 420, 300);
        cajaMenu.add(crearContenedorInterno(infoMenu, btnVerMenu), BorderLayout.CENTER);
        gbc.gridx = 0;
        panelCentral.add(cajaMenu, gbc);

        // Tarjeta Derecha (Horarios)
        JPanel cajaHorarios = crearTarjeta("Horarios de Servicio", 420, 300);
        cajaHorarios.add(crearContenedorInterno(infoTurnos, btnHorarios), BorderLayout.CENTER);
        gbc.gridx = 1;
        panelCentral.add(cajaHorarios, gbc);

        JPanel tarjetaSaldo = crearTarjeta("", 880, 130); 
        tarjetaSaldo.setLayout(new BorderLayout(40, 0));
        tarjetaSaldo.setBorder(BorderFactory.createEmptyBorder(20, 45, 20, 45));
        

        JLabel billeteraIcono = new JLabel();
        ImageIcon icono = new ImageIcon("Billetera.png");
        Image scaled = icono.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
        billeteraIcono.setIcon(new ImageIcon(scaled));
        
        
        tarjetaSaldo.add(billeteraIcono, BorderLayout.WEST);
        tarjetaSaldo.add(lblSaldo, BorderLayout.CENTER); 

        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 2; 
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 0, 0, 0);
        panelCentral.add(tarjetaSaldo, gbc);

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

    private JPanel crearTarjeta(String titulo, int ancho, int alto) {
        JPanel tarjeta = new JPanel(new BorderLayout(15, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(new Color(255, 255, 255, 30)); 
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 50);
                
                g2.setColor(new Color(255, 255, 255, 60));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 50, 50);
                g2.dispose();
            }
        };
        
        tarjeta.setOpaque(false); 
        tarjeta.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        tarjeta.add(lblTitulo, BorderLayout.NORTH);

        Dimension dim = new Dimension(ancho, alto);
        tarjeta.setPreferredSize(dim);
        tarjeta.setMinimumSize(dim);
        tarjeta.setMaximumSize(dim);
        
        return tarjeta;
    }
}