<table width="100%" height="20" align="center" border="0" background="/shop/images/main/bgtop.gif" cellpadding="0" cellspacing="0">
     <tr align="center">
          <td width="25" valign="middle"><img src="/shop/images/share/sysmenu.gif" width="14" height="14"></td>
          <td width="*" align="left" valign="middle"><spring:message code="index.location"/>
                <a href="<%=request.getContextPath()%>/personpage.jsp"><spring:message code="index.mainpanel"/></a>
                <%
                  out.write(com.facetime.mgr.taglib.NavigationBar.getNavigation(pageContext));
                  String CURRENT_MENU_ITEM=(String)session.getAttribute(com.facetime.mgr.bean.UserModel.CURRENT_MENU_ITEM);
                %>
	  </td>
</table>