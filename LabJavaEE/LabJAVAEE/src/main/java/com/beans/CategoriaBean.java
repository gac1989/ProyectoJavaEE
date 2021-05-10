package com.beans;

import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.model.Categoria;

@ManagedBean(name = "CategoriaBean")
@SessionScoped
public class CategoriaBean {
	private List<Categoria> categorias = this.obtenerCategorias();

	public CategoriaBean() {
		// TODO Auto-generated constructor stub
	}

	public List<Categoria> obtenerCategorias() {
		
		String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/categorias";
		Client client = ClientBuilder.newClient();
		WebTarget target= client.target(urlRestService);
        Response response = target.request().get();
        String response2 = response.readEntity(String.class);
        Categoria[] u = new Gson().fromJson(response2, Categoria[].class);
        List<Categoria> datos = Arrays.asList(u);
		System.out.println("IMPRESION ");
        return datos;
	}

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}
	
	public String mostrarCategorias() {

		return  "/faces/mostrarCategorias.xhtml";
	}
}
