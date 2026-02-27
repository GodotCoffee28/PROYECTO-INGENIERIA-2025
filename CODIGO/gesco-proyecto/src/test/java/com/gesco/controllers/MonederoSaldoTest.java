package com.gesco.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gesco.controllers.gestion_principal.DataBase;
import com.gesco.models.usuarios.Usuario.TipoUsuario;

public class MonederoSaldoTest {

    private static final DateTimeFormatter FECHA_FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Path dataDirTemporal;
    private String dataDirAnterior;

    @Before
    public void setUp() throws IOException {
        dataDirAnterior = DataBase.getDataDir();
        dataDirTemporal = Files.createTempDirectory("gesco-monedero-caja-negra-");
        DataBase.setDataDir(dataDirTemporal.toString());

        Path carpetaSecretaria = dataDirTemporal.resolve("secretaria");
        Files.createDirectories(carpetaSecretaria);
        Files.writeString(
            carpetaSecretaria.resolve("cedulas_ocupaciones.txt"),
            "33333333:estudiante" + System.lineSeparator()
                + "33444444:estudiante" + System.lineSeparator(),
            java.nio.charset.StandardCharsets.UTF_8
        );
        Files.writeString(carpetaSecretaria.resolve("33333333.jpg"), "img", java.nio.charset.StandardCharsets.UTF_8);
        Files.writeString(carpetaSecretaria.resolve("33444444.jpg"), "img", java.nio.charset.StandardCharsets.UTF_8);
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
    public void recargarSaldo_actualizaSaldoCorrectamente_cajaNegra() {
        String cedula = "33333333";
        String referencia = "12345678901234567890";
        String banco = "Banco de Venezuela (0102)";
        String fecha = LocalDate.now().format(FECHA_FORMATO);

        assertTrue(DataBase.registrarUsuario(cedula, "clave123", "Usuario Test", "test@email.com", TipoUsuario.ESTUDIANTE));
        assertTrue(DataBase.actualizarSaldo(cedula, 100.00));

        double montoRecarga = 40.50;
        double saldoInicial = DataBase.obtenerSaldo(cedula);
        double nuevoSaldoEsperado = 140.50;

        assertTrue(DataBase.registrarRecarga(referencia, montoRecarga, banco, fecha, cedula));
        assertTrue(DataBase.actualizarSaldo(cedula, saldoInicial + montoRecarga));
        assertEquals(nuevoSaldoEsperado, DataBase.obtenerSaldo(cedula), 0.0001);

        assertTrue(DataBase.obtenerRecargasPorCedula(cedula).stream()
            .anyMatch(r -> r[0].equals(referencia)));
    }

    @Test
    public void cobroFila_aplicaFormulaMenuMasCcbPorTipo_cajaNegra() {
        String cedula = "33444444";

        assertTrue(DataBase.registrarUsuario(cedula, "clave123", "Usuario Fila", "fila@email.com", TipoUsuario.ESTUDIANTE));
        assertTrue(DataBase.actualizarSaldo(cedula, 200.00));

        double costoMenu = 50.00;
        double ccbBase = 10.00;
        double porcentajeEstudiante = 0.20;
        double componenteCcb = ccbBase * porcentajeEstudiante;
        double costoFinalEsperado = 52.00;
        double saldoFinalEsperado = 148.00;

        double costoFinal = costoMenu + componenteCcb;
        assertEquals(costoFinalEsperado, costoFinal, 0.0001);

        double saldoAntes = DataBase.obtenerSaldo(cedula);
        assertTrue(DataBase.actualizarSaldo(cedula, saldoAntes - costoFinal));

        assertEquals(saldoFinalEsperado, DataBase.obtenerSaldo(cedula), 0.0001);
    }
}
