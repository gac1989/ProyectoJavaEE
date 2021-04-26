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
	
	public String paypalExito() {
		return "http://localhost:8080/demo_lab/";
	}
	
	public String paypalCancel() {
		return "http://localhost:8080/demo_lab/";
	}
}
