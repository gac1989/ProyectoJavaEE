package com.lab.java;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.dao.EventoDAO;
import com.model.Evento;

public class IniciarEvento extends TimerTask{
		private String nombre;
		
	  public IniciarEvento(String nombre){
	     //Some stuffs
		  this.nombre=nombre;
	   }

	   @Override
	   public void run() {
		   EventoDAO eventoDAO = new EventoDAO();
		   Evento e1 = eventoDAO.buscar(nombre);
		   if(e1!=null) {
			   e1.setActivo(1);
			   eventoDAO.guardar(e1);
		   }
		   long restante = e1.getFecha_fin().getTime() - new Date().getTime();
		   Timer t = new Timer();
		   FinalizarEvento mTask = new FinalizarEvento(e1.getNombre());
		   t.schedule(mTask, restante);
		   System.out.println("SE INICIA EL EVENTO");
	   }

	}
