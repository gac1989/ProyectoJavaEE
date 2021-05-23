package com.model;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.persistence.InheritanceType;


@Entity
@Table(name="usuario")
@Inheritance(strategy=InheritanceType.JOINED)
@XmlRootElement
public class Usuario {
	@Id
	private String nick;
	@Column
	private String password;
	@Column
	private String email;
	@Column
	private String rutaImg;
	
	
	public String getRutaImg() {
		return rutaImg;
	}

	public void setRutaImg(String rutaImg) {
		this.rutaImg = rutaImg;
	}

	public String getNick() {
		return nick;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Cliente [id=" + nick + 
				", email=" + email;
	}
	
}
