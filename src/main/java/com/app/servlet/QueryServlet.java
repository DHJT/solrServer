package com.app.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.app.entity.SolrBean;
import com.app.service.ServletService;
import com.app.util.Util;

@SuppressWarnings("serial")
public class QueryServlet extends BaseServlet {

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		doGet(request, response);
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
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
}
