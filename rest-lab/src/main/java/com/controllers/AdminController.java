package com.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;

import com.dao.AdminDAO;
import com.dao.EventoDAO;
import com.dao.UsuarioDAO;
import com.interfaces.AdminInterface;
import com.lab.java.IniciarEvento;
import com.model.AdminStats;
import com.model.Administrador;
import com.model.Chart;
import com.model.DevStat;
import com.model.Evento;
import com.model.Juego;
import com.model.Usuario;

public class AdminController implements AdminInterface {

	@Override
	public List<Chart> getDatosFecha(){
		return new AdminDAO().obtenerVentasFecha();
	}
	
	@Override
	public List<Chart> getIngresosFecha(){
		return new AdminDAO().obtenerIngresosFecha();
	}
	
	@Override
	public AdminStats adminStats() {
		return new AdminDAO().obtenerStats();
	}
	
	@Override
	public List<DevStat> adminStatsJuego(){
		return new AdminDAO().obtenerVentasJuego();
	}
	
	@Override
	public List<DevStat> adminStatsVentas(){
		return new AdminDAO().obtenerVentasTop();
	}
	
	@Override
	public List<DevStat> adminStatsDev(){
		return new AdminDAO().obtenerVentasDesarrollador();
	}
	
	@Override
	public boolean finalizarEvento(String nombre, String nick) {
		Usuario u1 = null;
		Evento e1 = null;
		UsuarioDAO controlus = new UsuarioDAO();
		EventoDAO controlev = new EventoDAO();
		if(nick!=null && !nick.isEmpty()) {
			u1 = controlus.buscar(nick);
		}
		if(nombre!=null && !nombre.isEmpty() && u1!=null) {
			e1 = controlev.buscar(nombre);
		}
		if(u1 instanceof Administrador && e1!=null) {
		   List<Juego> lista = e1.getJuegos();
		   for(Iterator<Juego> featureIterator = lista.iterator(); 
			    featureIterator.hasNext(); ) {
			    Juego feature = featureIterator.next();
			    System.out.println("El juego a borrar es: " + feature.getNombre() + "Su duenio es: " + feature.getDesarrollador().getNick());
			    feature.setEvento(null);
			    featureIterator.remove();
			}
		    e1.setActivo(0);
		    controlev.editar(e1);
		    controlev.cerrar();
		    return true;
		}
		controlev.cerrar();
		return false;
	}
	@Override
	public boolean iniciarEvento(String nombre, String nick) {
		System.out.println("El evento es: " + nombre + " El usuario es " + nick);
		UsuarioDAO control = new UsuarioDAO();
		EventoDAO controlev = new EventoDAO();
		Usuario u1 = null;
		Evento e1 = null;
		if(nick!=null && !nick.isEmpty()) {
			u1 = control.buscar(nick);
		}
		if(nombre!=null && !nombre.isEmpty() && u1!=null) {
			e1 = controlev.buscar(nombre);
		}
		if(u1 instanceof Administrador && e1!=null) {
		   e1.setActivo(1);
		   controlev.editar(e1);
		   return true;
		}
		return false;
	}
	
	@Override
	public boolean registroEvento(String nombre, String descuento, String fecha_ini, String fecha_fin) throws ParseException{
		System.out.println("La fecha es: " + fecha_ini);
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss");
		SimpleDateFormat formato2 = new SimpleDateFormat("yyyy-MM-dd");
		Date fechaInicio = null;
	      if(fecha_ini!=null){
	    	  try {
	    		  fechaInicio = formato.parse(fecha_ini);
	    	  }
	    	  catch(Exception e) {
	    		  System.out.println("Formato uno incorrecto");
	    		  fechaInicio = formato2.parse(fecha_ini);
	    	  }
	    	  System.out.println("La hora es: " + fechaInicio.toString());
	       }
		Date fechaFin = null;
	      if(fecha_fin!=null){
	    	  try {
	    		  fechaFin = formato.parse(fecha_fin);
	    	  }
	    	  catch(Exception e) {
	    		  System.out.println("Formato uno incorrecto");
	    		  fechaFin = formato2.parse(fecha_fin);
	    	  }
	    	  System.out.println("La hora es: " + fechaInicio.toString());
	      }
		if(nombre!=null &&  !nombre.equals("") && descuento!=null && !descuento.equals("")&& fecha_ini!=null && !fecha_ini.equals("")&& fecha_fin!=null && !fecha_fin.equals("")) {
			Evento evento = new Evento();
			evento.setNombre(nombre);
			evento.setDescuento(Float.parseFloat(descuento));
			evento.setFecha_ini(fechaInicio);
			evento.setFecha_fin(fechaFin);
			evento.setActivo(0);
			EventoDAO controlev = new EventoDAO();
			controlev.guardar(evento);
			//Se inicia una cuenta regresiva hasta la fecha de inicio
			long restante = fechaInicio.getTime() - new Date().getTime();
			Timer t = new Timer();
			IniciarEvento mTask = new IniciarEvento(nombre);
			if(restante<=0) {
				System.out.println("La fecha de inicio ya  ha pasado");
				mTask.run();
			}
			else {
				t.schedule(mTask, restante);
			}
			return true;
		}
		else {
			return false;
		}
	}
	
}
