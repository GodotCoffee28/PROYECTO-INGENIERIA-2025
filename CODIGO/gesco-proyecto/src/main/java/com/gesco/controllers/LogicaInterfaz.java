package com.gesco.controllers;

import javax.swing.SwingUtilities;

import com.gesco.views.VistaEspera;
import com.gesco.views.VistaInicio;
import com.gesco.views.VistaInicioComensal;  
import com.gesco.views.VistaInicioSesion;
import com.gesco.views.VistaRegistro;

public class LogicaInterfaz {

	private VistaInicio vistaInicio;
	private VistaInicioSesion vistaInicioSesion;
	private VistaRegistro vistaRegistro;
	private VistaEspera vistaEspera;
	private VistaInicioComensal vistaInicioComensal;  
	private boolean usuarioAdmin;
	private final MenuGescoControlador menuGescoController = new MenuGescoControlador();

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
			esAdmin -> mostrarPantallaPrincipal(esAdmin)  
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

	private void mostrarPantallaPrincipal(boolean esAdmin) {
		cerrarVistas();
		usuarioAdmin = esAdmin;

		if (esAdmin) {
			vistaInicioComensal = new VistaInicioComensal("Administrador", 999999);
		} else {
			vistaInicioComensal = new VistaInicioComensal("Estudiante", 50);
		}

		menuGescoController.conectar(vistaInicioComensal, esAdmin);
		new VistaInicioComensalControlador(
			vistaInicioComensal,
			this::iniciar
		).conectar();
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

	private void cerrarVistaInicioComensal() {
		if (vistaInicioComensal != null) {
			vistaInicioComensal.dispose();
			vistaInicioComensal = null;
		}
	}

	private void cerrarVistas() {
		cerrarVistaInicio();
		cerrarVistaInicioSesion();
		cerrarVistaRegistro();
		cerrarVistaEspera();
		cerrarVistaInicioComensal();  
	}
}