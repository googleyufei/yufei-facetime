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
public class FlowInfo implements BusinessObject {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;

	private String checkid;

	private Date createtime;

	private String flowflag;

	private String note;

	private Long process;

	private String stateflag;

	private int step;

	private String user;

	/** default constructor */
	public FlowInfo() {
	}

	/** minimal constructor */
	public FlowInfo(String flowflag, int step, String checkid, String stateflag, Date createtime) {
		this.flowflag = flowflag;
		this.step = step;
		this.checkid = checkid;
		this.stateflag = stateflag;
		this.createtime = createtime;
	}

	/** full constructor */
	public FlowInfo(String flowflag, int step, String checkid, String stateflag, Date createtime, String user,
			Long process, String note) {
		this.flowflag = flowflag;
		this.step = step;
		this.checkid = checkid;
		this.stateflag = stateflag;
		this.createtime = createtime;
		this.user = user;
		this.process = process;
		this.note = note;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof FlowInfo)) {
			return false;
		}
		FlowInfo castOther = (FlowInfo) other;
		return new EqualsBuilder().append(getId(), castOther.getId()).isEquals();
	}

	public String getCheckid() {
		return checkid;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public String getFlowflag() {
		return flowflag;
	}

	public String getId() {
		return id;
	}

	public String getNote() {
		return note;
	}

	public Long getProcess() {
		return process;
	}

	public String getStateflag() {
		return stateflag;
	}

	public int getStep() {
		return step;
	}

	public String getUser() {
		return user;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

	public void setCheckid(String checkid) {
		this.checkid = checkid;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public void setFlowflag(String flowflag) {
		this.flowflag = flowflag;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setProcess(Long process) {
		this.process = process;
	}

	public void setStateflag(String stateflag) {
		this.stateflag = stateflag;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("id", getId()).toString();
	}

}
