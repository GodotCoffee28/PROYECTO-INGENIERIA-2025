package com.gesco.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class InicioSesionRegistroTest {

    private Path usuariosPath;
    private byte[] usuariosBackup;

    @Before
    public void setUp() throws IOException {
        usuariosPath = Paths.get(System.getProperty("user.dir"))
            .resolve("src/main/java/com/gesco/models/data/usuarios.txt");

        Files.createDirectories(usuariosPath.getParent());
        if (Files.exists(usuariosPath)) {
            usuariosBackup = Files.readAllBytes(usuariosPath);
        } else {
            usuariosBackup = new byte[0];
            Files.createFile(usuariosPath);
        }

        // Start each test with a clean usuarios.txt
        Files.write(usuariosPath, new byte[0]);
    }

    @After
    public void tearDown() throws IOException {
        if (usuariosPath != null && usuariosBackup != null) {
            Files.write(usuariosPath, usuariosBackup);
        }
    }

    @Test
    public void registrarUsuario_nuevoUsuario_permitaInicioSesion() {
        boolean guardado = DataBase.registrarUsuario("12345", "clave", "Ana", "ana@correo.com");

        assertTrue(guardado);
        assertTrue(DataBase.validarInicioSesion("12345", "clave"));
        assertEquals("Ana", DataBase.obtenerNombre("12345"));
    }

    @Test
    public void registrarUsuario_duplicado_falla() {
        DataBase.registrarUsuario("222", "pass", "Luis", "luis@correo.com");

        boolean guardado = DataBase.registrarUsuario("222", "otra", "Luis2", "l2@correo.com");

        assertFalse(guardado);
    }

    @Test
    public void validarInicioSesion_credencialesInvalidas_devuelveFalse() throws IOException {
        Files.write(usuariosPath, "111:pass:Pedro:pedro@correo.com:0.0;\n".getBytes(StandardCharsets.UTF_8));

        assertFalse(DataBase.validarInicioSesion("111", "bad"));
        assertFalse(DataBase.validarInicioSesion("999", "pass"));
    }
}