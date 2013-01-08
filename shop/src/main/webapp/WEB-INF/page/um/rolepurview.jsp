<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/page/share/taglib.jsp"%>
<html>
<head><title>role purview</title></head>
<link href="/shop/css/css_v2.css" type=text/css rel=stylesheet>
<script language="javascript" src="/shop/js/tablesort.js"></script>
<script language="javascript" src="/shop/js/calendar/fixDate.js"></script>
<script language="JavaScript">
function checkline(num) {
	var em = roleInfoForm.elements;
	var name = "checkbox"+num;
	var linename = "check"+num
	for (var i=0;i<em.length;i++){
		if (em[i].name==name){
		em[i].checked=document.getElementById(linename).checked;
		}
	}
}

function saveRoleFuncs(){
	roleInfoForm.action="/shop/pages/um/roleinfo/setRoleFunc.do";
	roleInfoForm.submit();
}

function gotoBack() {
	roleInfoForm.action="/shop/pages/um/roleinfo/list.do";
	roleInfoForm.submit();
}
</script>
<body id="bodyid" leftmargin="0" topmargin="0">
<form name="roleInfoForm" action="/shop/pages/um/roleinfo/setRoleFunc.do" method="post">
<input type="hidden" name="roleid" value="">
<input type="hidden" name="menuno" value='${menu.menuno}'>
<table width="100%" height="20" border="0" cellspacing="0" cellpadding="0" background="/shop/images/main/bgtop.gif">
<tr>
	<td width="3%" valign="middle">
		&nbsp;<img src="/shop/images/share/list.gif" width="14" height="16">
	</td>
	<td class="orarowhead"><spring:message code="roleInfo.table2title" /></td>
	<td width="40%" align="right">
		<input type="button" name="save" value='<spring:message code="roleInfo.save" />' class="MyButton" AccessKey="S" onClick="saveRoleFuncs()" image="/shop/images/share/yes1.gif">
		<input type="button" name="btnReturn" value="<spring:message code="button.return"/>" class="MyButton" AccessKey="R" onClick="gotoBack()" image="<%=request.getContextPath()%>/images/share/refu.gif">
	</td>
</tr>
</table>
<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066">
<tr class="oracolumncenterheader">
	<td width="4%"><spring:message code="label.select"/></td>
	<td width="18%"><spring:message code="roleinfo.rolefunc.parentModule" /></td>
	<td width="14%"><spring:message code="roleinfo.rolefunc.modulename" /></td>
	<td width="64%"><spring:message code="roleinfo.rolefunc.operLst" /></td>
</tr>
<c:forEach var="menufunc" items="${menuTree}" varStatus="index">
<input type="hidden" name="rolecode" value='${menufunc.roleid}'/>
<tr id="tr${menufunc.roleid}" class="trClass${index.index%2}"  oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)> 
	<td align="center"> 
		<input type="checkbox" name="check" id="check${index.index}" value="1" onClick="checkline(${index.index})"/>
	</td>
	<td>${menufunc.parentMenu}</td>
	<td>${menufunc.menunname}</td>
	<td>
		<c:forEach var="oper" items="${menufunc.operLst}">
		 <label><input type="checkbox" name="checkbox${index.index}" value="${oper.funcid}" ${oper.checked} />${oper.opername}</label>
		</c:forEach>
	</td>
</tr>
<c:if test="${index.last}">
  <input type="hidden" name="count" value="${index.count}">
</c:if>
</c:forEach>			
</table>
</form>
</body>	
</html>