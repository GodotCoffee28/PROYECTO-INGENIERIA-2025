package com.gesco.controllers;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

import com.gesco.views.RegistroView;
import com.gesco.views.VistaInicio;
import com.gesco.views.VistaInicioSesion;

import java.awt.event.MouseEvent;

public class LogicaInterfaz {

	private VistaInicio vistaInicio;
	private VistaInicioSesion vistaInicioSesion;
	private RegistroView registroView;

	public void iniciar() {
		SwingUtilities.invokeLater(() -> {
			vistaInicio = new VistaInicio();
			conectarVistaInicio();
		});
	}

	private void conectarVistaInicio() {
		vistaInicio.getBtnInicioSesion().addActionListener(e -> mostrarInicioSesion());
		vistaInicio.getBtnRegistrarse().addActionListener(e -> mostrarRegistro());
	}

	private void mostrarInicioSesion() {
		if (vistaInicio != null) {
			vistaInicio.dispose();  // Cierra la ventana de inicio
		}
		vistaInicioSesion = new VistaInicioSesion();
		conectarVistaInicioSesion();

		// Botón ←: vuelve al inicio
		vistaInicioSesion.getBackIcon().addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				vistaInicioSesion.dispose();
				iniciar(); // Vuelve a la pantalla principal
			}
		});
	}

	private void conectarVistaInicioSesion() {
		vistaInicioSesion.getBtnInicioSesion().addActionListener(e -> procesarInicioSesion());
		vistaInicioSesion.getRegistroLink().addMouseListener(new MouseInputAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mostrarRegistro();
			}
		});
	}

	private void procesarInicioSesion() {
		String cedula = vistaInicioSesion.getCedula();
		String clave = vistaInicioSesion.getClave();

		if (cedula == null || cedula.isBlank() || clave == null || clave.isBlank()) {
			JOptionPane.showMessageDialog(
				vistaInicioSesion,
				"Debe completar cédula y clave.",
				"Datos incompletos",
				JOptionPane.WARNING_MESSAGE
			);
			return;
		}

		boolean valido = DataBase.validarInicioSesion(cedula, clave);
		if (valido) {
			JOptionPane.showMessageDialog(
				vistaInicioSesion,
				"Inicio de sesión correcto.",
				"Acceso",
				JOptionPane.INFORMATION_MESSAGE
			);
			// Aquí podrías ir a otra pantalla (ej. menú principal) en el futuro
		} else {
			JOptionPane.showMessageDialog(
				vistaInicioSesion,
				"Usuario o clave incorrectos.",
				"Acceso denegado",
				JOptionPane.ERROR_MESSAGE
			);
		}
	}

	private void mostrarRegistro() {
		if (vistaInicio != null) {
			vistaInicio.dispose();  // ← AQUÍ ESTABA EL ERROR: no cerraba la ventana de inicio
		}
		if (registroView != null) {
			registroView.dispose();
		}
		registroView = new RegistroView();
		registroView.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// Botón ←: vuelve al inicio
		registroView.getBackIcon().addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				registroView.dispose();
				iniciar(); // Vuelve a la pantalla principal
			}
		});
	}
}