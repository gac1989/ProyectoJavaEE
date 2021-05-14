package com.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="evento")
public class Evento {

	@Id
	private String nombre;
	@Column
	private Float descuento;
	@Column
	private Date fecha_ini;
	@Column
	private Date fecha_fin;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	@ManyToMany(cascade=CascadeType.MERGE, fetch=FetchType.LAZY)
	@JoinTable(name="eventojuego", joinColumns = @JoinColumn(name = "nombre_ev"),
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
	
	
	public Float getDescuento() {
		return descuento;
	}

	public void setDescuento(Float descuento) {
		this.descuento = descuento;
	}

	public Date getFecha_ini() {
		return fecha_ini;
	}

	public void setFecha_ini(Date fecha_ini) {
		this.fecha_ini = fecha_ini;
	}

	public Date getFecha_fin() {
		return fecha_fin;
	}

	public void setFecha_fin(Date fecha_fin) {
		this.fecha_fin = fecha_fin;
	}

	@Override
	public String toString() {
		return "Evento [nombre=" + nombre + "]";
	}

}
