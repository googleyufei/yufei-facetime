package com.facetime.mgr.action;

import com.facetime.core.utils.DateUtil;
import com.facetime.mgr.bean.SysOperLogForm;
import com.facetime.mgr.bean.UserModel;
import com.facetime.mgr.common.BusnDataDir;
import com.facetime.mgr.common.MsgPage;
import com.facetime.mgr.domain.SysOperLog;
import com.facetime.mgr.logic.MenuInfoManager;
import com.facetime.mgr.logic.MenuOperateManager;
import com.facetime.mgr.logic.SysLoginLogManager;
import com.facetime.mgr.logic.SysOperLogManager;
import com.facetime.spring.action.Action;
import com.facetime.spring.support.Page;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SysOperLogAction extends Action {

	@Autowired
	private SysOperLogManager sysOperLogManager;

	@RequestMapping("/pages/sysoperlog/clear")
	public String clear(HttpServletRequest request) {
		sysOperLogManager.delAllOperLog();
		return MsgPage.view(request, "/shop/pages/sysoperlog/list.do", "clear.ok");
	}

	@RequestMapping("/pages/sysoperlog/delete")
	public String delete(HttpServletRequest request, SysOperLogForm sysOperLogForm) {
		String[] delItem = StringUtils.split(request.getParameter("idList"), ",");
		sysOperLogManager.delOperLog(delItem);
		return MsgPage.view(request, "/shop/pages/sysoperlog/list.do", "delete.ok");
	}

	@RequestMapping("/pages/sysoperlog/mainframe")
	public String mainframe(HttpServletRequest request) throws Exception {
		// 用于功能权限控制
		String menuno = request.getParameter("menuno");
		if (menuno == null) {
			menuno = (String) request.getSession().getAttribute(UserModel.KEY_MENUNO);
		}
		request.getSession().setAttribute(UserModel.KEY_MENUNO, menuno);
		return "logmgr/sysOperLogMgr";
	}

	@RequestMapping("/pages/sysoperlog/list")
	public String list(SysOperLogForm form, HttpServletRequest request) {
		// 默认查询一天内的操作日志
		if (form.getLogbegintime() == null && form.getLogendtime() == null) {
			form.setLogbegintime(DateUtil.dateToStr(DateUtil.dayAdd(new Date(), -1)));
			form.setLogendtime(DateUtil.dateToStr(new Date()));
		}
		Page<SysOperLog> currPage = sysOperLogManager.getPage(form);
		request.setAttribute("currPage", currPage);
		request.setAttribute("queryAll", currPage.getQueryResult());
		Map<String, String> map = locate(MenuOperateManager.class).getMenuOperateMap();
		request.getSession().setAttribute("menuOper.map", map);
		// 需要增加的空行数
		int rownum = currPage.getNeedRowNum();
		request.setAttribute("rowNum", new Integer(rownum));
		return "logmgr/sysOperLogList";
	}

	@RequestMapping("/pages/sysoperlog/write")
	public String write(SysOperLogForm form, HttpServletRequest request) throws Exception {
		String operid = request.getParameter("_operid");
		UserModel loginUser = (UserModel) request.getSession().getAttribute(UserModel.LOGIN_USER_KEY);
		SysOperLog log = new SysOperLog();
		log.setUserid(form.getLoginUserid(request));
		log.setUsername(loginUser.getUsername());
		log.setHostip(loginUser.getHostip());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(new Date());
		log.setLogtime(sdf.parse(time));
		log.setMenuid((String) request.getSession().getAttribute(UserModel.CURRENT_MENU_ITEM));
		log.setOperid(operid);
		log.setResult("Y");

		String operName = "";
		if (com.facetime.core.utils.StringUtils.isValid(operid)) {
			operName = BusnDataDir.getMapValue("BusnDataDir.operMap", operid).toString();
		}
		// 设置备注
		log.setNote((String) request.getSession().getAttribute(UserModel.CURRENT_MENU_ITEM) + " " + operName);
		// 一级菜单ID
		String parentId = (String) BusnDataDir.getMapValue("BusnDataDir.menuIdMap", log.getMenuid());
		if (parentId == null) {
			parentId = locate(MenuInfoManager.class).getMenuParentId(log.getMenuid());
			BusnDataDir.putMapValue("BusnDataDir.menuIdMap", log.getMenuid(), parentId);
		}

		log.setParentId(parentId);
		SysOperLog beforeOper = sysOperLogManager.getFristSysOperLog();
		// 先保存当有操作信息到数据库及session中,设置当前用户操作时长为0
		log.setLongTime(Long.parseLong("0"));
		// 设置用户的登录日期
		log.setLogintime(locate(SysLoginLogManager.class).getLonginDate(log.getUserid()));
		sysOperLogManager.save(log);
		updateBeforeLog(log, beforeOper);
		return null;
	}

	private void updateBeforeLog(SysOperLog log, SysOperLog beforeOper) {
		if (beforeOper == null) {
			return;
		}
		// 判断当前用户与历史用户是否一致
		if (log.getUserid().equals(beforeOper.getUserid())) {
			// 如果用户一致，判断登录时间是否一致,根据用户id去匹配上次登录用户，本次登录时间和上次登录时间作比较
			Date befUserLgDate = locate(SysLoginLogManager.class).getLonginDate(log.getUserid());
			Long longTiem = null;
			if (log.getLogintime() != null && null != befUserLgDate
					&& log.getLogintime().getTime() == befUserLgDate.getTime()) {
				// 如果登录日期相同，本次操作时间-上次操作时间 = 上次操作时长
				longTiem = (log.getLogtime().getTime() - beforeOper.getLogtime().getTime()) / 1000;
			} else {
				// 如果登录时间不同，上次登录用户要根据id去取得退出时间
				Long befUserLgOutDate = locate(SysLoginLogManager.class).getLongOutDate(beforeOper.getUserid());
				if (befUserLgOutDate.toString().contains("-")) {
					longTiem = -befUserLgOutDate / 1000;
				} else {
					// 退出时间 - 上次操作时间 = 时长
					longTiem = (befUserLgOutDate - beforeOper.getLogtime().getTime()) / 1000;
				}
			}
			beforeOper.setLongTime(longTiem);
			// 更新上次用户的操作时长
			sysOperLogManager.update(beforeOper);
		} else {
			Long longTiem = null;
			// 判断是否是第一条记录
			List<SysOperLog> list = sysOperLogManager.getListById(log.getUserid());
			// 如果用户不一致，需要去操作日志表获得和当前用户最近的一条记录
			SysOperLog nearSysOperLog = sysOperLogManager.getSysOperLogNear(log.getUserid());
			if (list.size() > 0) {
				// 判断当前用户的登录时间和获得时间是否一致
				if (log.getLogintime().getTime() == nearSysOperLog.getLogintime().getTime()) {
					// 如果时间相等 当前用户的操作时长 - 最近用户的操作时长 = 最近用户的操作时长
					longTiem = (log.getLogtime().getTime() - nearSysOperLog.getLogtime().getTime()) / 1000;
				} else {
					// 如果时间不相等 要根据离当前用户最近的用户的退出时间
					Long nearLogOutDate = locate(SysLoginLogManager.class).getLongOutDate(nearSysOperLog.getUserid());
					if (nearLogOutDate.toString().contains("-")) {
						longTiem = -nearLogOutDate / 1000;
					} else {
						// 退出时间 - 该用户的操作时间 = 该用户的操作时长
						longTiem = (nearLogOutDate - nearSysOperLog.getLogtime().getTime()) / 1000;
					}
				}
			} else {
				longTiem = Long.parseLong("0");
			}
			// 更新该用户的操作时长
			nearSysOperLog.setLongTime(longTiem);
			sysOperLogManager.update(nearSysOperLog);
		}
	}
}
