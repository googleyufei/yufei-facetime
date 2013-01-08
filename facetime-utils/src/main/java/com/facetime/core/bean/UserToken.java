package com.facetime.core.bean;

import com.facetime.core.utils.StringUtils;

/**
 * 用户认证令牌对象
 */
public class UserToken implements BusinessBean {

	private static final long serialVersionUID = 1239249104571832191L;
	// 令牌节分割符
	public static final String SEPARATOR = "@";
	// 用户ID
	private long userId;
	// 用户令牌, 默认是空字符串
	private String token = "";
	private String userToken = "";

	public UserToken() {
	}

	public UserToken(String userToken) {
		parse(userToken);
	}

	/**
	 * @param userToken
	 */
	private void parse(String userToken) {
		this.userToken = userToken;
		int post = userToken.indexOf(SEPARATOR);

		if (post > 0) {
			this.userId = Long.parseLong(userToken.substring(0, post));
			this.token = userToken.substring(post + 1);
		} else {
			this.userId = Long.parseLong(userToken);
		}
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public void setUserToken(String userToken) {
		parse(userToken);
	}

	public String getUserTokenStr() {
		return StringUtils.isValid(userToken) ? userToken : (userToken = (userId + SEPARATOR + token));
	}

	public String getUserIdStr() {
		return String.valueOf(userId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (userId ^ (userId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserToken other = (UserToken) obj;
		if (userId != other.userId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(" {\"userId\":\"").append(userId).append("\", \"token\":\"").append(token).append("\"}");
		return builder.toString();
	}

}
