package com.steamdindie.curcriticos.controladores;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import com.steamindie.cucriticos.clases.Game;

public class ControladorJuegos {
	
	private List<Game> listaJuegos;
	private EntityManager em;
	
	public ControladorJuegos() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.steamindie_CUCriticos_war_1.0-SNAPSHOTPU"); 
        this.em = emf.createEntityManager();
	}
	
	public List<Game> listarJuegos() {
		
		TypedQuery<Game> query = em.createQuery("SELECT g FROM Game g", Game.class);
		
		return query.getResultList();
	}
	
	public Game buscarJuego(int Id) {
		
		return em.createNamedQuery("Game.findById", Game.class).setParameter("id", Id).getSingleResult();
	}
	
	public void persistirJuego(Game game) {
		em.getTransaction().begin();
        em.persist(game);
        em.getTransaction().commit();
	}
	
	public List<Game> buscarJuegos(String busqueda){
		
		TypedQuery<Game> query = em.createQuery("SELECT g FROM Game g WHERE g.nombre like '%" + busqueda + "%' ", Game.class);
		
		return query.getResultList();
	}
}
