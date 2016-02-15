package org.xandercat.ofe.filter;

/**
 * Filter for Float data type.
 * 
 * @author Scott Arnold
 */
public class FloatFilter extends AbstractNumericFilter<Float> {

	public FloatFilter(NumericMatchStyle matchStyle, Float number) {
		super(matchStyle, number);
	}

	@Override
	public Class<Float> getFilteredClass() {
		return Float.class;
	}
}
