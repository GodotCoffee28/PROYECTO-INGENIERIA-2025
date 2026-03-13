package com.gesco.controllers.gestion_principal;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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
    private static final String ARCHIVO_PORCENTAJES_BECARIOS = "porcentajes_becarios.txt";
    private static final String ARCHIVO_MENUS = "menus.txt";
    private static final String ARCHIVO_CFCV = "cfcv.txt";
    private static final String ARCHIVO_CCB = "ccb.txt";
    private static final String ARCHIVO_INSUMOS = "insumos.txt";
    private static final String ARCHIVO_FERIADOS = "feriados.txt";
    private static final String ARCHIVO_NO_LABORABLES = "sabados_domingos_2026.txt";
    private static final String ARCHIVO_BANCO = "banco.txt";
    private static final String CARPETA_SECRETARIA = "secretaria";
    private static final String CARPETA_IMAGENES_SECRETARIA = "imagenes_rostros";
    private static final String ARCHIVO_PADRON_SECRETARIA = "cedulas_ocupaciones.txt";
    private static final String ARCHIVO_REGISTRO_SALDO = "registroSaldo.txt";
    private static final String ARCHIVO_ACUDIERON = "acudieron.txt";

    private static final long CEDULA_MINIMA = 4_000_000L;
    private static final long CEDULA_MAXIMA = 45_000_000L;
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
            String[] recarga = parsearLineaRegistroSaldo(linea);
            if (recarga == null) {
                continue;
            }

            String cedulaRegistro = normalizarCedula(recarga[4]);
            if (!cedulaLimpia.equals(cedulaRegistro)) {
                continue;
            }

            recargas.add(new String[] {
                recarga[0],
                recarga[1],
                recarga[2],
                recarga[3],
                cedulaRegistro
            });
        }

        return recargas;
    }

    private static String[] parsearLineaRegistroSaldo(String linea) {
        String texto = valorSeguro(linea);
        if (texto.isBlank()) {
            return null;
        }

        int ultimoSeparador = texto.lastIndexOf(':');
        int penultimoSeparador = texto.lastIndexOf(':', ultimoSeparador - 1);
        int primerSeparador = texto.indexOf(':');
        int segundoSeparador = texto.indexOf(':', primerSeparador + 1);

        if (primerSeparador < 0 || segundoSeparador < 0 || penultimoSeparador < 0 || ultimoSeparador < 0) {
            return null;
        }

        if (!(primerSeparador < segundoSeparador && segundoSeparador < penultimoSeparador && penultimoSeparador < ultimoSeparador)) {
            return null;
        }

        String referencia = texto.substring(0, primerSeparador).trim();
        String monto = texto.substring(primerSeparador + 1, segundoSeparador).trim();
        String banco = texto.substring(segundoSeparador + 1, penultimoSeparador).trim();
        String fecha = texto.substring(penultimoSeparador + 1, ultimoSeparador).trim();
        String cedula = texto.substring(ultimoSeparador + 1).trim();

        if (referencia.isEmpty() || monto.isEmpty() || fecha.isEmpty() || cedula.isEmpty()) {
            return null;
        }

        return new String[] { referencia, monto, banco, fecha, cedula };
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

    public static boolean referenciaBancariaValida(String referencia) {
        String referenciaLimpia = valorSeguro(referencia);
        if (!referenciaLimpia.matches("\\d{20}")) {
            return false;
        }

        List<String> referenciasBanco = leerLineasGenericas(ARCHIVO_BANCO);
        for (String referenciaBanco : referenciasBanco) {
            if (referenciaLimpia.equals(valorSeguro(referenciaBanco))) {
                return true;
            }
        }

        return false;
    }

    public static boolean registrarAcudieron(
        String cedula,
        TipoUsuario tipoUsuario,
        Menu.TipoMenu tipoMenu,
        double montoCobrado
    ) {
        String cedulaLimpia = normalizarCedula(cedula);
        if (!cedulaValida(cedulaLimpia)) {
            return false;
        }

        TipoUsuario tipoSeguro = tipoUsuario == null ? obtenerTipoUsuario(cedulaLimpia) : tipoUsuario;
        Menu.TipoMenu tipoMenuSeguro = tipoMenu == null ? Menu.TipoMenu.DESAYUNO : tipoMenu;
        String fecha = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        String nombre = valorSeguro(obtenerNombre(cedulaLimpia));
        double monto = redondearMoneda(montoCobrado);

        String linea = String.format(
            Locale.ROOT,
            "%s:%s:%s:%s:%s:%.2f",
            fecha,
            cedulaLimpia,
            nombre,
            etiquetaTipoComensal(tipoSeguro),
            tipoMenuSeguro.name(),
            monto
        );

        return escribirLineaGenerica(ARCHIVO_ACUDIERON, linea + System.lineSeparator());
    }

    public static boolean eliminarRegistroAcudieron(String cedula) {
        String cedulaLimpia = normalizarCedula(cedula);
        if (!cedulaValida(cedulaLimpia)) {
            return false;
        }

        List<String> lineas = leerLineasGenericas(ARCHIVO_ACUDIERON);
        if (lineas.isEmpty()) {
            return true;
        }

        String fechaHoy = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        int indice = buscarIndiceRegistroAcudieron(lineas, cedulaLimpia, fechaHoy);

        if (indice < 0) {
            indice = buscarIndiceRegistroAcudieron(lineas, cedulaLimpia, null);
        }

        if (indice < 0) {
            return true;
        }

        lineas.remove(indice);
        return reescribirArchivoGenerico(ARCHIVO_ACUDIERON, lineas);
    }

    public static int contarAcudieronPorFecha(LocalDate fecha) {
        if (fecha == null) {
            return 0;
        }

        String fechaTexto = fecha.format(DateTimeFormatter.ISO_LOCAL_DATE);
        List<String> lineas = leerLineasGenericas(ARCHIVO_ACUDIERON);
        int total = 0;

        for (String linea : lineas) {
            String[] partes = parsearPartesRegistroAcudieron(linea);
            if (partes == null) {
                continue;
            }

            String fechaRegistro = valorSeguro(partes[0]);
            if (fechaTexto.equals(fechaRegistro)) {
                total++;
            }
        }

        return total;
    }

    public static boolean existeRegistroAcudieronPorCedulaYFecha(String cedula, LocalDate fecha) {
        String cedulaLimpia = normalizarCedula(cedula);
        if (!cedulaValida(cedulaLimpia) || fecha == null) {
            return false;
        }

        String fechaTexto = fecha.format(DateTimeFormatter.ISO_LOCAL_DATE);
        List<String> lineas = leerLineasGenericas(ARCHIVO_ACUDIERON);

        for (String linea : lineas) {
            String[] partes = parsearPartesRegistroAcudieron(linea);
            if (partes == null) {
                continue;
            }

            String fechaRegistro = valorSeguro(partes[0]);
            String cedulaRegistro = valorSeguro(partes[1]);
            if (fechaTexto.equals(fechaRegistro) && cedulaLimpia.equals(cedulaRegistro)) {
                return true;
            }
        }

        return false;
    }

    private static String[] parsearPartesRegistroAcudieron(String linea) {
        String texto = valorSeguro(linea);
        if (texto.isBlank()) {
            return null;
        }

        String[] partes = texto.contains(":") ? texto.split(":") : texto.split("\\|");
        if (partes.length < 2) {
            return null;
        }

        return partes;
    }

    private static int buscarIndiceRegistroAcudieron(List<String> lineas, String cedula, String fecha) {
        for (int i = lineas.size() - 1; i >= 0; i--) {
            String[] partes = parsearPartesRegistroAcudieron(lineas.get(i));
            if (partes == null) {
                continue;
            }

            String fechaRegistro = valorSeguro(partes[0]);
            String cedulaRegistro = valorSeguro(partes[1]);
            if (!cedula.equals(cedulaRegistro)) {
                continue;
            }

            if (fecha == null || fechaRegistro.startsWith(fecha)) {
                return i;
            }
        }

        return -1;
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

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo, StandardCharsets.UTF_8, false))) {
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

            String[] partes = limpia.split(":", 4);
            if (partes.length < 2) continue;

            String cedulaPadron = normalizarCedula(partes[0]);
            if (!cedulaLimpia.equals(cedulaPadron)) continue;

            TipoUsuario tipoResuelto = resolverTipoDesdeRegistroSecretaria(partes);
            if (tipoResuelto != null) {
                return tipoResuelto;
            }
        }
        return null;
    }

    public static String obtenerNombreSecretaria(String cedula) {
        String cedulaLimpia = normalizarCedula(cedula);
        if (!cedulaValida(cedulaLimpia)) {
            return null;
        }

        List<String> lineas = leerLineasSecretaria(ARCHIVO_PADRON_SECRETARIA);
        for (String linea : lineas) {
            String limpia = valorSeguro(linea);
            if (limpia.isEmpty() || limpia.startsWith("#")) {
                continue;
            }

            String[] partes = limpia.split(":", 4);
            if (partes.length < 2) {
                continue;
            }

            String cedulaPadron = normalizarCedula(partes[0]);
            if (!cedulaLimpia.equals(cedulaPadron)) {
                continue;
            }

            String tipoBase = normalizarTextoTipo(partes[1]);
            if ("estudiante".equals(tipoBase)) {
                if (partes.length >= 4) {
                    return valorSeguro(partes[3]);
                }
                if (partes.length == 3 && mapearSubtipoUsuario(partes[2]) == null) {
                    return valorSeguro(partes[2]);
                }
                return null;
            }

            if (partes.length >= 3) {
                return valorSeguro(partes[2]);
            }

            return null;
        }

        return null;
    }

    public static boolean tipoUsuarioCoincideConSecretaria(String cedula, TipoUsuario tipoUsuario) {
        TipoUsuario tipoPadron = obtenerTipoUsuarioSecretaria(cedula);
        if (tipoPadron == null || tipoUsuario == null) return false;

        TipoUsuario tipoSolicitado = tipoUsuario == TipoUsuario.COMENSAL
                ? TipoUsuario.ESTUDIANTE
                : tipoUsuario;

        if (tipoSolicitado == TipoUsuario.ESTUDIANTE && tipoPadron.esTipoEstudiantil()) {
            return true;
        }
        return tipoPadron == tipoSolicitado;
    }

    public static boolean registrarAdministradorPreAutorizado(String cedula, String clave, String nombre, String correo) {
        if (!asegurarAdminEnSecretaria(cedula)) return false;
        if (!asegurarImagenSecretariaParaCedula(cedula)) return false;

        if (usuarioExiste(cedula)) {
            return agregarAdmin(cedula);
        }

        if (!registrarUsuario(cedula, clave, nombre, correo, TipoUsuario.ADMIN)) return false;
        return agregarAdmin(cedula);
    }

    public static boolean cambiarTipoUsuarioPorCedula(String cedula, TipoUsuario nuevoTipo) {
        return cambiarTipoUsuarioPorCedula(cedula, nuevoTipo, null);
    }

    public static boolean cambiarTipoUsuarioPorCedula(String cedula, TipoUsuario nuevoTipo, Double porcentajeBecario) {
        String cedulaLimpia = normalizarCedula(cedula);
        if (!cedulaValida(cedulaLimpia) || nuevoTipo == null || nuevoTipo == TipoUsuario.COMENSAL) {
            return false;
        }

        TipoUsuario tipoAnterior = obtenerTipoUsuarioSecretaria(cedulaLimpia);
        if (tipoAnterior == null) {
            return false;
        }

        if (!tipoAnterior.esTipoEstudiantil() || !nuevoTipo.esTipoEstudiantil()) {
            return false;
        }

        if (nuevoTipo == TipoUsuario.BECARIO) {
            if (porcentajeBecario == null || !esPorcentajeBecarioValidoParaHoy(porcentajeBecario)) {
                return false;
            }
        }

        if (!actualizarTipoEnPadronSecretaria(cedulaLimpia, nuevoTipo)) {
            return false;
        }

        if (!actualizarTipoEnUsuariosRegistrados(cedulaLimpia, nuevoTipo)) {
            return false;
        }

        boolean adminActualizado = nuevoTipo.esAdmin()
            ? agregarAdmin(cedulaLimpia)
            : removerAdmin(cedulaLimpia);
        if (!adminActualizado) {
            return false;
        }

        if (nuevoTipo == TipoUsuario.BECARIO) {
            return guardarPorcentajeBecario(cedulaLimpia, porcentajeBecario);
        }

        return eliminarPorcentajeBecario(cedulaLimpia);
    }

    public static double obtenerPorcentajeCcbEstudiantilHoy() {
        CCB ccbEstudiantil = obtenerCcbPorFechaYTipo(LocalDate.now(), TipoUsuario.ESTUDIANTE);
        if (ccbEstudiantil == null) {
            return 0.0;
        }
        return CCB.normalizarPorcentajeParaTipo(TipoUsuario.ESTUDIANTE, ccbEstudiantil.getPorcentajeAplicado());
    }

    public static Double obtenerPorcentajeBecario(String cedula) {
        String cedulaLimpia = normalizarCedula(cedula);
        if (!cedulaValida(cedulaLimpia)) {
            return null;
        }

        List<String> lineas = leerLineasGenericas(ARCHIVO_PORCENTAJES_BECARIOS);
        for (int i = lineas.size() - 1; i >= 0; i--) {
            String[] partes = lineas.get(i).split("\\|");
            if (partes.length < 2) {
                continue;
            }

            if (!cedulaLimpia.equals(normalizarCedula(partes[0]))) {
                continue;
            }

            return redondearMoneda(parseDoubleSeguro(partes[1]));
        }

        return null;
    }

    public static boolean tienePorcentajeBecarioConfigurado(String cedula) {
        return obtenerPorcentajeBecario(cedula) != null;
    }

    public static boolean guardarPorcentajeBecario(String cedula, double porcentajeBecario) {
        String cedulaLimpia = normalizarCedula(cedula);
        if (!cedulaValida(cedulaLimpia) || !esPorcentajeBecarioValidoParaHoy(porcentajeBecario)) {
            return false;
        }

        List<String> lineas = leerLineasGenericas(ARCHIVO_PORCENTAJES_BECARIOS);
        List<String> actualizadas = new ArrayList<>();
        boolean actualizado = false;
        String nuevaLinea = String.format(Locale.US, "%s|%.4f", cedulaLimpia, redondearMoneda(porcentajeBecario));

        for (String linea : lineas) {
            String[] partes = linea.split("\\|");
            if (partes.length >= 1 && cedulaLimpia.equals(normalizarCedula(partes[0]))) {
                if (!actualizado) {
                    actualizadas.add(nuevaLinea);
                    actualizado = true;
                }
                continue;
            }
            actualizadas.add(linea);
        }

        if (!actualizado) {
            actualizadas.add(nuevaLinea);
        }

        return reescribirArchivoGenerico(ARCHIVO_PORCENTAJES_BECARIOS, actualizadas);
    }

    private static boolean eliminarPorcentajeBecario(String cedula) {
        String cedulaLimpia = normalizarCedula(cedula);
        if (!cedulaValida(cedulaLimpia)) {
            return false;
        }

        List<String> lineas = leerLineasGenericas(ARCHIVO_PORCENTAJES_BECARIOS);
        List<String> actualizadas = new ArrayList<>();
        boolean eliminado = false;

        for (String linea : lineas) {
            String[] partes = linea.split("\\|");
            if (partes.length >= 1 && cedulaLimpia.equals(normalizarCedula(partes[0]))) {
                eliminado = true;
                continue;
            }
            actualizadas.add(linea);
        }

        if (!eliminado) {
            return true;
        }

        return reescribirArchivoGenerico(ARCHIVO_PORCENTAJES_BECARIOS, actualizadas);
    }

    public static boolean esPorcentajeBecarioValidoParaHoy(double porcentajeBecario) {
        double porcentajeEstudiantilHoy = obtenerPorcentajeCcbEstudiantilHoy();
        return porcentajeEstudiantilHoy > 0.0
            && !Double.isNaN(porcentajeBecario)
            && !Double.isInfinite(porcentajeBecario)
            && porcentajeBecario >= 0.0
            && porcentajeBecario < porcentajeEstudiantilHoy;
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
        return esAdminDirecto(cedulaLimpia);
    }

    private static boolean esAdminDirecto(String cedula) {
        List<String> admins = leerLineasGenericas(ARCHIVO_ADMINS);
        for (String linea : admins) {
            if (normalizarCedula(linea).equals(cedula)) return true;
        }
        return false;
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

    private static boolean removerAdmin(String cedula) {
        String cedulaLimpia = normalizarCedula(cedula);
        if (!cedulaValida(cedulaLimpia)) return false;

        List<String> admins = leerLineasGenericas(ARCHIVO_ADMINS);
        List<String> actualizados = new ArrayList<>();
        boolean cambio = false;

        for (String linea : admins) {
            String admin = normalizarCedula(linea);
            if (cedulaLimpia.equals(admin)) {
                cambio = true;
                continue;
            }
            actualizados.add(linea);
        }

        if (!cambio) {
            return true;
        }

        return reescribirArchivoGenerico(ARCHIVO_ADMINS, actualizados);
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
            Menu.TipoMenu tipoMenu = menu.getTipoMenu() == null ? Menu.TipoMenu.DESAYUNO : menu.getTipoMenu();
            boolean encontrado = false;
            String nuevaLinea = menuToLine(menu);

            for (String linea : lineas) {
                String[] partes = linea.split("\\|");
                if (partes.length > 0 && partes[0].equals(fecha)) {
                        Menu.TipoMenu tipoLinea = partes.length >= 3
                            ? parsearTipoMenu(partes[2])
                            : Menu.TipoMenu.DESAYUNO;
                        boolean coincideTipo = tipoLinea == tipoMenu;
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
                if (partes.length > 0 && !fechasSemana.contains(partes[0])) {
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

    public static boolean reiniciarMenuDia(LocalDate fecha) {
        if (fecha == null) return false;

        try {
            List<String> actuales = leerLineasGenericas(ARCHIVO_MENUS);
            String fechaObjetivo = fecha.toString();

            List<String> nuevasLineas = new ArrayList<>();
            for (String linea : actuales) {
                String[] partes = linea.split("\\|");
                if (partes.length > 0 && !fechaObjetivo.equals(partes[0])) {
                    nuevasLineas.add(linea);
                }
            }

            asegurarMenuNoDisponible(nuevasLineas, fecha, Menu.TipoMenu.DESAYUNO);
            asegurarMenuNoDisponible(nuevasLineas, fecha, Menu.TipoMenu.ALMUERZO);

            return reescribirArchivoGenerico(ARCHIVO_MENUS, nuevasLineas);
        } catch (Exception e) {
            System.err.println("Error reiniciando menu del dia: " + e.getMessage());
            return false;
        }
    }

    public static List<LocalDate> obtenerFechasConMenusCreados() {
        List<LocalDate> fechas = new ArrayList<>();
        try {
            List<String> lineas = leerLineasGenericas(ARCHIVO_MENUS);
            Set<LocalDate> unicas = new java.util.TreeSet<>();

            for (String linea : lineas) {
                if (linea == null || linea.isBlank()) continue;

                String[] partes = linea.split("\\|");
                if (partes.length == 0) continue;

                LocalDate fecha = LocalDate.parse(partes[0].trim());
                EstadoMenu estado = partes.length >= 2 ? parsearEstadoMenu(partes[1]) : EstadoMenu.CON_MENU;
                if (estado != EstadoMenu.NO_DISPONIBLE) {
                    unicas.add(fecha);
                }
            }

            fechas.addAll(unicas);
            return fechas;
        } catch (Exception e) {
            System.err.println("Error obteniendo fechas con menus creados: " + e.getMessage());
            return fechas;
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

        Menu exacto = buscarMenuPorFechaYTipo(lineas, fechaStr, Menu.TipoMenu.DESAYUNO);
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
        Menu.TipoMenu tipoSeguro = tipoMenu == null ? Menu.TipoMenu.DESAYUNO : tipoMenu;
        Menu menu = buscarMenuPorFechaYTipo(lineas, fechaStr, tipoSeguro);
        if (menu != null) return menu;

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
            Menu.TipoMenu tipoMenu = Menu.TipoMenu.DESAYUNO;

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
        if (raw == null || raw.isBlank()) return Menu.TipoMenu.DESAYUNO;
        try {
            return Menu.TipoMenu.valueOf(raw.trim());
        } catch (IllegalArgumentException ex) {
            return Menu.TipoMenu.DESAYUNO;
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
        return tipoMenu == null ? Menu.TipoMenu.DESAYUNO.name() : tipoMenu.name();
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
        Menu.TipoMenu tipoSeguro = tipoMenu == null ? Menu.TipoMenu.DESAYUNO : tipoMenu;
        for (String linea : lineas) {
            String[] partes = linea.split("\\|");
            if (partes.length > 0 && fechaStr.equals(partes[0])) {
                Menu.TipoMenu tipoLinea = partes.length >= 3
                        ? parsearTipoMenu(partes[2])
                : Menu.TipoMenu.DESAYUNO;
            boolean coincideTipo = tipoLinea == tipoSeguro;
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
        Menu.TipoMenu tipoSeguro = tipoMenu == null ? Menu.TipoMenu.DESAYUNO : tipoMenu;
        for (String linea : lineas) {
            String[] partes = linea.split("\\|");
            if (partes.length > 0 && partes[0].equals(fechaStr)) {
                Menu.TipoMenu tipoLinea = partes.length >= 3
                        ? parsearTipoMenu(partes[2])
                : Menu.TipoMenu.DESAYUNO;
            boolean coincideTipo = tipoLinea == tipoSeguro;
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
        if (!Float.isFinite(precioUnitario) || precioUnitario < 1.0f || precioUnitario > PRECIO_MAXIMO_INSUMO) return false;

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
        if (ccb.getFecha() == null || ccb.getFecha().isBefore(LocalDate.now())) {
            return false;
        }
        String nuevaLinea = formatearLineaCcb(ccb);
        List<String> lineas = leerLineasGenericas(ARCHIVO_CCB);
        List<String> actualizadas = new ArrayList<>();

        TipoUsuario tipoNuevo = CCB.parseTipoUsuario(ccb.getTipoUsuario());
        LocalDate fechaNueva = ccb.getFecha();
        boolean reemplazado = false;

        for (String linea : lineas) {
            CCB existente = parsearLineaCcb(linea);
            if (existente == null) {
                actualizadas.add(linea);
                continue;
            }

            TipoUsuario tipoExistente = CCB.parseTipoUsuario(existente.getTipoUsuario());
            boolean mismaFecha = fechaNueva.equals(existente.getFecha());
            boolean mismoTipo = CCB.coincideTipoCcb(existente.getTipoUsuario(), tipoNuevo)
                || CCB.coincideTipoCcb(ccb.getTipoUsuario(), tipoExistente);

            if (mismaFecha && mismoTipo) {
                if (!reemplazado) {
                    actualizadas.add(nuevaLinea);
                    reemplazado = true;
                }
                continue;
            }

            actualizadas.add(linea);
        }

        if (!reemplazado) {
            actualizadas.add(nuevaLinea);
        }

        return reescribirArchivoGenerico(ARCHIVO_CCB, actualizadas);
    }

    private static String formatearLineaCcb(CCB ccb) {
        return String.format(
            Locale.US,
            "%s|%s|%.2f|%.2f|%.2f|%.4f|%.4f|%.4f",
            ccb.getFecha().toString(),
            CCB.normalizarTipoConfiguracion(ccb.getTipoUsuario()),
            ccb.getCf(),
            ccb.getCv(),
            ccb.getNb(),
            ccb.getMerma(),
            ccb.getCcb(),
            ccb.getPorcentajeAplicado()
        );
    }

    public static CCB obtenerUltimoCcb() {
        List<String> lineas = leerLineasGenericas(ARCHIVO_CCB);
        if (lineas.isEmpty()) return null;
        String ultima = lineas.get(lineas.size() - 1);
        return parsearLineaCcb(ultima);
    }

    public static CCB obtenerUltimoCcbPorTipo(TipoUsuario tipoUsuario) {
        List<String> lineas = leerLineasGenericas(ARCHIVO_CCB);
        if (lineas.isEmpty()) {
            return null;
        }

        CCB coincidenciaCompatible = null;

        for (int i = lineas.size() - 1; i >= 0; i--) {
            CCB ccb = parsearLineaCcb(lineas.get(i));
            if (ccb == null) {
                continue;
            }

            TipoUsuario tipoRegistro = CCB.parseTipoUsuario(ccb.getTipoUsuario());
            if (tipoUsuario != null && tipoUsuario.esTipoEstudiantil()) {
                if (tipoRegistro == TipoUsuario.ESTUDIANTE) {
                    return ccb;
                }

                if (coincidenciaCompatible == null && CCB.coincideTipoCcb(ccb.getTipoUsuario(), tipoUsuario)) {
                    coincidenciaCompatible = ccb;
                }
                continue;
            }

            if (CCB.coincideTipoCcb(ccb.getTipoUsuario(), tipoUsuario)) {
                return ccb;
            }
        }

        return coincidenciaCompatible;
    }

    public static CCB obtenerCcbPorFechaYTipo(LocalDate fecha, TipoUsuario tipoUsuario) {
        if (fecha == null || tipoUsuario == null) {
            return null;
        }

        List<String> lineas = leerLineasGenericas(ARCHIVO_CCB);
        if (lineas.isEmpty()) {
            return null;
        }

        CCB coincidenciaCompatible = null;

        for (int i = lineas.size() - 1; i >= 0; i--) {
            CCB ccb = parsearLineaCcb(lineas.get(i));
            if (ccb == null) {
                continue;
            }

            if (!fecha.equals(ccb.getFecha())) {
                continue;
            }

            TipoUsuario tipoRegistro = CCB.parseTipoUsuario(ccb.getTipoUsuario());
            if (tipoUsuario.esTipoEstudiantil()) {
                if (tipoRegistro == TipoUsuario.ESTUDIANTE) {
                    return ccb;
                }

                if (coincidenciaCompatible == null && CCB.coincideTipoCcb(ccb.getTipoUsuario(), tipoUsuario)) {
                    coincidenciaCompatible = ccb;
                }
                continue;
            }

            if (CCB.coincideTipoCcb(ccb.getTipoUsuario(), tipoUsuario)) {
                return ccb;
            }
        }

        return coincidenciaCompatible;
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
        return CCB.calcularMontoPorTipo(ccbBase, tipoUsuario);
    }

    public static double generarPorcentajeCcbPorTipo(TipoUsuario tipoUsuario) {
        return CCB.generarPorcentajePorTipo(tipoUsuario);
    }

    public static double calcularMontoCcbPorTipo(double ccbBase, TipoUsuario tipoUsuario, double porcentajeCcb) {
        return CCB.calcularMontoPorTipo(ccbBase, tipoUsuario, porcentajeCcb);
    }

    public static double calcularMontoCcbParaCedula(String cedula) {
        if (!cedulaValida(cedula)) {
            return 0.0;
        }

        TipoUsuario tipoUsuario = obtenerTipoUsuario(cedula);
        TipoUsuario tipoConfiguracion = tipoUsuario != null && tipoUsuario.esTipoEstudiantil()
            ? TipoUsuario.ESTUDIANTE
            : tipoUsuario;
        CCB ccbHoy = obtenerCcbPorFechaYTipo(LocalDate.now(), tipoConfiguracion);
        if (ccbHoy == null) {
            return 0.0;
        }

        if (tipoUsuario == TipoUsuario.EXONERADO) {
            return 0.0;
        }

        double porcentajeAplicado;
        if (tipoUsuario == TipoUsuario.BECARIO) {
            Double porcentajeBecario = obtenerPorcentajeBecario(cedula);
            if (porcentajeBecario == null) {
                return 0.0;
            }
            porcentajeAplicado = porcentajeBecario;
        } else {
            porcentajeAplicado = CCB.normalizarPorcentajeParaTipo(tipoUsuario, ccbHoy.getPorcentajeAplicado());
        }

        return calcularMontoCcbPorTipo(ccbHoy.getCcb(), tipoUsuario, porcentajeAplicado);
    }

    public static double calcularMontoCcbParaCedula(String cedula, double porcentajeCcb) {
        if (!cedulaValida(cedula)) {
            return 0.0;
        }

        TipoUsuario tipoUsuario = obtenerTipoUsuario(cedula);
        TipoUsuario tipoConfiguracion = tipoUsuario != null && tipoUsuario.esTipoEstudiantil()
            ? TipoUsuario.ESTUDIANTE
            : tipoUsuario;
        CCB ccbHoy = obtenerCcbPorFechaYTipo(LocalDate.now(), tipoConfiguracion);
        if (ccbHoy == null) {
            return 0.0;
        }

        return calcularMontoCcbPorTipo(ccbHoy.getCcb(), tipoUsuario, porcentajeCcb);
    }

    public static List<Menu> obtenerTodosLosMenus() {
        List<String> lineas = leerLineasGenericas(ARCHIVO_MENUS);
        List<Menu> resultado = new ArrayList<>();
        for (String linea : lineas) {
            if (linea == null || linea.isBlank()) {
                continue;
            }
            String[] partes = linea.split("\\|");
            resultado.add(parsearLineaMenu(partes));
        }
        return resultado;
    }

    public static List<Menu> obtenerUltimos5Menus() {
        List<Menu> menus = obtenerTodosLosMenus();
        List<Menu> resultado = new ArrayList<>();
        int inicio = Math.max(0, menus.size() - 5);
        for (int i = inicio; i < menus.size(); i++) {
            resultado.add(menus.get(i));
        }
        return resultado;
    }

    public static double calcularCostoMenusDelDia(LocalDate fecha) {
        if (fecha == null) return 0.0;

        List<String> lineas = leerLineasGenericas(ARCHIVO_MENUS);
        double total = 0.0;

        for (String linea : lineas) {
            String[] partes = linea.split("\\|");
            if (partes.length < 2 || !fecha.toString().equals(partes[0])) {
                continue;
            }

            Menu menu = parsearLineaMenu(partes);
            if (menu == null || menu.getEstado() != EstadoMenu.CON_MENU) {
                continue;
            }

            total += menu.getCostoMenu();
        }

        return redondearMoneda(total);
    }

    private static CCB parsearLineaCcb(String linea) {
        if (linea == null || linea.isBlank()) return null;
        String[] partes = linea.split("\\|");
        if (partes.length < 7) return null;
        try {
            LocalDate fecha = LocalDate.parse(partes[0].trim());
            String tipoUsuario = partes[1].trim();
            double cf = parseDoubleSeguro(partes[2]);
            double cv = parseDoubleSeguro(partes[3]);
            double nb = parseDoubleSeguro(partes[4]);
            double merma = parseDoubleSeguro(partes[5]);
            TipoUsuario tipoEnum = CCB.parseTipoUsuario(tipoUsuario);
            double[] rango = CCB.obtenerRangoPorTipoUsuario(tipoEnum);
            double porcentaje = (partes.length >= 8)
                ? parseDoubleSeguro(partes[7])
                : (rango[0] + rango[1]) / 2.0;
            return new CCB(fecha, tipoUsuario, cf, cv, nb, merma, porcentaje);
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
            String nombre = repararTextoMojibakeBasico(partes[0].trim());
            int cantidad = (int) parseFloatSeguro(partes[1]);
            float total = parseFloatSeguro(partes[2]);
            float unitario = (cantidad > 0) ? (total / cantidad) : 0.0f;
            Insumo insumo = new Insumo(nombre, cantidad, "Ingrediente", unitario);
            insumo.setCostoInsumoTotal(total);
            return insumo;
        }

        return new Insumo(repararTextoMojibakeBasico(limpio), 1, "Ingrediente", 0.0f);
    }

    private static Insumo parsearLineaInsumo(String linea) {
        String sinPuntoYComa = linea.replace(";", "");
        String[] partes = sinPuntoYComa.split(":");
        if (partes.length < 4) return null;

        String nombre = repararTextoMojibakeBasico(partes[0].trim());
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

    private static String repararTextoMojibakeBasico(String valor) {
        String texto = valorSeguro(valor);
        if (texto.isEmpty()) {
            return texto;
        }

        return texto
            .replace("Ã±", "ñ")
            .replace("Ã‘", "Ñ")
            .replace("Ã¡", "á")
            .replace("Ã©", "é")
            .replace("Ã­", "í")
            .replace("Ã³", "ó")
            .replace("Ãº", "ú")
            .replace("Ã", "Á")
            .replace("Ã‰", "É")
            .replace("Ã", "Í")
            .replace("Ã“", "Ó")
            .replace("Ãš", "Ú");
    }

    private static boolean nombreValido(String nombre) {
        return nombre != null
            && !nombre.isBlank()
            && nombre.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+$");
    }

    private static TipoUsuario mapearTipoSecretaria(String valorTipo) {
        String normalizado = normalizarTextoTipo(valorTipo);

        return switch (normalizado) {
            case "estudiante" -> TipoUsuario.ESTUDIANTE;
            case "becario" -> TipoUsuario.BECARIO;
            case "exonerado" -> TipoUsuario.EXONERADO;
            case "profesor" -> TipoUsuario.PROFESOR;
            case "trabajador", "empleado" -> TipoUsuario.EMPLEADO;
            case "administrador", "admin" -> TipoUsuario.ADMIN;
            default -> null;
        };
    }

    private static TipoUsuario resolverTipoDesdeRegistroSecretaria(String[] partes) {
        TipoUsuario tipoBase = mapearTipoSecretaria(partes[1]);
        if (tipoBase != TipoUsuario.ESTUDIANTE || partes.length < 3) {
            return tipoBase;
        }

        TipoUsuario subtipoUsuario = mapearSubtipoUsuario(partes[2]);
        return subtipoUsuario == null ? tipoBase : subtipoUsuario;
    }

    private static TipoUsuario mapearSubtipoUsuario(String valor) {
        String normalizado = normalizarTextoTipo(valor);
        return switch (normalizado) {
            case "regular", "estudiante" -> TipoUsuario.ESTUDIANTE;
            case "becario" -> TipoUsuario.BECARIO;
            case "exonerado" -> TipoUsuario.EXONERADO;
            default -> null;
        };
    }

    private static boolean actualizarTipoEnPadronSecretaria(String cedula, TipoUsuario nuevoTipo) {
        List<String> lineas = leerLineasSecretaria(ARCHIVO_PADRON_SECRETARIA);
        List<String> actualizadas = new ArrayList<>();
        boolean encontrado = false;
        String registroNuevo = construirRegistroSecretaria(cedula, nuevoTipo);
        if (registroNuevo == null) {
            return false;
        }

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
            if (cedula.equals(cedulaPadron)) {
                actualizadas.add(registroNuevo);
                encontrado = true;
            } else {
                actualizadas.add(linea);
            }
        }

        if (!encontrado) {
            return false;
        }

        return reescribirArchivoSecretaria(ARCHIVO_PADRON_SECRETARIA, actualizadas);
    }

    private static boolean actualizarTipoEnUsuariosRegistrados(String cedula, TipoUsuario nuevoTipo) {
        List<String> lineas = leerLineasGenericas(ARCHIVO_USUARIOS);
        List<String> actualizadas = new ArrayList<>();
        boolean encontrado = false;

        for (String linea : lineas) {
            String[] partes = linea.split(":");
            if (partes.length > 0 && cedula.equals(normalizarCedula(partes[0]))) {
                actualizadas.add(reconstruirLineaUsuarioConTipo(partes, nuevoTipo));
                encontrado = true;
            } else {
                actualizadas.add(linea);
            }
        }

        if (!encontrado) {
            return true;
        }

        return reescribirArchivoGenerico(ARCHIVO_USUARIOS, actualizadas);
    }

    private static String reconstruirLineaUsuarioConTipo(String[] partes, TipoUsuario nuevoTipo) {
        String cedula = partes.length > 0 ? partes[0].trim() : "";
        String clave = partes.length > 1 ? partes[1].trim() : "";
        String nombre = partes.length > 2 ? partes[2].trim() : "";
        String correo = partes.length > 3 ? partes[3].trim() : "";
        String saldo = "0.0;";

        if (partes.length >= 6) {
            saldo = asegurarSaldoConTerminador(partes[5]);
        } else if (partes.length == 5) {
            String quinto = valorSeguro(partes[4]);
            if (esValorNumerico(quinto.replace(";", ""))) {
                saldo = asegurarSaldoConTerminador(quinto);
            }
        }

        return String.format(
            Locale.ROOT,
            "%s:%s:%s:%s:%s:%s",
            cedula,
            clave,
            nombre,
            correo,
            nuevoTipo.toEtiqueta(),
            saldo
        );
    }

    private static String construirRegistroSecretaria(String cedula, TipoUsuario tipoUsuario) {
        if (tipoUsuario == null) {
            return null;
        }

        return switch (tipoUsuario) {
            case ESTUDIANTE -> cedula + ":estudiante:regular";
            case BECARIO -> cedula + ":estudiante:becario";
            case EXONERADO -> cedula + ":estudiante:exonerado";
            case PROFESOR -> cedula + ":profesor";
            case EMPLEADO -> cedula + ":empleado";
            case ADMIN -> cedula + ":administrador";
            default -> null;
        };
    }

    private static String asegurarSaldoConTerminador(String saldo) {
        String limpio = valorSeguro(saldo);
        return limpio.endsWith(";") ? limpio : limpio + ";";
    }

    private static boolean esValorNumerico(String valor) {
        if (valor == null) return false;
        String limpio = valor.trim();
        if (limpio.isEmpty()) return false;
        return limpio.matches("[-+]?\\d+(?:[\\.,]\\d+)?");
    }

    private static String normalizarTextoTipo(String valorTipo) {
        return valorSeguro(valorTipo)
                .toLowerCase(Locale.ROOT)
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u");
    }

    private static String etiquetaTipoComensal(TipoUsuario tipoUsuario) {
        if (tipoUsuario == null) {
            return TipoUsuario.COMENSAL.name();
        }

        return switch (tipoUsuario) {
            case ESTUDIANTE -> "ESTUDIANTE_REGULAR";
            case BECARIO -> "ESTUDIANTE_BECARIO";
            case EXONERADO -> "ESTUDIANTE_EXONERADO";
            default -> tipoUsuario.name();
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

