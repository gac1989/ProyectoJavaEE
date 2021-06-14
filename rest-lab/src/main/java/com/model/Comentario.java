package com.model;


import javax.persistence.*;



@Entity
@Table(name="comentario")
public class Comentario {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@Column
	private String texto;
	@Column
	private int nota;
	@Enumerated(EnumType.STRING)
	private Estado estado;
	@ManyToOne
	@JoinColumn(name="nick", foreignKey = @ForeignKey(name = "nick"))
	private Jugador autor;
	
	
	
	public Estado getEstado() {
		return estado;
	}
	public void setEstado(Estado estado) {
		this.estado = estado;
	}
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
