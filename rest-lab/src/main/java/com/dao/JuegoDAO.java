package com.dao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletContext;

import org.primefaces.model.file.UploadedFile;

import com.model.JPAUtil;
import com.model.Juego;

public class JuegoDAO {

	EntityManager entity = JPAUtil.getEntityManagerFactory().createEntityManager();
	
	// guardar juego
	public void guardar(Juego juego) {
		entity.getTransaction().begin();
		entity.persist(juego);
		entity.getTransaction().commit();
		//JPAUtil.shutdown();
	}

	// editar juego
	public void editar(Juego juego) {
		entity.getTransaction().begin();
		entity.merge(juego);
		entity.getTransaction().commit();
		/// JPAUtil.shutdown();
	}

	// buscar juego
	public Juego buscar(int id) {
		Juego j = new Juego();
		j = entity.find(Juego.class, id);
		// JPAUtil.shutdown();
		return j;
	}

	/// eliminar juego
	public void eliminar(int id) {
		Juego j = new Juego();
		j = entity.find(Juego.class, id);
		entity.getTransaction().begin();
		entity.remove(j);
		entity.getTransaction().commit();
	}

	// obtener todos los juegos
	public List<Juego> obtenerJuegos() {
		List<Juego> listaJuegos = new ArrayList<>();
		Query q = entity.createQuery("SELECT j FROM Juego j");
		listaJuegos = q.getResultList();
		return listaJuegos;
	}
	
	public List<Juego> buscarJuegos(String busqueda){
		List<Juego> listaJuegos = new ArrayList<>();
		System.out.println("La busqueda es " + busqueda);
		Query q = entity.createQuery("SELECT j FROM Juego j WHERE j.nombre like '%" + busqueda + "%'");
		listaJuegos = q.getResultList();
		return listaJuegos;
	}
	
	public void copiarImagenServidor(UploadedFile file) {
		//String ubicaciooImagen = null;
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		//String path = servletContext.getRealPath("") + "resources" + File.separatorChar + "img" + File.separatorChar + file.getFileName();
		//String path = servletContext.getContextPath();
		String path= "C:\\Users\\Surface\\Desktop\\pablo\\tecnologo\\quinto semestre\\Taller Java\\Laboratorio\\ProyectoJavaEE-Bruno\\rest-lab\\src\\main\\webapp\\img";
		File archivo = new File(path,file.getFileName());
		try {
			Files.copy(file.getInputStream(),archivo.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(path);
	}
	
	public void cerrar() {
		entity.close();
	}
}

