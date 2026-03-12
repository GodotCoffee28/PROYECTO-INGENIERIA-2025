package com.gesco.controllers;

import java.time.LocalDate;

import org.junit.Test;

import com.gesco.models.costos.CCB;

public class CcbCajaNegraNegativaTest {

    @Test(expected = IllegalArgumentException.class)
    public void ccb_mermaMayorACien_lanzaExcepcion() {
        new CCB(LocalDate.now(), "Estudiante", 1000.0, 500.0, 100.0, 150.0, 0.25);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ccb_costoFijoNegativo_lanzaExcepcion() {
        new CCB(LocalDate.now(), "Estudiante", -1000.0, 500.0, 100.0, 0.0, 0.25);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ccb_costoVariableNegativo_lanzaExcepcion() {
        new CCB(LocalDate.now(), "Estudiante", 1000.0, -500.0, 100.0, 0.0, 0.25);
    }
}