package com.app.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

/**
 * SolrJ接口类使用工具
 *
 * @packge com.app.util.SolrJUtils
 * @date 2018年1月15日
 * @author DHJT
 * @comment
 * @update
 */
public class SolrJUtils {

	// public static final String solrBaseUrl = "http://localhost:8983/solr/";
	public static final String solrBaseUrl = "http://localhost:8080/solrServer/";

	private static HttpSolrClient init(HttpSolrClient httpSolrClient) throws SolrServerException, IOException {
		httpSolrClient.setConnectionTimeout(100);
		httpSolrClient.setDefaultMaxConnectionsPerHost(100);
		httpSolrClient.setMaxTotalConnections(100);
		return httpSolrClient;
	}

	@SuppressWarnings("deprecation")
	public static SolrClient getClient() throws SolrServerException, IOException {
		HttpSolrClient httpSolrClient = new HttpSolrClient(solrBaseUrl);// early
		// version
		init(httpSolrClient);
		return httpSolrClient;
	}

	@SuppressWarnings("deprecation")
	public static SolrClient getClient(String solrUrl) throws SolrServerException, IOException {
		HttpSolrClient httpSolrClient = new HttpSolrClient(solrUrl);// early
																	// version
		init(httpSolrClient);
		return httpSolrClient;
	}

	public static SolrClient buildClient() throws SolrServerException, IOException {

		return new HttpSolrClient.Builder(solrBaseUrl).build();// recently
																// version
	}

	public static SolrClient buildClient(String solrUrl) throws SolrServerException, IOException {

		return new HttpSolrClient.Builder(solrUrl).build();// recently version
	}

	public static QueryResponse query(String keyword) throws SolrServerException, IOException {
		return SolrJUtils.buildClient().query(new SolrQuery(keyword));// keyword="*:*"
	}

	public static QueryResponse query(String core, String keyword) throws SolrServerException, IOException {
		return SolrJUtils.buildClient().query(new SolrQuery(core, keyword));// keyword="*:*"
	}

	public static QueryResponse query(SolrQuery solrQuery) throws SolrServerException, IOException {
		return SolrJUtils.buildClient().query(solrQuery);
	}

	public static QueryResponse query(String solrUrl, String core, String keyword)
			throws SolrServerException, IOException {
		return SolrJUtils.buildClient(solrUrl).query(new SolrQuery(core, keyword));// keyword="*:*"
	}

	public static QueryResponse query(String solrUrl, SolrQuery solrQuery) throws SolrServerException, IOException {
		return SolrJUtils.buildClient(solrUrl).query(solrQuery);
	}

	/**
	 * 自己将查询的Solr结果转为实体Beans
	 *
	 * @param clazz
	 * @param solrDocumentList
	 * @return
	 * @throws Exception
	 */
	public static final <T> List<T> getBeans(Class<T> clazz, SolrDocumentList solrDocumentList) throws Exception {
		List<T> list = new ArrayList<T>();
		T t = null;
		// 获取所有属性
		Field[] fields = Util.getAllField(clazz);
		for (SolrDocument solrDocument : solrDocumentList) {
			// 反射出实例
			t = clazz.newInstance();
			for (Field field : fields) {
				// 判断这个属性上是否存在注解SolrField //存在的话 设置值
				if (field.isAnnotationPresent(org.apache.solr.client.solrj.beans.Field.class)) {
					// 获取注解
					org.apache.solr.client.solrj.beans.Field solrField = field
							.getAnnotation(org.apache.solr.client.solrj.beans.Field.class);
					if ("#default".equals(solrField.value()) || Util.isNull(solrField.value())) {
						// 如果注解为默认的 采用此属性的name来从solr中获取值
						BeanUtils.setProperty(t, field.getName(), solrDocument.get(field.getName()));
					} else {
						// 如果注解为不是默认的 采用此注解上的值来从solr中获取值
						BeanUtils.setProperty(t, field.getName(), solrDocument.get(solrField.value()));
					}
				}
			}
			list.add(t);
		}
		return list;
	}

	/**
	 * 高亮处理，只对title、ocrtext字段高亮
	 * @param list
	 * @param response
	 * @return
	 */
	public static <T> List<T> queryHighlight(List<T> list, QueryResponse response) {
		Map<String, Map<String, List<String>>> highlightresult = response.getHighlighting();
        for (T t : list) {
            String id = ClassUtil.getInvoke("id", t).toString();;
            if (highlightresult.get(id) != null && highlightresult.get(id).get("title") != null) {
            	ClassUtil.setInvoke("title", new Class[] { String.class }, new Object[] { highlightresult.get(id).get("title").get(0) }, t);
            }
            if (highlightresult.get(id) != null && highlightresult.get(id).get("ocrtext") != null) {
            	ClassUtil.setInvoke("ocrtext", new Class[] { String.class }, new Object[] { highlightresult.get(id).get("ocrtext").get(0) }, t);
            }
        }
        return list;
    }

	/**
	 * 自己将查询的Solr结果转为实体Bean
	 *
	 * @param clazz
	 * @param solrDocumentList
	 * @return
	 * @throws Exception
	 */
	public static final <T> T getBean(Class<T> clazz, SolrDocument solrDocument) throws Exception {
		// 反射出实例
		T t = clazz.newInstance();
		// 获取所有属性
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			// 判断这个属性上是否存在注解SolrField //存在的话 设置值
			if (field.isAnnotationPresent(org.apache.solr.client.solrj.beans.Field.class)) {
				// 获取注解
				org.apache.solr.client.solrj.beans.Field solrField = field
						.getAnnotation(org.apache.solr.client.solrj.beans.Field.class);
				if ("#default".equals(solrField.value()) || Util.isNull(solrField.value())) {
					// 如果注解为默认的 采用此属性的name来从solr中获取值
					BeanUtils.setProperty(t, field.getName(), solrDocument.get(field.getName()));
				} else {
					// 如果注解为不是默认的 采用此注解上的值来从solr中获取值
					BeanUtils.setProperty(t, field.getName(), solrDocument.get(solrField.value()));
				}
			}
		}
		return t;
	}

	/**
	 * 根据查询条件获取从制定位置start获取rowLength个结果List<T>
	 * @param queryStr
	 * @param start
	 * @param rowLength
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> queryBeans(SolrClient solr, Class<T> clazz, String queryStr, Integer start, Integer rowLength, boolean isHighlight, String queryModel) throws Exception {
		SolrQuery query = new SolrQuery();
		if (isHighlight) {
			SolrJUtils.setHighlightConfig(query, new String[] { "title", "ocrtext"}, "<font color='red'>", "</font>", 1, 10000);
		}
		setQueryOrConfig(query, clazz, queryStr, queryModel);
		query.setStart(start);
		query.setRows(rowLength);
		query.setFacet(true);
		query.addFacetField("author_s");

		QueryResponse response = solr.query(query);
		List<T> list = response.getBeans(clazz);
		if (isHighlight) {
			queryHighlight(list, response);
		}
		return list;
	}

	/**
	 * 制定字段数组都包含同一个元素
	 *
	 * @param query
	 * @param strArray
	 * @param queryStr
	 */
	public static SolrQuery setQueryAndConfig(SolrQuery query, String[] strArray, String queryStr) {
		for (String str : strArray) {
			query.addFilterQuery(str + ":" + queryStr);
		}
		return query;
	}

	/**
	 * 设置排序
	 * @param query
	 * @param field	排序字段
	 * @param order 排序方式，正序、倒序
	 */
	public static void setSortQuery(SolrQuery query, String field, ORDER order) {
		query.addSort(field, order);
//		SolrQuery.ORDER.asc;
	}

	/**
	 * 一体化检索query设置 or查询
	 *
	 * @param query
	 * @param clazz
	 * @param queryStr
	 * @param queryModel
	 * @param iType
	 */
	public static SolrQuery setQueryOrConfig(SolrQuery query, Class clazz, String queryString, String queryModel,
			String iType, String iType2) {
		Field[] fields = Util.getAllField(clazz);
		StringBuffer sb = new StringBuffer();
		int strNum = 1;
		if (Util.notNull(queryString)) {
			String[] strArr = queryString.split(",");
			for (String queryStr : strArr) {
				if ("full".equalsIgnoreCase(queryModel)) {
					queryStr = "\"" + queryStr + "\"";
				} else {
					queryStr = transformSolrMetacharactor(queryStr);
				}
				sb.append("(");
				int count = 1;
				String[] strs = new String[] { "title", "iType", "sDH", "iType2", "sBZ", "sBGQX", "ocrTxt" };
				for (String field : strs) {
					sb.append(field + ":" + queryStr);
					if (count < strs.length) {
						sb.append(" OR ");
					}
					count++;
				}
				sb.append(")");
				if (strNum < strArr.length) {
					sb.append(" AND ");
				}
				strNum++;
			}
		} else {
			sb.append("*:*");
		}

		query.setQuery(sb.toString());
		if (Util.notNull(iType)) {
			query.addFilterQuery("iType:" + iType);
		}
		if (Util.notNull(iType2)) {
			query.addFilterQuery("iType2:" + iType2);
		}
//		query.setFilterQueries(iType2);
		return query;
	}

	/**
	 * 一体化检索query设置
	 * or查询
	 * @param query
	 * @param clazz
	 * @param queryStr
	 * @param queryModel
	 */
	public static <T> void setQueryOrConfig(SolrQuery query, Class<T> clazz, String queryString, String queryModel) {
        Field[] fields = Util.getAllField(clazz);
        StringBuffer sb = new StringBuffer();
        int strNum = 1;
        String[] strArr = queryString.split("\\+");
        for (String queryStr : strArr) {
        	if ("true".equalsIgnoreCase(queryModel)) {
        		queryStr = "\"" + queryStr + "\"";
			}
            sb.append("(");
            int count = 1;
            for (Field field : fields) {
                if (!"boolean".equals(field.getType().getName())) {
                    if ("java.lang.Integer".equals(field.getType().getName()) || "int".equals(field.getType().getName())) {
                        count++;
                        continue;
                    }
                    sb.append(field.getName() + ":" + queryStr);
                    if(count < fields.length) {
                        sb.append(" OR ");
                    }
                }
                count++;
            }
            sb.append(")") ;
            if(strNum < strArr.length) {
                sb.append(" AND ");
            }
            strNum++;
        }
        query.setQuery(sb.toString());
    }

	/**
	 * 一体化检索query设置
	 * @param query
	 * @param strs
	 * @param queryString
	 * @param queryModel
	 * @return
	 */
	public static SolrQuery setQueryOrConfig(SolrQuery query, String[] strs, String queryString, String queryModel) {
		StringBuffer sb = new StringBuffer();
		String[] strArr = queryString.split(",");
		for (int i = 0; i < strArr.length; i++) {
			String queryStr = strArr[i];
			if ("full".equalsIgnoreCase(queryModel)) {
				queryStr = "\"" + queryStr + "\"'";
			}
			sb.append("(");
			for (String str : strs) {
				sb.append(str + ":" + queryStr);
				sb.append(" OR ");
			}
			sb.append(")");
			if (i < strArr.length) {
				sb.append(" AND ");
			}
		}
		query.setQuery(sb.toString());
		return query;
	}

	/**
	 * 设置高亮
	 *
	 * @param query
	 * @param highlightField
	 * @param pre
	 * @param post
	 * @param snippets
	 * @param fragsize
	 */
	public static void setHighlightConfig(SolrQuery query, String[] highlightFields, String pre, String post,
			int snippets, int fragsize) {
		query.setHighlight(true); // 开启高亮组件
		for (String field : highlightFields) {
			query.addHighlightField(field);// 高亮字段
		}
		query.setHighlightSimplePre(pre);// 标记，高亮关键字前缀"<font color='red'>"
		query.setHighlightSimplePost(post);// 后缀 "</font>"
		query.setHighlightSnippets(snippets);//结果分片数，默认为1
		query.setHighlightFragsize(fragsize);//每个分片的最大长度，默认为100
	}

	/**
	 * 断开服务器
	 *
	 * @param solrClient
	 * @throws IOException
	 */
	public void close(SolrClient solrClient) throws IOException {
		if (solrClient != null) {
			solrClient.close();
			solrClient = null;
		}
	}

	/**
     * solr 转义字符方法，防止档号中 -等字符无法被搜索
     * @param str
     * @return
     */
	public static String transformSolrMetacharactor(String str) {
		StringBuffer sb = new StringBuffer();
		try {
			String regex = "[+\\-&|!(){}\\[\\]^\"~*?:(\\)\\s]";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(str);
			while (matcher.find()) {
				matcher.appendReplacement(sb, "\\\\" + matcher.group());
			}
			matcher.appendTail(sb);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
