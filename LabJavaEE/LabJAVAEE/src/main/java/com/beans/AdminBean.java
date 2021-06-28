package com.beans;

import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.model.AdminStats;
import com.model.DevStat;
import com.model.Juego;
import com.utils.ClientControl;
import com.utils.GsonHelper;

@ManagedBean(name = "AdminBean")
@RequestScoped
public class AdminBean {
	
	private AdminStats stats = null;
	
	private List<DevStat> specstat = null;
	
	private List<DevStat> statventas = null;
	
	private List<Juego> reportados = null;
	
	private Gson json = GsonHelper.customGson;
	
	public List<Juego> getReportados() {
		if(reportados==null) {
			reportados=this.obtenerJuegosReportados();
		}
		return reportados;
	}

	public void setReportados(List<Juego> reportados) {
		this.reportados = reportados;
	}
	
	public List<DevStat> getStatventas() {
		if(statventas==null) {
			statventas=this.obtenerStatsVentas();
		}
		return statventas;
	}

	public void setStatventas(List<DevStat> statventas) {
		this.statventas = statventas;
	}

	public List<DevStat> getSpecstat() {
		if(specstat==null) {
			specstat=this.obtenerStats();
		}
		return specstat;
	}

	public void setSpecstat(List<DevStat> specstat) {
		this.specstat = specstat;
	}

	private String type = "juego";
	
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
        System.out.println("El tipo es: " + type);
		this.type = type;
	}

	public AdminStats getStats() {
		if(stats==null) {
			stats=this.obtenerEstadisticas();
		}
		return stats;
	}

	public void testing() {
		 System.out.println("El tipo actualizado es: " + type);
	}

	public void setStats(AdminStats stats) {
		this.stats = stats;
	}

	public List<DevStat> obtenerStats(){
		if(type!=null && specstat==null) {
			String urlRestService = null;
			if(type.equals("juego")) {
				urlRestService = "http://localhost:8080/rest-lab/api/recursos/adminstatsjuego";
			}
			else if(type.equals("dev")){
				urlRestService = "http://localhost:8080/rest-lab/api/recursos/adminstatsdev";
			}
			Response response = new ClientControl().realizarPeticion(urlRestService, "POST", null);
	        String response2 = response.readEntity(String.class);
	        DevStat[] lista=null;
	        if(response2 != null && !response2.isEmpty()) {
	        	lista = new Gson().fromJson(response2, DevStat[].class);
	        }
	        specstat = Arrays.asList(lista);
		}
		return specstat;
	}
	
	public List<DevStat> obtenerStatsVentas(){
		if(statventas==null) {
			String urlRestService = "http://localhost:8080/rest-lab/api/recursos/adminstatsventas";
			Response response = new ClientControl().realizarPeticion(urlRestService, "POST", null);
	        String response2 = response.readEntity(String.class);
	        DevStat[] lista=null;
	        if(response2 != null && !response2.isEmpty()) {
	        	lista = new Gson().fromJson(response2, DevStat[].class);
	        }
	        statventas = Arrays.asList(lista);
		}
		return statventas;
	}

	public AdminStats obtenerEstadisticas() {
		if(stats==null) {
			String urlRestService = "http://localhost:8080/rest-lab/api/recursos/adminstats";
			Response response = new ClientControl().realizarPeticion(urlRestService, "POST", null);
	        String response2 = response.readEntity(String.class);
	        AdminStats stats = null;
	        if(response2 != null && !response2.isEmpty()) {
	        	stats = new Gson().fromJson(response2, AdminStats.class);
	        }
	        return stats;
		}
		return stats;
	}
	
	public List<Juego> obtenerJuegosReportados(){
		if(reportados==null) {
			String urlRestService = "http://localhost:8080/rest-lab/api/recursos/juegosreportados";
			Response response = new ClientControl().realizarPeticion(urlRestService, "GET", null);
			String response2 = response.readEntity(String.class);
	        Juego[] c = null;
	        if(response2!=null && !response2.isEmpty()){
	        	c = json.fromJson(response2, Juego[].class);
	        }
	        List<Juego> datos = null;
	        if(c!=null) {
	        	datos = Arrays.asList(c);
	        }
	        reportados=datos;
		}
		return reportados;
	}
	
	public String bloquearJuego(int id) {
		String urlRestService = "http://localhost:8080/rest-lab/api/recursos/bloquearjuego";
		Form form = new Form();
        form.param("id", String.valueOf(id));
        new ClientControl().realizarPeticion(urlRestService, "POST", form);
        return "/faces/Admin/juegos.xhtml?faces-redirect=true";
	}
	
	public String desbloquearJuego(int id) {
		String urlRestService = "http://localhost:8080/rest-lab/api/recursos/desbloquearjuego";
		Form form = new Form();
        form.param("id", String.valueOf(id));
        new ClientControl().realizarPeticion(urlRestService, "POST", form);
        return "/faces/Admin/juegos.xhtml?faces-redirect=true";
	}
	
	
}
