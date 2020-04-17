package clases;

public class Dispositivoparadalinea {
	
	private int iddispositivo_paradalinea_autobus;
	private long fecha_creacion;
	private int iddispositivo_parada;
	private int idlinea;
	
	public Dispositivoparadalinea(int iddispositivo_paradalinea_autobus, long fecha_creacion, int iddispositivo_parada,
			int idlinea) {
		super();
		this.iddispositivo_paradalinea_autobus = iddispositivo_paradalinea_autobus;
		this.fecha_creacion = fecha_creacion;
		this.iddispositivo_parada = iddispositivo_parada;
		this.idlinea = idlinea;
	}

	public int getIddispositivo_paradalinea_autobus() {
		return iddispositivo_paradalinea_autobus;
	}

	public void setIddispositivo_paradalinea_autobus(int iddispositivo_paradalinea_autobus) {
		this.iddispositivo_paradalinea_autobus = iddispositivo_paradalinea_autobus;
	}

	public long getFecha_creacion() {
		return fecha_creacion;
	}

	public void setFecha_creacion(long fecha_creacion) {
		this.fecha_creacion = fecha_creacion;
	}

	public int getIddispositivo_parada() {
		return iddispositivo_parada;
	}

	public void setIddispositivo_parada(int iddispositivo_parada) {
		this.iddispositivo_parada = iddispositivo_parada;
	}

	public int getIdlinea() {
		return idlinea;
	}

	public void setIdlinea(int idlinea) {
		this.idlinea = idlinea;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (fecha_creacion ^ (fecha_creacion >>> 32));
		result = prime * result + iddispositivo_parada;
		result = prime * result + iddispositivo_paradalinea_autobus;
		result = prime * result + idlinea;
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
		Dispositivoparadalinea other = (Dispositivoparadalinea) obj;
		if (fecha_creacion != other.fecha_creacion)
			return false;
		if (iddispositivo_parada != other.iddispositivo_parada)
			return false;
		if (iddispositivo_paradalinea_autobus != other.iddispositivo_paradalinea_autobus)
			return false;
		if (idlinea != other.idlinea)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Dispositivoparadalinea [iddispositivo_paradalinea_autobus=" + iddispositivo_paradalinea_autobus
				+ ", fecha_creacion=" + fecha_creacion + ", iddispositivo_parada=" + iddispositivo_parada + ", idlinea="
				+ idlinea + "]";
	}
	
	

}
