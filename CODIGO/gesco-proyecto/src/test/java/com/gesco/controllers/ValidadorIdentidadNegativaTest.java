package com.gesco.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gesco.controllers.gestion_principal.ValidadorIdentidad;

public class ValidadorIdentidadNegativaTest {

    private Path directorioTemporal;

    @Before
    public void setUp() throws IOException {
        directorioTemporal = Files.createTempDirectory("gesco-validador-negativo-");
    }

    @After
    public void tearDown() throws IOException {
        if (directorioTemporal != null && Files.exists(directorioTemporal)) {
            Files.walk(directorioTemporal)
                .sorted((a, b) -> b.compareTo(a))
                .forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException ignored) {
                    }
                });
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void compararImagenes_archivoNull_lanzaIllegalArgumentException() throws IOException {
        File imagenValida = crearArchivoTexto("imagen.txt", "esto no es una imagen").toFile();

        new ValidadorIdentidad().compararImagenes(null, imagenValida);
    }

    @Test(expected = IOException.class)
    public void compararImagenes_archivosNoSonImagenes_lanzaIOException() throws IOException {
        File archivo1 = crearArchivoTexto("archivo1.txt", "texto plano").toFile();
        File archivo2 = crearArchivoTexto("archivo2.txt", "otro texto plano").toFile();

        new ValidadorIdentidad().compararImagenes(archivo1, archivo2);
    }

    private Path crearArchivoTexto(String nombre, String contenido) throws IOException {
        Path archivo = directorioTemporal.resolve(nombre);
        Files.writeString(archivo, contenido, StandardCharsets.UTF_8);
        return archivo;
    }
}