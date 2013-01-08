package com.shop.domain.book;

import com.facetime.core.bean.BusinessObject;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.shop.domain.user.Gender;

/**
 * 配送信息
 */
@Entity
public class OrderDeliverInfo implements BusinessObject {
	private static final long serialVersionUID = 1785022563297215991L;
	private Integer deliverid;
	/* 收货人姓名 */
	private String recipients;
	/* 配送地址 */
	private String address;
	/* 电子邮箱 */
	private String email;
	/* 邮编 */
	private String postalcode;
	/* 座机 */
	private String tel;
	/* 手机 */
	private String mobile;
	/* 性别 */
	private Gender gender = Gender.MAN;
	/* 送货方式 */
	private DeliverWay deliverWay;
	/* 时间要求 */
	private String requirement;
	/* 所属的订单 */
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
		final OrderDeliverInfo other = (OrderDeliverInfo) obj;
		if (deliverid == null) {
			if (other.deliverid != null) {
				return false;
			}
		} else if (!deliverid.equals(other.deliverid)) {
			return false;
		}
		return true;
	}

	@Column(length = 40, nullable = false)
	public String getAddress() {
		return address;
	}

	@Id
	@GeneratedValue
	public Integer getDeliverid() {
		return deliverid;
	}

	@Enumerated(EnumType.STRING)
	@Column(length = 23, nullable = false)
	public DeliverWay getDeliverWay() {
		return deliverWay;
	}

	@Column(length = 40)
	public String getEmail() {
		return email;
	}

	@Enumerated(EnumType.STRING)
	@Column(length = 5, nullable = false)
	public Gender getGender() {
		return gender;
	}

	@Column(length = 11)
	public String getMobile() {
		return mobile;
	}

	@OneToOne(mappedBy = "orderDeliverInfo", cascade = CascadeType.REFRESH)
	public Order getOrder() {
		return order;
	}

	@Column(length = 6)
	public String getPostalcode() {
		return postalcode;
	}

	@Column(length = 8, nullable = false)
	public String getRecipients() {
		return recipients;
	}

	@Column(length = 30)
	public String getRequirement() {
		return requirement;
	}

	@Column(length = 18)
	public String getTel() {
		return tel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (deliverid == null ? 0 : deliverid.hashCode());
		return result;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setDeliverid(Integer deliverid) {
		this.deliverid = deliverid;
	}

	public void setDeliverWay(DeliverWay deliverWay) {
		this.deliverWay = deliverWay;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}

	public void setRecipients(String recipients) {
		this.recipients = recipients;
	}

	public void setRequirement(String requirement) {
		this.requirement = requirement;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

}
