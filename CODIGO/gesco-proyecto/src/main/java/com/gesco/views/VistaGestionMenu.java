import java.awt.*;
import javax.swing.*;

public class VistaGestionMenu extends PlantillaGesco {
     private BotonNeon btnEditar, btnCrear, btnReiniciar;
    private JLabel Titulo;

    public VistaGestionMenu() {
        super(); 
        inicializarComponentes();
        construirCuerpo();
        revalidate();
        repaint();
        setVisible(true);
    }

    private void inicializarComponentes() {
        Dimension tam = new Dimension(500, 80);

        btnEditar = new BotonNeon("Editar menú");
        btnEditar.setPreferredSize(tam);
        btnEditar.setMaximumSize(tam);
        btnEditar.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnCrear = new BotonNeon("Crear menú");
        btnCrear.setPreferredSize(tam);
        btnCrear.setMaximumSize(tam);
        btnCrear.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnReiniciar = new BotonNeon("Reiniciar menú");
        btnReiniciar.setPreferredSize(tam);
        btnReiniciar.setMaximumSize(tam);
        btnReiniciar.setAlignmentX(Component.CENTER_ALIGNMENT);

        Titulo = crearEtiquetaSimple("Gestión del menú", 32, new Color(240, 240, 240));
        Titulo.setFont(new Font("Arial", Font.BOLD, 32));
        
    }

    private void construirCuerpo() {
        JPanel panelFondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(new Color(255, 255, 255, 25)); 
                g2.fillRoundRect(250, 10, getWidth() - 500, getHeight() - 20, 50, 50);
                
                g2.setColor(new Color(255, 255, 255, 40));
                g2.drawRoundRect(250, 10, getWidth() - 500, getHeight() - 20, 50, 50);
                g2.dispose();
            }
        };
        
        panelFondo.setLayout(new BoxLayout(panelFondo, BoxLayout.Y_AXIS));
        panelFondo.setOpaque(false); 
        panelFondo.setBorder(BorderFactory.createEmptyBorder(60, 20, 60, 20));
        
        panelFondo.add(Box.createVerticalGlue());
        panelFondo.add(Titulo);
        panelFondo.add(Box.createVerticalStrut(80));

        panelFondo.add(btnEditar);
        panelFondo.add(Box.createVerticalStrut(50));

        panelFondo.add(btnCrear);
        panelFondo.add(Box.createVerticalStrut(50));
        
        panelFondo.add(btnReiniciar);
        panelFondo.add(Box.createVerticalGlue());

        this.contenedorPrincipal.add(panelFondo, BorderLayout.CENTER);
    }
}
