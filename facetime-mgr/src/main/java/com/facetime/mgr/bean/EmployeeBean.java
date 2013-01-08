package com.facetime.mgr.bean;

import java.util.Date;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class EmployeeBean extends BaseBean {
	private static final long serialVersionUID = 1404624601823029490L;

	private String username;
	private String password;
	private String realname;
	private String gender;
	private CommonsMultipartFile picture;
	private String cardno;
	private Date birthday;
	private String address;
	private String phone;
	private String email;
	private String degree;
	private String school;
	private String departmentid;
	private String[] groupids;
	private String imageName;
	private String imagePath;

	public String getAddress() {
		return address;
	}

	public Date getBirthday() {
		return birthday;
	}

	public String getCardno() {
		return cardno;
	}

	public String getDegree() {
		return degree;
	}

	public String getDepartmentid() {
		return departmentid;
	}

	public String getEmail() {
		return email;
	}

	public String[] getGroupids() {
		return groupids;
	}

	public String getImageName() {
		return imageName;
	}

	public String getImagePath() {
		return imagePath;
	}

	public String getPassword() {
		return password;
	}

	public String getPhone() {
		return phone;
	}

	public CommonsMultipartFile getPicture() {
		return picture;
	}

	public String getRealname() {
		return realname;
	}

	public String getSchool() {
		return school;
	}

	public String getUsername() {
		return username;
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

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public void setDepartmentid(String departmentid) {
		this.departmentid = departmentid;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setGroupids(String[] groupids) {
		this.groupids = groupids;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setPicture(CommonsMultipartFile picture) {
		this.picture = picture;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
