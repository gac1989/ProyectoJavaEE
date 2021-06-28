package com.interfaces;

import java.util.List;


import com.model.Juego;

public interface JugadorInterface {

	public List<Juego> obtenerJuegosUsuario(String nick);
	
	public boolean comprarJuego(String nick, String id);
	
	public boolean comentarJuego(String nick,  String juegoid,  String texto, String nota);
	
	public boolean juegoComprado( String nick, String id);
	
}
