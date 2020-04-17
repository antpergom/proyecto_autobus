package clases;

public class Administrador {
	private int idAdministrador;
	private String dni;
	private String nombre;
	private String apellidos;
	private long fecha_creacion;
	
	public Administrador() {
		super();
	}
	
	public Administrador(int idAdministrador, String dni, String nombre, String apellidos, long fecha_creacion) {
		super();
		this.idAdministrador = idAdministrador;
		this.dni = dni;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.fecha_creacion = fecha_creacion;
	}

	public int getIdAdministrador() {
		return idAdministrador;
	}

	public void setIdAdministrador(int idAdministrador) {
		this.idAdministrador = idAdministrador;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public long getfecha_creacion() {
		return fecha_creacion;
	}

	public void setfecha_creacion(long fecha_creacion) {
		this.fecha_creacion = fecha_creacion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((apellidos == null) ? 0 : apellidos.hashCode());
		result = prime * result + ((dni == null) ? 0 : dni.hashCode());
		result = prime * result + idAdministrador;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + (int) (fecha_creacion ^ (fecha_creacion >>> 32));
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
		Administrador other = (Administrador) obj;
		if (apellidos == null) {
			if (other.apellidos != null)
				return false;
		} else if (!apellidos.equals(other.apellidos))
			return false;
		if (dni == null) {
			if (other.dni != null)
				return false;
		} else if (!dni.equals(other.dni))
			return false;
		if (idAdministrador != other.idAdministrador)
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (fecha_creacion != other.fecha_creacion)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Administrador [idAdministrador=" + idAdministrador + ", dni=" + dni + ", nombre=" + nombre
				+ ", apellidos=" + apellidos + ", fecha_creacion=" + fecha_creacion + "]";
	}
	
	
	
}

