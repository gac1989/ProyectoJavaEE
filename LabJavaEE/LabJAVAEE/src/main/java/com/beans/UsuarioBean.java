package com.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.ws.rs.core.Response;

import com.dao.UsuarioDAO;
import com.google.gson.Gson;
import com.model.Usuario;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

@ManagedBean(name = "UsuarioBean")
@RequestScoped
public class UsuarioBean {
	
	private List<Usuario> usuarios = this.obtenerUsuarios();
	
	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> Usuarios) {
		this.usuarios = Usuarios;
	}

	public String nuevo() {
		Usuario c= new Usuario();
		System.out.println("ACA LLEGUEEEEEE");
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.put("Usuario", c);
		return  "/faces/nuevo.xhtml";
	}
	
	public String guardar (Usuario Usuario) {
		//guarda la fecha de registro
		UsuarioDAO UsuarioDAO= new UsuarioDAO();
		UsuarioDAO.guardar(Usuario);
		return  "/faces/index.xhtml";
	}

	public List<Usuario> obtenerUsuarios() {
		String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/usuarios";
		ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = Client.create(clientConfig);
        WebResource webResource = client.resource(urlRestService);  
        String response2 = webResource.get(String.class);
        Usuario[] u = new Gson().fromJson(response2, Usuario[].class);
        List<Usuario> datos = Arrays.asList(u);
        return datos;
	}

	public String editar(String nick) {
		UsuarioDAO UsuarioDAO = new UsuarioDAO();
		Usuario c = new Usuario();
		c = UsuarioDAO.buscar(nick);
		System.out.println("******************************************");
		System.out.println(c);

		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.put("Usuario", c);
		return "/faces/editar.xhtml";
	}

	public String actualizar(Usuario Usuario) {
		//guarda la fecha de actualizacion
		UsuarioDAO UsuarioDAO = new UsuarioDAO();
		UsuarioDAO.editar(Usuario);
		return "/faces/index.xhtml";
	}

	// eliminar un Usuario
	public String eliminar(String nick) {
		UsuarioDAO UsuarioDAO = new UsuarioDAO();
		UsuarioDAO.eliminar(nick);
		System.out.println("Usuario eliminado..");
		return "/faces/index.xhtml";
	}

}
