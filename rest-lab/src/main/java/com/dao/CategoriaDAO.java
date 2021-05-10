package com.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.model.JPAUtil;
import com.model.Categoria;

public class CategoriaDAO {
	EntityManager entity = JPAUtil.getEntityManagerFactory().createEntityManager();


	// guardar Categoria
	public void guardar(Categoria Categoria) {
		entity.getTransaction().begin();
		entity.persist(Categoria);
		entity.getTransaction().commit();
		//JPAUtil.shutdown();
	}

	// editar Categoria
	public void editar(Categoria Categoria) {
		entity.getTransaction().begin();
		entity.merge(Categoria);
		entity.getTransaction().commit();
		/// JPAUtil.shutdown();
	}

	// buscar Categoria
	public Categoria buscar(String nick) {
		Categoria c = new Categoria();
		c = entity.find(Categoria.class, nick);
		// JPAUtil.shutdown();
		return c;
	}

	/// eliminar Categoria
	public void eliminar(String nick) {
		Categoria c = new Categoria();
		c = entity.find(Categoria.class, nick);
		entity.getTransaction().begin();
		entity.remove(c);
		entity.getTransaction().commit();
	}

	/// eliminar Categoria
		public void agregarJuego(Categoria u) {
			entity.getTransaction().begin();
			entity.persist(u);
			entity.getTransaction().commit();
		}

	
	// obtener todos los Categoria
	public List<Categoria> obtenerCategorias() {
		List<Categoria> listaCategorias = new ArrayList<Categoria>();
		Query q = entity.createQuery("SELECT c FROM Categoria c");
		listaCategorias = q.getResultList();
		return listaCategorias;
	}

}
