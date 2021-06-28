package com.controllers;

import java.text.ParseException;
import java.util.List;

import javax.ws.rs.FormParam;

import com.dao.DesarrolladorDAO;
import com.dao.EventoDAO;
import com.dao.JuegoDAO;
import com.dao.UsuarioDAO;
import com.interfaces.DesarrolladorInterface;
import com.model.DatosVenta;
import com.model.Desarrollador;
import com.model.DevStat;
import com.model.Estado;
import com.model.Evento;
import com.model.Juego;
import com.model.Usuario;

public class DesarrolladorController implements DesarrolladorInterface {

	
	
	@Override
	public List<DatosVenta> obtenerVentasJuego(String juego){
		return new JuegoDAO().obtenerVentas(Integer.parseInt(juego));
	}
	
	@Override
	public List<Juego> getJuegosDesarrollador(String nick){
		UsuarioDAO control = new UsuarioDAO();
		Usuario u1 = control.buscar(nick);
		Desarrollador d1 = null;
		try {
			d1 = (Desarrollador)u1;
			if(d1!=null) {
				List<Juego> lista = d1.getJuegos();
				if(lista!=null && !lista.isEmpty()) {
					System.out.println("El primer juego es: " + lista.get(0));
				}
				control.cerrar();
				return lista;
			}
			else {
				control.cerrar();
				return null;
			}
		}
		catch(Exception e) {
			return null;
		}
	}
	
	@Override
	public boolean agregarJuegoEvento(String nombre,  String id_juego) {
		Juego j1 = null;
		JuegoDAO control = new JuegoDAO();
		System.out.print("El nombre del evento es: " + nombre + " El id del juego es: " + id_juego);
		if(id_juego!=null && !id_juego.isEmpty()) {
			j1 = control.buscar(Integer.parseInt(id_juego));
			System.out.println("El juego encontrado es: " + j1.getNombre());
		}
		if(j1!=null) {
			Evento e1 = null;
			if(nombre!=null && !nombre.isEmpty()) {
				EventoDAO evcon = new EventoDAO();
				e1 = evcon.buscar(nombre);
			}
			if(e1!=null && e1.getActivo()==1) {
				j1.setEvento(e1);
				e1.agregarJuego(j1);
				control.editar(j1);
				control.cerrar();
				return true;
			}
		}
		control.cerrar();
		return false;
	}
	
	@Override
	public boolean quitarJuegoEvento(String id_juego) {
		Juego j1 = null;
		System.out.print(" El id del juego es: " + id_juego);
		JuegoDAO controlpy = new JuegoDAO();
		if(id_juego!=null && !id_juego.isEmpty()) {
			j1 = controlpy.buscar(Integer.parseInt(id_juego));
		}
		System.out.println("El juego encontrado es: " + j1.getNombre());
		if(j1!=null) {
			Evento e1 = null;
			e1 = j1.getEvento();
			if(e1!=null && e1.getActivo()==1) {
				j1.setEvento(null);
				controlpy.editar(j1);
				controlpy.cerrar();
				return true;
			}
		}
		controlpy.cerrar();
		return false;
	}
	
	@Override
	public boolean solicitarDesbloqueo(String id_juego) {
		JuegoDAO controlpy = new JuegoDAO();
		Juego j1 = controlpy.buscar(Integer.parseInt(id_juego));
		if(j1!=null && j1.getEstado()==Estado.BLOQUEADO) {
			j1.setDesbloqueo(true);
			controlpy.editar(j1);
			controlpy.cerrar();
		}
		return true;
	}
	
	public List<DevStat> estadisticasDesarrollador(@FormParam("nick")String nick) throws ParseException{
		DesarrolladorDAO controldev = new DesarrolladorDAO();
		Desarrollador d1 = controldev.buscar(nick);
		if(d1!=null) {
			return controldev.obtenerStats(d1);
		}
		else {
			return null;
		}
	}
	
}
