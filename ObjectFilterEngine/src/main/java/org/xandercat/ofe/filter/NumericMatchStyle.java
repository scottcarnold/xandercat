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
	GREATER_THAN, LESS_THAN, EQUALS;
	
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
	
	public static NumericMatchStyle getMatchStyle(int testValue, int baseValue) {
		if (testValue > baseValue) {
			return GREATER_THAN;
		} else if (testValue < baseValue) {
			return LESS_THAN;
		} else {
			return EQUALS;
		}
	}
	
	public static NumericMatchStyle getMatchStyle(long testValue, long baseValue) {
		if (testValue > baseValue) {
			return GREATER_THAN;
		} else if (testValue < baseValue) {
			return LESS_THAN;
		} else {
			return EQUALS;
		}
	}

	public static NumericMatchStyle getMatchStyle(short testValue, short baseValue) {
		if (testValue > baseValue) {
			return GREATER_THAN;
		} else if (testValue < baseValue) {
			return LESS_THAN;
		} else {
			return EQUALS;
		}
	}
	
	public static NumericMatchStyle getMatchStyle(byte testValue, byte baseValue) {
		if (testValue > baseValue) {
			return GREATER_THAN;
		} else if (testValue < baseValue) {
			return LESS_THAN;
		} else {
			return EQUALS;
		}
	}
	
	public static NumericMatchStyle getMatchStyle(float testValue, float baseValue) {
		if (testValue > baseValue) {
			return GREATER_THAN;
		} else if (testValue < baseValue) {
			return LESS_THAN;
		} else {
			return EQUALS;
		}
	}
	
	public static NumericMatchStyle getMatchStyle(double testValue, double baseValue) {
		if (testValue > baseValue) {
			return GREATER_THAN;
		} else if (testValue < baseValue) {
			return LESS_THAN;
		} else {
			return EQUALS;
		}
	}
}
