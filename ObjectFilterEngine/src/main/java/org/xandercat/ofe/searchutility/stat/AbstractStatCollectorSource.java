package org.xandercat.ofe.searchutility.stat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.xandercat.ofe.searchutility.StatCollectorSource;
import org.xandercat.ofe.stat.StatCollector;

public abstract class AbstractStatCollectorSource implements StatCollectorSource {

	protected Map<String, List<StatCollector<?, ?>>> statCollectorMap = new HashMap<String, List<StatCollector<?, ?>>>();
	
	protected void addStatCollector(String fieldName, StatCollector<?, ?> statCollector) {
		List<StatCollector<?, ?>> statCollectorsForField = statCollectorMap.get(fieldName);
		if (statCollectorsForField == null) {
			statCollectorsForField = new ArrayList<StatCollector<?, ?>>();
			statCollectorMap.put(fieldName, statCollectorsForField);
		}
		statCollectorsForField.add(statCollector);
	}
	
	@Override
	public Set<String> getStatCollectorFieldNames() {
		return statCollectorMap.keySet();
	}

	@Override
	public List<StatCollector<?, ?>> getStatCollectors(String fieldName) {
		return statCollectorMap.get(fieldName);
	}
}
