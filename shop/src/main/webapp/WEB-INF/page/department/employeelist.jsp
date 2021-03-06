<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/page/share/taglib.jsp" %>
<html>
<head>
<title>员工显示</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="/shop/css/vip.css" type="text/css">
<script language="JavaScript">
<!--
	//到指定的分页页面
	function topage(page){
		var form = document.forms[0];
		form.page.value=page;
		form.submit();
	}
//-->
</script>
<SCRIPT language=JavaScript src="/shop/js/FoshanRen.js"></SCRIPT>
</head>

<body bgcolor="#FFFFFF" text="#000000" marginwidth="0" marginheight="0">
<form action="/shop/control/employee/list.do" method="post">
<input type="hidden" name="page"/>
  <table width="98%" border="0" cellspacing="1" cellpadding="2" align="center">
    <tr ><td colspan="11" bgcolor="6f8ac4" align="right">
    	<%@ include file="/WEB-INF/page/share/fenye.jsp" %>
   </td></tr>
    <tr>
      <td width="5%" nowrap bgcolor="6f8ac4"> <div align="center"><font color="#FFFFFF">修改</font></div></td>
      <td width="10%" bgcolor="6f8ac4"> <div align="center"><font color="#FFFFFF">用户名</font></div></td>
      <td width="8%" bgcolor="6f8ac4"> <div align="center"><font color="#FFFFFF">设置权限</font></div></td>
      <td width="10%" bgcolor="6f8ac4"> <div align="center"><font color="#FFFFFF">姓名</font></div></td>
      <td width="5%" bgcolor="6f8ac4"> <div align="center"><font color="#FFFFFF">性别</font></div></td>
      <td width="12%" bgcolor="6f8ac4"> <div align="center"><font color="#FFFFFF">联系电话</font></div></td>
      <td width="10%" bgcolor="6f8ac4"> <div align="center"><font color="#FFFFFF">电子邮件</font></div></td>
      <td width="10%" bgcolor="6f8ac4"> <div align="center"><font color="#FFFFFF">身份证号</font></div></td>
      <td width="10%" bgcolor="6f8ac4"> <div align="center"><font color="#FFFFFF">照片</font></div></td>
      <td bgcolor="6f8ac4"><div align="center"><font color="#FFFFFF">所属部门</font></div></td>
      <td width="9%" bgcolor="6f8ac4"></td>
    </tr>
<!---------------------------LOOP START------------------------------>
<c:forEach items="${pageView.records}" var="entry">
    <tr>
      <td bgcolor="f5f5f5"> 
         <itcast:permission module="employee" privilege="update">
           <div align="center"><a href="/shop/control/employee/editUI.do?username=${entry.username}"><img src="/shop/images/edit.gif" width="15" height="16" border="0"></a></div>
         </itcast:permission></td>
	  <td bgcolor="f5f5f5"> <div align="center">${entry.username}</div></td>
      <td bgcolor="f5f5f5"> 
          <itcast:permission module="employee" privilege="privilegeGroupSet">
           <div align="center"><a href="/shop/control/employee/privilege/setUI.do?username=${entry.username}">设置权限</a></div>
          </itcast:permission></td>
      <td bgcolor="f5f5f5"> <div align="center">${entry.realname}</div></td>
      <td bgcolor="f5f5f5"> <div align="center">${entry.gender}</div></td>
      <td bgcolor="f5f5f5"> <div align="center">${entry.phone}</div></td>
      <td bgcolor="f5f5f5"> <div align="center">${entry.email}</div></td>
      <td bgcolor="f5f5f5"> <div align="center">${entry.cardno}</div></td>
      <td bgcolor="f5f5f5"> <div align="center"><c:if test="${!empty entry.imagePath}"><img src="/shop${entry.imagePath}" width="80" border="0"></c:if></div></td>
      <td bgcolor="f5f5f5"> <div align="center">${entry.department.name}</div></td>
      <td bgcolor="f5f5f5"> 
         <div align="center"><c:if test="${entry.visible}">
         <itcast:permission module="employee" privilege="leave">
         <a href="/shop/control/employee/leave.do?username=${entry.username}">标志为离职</a>
         </itcast:permission></c:if> 
         <c:if test="${!entry.visible}"><font color=red>已离职</font></c:if></div>
      </td>
	</tr>
</c:forEach>
    <!----------------------LOOP END------------------------------->
    <tr> 
      <td bgcolor="f5f5f5" colspan="11" align="center"><table width="100%" border="0" cellspacing="1" cellpadding="3">
          <tr> 
            <td width="5%"></td>
              <td width="85%">
              <itcast:permission module="employee" privilege="insert">
                <input type="button" class="frm_btn" onClick="javascript:window.location.href='/shop/control/employee/regUI.do'" value="添加员工"> &nbsp;&nbsp;
              </itcast:permission>
            </td>
          </tr>
        </table></td>
    </tr>
  </table>
</form>
</body>
</html>