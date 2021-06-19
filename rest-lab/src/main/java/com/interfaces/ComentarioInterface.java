package com.interfaces;

import java.util.List;

import com.model.Comentario;

public interface ComentarioInterface {
	
	public boolean reportarComentario(String id);
	
	public List<Comentario> getComentariosReportados();
	
	public boolean bloquearComentario(String id);
	
	public boolean desbloquearComentario(String id);
	
}
