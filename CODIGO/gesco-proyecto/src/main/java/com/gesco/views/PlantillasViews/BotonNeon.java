
package com.gesco.views.PlantillasViews;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

public class BotonNeon extends JButton {
    private final int radioEsquinas = 40;
    
    private final Color colorBase = new Color(200, 200, 205);      
    private final Color colorBrillo = new Color(255, 255, 255); 
    private boolean mouseEncima = false;

    public BotonNeon(String texto) {
        super(texto);
        setContentAreaFilled(false); 
        setBorderPainted(false);     
        setFocusPainted(false);       
        setOpaque(false);
        
        setFont(new Font("Arial", Font.BOLD, 30));
        setForeground(Color.WHITE); 
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                mouseEncima = true;
                repaint(); 
            }
            @Override
            public void mouseExited(MouseEvent e) {
                mouseEncima = false;
                repaint(); 
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color colorActual = mouseEncima ? colorBrillo : colorBase;
        float grosorBorde = mouseEncima ? 5f : 3f;

        int capasGlow = mouseEncima ? 15 : 8; 
        int opacidadMax = mouseEncima ? 80 : 40;

        for (int i = 0; i < capasGlow; i++) {
            int alpha = opacidadMax - (i * (opacidadMax / capasGlow));
            g2.setColor(new Color(colorActual.getRed(), colorActual.getGreen(), colorActual.getBlue(), Math.max(0, alpha)));
            g2.fillRoundRect(i/2, i/2, getWidth() - i, getHeight() - i, radioEsquinas, radioEsquinas);
        }

        g2.setColor(new Color(50, 50, 55)); 
        g2.fillRoundRect(6, 6, getWidth() - 12, getHeight() - 12, radioEsquinas, radioEsquinas);

        g2.setStroke(new BasicStroke(grosorBorde));
        g2.setColor(colorActual);
        g2.drawRoundRect(6, 6, getWidth() - 12, getHeight() - 12, radioEsquinas, radioEsquinas);

        super.paintComponent(g2);
        g2.dispose();
    }
}