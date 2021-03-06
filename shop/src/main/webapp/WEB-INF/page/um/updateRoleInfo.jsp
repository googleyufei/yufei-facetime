﻿<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<html>
<head><title>update role info</title></head>
<link href="/shop/css/css_v2.css" type="text/css" rel="stylesheet">
<script language="javascript" src="../org/js/orgPages.js"></script>
<script language="javascript" src="/shop/js/Validator.js"></script>
<script language="javascript">
function validateInfo(){
	if (!Validate('rolename','Require')) {
		alert('<spring:message code="roleInfo.check.rolename" />')
		roleInfoForm.rolename.focus(); 
		return false;
	}
	return true;
}

function formsubmit(){
	if (validateInfo()){
		roleInfoForm.submit()
		parent.returnValue = true
	}
}
</script>
<body id="bodyid" leftmargin="10" topmargin="10">
<form action="/pages/um/roleInfo" method="post">
<input type='hidden' name='action' value='modify'/>
<table border="0" cellpadding="1" width="100%" cellspacing="1" class="tablebg" align="center">
<tr>
	<td class="oracolumncenterheader" colspan="2" height="24"><spring:message code="roleInfo.modifyrole" />
	</td>
</tr>
<tr>
	<td height="24" width="35%" class="oracletdtwo" align="right">
		<font color="#FF0000">**</font><spring:message code="roleInfo.modifyrolename" />
	</td>
	<td width="65%" class="oracletdone">
		<html:hidden property="rolecode"/>
		<html:text property="rolename" size="20" style="width:97%"/>
	</td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right"></td>
	<td class="oracletdone">
		<logic:messagesPresent message="true" >
        	<font color="#FF0000">
				<html:messages id="msg" message="true" >
					${msg" filter="false}
				</html:messages>
			</font>
		</logic:messagesPresent>
	</td>
</tr>
<tr>
	<td colspan="2" bgcolor="#FFFFFF" height="24" align="center">
		<input type="button" name="ok"  value='<spring:message code="button.save"/>' class="MyButton" accesskey="O" onClick="formsubmit()" image="/shop/images/share/save.gif"> 
        <input type="button" name="cancel" value='<spring:message code="button.close"/>' class="MyButton" accesskey="C" onClick="window.close()" image="/shop/images/share/f_closed.gif">
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
				<td valign="top"><bean:message  key="label.admin.content"/></td>
				</tr>
			</table>
	</td>
</tr>
</table>
</form>
</body>
</html>