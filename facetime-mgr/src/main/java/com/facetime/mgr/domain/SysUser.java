package com.facetime.mgr.domain;

import com.facetime.core.bean.BusinessObject;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
public class SysUser implements BusinessObject {

	private static final long serialVersionUID = 1L;
	@Id
	private String username;
	private Date createdate;
	/* 学历 10位 */
	private String degree;
	/* 毕业院校 20位 */
	private String school;
	/* 联系电话 20 */
	private String phone;
	/** 照片 41,只存放文件名称,而且文件名称采用uuid生成,图片保存在/images/employee/[username]/目录 */
	private String imageName;
	private String email;
	private String groupNames;
	/** 是否合法 */
	private boolean valid = true;
	private String levelflag;
	/** 性别 */
	private String gender;
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date logindate;
	private Integer loginfaile;
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifydate;
	private String pwdflag;
	private String roleNames;
	private String tel;
	private String typeflag;
	private String url;
	private String realname;
	private String password;
	/** 身份证 */
	private String cardno;
	/* 员工在职状态 true为在职,false为离职 */
	private Boolean visible = true;
	/* 员工所在部门 */
	private String departmentId;
	private String orgid;
	private String areaid;

	public String getAreaid() {
		return areaid;
	}

	public void setAreaid(String areaid) {
		this.areaid = areaid;
	}

	public String getOrgid() {
		return orgid;
	}

	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public String getEmail() {
		return email;
	}

	public String getGender() {
		return gender;
	}

	public String getGroupNames() {
		return groupNames;
	}

	public String getLevelflag() {
		return levelflag;
	}

	public Date getLogindate() {
		return logindate;
	}

	public Integer getLoginfaile() {
		return loginfaile;
	}

	public Date getModifydate() {
		return modifydate;
	}

	public String getPwdflag() {
		return pwdflag;
	}

	public String getRoleNames() {
		return roleNames;
	}

	public String getTel() {
		return tel;
	}

	public String getTypeflag() {
		return typeflag;
	}

	public String getUrl() {
		return url;
	}

	public String getUsername() {
		return username;
	}

	public Boolean getVisible() {
		return visible;
	}

	public boolean isValid() {
		return valid;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setGroupNames(String groupNames) {
		this.groupNames = groupNames;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public void setLevelflag(String levelflag) {
		this.levelflag = levelflag;
	}

	public void setLogindate(Date logindate) {
		this.logindate = logindate;
	}

	public void setLoginfaile(Integer loginfail) {
		loginfaile = loginfail;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getImageName() {
		return imageName;
	}

	@Transient
	public String getImagePath() {
		String imagePath = null;
		if (username != null && imageName != null)
			imagePath = "/images/employee/" + username + "/" + imageName;
		return imagePath;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public void setModifydate(Date modifydate) {
		this.modifydate = modifydate;
	}

	public void setPwdflag(String pwdflag) {
		this.pwdflag = pwdflag;
	}

	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public void setTypeflag(String typeflag) {
		this.typeflag = typeflag;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}
}
