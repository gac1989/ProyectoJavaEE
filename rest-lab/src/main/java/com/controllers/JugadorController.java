package com.controllers;

import java.util.Date;
import java.util.List;

import com.dao.CompraDAO;
import com.dao.JuegoDAO;
import com.dao.JugadorDAO;
import com.interfaces.JugadorInterface;
import com.model.Comentario;
import com.model.CompraJuego;
import com.model.Estado;
import com.model.Juego;
import com.model.Jugador;

public class JugadorController implements JugadorInterface {

	@Override
	public List<Juego> obtenerJuegosUsuario(String nick){
		JugadorDAO control = new JugadorDAO();
		Jugador u1 = control.buscar(nick);
		if(u1!=null) {
			List<Juego> juegos = u1.obtenerJuegos();
			if(juegos!=null && !juegos.isEmpty()) {
				System.out.println("El primer juego es: " + juegos.get(0));
				control.cerrar();
			}
			return juegos;
		}
		else {
			control.cerrar();
			return null;
		}
	}
	
	@Override
	public boolean comprarJuego(String nick, String id) {
		if(nick!=null && id!=null) {
			JugadorDAO controlpy = new JugadorDAO();
			Jugador u1 = controlpy.buscar(nick);
			int idjuego = Integer.parseInt(id);
			JuegoDAO controlgm = new JuegoDAO();
			Juego j1 = controlgm.buscar(idjuego);
			if(u1!=null && j1!=null) {
				CompraJuego compra = new CompraJuego();
				compra.setUser(u1);
				compra.setJuego(j1);
				compra.setPrecio(j1.getOferta());
				compra.setFecha(new Date());
				CompraDAO c1 = new CompraDAO();
				c1.editar(compra);
			}
			return true;
		}
		return false;
	}
	
	@Override
	public boolean comentarJuego(String nick,  String juegoid,  String texto, String nota) {
		if(nick!=null && !nick.isEmpty() && juegoid!=null && !juegoid.isEmpty()) {
			int id = Integer.parseInt(juegoid);
			JuegoDAO controlgm = new JuegoDAO();
			Juego j = controlgm.buscar(id);
			JugadorDAO controlpy = new JugadorDAO();
			Jugador j1 = controlpy.buscar(nick);
			if(j!=null && j1!=null) {
				Comentario c1 = new Comentario();
				c1.setTexto(texto);
				c1.setAutor(j1);
				c1.setNota(Integer.parseInt(nota));
				c1.setEstado(Estado.ACTIVO);
				j.agregarComentario(c1);
				controlgm.guardar(j);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean juegoComprado( String nick, String id) {
		JugadorDAO controlpy = new JugadorDAO();
		if(nick!=null && !nick.isEmpty() && id!=null &&  !id.isEmpty()) {
			Jugador j1 = controlpy.buscar(nick);
			if(j1!=null) {
				List<Juego> j2 = j1.obtenerJuegos();
				for(Juego juego : j2) {
					if(juego.getId()==Integer.parseInt(id)) {
						controlpy.cerrar();
						return true;
					}
				}
			}
		}
		controlpy.cerrar();
		return false;
	}
	
}
