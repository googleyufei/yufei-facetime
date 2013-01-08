package com.facetime.mgr.taglib;

/**
 * <p>Description:根据当前功能菜单menuno生成响应的导航条和菜单信息</p>
 */
import static com.facetime.core.conf.SysLogger.facetimeLogger;

import com.facetime.mgr.bean.UserModel;
import com.facetime.mgr.common.BusnDataDir;
import com.facetime.mgr.domain.MenuInfo;
import com.facetime.mgr.logic.MenuInfoManager;
import com.facetime.spring.support.SpringContextUtils;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.StringEscapeUtils;

public class NavigationBar {
	private NavigationBar() {
	}

	/**
	 * 返回系统导航条 把当前菜单项目保存到session中:
	 * setAttribute(Constants.CURRENT_MENU_ITEM,menuInfo.getMenuitem());
	 *
	 * @param pageContext
	 * @return
	 * @throws javax.servlet.jsp.JspException
	 */
	public static final String getNavigation(PageContext pageContext) throws javax.servlet.jsp.JspException {
		// 菜单项之间的分隔箭头
		String midflag = SpringContextUtils.message("index.function.flag");
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String menuNo = request.getParameter("menuno");
		if (menuNo != null) {
			pageContext.getSession().setAttribute("menuno", menuNo);
		} else {
			menuNo = (String) pageContext.getSession().getAttribute("menuno");
		}

		MenuInfoManager menuInfoManager = null;
		try {
			menuInfoManager = SpringContextUtils.locate(MenuInfoManager.class);

			ArrayList<MenuInfo> aryPath = new ArrayList<MenuInfo>();
			MenuInfo menuInfo = menuInfoManager.findById(menuNo);
			while (menuInfo != null) {
				aryPath.add(menuInfo);
				if (menuInfo.getParentid().equals(BusnDataDir.TOP_PARENT_ID)) {
					break;
				}
				menuInfo = menuInfoManager.findById(menuInfo.getParentid());
			}
			int iSize = aryPath.size();
			if (iSize == 0) {
				request.setAttribute(UserModel.CURRENT_MENU_ITEM, "");
				return "";
			}

			String strPrix = "<a href='" + request.getContextPath() + "/Login.do?action=setMainPanel&menuid=";
			StringBuffer sbfPath = new StringBuffer();
			midflag = "  " + midflag + "  ";
			for (int i = iSize - 1; i > 0; i--) {
				sbfPath.append(midflag);
				menuInfo = aryPath.get(i);
				sbfPath.append(strPrix);
				sbfPath.append(menuInfo.getMenuid());
				sbfPath.append("'>");
				sbfPath.append(StringEscapeUtils.escapeHtml(menuInfo.getMenuitem()));
				sbfPath.append("</a>");
			}

			menuInfo = aryPath.get(0);
			sbfPath.append(midflag);
			sbfPath.append(StringEscapeUtils.escapeHtml(menuInfo.getMenuitem()));
			// 把menuno对应的菜单项目名保存到session，用于显示在页面上
			pageContext.getSession().setAttribute(UserModel.CURRENT_MENU_ITEM, menuInfo.getMenuitem());
			return sbfPath.toString();
		} catch (Exception ee) {
			facetimeLogger.error(ee);
			return "";
		} finally {
			menuInfoManager = null;
		}
	}
}