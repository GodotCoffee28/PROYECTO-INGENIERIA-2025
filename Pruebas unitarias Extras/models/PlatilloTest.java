package com.gesco.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class PlatilloTest {

    @Test
    public void constructor_por_defecto_agrega_un_insumo() {
        Platillo platillo = new Platillo();

        assertEquals("Nuevo Platillo", platillo.getNombre());
        assertEquals(1, platillo.getInsumos().size());
    }

    @Test
    public void agregar_insumo_ignora_null() {
        Platillo platillo = new Platillo("Pasta");

        platillo.agregarInsumo(null);
        assertEquals(0, platillo.getInsumos().size());

        platillo.agregarInsumo(new Insumo("Salsa", 1, "Natural"));
        assertEquals(1, platillo.getInsumos().size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getInsumos_es_inmodificable() {
        Platillo platillo = new Platillo("Pasta");

        platillo.getInsumos().add(new Insumo("Queso", 1, "Proteina"));
    }

    @Test
    public void toString_muestra_el_nombre() {
        Platillo platillo = new Platillo("Pasta");

        assertNotNull(platillo.toString());
        assertEquals("Pasta", platillo.toString());
    }
}
