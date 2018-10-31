package com.app.servlet;

import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.app.entity.SolrBean;
import com.app.service.ServletService;
import com.app.util.Util;

import net.sf.json.JSONSerializer;

@SuppressWarnings("serial")
public class QueryServlet extends HttpServlet {

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		doGet(request, response);
	}

	@Override
	public  void doGet(HttpServletRequest request, HttpServletResponse response){
		String txt = Util.toUTF8(request.getParameter("txt"));
		String iType = Util.toUTF8(request.getParameter("iType"));
		String iType2 = Util.toUTF8(request.getParameter("iType2"));
		String model = Util.toUTF8(request.getParameter("model"));
		ServletService ss = new ServletService();
		List<SolrBean> list;
		try {
			list = ss.getResult(txt, model, iType, iType2);
			write(list, response);
		} catch (Exception e) {
			write(null, response);
		}
	}

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
