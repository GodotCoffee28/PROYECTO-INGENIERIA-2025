package com.gesco.controllers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ValidadorIdentidad {

    public boolean compararImagenes(File img1, File img2) throws IOException {
        BufferedImage bi1 = ImageIO.read(img1);
        BufferedImage bi2 = ImageIO.read(img2);

        if (bi1 == null || bi2 == null) {
            throw new IOException("No se pudo leer una o ambas imágenes. Verifique que sean archivos de imagen válidos.");
        }

        if (bi1.getWidth() != bi2.getWidth() || bi1.getHeight() != bi2.getHeight()) {
            return false;
        }

        for (int x = 0; x < bi1.getWidth(); x++) {
            for (int y = 0; y < bi1.getHeight(); y++) {
                if (bi1.getRGB(x, y) != bi2.getRGB(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }
}