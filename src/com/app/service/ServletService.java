package com.app.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
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
			SolrJUtils.setQueryOrConfig(query, SolrBean.class, txt, model,iType,iType2);
			query.setStart(0);
			query.setRows(5000);
			query.setTermsMaxCount(5000);
			QueryResponse response = solr.query(query);
			list = response.getBeans(SolrBean.class);
//			list = SolrJUtils.getBeans(SolrBean.class, response.getResults());
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

	private static boolean isContain(ArrayList<Field> fieldList, Field field) {
		for (Field temp : fieldList) {
			if (temp.getName().equals(field.getName())) {
				return true;
			}
		}
		return false;
	}
}
