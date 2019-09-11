package clase.recursos.memoria;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import clase.datos.Usuario;

@Path("/usuarios")
public class UsuariosRecurso {

	@Context
	private UriInfo uriInfo;
	/*
	 * private Usuario usuario
	 * 
	 * public UsuariosRecurso() { this.usuario = new Usuario(); }
	 */

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getUsuarios() {
		return Response.status(Response.Status.OK).build();
	}

}
