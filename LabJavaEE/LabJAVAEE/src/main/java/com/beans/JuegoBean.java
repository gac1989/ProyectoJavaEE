package com.beans;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
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
import com.login.SessionUtils;
import com.model.Juego;

@ManagedBean(name = "juegoBean")
@RequestScoped
public class JuegoBean implements Serializable{
	private static final long serialVersionUID = 5443351151396868724L;
	private List<Juego> juegos = null;
	private List<Juego> juegosjugador = null;
	private List<Juego> juegosdesarrollador = null;
	private String listar = "/faces/paginasJuegos/listarJuego.xhtml";;
	private UploadedFile file;
	private String busqueda;
	private String nuevo1  = this.nuevo();
	private List<Juego> resultado = null;
	private Juego prueba = null;
	private String categoria = null;
	
	
	
	public List<Juego> getJuegosdesarrollador() {
		if(juegosdesarrollador==null) {
			juegosdesarrollador=this.obtenerJuegosDesarrollador();
		}
		return juegosdesarrollador;
	}


	public void setJuegosdesarrollador(List<Juego> juegosdesarrollador) {
		this.juegosdesarrollador = juegosdesarrollador;
	}


	public List<Juego> getJuegosjugador() {
		if(juegosjugador==null) {
			juegosjugador=this.obtenerJuegosJugador();
		}
		return juegosjugador;
	}


	public void setJuegosjugador(List<Juego> juegosjugador) {
		this.juegosjugador = juegosjugador;
	}


	public String getCategoria() {
		return categoria;
	}


	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	
	public Juego getPrueba() {
		return prueba;
	}

	public void setPrueba(Juego prueba) {
		this.prueba = prueba;
	}

	public List<Juego> getResultado() {
		return resultado;
	}

	public void setResultado(List<Juego> resultado) {
		this.resultado = resultado;
	}

	public String getNuevo1() {
		return nuevo1;
	}

	public void setNuevo1(String nuevo1) {
		this.nuevo1 = nuevo1;
	}

	public String getListar() {
		return listar;
	}

	public void setListar(String listar) {
		this.listar = listar;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public List<Juego> getJuegos() {
		if(juegos==null) {
			juegos=this.obtenerJuegos();
		}
		return juegos;
	}

	public void setJuegos(List<Juego> juegos) {
		this.juegos = juegos;
	}

	public String getBusqueda() {
		return busqueda;
	}

	public void setBusqueda(String busqueda) {
		this.busqueda = busqueda;
	}
	
	public String perfilJuego(Juego j) {
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.put("perfil", j);
		return  "/faces/perfil.xhtml?faces-redirect=true";
	}
	
	public String nuevo() {
		Juego j= new Juego();
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.put("juego", j);
		return  "/faces/paginasJuegos/nuevoJuego.xhtml";
	}
	
	public void prueba() {
	   URL directory = this.getClass().getResource("tmp.png");
	   File archivo = new File(directory.getPath());
	}
	
	
	public String guardar (Juego juego) throws IOException {
		if (file != null) {
            String message = "Successful " + file.getFileName() + " is uploaded.";
            System.out.println(message);
            juego.setRutaImg(file.getFileName());
        }
		HttpSession session = SessionUtils.getSession();
		String nick = (String)session.getAttribute("username");
		String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/registrarJuego";
		Client client = ClientBuilder.newClient();
        Form form = new Form();
        System.out.println("El juego es: " + juego.getNombre());
        System.out.println("La imagen es: " + juego.getRutaImg());
        System.out.println("La categorias es: " + this.categoria);
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String path = servletContext.getContextPath();
        System.out.println("El path es: " + System.getProperty("user.dir"));
        form.param("nick", nick);
        form.param("nombre", juego.getNombre());
        form.param("descripcion", juego.getDescripcion());
        form.param("rutaImg", juego.getRutaImg());
        form.param("precio", String.valueOf(juego.getPrecio()));
        form.param("categoria", this.categoria);
        WebTarget target= client.target(urlRestService);
        Response response = target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
        URL directory = this.getClass().getResource("tmp.png");
        File archivo = new File(directory.getPath());
        System.out.println("LA RUTA ES" + archivo.getAbsolutePath() + " Y EL ARCHIVO ES: " + archivo.getName());
        try {
			Files.copy(file.getInputStream(),archivo.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //
        String urlRestService2 = "http://localhost:8080/rest-lab/api/ejemplo/subir";
        WebTarget target2= client.target(urlRestService2);
        MultipartFormDataOutput mdo = new MultipartFormDataOutput();
		mdo.addFormData("fichero", archivo, MediaType.APPLICATION_OCTET_STREAM_TYPE);
		mdo.addFormData("nombre", file.getFileName(), MediaType.TEXT_PLAIN_TYPE);
		GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(mdo) { };
		Response response2 = target2.request().post(Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE));
        String response3 = response.readEntity(String.class);
		System.out.println("La respuesta al subir el juego es: " + response2.getStatus());
  
		return  "/faces/index.xhtml?faces-redirect=true";
	}

	public List<Juego> obtenerJuegos(){
		System.out.println("LaLALALALlalalLALALAL EENTREEEEEE");
		String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/juegos";
		Client client = ClientBuilder.newClient();
		WebTarget target= client.target(urlRestService);
        Response response = target.request().get();
        String response2 = response.readEntity(String.class);
        Juego[] j = new Gson().fromJson(response2, Juego[].class);
        List<Juego> datos = null;
        if(j!=null) {
        	datos = Arrays.asList(j);
        }
        return datos;
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
	
	public List<Juego> obtenerJuegosDesarrollador(){
		HttpSession session = SessionUtils.getSession();
		String nick = (String)session.getAttribute("username");
		String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/juegosdesarrollador/" + nick;
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
        System.out.println("La respuesta es: " + response.getStatus());
        return datos;
	}
	
	public String listarJuegos() {
		System.out.println("LISTARRRRR");
		this.juegos=this.obtenerJuegos();
		return "/faces/paginasJuegos/listarJuego.xhtml";
	}
	
	public static Juego buscarJuego(int id) {
		String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/buscarJuego";
		Client client = ClientBuilder.newClient();
		System.out.println("El identificador es: " + id);
        Form form = new Form();
        form.param("id", String.valueOf(id));
        WebTarget target= client.target(urlRestService);
        Response response = target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
        String response2 = response.readEntity(String.class);
        Juego j = new Gson().fromJson(response2, Juego.class);
		return j;
	}

	
}
