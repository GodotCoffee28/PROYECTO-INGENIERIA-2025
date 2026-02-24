
package  com.gesco.models;

public class Insumo {
	private final String nombre;
    private final int cantidad;
    private final String tipoNutricional;
	private float costoInsumo;

    public Insumo(String nombre, int cantidad, String tipoNutricional, float costoInsumo) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.tipoNutricional = tipoNutricional;
        this.costoInsumo = costoInsumo;
    }
    public Insumo(String nombre, int cantidad, String tipoNutricional) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.tipoNutricional = tipoNutricional;
        this.costoInsumo = 0.0f;
    }
    public Insumo(){
        this.nombre = "Sin nombre";
        this.cantidad = 0;
        this.tipoNutricional = "N/A";
        this.costoInsumo = 0.0f;
    }
	public String getNombre() { return nombre; }
    public int getCantidad() { return cantidad; }
    public String getTipoNutricional() { return tipoNutricional; }
    public float getCostoInsumo() { return costoInsumo; }
    public float getCostoUnitario() { return costoInsumo / cantidad; }
    public void setCostoInsumo(float costoInsumo) { this.costoInsumo = costoInsumo; }
	@Override
    public String toString() {
        return nombre + " (" + cantidad + " unidades - " + tipoNutricional + " - $" + costoInsumo + ")";
    }
}