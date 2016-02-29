package org.xandercat.ofe.stat;

import java.util.List;

public interface StatCollector<T, U> {

	public Class<T> getStatCollectedClass();
	
	public void addToStats(T item);
	
	public String getDescription();
	
	public void compute();
	
	public List<DataPoint<U>> getStatistics();
	
	public String formatStatistic(DataPoint<U> dataPoint);
}
