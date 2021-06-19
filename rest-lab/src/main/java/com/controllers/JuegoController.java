package com.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import com.dao.CategoriaDAO;
import com.dao.JuegoDAO;
import com.dao.UsuarioDAO;
import com.google.gson.Gson;
import com.interfaces.JuegoInterface;
import com.model.Categoria;
import com.model.Comentario;
import com.model.Desarrollador;
import com.model.Estado;
import com.model.Imagen;
import com.model.Juego;

public class JuegoController implements JuegoInterface{

	 @Override
	 public boolean uploadFile(MultipartFormDataInput input) throws IOException {
		System.out.print("LLEGUE A SUBIR ACA");
        String fileName = "";
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> name = uploadForm.get("nombre");
        List<InputPart> juego = uploadForm.get("juego");
        List<InputPart> cat = uploadForm.get("categoria");
        fileName = name.get(0).getBodyAsString();
        String categoria = "";
        String[] categorias = null;
        if(cat!=null) {
        	categoria = cat.get(0).getBodyAsString();
        	System.out.println("El string de categoira es: " + categoria );
        	categorias = new Gson().fromJson(categoria, String[].class);
        }
		 try {
		    registroJuego(juego.get(0).getBodyAsString(),fileName,categorias,uploadForm);
			System.out.println("Done");
		  } catch (IOException e) {
		    return false;
		  }
		System.out.println("El Archivo es: " + fileName + " El juego es: " + juego.get(0).getBodyAsString());
        return true;
	 }
	 
	 public void registroJuego(String json, String nick, String[] categoria, Map<String, List<InputPart>> uploadForm ) throws IOException {
			UsuarioDAO controlus = new UsuarioDAO();
			Desarrollador d1 = (Desarrollador)controlus.buscar(nick);
			if(d1!=null) {
				Juego j1 = new Juego();
				j1 = new Gson().fromJson(json, Juego.class);
				JuegoDAO controlgm = new JuegoDAO();
				j1.setDesarrollador(d1);
				j1.setEstado(Estado.ACTIVO);
				j1.setDesbloqueo(false);
				if(uploadForm!=null) {
					List<InputPart> portada = uploadForm.get("portada");
				    InputStream inputStream = portada.get(0).getBody(InputStream.class,null);
	     		    byte [] bytes = IOUtils.toByteArray(inputStream);
	     		    j1.setImagen(bytes);
				    for (Map.Entry<String,List<InputPart>> entry : uploadForm.entrySet()) {
			        	if(entry.getKey().matches("(.*)fichero(.*)")) {
			        		InputStream fichero = entry.getValue().get(0).getBody(InputStream.class,null);
			     		    byte [] fich = IOUtils.toByteArray(fichero);
			     		    Imagen img = new Imagen();
			     		    img.setJuego(j1);
			     		    img.setContenido(fich);
			     		    j1.agregarImagen(img);
			        	}
			        }
				   
				}
				controlgm.guardar(j1);
				if(categoria!=null) {
					CategoriaDAO controlcat = new CategoriaDAO();
					for(String cat : categoria) {
						System.out.println("La categoria es: " + cat);
						Categoria c1 = controlcat.buscar(cat);
						if(c1!=null) {
							c1.agregarJuego(j1);
							controlcat.editar(c1);
						}
					}
					controlcat.cerrar();
				}
				System.out.println("SE CREO EL JUEGO");
			}
			else {
				System.out.println("ERROR AL CREAR EL JUEGO");
			}
	}
	 
	 
	 @Override
	 public List<Juego> getJuegos(){
		return new JuegoDAO().obtenerJuegos();
	 }
	 
	 @Override
	 public List<Juego> getJuegosOferta(){
		 return new JuegoDAO().obtenerJuegosOferta();
	 }
	 
	 @Override
	 public List<Juego> getUltimosJuegos(){
		 return new JuegoDAO().obtenerUltimosJuegos();
	 }
	 
	 @Override
	 public Juego buscarJuego(String calve) {
		 return new JuegoDAO().buscar(Integer.parseInt(calve));
	 }
	
	 @Override
	 public List<Juego> buscadorJuegos(String busqueda){
		 return new JuegoDAO().buscarJuegos(busqueda);
	 }
	 
	 @Override
	 public List<Comentario> comentariosJuegos(String id){
		JuegoDAO controlgm = new JuegoDAO();
		Juego juego = controlgm.buscar(Integer.parseInt(id));
		if(juego!=null) {
			List<Comentario> coments = juego.getComentarios();
			if(coments!=null && !coments.isEmpty()) {
				System.out.println("El primer juego es: " + coments.get(0));
			}
			controlgm.cerrar();
			return coments;
		}
		else {
			controlgm.cerrar();
			return null;
		}
	 }
	 
	 @Override
	 public boolean reportarJuego(String id){
		 JuegoDAO controlgm = new JuegoDAO();
		Juego j1 = controlgm.buscar(Integer.parseInt(id));
		if(j1!=null) {
			j1.setEstado(Estado.REPORTADO);
			controlgm.editar(j1);
			return true;
		}
		return false;
	 }
	 
	 @Override
	 public List<Juego> getJuegosReportados(){
		 return new JuegoDAO().obtenerJuegosReportados();
	 }
	 
	 @Override
	 public List<Imagen> getImagenesJuegos(String id){
		JuegoDAO control = new JuegoDAO();
		Juego j1 = control.buscar(Integer.parseInt(id));
		if(j1!=null) {
			List<Imagen> imagenes = j1.getImagenes();
			if(imagenes!=null && !imagenes.isEmpty()) {
				System.out.println("La primera imagen es: " + imagenes.get(0).getJuego().getDescripcion());
			}
			control.cerrar();
			return imagenes;
		}
		else {
			control.cerrar();
			return null;
		}
	 }
	 
	 @Override
	 public boolean bloquearJuego(String id){
		JuegoDAO controlcom = new JuegoDAO();
		Juego j1 = controlcom.buscar(Integer.parseInt(id));
		if(j1!=null) {
			j1.setEstado(Estado.BLOQUEADO);
			controlcom.editar(j1);
			return true;
		}
		return false;
	 }
	 
	 @Override
	 public boolean desbloquearJuego(String id) {
		 JuegoDAO controlcom = new JuegoDAO();
		Juego j1 = controlcom.buscar(Integer.parseInt(id));
		if(j1!=null) {
			j1.setDesbloqueo(false);
			j1.setEstado(Estado.ACTIVO);
			controlcom.editar(j1);
			return true;
		}
		return false;
	 }
	 
	 @Override
	 public boolean checkJuego(String nombre) {
		 return (new JuegoDAO().buscarJuego(nombre)!=null);
	 }
	 
}
