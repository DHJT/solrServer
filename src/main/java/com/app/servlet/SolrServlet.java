package com.app.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.app.service.ServletService;

/**
 * @author Deears
 * @date: 2018年8月22日 下午5:15:36
 */
@SuppressWarnings("serial")
public class SolrServlet extends BaseServlet {

	// 重建索引
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
}
