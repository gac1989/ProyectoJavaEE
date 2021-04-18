package com.lab.java;

import javax.ws.rs.POST;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.dao.UsuarioDAO;
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
	
	
}

