package com.model;



import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;


@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@XmlRootElement
public abstract class Usuario implements Serializable {
	@Id
	private String nick;
	@Column
	@JsonIgnore
	private String password;
	@Column
	private String email;
	@Lob
	private byte[] imagen;
	@OneToMany(mappedBy = "user", cascade=CascadeType.ALL)
	@JsonBackReference
	private List<Publicacion> publicaciones;
	
	public void agregarPublicacion(Publicacion p1) {
		this.publicaciones.add(p1);
	}
	
	public List<Publicacion> getPublicaciones() {
		return publicaciones;
	}

	public void setPublicaciones(List<Publicacion> publicaciones) {
		this.publicaciones = publicaciones;
	}

	public byte[] getImagen() {
		return imagen;
	}

	public void setImagen(byte[] imagen) {
		this.imagen = imagen;
	}

	public String getNick() {
		return nick;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + nick + 
				", email=" + email + "]";
	}
	
}
