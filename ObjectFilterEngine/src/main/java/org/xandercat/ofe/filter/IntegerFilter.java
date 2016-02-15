package org.xandercat.ofe.filter;

/**
 * AttributeFilter for Integer objects.
 * 
 * @author Scott Arnold
 */
public class IntegerFilter extends AbstractNumericFilter<Integer> {

	public IntegerFilter(NumericMatchStyle matchStyle, Integer number) {
		super(matchStyle, number);
	}

	@Override
	public Class<Integer> getFilteredClass() {
		return Integer.class;
	}
}
