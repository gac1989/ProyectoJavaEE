package com.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.ws.rs.core.Form;

import com.model.Juego;
import com.utils.ClientControl;

@ManagedBean(name = "ComprarJuego")
@SessionScoped
public class ComprarJuego implements Serializable{
	
	private static final long serialVersionUID = 5443351151396868724L;
	private Juego j = null;
	
	
	public Juego getJ() {
		return j;
	}

	public void setJ(Juego j) {
		this.j = j;
	}

	public static String comprar(String idjuego, String nick) throws IOException {
		String urlRestService = "http://localhost:8080/rest-lab/api/recursos/comprarjuego";
		Form form = new Form();
        form.param("nick", nick);
        form.param("id", idjuego);
        new ClientControl().realizarPeticion(urlRestService, "POST", form);
        return "";
	}
	
	public String buscarJuego(Juego j) {
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.put("juego",j);
		return "/faces/comprar.xhtml?faces-redirect=true&nombreJuego=" + j.getNombre() + "&precioJuego=" + j.getOferta() + "&id=" + j.getId();
	}
}
