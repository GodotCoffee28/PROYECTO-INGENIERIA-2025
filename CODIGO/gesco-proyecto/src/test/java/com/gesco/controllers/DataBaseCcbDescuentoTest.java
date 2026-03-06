package com.gesco.controllers;


import com.gesco.controllers.gestion_principal.DataBase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gesco.models.costos.CCB;
import com.gesco.models.usuarios.Usuario.TipoUsuario;

public class DataBaseCcbDescuentoTest {

    private Path dataDirTemporal;
    private String dataDirAnterior;

    @Before
    public void setUp() throws IOException {
        dataDirAnterior = DataBase.getDataDir();
        dataDirTemporal = Files.createTempDirectory("gesco-ccb-descuento-");
        DataBase.setDataDir(dataDirTemporal.toString());

        Path carpetaSecretaria = dataDirTemporal.resolve("secretaria");
        Files.createDirectories(carpetaSecretaria);
        Files.writeString(
            carpetaSecretaria.resolve("cedulas_ocupaciones.txt"),
            "12345678:estudiante" + System.lineSeparator(),
            java.nio.charset.StandardCharsets.UTF_8
        );
        Files.writeString(carpetaSecretaria.resolve("12345678.jpg"), "img", java.nio.charset.StandardCharsets.UTF_8);
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
    public void calcularMontoCcbPorTipo_estudiante_porcentajeValido() {
        double monto = DataBase.calcularMontoCcbPorTipo(100.0, TipoUsuario.ESTUDIANTE, 0.25);
        assertEquals(25.0, monto, 0.0001);
    }

    @Test
    public void calcularMontoCcbPorTipo_profesor_porcentajeValido() {
        double monto = DataBase.calcularMontoCcbPorTipo(100.0, TipoUsuario.PROFESOR, 0.80);
        assertEquals(80.0, monto, 0.0001);
    }

    @Test
    public void calcularMontoCcbPorTipo_empleado_porcentajeValido() {
        double monto = DataBase.calcularMontoCcbPorTipo(100.0, TipoUsuario.EMPLEADO, 1.00);
        assertEquals(100.0, monto, 0.0001);
    }

    @Test
    public void calcularMontoCcbPorTipo_profesor_porcentajeFueraDeRango_lanzaExcepcion() {
        try {
            DataBase.calcularMontoCcbPorTipo(100.0, TipoUsuario.PROFESOR, 0.60);
            fail("Se esperaba IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("rango"));
        }
    }

    @Test
    public void calcularMontoCcbParaCedula_usandoTipoGuardado() {
        DataBase.registrarUsuario("12345678", "clave123", "Juan Perez", "juan@email.com", TipoUsuario.ESTUDIANTE);
        DataBase.guardarCcb(new CCB(LocalDate.now(), "Estudiante", 1000.0, 500.0, 100.0, 0.0));

        double monto = DataBase.calcularMontoCcbParaCedula("12345678", 0.20);
        assertEquals(3.0, monto, 0.0001);
    }
}


