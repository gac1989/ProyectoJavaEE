package com.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.MapKey;
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
	@OneToMany(/*targetEntity = Curso.class,*/cascade=CascadeType.MERGE, fetch=FetchType.LAZY)
	@JoinTable(name="usuariojuego", joinColumns = @JoinColumn(name = "nick"),
    inverseJoinColumns = @JoinColumn(name = "juego_id") )
	@MapKey(name = "nombre")    
	private Map<String,Juego> juegos;
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
	
	public Map<String, Juego> getJuegos() {
		if(juegos==null) {
			juegos= new HashMap<String, Juego>();
		}
		return juegos;
	}
	public void setJuegos(Map<String, Juego> juegos) {
		this.juegos = juegos;
	}
	@Override
	public String toString() {
		return "Jugador [fecha_nac=" + fecha_nac + ", nombre=" + nombre + ", apellido=" + apellido + "]";
	}
}
