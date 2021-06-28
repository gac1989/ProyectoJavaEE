package com.controllers;

import java.util.List;


import com.dao.EventoDAO;
import com.interfaces.EventoInterface;
import com.model.Evento;
import com.model.Juego;

public class EventoController implements EventoInterface{

	@Override
	public List<Juego> getJuegosEventos(String nombre){
		System.out.println("Juegos Evento");
		EventoDAO controlev = new EventoDAO();
		Evento evento = controlev.buscar(nombre);
		if(evento != null) {
			List<Juego> lista = evento.getJuegos();
			if(lista!=null && !lista.isEmpty()) {
				System.out.println("El primer juego es: " + lista.get(0));
			}
			controlev.cerrar();
			return lista;
		}
		else {
			controlev.cerrar();
			return null;
		}
	}
	
	@Override
	public List<Evento> getEventos(){
		return new EventoDAO().obtenerEventos();
	}
	
	
	
}
