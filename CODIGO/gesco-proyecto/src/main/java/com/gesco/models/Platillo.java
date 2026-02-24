package com.gesco.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Platillo {
    private final String nombre;
    private final List<Insumo> insumos;
    private final float costoPlatillo;

    public Platillo(String nombre){
        this.nombre = nombre;
        this.insumos = new ArrayList<>();
        this.costoPlatillo = calcularCostoPlatillo();
    }
    public Platillo() {
        this.nombre = "Nuevo Platillo";
        this.insumos = new ArrayList<>();
        this.insumos.add(new Insumo());
        this.costoPlatillo = calcularCostoPlatillo();
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

    public final float calcularCostoPlatillo() {
        float costoTotal = 0.0f;
        for (Insumo insumo : insumos) {
            costoTotal += insumo.getCostoInsumo();
        }
        return costoTotal;
    }
    public float getCostoPlatillo() {
        return costoPlatillo;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(nombre);
        return sb.toString();
    }
}
