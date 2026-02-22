
package  com.gesco.models;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class Menu {
    public enum EstadoMenu {
        CON_MENU,
        NO_DISPONIBLE
    }

    private final LocalDate fecha; 
    private final List<Platillo> platillos;
    private EstadoMenu estado;

    public Menu(LocalDate fecha) {
        this.fecha = fecha;
        this.platillos = new ArrayList<>();
        this.estado = EstadoMenu.CON_MENU;
    }

    public Menu(LocalDate fecha, EstadoMenu estado) {
        this.fecha = fecha;
        this.platillos = new ArrayList<>();
        this.estado = estado == null ? EstadoMenu.CON_MENU : estado;
    }

    public Menu() {
        this.fecha = LocalDate.now();
        this.platillos = new ArrayList<>();
        this.estado = EstadoMenu.NO_DISPONIBLE;
    }

    public void agregarPlatillo(Platillo platillo) {
        if (platillo != null) {
            this.platillos.add(platillo);
            this.estado = EstadoMenu.CON_MENU;
        }
    }

    public LocalDate getFecha() { return fecha; }
    public EstadoMenu getEstado() { return estado; }
    public void setEstado(EstadoMenu estado) {
        if (estado != null) {
            this.estado = estado;
        }
    }
    
    public List<Platillo> getPlatillos() {
        return Collections.unmodifiableList(platillos);
    }

    public boolean tienePlatillos() {
        return !platillos.isEmpty();
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        StringBuilder sb = new StringBuilder();
        sb.append("--- CONFIGURACION DEL MENU [").append(fecha.format(formatter)).append("] ---\n");

        if (estado == EstadoMenu.NO_DISPONIBLE) {
            sb.append("Menú no disponible para este día.");
            return sb.toString();
        }
        
        if (platillos.isEmpty()) {
            sb.append("No hay platillos asignados para este día.");
        } else {
            for (Platillo p : platillos) {
                sb.append("- ").append(p.toString()).append("\n");
            }
        }
        return sb.toString();
    }
}



