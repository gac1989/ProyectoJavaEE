package com.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.ejb.Stateless;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import com.interfaces.FrontOfficeInterface;
import com.model.Categoria;
import com.model.Comentario;
import com.model.DatosVenta;
import com.model.DevStat;
import com.model.Evento;
import com.model.Imagen;
import com.model.Juego;
import com.model.Publicacion;
import com.model.Usuario;

@Stateless
public class FrontOfficeController implements FrontOfficeInterface {

	@Override
	public List<Juego> obtenerJuegosUsuario(String nick){
		return new JugadorController().obtenerJuegosUsuario(nick);
	}
	@Override
	public boolean comprarJuego(String nick, String id) {
		return new JugadorController().comprarJuego(nick, id);
	}
	@Override
	public boolean comentarJuego(String nick,  String juegoid,  String texto, String nota) {
		return new JugadorController().comentarJuego(nick, juegoid, texto, nota);
	}
	@Override
	public boolean juegoComprado( String nick, String id) {
		return new JugadorController().juegoComprado(nick, id);
	}
	
	@Override
	public List<DevStat> adminStatsDev(){
		return new AdminController().adminStatsDev();
	}
	
	@Override
	public List<DatosVenta> obtenerVentasJuego(String juego){
		return new DesarrolladorController().obtenerVentasJuego(juego);
	}
	
	@Override
	public List<Juego> getJuegosDesarrollador(String nick){
		return new DesarrolladorController().getJuegosDesarrollador(nick);
	}
	
	@Override
	public boolean agregarJuegoEvento(String nombre,  String id_juego) {
		return new DesarrolladorController().agregarJuegoEvento(nombre, id_juego);
	}
	
	@Override
	public boolean quitarJuegoEvento(String id_juego) {
		return new DesarrolladorController().quitarJuegoEvento(id_juego);
	}
	
	@Override
	public boolean solicitarDesbloqueo(String id_juego) {
		return new DesarrolladorController().solicitarDesbloqueo(id_juego);
	}
	
	@Override
	public boolean uploadFile(MultipartFormDataInput input) throws IOException{
		return new JuegoController().uploadFile(input);
	}
	 
	@Override
	public List<Juego> getJuegos(){
		return new JuegoController().getJuegos();
	}
	 
	@Override
	public List<Juego> getJuegosOferta(){
		return new JuegoController().getJuegosOferta();
	}
	 
	@Override
	public List<Juego> getUltimosJuegos(){
		return new JuegoController().getUltimosJuegos();
	}
	 
	@Override
	public Juego buscarJuego(String calve) {
		return new JuegoController().buscarJuego(calve);
	}
	
	@Override
	public List<Juego> buscadorJuegos(String busqueda){
		return new JuegoController().buscadorJuegos(busqueda);
	}
	 
	@Override
	public List<Comentario> comentariosJuegos(String id){
		return new JuegoController().comentariosJuegos(id);
	}
	 
	@Override
	public boolean reportarJuego(String id) {
		return new JuegoController().reportarJuego(id);
	}
	 
	@Override
	public List<Juego> getJuegosReportados(){
		return new JuegoController().getJuegosReportados();
	}
	 
	@Override
	public List<Imagen> getImagenesJuegos(String id){
		return new JuegoController().getImagenesJuegos(id);
	}
	 
	@Override
	public boolean bloquearJuego(String id) {
		return new JuegoController().bloquearJuego(id);
	}
	 
	@Override
	public boolean desbloquearJuego(String id) {
		return new JuegoController().desbloquearJuego(id);
	}
	
	@Override
	public List<Juego> getJuegosEventos(String nombre){
		return new EventoController().getJuegosEventos(nombre);
	}
	
	@Override
	public List<Evento> getEventos(){
		return new EventoController().getEventos();
	}
	
	@Override
	public boolean reportarComentario(String id) {
		return new ComentarioController().reportarComentario(id);
	}

	@Override
	public List<Usuario> getUsuarios(){
		return new UsuarioController().getUsuarios();
	}
	
	@Override
	public Usuario obtenerusuario(String nick){
		return new UsuarioController().obtenerusuario(nick);
	}
	
	@Override
	public boolean checkUser(String nick){
		return new UsuarioController().checkUser(nick);
	}
	
	@Override
	public boolean checkUserEmail(String email){
		return new UsuarioController().checkUserEmail(email);
	}
	
	@Override
	public Usuario checkUsuario(String nick, String pass){
		return new UsuarioController().checkUsuario(nick, pass);
	}
	
	@Override
	public boolean editar(String json){
		return new UsuarioController().editar(json);
	}
	
	@Override
	public boolean checkJuego(String nombre) {
		return new JuegoController().checkJuego(nombre);
	}
	
	@Override
	public Usuario registrarse(MultipartFormDataInput input) throws IOException{
		return new UsuarioController().registrarse(input);
	}
	
	@Override
	public boolean uploadPublicacion(MultipartFormDataInput input) throws IOException{
		return new UsuarioController().uploadPublicacion(input);
	}
	 
	@Override
	public List<Publicacion> getPublicaciones(String nick){
		return new UsuarioController().getPublicaciones(nick);
	}
	@Override
	public List<DevStat> estadisticasDesarrollador(String nick) throws ParseException{
		return new DesarrolladorController().estadisticasDesarrollador(nick);
	}
	
	@Override
	public List<Juego> obtenerJuegosCategoria(String nombrecat) {
		return new CategoriaController().obtenerJuegosCategoria(nombrecat);
	}

	@Override
	public boolean categoriaJuego(String nombre_cat, String id) {
		return new CategoriaController().categoriaJuego(nombre_cat, id);

	}

	@Override
	public List<Categoria> getCategorias() {
		return new CategoriaController().getCategorias();
	}
	
	
}
