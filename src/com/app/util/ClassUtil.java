package com.app.util;

import java.lang.reflect.Method;

public class ClassUtil {
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
