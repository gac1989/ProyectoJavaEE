package com.lab.java;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;



import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.dao.JuegoDAO;
import com.dao.UsuarioDAO;
import com.model.Juego;
import com.model.Usuario;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/ejemplo")
public class RecursosRest {
	
	final String UPLOAD_FILE_SERVER = "C:\\Users\\admin\\Desktop\\uploads\\";
	
	
	
	@GET
    @Path("/descarga/{imagen}")
    @Produces({"image/png", "image/jpg", "image/gif"})
    public Response downloadImageFile(@PathParam("imagen") String imagen) {
        System.out.println("LLEGUE A LAS FOTOSS");
        File file = new File(UPLOAD_FILE_SERVER + imagen);
 
        Response.ResponseBuilder responseBuilder = Response.ok((Object) file);
        responseBuilder.header("Content-Disposition", "attachment; filename=\"" + imagen + "\"");
        return responseBuilder.build();
    }
	
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
    @Path("/subir")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadImageFile(
            @FormDataParam("fichero") InputStream fileInputStream, @FormDataParam("fichero") FormDataContentDisposition fileDetail) throws IOException{
		System.out.print("ENTRE A SUBIR ACAAAA" + fileInputStream.read());
        String fileName = null;
        String uploadFilePath = null;
        
        try {
            fileName = fileDetail.getFileName();
            uploadFilePath = writeToFileServer(fileInputStream, fileName);
        }
        catch(IOException ioe){
            ioe.printStackTrace();
        }
        finally{
        }
        return Response.ok("Fichero subido a " + uploadFilePath).build();
	}
	 
	    private String writeToFileServer(InputStream inputStream, String fileName) throws IOException {
 
	        OutputStream outputStream = null;
	        String qualifiedUploadFilePath = UPLOAD_FILE_SERVER + fileName;
	 
	        try {
	        	File archivo = new File(qualifiedUploadFilePath);
	            outputStream = new FileOutputStream(archivo);
	            System.out.println("El directorio es: " + qualifiedUploadFilePath);
	            int read = 0;
	            byte[] bytes = new byte[1024];
	            while ((read = inputStream.read(bytes)) != -1) {
	                outputStream.write(bytes, 0, read);
	                System.out.println("ENTRE EN EL WHILE");
	            }
	            outputStream.flush();
	            outputStream.close();
	        }
	        catch (IOException ioe) {
	            ioe.printStackTrace();
	        }
	        return qualifiedUploadFilePath;
	    }
	
}

