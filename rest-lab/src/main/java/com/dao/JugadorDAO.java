package com.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.model.Jugador;
import com.model.JPAUtil;

public class JugadorDAO {
	EntityManager entity = JPAUtil.getEntityManagerFactory().createEntityManager();

	// guardar Jugador
	public void guardar(Jugador Jugador) {
		entity.getTransaction().begin();
		entity.persist(Jugador);
		entity.getTransaction().commit();
		//JPAUtil.shutdown();
	}

	// editar Jugador
	public void editar(Jugador Jugador) {
		entity.getTransaction().begin();
		entity.merge(Jugador);
		entity.getTransaction().commit();
		/// JPAUtil.shutdown();
	}

	// buscar Jugador
	public Jugador buscar(String nick) {
		Jugador c = new Jugador();
		c = entity.find(Jugador.class, nick);
		// JPAUtil.shutdown();
		return c;
	}

	/// eliminar Jugador
	public void eliminar(String nick) {
		Jugador c = new Jugador();
		c = entity.find(Jugador.class, nick);
		entity.getTransaction().begin();
		entity.remove(c);
		entity.getTransaction().commit();
	}

	/// eliminar Jugador
		public void agregarJuego(Jugador u) {
			entity.getTransaction().begin();
			entity.persist(u);
			entity.getTransaction().commit();
		}

	
	// obtener todos los Jugador
	public List<Jugador> obtenerJugadors() {
		List<Jugador> listaJugadors = new ArrayList<Jugador>();
		Query q = entity.createQuery("SELECT c FROM Jugador c");
		listaJugadors = q.getResultList();
		return listaJugadors;
	}

}
