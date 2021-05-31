package com.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import com.model.Desarrollador;
import com.model.DevStat;
import com.model.JPAUtil;
import com.model.Juego;

public class DesarrolladorDAO {
	EntityManager entity = JPAUtil.getEntityManagerFactory().createEntityManager();
	EntityTransaction trans = entity.getTransaction();
	// guardar Desarrollador
	public void guardar(Desarrollador Desarrollador) {
		entity.getTransaction().begin();
		entity.persist(Desarrollador);
		entity.getTransaction().commit();
		//JPAUtil.shutdown();
	}

	// editar Desarrollador
	public void editar(Desarrollador Desarrollador) {
		entity.getTransaction().begin();
		entity.merge(Desarrollador);
		entity.getTransaction().commit();
		/// JPAUtil.shutdown();
	}

	// buscar Desarrollador
	public Desarrollador buscar(String nick) {
		Desarrollador c = new Desarrollador();
		c = entity.find(Desarrollador.class, nick);
		// JPAUtil.shutdown();
		return c;
	}

	/// eliminar Desarrollador
	public void eliminar(String nick) {
		Desarrollador c = new Desarrollador();
		c = entity.find(Desarrollador.class, nick);
		entity.getTransaction().begin();
		entity.remove(c);
		entity.getTransaction().commit();
	}

	public List<Juego> obtenerJuegos(Desarrollador d1){
		List<Juego> juegos = null;
		Query q = entity.createQuery("SELECT j FROM Juego j JOIN j.desarrollador d WHERE j.desarrollador=d.nick");
		juegos = q.getResultList();
		return juegos;
	}
	

	public double obtenerTotal(Desarrollador d1){
		double total = 0;
		Query q = entity.createQuery("SELECT sum(c.precio) FROM CompraJuego c JOIN c.juego j WHERE j.desarrollador='"+d1.getNick()+"'");
		total = (double) q.getSingleResult();
		System.out.println("El total de ventas es: " + total);
		return total;
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
	
	public List<DevStat> obtenerStats(Desarrollador d1){
		 Query q = entity.createQuery("SELECT j.nombre, sum(c.precio), count(c.juego) FROM Juego j JOIN j.ventas c WHERE j.desarrollador='"+d1.getNick()+"' group by j.nombre");
		 List<Object[]> resultado= (List<Object[]>)q.getResultList();
		 List<DevStat> datos = new ArrayList<DevStat>();
	     for(Object[] dato: resultado){
	    	 DevStat stat = new DevStat();
	    	 stat.setNombre((String)dato[0]);
	    	 double total = (Double)dato[1];
	    	 stat.setTotal(round(total,2));
	    	 stat.setVentas((long)dato[2]);
	    	 datos.add(stat);
	     }
		return datos;
	}
	
	// obtener todos los Desarrollador
	public List<Desarrollador> obtenerDesarrolladores() {
		List<Desarrollador> listaDesarrolladors = new ArrayList<Desarrollador>();
		Query q = entity.createQuery("SELECT c FROM Desarrollador c");
		listaDesarrolladors = q.getResultList();
		entity.close();
		return listaDesarrolladors;
	}
	
	public void cerrar() {
		entity.close();
	}

}
