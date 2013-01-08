<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/htc.inc"%>
<html>
<head><title>update operation info</title></head>
<link href="/shop/css/css_v2.css" rel="stylesheet" type="text/css">
<script language="javascript" src="/shop/js/Validator.js"></script>
<script language="javascript">
function validateInfo(){
	if (!Validate('username','Require'))  {
       alert('<spring:message code="roleInfo.check.username" />');
	   return false;
	}
	var reg = /[^\x00-\xff]/;
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
	if (operInfoForm.email.value!=null &&operInfoForm.email.value.length!=0 && !Validate('email','Email'))  {
		alert('<spring:message code="operInfo.check.email" />')
		return false;
	}
	return true;
}
	
function formsubmit(){
	if (validateInfo()) {
		operInfoForm.submit()
		parent.returnValue = true
	}
}
</script>
<body id="bodyid" leftmargin="10" topmargin="10">
<form action="/pages/um/operInfo" method="post">
<input type='hidden' name='action' value='modify'/>
<table border="0" cellpadding="1" width="100%" cellspacing="1" class="tablebg" align="center">
<tr>
	<td class="oracolumncenterheader" colspan="4" height="24"><spring:message code="operInfo.updateTitle" />
</tr>
<tr>
	<td height="24" width="22%" class="oracletdtwo" align="right">
    <font color="#FF0000">**</font><spring:message code="operInfoform.userIdentifier" />
    </td>
	<td width="30%" class="oracletdone">
		${operInfoForm.userid" /><html:hidden property="userid}
	</td>
	<td width="18%" class="oracletdtwo" align="right">
		<font color="#FF0000">**</font><spring:message code="operInfoform.username" />
	</td>
	<td width="30%" class="oracletdone"><html:text property="username" style="width:100%"/></td>
</tr>
<tr> 
	<td height="24" class="oracletdtwo" align="right">
		<font color="#FF0000">**</font><spring:message code="operInfoform.bankid" />
	</td>
	<td class="oracletdone"> 
		<bean:define id="sorgid" name="liquiddata.loginUser" property="orgid"/>
		<view:viewSelect name="orgid" id="orgid" style="width:100%" selType="orgid" path='<%=String.valueOf(sorgid)%>' />
		<logic:notEmpty name="operInfoForm" property="orgid"> 
			<script language="javascript">
			  operInfoForm.orgid.value='${operInfoForm.orgid}';
			</script>
		</logic:notEmpty>
	</td>
	<td class="oracletdtwo" align="right"> 
    	<font color="#FF0000">**</font><spring:message code="tmlInfoForm.areaid"/>
	</td>
	<td class="oracletdone"> 
		<bean:define id="sareaid" name="liquiddata.loginUser" property="areaid"/>
		<view:viewSelect name="areaid" id="areaid" style="width:100%" selType="areaid" path='<%=String.valueOf(sareaid)%>' />
		<logic:notEmpty name="operInfoForm" property="areaid"> 
			<script language="javascript">
			  operInfoForm.areaid.value='${operInfoForm.areaid}';
			</script>
		</logic:notEmpty>
	</td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right"><spring:message code="operInfoform.tellerid" />: 
	</td>
	<td class="oracletdone"><html:text property="tellerid" style="width:100%" maxlength="5"/></td>
	<td class="oracletdtwo" align="right"><spring:message code="operInfoform.number" /></td>
	<td class="oracletdone"><html:text property="workid" style="width:100%"/></td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right"><spring:message code="operInfoform.tellerpwd" />
	</td>
	<td class="oracletdone">
		<html:password property="tellerpwd" style="width:100%" maxlength="6"/>
	</td>
	<td class="oracletdtwo" align="right"><spring:message code="operInfoform.isUsed" /></td>
	<td class="oracletdone">
		<logic:equal name="operInfoForm" property="pwdflag" value="Y">
			<input type="radio" name="pwdflag" value="Y" checked><spring:message code="label.yes"/>
			<input type="radio" name="pwdflag" value="N"><spring:message code="label.no"/>
		</logic:equal>
		<logic:notEqual name="operInfoForm" property="pwdflag" value="Y">
			<input type="radio" name="pwdflag" value="Y"><spring:message code="label.yes"/>
			<input type="radio" name="pwdflag" value="N" checked><spring:message code="label.no"/>
		</logic:notEqual>
	</td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right"><spring:message code="operInfoform.tellerlevel" />
	</td>
	<td class="oracletdone">
		<html:text property="tellerlevel" style="width:100%" maxlength="2"/>
	</td>
	<td class="oracletdtwo" align="right"><spring:message code="operInfoform.isUsed" /></td>
	<td class="oracletdone">
		<logic:equal name="operInfoForm" property="levelflag" value="Y">
			<input type="radio" name="levelflag" value="Y" checked><spring:message code="label.yes"/>
			<input type="radio" name="levelflag" value="N"><spring:message code="label.no"/>
		</logic:equal>
		<logic:notEqual name="operInfoForm" property="levelflag" value="Y">
			<input type="radio" name="levelflag" value="Y"><spring:message code="label.yes"/>
			<input type="radio" name="levelflag" value="N" checked><spring:message code="label.no"/>
		</logic:notEqual>
	</td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right"><spring:message code="operInfoform.tellertype" />
	</td>
	<td class="oracletdone"><html:text property="tellertype" style="width:100%" maxlength="2"/></td>
	<td class="oracletdtwo" align="right"><spring:message code="operInfoform.isUsed" /></td>
	<td class="oracletdone">
		<logic:equal name="operInfoForm" property="typeflag" value="Y">
			<input type="radio" name="typeflag" value="Y" checked><spring:message code="label.yes"/>
			<input type="radio" name="typeflag" value="N"><spring:message code="label.no"/>
		</logic:equal>
		<logic:notEqual name="operInfoForm" property="typeflag" value="Y">
			<input type="radio" name="typeflag" value="Y"><spring:message code="label.yes"/>
			<input type="radio" name="typeflag" value="N" checked><spring:message code="label.no"/>
		</logic:notEqual>
	</td>
</tr>
<tr> 
	<td height="24" class="oracletdtwo" align="right"><spring:message code="operInfoform.company" />
    </td>
	<td class="oracletdone"><html:text property="workcompany" style="width:100%"/></td> 
	<td class="oracletdtwo" align="right"><spring:message code="operInfoform.userflag" /></td>
	<td class="oracletdone">
		<logic:equal name="operInfoForm" property="isvalid" value="Y">
			<input type="radio" name="isvalid" value="Y" checked><spring:message code="label.yes"/>
			<input type="radio" name="isvalid" value="N"><spring:message code="label.no"/>
		</logic:equal>
		<logic:notEqual name="operInfoForm" property="isvalid" value="Y">
			<input type="radio" name="isvalid" value="Y"><spring:message code="label.yes"/>
			<input type="radio" name="isvalid" value="N" checked><spring:message code="label.no"/>
		</logic:notEqual>
	</td>
</tr>
<tr> 
	<td height="24" class="oracletdtwo" align="right"><spring:message code="operInfoform.phoneNum" />
    </td>
    <td class="oracletdone"><html:text property="tel" style="width:100%"/></td>
	<td class="oracletdtwo" align="right"><spring:message code="operInfoform.mobile" /></td>
	<td class="oracletdone"><html:text property="mobile" style="width:100%"/></td>
</tr>
<tr> 
	<td height="24" class="oracletdtwo" align="right"><spring:message code="operInfoform.email"  />
	</td>
	<td class="oracletdone" colspan="3"> 
		<html:text property="email" style="width:100%"/><html:hidden property="url" />
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
				<tr>
					<td align="center"><img src="/shop/images/share/lamp.gif"></td>
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