package com.facetime.mgr.bean;

import com.facetime.core.bean.BusinessBean;
import com.facetime.mgr.domain.MenuFunction;
import com.facetime.mgr.logic.MenuInfoManager;
import com.facetime.spring.support.SpringContextUtils;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 封装用户登录信息的对象
 */
public class UserModel implements BusinessBean {

	private static final long serialVersionUID = 1L;
	/** NavigationBar中把menuno对应的当前菜单项目保存到request中 */
	public static final String CURRENT_MENU_ITEM = "login.user.menu";
	/** 登录成功后，把用户信息通过LOGIN_USER_KEY保存到session中 */
	public static final String LOGIN_USER_KEY = "login.user.key";
	public static final String KEY_MENUNO = "menu.menuno";

	/** 用户唯一标示 */
	private String username;
	/** 用户名称 */
	private String realname;
	/** 密码剩余有效天数 */
	private int days = 90;
	/** 登录用户所属的群组 */
	private String[] groupids;
	/** 用户登录的IP */
	private String hostip;
	/** 上次登陆时间 */
	private Date logindate;
	/** 自上一次登陆成功以来登陆失败次数 */
	private Integer loginfaile;
	/** 登录后分配的连接id */
	private String loginid;
	/** 用户的登录时间 */
	private Date logintime;
	/** 用户的退出时间 */
	private Date logouttime;
	/** 用户权限集合 */
	private Map<String, String> operHash;
	/** 密码是否上传 */
	private String pwdflag;
	/** 用户登录主页 */
	private String url;
	private String orgid;
	private String areaid;

	public String getOrgid() {
		return orgid;
	}

	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}

	public String getAreaid() {
		return areaid;
	}

	public void setAreaid(String areaid) {
		this.areaid = areaid;
	}

	public int getDays() {
		return days;
	}

	/**
	 * @return Returns the groupids.
	 */
	public String[] getGroupids() {
		return groupids;
	}

	public String getHostip() {
		return hostip;
	}

	public Date getLogindate() {
		return logindate;
	}

	public Integer getLoginfaile() {
		return loginfaile;
	}

	/**
	 * @return Returns the loginid.
	 */
	public String getLoginid() {
		return loginid;
	}

	public Date getLogintime() {
		return logintime;
	}

	public Date getLogouttime() {
		return logouttime;
	}

	public Map<String, String> getOperHash() {
		return operHash;
	}

	public String getPwdflag() {
		return pwdflag;
	}

	public String getRealname() {
		return realname;
	}

	public String getUrl() {
		return url;
	}

	public String getUsername() {
		return username;
	}

	/**
	 * 判断当前用户是否有该菜单和功能点的权限
	 * @param menuid
	 * @param operid
	 * @return
	 */
	public boolean hasAuthorized(String menuid, String operid) {
		// 根据菜单与功能点找到对应的funcId
		MenuFunction function = SpringContextUtils.locate(MenuInfoManager.class).get(menuid, operid);
		String funcId = function != null ? function.getFuncid() : "";
		// 判断用户是否对funcId有权限
		if (operHash.containsKey(funcId)) {
			return true;
		}
		return false;
	}

	public void setDays(int days) {
		this.days = days;
	}

	/**
	 * @param groupids The groupids to set.
	 */
	public void setGroupids(String[] groupids) {
		this.groupids = groupids;
	}

	public void setHostip(String hostip) {
		this.hostip = hostip;
	}

	public void setLogindate(Date logindate) {
		this.logindate = logindate;
	}

	public void setLoginfaile(Integer loginfaile) {
		this.loginfaile = loginfaile;
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

	public void setOperHash(Map<String, String> operHash) {
		this.operHash = operHash;
	}

	public void setPwdflag(String pwdflag) {
		this.pwdflag = pwdflag;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 返回登录用户对象
	 * @param request
	 * @return
	 */
	public static final UserModel valueOf(HttpServletRequest request) {
		UserModel loginUser = (UserModel) request.getSession().getAttribute(LOGIN_USER_KEY);
		return loginUser;
	}
}
