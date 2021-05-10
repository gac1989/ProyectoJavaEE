package com.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;


@Entity
@Table(name="jugador")
@PrimaryKeyJoinColumn(name="nick")
public class Jugador extends Usuario {
	@Column
	private Date fecha_nac;
	@Column
	private String nombre;
	@Column
	private String apellido;
	@ManyToMany(cascade=CascadeType.MERGE, fetch=FetchType.LAZY)
	@JoinTable(name="usuariojuego", joinColumns = @JoinColumn(name = "nick"),
    inverseJoinColumns = @JoinColumn(name = "juego_id") )
	private List<Juego> juegos;
	
	
	public List<Juego> getJuegos() {
		return juegos;
	}
	public void setJuegos(List<Juego> juegos) {
		this.juegos = juegos;
	}
	public Date getFecha_nac() {
		return fecha_nac;
	}
	public void setFecha_nac(Date fecha_nac) {
		this.fecha_nac = fecha_nac;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	
	public void agregarJuego(Juego j) {
		this.juegos.add(j);
	}
	
	
	@Override
	public String toString() {
		return "Jugador [fecha_nac=" + fecha_nac + ", nombre=" + nombre + ", apellido=" + apellido + "]";
	}
}
