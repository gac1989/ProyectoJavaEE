package com.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import org.mindrot.jbcrypt.BCrypt;

import com.model.Usuario;
import com.model.JPAUtil;

public class UsuarioDAO {
	
	EntityManagerFactory factory = JPAUtil.getEntityManagerFactory();
	EntityManager entity = factory.createEntityManager();

	public String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }
	public boolean verifyHash(String password, String hash) {
		try {
			System.out.println("La pass es: " + password + " El hash es: " + hash);
		    return BCrypt.checkpw(password, hash);
		}
		catch(Exception e) {
			System.out.println("Error en la verificacion de contraseña");
		}
		return false;
    }
	
	// guardar Usuario
	public void guardar(Usuario Usuario) {
		String pass = hash(Usuario.getPassword());
		Usuario.setPassword(pass);
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
		Usuario c;
		c = entity.find(Usuario.class, nick);
		return c;
	}
	
	public Object buscarEmail(String email) {
		entity.getTransaction().begin();
		Query q = entity.createQuery("SELECT c.email FROM Usuario c where c.email='" +email+"'");
		entity.getTransaction().commit();
		try {
			return q.getSingleResult();
		}
		catch(Exception e){
			return null;
		}
	}
	
	
	public Usuario checkUser(String nick, String password) {
		Usuario u = buscar(nick);
		if(u!=null && verifyHash(password, u.getPassword())) {
			return u;
		}
		return null;
	}

	/// eliminar Usuario
	public void eliminar(String nick) {
		Usuario c;
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
	
	public List<Usuario> obtenerUsuariosDebiles() {
		entity.getTransaction().begin();
		List<Usuario> listaUsuarios = new ArrayList<Usuario>();
		Query q = entity.createQuery("SELECT c FROM Usuario c where type(c) != com.model.Administrador");
		listaUsuarios = q.getResultList();
		entity.getTransaction().commit();
		return listaUsuarios;
	}
	
	
	public void cerrar() {
		entity.close();
	}
	
}
