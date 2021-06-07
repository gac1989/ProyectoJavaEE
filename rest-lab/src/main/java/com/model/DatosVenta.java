package com.model;

public class DatosVenta {
	private int id_juego;
	private String juego;
	private String user;
	private double precio;
	
	
	
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public int getId_juego() {
		return id_juego;
	}
	public void setId_juego(int id_juego) {
		this.id_juego = id_juego;
	}
	public String getJuego() {
		return juego;
	}
	public void setJuego(String juego) {
		this.juego = juego;
	}
	
	public double getPrecio() {
		return precio;
	}
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	
	
	
	
}
