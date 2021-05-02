package com.model;




public class Usuario {
	
	private String nick;
	private String email;
	private String password;
	private String type;

	public String getNick() {
		return nick;
	}

	public String getPass() {
		return password;
	}

	public void setPass(String pass) {
		this.password = pass;
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
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Usuario [nick=" + nick + ", email=" + email + ", pass=" + password + "]";
	}

	
	
}
