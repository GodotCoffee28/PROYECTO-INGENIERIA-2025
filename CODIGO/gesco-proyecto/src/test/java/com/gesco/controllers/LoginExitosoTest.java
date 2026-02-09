package com.gesco.controllers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoginExitosoTest {
    
    @Test
    void testValidarInicioSesionExitoso() {
        DataBase.registrarUsuario("12345678", "password123", "Juan Perez", "juan@email.com");
        
        boolean resultado = DataBase.validarInicioSesion("12345678", "password123");
        
        assertTrue(resultado, "El inicio de sesion con credenciales validas debe ser exitoso");
    }
}
