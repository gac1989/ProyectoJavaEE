package com.model;
import java.io.Serializable;

public class CompraJuegoId implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String user;
    private int juego;
    
    
    
    public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public int getJuego() {
		return juego;
	}

	public void setJuego(int juego) {
		this.juego = juego;
	}

	public int hashCode() {
        return (int)this.user.hashCode();
    }
    
    public boolean equals(Object O) {
    	if(O==this) {
    		return true;
    	}
    	if (!(O instanceof CompraJuegoId)) return false;
    	CompraJuegoId id = (CompraJuegoId)O;
    	return this.user.equals(user) && this.juego==id.juego;
    }
    
}

