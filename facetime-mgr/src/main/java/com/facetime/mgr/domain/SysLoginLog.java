package com.facetime.mgr.domain;

import com.facetime.core.bean.BusinessObject;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class SysLoginLog implements BusinessObject {

	/***/
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String loginid;

	private String hostip;

	private Date logintime;

	private Date logouttime;

	private String note;

	private String result;

	private String userid;

	private String username;

	public SysLoginLog() {
	}

	public SysLoginLog(String loginid, String hostip, String userid, String username, Date logintime, Date logouttime,
			String result, String note) {
		super();
		// TODO 自动生成构造函数存根
		this.loginid = loginid;
		this.hostip = hostip;
		this.userid = userid;
		this.username = username;
		this.logintime = logintime;
		this.logouttime = logouttime;
		this.result = result;
		this.note = note;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof SysLoginLog)) {
			return false;
		}
		SysLoginLog castOther = (SysLoginLog) other;
		return new EqualsBuilder().append(getLoginid(), castOther.getLoginid())
				.append(getHostip(), castOther.getHostip()).append(getUserid(), castOther.getUserid())
				.append(getUsername(), castOther.getUsername()).append(getLogintime(), castOther.getLogintime())
				.append(getResult(), castOther.getResult()).append(getNote(), castOther.getNote()).isEquals();
	}

	public String getHostip() {
		return hostip;
	}

	public String getLoginid() {
		return loginid;
	}

	public Date getLogintime() {
		return logintime;
	}

	public Date getLogouttime() {
		return logouttime;
	}

	public String getNote() {
		return note;
	}

	public String getResult() {
		return result;
	}

	public String getUserid() {
		return userid;
	}

	public String getUsername() {
		return username;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getLoginid()).append(getHostip()).append(getUserid()).append(getUsername())
				.append(getLogintime()).append(getResult()).append(getNote()).toHashCode();
	}

	public void setHostip(String hostip) {
		this.hostip = hostip;
	}

	public void setLoginid(String loginid) {
		this.loginid = loginid;
	}

	public void setLogintime(Date logintime) {
		this.logintime = logintime;
	}

	public void setLogouttime(Date logouttime) {
		this.logouttime = logouttime;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("loginid", getLoginid()).append("hostip", getHostip())
				.append("userid", getUserid()).append("username", getUsername()).append("logintime", getLogintime())
				.append("logouttime", getLogouttime()).append("result", getResult()).append("note", getNote())
				.toString();
	}

}
