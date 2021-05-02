package com.model;


public class Desarrollador extends Usuario {
	private String nombre_empresa;
	private String pais;
	private String direccion;
	private String telefono;
	
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
