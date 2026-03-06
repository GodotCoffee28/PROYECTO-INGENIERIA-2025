package com.gesco.controllers;


import com.gesco.controllers.gestion_principal.DataBase;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LoginCedulaNegativaTest {

    private Path dataDirTemporal;
    private String dataDirAnterior;

    @Before
    public void setUp() throws IOException {
        dataDirAnterior = DataBase.getDataDir();
        dataDirTemporal = Files.createTempDirectory("gesco-login-cedula-");
        DataBase.setDataDir(dataDirTemporal.toString());
    }

    @After
    public void tearDown() throws IOException {
        if (dataDirAnterior != null && !dataDirAnterior.isBlank()) {
            DataBase.setDataDir(dataDirAnterior);
        }

        if (dataDirTemporal != null && Files.exists(dataDirTemporal)) {
            Files.walk(dataDirTemporal)
                .sorted((a, b) -> b.compareTo(a))
                .forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException ignored) {
                    }
                });
        }
    }

    @Test
    public void validarInicioSesion_cedulaNegativa_devuelveFalse() {
        assertFalse(DataBase.validarInicioSesion("-12345678", "password123"));
    }

    @Test
    public void validarInicioSesion_cedulaMenorA4Millones_devuelveFalse() {
        assertFalse(DataBase.validarInicioSesion("3999999", "password123"));
    }

    @Test
    public void validarInicioSesion_cedulaMayorA45Millones_devuelveFalse() {
        assertFalse(DataBase.validarInicioSesion("45000001", "password123"));
    }
}


