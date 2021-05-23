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
		System.out.println("ERRRRORRRR ");
		HttpSession session = SessionUtils.getSession();
		if(session==null) {
			System.out.println("NULOOOOOOOO");
			return null;
		}
		String type = (String)session.getAttribute("type");
		System.out.println("El tipo en el error es: " + type);
		
		if(type==null || type.equals("visitante")) {
			if(type==null) {
				session.setAttribute("type", "visitante");
			}
			System.out.println("LINDOOOOOOO ");
			return "login";
		}
		else {
			if(type.equals("administrador")) {
				return "/faces/Admin/admin.xhtml";
			}
			System.out.println("LINDOOOOOOO 222");
			return "index";
		}
	}
}
