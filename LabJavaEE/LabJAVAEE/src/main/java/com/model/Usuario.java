package com.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="usuario")
public class Usuario {
	@Id
	private String nick;
	@Column
	private String nombres;
	@Column
	private String apellidos;
	@Column
	private String direccion;
	@Column
	private String email;
	@Column
	private String telefono;


	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	

	@Override
	public String toString() {
		return "Cliente [id=" + nick + ", nombres=" + nombres + ", apellidos=" + apellidos + ", direccion=" + direccion
				+ ", email=" + email + ", telefono=" + telefono;
	}
	
}
