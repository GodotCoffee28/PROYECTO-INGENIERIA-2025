
package  com.gesco.models;

class Insumo {
	private final String nombre;
	private final int cantidad;

	public Insumo(String nombre, int cantidad) {
		this.nombre = nombre;
		this.cantidad = cantidad;
	}

	public String getNombre() {
		return nombre;
	}

	public int getCantidad() {
		return cantidad;
	}

	@Override
	public String toString() {
		return "Insumo{" +
			   "nombre='" + nombre + '\'' +
			   ", cantidad=" + cantidad +
			   '}';
	}
}