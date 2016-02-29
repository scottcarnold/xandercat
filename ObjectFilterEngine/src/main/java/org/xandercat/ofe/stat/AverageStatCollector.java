package org.xandercat.ofe.stat;

import java.text.NumberFormat;

public class AverageStatCollector<T extends Number> extends NumericStatCollector<T> {

	private double accumulator;
	private long counter;

	public AverageStatCollector(Class<T> statCollectedClass, NumberFormat numberFormat) {
		super(statCollectedClass, numberFormat);
	}

	public AverageStatCollector(Class<T> statCollectedClass) {
		super(statCollectedClass);
	}

	@Override
	public void addToStats(T item) {
		if (item != null) {
			accumulator += item.doubleValue();
			counter++;
		}
	}

	@Override
	public String getDescription() {
		return "Average";
	}

	@Override
	public Double computeValue() {
		return (counter > 0)? accumulator / (double) counter : null;
	}
}
