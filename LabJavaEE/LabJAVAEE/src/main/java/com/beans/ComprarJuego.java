package com.beans;

import java.io.IOException;
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

@ManagedBean(name = "ComprarJuego")
@SessionScoped
public class ComprarJuego implements Serializable{
	
	private static final long serialVersionUID = 5443351151396868724L;
	private Juego j = null;
	
	
	public Juego getJ() {
		return j;
	}

	public void setJ(Juego j) {
		this.j = j;
	}

	public static String comprar(String idjuego, String nick) throws IOException {
		String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/comprarjuego";
		Client client = ClientBuilder.newClient();
		WebTarget target= client.target(urlRestService);
		System.out.println("El id del juego es: " + idjuego+ " El nick es: " + nick);
		Form form = new Form();
        form.param("nick", nick);
        form.param("id", idjuego);
        Response response = target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
        System.out.println("LA RESPUESTA ES: " + response.getStatus());
        return "";
	}
	
	public String buscarJuego(Juego j) {
		System.out.println("El identificador es: " + j.getId());
		System.out.println("El juego es:  " + j.getNombre());
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.put("juego",j);
		return "/faces/comprar.xhtml?faces-redirect=true&nombreJuego=" + j.getNombre() + "&precioJuego=" + j.getPrecio() + "&id=" + j.getId();
	}
}
