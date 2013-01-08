<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/page/share/taglib.jsp"%>
<html>
<head><title>role info</title></head>
<link href="/shop/css/css_v2.css" type="text/css" rel="stylesheet">
<script language="javascript" src="/shop/js/tablesort.js"></script>
<script language="JavaScript">	

function getChecked(){
	var idStr=[];
	  $("input:checkbox:checked").each(function(){
		if(idStr) 
			 idStr[idStr.length] =$(this).val() ;
		else 
			idStr[0]=$(this).val();
	});
	  return idStr;
}

function isChecked() {
	var idList="";
    var  em= roleInfoForm.elements;
	for(var  i=0;i<em.length;i++) {
	       if(em[i].type=="checkbox") {
		       if(em[i].checked) {
					if (em[i].value == "administrator") {
						alert('<spring:message code="operDel.admin" />')
						return false
					}
			       idList+=","+em[i].value;
			   }
		   }
	}
	if(idList=="") {
		alert("<spring:message code="roleInfo.plscheck" />");
		return false;
	}
 }
	
function ShowTitleModal(theURL,features) {
	var returnValue = OpenModal(theURL,features)
	if (returnValue)
		roleInfoForm.submit()
}
 
function ShowUpdateWindow(roleid) {
	var strUrl="/pages/um/roleInfo.do?action=modifyUI&rolecode="+roleid
	var features="350,250,roleInfo.updateRole,um"
	ShowTitleModal(strUrl,features)
}

function ConfirmDel() {
	if (isChecked()==false)  return false
	if (confirm("<spring:message code="roleInfo.removeConfirm" />")) {
		roleInfoForm.action="<%=request.getContextPath()%>/pages/um/roleInfo.do?action=delete";
		roleInfoForm.submit();
    }
}

function GroupMenu(rolecode) {
	location.href="<%=request.getContextPath()%>/pages/um/roleinfo/purview.do?rolecode="+rolecode;
}

function add(){
	location.href="/shop/pages/um/roleinfo/addUI.do";
}

function del(){
	ConfirmDel()
}

function modify(){
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
	} else if (j>1){
		alert('<spring:message code="operator.updateone"/>')
		return false
	} else {
		ShowUpdateWindow(idList)
	}   		
}

function groupMenu(){
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
		alert('<spring:message code="operator.set"/>')
		return false
	} else if (j>1) {
		alert('<spring:message code="operator.setone"/>')
		return false
	} else {
		GroupMenu(idList)
	}   		
}
</script>
<body id="bodyid" leftmargin="0" topmargin="0">
<form name="roleInfoForm" action="/shop/pages/um/roleinfo/list.do" method="post">
<input type="hidden" name="rolecode" value="">
<input type="hidden" name="menuno" value='${menu.menuno}'>
<table width="100%" height="20" border="0" cellspacing="0" cellpadding="0" background="/shop/images/main/bgtop.gif">
<tr>
	<td width="3%" valign="middle">&nbsp;<img src="/shop/images/share/list.gif" width="14" height="16"></td>
	<td width="30%" class="orarowhead"><spring:message code="roleInfo.table2title" /></td>
	<td width="67%" align="right"><view:button menuid="menu.menuno" type="session"/></td>
</tr>
</table>
<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066">
<tr class="oracolumncenterheader"> 
	<td width="10%"><spring:message code="label.select"/></td>
	<td width="90%"><spring:message code="roleInfoForm.roleName" /></td>
</tr>
<c:if test="${fn:length(roleLst) > 0}">
<c:forEach var="role" items="${roleLst}" varStatus="index"> 
<tr class="trClass${index.index%2 }" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)> 
	<td align="center">
	  <input type="checkbox" name="checkbox" value='${role.rolecode}'> 
	</td>
	<td align="center">${role.rolename}</td>
</tr>
</c:forEach> 
	<c:forEach begin="0" end="${rowNum}" varStatus="index">
		<tr id="rownum${index.index}" class="trClass${index.index%2}" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
      <td height="20"></td>
      <td></td>
</tr>
</c:forEach>
</c:if>
</table>
</form>
</body>
</html>