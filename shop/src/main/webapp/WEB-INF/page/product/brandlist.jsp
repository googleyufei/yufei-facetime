<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/page/share/taglib.jsp" %>
<html>
<head>
<title>产品品牌显示</title>
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
<form action="/shop/control/product/brand/list.do" method="post">
<input type="hidden" name="page"/>
<input type="hidden" name="query"/>
<input type="hidden" name="name"/>
  <table width="98%" border="0" cellspacing="1" cellpadding="2" align="center">
    <tr ><td colspan="4" bgcolor="6f8ac4" align="right">
    	<%@ include file="/WEB-INF/page/share/fenye.jsp" %>
   </td></tr>
    <tr>
      <td width="30%" bgcolor="6f8ac4"> <div align="center"><font color="#FFFFFF">代号</font></div></td>
      <td width="5%" nowrap bgcolor="6f8ac4"> <div align="center"><font color="#FFFFFF">修改</font></div></td>
      <td width="35%" bgcolor="6f8ac4"> <div align="center"><font color="#FFFFFF">名称</font></div></td>
	  <td width="30%" nowrap bgcolor="6f8ac4"> <div align="center"><font color="#FFFFFF">Logo</font></div></td>
    </tr>
<!---------------------------LOOP START------------------------------>
<c:forEach items="${pageView.records}" var="entry">
    <tr>
      <td bgcolor="f5f5f5"> <div align="center">${entry.code}</div></td>
      <td bgcolor="f5f5f5" align="center"> 
       <itcast:permission privilege="update" module="brand">
        <a href="/shop/control/product/brand/editUI.do?code=${entry.code}"><img src="/shop/images/edit.gif" width="15" height="16" border="0"></a>
	   </itcast:permission></td>
      <td bgcolor="f5f5f5"> <div align="center">${entry.name}</div></td>
	  <td bgcolor="f5f5f5"> <div align="center"><c:if test="${empty entry.logopath}">没有logo</c:if>
	  <c:if test="${!empty entry.logopath}"><img src="/shop${entry.logopath}" width="40"></c:if></div></td>
	</tr>
</c:forEach>
    <!----------------------LOOP END------------------------------->
    <tr> 
      <td bgcolor="f5f5f5" colspan="4" align="center"><table width="100%" border="0" cellspacing="1" cellpadding="3">
          <tr> 
            <td width="5%"></td>
              <td width="85%">
              <itcast:permission privilege="insert" module="brand">
              <input type="button" class="frm_btn" onClick="javascript:window.location.href='/shop/control/product/brand/addUI.do'" value="添加品牌"> &nbsp;&nbsp;
              </itcast:permission>
              <itcast:permission privilege="view" module="brand">
			  <input name="query" type="button" class="frm_btn" id="query" onClick="javascript:window.location.href='/shop/control/product/brand/queryUI.do'" value=" 查 询 "> &nbsp;&nbsp;
			  </itcast:permission>
            </td>
          </tr>
        </table></td>
    </tr>
  </table>
</form>
</body>
</html>