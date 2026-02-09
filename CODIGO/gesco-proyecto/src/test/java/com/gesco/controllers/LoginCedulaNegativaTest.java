package com.gesco.controllers;

import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LoginCedulaNegativaTest {

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
    public void validarInicioSesion_cedulaNegativa_devuelveFalse() {
        assertFalse(DataBase.validarInicioSesion("-12345678", "password123"));
    }
}
