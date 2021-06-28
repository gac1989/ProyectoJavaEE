package com.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import com.model.Jugador;
import com.model.Desarrollador;
import com.model.JPAUtil;
import com.model.Juego;

public class JugadorDAO {
	EntityManager entity = JPAUtil.getEntityManagerFactory().createEntityManager();
	EntityTransaction trans = entity.getTransaction();
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
		
	public List<Juego> obtenerJuegos(Desarrollador d1){
		List<Juego> juegos = null;
		Query q = entity.createQuery("SELECT j FROM Juego j JOIN j.desarrollador d WHERE j.desarrollador=d.nick ");
		juegos = q.getResultList();
		return juegos;
	}
	
	// obtener todos los Jugador
	public List<Jugador> obtenerJugadors() {
		List<Jugador> listaJugadors = new ArrayList<Jugador>();
		Query q = entity.createQuery("SELECT c FROM Jugador c");
		listaJugadors = q.getResultList();
		entity.close();
		return listaJugadors;
	}
	
	public void cerrar() {
		entity.close();
	}

}
