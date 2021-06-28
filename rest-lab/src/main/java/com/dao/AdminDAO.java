package com.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.model.AdminStats;
import com.model.Administrador;
import com.model.Chart;
import com.model.Desarrollador;
import com.model.DevStat;
import com.model.JPAUtil;
import com.model.Juego;

public class AdminDAO {
	
	
	EntityManager entity = JPAUtil.getEntityManagerFactory().createEntityManager();
    // guardar Administrador
	public void guardar(Administrador Administrador) {
		entity.getTransaction().begin();
		entity.persist(Administrador);
		entity.getTransaction().commit();
		//JPAUtil.shutdown();
	}

	// editar Administrador
	public void editar(Administrador Administrador) {
		entity.getTransaction().begin();
		entity.merge(Administrador);
		entity.getTransaction().commit();
		/// JPAUtil.shutdown();
	}

	// buscar Administrador
	public Administrador buscar(String nick) {
		Administrador c = new Administrador();
		c = entity.find(Administrador.class, nick);
		// JPAUtil.shutdown();
		return c;
	}

	/// eliminar Administrador
	public void eliminar(String nick) {
		Administrador c = new Administrador();
		c = entity.find(Administrador.class, nick);
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
	
	public AdminStats obtenerStats(){
		 AdminStats stats = new AdminStats();
		 Query q = entity.createQuery("SELECT j.nombre, sum(c.precio), count(c.juego) FROM Juego j JOIN j.ventas c  group by j.nombre");
		 List<Object[]> resultado= (List<Object[]>)q.getResultList();
		 double montototal = 0;
		 long ventastotal = 0;
	     for(Object[] dato: resultado){
	    	 double total = DesarrolladorDAO.round((double)dato[1],2);
	    	 long ventas = (long)dato[2];
	    	 montototal+=total;
	    	 ventastotal+=ventas;
	     }
	     montototal = DesarrolladorDAO.round(montototal,2);
	     stats.setTotal(montototal);
	     stats.setTotalventas(ventastotal);
	     stats.setTotalneto(DesarrolladorDAO.round(montototal*0.1,2));
	     return stats;
	}
	
	
	public List<DevStat> obtenerVentasDesarrollador(){
		 Query q = entity.createQuery("SELECT j.desarrollador, sum(c.precio), count(c.juego) FROM Juego j JOIN j.ventas c group by j.desarrollador order by sum(c.precio) desc");
		 List<Object[]> resultado= (List<Object[]>)q.getResultList();
		 List<DevStat> datos = new ArrayList<DevStat>();
	     for(Object[] dato: resultado){
	    	 DevStat stat = new DevStat();
	    	 Desarrollador dev = (Desarrollador)dato[0];
	    	 stat.setNombre(dev.getNick());
	    	 double total = (Double)dato[1];
	    	 stat.setTotal(DesarrolladorDAO.round(total,2));
	    	 stat.setVentas((long)dato[2]);
	    	 datos.add(stat);
	     }
	     return datos;
	}
	
	public List<DevStat> obtenerVentasJuego(){
		 Query q = entity.createQuery("SELECT j.nombre, sum(c.precio), count(c.juego) FROM Juego j JOIN j.ventas c group by j.nombre order by sum(c.precio) desc");
		 List<Object[]> resultado= (List<Object[]>)q.getResultList();
		 List<DevStat> datos = new ArrayList<DevStat>();
	     for(Object[] dato: resultado){
	    	 DevStat stat = new DevStat();
	    	 stat.setNombre((String)dato[0]);
	    	 double total = (Double)dato[1];
	    	 stat.setTotal(DesarrolladorDAO.round(total,2));
	    	 stat.setVentas((long)dato[2]);
	    	 datos.add(stat);
	     }
	     return datos;
	}
	

	public List<DevStat> obtenerVentasTop(){
		 Query q = entity.createQuery("SELECT j.nombre, sum(c.precio), count(c.juego) FROM Juego j JOIN j.ventas c group by j.nombre order by count(c.juego) desc");
		 List<Object[]> resultado= (List<Object[]>)q.getResultList();
		 List<DevStat> datos = new ArrayList<DevStat>();
	     for(Object[] dato: resultado){
	    	 DevStat stat = new DevStat();
	    	 stat.setNombre((String)dato[0]);
	    	 double total = (Double)dato[1];
	    	 stat.setTotal(DesarrolladorDAO.round(total,2));
	    	 stat.setVentas((long)dato[2]);
	    	 datos.add(stat);
	     }
	     return datos;
	}
	
	public List<Chart> obtenerVentasFecha() {
		 Query q = entity.createQuery("SELECT monthname(c.fecha),count(c.juego) FROM CompraJuego c group by monthname(c.fecha) order by month(c.fecha) asc");
		 List<Object[]> resultado= (List<Object[]>)q.getResultList();
		 List<Chart> lista = new ArrayList<Chart>();
	     for(Object[] dato: resultado){
	    	Chart c1 = new Chart();
	    	c1.setMonth((String)dato[0]);
	    	c1.setTotal((long)dato[1]);
	    	lista.add(c1);
	     }
	     return lista;
	}
	
	
	public List<Chart> obtenerIngresosFecha() {
		 Query q = entity.createQuery("SELECT monthname(c.fecha),SUM(c.precio) FROM CompraJuego c group by monthname(c.fecha) order by month(c.fecha) asc");
		 List<Object[]> resultado= (List<Object[]>)q.getResultList();
		 List<Chart> lista = new ArrayList<Chart>();
	     for(Object[] dato: resultado){
	    	Chart c1 = new Chart();
	    	c1.setMonth((String)dato[0]);
	    	double total =  (double)dato[1];
	    	c1.setTotal(new Double(total).longValue());
	    	lista.add(c1);
	     }
	     return lista;
	}
	
	// obtener todos los Administrador
	public List<Administrador> obtenerAdministradors() {
		List<Administrador> listaAdministradors = new ArrayList<Administrador>();
		Query q = entity.createQuery("SELECT c FROM Administrador c");
		listaAdministradors = q.getResultList();
		entity.close();
		return listaAdministradors;
	}
	
	@Override
	public void finalize() {
		entity.close();
	}
	
	public void cerrar() {
		entity.close();
	}

}
