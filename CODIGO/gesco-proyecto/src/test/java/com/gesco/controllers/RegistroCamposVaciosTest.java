package com.gesco.controllers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RegistroCamposVaciosTest {
    
    @Test
    void testRegistrarUsuarioCamposVacios() {
        boolean resultado = DataBase.registrarUsuario("", "", "", "");
        
        assertFalse(resultado, "El registro con campos vacios debe fallar");
    }
}
