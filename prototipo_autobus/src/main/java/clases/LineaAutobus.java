package clases;

public class LineaAutobus {
	private int idlinea_autobus;
	private String nombre;
	
	public LineaAutobus() {
		super();
	}
	
	public LineaAutobus(int idlinea_autobus, String nombre) {
		super();
		this.idlinea_autobus = idlinea_autobus;
		this.nombre = nombre;
	}
	
	public int getIdlinea_autobus() {
		return idlinea_autobus;
	}
	public void setIdlinea_autobus(int idlinea_autobus) {
		this.idlinea_autobus = idlinea_autobus;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idlinea_autobus;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LineaAutobus other = (LineaAutobus) obj;
		if (idlinea_autobus != other.idlinea_autobus)
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LineaAutobus [idlinea_autobus=" + idlinea_autobus + ", nombre=" + nombre + "]";
	}
	
	
	

}
