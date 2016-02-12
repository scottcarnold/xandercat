package org.xandercat.ofe.searchutility;

import java.util.Collection;
import java.util.Properties;

/**
 * Interface used with SearchUtility that defines how to obtain the collection of candidates to be searched.
 * 
 * @author Scott Arnold
 *
 * @param <T>   candidate class type
 */
public interface CandidateSource<T> {

	/**
	 * Initialize the candidate source from the given properties.  The properties will be a subset of
	 * the main SearchUtility properties whose keys start with the SearchUtility.CANDIDATE_SOURCE_PROPERTY_PREFIX;
	 * property keys used will have the prefix truncated.
	 * 
	 * @param properties     properties for the candidate source
	 * @param candidateClass the candidate class
	 * 
	 * @throws Exception   if any errors occur
	 */
	public void initialize(Properties properties, Class<T> candidateClass) throws Exception;
	
	/**
	 * Return the collection of candidates to search.
	 * 
	 * @return    collection of candidates to search
	 * 
	 * @throws Exception   if any errors occur
	 */
	public Collection<T> getCandidates() throws Exception;
}
