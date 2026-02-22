package com.gesco.controllers;

import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RegistroCedulaNegativaTest {

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

        Files.write(usuariosPath, new byte[0]);
    }

    @After
    public void tearDown() throws IOException {
        if (usuariosPath != null && usuariosBackup != null) {
            Files.write(usuariosPath, usuariosBackup);
        }
    }

    @Test
    public void registrarUsuario_cedulaNegativa_devuelveFalse() {
        assertFalse(DataBase.registrarUsuario("-12345678", "password123", "Maria Lopez", "maria@email.com"));
    }

    @Test
    public void registrarUsuario_cedulaMenorA8Millones_devuelveFalse() {
        assertFalse(DataBase.registrarUsuario("7999999", "password123", "Maria Lopez", "maria@email.com"));
    }
}
