package com.interfaces;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.ejb.Local;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import com.model.Categoria;
import com.model.Comentario;
import com.model.DatosVenta;
import com.model.DevStat;
import com.model.Evento;
import com.model.Imagen;
import com.model.Juego;
import com.model.Publicacion;
import com.model.Usuario;

@Local
public interface FrontOfficeInterface {
	
	public List<Juego> obtenerJuegosUsuario(String nick);
	
	public boolean comprarJuego(String nick, String id);
	
	public boolean comentarJuego(String nick,  String juegoid,  String texto, String nota);
	
	public boolean juegoComprado( String nick, String id);
	
	public List<DevStat> adminStatsDev();
	
	public List<DatosVenta> obtenerVentasJuego(String juego);
	
	public List<Juego> getJuegosDesarrollador(String nick);
	
	public boolean agregarJuegoEvento(String nombre, String id_juego);
	
	public boolean quitarJuegoEvento(String id_juego);
	
	public boolean solicitarDesbloqueo(String id_juego);	
	
    public boolean uploadFile(MultipartFormDataInput input) throws IOException;
	 
	public List<Juego> getJuegos();
	 
	public List<Juego> getJuegosOferta();
	 
	public List<Juego> getUltimosJuegos();
	 
	public Juego buscarJuego(String calve);
	
	public List<Juego> buscadorJuegos(String busqueda);
	 
	public List<Comentario> comentariosJuegos(String id);
	 
	public boolean reportarJuego(String id);
	 
	public List<Juego> getJuegosReportados();
	 
	public List<Imagen> getImagenesJuegos(String id);
	 
	public boolean bloquearJuego(String id);
	 
	public boolean desbloquearJuego(String id);

	public List<Juego> getJuegosEventos(String nombre);
	
	public List<Evento> getEventos();
	
	public boolean reportarComentario(String id);

	public List<Usuario> getUsuarios();
	
	public Usuario obtenerusuario(String nick);
	
	public boolean checkUser(String nick);
	
	public boolean checkUserEmail(String email);
	
	public Usuario checkUsuario(String nick, String pass);
	
	public boolean editar(String json);
	
	public boolean checkJuego(String nombre);
	
	public Usuario registrarse(MultipartFormDataInput input) throws IOException;
	
	public boolean uploadPublicacion(MultipartFormDataInput input) throws IOException;
	 
	public List<Publicacion> getPublicaciones(String nick);
	
	public List<DevStat> estadisticasDesarrollador(String nick) throws ParseException;
	
	public List<Juego> obtenerJuegosCategoria(String nombrecat);
	
	public boolean categoriaJuego(String nombre_cat, String id);
	
	public List<Categoria> getCategorias();
	
}
