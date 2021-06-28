package com.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "enrutador")
@RequestScoped
public class EnrutadorBean {

	private String iniciar = this.inicio();
	
	
	
	public String getIniciar() {
		return iniciar;
	}

	public void setIniciar(String iniciar) {
		this.iniciar = iniciar;
	}

	public String inicio() {
		return  "/faces/index.xhtml";
	}
	
	public void paypalExito(String juego) {
		System.out.println("El juego comprado es: " + juego);
	}
	
	public String confirmar(int idjuego) {
		return "http://localhost:8080/LabJAVAEE/faces/paginasJuegos/confirmar.xhtml?juegoid=" + String.valueOf(idjuego);
	}
	
	public String paypalCancel() {
		System.out.println("ACA TAMBIEN ENTRO SOY UNA MIERDA");
		return "http://localhost:8080/demo_lab/";
	}
}
