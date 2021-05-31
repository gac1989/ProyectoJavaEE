package com.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import com.model.Usuario;
import com.model.JPAUtil;

public class UsuarioDAO {
	
	EntityManagerFactory factory = JPAUtil.getEntityManagerFactory();
	EntityManager entity = factory.createEntityManager();

	// guardar Usuario
	public void guardar(Usuario Usuario) {
		entity.getTransaction().begin();
		entity.persist(Usuario);
		entity.getTransaction().commit();
		//JPAUtil.shutdown();
	}

	// editar Usuario
	public void editar(Usuario Usuario) {
		entity.getTransaction().begin();
		entity.merge(Usuario);
		entity.getTransaction().commit();
		/// JPAUtil.shutdown();
	}

	// buscar Usuario
	public Usuario buscar(String nick) {
		Usuario c = new Usuario();
		c = entity.find(Usuario.class, nick);
		return c;
	}

	/// eliminar Usuario
	public void eliminar(String nick) {
		Usuario c = new Usuario();
		c = entity.find(Usuario.class, nick);
		entity.getTransaction().begin();
		entity.remove(c);
		entity.getTransaction().commit();
	}

	/// eliminar Usuario
	public void agregarJuego(Usuario u) {
		entity.getTransaction().begin();
		entity.persist(u);
		entity.getTransaction().commit();
	}

	
	// obtener todos los Usuario
	public List<Usuario> obtenerUsuarios() {
		entity.getTransaction().begin();
		List<Usuario> listaUsuarios = new ArrayList<Usuario>();
		Query q = entity.createQuery("SELECT c FROM Usuario c");
		listaUsuarios = q.getResultList();
		entity.getTransaction().commit();
		return listaUsuarios;
	}
	
	public void cerrar() {
		entity.close();
	}
	
}
