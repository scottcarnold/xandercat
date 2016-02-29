package org.xandercat.ofe.stat;

import java.util.Collections;
import java.util.List;

import org.xandercat.ofe.filter.AttributeFilter;

public class MatchCountStatCollector<T> implements StatCollector<T, Integer> {

	private Class<T> statCollectedClass;
	private AttributeFilter<T> filter;
	private int matchCount;
	private DataPoint<Integer> dataPoint;
	
	public MatchCountStatCollector(Class<T> statCollectedClass, AttributeFilter<T> filter) {
		this.statCollectedClass = statCollectedClass;
		this.filter = filter;
	}
	
	@Override
	public Class<T> getStatCollectedClass() {
		return statCollectedClass;
	}

	@Override
	public void addToStats(T item) {
		if (filter.isMatch(item)) {
			matchCount++;
		}
	}

	@Override
	public String getDescription() {
		return "Matches for " + filter.getDescription();
	}

	@Override
	public void compute() {
		this.dataPoint = new DefaultDataPoint<Integer>(Integer.valueOf(matchCount));
	}

	@Override
	public List<DataPoint<Integer>> getStatistics() {
		return Collections.singletonList(dataPoint);
	}

	@Override
	public String formatStatistic(DataPoint<Integer> dataPoint) {
		return dataPoint.getValue().toString();
	}



}
