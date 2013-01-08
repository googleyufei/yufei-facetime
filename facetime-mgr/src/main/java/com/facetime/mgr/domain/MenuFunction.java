package com.facetime.mgr.domain;

import com.facetime.core.bean.BusinessObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class MenuFunction implements BusinessObject {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String funcid;

	private String menuid;

	private String operid;

	/** default constructor */
	public MenuFunction() {
	}

	/** full constructor */
	public MenuFunction(String funcid, String menuid, String operid) {
		this.funcid = funcid;
		this.menuid = menuid;
		this.operid = operid;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof MenuFunction))
			return false;
		MenuFunction castOther = (MenuFunction) other;
		return new EqualsBuilder().append(getFuncid(), castOther.getFuncid()).isEquals();
	}

	public String getFuncid() {
		return funcid;
	}

	public String getMenuid() {
		return menuid;
	}

	public String getOperid() {
		return operid;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getFuncid()).toHashCode();
	}

	public void setFuncid(String funcid) {
		this.funcid = funcid;
	}

	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}

	public void setOperid(String operid) {
		this.operid = operid;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("funcid", getFuncid()).toString();
	}

}
