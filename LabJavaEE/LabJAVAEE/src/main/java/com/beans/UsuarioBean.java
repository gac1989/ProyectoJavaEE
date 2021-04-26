package com.beans;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
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
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.primefaces.model.file.UploadedFile;

import com.google.gson.Gson;
import com.model.Usuario;






@ManagedBean(name = "UsuarioBean")
@RequestScoped
public class UsuarioBean {
	
	private List<Usuario> usuarios = this.obtenerUsuarios();
	private String nuevo = this.nuevo();	
	
	public String getNuevo() {
		return nuevo;
	}

	public void setNuevo(String nuevo) {
		this.nuevo = nuevo;
	}

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
	
	public String mostrarUsuarios() {

		return  "/faces/mostrarUsuarios.xhtml";
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
		System.out.println("IMPRESION ");
        return datos;
	}

	public void subirImagen() throws FileNotFoundException {
		System.out.println("ENTRE A LA IMAGEN");
		String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/subir";
		Client client = ClientBuilder.newClient();
		File archivo = new File("C:\\Users\\admin\\Desktop\\correo.png");
		UploadedFile file = null;
		WebTarget target = client.target(urlRestService);
		MultipartFormDataOutput mdo = new MultipartFormDataOutput();
		mdo.addFormData("fichero", archivo, MediaType.APPLICATION_OCTET_STREAM_TYPE);
		mdo.addFormData("nombre", "correo.png", MediaType.TEXT_PLAIN_TYPE);
		GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(mdo) { };
		Response response = target.request().post(Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE));
		response.close();
	}
	
	public static Usuario obtenerUsuario(String nick) {
		String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/usuario/" + nick;
		Client client = ClientBuilder.newClient();
		WebTarget target= client.target(urlRestService);
		Response response = target.request().get();
		String response2 = response.readEntity(String.class);
        Usuario u = new Gson().fromJson(response2, Usuario.class);
		return u;
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
        System.out.println("LA RESPUESTA ES: " + response.getStatus());
		return "/faces/index.xhtml";
	}
	


	// eliminar un Usuario
	public String eliminar(String nick) {
		//UsuarioDAO UsuarioDAO = new UsuarioDAO();
		//UsuarioDAO.eliminar(nick);
		System.out.println("Usuario eliminado..");
		return "/faces/index.xhtml";
	}

}
