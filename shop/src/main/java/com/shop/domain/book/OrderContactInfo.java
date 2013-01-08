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
 * 订购者联系信息
 * 
 */
@Entity
public class OrderContactInfo implements BusinessObject {
	private static final long serialVersionUID = 4808757136933761357L;
	private Integer contactid;
	/* 购买人姓名 */
	private String buyerName;
	/* 联系地址 */
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
	/* 所属订单 */
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
		final OrderContactInfo other = (OrderContactInfo) obj;
		if (contactid == null) {
			if (other.contactid != null) {
				return false;
			}
		} else if (!contactid.equals(other.contactid)) {
			return false;
		}
		return true;
	}

	@Column(length = 40, nullable = false)
	public String getAddress() {
		return address;
	}

	@Column(length = 8, nullable = false)
	public String getBuyerName() {
		return buyerName;
	}

	@Id
	@GeneratedValue
	public Integer getContactid() {
		return contactid;
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

	@OneToOne(cascade = CascadeType.REFRESH, mappedBy = "orderContactInfo")
	public Order getOrder() {
		return order;
	}

	@Column(length = 6)
	public String getPostalcode() {
		return postalcode;
	}

	@Column(length = 18)
	public String getTel() {
		return tel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (contactid == null ? 0 : contactid.hashCode());
		return result;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public void setContactid(Integer contactid) {
		this.contactid = contactid;
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

	public void setTel(String tel) {
		this.tel = tel;
	}

}
