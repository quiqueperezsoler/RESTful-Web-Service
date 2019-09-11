

package clase.datos;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "amigo")
public class Amigo {

	private int id;
	private int idUsuario1;
	private int idUsuario2;

	public Amigo(int idUsuario1, int idUsuario2) {
		this.idUsuario1 = idUsuario1;
		this.idUsuario2 = idUsuario2;
	}

	public Amigo() {
	}

	@XmlAttribute(required = false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdUsuario1() {
		return idUsuario1;
	}

	public void setIdUsuario1(int idUsuario1) {
		this.idUsuario1 = idUsuario1;
	}

}
