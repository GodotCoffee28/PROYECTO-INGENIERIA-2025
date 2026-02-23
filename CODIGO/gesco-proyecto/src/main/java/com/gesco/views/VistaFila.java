package com.gesco.views;

import javax.swing.*;
import java.awt.*;

import com.gesco.views.PlantillasViews.BotonNeon;
import com.gesco.views.PlantillasViews.PlantillaGesco;

public class VistaFila extends PlantillaGesco{
    BotonNeon btnVerMenu, btnEntrar, btnSalir, btnVerHorario;
    JLabel bienvenida, infoMenu, inforTurnos, etiDesayuno, etiAlmuerzo;
    private String nombreUsuario;
    private int saldoDisponible = 0;

    VistaFila(){
        super();
        setImagenFondo("/FondoPrincipal.png");
        inicializarComponentes();
        construirCuerpo();
        revalidate();
        repaint();
        setVisible(true);
    }


    private void inicializarComponentes(){
        Dimension tamBoton = new Dimension(220, 50);

        btnVerMenu = new BotonNeon("Ver menú semanal");
        btnVerMenu.setPreferredSize(tamBoton);
        btnEntrar = new BotonNeon("Entrar en fila");
        btnEntrar.setPreferredSize(tamBoton);
        btnSalir = new BotonNeon("Salir de la fila");
        btnSalir.setPreferredSize(tamBoton);
        btnVerHorario = new BotonNeon("Ver horarios");
        btnVerHorario.setPreferredSize(tamBoton);

        bienvenida = crearEtiquetaPersonalizada("¡Bienvenido, " + nombreUsuario + "!" + "Su saldo es: "+ saldoDisponible + ". ", 
            "Arial",Font.BOLD, 45, new Color(240, 240, 240), "centro");
        inforTurnos = crearEtiquetaPersonalizada("El acceso esta habilitado unicamente durante los turnos programados. \n Consulte aqui los horarios especificos disponibles segun la capacidad del servicio.", 
            "Arial",Font.BOLD, 16, new Color(240, 240, 240), "centro");
        infoMenu = crearEtiquetaPersonalizada("Menu de hoy", 
            "Arial",Font.BOLD, 28, new Color(240, 240, 240), "centro");
            etiDesayuno = crearEtiquetaPersonalizada("Desayuno", 
        "Arial",Font.BOLD, 20, new Color(240, 240, 240), "centro");
            etiAlmuerzo = crearEtiquetaPersonalizada("Almuerzo", 
        "Arial",Font.BOLD, 20, new Color(240, 240, 240), "centro");
        

        
    }

    private void construirCuerpo(){
        JPanel cuerpo = crearPanel(100,10, 200, 20, 50,50);
        JPanel panelIz = crearPanel(50,5, 100, 20, 50,50);
        JPanel panelDer = crearPanel(50,5, 100, 20, 50,50);
        

    }

}
