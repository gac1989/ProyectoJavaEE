package com.model;

import java.util.Date;

public class Jugador extends Usuario {
	
	private Date fecha_nac;
	private String nombre;
	private String apellido;
	
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
	@Override
	public String toString() {
		return "Jugador [fecha_nac=" + fecha_nac + ", nombre=" + nombre + ", apellido=" + apellido + "]";
	}
}
