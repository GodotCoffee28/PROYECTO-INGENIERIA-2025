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
    public void recargarSaldo_actualizaSaldoCorrectamente() {
        String cedula = "33333333";

        assertTrue(DataBase.registrarUsuario(cedula, "clave123", "Usuario Test", "test@email.com", TipoUsuario.ESTUDIANTE));
        assertTrue(DataBase.actualizarSaldo(cedula, 100.00));

        double saldoAntes = DataBase.obtenerSaldo(cedula);
        double montoRecarga = 40.50;
        double nuevoSaldo = saldoAntes + montoRecarga;

        assertTrue(DataBase.actualizarSaldo(cedula, nuevoSaldo));
        assertEquals(140.50, DataBase.obtenerSaldo(cedula), 0.0001);
    }

    @Test
    public void entrarFila_cobraSegunMenuMasCcbPorDescuentoTipo() {
        String cedula = "33444444";

        assertTrue(DataBase.registrarUsuario(cedula, "clave123", "Usuario Fila", "fila@email.com", TipoUsuario.ESTUDIANTE));
        assertTrue(DataBase.actualizarSaldo(cedula, 200.00));

        double costoMenu = 50.00;
        double ccbBase = 10.00;
        double porcentajeEstudiante = 0.20;

        double componenteCcb = DataBase.calcularMontoCcbPorTipo(ccbBase, TipoUsuario.ESTUDIANTE, porcentajeEstudiante);
        double costoFinal = costoMenu + componenteCcb;

        double saldoAntes = DataBase.obtenerSaldo(cedula);
        assertTrue(DataBase.actualizarSaldo(cedula, saldoAntes - costoFinal));

        assertEquals(148.00, DataBase.obtenerSaldo(cedula), 0.0001);
    }
}
