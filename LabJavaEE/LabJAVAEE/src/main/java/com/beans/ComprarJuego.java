package com.beans;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.login.SessionUtils;
import com.model.Juego;

@ManagedBean(name = "ComprarJuego", eager = true)
@RequestScoped
public class ComprarJuego implements Serializable{
	
	private static final long serialVersionUID = 5443351151396868724L;
	
	public String comprar(String idjuego) {
		String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/comprarjuego";
		HttpSession session = SessionUtils.getSession();
		Client client = ClientBuilder.newClient();
		WebTarget target= client.target(urlRestService);
		String nick = (String)session.getAttribute("username");
		System.out.println("El id del juego es: " + idjuego+ " El nick es: " + nick);
		Form form = new Form();
        form.param("nick", nick);
        form.param("id", idjuego);
        Response response = target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
        System.out.println("LA RESPUESTA ES: " + response.getStatus());
        return "/faces/index.xhtml";
	}
}
