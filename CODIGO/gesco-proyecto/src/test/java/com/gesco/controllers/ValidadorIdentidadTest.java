package com.gesco.controllers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gesco.controllers.gestion_principal.ValidadorIdentidad;

public class ValidadorIdentidadTest {

    private Path directorioTemporal;

    @Before
    public void setUp() throws IOException {
        directorioTemporal = Files.createTempDirectory("gesco-validador-positivo-");
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

    @Test
    public void compararImagenes_imagenesIguales_devuelveTrue() throws IOException {
        File imagen1 = crearImagen("igual1.png", 10, 10, Color.BLUE);
        File imagen2 = crearImagen("igual2.png", 10, 10, Color.BLUE);

        assertTrue(new ValidadorIdentidad().compararImagenes(imagen1, imagen2));
    }

    @Test
    public void compararImagenes_imagenesDistintas_devuelveFalse() throws IOException {
        File imagen1 = crearImagen("distinta1.png", 10, 10, Color.BLUE);
        File imagen2 = crearImagen("distinta2.png", 10, 10, Color.RED);

        assertFalse(new ValidadorIdentidad().compararImagenes(imagen1, imagen2));
    }

    private File crearImagen(String nombre, int ancho, int alto, Color color) throws IOException {
        BufferedImage imagen = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < ancho; x++) {
            for (int y = 0; y < alto; y++) {
                imagen.setRGB(x, y, color.getRGB());
            }
        }

        File archivo = directorioTemporal.resolve(nombre).toFile();
        ImageIO.write(imagen, "png", archivo);
        return archivo;
    }
}