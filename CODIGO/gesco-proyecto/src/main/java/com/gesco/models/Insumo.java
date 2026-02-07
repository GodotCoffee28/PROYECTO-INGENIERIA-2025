
package  com.gesco.models;

public class Insumo {
	private final String nombre;
    private final int cantidad;
    private final String tipoNutricional;
	
    public Insumo(String nombre, int cantidad, String tipoNutricional) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.tipoNutricional = tipoNutricional;
    }
	public String getNombre() { return nombre; }
    public int getCantidad() { return cantidad; }
    public String getTipoNutricional() { return tipoNutricional; }

	@Override
    public String toString() {
        return nombre + " (" + cantidad + " unidades - " + tipoNutricional + ")";
    }
}