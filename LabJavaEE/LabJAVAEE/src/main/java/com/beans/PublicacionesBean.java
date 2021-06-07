package com.beans;

import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.model.Publicacion;
import com.model.Usuario;

@ManagedBean(name = "PublicacionBean", eager = true)
@RequestScoped
public class PublicacionesBean {

private List<Publicacion> publicaciones = null;
	
	public List<Publicacion> getPublicaciones() {
		return publicaciones;
	}

	public void setPublicaciones(List<Publicacion> publicaciones) {
		this.publicaciones = publicaciones;
	}
	
	public List<Publicacion> obtenerPublicaciones(Usuario user){
		if(this.publicaciones==null) {
			List<Publicacion> datos = null;
			String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/publicaciones/"+user.getNick();
			Client client = ClientBuilder.newClient();
			WebTarget target= client.target(urlRestService);
			Response response = target.request().get();
	        String response2 = response.readEntity(String.class);
	        Publicacion[] u = null;
	        if(response2!=null && !response2.isEmpty()) {
	        	u = new Gson().fromJson(response2, Publicacion[].class);
	        }
	        
	        if(u!=null) {
	        	datos=Arrays.asList(u);
	        }
	        publicaciones=datos;
		}
		return publicaciones;
	}
	
}
