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
    </head>
    <body>
        <h1>Ingrese los datos de su videojuego</h1>
        <div>
            <form action="PublicarJuego" method="POST" autocomplete="off">
                Nombre: <input type='text' placeholder="nombre" autocomplete="false" name="nombre"><br><br>
                Descripción: <input type='text' placeholder="descripcion" autocomplete="nope" name="desc"><br><br>
                Portada: <input type="file" name="foto" accept="image/*"><br><br>
             <!--   Galeria de imagenes: <input type="file" name="file" multiple="true" /> -->
               <input type='submit' value='Enviar'>
            </form>
        </div>
    </body>
</html>
