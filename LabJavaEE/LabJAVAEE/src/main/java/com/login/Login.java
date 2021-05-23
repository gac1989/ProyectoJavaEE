package com.login;

import java.io.Serializable;
import com.beans.UsuarioBean;
import com.model.Usuario;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;



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

	//validate login
	public String validateUsernamePassword() {
		boolean valid = false;
		Usuario u = null;
		HttpSession session = SessionUtils.getSession();
		session.setAttribute("type", "visitante");
		if(user!=null) {
			u = UsuarioBean.checkUser(user,pwd);
		}
		if(user!=null && u!=null) {
			valid = true;
		}
		if (valid) {
			session.setAttribute("username", user);
			session.setAttribute("sessionid", session.getId());
			session.setAttribute("type", u.getType());
			session.setAttribute("user", u);
			System.out.println("USUARIO: " + user + " TIPO: " + u.getType());
			if(u.getType().equals("administrador")) {
				return "faces/Admin/admin.xhtml?faces-redirect=true";
			}
			else {
				return "faces/index.xhtml";
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN,
							"Incorrect Username and Passowrd",
							"Please enter correct username and Password"));
			return "login";
		}
	}
	 public String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

	 public boolean verifyHash(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }
	//logout event, invalidate session
	public String logout() {
		HttpSession session = SessionUtils.getSession();
		session.invalidate();
		return "/faces/login.xhtml?faces-redirect=true";
	}
}