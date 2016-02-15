package org.xandercat.ofe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xandercat.ofe.filter.AttributeFilter;

/**
 * Filtering engine for running a collection of objects against a set of filters and returning
 * a collection of matches that exceed some set match threshold.  The filtering engine can also 
 * provide a listing of filtered collection changes or differences, useful for tracking changes
 * to the matches over time.
 *  
 * @author Scott Arnold
 *
 * @param <T>   the type of object to be filtered; must implement Candidate and be Serializable
 */
public class ObjectFilterEngine<T extends Candidate> {

	private Map<String, FilterGroup<?>> filterGroups = new HashMap<String, FilterGroup<?>>();
	private List<ScoredCandidate<T>> scoredCandidates = new ArrayList<ScoredCandidate<T>>();
	private float scoreThreshold = 0.5f;
	
	public ObjectFilterEngine() {
	}
	
	public ObjectFilterEngine(float scoreThreshold) {
		this.scoreThreshold = scoreThreshold;
	}
	
	public float getScoreThreshold() {
		return this.scoreThreshold;
	}
	
	public Collection<FilterGroup<?>> getFilterGroups() {
		return filterGroups.values();
	}
	
	/**
	 * Add a filter for the given field.  The field name must match a getter method within
	 * the filtered class type.
	 * 
	 * @param field
	 * @param attributeFilter
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
	
	/**
	 * Run a potential candidate against the list of filters.  Make sure all filters have been added before
	 * calling this method.
	 * 
	 * @param item                       the object to run against the filters
	 * 
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public void addCandidate(T item) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		float combinedWeight = 0f;
		float maxCombinedWeight = 0f;
		for (Map.Entry<String, FilterGroup<?>> entry : filterGroups.entrySet()) {
			String field = entry.getKey();
			FilterGroup<?> filter = entry.getValue();
			maxCombinedWeight += filter.getCandidateMaxWeight().getWeight();
			Method getterMethod = ReflectionUtil.getterMethod(field, filter.getFilteredClass(), item.getClass(), true);
			Object value = null;
			try {
				Object invocationTarget = ReflectionUtil.getInvocationTarget(field, item, false);
				value = getterMethod.invoke(invocationTarget, (Object[]) null);
			} catch (NullPointerException npe) {
				// this means a nested object on a complex path was null; in this case, just treat as a null value
				value = null;
			} 
			if (filter.isCandidate(value)) {
				combinedWeight += filter.getCandidateWeight(value).getWeight();
			} else {
				return;  // reject due to exclude match or required without match
			}
		}
		if (combinedWeight > 0) {
			float score = combinedWeight / maxCombinedWeight;
			if (score >= this.scoreThreshold) {
				scoredCandidates.add(new ScoredCandidate<T>(score, item));
			}
		}
	}
	
	/**
	 * Run a group of candidates against the list of filters.  Make sure all filters have been added before
	 * calling this method.
	 * 
	 * @param items                        the objects to run against the filters
	 * 
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
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
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public void addCandidates(Collection<T> items) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		for (T item : items) {
			addCandidate(item);
		}
	}
	
	/**
	 * Returns the list of candidates that matched the list of filters.
	 * 
	 * @return   list of candidates that matched the list of filters
	 */
	public List<ScoredCandidate<T>> getScoredCandidates() {
		Collections.sort(scoredCandidates);
		return scoredCandidates;
	}
	
	/**
	 * Returns a list of differences between the matched candidates stored in this filtering engine
	 * and another list of candidates (presumably from a previous run).
	 * 
	 * @param previousScoredCandidates    matched candidates to compare against
	 * 
	 * @return    list of differences (additions, changes, removals) between the matched candidates.
	 */
	public List<CandidateChange<T>> getDifferences(List<ScoredCandidate<T>> previousScoredCandidates) {
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
	
//	private String getterName(String fieldName, Class<?> filteredClass) {
//		String prefix = (filteredClass == Boolean.TYPE)? "is" : "get";
//		return prefix + fieldName.substring(0,  1).toUpperCase() + fieldName.substring(1);
//	}
}
