package com.lab.java;

import javax.ws.rs.POST;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import com.dao.CategoriaDAO;
import com.dao.EventoDAO;
import com.dao.JuegoDAO;
import com.dao.JugadorDAO;
import com.dao.UsuarioDAO;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.model.Administrador;
import com.model.Categoria;
import com.model.Comentario;
import com.model.Desarrollador;
import com.model.Evento;
import com.model.JPAUtil;
import com.model.Juego;
import com.model.Jugador;
import com.model.Usuario;
import com.sun.xml.bind.v2.model.core.Element;


@Path("/ejemplo")
public class RecursosRest {
	
	final String UPLOAD_FILE_SERVER = "C:\\Users\\admin\\Desktop\\uploads\\";
	
	private static JugadorDAO jugadorcontrol = new JugadorDAO();
	private static JuegoDAO juegocontrol = new JuegoDAO();
	private static UsuarioDAO usuariocontrol = new UsuarioDAO();
	private static CategoriaDAO categoriacontrol = new CategoriaDAO();
	private static EventoDAO eventocontrol = new EventoDAO();
	
	
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
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/saludo")
	public String saludar() {
		Timer t = new Timer();
		FinalizarEvento mTask = new FinalizarEvento();
		// This task is scheduled to run every 10 seconds
	    t.scheduleAtFixedRate(mTask, 0, 10000);
		return "Hola Mundo desde REST";
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/usuarios")
	public Response getUsuarios() {
		List<Usuario> users = usuariocontrol.obtenerUsuarios();
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
		Usuario user = usuariocontrol.buscar(nick);
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
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/juegosusuario/{nick}")
	public Response obtenerJuegosUsuario(@PathParam("nick") String nick) {
		Jugador u1 = jugadorcontrol.buscar(nick);
		if(u1!=null) {
			return Response.ok(u1.getJuegos()).build();
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
			Jugador u1 = jugadorcontrol.buscar(nick);
			int idjuego = Integer.parseInt(id);
			Juego j1 = juegocontrol.buscar(idjuego);
			if(u1!=null && j1!=null) {
				u1.agregarJuego(j1);
				jugadorcontrol.guardar(u1);
			}
			return Response.ok("SE COMPRO EL JUEGO CORRECTAMENTE").build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/juegoscategoria/{nombrecat}")
	public Response obtenerJuegosCategoria(@PathParam("nombrecat") String nombrecat) {
		Categoria c1 = categoriacontrol.buscar(nombrecat);
		if(c1!=null) {
			return Response.ok(c1.getJuegos()).build();
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
			Categoria c1 = categoriacontrol.buscar(nombre_cat);
			JuegoDAO j = new JuegoDAO();
			int idjuego = Integer.parseInt(id);
			Juego j1 = j.buscar(idjuego);
			if(c1!=null && j1!=null) {
				c1.agregarJuego(j1);
				categoriacontrol.guardar(c1);
			}
			return Response.ok("SE AGREGO EL JUEGO CORRECTAMENTE").build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	
	@POST
	@Path("/checkusuario")
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkUsuario(@FormParam("nick") String nick, @FormParam("pass") String pass) {
		Usuario user = usuariocontrol.buscar(nick);
		if(user!=null && user.getPassword().equals(pass)) {
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
		if(entity!=null) {
			switch(type1.getAsString()) {
				case "jugador":{
					Jugador u1 = new Gson().fromJson(entity.toString(), Jugador.class);
					usuariocontrol.guardar(u1);
					break;
				}
				case "desarrollador":{
					Desarrollador u1 = new Gson().fromJson(entity.toString(), Desarrollador.class);
					usuariocontrol.guardar(u1);
					break;
				}
				case "administrador":{
					Administrador u1 = new Gson().fromJson(entity.toString(), Administrador.class);
					usuariocontrol.guardar(u1);
					break;
				}
				default:{
					Usuario u1 = new Gson().fromJson(entity.toString(), Usuario.class);
					System.out.println("ENTREE al DEFAULT");
					usuariocontrol.guardar(u1);
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
		if(entity!=null) {
			switch(type1.getAsString()) {
				case "jugador":{
					Jugador u1 = new Gson().fromJson(entity.toString(), Jugador.class);
					usuariocontrol.editar(u1);
					break;
				}
				case "desarrollador":{
					String datos = entity.toString();
					System.out.println("El usuario es: " + datos);
					Desarrollador u1 = new Gson().fromJson(datos, Desarrollador.class);
					usuariocontrol.editar(u1);
					break;
				}
				case "administrador":{
					Administrador u1 = new Gson().fromJson(entity.toString(), Administrador.class);
					usuariocontrol.editar(u1);
					break;
				}
			}
			return Response.ok("SE ACTUALIZO CORRECTAMENTE EL USUARIO").build();
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/juegos")
	public Response getJuegos() {
		List<Juego> games = juegocontrol.obtenerJuegos();
		if(!games.isEmpty()) {
			return Response.ok(games).build();
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
		List<Categoria> categoria = categoriacontrol.obtenerCategorias();
		if(!categoria.isEmpty()) {
			return Response.ok(categoria).build();
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
			Juego j = juegocontrol.buscar(id);
			Jugador j1 = jugadorcontrol.buscar(nick);
			if(j!=null && j1!=null) {
				Comentario c1 = new Comentario();
				c1.setTexto(texto);
				c1.setAutor(j1);
				c1.setNota(Integer.parseInt(nota));
				j.agregarComentario(c1);
				juegocontrol.guardar(j);
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
	@Path("/buscarJuego")
	public Response buscarJuego(@FormParam("id") String calve) {
		if(calve!=null && !calve.equals("")) {
			int id = Integer.parseInt(calve);
			Juego j = juegocontrol.buscar(id);
			return Response.ok(j).build();
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}
	
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/crearCategoria")
	public Response crearCategoria(@FormParam("nombre") String nombre) {
		if(nombre!=null && !nombre.isEmpty()) {
			Categoria c1 = new Categoria();
			c1.setNombre(nombre);
			categoriacontrol.guardar(c1);
			return Response.ok("Se creo la categoria").build();
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}
	
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/juegoComprado")
	public Response juegoComprado(@FormParam("nick") String nick, @FormParam("juegoid") String id) {
		if(nick!=null && !nick.isEmpty() && id!=null &&  !id.isEmpty()) {
			Jugador j1 = jugadorcontrol.buscar(nick);
			if(j1!=null) {
				List<Juego> j2 = j1.getJuegos();
				for(Juego juego : j2) {
					if(juego.getId()==Integer.parseInt(id)) {
						return Response.ok("true").build();
					}
				}
			}
		}
		return Response.ok("false").build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/buscadorJuegos/{busqueda}")
	public Response buscadorJuegos(@PathParam("busqueda") String busqueda) {
		List<Juego> juegos = juegocontrol.buscarJuegos(busqueda);
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
		Juego juego = juegocontrol.buscar(Integer.parseInt(id));
		if(juego!=null) {
			return Response.ok(juego.getComentarios()).build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
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

        return Response.status(200)
            .entity("uploadFile is called, Uploaded file name : " + fileName).build();

    }
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/registrarJuego")
	public Response registroJuego(@FormParam("nombre") String nombre, @FormParam("descripcion") String descripcion, @FormParam("rutaImg") String rutaImg, @FormParam("precio") String precio, @FormParam("categoria") String categoria) {
		if(nombre!=null && !nombre.equals("") && descripcion!=null && !descripcion.equals("")&& rutaImg!=null && !rutaImg.equals("")&& precio!=null && !precio.equals("")) {
			Juego j = new Juego();
			j.setNombre(nombre);
			j.setDescripcion(descripcion);
			j.setRutaImg(rutaImg);
			j.setPrecio(Float.parseFloat(precio));
			juegocontrol.guardar(j);
			Categoria c1 = categoriacontrol.buscar(categoria);
			c1.agregarJuego(j);
			categoriacontrol.guardar(c1);
			return Response.ok("SE CREO CORRECTAMENTE EL Juego").build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	//Eventos
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/eventojuego")
	public Response agregarJuegoEvento(@FormParam("nombre") String nombre, @FormParam("id") String id_juego){
		Juego j1 = null;
		System.out.print("El nombre del evento es: " + nombre + " El id del juego es: " + id_juego);
		if(id_juego!=null && !id_juego.isEmpty()) {
			j1 = juegocontrol.buscar(Integer.parseInt(id_juego));
		}
		Evento e1 = null;
		if(nombre!=null && !nombre.isEmpty()) {
			e1 = eventocontrol.buscar(nombre);
		}
		if(j1!=null && e1!=null) {
			e1.agregarJuego(j1);
			j1.setEvento(e1);
			juegocontrol.guardar(j1);
			eventocontrol.guardar(e1);
			return Response.ok("SE AGREGO CORRECTAMENTE EL Juego al evento").build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/juegosevento/{nombre}")
	public Response getJuegosEventos(@PathParam("nombre")String nombre) {
		System.out.println("Juegos Evento");
		Evento evento = eventocontrol.buscar(nombre);
		if(evento != null) {
			return Response.ok(evento.getJuegos()).build();
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
		EventoDAO eventoDAO = new EventoDAO();
		List<Evento> eventos = eventoDAO.obtenerEventos();
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
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		Date fechaInicio = null;
	      if(fecha_ini!=null){
	    	  fechaInicio = formato.parse(fecha_ini);
	       }
	  	Date fechaFin = null;
	      if(fecha_fin!=null){
	        fechaFin = formato.parse(fecha_fin);
	      }
		if(nombre!=null &&  !nombre.equals("") && descuento!=null && !descuento.equals("")&& fecha_ini!=null && !fecha_ini.equals("")&& fecha_fin!=null && !fecha_fin.equals("")) {
			EventoDAO eventoDao = new EventoDAO();
			Evento evento = new Evento();
			evento.setNombre(nombre);
			evento.setDescuento(Float.parseFloat(descuento));
			evento.setFecha_ini(fechaInicio);
			evento.setFecha_fin(fechaFin);
			eventoDao.guardar(evento);
			return Response.ok("SE CREO CORRECTAMENTE EL evento").build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
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
	
}

