

package clase.datos;

import java.sql.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "usuario")
public class Estado {

	private int id;
	private int idUsuario;
	private String texto;
	private Date fecha;

	public Estado(int idUsuario, String texto) {
		this.idUsuario = idUsuario;
		this.texto = texto;
		this.fecha = new Date(System.currentTimeMillis());
	}

	public Estado() {
	}

	@XmlAttribute(required = false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Date getFecha() {
		return fecha;
	}

}
