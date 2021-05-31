package com.model;

public class Juego {
	private int id;
	private String nombre;
	private String descripcion;
	private String rutaImg;
	private float precio;
	private float nota;
	private float precio_descuento;
	private Evento evento;
	
	
	
	
	public Evento getEvento() {
		return evento;
	}
	public void setEvento(Evento evento) {
		this.evento = evento;
	}
	public float getPrecio_descuento() {
		
		return precio_descuento;
	}
	public void setPrecio_descuento(float precio_descuento) {
		this.precio_descuento = precio_descuento;
	}
	
	public float getNota() {
		return nota;
	}
	public void setNota(float nota) {
		this.nota = nota;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getRutaImg() {
		return rutaImg;
	}
	public void setRutaImg(String rutaImg) {
		this.rutaImg = rutaImg;
	}
	public float getPrecio() {
		return precio;
	}
	public void setPrecio(float precio) {
		this.precio = precio;
	}
	@Override
	public String toString() {
		return "Juego [nombre=" + nombre + ", descripcion=" + descripcion + ", rutaImg=" + rutaImg + ", precio="
				+ precio + "]";
	}
	
	
}
