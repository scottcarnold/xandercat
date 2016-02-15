package org.xandercat.ofe.filter;

/**
 * Filter for Double data type.
 * 
 * @author Scott Arnold
 */
public class DoubleFilter extends AbstractNumericFilter<Double> {

	public DoubleFilter(NumericMatchStyle matchStyle, Double number) {
		super(matchStyle, number);
	}

	@Override
	public Class<Double> getFilteredClass() {
		return Double.class;
	}
}
