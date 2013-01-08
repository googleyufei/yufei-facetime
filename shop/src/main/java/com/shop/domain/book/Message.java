package com.shop.domain.book;

import com.facetime.core.bean.BusinessObject;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 客服留言
 */
@Entity
public class Message implements BusinessObject {
	private static final long serialVersionUID = 536215059509476447L;
	private Integer id;
	/* 留言内容 */
	private String content;
	/* 留言时间 */
	private Date createtime = new Date();
	/* 留言的员工 */
	private String username;
	private Order order;

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Message other = (Message) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Column(length = 100, nullable = false)
	public String getContent() {
		return content;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getCreatetime() {
		return createtime;
	}

	@Id
	@GeneratedValue
	public Integer getId() {
		return id;
	}

	@ManyToOne(optional = false)
	@JoinColumn(name = "order_id")
	public Order getOrder() {
		return order;
	}

	@Column(length = 20, nullable = false)
	public String getUsername() {
		return username;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (id == null ? 0 : id.hashCode());
		return result;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
