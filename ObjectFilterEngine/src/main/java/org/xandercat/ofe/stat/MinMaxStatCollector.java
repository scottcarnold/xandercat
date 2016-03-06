package org.xandercat.ofe.stat;

import java.text.NumberFormat;

public class MinMaxStatCollector<T extends Number> extends NumericStatCollector<T> {

	private T min;
	private T max;
	
	public MinMaxStatCollector(Class<T> statCollectedClass, NumberFormat numberFormat) {
		super(statCollectedClass, numberFormat);
	}

	public MinMaxStatCollector(Class<T> statCollectedClass) {
		super(statCollectedClass);
	}

	@Override
	public void addToStats(T item) {
		if (min == null) {
			min = item;
		}
		if (max == null) {
			max = item;
		}
		if (item.doubleValue() < min.doubleValue()) {
			min = item;
		}
		if (item.doubleValue() > max.doubleValue()) {
			max = item;
		}
	}

	@Override
	public String getDescription() {
		return "Minimum and Maximum";
	}

	@Override
	protected String[] getValueLabels() {
		return new String[] { "Minimum", "Maximum" };
	}

	@Override
	protected Number[] computeValues() {
		return new Number[] { min, max };
	}

}
