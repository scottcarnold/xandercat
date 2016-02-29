package org.xandercat.ofe.stat;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;

public abstract class NumericStatCollector<T extends Number> implements StatCollector<T, Number> {

	protected Class<T> statCollectedClass;
	protected DataPoint<Number> dataPoint;
	protected NumberFormat numberFormat = NumberFormat.getNumberInstance();
	
	public NumericStatCollector(Class<T> statCollectedClass) {
		this.statCollectedClass = statCollectedClass;
	}
	
	public NumericStatCollector(Class<T> statCollectedClass, NumberFormat numberFormat) {
		this(statCollectedClass);
		this.numberFormat = numberFormat;
	}
	
	@Override
	public Class<T> getStatCollectedClass() {
		return statCollectedClass;
	}

	@Override
	public List<DataPoint<Number>> getStatistics() {
		return (dataPoint == null)? null : Collections.singletonList(dataPoint);
	}

	@Override
	public String formatStatistic(DataPoint<Number> dataPoint) {
		return numberFormat.format(dataPoint.getValue().doubleValue());
	}

	@Override
	public void compute() {
		Number value = computeValue();
		if (value != null) {
			this.dataPoint = new DefaultDataPoint<Number>(value);
		}
	}

	protected abstract Number computeValue();
}
