package org.xandercat.ofe.searchutility;

import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.xandercat.ofe.stat.StatCollector;

public interface StatCollectorSource {

	public void initialize(Properties properties) throws Exception;
	
	public Set<String> getStatCollectorFieldNames();
	
	public List<StatCollector<?, ?>> getStatCollectors(String fieldName);
}
