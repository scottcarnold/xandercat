package org.xandercat.ofe.filter;

/**
 * Interface to be implemented by any filter that will be used with the ObjectFilterEngine.
 * 
 * @author Scott Arnold
 *
 * @param <T>   the type of object being filtered against
 */
public interface AttributeFilter<T> {

	/**
	 * Returns the class type the filter runs against.
	 * 
	 * @return    class type the filter runs against
	 */
	public Class<T> getFilteredClass();
	
	/**
	 * Returns whether or not an object is required to match this filter.
	 *
	 * @return whether or not an object is required to match this filter
	 */
	public boolean isRequired();
	
	/**
	 * Returns whether or not an object matching this filter should be excluded.
	 * 
	 * @return whether or not an object matching this filter should be excluded
	 */
	public boolean isExcluded();
	
	/**
	 * Returns whether or not the given item matches this filter.
	 * 
	 * @param item   the object being checked
	 * 
	 * @return whether or not the given item matches this filter
	 */
	public boolean isMatch(T item);
	
	/**
	 * Returns the weighting this filter should have (not applicable if excluded).
	 * 
	 * @return the weighting this filter should have
	 */
	public FilterWeight getWeight();
	
	/**
	 * Returns a description of what this filter is.
	 * 
	 * @return description of the filter
	 */
	public String getDescription();
}
