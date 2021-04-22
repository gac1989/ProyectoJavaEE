package com.tallerjava.controller;

import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.file.UploadedFile;

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
		
		JuegoDAO juegoDAO= new JuegoDAO();
		juegoDAO.copiarImagenServidor(file);
		juegoDAO.guardar(juego);
		return  "/faces/index.xhtml";
	}
	
	public List<Juego> obtenerJuegos(){
		JuegoDAO juegoDAO= new JuegoDAO();
		return juegoDAO.obtenerJuegos();
	}
	
	public List<Juego> buscadorJuego(String busqueda){
		JuegoDAO juegoDAO= new JuegoDAO();
		return juegoDAO.buscarJuegos(busqueda);
	}
	
	public String listarJuegos() {
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.put("juegos",juegos);
		return "/faces/paginasJuegos/listarJuego.xhtml";
	}
	
	public String resultadoBusqueda() {
		this.juegos=this.buscadorJuego(busqueda);
		return "/faces/paginasJuegos/listarJuego.xhtml";
	}
	
	public String buscarJuego(int id) {
		JuegoDAO juegoDAO= new JuegoDAO();
		Juego j = juegoDAO.buscar(id);
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.put("juego",j);
		return "/faces/paginasJuegos/comprarJuego.xhtml";
	}
	

}
