package com.interfaces;

import java.io.IOException;
import java.util.List;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import com.model.Publicacion;
import com.model.Usuario;

public interface UsuarioInterface {
	
	public Usuario registrarse(MultipartFormDataInput input) throws IOException;
	
	public boolean uploadPublicacion(MultipartFormDataInput input) throws IOException;
	 
	public List<Publicacion> getPublicaciones(String nick);
	
	public List<Usuario> getUsuarios();
	
	public Usuario obtenerusuario(String nick);
	
	public boolean checkUser(String nick);
	
	public boolean checkUserEmail(String email);
	
	public Usuario checkUsuario(String nick, String pass);
	
	public boolean editar(String json);
	
	
	
}
