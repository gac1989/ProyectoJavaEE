package com.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="categoria")
public class Categoria {

	@Id
	private String nombre;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	@ManyToMany(cascade=CascadeType.MERGE, fetch=FetchType.LAZY)
	@JoinTable(name="categoriajuego", joinColumns = @JoinColumn(name = "nombre_cat"),
    inverseJoinColumns = @JoinColumn(name = "juego_id") )
	private List<Juego> juegos;

	
	public List<Juego> getJuegos() {
		return juegos;
	}

	public void setJuegos(List<Juego> juegos) {
		this.juegos = juegos;
	}

	public void agregarJuego(Juego j) {
		this.juegos.add(j);
	}
	
	
	@Override
	public String toString() {
		return "Categoria [nombre=" + nombre + "]";
	}

}
