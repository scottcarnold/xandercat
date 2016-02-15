package org.xandercat.ofe.filter;

/**
 * Filter for Short data type.
 * 
 * @author Scott Arnold
 */
public class ShortFilter extends AbstractNumericFilter<Short> {

	public ShortFilter(NumericMatchStyle matchStyle, Short number) {
		super(matchStyle, number);
	}

	@Override
	public Class<Short> getFilteredClass() {
		return Short.class;
	}
}
