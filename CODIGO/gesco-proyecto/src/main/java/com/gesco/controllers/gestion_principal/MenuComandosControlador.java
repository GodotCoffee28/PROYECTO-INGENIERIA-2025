package com.gesco.controllers.gestion_principal;

public class MenuComandosControlador {

    private final MenuAcciones acciones;

    public MenuComandosControlador(MenuAcciones acciones) {
        this.acciones = acciones;
    }

    public void procesar(String comando, boolean esAdmin) {
        if (comando == null || comando.isBlank()) {
            return;
        }

        switch (comando) {
            case "CMD_FILA"       -> acciones.irFila();
            case "CMD_MENUSEMANA" -> acciones.irMenuSemana();
            case "CMD_TURNOS"     -> acciones.irTurnos();
            case "CMD_HISTORIAL_SALDO" -> acciones.irHistorialSaldo();
            case "CMD_RECARGAR_SALDO"  -> acciones.irRecargarSaldo();
            case "CMD_SALIR"      -> acciones.salir();
            default -> {
            }
        }

        if (!esAdmin) {
            return;
        }

        switch (comando) {
            case "CMD_CONTROL"     -> acciones.irPanelControl();
            case "CMD_CCB"         -> acciones.irCargaCCB();
            case "CMD_CREARMENU"   -> acciones.irCrearMenu();
            case "CMD_EDITARMENU"  -> acciones.irEditarMenu();
            case "CMD_GESTIONMENU" -> acciones.irGestionMenu();
            case "CMD_AGREGAR_INSUMO" -> acciones.irAgregarInsumo();
            case "CMD_HISTORIAL_MENU" -> acciones.irHistorialMenu();
            case "CMD_HISTORIAL_CCB"  -> acciones.irHistorialCcb();
            case "CMD_SWAP"        -> acciones.swapInteraccion();
            default -> {
            }
        }
    }
}

