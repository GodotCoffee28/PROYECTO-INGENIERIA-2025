package com.gesco.controllers;


import com.gesco.controllers.gestion_principal.DataBase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gesco.models.costos.CCB;
import com.gesco.models.usuarios.Usuario.TipoUsuario;

public class DataBaseCcbDescuentoTest {

    private Path dataDirTemporal;
    private String dataDirAnterior;

    @Before
    public void setUp() throws IOException {
        dataDirAnterior = DataBase.getDataDir();
        dataDirTemporal = Files.createTempDirectory("gesco-ccb-descuento-");
        DataBase.setDataDir(dataDirTemporal.toString());

        Path carpetaSecretaria = dataDirTemporal.resolve("secretaria");
        Files.createDirectories(carpetaSecretaria);
        Files.writeString(
            carpetaSecretaria.resolve("cedulas_ocupaciones.txt"),
            "12345678:estudiante" + System.lineSeparator(),
            java.nio.charset.StandardCharsets.UTF_8
        );
        Files.writeString(carpetaSecretaria.resolve("12345678.jpg"), "img", java.nio.charset.StandardCharsets.UTF_8);
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
    public void calcularMontoCcbPorTipo_estudiante_porcentajeValido() {
        double monto = DataBase.calcularMontoCcbPorTipo(100.0, TipoUsuario.ESTUDIANTE, 0.25);
        assertEquals(25.0, monto, 0.0001);
    }

    @Test
    public void calcularMontoCcbPorTipo_profesor_porcentajeValido() {
        double monto = DataBase.calcularMontoCcbPorTipo(100.0, TipoUsuario.PROFESOR, 0.80);
        assertEquals(80.0, monto, 0.0001);
    }

    @Test
    public void calcularMontoCcbPorTipo_empleado_porcentajeValido() {
        double monto = DataBase.calcularMontoCcbPorTipo(100.0, TipoUsuario.EMPLEADO, 1.00);
        assertEquals(100.0, monto, 0.0001);
    }

    @Test
    public void calcularMontoCcbPorTipo_becario_aplicaDescuentoSobreCcbBase() {
        double monto = DataBase.calcularMontoCcbPorTipo(100.0, TipoUsuario.BECARIO, 0.05);
        assertEquals(5.0, monto, 0.0001);
    }

    @Test
    public void calcularMontoCcbPorTipo_exonerado_sinCobro() {
        double monto = DataBase.calcularMontoCcbPorTipo(100.0, TipoUsuario.EXONERADO);
        assertEquals(0.0, monto, 0.0001);
    }

    @Test
    public void calcularMontoCcbPorTipo_profesor_porcentajeFueraDeRango_lanzaExcepcion() {
        try {
            DataBase.calcularMontoCcbPorTipo(100.0, TipoUsuario.PROFESOR, 0.60);
            fail("Se esperaba IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("rango"));
        }
    }

    @Test
    public void calcularMontoCcbParaCedula_usandoTipoGuardado() {
        DataBase.registrarUsuario("12345678", "clave123", "Juan Perez", "juan@email.com", TipoUsuario.ESTUDIANTE);
        DataBase.guardarCcb(new CCB(LocalDate.now(), "Estudiante", 1000.0, 500.0, 100.0, 0.0, 0.20));

        double monto = DataBase.calcularMontoCcbParaCedula("12345678", 0.20);
        assertEquals(3.0, monto, 0.0001);
    }

    @Test
    public void calcularMontoCcbPorTipo_noEsAleatorio_yAdminCuentaComoEmpleado() {
        double estudiante1 = DataBase.calcularMontoCcbPorTipo(100.0, TipoUsuario.ESTUDIANTE);
        double estudiante2 = DataBase.calcularMontoCcbPorTipo(100.0, TipoUsuario.ESTUDIANTE);
        assertEquals(25.0, estudiante1, 0.0001);
        assertEquals(estudiante1, estudiante2, 0.0001);

        double admin = DataBase.calcularMontoCcbPorTipo(100.0, TipoUsuario.ADMIN);
        double empleado = DataBase.calcularMontoCcbPorTipo(100.0, TipoUsuario.EMPLEADO);
        assertEquals(100.0, admin, 0.0001);
        assertEquals(empleado, admin, 0.0001);
    }

    @Test
    public void guardarCcb_fechaFutura_permiteGuardar() {
        CCB ccbFuturo = new CCB(LocalDate.now().plusDays(1), "Estudiante", 1000.0, 500.0, 100.0, 0.0);
        assertTrue(DataBase.guardarCcb(ccbFuturo));
    }

    @Test
    public void guardarCcb_fechaAnterior_rechazaGuardado() {
        CCB ccbPasado = new CCB(LocalDate.now().minusDays(1), "Estudiante", 1000.0, 500.0, 100.0, 0.0);
        assertEquals(false, DataBase.guardarCcb(ccbPasado));
    }

    @Test
    public void guardarCcb_mismaFechaYMismoTipo_reemplazaRegistroExistente() {
        LocalDate fecha = LocalDate.now().plusDays(1);

        CCB primero = new CCB(fecha, "Estudiante", 1000.0, 500.0, 100.0, 0.0);
        CCB segundo = new CCB(fecha, "Estudiante", 2000.0, 500.0, 100.0, 0.0);

        assertTrue(DataBase.guardarCcb(primero));
        assertTrue(DataBase.guardarCcb(segundo));

        long cantidadMismoTipoYFecha = DataBase.obtenerHistorialCcb().stream()
            .filter(c -> fecha.equals(c.getFecha()))
            .filter(c -> "ESTUDIANTE".equalsIgnoreCase(c.getTipoUsuario()))
            .count();

        assertEquals(1L, cantidadMismoTipoYFecha);
        CCB ultimoEstudiante = DataBase.obtenerUltimoCcbPorTipo(TipoUsuario.ESTUDIANTE);
        assertEquals(segundo.getCcb(), ultimoEstudiante.getCcb(), 0.0001);
    }

    @Test
    public void calcularMontoCcbParaCedula_usaCcbDeHoy_yNoElDeFechaFutura() {
        DataBase.registrarUsuario("12345678", "clave123", "Juan Perez", "juan@email.com", TipoUsuario.ESTUDIANTE);

        CCB ccbHoy = new CCB(LocalDate.now(), "Estudiante", 1000.0, 500.0, 100.0, 0.0, 0.20); // ccb = 15
        CCB ccbFuturo = new CCB(LocalDate.now().plusDays(1), "Estudiante", 2000.0, 500.0, 100.0, 0.0, 0.25); // ccb = 25

        assertTrue(DataBase.guardarCcb(ccbHoy));
        assertTrue(DataBase.guardarCcb(ccbFuturo));

        double monto = DataBase.calcularMontoCcbParaCedula("12345678", 0.20);
        assertEquals(3.0, monto, 0.0001);
    }

    @Test
    public void guardarPorcentajeBecario_menorAlPorcentajeEstudiantilHoy_sePersiste() {
        assertTrue(DataBase.guardarCcb(new CCB(LocalDate.now(), "Estudiante", 1000.0, 500.0, 100.0, 0.0, 0.25)));
        assertTrue(DataBase.guardarPorcentajeBecario("12345678", 0.05));

        assertEquals(0.05, DataBase.obtenerPorcentajeBecario("12345678"), 0.0001);
    }

    @Test
    public void guardarPorcentajeBecario_mayorOIgualAlPorcentajeEstudiantilHoy_rechazaGuardado() {
        assertTrue(DataBase.guardarCcb(new CCB(LocalDate.now(), "Estudiante", 1000.0, 500.0, 100.0, 0.0, 0.25)));

        assertEquals(false, DataBase.guardarPorcentajeBecario("12345678", 0.25));
    }

    @Test
    public void calcularMontoCcbParaCedula_becario_usaPorcentajeIndividual() {
        assertTrue(DataBase.registrarUsuario("12345678", "clave123", "Juan Perez", "juan@email.com", TipoUsuario.ESTUDIANTE));
        assertTrue(DataBase.guardarCcb(new CCB(LocalDate.now(), "Estudiante", 1000.0, 500.0, 100.0, 0.0, 0.25)));
        assertTrue(DataBase.cambiarTipoUsuarioPorCedula("12345678", TipoUsuario.BECARIO, 0.05));

        double monto = DataBase.calcularMontoCcbParaCedula("12345678");
        assertEquals(0.75, monto, 0.0001);
    }
}


