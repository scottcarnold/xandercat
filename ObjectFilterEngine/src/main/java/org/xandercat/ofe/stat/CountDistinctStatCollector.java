package org.xandercat.ofe.stat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountDistinctStatCollector<T> implements StatCollector<T, Integer> {

	private Class<T> statCollectedClass;
	private Map<T, int[]> counts = new HashMap<T, int[]>();
	private List<DataPoint<Integer>> dataPoints = new ArrayList<DataPoint<Integer>>();
	
	public CountDistinctStatCollector(Class<T> statCollectedClass) {
		this.statCollectedClass = statCollectedClass;
	}
	
	@Override
	public Class<T> getStatCollectedClass() {
		return statCollectedClass;
	}

	@Override
	public void addToStats(T item) {
		int[] count = counts.get(item);
		if (count == null) {
			count = new int[] { 1 };
			counts.put(item, count);
		}
		count[0]++;
	}

	@Override
	public String getDescription() {
		return "Count distinct occurrences";
	}

	@Override
	public void compute() {
		for (Map.Entry<T, int[]> entry : counts.entrySet()) {
			dataPoints.add(new DefaultDataPoint<Integer>(entry.getKey().toString(), Integer.valueOf(entry.getValue()[0])));
		}
		Collections.sort(dataPoints, new Comparator<DataPoint<Integer>>() {
			@Override
			public int compare(DataPoint<Integer> o1, DataPoint<Integer> o2) {
				return -o1.getValue().compareTo(o2.getValue());
			}
		});
	}

	@Override
	public List<DataPoint<Integer>> getStatistics() {
		return dataPoints;
	}

	@Override
	public String formatStatistic(DataPoint<Integer> dataPoint) {
		return dataPoint.getLabel() + " occurs " + dataPoint.getValue().toString() + " times";
	}
}
