package org.xandercat.ofe.searchutility;

import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.xandercat.ofe.filter.AttributeFilter;

/**
 * Interface used with SearchUtility to define the filters that will be used in the object search.
 * 
 * @author Scott Arnold
 */
public interface FilterSource {

	/**
	 * Initialize the filter source from the given properties.  The properties will be a subset of
	 * the main SearchUtility properties whose keys start with the SearchUtility.FILTER_SOURCE_PROPERTY_PREFIX;
	 * property keys used will have the prefix truncated.
	 * 
	 * @param properties    properties for the filter source
	 * 
	 * @throws Exception   if any errors occur
	 */
	public void initialize(Properties properties) throws Exception;
	
	/**
	 * Returns the match threshold to use for the object filtering engine (in range 0 to 1).  If null
	 * is returned, the object filtering engine will use it's default value.
	 * 
	 * @return    match threshold to use for the object filtering engine
	 */
	public Float getMatchThreshold();
	
	/**
	 * Returns the set of field names that filters will be run against in the search.
	 * 
	 * @return    set of field names that filters will be run against
	 */
	public Set<String> getFilterFieldNames();
	
	/**
	 * Returns a list of filters for the given field name.
	 * 
	 * @param fieldName    name of field to be filtered
	 * 
	 * @return    list of filters for the given field
	 */
	public List<AttributeFilter<?>> getFilters(String fieldName);
}
