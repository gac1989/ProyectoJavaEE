package com.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.model.JPAUtil;
import com.model.Comentario;
import com.model.Desarrollador;

public class ComentarioDAO {
	EntityManager entity = JPAUtil.getEntityManagerFactory().createEntityManager();


	// guardar Comentario
	public void guardar(Comentario Comentario) {
		entity.getTransaction().begin();
		entity.persist(Comentario);
		entity.getTransaction().commit();
		//JPAUtil.shutdown();
	}

	// editar Comentario
	public void editar(Comentario Comentario) {
		entity.getTransaction().begin();
		entity.merge(Comentario);
		entity.getTransaction().commit();
		/// JPAUtil.shutdown();
	}

	// buscar Comentario
	public Comentario buscar(int id) {
		Comentario c = new Comentario();
		c = entity.find(Comentario.class, id);
		// JPAUtil.shutdown();
		return c;
	}

	/// eliminar Comentario
	public void eliminar(int nick) {
		Comentario c = new Comentario();
		c = entity.find(Comentario.class, nick);
		entity.getTransaction().begin();
		entity.remove(c);
		entity.getTransaction().commit();
	}

	/// eliminar Comentario
		public void agregarJuego(Comentario u) {
			entity.getTransaction().begin();
			entity.persist(u);
			entity.getTransaction().commit();
		}

	public List<Comentario> obtenerComentariosReportados(){
		List<Comentario> lista = null;
		Query q = entity.createQuery("select c from Comentario c where c.estado='REPORTADO' or c.estado='BLOQUEADO'");
		lista =  q.getResultList();
		return lista;
	}
		
	
	// obtener todos los Comentario
	public List<Comentario> obtenerComentarios() {
		List<Comentario> listaComentarios = new ArrayList<Comentario>();
		Query q = entity.createQuery("SELECT c FROM Comentario c");
		listaComentarios = q.getResultList();
		return listaComentarios;
	}

	public void cerrar() {
		entity.close();
	}
}
