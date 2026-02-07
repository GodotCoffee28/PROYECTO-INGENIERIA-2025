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
import java.util.HashMap;
import java.util.Map;

public class DataBase {
    private static final String ARCHIVO = "usuarios.txt";
    private static final String ADMIN_ARCHIVO = "admins.txt";
    private static final String DATA_DIR = "src/main/java/com/gesco/models/data";

    private static File obtenerArchivoUsuarios() {
        return obtenerArchivo(DATA_DIR, ARCHIVO);
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
            System.out.println("Error al crear el archivo de usuarios.");
        }
    }

    private static File obtenerArchivoAdmins() {
        return obtenerArchivo(DATA_DIR, ADMIN_ARCHIVO);
    }

    private static File obtenerArchivo(String carpeta, String nombreArchivo) {
        Path basePath = obtenerBaseProyecto();
        return basePath.resolve(carpeta).resolve(nombreArchivo).toFile();
    }

    private static Path obtenerBaseProyecto() {
        try {
            Path ubicacion = Paths.get(
                DataBase.class.getProtectionDomain().getCodeSource().getLocation().toURI()
            );
            if (ubicacion.endsWith("classes") && ubicacion.getParent() != null
                && ubicacion.getParent().getParent() != null) {
                return ubicacion.getParent().getParent();
            }
        } catch (URISyntaxException e) {
            // Fallback a user.dir si la URI no es valida.
        }

        return Paths.get(System.getProperty("user.dir"));
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
            System.out.println("Error al crear el archivo de admins.");
        }
    }

    // Nuevo registro: agrega saldo por defecto 0.0
    public static boolean registrarUsuario(String cedula, String clave, String nombre, String correo) {
        if (usuarioExiste(cedula)) {
            return false;
        }

        asegurarArchivo();

        String linea = String.format("%s:%s:%s:%s:0.0;%n", 
                valorSeguro(cedula), valorSeguro(clave), valorSeguro(nombre), valorSeguro(correo));

        File archivo = obtenerArchivoUsuarios();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo, true))) {
            writer.write(linea);
            return true;
        } catch (IOException e) {
            System.out.println("Error al registrar usuario.");
            return false;
        }
    }

    // Validación de login (solo cédula y clave)
    public static boolean validarInicioSesion(String cedula, String clave) {
        if (cedula == null || cedula.isBlank() || clave == null || clave.isBlank()) {
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

                String[] partes = linea.split(":", 5);
                if (partes.length < 4) {
                    continue;
                }

                String cedulaArchivo = partes[0].trim();
                String claveArchivo = partes[1].trim();

                if (cedula.equals(cedulaArchivo) && clave.equals(claveArchivo)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error al validar inicio de sesión.");
        }

        return false;
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

    // Nuevo método: obtener saldo del usuario (devuelve 0.0 si no existe o formato viejo)
    public static double obtenerSaldo(String cedula) {
        if (cedula == null || cedula.isBlank()) {
            return 0.0;
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
                if (partes.length < 1 || !cedula.equals(partes[0].trim())) {
                    continue;
                }

                // Si hay 5 partes (incluye saldo)
                if (partes.length >= 5) {
                    try {
                        return Double.parseDouble(partes[4].replace(";", "").trim());
                    } catch (NumberFormatException e) {
                        return 0.0;
                    }
                }
                // Formato viejo (sin saldo) → devuelve 0.0
                return 0.0;
            }
        } catch (IOException e) {
            System.out.println("Error al leer saldo.");
        }

        return 0.0;
    }

    // Nuevo método: actualizar saldo del usuario
    public static boolean actualizarSaldo(String cedula, double nuevoSaldo) {
        if (cedula == null || cedula.isBlank()) {
            return false;
        }

        asegurarArchivo();

        File archivo = obtenerArchivoUsuarios();
        File tempArchivo = new File(archivo.getParent(), "usuarios_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempArchivo))) {

            String linea;
            boolean encontrado = false;

            while ((linea = reader.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    writer.write(linea + System.lineSeparator());
                    continue;
                }

                String[] partes = linea.split(":", 5);
                if (partes.length < 4 || !cedula.equals(partes[0].trim())) {
                    writer.write(linea + System.lineSeparator());
                    continue;
                }

                // Reescribir con nuevo saldo
                String nuevaLinea = String.format("%s:%s:%s:%s:%.2f;%n",
                        partes[0].trim(), partes[1].trim(), partes[2].trim(), partes[3].trim(), nuevoSaldo);
                writer.write(nuevaLinea);
                encontrado = true;
            }

            if (!encontrado) {
                return false;
            }

        } catch (IOException e) {
            System.out.println("Error al actualizar saldo.");
            return false;
        }

        // Reemplazar archivo original
        if (archivo.delete() && tempArchivo.renameTo(archivo)) {
            return true;
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