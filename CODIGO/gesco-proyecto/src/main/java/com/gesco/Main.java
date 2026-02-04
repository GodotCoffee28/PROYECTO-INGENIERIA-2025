package com.gesco;
import com.gesco.views.PlantillaGesco;
/**
 * Hello world!
 *
 */
public class Main{
    public static void main( String[] args )    {
        javax.swing.SwingUtilities.invokeLater(() -> {
            PlantillaGesco VIsta = new PlantillaGesco();
            VIsta.setVisible(true);
        });
    }
}
