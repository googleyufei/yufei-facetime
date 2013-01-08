
<%
/*
* JSPname     : message.jsp
* Description    :Show the information
* Author        : 
* Date          : 2002-07-24
*
* Last Update   :
* Author        :
* Version       : 1.0
*/
%>
<%@ taglib uri="struts-html" prefix="html"%>
<%@ taglib uri="struts-logic" prefix="logic" %>
<%@ taglib uri="struts-bean" prefix="bean" %>
<%@ page import=" cn.grgbanking.phosphorframework.webapp.struts.action.WebConstants,
                  cn.grgbanking.phosphorframework.util.ErrorObject" %>
<%@ page contentType="text/html; charset=gb2312" %>

<html>
<head>
<title>
????
</title>
<META http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="/shop/css/global.css">
</head>
<%
    String currentPath = request.getContextPath();
%>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="100">
<table width="395" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr> 
    <td>
      <table width="375" border="0" align="center" cellpadding="0" cellspacing="0">
        <tr> 
          <td background="<%=currentPath%>/images/xxts2.gif" valign="top"><img src="<%=currentPath%>/images/xxts1.gif" width="195" height="45"></td>
        </tr>
        <tr> 
          <td bgcolor="C6D7FF" height="8"><img src="<%=currentPath%>/images/Spacer.gif" width="1" height="1"></td>
        </tr>

				 <logic:present name="<%=WebConstants.ERROR_MESSAGE%>">
                     <tr>
                     <td bgcolor="EFF3FF" height="120">
                     <font color="#669966">
					     ${<%=WebConstants.ERROR_MESSAGE%>}
	         		 </font>
                     </td>
                     </tr>
				 </logic:present>

            <logic:present name= "<%=WebConstants.ERROR_OBJECT%>">
            <tr>
               <td>&nbsp;</td>
            </tr>
            <tr>
                <td bgcolor="EFF3FF" >
                 <font color="darkred">发生系统错误,详细信息请联系系统管理员查询系统日志
                 </font>
                </td>
            </tr>
            <tr>
               <td bgcolor="EFF3FF" >

                    [错误序号]<font color="#339900">
                    ${<%=WebConstants.ERROR_OBJECT%>.errorCode}
                    </font>
                </td>
            </tr>
            <tr>
               <td bgcolor="EFF3FF" >

                    [简要信息]: <font color="#339900">
                    ${<%=WebConstants.ERROR_OBJECT%>.errorMsg}
                    </font>
               </td>
            </tr>
            <tr>
               <td bgcolor="EFF3FF" >

                  [请求]: <font color="#339900">
                    ${<%=WebConstants.ERROR_OBJECT%>.requestURL}
                  </font>
               </td>
            </tr>
            <tr>
               <td bgcolor="EFF3FF" >
                  <font color="#339900">
                   <a href= "#" onclick="document.getElementById('detail').style.visibility = 'visible'">+详细信息+</a>:
                  </font>
               </td>
            </tr>
            <tr>
              <td height="120">
              <div id = "detail"  style="height:120;visibility:hidden;overflow:scroll">
                   ${<%=WebConstants.ERROR_OBJECT%>.errorStack}
               </div>
               </td>
            </tr>
        </logic:present>
        <tr> 
          <td bgcolor="EFF3FF" height="20" align="center">
          <font color="#669966">
			<logic:present name="<%=WebConstants.LINK_URL%>">
				 <a href="<%=request.getAttribute(WebConstants.LINK_URL).toString()%>">
				   <logic:present name="<%=WebConstants.LINK_NAME%>">
				   ${<%=WebConstants.LINK_NAME%>}
				   </logic:present>
				   <logic:notPresent name="<%=WebConstants.LINK_NAME%>">
					  返回
				   </logic:notPresent>
				   </font>
				 </a>
			</logic:present>
		  </td>
        </tr>


        <tr> 
          <td bgcolor="C6D7FF" height="15"><img src="<%=currentPath%>/images/Spacer.gif" width="1" height="1"></td>
        </tr>
        <tr>
          <td height="10"><img src="<%=currentPath%>/images/Spacer.gif" width="1" height="1"></td>
        </tr>
      </table>
    </td>
  </tr>
</table>



</body>
</html>

