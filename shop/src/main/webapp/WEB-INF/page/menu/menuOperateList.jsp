<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<html>
<head><title><spring:message code="menuOperateInfo.table1title" />  </title></head>
<link href="/shop/css/css_v2.css" type="text/css" rel="stylesheet">
<script language="javascript" src="/shop/js/tablesort.js"></script>
<script language="JavaScript">
function SelAll(chkAll) {
	var chkSet = document.all.tags("input")
     for(var i=0;i<chkSet.length;i++) {
	    if(chkSet[i].name=="chkList") {
		   chkSet[i].checked=chkAll.checked
		}   
	 }
}

function query() {
    menuOperateForm.toPage.value = formList.toPage.value
	menuOperateForm.pageSize.value = formList.pageSize.value
	menuOperateForm.submit();
}

function add() {
	location.href="/shop/pages/menuoperate/addUI.do";
}

function GetSelIds() {
     var idList=""
     var chkSet = document.all.tags("input")
     for(var i=0;i<chkSet.length;i++)  {
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

function del() {
   var idList=GetSelIds();
   if(idList=="") {
       alert('<spring:message code="del.selectNone"/>  ')
	   return false
   }
   if(!confirm('<spring:message code="menuOperate.del.confirm" />  ')) {
      return false
   }
	location.href="/shop/pages/menuoperate/delete.do?idList="+idList;
}

function modify(){
   var aa=document.getElementsByName("chkList");
   var code
   var j=0;
   for (var i=0; i<aa.length; i++){
   		if (aa[i].checked){
			code=aa[i].value;
			j=j+1;
		}
   }
	if (j==0)
 		alert('<spring:message code="operator.update"/> ')
 	else if (j>1)
 		alert('<spring:message code="operator.updateone"/> ')
	else{
		location.href="/shop/pages/menuoperate/updateUI.do?objcode="+code;
	}
}
</script>
<body id="bodyid" leftmargin="0" topmargin="0">
<form action="/shop/pages/menuoperate/list.do" method="post" name="menuOperateForm">
<input name="toPage" type="hidden">
<input name="pageSize" type="hidden">
<input name="idList" type="hidden">
<input name="menuno" value='${"menuoperate.menuno"}' type="hidden">
</form>
<form name="formList" method="post">
<table width="100%" height="20" border="0" cellspacing="0" cellpadding="0" background="/shop/images/main/bgtop.gif">
  <tr>
    <td width="3%" valign="middle">&nbsp;<img src="/shop/images/share/list.gif" width="14" height="16"></td>
    <td width="40%" class="orarowhead"><spring:message code="menuOperateInfo.table1title"/>  
	</td>
	<td width="57%" align="right">
		<view:button menuid="menuoperate.menuno" type="session"/>
	</td>
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
  <td width="83%"><view:pagination pageName="currPage" formName="menuOperateForm"/></td></tr></table>
		</td>
	</tr>
</table>
<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
<tr class="oracolumncenterheader"> 
	<td width="5%"><spring:message code="label.select"/>  </td>
	<td width="18%">操作ID</td>
	<td width="18%"><spring:message code="menuOperateInfoForm.opername" />  </td>
	<td width="20%"><spring:message code="menuOperateInfoForm.clickname" />  </td>
	<td width="30%"><spring:message code="menuOperateInfoForm.picpath" />  </td>
	<td width="12%"><spring:message code="menuOperateInfoForm.keys" />  </td>
	<td width="15%"><spring:message code="menuOperateInfoForm.types" />  </td>
</tr>
<c:forEach items="${grpLst}" var="item" varStatus="index">
<tr class="trClass${index.index%2}" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this) onClick="pass(downloadList,this)">
	<td align="center">
	<input type="checkbox" name="chkList" value='${item.operid}'>
	</td>
	<td>${item.operid}</td>
	<td>${item.opername}</td>
	<td>${item.clickname}</td>
	<td>${item.picpath}</td>
	<td align="center">${item.keys}</td>
	<td align="center">${item.types}</td>	  
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
	</tr>		
	</c:forEach>
</table>
</form>		
</body>
</html>