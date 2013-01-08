package com.shop.web.support;

import com.facetime.mgr.bean.UserModel;
import com.facetime.spring.logic.Logic;
import com.facetime.spring.support.QueryFilter;
import com.facetime.spring.support.SpringContextUtils;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.shop.action.privilege.Permission;
import com.shop.domain.privilege.EmployeePrivilegeGrp;
import com.shop.domain.privilege.PrivilegeGroup;
import com.shop.domain.privilege.SystemPrivilege;
import com.shop.domain.privilege.SystemPrivilegeGroup;
import com.shop.domain.privilege.SystemPrivilegePK;
import com.shop.util.SiteUrl;
import com.shop.util.WebUtil;

public class PrivilegeRequestInterceptor extends HandlerInterceptorAdapter {

	private static final Logger log = Logger.getLogger(PrivilegeRequestInterceptor.class);

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		super.afterCompletion(request, response, handler, ex);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		log.debug("request params: " + getJSONString(request.getParameterMap()));
		// like /shop/control/department/list.do
		String requestURI = WebUtil.getRequestURI(request);
		// like /control/department/list
		String reqMapping = requestURI.substring(5, requestURI.indexOf(".do"));
		Map<String, String[]> reqInfo = new LinkedHashMap<String, String[]>();
		reqInfo.put("req uri", new String[] { reqMapping });
		reqInfo.put("req class", new String[] { handler.getClass().getName() });
		Method targetMethod = getTargetMethod(handler, reqMapping);
		if (targetMethod != null)
			reqInfo.put("req method", new String[] { targetMethod.getName() + "(...)" });
		if (reqMapping.startsWith("/control/")) {// 只对办公平台中的action进行校验
			boolean validateResult = validate(request, targetMethod);
			reqInfo.put("permission validate:", new String[] { validateResult + "" });
			log.debug("request info: " + getJSONString(reqInfo));
			if (!validateResult) {// 没有权限的时候执行下面这段代码
				request.setAttribute("message", "您没有执行该操作的权限");
				request.setAttribute("urladdress", SiteUrl.readUrl("control.control.right"));
				request.getRequestDispatcher("/WEB-INF/page/share/message.jsp").forward(request, response);
				return false;
			}
			return true;
		}
		reqInfo.put("permission validate:", new String[] { "not need" });
		log.debug("request info: " + getJSONString(reqInfo));
		return true;
	}

	private String getJSONString(Map<String, String[]> paramMap) {
		StringBuilder param = new StringBuilder();
		param.append("\r\n{\r\n");
		for (Entry<String, String[]> entry : paramMap.entrySet()) {
			param.append("  ").append(entry.getKey()).append(": ");
			String[] values = entry.getValue();
			if (values != null && values.length > 1) {
				param.append("[");
				for (String value : values)
					param.append(value).append(",");
				param.delete(param.length() - 1, param.length());
				param.append("]").append(",\n");
			} else
				param.append(values == null ? null : values[0]).append(",\n");
		}
		if (param.lastIndexOf(",") != -1)
			param.replace(param.lastIndexOf(","), param.lastIndexOf(",") + 1, "").append("\n");
		param.append("}\r\n");
		return param.toString();
	}

	private Method getTargetMethod(Object handler, String reqMapping) {
		Method[] methods = handler.getClass().getDeclaredMethods();
		for (Method method : methods) {
			boolean isAnnoted = method.isAnnotationPresent(RequestMapping.class);
			RequestMapping mappingAnnoted = method.getAnnotation(RequestMapping.class);
			if (isAnnoted && reqMapping.equals(mappingAnnoted.value()[0]))
				return method;
		}
		return null;
	}

	/**
	 * 权限校验
	 *
	 * @return
	 */
	private boolean validate(HttpServletRequest request, Method targetMethod) {
		if (targetMethod == null || !targetMethod.isAnnotationPresent(Permission.class))
			return true;
		Permission permission = targetMethod.getAnnotation(Permission.class);// 得到方法上的注解
		// 下面是得到执行方法需要的权限
		SystemPrivilegePK targetPrivilege = new SystemPrivilegePK(permission.module(), permission.privilege());
		UserModel employee = WebUtil.getEmployee(request);
		Logic logic = SpringContextUtils.locate(Logic.class, "defaultLogic");
		List<String> groupIds = logic.findPart(EmployeePrivilegeGrp.class,
				new QueryFilter("employeeId", employee.getUsername()), new String[] { "privilegeGrpId" });
		List<PrivilegeGroup> groupList = logic.findByIds(PrivilegeGroup.class, groupIds.toArray(new String[] {}));
		for (PrivilegeGroup group : groupList) {
			List<String> idArray = logic.findPart(SystemPrivilegeGroup.class,
					new QueryFilter("groupId", group.getGroupid()), new String[] { "privilegeId" });
			List<SystemPrivilege> privilegeList = logic.findByIds(SystemPrivilege.class,
					idArray.toArray(new String[] {}));
			for (SystemPrivilege privilege : privilegeList)
				if (privilege.getPermission().equals(targetPrivilege))
					return true;
		}
		return false;
	}
}
