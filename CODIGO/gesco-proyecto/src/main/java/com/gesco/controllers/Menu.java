package com.gesco.controllers;
import java.time.LocalDate;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.gesco.models.Insumo;
import com.gesco.models.Platillo;
import com.gesco.views.TarjetaMenu;

public class Menu{

    public static void main( String[] args ){
        LogicaInterfaz logica = new LogicaInterfaz();
        logica.iniciar();
        // La vista `VistaFila` fue eliminada; reemplazado por mensaje informativo.
        javax.swing.JOptionPane.showMessageDialog(null,
            "La vista 'Fila' ya no está disponible.",
            "Info",
            javax.swing.JOptionPane.INFORMATION_MESSAGE);
        SwingUtilities.invokeLater(() -> {
            

            com.gesco.models.Menu menuLunes = new com.gesco.models.Menu(LocalDate.of(2025, 2, 9));
            
            Platillo Carnita = new Platillo("Carnita asada");
            Insumo aceite = new Insumo("Aceite", 1, "Natural");
            Carnita.agregarInsumo(aceite);

            menuLunes.agregarPlatillo(Carnita);
            menuLunes.agregarPlatillo(Carnita);
            menuLunes.agregarPlatillo(Carnita);
            TarjetaMenu tarjeta = new TarjetaMenu(menuLunes);


            JFrame frame = new JFrame("Test de Tarjeta de Menú");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 600); 
            frame.setLayout(null); 
            
            tarjeta.setBounds(50, 50, 250, 250);
            
            frame.add(tarjeta);
            
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            
            System.out.println("Prueba iniciada: Verificando renderizado de " + menuLunes.getPlatillos().get(0));
        });
    }

}
