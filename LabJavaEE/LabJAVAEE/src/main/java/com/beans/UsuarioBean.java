package com.beans;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.primefaces.model.file.UploadedFile;

import com.google.gson.Gson;
import com.login.SessionUtils;
import com.model.Desarrollador;
import com.model.Jugador;
import com.model.Usuario;
import com.utils.ClientControl;


@ManagedBean(name = "UsuarioBean", eager = true)
@RequestScoped
public class UsuarioBean {
	private List<Usuario> usuarios = null;
	private String propios = "/faces/listarpropios.xhtml";;
	private UploadedFile file;
	private String texto = null;
	private List<Usuario> todosUsuarios = null;
	
	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public String getPropios() {
		return propios;
	}

	public void setPropios(String propios) {
		this.propios = propios;
	}

	public String perfil(Usuario u) {
		if(u.getType()=="jugador") {
			return "/faces/perfilJugador?faces-redirect=true";
		}
		else {
			return "/faces/perfilDesarrollador?faces-redirect=true";
		}
	}
	
	public List<Usuario> getUsuarios() {
		if(usuarios==null) {
			usuarios=this.obtenerUsuarios();
		}
		return usuarios;
	}

	public void setUsuarios(List<Usuario> Usuarios) {
		this.usuarios = Usuarios;
	}
	
	public String nuevoJugador() {
		Jugador c= new Jugador();
		c.setType("jugador");
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.put("Jugador", c);
		return  "/faces/nuevojugador.xhtml";
	}
	public String nuevoDesarrollador() {
		Desarrollador c= new Desarrollador();
		c.setType("desarrollador");
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.put("Desarrollador", c);
		return  "/faces/nuevodesarrollador.xhtml";
	}
	
	public String mostrarUsuarios() {

		return  "/faces/mostrarUsuarios.xhtml";
	}
	
	public String guardar (Usuario user) throws IOException {
        String urlRestService2 = "http://localhost:8080/rest-lab/api/recursos/registrarse";
        MultipartFormDataOutput mdo = new MultipartFormDataOutput();
        if(file!=null && file.getInputStream()!=null) {
        	mdo.addFormData("fichero", file.getInputStream(), MediaType.APPLICATION_OCTET_STREAM_TYPE);
        }
		mdo.addFormData("usuario", user, MediaType.APPLICATION_JSON_TYPE);
		Response response = new ClientControl().realizarPeticionMultiple(urlRestService2, mdo);
		String response2 = response.readEntity(String.class);
		String type = user.getType();
		if(response.getStatus()==200) {
			if(type.equals("desarrollador")) {
				user =  new Gson().fromJson(response2, Desarrollador.class);
			}
			else {
				user =  new Gson().fromJson(response2, Jugador.class);
			}
			user.setType(type);
			HttpSession session = SessionUtils.getSession();
			session.setAttribute("username", user.getNick());
			session.setAttribute("type", type);
			session.setAttribute("user", user);
			return  "/faces/index.xhtml";
		}
        return  "/faces/login.xhtml";
	}

	public List<Usuario> getTodosUsuarios() {
		if(todosUsuarios==null) {
			todosUsuarios=this.obtenerUsuarios();
		}
		return todosUsuarios;
	}

	public void setTodosUsuarios(List<Usuario> todosUsuarios) {
		this.todosUsuarios = todosUsuarios;
	}
	
	public String mostrarPropios() {
		System.out.println("Cargo los juegossssss ");
		return "/faces/listarpropios.xhtml";
	}
	
	public List<Usuario> obtenerUsuarios(){
		String urlRestService = "http://localhost:8080/rest-lab/api/recursos/usuarios";
        Response response = new ClientControl().realizarPeticion(urlRestService, "GET", null);
        String response2 = response.readEntity(String.class);
        Usuario[] j = null;
        if(response2!=null && !response2.isEmpty()) {
        	 j = new Gson().fromJson(response2, Usuario[].class);
        }
        List<Usuario> datos = null;
        if(j!=null) {
        	datos = Arrays.asList(j);
        }
        return datos;
	}
	
	public String listarUsuarios() {
		return "/faces/mostrarTodosUsuarios.xhtml";
	}
	
	public static Usuario obtenerUsuario(String nick) {
		String urlRestService = "http://localhost:8080/rest-lab/api/recursos/usuario/" + nick;
		Response response = new ClientControl().realizarPeticion(urlRestService, "GET", null);
		String response2 = response.readEntity(String.class);
        Usuario u = new Gson().fromJson(response2, Usuario.class);
		return u;
	}

	public String actualizar(Usuario Usuario) {
		String urlRestService = "http://localhost:8080/rest-lab/api/recursos/editar";
		Client client = ClientBuilder.newClient();
		WebTarget target= client.target(urlRestService);
        target.request().post(Entity.entity(Entity.json(Usuario), MediaType.APPLICATION_JSON));
		return "/faces/perfilDesarrollador.xhtml?faces-redirect=true";
	}
	
	public String agregarPublicacion(String texto, Usuario u) throws IOException {
		String urlRestService2 = "http://localhost:8080/rest-lab/api/recursos/agregarpublicacion";
        MultipartFormDataOutput mdo = new MultipartFormDataOutput();
        mdo.addFormData("nick", u.getNick(), MediaType.TEXT_PLAIN_TYPE);
 		mdo.addFormData("texto", texto, MediaType.TEXT_PLAIN_TYPE);
 		if(file!=null) {
 			mdo.addFormData("imagen", file.getInputStream(), MediaType.APPLICATION_OCTET_STREAM_TYPE);
 		}
 		new ClientControl().realizarPeticionMultiple(urlRestService2, mdo);
        return perfil(u);
	}
	
}
