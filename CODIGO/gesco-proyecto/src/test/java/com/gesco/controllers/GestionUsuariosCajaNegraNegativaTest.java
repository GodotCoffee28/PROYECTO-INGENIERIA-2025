package com.gesco.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gesco.controllers.gestion_principal.DataBase;
import com.gesco.models.usuarios.Usuario.TipoUsuario;

public class GestionUsuariosCajaNegraNegativaTest {

    private Path dataDirTemporal;
    private String dataDirAnterior;

    @Before
    public void setUp() throws IOException {
        dataDirAnterior = DataBase.getDataDir();
        dataDirTemporal = Files.createTempDirectory("gesco-usuarios-negativo-");
        DataBase.setDataDir(dataDirTemporal.toString());

        Path carpetaSecretaria = dataDirTemporal.resolve("secretaria");
        Files.createDirectories(carpetaSecretaria);
        Files.writeString(
            carpetaSecretaria.resolve("cedulas_ocupaciones.txt"),
            String.join(
                System.lineSeparator(),
                "32653001:administrador",
                "32653002:estudiante:regular:Ana Cambio"
            ) + System.lineSeparator(),
            StandardCharsets.UTF_8
        );

        crearImagenSecretaria("32653001");
        crearImagenSecretaria("32653002");

        Files.writeString(dataDirTemporal.resolve("admins.txt"), "32653001" + System.lineSeparator(), StandardCharsets.UTF_8);
        Files.writeString(dataDirTemporal.resolve("super_admins.txt"), "32653001" + System.lineSeparator(), StandardCharsets.UTF_8);
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
    public void cambiarTipoUsuarioPorCedula_superAdminARolInferior_devuelveFalse() {
        assertTrue(DataBase.esSuperAdmin("32653001"));
        assertFalse(DataBase.cambiarTipoUsuarioPorCedula("32653001", TipoUsuario.EMPLEADO));
        assertTrue(DataBase.esSuperAdmin("32653001"));
    }

    @Test
    public void cambiarTipoUsuarioPorCedula_estudianteAComensal_devuelveFalse() {
        String cedula = "32653002";

        assertTrue(DataBase.registrarUsuario(cedula, "clave123", "Ana Cambio", "ana@email.com", TipoUsuario.ESTUDIANTE));
        assertFalse(DataBase.cambiarTipoUsuarioPorCedula(cedula, TipoUsuario.COMENSAL));
        assertEquals(TipoUsuario.ESTUDIANTE, DataBase.obtenerTipoUsuarioSecretaria(cedula));
        assertEquals(TipoUsuario.ESTUDIANTE, DataBase.obtenerTipoUsuario(cedula));
    }

    private void crearImagenSecretaria(String cedula) throws IOException {
        Files.writeString(
            dataDirTemporal.resolve("secretaria").resolve(cedula + ".jpg"),
            "img",
            StandardCharsets.UTF_8
        );
    }
}