<%@page import="java.util.List"%>
<%@page import="com.steamindie.cucriticos.clases.Game"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Listar Juegos</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-eOJMYsd53ii+scO/bJGFsiCZc+5NDVN2yr8+0RDqr0Ql0h+rP48ckxlpbzKgwra6" crossorigin="anonymous">
<style>

        .card{
            float: left;
            padding: 10px;
            width: max-content;
            margin-left: 15px;
            margin-top: 20px;
        }
        .card:hover {
            /* opacity: 0.8; */
            transform: scale(1.1);
        }

</style>

<script>
        var xhr = new XMLHttpRequest();
        xhr.open('POST', 'cabezal.html');
        xhr.setRequestHeader('Content-Type', 'text/plain');
        xhr.send();
        xhr.onload = function (data) {
            document.querySelector("nav").innerHTML = data.currentTarget.response;
            //para más de una barra de navegación
            /*var navs = document.querySelectorAll("nav");
            for(var i=0; i<navs.length; i++) {
                navs[i].innerHTML = data.currentTarget.response;
            }*/
            
        };
</script>


</head>
<body>

	  	<div class="contenedor">
	        <header>
	            <!-- barra de navegación -->
	            <nav></nav>
	        </header>
    	</div>


	<h1>Lista de juegos de SteamIndie</h1>

        
         <%
        	List<Game> juegos = (List)request.getAttribute("resultados");
        	for(Game juego : juegos){
        %>
        <div class="card" style="width: 18rem;">
		  <img src="<%=juego.getPortada() %>" class="card-img-top" alt="..." width="100" height="200" >
		  <div class="card-body">
		    <h5 class="card-title"><%=juego.getNombre() %></h5>
		    <h6>U$S <%=juego.getPrecio() %></h6>
		    <p class="card-text"><%=juego.getDescripcion() %></p>
		    <a href="ComprarJuego?Id=<%=juego.getId()%>"><input type="button" value="Comprar" class="btn btn-primary"></a>
		  </div>
		  
		</div>
		<% } %> 
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/js/bootstrap.bundle.min.js" integrity="sha384-JEW9xMcG8R+pH31jmWH6WWP0WintQrMb4s7ZOdauHnUtxwoG2vI5DkLtS3qm9Ekf" crossorigin="anonymous"></script> 
</body>
</html>