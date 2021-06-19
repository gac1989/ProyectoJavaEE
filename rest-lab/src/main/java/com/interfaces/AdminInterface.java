package com.interfaces;

import java.text.ParseException;
import java.util.List;

import com.model.*;

public interface AdminInterface {
	
	
	
	public List<Chart> getDatosFecha();
	
	public AdminStats adminStats();
	
	public List<DevStat> adminStatsJuego();
	
	public List<DevStat> adminStatsVentas();
	
	public List<DevStat> adminStatsDev();
	
	public boolean finalizarEvento(String nombre, String nick);
	
	public boolean iniciarEvento(String nombre, String nick);
	
	public boolean registroEvento(String nombre, String descuento, String fecha_ini, String fecha_fin) throws ParseException;
	
}
