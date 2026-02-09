package com.gesco.controllers;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

public class MenuComandosControladorTest {

    private static class Contadores {
        final AtomicInteger inicio = new AtomicInteger();
        final AtomicInteger sesion = new AtomicInteger();
        final AtomicInteger registro = new AtomicInteger();
        final AtomicInteger fila = new AtomicInteger();
        final AtomicInteger menuSemana = new AtomicInteger();
        final AtomicInteger turnos = new AtomicInteger();
        final AtomicInteger panelControl = new AtomicInteger();
        final AtomicInteger cargaCcb = new AtomicInteger();
        final AtomicInteger crearMenu = new AtomicInteger();
        final AtomicInteger editarMenu = new AtomicInteger();
        final AtomicInteger gestionMenu = new AtomicInteger();
        final AtomicInteger salir = new AtomicInteger();
    }

    private MenuComandosControlador crearSut(Contadores c) {
        MenuAcciones acciones = new MenuAccionesControlador(
            c.inicio::incrementAndGet,
            c.sesion::incrementAndGet,
            c.registro::incrementAndGet,
            c.fila::incrementAndGet,
            c.menuSemana::incrementAndGet,
            c.turnos::incrementAndGet,
            c.panelControl::incrementAndGet,
            c.cargaCcb::incrementAndGet,
            c.crearMenu::incrementAndGet,
            c.editarMenu::incrementAndGet,
            c.gestionMenu::incrementAndGet,
            c.salir::incrementAndGet
        );

        return new MenuComandosControlador(acciones);
    }

    @Test
    public void procesa_comandos_basicos() {
        Contadores c = new Contadores();
        MenuComandosControlador sut = crearSut(c);

        sut.procesar("CMD_INICIO", false);
        sut.procesar("CMD_SESION", false);
        sut.procesar("CMD_REGISTRO", false);
        sut.procesar("CMD_FILA", false);
        sut.procesar("CMD_MENUSEMANA", false);
        sut.procesar("CMD_TURNOS", false);
        sut.procesar("CMD_SALIR", false);

        assertEquals(1, c.inicio.get());
        assertEquals(1, c.sesion.get());
        assertEquals(1, c.registro.get());
        assertEquals(1, c.fila.get());
        assertEquals(1, c.menuSemana.get());
        assertEquals(1, c.turnos.get());
        assertEquals(1, c.salir.get());
    }

    @Test
    public void no_ejecuta_comandos_admin_si_no_es_admin() {
        Contadores c = new Contadores();
        MenuComandosControlador sut = crearSut(c);

        sut.procesar("CMD_CONTROL", false);
        sut.procesar("CMD_CCB", false);
        sut.procesar("CMD_CREARMENU", false);
        sut.procesar("CMD_EDITARMENU", false);
        sut.procesar("CMD_GESTIONMENU", false);

        assertEquals(0, c.panelControl.get());
        assertEquals(0, c.cargaCcb.get());
        assertEquals(0, c.crearMenu.get());
        assertEquals(0, c.editarMenu.get());
        assertEquals(0, c.gestionMenu.get());
    }

    @Test
    public void ejecuta_comandos_admin_si_es_admin() {
        Contadores c = new Contadores();
        MenuComandosControlador sut = crearSut(c);

        sut.procesar("CMD_CONTROL", true);
        sut.procesar("CMD_CCB", true);
        sut.procesar("CMD_CREARMENU", true);
        sut.procesar("CMD_EDITARMENU", true);
        sut.procesar("CMD_GESTIONMENU", true);

        assertEquals(1, c.panelControl.get());
        assertEquals(1, c.cargaCcb.get());
        assertEquals(1, c.crearMenu.get());
        assertEquals(1, c.editarMenu.get());
        assertEquals(1, c.gestionMenu.get());
    }

    @Test
    public void ignora_comando_null_o_vacio() {
        Contadores c = new Contadores();
        MenuComandosControlador sut = crearSut(c);

        sut.procesar(null, true);
        sut.procesar("", true);
        sut.procesar("  ", true);

        assertEquals(0, c.inicio.get());
        assertEquals(0, c.sesion.get());
        assertEquals(0, c.registro.get());
        assertEquals(0, c.fila.get());
        assertEquals(0, c.menuSemana.get());
        assertEquals(0, c.turnos.get());
        assertEquals(0, c.panelControl.get());
        assertEquals(0, c.cargaCcb.get());
        assertEquals(0, c.crearMenu.get());
        assertEquals(0, c.editarMenu.get());
        assertEquals(0, c.gestionMenu.get());
        assertEquals(0, c.salir.get());
    }
}
