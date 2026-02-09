package com.gesco.controllers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoginCedulaNegativaTest {
    
    @Test
    void testValidarInicioSesionCedulaNegativa() {
        boolean resultado = DataBase.validarInicioSesion("-12345678", "password123");
        
        assertFalse(resultado, "El inicio de sesion con cedula negativa debe fallar");
    }
}
