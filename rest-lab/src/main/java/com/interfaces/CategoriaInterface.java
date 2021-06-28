package com.interfaces;

import java.util.List;


import com.model.Categoria;
import com.model.Juego;

public interface CategoriaInterface {
	
	public List<Juego> obtenerJuegosCategoria(String nombrecat);
	
	public boolean categoriaJuego(String nombre_cat, String id);
	
	public List<Categoria> getCategorias();
	
	public boolean crearCategoria(String nombre);
	
	public boolean editarCategoria(String nombre, String nombreNuevo);
	
	public boolean eliminarCategoria(String nombre);
	
}
