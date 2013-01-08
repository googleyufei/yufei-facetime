package com.shop.domain.user;

import com.facetime.core.bean.BusinessObject;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Buyer implements BusinessObject {
	private static final long serialVersionUID = 8394979715028899027L;
	/** 用户名 **/
	private String username;// 只允许字母/数字/下划线
	/** 密码 **/
	private String password;// 采用MD5加密
	/** 真实姓名 **/
	private String realname;
	/** 电子邮箱 **/
	private String email;
	/** 性别 **/
	private Gender gender = Gender.MAN;
	/** 联系信息 **/
	private ContactInfo contactInfo;
	/** 是否启用 **/
	private Boolean visible = true;
	/** 注册时间 **/
	private Date regTime = new Date();

	public Buyer() {
	}

	public Buyer(String username) {
		this.username = username;
	}

	public Buyer(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public Buyer(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
	}

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
		final Buyer other = (Buyer) obj;
		if (username == null) {
			if (other.username != null) {
				return false;
			}
		} else if (!username.equals(other.username)) {
			return false;
		}
		return true;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "contactid")
	public ContactInfo getContactInfo() {
		return contactInfo;
	}

	@Column(length = 50, nullable = false)
	public String getEmail() {
		return email;
	}

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 5)
	public Gender getGender() {
		return gender;
	}

	@Column(length = 32, nullable = false)
	public String getPassword() {
		return password;
	}

	@Column(length = 8)
	public String getRealname() {
		return realname;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getRegTime() {
		return regTime;
	}

	@Id
	@Column(length = 18)
	public String getUsername() {
		return username;
	}

	@Column(nullable = false)
	public Boolean getVisible() {
		return visible;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (username == null ? 0 : username.hashCode());
		return result;
	}

	public void setContactInfo(ContactInfo contactInfo) {
		this.contactInfo = contactInfo;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

}
