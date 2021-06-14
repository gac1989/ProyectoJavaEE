package com.beans;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.login.SessionUtils;
import com.model.Evento;
import com.model.Juego;

@ManagedBean(name = "EventoBean")
@RequestScoped
public class EventoBean {
	
	private String nombre;
	private String descuento;
	private String fecha_ini;
	private String fecha_fin;
	private List<Evento> eventos = this.obtenerEventos();
	private String seleccionado;
	private String eventoseleccionado;
	private List<Juego> juegosEvento = null;
	private List<Juego> juegosOferta = null;
	private static Gson json = new GsonBuilder().registerTypeAdapter(Date.class, UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter()).create();
	
	
	
	public List<Juego> getJuegosOferta() {
		return juegosOferta;
	}

	public void setJuegosOferta(List<Juego> juegosOferta) {
		this.juegosOferta = juegosOferta;
	}

	public String getSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(String seleccionado) {
		this.seleccionado = seleccionado;
	}

	public String getEventoseleccionado() {
		return eventoseleccionado;
	}

	public void setEventoseleccionado(String eventoseleccionado) {
		this.eventoseleccionado = eventoseleccionado;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescuento() {
		return descuento;
	}

	public void setDescuento(String descuento) {
		this.descuento = descuento;
	}

	public String getFecha_ini() {
		return fecha_ini;
	}

	public void setFecha_ini(String fecha_ini) {
		this.fecha_ini = fecha_ini;
	}

	public String getFecha_fin() {
		return fecha_fin;
	}

	public void setFecha_fin(String fecha_fin) {
		this.fecha_fin = fecha_fin;
	}
	
	public List<Evento> getEventos() {
		return eventos;
	}

	public void setEventos(List<Evento> eventos) {
		this.eventos = eventos;
	}
	
	public List<Juego> getJuegosEvento() {
		return juegosEvento;
	}

	public void setJuegosEvento(List<Juego> juegosEvento) {
		this.juegosEvento = juegosEvento;
	}
	
	
	public List<Juego> obtenerJuegosEvento(String nombreEvento){
		if(juegosEvento==null) {
			String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/juegosevento/" + nombreEvento;
			Client client = ClientBuilder.newClient();
			WebTarget target= client.target(urlRestService);
	        Response response = target.request().get();
	        String response2 = response.readEntity(String.class);
	        Juego[] j = null;
	        if(response2!=null && !response2.isEmpty()) {
	        	 j = json.fromJson(response2, Juego[].class);
	        }
	        List<Juego> datos = null;
	        if(j!=null) {
	        	datos = Arrays.asList(j);
	        }
			//System.out.println("IMPRESION ");
	        juegosEvento=datos;
		}
		return juegosEvento;
	}
	
	public List<Juego> obtenerJuegosOferta(){
		if(juegosOferta==null) {
			String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/juegosoferta";
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
			//System.out.println("IMPRESION ");
	        juegosOferta=datos;
		}
		return juegosOferta;
	}
	
	
	public String listarJuegosEvento(String nombre) {
		return "/faces/listarJuegoEvento.xhtml?faces-redirect=true&nombre=" + nombre;
	}
	
	public String finalizarEvento(String nombre) {
		String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/finalizarevento";
		HttpSession session = SessionUtils.getSession();
		String nick = (String)session.getAttribute("username");
		Client client = ClientBuilder.newClient();
		WebTarget target= client.target(urlRestService);
		Form form = new Form();
        form.param("nombre", nombre);
        form.param("nick", nick);
        Response response = target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
        String response2 = response.readEntity(String.class);
        System.out.println("La respuesta es:  " + response2);
		return "/faces/Admin/eventos.xhtml?faces-redirect=true";
	}
	
	public String iniciarEvento(String nombre) {
		String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/iniciarevento";
		HttpSession session = SessionUtils.getSession();
		String nick = (String)session.getAttribute("username");
		Client client = ClientBuilder.newClient();
		WebTarget target= client.target(urlRestService);
		Form form = new Form();
        form.param("nombre", nombre);
        form.param("nick", nick);
        Response response = target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
        String response2 = response.readEntity(String.class);
        System.out.println("La respuesta es:  " + response2);
		return "/faces/Admin/eventos.xhtml?faces-redirect=true";
	}
	
	public String crearEvento(String nombre, String descuento, String fecha_ini, String fecha_fin) {
		System.out.println("El nombre es: " + nombre + " La fecha inicial es: " + fecha_ini);
		String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/evento";
		Client client = ClientBuilder.newClient();
		WebTarget target= client.target(urlRestService);
		Form form = new Form();
        form.param("nombre", nombre);
        form.param("descuento", descuento);
        form.param("fecha_ini", fecha_ini);
        form.param("fecha_fin", fecha_fin);
        Response response = target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
        String response2 = response.readEntity(String.class);
        System.out.println("La respuesta es:  " + response2);
		return "/faces/Admin/eventos.xhtml?faces-redirect=true";
	}

	public List<Evento> obtenerEventos(){
		String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/eventos";
		Client client = ClientBuilder.newClient();
		WebTarget target= client.target(urlRestService);
        Response response = target.request().get();
        String response2 = response.readEntity(String.class);
        Gson json = new GsonBuilder().registerTypeAdapter(Date.class, UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter()).create();
        Evento[] u = json.fromJson(response2, Evento[].class);
        List<Evento> datos=null;
        if(u!=null) {
        	 datos = Arrays.asList(u);
        }
		System.out.println("IMPRESION ");
        return datos;
	}
	
	public String agregarJuegoEvento(String evento, String juego) {
		if(evento!=null && juego !=null) {
			System.out.println("El evento es: " + evento + " Y el juego es: " + juego);
			String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/eventojuego";
			Client client = ClientBuilder.newClient();
			WebTarget target= client.target(urlRestService);
			HttpSession session = SessionUtils.getSession();
			String nick = (String)session.getAttribute("username");
			Form form = new Form();
	        form.param("nombre", evento);
	        form.param("id", juego);
	        form.param("nick", nick);
	        Response response = target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
	        String response2 = response.readEntity(String.class);
	        System.out.println("La respuesta es:  " + response.getStatus());
			return "/faces/perfilDesarrollador.xhtml?faces-redirect=true";
		}
		return "";
	}
	
	public String quitarJuegoEvento(String juego) {
		System.out.println("el juego es: " + juego);
		String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/quitarjuegoevento";
		Client client = ClientBuilder.newClient();
		WebTarget target= client.target(urlRestService);
		HttpSession session = SessionUtils.getSession();
		String nick = (String)session.getAttribute("username");
		Form form = new Form();
        form.param("id", juego);
        form.param("nick", nick);
        Response response = target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
        String response2 = response.readEntity(String.class);
        System.out.println("La respuesta es:  " + response.getStatus());
		return "/faces/perfilDesarrollador.xhtml?faces-redirect=true";
	}
	
	
}
