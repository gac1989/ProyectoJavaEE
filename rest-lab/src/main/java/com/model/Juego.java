package com.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;


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
	private boolean desbloqueo;
	@Column
	private String trailer;
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
	@JsonManagedReference
	private Desarrollador desarrollador;
	@OneToMany(mappedBy = "juego")
	@JsonBackReference
	private List<CompraJuego> ventas;
	@Enumerated(EnumType.STRING)
	private Estado estado;
    @ManyToMany(mappedBy="juegos")
    @JsonBackReference
    private List<Categoria> categorias = new ArrayList<Categoria>();
	@Lob
    private byte[] imagen;
    @Column
    private String tags;
    @OneToMany(mappedBy = "juego", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JsonBackReference
	private List<Imagen> imagenes = new ArrayList<Imagen>();
	
    public List<Imagen> getImagenes() {
		return imagenes;
	}

	public void setImagenes(List<Imagen> imagenes) {
		this.imagenes = imagenes;
	}
	
	public void agregarImagen(Imagen i1) {
		this.imagenes.add(i1);
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public byte[] getImagen() {
		return imagen;
	}

	public void setImagen(byte[] imagen) {
		this.imagen = imagen;
	}

	public void agregarCategoria(Categoria c1) {
    	this.categorias.add(c1);
    }
    
	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}

	public String getTrailer() {
		return trailer;
	}

	public void setTrailer(String trailer) {
		this.trailer = trailer;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

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

	public boolean isDesbloqueo() {
		return desbloqueo;
	}

	public void setDesbloqueo(boolean desbloqueo) {
		this.desbloqueo = desbloqueo;
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
	
	private Comentario replaceComentario(Comentario c1) {
		for(Iterator<Comentario> featureIterator = comentarios.iterator(); 
		    featureIterator.hasNext(); ) {
		    Comentario feature = featureIterator.next();
		    if(feature.getAutor().getNick().equals(c1.getAutor().getNick())) {
		    	System.out.println("El autor ya hizo un comentario");
		    	return feature;
		    }
		}
		return null;
	}
	
	public void agregarComentario(Comentario c1) {
		Comentario valor = replaceComentario(c1);
		if(valor!=null) {
			valor.setNota(c1.getNota());
			valor.setTexto(c1.getTexto());
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
		return "Juego [nombre=" + nombre + ", descripcion=" + descripcion + ", " + ", precio="
				+ precio + "]";
	}
	
	
}
