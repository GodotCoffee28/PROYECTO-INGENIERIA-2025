package com.gesco.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gesco.models.CCB;
import com.gesco.models.TipoUsuario;

public class DataBaseCcbDescuentoTest {

    private Path usuariosPath;
    private Path ccbPath;
    private byte[] usuariosBackup;
    private byte[] ccbBackup;

    @Before
    public void setUp() throws IOException {
        Path dataDir = Paths.get(System.getProperty("user.dir"))
            .resolve("src/main/java/com/gesco/models/data");

        usuariosPath = dataDir.resolve("usuarios.txt");
        ccbPath = dataDir.resolve("ccb.txt");

        Files.createDirectories(dataDir);

        if (Files.exists(usuariosPath)) {
            usuariosBackup = Files.readAllBytes(usuariosPath);
        } else {
            usuariosBackup = new byte[0];
            Files.createFile(usuariosPath);
        }

        if (Files.exists(ccbPath)) {
            ccbBackup = Files.readAllBytes(ccbPath);
        } else {
            ccbBackup = new byte[0];
            Files.createFile(ccbPath);
        }

        Files.write(usuariosPath, new byte[0]);
        Files.write(ccbPath, new byte[0]);
    }

    @After
    public void tearDown() throws IOException {
        if (usuariosPath != null && usuariosBackup != null) {
            Files.write(usuariosPath, usuariosBackup);
        }
        if (ccbPath != null && ccbBackup != null) {
            Files.write(ccbPath, ccbBackup);
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
