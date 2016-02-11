package org.xandercat.ofe.filter;

import java.io.Serializable;
import java.text.NumberFormat;

public class FilterWeight implements Comparable<FilterWeight>, Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final FilterWeight MAXIMUM = new FilterWeight(1f, true);
	public static final FilterWeight HIGH = new FilterWeight(0.75f, true);
	public static final FilterWeight MEDIUM = new FilterWeight(0.5f, true);
	public static final FilterWeight LOW = new FilterWeight(0.25f, true);
	public static final FilterWeight NONE = new FilterWeight(0f, true);
	
	private float weight;
	private boolean unmodifiable;
	
	public FilterWeight(float weight) {
		if (weight < 0 || weight > 1) {
			throw new IllegalArgumentException("Weight must be between 0 and 1 inclusive.");
		}
		this.weight = weight;
	}
	
	public FilterWeight(float weight, boolean unmodifiable) {
		this(weight);
		this.unmodifiable = unmodifiable;
	}
	
	public float getWeight() {
		return weight;
	}
	
	public void setWeight(float weight) {
		if (weight < 0 || weight > 1) {
			throw new IllegalArgumentException("Weight must be between 0 and 1 inclusive.");
		}
		if (unmodifiable) {
			throw new UnsupportedOperationException("Unmodifiable.");
		}
		this.weight = weight;
	}
	
	public void increaseWeight(float weight) {
		if (weight < 0 || weight > 1) {
			throw new IllegalArgumentException("Weight must be between 0 and 1 inclusive.");
		}
		if (unmodifiable) {
			throw new UnsupportedOperationException("Unmodifiable.");
		}
		this.weight += (1f - this.weight) * weight;
	}
	
	public void increaseWeight(FilterWeight weight) {
		increaseWeight(weight.getWeight());
	}

	@Override
	public int compareTo(FilterWeight o) {
		if (weight < o.weight) {
			return 1;
		} else if (weight > o.weight) {
			return -1;
		} else {
			return 0;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(weight);
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
		FilterWeight other = (FilterWeight) obj;
		if (Float.floatToIntBits(weight) != Float.floatToIntBits(other.weight))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return NumberFormat.getPercentInstance().format(weight);
	}
}
