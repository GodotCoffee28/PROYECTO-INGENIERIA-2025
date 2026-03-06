package com.gesco.controllers.gestion_principal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import javax.imageio.ImageIO;

import com.gesco.models.costos.CCB;
import com.gesco.models.menu.Insumo;
import com.gesco.models.menu.Menu;
import com.gesco.models.menu.Menu.EstadoMenu;
import com.gesco.models.menu.Platillo;
import com.gesco.models.usuarios.Usuario.TipoUsuario;

public class DataBase {

    private static final String ARCHIVO_USUARIOS = "usuarios.txt";
    private static final String ARCHIVO_ADMINS = "admins.txt";
    private static final String ARCHIVO_SUPER_ADMINS = "super_admins.txt";
    private static final String ARCHIVO_ADMINS_AUTORIZADOS = "admins_autorizados.txt";
    private static final String ARCHIVO_MENUS = "menus.txt";
    private static final String ARCHIVO_CFCV = "cfcv.txt";
    private static final String ARCHIVO_CCB = "ccb.txt";
    private static final String ARCHIVO_INSUMOS = "insumos.txt";
    private static final String ARCHIVO_FERIADOS = "feriados.txt";
    private static final String ARCHIVO_NO_LABORABLES = "sabados_domingos_2026.txt";
    private static final String CARPETA_SECRETARIA = "secretaria";
    private static final String CARPETA_IMAGENES_SECRETARIA = "imagenes_rostros";
    private static final String ARCHIVO_PADRON_SECRETARIA = "cedulas_ocupaciones.txt";
    private static final String ARCHIVO_REGISTRO_SALDO = "registroSaldo.txt";

    private static final long CEDULA_MINIMA = 8_000_000L;
    private static final long CEDULA_MAXIMA = 45_000_000L;
    private static final double CCB_ESTUDIANTE_MIN = 0.20;
    private static final double CCB_ESTUDIANTE_MAX = 0.30;
    private static final double CCB_PROFESOR_MIN = 0.70;
    private static final double CCB_PROFESOR_MAX = 0.90;
    private static final double CCB_EMPLEADO_MIN = 0.90;
    private static final double CCB_EMPLEADO_MAX = 1.10;
    public static final int CANTIDAD_MAXIMA_INSUMO = 1000;
    public static final float PRECIO_MAXIMO_INSUMO = 1000.0f;
    private static final Set<String> FERIADOS_FIJOS_MM_DD = Set.of();

    private static String DATA_DIR = "src/main/java/com/gesco/models/data";

    static {
        String prop = System.getProperty("gesco.data.dir");
        String env = System.getenv("GESCO_DATA_DIR");
        if (prop != null && !prop.isBlank()) {
            DATA_DIR = prop.trim();
        } else if (env != null && !env.isBlank()) {
            DATA_DIR = env.trim();
        }
    }

    public static void setDataDir(String dataDir) {
        if (dataDir != null && !dataDir.isBlank()) DATA_DIR = dataDir.trim();
    }

    public static String getDataDir() { return DATA_DIR; }

    public static boolean registrarRecarga(String referencia, double monto, String banco, String fecha, String cedula) {
        String referenciaLimpia = valorSeguro(referencia);
        if (referenciaLimpia.isBlank() || referenciaRecargaExiste(referenciaLimpia)) {
            return false;
        }

        double montoRedondeado = redondearMoneda(monto);
        String linea = String.format(
            Locale.ROOT,
            "%s:%.2f:%s:%s:%s",
            referenciaLimpia,
            montoRedondeado,
            valorSeguro(banco),
            valorSeguro(fecha),
            valorSeguro(cedula)
        );
        return escribirLineaGenerica(ARCHIVO_REGISTRO_SALDO, linea + System.lineSeparator());
    }

    public static List<String[]> obtenerRecargasPorCedula(String cedula) {
        String cedulaLimpia = normalizarCedula(cedula);
        List<String[]> recargas = new ArrayList<>();
        if (!cedulaValida(cedulaLimpia)) {
            return recargas;
        }

        List<String> lineas = leerLineasGenericas(ARCHIVO_REGISTRO_SALDO);
        for (String linea : lineas) {
            String[] partes = linea.split(":");
            if (partes.length < 5) {
                continue;
            }

            String cedulaRegistro = normalizarCedula(partes[4]);
            if (!cedulaLimpia.equals(cedulaRegistro)) {
                continue;
            }

            recargas.add(new String[] {
                partes[0].trim(),
                partes[1].trim(),
                partes[2].trim(),
                partes[3].trim(),
                cedulaRegistro
            });
        }

        return recargas;
    }

    public static boolean referenciaRecargaExiste(String referencia) {
        String referenciaLimpia = valorSeguro(referencia);
        if (referenciaLimpia.isBlank()) return false;

        List<String> lineas = leerLineasGenericas(ARCHIVO_REGISTRO_SALDO);
        for (String linea : lineas) {
            String[] partes = linea.split(":");
            if (partes.length > 0 && referenciaLimpia.equals(partes[0].trim())) {
                return true;
            }
        }
        return false;
    }
    
    public static File obtenerCarpetaSecretaria() {
        Path basePath = obtenerBaseProyecto();
        Path dataPath = Paths.get(DATA_DIR);
        Path secretariaPath;

        if (dataPath.isAbsolute()) {
            secretariaPath = dataPath.resolve(CARPETA_SECRETARIA);
        } else {
            secretariaPath = basePath.resolve(DATA_DIR).resolve(CARPETA_SECRETARIA);
        }

        return secretariaPath.toFile();
    }

    private static List<String> leerLineasSecretaria(String nombreArchivo) {
        List<String> lineas = new ArrayList<>();
        File carpeta = obtenerCarpetaSecretaria();
        File archivo = new File(carpeta, nombreArchivo);

        if (!archivo.exists()) return lineas;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo, StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    lineas.add(linea);
                }
            }
        } catch (IOException e) {
            System.err.println("Error leyendo archivo de secretaría " + nombreArchivo + ": " + e.getMessage());
        }
        return lineas;
    }

    public static File obtenerImagenSecretaria(String cedula) {
        Path secretariaPath = obtenerCarpetaSecretaria().toPath();
        Path imagenesPath = obtenerCarpetaImagenesSecretaria().toPath();
        String base = valorSeguro(cedula);
        File jpg = imagenesPath.resolve(base + ".jpg").toFile();
        if (jpg.exists()) return jpg;

        File jpeg = imagenesPath.resolve(base + ".jpeg").toFile();
        if (jpeg.exists()) return jpeg;

        File png = imagenesPath.resolve(base + ".png").toFile();
        if (png.exists()) return png;

        File jpgLegacy = secretariaPath.resolve(base + ".jpg").toFile();
        if (jpgLegacy.exists()) return jpgLegacy;

        File jpegLegacy = secretariaPath.resolve(base + ".jpeg").toFile();
        if (jpegLegacy.exists()) return jpegLegacy;

        File pngLegacy = secretariaPath.resolve(base + ".png").toFile();
        if (pngLegacy.exists()) return pngLegacy;

        return jpg;
    }

    public static File obtenerCarpetaImagenesSecretaria() {
        File carpetaSecretaria = obtenerCarpetaSecretaria();
        File carpetaImagenes = new File(carpetaSecretaria, CARPETA_IMAGENES_SECRETARIA);
        if (!carpetaImagenes.exists()) {
            carpetaImagenes.mkdirs();
        }
        return carpetaImagenes;
    }

    public static boolean cedulaTieneImagenSecretaria(String cedula) {
        String cedulaLimpia = normalizarCedula(cedula);
        if (!cedulaValida(cedulaLimpia)) return false;

        File imagen = obtenerImagenSecretaria(cedulaLimpia);
        return imagen.exists() && imagen.isFile();
    }

    public static boolean asegurarImagenSecretariaParaCedula(String cedula) {
        String cedulaLimpia = normalizarCedula(cedula);
        if (!cedulaValida(cedulaLimpia)) return false;

        if (cedulaTieneImagenSecretaria(cedulaLimpia)) {
            return true;
        }

        File carpetaImagenes = obtenerCarpetaImagenesSecretaria();
        File destino = new File(carpetaImagenes, cedulaLimpia + ".png");

        BufferedImage imagen = new BufferedImage(360, 360, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = imagen.createGraphics();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int hash = Math.abs(cedulaLimpia.hashCode());
            int r = 70 + (hash % 120);
            int g = 70 + ((hash / 7) % 120);
            int b = 70 + ((hash / 13) % 120);

            g2.setColor(new Color(r, g, b));
            g2.fillRect(0, 0, 360, 360);

            g2.setColor(new Color(245, 245, 245));
            g2.fillOval(100, 70, 160, 160);
            g2.fillRoundRect(85, 215, 190, 110, 30, 30);

            g2.setColor(new Color(30, 30, 30));
            g2.setFont(new Font("Arial", Font.BOLD, 22));
            g2.drawString("ID " + cedulaLimpia, 95, 340);

            return ImageIO.write(imagen, "png", destino);
        } catch (IOException ex) {
            return false;
        } finally {
            g2.dispose();
        }
    }

    private static File obtenerArchivo(String nombreArchivo) {
        Path basePath = obtenerBaseProyecto();
        Path dataPath = Paths.get(DATA_DIR);
        if (dataPath.isAbsolute()) {
            return dataPath.resolve(nombreArchivo).toFile();
        } else {
            return basePath.resolve(DATA_DIR).resolve(nombreArchivo).toFile();
        }
    }

    private static void asegurarArchivoGenerico(String nombreArchivo) {
        File file = obtenerArchivo(nombreArchivo);
        if (!file.exists()) {
            try {
                File parent = file.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Error creando estructura para: " + nombreArchivo);
            }
        }
    }

    private static boolean escribirLineaGenerica(String nombreArchivo, String contenido) {
        asegurarArchivoGenerico(nombreArchivo);
        File archivo = obtenerArchivo(nombreArchivo);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo, StandardCharsets.UTF_8, true))) {
            writer.write(contenido);
            return true;
        } catch (IOException e) {
            System.err.println("Error escribiendo en " + nombreArchivo + ": " + e.getMessage());
            return false;
        }
    }

    private static List<String> leerLineasGenericas(String nombreArchivo) {
        List<String> lineas = new ArrayList<>();
        File archivo = obtenerArchivo(nombreArchivo);

        if (!archivo.exists()) return lineas;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo, StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    lineas.add(linea);
                }
            }
        } catch (IOException e) {
            System.err.println("Error leyendo " + nombreArchivo + ": " + e.getMessage());
        }
        return lineas;
    }

    private static boolean reescribirArchivoGenerico(String nombreArchivo, List<String> nuevasLineas) {
        asegurarArchivoGenerico(nombreArchivo);
        File archivo = obtenerArchivo(nombreArchivo);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo, false))) {
            for (String linea : nuevasLineas) {
                writer.write(linea);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error reescribiendo " + nombreArchivo);
            return false;
        }
    }

    public static boolean registrarUsuario(String cedula, String clave, String nombre, String correo) {
        return registrarUsuario(cedula, clave, nombre, correo, TipoUsuario.ESTUDIANTE);
    }

    public static boolean registrarUsuario(String cedula, String clave, String nombre, String correo, TipoUsuario tipoUsuario) {
        String cedulaLimpia = normalizarCedula(cedula);
        String nombreLimpio = valorSeguro(nombre);
        if (!cedulaValida(cedulaLimpia)) return false;
        if (!nombreValido(nombreLimpio)) return false;
        if (usuarioExiste(cedulaLimpia)) return false;

        TipoUsuario tipo = tipoUsuario == null ? TipoUsuario.COMENSAL : tipoUsuario;
        if (tipo == TipoUsuario.SUPER_ADMIN) {
            return false;
        }

        if (tipo != TipoUsuario.ADMIN) {
            if (!cedulaAutorizadaPorSecretaria(cedulaLimpia)) return false;
            if (!tipoUsuarioCoincideConSecretaria(cedulaLimpia, tipo)) return false;
        }

        if (!asegurarImagenSecretariaParaCedula(cedulaLimpia)) return false;

        String linea = String.format("%s:%s:%s:%s:%s:0.0;",
            cedulaLimpia, valorSeguro(clave), nombreLimpio, valorSeguro(correo), tipo.toEtiqueta());

        return escribirLineaGenerica(ARCHIVO_USUARIOS, linea + System.lineSeparator());
    }

    public static boolean cedulaAutorizadaPorSecretaria(String cedula) {
        return obtenerTipoUsuarioSecretaria(cedula) != null;
    }

    public static TipoUsuario obtenerTipoUsuarioSecretaria(String cedula) {
        String cedulaLimpia = normalizarCedula(cedula);
        if (!cedulaValida(cedulaLimpia)) return null;

        List<String> lineas = leerLineasSecretaria(ARCHIVO_PADRON_SECRETARIA);
        for (String linea : lineas) {
            String limpia = valorSeguro(linea);
            if (limpia.isEmpty() || limpia.startsWith("#")) continue;

            String[] partes = limpia.split(":");
            if (partes.length < 2) continue;

            String cedulaPadron = normalizarCedula(partes[0]);
            if (!cedulaLimpia.equals(cedulaPadron)) continue;

            return mapearTipoSecretaria(partes[1]);
        }
        return null;
    }

    public static boolean tipoUsuarioCoincideConSecretaria(String cedula, TipoUsuario tipoUsuario) {
        TipoUsuario tipoPadron = obtenerTipoUsuarioSecretaria(cedula);
        if (tipoPadron == null || tipoUsuario == null) return false;

        TipoUsuario tipoSolicitado = tipoUsuario == TipoUsuario.COMENSAL
                ? TipoUsuario.ESTUDIANTE
                : tipoUsuario;
        return tipoPadron == tipoSolicitado;
    }

    public static boolean registrarAdministrador(String cedula, String clave, String nombre, String correo, String codigoAutorizacion) {
        if (!adminAutorizadoPorSuperAdmin(cedula, codigoAutorizacion)) return false;
        if (!asegurarAdminEnSecretaria(cedula)) return false;
        if (!asegurarImagenSecretariaParaCedula(cedula)) return false;

        if (usuarioExiste(cedula)) {
            return agregarAdmin(cedula);
        }

        if (!registrarUsuario(cedula, clave, nombre, correo, TipoUsuario.ADMIN)) return false;
        return agregarAdmin(cedula);
    }

    public static boolean validarInicioSesion(String cedula, String clave) {
        if (!cedulaValida(cedula) || clave == null) return false;
        List<String> lineas = leerLineasGenericas(ARCHIVO_USUARIOS);

        for (String linea : lineas) {
            String[] partes = linea.split(":");
            if (partes.length >= 2 && partes[0].equals(cedula) && partes[1].equals(clave)) {
                return true;
            }
        }
        return false;
    }

    public static TipoUsuario obtenerTipoUsuario(String cedula) {
        if (esSuperAdmin(cedula)) return TipoUsuario.SUPER_ADMIN;
        if (esAdmin(cedula)) return TipoUsuario.ADMIN;

        List<String> lineas = leerLineasGenericas(ARCHIVO_USUARIOS);
        for (String linea : lineas) {
            String[] partes = linea.split(":");
            if (partes.length >= 6 && partes[0].equals(cedula)) {
                return TipoUsuario.fromEtiqueta(partes[4]);
            }
        }
        return TipoUsuario.COMENSAL;
    }

    public static String obtenerNombre(String cedula) {
        if (!cedulaValida(cedula)) return null;
        List<String> lineas = leerLineasGenericas(ARCHIVO_USUARIOS);
        for (String linea : lineas) {
            String[] partes = linea.split(":");
            if (partes.length >= 3 && partes[0].equals(cedula)) {
                return partes[2];
            }
        }
        return null;
    }

    private static boolean usuarioExiste(String cedula) {
        List<String> lineas = leerLineasGenericas(ARCHIVO_USUARIOS);
        for (String linea : lineas) {
            String[] partes = linea.split(":");
            if (partes.length > 0 && partes[0].equals(cedula)) return true;
        }
        return false;
    }

    public static boolean cedulaYaRegistrada(String cedula) {
        String cedulaLimpia = normalizarCedula(cedula);
        if (!cedulaValida(cedulaLimpia)) return false;
        return usuarioExiste(cedulaLimpia);
    }

    public static boolean esAdmin(String cedula) {
        String cedulaLimpia = normalizarCedula(cedula);
        if (!cedulaValida(cedulaLimpia)) return false;
        return esSuperAdmin(cedulaLimpia) || esAdminDirecto(cedulaLimpia);
    }

    public static boolean esSuperAdmin(String cedula) {
        String cedulaLimpia = normalizarCedula(cedula);
        if (!cedulaValida(cedulaLimpia)) return false;
        if (!esAdminDirecto(cedulaLimpia)) return false;

        List<String> supers = leerLineasGenericas(ARCHIVO_SUPER_ADMINS);
        for (String linea : supers) {
            if (normalizarCedula(linea).equals(cedulaLimpia)) return true;
        }
        return false;
    }

    private static boolean esAdminDirecto(String cedula) {
        List<String> admins = leerLineasGenericas(ARCHIVO_ADMINS);
        for (String linea : admins) {
            if (normalizarCedula(linea).equals(cedula)) return true;
        }
        return false;
    }

    public static boolean adminAutorizadoPorSuperAdmin(String cedula, String codigoAutorizacion) {
        if (!cedulaValida(cedula)) return false;
        List<String> autorizados = leerLineasGenericas(ARCHIVO_ADMINS_AUTORIZADOS);
        String codigo = valorSeguro(codigoAutorizacion);

        if (codigo.isBlank()) return false;

        for (String linea : autorizados) {
            String[] partes = linea.split(":", 2);
            if (partes.length == 0) continue;

            String cedulaPermitida = partes[0].trim();
            if (!cedula.equals(cedulaPermitida)) continue;

            if (partes.length < 2) continue;

            String codigoPermitido = partes[1].trim();
            if (!codigoPermitido.isBlank() && codigoPermitido.equals(codigo)) {
                return true;
            }
        }
        return false;
    }

    public static boolean autorizarAdministrador(String cedula, String codigoAutorizacion) {
        String cedulaLimpia = normalizarCedula(cedula);
        String codigo = valorSeguro(codigoAutorizacion);

        if (!cedulaValida(cedulaLimpia) || codigo.isBlank()) {
            return false;
        }

        if (!asegurarAdminEnSecretaria(cedulaLimpia)) {
            return false;
        }

        if (adminAutorizadoPorSuperAdmin(cedulaLimpia, codigo)) {
            return true;
        }

        List<String> autorizados = leerLineasGenericas(ARCHIVO_ADMINS_AUTORIZADOS);
        List<String> actualizadas = new ArrayList<>();
        boolean reemplazado = false;

        for (String linea : autorizados) {
            String[] partes = linea.split(":", 2);
            if (partes.length > 0 && cedulaLimpia.equals(partes[0].trim())) {
                actualizadas.add(cedulaLimpia + ":" + codigo);
                reemplazado = true;
            } else {
                actualizadas.add(linea);
            }
        }

        if (!reemplazado) {
            actualizadas.add(cedulaLimpia + ":" + codigo);
        }

        return reescribirArchivoGenerico(ARCHIVO_ADMINS_AUTORIZADOS, actualizadas);
    }

    private static boolean asegurarAdminEnSecretaria(String cedula) {
        String cedulaLimpia = normalizarCedula(cedula);
        if (!cedulaValida(cedulaLimpia)) return false;

        List<String> lineas = leerLineasSecretaria(ARCHIVO_PADRON_SECRETARIA);
        List<String> actualizadas = new ArrayList<>();
        boolean encontrado = false;

        for (String linea : lineas) {
            String limpia = valorSeguro(linea);
            if (limpia.isEmpty() || limpia.startsWith("#")) {
                actualizadas.add(linea);
                continue;
            }

            String[] partes = limpia.split(":", 2);
            if (partes.length < 2) {
                actualizadas.add(linea);
                continue;
            }

            String cedulaPadron = normalizarCedula(partes[0]);
            if (cedulaLimpia.equals(cedulaPadron)) {
                actualizadas.add(cedulaLimpia + ":administrador");
                encontrado = true;
            } else {
                actualizadas.add(linea);
            }
        }

        if (!encontrado) {
            actualizadas.add(cedulaLimpia + ":administrador");
        }

        return reescribirArchivoSecretaria(ARCHIVO_PADRON_SECRETARIA, actualizadas);
    }

    private static boolean reescribirArchivoSecretaria(String nombreArchivo, List<String> nuevasLineas) {
        File carpeta = obtenerCarpetaSecretaria();
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        File archivo = new File(carpeta, nombreArchivo);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo, StandardCharsets.UTF_8, false))) {
            for (String linea : nuevasLineas) {
                writer.write(linea);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error reescribiendo archivo de secretaría " + nombreArchivo + ": " + e.getMessage());
            return false;
        }
    }

    private static boolean agregarAdmin(String cedula) {
        if (esAdmin(cedula)) return true;
        return escribirLineaGenerica(ARCHIVO_ADMINS, cedula + System.lineSeparator());
    }

    public static double obtenerSaldo(String cedula) {
        if (!cedulaValida(cedula)) return 0.0;
        List<String> lineas = leerLineasGenericas(ARCHIVO_USUARIOS);
        for (String linea : lineas) {
            String[] partes = linea.split(":");
            if (partes.length >= 5 && partes[0].equals(cedula)) {
                try {
                    int indiceSaldo = partes.length >= 6 ? 5 : 4;
                    double saldo = Double.parseDouble(partes[indiceSaldo].replace(";", "").replace(",", ".").trim());
                    return redondearMoneda(saldo);
                } catch (NumberFormatException e) {
                    return 0.0; 
                }
            }
        }
        return 0.0;
    }

    public static boolean actualizarSaldo(String cedula, double nuevoSaldo) {
        if (!cedulaValida(cedula)) return false;
        double saldoRedondeado = redondearMoneda(nuevoSaldo);
        List<String> lineas = leerLineasGenericas(ARCHIVO_USUARIOS);
        List<String> lineasActualizadas = new ArrayList<>();
        boolean encontrado = false;

        for (String linea : lineas) {
            String[] partes = linea.split(":");
            if (partes.length >= 4 && partes[0].equals(cedula)) {
                String nuevaLinea;
                if (partes.length >= 6) {
                    String tipo = partes[4].trim();
                    nuevaLinea = String.format("%s:%s:%s:%s:%s:%.2f;",
                            partes[0], partes[1], partes[2], partes[3], tipo, saldoRedondeado);
                } else {
                    nuevaLinea = String.format("%s:%s:%s:%s:%.2f;",
                            partes[0], partes[1], partes[2], partes[3], saldoRedondeado);
                }
                lineasActualizadas.add(nuevaLinea);
                encontrado = true;
            } else {
                lineasActualizadas.add(linea);
            }
        }

        if (encontrado) {
            return reescribirArchivoGenerico(ARCHIVO_USUARIOS, lineasActualizadas);
        }
        return false;
    }

    public static boolean guardarMenu(Menu menu) {
        if (!menuValidoParaGuardar(menu)) return false;
        StringBuilder sb = new StringBuilder();
        sb.append(menu.getFecha().toString()).append("|")
                    .append(menu.getEstado().name())
                    .append("|")
                    .append(valorTipoMenu(menu.getTipoMenu()))
          .append("|");

        if (menu.getEstado() == EstadoMenu.NO_DISPONIBLE) {
            return escribirLineaGenerica(ARCHIVO_MENUS, sb.toString() + System.lineSeparator());
        }

        for (Platillo p : menu.getPlatillos()) {
            sb.append(p.getNombre()).append(">");
            List<Insumo> insumos = p.getInsumos();
            for (int i = 0; i < insumos.size(); i++) {
                sb.append(formatearInsumoMenu(insumos.get(i)));
                if (i < insumos.size() - 1) sb.append(",");
            }
            sb.append(";");
        }

        return escribirLineaGenerica(ARCHIVO_MENUS, sb.toString() + System.lineSeparator());
    }

    private static String menuToLine(Menu menu) {
        StringBuilder sb = new StringBuilder();
        sb.append(menu.getFecha().toString()).append("|")
                    .append(menu.getEstado().name())
                    .append("|")
                    .append(valorTipoMenu(menu.getTipoMenu()))
          .append("|");

        if (menu.getEstado() == EstadoMenu.NO_DISPONIBLE) {
            return sb.toString();
        }

        for (Platillo p : menu.getPlatillos()) {
            sb.append(p.getNombre()).append(">");
            List<Insumo> insumos = p.getInsumos();
            for (int i = 0; i < insumos.size(); i++) {
                sb.append(formatearInsumoMenu(insumos.get(i)));
                if (i < insumos.size() - 1) sb.append(",");
            }
            sb.append(";");
        }
        return sb.toString();
    }

    public static boolean actualizarMenu(Menu menu) {
        if (!menuValidoParaGuardar(menu)) return false;
        try {
            List<String> lineas = leerLineasGenericas(ARCHIVO_MENUS);
            List<String> nuevas = new ArrayList<>();
            String fecha = menu.getFecha().toString();
            Menu.TipoMenu tipoMenu = menu.getTipoMenu() == null ? Menu.TipoMenu.NO_DEFINIDO : menu.getTipoMenu();
            boolean encontrado = false;
            String nuevaLinea = menuToLine(menu);

            for (String linea : lineas) {
                String[] partes = linea.split("\\|");
                if (partes.length > 0 && partes[0].equals(fecha)) {
                    Menu.TipoMenu tipoLinea = partes.length >= 3
                            ? parsearTipoMenu(partes[2])
                            : Menu.TipoMenu.NO_DEFINIDO;
                    boolean coincideTipo = tipoMenu == Menu.TipoMenu.NO_DEFINIDO
                            ? tipoLinea == Menu.TipoMenu.NO_DEFINIDO
                            : tipoLinea == tipoMenu;
                    if (coincideTipo) {
                        nuevas.add(nuevaLinea);
                        encontrado = true;
                        continue;
                    }
                }
                nuevas.add(linea);
            }
            if (!encontrado) {
                nuevas.add(nuevaLinea);
            }
            return reescribirArchivoGenerico(ARCHIVO_MENUS, nuevas);
        } catch (Exception e) {
            System.err.println("Error actualizando menu: " + e.getMessage());
            return false;
        }
    }

    public static List<LocalDate> calcularFechasParaReiniciar() {
        try {
            LocalDate hoy = LocalDate.now();
            LocalDate lunes = hoy.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

            List<LocalDate> semana = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                semana.add(lunes.plusDays(i));
            }
            return semana;
        } catch (Exception e) {
            System.err.println("Error calculando fechas para reiniciar: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static boolean reiniciarMenusSemana() {
        try {
            List<String> actuales = leerLineasGenericas(ARCHIVO_MENUS);
            List<LocalDate> semanaActual = calcularFechasParaReiniciar();
            Set<String> fechasSemana = new java.util.HashSet<>();
            for (LocalDate fecha : semanaActual) {
                fechasSemana.add(fecha.toString());
            }
            List<String> nuevasLineas = new ArrayList<>();
            for (String linea : actuales) {
                String[] partes = linea.split("\\|");
                if (partes.length > 0 && fechasSemana.contains(partes[0])) {
                    Menu menu = parsearLineaMenu(partes);
                    menu.setEstado(EstadoMenu.NO_DISPONIBLE);
                    nuevasLineas.add(menuToLine(menu));
                } else {
                    nuevasLineas.add(linea);
                }
            }

            for (LocalDate fecha : semanaActual) {
                asegurarMenuNoDisponible(nuevasLineas, fecha, Menu.TipoMenu.DESAYUNO);
                asegurarMenuNoDisponible(nuevasLineas, fecha, Menu.TipoMenu.ALMUERZO);
            }

            return reescribirArchivoGenerico(ARCHIVO_MENUS, nuevasLineas);
        } catch (Exception e) {
            System.err.println("Error reiniciando menus: " + e.getMessage());
            return false;
        }
    }

    public static boolean prepararSemanaConEstados(LocalDate fechaReferencia) {
        if (fechaReferencia == null) return false;

        try {
            List<String> actuales = leerLineasGenericas(ARCHIVO_MENUS);
            List<String> nuevas = new ArrayList<>(actuales);
            LocalDate lunes = fechaReferencia.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

            for (int i = 0; i < 5; i++) {
                LocalDate fecha = lunes.plusDays(i);
                asegurarMenuNoDisponible(nuevas, fecha, Menu.TipoMenu.DESAYUNO);
                asegurarMenuNoDisponible(nuevas, fecha, Menu.TipoMenu.ALMUERZO);
            }

            return reescribirArchivoGenerico(ARCHIVO_MENUS, nuevas);
        } catch (Exception e) {
            System.err.println("Error preparando semana: " + e.getMessage());
            return false;
        }
    }

    public static Menu obtenerMenuPorFecha(String fechaStr) {
        List<String> lineas = leerLineasGenericas(ARCHIVO_MENUS);

        Menu exacto = buscarMenuPorFechaYTipo(lineas, fechaStr, Menu.TipoMenu.NO_DEFINIDO);
        if (exacto != null) return exacto;

        Menu cualquiera = buscarMenuPorFecha(lineas, fechaStr);
        if (cualquiera != null) return cualquiera;
        try {
            return new Menu(LocalDate.parse(fechaStr), EstadoMenu.NO_DISPONIBLE);
        } catch (Exception ex) {
            return new Menu();
        }
    }

    public static Menu obtenerMenuPorFechaYTipo(String fechaStr, Menu.TipoMenu tipoMenu) {
        List<String> lineas = leerLineasGenericas(ARCHIVO_MENUS);
        Menu.TipoMenu tipoSeguro = tipoMenu == null ? Menu.TipoMenu.NO_DEFINIDO : tipoMenu;
        Menu menu = buscarMenuPorFechaYTipo(lineas, fechaStr, tipoSeguro);
        if (menu != null) return menu;

        if (tipoSeguro == Menu.TipoMenu.NO_DEFINIDO) {
            Menu cualquiera = buscarMenuPorFecha(lineas, fechaStr);
            if (cualquiera != null) return cualquiera;
        }

        try {
            Menu noDisponible = new Menu(LocalDate.parse(fechaStr), EstadoMenu.NO_DISPONIBLE);
            noDisponible.setTipoMenu(tipoSeguro);
            return noDisponible;
        } catch (Exception ex) {
            return new Menu();
        }
    }

    private static Menu parsearLineaMenu(String[] partesPrincipales) {
        try {
            LocalDate fecha = LocalDate.parse(partesPrincipales[0]);
            EstadoMenu estado = EstadoMenu.CON_MENU;
            String bloquePlatillos = "";
            Menu.TipoMenu tipoMenu = Menu.TipoMenu.NO_DEFINIDO;

            if (partesPrincipales.length >= 2) {
                estado = parsearEstadoMenu(partesPrincipales[1]);
            }

            if (partesPrincipales.length >= 3) {
                if (esTipoMenuValido(partesPrincipales[2])) {
                    tipoMenu = parsearTipoMenu(partesPrincipales[2]);
                    if (partesPrincipales.length >= 4) {
                        bloquePlatillos = partesPrincipales[3];
                    }
                } else {
                    bloquePlatillos = partesPrincipales[2];
                }
            } else if (partesPrincipales.length == 2) {
                bloquePlatillos = partesPrincipales[1];
            }

            Menu menu = new Menu(fecha, estado);
            menu.setTipoMenu(tipoMenu);

            if (estado == EstadoMenu.NO_DISPONIBLE) {
                return menu;
            }

            if (bloquePlatillos.isBlank()) return menu;

            String[] partesPlatillos = bloquePlatillos.split(";");
            for (String parteP : partesPlatillos) {
                if (parteP.isBlank()) continue;

                String[] datosPlato = parteP.split(">");
                Platillo platillo = new Platillo(datosPlato[0]);

                if (datosPlato.length > 1) {
                    for (String rawInsumo : datosPlato[1].split(",")) {
                        if (rawInsumo.isBlank()) continue;
                        Insumo insumo = parsearInsumoMenu(rawInsumo);
                        platillo.agregarInsumo(insumo);
                    }
                }
                menu.agregarPlatillo(platillo);
            }
            return menu;
        } catch (Exception e) {
            System.err.println("Error parseando menú: " + e.getMessage());
            return new Menu();
        }
    }

    public static boolean esFechaValidaParaMenu(LocalDate fecha) {
        return fecha != null && esDiaHabil(fecha);
    }

    public static boolean esDiaHabil(LocalDate fecha) {
        if (fecha == null) return false;
        DayOfWeek dia = fecha.getDayOfWeek();
        if (dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY) {
            return false;
        }
        return !esFeriado(fecha);
    }

    public static List<LocalDate> obtenerUltimosCincoDiasHabiles(LocalDate referencia) {
        List<LocalDate> resultado = new ArrayList<>();
        LocalDate cursor = referencia == null ? LocalDate.now() : referencia;

        while (resultado.size() < 5) {
            if (esDiaHabil(cursor)) {
                resultado.add(0, cursor);
            }
            cursor = cursor.minusDays(1);
        }
        return resultado;
    }

    private static boolean esFeriado(LocalDate fecha) {
        String mmdd = String.format("%02d-%02d", fecha.getMonthValue(), fecha.getDayOfMonth());
        if (FERIADOS_FIJOS_MM_DD.contains(mmdd)) return true;

        List<String> feriados = leerLineasGenericas(ARCHIVO_FERIADOS);
        for (String linea : feriados) {
            try {
                if (LocalDate.parse(linea.trim()).equals(fecha)) {
                    return true;
                }
            } catch (Exception ignored) {
            }
        }

        List<String> noLaborables = leerLineasGenericas(ARCHIVO_NO_LABORABLES);
        for (String linea : noLaborables) {
            if (linea.trim().equals(fecha.toString())) return true;
        }
        return false;
    }

    private static EstadoMenu parsearEstadoMenu(String raw) {
        if (raw == null || raw.isBlank()) return EstadoMenu.CON_MENU;
        try {
            return EstadoMenu.valueOf(raw.trim());
        } catch (IllegalArgumentException ex) {
            return EstadoMenu.CON_MENU;
        }
    }

    private static Menu.TipoMenu parsearTipoMenu(String raw) {
        if (raw == null || raw.isBlank()) return Menu.TipoMenu.NO_DEFINIDO;
        try {
            return Menu.TipoMenu.valueOf(raw.trim());
        } catch (IllegalArgumentException ex) {
            return Menu.TipoMenu.NO_DEFINIDO;
        }
    }

    private static boolean esTipoMenuValido(String raw) {
        if (raw == null || raw.isBlank()) return false;
        try {
            Menu.TipoMenu.valueOf(raw.trim());
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    private static String valorTipoMenu(Menu.TipoMenu tipoMenu) {
        return tipoMenu == null ? Menu.TipoMenu.NO_DEFINIDO.name() : tipoMenu.name();
    }

    private static boolean menuValidoParaGuardar(Menu menu) {
        if (menu == null || menu.getFecha() == null) return false;
        if (!esFechaValidaParaMenu(menu.getFecha())) return false;

        if (menu.getEstado() == EstadoMenu.NO_DISPONIBLE) {
            return true;
        }

        return menu.tienePlatillos();
    }

    private static boolean existeMenuParaFechaYTipo(List<String> lineas, LocalDate fecha, Menu.TipoMenu tipoMenu) {
        String fechaStr = fecha.toString();
        Menu.TipoMenu tipoSeguro = tipoMenu == null ? Menu.TipoMenu.NO_DEFINIDO : tipoMenu;
        for (String linea : lineas) {
            String[] partes = linea.split("\\|");
            if (partes.length > 0 && fechaStr.equals(partes[0])) {
                Menu.TipoMenu tipoLinea = partes.length >= 3
                        ? parsearTipoMenu(partes[2])
                        : Menu.TipoMenu.NO_DEFINIDO;
                boolean coincideTipo = tipoSeguro == Menu.TipoMenu.NO_DEFINIDO
                        ? tipoLinea == Menu.TipoMenu.NO_DEFINIDO
                        : tipoLinea == tipoSeguro;
                if (coincideTipo) return true;
            }
        }
        return false;
    }

    private static void asegurarMenuNoDisponible(List<String> lineas, LocalDate fecha, Menu.TipoMenu tipoMenu) {
        if (!existeMenuParaFechaYTipo(lineas, fecha, tipoMenu)) {
            Menu menu = new Menu(fecha, EstadoMenu.NO_DISPONIBLE);
            menu.setTipoMenu(tipoMenu);
            lineas.add(menuToLine(menu));
        }
    }

    private static Menu buscarMenuPorFecha(List<String> lineas, String fechaStr) {
        for (String linea : lineas) {
            String[] partes = linea.split("\\|");
            if (partes.length > 0 && partes[0].equals(fechaStr)) {
                return parsearLineaMenu(partes);
            }
        }
        return null;
    }

    private static Menu buscarMenuPorFechaYTipo(List<String> lineas, String fechaStr, Menu.TipoMenu tipoMenu) {
        Menu.TipoMenu tipoSeguro = tipoMenu == null ? Menu.TipoMenu.NO_DEFINIDO : tipoMenu;
        for (String linea : lineas) {
            String[] partes = linea.split("\\|");
            if (partes.length > 0 && partes[0].equals(fechaStr)) {
                Menu.TipoMenu tipoLinea = partes.length >= 3
                        ? parsearTipoMenu(partes[2])
                        : Menu.TipoMenu.NO_DEFINIDO;
                boolean coincideTipo = tipoSeguro == Menu.TipoMenu.NO_DEFINIDO
                        ? tipoLinea == Menu.TipoMenu.NO_DEFINIDO
                        : tipoLinea == tipoSeguro;
                if (coincideTipo) {
                    return parsearLineaMenu(partes);
                }
            }
        }
        return null;
    }

    public static boolean guardarCfcv(com.gesco.models.costos.CFCV cfcv) {
        if (cfcv == null) return false;
        List<String> lineas = new ArrayList<>();
        lineas.add(cfcv.toLine());
        return reescribirArchivoGenerico(ARCHIVO_CFCV, lineas);
    }

    public static com.gesco.models.costos.CFCV obtenerCfcv() {
        List<String> lineas = leerLineasGenericas(ARCHIVO_CFCV);
        if (lineas.isEmpty()) return new com.gesco.models.costos.CFCV();
        return com.gesco.models.costos.CFCV.fromLine(lineas.get(0));
    }

    public static List<Insumo> obtenerInsumos() {
        List<String> lineas = leerLineasGenericas(ARCHIVO_INSUMOS);
        List<Insumo> resultado = new ArrayList<>();

        for (String linea : lineas) {
            if (linea == null) continue;
            String limpia = linea.trim();
            if (limpia.isEmpty()) continue;
            if (limpia.toLowerCase(Locale.ROOT).startsWith("nombre insumo")) continue;

            Insumo insumo = parsearLineaInsumo(limpia);
            if (insumo != null) {
                resultado.add(insumo);
            }
        }

        return resultado;
    }

    public static boolean guardarOActualizarInsumo(
        String nombre,
        int cantidadAgregar,
        String tipo,
        float precioUnitario
    ) {
        String nombreLimpio = valorSeguro(nombre);
        String tipoLimpio = valorSeguro(tipo);

        if (nombreLimpio.isBlank() || tipoLimpio.isBlank()) return false;
        if (!nombreLimpio.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+$")) return false;
        if (!tipoLimpio.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+$")) return false;
        if (cantidadAgregar <= 0 || cantidadAgregar > CANTIDAD_MAXIMA_INSUMO) return false;
        if (!Float.isFinite(precioUnitario) || precioUnitario < 0 || precioUnitario > PRECIO_MAXIMO_INSUMO) return false;

        List<String> lineas = leerLineasGenericas(ARCHIVO_INSUMOS);
        List<String> nuevas = new ArrayList<>();
        boolean encontrado = false;

        for (String linea : lineas) {
            String limpia = linea.trim();

            if (limpia.toLowerCase(Locale.ROOT).startsWith("nombre insumo")) {
                nuevas.add(linea);
                continue;
            }

            String[] partes = limpia.replace(";", "").split(":");
            String nombreLinea = partes.length > 0 ? partes[0].trim() : "";

            if (nombreLinea.equalsIgnoreCase(nombreLimpio)) {
                int cantExistente = 0;
                try {
                    cantExistente = (int) Float.parseFloat(partes.length > 2 ? partes[2].trim() : "0");
                } catch (NumberFormatException ignored) {
                }

                int nuevaCantidad = cantExistente + cantidadAgregar;
                if (nuevaCantidad > CANTIDAD_MAXIMA_INSUMO) {
                    return false;
                }

                nuevas.add(String.format(Locale.ROOT, "%s : %s : %d : %.1f :",
                    nombreLimpio, tipoLimpio, nuevaCantidad, precioUnitario));
                encontrado = true;
            } else {
                nuevas.add(linea);
            }
        }

        if (!encontrado) {
            nuevas.add(String.format(Locale.ROOT, "%s : %s : %d : %.1f :",
                nombreLimpio, tipoLimpio, cantidadAgregar, precioUnitario));
        }

        return reescribirArchivoGenerico(ARCHIVO_INSUMOS, nuevas);
    }

    public static boolean guardarCcb(CCB ccb) {
        if (ccb == null) return false;
        String linea = String.format(
                Locale.US,
                "%s|%s|%.2f|%.2f|%.2f|%.4f|%.4f",
                ccb.getFecha().toString(),
                valorSeguro(ccb.getTipoUsuario()),
                ccb.getCf(),
                ccb.getCv(),
                ccb.getNb(),
                ccb.getMerma(),
                ccb.getCcb()
        );
        return escribirLineaGenerica(ARCHIVO_CCB, linea + System.lineSeparator());
    }

    public static CCB obtenerUltimoCcb() {
        List<String> lineas = leerLineasGenericas(ARCHIVO_CCB);
        if (lineas.isEmpty()) return null;
        String ultima = lineas.get(lineas.size() - 1);
        return parsearLineaCcb(ultima);
    }

    public static List<CCB> obtenerHistorialCcb() {
        List<String> lineas = leerLineasGenericas(ARCHIVO_CCB);
        List<CCB> resultado = new ArrayList<>();
        for (String linea : lineas) {
            if (linea == null) continue;
            String limpia = linea.trim();
            if (limpia.isEmpty()) continue;

            CCB ccb = parsearLineaCcb(limpia);
            if (ccb != null) {
                resultado.add(ccb);
            }
        }
        return resultado;
    }

    public static double calcularMontoCcbPorTipo(double ccbBase, TipoUsuario tipoUsuario) {
        if (ccbBase < 0) {
            throw new IllegalArgumentException("El CCB base no puede ser negativo.");
        }

        double[] rango = obtenerRangoPorTipoUsuario(tipoUsuario);
        double porcentaje = rango[0] == rango[1]
                ? rango[0]
                : ThreadLocalRandom.current().nextDouble(rango[0], rango[1]);
        return redondearMoneda(ccbBase * porcentaje);
    }

    public static double calcularMontoCcbPorTipo(double ccbBase, TipoUsuario tipoUsuario, double porcentajeCcb) {
        if (ccbBase < 0) {
            throw new IllegalArgumentException("El CCB base no puede ser negativo.");
        }

        double[] rango = obtenerRangoPorTipoUsuario(tipoUsuario);
        if (porcentajeCcb < rango[0] || porcentajeCcb > rango[1]) {
            throw new IllegalArgumentException("El porcentaje no está en el rango permitido para el tipo de usuario.");
        }

        return redondearMoneda(ccbBase * porcentajeCcb);
    }

    public static double calcularMontoCcbParaCedula(String cedula) {
        CCB ultimoCcb = obtenerUltimoCcb();
        if (ultimoCcb == null || !cedulaValida(cedula)) {
            return 0.0;
        }

        TipoUsuario tipoUsuario = obtenerTipoUsuario(cedula);
        return calcularMontoCcbPorTipo(ultimoCcb.getCcb(), tipoUsuario);
    }

    public static double calcularMontoCcbParaCedula(String cedula, double porcentajeCcb) {
        CCB ultimoCcb = obtenerUltimoCcb();
        if (ultimoCcb == null || !cedulaValida(cedula)) {
            return 0.0;
        }

        TipoUsuario tipoUsuario = obtenerTipoUsuario(cedula);
        return calcularMontoCcbPorTipo(ultimoCcb.getCcb(), tipoUsuario, porcentajeCcb);
    }

    public static List<Menu> obtenerUltimos5Menus() {
        List<String> lineas = leerLineasGenericas(ARCHIVO_MENUS);
        List<Menu> resultado = new ArrayList<>();
        int inicio = Math.max(0, lineas.size() - 5);
        for (int i = inicio; i < lineas.size(); i++) {
            String[] partes = lineas.get(i).split("\\|");
            resultado.add(parsearLineaMenu(partes));
        }
        return resultado;
    }

    private static CCB parsearLineaCcb(String linea) {
        if (linea == null || linea.isBlank()) return null;
        String[] partes = linea.split("\\|");
        if (partes.length < 6) return null;
        try {
            LocalDate fecha = LocalDate.parse(partes[0].trim());
            String tipoUsuario = partes[1].trim();
            double cf = parseDoubleSeguro(partes[2]);
            double cv = parseDoubleSeguro(partes[3]);
            double nb = parseDoubleSeguro(partes[4]);
            double merma = parseDoubleSeguro(partes[5]);
            return new CCB(fecha, tipoUsuario, cf, cv, nb, merma);
        } catch (Exception ex) {
            return null;
        }
    }

    private static double parseDoubleSeguro(String valor) {
        if (valor == null) return 0.0;
        try {
            return Double.parseDouble(valor.trim().replace(',', '.'));
        } catch (NumberFormatException ex) {
            return 0.0;
        }
    }

    private static double[] obtenerRangoPorTipoUsuario(TipoUsuario tipoUsuario) {
        if (tipoUsuario == null) {
            return new double[] { 1.0, 1.0 };
        }

        return switch (tipoUsuario) {
            case ESTUDIANTE -> new double[] { CCB_ESTUDIANTE_MIN, CCB_ESTUDIANTE_MAX };
            case PROFESOR -> new double[] { CCB_PROFESOR_MIN, CCB_PROFESOR_MAX };
            case EMPLEADO -> new double[] { CCB_EMPLEADO_MIN, CCB_EMPLEADO_MAX };
            default -> new double[] { 1.0, 1.0 };
        };
    }

    private static float parseFloatSeguro(String valor) {
        if (valor == null) return 0.0f;
        try {
            return Float.parseFloat(valor.trim().replace(',', '.'));
        } catch (NumberFormatException ex) {
            return 0.0f;
        }
    }

    private static String formatearInsumoMenu(Insumo insumo) {
        if (insumo == null) return "";
        String nombre = valorSeguro(insumo.getNombre());
        int cantidad = insumo.getCantidad();
        float total = insumo.getCostoInsumoTotal();
        return String.format(Locale.US, "%s~%d~%.2f", nombre, cantidad, total);
    }

    private static Insumo parsearInsumoMenu(String raw) {
        String limpio = raw.trim();
        if (limpio.isEmpty()) {
            return new Insumo("", 1, "Ingrediente", 0.0f);
        }

        String[] partes = limpio.split("~");
        if (partes.length >= 3) {
            String nombre = partes[0].trim();
            int cantidad = (int) parseFloatSeguro(partes[1]);
            float total = parseFloatSeguro(partes[2]);
            float unitario = (cantidad > 0) ? (total / cantidad) : 0.0f;
            Insumo insumo = new Insumo(nombre, cantidad, "Ingrediente", unitario);
            insumo.setCostoInsumoTotal(total);
            return insumo;
        }

        return new Insumo(limpio, 1, "Ingrediente", 0.0f);
    }

    private static Insumo parsearLineaInsumo(String linea) {
        String sinPuntoYComa = linea.replace(";", "");
        String[] partes = sinPuntoYComa.split(":");
        if (partes.length < 4) return null;

        String nombre = partes[0].trim();
        String tipo = partes[1].trim();
        int cantidad = (int) parseFloatSeguro(partes[2]);
        float precioUnitario = parseFloatSeguro(partes[3]);

        if (nombre.isEmpty()) return null;
        return new Insumo(nombre, cantidad, tipo, precioUnitario);
    }

    public static boolean esDiaNoDisponible(String fechaStr) {
        try {
            LocalDate fecha = LocalDate.parse(fechaStr);
            return !esDiaHabil(fecha);
        } catch (Exception e) {
            return false;
        }
    }

    private static Path obtenerBaseProyecto() {
        try {
            Path ubicacion = Paths.get(DataBase.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            if (ubicacion.endsWith("classes") && ubicacion.getParent() != null && ubicacion.getParent().getParent() != null) {
                return ubicacion.getParent().getParent();
            }
        } catch (URISyntaxException e) { }
        return Paths.get(System.getProperty("user.dir"));
    }

    private static String valorSeguro(String valor) {
        return valor == null ? "" : valor.trim();
    }

    private static boolean nombreValido(String nombre) {
        return nombre != null
            && !nombre.isBlank()
            && nombre.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+$");
    }

    private static TipoUsuario mapearTipoSecretaria(String valorTipo) {
        String normalizado = valorSeguro(valorTipo)
                .toLowerCase(Locale.ROOT)
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u");

        return switch (normalizado) {
            case "estudiante" -> TipoUsuario.ESTUDIANTE;
            case "profesor" -> TipoUsuario.PROFESOR;
            case "trabajador", "empleado" -> TipoUsuario.EMPLEADO;
            case "administrador", "admin" -> TipoUsuario.ADMIN;
            default -> null;
        };
    }

    private static double redondearMoneda(double valor) {
        return BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public static String normalizarCedula(String cedula) {
        if (cedula == null) return "";
        return cedula.trim().replaceAll("[.\\-\\s]", "");
    }

    private static boolean cedulaValida(String cedula) {
        String cedulaLimpia = normalizarCedula(cedula);
        if (cedulaLimpia.isEmpty() || !cedulaLimpia.matches("\\d+")) {
            return false;
        }
        try {
            long cedulaNumero = Long.parseLong(cedulaLimpia);
            return cedulaNumero >= CEDULA_MINIMA && cedulaNumero <= CEDULA_MAXIMA;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}

