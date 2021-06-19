package com.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import com.dao.PublicacionDAO;
import com.dao.UsuarioDAO;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.interfaces.UsuarioInterface;
import com.lab.java.RecursosRest;
import com.model.Desarrollador;
import com.model.Jugador;
import com.model.Publicacion;
import com.model.Usuario;

public class UsuarioController implements UsuarioInterface {

	InputStream img =  RecursosRest.class.getResourceAsStream("default.png");
	
	@Override
	public Usuario registrarse(MultipartFormDataInput input) throws IOException{
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("fichero");
        List<InputPart> user = uploadForm.get("usuario");
        Usuario u1 = null;
        try {
        	byte [] bytes = IOUtils.toByteArray(img);
        	if(inputParts!=null) {
        		InputStream inputStream = inputParts.get(0).getBody(InputStream.class,null);
        		if(inputStream != null && inputStream.available()!=0) {
     		    	bytes = IOUtils.toByteArray(inputStream);
     		    }
        	}
		    u1 = registro(user.get(0).getBodyAsString(),bytes);
        } catch (IOException e) {
		    e.printStackTrace();
        }
        return u1;
	}
	
	public Usuario registro(String json, byte[] imagen) {
		JsonObject convertedObject = new Gson().fromJson(json, JsonObject.class);
		JsonPrimitive type = convertedObject.getAsJsonPrimitive("type");
		String tipo = type.getAsString();
		UsuarioDAO controlus = new UsuarioDAO();
		Usuario u1 = null;
		if(json!=null && !json.isEmpty()) {
			switch(tipo) {
				case "jugador":{
					u1 = new Gson().fromJson(json, Jugador.class);
					u1.setImagen(imagen);
					controlus.guardar(u1);
					break;
				}
				case "desarrollador":{
					u1 = new Gson().fromJson(json, Desarrollador.class);
					u1.setImagen(imagen);
					controlus.guardar(u1);
					break;
				}
				default:{
					break;
				}
			}
			return u1;
		}
		else {
			return null;
		}
	}
	
	@Override
	public boolean uploadPublicacion(MultipartFormDataInput input) throws IOException{
        String fileName = "";
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> nick = uploadForm.get("nick");
        List<InputPart> texto = uploadForm.get("texto");
        List<InputPart> img = uploadForm.get("imagen");
        fileName = nick.get(0).getBodyAsString();
        UsuarioDAO control = new UsuarioDAO();
        Usuario u1 = control.buscar(fileName);
        if(u1!=null) {
        	PublicacionDAO controlpub = new PublicacionDAO();
			Publicacion p1 = new Publicacion();
        	if(img!=null) {
        		InputStream inputStream = img.get(0).getBody(InputStream.class,null);
        		 byte [] bytes = IOUtils.toByteArray(inputStream);
        		 p1.setImagen(bytes);
        	}
			p1.setTexto(texto.get(0).getBodyAsString());
			p1.setUser(u1);
			controlpub.editar(p1);
			controlpub.cerrar();
			control.cerrar();
			return true;
		}
		control.cerrar();
        return false;
	}
	 
	@Override
	public List<Publicacion> getPublicaciones(String nick){
		UsuarioDAO control = new UsuarioDAO();
		Usuario u1 = control.buscar(nick);
		List<Publicacion> lista = u1.getPublicaciones();
		if(lista!= null && !lista.isEmpty()) {
			System.out.println(lista.get(0).getTexto());
			Collections.reverse(lista);
			control.cerrar();
			return lista;
		}
		else {
			control.cerrar();
			return null;
		}
	}
	
	@Override
	public List<Usuario> getUsuarios(){
		return new UsuarioDAO().obtenerUsuariosDebiles();
	}
	
	@Override
	public Usuario obtenerusuario(String nick) {
		return new UsuarioDAO().buscar(nick);
	}
	
	@Override
	public boolean checkUser(String nick) {
		return (new UsuarioDAO().buscar(nick)!=null);
	}
	
	@Override
	public boolean checkUserEmail(String email) {
		return (new UsuarioDAO().buscarEmail(email)!=null);
	}
	
	@Override
	public Usuario checkUsuario(String nick, String pass) {
		return new UsuarioDAO().checkUser(nick, pass);
	}
	
	@Override
	public boolean editar(String json) {
		JsonObject convertedObject = new Gson().fromJson(json, JsonObject.class);
		JsonObject type = convertedObject.getAsJsonObject("entity");
		JsonElement type1 = type.get("type");
		UsuarioDAO controlus = new UsuarioDAO();
		String nick = type.get("nick").getAsString();
		Usuario u1 = controlus.buscar(nick);
		if(u1!=null) {
			u1.setEmail(type.get("email").getAsString());
			if(type1!=null) {
				switch(type1.getAsString()) {
					case "jugador":{
						Jugador j1 = (Jugador)u1;
						j1.setApellido(type.get("apellido").getAsString());
						j1.setNombre(type.get("nombre").getAsString());
						controlus.editar(u1);
						break;
					}
					case "desarrollador":{
						Desarrollador d1 = (Desarrollador)u1;
						d1.setDireccion(type.get("direccion").getAsString());
						d1.setNombre_empresa(type.get("nombre_empresa").getAsString());
						d1.setTelefono(type.get("telefono").getAsString());
						d1.setPais(type.get("pais").getAsString());
						controlus.editar(u1);
						break;
					}
				}
				return true;
			}
		}
		return false;
	}
	
	
	
}
