package com.gesco.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DataBase {
    private static final String ARCHIVO = "usuarios.txt";

    private static File obtenerArchivoUsuarios() {
        try {
            Path basePath = Paths.get(
                DataBase.class.getProtectionDomain().getCodeSource().getLocation().toURI()
            );
            return basePath.resolve(ARCHIVO).toFile();
        } catch (URISyntaxException e) {
            return new File(ARCHIVO);
        }
    }

    private static void asegurarArchivo() {
        File file = obtenerArchivoUsuarios();
        if (file.exists()) {
            return;
        }

        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try {
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("No se pudo crear el archivo de usuarios.");
        }
    }

    public static boolean validarInicioSesion(String usuario, String clave) {
        if (usuario == null || usuario.isBlank() || clave == null || clave.isBlank()) {
            return false;
        }

        asegurarArchivo();

        File archivo = obtenerArchivoUsuarios();
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] partes = linea.split(":", 2);
                if (partes.length != 2) {
                    continue;
                }

                String usuarioArchivo = partes[0].trim();
                String claveArchivo = partes[1].trim();

                if (usuario.equals(usuarioArchivo) && clave.equals(claveArchivo)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de usuarios.");
        }

        return false;
    }
}
