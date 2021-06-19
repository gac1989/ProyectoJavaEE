package com.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.model.file.UploadedFiles;

import com.google.gson.Gson;
import com.login.SessionUtils;
import com.model.Imagen;
import com.model.Juego;
import com.utils.ClientControl;
import com.utils.GsonHelper;

@ManagedBean(name = "juegoBean")
@RequestScoped
public class JuegoBean implements Serializable{
	private static final long serialVersionUID = 5443351151396868724L;
	private List<Juego> juegos = null;
	private List<Juego> juegosjugador = null;
	private List<Juego> juegosdesarrollador = null;
	private String listar = "/faces/paginasJuegos/listarJuego.xhtml";;
	private UploadedFile file;
	private UploadedFiles files;
	private String busqueda;
	private String nuevo1  = this.nuevo();
	private List<Juego> resultado = null;
	private List<Juego> ultimos = null;
	private Juego prueba = null;
	private List<String> categoria = null;
	private static Gson json = GsonHelper.customGson;

	public List<Juego> getUltimos() {
		return ultimos;
	}

	public void setUltimos(List<Juego> ultimos) {
		this.ultimos = ultimos;
	}

	public UploadedFiles getFiles() {
		return files;
	}

	public void setFiles(UploadedFiles files) {
		this.files = files;
	}

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

	public List<String> getCategoria() {
		return categoria;
	}


	public void setCategoria(List<String> categoria) {
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
	
	public String guardar (Juego juego) throws IOException {
		HttpSession session = SessionUtils.getSession();
		String nick = (String)session.getAttribute("username");
        String urlRestService2 = "http://localhost:8080/rest-lab/api/recursos/registrarjuego";
        MultipartFormDataOutput mdo = new MultipartFormDataOutput();
        int cont=0;
        if(files!=null) {
	    	 for(UploadedFile img : files.getFiles()) {
	    		 String nombre = "";
	    		 if(cont==0) {
	    			 nombre = "portada";
	    		 }
	    		 else {
	    			 nombre = "fichero" + cont; 
	    		 }
	         	 mdo.addFormData(nombre, img.getInputStream(), MediaType.APPLICATION_JSON_TYPE);
	         	 cont++;
	         }
        }
		mdo.addFormData("nombre", nick, MediaType.TEXT_PLAIN_TYPE);
		if(categoria!=null) {
			mdo.addFormData("categoria", categoria, MediaType.APPLICATION_JSON_TYPE);
		}
		mdo.addFormData("juego", juego, MediaType.APPLICATION_JSON_TYPE);
		new ClientControl().realizarPeticionMultiple(urlRestService2, mdo);
		return  "/faces/index.xhtml?faces-redirect=true";
	}
	
	
	public List<Juego> obtenerUltimosJuegos(){
		if(ultimos==null) {
			String urlRestService = "http://localhost:8080/rest-lab/api/recursos/ultimosjuegos";
	        Response response = new ClientControl().realizarPeticion(urlRestService, "GET", null);
	        String response2 = response.readEntity(String.class);
	        Juego[] j = null;
	        if(response2!=null && !response2.isEmpty()) {
	        	 j = json.fromJson(response2, Juego[].class);
	        }
	        List<Juego> datos = null;
	        if(j!=null) {
	        	datos = Arrays.asList(j);
	        }
	        ultimos=datos;
		}
		return ultimos;
	}
	
	

	public List<Juego> obtenerJuegos(){
		String urlRestService = "http://localhost:8080/rest-lab/api/recursos/juegos";
        Response response = new ClientControl().realizarPeticion(urlRestService, "GET", null);
        String response2 = response.readEntity(String.class);
        Gson json2 = GsonHelper.customGson;
        Juego[] j = json2.fromJson(response2, Juego[].class);
        List<Juego> datos = null;
        if(j!=null) {
        	datos = Arrays.asList(j);
        }
        return datos;
	}

	public List<Juego> obtenerJuegosJugador(){
		HttpSession session = SessionUtils.getSession();
		String nick = (String)session.getAttribute("username");
		String urlRestService = "http://localhost:8080/rest-lab/api/recursos/juegosusuario/" + nick;
        Response response = new ClientControl().realizarPeticion(urlRestService, "GET", null);
        String response2 = response.readEntity(String.class);
        Juego[] j = null;
        if(response2!=null && !response2.isEmpty()) {
        	 j = json.fromJson(response2, Juego[].class);
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
		String urlRestService = "http://localhost:8080/rest-lab/api/recursos/juegosdesarrollador/" + nick;
        Response response = new ClientControl().realizarPeticion(urlRestService, "GET", null);
        String response2 = response.readEntity(String.class);
        Juego[] j = null;
        if(response2!=null && !response2.isEmpty()) {
        	 j = json.fromJson(response2, Juego[].class);
        }
        List<Juego> datos = null;
        if(j!=null) {
        	datos = Arrays.asList(j);
        }
        return datos;
	}
	
	public String listarJuegos() {
		this.juegos=this.obtenerJuegos();
		return "/faces/paginasJuegos/listarJuego.xhtml";
	}
	
	public static Juego buscarJuego(int id) {
		String urlRestService = "http://localhost:8080/rest-lab/api/recursos/buscarJuego";
        Form form = new Form();
        form.param("id", String.valueOf(id));
        Response response = new ClientControl().realizarPeticion(urlRestService, "POST", form);
        String response2 = response.readEntity(String.class);
        Juego j = json.fromJson(response2, Juego.class);
		return j;
	}

	public List<Imagen> obtenerImagenesJuego(Juego j1){
		if(j1!=null) {
			String urlRestService = "http://localhost:8080/rest-lab/api/recursos/imagenesjuego/" + j1.getId();
			Response response = new ClientControl().realizarPeticion(urlRestService, "GET", null);
			String string = response.readEntity(String.class);
			Imagen[] imagenes = null;
			if(string!=null && !string.isEmpty()) {
				 imagenes = json.fromJson(string,Imagen[].class);
			}
			if(imagenes!=null) {
				List<Imagen> lista = new ArrayList<Imagen>(Arrays.asList(imagenes));
				Imagen portada = new Imagen();
				portada.setContenido(j1.getImagen());
				lista.add(0, portada);
				return lista;
			}
		}
		return null;
	}
	
	public void reportarJuego(int id) {
		String urlRestService = "http://localhost:8080/rest-lab/api/recursos/reportarjuego";
		Form form = new Form();
        form.param("id", String.valueOf(id));
        new ClientControl().realizarPeticion(urlRestService, "POST", form);
	}
	
	
}
