package com.beans;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.servlet.http.HttpSession;

import com.login.SessionUtils;

@ManagedBean(name = "Error")
@RequestScoped

public class Error {
	
	private String ruta = "/faces/login.xhtml";
	private String rutacorrecta = this.getRutaBoton();
	
	
	
	public String getRutacorrecta() {
		if(rutacorrecta==null) {
			rutacorrecta = this.getRutaBoton();
		}
		return rutacorrecta;
	}


	public void setRutacorrecta(String rutacorrecta) {
		this.rutacorrecta = rutacorrecta;
	}


	public void setRuta(String ruta) {
		this.ruta = ruta;
	}


	public String getRuta() {
		return ruta;
	}

	public String getRutaBoton() {
		HttpSession session = SessionUtils.getSession();
		if(session==null) {
			return null;
		}
		String type = (String)session.getAttribute("type");
		
		if(type==null || type.equals("visitante")) {
			if(type==null) {
				session.setAttribute("type", "visitante");
			}
			return "login";
		}
		else {
			if(type.equals("administrador")) {
				return "/faces/Admin/admin.xhtml";
			}
			return "index";
		}
	}
}
