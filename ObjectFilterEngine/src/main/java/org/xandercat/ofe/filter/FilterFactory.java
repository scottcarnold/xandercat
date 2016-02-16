package org.xandercat.ofe.filter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Factor class for creating filters.
 * 
 * @author Scott Arnold
 */
public class FilterFactory {

	private static interface FilterBuilder<T> {
		public AbstractFilter<T> buildFilter(String matchType, String matchValue) throws Exception;
	}

	private static final Map<String, FilterBuilder<?>> FILTER_BUILDERS = new HashMap<String, FilterBuilder<?>>();
	
	static {
		FILTER_BUILDERS.put("string", new FilterBuilder<String>() {
			@Override
			public AbstractFilter<String> buildFilter(String matchType, String matchValue) {
				StringMatchStyle matchStyle = StringMatchStyle.valueOf(matchType);
				return new StringFilter(matchStyle, matchValue);
			}
		});
		FILTER_BUILDERS.put("integer", new FilterBuilder<Integer>() {
			@Override
			public AbstractFilter<Integer> buildFilter(String matchType, String matchValue) {
				NumericMatchStyle matchStyle = NumericMatchStyle.valueOf(matchType);
				Integer i = (matchValue == null || matchValue.trim().length() == 0)? null : Integer.valueOf(matchValue);
				return new IntegerFilter(matchStyle, i);
			}
		});
		FILTER_BUILDERS.put("boolean", new FilterBuilder<Boolean>() {
			@Override
			public AbstractFilter<Boolean> buildFilter(String matchType, String matchValue) {
				Boolean b = (matchValue == null || matchValue.trim().length() == 0)? null : Boolean.valueOf(matchValue);
				return new BooleanFilter(b);
			}			
		});
		FILTER_BUILDERS.put("long", new FilterBuilder<Long>() {
			@Override
			public AbstractFilter<Long> buildFilter(String matchType, String matchValue) {
				NumericMatchStyle matchStyle = NumericMatchStyle.valueOf(matchType);
				Long l = (matchValue == null || matchValue.trim().length() == 0)? null : Long.valueOf(matchValue);
				return new LongFilter(matchStyle, l);
			}
		});
		FILTER_BUILDERS.put("float", new FilterBuilder<Float>() {
			@Override
			public AbstractFilter<Float> buildFilter(String matchType, String matchValue) {
				NumericMatchStyle matchStyle = NumericMatchStyle.valueOf(matchType);
				Float f = (matchValue == null || matchValue.trim().length() == 0)? null : Float.valueOf(matchValue);
				return new FloatFilter(matchStyle, f);
			}
		});
		FILTER_BUILDERS.put("double", new FilterBuilder<Double>() {
			@Override
			public AbstractFilter<Double> buildFilter(String matchType, String matchValue) {
				NumericMatchStyle matchStyle = NumericMatchStyle.valueOf(matchType);
				Double d = (matchValue == null || matchValue.trim().length() == 0)? null : Double.valueOf(matchValue);
				return new DoubleFilter(matchStyle, d);
			}
		});
		FILTER_BUILDERS.put("short", new FilterBuilder<Short>() {
			@Override
			public AbstractFilter<Short> buildFilter(String matchType, String matchValue) {
				NumericMatchStyle matchStyle = NumericMatchStyle.valueOf(matchType);
				Short s = (matchValue == null || matchValue.trim().length() == 0)? null : Short.valueOf(matchValue);
				return new ShortFilter(matchStyle, s);
			}
		});
		FILTER_BUILDERS.put("byte", new FilterBuilder<Byte>() {
			@Override
			public AbstractFilter<Byte> buildFilter(String matchType, String matchValue) {
				NumericMatchStyle matchStyle = NumericMatchStyle.valueOf(matchType);
				Byte b = (matchValue == null || matchValue.trim().length() == 0)? null : Byte.valueOf(matchValue);
				return new ByteFilter(matchStyle, b);
			}
		});
		FILTER_BUILDERS.put("date", new FilterBuilder<Date>() {
			@Override
			public AbstractFilter<Date> buildFilter(String matchType, String matchValue) throws Exception {
				int i = matchValue.indexOf("(") + 1;
				int j = matchValue.indexOf(")");
				DateFormat dateFormat = null;
				if (i > 0 && j > i) {
					String dateFormatString = matchValue.substring(i, j);
					matchValue = matchValue.substring(0, i-1).trim();
					dateFormat = new SimpleDateFormat(dateFormatString);
				} else {
					// would just call DateFormat.getDateInstance() here, but no one would ever really use
					// that format, so going to be biased and just go with typical United States format using slashes.
					dateFormat = new SimpleDateFormat("MM/dd/yyyy");
				}
				DateMatchStyle matchStyle = DateMatchStyle.valueOf(matchType);
				Date d = (matchValue == null || matchValue.trim().length() == 0)? null : dateFormat.parse(matchValue);
				return new DateFilter(matchStyle, d);
			}
		});
	}
	
	/**
	 * Returns a filter based on the provided attributes.  Support for data types String, Integer, Boolean,
	 * Long, Float, Double, Short, Byte, and Date.
	 * 
	 * A custom date format can be provided for the Date fieldType by providing the date format immediately after
	 * "Date" wrapped in parenthesis.  For example, if you wanted to provide a date in format "YYYY-mm-dd", the 
	 * field type passed should be "Date(YYYY-mm-dd)".  If no date format is provided, the expected format is MM/dd/yyyy.
	 * 
	 * @param fieldType    the field type (e.g. "String", "Integer", etc.)
	 * @param matchType    the match type (e.g., for String, might be "CONTAINS", "EQUALS", etc)
	 * @param matchValue   the base value to match against in String format
	 * @param attributes   can be "required", "excluded", and/or a weight; weight can be keyword or number from 0 to 1.
	 * @param <T>          class type filter will filter upon
	 * 
	 * @return             attribute filter based on the provided filter attributes
	 * 
	 * @throws Exception   if filter cannot be created
	 */
	@SuppressWarnings("unchecked")
	public static <T> AttributeFilter<T> getFilter(String fieldType, 
			String matchType, String matchValue, String... attributes) throws Exception {
		FilterBuilder<T> filterBuilder = (FilterBuilder<T>) FILTER_BUILDERS.get(fieldType.toLowerCase());
		if (filterBuilder == null) {
			if (fieldType.toLowerCase().startsWith("date")) {  // check for custom date format
				filterBuilder = (FilterBuilder<T>) FILTER_BUILDERS.get("date");
				matchValue = matchValue + fieldType.substring(4);  // append date format to match value; date builder will extract it
			} else {
				throw new IllegalArgumentException("Field type " + fieldType + " is not currently supported.");
			}
		}
		AbstractFilter<T> filter = filterBuilder.buildFilter(matchType, matchValue); 
		if (attributes != null) {
			for (String attribute : attributes) {
				if ("required".equalsIgnoreCase(attribute)) {
					filter.setRequired(true);
				} else if ("excluded".equalsIgnoreCase(attribute)) {
					filter.setExcluded(true);
				} else if ("HIGH".equalsIgnoreCase(attribute)){
					filter.setWeight(FilterWeight.HIGH);
				} else if ("MEDIUM".equalsIgnoreCase(attribute)) {
					filter.setWeight(FilterWeight.MEDIUM);
				} else if ("LOW".equalsIgnoreCase(attribute)) {
					filter.setWeight(FilterWeight.LOW);
				} else if ("MAXIMUM".equalsIgnoreCase(attribute)) {
					filter.setWeight(FilterWeight.MAXIMUM);
				} else if ("NONE".equalsIgnoreCase(attribute)) {
					filter.setWeight(FilterWeight.NONE);
				} else if (attribute != null && attribute.length() > 0) {
					filter.setWeight(new FilterWeight(Float.parseFloat(attribute)));
				}
			}
		}
		return filter;
	}
}
