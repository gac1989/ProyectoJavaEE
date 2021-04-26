package com.beans;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
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
import com.model.Juego;

@ManagedBean(name = "juegoBean")
@RequestScoped
public class JuegoBean {

	private List<Juego> juegos = this.obtenerJuegos();
	private List<Juego> resultado =null;
	private String listar =  this.listarJuegos();
	private UploadedFile file;
	private String busqueda;
	
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


	public String nuevo() {
		Juego j= new Juego();
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.put("juego", j);
		return  "/faces/paginasJuegos/nuevoJuego.xhtml";
	}
	
	public void prueba() {
	   URL directory = this.getClass().getResource("tmp.png");
	   File archivo = new File(directory.getPath());
	   System.out.println("LA RUTA ES" + archivo.getAbsolutePath() + " Y EL ARCHIVO SE LLAMA: " + archivo.getName());
	}
	
	
	public String guardar (Juego juego) throws IOException {
		if (file != null) {
            String message = "Successful " + file.getFileName() + " is uploaded.";
            System.out.println(message);
            juego.setRutaImg(file.getFileName());
        }
		String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/registrarJuego";
		Client client = ClientBuilder.newClient();
        Form form = new Form();
        System.out.println("El juego es: " + juego.getNombre());
        System.out.println("La imagen es: " + juego.getRutaImg());
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String path = servletContext.getContextPath();
        System.out.println("El path es: " + System.getProperty("user.dir"));
        form.param("nombre", juego.getNombre());
        form.param("descripcion", juego.getDescripcion());
        form.param("rutaImg", juego.getRutaImg());
        form.param("precio", String.valueOf(juego.getPrecio()));
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
  
		return  "/faces/index.xhtml";
	}

	public List<Juego> obtenerJuegos(){
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

	public List<Juego> buscadorJuego(String busqueda){
		System.out.println("Estoy en buscadorJuego() " + busqueda);	
		String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/buscadorJuegos/" + busqueda;
		Client client = ClientBuilder.newClient();
		WebTarget target= client.target(urlRestService);
        Response response = target.request().get();
        String response2 = response.readEntity(String.class);
        Juego[] j = new Gson().fromJson(response2, Juego[].class);
		List<Juego> datos = Arrays.asList(j);
		return datos;
	}
	
	public String listarJuegos() {
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.put("juegos",juegos);
		return "/faces/paginasJuegos/listarJuego.xhtml";
	}
	
	public String resultadoBusqueda() {
		System.out.println("Estoy en resultadoBusqueda() " + busqueda);
		this.juegos=this.buscadorJuego(busqueda);
		return "/faces/paginasJuegos/listarJuego.xhtml";
	}
	

	public String buscarJuego(int id) {
		String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/buscarJuego";
		Client client = ClientBuilder.newClient();
		System.out.println("El id es:  " + id);
        Form form = new Form();
        form.param("id", String.valueOf(id));
        WebTarget target= client.target(urlRestService);
        Response response = target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
        String response2 = response.readEntity(String.class);
        System.out.println("La respuesta es:  " + response2);
        Juego j = new Gson().fromJson(response2, Juego.class);
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.put("juego",j);
		return "/faces/paginasJuegos/comprarJuego.xhtml";
	
	}
	
}
