package com.gesco;

import com.gesco.controllers.LogicaInterfaz;

public class Main {

    public static void main(String[] args) {
        LogicaInterfaz logica = new LogicaInterfaz();
        logica.iniciar();
    }
}

/*package com.gesco;
import java.time.LocalDate;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.gesco.models.Insumo;
import com.gesco.models.Menu;
import com.gesco.models.Platillo;
import com.gesco.views.TarjetaMenu;

public class Main{

    public static void main( String[] args ){
        LogicaInterfaz logica = new LogicaInterfaz();
        logica.iniciar();
        VistaFila vistafila = new VistaFila("Enrique");
        vistafila.setVisible(true);
        SwingUtilities.invokeLater(() -> {
            

            Menu menuLunes = new Menu(LocalDate.of(2025, 2, 9));
            
            Platillo Carnita = new Platillo("Carnita asada");
            Insumo aceite = new Insumo("Aceite", 1, "Natural");
            Carnita.agregarInsumo(aceite);

            menuLunes.agregarPlatillo(Carnita);
            menuLunes.agregarPlatillo(Carnita);
            menuLunes.agregarPlatillo(Carnita);
            TarjetaMenu tarjeta = new TarjetaMenu(menuLunes);


            JFrame frame = new JFrame("Test de Tarjeta de Menú");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 600); // Tamaño similar a tu mockup
            frame.setLayout(null); // O BorderLayout si quieres que ocupe todo
            
            // Configuramos la posición de la tarjeta manualmente para la prueba
            tarjeta.setBounds(50, 50, 250, 250);
            
            // Añadimos la tarjeta al frame
            frame.add(tarjeta);
            
            // Centrar y mostrar
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            
            System.out.println("Prueba iniciada: Verificando renderizado de " + menuLunes.getPlatillos().get(0));
        });
    }

}*/
