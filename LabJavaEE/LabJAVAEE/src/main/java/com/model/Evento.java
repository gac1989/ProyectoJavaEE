package com.model;

import java.util.Date;


public class Evento {

	private String nombre;
	private Float descuento;
	private Date fecha_ini;
	private Date fecha_fin;
	private int activo;
	private String estado;
	
	
	
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
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
	public void setFecha_ini(long fecha_ini_sec) {
		this.fecha_ini = new Date(fecha_ini_sec);
	}
	public Date getFecha_fin() {
		return fecha_fin;
	}
	public void setFecha_fin(long fecha_fin_sec) {
		this.fecha_fin = new Date(fecha_fin_sec);
	}
	public int getActivo() {
		return activo;
	}
	public void setActivo(int activo) {
		this.activo = activo;
	}
	

	
	
}
