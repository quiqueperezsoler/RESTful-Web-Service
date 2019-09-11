/*
 * Author: Javier Gallego Carracedo
 * Date: 05/2018
 */

package clase.recursos.bbdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.naming.NamingContext;

import clase.datos.Link;
import clase.datos.Usuario;
import clase.datos.Usuarios;

@Path("/usuarios")
public class UsuariosRecurso {

	@Context
	private UriInfo uriInfo;

	private DataSource ds;
	private Connection conn;

	public UsuariosRecurso() {
		InitialContext ctx;
		try {
			ctx = new InitialContext();
			NamingContext envCtx = (NamingContext) ctx.lookup("java:comp/env");

			ds = (DataSource) envCtx.lookup("jdbc/UPMSocial");
			conn = ds.getConnection();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getUsuarios() {
		try {
			String sql = "SELECT * FROM UPMSocial.Usuario;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			Usuarios g = new Usuarios();
			ArrayList<Link> usuarios = g.getUsuarios();
			rs.beforeFirst();
			while (rs.next()) {
				usuarios.add(new Link(uriInfo.getAbsolutePath() + "/" + rs.getInt("id"), "self"));
			}
			return Response.status(Response.Status.OK).entity(g).build(); // No se puede devolver el ArrayList (para
																			// generar XML)
		} catch (NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No se pudieron convertir los índices a números")
					.build();
		} catch (SQLException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error de acceso a BBDD").build();
		}
	}

	@GET
	@Path("{usuario_id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getUsuario(@PathParam("usuario_id") String id) {
		try {
			int int_id = Integer.parseInt(id);
			String sql = "SELECT * FROM Usuario where id=?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, int_id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Usuario usuario = usuarioFromRS(rs);
				return Response.status(Response.Status.OK).entity(usuario).build();
			}
		} catch (NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No puedo parsear a entero").build();
		} catch (SQLException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error de acceso a BBDD").build();
		}
	}

	private Usuario usuarioFromRS(ResultSet rs) throws SQLException {
		Usuario usuario = new Usuario(rs.getString("nombre"), rs.getString("descripcion"));
		usuario.setId(rs.getInt("id"));
		return usuario;
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("{usuario_id}")
	public Response updateUsuario(@PathParam("usuario_id") String id, Usuario nuevoUsuario) {
		try {
			Usuario usuario;
			int int_id = Integer.parseInt(id);
			String sql = "SELECT * FROM UPMSocial.Usuario where id=?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, int_id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				usuario = usuarioFromRS(rs);
			} else {
				return Response.status(Response.Status.NOT_FOUND).entity("Elemento no encontrado").build();
			}
			usuario.setDescripcion(nuevoUsuario.getDescripcion());

			String location = uriInfo.getBaseUri() + "usuarios/" + usuario.getId();
			return Response.status(Response.Status.OK).entity(usuario).header("Content-Location", location).build();
		} catch (SQLException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("No se pudo actualizar el usuario\n" + e.getStackTrace()).build();
		}
	}

	@DELETE
	@Path("{usuario_id}")
	public Response deleteUsuario(@PathParam("usuario_id") String id) {
		try {
			int int_id = Integer.parseInt(id);
			String sql = "DELETE FROM UPMSocial.Usuario";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, int_id);
			int affectedRows = ps.executeUpdate();
			if (affectedRows == 1)
				return Response.status(Response.Status.NO_CONTENT)();
			else
				return Response.status(Response.Status.NOT_FOUND).entity("Elemento no encontrado").build();
		} catch (SQLException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("No se pudo eliminar el usaurio\n" + e.getStackTrace()).build();
		}
	}
}
