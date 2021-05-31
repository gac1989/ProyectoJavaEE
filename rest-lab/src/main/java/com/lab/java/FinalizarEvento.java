package com.lab.java;

import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;

import com.dao.EventoDAO;
import com.dao.JuegoDAO;
import com.model.Evento;
import com.model.Juego;

class FinalizarEvento extends TimerTask{
	private String nombre;
   public FinalizarEvento(String nombre){
     //Some stuffs
	   this.nombre=nombre;
   }
   @Override
   public void run() {
	   EventoDAO evento = new EventoDAO();
	   Evento e1 = evento.buscar(nombre);
	   System.out.println("El nombre del evento es:" + e1.getNombre());
	   if(e1!=null) {
		   List<Juego> lista = e1.getJuegos();
		   for(Iterator<Juego> featureIterator = lista.iterator(); 
			    featureIterator.hasNext(); ) {
			    Juego feature = featureIterator.next();
			    JuegoDAO juegodao = new JuegoDAO(); 
			    Juego original = juegodao.buscar(feature.getId());
			    original.setEvento(null);
			    juegodao.guardar(original);
			    featureIterator.remove();
			}
		   e1.setActivo(0);
		   evento.guardar(e1);
	   }
     System.out.println("TERMINA EL EVENTO");
   }

}