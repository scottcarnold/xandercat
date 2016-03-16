package org.xandercat.ofe.searchutility;

import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.xandercat.ofe.stat.StatCollector;

/**
 * Interface used with SearchUtility to define the stat collectors that will be used in the object search.
 * 
 * @author Scott Arnold
 */
public interface StatCollectorSource {

	/**
	 * Initialize the stat collector source from the given properties.  The properties will be a subset of
	 * the main SearchUtility properties whose keys start with the SearchUtility.STAT_COLLECTOR_SOURCE_PROPERTY_PREFIX;
	 * property keys used will have the prefix truncated.
	 * 
	 * @param properties    properties for the filter source
	 * 
	 * @throws Exception   if any errors occur
	 */
	public void initialize(Properties properties) throws Exception;
	
	/**
	 * Returns the set of field names that stat collectors will be run against in the search.
	 * 
	 * @return    set of field names that stat collectors will be run against
	 */
	public Set<String> getStatCollectorFieldNames();
	
	/**
	 * Returns a list of stat collectors for the given field name.
	 * 
	 * @param fieldName    name of field to collect statistics on
	 * 
	 * @return    list of stat collectors for the given field
	 */
	public List<StatCollector<?, ?>> getStatCollectors(String fieldName);
}
