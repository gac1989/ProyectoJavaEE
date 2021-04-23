package com.tallerjava.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.primefaces.model.file.UploadedFile;

import com.google.gson.Gson;
import com.tallerjava.dao.JuegoDAO;
import com.tallerjava.model.Juego;


@ManagedBean(name = "juegoBean")
@RequestScoped
public class JuegoBean {

	private List<Juego> juegos = this.obtenerJuegos();
	private UploadedFile file;
	private String busqueda;

	
	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public List<Juego> getJuegos() {
		return juegos;
	}

	public void setJuegos(List<Juego> juegos) {
		this.juegos = juegos;
	}
	
	

	public String getBusqueda() {
		return busqueda;
	}

	public void setBusqueda(String busqueda) {
		this.busqueda = busqueda;
	}


	public String nuevo() {
		Juego j= new Juego();
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.put("juego", j);
		return  "/faces/paginasJuegos/nuevoJuego.xhtml";
	}
	
	public String guardar (Juego juego) {
		
		if (file != null) {
            String message = "Successful " + file.getFileName() + " is uploaded.";
            System.out.println(message);
            juego.setRutaImg(file.getFileName());
        }
		
		String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/registrarJuego";
		Client client = ClientBuilder.newClient();
        Form form = new Form();
        form.param("nombre", juego.getNombre());
        form.param("descripcion", juego.getDescripcion());
        form.param("rutaImg", juego.getRutaImg());
        form.param("precio", String.valueOf(juego.getPrecio()));
        WebTarget target= client.target(urlRestService);
        Response response = target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
        String response2 = response.readEntity(String.class);
		return  "/faces/index.xhtml";
	}
	/*	
	public List<Juego> obtenerJuegos(){
		JuegoDAO juegoDAO= new JuegoDAO();
		return juegoDAO.obtenerJuegos();
	}*/
	
	public List<Juego> obtenerJuegos(){
		String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/obtenerJuegos";
		Client client = ClientBuilder.newClient();
		WebTarget target= client.target(urlRestService);
        Response response = target.request().get();
        String response2 = response.readEntity(String.class);
        Juego[] j = new Gson().fromJson(response2, Juego[].class);
        List<Juego> datos = Arrays.asList(j);
        return datos;
	}
	/*
	public List<Juego> buscadorJuego(String busqueda){
		JuegoDAO juegoDAO= new JuegoDAO();
		return juegoDAO.buscarJuegos(busqueda);
	}*/
	
	public List<Juego> buscadorJuego(String busqueda){
		System.out.println("Estoy en buscadorJuego() " + busqueda);	
		String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/buscadorJuegos/" + busqueda;
		Client client = ClientBuilder.newClient();
		WebTarget target= client.target(urlRestService);
        Response response = target.request().get();
        String response2 = response.readEntity(String.class);
        Juego[] j = new Gson().fromJson(response2, Juego[].class);
		List<Juego> datos = Arrays.asList(j);
		return datos;
	}
	
	public String listarJuegos() {
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.put("juegos",juegos);
		return "/faces/paginasJuegos/listarJuego.xhtml";
	}
	
	public String resultadoBusqueda() {
		System.out.println("Estoy en resultadoBusqueda() " + busqueda);
		this.juegos=this.buscadorJuego(busqueda);
		return "/faces/paginasJuegos/listarJuego.xhtml";
	}
	
	/*
	public String buscarJuego(int id) {
		JuegoDAO juegoDAO= new JuegoDAO();
		Juego j = juegoDAO.buscar(id);
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.put("juego",j);
		return "/faces/paginasJuegos/comprarJuego.xhtml";
	}*/
	
	public String buscarJuego(int id) {
		String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/buscarJuego";
		Client client = ClientBuilder.newClient();
        Form form = new Form();
        form.param("id", String.valueOf(id));
        WebTarget target= client.target(urlRestService);
        Response response = target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
        String response2 = response.readEntity(String.class);
        Juego j = new Gson().fromJson(response2, Juego.class);
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.put("juego",j);
		return "/faces/paginasJuegos/comprarJuego.xhtml";
	}
	
}
