package com.gesco.controllers.gestion_principal;

import javax.swing.SwingUtilities;

import com.gesco.controllers.autentificacion.ControladorInicioSesion;
import com.gesco.controllers.autentificacion.ControladorRegistro;
import com.gesco.controllers.costos.ControladorCargarCCB;
import com.gesco.controllers.costos.ControladorHistorialCCB;
import com.gesco.controllers.costos.ControladorRecargarSaldo;
import com.gesco.controllers.inicio.ControladorInicio;
import com.gesco.controllers.inicio.ControladorInicioAdmin;
import com.gesco.controllers.inicio.ControladorInicioComensal;
import com.gesco.controllers.menu.ControladorCrearMenu;
import com.gesco.controllers.menu.ControladorEditarMenu;
import com.gesco.controllers.menu.ControladorGestionMenu;
import com.gesco.controllers.menu.ControladorMenuSemana;
import com.gesco.controllers.otros.ControladorAutorizarAdmin;
import com.gesco.controllers.otros.ControladorEspera;
import com.gesco.controllers.otros.ControladorFila;
import com.gesco.controllers.otros.ControladorTurnos;
import com.gesco.views.Registros.VistaHistorialCCB;
import com.gesco.views.Registros.VistaHistorialMenu;
import com.gesco.views.Registros.VistaHistorialSaldo;
import com.gesco.views.auntentificacion.VistaInicioSesion;
import com.gesco.views.auntentificacion.VistaRegistro;
import com.gesco.views.costos.VistaCargaCCB;
import com.gesco.views.costos.VistaRecargarSaldo;
import com.gesco.views.costos.VistaVerCFCV;
import com.gesco.views.inicio.VistaInicio;
import com.gesco.views.inicio.VistaInicioAdmin;
import com.gesco.views.inicio.VistaInicioComensal;  
import com.gesco.views.menu.VistaAgregarInsumo;
import com.gesco.views.menu.VistaCrearMenu;
import com.gesco.views.menu.VistaEditarMenu;
import com.gesco.views.menu.VistaGestionMenu;
import com.gesco.views.menu.VistaMenuSemana;
import com.gesco.views.otros.VistaEspera;
import com.gesco.views.otros.VistaFila;
import com.gesco.views.otros.VistaTurnos;
import com.gesco.views.otros.VistaAutorizarAdmin;
import com.gesco.views.Registros.VistaHistorialMenu;
import com.gesco.views.Registros.VistaHistorialSaldo;

public class LogicaInterfaz {

    private VistaInicio vistaInicio;
    private VistaInicioSesion vistaInicioSesion;
    private VistaRegistro vistaRegistro;
    private VistaEspera vistaEspera;
    private VistaInicioAdmin vistaInicioAdmin;
    private VistaInicioComensal vistaInicioComensal;  
    private VistaRecargarSaldo vistaRecargarSaldo;
    private VistaFila vistaFila;
    private VistaMenuSemana vistaMenuSemana;
    private VistaTurnos vistaTurnos;
    private VistaCargaCCB vistaCargaCCB;
    private VistaHistorialCCB vistaHistorialCcb;
    private VistaVerCFCV vistaVerCfcv;
    private VistaCrearMenu vistaCrearMenu;
    private VistaEditarMenu vistaEditarMenu;
    private VistaAgregarInsumo vistaAgregarInsumo;
    private VistaGestionMenu vistaGestionMenu;
    private VistaHistorialMenu vistaHistorialMenu;
    private VistaHistorialSaldo vistaHistorialSaldo;
    private VistaAutorizarAdmin vistaAutorizarAdmin;
    private boolean usuarioAdmin;
    private boolean sesionAdmin;
    private String nombreUsuario;
    private String cedulaSesionActual;
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
        cedulaSesionActual = null;
        SwingUtilities.invokeLater(() -> {
            cerrarVistas();
            vistaInicio = new VistaInicio();
            new ControladorInicio(
                vistaInicio,
                this::mostrarInicioSesion,
                this::mostrarRegistro
            ).conectar();
        });
    }

    private void mostrarInicioSesion() {
        cerrarVistas();
        vistaInicioSesion = new VistaInicioSesion();
        new ControladorInicioSesion(
            vistaInicioSesion,
            this::iniciar,  
            this::mostrarRegistro,
            cedula -> this.cedulaSesionActual = cedula,
            (tipoUsuario, nombre) -> inicioSesionRedireccionador.redirigir(tipoUsuario, nombre)
        ).conectar();
    }

    private void mostrarRegistro() {
        cerrarVistas();
        vistaRegistro = new VistaRegistro();
        new ControladorRegistro(
            vistaRegistro,
            this::iniciar,  
            this::mostrarInicioSesion,
            this::mostrarEsperaRegistro  
        ).conectar();
    }

    private void mostrarEsperaRegistro() {
        cerrarVistas();
        vistaEspera = new VistaEspera();
        new ControladorEspera(
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
        double saldo = 0.0;
        if (cedulaSesionActual != null && !cedulaSesionActual.isBlank()) {
            saldo = DataBase.obtenerSaldo(cedulaSesionActual);
        } else if (sesionAdmin) {
            saldo = 999999;
        } else {
            saldo = 50;
        }

        vistaInicioComensal = new VistaInicioComensal(nombreMostrar, saldo);

        menuGescoController.conectar(vistaInicioComensal, sesionAdmin, cedulaSesionActual);
        new ControladorInicioComensal(
            vistaInicioComensal,
            this::iniciar,
            this::mostrarMenuSemana,
            this::mostrarTurnos,
            this::mostrarRecargarSaldo,
            this::mostrarFila,
            this::mostrarHistorialSaldo
        ).conectar();
    }

    private void mostrarPantallaAdmin(String nombre) {
        cerrarVistas();
        usuarioAdmin = true;
        sesionAdmin = true;
        nombreUsuario = nombre;
        vistaInicioAdmin = new VistaInicioAdmin();
        boolean esSuperAdmin = DataBase.esSuperAdmin(cedulaSesionActual);
        vistaInicioAdmin.setEsSuperAdmin(esSuperAdmin);
        if (esSuperAdmin) {
            vistaInicioAdmin.getIlblSprAdmin().addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    mostrarAutorizarAdmin();
                }
            });
        }
        menuGescoController.conectar(vistaInicioAdmin, true, cedulaSesionActual);
        new ControladorInicioAdmin(
            vistaInicioAdmin,
            this::iniciar,
            this::mostrarGestionMenu,
            this::mostrarCargaCCB,
            this::mostrarVerCcb,
            this::swapInteraccion,
            this::mostrarHistorialMenu
        ).conectar();
    }

    private void mostrarAutorizarAdmin() {
        if (!DataBase.esSuperAdmin(cedulaSesionActual)) {
            javax.swing.JOptionPane.showMessageDialog(null,
                "Solo un super admin puede autorizar administradores.",
                "Acceso denegado",
                javax.swing.JOptionPane.WARNING_MESSAGE);
            mostrarPanelControl();
            return;
        }

        cerrarVistas();
        vistaAutorizarAdmin = new VistaAutorizarAdmin();
        menuGescoController.conectar(vistaAutorizarAdmin, true, cedulaSesionActual);
        new ControladorAutorizarAdmin(
            vistaAutorizarAdmin,
            this::mostrarPanelControl
        ).conectar();
    }

    private void mostrarPanelControl() {
        String nombreMostrar = (nombreUsuario == null || nombreUsuario.isBlank()) ? "Usuario" : nombreUsuario;
        mostrarPantallaAdmin(nombreMostrar);
    }

    private void mostrarMenuSemana() {
        cerrarVistas();
        vistaMenuSemana = new VistaMenuSemana();
        menuGescoController.conectar(vistaMenuSemana, sesionAdmin, cedulaSesionActual);
        new ControladorMenuSemana(
            vistaMenuSemana,
            this::volverAPantallaPrincipal
        ).conectar();
    }

    private void mostrarTurnos() {
        cerrarVistas();
        vistaTurnos = new VistaTurnos();
        menuGescoController.conectar(vistaTurnos, sesionAdmin, cedulaSesionActual);
        new ControladorTurnos(
            vistaTurnos,
            this::volverAPantallaPrincipal,
            this::mostrarMenuSemana
        ).conectar();
    }

    private void mostrarFila() {
        cerrarVistas();
        vistaFila = new VistaFila();
        menuGescoController.conectar(vistaFila, sesionAdmin, cedulaSesionActual);
        new ControladorFila(
            vistaFila,
            cedulaSesionActual,
            this::volverAPantallaPrincipal,
            this::mostrarMenuSemana
        ).conectar();
    }

    private void mostrarCargaCCB() {
        cerrarVistas();
        vistaCargaCCB = new VistaCargaCCB();
        menuGescoController.conectar(vistaCargaCCB, usuarioAdmin, cedulaSesionActual);
        ControladorCargarCCB controlador = new ControladorCargarCCB(
            vistaCargaCCB,
            this::mostrarPanelControl
        );
        controlador.conectar();
    }

    private void mostrarVerCcb() {
        cerrarVistas();
        vistaHistorialCcb = new VistaHistorialCCB();
        
        menuGescoController.conectar(vistaHistorialCcb, sesionAdmin, cedulaSesionActual);
        ControladorHistorialCCB controlador = new ControladorHistorialCCB(
            vistaHistorialCcb,
            this::mostrarPanelControl
        );
        controlador.conectar();
    }

    private void mostrarCrearMenu() {
        cerrarVistas();
        vistaCrearMenu = new VistaCrearMenu();
        menuGescoController.conectar(vistaCrearMenu, sesionAdmin, cedulaSesionActual);
        new ControladorCrearMenu(
            vistaCrearMenu,
            this::mostrarGestionMenu
        ).conectar();
    }

    private void mostrarEditarMenu() {
        cerrarVistas();
        vistaEditarMenu = new VistaEditarMenu();
        menuGescoController.conectar(vistaEditarMenu, sesionAdmin, cedulaSesionActual);
        new ControladorEditarMenu(
            vistaEditarMenu,
            this::mostrarGestionMenu
        ).conectar();
    }

    private void mostrarGestionMenu() {
        cerrarVistas();
        vistaGestionMenu = new VistaGestionMenu();
        menuGescoController.conectar(vistaGestionMenu, sesionAdmin, cedulaSesionActual);
        new ControladorGestionMenu(
            vistaGestionMenu,
            this::mostrarPanelControl,
            this::mostrarEditarMenu,
            this::mostrarCrearMenu,
            this::mostrarAgregarInsumo,
            this::reiniciarMenusSemana
        ).conectar();
    }

    private void mostrarAgregarInsumo() {
        cerrarVistas();
        vistaAgregarInsumo = new VistaAgregarInsumo();
        menuGescoController.conectar(vistaAgregarInsumo, sesionAdmin, cedulaSesionActual);
        vistaAgregarInsumo.getBackIcon().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                mostrarGestionMenu();
            }
        });
    }

    private void mostrarHistorialMenu() {
        cerrarVistas();
        vistaHistorialMenu = new VistaHistorialMenu();
        menuGescoController.conectar(vistaHistorialMenu, sesionAdmin, cedulaSesionActual);
        vistaHistorialMenu.getBackIcon().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                volverAPantallaPrincipal();
            }
        });
    }

    private void mostrarHistorialSaldo() {
        if (cedulaSesionActual == null || cedulaSesionActual.isBlank()) {
            javax.swing.JOptionPane.showMessageDialog(null,
                "No hay una cédula de sesión activa para ver movimientos.",
                "Sesión requerida",
                javax.swing.JOptionPane.WARNING_MESSAGE);
            volverAPantallaPrincipal();
            return;
        }

        cerrarVistas();
        vistaHistorialSaldo = new VistaHistorialSaldo();
        menuGescoController.conectar(vistaHistorialSaldo, sesionAdmin, cedulaSesionActual);

        java.util.List<String[]> recargas = DataBase.obtenerRecargasPorCedula(cedulaSesionActual);
        for (int i = recargas.size() - 1; i >= 0; i--) {
            String[] recarga = recargas.get(i);
            String referencia = recarga[0];
            String monto = recarga[1];
            String banco = recarga[2];
            String fecha = recarga[3];
            String cedula = recarga[4];
            vistaHistorialSaldo.agregarTransaccionALista(fecha, referencia, monto, banco, cedula);
        }
    }

    private void cerrarVistaHistorialMenu() {
        if (vistaHistorialMenu != null) {
            vistaHistorialMenu.dispose();
            vistaHistorialMenu = null;
        }
    }

    private void cerrarVistaHistorialSaldo() {
        if (vistaHistorialSaldo != null) {
            vistaHistorialSaldo.dispose();
            vistaHistorialSaldo = null;
        }
    }

    private void cerrarVistaAutorizarAdmin() {
        if (vistaAutorizarAdmin != null) {
            vistaAutorizarAdmin.dispose();
            vistaAutorizarAdmin = null;
        }
    }

    private void reiniciarMenusSemana() {
        java.util.List<java.time.LocalDate> fechasSemana = DataBase.calcularFechasParaReiniciar();

        boolean ok = DataBase.reiniciarMenusSemana();
        if (!ok) {
            javax.swing.JOptionPane.showMessageDialog(null,
                "Error al reiniciar menús.",
                "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            mostrarGestionMenu();
            return;
        }

        if (fechasSemana.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(null,
                "No se pudieron calcular los días de la semana actual.",
                "Sin cambios",
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
            mostrarGestionMenu();
            return;
        }

        javax.swing.JOptionPane.showMessageDialog(null,
            "Se reinició el menú de la semana actual.\nA continuación, configura el menú para cada uno.",
            "Menú reiniciado",
            javax.swing.JOptionPane.INFORMATION_MESSAGE);

        java.util.Queue<java.time.LocalDate> cola = new java.util.LinkedList<>(fechasSemana);
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

        menuGescoController.conectar(vistaCrearMenu, usuarioAdmin, cedulaSesionActual);

        new ControladorCrearMenu(
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

    private void cerrarVistaFila() {
        if (vistaFila != null) {
            vistaFila.dispose();
            vistaFila = null;
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

    private void cerrarVistaVerCcb() {
        if (vistaHistorialCcb != null) {
            vistaHistorialCcb.dispose();
            vistaHistorialCcb = null;
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

    private void cerrarVistaAgregarInsumo() {
        if (vistaAgregarInsumo != null) {
            vistaAgregarInsumo.dispose();
            vistaAgregarInsumo = null;
        }
    }

    private void cerrarVistaGestionMenu() {
        if (vistaGestionMenu != null) {
            vistaGestionMenu.dispose();
            vistaGestionMenu = null;
        }
    }

    private void cerrarVistaRecargarSaldo() {
        if (vistaRecargarSaldo != null) {
            vistaRecargarSaldo.dispose();
            vistaRecargarSaldo = null;
        }
    }

    private void cerrarVistas() {
        cerrarVistaInicio();
        cerrarVistaInicioSesion();
        cerrarVistaRegistro();
        cerrarVistaEspera();
        cerrarVistaInicioAdmin();
        cerrarVistaInicioComensal();  
        cerrarVistaFila();
        cerrarVistaMenuSemana();
        cerrarVistaTurnos();
        cerrarVistaCargaCCB();
        cerrarVistaVerCcb();
        cerrarVistaVerCfcv();
        cerrarVistaCrearMenu();
        cerrarVistaEditarMenu();
        cerrarVistaAgregarInsumo();
        cerrarVistaGestionMenu();
        cerrarVistaRecargarSaldo();
        cerrarVistaHistorialMenu();
        cerrarVistaHistorialSaldo();
        cerrarVistaAutorizarAdmin();
    }

    private void mostrarRecargarSaldo() {
        if (cedulaSesionActual == null || cedulaSesionActual.isBlank()) {
            javax.swing.JOptionPane.showMessageDialog(null,
                "No hay una cédula de sesión activa para recargar saldo.",
                "Sesión requerida",
                javax.swing.JOptionPane.WARNING_MESSAGE);
            volverAPantallaPrincipal();
            return;
        }

        cerrarVistas();
        vistaRecargarSaldo = new VistaRecargarSaldo();
        menuGescoController.conectar(vistaRecargarSaldo, sesionAdmin, cedulaSesionActual);
        new ControladorRecargarSaldo(
            vistaRecargarSaldo,
            cedulaSesionActual,
            this::volverAPantallaPrincipal,
            this::volverAPantallaPrincipal
        ).conectar();
    }

}


