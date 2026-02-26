package com.gesco.controllers;


import com.gesco.controllers.gestion_principal.DataBase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gesco.models.usuarios.Usuario.TipoUsuario;

public class RegistroExitosoTest {

    private Path dataDirTemporal;
    private String dataDirAnterior;

    @Before
    public void setUp() throws IOException {
        dataDirAnterior = DataBase.getDataDir();
        dataDirTemporal = Files.createTempDirectory("gesco-registro-exitoso-");
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
    public void registrarUsuario_datosValidos_devuelveTrue() {
        assertTrue(DataBase.registrarUsuario("32654321", "clave456", "Ana Garcia", "ana@email.com"));
    }

    @Test
    public void registrarUsuario_estudiante_guardaTipoUsuario() {
        assertTrue(DataBase.registrarUsuario("32654322", "clave456", "Ana Garcia", "ana@email.com", TipoUsuario.ESTUDIANTE));
        assertEquals(TipoUsuario.ESTUDIANTE, DataBase.obtenerTipoUsuario("32654322"));
    }

    @Test
    public void registrarUsuario_profesor_guardaTipoUsuario() {
        assertTrue(DataBase.registrarUsuario("32654323", "clave456", "Ana Garcia", "ana@email.com", TipoUsuario.PROFESOR));
        assertEquals(TipoUsuario.PROFESOR, DataBase.obtenerTipoUsuario("32654323"));
    }

    @Test
    public void registrarUsuario_empleado_guardaTipoUsuario() {
        assertTrue(DataBase.registrarUsuario("32654324", "clave456", "Ana Garcia", "ana@email.com", TipoUsuario.EMPLEADO));
        assertEquals(TipoUsuario.EMPLEADO, DataBase.obtenerTipoUsuario("32654324"));
    }
}


