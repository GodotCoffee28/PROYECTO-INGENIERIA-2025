package com.gesco.controllers;

import javax.swing.SwingUtilities;

import com.gesco.views.VistaEspera;
import com.gesco.views.VistaInicio;
import com.gesco.views.VistaInicioSesion;
import com.gesco.views.VistaRegistro;
public class LogicaInterfaz {

	private VistaInicio vistaInicio;
	private VistaInicioSesion vistaInicioSesion;
	private VistaRegistro vistaRegistro;
	private VistaEspera vistaEspera;
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
	/* queria generalizar el inicio jj
		public void iniciar(PlantillaGesco pG){
		SwingUtilities.invokeLater(() -> {
			pG = new PlantillaGesco();

		});
	}

	*/
	private void mostrarInicioSesion() {
		cerrarVistaInicio();
		cerrarVistaRegistro();
		cerrarVistaInicioSesion();
		vistaInicioSesion = new VistaInicioSesion();
		menuGescoController.conectar(vistaInicioSesion, usuarioAdmin);
		new VistaInicioSesionControlador(
			vistaInicioSesion,
			this::volverDesdeInicioSesion,
			this::mostrarRegistroDesdeInicioSesion,
			esAdmin -> usuarioAdmin = esAdmin
		).conectar();
	}

	private void mostrarRegistro() {
		cerrarVistaInicio();
		cerrarVistaInicioSesion();
		cerrarVistaRegistro();
		vistaRegistro = new VistaRegistro();
		menuGescoController.conectar(vistaRegistro, false);
		new VistaRegistroControlador(
			vistaRegistro,
			this::volverDesdeRegistro,
			this::mostrarInicioSesionDesdeRegistro,
			this::mostrarEsperaDesdeRegistro
		).conectar();
	}

	private void mostrarEspera() {
		cerrarVistaRegistro();
		cerrarVistaEspera();
		vistaEspera = new VistaEspera();
		new VistaEsperaControlador(
			vistaEspera,
			this::volverDesdeEspera
		).conectar();
	}

	private void mostrarRegistroDesdeInicioSesion() {
		cerrarVistaInicioSesion();
		mostrarRegistro();
	}

	private void mostrarInicioSesionDesdeRegistro() {
		cerrarVistaRegistro();
		mostrarInicioSesion();
	}

	private void mostrarEsperaDesdeRegistro() {
		cerrarVistaRegistro();
		mostrarEspera();
	}

	private void volverDesdeInicioSesion() {
		cerrarVistaInicioSesion();
		iniciar();
	}

	private void volverDesdeRegistro() {
		cerrarVistaRegistro();
		iniciar();
	}

	private void volverDesdeEspera() {
		cerrarVistaEspera();
		iniciar();
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

	private void cerrarVistas() {
		cerrarVistaInicio();
		cerrarVistaInicioSesion();
		cerrarVistaRegistro();
		cerrarVistaEspera();
	}
}