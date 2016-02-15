package org.xandercat.ofe.filter;

/**
 * Filter for Long data type.
 * 
 * @author Scott Arnold
 */
public class LongFilter extends AbstractNumericFilter<Long> {

	public LongFilter(NumericMatchStyle matchStyle, Long number) {
		super(matchStyle, number);
	}

	@Override
	public Class<Long> getFilteredClass() {
		return Long.class;
	}
}
