class Administrador extends Empleado {
    public Administrador(int id, String nombre, String email, String comprobante) {
        super(id, nombre, email, comprobante);
    }
    public void GenerarReporte() {
        System.out.println("Generando reporte");
    }
    public void GestionarMenu() {
        System.out.println("Gestionando menú");
    }

    public void GenerarTurno() {
        System.out.println("Generando turno");
    }

    public void modificarSys() {
        System.out.println("Modificando sistema");
    }
    
}
