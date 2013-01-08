<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<html>
<head><title></title></head>
<link href="/shop/css/css_v2.css" type="text/css" rel="stylesheet">
<script language="javascript" src="/shop/js/tablesort.js"></script>
<script language="javascript" type="text/javascript">
function setMenuLeafInfo(id){
	parent.returnValue = id
	window.close()
}
</script>
<body id="bodyid" leftmargin="5" topmargin="5">
<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066">
<tr class="oracolumncenterheader"> 
	<td width="8%"><spring:message code="label.select"/></td>
    <td width="26%"><spring:message code="menuInfoForm.menuname" bundle="menu"/></td>
    <td width="66%"><spring:message code="menuInfoForm.setorder" bundle="menu"/></td>
</tr>
<tr class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)> 
	<td align="center"> 
		<input type="radio" name="chkList" value="" onClick="setMenuLeafInfo(this.value)">
    </td>
    <td><spring:message code="operInfoform.mpage" /></td>
    <td></td>
</tr> 
<logic:iterate id="menuList" name="menuLeafInfoList" indexId="index"> 
<tr class="trClass<%=(index.intValue()%2)%>"  oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)> 
	<td align="center"> 
		<input type="radio" name="chkList" value='${menuList" property="actionto"/>,<bean:write name="menuList.menuid}' onClick="setMenuLeafInfo(this.value)">
    </td>
    <td>${menuList.menuitem}</td>
    <td>${menuList.actionto}</td>
</tr>
</logic:iterate>
</table>
</body>
</html>