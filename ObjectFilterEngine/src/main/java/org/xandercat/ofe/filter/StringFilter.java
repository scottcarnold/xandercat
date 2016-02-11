package org.xandercat.ofe.filter;

/**
 * AttributeFilter for String objects.
 * 
 * @author Scott Arnold
 */
public class StringFilter extends AbstractFilter<String> {

	public static enum MatchStyle { CONTAINS, STARTS_WITH, ENDS_WITH, EQUALS }
	
	public static final MatchStyle CONTAINS = MatchStyle.CONTAINS;
	public static final MatchStyle STARTS_WITH = MatchStyle.STARTS_WITH;
	public static final MatchStyle ENDS_WITH = MatchStyle.ENDS_WITH;
	public static final MatchStyle EQUALS = MatchStyle.EQUALS;
	
	private String matchString;
	private String matchStringLowerCase;
	private MatchStyle matchStyle;
	private boolean caseSensitive;
	
	public StringFilter(MatchStyle matchStyle, String matchString) {
		this.matchString = matchString;
		this.matchStringLowerCase = matchString.toLowerCase();
		this.matchStyle = matchStyle;
	}
	
	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	@Override
	public Class<String> getFilteredClass() {
		return String.class;
	}

	@Override
	public boolean isMatch(String item) {
		if (item == null) {
			return false;
		}
		String testString = (caseSensitive)? item : item.toLowerCase();
		String matchString = (caseSensitive)? this.matchString : this.matchStringLowerCase;
		if (matchStyle == MatchStyle.CONTAINS) {
			return testString.contains(matchString);
		} else if (matchStyle == MatchStyle.STARTS_WITH) {
			return testString.startsWith(matchString);
		} else if (matchStyle == MatchStyle.ENDS_WITH) {
			return testString.endsWith(matchString);
		} else {
			return testString.equals(matchString);
		}
	}

	@Override
	public String getMatchDescription() {
		return matchStyle.name() + " \"" + matchString + "\"" + (caseSensitive? " (case sensitive)" : "");
	}
}
