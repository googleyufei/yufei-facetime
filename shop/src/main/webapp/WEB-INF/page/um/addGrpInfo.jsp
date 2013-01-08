<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/page/share/taglib.jsp"%>
<html>
<head><title>add group</title></head>
<link href="/shop/css/css_v2.css" rel="stylesheet" type="text/css">
<script language="javascript" src="/shop/js/Validator.js"></script>
<body id="bodyid" leftmargin="10" topmargin="10">
<form name="grpInfoForm" action="/shop/pages/um/grpinfo/add.do" method="post">
<table border="0" cellpadding="1" width="100%" cellspacing="1" class="tablebg" align="center">
<tr>
	<td class="oracolumncenterheader" colspan="2" height="24"><spring:message code="grpInfo.addGrp" />
	</td>
</tr>
<tr>
	<td height="24" width="35%" class="oracletdtwo" align="right">
		<font color="#FF0000">**</font><spring:message code="grpInfo.addGrpName" />
	</td>
	<td width="65%" class="oracletdone">
		<input type="text" name="grpname" style="width:97%">
	</td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right"></td>
	<td class="oracletdone"></td>
</tr>
<tr>
	<td colspan="2" bgcolor="#FFFFFF" height="24" align="center">
		<input type="button" name="ok"  value='<spring:message code="button.save"/>' class="MyButton" accesskey="O" onClick="grpInfoForm.submit()" image="/shop/images/share/save.gif"> 
        <input type="button" name="cancel" value='<spring:message code="button.close"/>' class="MyButton" accesskey="C" onClick="location.href='/shop/pages/um/grpinfo/list.do'" image="/shop/images/share/f_closed.gif">
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
		<td valign="top"><spring:message  code="label.admin.content"/></td>
		</tr>
	</table>
	</td>
</tr>
</table>
</form>
</body>
</html>