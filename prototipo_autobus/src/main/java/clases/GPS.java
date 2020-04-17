package clases;

public class GPS {
	private int idsensorgps;
	private String nombre;
	private float valorLatitud;
	private float valorLongitud;
	private float precision;
	private int idDispositivoAutobus;
	public GPS() {
		super();
	}
	public GPS(int idsensorgps, String nombre, float valorLatitud, float valorLongitud, float precision,
			int idDispositivoAutobus) {
		super();
		this.idsensorgps = idsensorgps;
		this.nombre = nombre;
		this.valorLatitud = valorLatitud;
		this.valorLongitud = valorLongitud;
		this.precision = precision;
		this.idDispositivoAutobus = idDispositivoAutobus;
	}
	public int getIdsensorgps() {
		return idsensorgps;
	}
	public void setIdsensorgps(int idsensorgps) {
		this.idsensorgps = idsensorgps;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public float getValorLatitud() {
		return valorLatitud;
	}
	public void setValorLatitud(float valorLatitud) {
		this.valorLatitud = valorLatitud;
	}
	public float getValorLongitud() {
		return valorLongitud;
	}
	public void setValorLongitud(float valorLongitud) {
		this.valorLongitud = valorLongitud;
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
		result = prime * result + idsensorgps;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + Float.floatToIntBits(precision);
		result = prime * result + Float.floatToIntBits(valorLatitud);
		result = prime * result + Float.floatToIntBits(valorLongitud);
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
		GPS other = (GPS) obj;
		if (idDispositivoAutobus != other.idDispositivoAutobus)
			return false;
		if (idsensorgps != other.idsensorgps)
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (Float.floatToIntBits(precision) != Float.floatToIntBits(other.precision))
			return false;
		if (Float.floatToIntBits(valorLatitud) != Float.floatToIntBits(other.valorLatitud))
			return false;
		if (Float.floatToIntBits(valorLongitud) != Float.floatToIntBits(other.valorLongitud))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "GPS [idsensorgps=" + idsensorgps + ", nombre=" + nombre + ", valorLatitud=" + valorLatitud
				+ ", valorLongitud=" + valorLongitud + ", precision=" + precision + ", idDispositivoAutobus="
				+ idDispositivoAutobus + "]";
	}
	
	
	

}
