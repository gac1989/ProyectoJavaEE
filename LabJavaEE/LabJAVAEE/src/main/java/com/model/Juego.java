package com.model;

import java.util.List;

public class Juego {
	
	private int id;
	private String nombre;
	private String descripcion;
	private float precio;
	private float nota;
	private float precio_descuento;
	private Evento evento;
	private String estado;
	private boolean desbloqueo;
	private String trailer;
	private String tags;
	private String imagen;
	private List<Imagen> imagenes;
	
	
	
	
	public String getImagen() {
		return imagen;
	}
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	public boolean isDesbloqueo() {
		return desbloqueo;
	}
	public void setDesbloqueo(boolean desbloqueo) {
		this.desbloqueo = desbloqueo;
	}
	public List<Imagen> getImagenes() {
		return imagenes;
	}
	public void setImagenes(List<Imagen> imagenes) {
		this.imagenes = imagenes;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getTrailer() {
		return trailer;
	}
	public void setTrailer(String trailer) {
		this.trailer = trailer;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
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
	public float getPrecio() {
		return precio;
	}
	public void setPrecio(float precio) {
		this.precio = precio;
	}
	
}
