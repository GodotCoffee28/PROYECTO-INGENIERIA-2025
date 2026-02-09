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
import java.util.ArrayList;
import java.util.List;

import com.gesco.models.Insumo;
import com.gesco.models.Menu;
import com.gesco.models.Platillo;

public class DataBase {

    // --- CONSTANTES ---
    private static final String ARCHIVO_USUARIOS = "usuarios.txt";
    private static final String ARCHIVO_ADMINS = "admins.txt";
    private static final String ARCHIVO_MENUS = "menus.txt";
    private static final String ARCHIVO_CFCV = "cfcv.txt";
    /**
     * Directorio relativo o absoluto donde se almacenan los archivos de datos.
     * Por defecto apunta a la carpeta existente dentro del proyecto pero puede
     * ser sobrescrito mediante la propiedad del sistema `gesco.data.dir` o la
     * variable de entorno `GESCO_DATA_DIR`.
     */
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

    /** Permite cambiar en tiempo de ejecución la carpeta de datos. */
    public static void setDataDir(String dataDir) {
        if (dataDir != null && !dataDir.isBlank()) DATA_DIR = dataDir.trim();
    }

    /** Obtiene la ruta actualmente configurada para datos. */
    public static String getDataDir() { return DATA_DIR; }



    /**
     * Obtiene el objeto File para cualquier nombre de archivo dentro de DATA_DIR.
     */
    private static File obtenerArchivo(String nombreArchivo) {
        Path basePath = obtenerBaseProyecto();
        Path dataPath = Paths.get(DATA_DIR);
        if (dataPath.isAbsolute()) {
            return dataPath.resolve(nombreArchivo).toFile();
        } else {
            return basePath.resolve(DATA_DIR).resolve(nombreArchivo).toFile();
        }
    }

    /**
     * Asegura que el archivo y sus carpetas existan.
     */
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

    /**
     * Escribe contenido al final de un archivo (Append).
     * Maneja automáticamente la creación del archivo y los errores.
     */
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

    /**
     * Lee todo el contenido de un archivo y lo devuelve como una lista de líneas.
     * Filtra líneas vacías automáticamente.
     */
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

    /**
     * Sobrescribe un archivo completo con una nueva lista de líneas.
     * Útil para actualizaciones (Update/Delete).
     */
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


    // USUARIOS 

    public static boolean registrarUsuario(String cedula, String clave, String nombre, String correo) {
        if (!cedulaValida(cedula)) return false;
        if (usuarioExiste(cedula)) return false;

        String linea = String.format("%s:%s:%s:%s:0.0;", 
                valorSeguro(cedula), valorSeguro(clave), valorSeguro(nombre), valorSeguro(correo));
        
        return escribirLineaGenerica(ARCHIVO_USUARIOS, linea + System.lineSeparator());
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

    //  ADMINS 

    public static boolean esAdmin(String cedula) {
        if (!cedulaValida(cedula)) return false;
        List<String> admins = leerLineasGenericas(ARCHIVO_ADMINS);
        for (String linea : admins) {
            if (linea.trim().equals(cedula)) return true;
        }
        return false;
    }

    // SALDOS

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

    //  MENÚS 

    public static boolean guardarMenu(Menu menu) {
        StringBuilder sb = new StringBuilder();
        sb.append(menu.getFecha().toString()).append("|");

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
        sb.append(menu.getFecha().toString()).append("|");
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

    /**
     * Sobrescribe la entrada del menú para la fecha indicada. Si no existe,
     * añade la línea al final del archivo.
     */
    public static boolean actualizarMenu(Menu menu) {
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

    /**
     * Reinicia los menús de la semana actual (Lunes-Viernes) a estado por defecto.
     * Devuelve true si la operación fue exitosa.
     */
    public static boolean reiniciarMenusSemana() {
        try {
            List<String> actuales = leerLineasGenericas(ARCHIVO_MENUS);
            java.time.LocalDate hoy = java.time.LocalDate.now();
            java.time.DayOfWeek d = hoy.getDayOfWeek();
            java.time.LocalDate lunes = hoy.with(java.time.DayOfWeek.MONDAY);

            // Filtrar fuera las líneas correspondientes a la semana actual (lunes-viernes)
            java.util.Set<String> fechasSemana = new java.util.HashSet<>();
            for (int i = 0; i < 5; i++) {
                fechasSemana.add(lunes.plusDays(i).toString());
            }

            List<String> nuevas = new ArrayList<>();
            for (String linea : actuales) {
                String[] partes = linea.split("\\|");
                if (partes.length > 0 && fechasSemana.contains(partes[0])) {
                    // omitimos: será reemplazada por estado por defecto
                    continue;
                }
                nuevas.add(linea);
            }

            // Añadir líneas por defecto para lunes-viernes (sin platillos)
            for (int i = 0; i < 5; i++) {
                String fecha = lunes.plusDays(i).toString();
                nuevas.add(fecha + "|");
            }

            return reescribirArchivoGenerico(ARCHIVO_MENUS, nuevas);
        } catch (Exception e) {
            System.err.println("Error reiniciando menus: " + e.getMessage());
            return false;
        }
    }

    public static Menu obtenerMenuPorFecha(String fechaStr) {
        List<String> lineas = leerLineasGenericas(ARCHIVO_MENUS);

        for (String linea : lineas) {
            String[] partes = linea.split("\\|");
            if (partes.length > 0 && partes[0].equals(fechaStr)) {
                return parsearLineaMenu(partes); // Delegamos el parsing a otro método auxiliar
            }
        }
        return new Menu(); // Retorno por defecto
    }

    private static Menu parsearLineaMenu(String[] partesPrincipales) {
        try {
            LocalDate fecha = LocalDate.parse(partesPrincipales[0]);
            Menu menu = new Menu(fecha);

            if (partesPrincipales.length < 2) return menu;

            String[] partesPlatillos = partesPrincipales[1].split(";");
            for (String parteP : partesPlatillos) {
                if (parteP.isBlank()) continue;

                String[] datosPlato = parteP.split(">");
                Platillo platillo = new Platillo(datosPlato[0]);

                if (datosPlato.length > 1) {
                    for (String nomInsumo : datosPlato[1].split(",")) {
                        platillo.agregarInsumo(new Insumo(nomInsumo, 1, "Ingrediente"));
                    }
                } else {
                    platillo.agregarInsumo(new Insumo("Sin insumos", 0, "N/A"));
                }
                menu.agregarPlatillo(platillo);
            }
            return menu;
        } catch (Exception e) {
            System.err.println("Error parseando menú: " + e.getMessage());
            return new Menu();
        }
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
        return cedula != null && cedula.matches("\\d+");
    }
}