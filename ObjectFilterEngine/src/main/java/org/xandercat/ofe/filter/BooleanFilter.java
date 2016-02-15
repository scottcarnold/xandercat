package org.xandercat.ofe.filter;

/**
 * AttributeFilter for Boolean objects.
 * 
 * @author Scott Arnold
 */
public class BooleanFilter extends AbstractFilter<Boolean> {

	private Boolean matchValue;
	
	public BooleanFilter(Boolean matchValue) {
		this.matchValue = matchValue;
	}
	
	@Override
	public Class<Boolean> getFilteredClass() {
		return Boolean.class;
	}

	@Override
	public boolean isMatch(Boolean item) {
		if (item == null || matchValue == null) {
			return (item == null && matchValue == null);
		}
		return item.booleanValue() == matchValue;
	}

	@Override
	public String getMatchDescription() {
		return "is " + ((matchValue == null)? "null" : matchValue);
	}
}
