package org.xandercat.ofe.filter;

/**
 * Factor class for creating filters.
 * 
 * @author Scott Arnold
 */
public class FilterFactory {

	@SuppressWarnings("unchecked")
	public static <T> AttributeFilter<T> getFilter(String fieldType, 
			String matchType, String matchValue, String... weights) {
		AbstractFilter<T> filter = null;
		if ("string".equalsIgnoreCase(fieldType)) {
			StringFilter.MatchStyle matchStyle = StringFilter.MatchStyle.valueOf(matchType);
			filter = (AbstractFilter<T>) new StringFilter(matchStyle, matchValue);
		} else if ("integer".equalsIgnoreCase(fieldType)) {
			IntegerFilter.MatchStyle matchStyle = IntegerFilter.MatchStyle.valueOf(matchType);
			filter = (AbstractFilter<T>) new IntegerFilter(matchStyle, Integer.parseInt(matchValue));
		} else if ("boolean".equalsIgnoreCase(fieldType)) {
			filter = (AbstractFilter<T>) new BooleanFilter(Boolean.parseBoolean(matchValue));
		} else {
			throw new IllegalArgumentException("Field type " + fieldType + " is not currently supported.");
		}
		if (weights != null) {
			for (String weight : weights) {
				if ("required".equalsIgnoreCase(weight)) {
					filter.setRequired(true);
				} else if ("excluded".equalsIgnoreCase(weight)) {
					filter.setExcluded(true);
				} else if ("HIGH".equalsIgnoreCase(weight)){
					filter.setWeight(FilterWeight.HIGH);
				} else if ("MEDIUM".equalsIgnoreCase(weight)) {
					filter.setWeight(FilterWeight.MEDIUM);
				} else if ("LOW".equalsIgnoreCase(weight)) {
					filter.setWeight(FilterWeight.LOW);
				} else if ("MAXIMUM".equalsIgnoreCase(weight)) {
					filter.setWeight(FilterWeight.MAXIMUM);
				} else if ("NONE".equalsIgnoreCase(weight)) {
					filter.setWeight(FilterWeight.NONE);
				} else if (weight != null && weight.length() > 0) {
					filter.setWeight(new FilterWeight(Float.parseFloat(weight)));
				}
			}
		}
		return filter;
	}
}
