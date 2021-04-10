<%-- 
    Document   : PublicarJuego
    Created on : 07/04/2021, 03:10:46 PM
    Author     : german
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Publique su videojuego</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-eOJMYsd53ii+scO/bJGFsiCZc+5NDVN2yr8+0RDqr0Ql0h+rP48ckxlpbzKgwra6" crossorigin="anonymous">
        
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
    
        <h1>Ingrese los datos de su videojuego</h1>
        <div>
            <form action="PublicarJuego" method="POST" autocomplete="off">
                Nombre: <input type='text' placeholder="nombre" autocomplete="false" name="nombre"><br><br>
                Descripción: <input type='text' placeholder="descripcion" autocomplete="nope" name="desc"><br><br>
                Precio: <input type="number" placeholder="descripcion" autocomplete="nope" name="precio"><br><br>
                Portada: <input type="file" name="foto" accept="image/*"><br><br>
             <!--   Galeria de imagenes: <input type="file" name="file" multiple="true" /> -->
               <input type='submit' value='Enviar'>
            </form>
        </div>
   	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/js/bootstrap.bundle.min.js" integrity="sha384-JEW9xMcG8R+pH31jmWH6WWP0WintQrMb4s7ZOdauHnUtxwoG2vI5DkLtS3qm9Ekf" crossorigin="anonymous"></script>
    </body>
</html>
