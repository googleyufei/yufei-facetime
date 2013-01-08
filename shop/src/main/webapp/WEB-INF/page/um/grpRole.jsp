<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/page/share/taglib.jsp"%>
<html>
<head><title>set group role</title></head>
<link href="/shop/css/css_v2.css" rel="stylesheet" type="text/css">
<script language="javascript" src="/shop/js/tablesort.js"></script>
<script language="javascript" src="../../calendar/fixDate.js"></script>
<script language="JavaScript">
function saveGrprole() {
	grpInfoForm.submit();
}

function returnBefore() {
	location.href="/shop/pages/um/grpinfo/list.do";
}
</script>
<body id="bodyid" leftmargin="0" topmargin="0">
<form name="grpInfoForm" action="/shop/pages/um/grpinfo/addGrpRole.do" method="post">
<input type="hidden" name="menuno" value='${menu.menuno}'>
<table width="100%" align="center" height="20" border="0" cellspacing="0" cellpadding="0" background="/shop/images/main/bgtop.gif">
<tr>
	<td width="3%" valign="middle">
		&nbsp;<img src="/shop/images/share/list.gif" width="14" height="16">
	</td>
	<td class="orarowhead"><spring:message code="roleInfo.table2title" /></td>
	<td width="40%" align="right">
		<input type="button" name="save" value='<spring:message code="button.save"/>' class="MyButton" AccessKey="S" onClick="saveGrprole()" image="/shop/images/share/save.gif">
		<input type="button" name="return" value='<spring:message code="button.return"/>' class="MyButton" AccessKey="T" onClick="returnBefore()" image="/shop/images/share/refu.gif">
	</td>
</tr>
</table>
<table width="100%" border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="#000066">
<tr class="oracolumncenterheader">
	<td width="10%"><spring:message code="label.select"/></td>
	<td width="90%"><spring:message code="roleInfoForm.roleName" /></td>					
</tr>
<c:forEach var="grpRole" items="${grpRoleLst}" varStatus="index">
<input type="hidden" name="grpcode" value='${grpRole.grpcode}'>
<tr class="trClass${index.index%2}" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)> 
	<td align="center">
		<input type="checkbox" name="checkbox" value="${grpRole.rolecode}" ${grpRole.checked}>
	</td>
	<td>${grpRole.rolename}</td>
</tr>
</c:forEach>
<c:forEach begin="0" end="${rowNum}" varStatus="index">
 <tr id="rownum${index.index}" class="trClass${index.index%2}" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
	<td height="20"></td>
	<td></td>
</tr>		
</c:forEach>
</table>
</form>
</body>
</html>