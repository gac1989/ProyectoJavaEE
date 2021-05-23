package com.beans;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.login.SessionUtils;
import com.model.Comentario;
import com.model.Juego;

@ManagedBean(name = "ComentarioBean")
@RequestScoped
public class ComentarioBean {
	private String texto = null;
	private int nota = 0;
	private boolean done;
	private boolean done2=false;

	
	
	public boolean isDone2() {
		return done2;
	}

	public void setDone2(boolean done2) {
		this.done2 = done2;
	}

	@PostConstruct
	public void init(){
		done=true;
		done2=false;
	}
	
	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public int getNota() {
		return nota;
	}

	public void setNota(int nota) {
		this.nota = nota;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	
	
	public String publicarComentario(String texto, int id, int nota) {
		System.out.println("LLEGUE A PUBLICAR ");
		String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/comentarJuego";
		Client client = ClientBuilder.newClient();
		WebTarget target= client.target(urlRestService);
		HttpSession session = SessionUtils.getSession();
		String nick = (String)session.getAttribute("username");
		String type = (String)session.getAttribute("type");
		if(nick!=null && !nick.isEmpty() && type != null && !type.isEmpty() && type.equals("jugador") && texto!=null && !texto.isEmpty()) {
			Form form = new Form();
	        form.param("nick", nick);
	        form.param("juegoid", String.valueOf(id));
	        form.param("texto", texto);
	        form.param("nota", String.valueOf(nota));
	        Response response = target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
	        System.out.println("La respuesta es: " + response.getStatus());
		}
		setTexto(null);
		return "/faces/perfil.xhtml?faces-redirect=true";
	}
	
	public List<Comentario> obtenerComentarios(int id){
		String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/comentariosJuegos/" + id;
		Client client = ClientBuilder.newClient();
		WebTarget target= client.target(urlRestService);
		Response response = target.request().get();
		String response2 = response.readEntity(String.class);
        Comentario[] c = null;
        if(response2!=null && !response2.isEmpty()){
        	c = new Gson().fromJson(response2, Comentario[].class);
        }
        List<Comentario> datos = null;
        if(c!=null) {
        	datos = Arrays.asList(c);
        }
		return datos;
	}
	
	public boolean juegoComprado(int id) {
		System.out.println("LLEGUE A VERIFICARRRRRRRR ");
		String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/juegoComprado";
		Client client = ClientBuilder.newClient();
		WebTarget target= client.target(urlRestService);
		HttpSession session = SessionUtils.getSession();
		String nick = (String)session.getAttribute("username");
		String type = (String)session.getAttribute("type");
		if(done==true) {
			System.out.println("CASO POSITIVOOOOO ");
		}
		else {
			System.out.println("CASOO NEGATIVOOOOOO ");
		}
		if(nick!=null && !nick.isEmpty() && type != null && !type.isEmpty() && type.equals("jugador")) {
			Form form = new Form();
	        form.param("nick", nick);
	        form.param("juegoid", String.valueOf(id));
	        Response response = target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
	        String respuesta = response.readEntity(String.class);
	        System.out.println("La respuesta es: " + respuesta);
	        this.done=respuesta.equals("true");
	        this.done2=respuesta.equals("true");
	        return respuesta.equals("true");
		}
		return false;
	}
	
	
	
	
	
}
