package com.gesco.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gesco.controllers.gestion_principal.DataBase;
import com.gesco.models.costos.CCB;
import com.gesco.models.usuarios.Usuario.TipoUsuario;

public class CcbCajaNegraTest {

    private Path dataDirTemporal;
    private String dataDirAnterior;

    @Before
    public void setUp() throws IOException {
        dataDirAnterior = DataBase.getDataDir();
        dataDirTemporal = Files.createTempDirectory("gesco-ccb-caja-negra-");
        DataBase.setDataDir(dataDirTemporal.toString());

        Path carpetaSecretaria = dataDirTemporal.resolve("secretaria");
        Files.createDirectories(carpetaSecretaria);
        Files.writeString(
            carpetaSecretaria.resolve("cedulas_ocupaciones.txt"),
            "33445566:profesor" + System.lineSeparator(),
            java.nio.charset.StandardCharsets.UTF_8
        );
        Files.writeString(carpetaSecretaria.resolve("33445566.jpg"), "img", java.nio.charset.StandardCharsets.UTF_8);
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
    public void calcularMontoCcbParaCedula_usandoTipoGuardado() {
        String cedula = "33445566";

        assertTrue(DataBase.registrarUsuario(cedula, "clave123", "Profesor User", "prof@email.com", TipoUsuario.PROFESOR));
        assertTrue(DataBase.guardarCcb(new CCB(LocalDate.now(), "Profesor", 1000.0, 500.0, 100.0, 0.0)));

        double monto = DataBase.calcularMontoCcbParaCedula(cedula, 0.80);
        assertEquals(12.0, monto, 0.0001);
    }
}
