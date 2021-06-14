package com.login;

import java.io.Serializable;
import java.util.Map;

import com.beans.UsuarioBean;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.model.Desarrollador;
import com.model.Jugador;
import com.model.Usuario;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;




@ManagedBean
@SessionScoped
public class Login implements Serializable {

	private static final long serialVersionUID = 1094801825228386363L;
	
	private String pwd;
	private String msg;
	private String user;
	private Usuario usuario;
	private String validateUsernamePassword = this.validateUsernamePassword();
	
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	
	public static Usuario checkUser(String nick, String pass) {
		String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/checkusuario";
		Client client = ClientBuilder.newClient();
        WebTarget target= client.target(urlRestService);
        Form form = new Form();
        form.param("nick", nick);
        form.param("pass", pass);
        Response response = target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
        String response2 = response.readEntity(String.class);
        JsonObject convertedObject = null;
        if(response2 != null && !response2.isEmpty()) {
        	convertedObject = new Gson().fromJson(response2, JsonObject.class);
        }
        JsonElement type=null;
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        Usuario u = null;
        if(convertedObject!=null) {
        	type = convertedObject.get("pais");
        	if(type!=null) {
             	sessionMap.put("type", "desarrollador");
     			u = new Gson().fromJson(response2, Desarrollador.class);
     			u.setType("desarrollador");
     		}
     		else {
     			if(convertedObject.get("nombre")!=null) {
     				sessionMap.put("type", "jugador");
     				u = new Gson().fromJson(response2, Jugador.class);
     				u.setType("jugador");
     			}
     			else {
     				u = new Gson().fromJson(response2, Usuario.class);
     				sessionMap.put("type", "administrador");
     				u.setType("administrador");
     			}
     		}
        }
		return u;
	}
	
	public String validateUsernamePassword() {
		boolean valid = false;
		Usuario u = null;
		HttpSession session = SessionUtils.getSession();
		session.setAttribute("type", "visitante");
		if(user!=null && pwd!=null) {
			u = checkUser(user,pwd);
			if(u!=null) {
				valid = true;
			}
		}
		if (valid) {
			session.setAttribute("username", user);
			session.setAttribute("type", u.getType());
			session.setAttribute("user", u);
			if(u.getType().equals("administrador")) {
				return "faces/Admin/admin.xhtml?faces-redirect=true";
			}
			else {
				return "faces/index.xhtml?faces-redirect=true";
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN,
							"Incorrect Username and Passowrd",
							"Please enter correct username and Password"));
			return "/faces/login.xhtml?faces-redirect=true";
		}
	}
	//logout event, invalidate session
	public String logout() {
		HttpSession session = SessionUtils.getSession();
		session.invalidate();
		return "/faces/login.xhtml?faces-redirect=true";
	}
}