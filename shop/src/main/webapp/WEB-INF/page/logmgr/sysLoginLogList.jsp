<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/page/share/taglib.jsp" %>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<html>
<head><title>login log query</title></head>
<link href="/shop/css/css_v2.css" rel="stylesheet" type="text/css">
<script language="javascript" src="/shop/js/tablesort.js"></script>
<script language="JavaScript" type="text/JavaScript">
function SelAll(chkAll) {
     var chkSet = document.all.tags("input")
     for(var i=0;i<chkSet.length;i++)
	 {
	    if(chkSet[i].name=="chkList")
		{
		   chkSet[i].checked=chkAll.checked
		}   
	 }
}

function GetSelIds() {
     var idList=""
     var chkSet = document.all.tags("input")
     for(var i=0;i<chkSet.length;i++) {
		if(chkSet[i].name=="chkList") {
			if(chkSet[i].checked) {
				idList+=","+chkSet[i].value
			}    
		}   
	 }
	 
	if(idList=="")
		return ""
	return idList.substring(1)
}

function query() {
	ToPage()
}

function ToPage() {
	sysLoginLogForm.toPage.value = formList.toPage.value
	sysLoginLogForm.pageSize.value = formList.pageSize.value
	sysLoginLogForm.submit()
}

function del() {
	var idList = GetSelIds();
	if(idList == "") {
		alert('<spring:message code="del.selectNone"/> ')
		return false
	}

	if (confirm('<spring:message code="confirmMsg.deleteTermType"/>')) {
		sysLoginLogForm.toPage.value = formList.toPage.value
		sysLoginLogForm.pageSize.value = formList.pageSize.value
		sysLoginLogForm.idList.value = idList
		sysLoginLogForm.action="<%=request.getContextPath()%>/pages/logmgr/sysloginlog.do?action=del"
		sysLoginLogForm.submit()
	}
}

function clearAll() {
	if (confirm('<spring:message code="operator.clearalllog"/> ')) {
		sysLoginLogForm.action = "<%=request.getContextPath()%>/pages/logmgr/sysloginlog.do?action=clear"
		sysLoginLogForm.submit()
	}	
}
</script>
<body id="bodyid" leftmargin="0" topmargin="2">
<form name="sysLoginLogForm" action="/shop/pages/sysloginlog/list.do" method="post">
<table width="100%" cellspacing="0" cellpadding="0">
<tr> 
    <td> 
	<fieldset class="jui_fieldset" width="100%"><legend><spring:message code="label.condition"/> </legend> 
	<table width="100%">
    <tr> 
      <td width="10%" align="right"><spring:message code="logmgr.logintime" />:  </td>
       <td width="55%">
		<input name="loginbegintime" type="text" value='${sysLoginLogForm.loginbegintime}' size="21" class="MyInput" issel="true" isdate="true" dofun="ShowDateTime(this)">
		&nbsp;<spring:message code="calendar.to"/> &nbsp;
		<input name="loginendtime" type="text" value='${sysLoginLogForm.loginendtime}' size="21" class="MyInput" issel="true" isdate="true" dofun="ShowDateTime(this)">
		</td>
		<td width="10%" align="right"><spring:message code="logmgr.username" />:  </td>
		<td width="25%"><input type="text" name="username" style="width:95%"/></td>
	</tr>
	<tr>
		<td align="right"><spring:message code="logmgr.logouttime" />:  </td>
        <td>
		<input name="logoutbegintime" type="text" value='${sysLoginLogForm.logoutbegintime}' size="21" class="MyInput" issel="true" isdate="true" dofun="ShowDateTime(this)">
		&nbsp;<spring:message code="calendar.to"/> &nbsp;
		<input name="logoutendtime" type="text" value='${sysLoginLogForm.logoutendtime}' size="21" class="MyInput" issel="true" isdate="true" dofun="ShowDateTime(this)">
		</td>
		<td align="right"><spring:message code="logmgr.hostip" />:  </td>
		<td><input type="text" name="hostip" style="width:95%"/></td>
		</tr>
		</table>
		</fieldset>
	</td>
  </tr>
</table>
<input type="hidden" name="pageSize" value="10">
<input type="hidden" name="toPage" value="1">
<input type="hidden" name="idList">
<input type="hidden" name="menuno" value='${menu.menuno}'>
</form>
<form name="formList" method="post">
<table width="100%" height="20" border="0" cellspacing="0" cellpadding="0" background="/shop/images/main/bgtop.gif">
  <tr>
    <td width="3%" valign="middle">&nbsp;<img src="/shop/images/share/list.gif" width="14" height="16"></td>
    <td width="15%" class="orarowhead"><spring:message code="label.query.result"/> </td>
	<td align="right"><view:button menuid="menu.menuno" type="session"/></td>
  </tr>
</table>
<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066">
  <tr bgcolor="#FFFFFF"> 
  	<td>
       <table width="100%" cellSpacing="0" cellPadding="0">
          <tr>
            <td width="5%" align="center">
              <input type="checkbox" name="chkAll" id="chkAll" value="all" onClick="SelAll(this)">
            </td>
          	<td width="12%"><label for=chkAll><spring:message code="label.select.all"/> </label></td>
    <td width="83%"><view:pagination pageName="currPage" formName="sysLoginLogForm" /></td></tr></table>
	  </td>
	</tr>
</table>
<table width="100%"  border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
<tr class="oracolumncenterheader"> 
	  <td width="5%" ><spring:message code="label.select"/> </td>
	  <td width="10%" align="center"><spring:message code="label.user.name"/> </td>
	  <td width="15%" align="center"><spring:message  code="logmgr.hostip" /></td>
	  <td width="30%" align="center"><spring:message  code="logmgr.logintime" /></td>
	  <td width="30%" align="center"><spring:message  code="logmgr.logouttime" /></td>
	  <td width="10%" align="center"><spring:message  code="logmgr.result" /></td>
	</tr>
	<c:forEach var="logmgr" items="${queryAll}" varStatus="index">
	<tr class="trClass${index.index%2}" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)> 
		<td align="center">
		<input type="checkbox" name="chkList" value='${logmgr.loginid}'>
		</td>
		<td align="center">${logmgr.username}</td>
		<td>${logmgr.hostip}</td>
		<td>${logmgr.logintime}</td>
		<td>${logmgr.logouttime}</td>
		<td align="center">${logmgr.result}</td>
	</tr>
	</c:forEach>	
	<c:forEach begin="0" end="${rowNum}" varStatus="index">
		<tr id="rownum${index.index}" class="trClass${index.index%2}" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
			<td height="20"></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>		
	</c:forEach>
</table>
</form>
</body>
</html>