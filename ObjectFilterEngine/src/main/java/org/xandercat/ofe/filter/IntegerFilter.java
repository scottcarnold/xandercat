package org.xandercat.ofe.filter;

/**
 * AttributeFilter for Integer objects.
 * 
 * @author Scott Arnold
 */
public class IntegerFilter extends AbstractFilter<Integer> {

	public static enum MatchStyle { GREATER_THAN, LESS_THAN, EQUALS }
	
	public static MatchStyle GREATER_THAN = MatchStyle.GREATER_THAN;
	public static MatchStyle LESS_THAN = MatchStyle.LESS_THAN;
	public static MatchStyle EQUALS = MatchStyle.EQUALS;
	
	private MatchStyle matchStyle;
	private int value;
	
	public IntegerFilter(MatchStyle matchStyle, int value) {
		this.matchStyle = matchStyle;
		this.value = value;
	}
	
	@Override
	public Class<Integer> getFilteredClass() {
		return Integer.class;
	}

	@Override
	public boolean isMatch(Integer item) {
		if (item == null) {
			return false;
		}
		if (matchStyle == GREATER_THAN) {
			return item.intValue() > value;
		} else if (matchStyle == LESS_THAN) {
			return item.intValue() < value;
		} else {
			return item.intValue() == value;
		}
	}

	@Override
	public String getMatchDescription() {
		return matchStyle.name() + " " + value;
	}

}
