package com.gesco.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gesco.controllers.gestion_principal.DataBase;
import com.gesco.models.usuarios.Usuario.TipoUsuario;

public class MonederoSaldoTest {

    private Path usuariosPath;
    private byte[] usuariosBackup;

    @Before
    public void setUp() throws IOException {
        usuariosPath = Paths.get(System.getProperty("user.dir"))
            .resolve("src/main/java/com/gesco/models/data/usuarios.txt");

        Files.createDirectories(usuariosPath.getParent());
        if (Files.exists(usuariosPath)) {
            usuariosBackup = Files.readAllBytes(usuariosPath);
        } else {
            usuariosBackup = new byte[0];
            Files.createFile(usuariosPath);
        }

        Files.write(usuariosPath, new byte[0]);
    }

    @After
    public void tearDown() throws IOException {
        if (usuariosPath != null && usuariosBackup != null) {
            Files.write(usuariosPath, usuariosBackup);
        }
    }

    @Test
    public void recargarSaldo_actualizaSaldoCorrectamente_cajaNegra() {
        String cedula = "33333333";

        assertTrue(DataBase.registrarUsuario(cedula, "clave123", "Usuario Test", "test@email.com", TipoUsuario.ESTUDIANTE));
        assertTrue(DataBase.actualizarSaldo(cedula, 100.00));

        double montoRecarga = 40.50;
        double nuevoSaldoEsperado = 140.50;

        assertTrue(DataBase.actualizarSaldo(cedula, 100.00 + montoRecarga));
        assertEquals(nuevoSaldoEsperado, DataBase.obtenerSaldo(cedula), 0.0001);
    }

    @Test
    public void cobroFila_aplicaFormulaMenuMasCcbPorTipo_cajaNegra() {
        String cedula = "33444444";

        assertTrue(DataBase.registrarUsuario(cedula, "clave123", "Usuario Fila", "fila@email.com", TipoUsuario.ESTUDIANTE));
        assertTrue(DataBase.actualizarSaldo(cedula, 200.00));

        double costoMenu = 50.00;
        double ccbBase = 10.00;
        double porcentajeEstudiante = 0.20;
        double componenteCcbEsperado = 2.00;
        double costoFinalEsperado = 52.00;
        double saldoFinalEsperado = 148.00;

        double componenteCcb = DataBase.calcularMontoCcbPorTipo(ccbBase, TipoUsuario.ESTUDIANTE, porcentajeEstudiante);
        assertEquals(componenteCcbEsperado, componenteCcb, 0.0001);
        double costoFinal = costoMenu + componenteCcb;
        assertEquals(costoFinalEsperado, costoFinal, 0.0001);

        double saldoAntes = DataBase.obtenerSaldo(cedula);
        assertTrue(DataBase.actualizarSaldo(cedula, saldoAntes - costoFinal));

        assertEquals(saldoFinalEsperado, DataBase.obtenerSaldo(cedula), 0.0001);
    }
}
