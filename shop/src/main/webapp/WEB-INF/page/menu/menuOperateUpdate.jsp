<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<html>
<head><title><spring:message code="menuOperate.title.modify" />  </title></head>
<link href="/shop/css/css_v2.css" type="text/css" rel="stylesheet">
<html:javascript formName="menuOperateForm"/>
<script language="javascript" src="/shop/js/Validator.js"></script>
<script language="javascript">
function SubmitForm(formInfo) {
	document.all.divMsg.style.display = "none"
	if (!Validate('opername','Require')) {  
		alert('<spring:message code="check.opername"/> ')
		formInfo.opername.focus(); 
		return false;
	}
	if (!Validate('clickname','Require')) {  
		alert('<spring:message code="check.clickname"/> ')
		formInfo.clickname.focus(); 
		return false;
	}
	parent.returnValue = true
}

function closeform() {
	if (!parent.returnValue) {
		parent.returnValue = false
		window.close()
	}
}
</script>
<body id="bodyid" leftmargin="10" topmargin="10" onUnload="closeform()">
<form action="/pages/menu/MenuOperate.do?action=saveupdate" method="post" onsubmit="return SubmitForm(this)">
<html:hidden property="operid"/>
<table border="0" cellpadding="1" width="100%" cellspacing="1" class="tablebg" align="center">
<tr>
	<td class="oracolumncenterheader" colspan="2" height="24"><spring:message code="menuOperateForm.title"/>  
	</td>
</tr>
<tr>
	<td height="24" width="28%" class="oracletdtwo" align="right">
		<font color="#FF0000">**</font><spring:message code="menuOperateForm.opername"/>  
	</td>
	<td width="72%" class="oracletdone">
		<html:text property="opername" style="width:97%" maxlength="25"/>
	</td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right">
		<font color="#FF0000">**</font><spring:message code="menuOperateInfoForm.clickname"/>  :
	</td>
	<td class="oracletdone">
		<html:text property="clickname" style="width:97%" maxlength="200"/>
	</td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right"><spring:message code="menuOperateInfoForm.picpath"/>  :
	</td>
	<td class="oracletdone">
		<html:text property="picpath" style="width:97%" maxlength="200"/>
	</td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right"><spring:message code="menuOperateInfoForm.types"/>  :
	</td>
	<td class="oracletdone">	
		<html:select property="types" style="width:97%">
		<html:option key="button" value="button" />
		<html:option key="submit" value="submit" />
		<html:option key="reset" value="reset" />
		</html:select>
	</td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right"><spring:message code="menuOperateInfoForm.keys"/>  :
	</td>
	<td class="oracletdone">	
		<html:text property="keys" style="width:97%" maxlength="2"/>
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
		<input type="submit" name="submit" value='<spring:message code="button.save"/>  ' class="MyButton" AccessKey="O" image="/shop/images/share/save.gif">
         <input type="button" name="btnClose" value='<spring:message code="button.close"/>  ' class="MyButton" AccessKey="C" onClick="closeform()" image="/shop/images/share/f_closed.gif">
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
				<font color="#FF0000"><spring:message code="tips.title"/> </font>
			</td>
		</tr>
		<tr>
			<td valign="top"><spring:message code="label.content"/>  </td>
		</tr>
	</table>
	</td>
</tr>
</table>
</form>
</body>
</html>