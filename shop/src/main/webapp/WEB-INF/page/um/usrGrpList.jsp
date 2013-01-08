<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<html>
<head><title>user group</title>
<link href="/shop/css/css_v2.css" rel="stylesheet" type="text/css">
</head>
<script language="javascript" src="/shop/js/tablesort.js"></script>
<script language="javascript" src="../../calendar/fixDate.js"></script>
<script language="JavaScript" type="text/javascript">	
function saveGrprole(){
	grpRoleForm.action="<%=request.getContextPath()%>/pages/um/usrGrp.do?action=modify";
	grpRoleForm.submit();
}

function GotoBack(){
	grpRoleForm.action="<%=request.getContextPath()%>/pages/um/operInfo.do";
	grpRoleForm.submit();
}
</script>
<body id="bodyid" leftmargin="10" topmargin="10">
<form name="grpRoleForm">
<table width="100%" height="20" border="0" cellspacing="0" cellpadding="0" background="/shop/images/main/bgtop.gif">
<tr>
	<td width="3%" valign="middle">
		&nbsp;<img src="/shop/images/share/list.gif" width="14" height="16">
	</td>
	<td class="orarowhead"><spring:message code="roleinfo.grproleRelation" /></td>
	<td width="40%" align="right">
		<input type="button" name="save" value='<spring:message code="button.save"/>' class="MyButton" AccessKey="S" onClick="saveGrprole()" image="/shop/images/share/save.gif">
		<input type="button" name="btnReturn" value='<spring:message code="button.close"/>' class="MyButton" AccessKey="R" onClick="window.close()" image="/shop/images/share/f_closed.gif">
	</td>
</tr>
</table>
<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066">
<tr class="oracolumncenterheader">
	<td width="30%"><spring:message code="label.select"/></td>
	<td width="70%"><spring:message code="grpInfoForm.grpname" /></td>
</tr>
<logic:iterate id="usrGrp" name="usrGrpLst" indexId="index">
<input type="hidden" name="userid" value='${usrGrp.userid}'>
<tr class="trClass<%=(index.intValue()%2)%>"  oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
	<td align="center">
		<input type="checkbox" name="checkbox" value="${usrGrp" property="grpcode"/>" <bean:write name="usrGrp.checked}>
	</td>
	<td>${usrGrp.grpname}</td>
</tr>
</logic:iterate>
<%
	if (request.getAttribute("rowNum")!=null){
		int rownum = Integer.parseInt((String)request.getAttribute("rowNum"));
		if (rownum!=0){
			for(int i=rownum;i<10;i++){
%>	
<tr id="rownum<%=i%>" class="trClass<%=(i%2)%>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
	<td height="20"></td>
	<td></td>
</tr>		
<%
	}
	}
}
%>
</table>
</form>	
</body>
</html>