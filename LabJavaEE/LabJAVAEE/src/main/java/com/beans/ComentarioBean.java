package com.beans;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.login.SessionUtils;
import com.model.Comentario;
import com.utils.ClientControl;

@ManagedBean(name = "ComentarioBean")
@RequestScoped
public class ComentarioBean {
	private String texto = null;
	private int nota = 0;
	private boolean done;
	private boolean done2=false;
	private List<Comentario> reportados = null ;
	private List<Comentario> comentarios = null;
	
	public List<Comentario> getComentarios() {
		return comentarios;
	}

	public void setComentarios(List<Comentario> comentarios) {
		this.comentarios = comentarios;
	}

	public List<Comentario> getReportados() {
		if(this.reportados==null) {
			reportados=this.obtenerComentariosReportados();
		}
		return reportados;
	}

	public void setReportados(List<Comentario> reportados) {
		this.reportados = reportados;
	}

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
		String urlRestService = "http://localhost:8080/rest-lab/api/recursos/comentarJuego";
		HttpSession session = SessionUtils.getSession();
		String nick = (String)session.getAttribute("username");
		String type = (String)session.getAttribute("type");
		if(nick!=null && !nick.isEmpty() && type != null && !type.isEmpty() && type.equals("jugador") && texto!=null && !texto.isEmpty()) {
			Form form = new Form();
	        form.param("nick", nick);
	        form.param("juegoid", String.valueOf(id));
	        form.param("texto", texto);
	        form.param("nota", String.valueOf(nota));
	        new ClientControl().realizarPeticion(urlRestService, "POST", form);
		}
		setTexto(null);
		return "/faces/perfil.xhtml?faces-redirect=true";
	}
	
	public List<Comentario> obtenerComentarios(int id){
		if(comentarios==null) {
			String urlRestService = "http://localhost:8080/rest-lab/api/recursos/comentariosJuegos/" + id;
			Response response = new ClientControl().realizarPeticion(urlRestService, "GET", null);
			String response2 = response.readEntity(String.class);
	        Comentario[] c = null;
	        if(response2!=null && !response2.isEmpty()){
	        	c = new Gson().fromJson(response2, Comentario[].class);
	        }
	        List<Comentario> datos = null;
	        if(c!=null) {
	        	datos = Arrays.asList(c);
	        	comentarios=datos;
	        }
		}
		return comentarios;
	}
	
	public boolean juegoComprado(int id) {
		String urlRestService = "http://localhost:8080/rest-lab/api/recursos/juegoComprado";
		HttpSession session = SessionUtils.getSession();
		String nick = (String)session.getAttribute("username");
		String type = (String)session.getAttribute("type");
		if(nick!=null && !nick.isEmpty() && type != null && !type.isEmpty() && type.equals("jugador")) {
			Form form = new Form();
	        form.param("nick", nick);
	        form.param("juegoid", String.valueOf(id));
	        Response response = new ClientControl().realizarPeticion(urlRestService, "POST", form);
	        String respuesta = response.readEntity(String.class);
	        this.done=respuesta.equals("true");
	        this.done2=respuesta.equals("true");
	        return respuesta.equals("true");
		}
		return false;
	}
	
	public void reportarComentario(int id) {
		String urlRestService = "http://localhost:8080/rest-lab/api/recursos/reportarcomentario";
		Form form = new Form();
        form.param("id", String.valueOf(id));
        new ClientControl().realizarPeticion(urlRestService, "POST", form);
	}
	
	public String bloquearComentario(int id) {
		String urlRestService = "http://localhost:8080/rest-lab/api/recursos/bloquearcomentario";
		Form form = new Form();
        form.param("id", String.valueOf(id));
        new ClientControl().realizarPeticion(urlRestService, "POST", form);
        return "/faces/Admin/comentarios.xhtml?faces-redirect=true";
	}
	
	public String desbloquearComentario(int id) {
		String urlRestService = "http://localhost:8080/rest-lab/api/recursos/desbloquearcomentario";
		Form form = new Form();
        form.param("id", String.valueOf(id));
        new ClientControl().realizarPeticion(urlRestService, "POST", form);
        return "/faces/Admin/comentarios.xhtml?faces-redirect=true";
	}
	
	public List<Comentario> obtenerComentariosReportados(){
		List<Comentario> datos = null;
		if(this.reportados==null) {
			String urlRestService = "http://localhost:8080/rest-lab/api/recursos/comentariosreportados/";
			Response response = new ClientControl().realizarPeticion(urlRestService, "GET", null);
			String response2 = response.readEntity(String.class);
	        Comentario[] c = null;
	        if(response2!=null && !response2.isEmpty()){
	        	c = new Gson().fromJson(response2, Comentario[].class);
	        }
	        if(c!=null) {
	        	datos = Arrays.asList(c);
	        }
	        this.reportados=datos;
		}
		return datos;
	}
	
}
