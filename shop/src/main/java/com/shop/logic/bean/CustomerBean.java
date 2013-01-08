package com.shop.logic.bean;


public class CustomerBean extends BaseBean {

	private static final long serialVersionUID = -2224170876901157941L;

	private String username;
	private String password;
	private String email;
	private String[] usernames;
	private String directUrl;
	private String realname;
	private String validateCode;

	public String getDirectUrl() {
		return directUrl;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getRealname() {
		return realname;
	}

	public String getUsername() {
		return username;
	}

	public String[] getUsernames() {
		return usernames;
	}

	public String getValidateCode() {
		return validateCode;
	}

	public void setDirectUrl(String directUrl) {
		this.directUrl = directUrl;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setUsernames(String[] usernames) {
		this.usernames = usernames;
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}

}
