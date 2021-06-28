/# ProyectoJavaEE
Proyecto Java EE 2021

*Software necesario:

  - Wildlfy: Version 22 en adelante
  - Java: Version 1.8
  - MySQL: 8.0
  - Eclipse IDE for Enterprise Java and Web Developers 2021-03
  
*Pasos para ejecutar el proyecto desde eclipse:
  
  1- Crear en la conexion MySQL a utilizar una nueva base de datos(schema) llamada "labjava_base" sin las comillas.
  2- En Eclipse configurar el ambiente agregando el servidor Wildlfy a la lista de servidores disponibles y asegurarse que esta configurada la version correspondiente de Java. Nota: Es posible que para agregar el servidor Wildfly sea necesario instalar el plugin JBoss Tools desde Eclipse Marketplace.
  3- Importar los proyectos: LabJAVAEE(Front) y rest-lab(API).
  4- En el Project Explorer de Eclipse, actualizar las dependencias de los proyectos, haciendo click derecho sobre cada proyecto, y en la opcion de "Maven" seleccionando "Update project".
  5- Editar el archivo "persistence.xml" ubicado en el proyecto rest-lab en la ruta src/main/java/META-INF, ingresando las credenciales correspondientes(user, password de la conexion MySQL a utiizar).
  6- En el Project explorer de Eclipse click derecho en el proyecto rest-lab y seleccionar la opcion "Run as" y "Run on server".
  7- Seleccionar el servidor Wildfly y luego hacer click en Finish, el procedimiento es el mismo para el proyecto LabJAVAEE.
