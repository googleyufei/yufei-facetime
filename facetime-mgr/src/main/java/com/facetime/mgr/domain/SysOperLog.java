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
public class SysOperLog implements BusinessObject {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;

	private String hostip;

	private Date logintime;

	private Date logtime;

	private Long longTime;

	private String menuid;

	private String note;

	private String operid;

	private String parentId;

	private String result;

	private String userid;

	private String username;

	/** default constructor */
	public SysOperLog() {
	}

	/** minimal constructor */
	public SysOperLog(String userid, Date logtime, String menuid, String operid, String result) {
		this.userid = userid;
		this.logtime = logtime;
		this.menuid = menuid;
		this.operid = operid;
		this.result = result;
	}

	/** full constructor */
	public SysOperLog(String userid, String username, Date logtime, String menuid, String operid, String result,
			String note) {
		this.userid = userid;
		this.username = username;
		this.logtime = logtime;
		this.menuid = menuid;
		this.operid = operid;
		this.result = result;
		this.note = note;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof SysOperLog)) {
			return false;
		}
		SysOperLog castOther = (SysOperLog) other;
		return new EqualsBuilder().append(getId(), castOther.getId()).isEquals();
	}

	public String getHostip() {
		return hostip;
	}

	public String getId() {
		return id;
	}

	public Date getLogintime() {
		return logintime;
	}

	public Date getLogtime() {
		return logtime;
	}

	public Long getLongTime() {
		return longTime;
	}

	public String getMenuid() {
		return menuid;
	}

	public String getNote() {
		return note;
	}

	public String getOperid() {
		return operid;
	}

	public String getParentId() {
		return parentId;
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
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

	public void setHostip(String hostip) {
		this.hostip = hostip;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLogintime(Date logintime) {
		this.logintime = logintime;
	}

	public void setLogtime(Date logtime) {
		this.logtime = logtime;
	}

	public void setLongTime(Long longTime) {
		this.longTime = longTime;
	}

	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setOperid(String operid) {
		this.operid = operid;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
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
		return new ToStringBuilder(this).append("id", getId()).toString();
	}

}
