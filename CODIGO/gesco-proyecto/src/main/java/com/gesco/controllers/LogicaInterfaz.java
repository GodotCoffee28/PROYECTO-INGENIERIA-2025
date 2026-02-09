package com.gesco.controllers;

import javax.swing.SwingUtilities;

import com.gesco.views.VistaCargarCFCV;
import com.gesco.views.VistaCrearMenu;
import com.gesco.views.VistaEditarMenu;
import com.gesco.views.VistaEspera;
import com.gesco.views.VistaFila;
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
	private VistaFila vistaFila;
	private VistaCargarCFCV vistaCargaCCB;
	private VistaVerCFCV vistaVerCfcv;
	private VistaCrearMenu vistaCrearMenu;
	private VistaEditarMenu vistaEditarMenu;
	private VistaGestionMenu vistaGestionMenu;
	private boolean usuarioAdmin;
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
			(esAdmin, nombre) -> inicioSesionRedireccionador.redirigir(esAdmin, nombre)  
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

	private void mostrarPantallaPrincipal(boolean esAdmin, String nombre) {
		cerrarVistas();
		usuarioAdmin = esAdmin;
		nombreUsuario = nombre;

		String nombreMostrar = (nombre == null || nombre.isBlank()) ? "Usuario" : nombre;
		if (esAdmin) {
			vistaInicioComensal = new VistaInicioComensal(nombreMostrar, 999999);
		} else {
			vistaInicioComensal = new VistaInicioComensal(nombreMostrar, 50);
		}

		menuGescoController.conectar(vistaInicioComensal, esAdmin);
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
		nombreUsuario = nombre;
		vistaInicioAdmin = new VistaInicioAdmin();
		menuGescoController.conectar(vistaInicioAdmin, true);
		new VistaInicioAdminControlador(
			vistaInicioAdmin,
			this::iniciar,
			this::mostrarGestionMenu,
			this::mostrarCargaCCB,
			this::mostrarVerCfcv,
			() -> mostrarPantallaPrincipal(false, nombreUsuario)
		).conectar();
	}

	private void mostrarPanelControl() {
		String nombreMostrar = (nombreUsuario == null || nombreUsuario.isBlank()) ? "Usuario" : nombreUsuario;
		mostrarPantallaAdmin(nombreMostrar);
	}

	private void mostrarMenuSemana() {
		cerrarVistas();
		vistaMenuSemana = new VistaMenuSemana();
		menuGescoController.conectar(vistaMenuSemana, usuarioAdmin);
		new VistaMenuSemanaControlador(
			vistaMenuSemana,
			this::volverAPantallaPrincipal
		).conectar();
	}

	private void mostrarTurnos() {
		cerrarVistas();
		vistaTurnos = new VistaTurnos();
		menuGescoController.conectar(vistaTurnos, usuarioAdmin);
		new VistaTurnosControlador(
			vistaTurnos,
			this::volverAPantallaPrincipal,
			this::mostrarMenuSemana
		).conectar();
	}

	private void mostrarFila() {
		cerrarVistas();
		String nombreMostrar = (nombreUsuario == null || nombreUsuario.isBlank()) ? "Usuario" : nombreUsuario;
		vistaFila = new VistaFila(nombreMostrar);
		menuGescoController.conectar(vistaFila, usuarioAdmin);
		new VistaFilaControlador(
			vistaFila,
			this::volverAPantallaPrincipal,
			this::mostrarMenuSemana
		).conectar();
	}

	private void mostrarCargaCCB() {
		cerrarVistas();
		vistaCargaCCB = new VistaCargarCFCV();
		menuGescoController.conectar(vistaCargaCCB, usuarioAdmin);
		new VistaCargarCFCVControlador(
			vistaCargaCCB,
			this::mostrarPanelControl
		).conectar();
	}

	private void mostrarVerCfcv() {
		cerrarVistas();
		vistaVerCfcv = new VistaVerCFCV();
		menuGescoController.conectar(vistaVerCfcv, usuarioAdmin);
		new VistaVerCFCVControlador(
			vistaVerCfcv,
			this::mostrarPanelControl
		).conectar();
	}

	private void mostrarCrearMenu() {
		cerrarVistas();
		vistaCrearMenu = new VistaCrearMenu();
		menuGescoController.conectar(vistaCrearMenu, usuarioAdmin);
		new VistaCrearMenuControlador(
			vistaCrearMenu,
			this::volverAPantallaPrincipal
		).conectar();
	}

	private void mostrarEditarMenu() {
		cerrarVistas();
		vistaEditarMenu = new VistaEditarMenu();
		menuGescoController.conectar(vistaEditarMenu, usuarioAdmin);
		new VistaEditarMenuControlador(
			vistaEditarMenu,
			this::volverAPantallaPrincipal
		).conectar();
	}

	private void mostrarGestionMenu() {
		cerrarVistas();
		vistaGestionMenu = new VistaGestionMenu();
		menuGescoController.conectar(vistaGestionMenu, usuarioAdmin);
		new VistaGestionMenuControlador(
			vistaGestionMenu,
			this::volverAPantallaPrincipal,
			this::mostrarEditarMenu,
			this::mostrarCrearMenu,
			this::reiniciarMenusSemana
		).conectar();
	}

	private void reiniciarMenusSemana() {
		boolean ok = DataBase.reiniciarMenusSemana();
		if (ok) {
			javax.swing.JOptionPane.showMessageDialog(null, "Menús de la semana reiniciados.", "Reiniciado", javax.swing.JOptionPane.INFORMATION_MESSAGE);
		} else {
			javax.swing.JOptionPane.showMessageDialog(null, "Error al reiniciar menús.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		// Volver a mostrar la pantalla de gestión para refrescar
		mostrarGestionMenu();
	}

	private void volverAPantallaPrincipal() {
		mostrarPantallaPrincipal(usuarioAdmin, nombreUsuario);
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

	private void cerrarVistaFila() {
		if (vistaFila != null) {
			vistaFila.dispose();
			vistaFila = null;
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
		cerrarVistaFila();
		cerrarVistaCargaCCB();
		cerrarVistaVerCfcv();
		cerrarVistaCrearMenu();
		cerrarVistaEditarMenu();
		cerrarVistaGestionMenu();
	}

}