package com.gesco.controllers;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

public class MenuAccionesControladorTest {

    @Test
    public void ejecuta_las_acciones_correspondientes() {
        AtomicInteger inicio = new AtomicInteger();
        AtomicInteger sesion = new AtomicInteger();
        AtomicInteger registro = new AtomicInteger();
        AtomicInteger fila = new AtomicInteger();
        AtomicInteger menuSemana = new AtomicInteger();
        AtomicInteger turnos = new AtomicInteger();
        AtomicInteger panelControl = new AtomicInteger();
        AtomicInteger cargaCcb = new AtomicInteger();
        AtomicInteger crearMenu = new AtomicInteger();
        AtomicInteger editarMenu = new AtomicInteger();
        AtomicInteger gestionMenu = new AtomicInteger();
        AtomicInteger salir = new AtomicInteger();

        MenuAccionesControlador controlador = new MenuAccionesControlador(
            inicio::incrementAndGet,
            sesion::incrementAndGet,
            registro::incrementAndGet,
            fila::incrementAndGet,
            menuSemana::incrementAndGet,
            turnos::incrementAndGet,
            panelControl::incrementAndGet,
            cargaCcb::incrementAndGet,
            crearMenu::incrementAndGet,
            editarMenu::incrementAndGet,
            gestionMenu::incrementAndGet,
            salir::incrementAndGet
        );

        controlador.irInicio();
        controlador.irSesion();
        controlador.irRegistro();
        controlador.irFila();
        controlador.irMenuSemana();
        controlador.irTurnos();
        controlador.irPanelControl();
        controlador.irCargaCCB();
        controlador.irCrearMenu();
        controlador.irEditarMenu();
        controlador.irGestionMenu();
        controlador.salir();

        assertEquals(1, inicio.get());
        assertEquals(1, sesion.get());
        assertEquals(1, registro.get());
        assertEquals(1, fila.get());
        assertEquals(1, menuSemana.get());
        assertEquals(1, turnos.get());
        assertEquals(1, panelControl.get());
        assertEquals(1, cargaCcb.get());
        assertEquals(1, crearMenu.get());
        assertEquals(1, editarMenu.get());
        assertEquals(1, gestionMenu.get());
        assertEquals(1, salir.get());
    }
}
