package com.controllers;

import java.util.List;
import com.dao.ComentarioDAO;
import com.interfaces.ComentarioInterface;
import com.model.Comentario;
import com.model.Estado;

public class ComentarioController implements ComentarioInterface {

	@Override
	public boolean reportarComentario(String id) {
		ComentarioDAO controlcom = new ComentarioDAO();
		Comentario c1 = controlcom.buscar(Integer.parseInt(id));
		if(c1!=null) {
			c1.setEstado(Estado.REPORTADO);
			controlcom.editar(c1);
			return true;
		}
		return false;
	}

	@Override
	public List<Comentario> getComentariosReportados() {
		ComentarioDAO control = new ComentarioDAO();
		List<Comentario> lista = control.obtenerComentariosReportados();
		if(!lista.isEmpty()) {
			return lista;
		}
		else {
			return null;
		}
	}

	@Override
	public boolean bloquearComentario(String id) {
		ComentarioDAO controlcom = new ComentarioDAO();
		Comentario c1 = controlcom.buscar(Integer.parseInt(id));
		if(c1!=null) {
			c1.setEstado(Estado.BLOQUEADO);
			controlcom.editar(c1);
			return true;
		}
		return false;
	}

	@Override
	public boolean desbloquearComentario(String id) {
		ComentarioDAO controlcom = new ComentarioDAO();
		Comentario c1 = controlcom.buscar(Integer.parseInt(id));
		if(c1!=null) {
			c1.setEstado(Estado.ACTIVO);
			controlcom.editar(c1);
			return true;
		}
		return false;
	}

}
