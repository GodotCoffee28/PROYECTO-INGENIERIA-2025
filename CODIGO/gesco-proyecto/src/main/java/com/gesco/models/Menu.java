
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
    public enum TipoMenu{
        DESAYUNO,
        ALMUERZO,
        NO_DEFINIDO
    }

    private final LocalDate fecha; 
    private final List<Platillo> platillos;
    private EstadoMenu estado;
    private TipoMenu tipoMenu;
    private float costoMenu;

    public Menu(LocalDate fecha) {
        this.fecha = fecha;
        this.platillos = new ArrayList<>();
        this.estado = EstadoMenu.CON_MENU;
        this.tipoMenu = TipoMenu.NO_DEFINIDO;
    }

    public Menu(LocalDate fecha, EstadoMenu estado) {
        this.fecha = fecha;
        this.platillos = new ArrayList<>();
        this.estado = estado == null ? EstadoMenu.CON_MENU : estado;
        this.tipoMenu = TipoMenu.NO_DEFINIDO;
    }

    public Menu() {
        this.fecha = LocalDate.now();
        this.platillos = new ArrayList<>();
        this.estado = EstadoMenu.NO_DISPONIBLE;
        this.tipoMenu = TipoMenu.NO_DEFINIDO;
        this.costoMenu = 0.0f;
    }
    
    public final float calcularCostoMenu() {
        float costoTotal = 0.0f;
        for (Platillo platillo : platillos) {
            costoTotal += platillo.getCostoPlatillo();
        }
        return costoTotal;
    }

    public void agregarPlatillo(Platillo platillo) {
        if (platillo != null) {
            this.platillos.add(platillo);
            this.estado = EstadoMenu.CON_MENU;
            this.costoMenu = calcularCostoMenu();
        }
    }

    public LocalDate getFecha() { return fecha; }
    public EstadoMenu getEstado() { return estado; }

    public float getCostoMenu() {
        this.costoMenu = calcularCostoMenu();
        return this.costoMenu;
    }
    public void setEstado(EstadoMenu estado) {
        if (estado != null) {
            this.estado = estado;
        }
    }
    public TipoMenu getTipoMenu() { return tipoMenu; }
    public void setTipoMenu(TipoMenu tipoMenu) {
        if (tipoMenu != null) {
            this.tipoMenu = tipoMenu;
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



