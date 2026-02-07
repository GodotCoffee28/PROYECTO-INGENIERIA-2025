package com.gesco.controllers;

import java.util.function.Consumer;

public class InicioSesionRedireccionador {

    private final Consumer<String> onAdmin;
    private final Consumer<String> onComensal;

    public InicioSesionRedireccionador(Consumer<String> onAdmin, Consumer<String> onComensal) {
        this.onAdmin = onAdmin;
        this.onComensal = onComensal;
    }

    public void redirigir(boolean esAdmin, String nombre) {
        if (esAdmin) {
            onAdmin.accept(nombre);
            return;
        }

        onComensal.accept(nombre);
    }
}
