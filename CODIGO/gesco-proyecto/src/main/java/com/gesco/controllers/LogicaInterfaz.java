package com.gesco.controllers;

import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

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
<<<<<<< HEAD
			vistaInicio.dispose();  
=======
			vistaInicio.dispose();  // ← AQUÍ ESTABA EL ERROR: no cerraba la ventana de inicio
			vistaInicio = null;
>>>>>>> c4b8c0a8bfba150e7d197e2ffdcb7cbcfafd6563
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
	private void conectarMenuGesco(com.gesco.views.PlantillasViews.PlantillaGesco vista) {
        
        vista.getMenuIcon().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                vista.getPopupMenu().getMenu().show(e.getComponent(), 0, e.getComponent().getHeight());
            }
        });


        for (java.awt.Component comp : vista.getPopupMenu().getMenu().getComponents()) {
            if (comp instanceof javax.swing.JMenuItem) {
                ((javax.swing.JMenuItem) comp).addActionListener(e -> {
                    procesarAccionMenu(e.getActionCommand());
                });
            }
        }
    }

    private void procesarAccionMenu(String comando) {
        switch (comando) {
            case "CMD_INICIO_":
				//iR A inicio
                break;
            case "CMD_SESION":
					//Ir a inicio de sesion
                break;
			case "CMD_REGISTRO":
				//Ir al registro
				break;
			case "CMD_FILA":
				//Ir a fila (Ya montare esa interfaz)
				break;
			case "CMD_MENUSEMANA":
				//Ir menu de la semana (me falta hacerlo)
				break;
			case "CMD_TURNOS":
				//Ir a la interfaz de turnos (por hacer)
				break;
            case "CMD_SALIR":
                System.exit(0); // Cierra la app
                break;
        }
    }
}