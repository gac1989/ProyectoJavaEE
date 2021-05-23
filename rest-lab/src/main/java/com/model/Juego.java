package com.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;


@Entity
@Table(name="juego")
public class Juego {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@Column
	private String nombre;
	@Column
	private String descripcion;
	@Column
	private String rutaImg;
	@Column
	private float precio;
	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinTable(name="comentariojuego", joinColumns = @JoinColumn(name = "juego_id"),
    inverseJoinColumns = @JoinColumn(name = "comentario_id") )
	@JsonBackReference
	private List<Comentario> comentarios;
	@ManyToOne
	@JoinColumn(name="nombreEvento", foreignKey = @ForeignKey(name = "nombre"))
	private Evento evento;
	@Column
	private float nota = 0;
	
	
	public Evento getEvento() {
		return evento;
	}
	public void setEvento(Evento evento) {
		this.evento = evento;
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
	
	public List<Comentario> getComentarios() {
		return comentarios;
	}
	public void setComentarios(List<Comentario> comentarios) {
		this.comentarios = comentarios;
	}
	
	public void agregarComentario(Comentario c1) {
		this.comentarios.add(c1);
		int suma = 0;
		for(Comentario c : this.comentarios) {
			suma = suma + c.getNota();
		}
		this.nota=(float)suma/comentarios.size();
		System.out.println("La nota es: " + this.nota);
	}
	
	
	@Override
	public String toString() {
		return "Juego [nombre=" + nombre + ", descripcion=" + descripcion + ", rutaImg=" + rutaImg + ", precio="
				+ precio + "]";
	}
	
	
}
