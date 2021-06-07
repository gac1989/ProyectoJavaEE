package com.model;



import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;


@Entity
@Table(name="usuario")
@Inheritance(strategy=InheritanceType.JOINED)
@XmlRootElement
public class Usuario {
	@Id
	private String nick;
	@Column
	private String password;
	@Column
	private String email;
	@Column
	private String rutaImg;
	@OneToMany(mappedBy = "user")
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

	public String getRutaImg() {
		return rutaImg;
	}

	public void setRutaImg(String rutaImg) {
		this.rutaImg = rutaImg;
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
		return "Cliente [id=" + nick + 
				", email=" + email;
	}
	
}
