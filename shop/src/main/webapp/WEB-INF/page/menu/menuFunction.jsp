<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<html>
<head><title>addOrModify</title></head>
<link href="/shop/css/css_v2.css" type="text/css" rel="stylesheet">
<script language="javascript" src="/shop/js/tablesort.js"></script>
<script language="javascript">
function SubmitForm(formInfo) {
	window.returnValue=true
	return true
}
</script>
<body id="bodyid" leftmargin="5" topmargin="0" style="overflow-x:hidden">	
<form  action="/shop/pages/menu/saveOperate.do" method="post" onsubmit="return SubmitForm(this)">
<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066">
<tr>
	<td class="trClass1" width="31%" height="22" align="center">
		<font color="#990066"><spring:message code="menuInfo.title.setting" /> </font>
	</td>
	<td class="trClass1" width="69%" align="right">
		<input type="submit" name="btnSave" value='<spring:message code="button.save"/>  ' class="MyButton" accesskey="S" image="/shop/images/share/save.gif">
		<input type="button" name="btnClose" value='<spring:message code="button.close"/>  ' class="MyButton" AccessKey="C" image="/shop/images/share/f_closed.gif" onClick="window.close()">
	</td>
</tr>

<c:forEach items="${menuOperLst}" var="menuOper" varStatus="index" >
	<input type="hidden" name="menuid" value='${menuOper.menuid}'>
	<tr class="trClass${index.index%2}" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
	<td align="center">
		<c:if test="${empty menuOper.checked}">
			<input type="checkbox" name="checkbox" value='${menuOper.operid}'>
		</c:if>
		<c:if test="${not empty menuOper.checked}">
	    <input type="checkbox" name="checkbox" value='${menuOper.operid}' checked>
		</c:if>
	</td>
	<td>${menuOper.opername}</td>						
	</tr>
</c:forEach>
<c:forEach items="menuOperLst" var="menuOper" varStatus="index"></c:forEach>
</table>
</form>
</body>
</html>