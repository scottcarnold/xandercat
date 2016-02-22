package org.xandercat.ofe.searchutility;

import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.SortedSet;

import org.xandercat.ofe.Candidate;
import org.xandercat.ofe.CandidateChange;
import org.xandercat.ofe.FilterGroup;
import org.xandercat.ofe.ScoredCandidate;

/**
 * Interface used with the SearchUtility that defines what to do with the search results.
 * 
 * @author Scott Arnold
 *
 * @param <T>   type of object that was searched
 */
public interface ResultDestination<T extends Candidate> {
	
	/**
	 * Initialize the result destination from the given properties.  The properties will be a subset of
	 * the main SearchUtility properties whose keys start with the SearchUtility.RESULT_DESTINATION_PROPERTY_PREFIX;
	 * property keys used will have the prefix truncated.
	 * 
	 * @param properties     properties for the result destination
	 * @param candidateClass the candidate class
	 * 
	 * @throws Exception   if any errors occur
	 */	
	public void initialize(Properties properties, Class<T> candidateClass) throws Exception;
	
	/**
	 * Does something with the outcome of the search.
	 * 
	 * @param threshold           the threshold for matches (from 0 to 1; a percentage); null means no threshold
	 * @param maxResults          the max results for the search; null means no maximum
	 * @param filterGroups        the collection of filters for the various fields
	 * @param changes             the changes in the search results since the last run of the search
	 * @param scoredCandidates    the search matches and their match percentages
	 */
	public void handleSearchResults(Float threshold, Integer maxResults,
			Collection<FilterGroup<?>> filterGroups, 
			List<CandidateChange<T>> changes, 
			SortedSet<ScoredCandidate<T>> scoredCandidates);
	
	/**
	 * Handles any exceptions that might have interrupted the search.  
	 * 
	 * @param e    the exception that occurred during the search
	 */
	public void handleError(Exception e);
}
