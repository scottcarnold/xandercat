package org.xandercat.ofe.searchutility;

import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.xandercat.ofe.ReflectionUtil;

/**
 * Formatter class for converting between objects and CSV strings.  CSV strings parsed or formatted by this
 * formatter conform to the basic escape conventions used by Microsoft Excel.  
 * 
 * When the object type is Map, the object (T) will be treated as a LinkedHashMap with String keys and String values.
 * 
 * Each field within the object class will be parsed or formatted by a ValueFormatter.  Default ValueFormatters
 * are provided for some common types including String, Integer, Long, Float, Double, and Boolean.  Custom 
 * ValueFormatters can be added for any types required.
 * 
 * When formatting a value for which a ValueFormatter hasn't been defined, the objects toString() method will be used.
 * 
 * When parsing a value for which a ValueFormatter hasn't been defined, an Exception is thrown.
 * 
 * When formatting or parsing, nested field names are allowed.  When parsing with nested field names, any intermediate
 * objects that are null will be constructed using default constructors. 
 * 
 * @author Scott Arnold
 *
 * @param <T> the type to be parsed into or formatted out from; must have default constructor and standard getter and setter methods.
 */
public class CSVFormatter<T> {

	public static interface ValueFormatter<S> {
		
		public S parse(String value) throws Exception;
		
		public String format(S value);
	}
	
	private Class<T> clazz;
	private Class<?>[] fieldClasses;
	private String[] fields;
	private Set<String> forceAsStringFields;
	private Map<Class<?>, ValueFormatter<?>> valueFormatters = new HashMap<Class<?>, ValueFormatter<?>>();
	private boolean failOnNotEnoughValues;
	private boolean failOnTooManyValues;
	
	/**
	 * Returns a generic formatter that will use a Map with String keys and String values.
	 * 
	 * @return    generic formatter using a Map with String keys and String values.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static CSVFormatter<Map<String, String>> genericFormatter() {
		return new CSVFormatter(Map.class);
	}
	
	/**
	 * Create a CSVFormatter for the given class type.
	 * 
	 * @param clazz   the class type to be formatted as csv or parsed into from csv
	 */
	public CSVFormatter(Class<T> clazz) {
		this.clazz = clazz;
		// add a few default value formatters for common types
		valueFormatters.put(String.class, new ValueFormatter<String>() {
			public String parse(String value) {
				return value;
			}
			public String format(String value) {
				return value;
			}
		});
		valueFormatters.put(Integer.class, new ValueFormatter<Integer>() {
			public Integer parse(String value) {
				return (value == null || value.length() == 0)? null : Integer.valueOf(value);
			}
			public String format(Integer value) {
				return (value == null)? "" : value.toString();
			}
		});
		valueFormatters.put(Boolean.class, new ValueFormatter<Boolean>() {
			public Boolean parse(String value) {
				return Boolean.valueOf(value);
			}
			public String format(Boolean value) {
				return (value == null)? "" : value.toString();
			}
		});
		valueFormatters.put(Long.class, new ValueFormatter<Long>() {
			public Long parse(String value) {
				return (value == null || value.length() == 0)? null : Long.valueOf(value);
			}
			public String format(Long value) {
				return (value == null)? "" : value.toString();
			}
		});
		valueFormatters.put(Float.class, new ValueFormatter<Float>() {
			public Float parse(String value) {
				return (value == null || value.length() == 0)? null : Float.valueOf(value);
			}
			public String format(Float value) {
				return NumberFormat.getNumberInstance().format(value);
			}
		});
		valueFormatters.put(Double.class, new ValueFormatter<Double>() {
			public Double parse(String value) {
				return (value == null || value.length() == 0)? null : Double.valueOf(value);
			}
			public String format(Double value) {
				return NumberFormat.getNumberInstance().format(value);
			}
		});
		valueFormatters.put(Boolean.TYPE, new ValueFormatter<Boolean>() {
			public Boolean parse(String value) {
				return Boolean.valueOf(value);
			}
			public String format(Boolean value) {
				return (value == null)? "" : value.toString();
			}
		});
		valueFormatters.put(Integer.TYPE, new ValueFormatter<Integer>() {
			public Integer parse(String value) {
				return Integer.valueOf(value);
			}
			public String format(Integer value) {
				return (value == null)? "" : value.toString();
			}
		});
		valueFormatters.put(Long.TYPE, new ValueFormatter<Long>() {
			public Long parse(String value) {
				return Long.valueOf(value);
			}
			public String format(Long value) {
				return (value == null)? "" : value.toString();
			}
		});
		valueFormatters.put(Float.TYPE, new ValueFormatter<Float>() {
			public Float parse(String value) {
				return Float.valueOf(value);
			}
			public String format(Float value) {
				return NumberFormat.getNumberInstance().format(value);
			}
		});
		valueFormatters.put(Double.TYPE, new ValueFormatter<Double>() {
			public Double parse(String value) {
				return Double.valueOf(value);
			}
			public String format(Double value) {
				return NumberFormat.getNumberInstance().format(value);
			}
		});
	}
	
	public boolean isFailOnNotEnoughValues() {
		return failOnNotEnoughValues;
	}

	public void setFailOnNotEnoughValues(boolean failOnNotEnoughValues) {
		this.failOnNotEnoughValues = failOnNotEnoughValues;
	}

	public boolean isFailOnTooManyValues() {
		return failOnTooManyValues;
	}

	public void setFailOnTooManyValues(boolean failOnTooManyValues) {
		this.failOnTooManyValues = failOnTooManyValues;
	}

	/**
	 * Add a value formatter for parsing and formatting individual values of the given class type.
	 * 
	 * @param clazz             class type
	 * @param valueFormatter    formatter for class type
	 * @param <S>               class type
	 */
	public <S> void addValueFormatter(Class<S> clazz, ValueFormatter<S> valueFormatter) {
		valueFormatters.put(clazz, valueFormatter);
	}
	
	/**
	 * Set the field names to parse or format.  Field types will be auto-discovered by their getter methods.
	 * 
	 * @param fields    the fields to parse or format
	 * 
	 * @throws Exception if getter method for any field is not available
	 */
	public void setFields(String[] fields) throws Exception {
		Class<?>[] classes = new Class<?>[fields.length];
		if (clazz == Map.class || Map.class.isAssignableFrom(clazz)) {
			for (int i=0; i<fields.length; i++) {
				classes[i] = String.class;
			}
		} else {
			for (int i=0; i<fields.length; i++) {
				String field = fields[i];
				Method method = ReflectionUtil.getterMethod(field, clazz, true);
				classes[i] = method.getReturnType();
			}
		}
		setFields(fields, classes);
	}
	
	/**
	 * Set the field names and types to parse or format.  Fields and field classes array must be of the same length.
	 * 
	 * @param fields          the fields to parse or format
	 * @param fieldClasses    the classses of the fields to parse or format
	 */
	public void setFields(String[] fields, Class<?>[] fieldClasses) {
		if (fields.length != fieldClasses.length) {
			throw new IllegalArgumentException("There must be the same number of field classes as their are fields.");
		}
		this.fields = fields;
		this.fieldClasses = fieldClasses;
	}
	
	/**
	 * Sets fields for which you want the formatted output to be wrapped in such a way that Microsoft Excel will 
	 * treat the fields as text regardless of their content.
	 * 
	 * @param fields   set of fields to wrap so that Microsoft Excel will treat the fields as strings regardless of their content
	 */
	public void setForceAsStringFields(Set<String> fields) {
		this.forceAsStringFields = fields;
	}
	
	@SuppressWarnings("unchecked")
	private <S> String formatValue(Object value, Class<S> valueType) {
		ValueFormatter<S> valueFormatter = (ValueFormatter<S>) valueFormatters.get(valueType);
		if (valueFormatter == null) {
			return (value == null)? "" : value.toString();
		} else {
			return valueFormatter.format((S) value);
		}
	}
	
	/**
	 * Format the given object as a CSV string.
	 * 
	 * @param object    the object to format as a CSV string
	 * 
	 * @return          CSV string representing the object (for defined fields only)
	 * 
	 * @throws Exception if object cannot be formated
	 */
	public String format(T object) throws Exception {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<fields.length; i++) {
			if (i > 0) {
				sb.append(",");
			}
			String field = fields[i];
			Object value = getValue(object, field);
			String stringValue = formatValue(value, fieldClasses[i]);
			if (forceAsStringFields != null && forceAsStringFields.contains(field)) {
				stringValue = "=\"" + stringValue + "\"";
			}
			if (stringValue.indexOf(',') >= 0 || stringValue.indexOf('"') >= 0) {
				// following Excel convention, wrap values containing commas with double quotes
				// e.g. value of [Doe, John] becomes ["Doe, John"]
				if (stringValue.indexOf('"') >= 0) {
					// following Excel convention, encode double quotes within double quotes wrapped values with an additional with an additional double quotes
					// e.g. value [Provolone, Angelo "Snaps"] becomes ["Provolone, Angelo ""Snaps"""]
					sb.append('"');
					for (int j=0; j<stringValue.length(); j++) {
						sb.append(stringValue.charAt(j));
						if (stringValue.charAt(j) == '"') {
							sb.append('"');
						}
					}
					sb.append('"');
				} else {
					sb.append('"').append(stringValue).append('"');
				}
			} else {
				sb.append(stringValue);
			}
		}
		return sb.toString();
	}
	
	private String trimTrailingSpaces(StringBuilder sb, int requiredLength) {
		int j = sb.length();
		while (j > 0 && sb.charAt(j-1) == ' ' && j > requiredLength) {
			j--;
		}	
		return (j < sb.length())? sb.substring(0, j) : sb.toString();
	}
	
	/**
	 * Parse the given CSV String into an object.
	 * 
	 * @param csvString    the CSV string to parse
	 * 
	 * @return             object built from the CSV string
	 * 
	 * @throws Exception   if any parsing fails
	 */
	public T parse(String csvString) throws Exception {
		@SuppressWarnings("unchecked")
		T instance = (clazz == Map.class)? (T) new LinkedHashMap<String, String>() : clazz.newInstance();
		List<Object> values = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder();
		boolean inQuotes = false;
		int fieldIndex = 0;
		int requiredLength = 0;
		for (int i=0; i<csvString.length(); i++) {
			if (!inQuotes && csvString.charAt(i) == ',') {
				String value = trimTrailingSpaces(sb, requiredLength);
				values.add(parseValue(value, fieldIndex, i));
				fieldIndex++;
				sb.setLength(0);	
				requiredLength = 0;
			} else {
				if (inQuotes) {
					if (csvString.charAt(i) == '"') {
						if (((i+1) < csvString.length()) && (csvString.charAt(i+1) == '"')) {
							// double quotes found while already in quotes; treat as literal double quote being part of string
							sb.append('"');
							i++;
						} else {
							inQuotes = false;
							requiredLength = sb.length(); 
						}
					} else {
						sb.append(csvString.charAt(i));
					}
				} else {
					if (csvString.charAt(i) == '"') {
						inQuotes = true;
					} else {
						if (sb.length() > 0 || csvString.charAt(i) != ' ') { // drop leading spaces not contained within quotes
							sb.append(csvString.charAt(i));
						}
					}
				}
			}
		}
		if (sb.length() > 0) {
			String value = trimTrailingSpaces(sb, requiredLength);
			values.add(parseValue(value, fieldIndex, csvString.length()));
		}
		if (failOnNotEnoughValues && (values.size() < fields.length)) {
			throw new ParseException("Required at least " + fields.length + " values, but found only " + values.size(), csvString.length());
		} else if (failOnTooManyValues && (values.size() > fields.length)) {
			throw new ParseException("Required no more than " + fields.length + " values, but found " + values.size(), csvString.length());
		}
		for (int i=0; i<values.size() && i<fields.length; i++) {
			setValue(instance, fields[i], values.get(i), fieldClasses[i]);
		}
		return instance;
	}
	
	private Object parseValue(String value, int fieldIndex, int csvStringIndex) throws ParseException {
		if (fieldIndex >= fieldClasses.length) {
			return value; // more values than are listed to parse; just return as String
		}
		ValueFormatter<?> valueFormatter = valueFormatters.get(fieldClasses[fieldIndex]);
		if (valueFormatter == null) {
			throw new ParseException("ValueFormatter for class type " + fieldClasses[fieldIndex].getName() + " does not exist.", csvStringIndex);
		}
		try {
			return valueFormatter.parse(value);
		} catch (Exception e) {
			throw new ParseException("ValueFormatter for class type " + fieldClasses[fieldIndex].getName() + " cannot parse value \"" + value + "\"", csvStringIndex);
		}
	}
	
	@SuppressWarnings("rawtypes")
	private Object getValue(Object object, String field) throws Exception {
		if (object instanceof Map) {
			return ((Map) object).get(field);
		} else {
			Method method = ReflectionUtil.getterMethod(field, object.getClass(), true);
			try {
				Object invocationTarget = ReflectionUtil.getInvocationTarget(field, object, false);
				return method.invoke(invocationTarget, (Object[]) null);
			} catch (NullPointerException npe) {
				// happens if intermediate object in chain is null; acceptable to return null in this case
				return null;
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setValue(Object object, String field, Object value, Class<?> valueClass) throws Exception {
		if (object instanceof Map) {
			((Map) object).put(field, value);
		} else {
			Method method = ReflectionUtil.setterMethod(field, valueClass, object.getClass(), true);
			Object invocationTarget = ReflectionUtil.getInvocationTarget(field, object, true);
			method.invoke(invocationTarget, value);
		}
	}
	
	public CSVFormatter<T> fields(String... fields) throws Exception {
		setFields(fields);
		return this;
	}
}
