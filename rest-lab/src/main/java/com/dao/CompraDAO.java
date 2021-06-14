package com.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.model.JPAUtil;
import com.model.CompraJuego;

public class CompraDAO {
	EntityManager entity = JPAUtil.getEntityManagerFactory().createEntityManager();


	// guardar Compra
	public void guardar(CompraJuego Compra) {
		entity.getTransaction().begin();
		entity.persist(Compra);
		entity.getTransaction().commit();
		//JPAUtil.shutdown();
	}

	// editar Compra
	public void editar(CompraJuego Compra) {
		entity.getTransaction().begin();
		entity.merge(Compra);
		entity.getTransaction().commit();
		/// JPAUtil.shutdown();
	}

	// buscar Compra
	public CompraJuego buscar(String nick) {
		CompraJuego c = new CompraJuego();
		c = entity.find(CompraJuego.class, nick);
		// JPAUtil.shutdown();
		return c;
	}

	/// eliminar Compra
	public void eliminar(String nick) {
		CompraJuego c = new CompraJuego();
		c = entity.find(CompraJuego.class, nick);
		entity.getTransaction().begin();
		entity.remove(c);
		entity.getTransaction().commit();
	}

	/// eliminar Compra
		public void agregarJuego(CompraJuego u) {
			entity.getTransaction().begin();
			entity.persist(u);
			entity.getTransaction().commit();
		}

	
	// obtener todos los Compra
	public List<CompraJuego> obtenerCompras() {
		List<CompraJuego> listaCompras = new ArrayList<CompraJuego>();
		Query q = entity.createQuery("SELECT c FROM Compra c");
		listaCompras = q.getResultList();
		return listaCompras;
	}
	
	public void cerrar() {
		entity.close();
	}

}
