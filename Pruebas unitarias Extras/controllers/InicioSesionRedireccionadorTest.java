package com.gesco.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

public class InicioSesionRedireccionadorTest {

    @Test
    public void redirigir_admin_llama_onAdmin() {
        AtomicReference<String> adminNombre = new AtomicReference<>();
        AtomicReference<String> comensalNombre = new AtomicReference<>();

        InicioSesionRedireccionador redireccionador = new InicioSesionRedireccionador(
            adminNombre::set,
            comensalNombre::set
        );

        redireccionador.redirigir(true, "Ana");

        assertEquals("Ana", adminNombre.get());
        assertNull(comensalNombre.get());
    }

    @Test
    public void redirigir_comensal_llama_onComensal() {
        AtomicReference<String> adminNombre = new AtomicReference<>();
        AtomicReference<String> comensalNombre = new AtomicReference<>();

        InicioSesionRedireccionador redireccionador = new InicioSesionRedireccionador(
            adminNombre::set,
            comensalNombre::set
        );

        redireccionador.redirigir(false, "Luis");

        assertNull(adminNombre.get());
        assertEquals("Luis", comensalNombre.get());
    }
}
