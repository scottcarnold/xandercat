package org.xandercat.ofe.filter;

/**
 * AttributeFilter for String objects.
 * 
 * @author Scott Arnold
 */
public class StringFilter extends AbstractFilter<String> {

	private String matchString;
	private String matchStringLowerCase;
	private StringMatchStyle matchStyle;
	private boolean caseSensitive;
	
	public StringFilter(StringMatchStyle matchStyle, String matchString) {
		this.matchString = matchString;
		if (matchString != null) {
			this.matchStringLowerCase = matchString.toLowerCase();
		}
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
		if (item == null || matchString == null) {
			return (item == null && matchString == null);
		}
		String testString = (caseSensitive)? item : item.toLowerCase();
		String matchString = (caseSensitive)? this.matchString : this.matchStringLowerCase;
		if (matchStyle == StringMatchStyle.CONTAINS) {
			return testString.contains(matchString);
		} else if (matchStyle == StringMatchStyle.STARTS_WITH) {
			return testString.startsWith(matchString);
		} else if (matchStyle == StringMatchStyle.ENDS_WITH) {
			return testString.endsWith(matchString);
		} else {
			return testString.equals(matchString);
		}
	}

	@Override
	public String getMatchDescription() {
		return matchStyle.name() + ((matchString == null)? " null" : " \"" + matchString + "\"" + (caseSensitive? " (case sensitive)" : ""));
	}
}
