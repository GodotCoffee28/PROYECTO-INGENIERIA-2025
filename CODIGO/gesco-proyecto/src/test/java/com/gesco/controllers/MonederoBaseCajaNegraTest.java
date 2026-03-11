package com.gesco.controllers;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gesco.controllers.gestion_principal.DataBase;

public class MonederoBaseCajaNegraTest {

    protected Path dataDirTemporal;
    protected String dataDirAnterior;

    @Before
    public void setUpBase() throws IOException {
        dataDirAnterior = DataBase.getDataDir();
        dataDirTemporal = Files.createTempDirectory("gesco-monedero-caja-negra-");
        DataBase.setDataDir(dataDirTemporal.toString());

        Path carpetaSecretaria = dataDirTemporal.resolve("secretaria");
        Files.createDirectories(carpetaSecretaria);
        Files.writeString(
            carpetaSecretaria.resolve("cedulas_ocupaciones.txt"),
            "33333333:estudiante" + System.lineSeparator()
                + "33444444:estudiante" + System.lineSeparator()
                + "33555555:estudiante:becario" + System.lineSeparator()
                + "33666666:estudiante:exonerado" + System.lineSeparator(),
            java.nio.charset.StandardCharsets.UTF_8
        );

        Files.writeString(carpetaSecretaria.resolve("33333333.jpg"), "img", java.nio.charset.StandardCharsets.UTF_8);
        Files.writeString(carpetaSecretaria.resolve("33444444.jpg"), "img", java.nio.charset.StandardCharsets.UTF_8);
        Files.writeString(carpetaSecretaria.resolve("33555555.jpg"), "img", java.nio.charset.StandardCharsets.UTF_8);
        Files.writeString(carpetaSecretaria.resolve("33666666.jpg"), "img", java.nio.charset.StandardCharsets.UTF_8);
    }

    @After
    public void tearDownBase() throws IOException {
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
    public void entornoTemporal_baseMonedero_seInicializaCorrectamente() {
        Path carpetaSecretaria = dataDirTemporal.resolve("secretaria");
        assertTrue(Files.exists(dataDirTemporal));
        assertTrue(Files.isDirectory(carpetaSecretaria));
        assertTrue(Files.exists(carpetaSecretaria.resolve("cedulas_ocupaciones.txt")));
    }
}
