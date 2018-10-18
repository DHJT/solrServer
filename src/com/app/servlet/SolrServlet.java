package com.app.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.app.service.ServletService;

import net.sf.json.JSONSerializer;

/**
 * @author Deears
 * @date: 2018年8月22日 下午5:15:36
 */
@SuppressWarnings("serial")
public class SolrServlet extends HttpServlet {
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("开始操作");
		ServletService ss = new ServletService();
		try {
			ss.indexSolar();
			write("{success:'true'}", response);
		} catch (Exception e) {
			e.printStackTrace();
			write("{success:'false'}", response);
		}
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
	}

	// 重建索引

	// 返回到前台
	private void write(Object result, HttpServletResponse response) {
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
