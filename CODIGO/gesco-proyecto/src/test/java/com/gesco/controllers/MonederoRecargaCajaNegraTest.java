package com.gesco.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.Test;

import com.gesco.controllers.gestion_principal.DataBase;
import com.gesco.models.usuarios.Usuario.TipoUsuario;

public class MonederoRecargaCajaNegraTest extends MonederoBaseCajaNegraTest {

    private static final DateTimeFormatter FECHA_FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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
}
