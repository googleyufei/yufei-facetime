<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<html> 
<head><title><spring:message code="orginfo.addinfo"/> </title></head>
<link href="/shop/css/css_v2.css" rel="stylesheet" type="text/css">
<script language="javascript" src="/shop/js/tablesort.js"></script>
<script language="javascript" src="/shop/js/Validator.js"></script>
<script language="JavaScript" type="text/JavaScript">
function validateGeogArea(){
	document.all.divMsg.style.display = "none"
	if (!Validate('areaid','Require')) {  
		alert('<spring:message code="check.areaid"/> ')
		geogAreaForm.areaid.focus(); 
		return false;
	}
	if (!Validate('name','Require')) {
		alert('<spring:message code="check.areaname"/> ')
		geogAreaForm.name.focus(); 
		return false;
	}
	return true;	   
}

function saveGeogArea() {
	if (validateGeogArea()) {
		parent.returnValue = true 
		geogAreaForm.submit()
	} 
}
</script>
<body id="bodyid" topmargin="10" leftmargin="10">
<form action="/pages/geog/areaMgr" method="post" >  
<table border="0" cellpadding="1" width="100%" cellspacing="1" class="tablebg" align="center">
<tr>
	<td class="oracolumncenterheader" colspan="2" height="24"><spring:message code="geogarea.modifyinfo" bundle="geog"/> 
	</td>
</tr>
<tr>
	<td height="24" width="25%" class="oracletdtwo" align="right"><spring:message code="geogarea.postiton" bundle="geog"/>:  
	</td>
	<td width="75%" class="oracletdone">
		<font color="#FF0099">${parentName}</font>
	</td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right">
		<font color="#FF0000">**</font><spring:message code="geogAreaForm.areaid"/> 
	</td>
	<td class="oracletdone">
		${geogAreaForm" property="areaid}
		<html:hidden property="areaid" />
	</td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right">
		<font color="#FF0000">**</font><spring:message code="geogAreaForm.areaname"/> 
	</td>
	<td class="oracletdone">
		<html:text property="name" style="width:98%"/>
	</td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right"><spring:message code="label.note.en" bundle="datadir"/>:  
	</td>
	<td class="oracletdone">	
		<html:text property="nameEn" style="width:98%"/>
	</td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right"><spring:message code="geogarea.note" bundle="geog"/>:  
	</td>
	<td class="oracletdone">
		<html:textarea property="note" rows="7" style="width:98%"/>
		<html:hidden property="parentid" />
		<html:hidden property="childnum" />
		<input type="hidden" name="action" value="update">
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
		<input type="button" name="btnAdd" image="/shop/images/share/save.gif" value='<spring:message code="button.save"/> ' class="MyButton" accesskey="S" onClick="saveGeogArea()">
         <input type="button" name="btnClose" value='<spring:message code="button.close"/> ' class="MyButton" id="btnClose" accesskey="B" onClick="window.close()" image="/shop/images/share/f_closed.gif" >
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
			<td valign="top"><spring:message code="label.content"/> </td>
		</tr>
		</table>
	</td>
</tr>
</table>  
</form>
</body>
</html>