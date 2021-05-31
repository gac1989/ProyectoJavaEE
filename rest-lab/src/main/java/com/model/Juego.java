package com.model;

import java.util.Iterator;
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
import javax.persistence.OneToMany;
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
	@Column(length = 1000000)
	private String descripcion;
	@Column
	private String rutaImg;
	@Column
	private float precio;
	@Column
	private float oferta;
	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinTable(name="comentariojuego", joinColumns = @JoinColumn(name = "juego_id"),
    inverseJoinColumns = @JoinColumn(name = "comentario_id") )
	@JsonBackReference
	private List<Comentario> comentarios;
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="nombreEvento")
	private Evento evento;
	@Column
	private float nota = 0;
	@ManyToOne
	@JoinColumn(name = "desarrollador_id")
	@JsonBackReference
	private Desarrollador desarrollador;
	@OneToMany(mappedBy = "juego")
	@JsonBackReference
	private List<CompraJuego> ventas;
	 
	
	
	
	public float getOferta() {
		if(evento!=null) {
			this.oferta=this.precio-((this.precio*evento.getDescuento())/100);
		}
		else {
			this.oferta=this.precio;
		}
		return oferta;
	}
	
	public void setOferta(float oferta) {
		this.oferta = oferta;
	}
	public List<CompraJuego> getVentas() {
		return ventas;
	}
	public void setVentas(List<CompraJuego> ventas) {
		this.ventas = ventas;
	}
	public Desarrollador getDesarrollador() {
		return desarrollador;
	}
	public void setDesarrollador(Desarrollador desarrollador) {
		this.desarrollador = desarrollador;
	}
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
	
	private int replaceComentario(Comentario c1) {
		for(Iterator<Comentario> featureIterator = comentarios.iterator(); 
		    featureIterator.hasNext(); ) {
		    Comentario feature = featureIterator.next();
		    if(feature.getAutor().getNick().equals(feature.getAutor().getNick())) {
		    	System.out.println("El autor ya hizo un comentario");
		    	return comentarios.indexOf(feature);
		    }
		}
		return -1;
	}
	
	public void agregarComentario(Comentario c1) {
		int valor = replaceComentario(c1);
		if(valor!=-1) {
			this.comentarios.set(valor, c1);
		}
		else {
			this.comentarios.add(c1);
		}
		int suma = 0;
		for(Comentario c : this.comentarios) {
			suma = suma + c.getNota();
		}
		this.nota=(float)suma/comentarios.size();
		System.out.println("La nota es: " + this.nota);
	}
	
	
	public void agregarVenta(CompraJuego compra) {
		this.ventas.add(compra);
	}
	
	@Override
	public String toString() {
		return "Juego [nombre=" + nombre + ", descripcion=" + descripcion + ", rutaImg=" + rutaImg + ", precio="
				+ precio + "]";
	}
	
	
}
