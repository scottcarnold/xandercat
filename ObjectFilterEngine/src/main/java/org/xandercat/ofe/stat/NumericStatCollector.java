package org.xandercat.ofe.stat;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public abstract class NumericStatCollector<T extends Number> implements StatCollector<T, Number> {

	protected Class<T> statCollectedClass;
	protected List<DataPoint<Number>> dataPoints;
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
		return dataPoints;
	}

	@Override
	public String formatStatistic(DataPoint<Number> dataPoint) {
		if (dataPoint.getLabel() != null && dataPoint.getLabel().length() > 0) {
			return dataPoint.getLabel() + ": " + numberFormat.format(dataPoint.getValue().doubleValue());
		} else {
			return numberFormat.format(dataPoint.getValue().doubleValue());
		}
	}

	@Override
	public void compute() {
		Number[] values = computeValues();
		if (values != null) {
			this.dataPoints = new ArrayList<DataPoint<Number>>();
			String[] labels = getValueLabels();
			for (int i=0; i<values.length; i++) {
				if (labels != null && labels.length > i) {
					this.dataPoints.add(new DefaultDataPoint<Number>(labels[i], values[i]));
				} else {
					this.dataPoints.add(new DefaultDataPoint<Number>(values[i]));
				}
			}
		}
	}

	protected String[] getValueLabels() {
		return null;
	}
	
	protected abstract Number[] computeValues();
}
