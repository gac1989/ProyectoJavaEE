package com.lab.java;

import javax.ws.rs.POST;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import com.dao.AdminDAO;
import com.dao.CategoriaDAO;
import com.dao.ComentarioDAO;
import com.dao.CompraDAO;
import com.dao.DesarrolladorDAO;
import com.dao.EventoDAO;
import com.dao.JuegoDAO;
import com.dao.JugadorDAO;
import com.dao.PublicacionDAO;
import com.dao.UsuarioDAO;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.model.AdminStats;
import com.model.Administrador;
import com.model.Categoria;
import com.model.Chart;
import com.model.Comentario;
import com.model.CompraJuego;
import com.model.DatosVenta;
import com.model.Desarrollador;
import com.model.DevStat;
import com.model.Estado;
import com.model.Evento;
import com.model.Juego;
import com.model.Jugador;
import com.model.Publicacion;
import com.model.Usuario;


@Path("/ejemplo")
public class RecursosRest {
	
	final String UPLOAD_FILE_SERVER = "C:\\Users\\admin\\Desktop\\uploads\\";
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/saludo")
	public Response saludar(@FormParam("nick")String nick) throws ParseException {
		JuegoDAO control = new JuegoDAO();
		List<Juego> lista = control.obtenerJuegosReportados();
		return Response.ok(lista).build();
	}
	
	//Imagen
	
	@GET
    @Path("/descarga/{imagen}")
    @Produces({"image/png", "image/jpg", "image/gif"})
    public Response downloadImageFile(@PathParam("imagen") String imagen) {
        File file = new File(UPLOAD_FILE_SERVER + imagen);
        Response.ResponseBuilder responseBuilder = Response.ok((Object) file);
        responseBuilder.header("Content-Disposition", "attachment; filename=\"" + imagen + "\"");
        return responseBuilder.build();
    }
	
	@POST
    @Path("/subir")
    @Consumes("multipart/form-data")
    public Response uploadFile(MultipartFormDataInput input) throws IOException {
		System.out.print("LLEGUE A SUBIR ACA");
        String fileName = "";
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("fichero");
        List<InputPart> name = uploadForm.get("nombre");
        fileName = name.get(0).getBodyAsString();
        for (InputPart inputPart : inputParts) {
	         try {
	            InputStream inputStream = inputPart.getBody(InputStream.class,null);
	            byte [] bytes = IOUtils.toByteArray(inputStream);
	            //constructs upload file path
	            fileName = UPLOAD_FILE_SERVER + fileName;
	            writeFile(bytes,fileName);
	            System.out.println("Done");
	          } catch (IOException e) {
	            e.printStackTrace();
	          }
        }
        return Response.status(200).entity("uploadFile is called, Uploaded file name : " + fileName).build();

    }
	
	 //save to somewhere
    private void writeFile(byte[] content, String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fop = new FileOutputStream(file);
        fop.write(content);
        fop.flush();
        fop.close();
    }
	
	//Estadisticas
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/devstats")
	public Response estadisticasDesarrollador(@FormParam("nick")String nick) throws ParseException {
		DesarrolladorDAO controldev = new DesarrolladorDAO();
		Desarrollador d1 = controldev.buscar(nick);
		if(d1!=null) {
			return Response.ok(controldev.obtenerStats(d1)).build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/datosfecha")
	public Response getDatosFecha() {
		AdminDAO controlad = new AdminDAO();
		List<Chart> datos = controlad.obtenerVentasFecha();
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
		AdminDAO controlad = new AdminDAO();
		AdminStats stats = controlad.obtenerStats();
		if(stats!=null) {
			return Response.ok(stats).build(); 
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/adminstatsjuego")
	public Response adminStatsJuego() {
		AdminDAO controlad = new AdminDAO();
		List<DevStat> stats = controlad.obtenerVentasJuego();
		if(stats!=null) {
			return Response.ok(stats).build(); 
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/adminstatsventas")
	public Response adminStatsVentas() {
		AdminDAO controlad = new AdminDAO();
		List<DevStat> stats = controlad.obtenerVentasTop();
		if(stats!=null) {
			return Response.ok(stats).build(); 
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}
	
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/adminstatsdev")
	public Response adminStatsDev() {
		AdminDAO controlad = new AdminDAO();
		List<DevStat> stats = controlad.obtenerVentasDesarrollador();
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
		UsuarioDAO control = new UsuarioDAO();
		List<Usuario> users = control.obtenerUsuarios();
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
		UsuarioDAO control = new UsuarioDAO();
		Usuario user = control.buscar(nick);
		if(user!=null) {
			if(user instanceof Jugador) {
				Jugador j = (Jugador)user;
				return Response.ok(j).build();
			}
			else {
				if(user instanceof Desarrollador) {
					Desarrollador d = (Desarrollador)user;
					return Response.ok(d).build();
				}
				else {
					Administrador a = (Administrador)user;
					return Response.ok(a).build();
				}
			}
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@POST
	@Path("/checkusuario")
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkUsuario(@FormParam("nick") String nick, @FormParam("pass") String pass) {
		UsuarioDAO controlus = new UsuarioDAO();
		Usuario user = controlus.buscar(nick);
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
	@Path("/registrarse")
	public Response registro(String json) {
		JsonObject convertedObject = new Gson().fromJson(json, JsonObject.class);
		JsonObject type = convertedObject.getAsJsonObject("entity");
		JsonElement type1 = type.get("type");
		System.out.println("El tipo es: " + type1.getAsString());
		JsonElement tree = new Gson().toJsonTree(convertedObject);
		JsonElement entity = tree.getAsJsonObject().getAsJsonObject("entity");
		UsuarioDAO controlus = new UsuarioDAO();
		if(entity!=null) {
			switch(type1.getAsString()) {
				case "jugador":{
					Jugador u1 = new Gson().fromJson(entity.toString(), Jugador.class);
					controlus.guardar(u1);
					break;
				}
				case "desarrollador":{
					Desarrollador u1 = new Gson().fromJson(entity.toString(), Desarrollador.class);
					controlus.guardar(u1);
					break;
				}
				case "administrador":{
					Administrador u1 = new Gson().fromJson(entity.toString(), Administrador.class);
					controlus.guardar(u1);
					break;
				}
				default:{
					Usuario u1 = new Gson().fromJson(entity.toString(), Usuario.class);
					System.out.println("ENTREE al DEFAULT");
					controlus.guardar(u1);
					break;
				}
			}
			return Response.ok("SE CREO CORRECTAMENTE EL USUARIO").build();
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
		JsonObject convertedObject = new Gson().fromJson(json, JsonObject.class);
		JsonObject type = convertedObject.getAsJsonObject("entity");
		JsonElement type1 = type.get("type");
		System.out.println("El tipo es: " + type1.getAsString());
		JsonElement tree = new Gson().toJsonTree(convertedObject);
		JsonElement entity = tree.getAsJsonObject().getAsJsonObject("entity");
		UsuarioDAO controlus = new UsuarioDAO();
		if(entity!=null) {
			switch(type1.getAsString()) {
				case "jugador":{
					Jugador u1 = new Gson().fromJson(entity.toString(), Jugador.class);
					controlus.editar(u1);
					break;
				}
				case "desarrollador":{
					String datos = entity.toString();
					System.out.println("El usuario es: " + datos);
					Desarrollador u1 = new Gson().fromJson(datos, Desarrollador.class);
					controlus.editar(u1);
					break;
				}
				case "administrador":{
					Administrador u1 = new Gson().fromJson(entity.toString(), Administrador.class);
					controlus.editar(u1);
					break;
				}
			}
			return Response.ok("SE ACTUALIZO CORRECTAMENTE EL USUARIO").build();
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}
	
	//Jugador
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/juegosusuario/{nick}")
	public Response obtenerJuegosUsuario(@PathParam("nick") String nick) {
		JugadorDAO control = new JugadorDAO();
		Jugador u1 = control.buscar(nick);
		if(u1!=null) {
			List<Juego> juegos = u1.obtenerJuegos();
			if(juegos!=null && !juegos.isEmpty()) {
				System.out.println("El primer juego es: " + juegos.get(0));
				control.cerrar();
			}
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
		System.out.println("El nick en la api es: " + nick + " El id en la api es: " + id);
		if(nick!=null && id!=null) {
			JugadorDAO controlpy = new JugadorDAO();
			Jugador u1 = controlpy.buscar(nick);
			int idjuego = Integer.parseInt(id);
			JuegoDAO controlgm = new JuegoDAO();
			Juego j1 = controlgm.buscar(idjuego);
			if(u1!=null && j1!=null) {
				CompraJuego compra = new CompraJuego();
				compra.setUser(u1);
				compra.setJuego(j1);
				compra.setPrecio(j1.getOferta());
				compra.setFecha(new Date());
				CompraDAO c1 = new CompraDAO();
				c1.editar(compra);
			}
			return Response.ok("SE COMPRO EL JUEGO CORRECTAMENTE").build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/comentarJuego")
	public Response comentarJuego(@FormParam("nick") String nick, @FormParam("juegoid") String juegoid, @FormParam("texto") String texto, @FormParam("nota") String nota) {
		if(nick!=null && !nick.isEmpty() && juegoid!=null && !juegoid.isEmpty()) {
			int id = Integer.parseInt(juegoid);
			JuegoDAO controlgm = new JuegoDAO();
			Juego j = controlgm.buscar(id);
			JugadorDAO controlpy = new JugadorDAO();
			Jugador j1 = controlpy.buscar(nick);
			if(j!=null && j1!=null) {
				Comentario c1 = new Comentario();
				c1.setTexto(texto);
				c1.setAutor(j1);
				c1.setNota(Integer.parseInt(nota));
				c1.setEstado(Estado.ACTIVO);
				j.agregarComentario(c1);
				controlgm.guardar(j);
				return Response.ok("SE AGREGO EL COMENTARIO").build();
			}
			else {
				return Response.status(Response.Status.NOT_FOUND).build();
			}
			
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/juegoComprado")
	public Response juegoComprado(@FormParam("nick") String nick, @FormParam("juegoid") String id) {
		JugadorDAO controlpy = new JugadorDAO();
		if(nick!=null && !nick.isEmpty() && id!=null &&  !id.isEmpty()) {
			Jugador j1 = controlpy.buscar(nick);
			if(j1!=null) {
				List<Juego> j2 = j1.obtenerJuegos();
				for(Juego juego : j2) {
					if(juego.getId()==Integer.parseInt(id)) {
						controlpy.cerrar();
						return Response.ok("true").build();
					}
				}
			}
		}
		controlpy.cerrar();
		return Response.ok("false").build();
	}
	
	//Desarrollador
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/ventasjuego")
	public Response obtenerVentasJuego(@FormParam("nick") String nick, @FormParam("juego") String juego) {
		JuegoDAO controlgm = new JuegoDAO();
		List<DatosVenta> datos = controlgm.obtenerVentas(Integer.parseInt(juego));
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
		UsuarioDAO control = new UsuarioDAO();
		Usuario u1 = control.buscar(nick);
		Desarrollador d1 = null;
		try {
			d1 = (Desarrollador)u1;
		}
		catch(Exception e) {
			
		}
		if(d1!=null) {
			List<Juego> lista = d1.getJuegos();
			if(lista!=null && !lista.isEmpty()) {
				System.out.println("El primer juego es: " + lista.get(0));
			}
			control.cerrar();
			return Response.ok(lista).build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	public boolean verificarDesarrollador(Desarrollador d1, Juego j1) {
		for(Juego juego : d1.getJuegos()) {
			if(juego.getId()==j1.getId()) {
				return true;
			}
		}
		return false;
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/eventojuego")
	public Response agregarJuegoEvento(@FormParam("nombre") String nombre, @FormParam("id") String id_juego, @FormParam("nick") String nick ){
		Juego j1 = null;
		JuegoDAO control = new JuegoDAO();
		UsuarioDAO controlus = new UsuarioDAO();
		System.out.print("El nombre del evento es: " + nombre + " El id del juego es: " + id_juego);
		Usuario u1 = controlus.buscar(nick);
		if(u1!=null && u1 instanceof Desarrollador) {
			Desarrollador d1 = (Desarrollador)u1;
			if(id_juego!=null && !id_juego.isEmpty()) {
				j1 = control.buscar(Integer.parseInt(id_juego));
				System.out.println("El juego encontrado es: " + j1.getNombre());
			}
			if(j1!=null && verificarDesarrollador(d1,j1)) {
				Evento e1 = null;
				if(nombre!=null && !nombre.isEmpty()) {
					EventoDAO evcon = new EventoDAO();
					e1 = evcon.buscar(nombre);
				}
				if(e1!=null && e1.getActivo()==1) {
					j1.setEvento(e1);
					e1.agregarJuego(j1);
					control.editar(j1);
					control.cerrar();
					controlus.cerrar();
					return Response.ok("SE AGREGO CORRECTAMENTE EL Juego al evento").build();
				}
			}
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/quitarjuegoevento")
	public Response quitarJuegoEvento(@FormParam("id") String id_juego, @FormParam("nick") String nick ){
		Juego j1 = null;
		System.out.print(" El id del juego es: " + id_juego);
		UsuarioDAO control = new UsuarioDAO();
		JuegoDAO controlpy = new JuegoDAO();
		Usuario u1 = control.buscar(nick);
		if(u1!=null && u1 instanceof Desarrollador) {
			Desarrollador d1 = (Desarrollador)u1;
			if(id_juego!=null && !id_juego.isEmpty()) {
				j1 = controlpy.buscar(Integer.parseInt(id_juego));
			}
			System.out.println("El juego encontrado es: " + j1.getNombre());
			if(j1!=null && verificarDesarrollador(d1,j1)) {
				Evento e1 = null;
				e1 = j1.getEvento();
				if(e1!=null && e1.getActivo()==1) {
					j1.setEvento(null);
					controlpy.editar(j1);
					return Response.ok("SE QUITO CORRECTAMENTE EL Juego al evento").build();
				}
			}
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	//Categoria
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/juegoscategoria/{nombrecat}")
	public Response obtenerJuegosCategoria(@PathParam("nombrecat") String nombrecat) {
		CategoriaDAO controlcat = new CategoriaDAO();
		Categoria c1 = controlcat.buscar(nombrecat);
		if(c1!=null) {
			List<Juego> lista = c1.getJuegos();
			if(lista!=null && !lista.isEmpty()) {
				System.out.println("El primer juego es: " + lista.get(0));
			}
			controlcat.cerrar();
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
		System.out.println("El nombre de la categoria es: " + nombre_cat + " El id en la api es: " + id);
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
		System.out.println("CATEGORIA");
		CategoriaDAO controlcat = new CategoriaDAO();
		List<Categoria> categoria = controlcat.obtenerCategorias();
		if(!categoria.isEmpty()) {
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
		if(nombre!=null && !nombre.isEmpty()) {
			Categoria c1 = new Categoria();
			c1.setNombre(nombre);
			CategoriaDAO controlcat = new CategoriaDAO();
			controlcat.guardar(c1);
			return Response.ok("Se creo la categoria").build();
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/editarCategoria")
	public Response editarCategoria(@FormParam("nombre")String nombre, @FormParam("nombreNuevo")String nombreNuevo) {
		CategoriaDAO controlcat = new CategoriaDAO();
		Categoria c1 = controlcat.buscar(nombre);
		if(c1!=null) {
			c1.setNombre(nombreNuevo);
			controlcat.editar(c1);
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
		try {
			CategoriaDAO controlcat = new CategoriaDAO();
			controlcat.eliminar(nombre);
		}
		catch(Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	//Juegos
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/juegos")
	public Response getJuegos() {
		JuegoDAO controlgm = new JuegoDAO();
		List<Juego> games = controlgm.obtenerJuegos();
		if(!games.isEmpty()) {
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
		if(calve!=null && !calve.equals("")) {
			int id = Integer.parseInt(calve);
			JuegoDAO controlgm = new JuegoDAO();
			Juego j = controlgm.buscar(id);
			return Response.ok(j).build();
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/buscadorJuegos/{busqueda}")
	public Response buscadorJuegos(@PathParam("busqueda") String busqueda) {
		JuegoDAO controlgm = new JuegoDAO();
		List<Juego> juegos = controlgm.buscarJuegos(busqueda);
		if(!juegos.isEmpty()) {
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
		JuegoDAO controlgm = new JuegoDAO();
		Juego juego = controlgm.buscar(Integer.parseInt(id));
		if(juego!=null) {
			List<Comentario> coments = juego.getComentarios();
			if(coments!=null && !coments.isEmpty()) {
				System.out.println("El primer juego es: " + coments.get(0));
			}
			controlgm.cerrar();
			return Response.ok(coments).build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/registrarJuego")
	public Response registroJuego(@FormParam("nick") String nick, @FormParam("nombre") String nombre, @FormParam("descripcion") String descripcion, @FormParam("rutaImg") String rutaImg, @FormParam("precio") String precio, @FormParam("categoria") String categoria) {
		if(nick!=null && !nick.isEmpty() && nombre!=null && !nombre.equals("") && descripcion!=null && !descripcion.equals("")&& rutaImg!=null && !rutaImg.equals("")&& precio!=null && !precio.equals("")) {
			Desarrollador d1 = null;
			UsuarioDAO controlus = new UsuarioDAO();
			CategoriaDAO controlcat = new CategoriaDAO();
			try {
				d1 = (Desarrollador)controlus.buscar(nick);
			}
			catch(Exception e) {
				
			}
			if(d1!=null) {
				Juego j = new Juego();
				j.setNombre(nombre);
				j.setDescripcion(descripcion);
				j.setRutaImg(rutaImg);
				j.setPrecio(Float.parseFloat(precio));
				j.setDesarrollador(d1);
				d1.agregarJuego(j);
				controlus.editar(d1);
				Categoria c1 = controlcat.buscar(categoria);
				c1.agregarJuego(j);
				controlcat.guardar(c1);
				return Response.ok("SE CREO CORRECTAMENTE EL Juego").build();
			}
			else {
				return Response.status(Response.Status.NOT_FOUND).build();
			}
			
		}
		else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/reportarjuego")
	public Response reportarJuego(@FormParam("id") String id){
		JuegoDAO controlgm = new JuegoDAO();
		Juego j1 = controlgm.buscar(Integer.parseInt(id));
		if(j1!=null) {
			j1.setEstado(Estado.REPORTADO);
			controlgm.editar(j1);
			return Response.ok("Se reporto el juego").build();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/juegosreportados")
	public Response getJuegosReportados() {
		JuegoDAO control = new JuegoDAO();
		List<Juego> lista = control.obtenerJuegosReportados();
		if(!lista.isEmpty()) {
			return Response.ok(lista).build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/bloquearjuego")
	public Response bloquearJuego(@FormParam("id") String id){
		JuegoDAO controlcom = new JuegoDAO();
		Juego j1 = controlcom.buscar(Integer.parseInt(id));
		if(j1!=null) {
			j1.setEstado(Estado.BLOQUEADO);
			controlcom.editar(j1);
			return Response.ok("Se bloqueo el juego").build();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/desbloquearjuego")
	public Response desbloquearJuego(@FormParam("id") String id){
		JuegoDAO controlcom = new JuegoDAO();
		Juego j1 = controlcom.buscar(Integer.parseInt(id));
		if(j1!=null) {
			j1.setEstado(Estado.ACTIVO);
			controlcom.editar(j1);
			return Response.ok("Se bloqueo el juego").build();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	
	//Evento
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/finalizarevento")
	public Response finalizarEvento(@FormParam("nombre") String nombre, @FormParam("nick") String nick){
		Usuario u1 = null;
		Evento e1 = null;
		UsuarioDAO controlus = new UsuarioDAO();
		EventoDAO controlev = new EventoDAO();
		if(nick!=null && !nick.isEmpty()) {
			u1 = controlus.buscar(nick);
		}
		if(nombre!=null && !nombre.isEmpty() && u1!=null) {
			e1 = controlev.buscar(nombre);
		}
		if(u1 instanceof Administrador && e1!=null) {
		   List<Juego> lista = e1.getJuegos();
		   for(Iterator<Juego> featureIterator = lista.iterator(); 
			    featureIterator.hasNext(); ) {
			    Juego feature = featureIterator.next();
			    System.out.println("El juego a borrar es: " + feature.getNombre() + "Su duenio es: " + feature.getDesarrollador().getNick());
			    feature.setEvento(null);
			    featureIterator.remove();
			}
		    e1.setActivo(0);
		    controlev.editar(e1);
		    controlev.cerrar();
		    return Response.ok("Se finalizo el evento").build();
		}
		controlev.cerrar();
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/iniciarevento")
	public Response iniciarEvento(@FormParam("nombre") String nombre, @FormParam("nick") String nick){
		System.out.println("El evento es: " + nombre + " El usuario es " + nick);
		UsuarioDAO control = new UsuarioDAO();
		EventoDAO controlev = new EventoDAO();
		Usuario u1 = null;
		Evento e1 = null;
		if(nick!=null && !nick.isEmpty()) {
			u1 = control.buscar(nick);
		}
		if(nombre!=null && !nombre.isEmpty() && u1!=null) {
			e1 = controlev.buscar(nombre);
		}
		if(u1 instanceof Administrador && e1!=null) {
		   e1.setActivo(1);
		   controlev.editar(e1);
		   return Response.ok("Se inicio el evento").build();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/juegosevento/{nombre}")
	public Response getJuegosEventos(@PathParam("nombre")String nombre) {
		System.out.println("Juegos Evento");
		EventoDAO controlev = new EventoDAO();
		Evento evento = controlev.buscar(nombre);
		if(evento != null) {
			List<Juego> lista = evento.getJuegos();
			if(lista!=null && !lista.isEmpty()) {
				System.out.println("El primer juego es: " + lista.get(0));
			}
			controlev.cerrar();
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
		System.out.println("Evento");
		EventoDAO control = new EventoDAO();
		List<Evento> eventos = control.obtenerEventos();
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
		System.out.println("La fecha es: " + fecha_ini);
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss");
		SimpleDateFormat formato2 = new SimpleDateFormat("yyyy-MM-dd");
		Date fechaInicio = null;
	      if(fecha_ini!=null){
	    	  try {
	    		  fechaInicio = formato.parse(fecha_ini);
	    	  }
	    	  catch(Exception e) {
	    		  System.out.println("Formato uno incorrecto");
	    		  fechaInicio = formato2.parse(fecha_ini);
	    	  }
	    	  System.out.println("La hora es: " + fechaInicio.toString());
	       }
  		Date fechaFin = null;
	      if(fecha_fin!=null){
	    	  try {
	    		  fechaFin = formato.parse(fecha_fin);
	    	  }
	    	  catch(Exception e) {
	    		  System.out.println("Formato uno incorrecto");
	    		  fechaFin = formato2.parse(fecha_fin);
	    	  }
	    	  System.out.println("La hora es: " + fechaInicio.toString());
	      }
		if(nombre!=null &&  !nombre.equals("") && descuento!=null && !descuento.equals("")&& fecha_ini!=null && !fecha_ini.equals("")&& fecha_fin!=null && !fecha_fin.equals("")) {
			Evento evento = new Evento();
			evento.setNombre(nombre);
			evento.setDescuento(Float.parseFloat(descuento));
			evento.setFecha_ini(fechaInicio);
			evento.setFecha_fin(fechaFin);
			evento.setActivo(0);
			EventoDAO controlev = new EventoDAO();
			controlev.guardar(evento);
			//Se inicia una cuenta regresiva hasta la fecha de inicio
			long restante = fechaInicio.getTime() - new Date().getTime();
			Timer t = new Timer();
			IniciarEvento mTask = new IniciarEvento(nombre);
		    t.schedule(mTask, restante);
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
		ComentarioDAO controlcom = new ComentarioDAO();
		Comentario c1 = controlcom.buscar(Integer.parseInt(id));
		if(c1!=null) {
			c1.setEstado(Estado.REPORTADO);
			controlcom.editar(c1);
			return Response.ok("Se reporto el comentario").build();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/comentariosreportados")
	public Response getComentariosReportados() {
		ComentarioDAO control = new ComentarioDAO();
		List<Comentario> lista = control.obtenerComentariosReportados();
		if(!lista.isEmpty()) {
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
		ComentarioDAO controlcom = new ComentarioDAO();
		Comentario c1 = controlcom.buscar(Integer.parseInt(id));
		if(c1!=null) {
			c1.setEstado(Estado.BLOQUEADO);
			controlcom.editar(c1);
			return Response.ok("Se reporto el comentario").build();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/desbloquearcomentario")
	public Response desbloquearComentario(@FormParam("id") String id){
		ComentarioDAO controlcom = new ComentarioDAO();
		Comentario c1 = controlcom.buscar(Integer.parseInt(id));
		if(c1!=null) {
			c1.setEstado(Estado.ACTIVO);
			controlcom.editar(c1);
			return Response.ok("Se reporto el comentario").build();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	//Usuario
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/agregarpublicacion")
	public Response agregarPublicacion(@FormParam("nick") String nick, @FormParam("texto") String texto, @FormParam("imagen") String imagen){
		UsuarioDAO controlcom = new UsuarioDAO();
		Usuario u1 = controlcom.buscar(nick);
		if(u1!=null) {
			PublicacionDAO controlpub = new PublicacionDAO();
			Publicacion p1 = new Publicacion();
			p1.setTexto(texto);
			p1.setUser(u1);
			p1.setImagen(imagen);
			controlpub.editar(p1);
			return Response.ok("Se reporto el comentario").build();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/publicaciones/{nick}")
	public Response getPublicaciones(@PathParam("nick") String nick) {
		UsuarioDAO control = new UsuarioDAO();
		Usuario u1 = control.buscar(nick);
		List<Publicacion> lista = u1.getPublicaciones();
		if(lista!= null && !lista.isEmpty()) {
			System.out.println(lista.get(0).getTexto());
			control.cerrar();
			return Response.ok(lista).build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
}

