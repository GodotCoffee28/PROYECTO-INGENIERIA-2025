package com.gesco.controllers.registros;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import com.gesco.controllers.gestion_principal.DataBase;
import com.gesco.views.Registros.VistaHistorialFila;

public class ControladorHistorialFila {

    private final VistaHistorialFila vista;
    private final Runnable onBack;
    private Timer refrescoTimer;

    public ControladorHistorialFila(VistaHistorialFila vista, Runnable onBack) {
        this.vista = vista;
        this.onBack = onBack;
    }

    public void conectar() {
        vista.getBackIcon().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                detenerRefresco();
                onBack.run();
            }
        });

        vista.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                detenerRefresco();
            }
        });

        cargar();
        iniciarRefrescoAutomatico();
    }

    public void cargar() {
        Path archivoAcudieron = resolverRutaAcudieron();

        vista.limpiarHistorial();
        vista.actualizarGananciaSemanal(0.0);
        vista.actualizarResumenHoy(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        vista.actualizarResumenSemanal(0, 0, 0, 0, 0, 0);

        if (!Files.exists(archivoAcudieron)) {
            vista.mostrarMensajeVacio("No hay registros en acudieron.txt para mostrar.");
            return;
        }

        try {
            List<String> lineas = Files.readAllLines(archivoAcudieron, StandardCharsets.UTF_8);
            List<RegistroAcudieron> registros = parsearRegistros(lineas);

            if (registros.isEmpty()) {
                vista.mostrarMensajeVacio("No hay registros validos en acudieron.txt.");
                return;
            }

            LocalDate hoy = LocalDate.now();
            WeekFields weekFields = WeekFields.of(Locale.getDefault());
            int semanaActual = hoy.get(weekFields.weekOfWeekBasedYear());
            int anioSemanaActual = hoy.get(weekFields.weekBasedYear());

            double gananciaSemanal = 0.0;
            Contadores resumenSemanal = new Contadores();
            Contadores hoyDesayuno = new Contadores();
            Contadores hoyAlmuerzo = new Contadores();

            Map<String, Contadores> conteoAcumuladoTurno = new LinkedHashMap<>();
            Map<String, Double> cobroAcumuladoTurno = new LinkedHashMap<>();

            for (int i = 0; i < registros.size(); i++) {
                RegistroAcudieron registro = registros.get(i);

                if (esMismaSemana(registro.fecha, semanaActual, anioSemanaActual, weekFields)) {
                    gananciaSemanal += registro.cobro;
                    resumenSemanal.incrementar(registro.tipoUsuario);
                }

                if (hoy.equals(registro.fecha)) {
                    if ("DESAYUNO".equals(registro.tipoServicio)) {
                        hoyDesayuno.incrementar(registro.tipoUsuario);
                    } else {
                        hoyAlmuerzo.incrementar(registro.tipoUsuario);
                    }
                }

                String claveTurno = registro.fechaTexto + "|" + registro.tipoServicio;
                Contadores acumuladoTurno = conteoAcumuladoTurno.computeIfAbsent(claveTurno, k -> new Contadores());
                acumuladoTurno.incrementar(registro.tipoUsuario);

                double totalTurno = cobroAcumuladoTurno.getOrDefault(claveTurno, 0.0) + registro.cobro;
                cobroAcumuladoTurno.put(claveTurno, totalTurno);

                vista.agregarRegistroALista(
                    registro.fechaTexto,
                    registro.tipoServicio,
                    registro.cedula,
                    registro.tipoUsuario,
                    registro.cobro,
                    totalTurno,
                    acumuladoTurno.regulares,
                    acumuladoTurno.exonerados,
                    acumuladoTurno.becarios,
                    acumuladoTurno.empleados,
                    acumuladoTurno.profesores,
                    acumuladoTurno.admins
                );

                if (esFinDeTurno(registros, i, claveTurno)) {
                    vista.agregarSeparadorTurno();
                }
            }

            vista.actualizarGananciaSemanal(gananciaSemanal);
            vista.actualizarResumenHoy(
                hoyDesayuno.regulares,
                hoyDesayuno.becarios,
                hoyDesayuno.exonerados,
                hoyDesayuno.profesores,
                hoyDesayuno.empleados,
                hoyDesayuno.admins,
                hoyAlmuerzo.regulares,
                hoyAlmuerzo.becarios,
                hoyAlmuerzo.exonerados,
                hoyAlmuerzo.profesores,
                hoyAlmuerzo.empleados,
                hoyAlmuerzo.admins
            );
            vista.actualizarResumenSemanal(
                resumenSemanal.regulares,
                resumenSemanal.becarios,
                resumenSemanal.exonerados,
                resumenSemanal.empleados,
                resumenSemanal.profesores,
                resumenSemanal.admins
            );
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                vista,
                "No se pudo leer el archivo acudieron.txt.",
                "Error de lectura",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private List<RegistroAcudieron> parsearRegistros(List<String> lineas) {
        List<RegistroAcudieron> registros = new ArrayList<>();

        for (String linea : lineas) {
            String texto = linea == null ? "" : linea.trim();
            if (texto.isBlank()) {
                continue;
            }

            String[] partes = texto.split(":", 6);
            if (partes.length < 6) {
                continue;
            }

            LocalDate fecha = parseFecha(partes[0].trim());
            if (fecha == null) {
                continue;
            }

            registros.add(new RegistroAcudieron(
                fecha,
                partes[0].trim(),
                partes[1].trim(),
                normalizarTipoUsuario(partes[3].trim()),
                normalizarTipoServicio(partes[4].trim()),
                parseMonto(partes[5])
            ));
        }

        return registros;
    }

    private boolean esFinDeTurno(List<RegistroAcudieron> registros, int indiceActual, String claveTurnoActual) {
        if (indiceActual >= registros.size() - 1) {
            return true;
        }

        RegistroAcudieron siguiente = registros.get(indiceActual + 1);
        String claveSiguiente = siguiente.fechaTexto + "|" + siguiente.tipoServicio;
        return !claveTurnoActual.equals(claveSiguiente);
    }

    private boolean esMismaSemana(LocalDate fecha, int semanaActual, int anioSemanaActual, WeekFields weekFields) {
        return fecha.get(weekFields.weekOfWeekBasedYear()) == semanaActual
            && fecha.get(weekFields.weekBasedYear()) == anioSemanaActual;
    }

    private LocalDate parseFecha(String valor) {
        try {
            return LocalDate.parse(valor, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    private String normalizarTipoServicio(String valor) {
        String tipo = valor == null ? "" : valor.trim().toUpperCase(Locale.ROOT);
        return "ALMUERZO".equals(tipo) ? "ALMUERZO" : "DESAYUNO";
    }

    private String normalizarTipoUsuario(String valor) {
        String tipo = valor == null ? "" : valor.trim().toUpperCase(Locale.ROOT);

        if (tipo.contains("BECARIO")) {
            return "BECARIO";
        }
        if (tipo.contains("EXONERADO")) {
            return "EXONERADO";
        }
        if (tipo.contains("PROFESOR")) {
            return "PROFESOR";
        }
        if (tipo.contains("EMPLEADO") || tipo.contains("TRABAJADOR")) {
            return "EMPLEADO";
        }
        if (tipo.contains("ADMIN")) {
            return "ADMIN";
        }
        return "REGULAR";
    }

    private void iniciarRefrescoAutomatico() {
        detenerRefresco();
        refrescoTimer = new Timer(2000, e -> cargar());
        refrescoTimer.start();
    }

    private void detenerRefresco() {
        if (refrescoTimer != null) {
            refrescoTimer.stop();
            refrescoTimer = null;
        }
    }

    private double parseMonto(String valor) {
        String monto = valor == null ? "" : valor.trim();
        if (monto.isEmpty()) {
            return 0.0;
        }

        try {
            return Double.parseDouble(monto.replace(',', '.'));
        } catch (NumberFormatException ex) {
            return 0.0;
        }
    }

    private Path resolverRutaAcudieron() {
        Path dataPath = Paths.get(DataBase.getDataDir());
        Path basePath = obtenerBaseProyecto();

        if (dataPath.isAbsolute()) {
            return dataPath.resolve("acudieron.txt");
        }

        return basePath.resolve(dataPath).resolve("acudieron.txt");
    }

    private Path obtenerBaseProyecto() {
        try {
            Path ubicacion = Paths.get(DataBase.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            if (ubicacion.endsWith("classes") && ubicacion.getParent() != null && ubicacion.getParent().getParent() != null) {
                return ubicacion.getParent().getParent();
            }
        } catch (URISyntaxException e) {
            // No-op: si falla, usa user.dir
        }
        return Paths.get(System.getProperty("user.dir"));
    }

    private static final class RegistroAcudieron {
        private final LocalDate fecha;
        private final String fechaTexto;
        private final String cedula;
        private final String tipoUsuario;
        private final String tipoServicio;
        private final double cobro;

        private RegistroAcudieron(
            LocalDate fecha,
            String fechaTexto,
            String cedula,
            String tipoUsuario,
            String tipoServicio,
            double cobro
        ) {
            this.fecha = fecha;
            this.fechaTexto = fechaTexto;
            this.cedula = cedula;
            this.tipoUsuario = tipoUsuario;
            this.tipoServicio = tipoServicio;
            this.cobro = cobro;
        }
    }

    private static final class Contadores {
        private int regulares;
        private int becarios;
        private int exonerados;
        private int profesores;
        private int empleados;
        private int admins;

        private void incrementar(String tipo) {
            switch (tipo) {
                case "BECARIO" -> becarios++;
                case "EXONERADO" -> exonerados++;
                case "PROFESOR" -> profesores++;
                case "EMPLEADO" -> empleados++;
                case "ADMIN" -> admins++;
                default -> regulares++;
            }
        }
    }
}
