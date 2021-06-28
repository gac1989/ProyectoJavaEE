package com.controllers;

import java.util.List;


import com.dao.CategoriaDAO;
import com.dao.JuegoDAO;
import com.interfaces.CategoriaInterface;
import com.model.Categoria;
import com.model.Juego;

public class CategoriaController implements CategoriaInterface {

	@Override
	public List<Juego> obtenerJuegosCategoria(String nombrecat) {
		CategoriaDAO controlcat = new CategoriaDAO();
		Categoria c1 = controlcat.buscar(nombrecat);
		if(c1!=null) {
			List<Juego> lista = c1.getJuegos();
			if(lista!=null && !lista.isEmpty()) {
				System.out.println("El primer juego es: " + lista.get(0));
			}
			controlcat.cerrar();
			return lista;
		}
		else {
			controlcat.cerrar();
			return null;
		}
	}

	@Override
	public boolean categoriaJuego(String nombre_cat, String id) {
		
		if(nombre_cat!=null && id!=null) {
			CategoriaDAO controlcat = new CategoriaDAO();
			Categoria c1 = controlcat.buscar(nombre_cat);
			JuegoDAO j = new JuegoDAO();
			int idjuego = Integer.parseInt(id);
			Juego j1 = j.buscar(idjuego);
			if(c1!=null && j1!=null) {
				c1.agregarJuego(j1);
				controlcat.guardar(c1);
			}
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public List<Categoria> getCategorias() {
		CategoriaDAO controlcat = new CategoriaDAO();
		List<Categoria> categoria = controlcat.obtenerCategorias();
		if(!categoria.isEmpty()) {
			return categoria;
		}
		else {
			return null;
		}
	}

	@Override //back
	public boolean crearCategoria(String nombre) {
		if(nombre!=null && !nombre.isEmpty()) {
			Categoria c1 = new Categoria();
			c1.setNombre(nombre);
			CategoriaDAO controlcat = new CategoriaDAO();
			controlcat.guardar(c1);
			return true;
		}
		return false;
	}

	@Override //back
	public boolean editarCategoria(String nombre, String nombreNuevo) {
		CategoriaDAO controlcat = new CategoriaDAO();
		Categoria c1 = controlcat.buscar(nombre);
		if(c1!=null) {
			c1.setNombre(nombreNuevo);
			controlcat.editar(c1);
			return true;
		}
		else {
			return false;
		}
	}

	@Override //back
	public boolean eliminarCategoria(String nombre) {
		try {
			CategoriaDAO controlcat = new CategoriaDAO();
			controlcat.eliminar(nombre);
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}

}
