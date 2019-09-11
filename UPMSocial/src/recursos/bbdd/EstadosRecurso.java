/*
 * Author: Javier Gallego Carracedo
 * Date: 05/2018
 */

package clase.recursos.bbdd;

import java.sql.Connection;
import java.sql.Date;
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

import clase.datos.Estado;
import clase.datos.Estados;
import clase.datos.Link;

@Path("/estados")
public class EstadosRecurso {

	@Context
	private UriInfo uriInfo;

	private DataSource ds;
	private Connection conn;

	public EstadosRecurso() {
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

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createEstado(Estado estado) {
		try {
			String sql = "INSERT INTO UPMSocial.Estado (idUsuario,texto,fecha) " + "VALUES (?,?,?)";
			PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, estado.getIdUsuario());
			ps.setString(2, estado.getTexto());
			ps.setDate(3, new Date(System.currentTimeMillis()));
			ps.executeUpdate();
			ResultSet generatedID = ps.getGeneratedKeys();
			if (generatedID.next()) {
				estado.setId(generatedID.getInt(1));
				String location = uriInfo.getAbsolutePath() + "/" + estado.getId();
				return Response.status(Response.Status.CREATED).header("Location", location)
						.header("Content-Location", location).build();
			}
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("No se pudo crear el estado").build();

		} catch (SQLException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("No se pudo crear el estado\n" + e.getStackTrace()).build();
		}
	}

	@DELETE
	@Path("{estado_id}")
	public Response deleteEstado(@PathParam("estado_id") String id) {
		try {
			int int_id = Integer.parseInt(id);
			String sql = "DELETE FROM UPMSocial.Estado WHERE id=?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, int_id);
			int affectedRows = ps.executeUpdate();
			if (affectedRows == 1)
				return Response.status(Response.Status.NO_CONTENT).build();
			else
				return Response.status(Response.Status.NOT_FOUND).entity("Elemento no encontrado").build();
		} catch (SQLException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("No se pudo eliminar el estado\n" + e.getStackTrace()).build();
		}
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getEstado(@QueryParam("offset") @DefaultValue("1") String offset,
			@QueryParam("count") @DefaultValue("10") String count,
			@QueryParam("iniDate") @DefaultValue("2000-01-01") String fechaIni,
			@QueryParam("lastDate") @DefaultValue("") String lastFecha) {
		try {
			if (lastFecha.equals(""))
				lastFecha = new Date(System.currentTimeMillis()).toString();
			int off = Integer.parseInt(offset);
			int c = Integer.parseInt(count);
			Date ini = Date.valueOf(fechaIni);
			Date last = Date.valueOf(lastFecha);
			if (ini.after(last))
				return Response.status(Response.Status.BAD_REQUEST).entity("Fecha inicio posterior a final").build();
			String sql = "SELECT * FROM UPMSocial.Estado WHERE (fecha BETWEEN ? AND ?) ORDER BY id LIMIT ?,?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setDate(1, ini);
			ps.setDate(2, last);
			ps.setInt(3, off - 1);
			ps.setInt(4, c);
			ResultSet rs = ps.executeQuery();
			Estados e = new Estados();
			ArrayList<Link> estados = e.getEstados();
			rs.beforeFirst();
			while (rs.next()) {
				estados.add(new Link(uriInfo.getAbsolutePath() + "/" + rs.getInt("id"), "self"));
			}
			return Response.status(Response.Status.OK).entity(e).build(); // No se puede devolver el ArrayList (para
																			// generar XML)
		} catch (NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No se pudieron convertir los índices a números")
					.build();
		} catch (SQLException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error de acceso a BBDD").build();
		}
	}

	@GET
	@Path("recuento")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response countEstados(@QueryParam("iniDate") @DefaultValue("2000-01-01") String fechaIni,
			@QueryParam("lastDate") @DefaultValue("") String lastFecha) {
		try {
			if (lastFecha.equals(""))
				lastFecha = new Date(System.currentTimeMillis()).toString();
			Date ini = Date.valueOf(fechaIni);
			Date last = Date.valueOf(lastFecha);
			String sql = "SELECT * FROM UPMSocial.Estado WHERE (fecha BETWEEN ? AND ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setDate(1, ini);
			ps.setDate(2, last);
			ResultSet rs = ps.executeQuery();
			rs.beforeFirst();
			int i = 0;
			while (rs.next())
				i++;
			if (i > 0)
				return Response.status(Response.Status.OK).entity("" + i).build();
			else
				return Response.status(Response.Status.NOT_FOUND).entity("Elemento no encontrado").build();
		} catch (SQLException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("No se pudo acceder a los estados\n" + e.getStackTrace()).build();
		}
	}

	@GET
	@Path("amigos/{id_usuario}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getEstado(@PathParam("id_usuario") String id,
			@QueryParam("offset") @DefaultValue("1") String offset,
			@QueryParam("count") @DefaultValue("10") String count,
			@QueryParam("iniDate") @DefaultValue("2000-01-01") String fechaIni,
			@QueryParam("lastDate") @DefaultValue("") String lastFecha) {
		try {
			if (lastFecha.equals(""))
				lastFecha = new Date(System.currentTimeMillis()).toString();
			int int_id = Integer.parseInt(id);
			int off = Integer.parseInt(offset);
			int c = Integer.parseInt(count);
			Date ini = Date.valueOf(fechaIni);
			Date last = Date.valueOf(lastFecha);
			if (ini.after(last))
				return Response.status(Response.Status.BAD_REQUEST).entity("Fecha inicio posterior a final").build();
			String sql = "SELECT E.id FROM UPMSocial.Estado E, UPMSocial.esAmigo A WHERE E.idUsuario = A.idUsuariob AND (E.fecha BETWEEN ? AND ?) AND A.idUsuarioa = ? LIMIT ?,?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setDate(1, ini);
			ps.setDate(2, last);
			ps.setInt(3, int_id);
			ps.setInt(4, off - 1);
			ps.setInt(5, c);
			ResultSet rs = ps.executeQuery();
			Estados e = new Estados();
			ArrayList<Link> estados = e.getEstados();
			rs.beforeFirst();
			while (rs.next()) {
				estados.add(new Link(uriInfo.getBaseUri() + "estados/" + rs.getInt("id"), "self"));
			}
			return Response.status(Response.Status.OK).entity(e).build(); // No se puede devolver el ArrayList (para
																			// generar XML)
		} catch (NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No se pudieron convertir los índices a números")
					.build();
		} catch (SQLException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error de acceso a BBDD").build();
		}
	}

	@GET
	@Path("amigos/{id_usuario}/filtrar")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getEstadoFiltro(@PathParam("id_usuario") String id,
			@QueryParam("pattern") @DefaultValue("") String pattern,
			@QueryParam("offset") @DefaultValue("1") String offset,
			@QueryParam("count") @DefaultValue("10") String count) {
		try {
			int int_id = Integer.parseInt(id);
			int off = Integer.parseInt(offset);
			int c = Integer.parseInt(count);
			String sql = "SELECT E.id FROM UPMSocial.Estado E, UPMSocial.esAmigo A WHERE E.idUsuario = A.idUsuariob AND E.texto LIKE ? AND A.idUsuarioa = ? LIMIT ?,?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			pattern = "%" + pattern + "%";
			ps.setString(1, pattern);
			ps.setInt(2, int_id);
			ps.setInt(3, off - 1);
			ps.setInt(4, c);
			ResultSet rs = ps.executeQuery();
			Estados e = new Estados();
			ArrayList<Link> estados = e.getEstados();
			rs.beforeFirst();
			while (rs.next()) {
				estados.add(new Link(uriInfo.getBaseUri() + "estados/" + rs.getInt("id"), "self"));
			}
			return Response.status(Response.Status.OK).entity(e).build(); // No se puede devolver el ArrayList (para
																			// generar XML)
		} catch (NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No se pudieron convertir los índices a números")
					.build();
		} catch (SQLException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error de acceso a BBDD").build();
		}
	}

	/************************* EXTRA ***********************************/
	@GET
	@Path("{estado_id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getUsuario(@PathParam("estado_id") String id) {
		try {
			int int_id = Integer.parseInt(id);
			String sql = "SELECT * FROM Estado where id=?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, int_id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Estado estado = estadoFromRS(rs);
				return Response.status(Response.Status.OK).entity(estado).build();
			} else {
				return Response.status(Response.Status.NOT_FOUND).entity("Elemento no encontrado").build();
			}
		} catch (NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No puedo parsear a entero").build();
		} catch (SQLException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error de acceso a BBDD").build();
		}
	}

	private Estado estadoFromRS(ResultSet rs) throws SQLException {
		Estado estado = new Estado(rs.getInt("idUsuario"), rs.getString("texto"));
		estado.setId(rs.getInt("id"));
		estado.setFecha(rs.getDate("fecha"));
		return estado;
	}

}
