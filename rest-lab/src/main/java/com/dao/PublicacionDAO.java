package com.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import com.model.Publicacion;
import com.model.Desarrollador;
import com.model.JPAUtil;
import com.model.Juego;

public class PublicacionDAO {
	EntityManager entity = JPAUtil.getEntityManagerFactory().createEntityManager();
	EntityTransaction trans = entity.getTransaction();
	// guardar Publicacion
	public void guardar(Publicacion Publicacion) {
		entity.getTransaction().begin();
		entity.persist(Publicacion);
		entity.getTransaction().commit();
		//JPAUtil.shutdown();
	}

	// editar Publicacion
	public void editar(Publicacion Publicacion) {
		entity.getTransaction().begin();
		entity.merge(Publicacion);
		entity.getTransaction().commit();
		/// JPAUtil.shutdown();
	}

	// buscar Publicacion
	public Publicacion buscar(int nick) {
		Publicacion c = new Publicacion();
		c = entity.find(Publicacion.class, nick);
		// JPAUtil.shutdown();
		return c;
	}

	/// eliminar Publicacion
	public void eliminar(int nick) {
		Publicacion c = new Publicacion();
		c = entity.find(Publicacion.class, nick);
		entity.getTransaction().begin();
		entity.remove(c);
		entity.getTransaction().commit();
	}

	// obtener todos los Publicacion
	public List<Publicacion> obtenerPublicacions() {
		List<Publicacion> listaPublicacions = new ArrayList<Publicacion>();
		Query q = entity.createQuery("SELECT c FROM Publicacion c");
		listaPublicacions = q.getResultList();
		entity.close();
		return listaPublicacions;
	}
	
	public void cerrar() {
		entity.close();
	}

}
