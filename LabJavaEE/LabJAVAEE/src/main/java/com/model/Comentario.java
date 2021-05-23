package com.model;


public class Comentario {
	private int id;
	private String texto;
	private int nota;
	private Jugador autor;

	public int getNota() {
		return nota;
	}
	public void setNota(int nota) {
		this.nota = nota;
	}
	public Jugador getAutor() {
		return autor;
	}
	public void setAutor(Jugador autor) {
		this.autor = autor;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	@Override
	public String toString() {
		return "Comentario [id=" + id + ", texto=" + texto + ", autor=" + "]";
	}
	
}
