package com.app.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.response.QueryResponse;

import com.app.entity.SolrBean;
import com.app.util.JDBCUtil;
import com.app.util.SolrJUtils;

public class ServletService {
	SolrClient solr;
	static final String SERVER_PATH = "E:\\Code\\ArchivesBureau";

	public List<SolrBean> getResult(String txt, String model, String iType, String iType2) {
		List<SolrBean> list = new ArrayList<SolrBean>();
		try {
			solr = SolrJUtils.buildClient(SolrJUtils.solrBaseUrl + "demo");
			SolrQuery query = new SolrQuery();
			SolrJUtils.setQueryOrConfig(query, SolrBean.class, txt, model, iType, iType2);
			query.setStart(0);
			query.setRows(5000);
			query.setTermsMaxCount(5000);
			// 默认生成的response是GET方式请求，将其改为POST可以防止header过大报错
//			QueryResponse response = solr.query(query);
			QueryResponse response = solr.query(query, METHOD.POST);
			list = response.getBeans(SolrBean.class);
//			list = SolrJUtils.getBeans(SolrBean.class, response.getResults());// 使用自己定义的转Bean方法
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 重建索引文件
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean indexSolar() {
		List<SolrBean> list = null;
		try {
			solr = SolrJUtils.buildClient(SolrJUtils.solrBaseUrl + "demo");
			list = getAll(SolrBean.class);
			solr.deleteByQuery("*:*");
			solr.commit();
			if (list.size() > 0) {
				solr.addBeans(list.iterator());
				solr.optimize();
				System.out.println("生成成功");
			} else {
				System.out.println("查询数据失败");
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 获取list对象，用于建立索引
	 * @return
	 */
	public <T> List<T> getAll(Class<T> clazz) {
		String sql = "select * from li_user_collect";
		List<T> list = null;
		try {
			list = JDBCUtil.find(sql, clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}