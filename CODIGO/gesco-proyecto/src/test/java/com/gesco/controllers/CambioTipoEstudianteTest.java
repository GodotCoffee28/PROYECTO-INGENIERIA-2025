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
import com.gesco.models.costos.CCB;
import com.gesco.models.usuarios.Usuario.TipoUsuario;

public class CambioTipoEstudianteTest {

    private Path dataDirTemporal;
    private String dataDirAnterior;

    @Before
    public void setUp() throws IOException {
        dataDirAnterior = DataBase.getDataDir();
        dataDirTemporal = Files.createTempDirectory("gesco-cambio-tipo-");
        DataBase.setDataDir(dataDirTemporal.toString());

        Path carpetaSecretaria = dataDirTemporal.resolve("secretaria");
        Files.createDirectories(carpetaSecretaria);
        Files.writeString(
            carpetaSecretaria.resolve("cedulas_ocupaciones.txt"),
            String.join(
                System.lineSeparator(),
                "32650001:estudiante:regular:Ana Regular",
                "32650002:estudiante:becario:Pedro Becario",
                "32650003:estudiante:exonerado:Luisa Exonerada",
                "32650004:profesor:Carlos Docente",
                "32650005:estudiante:becario:Maria Apoyo",
                "32650006:estudiante:regular:Jose Pendiente"
            ) + System.lineSeparator(),
            StandardCharsets.UTF_8
        );

        crearImagenSecretaria("32650001");
        crearImagenSecretaria("32650002");
        crearImagenSecretaria("32650003");
        crearImagenSecretaria("32650004");
        crearImagenSecretaria("32650005");
        crearImagenSecretaria("32650006");

        assertTrue(DataBase.guardarCcb(new CCB(LocalDate.now(), "Estudiante", 1000.0, 500.0, 100.0, 0.0, 0.25)));
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
    public void cambiarTipoUsuarioPorCedula_regularABecario_actualizaPadronYUsuarioRegistrado() {
        String cedula = "32650001";

        assertTrue(DataBase.registrarUsuario(cedula, "clave123", "Ana Regular", "ana@email.com", TipoUsuario.ESTUDIANTE));
        assertTrue(DataBase.cambiarTipoUsuarioPorCedula(cedula, TipoUsuario.BECARIO, 0.05));

        assertEquals(TipoUsuario.BECARIO, DataBase.obtenerTipoUsuarioSecretaria(cedula));
        assertEquals(TipoUsuario.BECARIO, DataBase.obtenerTipoUsuario(cedula));
        assertEquals(0.05, DataBase.obtenerPorcentajeBecario(cedula), 0.0001);
        assertTrue(padronContiene("32650001:estudiante:becario"));
    }

    @Test
    public void cambiarTipoUsuarioPorCedula_becarioAExonerado_actualizaPadronYUsuarioRegistrado() {
        String cedula = "32650002";

        assertTrue(DataBase.registrarUsuario(cedula, "clave123", "Pedro Becario", "pedro@email.com", TipoUsuario.BECARIO));
        assertTrue(DataBase.guardarPorcentajeBecario(cedula, 0.05));
        assertTrue(DataBase.cambiarTipoUsuarioPorCedula(cedula, TipoUsuario.EXONERADO));

        assertEquals(TipoUsuario.EXONERADO, DataBase.obtenerTipoUsuarioSecretaria(cedula));
        assertEquals(TipoUsuario.EXONERADO, DataBase.obtenerTipoUsuario(cedula));
        assertEquals(null, DataBase.obtenerPorcentajeBecario(cedula));
        assertTrue(padronContiene("32650002:estudiante:exonerado"));
    }

    @Test
    public void cambiarTipoUsuarioPorCedula_exoneradoARegular_actualizaPadronYUsuarioRegistrado() {
        String cedula = "32650003";

        assertTrue(DataBase.registrarUsuario(cedula, "clave123", "Luisa Exonerada", "luisa@email.com", TipoUsuario.EXONERADO));
        assertTrue(DataBase.cambiarTipoUsuarioPorCedula(cedula, TipoUsuario.ESTUDIANTE));

        assertEquals(TipoUsuario.ESTUDIANTE, DataBase.obtenerTipoUsuarioSecretaria(cedula));
        assertEquals(TipoUsuario.ESTUDIANTE, DataBase.obtenerTipoUsuario(cedula));
        assertTrue(padronContiene("32650003:estudiante:regular"));
    }

    @Test
    public void cambiarTipoUsuarioPorCedula_usuarioSoloEnPadron_actualizaPadronSinExigirRegistroPrevio() {
        String cedula = "32650006";

        assertEquals(TipoUsuario.ESTUDIANTE, DataBase.obtenerTipoUsuarioSecretaria(cedula));
        assertEquals(TipoUsuario.COMENSAL, DataBase.obtenerTipoUsuario(cedula));

        assertTrue(DataBase.cambiarTipoUsuarioPorCedula(cedula, TipoUsuario.BECARIO, 0.05));

        assertEquals(TipoUsuario.BECARIO, DataBase.obtenerTipoUsuarioSecretaria(cedula));
        assertEquals(TipoUsuario.COMENSAL, DataBase.obtenerTipoUsuario(cedula));
        assertEquals(0.05, DataBase.obtenerPorcentajeBecario(cedula), 0.0001);
        assertTrue(padronContiene("32650006:estudiante:becario"));
    }

    @Test
    public void cambiarTipoUsuarioPorCedula_becarioConPorcentajeMayorAlEstudianteHoy_rechazaCambio() {
        String cedula = "32650001";

        assertTrue(DataBase.registrarUsuario(cedula, "clave123", "Ana Regular", "ana@email.com", TipoUsuario.ESTUDIANTE));

        assertFalse(DataBase.cambiarTipoUsuarioPorCedula(cedula, TipoUsuario.BECARIO, 0.25));

        assertEquals(TipoUsuario.ESTUDIANTE, DataBase.obtenerTipoUsuarioSecretaria(cedula));
        assertEquals(TipoUsuario.ESTUDIANTE, DataBase.obtenerTipoUsuario(cedula));
    }

    @Test
    public void cambiarTipoUsuarioPorCedula_nuevoTipoNoEstudiantil_rechazaCambioYConservaDatos() {
        String cedula = "32650001";

        assertTrue(DataBase.registrarUsuario(cedula, "clave123", "Ana Regular", "ana@email.com", TipoUsuario.ESTUDIANTE));

        assertFalse(DataBase.cambiarTipoUsuarioPorCedula(cedula, TipoUsuario.PROFESOR));

        assertEquals(TipoUsuario.ESTUDIANTE, DataBase.obtenerTipoUsuarioSecretaria(cedula));
        assertEquals(TipoUsuario.ESTUDIANTE, DataBase.obtenerTipoUsuario(cedula));
        assertTrue(padronContiene("32650001:estudiante:regular"));
    }

    @Test
    public void cambiarTipoUsuarioPorCedula_cedulaNoEstudiantil_rechazaCambio() {
        String cedula = "32650004";

        assertFalse(DataBase.cambiarTipoUsuarioPorCedula(cedula, TipoUsuario.BECARIO));

        assertEquals(TipoUsuario.PROFESOR, DataBase.obtenerTipoUsuarioSecretaria(cedula));
        assertTrue(padronContiene("32650004:profesor:Carlos Docente"));
    }

    @Test
    public void registrarUsuario_tipoNoCoincideConPadron_rechazaRegistro() {
        assertFalse(DataBase.registrarUsuario(
            "32650005",
            "clave123",
            "Maria Apoyo",
            "maria@email.com",
            TipoUsuario.PROFESOR
        ));
    }

    @Test
    public void registrarUsuario_tipoEstudianteGenerico_conPadronBecario_estaPermitido() {
        String cedula = "32650005";

        assertTrue(DataBase.registrarUsuario(cedula, "clave123", "Maria Apoyo", "maria@email.com", TipoUsuario.ESTUDIANTE));
        assertEquals(TipoUsuario.ESTUDIANTE, DataBase.obtenerTipoUsuario(cedula));
        assertEquals(TipoUsuario.BECARIO, DataBase.obtenerTipoUsuarioSecretaria(cedula));
    }

    private void crearImagenSecretaria(String cedula) throws IOException {
        Files.writeString(
            dataDirTemporal.resolve("secretaria").resolve(cedula + ".jpg"),
            "img",
            StandardCharsets.UTF_8
        );
    }

    private boolean padronContiene(String lineaEsperada) {
        return leerPadron().stream().anyMatch(linea -> linea.startsWith(lineaEsperada));
    }

    private List<String> leerPadron() {
        try {
            return Files.readAllLines(
                dataDirTemporal.resolve("secretaria").resolve("cedulas_ocupaciones.txt"),
                StandardCharsets.UTF_8
            );
        } catch (IOException ex) {
            throw new AssertionError("No se pudo leer el padrón temporal", ex);
        }
    }
}