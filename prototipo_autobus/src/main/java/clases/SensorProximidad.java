package clases;

public class SensorProximidad {
	
	
	private int idsensorproximidad;
	private String nombre;
	private float valor;
	private float precision;
	private int idDispositivoAutobus;
	public SensorProximidad() {
		super();
	}
	public SensorProximidad(int idsensorproximidad, String nombre, float valor, float precision,
			int idDispositivoAutobus) {
		super();
		this.idsensorproximidad = idsensorproximidad;
		this.nombre = nombre;
		this.valor = valor;
		this.precision = precision;
		this.idDispositivoAutobus = idDispositivoAutobus;
	}
	public int getIdsensorproximidad() {
		return idsensorproximidad;
	}
	public void setIdsensorproximidad(int idsensorproximidad) {
		this.idsensorproximidad = idsensorproximidad;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public float getValor() {
		return valor;
	}
	public void setValor(float valor) {
		this.valor = valor;
	}
	public float getPrecision() {
		return precision;
	}
	public void setPrecision(float precision) {
		this.precision = precision;
	}
	public int getIdDispositivoAutobus() {
		return idDispositivoAutobus;
	}
	public void setIdDispositivoAutobus(int idDispositivoAutobus) {
		this.idDispositivoAutobus = idDispositivoAutobus;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idDispositivoAutobus;
		result = prime * result + idsensorproximidad;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + Float.floatToIntBits(precision);
		result = prime * result + Float.floatToIntBits(valor);
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
		SensorProximidad other = (SensorProximidad) obj;
		if (idDispositivoAutobus != other.idDispositivoAutobus)
			return false;
		if (idsensorproximidad != other.idsensorproximidad)
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (Float.floatToIntBits(precision) != Float.floatToIntBits(other.precision))
			return false;
		if (Float.floatToIntBits(valor) != Float.floatToIntBits(other.valor))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "SensorProximidad [idsensorproximidad=" + idsensorproximidad + ", nombre=" + nombre + ", valor=" + valor
				+ ", precision=" + precision + ", idDispositivoAutobus=" + idDispositivoAutobus + "]";
	}
	
	
	
}
