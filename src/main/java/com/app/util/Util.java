package com.app.util;

import java.beans.IntrospectionException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.MessageDigest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 应用系统通用功能函数
 *
 *
 */
public class Util {
	/**
	 * 日志
	 */
	private static Log logger = LogFactory.getLog(Util.class);
	private static sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
	private static sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
	private static long currentId=0;

	/**
	 * 判断布尔
	 *
	 * @param obj
	 * @return
	 */
	public static boolean getBoolean(Object obj) {
		if ((obj != null) && ("true".equals(obj.toString()) || "1".equals(obj.toString())))
			return true;
		else
			return false;
	}

	/**
	 * 获取obj的int值
	 * @param obj
	 * @return
	 */
	public static int getInt(Object obj) {
		if (obj != null) {
			try {
				return Integer.parseInt(obj.toString());
			} catch (Exception e) {
				return 0;
			}
		} else {
			return 0;
		}
	}

	/**
	 * 判断是否为空
	 *
	 * @param obj
	 * @return
	 */
	public static boolean isNull(Object obj) {
		if ((obj != null) && (!"".equals(obj)))
			return false;
		else
			return true;
	}

	/**
	 * 判断是否非空
	 *
	 * @param obj
	 * @return
	 */
	public static boolean notNull(Object obj) {
		if ((obj != null) && (!"".equals(obj.toString())))
			return true;
		else
			return false;
	}

	// 最后的aa表示“上午”或“下午” HH表示24小时制 如果换成hh表示12小时制
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss aa");
	final public static SimpleDateFormat DF_DATE = new SimpleDateFormat("yyyy-MM-dd");
	final public static SimpleDateFormat DF_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 将字符串转为日期型返回，转换异常返回null
	 *
	 * @param s
	 *            param sf
	 * @return
	 */
	public static Date toDate(String s, SimpleDateFormat sf) {
		try {
			if (s == null)
				return null;
			return sf.parse(s);
		} catch (Throwable e) {
			return null;
		}
	}

	/**
	 * 将Date类型的对象转化为String
	 *
	 * @param date
	 * @param sf
	 * @return
	 */
	public static String toStr(Date date, SimpleDateFormat sf) {
		if (isNull(date))
			return "";
		return sf.format(date);
	}

	public synchronized static String generateId() {
		long id = new Date().getTime();
		if (currentId < id) {
			Util.currentId = id;
		} else {
			currentId++;
		}
		return currentId + "";
	}
	/**
	 * 取得与指定日期间隔指定天数的日期
	 *
	 * @return
	 */
	public static Date getDateAfterDay(Date date, int day) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, day);
		return c.getTime();
	}

	/**
	 * 比较两个日期
	 *
	 * @return
	 */
	public static boolean compareDate(Date basicDate, Date compareDate) {
		return compareDate.getTime() > basicDate.getTime();
	}

	/**
	 * 取得当前日期，格式为2008-01-01 01:01
	 *
	 * @return
	 */
	public static String getCurrentDate() {
		return Util.getCurrentDate(true);
	}

	/**
	 * 获取当前星期几
	 */
	public static String getDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		switch (dayOfWeek) {
		case 1:
			return "星期日";
		case 2:
			return "星期一";
		case 3:
			return "星期二";
		case 4:
			return "星期三";
		case 5:
			return "星期四";
		case 6:
			return "星期五";
		case 7:
			return "星期六";
		}
		return "";
	}

	/**
	 * 取得当前日期,hasTime为true 格式为2008-01-01 01:01,hasTime为false 格式为2008-01-01
	 *
	 * @param hasTime
	 * @return
	 */
	public static String getCurrentDate(boolean hasTime) {
		return Util.toStr(new Date(), hasTime ? Util.DF_TIME : Util.DF_DATE);
	}

	/**
	 * 将日期毫秒数转化为年月格式（YYYYMM），例如20080607
	 *
	 * @param time
	 *            日期毫秒数
	 * @return
	 */
	public static String toYearMonth(Long time) {
		Calendar d = Calendar.getInstance();
		d.setTimeInMillis(time);
		int year = d.get(Calendar.YEAR);
		int month = d.get(Calendar.MONTH) + 1;
		return "" + year + (month > 9 ? ("" + month) : ("0" + month));
	}

	/**
	 * 将日期转换为星期，传入参数为YYYY-mm-dd
	 *
	 * @param date
	 *
	 * @return
	 */
	public static String toWeek(Date date) {
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

	/**
	 * 将日期转换为星期，传入参数为YYYY-mm-dd
	 *
	 * @param date
	 *
	 * @return
	 */
	public static String toWeek(String date) {
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

	/**
	 * 将日期毫秒数转化为年月日格式（YYYYMMDD），例如2008060708
	 *
	 * @param time
	 *            日期毫秒数
	 * @return
	 */
	public static String toDate(Long time) {
		Calendar d = Calendar.getInstance();
		d.setTimeInMillis(time);
		int year = d.get(Calendar.YEAR);
		int month = d.get(Calendar.MONTH) + 1;
		int date = d.get(Calendar.DATE);
		return "" + year + "-" + (month > 9 ? ("" + month) : ("0" + month)) + "-"
				+ (date > 9 ? ("" + date) : ("0" + date));
	}

	/**
	 * 获得本周的第一天，周一
	 *
	 * @return
	 */
	public static String getCurrentWeekDayStartDate() {
		Calendar c = Calendar.getInstance();
		try {
			int weekday = c.get(Calendar.DAY_OF_WEEK) - 2;
			c.add(Calendar.DATE, -weekday);
			c.setTime(DF_TIME.parse(DF_DATE.format(c.getTime()) + " 00:00:00"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Util.toStr(c.getTime(), Util.DF_DATE);
	}

	/**
	 * 获得本周的最后一天，周日
	 *
	 * @return
	 */
	public static String getCurrentWeekDayEndDate() {
		Calendar c = Calendar.getInstance();
		try {
			int weekday = c.get(Calendar.DAY_OF_WEEK);
			c.add(Calendar.DATE, 8 - weekday);
			c.setTime(DF_TIME.parse(DF_DATE.format(c.getTime()) + " 23:59:59"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Util.toStr(c.getTime(), Util.DF_DATE);
	}

	/**
	 * 获得本月的开始日期，即2017-01-01
	 *
	 * @return
	 */
	public static String getCurrentMonthStartDate(String date) {
		Calendar c = Calendar.getInstance();
		if (Util.notNull(date)) {
			try {
				c.setTime(DF_DATE.parse(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		Date now = null;
		try {
			c.set(Calendar.DATE, 1);
			now = DF_DATE.parse(DF_DATE.format(c.getTime()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Util.toStr(now, Util.DF_DATE);
	}

	/**
	 * 获得本月的结束日期，即2017-01-31
	 *
	 * @return
	 */
	public static String getCurrentMonthEndDate(String date) {
		Calendar c = Calendar.getInstance();
		if (Util.notNull(date)) {
			try {
				c.setTime(DF_DATE.parse(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		Date now = null;
		try {
			c.set(Calendar.DATE, 1);
			c.add(Calendar.MONTH, 1);
			c.add(Calendar.DATE, -1);
			now = DF_TIME.parse(DF_DATE.format(c.getTime()) + " 23:59:59");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Util.toStr(now, Util.DF_DATE);
	}

	/**
	 * 当前季度的开始时间，即2012-01-01 00:00:00
	 *
	 * @return
	 */
	public static String getCurrentQuarterStartDate() {
		Calendar c = Calendar.getInstance();
		int currentMonth = c.get(Calendar.MONTH) + 1;
		Date now = null;
		try {
			if (currentMonth >= 1 && currentMonth <= 3)
				c.set(Calendar.MONTH, 0);
			else if (currentMonth >= 4 && currentMonth <= 6)
				c.set(Calendar.MONTH, 3);
			else if (currentMonth >= 7 && currentMonth <= 9)
				c.set(Calendar.MONTH, 4);
			else if (currentMonth >= 10 && currentMonth <= 12)
				c.set(Calendar.MONTH, 9);
			c.set(Calendar.DATE, 1);
			now = DF_TIME.parse(DF_DATE.format(c.getTime()) + " 00:00:00");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Util.toStr(now, Util.DF_DATE);
	}

	/**
	 * 当前季度的结束时间，即2012-03-31 23:59:59
	 *
	 * @return
	 */
	public static String getCurrentQuarterEndDate() {
		Calendar c = Calendar.getInstance();
		int currentMonth = c.get(Calendar.MONTH) + 1;
		Date now = null;
		try {
			if (currentMonth >= 1 && currentMonth <= 3) {
				c.set(Calendar.MONTH, 2);
				c.set(Calendar.DATE, 31);
			} else if (currentMonth >= 4 && currentMonth <= 6) {
				c.set(Calendar.MONTH, 5);
				c.set(Calendar.DATE, 30);
			} else if (currentMonth >= 7 && currentMonth <= 9) {
				c.set(Calendar.MONTH, 8);
				c.set(Calendar.DATE, 30);
			} else if (currentMonth >= 10 && currentMonth <= 12) {
				c.set(Calendar.MONTH, 11);
				c.set(Calendar.DATE, 31);
			}
			now = DF_TIME.parse(DF_DATE.format(c.getTime()) + " 23:59:59");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Util.toStr(now, Util.DF_DATE);
	}

	/**
	 * 判断是否为字母a-z,A-Z
	 *
	 * @param c
	 * @return
	 */
	static public boolean isCharator(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
	}

	/**
	 * 判断是否为数字0-9
	 *
	 * @param c
	 * @return
	 */
	static public boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}

	/**
	 * 对集合排序
	 *
	 * @param data
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	static public <T extends Comparable> List<T> sort(Collection<T> data) {
		List<T> list = new ArrayList<T>(data);
		Collections.sort(list);
		return list;
	}
	public static String toUTF8(String str) {
		if (str == null)
			return null;
		String retStr = str;
		byte b[];
		try {
			b = str.getBytes("ISO8859_1");
			for (int i = 0; i < b.length; i++) {
				byte b1 = b[i];
				if (b1 == 63)
					break; // 1
				else if (b1 > 0)
					continue;// 2
				else if (b1 < 0) { // 不可能为0，0为字符串结束符
					// 小于0乱码
					retStr = new String(b, "UTF8");
					break;
				}
			}
		} catch (UnsupportedEncodingException e) {
			// e.printStackTrace();
		}
		return retStr;
	}
	/**
	 * 指定属性名和属性值，利用反射调用set方法
	 *
	 * @param obj
	 * @param fieldName
	 * @param value
	 * @throws Exception
	 */
	public static void invokeSetMethod(Object obj, String fieldName, Object value) {
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String setMethodName = "set" + firstLetter + fieldName.substring(1);
			String getMethodName = "get" + firstLetter + fieldName.substring(1);
			if (value != null) {
				if (!obj.getClass().getMethod(getMethodName).getReturnType().toString()
						.equals(value.getClass().toString())) {// 判断get类型是否和value类型一致
					if (obj.getClass().getMethod(getMethodName).getReturnType().getClass().isInstance(boolean.class)) {// 如果不一致，并且get类型为boolean
						if (value.toString().length() < 3) {
							if (value.equals("0") || value.equals("是")) {
								value = "true";
								value = Boolean.parseBoolean((String) value);
							} else if (value.equals("1") || value.equals("否")) {
								value = "false";
								value = Boolean.parseBoolean((String) value);
							}

						}
					}
				}
				Method m = obj.getClass().getMethod(setMethodName, value.getClass());
				m.invoke(obj, new Object[] { value });
			} else {
				Method[] methods = obj.getClass().getMethods();
				for (Method m : methods) {
					if (m.getName().equals(setMethodName) && (m.getParameterTypes().length == 1)) {
						m.invoke(obj, new Object[] { null });
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 指定属性名，利用反射调用get方法，返回属性值
	 *
	 * @param obj
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	public static Object invokeGetMethod(Object obj, String fieldName) {
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getMethodName = "get" + firstLetter + fieldName.substring(1);
			Method m = obj.getClass().getMethod(getMethodName);
			return m.invoke(obj, new Object[] {});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 指定方法名和参数，利用反射调用方法
	 *
	 * @param obj
	 * @param methodName
	 * @param parameter
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("rawtypes")
	public static Object invokeMethod(Object obj, String methodName, Object[] parameter) {
		try {
			Method[] methods = obj.getClass().getMethods();
			for (int i = 0; i < methods.length; i++) {
				Method m = methods[i];
				if (m.getName().equals(methodName)) {
					Class[] paraType = m.getParameterTypes();
					if (paraType != null && paraType.length == parameter.length) {
						// logger.debug("����:"+obj.getClass().getName()+"."+methodName);
						Object[] newParam = new Object[paraType.length];
						for (int j = 0; j < paraType.length; j++) {
							String typeName = paraType[j].getName();
							String typeShortName = typeName.substring(typeName.lastIndexOf(".") + 1);
							// logger.debug("����"+parameter[j]);
							if (Util.isNull(parameter[j]))
								newParam[j] = null;
							else {
								if (typeShortName.equals("Long") || typeShortName.equals("long")) {
									newParam[j] = new Long(parameter[j].toString());
								} else if (typeShortName.equals("Integer") || typeShortName.equals("int")) {
									newParam[j] = new Integer(parameter[j].toString());
								} else if (typeShortName.equals("Float") || typeShortName.equals("float")) {
									newParam[j] = new Float(parameter[j].toString());
								} else if (typeShortName.equals("Double") || typeShortName.equals("double")) {
									newParam[j] = new Double(parameter[j].toString());
								} else if (typeShortName.equals("String")) {
									newParam[j] = parameter[j].toString();
								} else {
									newParam[j] = parameter[j];
								}
							}
						}
						return m.invoke(obj, newParam);
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	/**
	 * 取得消息摘要码
	 *
	 * @param b
	 * @return
	 */
	public static String md5Encode(byte[] b) {
		String resultString = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(b));
		} catch (Exception ex) {
			resultString = null;
		}
		return resultString;
	}

	/**
	 * 取得消息摘要码
	 *
	 * @param fileName
	 * @return
	 */
	public static String md5Encode(String str) {
		return md5Encode(str.getBytes());
	}

	/**
	 * 取得消息摘要码
	 *
	 * @param file
	 * @return
	 */
	public static String md5Encode(File file) {
		if (file.exists() && file.isFile()) {
			FileInputStream in = null;
			try {
				byte[] b = new byte[1024];
				MessageDigest md = MessageDigest.getInstance("MD5");
				in = new FileInputStream(file);
				while ((in.read(b)) != -1) {
					md.update(b);
				}
				return Util.byteArrayToHexString(md.digest());
			} catch (Exception e) {
				logger.error(e.toString());
				return "";
			} finally {
				try {
					in.close();
					in = null;
				} catch (Exception e) {
					logger.error(e.toString());
				}
			}
		}
		return "";
	}

	/**
	 * 取得消息摘要码 三性检查时调用
	 *
	 * @param file
	 * @return
	 */
	public static String md5EncodeCheck(File file) {
		if (file.exists() && file.isFile()) {
			FileInputStream in = null;
			try {
				byte[] b = new byte[1024];
				MessageDigest md = MessageDigest.getInstance("MD5");
				in = new FileInputStream(file);
				int numRead = 0;
				while ((numRead = in.read(b)) > 0) {
					md.update(b, 0, numRead);
				}
				return Util.byteArrayToHexString(md.digest());
			} catch (Exception e) {
				logger.error(e.toString());
				return "";
			} finally {
				try {
					in.close();
					in = null;
				} catch (Exception e) {
					logger.error(e.toString());
				}
			}
		}
		return "";
	}

	/**
	 * 取得PDF的版本
	 *
	 * @param fileName
	 * @return
	 * @since 2009-7-9
	 */
	public static double getPdfVersion(String fileName) {
		FileInputStream fileInStream = null;
		InputStreamReader inputReader = null;
		BufferedReader bufReader = null;
		try {
			fileInStream = new FileInputStream(fileName);
			inputReader = new InputStreamReader(fileInStream);
			bufReader = new BufferedReader(inputReader);
			String lineStr = "";
			lineStr = bufReader.readLine();
			String version = lineStr.substring(5, 8);
			double versionNumber = Double.parseDouble(version);
			return versionNumber;
		} catch (Throwable e) {
			// e.printStackTrace();
			return 0;
		} finally {
			try {
				bufReader.close();
				inputReader.close();
				fileInStream.close();
			} catch (Exception e) {

			}
		}
	}

	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
			"e", "f" };

	public static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	/**
	 * copy properties from a bean. Not like implementing the Clone interface,
	 * <br>
	 * just using reflect, <br>
	 * so Collenction, relation of the other Beans will be ignored.
	 *
	 * @param bean
	 * @return A
	 * @author liuliang 2009-4-30
	 */
	public static <T> T copyBean(final T bean) {
		if (bean == null)
			return null;
		try {
			T newBean = (T) bean.getClass().newInstance();
			Method[] methods = bean.getClass().getMethods();
			String name;
			Object value;
			Method get;
			for (Method m : methods) {
				if (!Modifier.isPublic(m.getModifiers()))
					continue;
				if (!m.getName().startsWith("set"))
					continue;
				if (m.getParameterTypes() == null || m.getParameterTypes().length > 1)
					continue;
				name = m.getName().substring(3);
				try {
					get = bean.getClass().getMethod("get" + name);
					if (get == null)
						get = bean.getClass().getMethod("is" + name);
					if (get == null)
						continue;
					value = get.invoke(bean);
					if (value != null && !(value instanceof Number // || value
							// instanceof
							// Collection
							|| value instanceof String || value instanceof Boolean || value instanceof Character
							|| value.getClass().isArray() || value.getClass().isEnum()))
						continue;
					m.invoke(newBean, new Object[] { value });
				} catch (Exception e) {
					continue;
				}
			}
			return newBean;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 复制
	 *
	 * @param <T>
	 * @param bean
	 * @return
	 */
	public static <T> T copy(final T bean) {
		if (bean == null)
			return null;
		try {
			T newBean = (T) bean.getClass().newInstance();
			for (Method m : bean.getClass().getMethods()) {
				if (!m.getName().startsWith("set"))
					continue;
				if (m.getParameterTypes() != null && m.getParameterTypes().length > 1)
					continue;
				String name = m.getName().substring(3);
				try {
					Method get = bean.getClass().getMethod("get" + name);
					if (get == null)
						get = bean.getClass().getMethod("is" + name);
					if (get == null)
						continue;
					Object value = get.invoke(bean);
					m.invoke(newBean, new Object[] { value });
				} catch (Exception e) {
					continue;
				}
			}
			return newBean;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 复制源对象中不为空的值到目标对象
	 *
	 * @param <T>
	 *            java bean
	 * @param src
	 *            源对象
	 * @param dest
	 *            目标对象
	 * @author xjf 09-05-27
	 */
	public static <T> void copyProperties(T src, T dest) {
		if (src == null || dest == null) {
			return;
		}
		try {
			for (Method set : src.getClass().getMethods()) {
				if (!set.getName().startsWith("set")) {
					continue;
				}
				String name = set.getName().substring(3);
				Method get = src.getClass().getMethod("get" + name);
				if (get == null)
					get = src.getClass().getMethod("is" + name);
				if (get == null)
					continue;
				Object value = get.invoke(src);
				if (value != null) {// && !"".equals(value)
					if (value instanceof Character) {
						char c = (Character) value;
						if (c == '\u0000')
							continue;
					}
					set.invoke(dest, value);
				}
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 复制文件
	 *
	 * @param sourceFile
	 * @param targetFile
	 * @throws IOException
	 */
	public static void copyFile(File sourceFile, File targetFile) {
		try {
			if (!sourceFile.exists()) {
				System.out.println("源文件不存在！已跳过！");
				return;
			}
			if (!targetFile.getParentFile().exists()) {
				targetFile.getParentFile().mkdirs();
			}
			// 新建文件输入流并对它进行缓冲
			FileInputStream input = new FileInputStream(sourceFile);
			BufferedInputStream inBuff = new BufferedInputStream(input);

			// 新建文件输出流并对它进行缓冲
			FileOutputStream output = new FileOutputStream(targetFile);
			BufferedOutputStream outBuff = new BufferedOutputStream(output);

			// 缓冲数组
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			// 刷新此缓冲的输出流
			outBuff.flush();

			// 关闭流
			inBuff.close();
			outBuff.close();
			output.close();
			input.close();
		} catch (IOException e) {
			logger.debug("复制文件失败！");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 删除文件,或者目录下面的所有文件和目录。
	 *
	 * @param f
	 *            如果是文件，直接删除，是目录则递归删除
	 * @since 2009-8-19
	 */
	public static void deleteFiles(File f) throws IOException {
		if (!f.exists())
			return;
		if (f.isFile()) {
			f.delete();
			return;
		}
		for (File file : f.listFiles()) {
			if (file.isDirectory() && file.list().length > 0) {
				deleteFiles(file);
			}
			file.delete();
		}
		f.delete();
	}

	/**
	 * 将一个 JavaBean 对象转化为一个 Map
	 *
	 * @param bean
	 *            要转化的JavaBean 对象
	 * @return 转化出来的 Map 对象
	 * @throws IntrospectionException
	 *             如果分析类属性失败
	 * @throws IllegalAccessException
	 *             如果实例化 JavaBean 失败
	 * @throws InvocationTargetException
	 *             如果调用属性的 setter 方法失败
	 */
	public static Map convertBean(Object bean) {
		if (bean == null)
			return null;
		return new org.apache.commons.beanutils.BeanMap(bean);
	}

	/**
	 * 将一个 Map 对象转化为一个 JavaBean
	 *
	 * @param type
	 *            要转化的类型
	 * @param map
	 *            包含属性值的 map
	 * @return 转化出来的 JavaBean 对象
	 * @throws IntrospectionException
	 *             如果分析类属性失败
	 * @throws IllegalAccessException
	 *             如果实例化 JavaBean 失败
	 * @throws InstantiationException
	 *             如果实例化 JavaBean 失败
	 * @throws InvocationTargetException
	 *             如果调用属性的 setter 方法失败
	 */
	public static Object convertMap(Class type, Map map)
			throws IllegalAccessException, InvocationTargetException, InstantiationException {
		Object obj = type.newInstance();
		BeanUtils.populate(obj, map);
		return obj;
	}

	/**
	 * 和JS中的join方法功能一样
	 *
	 * @param ids
	 * @param symbol
	 * @return
	 * @since 2009-8-11
	 */
	public static String join(Long[] ids, String symbol) {
		StringBuffer buf = new StringBuffer();
		for (Long id : ids) {
			buf.append(symbol + id);
		}
		return buf.substring(1);
	}

	/**
	 * 和JS中的join方法功能一样
	 *
	 * @param ids
	 * @param symbol
	 * @return
	 * @since 2009-8-11
	 */
	public static String join(List list, String symbol) {
		StringBuffer buf = new StringBuffer();
		if (list == null || list.size() == 0)
			return "";
		for (Object obj : list) {
			buf.append(symbol + obj);
		}
		return buf.substring(1);
	}

	public static List<String> toList(String content, int size) {
		List<String> list = new ArrayList<String>();
		if (Util.isNull(content))
			return list;
		int i = 0;
		while (i + size <= content.length()) {
			list.add(content.substring(i, i + size));
			i += size;
		}
		if (i < content.length())
			list.add(content.substring(i));
		return list;
	}

	public static String toString(List<String> list) {
		StringBuffer str = new StringBuffer(3000);
		for (String s : list) {
			// logger.debug(s);
			str.append(s);
		}
		return str.toString();
	}

	/**
	 * get all files of the base directory with specified formats
	 *
	 * @param baseDir
	 *            the base directory to get files
	 * @param format
	 *            can split by ','
	 * @return file array of the base directory
	 * @author xjf
	 * @since 2010-05-18
	 */
	public static File[] filterFiles(File baseDir, String format) {
		final String[] formats = format.split(",");
		if (baseDir == null || !baseDir.exists() || !baseDir.isDirectory()) {
			return new File[0];
		}
		FileFilter f = new FileFilter() {
			@Override
			public boolean accept(File cur) {
				if (!cur.exists() || cur.isDirectory())
					return false;
				String name = cur.getName();
				int index = name.lastIndexOf(".");
				if (index == -1 && formats.length == 0)
					return true;
				else
					for (String suffix : formats) {
						if (suffix.toLowerCase().equals(name.substring(index + 1).toLowerCase()))
							return true;
					}
				return false;
			}
		};
		return baseDir.listFiles(f);
	}

	public static void listFiles(File file, List<File> ret, String format) {
		if (!file.exists() || ret == null)
			return;
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				if (f.isFile())
					for (String suffix : format.split(",")) {
						if (f.getName().toLowerCase().endsWith(suffix.toLowerCase()))
							ret.add(f);
					}
				else
					listFiles(f, ret, format);
			}
		} else {
			for (String suffix : format.split(",")) {
				if (file.getName().toLowerCase().endsWith(suffix.toLowerCase()))
					ret.add(file);
			}
		}

	}

	/**
	 * base64加密字符串.
	 *
	 * @param oldStr
	 * @return
	 */
	public static String encode(String oldStr) {
		return encoder.encode(oldStr.getBytes());
	}

	/**
	 * base64解密字符串.
	 *
	 * @param oldStr
	 * @return
	 * @throws IOException
	 */
	public static String decode(String oldStr) throws IOException {
		return new String(decoder.decodeBuffer(oldStr));
	}

	/**
	 * base64编码输入流.
	 *
	 * @param inputStream
	 *            输入流
	 * @param outputStream
	 *            输出流
	 * @throws IOException
	 */
	public static void encode(InputStream inputStream, OutputStream outputStream) throws IOException {
		encoder.encode(inputStream, outputStream);
		inputStream.close();
		outputStream.close();
	}

	/**
	 * base64解密输入流.
	 *
	 * @param inputStream
	 * @param outputStream
	 * @throws IOException
	 */
	public static void decode(InputStream inputStream, OutputStream outputStream) throws IOException {
		decoder.decodeBuffer(inputStream, outputStream);
		inputStream.close();
		outputStream.close();
	}

	/**
	 * base64加密文件.
	 *
	 * @param inFileName
	 *            源文件
	 * @param outFileName
	 *            新的文件
	 * @throws IOException
	 */
	public static void encode(String inFileName, String outFileName) throws IOException {
		File oldFile = new File(inFileName);
		File newFile = new File(outFileName);
		InputStream input = new BufferedInputStream(new FileInputStream(oldFile));
		OutputStream out = new BufferedOutputStream(new FileOutputStream(newFile));
		encode(input, out);
	}

	/**
	 * base64解密文件.
	 *
	 * @param inFileName
	 *            源文件
	 * @param outFileName
	 *            新的文件
	 * @throws IOException
	 */
	public static void decode(String inFileName, String outFileName) throws IOException {
		File oldFile = new File(inFileName);
		File newFile = new File(outFileName);
		InputStream input = new BufferedInputStream(new FileInputStream(oldFile));
		OutputStream out = new BufferedOutputStream(new FileOutputStream(newFile));
		decode(input, out);
	}

	/**
	 * 写文件从src写到dst
	 *
	 * @param src
	 * @param dst
	 */
	public static void writeFile(File src, File dst) {
		try {
			InputStream in = null;
			OutputStream out = null;
			try {

				in = new BufferedInputStream(new FileInputStream(src), 16 * 1024);
				out = new BufferedOutputStream(new FileOutputStream(dst), 16 * 1024);
				byte[] buffer = new byte[16 * 1024];
				while (in.read(buffer) > 0) {
					out.write(buffer);
				}
			} finally {
				if (null != in) {
					in.close();
				}
				if (null != out) {
					out.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 类反射 获取GET方法
	public static Method getGetMethod(Class objectClass, String fieldName) {
		StringBuffer sb = new StringBuffer();
		sb.append("get");
		sb.append(fieldName.substring(0, 1).toUpperCase());
		sb.append(fieldName.substring(1));
		try {
			return objectClass.getMethod(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 类反射 获取GET方法
	public static Method getSetMethod(Class objectClass, String fieldName) {
		try {
			Class[] parameterTypes = new Class[1];
			Field field = objectClass.getDeclaredField(fieldName);
			parameterTypes[0] = field.getType();
			StringBuffer sb = new StringBuffer();
			sb.append("set");
			sb.append(fieldName.substring(0, 1).toUpperCase());
			sb.append(fieldName.substring(1));
			Method method = objectClass.getMethod(sb.toString(), parameterTypes);
			return method;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 数字转换为中文
	 *
	 * @author 刘佳彬
	 */
	public static String getStringNum(String str) {
		int intstr = Integer.parseInt(str);
		switch (intstr) {
		case 1:
			return "一";
		case 2:
			return "二";
		case 3:
			return "三";
		case 4:
			return "四";
		case 5:
			return "五";
		case 6:
			return "六";
		case 7:
			return "七";
		default:
			return "";
		}
	}

	/**
	 * 中文转换为数字
	 *
	 * @author 刘佳彬
	 */
	public static Integer getNumString(String str) {
		if (str.equals("一")) {
			return 1;
		} else if (str.equals("二")) {
			return 2;
		} else if (str.equals("三")) {
			return 3;
		} else if (str.equals("四")) {
			return 4;
		} else if (str.equals("五")) {
			return 5;
		} else if (str.equals("六")) {
			return 6;
		} else if (str.equals("七")) {
			return 7;
		} else if (str.equals("八")) {
			return 8;
		}
		return 0;
	}

	/**
	 * 将json格式的字符串解析成Map对象
	 * <li>json格式：{"name":"admin","retries":"3fff","testname"
	 * :"ddd","testretries":"fffffffff"}
	 */
	// public static HashMap<String, Object> JsonToHashMap(Object object) {
	// HashMap<String, Object> data = new HashMap<String, Object>();
	// // 将json字符串转换成jsonObject
	// JSONObject jsonObject = JSONObject.fromObject(object);
	// Iterator it = jsonObject.keys();
	// // 遍历jsonObject数据，添加到Map对象
	// while (it.hasNext()) {
	// String key = String.valueOf(it.next());
	// Object value = jsonObject.get(key);
	// data.put(key, value);
	// }
	// return data;
	// }

	/**
	 * 获取类clazz的所有Field，包括其父类的Field，如果重名，以子类Field为准。
	 *
	 * @param clazz
	 * @return Field数组
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
	 * 检测Field List中是否已经包含了目标field
	 *
	 * @param fieldList
	 * @param field
	 *            带检测field
	 * @return
	 */
	private static boolean isContain(ArrayList<Field> fieldList, Field field) {
		for (Field temp : fieldList) {
			if (temp.getName().equals(field.getName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 检测Field ResultSet中是否已经包含传入的字段名
	 *
	 * @author Liu {@link Date} 2017年5月4日14:56:40
	 * @return
	 */
	public static boolean isExistColumn(ResultSet rs, String columnName) {
		try {
			if (rs.findColumn(columnName) > 0) {
				return true;
			}
		} catch (SQLException e) {
			return false;
		}

		return false;
	}

	/**
	 * 获取系统根目录，即web-inf目录同级目录
	 *
	 * @return
	 */
	public static String getProjectRootPath() {
		String path = new Object() {
			public String getPath() {
				File f = new File(this.getClass().getResource("/").getPath());
				String path = f.getParentFile().getParent();
				return path;
			}
		}.getPath();
		return path;
	}

	// 插入OCRTEXT
	/*
	 * @Date 2017年3月21日09:24:31 刘佳彬
	 */
	public static StringBuffer readTxtFile(String filePath) {
		StringBuffer sb = new StringBuffer();
		try {
			String encoding = "GBK";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					sb.append(lineTxt);
				}
				read.close();
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		return sb;
	}

	/** 获取文件的前缀名 */
	public static String getFilePerfix(String fileName) {
		return fileName.substring(0,fileName.lastIndexOf("."));
	}
}
