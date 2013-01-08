<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/page/share/taglib.jsp"%>
<html>
<head><title>operation info</title></head>
<link href="/shop/css/css_v2.css" type="text/css" rel="stylesheet">
<script language="javascript" src="/shop/js/tablesort.js"></script>
<script language="JavaScript">
function SelAll(chkAll) {
     var em=formList.elements;
	 for(var  i=0;i<em.length;i++) {
		if(em[i].type=="checkbox") {
			em[i].checked=chkAll.checked
		}
	 }
}

function ShowTitleModal(theURL,features) {
	var returnValue = OpenModal(theURL,features)
	if (returnValue)
		query()
}

function isChecked(){
	var idList="";
	var em= formList.elements;
	for(var i=0;i<em.length;i++) {
		if(em[i].type=="checkbox") {
			if(em[i].checked) {
				if (em[i].value == "admin") {
					alert('<spring:message code="operDel.admin" />')
					return false
				}
				idList+=","+em[i].value;
			}
		}
	}
	if(idList=="") {
	   alert("<spring:message code="operInfoform.plscheck" />");
	   return false;
	}
	return true
}

function ShowUpdateWindow(operid){
	var strUrl="/pages/um/operInfo.do?action=modifyUI&username="+operid
	var features="550,480,operInfo.updateTitle,um"
	ShowTitleModal(strUrl,features)
}

function pwdInit(){
	var idList=""
	var em = formList.elements;
	for(var i=0;i<em.length;i++) {
	   if(em[i].name=="checkbox") {	  
		   if(em[i].checked) {
				idList+=","+em[i].value
		   }
	   }
	}	
	if (idList=="") {
		alert("<spring:message code="resetPwd.nocheck" />");
		return false;
	} else {
		if (confirm("<spring:message code="resetPwd.confirmed" />")) {			
			idList = idList.substring(1)
			var theURL="/pages/um/operInfo.do?action=pwdInit&user="+idList;
			var features="500,350,operator.setpage,um";
			OpenModal(theURL,features)
		}
	} 
}

function query() {
    operInfoForm.toPage.value = formList.toPage.value;
	operInfoForm.pageSize.value = formList.pageSize.value;
	operInfoForm.submit();
}

function setPages(){
   var aa=document.getElementsByName("checkbox");
   var userid=""
   var j=0;
   for (var i=0; i<aa.length; i++){
   		if (aa[i].checked){
			userid=aa[i].value;
			j=j+1;
		}
   }
	if (j==0)
 		alert('<spring:message code="operator.set"/>')
 	else if (j>1)
 		alert('<spring:message code="operator.setone"/>')
	else{
   		var theURL="/pages/um/operInfo.do?action=setPage&user=" + userid;
		var features="550,500,operator.setpage,um";
		var returnValue = OpenModal(theURL,features)
		if (returnValue != null) {
			var str = returnValue.split(",")
			operInfoForm.action="<%=request.getContextPath()%>/pages/um/queryOper.do?action=save&user="+userid+"&url="+str[0]+"&menuid="+str[1]
			operInfoForm.toPage.value = formList.toPage.value
			operInfoForm.pageSize.value = formList.pageSize.value
			operInfoForm.submit()
		}
	}
}

function add() {
	href.location="/shop/pages/um/operinfo/addUI.do";
}

function modify() {
	var idList
	var chkSet = document.all.tags("input")
	var j=0;
   for(var i=0;i<chkSet.length;i++) {
	    if(chkSet[i].name=="checkbox") {
			if(chkSet[i].checked) {
				j++;
				idList=chkSet[i].value
			}    
		}   
	 }

	if(j<1){
		alert('<spring:message code="operator.update"/>')
		return false
	} else if (j>1) {
		alert('<spring:message code="operator.updateone"/>')
		return false
	} else {
		ShowUpdateWindow(idList)
	}   		
}

function del() {
    if (isChecked() == false)
		return false;
 	if (confirm("<spring:message code="roleInfo.removeConfirm" />")) {
		formList.action="<%=request.getContextPath()%>/pages/um/operInfo.do?action=delete";
		formList.submit()
    }	
}

function unlock(){
	var j=0 ;
	var userid
	var em = formList.elements;
	for(var i=0;i<em.length;i++) {
	   if(em[i].type=="checkbox") {	  
		   if(em[i].checked){		
		   userid=em[i].value;	
			   j=j+1;
		   }
	   }
	}
	
	if (j==0) {
		alert("<spring:message code="resetPwd.unlocl" />");
		return false;
	} else {	
		if (confirm("<spring:message code="confirm.unlock"/>")) {
			var theURL="/pages/um/operInfo.do?action=unlock&userid="+userid;
			var features="500,350,operator.setpage,um";
			OpenModal(theURL,features)
		}
	} 
}

function usrGrp() {
	var idList
	var chkSet = document.all.tags("input")
	var j=0;
	for(var i=0;i<chkSet.length;i++) {
	    if(chkSet[i].name=="checkbox") {
			if(chkSet[i].checked) {
				j++;
				idList=chkSet[i].value
			}    
		}   
	 }
	if(j<1) {
		alert('<spring:message code="operator.update"/>')
		return false
	} else if (j>1) {
		alert('<spring:message code="operator.updateone"/>')
		return false
	} else {
		var strUrl="/pages/um/usrGrp.do?userid="+idList
		var features="500,350,operInfo.updateTitle,um"
		OpenModal(strUrl,features)
	} 
}
</script>
<body id="bodyid" leftmargin="0" topmargin="2">
<form name="operInfoForm" action="/pages/um/operInfo" method="post">
<table width="100%" cellSpacing="0" cellPadding="0">
<tr>
	<td>
	<fieldset class="jui_fieldset" width="100%">
	<legend><spring:message code="queryCondition.query" /></legend>
	<table width="100%">
	<tr>
		<td width="9%" align="right"><spring:message code="operInfoform.userIdentifier" />
		</td>
		<td width="10%"><input type="text" name="username" style="width:100%" /></td>
		<td width="6%" align="right"><spring:message code="label.user.name"/>: 
		</td>
		<td width="15%"><input type="text" name="username" style="width:100%" /></td>
	</tr>                                   
</table>
</fieldset>
	</td>
</tr>
</table>
<input type="hidden" name="pageSize" value="">
<input type="hidden" name="toPage" value="">
<input type="hidden" name="menuno" value='${menu.menuno}'>
</form>
<form name="formList">
<table width="100%" height="20" border="0" cellspacing="0" cellpadding="0" background="/shop/images/main/bgtop.gif">
<tr>
    <td width="3%" valign="middle">
		&nbsp;<img src="/shop/images/share/list.gif" width="14" height="16">
	</td>
	<td width="7%" class="orarowhead"><spring:message code="label.operate"/></td>
    <td width="90%" align="right"><view:button menuid="menu.menuno" type="session"/></td>
</tr>
</table>
<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066">  
<tr bgcolor="#FFFFFF"> 
  	<td>
     <table width="100%" cellSpacing="0" cellPadding="0">
     <tr>
    <td width="5%" align="center"> 
        <input type="checkbox" name="all" id=chkAll value="all" onClick="SelAll(this)">
    </td>
    <td width="12%"><label for=chkAll><spring:message code="operInfo.checkall" /></label></td>
    <td width="83%"><view:pagination pageName="currPage" formName="operInfoForm" action="/shop/pages/um/operinfo/list.do"/></td></tr></table>
	  </td>
	</tr>
</table>
  <table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066">
    <tr class="oracolumncenterheader"> 
      <td width="5%"><spring:message code="button.select"/></td>
      <td width="10%"><spring:message code="operInfo.operIdentifier" /></td>
      <td width="8%"><spring:message code="operInfo.opername" /></td>
	  <td width="20%"><spring:message code="operInfoform.usergrp" /></td>
	  <td width="24%"><spring:message code="operInfoform.userrole" /></td>
    </tr>
	<c:forEach var="operator" items="${operLst}" varStatus="index"> 
    <tr class="trClass${index.index%2 }" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)> 
      <td align="center"><input type="checkbox" name="checkbox" value='${operator.username}'></td>
      <td>${operator.username}</td>
      <td align="center">${operator.realname}</td>
	  <td>${operator.groupNames}</td>
	  <td>${operator.roleNames}</td>
      </tr>
    </c:forEach>  
	<c:forEach begin="0" end="${rowNum}" varStatus="index">
		<tr id="rownum${index.index}" class="trClass${index.index%2}" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
      <td height="20"></td>
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