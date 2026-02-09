package com.gesco.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Test;

public class MenuTest {

    @Test
    public void constructor_por_defecto_agrega_un_platillo() {
        Menu menu = new Menu();

        assertNotNull(menu.getFecha());
        assertEquals(1, menu.getPlatillos().size());
    }

    @Test
    public void agregar_platillo_ignora_null() {
        LocalDate fecha = LocalDate.of(2025, 2, 9);
        Menu menu = new Menu(fecha);

        menu.agregarPlatillo(null);
        assertEquals(0, menu.getPlatillos().size());

        menu.agregarPlatillo(new Platillo("Arepa"));
        assertEquals(1, menu.getPlatillos().size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getPlatillos_es_inmodificable() {
        Menu menu = new Menu(LocalDate.of(2025, 2, 9));

        menu.getPlatillos().add(new Platillo("Arepa"));
    }

    @Test
    public void toString_incluye_fecha_formateada() {
        LocalDate fecha = LocalDate.of(2025, 2, 9);
        Menu menu = new Menu(fecha);

        String texto = menu.toString();

        assertTrue(texto.contains("09/02/2025"));
        assertTrue(texto.contains("MENU"));
    }
}
