package com.facetime.mgr.logic;

import com.facetime.mgr.bean.EmployeeBean;
import com.facetime.mgr.bean.OperInfoForm;
import com.facetime.mgr.bean.UserModel;
import com.facetime.mgr.domain.MenuFunction;
import com.facetime.mgr.domain.MenuInfo;
import com.facetime.mgr.domain.MenuOperate;
import com.facetime.mgr.domain.SysUser;
import com.facetime.spring.logic.Logic;
import com.facetime.spring.support.Limitable.PageBy;
import com.facetime.spring.support.Page;

import java.util.List;
import java.util.Map;

/**
 * 登陆控制实现接口
 * 
 * @author arco
 * @version 4.0
 */
public interface LoginManager extends Logic {

	public void deleteUser(String[] usernames);

	public Page<SysUser> getOperInfo1(OperInfoForm form, int pageNum,
			int pageSize);

	public abstract Map<String, String> getSysUserMap();

	public int modifyPassword(String username, String oldPwd, String newPwd);

	public void resetPwd(String[] usernames);

	public void savePage(String username, String url);

	/**
	 * 解锁
	 */
	public void unlockPwd(String[] usernames);

	/**
	 * 删除指定ID的员工
	 */
	void delete(String... entityids);

	/**
	 * 通过将当前用户的Employee信息封装到EmployeeBean中
	 * 
	 * @param bean
	 */
	void fillBean(EmployeeBean bean);

	/**
	 * 根据员工条件查询员工
	 * 
	 * @param employeeBean
	 * @param bound
	 *            分页
	 */
	Page<SysUser> getEmployeeResult(EmployeeBean employeeBean, PageBy bound);

	void save(EmployeeBean bean);

	/**
	 * 更新指定的员工
	 * 
	 * @param employeeBean
	 */
	void update(EmployeeBean employeeBean);

	/**
	 * 检验该用户名和密码员工是否合法
	 * 
	 * @param username
	 * @param password
	 */
	boolean validate(String username, String password);

	/**
	 * 创建菜单项
	 * 
	 * @param menuid
	 *            菜单的首节点
	 * @param grps
	 *            用户所属组别
	 * @param surl
	 *            网站发布目录地址
	 */
	public String createTree(String menuid, String[] grps, String surl);

	/**
	 * // 菜单功能操作menuid所对应的用户ID所具有的操作ID
	 */
	public Map<String, List<MenuOperate>> findMenuFuncMap(String userid);

	public List<MenuFunction> findMenuFunction(String userid);

	/**
	 * 获取叶节点
	 * 
	 * @param surl
	 *            网站发布目录地址
	 * @param leafMenu
	 *            叶节点集合
	 */
	public void getleafmenu(String menuid, String[] grps,
			List<MenuInfo> leafMenu);

	/**
	 * 根据用户组查询根目录菜单
	 */
	public List<MenuInfo> getMenubyGrp(String[] grps);

	public MenuInfo getMenuInfo(String menuid);

	/**
	 * 根据用户id查询用户组
	 */
	public String[] searchUsrGrp(String userid);

	/**
	 * 装备用户登录对象 设置登录用组信息 设置登录用户所属区域级别 设置用户的权限信息hashmap
	 */
	public boolean setupLoginUser(UserModel loginUser);

	public abstract void initAdmin();

}
