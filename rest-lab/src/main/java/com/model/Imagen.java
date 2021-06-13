package com.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name="imagenes")
public class Imagen {
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@Lob
	private byte[] contenido;

	@ManyToOne()
	@JoinColumn(name="juego")
	@JsonBackReference
	private Juego juego;
	
	
	
	
	public Juego getJuego() {
		return juego;
	}


	public void setJuego(Juego juego) {
		this.juego = juego;
	}


	public byte[] getContenido() {
		return contenido;
	}
	
	
	public void setContenido(byte[] contenido) {
		this.contenido = contenido;
	}
	
	
	
}
