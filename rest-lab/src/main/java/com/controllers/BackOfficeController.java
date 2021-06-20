package com.controllers;

import java.text.ParseException;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.interfaces.BackOfficeInterface;
import com.model.AdminStats;
import com.model.Chart;
import com.model.Comentario;
import com.model.DevStat;

@Stateless
@LocalBean
public class BackOfficeController implements BackOfficeInterface {
	
	@Override
	public List<Chart> getDatosFecha(){
		return new AdminController().getDatosFecha();
	}
	
	@Override
	public List<Chart> getIngresosFecha(){
		return new AdminController().getIngresosFecha();
	}
	
	@Override
	public AdminStats adminStats() {
		return new AdminController().adminStats();
	}
	
	@Override
	public List<DevStat> adminStatsJuego(){
		return new AdminController().adminStatsJuego();
	}
	@Override
	public List<DevStat> adminStatsVentas(){
		return new AdminController().adminStatsVentas();
	}
	
	@Override
	public boolean finalizarEvento(String nombre, String nick) {
		return new AdminController().finalizarEvento(nombre, nick);
	}
	
	@Override
	public boolean iniciarEvento(String nombre, String nick) {
		return new AdminController().iniciarEvento(nombre, nick);
	}
	
	@Override
	public boolean registroEvento(String nombre, String descuento, String fecha_ini, String fecha_fin) throws ParseException{
		return new AdminController().registroEvento(nombre, descuento, fecha_ini, fecha_fin);
	}
	
	@Override
	public List<Comentario> getComentariosReportados() {
		return new ComentarioController().getComentariosReportados();
	}
	@Override
	public boolean bloquearComentario(String id) {
		return new ComentarioController().bloquearComentario(id);
	}

	@Override
	public boolean desbloquearComentario(String id) {
		return new ComentarioController().desbloquearComentario(id);
	}
	
	@Override
	public boolean crearCategoria(String nombre) {
		
		return new CategoriaController().crearCategoria(nombre);
	}

	@Override
	public boolean editarCategoria(String nombre, String nombreNuevo) {
	
		return new CategoriaController().editarCategoria(nombre, nombreNuevo);
	}

	@Override
	public boolean eliminarCategoria(String nombre) {
		return new CategoriaController().eliminarCategoria(nombre);
	}

	
}
