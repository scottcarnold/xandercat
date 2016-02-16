package org.xandercat.ofe;

import java.util.ArrayList;
import java.util.List;

import org.xandercat.ofe.filter.AttributeFilter;
import org.xandercat.ofe.filter.FilterWeight;

/**
 * Container for the list of attribute filters to be applied to one particular field.
 * 
 * @author Scott Arnold
 *
 * @param <T>    the field type
 */
public class FilterGroup<T> {

	private List<AttributeFilter<T>> filters = new ArrayList<AttributeFilter<T>>();
	private Class<T> filteredClass;
	private String fieldName;
	
	public FilterGroup(String fieldName, Class<T> filteredClass) {
		this.fieldName = fieldName;
		this.filteredClass = filteredClass;
	}
	
	public void addFilter(AttributeFilter<T> filter) {
		this.filters.add(filter);
	}

	public String getFieldName() {
		return fieldName;
	}
	
	public Class<T> getFilteredClass() {
		return filteredClass;
	}
	
	public List<AttributeFilter<T>> getFilters() {
		return filters;
	}
	
	/**
	 * Returns whether or not the candidate should continue to be considered a candidate
	 * based on the value of the field for the filter group.  This is used to enforce
	 * excluded and required rules.
	 * 
	 * @param value   the value to consider for the field
	 * 
	 * @return    whether or not the candidate with given value should remain a valid candidate for the final results
	 */
	public boolean isCandidate(Object value) {
		@SuppressWarnings("unchecked")
		T item = (T) value;
		boolean requiredFilterExists = false;
		boolean requiredFilterMatched = false;
		for (AttributeFilter<T> filter : filters) {
			if (filter.isRequired()) {
				requiredFilterExists = true;
			}
			if (filter.isMatch(item)) {
				if (filter.isExcluded()) {
					return false;
				} else if (filter.isRequired()) {
					requiredFilterMatched = true;
				}
			} 
		}
		return !requiredFilterExists || (requiredFilterExists && requiredFilterMatched);
	}

	/**
	 * Returns the weight that should be used for this field for the given value.  This will
	 * return the highest weight out of those filters for the field that match for the 
	 * given value.
	 * 
	 * @param value    the value to get weight for
	 * 
	 * @return         weight to use for the field for the given value.
	 */
	public FilterWeight getCandidateWeight(Object value) {
		FilterWeight weight = null;
		@SuppressWarnings("unchecked")
		T item = (T) value;
		for (AttributeFilter<T> filter : filters) {
			if (!filter.isExcluded() 
					&& filter.isMatch(item) 
					&& (weight == null || filter.getWeight().getWeight() > weight.getWeight())) {
				weight = filter.getWeight();
			}
		}
		return (weight == null)? FilterWeight.NONE : weight;
	}
	
	/**
	 * Returns the maximum weight possible out of all filters for the field.
	 * 
	 * @return    maximum weight possible for the field
	 */
	public FilterWeight getCandidateMaxWeight() {
		FilterWeight maxWeight = new FilterWeight(0);
		for (AttributeFilter<T> filter : filters) {
			if (!filter.isExcluded() && filter.getWeight().getWeight() > maxWeight.getWeight()) {
				maxWeight = filter.getWeight();
			}
		}
		return maxWeight;
	}
}
