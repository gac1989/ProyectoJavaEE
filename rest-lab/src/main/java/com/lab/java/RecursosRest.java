package com.lab.java;

import javax.ws.rs.POST;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import com.controllers.BackOfficeController;
import com.controllers.FrontOfficeController;
import com.interfaces.BackOfficeInterface;
import com.interfaces.FrontOfficeInterface;
import com.model.AdminStats;
import com.model.Categoria;
import com.model.Chart;
import com.model.Comentario;
import com.model.DatosVenta;
import com.model.DevStat;
import com.model.Evento;
import com.model.Imagen;
import com.model.Juego;
import com.model.Publicacion;
import com.model.Usuario;


@Path("/recursos")
public class RecursosRest {
	
	@EJB
	private BackOfficeInterface back = new BackOfficeController();
	
	@EJB
	private FrontOfficeInterface fo = new FrontOfficeController();
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/saludo")
	public Response saludar() throws ParseException, IOException {
		return Response.ok().build();
	}
	
	//Imagen
	
	@POST
    @Path("/registrarjuego")
    @Consumes("multipart/form-data")
    public Response uploadFile(MultipartFormDataInput input) throws IOException {
        if(fo.uploadFile(input)) {
        	return Response.ok("JUEGO CREADO").build(); 
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
	
	@POST
    @Path("/registrarse")
    @Consumes("multipart/form-data")
	@Produces(MediaType.APPLICATION_JSON)
    public Response registrarse(MultipartFormDataInput input) throws IOException {
    	
		Usuario u1 = fo.registrarse(input);
		if(u1!=null) {
			return Response.ok(u1).build(); 
    	}
		return Response.status(Response.Status.BAD_REQUEST).build();
    }
	
	//Estadisticas
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/devstats")
	public Response estadisticasDesarrollador(@FormParam("nick")String nick) throws ParseException {
		List<DevStat> lista = fo.estadisticasDesarrollador(nick);
		if(lista!=null) {
			return Response.ok(lista).build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/datosfecha")
	public Response getDatosFecha() {
		List<Chart> datos = back.getDatosFecha();
		if(datos!=null) {
			return Response.ok(datos).build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/ingresosfecha")
	public Response getIngresosFecha() {
		List<Chart> datos = back.getIngresosFecha();
		if(datos!=null) {
			return Response.ok(datos).build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/adminstats")
	public Response adminStats() {
		AdminStats stats = back.adminStats();
		if(stats!=null) {
			return Response.ok(stats).build(); 
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/adminstatsjuego")
	public Response adminStatsJuego() {
		List<DevStat> stats =back.adminStatsJuego();
		if(stats!=null) {
			return Response.ok(stats).build(); 
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/adminstatsventas")
	public Response adminStatsVentas() {
		List<DevStat> stats = back.adminStatsVentas();
		if(stats!=null) {
			return Response.ok(stats).build(); 
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/adminstatsdev")
	public Response adminStatsDev() {
		List<DevStat> stats = fo.adminStatsDev();
		if(stats!=null) {
			return Response.ok(stats).build(); 
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}
	
	//Usuarios
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/usuarios")
	public Response getUsuarios() {
		List<Usuario> users = fo.getUsuarios();
		if(users!=null) {
			return Response.ok(users).build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/usuario/{nick}")
	public Response obtenerusuario(@PathParam("nick") String nick) {
		Usuario user = fo.obtenerusuario(nick);
		if(user!=null) {
			return Response.ok(user).build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/ckusername/{nick}")
	public Response checkUser(@PathParam("nick") String nick) {
		return Response.ok(fo.checkUser(nick)).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/ckjuego/{nombre}")
	public Response checkJuego(@PathParam("nombre") String nombre) {
		return Response.ok(fo.checkJuego(nombre)).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/ckemail/{email}")
	public Response checkUserEmail(@PathParam("email") String email) {
		return Response.ok(fo.checkUserEmail(email)).build();
	}
	
	
	@POST
	@Path("/checkusuario")
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkUsuario(@FormParam("nick") String nick, @FormParam("pass") String pass) {
		Usuario user = fo.checkUsuario(nick, pass);
		if(user!=null) {
			return Response.ok(user).build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/editar")
	public Response editar(String json) {
		if(fo.editar(json)) {
			return Response.ok("SE ACTUALIZO CORRECTAMENTE EL USUARIO").build();
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}
	
	//Jugador
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/juegosusuario/{nick}")
	public Response obtenerJuegosUsuario(@PathParam("nick") String nick) {
		List<Juego> juegos = fo.obtenerJuegosUsuario(nick);
		if(juegos!=null) {
			return Response.ok(juegos).build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@POST
	@Path("/comprarjuego")
	@Produces(MediaType.APPLICATION_JSON)
	public Response comprarJuego(@FormParam("nick") String nick, @FormParam("id") String id) {
		if(fo.comprarJuego(nick, id)) {
			return Response.ok("SE COMPRO EL JUEGO CORRECTAMENTE").build();
		}
		else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/comentarJuego")
	public Response comentarJuego(@FormParam("nick") String nick, @FormParam("juegoid") String juegoid, @FormParam("texto") String texto, @FormParam("nota") String nota) {
		if(fo.comentarJuego(nick, juegoid, texto, nota)) {
			return Response.ok("SE COMENTO EL JUEGO CORRECTAMENTE").build();
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/juegoComprado")
	public Response juegoComprado(@FormParam("nick") String nick, @FormParam("juegoid") String id) {
		if(fo.juegoComprado(nick, id)) {
			return Response.ok("true").build();
		}
		return Response.ok("false").build();
	}
	
	//Desarrollador
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/ventasjuego")
	public Response obtenerVentasJuego(@FormParam("juego") String juego) {
		List<DatosVenta> datos = fo.obtenerVentasJuego(juego);
		if(datos!=null) {
			return Response.ok(datos).build(); 
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/juegosdesarrollador/{nick}")
	public Response getJuegosDesarrollador(@PathParam("nick")String nick) {
		List<Juego> lista = fo.getJuegosDesarrollador(nick);
		if(lista!=null) {
			return Response.ok(lista).build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/eventojuego")
	public Response agregarJuegoEvento(@FormParam("nombre") String nombre, @FormParam("id") String id_juego){
		if(fo.agregarJuegoEvento(nombre, id_juego)) {
			return Response.ok("SE AGREGO CORRECTAMENTE EL Juego al evento").build();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/quitarjuegoevento")
	public Response quitarJuegoEvento(@FormParam("id") String id_juego){
		if(fo.quitarJuegoEvento(id_juego)) {
			return Response.ok("SE QUITO CORRECTAMENTE EL Juego al evento").build();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/solicitardesbloqueo")
	public Response solicitarDesbloqueo(@FormParam("id") String id_juego){
		if(fo.solicitarDesbloqueo(id_juego)) {
			return Response.ok("SE QUITO CORRECTAMENTE EL Juego al evento").build();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	

	//Categoria
	

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/juegoscategoria/{nombrecat}")
	public Response obtenerJuegosCategoria(@PathParam("nombrecat") String nombrecat) {
		List<Juego> lista = fo.obtenerJuegosCategoria(nombrecat);
		if(lista!=null) {
			return Response.ok(lista).build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@POST
	@Path("/categoriajuego")
	@Produces(MediaType.APPLICATION_JSON)
	public Response categoriaJuego(@FormParam("nombrecat") String nombre_cat, @FormParam("id") String id) {
		if(fo.categoriaJuego(nombre_cat, id)) {
			return Response.ok("SE AGREGO EL JUEGO CORRECTAMENTE").build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/categorias")
	public Response getCategorias() {
		List<Categoria> categoria = fo.getCategorias();
		if(categoria!=null) {
			return Response.ok(categoria).build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/crearCategoria")
	public Response crearCategoria(@FormParam("nombre") String nombre) {
		if(back.crearCategoria(nombre)) {
			return Response.ok("Se creo la categoria").build();
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/editarCategoria")
	public Response editarCategoria(@FormParam("nombre")String nombre, @FormParam("nombreNuevo")String nombreNuevo) {
		if(back.editarCategoria(nombre, nombreNuevo)) {
			return Response.ok("SE EDITO LA CATEGORIA").build();
		}
		else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/eliminarCategoria")
	public Response eliminarCategoria(@FormParam("nombre")String nombre) {
		if(back.eliminarCategoria(nombre)) {
			return Response.ok("SE ELIMINO LA CATEGORIA").build();
		}else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	//Juegos
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/juegosoferta")
	public Response getJuegosOferta() {
		List<Juego> games = fo.getJuegosOferta();
		if(games!=null) {
			return Response.ok(games).build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/ultimosjuegos")
	public Response getUltimosJuegos() {
		List<Juego> games = fo.getUltimosJuegos();
		if(games!=null) {
			return Response.ok(games).build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/juegos")
	public Response getJuegos() {
		List<Juego> games = fo.getJuegos();
		if(games!=null) {
			return Response.ok(games).build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/buscarJuego")
	public Response buscarJuego(@FormParam("id") String calve) {
		Juego j1 = fo.buscarJuego(calve);
		if(j1!=null) {
			return Response.ok(j1).build();
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/buscadorJuegos/{busqueda}")
	public Response buscadorJuegos(@PathParam("busqueda") String busqueda) {
		List<Juego> juegos = fo.buscadorJuegos(busqueda);
		if(juegos!=null) {
			return Response.ok(juegos).build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/comentariosJuegos/{id}")
	public Response comentariosJuegos(@PathParam("id") String id) {
		List<Comentario> coments = fo.comentariosJuegos(id);
		if(coments!=null && !coments.isEmpty()) {
			return Response.ok(coments).build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/reportarjuego")
	public Response reportarJuego(@FormParam("id") String id){
		if(fo.reportarJuego(id)) {
			return Response.ok("Se reporto el juego").build();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/juegosreportados")
	public Response getJuegosReportados() {
		List<Juego> lista = fo.getJuegosReportados();
		if(lista!=null) {
			return Response.ok(lista).build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/imagenesjuego/{id}")
	public Response getImagenesJuegos(@PathParam("id") String id) {
		List<Imagen> imagenes = fo.getImagenesJuegos(id);
		if(imagenes!=null && !imagenes.isEmpty()) {
			return Response.ok(imagenes).build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/bloquearjuego")
	public Response bloquearJuego(@FormParam("id") String id){
		if(fo.bloquearJuego(id)) {
			return Response.ok("Se bloqueo el juego").build();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/desbloquearjuego")
	public Response desbloquearJuego(@FormParam("id") String id){
		if(fo.desbloquearJuego(id)) {
			return Response.ok("Se desbloqueo el juego").build();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	//Evento
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/finalizarevento")
	public Response finalizarEvento(@FormParam("nombre") String nombre, @FormParam("nick") String nick){
		if(back.finalizarEvento(nombre, nick)) {
		    return Response.ok("Se finalizo el evento").build();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/iniciarevento")
	public Response iniciarEvento(@FormParam("nombre") String nombre, @FormParam("nick") String nick){
		if(back.iniciarEvento(nombre, nick)) {
		   return Response.ok("Se inicio el evento").build();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/juegosevento/{nombre}")
	public Response getJuegosEventos(@PathParam("nombre")String nombre) {
		List<Juego> lista = fo.getJuegosEventos(nombre);
		if(lista != null) {
			return Response.ok(lista).build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/eventos")
	public Response getEventos() {
		List<Evento> eventos = fo.getEventos();
		if(!eventos.isEmpty()) {
			return Response.ok(eventos).build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
 
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/evento")
	public Response registroEvento(@FormParam("nombre") String nombre, @FormParam("descuento") String descuento,  @FormParam("fecha_ini") String fecha_ini,  @FormParam("fecha_fin") String fecha_fin) throws ParseException {
		if(back.registroEvento(nombre, descuento, fecha_ini, fecha_fin)) {
			return Response.ok("SE CREO CORRECTAMENTE EL evento").build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	//Comentarios
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/reportarcomentario")
	public Response reportarComentario(@FormParam("id") String id){
		if(fo.reportarComentario(id)) {
			return Response.ok("Se reporto el comentario").build();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/comentariosreportados")
	public Response getComentariosReportados() {
		List<Comentario> lista = back.getComentariosReportados();
		if(lista!=null) {
			return Response.ok(lista).build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/bloquearcomentario")
	public Response bloquearComentario(@FormParam("id") String id){
		if(back.bloquearComentario(id)) {
			return Response.ok("Se reporto el comentario").build();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/desbloquearcomentario")
	public Response desbloquearComentario(@FormParam("id") String id){
		if(back.desbloquearComentario(id)) {
			return Response.ok("Se reporto el comentario").build();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	//Usuario
	
	@POST
    @Path("/agregarpublicacion")
    @Consumes("multipart/form-data")
    public Response uploadPublicacion(MultipartFormDataInput input) throws IOException {
        if(fo.uploadPublicacion(input)) {
        	return Response.ok("Se agrego la publicacion").build();
		}
        return Response.status(404).build();
    }
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/publicaciones/{nick}")
	public Response getPublicaciones(@PathParam("nick") String nick) {
		List<Publicacion> lista = fo.getPublicaciones(nick);
		if(lista!= null) {
			return Response.ok(lista).build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
}

