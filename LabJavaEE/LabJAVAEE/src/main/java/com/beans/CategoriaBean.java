package com.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.model.Categoria;
import com.model.Juego;

@ManagedBean(name = "CategoriaBean")
@SessionScoped
public class CategoriaBean {
	private List<Categoria> categorias = this.obtenerCategorias();
	private List<Juego> juegos = null;
	private String nombreCat = null;
	private String nueva = null;

	
	
	public String getNueva() {
		return nueva;
	}


	public void setNueva(String nueva) {
		this.nueva = nueva;
	}


	public List<SelectItem> getCategoriasList(){
		 List<SelectItem> retVal = new ArrayList<SelectItem>();
		 for(Categoria categoria : categorias) {
			 retVal.add(new SelectItem(categoria.getNombre()));
		 }
		 return retVal;
	}
	
	
	public String getNombreCat() {
		return nombreCat;
	}

	public void setNombreCat(String nombreCat) {
		this.nombreCat = nombreCat;
	}

	public List<Juego> getJuegos() {
		if(juegos==null) {
			juegos=this.obtenerJuegosCategoria(nombreCat);
		}
		return juegos;
	}

	public void setJuegos(List<Juego> juegos) {
		this.juegos = juegos;
	}

	public CategoriaBean() {
		// TODO Auto-generated constructor stub
	}
	
	public String listarJuegos(String nombre) {
		this.juegos = this.obtenerJuegosCategoria(nombre);
		return "/faces/listarJuegosCat.xhtml";
	}
	
	public List<Juego> obtenerJuegosCategoria(String nombre){
		String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/juegoscategoria/" + nombre;
		Client client = ClientBuilder.newClient();
		WebTarget target= client.target(urlRestService);
        Response response = target.request().get();
        String response2 = response.readEntity(String.class);
        System.out.println("La respuesta es: " + response2);
        Juego[] u = null;
        if(response2 != null && !response2.isEmpty()) {
        	u = new Gson().fromJson(response2, Juego[].class);
        }
        List<Juego> datos = null;
        if(u!=null) {
        	 datos = Arrays.asList(u);
        }
        return datos;
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

	public String crearCategoria(String nombre) {
		String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/crearCategoria";
		Client client = ClientBuilder.newClient();
		WebTarget target= client.target(urlRestService);
		Form form = new Form();
        form.param("nombre", nombre);
        Response response = target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
        String response2 = response.readEntity(String.class);
        System.out.println("La respuesta es:  " + response2);
        return "/faces/Admin/admin.xhtml";
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
