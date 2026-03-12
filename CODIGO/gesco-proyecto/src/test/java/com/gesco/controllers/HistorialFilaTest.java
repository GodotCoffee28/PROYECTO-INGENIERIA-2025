package com.gesco.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gesco.controllers.gestion_principal.DataBase;
import com.gesco.models.menu.Menu;
import com.gesco.models.usuarios.Usuario.TipoUsuario;

public class HistorialFilaTest {

    private Path dataDirTemporal;
    private String dataDirAnterior;

    @Before
    public void setUp() throws IOException {
        dataDirAnterior = DataBase.getDataDir();
        dataDirTemporal = Files.createTempDirectory("gesco-historial-fila-");
        DataBase.setDataDir(dataDirTemporal.toString());

        Path carpetaSecretaria = dataDirTemporal.resolve("secretaria");
        Files.createDirectories(carpetaSecretaria);
        Files.writeString(
            carpetaSecretaria.resolve("cedulas_ocupaciones.txt"),
            String.join(
                System.lineSeparator(),
                "32651001:estudiante:regular:Ana Fila",
                "32651002:estudiante:becario:Luis Fila"
            ) + System.lineSeparator(),
            StandardCharsets.UTF_8
        );

        crearImagenSecretaria("32651001");
        crearImagenSecretaria("32651002");
    }

    @After
    public void tearDown() throws IOException {
        if (dataDirAnterior != null && !dataDirAnterior.isBlank()) {
            DataBase.setDataDir(dataDirAnterior);
        }

        if (dataDirTemporal != null && Files.exists(dataDirTemporal)) {
            Files.walk(dataDirTemporal)
                .sorted((a, b) -> b.compareTo(a))
                .forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException ignored) {
                    }
                });
        }
    }

    @Test
    public void registrarAcudieron_dosUsuarios_mantieneHistorialYConteoDelDia() {
        String cedulaRegular = "32651001";
        String cedulaBecario = "32651002";
        LocalDate hoy = LocalDate.now();

        assertTrue(DataBase.registrarUsuario(cedulaRegular, "clave123", "Ana Fila", "ana@email.com", TipoUsuario.ESTUDIANTE));
        assertTrue(DataBase.registrarUsuario(cedulaBecario, "clave123", "Luis Fila", "luis@email.com", TipoUsuario.BECARIO));

        assertTrue(DataBase.registrarAcudieron(cedulaRegular, TipoUsuario.ESTUDIANTE, Menu.TipoMenu.DESAYUNO, 52.25));
        assertTrue(DataBase.registrarAcudieron(cedulaBecario, TipoUsuario.BECARIO, Menu.TipoMenu.ALMUERZO, 50.75));

        assertEquals(2, DataBase.contarAcudieronPorFecha(hoy));
        assertTrue(DataBase.existeRegistroAcudieronPorCedulaYFecha(cedulaRegular, hoy));
        assertTrue(DataBase.existeRegistroAcudieronPorCedulaYFecha(cedulaBecario, hoy));

        List<String> registros = leerHistorialFila();
        assertEquals(2, registros.size());
        assertTrue(registros.stream().anyMatch(linea -> linea.contains(cedulaRegular)
            && linea.contains("Ana Fila")
            && linea.contains("ESTUDIANTE_REGULAR")
            && linea.endsWith("52.25")));
        assertTrue(registros.stream().anyMatch(linea -> linea.contains(cedulaBecario)
            && linea.contains("Luis Fila")
            && linea.contains("ESTUDIANTE_BECARIO")
            && linea.endsWith("50.75")));
    }

    @Test
    public void eliminarRegistroAcudieron_usuarioRegistrado_reduceConteoYEliminaExistencia() {
        String cedula = "32651001";
        LocalDate hoy = LocalDate.now();

        assertTrue(DataBase.registrarUsuario(cedula, "clave123", "Ana Fila", "ana@email.com", TipoUsuario.ESTUDIANTE));
        assertTrue(DataBase.registrarAcudieron(cedula, TipoUsuario.ESTUDIANTE, Menu.TipoMenu.DESAYUNO, 52.25));
        assertTrue(DataBase.existeRegistroAcudieronPorCedulaYFecha(cedula, hoy));

        assertTrue(DataBase.eliminarRegistroAcudieron(cedula));

        assertFalse(DataBase.existeRegistroAcudieronPorCedulaYFecha(cedula, hoy));
        assertEquals(0, DataBase.contarAcudieronPorFecha(hoy));
        assertTrue(leerHistorialFila().isEmpty());
    }

    private void crearImagenSecretaria(String cedula) throws IOException {
        Files.writeString(
            dataDirTemporal.resolve("secretaria").resolve(cedula + ".jpg"),
            "img",
            StandardCharsets.UTF_8
        );
    }

    private List<String> leerHistorialFila() {
        try {
            Path archivo = dataDirTemporal.resolve("acudieron.txt");
            if (!Files.exists(archivo)) {
                return List.of();
            }
            return Files.readAllLines(archivo, StandardCharsets.UTF_8).stream()
                .map(String::trim)
                .filter(linea -> !linea.isEmpty())
                .toList();
        } catch (IOException ex) {
            throw new AssertionError("No se pudo leer el historial de fila temporal", ex);
        }
    }
}