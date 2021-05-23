package com.beans;

import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.model.Juego;

@ManagedBean(name = "BusquedaBean")
@SessionScoped
public class BusquedaBean {
	private String busqueda = null;
	private List<Juego> resultado= null;
	
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
		String urlRestService = "http://localhost:8080/rest-lab/api/ejemplo/buscadorJuegos/" + busqueda;
		System.out.println("ACA ESTAMOS Y LA BUSQUEDA ES: " + busqueda);
		Client client = ClientBuilder.newClient();
		WebTarget target= client.target(urlRestService);
        Response response = target.request().get();
        String response2 = response.readEntity(String.class);
        Juego[] j = null;
        System.out.println("RESPUESTA: " + response2);
        if(response2!=null && !response2.isEmpty()){
        	j = new Gson().fromJson(response2, Juego[].class);
        }
        List<Juego> datos = null;
        if(j!=null) {
        	datos = Arrays.asList(j);
        	System.out.println("RESULTADO: " + datos.get(0).getNombre());
        }
		return datos;
	}
	
	public String resultadoBusqueda(String busqueda1) {
		System.out.println("La busqueda es:    " + busqueda);
		this.resultado=this.buscadorJuego(busqueda);
		return "/faces/listarBusqueda.xhtml";
	}
	
}
