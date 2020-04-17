package clases;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SensorDistancia {
	
	private static final AtomicInteger COUNTER = new AtomicInteger();
	
	private int id;
	private float value;
	private long timestamp;
	private int accuracy;
	private TipoSensor tipo;
	
	@JsonCreator
	public SensorDistancia(
			@JsonProperty("distance")float value, 
			@JsonProperty("timestamp")long timestamp, 
			@JsonProperty("accuracy")int accuracy,
			@JsonProperty("type") TipoSensor tipo) {
		super();
		this.id = COUNTER.getAndIncrement();
		this.value = value;
		this.timestamp = timestamp;
		this.accuracy = accuracy;
		this.tipo = tipo;
	}
	
	public SensorDistancia(){
		super();
		this.id = COUNTER.getAndIncrement();
		this.value = 0;
		this.timestamp = Calendar.getInstance().getTimeInMillis();
		Calendar calendar = Calendar.getInstance();
		/*
		 * calendar.set(Calendar.DAY_OF_MONTH,25);
		 * calendar.set(Calendar.MONTH,Calendar.JANUARY);
		 * calendar.set(Calendar.HOUR,22); calendar.set(Calendar.MINUTE,15);
		 * 
		 */
		
		this.accuracy = 0;
		this.tipo = TipoSensor.Entrada;
	}
	
	public int getId() {
		return id;
	}
	
	public TipoSensor getTipo() {
		return tipo;
	}

	public void setTipo(TipoSensor tipo) {
		this.tipo = tipo;
	}

	public float getValue() {
		return value;
	}
	public void setValue(float value) {
		this.value = value;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public int getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + accuracy;
		result = prime * result + id;
		result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
		result = prime * result + Float.floatToIntBits(value);
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
		SensorDistancia other = (SensorDistancia) obj;
		if (accuracy != other.accuracy)
			return false;
		if (id != other.id)
			return false;
		if (timestamp != other.timestamp)
			return false;
		if (tipo != other.tipo)
			return false;
		if (Float.floatToIntBits(value) != Float.floatToIntBits(other.value))
			return false;
		return true;
	}
	
	
}
