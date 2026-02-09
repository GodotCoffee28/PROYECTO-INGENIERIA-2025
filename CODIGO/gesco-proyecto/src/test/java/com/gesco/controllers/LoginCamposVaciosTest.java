package com.gesco.controllers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoginCamposVaciosTest {
    
    @Test
    void testValidarInicioSesionCamposVacios() {
        boolean resultado = DataBase.validarInicioSesion("", "");
        
        assertFalse(resultado, "El inicio de sesion con campos vacios debe fallar");
    }
}
