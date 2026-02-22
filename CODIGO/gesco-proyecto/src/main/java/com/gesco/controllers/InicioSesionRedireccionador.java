package com.gesco.controllers;

import java.util.function.Consumer;

import com.gesco.models.TipoUsuario;

public class InicioSesionRedireccionador {

    private final Consumer<String> onAdmin;
    private final Consumer<String> onComensal;

    public InicioSesionRedireccionador(Consumer<String> onAdmin, Consumer<String> onComensal) {
        this.onAdmin = onAdmin;
        this.onComensal = onComensal;
    }

    public void redirigir(TipoUsuario tipoUsuario, String nombre) {
        if (tipoUsuario == TipoUsuario.ADMIN || tipoUsuario == TipoUsuario.SUPER_ADMIN) {
            onAdmin.accept(nombre);
            return;
        }

        onComensal.accept(nombre);
    }
}
