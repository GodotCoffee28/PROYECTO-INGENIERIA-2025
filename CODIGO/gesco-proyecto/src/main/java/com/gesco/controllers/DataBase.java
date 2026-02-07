package com.gesco.controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DataBase {
    private static final String ARCHIVO = "data/usuarios.txt";
    private static final String ADMIN_ARCHIVO = "data/admins.txt";

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

    private static File obtenerArchivoAdmins() {
        try {
            Path basePath = Paths.get(
                DataBase.class.getProtectionDomain().getCodeSource().getLocation().toURI()
            );
            return basePath.resolve(ADMIN_ARCHIVO).toFile();
        } catch (URISyntaxException e) {
            return new File(ADMIN_ARCHIVO);
        }
    }

    private static void asegurarArchivoAdmins() {
        File file = obtenerArchivoAdmins();
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
            System.out.println("No se pudo crear el archivo de admins.");
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

                String[] partes = linea.split(":");
                if (partes.length < 2) {
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

    public static boolean registrarUsuario(String cedula, String clave, String nombre, String correo) {
        if (cedula == null || cedula.isBlank() || clave == null || clave.isBlank()) {
            return false;
        }

        asegurarArchivo();

        if (usuarioExiste(cedula)) {
            return false;
        }

        File archivo = obtenerArchivoUsuarios();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo, true))) {
            String linea = String.format("%s:%s:%s:%s", cedula.trim(), clave.trim(),
                valorSeguro(nombre), valorSeguro(correo));
            writer.write(linea);
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.out.println("Error al escribir el archivo de usuarios.");
            return false;
        }
    }

    public static boolean esAdmin(String cedula) {
        if (cedula == null || cedula.isBlank()) {
            return false;
        }

        asegurarArchivoAdmins();

        File archivo = obtenerArchivoAdmins();
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                if (cedula.trim().equals(linea.trim())) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de admins.");
        }

        return false;
    }

    private static boolean usuarioExiste(String cedula) {
        if (cedula == null || cedula.isBlank()) {
            return false;
        }

        File archivo = obtenerArchivoUsuarios();
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] partes = linea.split(":");
                if (partes.length < 1) {
                    continue;
                }

                String cedulaArchivo = partes[0].trim();
                if (cedula.equals(cedulaArchivo)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de usuarios.");
        }

        return false;
    }

    private static String valorSeguro(String valor) {
        return valor == null ? "" : valor.trim();
    }
}
