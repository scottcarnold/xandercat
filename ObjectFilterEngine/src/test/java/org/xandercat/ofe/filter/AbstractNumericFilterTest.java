package org.xandercat.ofe.filter;

import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractNumericFilterTest {

	@Test
	public void testIntegerNullHandling() {
		IntegerFilter integerFilter = new IntegerFilter(NumericMatchStyle.EQUALS, null);
		assertTrue(integerFilter.isMatch(null));
		assertFalse(integerFilter.isMatch(Integer.valueOf(2)));
		integerFilter = new IntegerFilter(NumericMatchStyle.EQUALS, Integer.valueOf(2));
		assertFalse(integerFilter.isMatch(null));
		assertFalse(integerFilter.isMatch(Integer.valueOf(0)));
		assertTrue(integerFilter.isMatch(Integer.valueOf(2)));
	}
	
	@Test
	public void testDoubleNullHandling() {
		DoubleFilter doubleFilter = new DoubleFilter(NumericMatchStyle.GREATER_THAN, Double.valueOf(2.5));
		assertFalse(doubleFilter.isMatch(null));
		assertTrue(doubleFilter.isMatch(Double.valueOf(3.5)));	
		// test nonsensical case -- less than null; it's allowed but should never match
		doubleFilter = new DoubleFilter(NumericMatchStyle.LESS_THAN, null);
		assertFalse(doubleFilter.isMatch(null));
		assertFalse(doubleFilter.isMatch(Double.valueOf(-10000)));
	}
	
	@Test
	public void testStringNullHandling() {
		StringFilter filter = new StringFilter(StringMatchStyle.EQUALS, null);
		assertTrue(filter.isMatch(null));
		assertFalse(filter.isMatch(""));
		// string filter is lenient with null, allowing any match style -- might revisit in future but for now verify as is
		filter = new StringFilter(StringMatchStyle.ENDS_WITH, null);
		assertTrue(filter.isMatch(null));
		assertFalse(filter.isMatch(""));
	}
}
