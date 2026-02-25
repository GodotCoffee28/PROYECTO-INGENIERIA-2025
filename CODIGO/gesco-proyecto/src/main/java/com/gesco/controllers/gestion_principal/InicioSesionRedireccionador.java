package com.gesco.controllers.gestion_principal;

import java.util.function.Consumer;

import com.gesco.models.usuarios.Usuario.TipoUsuario;

public class InicioSesionRedireccionador {

    private final Consumer<String> onAdmin;
    private final Consumer<String> onComensal;

    public InicioSesionRedireccionador(Consumer<String> onAdmin, Consumer<String> onComensal) {
        this.onAdmin = onAdmin;
        this.onComensal = onComensal;
    }

    public void redirigir(TipoUsuario tipoUsuario, String nombre) {
        if (tipoUsuario != null && tipoUsuario.esAdmin()) {
            onAdmin.accept(nombre);
            return;
        }

        onComensal.accept(nombre);
    }
}


