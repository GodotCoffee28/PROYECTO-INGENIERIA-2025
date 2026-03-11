package com.gesco.controllers.registros;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.gesco.controllers.gestion_principal.DataBase;
import com.gesco.views.Registros.VistaHistorialFila;

public class ControladorHistorialFila {

    private final VistaHistorialFila vista;
    private final Runnable onBack;

    public ControladorHistorialFila(VistaHistorialFila vista, Runnable onBack) {
        this.vista = vista;
        this.onBack = onBack;
    }

    public void conectar() {
        vista.getBackIcon().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onBack.run();
            }
        });

        cargar();
    }

    public void cargar() {
        Path archivoAcudieron = resolverRutaAcudieron();

        if (!Files.exists(archivoAcudieron)) {
            vista.setGananciasTotales(0.0);
            vista.mostrarMensajeVacio("No hay registros en acudieron.txt para mostrar.");
            return;
        }

        try {
            List<String> lineas = Files.readAllLines(archivoAcudieron, StandardCharsets.UTF_8);
            double gananciasTotales = 0.0;
            List<String[]> registros = new ArrayList<>();

            for (String linea : lineas) {
                String registro = linea == null ? "" : linea.trim();
                if (registro.isBlank()) {
                    continue;
                }

                String[] partes = registro.split(":", 6);
                if (partes.length < 6) {
                    continue;
                }

                String fecha = partes[0].trim();
                String ci = partes[1].trim();
                String nombre = partes[2].trim();
                String tipoUsuario = partes[3].trim();
                String tipoServicio = partes[4].trim();
                double cobro = parseMonto(partes[5]);

                registros.add(new String[] { fecha, tipoServicio, ci, nombre, tipoUsuario, String.valueOf(cobro) });
                gananciasTotales += cobro;
            }

            if (registros.isEmpty()) {
                vista.setGananciasTotales(0.0);
                vista.mostrarMensajeVacio("No hay registros validos en acudieron.txt.");
                return;
            }

            for (String[] reg : registros) {
                double cobro = parseMonto(reg[5]);

                vista.agregarRegistroALista(
                    reg[0],
                    reg[1],
                    reg[2],
                    reg[3] + " (" + reg[4] + ")",
                    cobro
                );
            }

            vista.setGananciasTotales(gananciasTotales);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                vista,
                "No se pudo leer el archivo acudieron.txt.",
                "Error de lectura",
                JOptionPane.ERROR_MESSAGE
            );
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
        } catch (URISyntaxException e) { }
        return Paths.get(System.getProperty("user.dir"));
    }
}