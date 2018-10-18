package com.app.util;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class JDBCUtil {

	public static String DRIVERNAME = null;
	public static String URL = null;
	public static String USER = null;
	public static String PASSWORD = null;

	public static Connection getConnection() throws Exception {
		Connection connn = null;
		try {
			Properties props = new Properties();
			// Reader in = new FileReader("db.properties");
			InputStream in = JDBCUtil.class.getClassLoader().getResourceAsStream("db.properties");
			props.load(in);
			DRIVERNAME = props.getProperty("db.driverClassName");
			URL = props.getProperty("db.url");
			USER = props.getProperty("db.username");
			PASSWORD = props.getProperty("db.password");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		Class.forName(DRIVERNAME);
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}

	public static void closeResource(Connection conn, PreparedStatement st) throws SQLException {
		st.close();
		conn.close();
	}

	public static void closeResource(Connection conn, ResultSet rs, PreparedStatement st) throws SQLException {
		st.close();
		rs.close();
		conn.close();
	}

	public static <T> List find(String sql, Class<T> clazz) throws Exception {
		List<T> list = new ArrayList<T>();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				T obj = clazz.newInstance();
				Field[] fields = clazz.getDeclaredFields();
				for (int i = 0; i < fields.length; i++) {
					fields[i].setAccessible(true);
					if (fields[i].getType().toString().indexOf("java.lang.String") != -1) {
						fields[i].set(obj, rs.getString(fields[i].getName().toUpperCase()));
					} else if (fields[i].getType().toString().indexOf("java.util.Date") != -1) {
						fields[i].set(obj, rs.getDate(fields[i].getName().toUpperCase()));
					} else {
						fields[i].set(obj, rs.getInt(fields[i].getName().toUpperCase()));
					}
				}
				if (obj != null && !"".equals(obj)) {
					list.add(obj);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		// ResultSet rs = stmt.executeQuery(sql);
		return list;
	}
}
