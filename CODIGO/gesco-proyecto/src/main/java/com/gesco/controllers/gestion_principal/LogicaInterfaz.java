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
import com.gesco.controllers.menu.ControladorAgregarInsumo;
import com.gesco.controllers.menu.ControladorCrearMenu;
import com.gesco.controllers.menu.ControladorEditarMenu;
import com.gesco.controllers.menu.ControladorGestionMenu;
import com.gesco.controllers.menu.ControladorMenuSemana;
import com.gesco.controllers.otros.ControladorCambiarTipoEstudiante;
import com.gesco.controllers.otros.ControladorEspera;
import com.gesco.controllers.otros.ControladorFila;
import com.gesco.controllers.otros.ControladorTurnos;
import com.gesco.controllers.registros.ControladorHistorialFila;
import com.gesco.controllers.registros.ControladorHistorialMenu;
import com.gesco.controllers.registros.ControladorHistorialSaldo;
import com.gesco.models.menu.Menu;
import com.gesco.views.Registros.VistaHistorialCCB;
import com.gesco.views.Registros.VistaHistorialFila;
import com.gesco.views.Registros.VistaHistorialMenu;
import com.gesco.views.Registros.VistaHistorialSaldo;
import com.gesco.views.auntentificacion.VistaInicioSesion;
import com.gesco.views.auntentificacion.VistaRegistro;
import com.gesco.views.costos.VistaCargaCCB;
import com.gesco.views.costos.VistaRecargarSaldo;
import com.gesco.views.inicio.VistaInicio;
import com.gesco.views.inicio.VistaInicioAdmin;
import com.gesco.views.inicio.VistaInicioComensal;  
import com.gesco.views.menu.VistaAgregarInsumo;
import com.gesco.views.menu.VistaCrearMenu;
import com.gesco.views.menu.VistaEditarMenu;
import com.gesco.views.menu.VistaGestionMenu;
import com.gesco.views.menu.VistaMenuSemana;
import com.gesco.views.otros.VistaCambiarTipoEstudiante;
import com.gesco.views.otros.VistaEspera;
import com.gesco.views.otros.VistaFila;
import com.gesco.views.otros.VistaTurnos;

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
    private VistaCrearMenu vistaCrearMenu;
    private VistaEditarMenu vistaEditarMenu;
    private VistaAgregarInsumo vistaAgregarInsumo;
    private VistaGestionMenu vistaGestionMenu;
    private VistaHistorialFila vistaHistorialFila;
    private VistaHistorialMenu vistaHistorialMenu;
    private VistaHistorialSaldo vistaHistorialSaldo;
    private VistaCambiarTipoEstudiante vistaCambiarTipoEstudiante;
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
            this::mostrarHistorialSaldo,
            this::mostrarRecargarSaldo,
            this::mostrarPanelControl,
            this::mostrarCargaCCB,
            this::mostrarCrearMenu,
            this::mostrarEditarMenu,
            this::mostrarGestionMenu,
            this::mostrarAgregarInsumo,
            this::mostrarHistorialMenu,
            this::mostrarVerCcb,
            this::mostrarHistorialFila,
            this::mostrarCambiarTipoEstudiante,
            this::swapInteraccion,
            this::manejarSolicitudSalida
        )
    );

    public void iniciar() {
        cedulaSesionActual = null;
        sesionAdmin = false;
        usuarioAdmin = false;
        nombreUsuario = null;
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
        double saldo;
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
            this::intentarCerrarSesionConConfirmacion,
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
        menuGescoController.conectar(vistaInicioAdmin, true, cedulaSesionActual);
        new ControladorInicioAdmin(
            vistaInicioAdmin,
            this::intentarCerrarSesionConConfirmacion,
            this::mostrarGestionMenu,
            this::mostrarCargaCCB,
            this::mostrarVerCcb,
            this::swapInteraccion,
            this::mostrarHistorialMenu,
            this::mostrarCambiarTipoEstudiante,
            this::mostrarHistorialFila
        ).conectar();
    }

    private void manejarSolicitudSalida() {
        if (!haySesionActiva()) {
            confirmarYCerrarPrograma();
            return;
        }

        Object[] opciones = { "Cerrar sesión", "Cerrar programa", "Cancelar" };
        int seleccion = javax.swing.JOptionPane.showOptionDialog(
            null,
            "¿Qué deseas hacer?",
            "Salir",
            javax.swing.JOptionPane.DEFAULT_OPTION,
            javax.swing.JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[0]
        );

        if (seleccion == 0) {
            intentarCerrarSesionConConfirmacion();
            return;
        }

        if (seleccion == 1) {
            confirmarYCerrarPrograma();
        }
    }

    private void intentarCerrarSesionConConfirmacion() {
        if (!haySesionActiva()) {
            iniciar();
            return;
        }

        int respuesta = javax.swing.JOptionPane.showConfirmDialog(
            null,
            "¿En verdad desea salir de su sesión?",
            "Confirmar cierre de sesión",
            javax.swing.JOptionPane.YES_NO_OPTION,
            javax.swing.JOptionPane.QUESTION_MESSAGE
        );

        if (respuesta == javax.swing.JOptionPane.YES_OPTION) {
            iniciar();
        }
    }

    private void confirmarYCerrarPrograma() {
        int respuesta = javax.swing.JOptionPane.showConfirmDialog(
            null,
            "¿En verdad desea cerrar el programa?",
            "Confirmar salida",
            javax.swing.JOptionPane.YES_NO_OPTION,
            javax.swing.JOptionPane.QUESTION_MESSAGE
        );

        if (respuesta == javax.swing.JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private boolean haySesionActiva() {
        return sesionAdmin || (cedulaSesionActual != null && !cedulaSesionActual.isBlank());
    }

    private void mostrarCambiarTipoEstudiante() {
        cerrarVistas();
        vistaCambiarTipoEstudiante = new VistaCambiarTipoEstudiante();
        menuGescoController.conectar(vistaCambiarTipoEstudiante, sesionAdmin, cedulaSesionActual);
        new ControladorCambiarTipoEstudiante(
            vistaCambiarTipoEstudiante,
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
            this::mostrarGestionMenu,
            () -> { }
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
            this::reiniciarMenus
        ).conectar();
    }

    private void mostrarAgregarInsumo() {
        cerrarVistas();
        vistaAgregarInsumo = new VistaAgregarInsumo();
        menuGescoController.conectar(vistaAgregarInsumo, sesionAdmin, cedulaSesionActual);
        new ControladorAgregarInsumo(
            vistaAgregarInsumo,
            this::mostrarGestionMenu
        ).conectar();
    }

    private void mostrarHistorialMenu() {
        cerrarVistas();
        vistaHistorialMenu = new VistaHistorialMenu();
        menuGescoController.conectar(vistaHistorialMenu, sesionAdmin, cedulaSesionActual);
        new ControladorHistorialMenu(vistaHistorialMenu, this::volverAPantallaPrincipal).conectar();
    }

    private void mostrarHistorialFila() {
        cerrarVistas();
        vistaHistorialFila = new VistaHistorialFila();
        menuGescoController.conectar(vistaHistorialFila, sesionAdmin, cedulaSesionActual);
        new ControladorHistorialFila(vistaHistorialFila, this::volverAPantallaPrincipal).conectar();
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

        new ControladorHistorialSaldo(
            vistaHistorialSaldo,
            this::volverAPantallaPrincipal
        ).conectar(cedulaSesionActual);
    }

    private void cerrarVistaHistorialMenu() {
        if (vistaHistorialMenu != null) {
            vistaHistorialMenu.dispose();
            vistaHistorialMenu = null;
        }
    }

    private void cerrarVistaHistorialFila() {
        if (vistaHistorialFila != null) {
            vistaHistorialFila.dispose();
            vistaHistorialFila = null;
        }
    }

    private void cerrarVistaHistorialSaldo() {
        if (vistaHistorialSaldo != null) {
            vistaHistorialSaldo.dispose();
            vistaHistorialSaldo = null;
        }
    }

    private void reiniciarMenus() {
        javax.swing.UIManager.put("Button.margin", new java.awt.Insets(15, 35, 15, 35));
        javax.swing.UIManager.put("Button.font", new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        
        Object[] opciones = {
            "Reiniciar semana actual",
            "Reiniciar un día",
            "Cancelar"
        };

        javax.swing.JPanel panelTitulo = new javax.swing.JPanel(new java.awt.BorderLayout());

        panelTitulo.setPreferredSize(new java.awt.Dimension(600, 80)); 

        javax.swing.JLabel etiquetaTitulo = new javax.swing.JLabel("¿Qué deseas reiniciar?");
        etiquetaTitulo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20)); 
        etiquetaTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        panelTitulo.add(etiquetaTitulo, java.awt.BorderLayout.CENTER);

        int seleccion = javax.swing.JOptionPane.showOptionDialog(null, panelTitulo, "Reiniciar menú",javax.swing.JOptionPane.DEFAULT_OPTION,javax.swing.JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);

        javax.swing.UIManager.put("Button.margin", null);
        javax.swing.UIManager.put("Button.font", null);

        if (seleccion == 0) {
            reiniciarMenusSemana();
        } 
        else if (seleccion == 1) {
            reiniciarMenuDia();
        }
    }

    private void reiniciarMenusSemana() {
        java.util.List<java.time.LocalDate> fechasSemana = DataBase.calcularFechasParaReiniciar();

        boolean ok = DataBase.reiniciarMenusSemana();
        if (!ok) {
            javax.swing.JOptionPane.showMessageDialog(null,"Error al reiniciar menús.","Error",javax.swing.JOptionPane.ERROR_MESSAGE);
            mostrarGestionMenu();
            return;
        }

        if (fechasSemana.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(null,"No se pudieron calcular los días de la semana actual.","Sin cambios",javax.swing.JOptionPane.INFORMATION_MESSAGE);
            mostrarGestionMenu();
            return;
        }

        iniciarFlujoCreacionMenus(fechasSemana,"Se reinició el menú de la semana actual.\nA continuación, configura desayuno y almuerzo para cada día.");
    }

    private void reiniciarMenuDia() {
        java.util.List<java.time.LocalDate> fechasDisponibles = DataBase.obtenerFechasConMenusCreados();
        if (fechasDisponibles.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(null, "No hay días con menú creado para reiniciar.", "Sin días disponibles", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        DiaDisponible[] opcionesDias = fechasDisponibles.stream()
            .map(f -> new DiaDisponible(f, nombreDiaSemana(f) + " " + f)).toArray(DiaDisponible[]::new);

        javax.swing.UIManager.put("Button.margin", new java.awt.Insets(12, 40, 12, 40));
        javax.swing.UIManager.put("Button.font", new java.awt.Font("Arial", java.awt.Font.BOLD, 14));

        javax.swing.JPanel panelContenedor = new javax.swing.JPanel(new java.awt.GridBagLayout());
        panelContenedor.setPreferredSize(new java.awt.Dimension(550, 160)); 
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = java.awt.GridBagConstraints.NONE;

        javax.swing.JLabel etiqueta = new javax.swing.JLabel("Selecciona el día que deseas reiniciar:");
        etiqueta.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 18));
        gbc.gridy = 0;
        gbc.insets = new java.awt.Insets(0, 0, 20, 0); 
        panelContenedor.add(etiqueta, gbc);

        javax.swing.JComboBox<DiaDisponible> comboDias = new javax.swing.JComboBox<>(opcionesDias);
        comboDias.setPreferredSize(new java.awt.Dimension(350, 40));
        comboDias.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
        gbc.gridy = 1;
        gbc.insets = new java.awt.Insets(0, 0, 10, 0);
        panelContenedor.add(comboDias, gbc);

        int respuesta = javax.swing.JOptionPane.showConfirmDialog(null, panelContenedor, "Reiniciar menú de un día", javax.swing.JOptionPane.OK_CANCEL_OPTION, javax.swing.JOptionPane.PLAIN_MESSAGE);

        javax.swing.UIManager.put("Button.margin", null);
        javax.swing.UIManager.put("Button.font", null);

        if (respuesta == javax.swing.JOptionPane.OK_OPTION) {
            DiaDisponible seleccion = (DiaDisponible) comboDias.getSelectedItem();
            if (seleccion != null && DataBase.reiniciarMenuDia(seleccion.fecha())) {
                iniciarFlujoCreacionMenus(java.util.List.of(seleccion.fecha()), 
                    "Se reinició el menú del día seleccionado correctamente.");
            }
        }
    }
    private void iniciarFlujoCreacionMenus(java.util.List<java.time.LocalDate> fechas,String mensaje) {
        if (fechas == null || fechas.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(null,"No hay fechas para configurar menus.","Sin cambios",javax.swing.JOptionPane.INFORMATION_MESSAGE);
            mostrarGestionMenu();
            return;
        }

        javax.swing.JOptionPane.showMessageDialog(null,mensaje,"Menu reiniciado",javax.swing.JOptionPane.INFORMATION_MESSAGE);

        java.util.Queue<MenuPendiente> cola = new java.util.LinkedList<>();
        for (java.time.LocalDate fecha : fechas) {
            cola.add(new MenuPendiente(fecha, Menu.TipoMenu.DESAYUNO));
            cola.add(new MenuPendiente(fecha, Menu.TipoMenu.ALMUERZO));
        }

        mostrarCrearMenuParaFecha(cola, 1, cola.size());
    }

    private void mostrarCrearMenuParaFecha(java.util.Queue<MenuPendiente> cola, int actual, int total) {
        if (cola.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(null,
                "¡Todos los menús han sido configurados!",
                "Listo", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            mostrarGestionMenu();
            return;
        }

        MenuPendiente pendiente = cola.poll();
        java.time.LocalDate fecha = pendiente.fecha();
        Menu.TipoMenu tipoMenu = pendiente.tipoMenu();

        cerrarVistas();
        vistaCrearMenu = new VistaCrearMenu();

        String dia  = String.format("%02d", fecha.getDayOfMonth());
        String mes  = String.format("%02d", fecha.getMonthValue());
        String anio = String.valueOf(fecha.getYear());
        vistaCrearMenu.setFecha(dia, mes, anio);

        String nombreDia = nombreDiaSemana(fecha);
        String etiquetaTipo = tipoMenu == Menu.TipoMenu.DESAYUNO ? "Desayuno" : "Almuerzo";
        vistaCrearMenu.setTipoMenu(tipoMenu);
        vistaCrearMenu.setTipoMenuEditable(false);
        vistaCrearMenu.setTitle("Crear menú — " + nombreDia + " " + etiquetaTipo + " (" + actual + "/" + total + ")");

        menuGescoController.conectar(vistaCrearMenu, usuarioAdmin, cedulaSesionActual);

        new ControladorCrearMenu(vistaCrearMenu,this::mostrarGestionMenu,() -> mostrarCrearMenuParaFecha(cola, actual + 1, total)).conectar();
    }

    private String nombreDiaSemana(java.time.LocalDate fecha) {
        return switch (fecha.getDayOfWeek()) {
            case MONDAY -> "Lunes";
            case TUESDAY -> "Martes";
            case WEDNESDAY -> "Miércoles";
            case THURSDAY -> "Jueves";
            case FRIDAY -> "Viernes";
            case SATURDAY -> "Sábado";
            case SUNDAY -> "Domingo";
        };
    }

    private record MenuPendiente(java.time.LocalDate fecha, Menu.TipoMenu tipoMenu) {}

    private record DiaDisponible(java.time.LocalDate fecha, String etiqueta) {
        @Override
        public String toString() {
            return etiqueta;
        }
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

    private void cerrarVistaCambiarTipoEstudiante() {
        if (vistaCambiarTipoEstudiante != null) {
            vistaCambiarTipoEstudiante.dispose();
            vistaCambiarTipoEstudiante = null;
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
        cerrarVistaCrearMenu();
        cerrarVistaEditarMenu();
        cerrarVistaAgregarInsumo();
        cerrarVistaGestionMenu();
        cerrarVistaRecargarSaldo();
        cerrarVistaCambiarTipoEstudiante();
        cerrarVistaHistorialFila();
        cerrarVistaHistorialMenu();
        cerrarVistaHistorialSaldo();
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


