package com.app.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONSerializer;

public class BaseServlet extends HttpServlet {
	// 返回到前台
	protected void write(Object result, HttpServletResponse response) {
		try {
			response.setContentType("text/html;charset=UTF-8");
			String data;
			if (result instanceof String) {
				data = result.toString();
			} else {
				data = JSONSerializer.toJSON(result).toString();
			}
			response.getWriter().print(data);
			response.getWriter().flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
