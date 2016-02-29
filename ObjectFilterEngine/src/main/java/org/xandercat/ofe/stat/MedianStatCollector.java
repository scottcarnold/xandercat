package org.xandercat.ofe.stat;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MedianStatCollector<T extends Number> extends NumericStatCollector<T> {

	private List<T> values = new ArrayList<T>();
	private Comparator<T> comparator;
	
	public MedianStatCollector(Class<T> statCollectedClass, NumberFormat numberFormat) {
		super(statCollectedClass, numberFormat);
	}

	public MedianStatCollector(Class<T> statCollectedClass) {
		super(statCollectedClass);
	}

	@Override
	public void addToStats(T item) {
		values.add(item);
	}

	@Override
	public String getDescription() {
		return "Median";
	}

	@Override
	protected Number computeValue() {
		Collections.sort(values, comparator);
		return values.get(values.size() / 2);
	}

}
