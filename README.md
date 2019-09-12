# RESTful-Web-Service
Design and Implementation of an JAX-RS Java API RESTful and a working prototype of a simple service for a social network, where the users can interact between them in different ways. Developed in Eclipse with the Java programming language

Se trata de diseñar e implementar una API REST y un prototipo funcional de un servicio sencillo para una red social de tipo Facebook, donde los usuarios puedan interactuar entre ellos de diversas formas. Se asume que la autenticación y seguridad de la herramienta está realizada y por tanto no hay que implementarla.
En este servicio los usuarios publican mensajes en su página personal y pueden ser amigos de otros usuarios (relación de amistad recíproca) para poder ver los mensajes de éstos, a los que además pueden enviar mensajes privados (que no puede ver nadie más que los implicados en el envío, emisor y receptor). El servicio debe soportar a través de esa API las siguientes operaciones:
• Añadir un nuevo usuario a la red.

• Ver los datos básicos de un usuario.

• Cambiar datos básicos de nuestro perfil de usuario (excepto nombre de usuario).2

• Obtener una lista de todos los usuarios existentes en la red social3. Esta lista debe permitir ser filtrada por patrón de nombre (eg. Buscar todos los usuarios que contengan “Mar” en su nombre, “Mario”, “María”…etc.)

• Publicar un nuevo mensaje en la página personal de un usuario.

• Eliminar un mensaje propio.

• Editar un mensaje propio.

• Obtener una lista de todos los mensajes de un usuario en su página personal. Además, esta lista debe permitir la opción de ser filtrada por fecha o limitar la cantidad de información obtenida por número de mensajes (e.g. los 10 primeros elementos, los elementos entre el 11 y el 20, etc.)

• Añadir un nuevo amigo

• Eliminar un amigo

• Obtener una lista de todos nuestros amigos. Además, esta lista debe permitir la opción de ser filtrada por el patrón de nombre o limitar la cantidad de información obtenida por número de amigos (e.g. los 10 primeros elementos, los elementos entre el 11 y el 20, etc.)

• Enviar un mensaje personal a otro usuario.

• Borrar nuestro perfil de la red social.

• Obtener una lista con los últimos mensajes de las páginas de nuestros amigos ordenados por fecha (similar a como lo muestra Facebook). Esta lista debe permitir la opción de ser filtrada por la búsqueda de contenido de texto (patrón) en el mensaje.

• Consultar fácilmente la descripción necesaria para una aplicación móvil que queremos realizar, que muestre los datos básicos de un usuario, su último mensaje en la página, el número de amigos y los 10 últimos mensajes de las páginas de sus amigos que se han actualizado.

Implementar un cliente Java que pruebe ese servicio y por tanto, pruebe todas las operaciones anteriormente descritas (haciendo uso de los filtros en los casos necesarios).
