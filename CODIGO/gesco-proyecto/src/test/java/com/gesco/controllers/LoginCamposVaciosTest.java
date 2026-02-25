package com.gesco.controllers;


import com.gesco.controllers.gestion_principal.DataBase;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LoginCamposVaciosTest {

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
    public void validarInicioSesion_camposVacios_devuelveFalse() {
        assertFalse(DataBase.validarInicioSesion("", ""));
    }
}


