package com.model;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

public class UsuarioDAO {
	Client client = ClientBuilder.newClient();
	
	public List<Usuario> obtenerUsuarios(){
		String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/usuarios";
		WebTarget target= client.target(urlRestService);
		Response response = target.request().get();
		String response2 = response.readEntity(String.class);
        Usuario[] u = null;
        if(response2!=null && !response2.equals("")) {
        	u = new Gson().fromJson(response2, Usuario[].class);
        }
        List<Usuario> datos = null;
        if(u!=null) {
        	datos = Arrays.asList(u);
        }
		System.out.println("TODO OK ");
        return datos;
	}
	
}
