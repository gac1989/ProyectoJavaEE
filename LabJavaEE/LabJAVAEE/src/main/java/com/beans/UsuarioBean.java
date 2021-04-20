package com.beans;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.primefaces.shaded.commons.io.IOUtils;

import com.dao.UsuarioDAO;
import com.google.gson.Gson;
import com.model.Usuario;



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
		
		String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/registrarse";
		Client client = ClientBuilder.newClient();
        Form form = new Form();
        form.param("nick", Usuario.getNick());
        form.param("nombre", Usuario.getNombres());
        form.param("apellido", Usuario.getApellidos());
        form.param("direccion", Usuario.getDireccion());
        form.param("email", Usuario.getEmail());
        form.param("telefono", Usuario.getTelefono());
        WebTarget target= client.target(urlRestService);
        Response response = target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
        String response2 = response.readEntity(String.class);
        System.out.println("La respuesta es: " + response2);
        
        return  "/faces/index.xhtml";
	}

	public List<Usuario> obtenerUsuarios() {
	
		String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/usuarios";
		Client client = ClientBuilder.newClient();
		WebTarget target= client.target(urlRestService);
        Response response = target.request().get();
        String response2 = response.readEntity(String.class);
        Usuario[] u = new Gson().fromJson(response2, Usuario[].class);
        List<Usuario> datos = Arrays.asList(u);
        return datos;
	}

	public String editar(String Usuario) {
		String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/usuario/" + Usuario;
		Client client = ClientBuilder.newClient();
		WebTarget target= client.target(urlRestService);
		Response response = target.request().get();
        String response2 = response.readEntity(String.class);
        Usuario u = new Gson().fromJson(response2, Usuario.class);
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.put("Usuario", u);
		return "/faces/editar.xhtml";
	}

	public String actualizar(Usuario Usuario) {

		String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/editar";
		Client client = ClientBuilder.newClient();
		WebTarget target= client.target(urlRestService);
		Form form = new Form();
        form.param("nick", Usuario.getNick());
        form.param("nombre", Usuario.getNombres());
        form.param("apellido", Usuario.getApellidos());
        form.param("direccion", Usuario.getDireccion());
        form.param("email", Usuario.getEmail());
        form.param("telefono", Usuario.getTelefono());
        Response response = target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
        String response2 = response.readEntity(String.class);
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
