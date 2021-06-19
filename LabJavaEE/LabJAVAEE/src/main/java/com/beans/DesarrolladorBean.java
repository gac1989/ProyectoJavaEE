package com.beans;

import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;


import com.google.gson.Gson;
import com.login.SessionUtils;
import com.model.DevStat;
import com.model.Juego;
import com.model.Venta;
import com.utils.ClientControl;

@ManagedBean(name = "DesarrolladorBean")
@RequestScoped
public class DesarrolladorBean {

	private int juego=0;
	private String user;
	private List<DevStat> estadisticas = this.obtenerEstadisticas();
	private double total = 0;
	private List<Venta> ventasjuego = this.obtenerVentasJuego();
	
	public List<Venta> getVentasjuego() {
		return ventasjuego;
	}

	public void setVentasjuego(List<Venta> ventasjuego) {
		this.ventasjuego = ventasjuego;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public List<DevStat> getEstadisticas() {
		return estadisticas;
	}

	public void setEstadisticas(List<DevStat> estadisticas) {
		this.estadisticas = estadisticas;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user=user;
	}

	public int getJuego() {
		return juego;
	}

	public void setJuego(int juego) {
		this.juego = juego;
	}

	
	public String mostrarJuego() {
		return user;
	}

	public List<Venta> obtenerVentasJuego(){
		if(juego!=0 && this.ventasjuego==null) {
			String urlRestService = "http://localhost:8080/rest-lab/api/recursos/ventasjuego";
			HttpSession session = SessionUtils.getSession();
			String nick = (String)session.getAttribute("username");
			Form form = new Form();
	        form.param("juego", String.valueOf(juego));
	        form.param("nick", nick);
	        Response response = new ClientControl().realizarPeticion(urlRestService, "POST", form);
	        String response2 = response.readEntity(String.class);
	        Venta[] u = new Gson().fromJson(response2, Venta[].class);
	        List<Venta> datos=null;
	        if(u!=null) {
	        	datos = Arrays.asList(u);
	        }
	        this.ventasjuego=datos;
			return datos;
		}
		return ventasjuego;
	}
	
	public List<DevStat> obtenerEstadisticas(){
		if(estadisticas==null) {
			String urlRestService = "http://localhost:8080/rest-lab/api/recursos/devstats";
			HttpSession session = SessionUtils.getSession();
			String nick = (String)session.getAttribute("username");
			Form form = new Form();
	        form.param("nick", nick);
	        Response response = new ClientControl().realizarPeticion(urlRestService, "POST", form);
	        String response2 = response.readEntity(String.class);
	        DevStat[] u = new Gson().fromJson(response2, DevStat[].class);
	        List<DevStat> datos=null;
	        if(u!=null) {
	        	datos = Arrays.asList(u);
	        }
			return datos;
		}
		return estadisticas;
	}
	
	public void solicitarDesbloqueo(int id){
		String urlRestService = "http://localhost:8080/rest-lab/api/recursos/solicitardesbloqueo";
		Form form = new Form();
        form.param("id", String.valueOf(id));
        new ClientControl().realizarPeticion(urlRestService, "POST", form);
	}
	
	public int obtenerTotal() {
		if(estadisticas!=null) {
			double total = 0;
			int ventastotal = 0;
			for(DevStat stat : estadisticas) {
				total+=stat.getTotal();
				ventastotal+=stat.getVentas();
			}
			this.total=total;
			return ventastotal;
		}
		return 0;
	}
	
	public List<Juego> obtenerJuegosDesarrollador(){
		String urlRestService = "http://localhost:8080/rest-lab/api/recursos/juegosdesarrollador/" + user;
        Response response = new ClientControl().realizarPeticion(urlRestService, "GET", null);
        String response2 = response.readEntity(String.class);
        Juego[] j = null;
        if(response2!=null && !response2.isEmpty()) {
        	 j = new Gson().fromJson(response2, Juego[].class);
        }
        List<Juego> datos = null;
        if(j!=null) {
        	datos = Arrays.asList(j);
        }
        return datos;
	}
	
}
