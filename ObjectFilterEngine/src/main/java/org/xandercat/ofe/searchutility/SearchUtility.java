package org.xandercat.ofe.searchutility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.xandercat.ofe.Candidate;
import org.xandercat.ofe.CandidateChange;
import org.xandercat.ofe.ObjectFilterEngine;
import org.xandercat.ofe.ScoredCandidate;
import org.xandercat.ofe.filter.AttributeFilter;

/**
 * Generic search utility for running a collection of objects against an object filtering engine
 * and processing the search results.
 * 
 * @author Scott Arnold
 *
 * @param <T>   the type of object being searched
 */
public class SearchUtility<T extends Candidate> {

	public static final String FILTER_SOURCE_PROPERTY_PREFIX = "filter.source.";
	public static final String CANDIDATE_SOURCE_PROPERTY_PREFIX = "candidate.source.";
	public static final String RESULT_DESTINATION_PROPERTY_PREFIX = "result.destination.";
	
	private static final Logger LOGGER = Logger.getLogger(SearchUtility.class);
	private static final String DEFAULT_PREVIOUS_RESULTS_FILE_NAME = "previous-results.dat";
	
	private Properties properties;
	private FilterSource filterSource;
	private CandidateSource<T> candidateSource;
	private ResultDestination<T> resultDestination;
	
	private static Properties getTrimmedProperties(String propertyPrefix, Properties properties) {
		Properties trimmedProperties = new Properties();
		for (String propertyName : properties.stringPropertyNames()) {
			if (propertyName.startsWith(propertyPrefix)) {
				trimmedProperties.setProperty(propertyName.substring(propertyPrefix.length()), properties.getProperty(propertyName));
			}
		}
		return trimmedProperties;
	}
	
	@SuppressWarnings("unchecked")
	public SearchUtility(Properties properties) throws Exception {
		this.properties = properties;
		String filterSourceClassName = properties.getProperty(FILTER_SOURCE_PROPERTY_PREFIX + "class");
		String candidateSourceClassName = properties.getProperty(CANDIDATE_SOURCE_PROPERTY_PREFIX + "class");
		String resultDestinationClassName = properties.getProperty(RESULT_DESTINATION_PROPERTY_PREFIX + "class");
		this.filterSource = (FilterSource) Class.forName(filterSourceClassName).newInstance();
		this.candidateSource = (CandidateSource<T>) Class.forName(candidateSourceClassName).newInstance();
		this.resultDestination = (ResultDestination<T>) Class.forName(resultDestinationClassName).newInstance();
		this.filterSource.initialize(getTrimmedProperties(FILTER_SOURCE_PROPERTY_PREFIX, properties));
		this.candidateSource.initialize(getTrimmedProperties(CANDIDATE_SOURCE_PROPERTY_PREFIX, properties));
		this.resultDestination.initialize(getTrimmedProperties(RESULT_DESTINATION_PROPERTY_PREFIX, properties));
	}
	
	public void searchAndNotify() {
		try {
			File scoredCandidatesFile = new File(properties.getProperty("previous.results.file", DEFAULT_PREVIOUS_RESULTS_FILE_NAME));
			ObjectFilterEngine<T> ofe = new ObjectFilterEngine<T>(filterSource.getMatchThreshold());
			for (String fieldName : filterSource.getFilterFieldNames()) {
				for (AttributeFilter<?> filter : filterSource.getFilters(fieldName)) {
					ofe.addFilter(fieldName, filter);
				}			
			}
			Collection<T> candidates = candidateSource.getCandidates();
			ofe.addCandidates(candidates);
			List<ScoredCandidate<T>> scoredCandidates = ofe.getScoredCandidates();
			LOGGER.info("Candidates found: " + scoredCandidates.size() + " out of " + candidates.size() + " candidates.");
			List<ScoredCandidate<T>> previousScoredCandidates = loadPreviousScoredCandidates(scoredCandidatesFile);
			List<CandidateChange<T>> changes = ofe.getDifferences(previousScoredCandidates);
			resultDestination.handleSearchResults(ofe.getScoreThreshold(), ofe.getFilterGroups(), 
					changes, scoredCandidates);
			saveScoredCandidates(scoredCandidatesFile, scoredCandidates);
		} catch (Exception e) {
			LOGGER.error("An error occurred while executing the search.", e);
			resultDestination.handleError(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<ScoredCandidate<T>> loadPreviousScoredCandidates(File previousScoredCandidatesFile) throws IOException, ClassNotFoundException {
		List<ScoredCandidate<T>> previousScoredCandidates = null;
		if (previousScoredCandidatesFile.isFile()) {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(previousScoredCandidatesFile));
			previousScoredCandidates = (List<ScoredCandidate<T>>) ois.readObject();
			ois.close();
		} else {
			LOGGER.warn("No prior scored candidates found.");
		}
		return previousScoredCandidates;
	}
	
	private void saveScoredCandidates(File scoredCandidatesFile, List<ScoredCandidate<T>> scoredCandidates) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(scoredCandidatesFile));
		oos.writeObject(scoredCandidates);
		oos.close();
	}
}
