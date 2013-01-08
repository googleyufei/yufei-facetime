<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/page/share/taglib.jsp"%>
<html>
<head><title>add operation info</title></head>
<link href="/shop/css/css_v2.css" rel="stylesheet" type="text/css">
<script language="javascript" src="/shop/js/Validator.js"></script>
<script language="javascript">
function validateInfo() {
	document.all.divMsg.style.display = "none"
	if (!Validate('userid','Require')) {  
		alert('<spring:message code="operInfo.check.useridentifiernull" />');	 
		return false;
	}
	var reg = /[^\x00-\xff]/;
	if(reg.test(operInfoForm.userid.value)){
		alert("<spring:message code="operAdd.checkForm.nochinese" />");
		return;	
	}
	if (!Validate('username','Require'))  {
       alert('<spring:message code="roleInfo.check.username" />');
	   return false;
	}
	if(reg.test(operInfoForm.workid.value)){
		alert("<spring:message code="operAdd.check.workid" />");
		return;	
	}
	if (!Validate('areaid','Require'))  {
		alert('<spring:message code="roleInfo.check.area" />')
		operInfoForm.areaid.focus(); 
		return false;
	}
	if (!Validate('orgid','Require'))  {
		alert('<spring:message code="roleInfo.check.org" />')
		operInfoForm.orgid.focus(); 
		return false;
	}
	if (!Validate('userpwd','UnSafe'))  {
		alert("<spring:message code="pwdsetting.unsafe" />");
		return false;
	}
	if (!Validate('userpwd2','Require'))  {
		alert('<spring:message code="operInfo.check.pwd2null" />')
		return false;
	}
	if (operInfoForm.userpwd.value!=operInfoForm.userpwd2.value){
		alert('<spring:message code="pwdsetting.differ" />')
		return false;
	}
	if (operInfoForm.email.value!=null &&operInfoForm.email.value.length!=0 && !Validate('email','Email'))  {
		alert('<spring:message code="operInfo.check.email" />')
		return false;
	}
	return true;
}
	
function formsubmit(){
	if (validateInfo()) {
		parent.returnValue = true
		operInfoForm.submit()
	}
}
</script>
<body id="bodyid" leftmargin="10" topmargin="10">
<form name="operInfoForm" action="/shop/pages/um/operinfo/add.do" method="post">
<table border="0" cellpadding="1" width="100%" cellspacing="1" class="tablebg" align="center">
<tr>
	<td class="oracolumncenterheader" colspan="4" height="24">
	   <spring:message code="operInfo.addOperator" />
	</td>
</tr>
<tr>
	<td height="24" width="22%" class="oracletdtwo" align="right">
    <font color="#FF0000">**</font><spring:message code="operInfoform.userIdentifier" />
    </td>
	<td width="30%" class="oracletdone">
		<input name="userid" type="text" id="userid" style="width:100%">
	</td>
	<td width="18%" class="oracletdtwo" align="right">
		<font color="#FF0000">**</font><spring:message code="operInfoform.username" />
	</td>
	<td width="30%" class="oracletdone"> 
		<input name="username" type="text" id="username" style="width:100%">
	</td>
</tr>
<tr> 
	<td height="24" class="oracletdtwo" align="right">
		<font color="#FF0000">**</font><spring:message code="operInfoform.pwd" />
	</td>
	<td class="oracletdone"> 
		<input name="userpwd" type="password" id="userpwd" style="width:100%">
	</td>
	<td class="oracletdtwo" align="right"> 
    	<font color="#FF0000">**</font><spring:message code="operInfoform.pwdconfirm" />
	</td>
	<td class="oracletdone"> 
		<input name="userpwd2" type="password" id="userpwd2" style="width:100%">
	</td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right"><spring:message code="operInfoform.tellerid" />: 
	</td>
	<td class="oracletdone"><input type="text" name="tellerid" style="width:100%" maxlength="5"></td>
	<td class="oracletdtwo" align="right"><spring:message code="operInfoform.number" /></td>
	<td class="oracletdone"><input name="workid" type="text" id="workid" style="width:100%"></td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right"><spring:message code="operInfoform.tellerpwd" />
	</td>
	<td class="oracletdone">
		<input type="password" name="tellerpwd" style="width:100%" maxlength="6">
	</td>
	<td class="oracletdtwo" align="right"><spring:message code="operInfoform.isUsed" /></td>
	<td class="oracletdone">
		<input type="radio" name="pwdflag" value="Y"><spring:message code="label.yes"/>
		<input type="radio" name="pwdflag" value="N" checked><spring:message code="label.no"/>
	</td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right"><spring:message code="operInfoform.tellerlevel" />
	</td>
	<td class="oracletdone">
		<input type="text" name="tellerlevel" style="width:100%" maxlength="2">
	</td>
	<td class="oracletdtwo" align="right"><spring:message code="operInfoform.isUsed" /></td>
	<td class="oracletdone">
		<input type="radio" name="levelflag" value="Y"><spring:message code="label.yes"/>
		<input type="radio" name="levelflag" value="N" checked><spring:message code="label.no"/>
	</td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right"><spring:message code="operInfoform.tellertype" />
	</td>
	<td class="oracletdone">
		<input type="text" name="tellertype" style="width:100%" maxlength="2">
	</td>
	<td class="oracletdtwo" align="right"><spring:message code="operInfoform.isUsed" /></td>
	<td class="oracletdone">
		<input type="radio" name="typeflag" value="Y"><spring:message code="label.yes"/>
		<input type="radio" name="typeflag" value="N" checked><spring:message code="label.no"/>
	</td>
</tr>
<tr> 
	<td height="24" class="oracletdtwo" align="right"><spring:message code="operInfoform.company" />
    </td>
   <td class="oracletdone">
		<input name="workcompany" type="text" id="workcompany" style="width:100%">
	</td> 
	<td class="oracletdtwo" align="right"><spring:message code="operInfoform.userflag" />
	</td>
	<td class="oracletdone"> 
		<input type="radio" name="isvalid" value="Y" checked><spring:message code="label.yes"/>
		<input type="radio" name="isvalid" value="N"><spring:message code="label.no"/>
	</td>
</tr>
<tr> 
	<td height="24" class="oracletdtwo" align="right"><spring:message code="operInfoform.phoneNum" />
    </td>
    <td class="oracletdone"> 
        <input name="tel" type="text" id="tel" style="width:100%">
    </td>
	<td class="oracletdtwo" align="right"><spring:message code="operInfoform.mobile" />
	</td>
	<td class="oracletdone"> 
		<input name="mobile" type="text" id="mobile" style="width:100%">
	</td>
</tr>
<tr> 
	<td height="24" class="oracletdtwo" align="right"><spring:message code="operInfoform.email"  />
	</td>
	<td class="oracletdone" colspan="3"> 
		<input name="email" type="text" id="email" style="width:100%">
	</td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right"></td>
	<td class="oracletdone" colspan="3">
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
	<td colspan="4" bgcolor="#FFFFFF" height="24" align="center">
	<input type="button" name="ok"  value='<spring:message code="button.save"/>' class="MyButton" accesskey="S" onClick="formsubmit()" image="/shop/images/share/save.gif"> 
       <input type="button" name="cancel" value='<spring:message code="button.close"/>' class="MyButton" accesskey="C" onClick="window.close()" image="/shop/images/share/f_closed.gif">
	</td>
</tr>
<tr>
	<td bgcolor="#F5F5F5" align="center">
			<table width="93%" border="0" cellspacing="0" cellpadding="0">
				<tr align="center">
					<td><img src="/shop/images/share/lamp.gif"></td>
				</tr>
			</table>
	</td>
	<td bgcolor="#FFFFFF" valign="top" colspan="3">&nbsp;
	<table width="99%" border="0" align="center" cellpadding="0" cellspacing="2" height="100%">
		<tr>
			<td valign="bottom">
				<font color="#FF0000"><spring:message code="tips.title"/></font>
			</td>
		</tr>
		<tr>
		<td valign="top"><spring:message code="label.admin.content" /><spring:message code="label.admin.pwdsafe" />
		</td>
		</tr>
	</table>
	</td>
</tr>    
</table>
</form>
</body>
</html>