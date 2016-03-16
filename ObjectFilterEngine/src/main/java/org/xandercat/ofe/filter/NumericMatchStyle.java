package org.xandercat.ofe.filter;

/**
 * Enum to represent match style for all numeric types.
 * 
 * Includes utility methods for getting the match style that would be true for 
 * given test and base values.
 * 
 * @author Scott Arnold
 */
public enum NumericMatchStyle {
	GREATER_THAN, LESS_THAN, EQUALS, GREATER_THAN_OR_EQUALS, LESS_THAN_OR_EQUALS;
	
	/**
	 * Return the most appropriate match style for the test value given the base value.
	 * This will always return either GREATER_THAN, LESS_THAN, or EQUALS.
	 * 
	 * @param testValue    the value to be tested
	 * @param baseValue    the value to compare the test value against
	 * 
	 * @return    the most appropriate match style for the test value given the base value.
	 */
	public static NumericMatchStyle getMatchStyle(Number testValue, Number baseValue) {
		if (testValue == null || baseValue == null) {
			return (testValue == null && baseValue == null)? EQUALS : null;
		} else {
			if ((testValue instanceof Long || testValue instanceof Integer || testValue instanceof Short || testValue instanceof Byte) 
					&& (baseValue instanceof Long || baseValue instanceof Integer || baseValue instanceof Short || baseValue instanceof Byte)) {
				return getMatchStyle(testValue.longValue(), baseValue.longValue());
			} else {
				return getMatchStyle(testValue.doubleValue(), baseValue.doubleValue());
			}
		}
	}
	
	/**
	 * Return the most appropriate match style for the test value given the base value.
	 * This will always return either GREATER_THAN, LESS_THAN, or EQUALS.
	 * 
	 * @param testValue    the value to be tested
	 * @param baseValue    the value to compare the test value against
	 * 
	 * @return    the most appropriate match style for the test value given the base value.
	 */
	public static NumericMatchStyle getMatchStyle(int testValue, int baseValue) {
		if (testValue > baseValue) {
			return GREATER_THAN;
		} else if (testValue < baseValue) {
			return LESS_THAN;
		} else {
			return EQUALS;
		}
	}
	
	/**
	 * Return the most appropriate match style for the test value given the base value.
	 * This will always return either GREATER_THAN, LESS_THAN, or EQUALS.
	 * 
	 * @param testValue    the value to be tested
	 * @param baseValue    the value to compare the test value against
	 * 
	 * @return    the most appropriate match style for the test value given the base value.
	 */
	public static NumericMatchStyle getMatchStyle(long testValue, long baseValue) {
		if (testValue > baseValue) {
			return GREATER_THAN;
		} else if (testValue < baseValue) {
			return LESS_THAN;
		} else {
			return EQUALS;
		}
	}

	/**
	 * Return the most appropriate match style for the test value given the base value.
	 * This will always return either GREATER_THAN, LESS_THAN, or EQUALS.
	 * 
	 * @param testValue    the value to be tested
	 * @param baseValue    the value to compare the test value against
	 * 
	 * @return    the most appropriate match style for the test value given the base value.
	 */
	public static NumericMatchStyle getMatchStyle(short testValue, short baseValue) {
		if (testValue > baseValue) {
			return GREATER_THAN;
		} else if (testValue < baseValue) {
			return LESS_THAN;
		} else {
			return EQUALS;
		}
	}
	
	/**
	 * Return the most appropriate match style for the test value given the base value.
	 * This will always return either GREATER_THAN, LESS_THAN, or EQUALS.
	 * 
	 * @param testValue    the value to be tested
	 * @param baseValue    the value to compare the test value against
	 * 
	 * @return    the most appropriate match style for the test value given the base value.
	 */
	public static NumericMatchStyle getMatchStyle(byte testValue, byte baseValue) {
		if (testValue > baseValue) {
			return GREATER_THAN;
		} else if (testValue < baseValue) {
			return LESS_THAN;
		} else {
			return EQUALS;
		}
	}
	
	/**
	 * Return the most appropriate match style for the test value given the base value.
	 * This will always return either GREATER_THAN, LESS_THAN, or EQUALS.
	 * 
	 * @param testValue    the value to be tested
	 * @param baseValue    the value to compare the test value against
	 * 
	 * @return    the most appropriate match style for the test value given the base value.
	 */
	public static NumericMatchStyle getMatchStyle(float testValue, float baseValue) {
		if (testValue > baseValue) {
			return GREATER_THAN;
		} else if (testValue < baseValue) {
			return LESS_THAN;
		} else {
			return EQUALS;
		}
	}
	
	/**
	 * Return the most appropriate match style for the test value given the base value.
	 * This will always return either GREATER_THAN, LESS_THAN, or EQUALS.
	 * 
	 * @param testValue    the value to be tested
	 * @param baseValue    the value to compare the test value against
	 * 
	 * @return    the most appropriate match style for the test value given the base value.
	 */
	public static NumericMatchStyle getMatchStyle(double testValue, double baseValue) {
		if (testValue > baseValue) {
			return GREATER_THAN;
		} else if (testValue < baseValue) {
			return LESS_THAN;
		} else {
			return EQUALS;
		}
	}
	
	/**
	 * Returns whether or not the base match style provided matches for the given test and base values.
	 * 
	 * @param testValue    the value to be tested
	 * @param baseValue    the value to test against
	 * @param baseMatchStyle    the match style to compare against
	 * 
	 * @return    whether or not the base match style provided matches for the given test and base values.
	 */
	public static boolean matches(Number testValue, Number baseValue, NumericMatchStyle baseMatchStyle) {
		NumericMatchStyle testMatchStyle = getMatchStyle(testValue, baseValue);
		if (baseMatchStyle == GREATER_THAN_OR_EQUALS) {
			return (testMatchStyle == GREATER_THAN || testMatchStyle == EQUALS);
		} else if (baseMatchStyle == LESS_THAN_OR_EQUALS) {
			return (testMatchStyle == LESS_THAN || testMatchStyle == EQUALS);
		} else {
			return baseMatchStyle == testMatchStyle;
		}
	}
}
