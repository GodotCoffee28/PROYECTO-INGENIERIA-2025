package com.gesco.models;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Comida{

	private String nombre; //Nombre de la comida
	private final List<String> insumos;

	public Comida(){
		nombre = "";
		insumos = new ArrayList<>();
	}

	public Comida(String nombre, List<String> listaInsumos){
		this.nombre = nombre;
		this.insumos = new ArrayList<>(listaInsumos);
	}

	public void agregarNuevoInsumo(String insumo){
		this.insumos.add(insumo);
	}


	public String getNombreComida(){
		return this.nombre;
	}

	public void resetearComida(){
		this.nombre = "";
		this.insumos.clear();
	}
	public List<String> getListaInsumos(){
		return Collections.unmodifiableList(this.insumos);
	}
	@Override
    public String toString() {
        return nombre;
    }


}
