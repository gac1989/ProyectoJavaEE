package com.beans;

import java.io.File;
import java.io.FileNotFoundException;
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
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.mindrot.jbcrypt.BCrypt;
import org.primefaces.model.file.UploadedFile;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.login.SessionUtils;
import com.model.Desarrollador;
import com.model.Juego;
import com.model.Jugador;
import com.model.Usuario;
import com.model.UsuarioDAO;


@ManagedBean(name = "UsuarioBean", eager = true)
@RequestScoped
public class UsuarioBean {
	private List<Juego> juegos = null;
	private List<Usuario> usuarios = null;
	private String nuevoJug = this.nuevoJugador();
	private String nuevoDes = this.nuevoDesarrollador();
	private String propios = "/faces/listarpropios.xhtml";;
	
	
	
	public String getPropios() {
		return propios;
	}

	public void setPropios(String propios) {
		this.propios = propios;
	}

	
	public List<Juego> getJuegos() {
		System.out.println("Entre a buscar juegos");
		if(juegos==null) {
			juegos=this.obtenerJuegosJugador();
		}
		return juegos;
	}

	public void setJuegos(List<Juego> juegos) {
		this.juegos = juegos;
	}

	public String getNuevoJug() {
		return nuevoJug;
	}

	public void setNuevoJug(String nuevoJug) {
		this.nuevoJug = nuevoJug;
	}

	public String getNuevoDes() {
		return nuevoDes;
	}

	public void setNuevoJDes(String nuevoJDes) {
		this.nuevoDes = nuevoJDes;
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
	
	public String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }
	public static boolean verifyHash(String password, String hash) {
		System.out.println("La pass es: " + password + " El hash es: " + hash);
        return BCrypt.checkpw(password, hash);
    }
	
	
	public String nuevoJugador() {
		Jugador c= new Jugador();
		c.setType("jugador");
		System.out.println("ACA LLEGUEEEEEE");
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.put("Jugador", c);
		return  "/faces/nuevojugador.xhtml";
	}
	public String nuevoDesarrollador() {
		Desarrollador c= new Desarrollador();
		c.setType("desarrollador");
		System.out.println("ACA LLEGUEEEEEE DESARROLLADOR");
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.put("Desarrollador", c);
		return  "/faces/nuevodesarrollador.xhtml";
	}
	
	public String mostrarUsuarios() {

		return  "/faces/mostrarUsuarios.xhtml";
	}
	
	public static Usuario checkUser(String nick, String pass) {
		String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/usuario/" + nick;
		Client client = ClientBuilder.newClient();
        WebTarget target= client.target(urlRestService);
        Response response = target.request().get();
        String response2 = response.readEntity(String.class);
        
        System.out.println("La respuesta es: " + response2);
        JsonObject convertedObject = new Gson().fromJson(response2, JsonObject.class);
        JsonElement type=null;
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        if(convertedObject!=null) {
        	type = convertedObject.get("pais");
        }
        Usuario u = new Gson().fromJson(response2, Usuario.class);
        if(type!=null) {
        	sessionMap.put("type", "desarrollador");
			u.setType("desarrollador");
		}
		else {
			if(u!=null) {
				sessionMap.put("type", "jugador");
				u.setType("jugador");
			}
		}
        if(u!=null && verifyHash(pass, u.getPass())) {
    		return u;
        }
        System.out.println("La respuesta es: NULA " );
        return null;
	}
	
	public String guardar (Usuario user) {
		
		String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/registrarse";
		Client client = ClientBuilder.newClient();
        System.out.println("La contraseña es: " + user.getPass() + " El hash es:  " + hash(user.getPass()) + " "+ user.getEmail());
        user.setPassword(hash(user.getPass()));
        WebTarget target= client.target(urlRestService);
        Response response = target.request().post(Entity.entity(Entity.json(user), MediaType.APPLICATION_JSON));
        String response2 = response.readEntity(String.class);
        System.out.println("La respuesta es: " + response2);
        return  "/faces/index.xhtml";
	}

	public List<Usuario> obtenerUsuarios() {
	
        return new UsuarioDAO().obtenerUsuarios();
	}

	public String mostrarPropios() {
		System.out.println("Cargo los juegossssss ");
		return "/faces/listarpropios.xhtml";
	}
	
	public List<Juego> obtenerJuegosJugador(){
		HttpSession session = SessionUtils.getSession();
		String nick = (String)session.getAttribute("username");
		String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/juegosusuario/" + nick;
		Client client = ClientBuilder.newClient();
		WebTarget target= client.target(urlRestService);
        Response response = target.request().get();
        String response2 = response.readEntity(String.class);
        Juego[] j = null;
        if(response2!=null && !response2.isEmpty()) {
        	 j = new Gson().fromJson(response2, Juego[].class);
        }
        List<Juego> datos = null;
        if(j!=null) {
        	datos = Arrays.asList(j);
        }
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
        form.param("email", Usuario.getEmail());
        form.param("pass", Usuario.getPass());
        Response response = target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
        System.out.println("LA RESPUESTA ES: " + response.getStatus());
		return "/faces/index.xhtml";
	}
	
	// eliminar un Usuario
	public String eliminar(String nick) {
		System.out.println("Usuario eliminado..");
		return "/faces/index.xhtml";
	}

}
