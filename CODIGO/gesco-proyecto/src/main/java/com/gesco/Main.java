package com.gesco;
import com.gesco.views.RegistroView;
/**
 * Hello world!
 *
 */
public class Main{
    public static void main( String[] args )    {
        javax.swing.SwingUtilities.invokeLater(() -> {
            RegistroView view = new RegistroView();
            view.setVisible(true);
        });
    }
}
