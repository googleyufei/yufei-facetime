<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<html>
<head><title><spring:message code="check.title"/> </title></head>
<link href="/shop/css/css_v2.css" type="text/css" rel="stylesheet">
<body bgColor="#ffffff" leftMargin="0" topMargin="0" marginheight="0" marginwidth="0">
<form>
<logic:notEmpty name="dpLog">
<bean:define id="dplog" name="dpLog" />
<bean:define id="cleardate" property="cleardate" name="dplog"/>
<table width="100%">
  	<tr>                
    	<td width="3%"><img src="/shop/images/share/arrow-4.gif"></td>            
    	<td width="97%"><b><spring:message code="logmgr.detail"/> </b></td>
	</tr>
</table>
<table bgcolor="#000066" width="100%">
<tr>
     <td class="oracletdtwo" width="10%" align="right">
		<bean:message bundle="liquidata" key="clear.account.errReason" />: 
	</td>
    <td class="oracletdone">
		<view:EnToZh name="dplog" hashMap="accchk.errorRessonMap" type="dataDir" property="errReason"/>
	</td> 
    <td class="oracletdtwo" align="right" width="10%"><spring:message code="label.user.name"/> : 
	</td>
    <td class="oracletdone">
		<view:EnToZh name="dpLog" property="userid" hashMap="BusnDataDir.operInfoMap" type="dataDir"/>
	</td> 
</tr>
<tr>
     <td class="oracletdtwo" width="10%" align="right">
		<bean:message bundle="liquidata" key="clear.account.cleardate" />: 
	</td>
    <td class="oracletdone">
	<%
		String str=String.valueOf(cleardate);
		out.print(str.substring(0,4)+"-"+str.substring(4,6)+"-"+str.substring(6,8));
	%>
	</td>
    <td class="oracletdtwo" align="right" width="10%">
		<bean:message bundle="liquidata" key="clear.account.createdate" />: 
	</td>
    <td class="oracletdone">
		${dplog.createdate" format="yyyy-MM-dd HH:mm:ss}
	</td> 
</tr>
<tr>
     <td class="oracletdtwo" width="10%" align="right">
		<bean:message bundle="liquidata" key="clear.account.unionorgid" />: 
	</td>
    <td class="oracletdone">
		<view:EnToZh name="dpLog" property="unionorgid" hashMap="BusnDataDir.unionOrgInfoMap" type="dataDir"/>	</td> 
    <td class="oracletdtwo" align="right" width="10%">
		<bean:message bundle="liquidata" key="clear.account.type" />: 
	</td>
    <td class="oracletdone">
		<view:EnToZh name="dpLog" property="type" hashMap="log.processType" type="dataDir"/>
	</td> 
</tr>
<tr>
     <td class="oracletdtwo" width="10%" align="right">
		<bean:message bundle="liquidata" key="clear.account.hostip" />: 
	</td>
    <td class="oracletdone">
		${dpLog.hostip}
	</td> 
    <td class="oracletdtwo" align="right" width="10%">
		<bean:message bundle="liquidata" key="clear.account.doneflag" />: 
	</td>
    <td class="oracletdone">
		<view:EnToZh name="dpLog" property="flag" hashMap="log.doneflag" type="dataDir"/>	
		</td> 
</tr>
</table>
</logic:notEmpty>
</form>
</body>
</html>