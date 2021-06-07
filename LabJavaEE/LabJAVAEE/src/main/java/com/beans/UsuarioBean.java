package com.beans;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
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
import org.primefaces.model.file.UploadedFile;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.login.Login;
import com.login.SessionUtils;
import com.model.Desarrollador;
import com.model.Juego;
import com.model.Jugador;
import com.model.Publicacion;
import com.model.Usuario;
import com.model.UsuarioDAO;


@ManagedBean(name = "UsuarioBean", eager = true)
@SessionScoped
public class UsuarioBean {
	private List<Juego> juegos = null;
	private List<Usuario> usuarios = null;
	private String nuevoJug = this.nuevoJugador();
	private String nuevoDes = this.nuevoDesarrollador();
	private String propios = "/faces/listarpropios.xhtml";;
	private UploadedFile file;
	private String texto = null;
	

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
	
	
	public String guardar (Usuario user) {
		if (file != null) {
            String message = "Successful " + file.getFileName() + " is uploaded.";
            System.out.println(message);
            user.setRutaImg(file.getFileName());
        }
		String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/registrarse";
		Client client = ClientBuilder.newClient();
        System.out.println("La contraseña es: " + user.getPass() + " El hash es:  " + user.getPass() + " "+ user.getEmail());
        user.setPassword(user.getPass());
        WebTarget target= client.target(urlRestService);
        Response response = target.request().post(Entity.entity(Entity.json(user), MediaType.APPLICATION_JSON));
        String response2 = response.readEntity(String.class);
        System.out.println("La respuesta es: " + response2);
        //Cargar imagen
        URL directory = this.getClass().getResource("tmp.png");
        File archivo = new File(directory.getPath());
        System.out.println("LA RUTA ES" + archivo.getAbsolutePath() + " Y EL ARCHIVO ES: " + archivo.getName());
        try {
			Files.copy(file.getInputStream(),archivo.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String urlRestService2 = "http://localhost:8080/rest-lab/api/ejemplo/subir";
        WebTarget target2= client.target(urlRestService2);
        MultipartFormDataOutput mdo = new MultipartFormDataOutput();
		mdo.addFormData("fichero", archivo, MediaType.APPLICATION_OCTET_STREAM_TYPE);
		mdo.addFormData("nombre", file.getFileName(), MediaType.TEXT_PLAIN_TYPE);
		GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(mdo) { };
		Response response3 = target2.request().post(Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE));
		System.out.println("La respuesta al subir el juego es: " + response3.getStatus());
		HttpSession session = SessionUtils.getSession();
		session.setAttribute("username", user);
		session.setAttribute("type", user.getType());
		session.setAttribute("user", user);
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
		/*Form form = new Form();
        form.param("nick", Usuario.getNick());
        form.param("email", Usuario.getEmail());
        form.param("pass", Usuario.getPass());*/
        Response response = target.request().post(Entity.entity(Entity.json(Usuario), MediaType.APPLICATION_JSON));
        System.out.println("LA RESPUESTA ES: " + response.getStatus());
		return "/faces/index.xhtml";
	}
	
	// eliminar un Usuario
	public String eliminar(String nick) {
		System.out.println("Usuario eliminado..");
		return "/faces/index.xhtml";
	}
	
	
	
	public String agregarPublicacion(String texto) throws FileNotFoundException {
		String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/agregarpublicacion";
		Client client = ClientBuilder.newClient();
		WebTarget target= client.target(urlRestService);
		HttpSession session = SessionUtils.getSession();
		String nick = (String)session.getAttribute("username");
		Form form = new Form();
		System.out.println("El texto es: " + texto + " El user es: " + nick);
        form.param("nick", nick);
        form.param("texto", texto);
        form.param("imagen", file.getFileName());
        Response response = target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
        System.out.println("LA RESPUESTA ES: " + response.getStatus());
        //Cargar imagen
        URL directory = this.getClass().getResource("tmp.png");
        File archivo = new File(directory.getPath());
        System.out.println("LA RUTA ES" + archivo.getAbsolutePath() + " Y EL ARCHIVO ES: " + archivo.getName());
        try {
			Files.copy(file.getInputStream(),archivo.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if(file.getFileName()!=null) {
	    	String urlRestService2 = "http://localhost:8080/rest-lab/api/ejemplo/subir";
	        WebTarget target2= client.target(urlRestService2);
	        MultipartFormDataOutput mdo = new MultipartFormDataOutput();
     		mdo.addFormData("fichero", archivo, MediaType.APPLICATION_OCTET_STREAM_TYPE);
     		mdo.addFormData("nombre", file.getFileName(), MediaType.TEXT_PLAIN_TYPE);
     		GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(mdo) { };
     		Response response3 = target2.request().post(Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE));
     		System.out.println("La respuesta al subir el juego es: " + response3.getStatus());
        }
        return "/faces/perfilJugador.xhtml?faces-redirect=true";
	}
	
}
