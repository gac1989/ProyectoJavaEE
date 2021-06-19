package com.beans;

import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.model.Juego;
import com.utils.ClientControl;
import com.utils.GsonHelper;

@ManagedBean(name = "BusquedaBean")
@SessionScoped
public class BusquedaBean {
	private String busqueda = null;
	private List<Juego> resultado= null;
	private Gson json = GsonHelper.customGson;
	
	public String getBusqueda() {
		return busqueda;
	}
	
	public void setBusqueda(String busqueda) {
		this.busqueda = busqueda;
	}
	
	public List<Juego> getResultado() {
		return resultado;
	}
	
	public void setResultado(List<Juego> resultado) {
		this.resultado = resultado;
	}
	
	public List<Juego> buscadorJuego(String busqueda){
		String busquedaok = busqueda.replaceAll("[^A-Za-z0-9]","");
		String urlRestService = "http://localhost:8080/rest-lab/api/recursos/buscadorJuegos/" + busquedaok;
        Response response = new ClientControl().realizarPeticion(urlRestService, "GET", null);
        String response2 = response.readEntity(String.class);
        Juego[] j = null;
        if(response2!=null && !response2.isEmpty()){
        	j = json.fromJson(response2, Juego[].class);
        }
        List<Juego> datos = null;
        if(j!=null) {
        	datos = Arrays.asList(j);
        }
		return datos;
	}
	
	public String resultadoBusqueda(String busqueda1) {
		this.resultado=this.buscadorJuego(busqueda);
		return "/faces/listarBusqueda.xhtml";
	}
	
}
