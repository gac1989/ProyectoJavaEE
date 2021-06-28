package com.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.model.JPAUtil;
import com.model.Evento;

public class EventoDAO {
	EntityManager entity = JPAUtil.getEntityManagerFactory().createEntityManager();


	// guardar Evento
	public void guardar(Evento evento) {
		entity.getTransaction().begin();
		entity.persist(evento);
		entity.getTransaction().commit();
		//JPAUtil.shutdown();
	}

	// editar Evento
	public void editar(Evento evento) {
		entity.getTransaction().begin();
		entity.merge(evento);
		entity.getTransaction().commit();
		/// JPAUtil.shutdown();
	}

	// buscar Evento
	public Evento buscar(String nick) {
		Evento c = new Evento();
		c = entity.find(Evento.class, nick);
		// JPAUtil.shutdown();
		return c;
	}

	/// eliminar Evento
	public void eliminar(String nick) {
		Evento c = new Evento();
		c = entity.find(Evento.class, nick);
		entity.getTransaction().begin();
		entity.remove(c);
		entity.getTransaction().commit();
	}

	/// eliminar Evento
		public void agregarJuego(Evento u) {
			entity.getTransaction().begin();
			entity.persist(u);
			entity.getTransaction().commit();
		}

	
	// obtener todos los Evento
	public List<Evento> obtenerEventos() {
		List<Evento> listaEventos = new ArrayList<Evento>();
		Query q = entity.createQuery("SELECT c FROM Evento c");
		listaEventos = q.getResultList();
		return listaEventos;
	}
	
	public void cerrar() {
		entity.close();
	}

}
