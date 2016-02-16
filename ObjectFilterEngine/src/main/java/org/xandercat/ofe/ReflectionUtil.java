package org.xandercat.ofe;

import java.lang.reflect.InvocationTargetException;
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
	 * Supports getting method for nested objects (e.g. if User has field Address which has field ZipCode,
	 * you can get the getter method for ZipCode by specifying fieldName "address.zipCode" for the User class;
	 * allowNested must be true for this.
	 * 
	 * @param fieldName    name of field to get getter method for
	 * @param clazz        class to get getter method from
	 * @param allowNested  whether or not to allow nested field names
	 * 
	 * @return  getter method
	 * 
	 * @throws NoSuchMethodException if method not found
	 */
	public static Method getterMethod(String fieldName, Class<?> clazz, boolean allowNested) throws NoSuchMethodException {
		return getterMethod(fieldName, null, clazz, allowNested);
	}
	
	/**
	 * Returns getter method for the given fieldName of given fieldType from the given clazz.
	 * 
	 * Supports getting method for nested objects (e.g. if User has field Address which has field ZipCode,
	 * you can get the getter method for ZipCode by specifying fieldName "address.zipCode" for the User class.
	 *  
	 * @param fieldName    name of field to get getter method for
	 * @param fieldType    class type of the field
	 * @param clazz        class to get getter method from
	 * @param allowNested  whether or not to allow nested field names
	 * 
	 * @return  getter method
	 * 
	 * @throws NoSuchMethodException if method not found
	 */
	public static Method getterMethod(String fieldName, Class<?> fieldType, Class<?> clazz, boolean allowNested) throws NoSuchMethodException {
		String remainingPath = null;
		if (allowNested) {
			int pathSepIndex = fieldName.indexOf(".");
			if (pathSepIndex >= 0) {
				remainingPath = fieldName.substring(pathSepIndex + 1);
				fieldName = fieldName.substring(0, pathSepIndex);
			}
		}
		Method method = null;
		String getterNamePostfix = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		if (fieldType == null || remainingPath != null) {
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
		if (remainingPath != null) {
			method = getterMethod(remainingPath, fieldType, method.getReturnType(), allowNested);
		}
		return method;
	}
	
	/**
	 * Returns setter method for the given fieldName of given fieldType from the given clazz.
	 * 
	 * Supports getting method for nested objects (e.g. if User has field Address which has field ZipCode,
	 * you can get the setter method for ZipCode by specifying fieldName "address.zipCode" for the User class.
	 * 
	 * @param fieldName    name of field to get setter method for
	 * @param fieldType    class type of the field
	 * @param clazz        class to get setter method from
	 * @param allowNested  whether or not to allow nested field names
	 * 
	 * @return    setter method
	 * 
	 * @throws NoSuchMethodException if method not found
	 */
	public static Method setterMethod(String fieldName, Class<?> fieldType, Class<?> clazz, boolean allowNested) throws NoSuchMethodException {
		if (allowNested) {
			int lastPathSepIndex = fieldName.lastIndexOf(".");
			if (lastPathSepIndex >= 0) {
				String pathToField = fieldName.substring(0, lastPathSepIndex);
				fieldName = fieldName.substring(lastPathSepIndex+1);
				Method getterMethod = getterMethod(pathToField, clazz, true);
				clazz = getterMethod.getReturnType();
			}
		}
		String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		return clazz.getMethod(setterName, fieldType);
	}
	
	/**
	 * Returns the object to invoke a reflection method for when the fieldPath is potentially a nested path to a field.
	 * For example, fieldPath might be "address.zipCode" for a User class, in which case the invocation target
	 * will be the Address object of User.
	 * 
	 * @param fieldPath               field path (e.g. "user.address.zipCode")
	 * @param object                  top level object
	 * @param instantiateNullObjects  if true, nested objects will be constructed from default constructor if null; however, top level object cannot be null
	 * 
	 * @return                        object at the tail of the field path
	 * 
	 * @throws NullPointerException   if any object in the nested path leading up the the final field is null and could not be instantiated.
	 * @throws NoSuchMethodException  if any part of the path is invalid
	 * @throws InvocationTargetException if reflection failure
	 * @throws IllegalAccessException    if reflection failure
	 */
	public static Object getInvocationTarget(String fieldPath, Object object, boolean instantiateNullObjects) 
			throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		if (object == null) {
			throw new NullPointerException("Object is null at path " + fieldPath);
		}
		if (fieldPath.contains(".")) {
			int i = fieldPath.indexOf(".");
			Method getterMethod = getterMethod(fieldPath.substring(0, i), null, object.getClass(), false);
			Object nextObjectDown = getterMethod.invoke(object, (Object[]) null);
			if (nextObjectDown == null && instantiateNullObjects) {
				try {
					nextObjectDown = getterMethod.getReturnType().newInstance();
				} catch (InstantiationException ie) {
					// since this only occurs if instantiateNullObjects is true, catch and rethrow as NPE so calling methods don't have to deal with it
					throw new NullPointerException("Object in path remains null due to instantiation failure: " + ie.getMessage());
				}
				Method setterMethod = setterMethod(fieldPath.substring(0, i), getterMethod.getReturnType(), object.getClass(), false);
				setterMethod.invoke(object, nextObjectDown);
			}
			return getInvocationTarget(fieldPath.substring(i+1), nextObjectDown, instantiateNullObjects);
		} else {
			return object;
		}
	}
}
