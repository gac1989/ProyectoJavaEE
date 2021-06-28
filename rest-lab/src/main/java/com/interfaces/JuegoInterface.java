package com.interfaces;

import java.io.IOException;
import java.util.List;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import com.model.Comentario;
import com.model.Imagen;
import com.model.Juego;

public interface JuegoInterface {

	 public boolean uploadFile(MultipartFormDataInput input) throws IOException;
	 
	 public List<Juego> getJuegos();
	 
	 public List<Juego> getJuegosOferta();
	 
	 public List<Juego> getUltimosJuegos();
	 
	 public Juego buscarJuego(String calve);
	
	 public List<Juego> buscadorJuegos(String busqueda);
	 
	 public List<Comentario> comentariosJuegos(String id);
	 
	 public boolean reportarJuego(String id);
	 
	 public List<Juego> getJuegosReportados();
	 
	 public List<Imagen> getImagenesJuegos(String id);
	 
	 public boolean bloquearJuego(String id);
	 
	 public boolean desbloquearJuego(String id);
	 
	 public boolean checkJuego(String nombre);
	 
}
