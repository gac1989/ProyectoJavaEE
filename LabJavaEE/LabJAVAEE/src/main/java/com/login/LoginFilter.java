package com.login;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(filterName = "AuthFilter", urlPatterns = { "*.xhtml" })
public class LoginFilter implements Filter {

	public LoginFilter() {
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}
	
	public boolean publico(String url) {
		if(url.equals("/LabJAVAEE/faces/paginasJuegos/listarJuego.xhtml")|| url.equals("/LabJAVAEE/faces/error.xhtml")|| url.indexOf("/public/") >= 0 || url.contains("javax.faces.resource")|| url.equals("/LabJAVAEE/faces/listarBusqueda.xhtml") || url.equals("/LabJAVAEE/faces/mostrarCategorias.xhtml") || url.equals("/LabJAVAEE/faces/listarJuegosCat.xhtml") || url.equals("/LabJAVAEE/faces/perfil.xhtml") || url.equals("/LabJAVAEE/faces/perfilVisitanteDev.xhtml") ||  url.equals("/LabJAVAEE/faces/perfilVisitantePlay.xhtml") || url.equals("/LabJAVAEE/faces/listarJuegoEvento.xhtml") || url.equals("/LabJAVAEE/faces/mostrarTodosUsuarios.xhtml") || url.equals("/LabJAVAEE/faces/index.xhtml")) {
			return true;
		}
		return false;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
			HttpServletRequest reqt = (HttpServletRequest) request;
			HttpServletResponse resp = (HttpServletResponse) response;
			HttpSession ses = reqt.getSession(false);
			String type = "";
			String reqURI = reqt.getRequestURI();
			if(ses!=null) {
				type=(String)ses.getAttribute("type");
			}
			if(type==null || type.equals("")) {
				type="visitante";
			}
			System.out.println("El tipo es: " + type);
			switch(type) {
				case "jugador":{
					if (this.publico(reqURI) || reqURI.equals("/LabJAVAEE/faces/paginasJuegos/comprarJuego.xhtml") ||reqURI.equals("/LabJAVAEE/faces/admin.xhtml") || reqURI.equals("/LabJAVAEE/faces/listarpropios.xhtml") || reqURI.equals("/LabJAVAEE/faces/confirmar.xhtml")|| reqURI.equals("/LabJAVAEE/faces/perfilJugador.xhtml")||reqURI.equals("/LabJAVAEE/faces/comprar.xhtml")|| reqURI.equals("/LabJAVAEE/") ) {
						chain.doFilter(request, response);
						return;
					}
					else {
						resp.sendRedirect(reqt.getContextPath() + "/faces/index.xhtml");
						return;
					}
				}
				case "desarrollador":{
					System.out.println("LLegue al desarrollador: " + type);
					System.out.println("La ruta desarrollador es: " + reqURI);
					if (this.publico(reqURI) || reqURI.equals("/LabJAVAEE/faces/paginasJuegos/nuevoJuego.xhtml") || reqURI.equals("/LabJAVAEE/faces/index.xhtml")||reqURI.equals("/LabJAVAEE/faces/admin.xhtml")||reqURI.equals("/LabJAVAEE/faces/perfilDesarrollador.xhtml")|| reqURI.equals("/LabJAVAEE/") ) {
						chain.doFilter(request, response);
						return;
					}
					else {
						resp.sendRedirect(reqt.getContextPath() + "/faces/index.xhtml");
						return;
					}
				}
				
				case "administrador":{
					if (this.publico(reqURI) || reqURI.equals("/LabJAVAEE/faces/Admin/admin.xhtml") || reqURI.equals("/LabJAVAEE/faces/Admin/categoria.xhtml")|| reqURI.equals("/LabJAVAEE/faces/Admin/eventos.xhtml") || reqURI.equals("/LabJAVAEE/faces/Admin/stats.xhtml")|| reqURI.equals("/LabJAVAEE/faces/Admin/comentarios.xhtml")|| reqURI.equals("/LabJAVAEE/faces/Admin/juegos.xhtml") ) {
						chain.doFilter(request, response);
						return;
					}
					else {
						resp.sendRedirect(reqt.getContextPath() + "/faces/Admin/admin.xhtml");
						return;
					}
				}
				
				case "visitante":{
					if (this.publico(reqURI) || reqURI.equals("/LabJAVAEE/faces/login.xhtml")||reqURI.equals("/LabJAVAEE/faces/nuevojugador.xhtml") ||reqURI.equals("/LabJAVAEE/faces/nuevodesarrollador.xhtml")||reqURI.equals("/LabJAVAEE/")) {
						chain.doFilter(request, response);
						return;
					}
					else {
						resp.sendRedirect(reqt.getContextPath() + "/faces/login.xhtml");
						return;
					}
				}
				default:{
					
				}
			}
		
	}

	@Override
	public void destroy() {

	}
}