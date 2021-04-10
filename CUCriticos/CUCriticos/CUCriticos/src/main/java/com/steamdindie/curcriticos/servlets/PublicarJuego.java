/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steamdindie.curcriticos.servlets;

import com.steamdindie.curcriticos.controladores.ControladorJuegos;
import com.steamindie.cucriticos.clases.Game;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author german
 */
@MultipartConfig
public class PublicarJuego extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            
   /*         String nombre = request.getParameter("nombre");
            String desc = request.getParameter("desc");
            
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.steamindie_CUCriticos_war_1.0-SNAPSHOTPU"); // el nombre de la unidad de persistencia
    
            EntityManager em = emf.createEntityManager();
    

            
            Game game1= new Game();
            game1.setNombre(nombre);
            game1.setDescripcion(desc);
            em.getTransaction().begin();
            em.persist(game1);
            em.getTransaction().commit();*/
            
            request.getRequestDispatcher("WEB-INF/PublicarJuego.jsp").forward(request, response); 
        	//response.sendRedirect("PublicarJuego.jsp");
        }catch (Exception e) {
			// TODO: handle exception
        	System.err.println("Algo fue mal");
		}        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
        //response.sendRedirect("WEB-INF/PublicarJuego.jsp");
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
            //request.getRequestDispatcher("WEB-INF/PublicarJuego.jsp").forward(request, response);
        
            String nombre = request.getParameter("nombre");
            String desc = request.getParameter("desc");
            String portada = request.getParameter("foto");
            String campoprecio = request.getParameter("precio");
            String dir_portada = "img/default.jpg";
          
            float precio=Float.parseFloat(campoprecio);
            
            if(portada!=null && !portada.equals("") ){
                dir_portada = "img/" + portada;
            }
            
            //EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.steamindie_CUCriticos_war_1.0-SNAPSHOTPU"); // el nombre de la unidad de persistencia
            //EntityManager em = emf.createEntityManager();
            
            ControladorJuegos controler = new ControladorJuegos();
            
            if (!nombre.equals("") && !desc.equals("")){
                Game game1= new Game();
                game1.setNombre(nombre);
                game1.setDescripcion(desc);
                game1.setPortada(dir_portada);
                game1.setPrecio(precio);
                controler.persistirJuego(game1);
            }
            
          request.getRequestDispatcher("WEB-INF/PublicarJuego.jsp").forward(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
