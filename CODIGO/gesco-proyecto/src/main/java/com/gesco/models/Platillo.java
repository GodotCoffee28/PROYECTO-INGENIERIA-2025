package com.gesco.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Platillo {
    String nombre;
    List<Insumo> insumos;

    public Platillo(String nombre){
        this.nombre = nombre;
        this.insumos = new ArrayList<>();
    }
    public Platillo() {
        this.nombre = "Nuevo Platillo";
        this.insumos = new ArrayList<>();
        this.insumos.add(new Insumo());
    }
    public void agregarInsumo(Insumo insumo) {
        if (insumo != null) {
            this.insumos.add(insumo);
        }
    }
    public String getNombre() { return nombre; }
    
    public List<Insumo> getInsumos() {
        return Collections.unmodifiableList(insumos);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(nombre);
        /*.append(" (Insumos: ");
        for (int i = 0; i < insumos.size(); i++) {
            sb.append(insumos.get(i).getNombre());
            if (i < insumos.size() - 1) sb.append(", ");
        }
        sb.append(")"); */
        return sb.toString();
    }
}
