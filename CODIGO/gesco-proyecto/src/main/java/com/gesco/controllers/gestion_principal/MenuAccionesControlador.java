package com.gesco.controllers.gestion_principal;

public class MenuAccionesControlador implements MenuAcciones {

    private final Runnable onInicio;
    private final Runnable onSesion;
    private final Runnable onRegistro;
    private final Runnable onFila;
    private final Runnable onMenuSemana;
    private final Runnable onTurnos;
    private final Runnable onPanelControl;
    private final Runnable onCargaCCB;
    private final Runnable onCrearMenu;
    private final Runnable onEditarMenu;
    private final Runnable onGestionMenu;
    private final Runnable onSwapInteraccion;
    private final Runnable onSalir;

    public MenuAccionesControlador(
        Runnable onInicio,
        Runnable onSesion,
        Runnable onRegistro,
        Runnable onFila,
        Runnable onMenuSemana,
        Runnable onTurnos,
        Runnable onPanelControl,
        Runnable onCargaCCB,
        Runnable onCrearMenu,
        Runnable onEditarMenu,
        Runnable onGestionMenu,
        Runnable onSwapInteraccion,
        Runnable onSalir
    ) {
        this.onInicio = onInicio;
        this.onSesion = onSesion;
        this.onRegistro = onRegistro;
        this.onFila = onFila;
        this.onMenuSemana = onMenuSemana;
        this.onTurnos = onTurnos;
        this.onPanelControl = onPanelControl;
        this.onCargaCCB = onCargaCCB;
        this.onCrearMenu = onCrearMenu;
        this.onEditarMenu = onEditarMenu;
        this.onGestionMenu = onGestionMenu;
        this.onSwapInteraccion = onSwapInteraccion;
        this.onSalir = onSalir;
    }

    @Override
    public void irInicio() {
        onInicio.run();
    }

    @Override
    public void irSesion() {
        onSesion.run();
    }

    @Override
    public void irRegistro() {
        onRegistro.run();
    }

    @Override
    public void irFila() {
        onFila.run();
    }

    @Override
    public void irMenuSemana() {
        onMenuSemana.run();
    }

    @Override
    public void irTurnos() {
        onTurnos.run();
    }

    @Override
    public void irPanelControl() {
        onPanelControl.run();
    }

    @Override
    public void irCargaCCB() {
        onCargaCCB.run();
    }

    @Override
    public void irCrearMenu() {
        onCrearMenu.run();
    }

    @Override
    public void irEditarMenu() {
        onEditarMenu.run();
    }

    @Override
    public void irGestionMenu() {
        onGestionMenu.run();
    }

    @Override
    public void swapInteraccion() {
        onSwapInteraccion.run();
    }

    @Override
    public void salir() {
        onSalir.run();
    }
}

