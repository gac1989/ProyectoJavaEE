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
import java.util.Date;
import java.util.List;
import java.util.Map;

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

import com.dao.JuegoDAO;
import com.dao.JugadorDAO;
import com.dao.UsuarioDAO;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.model.Administrador;
import com.model.Desarrollador;
import com.model.Juego;
import com.model.Jugador;
import com.model.Usuario;


@Path("/ejemplo")
public class RecursosRest {
	
	final String UPLOAD_FILE_SERVER = "C:\\Users\\admin\\Desktop\\uploads\\";
	
	
	
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
		JugadorDAO u = new JugadorDAO();
		Jugador u1 = u.buscar("bruno540");
		JuegoDAO j = new JuegoDAO();
		Juego j1 = j.buscar(1);
		Map<String, Juego> juego = u1.getJuegos();
		juego.put(j1.getNombre(), j1);
		u.agregarJuego(u1);
		u1 = u.buscar("bruno540");
		Map<String, Juego> juego2 = u1.getJuegos();
	   for (String name : juego2.keySet())
        {
            // search  for value
            Juego url = juego2.get(name);
            System.out.println("Key = " + name + ", Value = " + url.getNombre());
        }
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
	@Path("/checkusuario")
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkUsuario(@FormParam("nick") String nick, @FormParam("pass") String pass) {
		UsuarioDAO UsuarioDAO = new UsuarioDAO();
		Usuario user = UsuarioDAO.buscar(nick);
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
					UsuarioDAO u = new UsuarioDAO();
					Jugador u1 = new Gson().fromJson(entity.toString(), Jugador.class);
					u.guardar(u1);
				}
				case "desarrollador":{
					UsuarioDAO u = new UsuarioDAO();
					Desarrollador u1 = new Gson().fromJson(entity.toString(), Desarrollador.class);
					u.guardar(u1);
				}
				case "administrador":{
					UsuarioDAO u = new UsuarioDAO();
					Administrador u1 = new Gson().fromJson(entity.toString(), Administrador.class);
					u.guardar(u1);
				}
				default:{
					UsuarioDAO u = new UsuarioDAO();
					Usuario u1 = new Gson().fromJson(entity.toString(), Usuario.class);
					System.out.println("ENTREE al DEFAULT");
					u.guardar(u1);
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
	@Path("/editar")
	public Response editar(@FormParam("nick") String nick, @FormParam("email") String email, @FormParam("pass") String pass) {
		if(email!=null && !email.equals("") && pass!=null && !pass.equals("")) {
			UsuarioDAO u = new UsuarioDAO();
			Usuario u1 = new Usuario();
			u1.setNick(nick);
			u1.setEmail(email);
			u1.setPassword(pass);
			u.editar(u1);
			return Response.ok("SE EDITO CORRECTAMENTE EL USUARIO").build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/juegos")
	public Response getJuegos() {
		JuegoDAO juego = new JuegoDAO();
		List<Juego> games = juego.obtenerJuegos();
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
		int id = Integer.parseInt(calve);
		JuegoDAO juegoDAO = new JuegoDAO();
		Juego j = juegoDAO.buscar(id);
		return Response.ok(j).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/buscadorJuegos/{busqueda}")
	public Response buscadorJuegos(@PathParam("busqueda") String busqueda) {
		JuegoDAO juegoDAO = new JuegoDAO();
		List<Juego> juegos = juegoDAO.buscarJuegos(busqueda);
		if(!juegos.isEmpty()) {
			return Response.ok(juegos).build();
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

