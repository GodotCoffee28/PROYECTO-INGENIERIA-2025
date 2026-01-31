import javax.swing.*;
import java.awt.*;

public class ComedorUCVLogin extends JFrame {

    // Clase personalizada para botones con esquinas redondeadas
    class RoundedButton extends JButton {
        private final int arc = 40; // Radio de las esquinas (ajustable)

        public RoundedButton(String text) {
            super(text);
            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setFont(new Font("Arial", Font.BOLD, 20));
            setForeground(Color.BLACK);
            setBackground(new Color(169, 169, 169)); // Gris medio
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    public ComedorUCVLogin() {
        setTitle("Comedor estudiantil UCV");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 700);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // === Barra superior ===
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(220, 220, 220));
        topPanel.setPreferredSize(new Dimension(400, 70));

        JLabel menuIcon = new JLabel("☰");
        menuIcon.setFont(new Font("Arial", Font.PLAIN, 50));
        menuIcon.setHorizontalAlignment(SwingConstants.CENTER);
        menuIcon.setPreferredSize(new Dimension(80, 70));

        JLabel topTitle = new JLabel("Comedor estudiantil UCV", SwingConstants.CENTER);
        topTitle.setFont(new Font("Arial", Font.BOLD, 20));

        topPanel.add(menuIcon, BorderLayout.WEST);
        topPanel.add(topTitle, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // === Panel central con BoxLayout para alineación vertical ===
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(60, 40, 60, 40));

        // Saludo
        JLabel greeting = new JLabel("¡Hola, Ucevista!");
        greeting.setFont(new Font("Arial", Font.BOLD, 32));
        greeting.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(greeting);

        contentPanel.add(Box.createVerticalStrut(20));

        // Texto acceso
        JLabel accessText = new JLabel("Accede a la plataforma del comedor");
        accessText.setFont(new Font("Arial", Font.PLAIN, 20));
        accessText.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(accessText);

        contentPanel.add(Box.createVerticalStrut(50));

        // Pregunta
        JLabel firstTime = new JLabel("¿Primera vez accediendo?");
        firstTime.setFont(new Font("Arial", Font.PLAIN, 18));
        firstTime.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(firstTime);

        contentPanel.add(Box.createVerticalStrut(30));

        // Botón Inicio de sesión
        RoundedButton loginButton = new RoundedButton("Registrarse");
        loginButton.setPreferredSize(new Dimension(320, 60));
        loginButton.setMaximumSize(new Dimension(320, 60));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(loginButton);

        contentPanel.add(Box.createVerticalStrut(60));

        // Texto inferior
        JLabel bottomTitle = new JLabel("Comedor estudiantil UCV");
        bottomTitle.setFont(new Font("Arial", Font.PLAIN, 16));
        bottomTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(bottomTitle);

        contentPanel.add(Box.createVerticalStrut(20));
        // Botón Registrarse
        RoundedButton registerButton = new RoundedButton("Inicio de sesión");
        registerButton.setPreferredSize(new Dimension(280, 55));
        registerButton.setMaximumSize(new Dimension(280, 55));
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(registerButton);

        // Añadir espacio flexible arriba y abajo para centrar mejor
        contentPanel.add(Box.createVerticalGlue());

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ComedorUCVLogin().setVisible(true);
        });
    }
}