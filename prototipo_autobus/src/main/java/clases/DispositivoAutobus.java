package clases;

public class DispositivoAutobus {

	public DispositivoAutobus() {
		super();
	}
	
	public DispositivoAutobus(int iddispositivo_autobus, String identificador_autobus, float fecha_creacion,
			int capacidad, int ocupacion, int idlinea_autobus, int idadministrador) {
		super();
		this.iddispositivo_autobus = iddispositivo_autobus;
		this.identificador_autobus = identificador_autobus;
		this.fecha_creacion = fecha_creacion;
		this.capacidad = capacidad;
		this.ocupacion = ocupacion;
		this.idlinea_autobus = idlinea_autobus;
		this.idadministrador = idadministrador;
	}
	private int iddispositivo_autobus;
	private String identificador_autobus;
	private float fecha_creacion;
	private int capacidad;
	private int ocupacion;
	private int idlinea_autobus;
	private int idadministrador;
	
	public int getIddispositivo_autobus() {
		return iddispositivo_autobus;
	}
	public void setIddispositivo_autobus(int iddispositivo_autobus) {
		this.iddispositivo_autobus = iddispositivo_autobus;
	}
	public String getIdentificador_autobus() {
		return identificador_autobus;
	}
	public void setIdentificador_autobus(String identificador_autobus) {
		this.identificador_autobus = identificador_autobus;
	}
	public float getFecha_creacion() {
		return fecha_creacion;
	}
	public void setFecha_creacion(float fecha_creacion) {
		this.fecha_creacion = fecha_creacion;
	}
	public int getCapacidad() {
		return capacidad;
	}
	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}
	public int getOcupacion() {
		return ocupacion;
	}
	public void setOcupacion(int ocupacion) {
		this.ocupacion = ocupacion;
	}
	public int getIdlinea_autobus() {
		return idlinea_autobus;
	}
	public void setIdlinea_autobus(int idlinea_autobus) {
		this.idlinea_autobus = idlinea_autobus;
	}
	public int getIdadministrador() {
		return idadministrador;
	}
	public void setIdadministrador(int idadministrador) {
		this.idadministrador = idadministrador;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + capacidad;
		result = prime * result + Float.floatToIntBits(fecha_creacion);
		result = prime * result + idadministrador;
		result = prime * result + iddispositivo_autobus;
		result = prime * result + ((identificador_autobus == null) ? 0 : identificador_autobus.hashCode());
		result = prime * result + idlinea_autobus;
		result = prime * result + ocupacion;
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
		DispositivoAutobus other = (DispositivoAutobus) obj;
		if (capacidad != other.capacidad)
			return false;
		if (Float.floatToIntBits(fecha_creacion) != Float.floatToIntBits(other.fecha_creacion))
			return false;
		if (idadministrador != other.idadministrador)
			return false;
		if (iddispositivo_autobus != other.iddispositivo_autobus)
			return false;
		if (identificador_autobus == null) {
			if (other.identificador_autobus != null)
				return false;
		} else if (!identificador_autobus.equals(other.identificador_autobus))
			return false;
		if (idlinea_autobus != other.idlinea_autobus)
			return false;
		if (ocupacion != other.ocupacion)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "DispositivoAutobus [iddispositivo_autobus=" + iddispositivo_autobus + ", identificador_autobus="
				+ identificador_autobus + ", fecha_creacion=" + fecha_creacion + ", capacidad=" + capacidad
				+ ", ocupacion=" + ocupacion + ", idlinea_autobus=" + idlinea_autobus + ", idadministrador="
				+ idadministrador + "]";
	}
	
	
	

}
