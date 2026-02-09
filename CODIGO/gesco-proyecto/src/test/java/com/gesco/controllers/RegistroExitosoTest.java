package com.gesco.controllers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RegistroExitosoTest {
    
    @Test
    void testRegistrarUsuarioExitoso() {
        boolean resultado = DataBase.registrarUsuario("87654321", "clave456", "Ana Garcia", "ana@email.com");
        
        assertTrue(resultado, "El registro con datos validos debe ser exitoso");
    }
}
