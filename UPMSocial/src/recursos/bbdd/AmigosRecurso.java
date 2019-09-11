

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
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.naming.NamingContext;

import clase.datos.Amigo;
import clase.datos.Link;
import clase.datos.Usuarios;

@Path("/amigos")
public class AmigosRecurso {

	@Context
	private UriInfo uriInfo;

	private DataSource ds;
	private Connection conn;

	public AmigosRecurso() {
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
	public Response getPosiblesAmigos(@QueryParam("pattern") @DefaultValue("") String pattern) {
		try {
			String sql = "SELECT * FROM UPMSocial.Usuario WHERE nombre LIKE ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			pattern = "%" + pattern + "%";
			ps.setString(1, pattern);
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

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createAmigo(Amigo amigo) {
		try {
			String sql = "INSERT INTO UPMSocial.esAmigo (idUsuarioa, idUsuariob) VALUES (?,?);";
			PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, amigo.getIdUsuario1());
			ps.setInt(2, amigo.getIdUsuario2());
			ps.executeUpdate();
			ResultSet generatedID = ps.getGeneratedKeys();
			if (generatedID.next()) {
				amigo.setId(generatedID.getInt(1));
				String location = uriInfo.getAbsolutePath() + "/" + amigo.getId();
				return Response.status(Response.Status.CREATED).header("Location", location)
						.header("Content-Location", location).build();
			}
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("No se pudo crear la relacion de amistad").build();

		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("No se pudo crear la amistad\n" + e.getStackTrace()).build();
		}
	}

	@GET
	@Path("{amistad_id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getPosiblesAmigos(@PathParam("amistad_id") String id,
			@QueryParam("pattern") @DefaultValue("") String pattern,
			@QueryParam("offset") @DefaultValue("1") String offset,
			@QueryParam("count") @DefaultValue("10") String count) {
		try {
			int int_id = Integer.parseInt(id);
			int off = Integer.parseInt(offset);
			int c = Integer.parseInt(count);
			String sql = "SELECT U.id FROM UPMSocial.Usuario U, UPMSocial.esAmigo A WHERE U.id = A.idUsuariob AND U.nombre LIKE ? AND A.idUsuarioa = ? LIMIT ?,?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			pattern = "%" + pattern + "%";
			ps.setString(1, pattern);
			ps.setInt(2, int_id);
			ps.setInt(3, off - 1);
			ps.setInt(4, c);
			ResultSet rs = ps.executeQuery();
			Usuarios g = new Usuarios();
			ArrayList<Link> usuarios = g.getUsuarios();
			rs.beforeFirst();
			while (rs.next()) {
				usuarios.add(new Link(uriInfo.getBaseUri() + "usuarios/" + rs.getInt("id"), "self"));
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

}
