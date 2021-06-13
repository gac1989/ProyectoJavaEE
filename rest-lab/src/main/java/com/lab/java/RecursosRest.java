package com.lab.java;

import javax.ws.rs.POST;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.mindrot.jbcrypt.BCrypt;

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
import com.google.gson.JsonPrimitive;
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
import com.model.Imagen;
import com.model.Juego;
import com.model.Jugador;
import com.model.Publicacion;
import com.model.Usuario;


@Path("/ejemplo")
public class RecursosRest {
	
	final String UPLOAD_FILE_SERVER = "C:\\Users\\admin\\Desktop\\uploads\\";
	InputStream img =  RecursosRest.class.getResourceAsStream("default.png");
	
	@POST
	@Produces({"image/png", "image/jpg", "image/gif"})
	@Path("/saludo")
	public Response saludar() throws ParseException, IOException {
		byte[] default_img = IOUtils.toByteArray(img);
		return Response.ok(default_img).build();
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
    @Path("/registrarjuego")
    @Consumes("multipart/form-data")
    public Response uploadFile(MultipartFormDataInput input) throws IOException {
		System.out.print("LLEGUE A SUBIR ACA");
        String fileName = "";
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> name = uploadForm.get("nombre");
        List<InputPart> juego = uploadForm.get("juego");
        List<InputPart> cat = uploadForm.get("categoria");
        fileName = name.get(0).getBodyAsString();
        String categoria = "";
        String[] categorias = null;
        if(cat!=null) {
        	categoria = cat.get(0).getBodyAsString();
        	System.out.println("El string de categoira es: " + categoria );
        	categorias = new Gson().fromJson(categoria, String[].class);
        }
		 try {
		    registroJuego(juego.get(0).getBodyAsString(),fileName,categorias,uploadForm);
			System.out.println("Done");
		  } catch (IOException e) {
		    e.printStackTrace();
		  }
		System.out.println("El Archivo es: " + fileName + " El juego es: " + juego.get(0).getBodyAsString());
        return Response.status(200).entity("uploadFile is called, Uploaded file name : " + fileName).build();
    }
	
	public void registroJuego(String json, String nick, String[] categoria, Map<String, List<InputPart>> uploadForm ) throws IOException {
		UsuarioDAO controlus = new UsuarioDAO();
		Desarrollador d1 = (Desarrollador)controlus.buscar(nick);
		if(d1!=null) {
			Juego j1 = new Juego();
			j1 = new Gson().fromJson(json, Juego.class);
			JuegoDAO controlgm = new JuegoDAO();
			j1.setDesarrollador(d1);
			j1.setEstado(Estado.ACTIVO);
			j1.setDesbloqueo(false);
			if(uploadForm!=null) {
			    for (Map.Entry<String,List<InputPart>> entry : uploadForm.entrySet()) {
		        	if(entry.getKey().matches("(.*)fichero(.*)")) {
		        		InputStream inputStream = entry.getValue().get(0).getBody(InputStream.class,null);
		     		    byte [] bytes = IOUtils.toByteArray(inputStream);
		     		    Imagen img = new Imagen();
		     		    img.setJuego(j1);
		     		    img.setContenido(bytes);
		     		    j1.agregarImagen(img);
		        	}
		        }
			}
			controlgm.guardar(j1);
			if(categoria!=null) {
				CategoriaDAO controlcat = new CategoriaDAO();
				for(String cat : categoria) {
					System.out.println("La categoria es: " + cat);
					Categoria c1 = controlcat.buscar(cat);
					if(c1!=null) {
						c1.agregarJuego(j1);
						controlcat.editar(c1);
					}
				}
				controlcat.cerrar();
			}
			System.out.println("SE CREO EL JUEGO");
		}
		else {
			System.out.println("ERROR AL CREAR EL JUEGO");
		}
	}
	
	@POST
    @Path("/registrarse")
    @Consumes("multipart/form-data")
	@Produces(MediaType.APPLICATION_JSON)
    public Response regitrarse(MultipartFormDataInput input) throws IOException {
		System.out.print("LLEGUE A SUBIR ACA");
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("fichero");
        List<InputPart> user = uploadForm.get("usuario");
        Usuario u1 = null;
        try {
        	byte [] bytes = IOUtils.toByteArray(img);
        	if(inputParts!=null) {
        		InputStream inputStream = inputParts.get(0).getBody(InputStream.class,null);
        		if(inputStream != null && inputStream.available()!=0) {
     		    	bytes = IOUtils.toByteArray(inputStream);
     		    }
        	}
		    u1 = registro(user.get(0).getBodyAsString(),bytes);
			System.out.println("Done");
        } catch (IOException e) {
		    e.printStackTrace();
        }
        return Response.ok(u1).build();
    }
	
	public Usuario registro(String json, byte[] imagen) {
		JsonObject convertedObject = new Gson().fromJson(json, JsonObject.class);
		JsonPrimitive type = convertedObject.getAsJsonPrimitive("type");
		String tipo = type.getAsString();
		UsuarioDAO controlus = new UsuarioDAO();
		Usuario u1 = null;
		if(json!=null && !json.isEmpty()) {
			switch(tipo) {
				case "jugador":{
					u1 = new Gson().fromJson(json, Jugador.class);
					u1.setImagen(imagen);
					controlus.guardar(u1);
					break;
				}
				case "desarrollador":{
					u1 = new Gson().fromJson(json, Desarrollador.class);
					u1.setImagen(imagen);
					controlus.guardar(u1);
					break;
				}
				default:{
					break;
				}
			}
			return u1;
		}
		else {
			return null;
		}
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
		List<Usuario> users = control.obtenerUsuariosDebiles();
		if(users!=null && !users.isEmpty()) {
			control.cerrar();
			return Response.ok(users).build();
		}
		else {
			control.cerrar();
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
		return Response.ok((new UsuarioDAO().buscar(nick)!=null)).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/ckjuego/{nombre}")
	public Response checkJuego(@PathParam("nombre") String nombre) {
		return Response.ok((new JuegoDAO().buscarJuego(nombre)!=null)).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/ckemail/{email}")
	public Response checkUserEmail(@PathParam("email") String email) {
		return Response.ok((new UsuarioDAO().buscarEmail(email)!=null)).build();
	}
	
	
	@POST
	@Path("/checkusuario")
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkUsuario(@FormParam("nick") String nick, @FormParam("pass") String pass) {
		UsuarioDAO controlus = new UsuarioDAO();
		Usuario user = controlus.checkUser(nick, pass);
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
		JsonObject convertedObject = new Gson().fromJson(json, JsonObject.class);
		JsonObject type = convertedObject.getAsJsonObject("entity");
		JsonElement type1 = type.get("type");
		/*System.out.println("El tipo es: " + type1.getAsString());
		JsonElement tree = new Gson().toJsonTree(convertedObject);
		JsonElement entity = tree.getAsJsonObject().getAsJsonObject("entity");*/
		UsuarioDAO controlus = new UsuarioDAO();
		String nick = type.get("nick").getAsString();
		Usuario u1 = controlus.buscar(nick);
		if(u1!=null) {
			u1.setEmail(type.get("email").getAsString());
			if(type1!=null) {
				switch(type1.getAsString()) {
					case "jugador":{
						Jugador j1 = (Jugador)u1;
						j1.setApellido(type.get("apellido").getAsString());
						j1.setNombre(type.get("nombre").getAsString());
						controlus.editar(u1);
						break;
					}
					case "desarrollador":{
						Desarrollador d1 = (Desarrollador)u1;
						d1.setDireccion(type.get("direccion").getAsString());
						d1.setNombre_empresa(type.get("nombre_empresa").getAsString());
						d1.setTelefono(type.get("telefono").getAsString());
						d1.setPais(type.get("pais").getAsString());
						controlus.editar(u1);
						break;
					}
				}
				return Response.ok("SE ACTUALIZO CORRECTAMENTE EL USUARIO").build();
			}
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
			control.cerrar();
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
			control.cerrar();
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
		control.cerrar();
		controlus.cerrar();
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
					controlpy.cerrar();
					return Response.ok("SE QUITO CORRECTAMENTE EL Juego al evento").build();
				}
			}
		}
		controlpy.cerrar();
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/solicitardesbloqueo")
	public Response solicitarDesbloqueo(@FormParam("id") String id_juego){
		JuegoDAO controlpy = new JuegoDAO();
		Juego j1 = controlpy.buscar(Integer.parseInt(id_juego));
		if(j1!=null && j1.getEstado()==Estado.BLOQUEADO) {
			j1.setDesbloqueo(true);
			controlpy.editar(j1);
			controlpy.cerrar();
			return Response.ok("SE QUITO CORRECTAMENTE EL Juego al evento").build();
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
			controlcat.cerrar();
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
			controlgm.cerrar();
			return Response.status(Response.Status.NOT_FOUND).build();
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
			j1.setDesbloqueo(false);
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
			controlev.cerrar();
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
    @Path("/agregarpublicacion")
    @Consumes("multipart/form-data")
    public Response uploadPublicacion(MultipartFormDataInput input) throws IOException {
		System.out.print("LLEGUE A SUBIR ACA");
        String fileName = "";
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> nick = uploadForm.get("nick");
        List<InputPart> texto = uploadForm.get("texto");
        List<InputPart> img = uploadForm.get("imagen");
        fileName = nick.get(0).getBodyAsString();
        UsuarioDAO control = new UsuarioDAO();
        Usuario u1 = control.buscar(fileName);
        if(u1!=null) {
        	PublicacionDAO controlpub = new PublicacionDAO();
			Publicacion p1 = new Publicacion();
        	if(img!=null) {
        		InputStream inputStream = img.get(0).getBody(InputStream.class,null);
        		 byte [] bytes = IOUtils.toByteArray(inputStream);
        		 p1.setImagen(bytes);
        	}
			p1.setTexto(texto.get(0).getBodyAsString());
			p1.setUser(u1);
			controlpub.editar(p1);
			controlpub.cerrar();
			control.cerrar();
			return Response.ok("Se realizo la publicacion").build();
		}
		control.cerrar();
        return Response.status(404).entity("uploadFile is called, Uploaded file name : " + fileName).build();
    }
	
	
	/*public Response agregarPublicacion(@FormParam("nick") String nick, @FormParam("texto") String texto, @FormParam("imagen") String imagen){
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
	}*/
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/publicaciones/{nick}")
	public Response getPublicaciones(@PathParam("nick") String nick) {
		UsuarioDAO control = new UsuarioDAO();
		Usuario u1 = control.buscar(nick);
		List<Publicacion> lista = u1.getPublicaciones();
		if(lista!= null && !lista.isEmpty()) {
			System.out.println(lista.get(0).getTexto());
			Collections.reverse(lista);
			control.cerrar();
			return Response.ok(lista).build();
		}
		else {
			control.cerrar();
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
}

