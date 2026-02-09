package com.gesco.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UsuarioTest {

    @Test
    public void getters_y_setters_actualizan_datos() {
        Usuario usuario = new Usuario(1, "Ana", "ana@example.com", "COMP-1");

        assertEquals(1, usuario.getId());
        assertEquals("Ana", usuario.getNombre());
        assertEquals("ana@example.com", usuario.getEmail());
        assertEquals("COMP-1", usuario.getComprobante());

        usuario.setNombre("Luis");
        usuario.setEmail("luis@example.com");
        usuario.setComprobante("COMP-2");

        assertEquals("Luis", usuario.getNombre());
        assertEquals("luis@example.com", usuario.getEmail());
        assertEquals("COMP-2", usuario.getComprobante());
    }

    @Test
    public void toString_incluye_campos_basicos() {
        Usuario usuario = new Usuario(2, "Marta", "marta@example.com", "COMP-3");

        String texto = usuario.toString();

        assertTrue(texto.contains("id=2"));
        assertTrue(texto.contains("nombre='Marta'"));
        assertTrue(texto.contains("email='marta@example.com'"));
        assertTrue(texto.contains("comprobante='COMP-3'"));
    }
}
