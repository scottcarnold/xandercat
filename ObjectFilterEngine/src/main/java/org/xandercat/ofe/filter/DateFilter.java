package org.xandercat.ofe.filter;

import java.util.Date;

/**
 * Filter for Date fields.
 * 
 * @author Scott Arnold
 */
public class DateFilter extends AbstractFilter<Date> {

	private DateMatchStyle matchStyle;
	private Date matchDate;
	
	public DateFilter(DateMatchStyle matchStyle, Date matchDate) {
		this.matchStyle = matchStyle;
		this.matchDate = matchDate;
	}
	
	@Override
	public Class<Date> getFilteredClass() {
		return Date.class;
	}

	@Override
	public boolean isMatch(Date item) {
		if (matchDate == null || item == null) {
			return (matchDate == null && item == null);
		} else if (matchStyle == DateMatchStyle.BEFORE) {
			return item.before(matchDate);
		} else if (matchStyle == DateMatchStyle.AFTER) {
			return item.after(matchDate);
		} else {
			return item.equals(matchDate);
		}
	}

	@Override
	public String getMatchDescription() {
		return matchStyle.name() + " " + matchDate.toString();
	}

}
