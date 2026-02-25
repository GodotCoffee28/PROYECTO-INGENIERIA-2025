package com.gesco.controllers;


import com.gesco.controllers.gestion_principal.DataBase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gesco.models.usuarios.Usuario.TipoUsuario;

public class RegistroExitosoTest {

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
    public void registrarUsuario_datosValidos_devuelveTrue() {
        assertTrue(DataBase.registrarUsuario("87654321", "clave456", "Ana Garcia", "ana@email.com"));
    }

    @Test
    public void registrarUsuario_estudiante_guardaTipoUsuario() {
        assertTrue(DataBase.registrarUsuario("87654322", "clave456", "Ana Garcia", "ana@email.com", TipoUsuario.ESTUDIANTE));
        assertEquals(TipoUsuario.ESTUDIANTE, DataBase.obtenerTipoUsuario("87654322"));
    }

    @Test
    public void registrarUsuario_profesor_guardaTipoUsuario() {
        assertTrue(DataBase.registrarUsuario("87654323", "clave456", "Ana Garcia", "ana@email.com", TipoUsuario.PROFESOR));
        assertEquals(TipoUsuario.PROFESOR, DataBase.obtenerTipoUsuario("87654323"));
    }

    @Test
    public void registrarUsuario_empleado_guardaTipoUsuario() {
        assertTrue(DataBase.registrarUsuario("87654324", "clave456", "Ana Garcia", "ana@email.com", TipoUsuario.EMPLEADO));
        assertEquals(TipoUsuario.EMPLEADO, DataBase.obtenerTipoUsuario("87654324"));
    }
}


