package org.xandercat.ofe.filter;

/**
 * Filter for Byte data type.
 * 
 * @author Scott Arnold
 */
public class ByteFilter extends AbstractNumericFilter<Byte> {

	public ByteFilter(NumericMatchStyle matchStyle, Byte number) {
		super(matchStyle, number);
	}

	@Override
	public Class<Byte> getFilteredClass() {
		return Byte.class;
	}
}
