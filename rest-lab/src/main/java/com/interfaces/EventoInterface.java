package com.interfaces;

import java.util.List;


import com.model.Evento;
import com.model.Juego;

public interface EventoInterface {

	
	public List<Juego> getJuegosEventos(String nombre);
	
	public List<Evento> getEventos();
	
	
	
}
