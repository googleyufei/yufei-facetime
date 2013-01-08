<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/page/share/taglib.jsp"%>
<html>
<head><title>group info</title></head>
<link href="/shop/css/css_v2.css" type="text/css" rel="stylesheet">
<script language="javascript" src="/shop/js/tablesort.js"></script>
<script language="JavaScript">
function isChecked(){
    var idList=""
    var em = grpInfoForm.elements
	for(var i=0;i<em.length;i++) {
		if(em[i].type=="checkbox") {
			if(em[i].checked) {
				if (em[i].value == "administrator") {
			       alert('<spring:message code="operDel.admin" />')
				   return false
			   	}
				idList+=em[i].value+",";
			}
		}
	}
    if(idList.length > 0)
    	idList = idList.substring(0, idList.length - 1);
    return idList;
}	

function ShowTitleModal(theURL,features){
	var returnValue = OpenModal(theURL,features)
	if (returnValue)
		grpInfoForm.submit()
} 

function ShowUpdateWindow(grpid){
	var strUrl="/pages/um/grpInfo.do?action=modifyUI&grpcode="+grpid
	var features="350,250,grpInfo.updateTitle,um"
	ShowTitleModal(strUrl,features)
}

function add(){
   location.href="/shop/pages/um/grpinfo/addUI.do";
}

function del(){
	var idList = isChecked();
	if(idList=="") {
		alert('<spring:message code="grpInfo.plsCheck" />');
		return false;
	}
	if (confirm("<spring:message code="grpInfo.removeConfirm" />")) {
		var strUrl="/pages/um/grpInfo.do?action=delete&grpcode="+idList;
		var features="450,450,grpInfo.updateTitle,um"
		ShowTitleModal(strUrl,features);
    }
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
	} else if (j>1) {
		alert('<spring:message code="operator.updateone"/>')
		return false
	} else {
		ShowUpdateWindow(idList)
	}   		
}


function roleGroup(){
	var idStr=getChecked();
   if(idStr.length<1){
	  alert('<spring:message code="operator.set"/>')
	   return false
	} else if (idStr.length>1){
		alert('<spring:message code="operator.setone"/>')
		return false
	} else {
		grpInfoForm.action="<%=request.getContextPath()%>/pages/um/grpinfo/setRole.do?grpcode="+idStr.join(",");
		grpInfoForm.submit();
	}   		
}

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

function orgMenu(){
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
	  alert('<spring:message code="operator.set"/>')
	   return false
	} else if (j>1){
	 alert('<spring:message code="operator.setone"/>')
	  return false
	} else {
		OrgMenu(idList)
	} 
}
</script>
<body id="bodyid" leftmargin="0" topmargin="0">
<form  name="grpInfoForm" action="/shop/pages/um/grpinfo/list.do" method="post">
<input type="hidden" name="menuno" value='${menu.menuno}'>
<input type="hidden" name="grpcode">
<table width="100%" height="20" border="0" cellspacing="0" cellpadding="0" background="/shop/images/main/bgtop.gif">
<tr>
	<td width="3%" valign="middle">
		&nbsp;<img src="/shop/images/share/list.gif" width="14" height="16">
	</td>
	<td width="29%" class="orarowhead"><spring:message code="grpInfo.table2title" /></td>
	<td width="68%" align="right">
	 <view:button menuid="menu.menuno" type="session"/>
	</td>
</tr>
</table>		
<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
    <tr class="oracolumncenterheader"> 
      <td width="10%"><spring:message code="label.select"/></td>
      <td width="90%"><spring:message code="grpInfoForm.grpname" /></td>
    </tr>
    <c:forEach var="grp" items="${grpLst}" varStatus="index"> 
    <tr class="trClass${index.index%2}" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)> 
      <td align="center"><input type="checkbox" name="checkbox" value='${grp.grpcode}'/> 
      </td>
      <td align="center">${grp.grpname}</td>
    </tr>
    </c:forEach> 
<c:forEach begin="0" end="${rowNum}" varStatus="index">
		<tr id="rownum${index.index}" class="trClass${index.index%2}" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
      <td height="20"></td>
      <td></td>
    </tr>
    </c:forEach>
</table>
</form>
</body>
</html>