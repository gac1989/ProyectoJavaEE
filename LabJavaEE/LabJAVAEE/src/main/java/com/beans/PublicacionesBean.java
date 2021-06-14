package com.beans;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.model.Desarrollador;
import com.model.Jugador;
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
	
	public String mostrarPerfil(String nick) {
		String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/usuario/" + nick;
		Client client = ClientBuilder.newClient();
		WebTarget target= client.target(urlRestService);
		Response response = target.request().get();
        String response2 = response.readEntity(String.class);
        if(response2!=null && !response2.isEmpty()) {
        	Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        	JsonObject convertedObject = new Gson().fromJson(response2, JsonObject.class);
        	JsonElement type = convertedObject.get("pais");
        	if(type!=null) {
        		Desarrollador d1 = new Gson().fromJson(response2, Desarrollador.class);
     			d1.setType("desarrollador");
     			sessionMap.put("visitado", d1);
     			return "/faces/perfilVisitanteDev?faces-redirect=true";
        	}
        	else {
        		Jugador j1 = new Gson().fromJson(response2, Jugador.class);
     			j1.setType("desarrollador");
     			sessionMap.put("visitado", j1);
     			return "/faces/perfilVisitantePlay?faces-redirect=true";
        	}
        }
        return "";
	}
	
}
