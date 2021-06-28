package com.utils;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

public class ClientControl {

	private Client client = ClientBuilder.newClient();
	
	public Response realizarPeticion(String url, String type,Form datos) {
		WebTarget target= client.target(url);
		if(type.equals("GET")) {
			return target.request().get();
		}
		else {
			return target.request().post(Entity.entity(datos, MediaType.APPLICATION_FORM_URLENCODED));
		}
	}
	
	public Response realizarPeticionMultiple(String url, MultipartFormDataOutput datos) {
		WebTarget target= client.target(url);
		GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(datos) { };
 		return target.request().post(Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE));
	}
	
	
	
}
