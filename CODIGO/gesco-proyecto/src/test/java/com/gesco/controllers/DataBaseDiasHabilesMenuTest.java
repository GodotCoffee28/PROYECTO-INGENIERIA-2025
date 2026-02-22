package com.gesco.controllers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Test;

public class DataBaseDiasHabilesMenuTest {

    @Test
    public void esDiaHabil_sabadoYDomingo_devuelveFalse() {
        assertFalse(DataBase.esDiaHabil(LocalDate.of(2026, 2, 21))); // sábado
        assertFalse(DataBase.esDiaHabil(LocalDate.of(2026, 2, 22))); // domingo
    }

    @Test
    public void esDiaHabil_lunesNoFeriado_devuelveTrue() {
        assertTrue(DataBase.esDiaHabil(LocalDate.of(2026, 2, 23))); // lunes
    }
}
