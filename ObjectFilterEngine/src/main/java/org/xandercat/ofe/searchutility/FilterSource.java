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
	 * If null is returned from both getMatchThreshold and getMaxResults, the object filtering engine 
	 * will be allowed to use default values.
	 * 
	 * @param properties    properties for the filter source
	 * 
	 * @throws Exception   if any errors occur
	 */
	public void initialize(Properties properties) throws Exception;
	
	/**
	 * Returns the match threshold to use for the object filtering engine (in range 0 to 1).  
	 * 
	 * @return    match threshold to use for the object filtering engine
	 */
	public Float getMatchThreshold();
	
	/**
	 * Returns the maximum number of results that should be returned by the object filtering engine.
	 * 
	 * @return    maximum number of results
	 */
	public Integer getMaxResults();
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
