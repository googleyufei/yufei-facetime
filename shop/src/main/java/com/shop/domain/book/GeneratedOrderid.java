package com.shop.domain.book;

import com.facetime.core.bean.BusinessObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class GeneratedOrderid implements BusinessObject {
	private static final long serialVersionUID = 2467398331334473570L;
	private String id;
	private Integer orderid = 0;

	@Id
	@Column(length = 5)
	public String getId() {
		return id;
	}

	@Column(nullable = false)
	public Integer getOrderid() {
		return orderid;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setOrderid(Integer orderid) {
		this.orderid = orderid;
	}
}
