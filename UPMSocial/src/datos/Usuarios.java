package clase.datos;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;

@XmlRootElement(name = "usuarios")
public class Usuarios {

	private ArrayList<Link> usuarios;

	public Usuarios() {
		this.usuarios = new ArrayList<Link>();
	}

}
