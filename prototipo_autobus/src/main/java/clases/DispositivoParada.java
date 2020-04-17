package clases;

public class DispositivoParada {
	private int idDispositivoParada;
	private long fechaCreacion;
	private int numeroParada;
	private float longitud;
	private float latitud;
	
	public DispositivoParada(){
		super();
	}
	
	public DispositivoParada(int idDispositivoParada, long fechaCreacion, int numeroParada, float longitud, float latitud) {
		super();
		this.idDispositivoParada = idDispositivoParada;
		this.fechaCreacion = fechaCreacion;
		this.numeroParada = numeroParada;
		this.longitud = longitud;
		this.latitud = latitud;
	}


	public int getIdDispositivoParada() {
		return idDispositivoParada;
	}


	public void setIdDispositivoParada(int idDispositivoParada) {
		this.idDispositivoParada = idDispositivoParada;
	}


	public long getFechaCreacion() {
		return fechaCreacion;
	}


	public void setFechaCreacion(long fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}


	public int getNumeroParada() {
		return numeroParada;
	}


	public void setNumeroParada(int numeroParada) {
		this.numeroParada = numeroParada;
	}


	public float getLongitud() {
		return longitud;
	}


	public void setLongitud(float longitud) {
		this.longitud = longitud;
	}


	public float getLatitud() {
		return latitud;
	}


	public void setLatitud(float latitud) {
		this.latitud = latitud;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (fechaCreacion ^ (fechaCreacion >>> 32));
		result = prime * result + idDispositivoParada;
		result = prime * result + Float.floatToIntBits(latitud);
		result = prime * result + Float.floatToIntBits(longitud);
		result = prime * result + numeroParada;
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
		DispositivoParada other = (DispositivoParada) obj;
		if (fechaCreacion != other.fechaCreacion)
			return false;
		if (idDispositivoParada != other.idDispositivoParada)
			return false;
		if (Float.floatToIntBits(latitud) != Float.floatToIntBits(other.latitud))
			return false;
		if (Float.floatToIntBits(longitud) != Float.floatToIntBits(other.longitud))
			return false;
		if (numeroParada != other.numeroParada)
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "DispositivoParada [idDispositivoParada=" + idDispositivoParada + ", fechaCreacion=" + fechaCreacion
				+ ", numeroParada=" + numeroParada + ", longitud=" + longitud + ", latitud=" + latitud + "]";
	}
	
	
	
}
