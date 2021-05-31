package com.model;

import java.util.Date;

public class Venta {

	
	private Jugador user;
	private Juego juego;
	private double precio;
	private Date fecha;
	
	
	
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Jugador getUser() {
		return user;
	}
	public void setUser(Jugador user) {
		this.user = user;
	}
	public Juego getJuego() {
		return juego;
	}
	public void setJuego(Juego juego) {
		this.juego = juego;
	}
	public double getPrecio() {
		return precio;
	}
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	
	
	
}
