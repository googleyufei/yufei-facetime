package com.facetime.mgr.domain;

import com.facetime.core.bean.BusinessObject;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class IDCard implements BusinessObject {
	private static final long serialVersionUID = -6933379853947708576L;
	@Id
	@Column(length = 18, nullable = false)
	/* 18 ,不能为null */
	private String cardno;
	/* 50 不能为null */
	private String address;
	/* 出生日期 */
	private Date birthday;// 采用只含有日期部分的类型表示
	/* 所属的员工 */
	private String userid;

	public IDCard() {
	}

	public IDCard(String cardno, String address, Date birthday) {
		this.cardno = cardno;
		this.address = address;
		this.birthday = birthday;
	}

	@Column(length = 40, nullable = false)
	public String getAddress() {
		return address;
	}

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getBirthday() {
		return birthday;
	}

	public String getCardno() {
		return cardno;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
}
