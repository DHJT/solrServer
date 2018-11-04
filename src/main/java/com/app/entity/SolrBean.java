package com.app.entity;

import java.util.Date;

import org.apache.solr.client.solrj.beans.Field;

/**
 * 建立索引的类和查询类
 * @author DHJT
 * @date: 2018年10月18日
 */
public class SolrBean {
	@Field
	private String id;
	@Field
	private String url;
	@Field
	private String title;
	@Field
	private String author;
	@Field
	private Date createtime;
	@Field
	private String type;
	@Field
	private int aid;
	@Field
	private String iscollect;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getAid() {
		return aid;
	}

	public void setAid(int aid) {
		this.aid = aid;
	}

	public String getIscollect() {
		return iscollect;
	}

	public void setIscollect(String iscollect) {
		this.iscollect = iscollect;
	}

}
