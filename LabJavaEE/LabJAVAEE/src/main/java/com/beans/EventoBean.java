package com.beans;

import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.login.SessionUtils;
import com.model.Evento;
import com.model.Juego;
import com.utils.ClientControl;
import com.utils.GsonHelper;

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
	private static Gson json = GsonHelper.customGson;
	
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
			String urlRestService = "http://localhost:8080/rest-lab/api/recursos/juegosevento/" + nombreEvento;
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
	        juegosEvento=datos;
		}
		return juegosEvento;
	}
	
	public List<Juego> obtenerJuegosOferta(){
		if(juegosOferta==null) {
			String urlRestService = "http://localhost:8080/rest-lab/api/recursos/juegosoferta";
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
	        juegosOferta=datos;
		}
		return juegosOferta;
	}
	
	public String listarJuegosEvento(String nombre) {
		return "/faces/listarJuegoEvento.xhtml?faces-redirect=true&nombre=" + nombre;
	}
	
	public String finalizarEvento(String nombre) {
		String urlRestService = "http://localhost:8080/rest-lab/api/recursos/finalizarevento";
		HttpSession session = SessionUtils.getSession();
		String nick = (String)session.getAttribute("username");
		Form form = new Form();
        form.param("nombre", nombre);
        form.param("nick", nick);
        new ClientControl().realizarPeticion(urlRestService, "POST", form);
		return "/faces/Admin/eventos.xhtml?faces-redirect=true";
	}
	
	public String iniciarEvento(String nombre) {
		String urlRestService = "http://localhost:8080/rest-lab/api/recursos/iniciarevento";
		HttpSession session = SessionUtils.getSession();
		String nick = (String)session.getAttribute("username");
		Form form = new Form();
        form.param("nombre", nombre);
        form.param("nick", nick);
        new ClientControl().realizarPeticion(urlRestService, "POST", form);
		return "/faces/Admin/eventos.xhtml?faces-redirect=true";
	}
	
	public String crearEvento(String nombre, String descuento, String fecha_ini, String fecha_fin) {
		String urlRestService = "http://localhost:8080/rest-lab/api/recursos/evento";
		Form form = new Form();
        form.param("nombre", nombre);
        form.param("descuento", descuento);
        form.param("fecha_ini", fecha_ini);
        form.param("fecha_fin", fecha_fin);
        new ClientControl().realizarPeticion(urlRestService, "POST", form);
		return "/faces/Admin/eventos.xhtml?faces-redirect=true";
	}

	public List<Evento> obtenerEventos(){
		String urlRestService = "http://localhost:8080/rest-lab/api/recursos/eventos";
        Response response = new ClientControl().realizarPeticion(urlRestService, "GET", null);
        String response2 = response.readEntity(String.class);
        Evento[] u = json.fromJson(response2, Evento[].class);
        List<Evento> datos=null;
        if(u!=null) {
        	 datos = Arrays.asList(u);
        }
        return datos;
	}
	
	public String agregarJuegoEvento(String evento, String juego) {
		if(evento!=null && juego !=null) {
			String urlRestService = "http://localhost:8080/rest-lab/api/recursos/eventojuego";
			HttpSession session = SessionUtils.getSession();
			String nick = (String)session.getAttribute("username");
			Form form = new Form();
	        form.param("nombre", evento);
	        form.param("id", juego);
	        form.param("nick", nick);
	        new ClientControl().realizarPeticion(urlRestService, "POST", form);
			return "/faces/perfilDesarrollador.xhtml?faces-redirect=true";
		}
		return "";
	}
	
	public String quitarJuegoEvento(String juego) {
		String urlRestService = "http://localhost:8080/rest-lab/api/recursos/quitarjuegoevento";
		HttpSession session = SessionUtils.getSession();
		String nick = (String)session.getAttribute("username");
		Form form = new Form();
        form.param("id", juego);
        form.param("nick", nick);
        new ClientControl().realizarPeticion(urlRestService, "POST", form);
		return "/faces/perfilDesarrollador.xhtml?faces-redirect=true";
	}
	
}
