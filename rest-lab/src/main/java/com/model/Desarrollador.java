package com.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name="desarrollador")
@PrimaryKeyJoinColumn(name="nick")
public class Desarrollador extends Usuario {
	@Column
	private String nombre_empresa;
	@Column
	private String pais;
	@Column
	private String direccion;
	@Column
	private String telefono;
	@OneToMany(mappedBy = "desarrollador",cascade=CascadeType.ALL)
	@JsonBackReference
	private List<Juego> juegos;
	
	
	
	public Juego obtenerJuego(int id) {
		for(Juego j  : this.juegos) {
			if(j.getId()==id) {
				return j;
			}
		}
		return null;
	}

	public void reemplazarJuego(int index, Juego j) {
		this.juegos.set(index, j);
	}
	
	public List<Juego> getJuegos() {
		return juegos;
	}
	public void setJuegos(List<Juego> juegos) {
		this.juegos = juegos;
	}
	
	public void agregarJuego(Juego j) {
		if(j!=null) {
			this.juegos.add(j);
			j.setDesarrollador(this);
		}
	}
	
	public String getNombre_empresa() {
		return nombre_empresa;
	}
	public void setNombre_empresa(String nombre_empresa) {
		this.nombre_empresa = nombre_empresa;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	@Override
	public String toString() {
		return "Desarrollador [nombre_empresa=" + nombre_empresa + ", pais=" + pais + ", direccion=" + direccion
				+ ", telefono=" + telefono + "]";
	}
	
}
