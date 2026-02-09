package com.gesco.controllers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RegistroCedulaNegativaTest {
    
    @Test
    void testRegistrarUsuarioCedulaNegativa() {
        boolean resultado = DataBase.registrarUsuario("-12345678", "password123", "Maria Lopez", "maria@email.com");
        
        assertFalse(resultado, "El registro con cedula negativa debe fallar");
    }
}
