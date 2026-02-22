package com.gesco.controllers;

import javax.swing.SwingUtilities;

import com.gesco.views.VistaCargaCCB;
import com.gesco.views.VistaCrearMenu;
import com.gesco.views.VistaEditarMenu;
import com.gesco.views.VistaEspera;
import com.gesco.views.VistaGestionMenu;
import com.gesco.views.VistaInicio;
import com.gesco.views.VistaInicioAdmin;
import com.gesco.views.VistaInicioComensal;  
import com.gesco.views.VistaInicioSesion;
import com.gesco.views.VistaMenuSemana;
import com.gesco.views.VistaRegistro;
import com.gesco.views.VistaTurnos;
import com.gesco.views.VistaVerCFCV;

public class LogicaInterfaz {

    private VistaInicio vistaInicio;
    private VistaInicioSesion vistaInicioSesion;
    private VistaRegistro vistaRegistro;
    private VistaEspera vistaEspera;
    private VistaInicioAdmin vistaInicioAdmin;
    private VistaInicioComensal vistaInicioComensal;  
    private VistaMenuSemana vistaMenuSemana;
    private VistaTurnos vistaTurnos;
    private VistaCargaCCB vistaCargaCCB;
    private VistaVerCFCV vistaVerCfcv;
    private VistaCrearMenu vistaCrearMenu;
    private VistaEditarMenu vistaEditarMenu;
    private VistaGestionMenu vistaGestionMenu;
    private boolean usuarioAdmin;
    private boolean sesionAdmin;
    private String nombreUsuario;
    private final InicioSesionRedireccionador inicioSesionRedireccionador =
        new InicioSesionRedireccionador(this::mostrarPantallaAdmin, nombre -> mostrarPantallaPrincipal(false, nombre));
    private final MenuGescoControlador menuGescoController = new MenuGescoControlador(
        new MenuAccionesControlador(
            this::iniciar,
            this::mostrarInicioSesion,
            this::mostrarRegistro,
            this::mostrarFila,
            this::mostrarMenuSemana,
            this::mostrarTurnos,
            this::mostrarPanelControl,
            this::mostrarCargaCCB,
            this::mostrarCrearMenu,
            this::mostrarEditarMenu,
            this::mostrarGestionMenu,
            this::swapInteraccion,
            () -> System.exit(0)
        )
    );

    public void iniciar() {
        SwingUtilities.invokeLater(() -> {
            cerrarVistas();
            vistaInicio = new VistaInicio();
            new VistaInicioControlador(
                vistaInicio,
                this::mostrarInicioSesion,
                this::mostrarRegistro
            ).conectar();
        });
    }

    private void mostrarInicioSesion() {
        cerrarVistas();
        vistaInicioSesion = new VistaInicioSesion();
        new VistaInicioSesionControlador(
            vistaInicioSesion,
            this::iniciar,  
            this::mostrarRegistro,
            (tipoUsuario, nombre) -> inicioSesionRedireccionador.redirigir(tipoUsuario, nombre)
        ).conectar();
    }

    private void mostrarRegistro() {
        cerrarVistas();
        vistaRegistro = new VistaRegistro();
        new VistaRegistroControlador(
            vistaRegistro,
            this::iniciar,  
            this::mostrarInicioSesion,
            this::mostrarEsperaRegistro  
        ).conectar();
    }

    private void mostrarEsperaRegistro() {
        cerrarVistas();
        vistaEspera = new VistaEspera();
        new VistaEsperaControlador(
            vistaEspera,
            this::iniciar
        ).conectar();
    }

    private void mostrarPantallaPrincipal(boolean esSesionAdmin, String nombre) {
        cerrarVistas();
        usuarioAdmin = false;
        sesionAdmin = esSesionAdmin;
        nombreUsuario = nombre;

        String nombreMostrar = (nombre == null || nombre.isBlank()) ? "Usuario" : nombre;
        if (sesionAdmin) {
            vistaInicioComensal = new VistaInicioComensal(nombreMostrar, 999999);
        } else {
            vistaInicioComensal = new VistaInicioComensal(nombreMostrar, 50);
        }

        menuGescoController.conectar(vistaInicioComensal, sesionAdmin);
        new VistaInicioComensalControlador(
            vistaInicioComensal,
            this::iniciar,
            this::mostrarMenuSemana,
            this::mostrarTurnos
        ).conectar();
    }

    private void mostrarPantallaAdmin(String nombre) {
        cerrarVistas();
        usuarioAdmin = true;
        sesionAdmin = true;
        nombreUsuario = nombre;
        vistaInicioAdmin = new VistaInicioAdmin();
        menuGescoController.conectar(vistaInicioAdmin, true);
        new VistaInicioAdminControlador(
            vistaInicioAdmin,
            this::iniciar,
            this::mostrarGestionMenu,
            this::mostrarCargaCCB,
            this::mostrarVerCfcv,
            this::swapInteraccion
        ).conectar();
    }

    private void mostrarPanelControl() {
        String nombreMostrar = (nombreUsuario == null || nombreUsuario.isBlank()) ? "Usuario" : nombreUsuario;
        mostrarPantallaAdmin(nombreMostrar);
    }

    private void mostrarMenuSemana() {
        cerrarVistas();
        vistaMenuSemana = new VistaMenuSemana();
        menuGescoController.conectar(vistaMenuSemana, sesionAdmin);
        new VistaMenuSemanaControlador(
            vistaMenuSemana,
            this::volverAPantallaPrincipal
        ).conectar();
    }

    private void mostrarTurnos() {
        cerrarVistas();
        vistaTurnos = new VistaTurnos();
        menuGescoController.conectar(vistaTurnos, sesionAdmin);
        new VistaTurnosControlador(
            vistaTurnos,
            this::volverAPantallaPrincipal,
            this::mostrarMenuSemana
        ).conectar();
    }

    private void mostrarFila() {
        javax.swing.JOptionPane.showMessageDialog(null,
            "La funcionalidad de fila ha sido deshabilitada.",
            "Funcionalidad deshabilitada",
            javax.swing.JOptionPane.INFORMATION_MESSAGE);
        volverAPantallaPrincipal();
    }

    private void mostrarCargaCCB() {
        cerrarVistas();
        vistaCargaCCB = new VistaCargarCFCV();
        menuGescoController.conectar(vistaCargaCCB, sesionAdmin);
        new VistaCargarCFCVControlador(
        vistaCargaCCB = new VistaCargaCCB();
        menuGescoController.conectar(vistaCargaCCB, usuarioAdmin);
        new VistaCargarCCBControlador(
            vistaCargaCCB,
            this::mostrarPanelControl
        ).conectar();
    }

    private void mostrarVerCfcv() {
        cerrarVistas();
        vistaVerCfcv = new VistaVerCFCV();
        menuGescoController.conectar(vistaVerCfcv, sesionAdmin);
        new VistaVerCFCVControlador(
            vistaVerCfcv,
            this::mostrarPanelControl
        ).conectar();
    }

    private void mostrarCrearMenu() {
        cerrarVistas();
        vistaCrearMenu = new VistaCrearMenu();
        menuGescoController.conectar(vistaCrearMenu, sesionAdmin);
        new VistaCrearMenuControlador(
            vistaCrearMenu,
            this::mostrarGestionMenu
        ).conectar();
    }

    private void mostrarEditarMenu() {
        cerrarVistas();
        vistaEditarMenu = new VistaEditarMenu();
        menuGescoController.conectar(vistaEditarMenu, sesionAdmin);
        new VistaEditarMenuControlador(
            vistaEditarMenu,
            this::mostrarGestionMenu
        ).conectar();
    }

    private void mostrarGestionMenu() {
        cerrarVistas();
        vistaGestionMenu = new VistaGestionMenu();
        menuGescoController.conectar(vistaGestionMenu, sesionAdmin);
        new VistaGestionMenuControlador(
            vistaGestionMenu,
            this::mostrarPanelControl,
            this::mostrarEditarMenu,
            this::mostrarCrearMenu,
            this::reiniciarMenusSemana
        ).conectar();
    }

    private void reiniciarMenusSemana() {
        java.util.List<java.time.LocalDate> fechasNuevas = DataBase.calcularFechasParaReiniciar();

        boolean ok = DataBase.reiniciarMenusSemana();
        if (!ok) {
            javax.swing.JOptionPane.showMessageDialog(null,
                "Error al reiniciar menús.",
                "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            mostrarGestionMenu();
            return;
        }

        if (fechasNuevas.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(null,
                "Todos los días de la siguiente semana hábil ya tienen menú asignado.",
                "Sin cambios",
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
            mostrarGestionMenu();
            return;
        }

        javax.swing.JOptionPane.showMessageDialog(null,
            "Se agregaron " + fechasNuevas.size() + " día(s) nuevos.\nA continuación, configura el menú para cada uno.",
            "Menús reiniciados",
            javax.swing.JOptionPane.INFORMATION_MESSAGE);

        java.util.Queue<java.time.LocalDate> cola = new java.util.LinkedList<>(fechasNuevas);
        mostrarCrearMenuParaFecha(cola);
    }

    private void mostrarCrearMenuParaFecha(java.util.Queue<java.time.LocalDate> cola) {
        if (cola.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(null,
                "¡Todos los menús han sido configurados!",
                "Listo", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            mostrarGestionMenu();
            return;
        }

        java.time.LocalDate fecha = cola.poll();
        int restantes = cola.size();

        cerrarVistas();
        vistaCrearMenu = new VistaCrearMenu();

        String dia  = String.format("%02d", fecha.getDayOfMonth());
        String mes  = String.format("%02d", fecha.getMonthValue());
        String anio = String.valueOf(fecha.getYear());
        vistaCrearMenu.setFecha(dia, mes, anio);

        String nombreDia = switch (fecha.getDayOfWeek()) {
            case MONDAY    -> "Lunes";
            case TUESDAY   -> "Martes";
            case WEDNESDAY -> "Miércoles";
            case THURSDAY  -> "Jueves";
            case FRIDAY    -> "Viernes";
            default        -> fecha.getDayOfWeek().toString();
        };
        vistaCrearMenu.setTitle("Crear menú — " + nombreDia + " (" + (5 - restantes) + "/5)");

        menuGescoController.conectar(vistaCrearMenu, usuarioAdmin);

        new VistaCrearMenuControlador(
            vistaCrearMenu,
            () -> mostrarCrearMenuParaFecha(cola)
        ).conectar();
    }

    private void volverAPantallaPrincipal() {
        if (usuarioAdmin && sesionAdmin) {
            mostrarPantallaAdmin(nombreUsuario);
            return;
        }
        mostrarPantallaPrincipal(sesionAdmin, nombreUsuario);
    }

    private void swapInteraccion() {
        if (!sesionAdmin) {
            javax.swing.JOptionPane.showMessageDialog(null,
                "El cambio de interacción (Swap) solo está disponible para sesión de administrador.",
                "Swap no disponible",
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (usuarioAdmin) {
            mostrarPantallaPrincipal(true, nombreUsuario);
        } else {
            mostrarPantallaAdmin(nombreUsuario);
        }
    }

    private void cerrarVistaInicio() {
        if (vistaInicio != null) {
            vistaInicio.dispose();
            vistaInicio = null;
        }
    }

    private void cerrarVistaInicioSesion() {
        if (vistaInicioSesion != null) {
            vistaInicioSesion.dispose();
            vistaInicioSesion = null;
        }
    }

    private void cerrarVistaRegistro() {
        if (vistaRegistro != null) {
            vistaRegistro.dispose();
            vistaRegistro = null;
        }
    }

    private void cerrarVistaEspera() {
        if (vistaEspera != null) {
            vistaEspera.dispose();
            vistaEspera = null;
        }
    }

    private void cerrarVistaInicioAdmin() {
        if (vistaInicioAdmin != null) {
            vistaInicioAdmin.dispose();
            vistaInicioAdmin = null;
        }
    }

    private void cerrarVistaInicioComensal() {
        if (vistaInicioComensal != null) {
            vistaInicioComensal.dispose();
            vistaInicioComensal = null;
        }
    }

    private void cerrarVistaMenuSemana() {
        if (vistaMenuSemana != null) {
            vistaMenuSemana.dispose();
            vistaMenuSemana = null;
        }
    }

    private void cerrarVistaTurnos() {
        if (vistaTurnos != null) {
            vistaTurnos.dispose();
            vistaTurnos = null;
        }
    }

    private void cerrarVistaCargaCCB() {
        if (vistaCargaCCB != null) {
            vistaCargaCCB.dispose();
            vistaCargaCCB = null;
        }
    }

    private void cerrarVistaVerCfcv() {
        if (vistaVerCfcv != null) {
            vistaVerCfcv.dispose();
            vistaVerCfcv = null;
        }
    }

    private void cerrarVistaCrearMenu() {
        if (vistaCrearMenu != null) {
            vistaCrearMenu.dispose();
            vistaCrearMenu = null;
        }
    }

    private void cerrarVistaEditarMenu() {
        if (vistaEditarMenu != null) {
            vistaEditarMenu.dispose();
            vistaEditarMenu = null;
        }
    }

    private void cerrarVistaGestionMenu() {
        if (vistaGestionMenu != null) {
            vistaGestionMenu.dispose();
            vistaGestionMenu = null;
        }
    }

    private void cerrarVistas() {
        cerrarVistaInicio();
        cerrarVistaInicioSesion();
        cerrarVistaRegistro();
        cerrarVistaEspera();
        cerrarVistaInicioAdmin();
        cerrarVistaInicioComensal();  
        cerrarVistaMenuSemana();
        cerrarVistaTurnos();
        cerrarVistaCargaCCB();
        cerrarVistaVerCfcv();
        cerrarVistaCrearMenu();
        cerrarVistaEditarMenu();
        cerrarVistaGestionMenu();
    }

}