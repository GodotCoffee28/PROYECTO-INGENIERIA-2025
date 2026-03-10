package com.gesco.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Test;

import com.gesco.controllers.gestion_principal.DataBase;
import com.gesco.models.costos.CCB;
import com.gesco.models.usuarios.Usuario.TipoUsuario;

public class MonederoCobroTiposCajaNegraTest extends MonederoBaseCajaNegraTest {

    @Test
    public void cobroFila_aplicaFormulaMenuMasCcbPorTipoEstudiante_cajaNegra() {
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

    @Test
    public void cobroPedidoMenu_becario_aplicaCincoPorcientoCcb_yActualizaSaldo_cajaNegra() {
        String cedula = "33555555";

        assertTrue(DataBase.registrarUsuario(cedula, "clave123", "Becario Test", "becario@email.com", TipoUsuario.BECARIO));
        assertTrue(DataBase.actualizarSaldo(cedula, 200.00));

        assertTrue(DataBase.guardarCcb(new CCB(LocalDate.now(), "Estudiante", 1000.0, 500.0, 100.0, 0.0)));

        double costoMenu = 50.00;
        double montoCcb = DataBase.calcularMontoCcbParaCedula(cedula);
        double costoFinal = costoMenu + montoCcb;

        assertEquals(0.75, montoCcb, 0.0001);
        assertEquals(50.75, costoFinal, 0.0001);

        double saldoAntes = DataBase.obtenerSaldo(cedula);
        assertTrue(DataBase.actualizarSaldo(cedula, saldoAntes - costoFinal));
        assertEquals(149.25, DataBase.obtenerSaldo(cedula), 0.0001);
    }

    @Test
    public void cobroPedidoMenu_exonerado_noCobraCcb_yActualizaSaldoSoloPorMenu_cajaNegra() {
        String cedula = "33666666";

        assertTrue(DataBase.registrarUsuario(cedula, "clave123", "Exonerado Test", "exonerado@email.com", TipoUsuario.EXONERADO));
        assertTrue(DataBase.actualizarSaldo(cedula, 200.00));

        assertTrue(DataBase.guardarCcb(new CCB(LocalDate.now(), "Estudiante", 1000.0, 500.0, 100.0, 0.0)));

        double costoMenu = 50.00;
        double montoCcb = DataBase.calcularMontoCcbParaCedula(cedula);
        double costoFinal = costoMenu + montoCcb;

        assertEquals(0.0, montoCcb, 0.0001);
        assertEquals(50.0, costoFinal, 0.0001);

        double saldoAntes = DataBase.obtenerSaldo(cedula);
        assertTrue(DataBase.actualizarSaldo(cedula, saldoAntes - costoFinal));
        assertEquals(150.00, DataBase.obtenerSaldo(cedula), 0.0001);
    }
}
