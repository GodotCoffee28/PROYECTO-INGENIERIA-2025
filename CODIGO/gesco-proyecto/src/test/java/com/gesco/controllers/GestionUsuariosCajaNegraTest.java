package com.gesco.controllers;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gesco.controllers.gestion_principal.DataBase;
import com.gesco.models.usuarios.Usuario.TipoUsuario;

public class GestionUsuariosCajaNegraTest {

    private Path dataDirTemporal;
    private String dataDirAnterior;

    @Before
    public void setUp() throws IOException {
        dataDirAnterior = DataBase.getDataDir();
        dataDirTemporal = Files.createTempDirectory("gesco-usuarios-caja-negra-");
        DataBase.setDataDir(dataDirTemporal.toString());

        Path carpetaSecretaria = dataDirTemporal.resolve("secretaria");
        Files.createDirectories(carpetaSecretaria);
        Files.writeString(
            carpetaSecretaria.resolve("cedulas_ocupaciones.txt"),
            "44556677:trabajador" + System.lineSeparator()
                + "35667788:estudiante" + System.lineSeparator(),
            java.nio.charset.StandardCharsets.UTF_8
        );
            Files.writeString(carpetaSecretaria.resolve("44556677.jpg"), "img", java.nio.charset.StandardCharsets.UTF_8);
            Files.writeString(carpetaSecretaria.resolve("35667788.jpg"), "img", java.nio.charset.StandardCharsets.UTF_8);
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
    public void registrarUsuario_conDatosValidos_devuelveTrue() {
        boolean registrado = DataBase.registrarUsuario(
            "44556677",
            "clave123",
            "Usuario Valido",
            "usuario@email.com",
            TipoUsuario.EMPLEADO
        );
        assertTrue(registrado);
    }

    @Test
    public void validarInicioSesion_conCredencialesCorrectas_devuelveTrue() {
        String cedula = "35667788";
        String clave = "contra123";

        assertTrue(DataBase.registrarUsuario(cedula, clave, "Login", "login@email.com", TipoUsuario.ESTUDIANTE));
        assertTrue(DataBase.validarInicioSesion(cedula, clave));
    }
}
