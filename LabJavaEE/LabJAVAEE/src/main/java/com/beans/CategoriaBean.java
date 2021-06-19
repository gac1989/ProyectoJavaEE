package com.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.model.Categoria;
import com.model.Juego;
import com.utils.ClientControl;
import com.utils.GsonHelper;

@ManagedBean(name = "CategoriaBean")
@RequestScoped
public class CategoriaBean {
	private List<Categoria> categorias = this.obtenerCategorias();
	private List<Juego> juegos = null;
	private String nombreCat = null;
	private String nueva = null;
	private Gson json = GsonHelper.customGson;
	
	
	public String getNueva() {
		return nueva;
	}


	public void setNueva(String nueva) {
		this.nueva = nueva;
	}


	public List<SelectItem> getCategoriasList(){
		 List<SelectItem> retVal = new ArrayList<SelectItem>();
		 if(categorias!=null) {
			 for(Categoria categoria : categorias) {
				 retVal.add(new SelectItem(categoria.getNombre()));
			 }
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
		return juegos;
	}

	public void setJuegos(List<Juego> juegos) {
		this.juegos = juegos;
	}

	public CategoriaBean() {
		// TODO Auto-generated constructor stub
	}
	
	public String listarJuegos(String nombre) {
		return "/faces/listarJuegosCat.xhtml?faces-redirect=true&nombre=" + nombre;
	}
	
	public List<Juego> obtenerJuegosCategoria(String nombre){
		if(juegos==null) {
			String urlRestService = "http://localhost:8080/rest-lab/api/recursos/juegoscategoria/" + nombre;
	        Response response = new ClientControl().realizarPeticion(urlRestService, "GET", null);
	        String response2 = response.readEntity(String.class);
	        Juego[] u = null;
	        if(response2 != null && !response2.isEmpty()) {
	        	u = json.fromJson(response2, Juego[].class);
	        }
	        List<Juego> datos = null;
	        if(u!=null) {
	        	 datos = Arrays.asList(u);
	        }
	        juegos=datos;
		}
		return juegos;
	}
	
	public List<Categoria> obtenerCategorias() {
		String urlRestService = "http://localhost:8080/rest-lab/api/recursos/categorias";
        Response response = new ClientControl().realizarPeticion(urlRestService, "GET", null);
        String response2 = response.readEntity(String.class);
        Categoria[] u = new Gson().fromJson(response2, Categoria[].class);
        List<Categoria> datos=null;
        if(u!=null) {
        	datos = Arrays.asList(u);
        }
        return datos;
	}

	public String crearCategoria(String nombre) {
		String urlRestService = "http://localhost:8080/rest-lab/api/recursos/crearCategoria";
		Form form = new Form();
        form.param("nombre", nombre);
        new ClientControl().realizarPeticion(urlRestService, "POST", form);
        return "/faces/Admin/categoria.xhtml?faces-redirect=true";
	}
	
	public String editarCategoria(String nombre, String nuevoNombre) {
		String urlRestService = "http://localhost:8080/rest-lab/api/recursos/editarCategoria";
		Form form = new Form();
        form.param("nombre", nombre);
        form.param("nombreNuevo", nuevoNombre);
        new ClientControl().realizarPeticion(urlRestService, "POST", form);
        return "/faces/Admin/categoria.xhtml";
	}
	
	public String eliminarCategoria(String nombre) {
		String urlRestService = "http://localhost:8080/rest-lab/api/recursos/eliminarCategoria";
		Form form = new Form();
        form.param("nombre", nombre);
        new ClientControl().realizarPeticion(urlRestService, "POST", form);
        return "/faces/Admin/categoria.xhtml?faces-redirect=true";
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
