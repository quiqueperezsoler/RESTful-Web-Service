# RESTful-Web-Service
Design and Implementation of a API REST and a working prototype of a simple service for a social network, where the users can interact between them in different ways. Developed in Eclipse with the Java programming language

Práctica: Diseño e implementación de un servicio web RESTful
Es común encontrar una dificultad (cuando no reticencia) inicial a la hora de abordar un diseño RESTful de un servicio web. El principal problema recae en que REST no es algo tangible como pueda ser una especificación o una API. En su lugar, REST denota un estilo arquitectónico, esto es, un conjunto nombrado de restricciones sobre la interacción de diferentes componentes de la arquitectura de la Web que, cuando se respeta, dota a la arquitectura resultante de determinadas propiedades arquitectónicas deseables (por ejemplo, maximiza el incremento de la información identificada dentro del sistema). REST puede verse, por tanto, desde un punto de vista práctico, como un conjunto de mejores prácticas de diseño arquitectónico construidas sobre los estándares de http, URI y los principios arquitectónicos de la Web.
Como consecuencia, muchos servicios web y muchas API que se denominan RESTful no lo son en realidad, ya que sus diseños no siguen esas mejores prácticas y caen en errores como los siguientes:
a) Los objetos relevantes no se exponen como recursos, por lo que no podemos acceder a ellos directamente.
b) Los métodos HTTP no se utilizan correctamente. En su lugar todo se realiza mediante GET (incluso las operaciones que producen cambios) o POST.
c) Las representaciones de los recursos no están interconectadas, por lo que no puede “navegarse” desde un recurso dado al resto de recursos que conceptualmente deberían estar ligados al primero.
Estas APIs no pueden denominarse RESTful. En su lugar, deberían ser tratadas como meras APIs POX-RPC en las que se envían y reciben mensajes XML “planos” (sin ensobrado SOAP) mediante peticiones al estilo RPC sobre un protocolo de “transporte” http, utilizando un conjunto de parámetros opcionales en la cadena de consulta que influyen en los resultados obtenidos. Se trata de APIs equivalentes a las ofrecidas por los servicios SOAP o XML-RPC1.
Teniendo en mente todo lo anteriormente comentado, esta práctica se plantea atendiendo a dos objetivos fundamentales:
a) Que el alumno aborde el diseño RESTful de un servicio sencillo, aplicando las mejores prácticas y las recomendaciones explicadas en las sesiones presenciales de la asignatura.
b) Que el alumno practique con el entorno Eclipse + Tomcat y la implementación de referencia de la API JAX-RS (Jersey) que soporta ese entorno construyendo una implementación prototipo del servicio diseñado en el paso anterior. Porúltimo, se plantea al alumno el desarrollo de un cliente sencillo que sirva de prueba del servicio implementado.
Ejercicio propuesto
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
