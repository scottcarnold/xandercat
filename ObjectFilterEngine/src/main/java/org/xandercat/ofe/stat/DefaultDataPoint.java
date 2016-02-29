package org.xandercat.ofe.stat;

public class DefaultDataPoint<T> implements DataPoint<T> {

	private static final long serialVersionUID = 1L;
	
	private String label;
	private T value;
	
	public DefaultDataPoint() {
	}
	
	public DefaultDataPoint(T value) {
		this(null, value);
	}
	
	public DefaultDataPoint(String label, T value) {
		this.label = label;
		this.value = value;
	}
	
	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public T getValue() {
		return value;
	}
}
