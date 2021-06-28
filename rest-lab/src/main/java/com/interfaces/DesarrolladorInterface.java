package com.interfaces;

import java.text.ParseException;
import java.util.List;


import com.model.DatosVenta;
import com.model.DevStat;
import com.model.Juego;

public interface DesarrolladorInterface {

	
	public List<DatosVenta> obtenerVentasJuego(String juego);
	
	public List<Juego> getJuegosDesarrollador(String nick);
	
	public boolean agregarJuegoEvento(String nombre, String id_juego);
	
	public boolean quitarJuegoEvento(String id_juego);
	
	public boolean solicitarDesbloqueo(String id_juego);
	
	public List<DevStat> estadisticasDesarrollador(String nick) throws ParseException;
	
}
