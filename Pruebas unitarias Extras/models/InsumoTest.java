package com.gesco.models;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class InsumoTest {

    @Test
    public void toString_incluye_nombre_cantidad_y_tipo() {
        Insumo insumo = new Insumo("Aceite", 2, "Natural");

        assertEquals("Aceite (2 unidades - Natural)", insumo.toString());
    }
}
