package org.xandercat.ofe;

import java.lang.reflect.Method;

/**
 * Utility methods for reflection.
 * 
 * @author Scott Arnold
 */
public class ReflectionUtil {

	/**
	 * Returns getter method for the given fieldName from the given clazz.  As the field type is 
	 * unknown, a prefix of either "get" or "is" in the getter method name will be accepted.
	 * 
	 * @param fieldName    name of field to get getter method for
	 * @param clazz        class to get getter method from
	 * 
	 * @return  getter method
	 * 
	 * @throws NoSuchMethodException
	 */
	public static Method getterMethod(String fieldName, Class<?> clazz) throws NoSuchMethodException {
		return getterMethod(fieldName, null, clazz);
	}
	
	/**
	 * Returns getter method for the given fieldName of given fieldType from the given clazz.
	 *  
	 * @param fieldName    name of field to get getter method for
	 * @param fieldType    class type of the field
	 * @param clazz        class to get getter method from
	 * 
	 * @return  getter method
	 * 
	 * @throws NoSuchMethodException
	 */
	public static Method getterMethod(String fieldName, Class<?> fieldType, Class<?> clazz) throws NoSuchMethodException {
		Method method = null;
		String getterNamePostfix = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		if (fieldType == null) {
			try {
				method = clazz.getMethod("get" + getterNamePostfix, (Class<?>[]) null);
			} catch (NoSuchMethodException e) {
				method = clazz.getMethod("is" + getterNamePostfix, (Class<?>[]) null);
			}
		} else {
			if (fieldType == Boolean.TYPE) {
				method = clazz.getMethod("is" + getterNamePostfix, (Class<?>[]) null);
			} else {
				method = clazz.getMethod("get" + getterNamePostfix, (Class<?>[]) null);
			}
		}
		return method;
	}
	
	/**
	 * Returns setter method for the given fieldName of given fieldType from the given clazz.
	 * 
	 * @param fieldName    name of field to get setter method for
	 * @param fieldType    class type of the field
	 * @param clazz        class to get setter method from
	 * @return
	 * @throws NoSuchMethodException
	 */
	public static Method setterMethod(String fieldName, Class<?> fieldType, Class<?> clazz) throws NoSuchMethodException {
		String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		return clazz.getMethod(setterName, fieldType);
	}
}
