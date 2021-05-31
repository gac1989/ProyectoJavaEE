package com.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.dao.CompraDAO;
import com.fasterxml.jackson.annotation.JsonBackReference;


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
	@OneToMany(mappedBy = "user")
	@JsonBackReference
	private List<CompraJuego> juegos;
	
	
	public List<CompraJuego> getJuegos() {
		return juegos;
	}
	
	public List<Juego> obtenerJuegos(){
		List<Juego> misjuegos = new ArrayList<Juego>();
		for(CompraJuego compra: this.juegos) {
			misjuegos.add(compra.getJuego());
		}
		return misjuegos;
	}
	
	public void setJuegos(List<CompraJuego> juegos) {
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
	
	
	public void agregarJuego(CompraJuego compra) {
		this.juegos.add(compra);
	}
	
	
	@Override
	public String toString() {
		return "Jugador [fecha_nac=" + fecha_nac + ", nombre=" + nombre + ", apellido=" + apellido + "]";
	}
}
