
package  com.gesco.models;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class Menu {
    private final LocalDate fecha; 
    
    private final List<Platillo> platillos;

    public Menu(LocalDate fecha) {
        this.fecha = fecha;
        this.platillos = new ArrayList<>();
    }
    public void agregarPlatillo(Platillo platillo) {
        if (platillo != null) {
            this.platillos.add(platillo);
        }
    }

    public LocalDate getFecha() { return fecha; }
    
    public List<Platillo> getPlatillos() {
        return Collections.unmodifiableList(platillos);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        StringBuilder sb = new StringBuilder();
        sb.append("--- CONFIGURACIÓN DEL MENÚ [").append(fecha.format(formatter)).append("] ---\n");
        
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



