package org.xandercat.ofe.filter;

import java.text.NumberFormat;

/**
 * Abstract filter class for number types.
 * 
 * @author Scott Arnold
 *
 * @param <T>
 */
public abstract class AbstractNumericFilter<T extends Number> extends AbstractFilter<T> {

	private NumericMatchStyle matchStyle;
	private Number number;
	
	public AbstractNumericFilter(NumericMatchStyle matchStyle, Number number) {
		this.matchStyle = matchStyle;
		this.number = number;
	}

	@Override
	public boolean isMatch(T item) {
		return matchStyle == NumericMatchStyle.getMatchStyle(item, number);
	}

	@Override
	public String getMatchDescription() {
		return matchStyle.name() + " " + ((number == null)? "null" : NumberFormat.getNumberInstance().format(number));
	}
}
