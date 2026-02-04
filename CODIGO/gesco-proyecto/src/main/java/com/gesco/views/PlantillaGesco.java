import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PlantillaGesco extends JFrame {

    protected JPanel contenedorPrincipal; 

    public PlantillaGesco() {
        //Configuración de la ventana
        setTitle("Comedor estudiantil UCV");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750); 
        setLocationRelativeTo(null);
        setResizable(false);

        //Fondo con degradado
        contenedorPrincipal = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(45, 45, 50), 0, getHeight(), new Color(15, 15, 18));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        contenedorPrincipal.add(Encabezado(), BorderLayout.NORTH);

        add(contenedorPrincipal);
    }

    private JPanel Encabezado() {
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(true);
        topPanel.setBackground(new Color(60, 60, 65));
        topPanel.setPreferredSize(new Dimension(1100, 100));
        
        
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(255, 255, 255)));
        
        JLabel menuIcon = new JLabel("☰");
        menuIcon.setFont(new Font("Dialog", Font.PLAIN, 45));
        menuIcon.setForeground(new Color(180, 180, 180));
        menuIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        menuIcon.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));

        menuIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                menuIcon.setForeground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                menuIcon.setForeground(new Color(180, 180, 180));
            }
        });
        
        JLabel tituloEncabezado = new JLabel("Comedor estudiantil UCV", SwingConstants.CENTER);
        tituloEncabezado.setFont(new Font("Arial", Font.BOLD, 30));
        tituloEncabezado.setForeground(Color.WHITE);

        JLabel relleno = new JLabel("☰");
        relleno.setFont(new Font("Dialog", Font.PLAIN, 45));
        relleno.setForeground(new Color(0,0,0,0)); 
        relleno.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));

        topPanel.add(menuIcon, BorderLayout.WEST);
        topPanel.add(tituloEncabezado, BorderLayout.CENTER);
        topPanel.add(relleno, BorderLayout.EAST);

        return topPanel;
    }
     protected JLabel crearEtiquetaSimple(String texto, int size, Color color) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(new Font("Arial", Font.PLAIN, size));
        etiqueta.setForeground(color);
        etiqueta.setAlignmentX(Component.CENTER_ALIGNMENT);
        return etiqueta;
    }
}