<%@page import="com.facetime.mgr.bean.UserModel" %>
<%	
	if(session.getAttribute(UserModel.LOGIN_USER_KEY) != null){
		session.removeAttribute(UserModel.LOGIN_USER_KEY);
	}
    session.invalidate();
    response.sendRedirect(request.getContextPath()+"/indexShow.jsp");
%>