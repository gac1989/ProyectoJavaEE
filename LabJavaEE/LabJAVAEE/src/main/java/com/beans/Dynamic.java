package com.beans;


import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
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
import com.model.Venta;


@ManagedBean(name = "Dynamic")
@RequestScoped
public class Dynamic {

	
	private int juego=0;
	private List<Venta> ventasjuego = obtenerVentasJuego();
	
	
	public int getJuego() {
		return juego;
	}

	public void setJuego(int juego) {
		this.juego = juego;
	}

	public List<Venta> getVentasjuego() {
		return ventasjuego;
	}

	public void setVentasjuego(List<Venta> ventasjuego) {
		this.ventasjuego = ventasjuego;
	}


	public List<Venta> obtenerVentasJuego(){
		if(juego!=0 && this.ventasjuego==null) {
			String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/ventasjuego";
			HttpSession session = SessionUtils.getSession();
			String nick = (String)session.getAttribute("username");
			Client client = ClientBuilder.newClient();
			WebTarget target= client.target(urlRestService);
			Form form = new Form();
	        form.param("juego", String.valueOf(juego));
	        form.param("nick", nick);
	        Response response = target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
	        String response2 = response.readEntity(String.class);
	        Venta[] u = new Gson().fromJson(response2, Venta[].class);
	        List<Venta> datos=null;
	        if(u!=null) {
	        	datos = Arrays.asList(u);
	        }
	        System.out.println("La respuesta es:  " + response.getStatus());
	        this.ventasjuego=datos;
			return datos;
		}
		return ventasjuego;
	}
}
