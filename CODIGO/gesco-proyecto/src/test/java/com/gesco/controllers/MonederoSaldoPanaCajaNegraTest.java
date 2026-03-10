package com.gesco.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.Test;

import com.gesco.controllers.gestion_principal.DataBase;
import com.gesco.models.usuarios.Usuario.TipoUsuario;

public class MonederoSaldoPanaCajaNegraTest extends MonederoBaseCajaNegraTest {

    private static final DateTimeFormatter FECHA_FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Test
    public void saldoPana_recargaAOtroUsuario_actualizaDestino_yConservaOrigen_cajaNegra() {
        String cedulaOrigen = "33333333";
        String cedulaDestino = "33444444";
        String referencia = "98765432109876543210";
        String banco = "Banco de Venezuela (0102)";
        String fecha = LocalDate.now().format(FECHA_FORMATO);

        assertTrue(DataBase.registrarUsuario(cedulaOrigen, "clave123", "Origen", "origen@email.com", TipoUsuario.ESTUDIANTE));
        assertTrue(DataBase.registrarUsuario(cedulaDestino, "clave123", "Destino", "destino@email.com", TipoUsuario.ESTUDIANTE));

        assertTrue(DataBase.actualizarSaldo(cedulaOrigen, 80.00));
        assertTrue(DataBase.actualizarSaldo(cedulaDestino, 20.00));

        double montoRecarga = 35.50;
        double saldoOrigenAntes = DataBase.obtenerSaldo(cedulaOrigen);
        double saldoDestinoAntes = DataBase.obtenerSaldo(cedulaDestino);

        assertTrue(DataBase.registrarRecarga(referencia, montoRecarga, banco, fecha, cedulaOrigen));
        assertTrue(DataBase.actualizarSaldo(cedulaDestino, saldoDestinoAntes + montoRecarga));

        assertEquals(80.00, DataBase.obtenerSaldo(cedulaOrigen), 0.0001);
        assertEquals(55.50, DataBase.obtenerSaldo(cedulaDestino), 0.0001);
        assertEquals(saldoOrigenAntes, DataBase.obtenerSaldo(cedulaOrigen), 0.0001);

        assertTrue(DataBase.obtenerRecargasPorCedula(cedulaOrigen).stream()
            .anyMatch(r -> r[0].equals(referencia) && r[1].equals("35.50")));
    }
}
