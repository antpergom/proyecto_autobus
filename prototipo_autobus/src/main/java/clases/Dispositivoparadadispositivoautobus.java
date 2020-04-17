package clases;

public class Dispositivoparadadispositivoautobus {
	private int iddispositivo_paradaDispositivo_autobus;
	private long fecha_creacion;
	private int iddispositivo_parada;
	private int iddispositivo_autobus;
	
	public Dispositivoparadadispositivoautobus(int iddispositivo_paradaDispositivo_autobus, long fecha_creacion,
			int iddispositivo_parada, int iddispositivo_autobus) {
		super();
		this.iddispositivo_paradaDispositivo_autobus = iddispositivo_paradaDispositivo_autobus;
		this.fecha_creacion = fecha_creacion;
		this.iddispositivo_parada = iddispositivo_parada;
		this.iddispositivo_autobus = iddispositivo_autobus;
	}

	public int getIddispositivo_paradaDispositivo_autobus() {
		return iddispositivo_paradaDispositivo_autobus;
	}

	public void setIddispositivo_paradaDispositivo_autobus(int iddispositivo_paradaDispositivo_autobus) {
		this.iddispositivo_paradaDispositivo_autobus = iddispositivo_paradaDispositivo_autobus;
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

	public int getIddispositivo_autobus() {
		return iddispositivo_autobus;
	}

	public void setIddispositivo_autobus(int iddispositivo_autobus) {
		this.iddispositivo_autobus = iddispositivo_autobus;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (fecha_creacion ^ (fecha_creacion >>> 32));
		result = prime * result + iddispositivo_autobus;
		result = prime * result + iddispositivo_parada;
		result = prime * result + iddispositivo_paradaDispositivo_autobus;
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
		Dispositivoparadadispositivoautobus other = (Dispositivoparadadispositivoautobus) obj;
		if (fecha_creacion != other.fecha_creacion)
			return false;
		if (iddispositivo_autobus != other.iddispositivo_autobus)
			return false;
		if (iddispositivo_parada != other.iddispositivo_parada)
			return false;
		if (iddispositivo_paradaDispositivo_autobus != other.iddispositivo_paradaDispositivo_autobus)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Dispositivoparadadispositivoautobus [iddispositivo_paradaDispositivo_autobus="
				+ iddispositivo_paradaDispositivo_autobus + ", fecha_creacion=" + fecha_creacion
				+ ", iddispositivo_parada=" + iddispositivo_parada + ", iddispositivo_autobus=" + iddispositivo_autobus
				+ "]";
	}
	
	
}
