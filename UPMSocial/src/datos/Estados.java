
package clase.datos;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "estados")
public class Estados {

	private ArrayList<Link> estados;

	public Estados() {
		this.estados = new ArrayList<Link>();
	}

	@XmlElement(name = "estado")
	public ArrayList<Link> getEstados() {
		return estados;
	}

}
