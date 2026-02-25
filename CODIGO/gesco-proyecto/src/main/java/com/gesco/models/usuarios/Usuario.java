

package com.gesco.models.usuarios;

import java.util.Locale;


public class Usuario {
	public enum TipoUsuario {
		COMENSAL,
		ESTUDIANTE,
		PROFESOR,
		EMPLEADO,
		ADMIN,
		SUPER_ADMIN;

		public boolean esAdmin() {
			return this == ADMIN || this == SUPER_ADMIN;
		}

		public String toEtiqueta() {
			return switch (this) {
				case ESTUDIANTE -> "Estudiante";
				case PROFESOR -> "Profesor";
				case EMPLEADO -> "Empleado";
				default -> "Comensal";
			};
		}

		public static TipoUsuario fromEtiqueta(String valor) {
			if (valor == null) return COMENSAL;
			return switch (valor.trim().toLowerCase(Locale.ROOT)) {
				case "estudiante" -> ESTUDIANTE;
				case "profesor" -> PROFESOR;
				case "empleado" -> EMPLEADO;
				default -> COMENSAL;
			};
		}
	}
	private final int id;
	private String nombre;
	private String email;
	private String comprobante;

	public Usuario(int id, String nombre, String email, String comprobante) {
		this.id = id;
		this.nombre = nombre;
		this.email = email;
		this.comprobante = comprobante;
	}

	public int getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getComprobante() {
		return comprobante;
	}

	public void setComprobante(String comprobante) {
		this.comprobante = comprobante;
	}

	@Override
	public String toString() {
		return "Usuario{" +
			   "id=" + id +
			   ", nombre='" + nombre + '\'' +
			   ", email='" + email + '\'' +
			   ", comprobante='" + comprobante + '\'' +
			   '}';
	}

	public void consultarMenu() {
		System.out.println("--- Menú ---");
		System.out.println("1. Ver perfil");
		System.out.println("2. Ver comprobante");
		System.out.println("3. Salir");
	}

	public void accederAlSistema() {
		System.out.println(nombre + " accedió al sistema.");
	}

	public void registrarseEnElSistema() {
		System.out.println(nombre + " se registró en el sistema.");
	}
}

