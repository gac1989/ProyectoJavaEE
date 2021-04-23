package com.lab.java;

import javax.ws.rs.POST;

import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.dao.JuegoDAO;
import com.dao.UsuarioDAO;
import com.model.Juego;
import com.model.Usuario;

@Path("/ejemplo")
public class RecursosRest {
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/saludo")
	public String saludar() {
		System.out.println("Hola Mundo desde REST");
		return "Hola Mundo desde REST";
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/usuarios")
	public Response getUsuarios() {
		UsuarioDAO UsuarioDAO = new UsuarioDAO();
		List<Usuario> users = UsuarioDAO.obtenerUsuarios();
		if(!users.isEmpty()) {
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
		UsuarioDAO UsuarioDAO = new UsuarioDAO();
		Usuario user = UsuarioDAO.buscar(nick);
		if(user!=null) {
			return Response.ok(user).build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/registrarse")
	public Response registro(@FormParam("nick") String nick, @FormParam("nombre") String nombre, @FormParam("apellido") String apellido, @FormParam("direccion") String direccion, @FormParam("email") String email, @FormParam("telefono") String telefono) {
		if(nick!=null && !nick.equals("") && nombre!=null && !nombre.equals("")&& apellido!=null && !apellido.equals("")&& direccion!=null && !direccion.equals("")&& telefono!=null && !telefono.equals("")&& email!=null && !email.equals("")) {
			UsuarioDAO u = new UsuarioDAO();
			Usuario u1 = new Usuario();
			u1.setNick(nick);
			u1.setNombres(nombre);
			u1.setApellidos(apellido);
			u1.setDireccion(direccion);
			u1.setTelefono(telefono);
			u1.setEmail(email);
			u.guardar(u1);
			return Response.ok("SE CREO CORRECTAMENTE EL USUARIO").build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/editar")
	public Response editar(@FormParam("nick") String nick, @FormParam("nombre") String nombre, @FormParam("apellido") String apellido, @FormParam("direccion") String direccion, @FormParam("email") String email, @FormParam("telefono") String telefono) {
		if(nombre!=null && !nombre.equals("")&& apellido!=null && !apellido.equals("")&& direccion!=null && !direccion.equals("")&& telefono!=null && !telefono.equals("")&& email!=null && !email.equals("")) {
			UsuarioDAO u = new UsuarioDAO();
			Usuario u1 = new Usuario();
			u1.setNick(nick);
			u1.setNombres(nombre);
			u1.setApellidos(apellido);
			u1.setDireccion(direccion);
			u1.setTelefono(telefono);
			u1.setEmail(email);
			u.editar(u1);
			return Response.ok("SE EDITO CORRECTAMENTE EL USUARIO").build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/registrarJuego")
	public Response registroJuego(@FormParam("nombre") String nombre, @FormParam("descripcion") String descripcion, @FormParam("rutaImg") String rutaImg, @FormParam("precio") String precio) {
		if(nombre!=null && !nombre.equals("") && descripcion!=null && !descripcion.equals("")&& rutaImg!=null && !rutaImg.equals("")&& precio!=null && !precio.equals("")) {
			JuegoDAO jDao = new JuegoDAO();
			Juego j = new Juego();
			j.setNombre(nombre);
			j.setDescripcion(descripcion);
			j.setRutaImg(rutaImg);
			j.setPrecio(Float.parseFloat(precio));
			jDao.guardar(j);
			return Response.ok("SE CREO CORRECTAMENTE EL Juego").build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/obtenerJuegos")
	public Response getJuegos() {
		System.out.println("Esto en la api");
		JuegoDAO juegoDAO = new JuegoDAO();
		List<Juego> juegos = juegoDAO.obtenerJuegos();
		if(!juegos.isEmpty()) {
			return Response.ok(juegos).build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/buscarJuego")
	public Response buscarJuego(@FormParam("id") String calve) {
		int id = Integer.parseInt(calve);
		JuegoDAO juegoDAO = new JuegoDAO();
		Juego j = juegoDAO.buscar(id);
		return Response.ok(j).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/buscadorJuegos/{busqueda}")
	public Response buscadorJuegos(@PathParam("busqueda") String busqueda) {
		System.out.println("Esto en la api" + busqueda);
		JuegoDAO juegoDAO = new JuegoDAO();
		List<Juego> juegos = juegoDAO.buscarJuegos(busqueda);
		if(!juegos.isEmpty()) {
			return Response.ok(juegos).build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
}
