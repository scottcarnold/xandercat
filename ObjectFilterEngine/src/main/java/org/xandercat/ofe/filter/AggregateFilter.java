package org.xandercat.ofe.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Filter that combines the tests of multiple other filters into a single filter.
 * Required, excluded, or weight changes should be set on the aggregate filter itself;
 * required, excluded, and weight values of the internal filters are ignored.
 * 
 * @author Scott Arnold
 *
 * @param <T>
 */
public class AggregateFilter<T> extends AbstractFilter<T> {

	private Class<T> clazz;
	private List<AttributeFilter<T>> filters = new ArrayList<AttributeFilter<T>>();
	
	public AggregateFilter() {
	}
	
	@SafeVarargs
	public AggregateFilter(AttributeFilter<T>... attributeFilters) {
		for (AttributeFilter<T> attributeFilter : attributeFilters) {
			if (clazz != null && clazz != attributeFilter.getFilteredClass()) {
				// take care of class checking here to allow for safe generic varargs
				throw new IllegalArgumentException("Attribute filter not of expected class type; expected " + clazz.getName() + "; found " + attributeFilter.getFilteredClass().getName());
			}
			addFilter(attributeFilter);
		}
	}
	
	public void addFilter(AttributeFilter<T> filter) {
		if (clazz == null) {
			this.clazz = filter.getFilteredClass();
		}
		this.filters.add(filter);
	}
	
	@Override
	public Class<T> getFilteredClass() {
		return clazz;
	}

	@Override
	public boolean isMatch(T item) {
		for (AttributeFilter<T> filter : filters) {
			if (!filter.isMatch(item)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String getMatchDescription() {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<filters.size(); i++) {
			if (i > 0) {
				sb.append(" and ");
			}
			AttributeFilter<?> filter = filters.get(i);
			if (filter instanceof AbstractFilter) {
				sb.append(((AbstractFilter<?>) filter).getMatchDescription());
			} else {
				sb.append(filter.getDescription());
			}
		}
		return sb.toString();
	}
}
