
package  com.gesco.models;

public class Insumo {
	private final String nombre;
    private final int cantidad;
    private final String tipoNutricional;
	private float costoInsumoUnitario, costoInsumoTotal;

    public Insumo(String nombre, int cantidad, String tipoNutricional, float costoInsumoUnitario) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.tipoNutricional = tipoNutricional;
        this.costoInsumoUnitario = costoInsumoUnitario;
        this.costoInsumoTotal = costoInsumoUnitario * cantidad;
    }
    public Insumo(String nombre, int cantidad, String tipoNutricional) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.tipoNutricional = tipoNutricional;
        this.costoInsumoUnitario = 0.0f;
        this.costoInsumoTotal = 0.0f;
    }
    public Insumo(){
        this.nombre = "Sin nombre";
        this.cantidad = 0;
        this.tipoNutricional = "N/A";
        this.costoInsumoTotal = 0.0f;
        this.costoInsumoUnitario = 0.0f;
    }
	public String getNombre() { return nombre; }
    public int getCantidad() { return cantidad; }
    public String getTipoNutricional() { return tipoNutricional; }
    public float getCostoInsumoTotal() { return costoInsumoTotal; }
    public float getCostoUnitario() { return costoInsumoUnitario; }
    public void setCostoInsumoTotal(float costoInsumoTotal) { this.costoInsumoTotal = costoInsumoTotal; }
    @Override
    public String toString() {
        return nombre + " (" + cantidad + " unidades - " + tipoNutricional + " - $" + costoInsumoTotal + ")";
    }	
}