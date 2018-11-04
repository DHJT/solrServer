package com.app.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

public class ClassUtil {

	/**
	 * 获取传入类的所有定义字段，包含父类字段
	 * @param clazz
	 * @return
	 */
	public static Field[] getAllField(Class<?> clazz) {
		ArrayList<Field> fieldList = new ArrayList<Field>();
		Field[] dFields = clazz.getDeclaredFields();
		if (null != dFields && dFields.length > 0) {
			fieldList.addAll(Arrays.asList(dFields));
		}

		Class<?> superClass = clazz.getSuperclass();
		if (superClass != Object.class) {
			Field[] superFields = getAllField(superClass);
			if (null != superFields && superFields.length > 0) {
				for (Field field : superFields) {
					if (!isContain(fieldList, field)) {
						fieldList.add(field);
					}
				}
			}
		}
		Field[] result = new Field[fieldList.size()];
		fieldList.toArray(result);
		return result;
	}

	/**
	 * 字段是否在List中
	 * @param fieldList
	 * @param field
	 * @return
	 */
	private static boolean isContain(ArrayList<Field> fieldList, Field field) {
		for (Field temp : fieldList) {
			if (temp != null && temp.getName().equals(field.getName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 简易版方法，默认不传参
	 * @param 方法名，不用写get
	 * @param 需要执行方法的类
	 * @return
	 */
	public static Object getInvoke(String methodName, Object clazz) {
		try {
			methodName = "get"+methodName.substring(0,1).toUpperCase()+methodName.substring(1);
			Method method = clazz.getClass().getMethod(methodName, new Class[] {});
			return method.invoke(clazz, new Object[] {});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Object getInvoke(String methodName, Class<?>[] methodValue, Object[] params, Object clazz) {
		try {
			methodName = "get"+methodName.substring(0,1).toUpperCase()+methodName.substring(1);
			Method method = clazz.getClass().getMethod(methodName, methodValue);
			return method.invoke(clazz, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 调用set方法
	 * @param 方法名，不用写set
	 * @param 调用方法时传参类型
	 * @param 调用方法的传参
	 * @param 调用方法的执行类
	 * @return
	 */
	public static Object setInvoke(String methodName, Class<?>[] methodValue, Object[] params, Object clazz) {
		try {
			methodName = "set"+methodName.substring(0,1).toUpperCase()+methodName.substring(1);
			Method method = clazz.getClass().getMethod(methodName, methodValue);
			return method.invoke(clazz, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
