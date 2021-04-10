<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Comprar Juego</title>

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
	
	<h1>Comprar Juego</h1>
	<form name='formTpv' method='post' action='https://www.sandbox.paypal.com/cgi-bin/webscr'>

	<input type='hidden' name='cmd' value='_xclick'>
    Email:<input type='text' name='business' value='sb-v143vd5841920@personal.example.com'> <br><br>
    Nombre del Juego:  <input type='text' name='item_name' value='<%= (String)request.getAttribute("juego") %>' readonly="readonly"> <br><br>
    Item Numero: <input type='text' name='item_number' value='VENTA-X2561' readonly="readonly"> <br><br>
    Precio:   <input type='text' name='amount' value="<%= request.getAttribute("precio") %>" readonly="readonly"> <br><br>
        <input type='hidden' name='page_style' value='primary'>
        <input type='hidden' name='no_shipping' value='1'>
        <input type='hidden' name='return' value='http://localhost:8080/CUCriticos/compraExito.html'>
        <input type='hidden' name='rm' value='2'>
        <input type='hidden' name='cancel_return' value='http://localhost:8080/CUCriticos/compraCancelada.html'>
        <input type='hidden' name='no_note' value='1'>
        <input type='hidden' name='currency_code' value='EUR'>
        <input type='hidden' name='cn' value='PP-BuyNowBF'>
        <input type='hidden' name='custom' value=''>
   Nombre: <input type='text' name='first_name' value='NOMBRE'> <br><br>
   Apellido: <input type='text' name='last_name' value='APELLIDOS'> <br><br>
   Direccion: <input type='text' name='address1' value='DIRECCIÓN'> <br><br>
   Ciudad: <input type='text' name='city' value='POBLACIÓN'> <br><br>
   Codigo Postal:     <input type='text' name='zip' value='CÓDIGO POSTAL'> <br><br>
        <input type='hidden' name='night_phone_a' value=''> 
   Telefono:     <input type='text' name='night_phone_b' value='TELÉFONO'> <br><br>
        <input type='hidden' name='night_phone_c' value=''>
        <input type='hidden' name='lc' value='es'>
        <input type='hidden' name='country' value='ES'>
	<input type="submit" value="pagar">
	</form>
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta2/dist/js/bootstrap.bundle.min.js" integrity="sha384-b5kHyXgcpbZJO/tY9Ul7kGkf1S0CWuKcCD38l8YkeH8z8QjE0GmW1gYU5S9FOnJ0" crossorigin="anonymous"></script>
</body>
</html>