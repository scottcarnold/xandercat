package org.xandercat.ofe;

import java.util.ArrayList;
import java.util.List;

import org.xandercat.ofe.stat.StatCollector;

public class StatCollectorGroup<T> {

	private List<StatCollector<T, ?>> statCollectors = new ArrayList<StatCollector<T, ?>>();
	private Class<T> statCollectedClass;
	private String fieldName;
	
	public StatCollectorGroup(String fieldName, Class<T> statCollectedClass) {
		this.fieldName = fieldName;
		this.statCollectedClass = statCollectedClass;
	}
	
	public void addStatCollector(StatCollector<T, ?> statCollector) {
		this.statCollectors.add(statCollector);
	}

	public String getFieldName() {
		return fieldName;
	}
	
	public Class<T> getStatCollectedClass() {
		return statCollectedClass;
	}
	
	public List<StatCollector<T, ?>> getStatCollectors() {
		return statCollectors;
	}	
	
	@SuppressWarnings("unchecked")
	public void collectStatistics(Object value) {
		for (StatCollector<T, ?> statCollector : statCollectors) {
			statCollector.addToStats((T) value);
		}
	}
	
	public void compute() {
		for (StatCollector<T, ?> statCollector : statCollectors) {
			statCollector.compute();
		}
	}
}
