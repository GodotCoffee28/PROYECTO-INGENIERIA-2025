package com.gesco.controllers;


import com.gesco.controllers.gestion_principal.DataBase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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

        Path carpetaSecretaria = dataDirTemporal.resolve("secretaria");
        Files.createDirectories(carpetaSecretaria);
        Files.writeString(
            carpetaSecretaria.resolve("cedulas_ocupaciones.txt"),
            "32654321:estudiante" + System.lineSeparator()
                + "32654322:estudiante" + System.lineSeparator()
                + "32654323:profesor" + System.lineSeparator()
                + "32654324:trabajador" + System.lineSeparator()
                + "32654325:estudiante:becario" + System.lineSeparator()
                + "32654326:exonerado" + System.lineSeparator()
                + "32654327:estudiante:exonerado:Juan Exonerado" + System.lineSeparator(),
            java.nio.charset.StandardCharsets.UTF_8
        );
            Files.writeString(carpetaSecretaria.resolve("32654321.jpg"), "img", java.nio.charset.StandardCharsets.UTF_8);
            Files.writeString(carpetaSecretaria.resolve("32654322.jpg"), "img", java.nio.charset.StandardCharsets.UTF_8);
            Files.writeString(carpetaSecretaria.resolve("32654323.jpg"), "img", java.nio.charset.StandardCharsets.UTF_8);
            Files.writeString(carpetaSecretaria.resolve("32654324.jpg"), "img", java.nio.charset.StandardCharsets.UTF_8);
            Files.writeString(carpetaSecretaria.resolve("32654325.jpg"), "img", java.nio.charset.StandardCharsets.UTF_8);
            Files.writeString(carpetaSecretaria.resolve("32654326.jpg"), "img", java.nio.charset.StandardCharsets.UTF_8);
            Files.writeString(carpetaSecretaria.resolve("32654327.jpg"), "img", java.nio.charset.StandardCharsets.UTF_8);
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

    @Test
    public void registrarUsuario_nombreConNumeros_devuelveFalse() {
        assertFalse(DataBase.registrarUsuario("32654324", "clave456", "Ana1 Garcia", "ana@email.com", TipoUsuario.EMPLEADO));
    }

    @Test
    public void obtenerTipoUsuarioSecretaria_estudianteBecario_resuelveBecario() {
        assertEquals(TipoUsuario.BECARIO, DataBase.obtenerTipoUsuarioSecretaria("32654325"));
    }

    @Test
    public void registrarUsuario_becario_guardaTipoUsuario() {
        assertTrue(DataBase.registrarUsuario("32654325", "clave456", "Ana Garcia", "ana@email.com", TipoUsuario.BECARIO));
        assertEquals(TipoUsuario.BECARIO, DataBase.obtenerTipoUsuario("32654325"));
    }

    @Test
    public void registrarUsuario_exonerado_guardaTipoUsuario() {
        assertTrue(DataBase.registrarUsuario("32654326", "clave456", "Ana Garcia", "ana@email.com", TipoUsuario.EXONERADO));
        assertEquals(TipoUsuario.EXONERADO, DataBase.obtenerTipoUsuario("32654326"));
    }

    @Test
    public void obtenerTipoUsuarioSecretaria_estudianteExoneradoConNombre_resuelveExonerado() {
        assertEquals(TipoUsuario.EXONERADO, DataBase.obtenerTipoUsuarioSecretaria("32654327"));
    }

    @Test
    public void registrarUsuario_desdePadronEstudianteExoneradoConNombre_guardaExonerado() {
        TipoUsuario tipoPadron = DataBase.obtenerTipoUsuarioSecretaria("32654327");
        assertEquals(TipoUsuario.EXONERADO, tipoPadron);
        assertTrue(DataBase.registrarUsuario("32654327", "clave456", "Juan Exonerado", "juan@email.com", tipoPadron));
        assertEquals(TipoUsuario.EXONERADO, DataBase.obtenerTipoUsuario("32654327"));
    }
}


