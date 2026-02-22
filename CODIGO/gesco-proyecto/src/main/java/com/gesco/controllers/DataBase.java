package com.gesco.controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.gesco.models.CCB;
import com.gesco.models.Insumo;
import com.gesco.models.Menu;
import com.gesco.models.Menu.EstadoMenu;
import com.gesco.models.Platillo;
import com.gesco.models.TipoUsuario;

public class DataBase {

    private static final String ARCHIVO_USUARIOS = "usuarios.txt";
    private static final String ARCHIVO_ADMINS = "admins.txt";
    private static final String ARCHIVO_SUPER_ADMINS = "super_admins.txt";
    private static final String ARCHIVO_ADMINS_AUTORIZADOS = "admins_autorizados.txt";
    private static final String ARCHIVO_MENUS = "menus.txt";
    private static final String ARCHIVO_CFCV = "cfcv.txt";
    private static final String ARCHIVO_CCB = "ccb.txt";
    private static final String ARCHIVO_FERIADOS = "feriados.txt";
    private static final String ARCHIVO_FESTIVOS = "dias_festivos_2026.txt";
    private static final String ARCHIVO_NO_LABORABLES = "sabados_domingos_2026.txt";

    private static final long CEDULA_MINIMA = 8_000_000L;
    private static final Set<String> FERIADOS_FIJOS_MM_DD = Set.of(
        "01-01", "05-01", "12-24", "12-25", "12-31"
    );

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

    // Obtiene la ruta actual 
    public static String getDataDir() { return DATA_DIR; }




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


    //usuaros

    public static boolean registrarUsuario(String cedula, String clave, String nombre, String correo) {
        if (!cedulaValida(cedula)) return false;
        if (usuarioExiste(cedula)) return false;

        String linea = String.format("%s:%s:%s:%s:0.0;", 
                valorSeguro(cedula), valorSeguro(clave), valorSeguro(nombre), valorSeguro(correo));
        
        return escribirLineaGenerica(ARCHIVO_USUARIOS, linea + System.lineSeparator());
    }

    public static boolean registrarAdministrador(String cedula, String clave, String nombre, String correo, String codigoAutorizacion) {
        if (!adminAutorizadoPorSuperAdmin(cedula, codigoAutorizacion)) return false;

        if (usuarioExiste(cedula)) {
            return agregarAdmin(cedula);
        }

        if (!registrarUsuario(cedula, clave, nombre, correo)) return false;
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

    //admin

    public static boolean esAdmin(String cedula) {
        if (!cedulaValida(cedula)) return false;
        if (esSuperAdmin(cedula)) return true;
        List<String> admins = leerLineasGenericas(ARCHIVO_ADMINS);
        for (String linea : admins) {
            if (linea.trim().equals(cedula)) return true;
        }
        return false;
    }

    public static boolean esSuperAdmin(String cedula) {
        if (!cedulaValida(cedula)) return false;
        List<String> supers = leerLineasGenericas(ARCHIVO_SUPER_ADMINS);
        for (String linea : supers) {
            if (linea.trim().equals(cedula)) return true;
        }
        return false;
    }

    public static boolean adminAutorizadoPorSuperAdmin(String cedula, String codigoAutorizacion) {
        if (!cedulaValida(cedula)) return false;
        List<String> autorizados = leerLineasGenericas(ARCHIVO_ADMINS_AUTORIZADOS);
        String codigo = valorSeguro(codigoAutorizacion);

        if (codigo.isBlank()) {
            return false;
        }

        for (String linea : autorizados) {
            String[] partes = linea.split(":", 2);
            if (partes.length == 0) continue;

            String cedulaPermitida = partes[0].trim();
            if (!cedula.equals(cedulaPermitida)) continue;

            if (partes.length < 2) {
                continue;
            }

            String codigoPermitido = partes[1].trim();
            if (!codigoPermitido.isBlank() && codigoPermitido.equals(codigo)) {
                return true;
            }
        }

        return false;
    }

    private static boolean agregarAdmin(String cedula) {
        if (esAdmin(cedula)) return true;
        return escribirLineaGenerica(ARCHIVO_ADMINS, cedula + System.lineSeparator());
    }

    //saldo

    public static double obtenerSaldo(String cedula) {
        if (!cedulaValida(cedula)) return 0.0;
        List<String> lineas = leerLineasGenericas(ARCHIVO_USUARIOS);
        for (String linea : lineas) {
            String[] partes = linea.split(":");
            if (partes.length >= 5 && partes[0].equals(cedula)) {
                try {
                    return Double.parseDouble(partes[4].replace(";", "").trim());
                } catch (NumberFormatException e) { return 0.0; }
            }
        }
        return 0.0;
    }

    public static boolean actualizarSaldo(String cedula, double nuevoSaldo) {
        if (!cedulaValida(cedula)) return false;
        List<String> lineas = leerLineasGenericas(ARCHIVO_USUARIOS);
        List<String> lineasActualizadas = new ArrayList<>();
        boolean encontrado = false;

        for (String linea : lineas) {
            String[] partes = linea.split(":", 5);
            if (partes.length >= 4 && partes[0].equals(cedula)) {
                String nuevaLinea = String.format("%s:%s:%s:%s:%.2f;",
                        partes[0], partes[1], partes[2], partes[3], nuevoSaldo);
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

    //menu 

    public static boolean guardarMenu(Menu menu) {
        if (!menuValidoParaGuardar(menu)) return false;
        StringBuilder sb = new StringBuilder();
        sb.append(menu.getFecha().toString()).append("|")
          .append(menu.getEstado().name())
          .append("|");

        if (menu.getEstado() == EstadoMenu.NO_DISPONIBLE) {
            return escribirLineaGenerica(ARCHIVO_MENUS, sb.toString() + System.lineSeparator());
        }

        for (Platillo p : menu.getPlatillos()) {
            sb.append(p.getNombre()).append(">");
            List<Insumo> insumos = p.getInsumos();
            for (int i = 0; i < insumos.size(); i++) {
                sb.append(insumos.get(i).getNombre());
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
          .append("|");

        if (menu.getEstado() == EstadoMenu.NO_DISPONIBLE) {
            return sb.toString();
        }

        for (Platillo p : menu.getPlatillos()) {
            sb.append(p.getNombre()).append(">");
            List<Insumo> insumos = p.getInsumos();
            for (int i = 0; i < insumos.size(); i++) {
                sb.append(insumos.get(i).getNombre());
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
            boolean encontrado = false;
            String nuevaLinea = menuToLine(menu);

            for (String linea : lineas) {
                String[] partes = linea.split("\\|");
                if (partes.length > 0 && partes[0].equals(fecha)) {
                    nuevas.add(nuevaLinea);
                    encontrado = true;
                } else {
                    nuevas.add(linea);
                }
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

    public static boolean reiniciarMenusSemana() {
        try {
            List<String> actuales = leerLineasGenericas(ARCHIVO_MENUS);
            LocalDate hoy = LocalDate.now();
            LocalDate ultimaFecha = fechaMaximaEnMenus(actuales);
            LocalDate base = (ultimaFecha != null && ultimaFecha.isAfter(hoy)) ? ultimaFecha : hoy.minusDays(1);

            List<LocalDate> nuevasFechas = obtenerSiguientesCincoDiasHabiles(base);

            List<String> nuevasLineas = new ArrayList<>(actuales);
            for (LocalDate fecha : nuevasFechas) {
                if (!existeMenuParaFecha(actuales, fecha)) {
                    Menu menuNoDisponible = new Menu(fecha, EstadoMenu.NO_DISPONIBLE);
                    nuevasLineas.add(menuToLine(menuNoDisponible));
                }
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
                if (!existeMenuParaFecha(nuevas, fecha)) {
                    nuevas.add(menuToLine(new Menu(fecha, EstadoMenu.NO_DISPONIBLE)));
                }
            }

            return reescribirArchivoGenerico(ARCHIVO_MENUS, nuevas);
        } catch (Exception e) {
            System.err.println("Error preparando semana: " + e.getMessage());
            return false;
        }
    }

    public static Menu obtenerMenuPorFecha(String fechaStr) {
        List<String> lineas = leerLineasGenericas(ARCHIVO_MENUS);

        for (String linea : lineas) {
            String[] partes = linea.split("\\|");
            if (partes.length > 0 && partes[0].equals(fechaStr)) {
                return parsearLineaMenu(partes); 
            }
        }
        try {
            return new Menu(LocalDate.parse(fechaStr), EstadoMenu.NO_DISPONIBLE);
        } catch (Exception ex) {
            return new Menu();
        }
    }

    private static Menu parsearLineaMenu(String[] partesPrincipales) {
        try {
            LocalDate fecha = LocalDate.parse(partesPrincipales[0]);
            EstadoMenu estado = EstadoMenu.CON_MENU;
            String bloquePlatillos = "";

            if (partesPrincipales.length >= 3) {
                estado = parsearEstadoMenu(partesPrincipales[1]);
                bloquePlatillos = partesPrincipales[2];
            } else if (partesPrincipales.length == 2) {
                bloquePlatillos = partesPrincipales[1];
            }

            Menu menu = new Menu(fecha, estado);

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
                    for (String nomInsumo : datosPlato[1].split(",")) {
                        platillo.agregarInsumo(new Insumo(nomInsumo, 1, "Ingrediente"));
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

    private static List<LocalDate> obtenerSiguientesCincoDiasHabiles(LocalDate baseExclusiva) {
        List<LocalDate> resultado = new ArrayList<>();
        LocalDate cursor = (baseExclusiva == null ? LocalDate.now() : baseExclusiva).plusDays(1);

        while (resultado.size() < 5) {
            if (esDiaHabil(cursor)) {
                resultado.add(cursor);
            }
            cursor = cursor.plusDays(1);
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

        List<String> festivos = leerLineasGenericas(ARCHIVO_FESTIVOS);
        for (String linea : festivos) {
            if (linea.trim().equals(fecha.toString())) return true;
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

    private static boolean menuValidoParaGuardar(Menu menu) {
        if (menu == null || menu.getFecha() == null) return false;
        if (!esFechaValidaParaMenu(menu.getFecha())) return false;

        if (menu.getEstado() == EstadoMenu.NO_DISPONIBLE) {
            return true;
        }

        return menu.tienePlatillos();
    }

    private static LocalDate fechaMaximaEnMenus(List<String> lineas) {
        LocalDate maxima = null;
        for (String linea : lineas) {
            String[] partes = linea.split("\\|");
            if (partes.length == 0) continue;
            try {
                LocalDate fecha = LocalDate.parse(partes[0]);
                if (maxima == null || fecha.isAfter(maxima)) {
                    maxima = fecha;
                }
            } catch (Exception ignored) {
            }
        }
        return maxima;
    }

    private static boolean existeMenuParaFecha(List<String> lineas, LocalDate fecha) {
        String fechaStr = fecha.toString();
        for (String linea : lineas) {
            String[] partes = linea.split("\\|");
            if (partes.length > 0 && fechaStr.equals(partes[0])) {
                return true;
            }
        }
        return false;
    }

    // CF/CV

    public static boolean guardarCfcv(com.gesco.models.CFCV cfcv) {
        if (cfcv == null) return false;
        List<String> lineas = new ArrayList<>();
        lineas.add(cfcv.toLine());
        return reescribirArchivoGenerico(ARCHIVO_CFCV, lineas);
    }

    public static com.gesco.models.CFCV obtenerCfcv() {
        List<String> lineas = leerLineasGenericas(ARCHIVO_CFCV);
        if (lineas.isEmpty()) return new com.gesco.models.CFCV();
        return com.gesco.models.CFCV.fromLine(lineas.get(0));
    }

    // CCB

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

    private static boolean cedulaValida(String cedula) {
        if (cedula == null || !cedula.matches("\\d+")) {
            return false;
        }
        try {
            return Long.parseLong(cedula) >= CEDULA_MINIMA;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}