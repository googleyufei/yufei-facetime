package com.shop.domain.user;

import com.facetime.core.bean.BusinessObject;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class ContactInfo implements BusinessObject {
	private static final long serialVersionUID = -4336182674133849896L;
	private Integer contactid;
	/** 地址 **/
	private String address;
	/** 邮编 **/
	private String postalcode;
	/** 座机 **/
	private String phone;
	/** 手机 **/
	private String mobile;
	/** 所属用户 **/
	private Buyer buyer;

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
		final ContactInfo other = (ContactInfo) obj;
		if (contactid == null) {
			if (other.contactid != null) {
				return false;
			}
		} else if (!contactid.equals(other.contactid)) {
			return false;
		}
		return true;
	}

	@Column(length = 100, nullable = false)
	public String getAddress() {
		return address;
	}

	@OneToOne(mappedBy = "contactInfo", cascade = CascadeType.REFRESH)
	public Buyer getBuyer() {
		return buyer;
	}

	@Id
	@GeneratedValue
	public Integer getContactid() {
		return contactid;
	}

	@Column(length = 11)
	public String getMobile() {
		return mobile;
	}

	@Column(length = 20)
	public String getPhone() {
		return phone;
	}

	@Column(length = 6)
	public String getPostalcode() {
		return postalcode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (contactid == null ? super.hashCode() : contactid.hashCode());
		return result;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setBuyer(Buyer buyer) {
		this.buyer = buyer;
	}

	public void setContactid(Integer contactid) {
		this.contactid = contactid;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setPostalcode(String postcode) {
		postalcode = postcode;
	}

}
