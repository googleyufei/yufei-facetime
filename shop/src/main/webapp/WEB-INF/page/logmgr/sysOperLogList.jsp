<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/page/share/taglib.jsp" %>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<html>
<head>
<title>system operator list</title>
<link href="/shop/css/css_v2.css" rel="stylesheet" type="text/css">
</head>
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
     for(var i=0;i<chkSet.length;i++)
	 {
	    if(chkSet[i].name=="chkList")
		{
			if(chkSet[i].checked)
			{
				idList+=","+chkSet[i].value
			}    
		}   
	 }
	 
	if(idList=="")
		return ""
	return idList.substring(1)
}

function query(){
	sysOperLogForm.toPage.value = formList.toPage.value;
	sysOperLogForm.pageSize.value = formList.pageSize.value;
	sysOperLogForm.submit();
}

function del() {
	var idList = GetSelIds();
	if(idList == "") {
       alert('<spring:message code="del.selectNone"/> ')
	   return false
	}

	if (confirm('<spring:message code="confirmMsg.deleteTermType"/>')) {
		sysOperLogForm.toPage.value = formList.toPage.value
		sysOperLogForm.pageSize.value = formList.pageSize.value
		sysOperLogForm.idList.value = idList
		sysOperLogForm.action = "<%=request.getContextPath()%>/pages/logmgr/sysoperlog.do?action=del"
		sysOperLogForm.submit()
	}
}

function clearAll() {
	if (confirm('<spring:message code="operator.clearalllog"/> ')) {
		sysOperLogForm.action = "<%=request.getContextPath()%>/pages/logmgr/sysoperlog.do?action=clear"
		sysOperLogForm.submit()
	}	
}
</script>
<body id="bodyid" leftmargin="0" topmargin="2">
<!-- 查询条件区 -->
<form name="sysOperLogForm" action="/shop/pages/sysoperlog/list.do" method="post">
<table width="100%" cellspacing="0" cellpadding="0">
  <tr> 
    <td> 
		<fieldset class="jui_fieldset" width="100%"><legend><spring:message code="label.condition"/> </legend> 
		<table width="100%">
        	<tr> 
          <td width="9%" align="right"><spring:message code="logmgr.logtime" />:  </td>          	
          <td width="47%">
            <input name="logbegintime" type="text" value='${sysOperLogForm.logbegintime}' size="21" class="MyInput" issel="true" isdate="true" dofun="ShowDateTime(this)">
              &nbsp;<spring:message code="calendar.to"/> &nbsp;
            <input name="logendtime" type="text" value='${sysOperLogForm.logendtime}' size="21" class="MyInput" issel="true" isdate="true" dofun="ShowDateTime(this)">
		  </td>
		<td width="7%" align="right"><spring:message code="logmgr.note" />:  
		</td>
		<td width="15%"><input type="text" name="note" style="width:100%"/></td>
        <td width="7%" align="right"><spring:message code="label.user.name"/>:  
		</td>
        <td width="15%"><input type="text" name="username" style="width:100%"/></td>
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

<!-- 列表显示区 -->
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
    		<td width="83%"><view:pagination pageName="currPage" formName="sysOperLogForm" />
		</td></tr></table>
		</td>
	</tr>
</table>
<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
	<tr class="oracolumncenterheader"> 
	  <td width="6%" align="center"><spring:message code="label.select"/> </td>
	  <td width="7%"><spring:message code="label.user.name"/> </td>
	  <td width="12%"><spring:message  code="logmgr.hostip" /></td>
  	  <td width="18%"><spring:message  code="logmgr.logtime" /></td>
  	  <td width="13%"><spring:message  code="logmgr.longtime" /></td>
  	  <td width="13%"><spring:message  code="logmgr.menuid" /></td>
  	  <td width="10%"><spring:message  code="logmgr.operid" /></td>
  	  <td width="30%"><spring:message  code="logmgr.note" /></td>
	</tr>
	<c:forEach items="${queryAll}" var="logmgr" varStatus="index">
		<tr id="${index.index}" class="trClass${index.index%2}" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)> 
		<td align="center"><input type="checkbox" name="chkList" value='${logmgr.id}'></td>
		<td align="center">${logmgr.username}</td>
		<td>${logmgr.hostip}</td>
		<td>${logmgr.logtime}</td>
		<td>${logmgr.longTime}</td>
		<td>${logmgr.menuid}</td>
		<td><view:dataDir beanName="logmgr" property="operid" path="menuOper.map"/></td>
		<td>${logmgr.note}</td>
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
			<td></td>
			<td></td>
		</tr>		
	</c:forEach>
</table>
</form>
</body>
</html>