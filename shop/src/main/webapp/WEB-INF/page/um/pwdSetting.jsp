<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<html>
<head><title>update user password</title></head>
<link href="/shop/css/css_v2.css" type="text/css" rel="stylesheet">
<script language="javascript" src="/shop/js/Validator.js"></script>
<script language="javascript">
function CheckForm() {
	document.all.divMsg.style.display = "none"  
    if (operInfoForm.password.value=="") {
        alert("<spring:message code="pwdsetting.password" />");
        return false;
    }
    if (operInfoForm.newPassword.value=="") {
        alert("<spring:message code="pwdsetting.newPassword" />");
        return false;
    }
	if (!Validate('newPassword','UnSafe'))  {
		alert("<spring:message code="pwdsetting.unsafe" />");
		return false;
	}
    if (operInfoForm.newPassword.value!=operInfoForm.newPassword2.value) {
        alert("<spring:message code="pwdsetting.differ" />");
        return false;
    }
    operInfoForm.submit()
}
</script>
<body id="bodyid" leftmargin="20" topmargin="10">
<form action="/pages/um/operInfo" method="post">
<input type='hidden' name='action' value='pwdSave'/>
<table border="0" cellpadding="1" cellspacing="1" class="tablebg" width="100%">
<tr>
	<td colspan="2" class="oracolumncenterheader" height="24"><spring:message code="pwdsetting.title" />
	</td>
</tr>
<tr> 
	<td height=24 width="25%" class="oracletdtwo" align="right">
		<font color="red">**</font><spring:message code="pwdsetting.oldpwd" />
	</td>
    <td width="75%" class="oracletdone">          
        <input name="password" type="password" id="password" style="width:97%">
	</td>
</tr>
<tr> 
	<td height=24 width="25%" class="oracletdtwo" align="right">
		<font color="red">**</font><spring:message code="pwdsetting.newpwd" />
	</td>
    <td width="75%" class="oracletdone">          
		<input name="newPassword" type="password" id="newPassword" style="width:97%">
	</td>
</tr>
<tr> 
	<td height=24 width="25%" class="oracletdtwo" align="right">
		<font color="red">**</font><spring:message code="pwdsetting.newpwdagain" />
	</td>
    <td width="75%" class="oracletdone">          
         <input name="newpwdagain" type="password" id="newPassword2" style="width:97%">
	</td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right"></td>
	<td class="oracletdone">
		<div id="divMsg" style="display:block">
		<logic:messagesPresent message="true" >
        	<font color="#FF0000">
				<html:messages id="msg" message="true" >
					${msg" filter="false}
				</html:messages>
			</font>
		</logic:messagesPresent>
		</div>
	</td>
</tr>
<tr> 
	<td colspan="2" bgcolor="#FFFFFF" height="24" align="center"> 
		<input type="button" name="Submit1" value='<spring:message code="button.save"/>' class="MyButton" AccessKey="S" onClick="CheckForm()" image="/shop/images/share/save.gif">&nbsp;
		<input type="reset" name="reset" value='<spring:message code="button.cancel"/>' class="MyButton" AccessKey="C" image="/shop/images/share/refu.gif">
	</td>
</tr>
<tr> 
	<td width="25%" bgcolor="#F5F5F5" align="center"> 
    <table width="93%" border="0" cellspacing="0" cellpadding="0" >
    <tr> 
       <td align="center"><img src="/shop/images/share/lamp.gif" ></td>
	</tr>
	</table>
	</td>
	<td width="75%" bgcolor="#FFFFFF" valign="top">&nbsp;
    <table width="99%" border="0" align="center" cellpadding="0" cellspacing="2" height="100%">
    <tr> 
      <td valign="bottom"><font color="#FF0000"><spring:message code="tips.title"/></font></td>
    </tr>
    <tr> 
       <td valign="top"><spring:message code="label.admin.pwdsafe" /><spring:message code="label.admin.content" />  
       </td>
     </tr>
    </table>
     </td>
</tr>
</table>
</form>
</body>
</html>