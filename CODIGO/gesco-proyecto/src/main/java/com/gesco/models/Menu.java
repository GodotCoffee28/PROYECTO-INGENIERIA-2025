class Menu {
	private final String nombre;
	private final Insumo insumo;

	public Menu(String nombre, Insumo insumo) {
		this.nombre = nombre;
		this.insumo = insumo;
	}

	public String getNombre() {
		return nombre;
	}

	public Insumo getInsumo() {
		return insumo;
	}

	@Override
	public String toString() {
		return "Menu{" +
			   "nombre='" + nombre + '\'' +
			   ", insumo=" + insumo +
			   '}';
	}
}