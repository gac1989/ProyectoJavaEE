package com.beans;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.servlet.http.HttpSession;

import com.login.SessionUtils;

@ManagedBean(name = "Error")
@ApplicationScoped

public class Error {
	
	private String ruta = "/faces/login.xhtml";
	
	
	
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
			System.out.println("LINDOOOOOOO ");
			return "/faces/login.xhtml";
		}
		else {
			return "faces/index.xhtml";
		}
	}
}
