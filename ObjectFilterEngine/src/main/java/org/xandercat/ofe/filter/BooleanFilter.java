package org.xandercat.ofe.filter;

/**
 * AttributeFilter for Boolean objects.
 * 
 * @author Scott Arnold
 */
public class BooleanFilter extends AbstractFilter<Boolean> {

	private boolean matchValue;
	
	public BooleanFilter(boolean matchValue) {
		this.matchValue = matchValue;
	}
	
	@Override
	public Class<Boolean> getFilteredClass() {
		return Boolean.class;
	}

	@Override
	public boolean isMatch(Boolean item) {
		return item != null && item.booleanValue() == matchValue;
	}

	@Override
	public String getMatchDescription() {
		return "is " + matchValue;
	}
}
