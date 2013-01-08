<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<html> 
<head><title><bean:message  key="orginfo.addinfo" bundle="org"/></title></head>
<link href="/shop/css/css_v2.css" rel="stylesheet" type="text/css">
<script language="javascript" src="/shop/js/tablesort.js"></script>
<script language="javascript" src="/shop/js/Validator.js"></script>
<script language="JavaScript" type="text/JavaScript">
function validateOrgInfo(){
	document.all.divMsg.style.display = "none"
	if (!Validate('orgid','Require')){  
		alert('<bean:message  key="check.orgid" bundle="org"/>')
		orgInfoForm.orgid.focus(); 
		return false;
	}
	if (!Validate('orgname','Require'))  {
		alert('<bean:message  key="check.orgname" bundle="org"/>')
		orgInfoForm.orgname.focus(); 
		return false;
	}
	if (!Validate('orgfullname','Require'))  {
		alert('<bean:message  key="check.orgfullname" bundle="org"/>')
		orgInfoForm.orgfullname.focus(); 
		return false;
	}
	return true;	   
}

function saveOrg() {
	if (validateOrgInfo()) {
		parent.returnValue = true
		orgInfoForm.submit()
	}
}
</script>
<body topmargin="10" leftmargin="10">
<form action="/pages/org/orgMgr" method="post">
<table border="0" cellpadding="1" width="100%" cellspacing="1" class="tablebg" align="center">
<tr>
	<td class="oracolumncenterheader" colspan="2" height="24"><spring:message code="orginfo.addinfo" bundle="org"/> 
	</td>
</tr>
<tr>
	<td height="24" width="29%" class="oracletdtwo" align="right"><spring:message code="orginfo.postiton" bundle="org"/>:  
	</td>
	<td width="71%" class="oracletdone">
		<font color="#FF0099">${parentName}</font>
	</td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right">
		<font color="#FF0000">**</font><spring:message code="orginfo.orgid" bundle="org"/>:  
	</td>
	<td class="oracletdone"><html:text property="orgid" style="width:90%"/></td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right">
		<font color="#FF0000">**</font><spring:message code="orginfo.orgname" bundle="org"/>:  
	</td>
	<td class="oracletdone">
		<html:text property="orgname" style="width:90%"/>
	</td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right">
		<font color="#FF0000">**</font><spring:message code="orginfo.orgfullname" bundle="org"/>:  
	</td>
	<td class="oracletdone">	
		<html:text property="orgfullname" style="width:90%"/>
	</td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right"><spring:message code="label.note.en" bundle="datadir"/>:  
	</td>
	<td class="oracletdone">
		<html:text property="orgnameEn" style="width:90%"/>
	</td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right"><spring:message code="orginfo.unionorgid" bundle="org"/>:  
	</td>
	<td class="oracletdone"><view:viewSelect name="unionorgid" id="unionorgid" style="width:90%" selType="unionorgid" path="Top_Parentid"/>
	</td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right"><spring:message code="tmlInfoForm.areaid"/> 
	</td>
	<td class="oracletdone">
<bean:define id="sareaid" name="liquiddata.loginUser" property="areaid" /><view:viewSelect name="areaid" id="areaid" style="width:90%" selType="areaid" path='<%=String.valueOf(sareaid)%>' />
	</td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right"><spring:message code="orginfo.pan" bundle="org"/>:  
	</td>
	<td class="oracletdone">
		<html:text property="pan" style="width:90%"/>
	</td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right"><spring:message code="orginfo.paper" bundle="org"/>:  
	</td>
	<td class="oracletdone">
		<html:text property="paper" style="width:90%"/>
	</td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right"><spring:message code="orginfo.orgcode" bundle="org"/>:  
	</td>
	<td class="oracletdone">
		<html:text property="orgcode" style="width:90%"/>
	</td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right"><spring:message code="orginfo.contact" bundle="org"/>:  				
	</td>
	<td class="oracletdone">
		<html:text property="contact" style="width:90%"/>
	</td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right"><spring:message code="orginfo.tel" bundle="org"/>:  
	</td>
    <td class="oracletdone">
		<html:text property="tel" style="width:90%"/>
	</td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right"><spring:message code="orginfo.address" bundle="org"/>:  </td>
    <td class="oracletdone">
		<html:text property="address" style="width:90%"/>
		<html:hidden property="parentid"/>
		<input type="hidden" name="action" value="insert">
	</td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right"><spring:message code="orginfo.fcrorg" bundle="org"/>:  </td>
    <td class="oracletdone">
		<html:text property="fcrOrgId" style="width:90%"/>
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
		<input type="button" name="btnAdd" value='<spring:message code="button.add"/> ' class="MyButton" id="btnAdd" accesskey="A" onClick="saveOrg()" image="/shop/images/share/save.gif">
         <input type="button" name="btnClose" value='<spring:message code="button.close"/> ' class="MyButton" id="btnClose" accesskey="B" onClick="window.close()" image="/shop/images/share/f_closed.gif" >
	</td>
</tr>
<tr>
	<td bgcolor="#F5F5F5" align="center">
			<table width="93%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						<div align="center">
							<img src="/shop/images/share/lamp.gif">
						</div>
					</td>
				</tr>
			</table>
	</td>
	<td bgcolor="#FFFFFF" valign="top">
		<font color="#0033FF">&nbsp; </font>
		<div align="left">
		<table width="99%" border="0" align="center" cellpadding="0" cellspacing="2" height="100%">
				<tr>
					<td valign="bottom">
						<font color="#FF0000"><spring:message code="tips.title"/> </font>
					</td>
				</tr>
				<tr>
					<td valign="top">
			1、<bean:message  key="orginfo.desc1" bundle="org"/><br>
			2、<bean:message  key="orginfo.desc2" bundle="org"/>“<font color="#FF0000">**</font>”<spring:message code="orginfo.desc3"/> <br>
            3、<bean:message  key="orginfo.desc4" bundle="org"/>
					</td>
				</tr>
			</table>
		</div>
	</td>
</tr>
</table>	
</form> 
</body>    
</html>