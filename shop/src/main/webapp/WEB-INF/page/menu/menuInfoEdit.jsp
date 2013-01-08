<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/htc.inc"%>
<html>
<head><title>add Menu Info</title></head>
<link href="/shop/css/css_v2.css" type="text/css" rel="stylesheet">
<script language="javascript">
function closeform() {
	location.href="/shop/pages/menu/list.do?menuid=${menuInfoForm.parentid}";
}
</script>
<body id="bodyid" leftmargin="10" topmargin="10">	
<form action="/shop/pages/menu/update.do" focus="menuitem" method="post"  name="menuInfoForm">
<table border="0" cellpadding="1" width="100%" cellspacing="1" class="tablebg" align="center">
<tr>
	<td class="oracolumncenterheader" colspan="2" height="24">
	   <c:if test="${!empty menuInfoForm.menuid}">
			<input type="hidden" name="control" value="add">
		    <spring:message code="menuInfo.title.add"/>  
	   </c:if>	 
	   <c:if test="${empty menuInfoForm.menuid }">
		    <script type="text/javascript">
		    	 menuInfoForm.action="/shop/pages/menu/save.do";
		    </script>
		    <spring:message code="menuInfo.title.modify"/>  
			<input type="hidden" name="control" value="modify">
	   </c:if>
	   <c:if test="${not empty menuPath }">
			<input type="hidden" name="menuPath" value='${menuPath}'>
	   </c:if>
	</td>
</tr>
<tr>
	<td height="24" width="29%" class="oracletdtwo" align="right">
		<font color="#FF0000">**</font><spring:message code="menuInfoForm.menuname"/>  :
	</td>
	<td width="71%" class="oracletdone">
		<input type="text" name="menuitem" value="${menuInfoForm.menuitem }" size="50" style="width:90%" />
	</td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right"><spring:message code="menuInfoForm.menuname.English"/>  :
	</td>
	<td class="oracletdone"><input type="text" name="menuitemEn" value="${menuInfoForm.menuitemEn}" size="50" style="width:90%"/></td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right">
		<font color="#FF0000">**</font><spring:message code="menuInfoForm.setorder"/>  :
	</td>
	<td class="oracletdone">
		<input type="text" name="actionto" value="${menuInfoForm.actionto }" style="width:90%"/>
	</td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right">
		<font color="#FF0000">**</font><spring:message code="menuInfoForm.jumpurl"/>  :
	</td>
	<td class="oracletdone">	
		<view:viewSelect name="target" id="target" style="width:90%" selType="dataDir" path="menuMgr.target" />
		<c:if test="${not empty menuInfoForm.menuid }">
		<script language="javascript">
			menuInfoForm.target.value='${menuInfoForm.target}';
		</script>
		</c:if>
		<input type="hidden" name="menuid" value="${menuInfoForm.menuid }"/>
		<input type="hidden" name="parentid" value="${menuInfoForm.parentid }"/>
		<input type="hidden" name="floor" value="${menuInfoForm.floor }"/>
	</td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right"><spring:message code="menuInfoForm.image"/>  :
	</td>
	<td class="oracletdone">	
		<input type="text" name="image" value="${menuInfoForm.image }" style="width:90%"/>
	</td>
</tr>
<tr>
	<td colspan="2" bgcolor="#FFFFFF" height="24" align="center">
		<input type="submit" name="btnSave" value='<spring:message code="button.save"/>  ' class="MyButton" accesskey="S" image="/shop/images/share/save.gif">
        <input type="button" name="btnClose" value='<spring:message code="button.close"/>  ' class="MyButton" AccessKey="C" image="/shop/images/share/f_closed.gif" onClick="closeform()">
        
		<c:if test="${empty menuInfoForm.menuid }">
		<input type="checkbox" name="newAnOther" id="newAnOther" value="1" checked><spring:message code="menuInfo.label.addAnOther"/>  
		</c:if>
	</td>
</tr>
<tr>
	<td bgcolor="#F5F5F5" align="center">
	<table width="93%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td align="center"><img src="/shop/images/share/lamp.gif"></td>
	</tr>
	</table>
	</td>
	<td bgcolor="#FFFFFF" valign="top">&nbsp;
		<table width="99%" border="0" align="center" cellpadding="0" cellspacing="2" height="100%">
		<tr>
			<td valign="bottom">
				<font color="#FF0000"><spring:message code="tips.title"/></font>
			</td>
		</tr>
		<tr>
			<td valign="top"><spring:message code="menuInfo.content"/>  </td>
		</tr>
		</table>
	</td>
</tr>
</table>
</form>
</body>
</html>