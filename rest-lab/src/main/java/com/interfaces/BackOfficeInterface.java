package com.interfaces;

import java.text.ParseException;
import java.util.List;

import javax.ejb.Local;

import com.model.AdminStats;
import com.model.Chart;
import com.model.Comentario;
import com.model.DevStat;

@Local
public interface BackOfficeInterface {

	public List<Chart> getDatosFecha();
	
	public List<Chart> getIngresosFecha();
	
	public AdminStats adminStats();
	
	public List<DevStat> adminStatsJuego();
	
	public List<DevStat> adminStatsVentas();
	
	public boolean finalizarEvento(String nombre, String nick);
	
	public boolean iniciarEvento(String nombre, String nick);
	
	public boolean registroEvento(String nombre, String descuento, String fecha_ini, String fecha_fin) throws ParseException;
	
	public boolean bloquearComentario(String id);
	
	public boolean desbloquearComentario(String id);

	public List<Comentario> getComentariosReportados();
	
	public boolean crearCategoria(String nombre);
	
	public boolean editarCategoria(String nombre, String nombreNuevo);
	
	public boolean eliminarCategoria(String nombre);
	
	
}
