package org.xandercat.ofe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.xandercat.ofe.filter.AttributeFilter;
import org.xandercat.ofe.stat.StatCollector;

/**
 * Filtering engine for running a collection of objects against a set of filters and returning
 * a collection of matches that exceed some set match threshold and/or limited by a set maximum
 * number of results.  The filtering engine can also provide a listing of filtered collection 
 * changes or differences, useful for tracking changes to the matches over time.
 *  
 * @author Scott Arnold
 *
 * @param <T>   the type of object to be filtered; must implement Candidate
 */
public class ObjectFilterEngine<T extends Candidate> {

	private Map<String, FilterGroup<?>> filterGroups = new HashMap<String, FilterGroup<?>>();
	private Map<String, StatCollectorGroup<?>> statCollectorGroups = new HashMap<String, StatCollectorGroup<?>>();
	private SortedSet<ScoredCandidate<T>> scoredCandidates = new TreeSet<ScoredCandidate<T>>();
	private Float scoreThreshold = Float.valueOf(0.5f);
	private Integer maxResults = null;
	
	public ObjectFilterEngine() {
	}
	
	public ObjectFilterEngine(float scoreThreshold) {
		this(Float.valueOf(scoreThreshold), null);
	}
	
	public ObjectFilterEngine(int maxResults) {
		this(null, Integer.valueOf(maxResults));
	}
	
	public ObjectFilterEngine(Float scoreThreshold, Integer maxResults) {
		this.scoreThreshold = scoreThreshold;
		this.maxResults = maxResults;
	}
	
	public Float getScoreThreshold() {
		return scoreThreshold;
	}
	
	public Integer getMaxResults() {
		return maxResults;
	}
	
	public Collection<FilterGroup<?>> getFilterGroups() {
		return filterGroups.values();
	}
	
	public Collection<StatCollectorGroup<?>> getStatCollectorGroups() {
		return statCollectorGroups.values();
	}
	
	/**
	 * Add a filter for the given field.  The field name must match a getter method within
	 * the filtered class type.
	 * 
	 * @param field              the field the filter should be applied to
	 * @param attributeFilter    the filter
	 * @param <S>                class type the filter applies to
	 */
	public <S> void addFilter(String field, AttributeFilter<S> attributeFilter) {
		@SuppressWarnings("unchecked")
		FilterGroup<S> aggFilter = (FilterGroup<S>) filterGroups.get(field);
		if (aggFilter == null) {
			aggFilter = new FilterGroup<S>(field, attributeFilter.getFilteredClass());
			filterGroups.put(field, aggFilter);
		}
		aggFilter.addFilter(attributeFilter);
	}
	
	public <S> void addStatCollector(String field, StatCollector<S, ?> statCollector) {
		@SuppressWarnings("unchecked")
		StatCollectorGroup<S> group = (StatCollectorGroup<S>) statCollectorGroups.get(field);
		if (group == null) {
			group = new StatCollectorGroup<S>(field, statCollector.getStatCollectedClass());
			statCollectorGroups.put(field, group);
		}
		group.addStatCollector(statCollector);
	}
	
	private Object getCandidateFieldValue(String field, Class<?> fieldClass, Object candidate) 
			throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Method getterMethod = ReflectionUtil.getterMethod(field, fieldClass, candidate.getClass(), true);
		try {
			Object invocationTarget = ReflectionUtil.getInvocationTarget(field, candidate, false);
			return getterMethod.invoke(invocationTarget, (Object[]) null);
		} catch (NullPointerException npe) {
			// this means a nested object on a complex path was null; in this case, just treat as a null value
		}		
		return null;
	}
	
	/**
	 * Run a potential candidate against the list of filters.  Make sure all filters have been added before
	 * calling this method.
	 * 
	 * @param item                       the object to run against the filters
	 * 
	 * @throws NoSuchMethodException     if reflection failure
	 * @throws InvocationTargetException if reflection failure
	 * @throws IllegalAccessException    if reflection failure
	 */
	public void addCandidate(T item) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		for (Map.Entry<String, StatCollectorGroup<?>> entry : statCollectorGroups.entrySet()) {
			String field = entry.getKey();
			StatCollectorGroup<?> statCollectorGroup = entry.getValue();
			Object value = getCandidateFieldValue(field, statCollectorGroup.getStatCollectedClass(), item);
			statCollectorGroup.collectStatistics(value);
		}
		float combinedWeight = 0f;
		float maxCombinedWeight = 0f;
		for (Map.Entry<String, FilterGroup<?>> entry : filterGroups.entrySet()) {
			String field = entry.getKey();
			FilterGroup<?> filterGroup = entry.getValue();
			maxCombinedWeight += filterGroup.getCandidateMaxWeight().getWeight();
			Object value = getCandidateFieldValue(field, filterGroup.getFilteredClass(), item);
			if (filterGroup.isCandidate(value)) {
				combinedWeight += filterGroup.getCandidateWeight(value).getWeight();
			} else {
				return;  // reject due to exclude match or required without match
			}
		}
		if (combinedWeight > 0) {
			float score = combinedWeight / maxCombinedWeight;
			if (this.scoreThreshold == null || score >= this.scoreThreshold.floatValue()) {
				scoredCandidates.add(new ScoredCandidate<T>(score, item));
			}
			if (this.maxResults != null && scoredCandidates.size() > this.maxResults.intValue()) {
				scoredCandidates.remove(scoredCandidates.last());
			}
		}
	}
	
	/**
	 * Run a group of candidates against the list of filters.  Make sure all filters have been added before
	 * calling this method.
	 * 
	 * @param items                        the objects to run against the filters
	 * 
	 * @throws NoSuchMethodException     if reflection failure
	 * @throws InvocationTargetException if reflection failure
	 * @throws IllegalAccessException    if reflection failure
	 */
	@SuppressWarnings("unchecked")
	public void addCandidates(T... items) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		for (T item : items) {
			addCandidate(item);
		}
	}

	/**
	 * Run a group of candidates against the list of filters.  Make sure all filters have been added before
	 * calling this method.
	 * 
	 * @param items                        the objects to run against the filters
	 * 
	 * @throws NoSuchMethodException     if reflection failure
	 * @throws InvocationTargetException if reflection failure
	 * @throws IllegalAccessException    if reflection failure
	 */
	public void addCandidates(Collection<T> items) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		for (T item : items) {
			addCandidate(item);
		}
	}
	
	/**
	 * Returns the sorted set of candidates that matched the list of filters.
	 * 
	 * @return   sorted set of candidates that matched the list of filters
	 */
	public SortedSet<ScoredCandidate<T>> getScoredCandidates() {
		return scoredCandidates;
	}
	
	public void computeStatistics() {
		for (StatCollectorGroup<?> statCollectorGroup : statCollectorGroups.values()) {
			statCollectorGroup.compute();
		}
	}
	
	/**
	 * Returns a list of differences between the matched candidates stored in this filtering engine
	 * and another list of candidates (presumably from a previous run).
	 * 
	 * @param previousScoredCandidates    matched candidates to compare against
	 * 
	 * @return    list of differences (additions, changes, removals) between the matched candidates.
	 */
	public List<CandidateChange<T>> getDifferences(SortedSet<ScoredCandidate<T>> previousScoredCandidates) {
		List<CandidateChange<T>> differences = new ArrayList<CandidateChange<T>>();
		Map<String, ScoredCandidate<T>> currentCandidates = new HashMap<String, ScoredCandidate<T>>();
		for (ScoredCandidate<T> scoredCandidate : scoredCandidates) {
			currentCandidates.put(scoredCandidate.getCandidate().getUniqueId(), scoredCandidate);
		}
		Map<String, ScoredCandidate<T>> previousCandidates = new HashMap<String, ScoredCandidate<T>>();
		if (previousScoredCandidates != null) {
			for (ScoredCandidate<T> scoredCandidate : previousScoredCandidates) {
				previousCandidates.put(scoredCandidate.getCandidate().getUniqueId(), scoredCandidate);
			}
		}
		for (Map.Entry<String, ScoredCandidate<T>> entry : currentCandidates.entrySet()) {
			String key = entry.getKey();
			ScoredCandidate<T> currentCandidate = entry.getValue();
			if (previousCandidates.containsKey(key)) {
				ScoredCandidate<T> previousCandidate = previousCandidates.get(key);
				if (!currentCandidate.equals(previousCandidate)) {
					differences.add(new CandidateChange<T>(ChangeType.CHANGED, previousCandidate, currentCandidate));
				}
				previousCandidates.remove(key);
			} else {
				differences.add(new CandidateChange<T>(ChangeType.ADDED, null, currentCandidate));
			}
		}
		for (Map.Entry<String, ScoredCandidate<T>> entry : previousCandidates.entrySet()) {
			differences.add(new CandidateChange<T>(ChangeType.REMOVED, entry.getValue(), null));
		}
		Collections.sort(differences);
		return differences;
	}
}
