package com.gesco.controllers;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import  javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

import com.gesco.views.PlantillasViews.PlantillaGesco;
import com.gesco.views.VistaInicio;
import com.gesco.views.VistaInicioSesion;
import com.gesco.views.VistaRegistro;
public class LogicaInterfaz {

	private VistaInicio vistaInicio;
	private VistaInicioSesion vistaInicioSesion;
	private VistaRegistro vistaRegistro;

	public void iniciar() {
		SwingUtilities.invokeLater(() -> {
			vistaInicio = new VistaInicio();
			conectarVistaInicio();
		});
	}
	/* queria generalizar el inicio jj
		public void iniciar(PlantillaGesco pG){
		SwingUtilities.invokeLater(() -> {
			pG = new PlantillaGesco();

		});
	}

	*/
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
		conectarMenuGesco(vistaInicioSesion);
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
			vistaInicio.dispose();  
			vistaInicio.dispose();  
			vistaInicio = null;

		}
		if (vistaInicioSesion != null) {
        	vistaInicioSesion.dispose();
        	vistaInicioSesion = null;
    	}
		if (vistaRegistro != null) {
			vistaRegistro.dispose();
			vistaRegistro = null;
		}
		vistaRegistro = new VistaRegistro();
		conectarMenuGesco(vistaRegistro);
		vistaRegistro.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// Botón ←: vuelve al inicio
		vistaRegistro.getBackIcon().addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				vistaRegistro.dispose();
				iniciar(); // Vuelve a la pantalla principal
			}
		});
		vistaRegistro.getLoginLink().addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				vistaRegistro.dispose();
				mostrarInicioSesion(); // Vuelve a inicio sesion
			}
		});
	}

	
    private void conectarMenuGesco(PlantillaGesco vista) {
        var menuFactory = vista.getPopupMenu();

        vista.getMenuIcon().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Obtenemos el menú real y lo mostramos
                JPopupMenu popup = menuFactory.getMenu();
                popup.show(e.getComponent(), 0, e.getComponent().getHeight());
            }
        });

        // Aplicando Pattern Matching e Instanceof moderno
        for (Component comp : menuFactory.getMenu().getComponents()) {
            // Aquí ocurre la magia: check + cast automático a la variable 'item'
            if (comp instanceof JMenuItem item) { 
                item.addActionListener(e -> {
                    procesarAccionMenu(e.getActionCommand());
                    procesarAccionMenuAdmin(e.getActionCommand());
                });
            }
        }
    }

    private void procesarAccionMenu(String comando) {
        // Aplicando también el Rule Switch para limpiar todos los warnings
        switch (comando) {
            case "CMD_INICIO"     -> System.out.println("Inicio");
            case "CMD_SESION"     -> System.out.println("Sesión");
            case "CMD_REGISTRO"   -> System.out.println("Registro");
            case "CMD_FILA"       -> System.out.println("Fila");
            case "CMD_MENUSEMANA" -> System.out.println("Menú");
            case "CMD_TURNOS"     -> System.out.println("Turnos");
            case "CMD_SALIR"      -> System.exit(0);
            default -> System.out.println("Comando: " + comando);
        }
    }

    private void procesarAccionMenuAdmin(String comando) {
        switch (comando) {
            case "CMD_CCB"         -> System.out.println("CCB");
            case "CMD_CREARMENU"   -> System.out.println("Crear");
            case "CMD_EDITARMENU"  -> System.out.println("Editar");
            case "CMD_GESTIONMENU" -> System.out.println("Gestión");
        }
    }
}