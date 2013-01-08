<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/page/share/taglib.jsp"%>
<%@ page import="com.facetime.mgr.common.Constants"%>
<html>
<head><title>org info list</title></head>
<link href="/shop/css/css_v2.css" rel="stylesheet" type="text/css">
<script language="javascript" src="/shop/js/tablesort.js"></script>
<script language="javascript" type="text/javascript">
function SelAll(chkAll) {
     var chkSet = document.all.tags("input")
     for(var i=0;i<chkSet.length;i++) {
	    if(chkSet[i].name=="deleteOrgid") {
		   chkSet[i].checked=chkAll.checked
		}
	 }
}

function IsDeleteCheckValues(itemName){
	var aa = document.getElementsByName(itemName);
	var result = false;
	for (var i=0; i<aa.length; i++){
        if (aa[i].checked ){
	    	result=true;
			break;
	    }
     }
	 return result;
}

function add(){
	var theURL='/pages/org/orgMgr.do?action=add&parentid=${orgInfoForm.parentid}'
	ShowTitleModal(theURL,"600,550,orginfo.addinfo,org")
}

function modify(){
	var idList
	var chkSet = document.all.tags("input")
	var j=0;
   for(var i=0;i<chkSet.length;i++) {
	    if(chkSet[i].name=="deleteOrgid") {
			if(chkSet[i].checked) {
				j++;
				idList=chkSet[i].value
			}
		}
	 }
	if(j<1) {
		alert('<spring:message code="operator.update"/> ')
		return false
	} else if (j>1) {
		alert('<spring:message code="operator.updateone"/> ')
		return false
	} else {
		var theURL="/pages/org/orgMgr.do?action=modify&orgid="+idList
		ShowTitleModal(theURL,"600,550,operator.modify,org")
	}
}

function MODIFY() {
 	modify()
}

function del() {
	if(IsDeleteCheckValues('deleteOrgid')){
 		doyou = confirm('<spring:message code="back.deleteinfo"/> ')
		if (doyou == true){
			var strUrl="<%=request.getContextPath()%>/pages/org/orgMgr.do?action=delete&parentid="+orgInfoForm.parentid.value
			orgInfoForm.action = strUrl
			orgInfoForm.submit()
		}
   } else {
		alert('<spring:message code="check.deleteinfo"/> ');
	}
}

function setCardType() {
	var idList = ""
	var chkSet = document.all.tags("input")
	var j=0;
	for(var i=0;i<chkSet.length;i++) {
		if(chkSet[i].name=="deleteOrgid") {
			if(chkSet[i].checked) {
				j++;
				idList=chkSet[i].value
			}
		}
	 }
	if(j<1){
		alert('<spring:message code="operator.set"/> ')
		return false
	} else if (j>1) {
		alert('<spring:message code="operator.setone"/> ')
		return false
	} else {
		var theURL="<%=request.getContextPath()%>/pages/cardtype/cardType.do?action=query&orgid="+idList+"&menuno=402880ef1d4b5500011d4bc1cf5e0105"
		window.open(theURL,"carTypeWindow","height=650, width=800, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no")
	}
}

function ShowTitleModal(theURL,features) {
	var strReturn = OpenModal(theURL,features)
	if (strReturn) {
		getChild(orgInfoForm.parentid.value)
	}
}

function getChild(orgid) {
	var strUrl = "<%=request.getContextPath()%>/pages/org/orgMgr.do?action=list&parentid="+orgid
	orgInfoForm.action = strUrl
	orgInfoForm.submit()
}

function importFile(){
	orgInfoForm.action="<%=request.getContextPath()%>/pages/org/orgMgr.do?action=import";
	orgInfoForm.submit()
}

function outportFile() {
	var strUrl="<%=request.getContextPath()%>/pages/org/orgMgr.do?action=outport&parentid="+orgInfoForm.parentid.value
	orgInfoForm.action = strUrl
	orgInfoForm.submit()
}
</script>
<body id="bodyid" leftmargin="0" topmargin="0">
<form name="orgInfoForm" action="/pages/org/orgMgr" method="post">
<table width="100%" height="20" border="0" cellpadding="0" cellspacing="0" background="/shop/images/main/bgtop.gif">
<tr>
    <td width="3%" valign="middle">
		&nbsp;<img src="/shop/images/share/list.gif" width="14" height="16">
	</td>
    <td width="30%" class="orarowhead"><spring:message  code="orginfo.postiton" />: ${parentName}</td>
	<td width="67%" align="right">
		<input type="hidden" name="parentid" name="orgInfoForm"/>
		<c:if  test="${orgInfoForm.parentid  != hasChild}">
		 <view:button  menuid="menu.menuno" type="session"/>
		</c:if>
		<c:if test="${orgInfoForm.parentid  == hasChild}">
			<view:purview operId="<%=Constants.OPER_MODIFY_VALUE%>" imageUrl="/shop/images/share/export.gif" controlType="button" labelValue="button.export" controlName="btnExport" shortcutKey="E" visible="false">
			 <input type="button" name="modify" onClick="MODIFY()" value='<spring:message code="button.modify"/> ' class="MyButton" accesskey="M" image="/shop/images/share/modify.gif" >
			 <input type="button" name="cardtype" onClick="setCardType()" value='<spring:message code="orginfo.cardSetup" /> ' class="MyButton" accesskey="C" image="/shop/images/share/setting.gif" >
		     </view:purview>
				<view:purview operId="<%=Constants.OPER_IMPORT_VALUE%>" imageUrl="/shop/images/share/import.gif" controlType="button" labelValue="button.import" controlName="btnImport" shortcutKey="I" visible="false">
				<input name="btnImport" type="button" class="MyButton" id="btnImport" AccessKey="I" onClick="importFile()" value='<spring:message code="button.import"/> ' image="/shop/images/share/export.gif">
				</view:purview>
			  <view:purview operId="<%=Constants.OPER_EXPORT_VALUE%>" imageUrl="/shop/images/share/export.gif" controlType="button" labelValue="button.export" controlName="btnExport" shortcutKey="E" visible="false">
			  <input name="btnExport" type="button" class="MyButton" id="btnExport" AccessKey="E" onClick="outportFile()" value='<spring:message code="button.export"/> ' image="/shop/images/share/export.gif">
		     </view:purview>
	 </c:if>
	</td>
  </tr>
</table>
<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066">
  <tr bgcolor="#FFFFFF">
  	<td>
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr bgcolor="#FFFFFF">
    <td width="5%" align="center">
	   <input type="checkbox" name="all" id="all" value="CheckAll" onClick="SelAll(this)">
	</td>
	<td width="12%"><label for=chkAll><spring:message code="label.select.all"/> </label></td>
    <td width="83%"><spring:message code="orginfo.orgnum"/>: ${countAllNum}
	</td>
  </tr>
</table>
	</td>
  </tr>
</table>
<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066">
<tr class="oracolumncenterheader">
    <td width="5%"><spring:message code="button.select"/> </td>
    <td width="3%"></td>
    <td width="25%"><spring:message code="orginfo.orgname"/> </td>
	<td width="5%"><spring:message code="orginfo.seqno"/> </td>
    <td width="14%"><spring:message code="orginfo.orgcode"/> </td>
</tr>
<c:if  test="${orgInfoForm.parentid  != hasChild}">
<tr class="oracletdtwo">
    <td height="20"></td>
    <td></td>
	<td><a href="/shop/pages/org/back.do?parentid=${orgInfoForm.parentid }"><spring:message code="operator.backup"/> </a></td>
	<td></td>
	<td></td>
</tr>
</c:if>
<c:forEach var="orgInfo" items="${orgInfoList}" varStatus="index">
<tr class="trClass${index.index%2}" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
    <td align="center"><input name="deleteOrgid" type="checkbox" id="deleteOrgid" value='${orgInfo.orgid}'></td>
    <td align="center">
	  <c:if test="${orgInfo.childnum > 0 }"><img style="cursor:hand" src="/shop/images/share/foldericon.gif" onClick='getChild("${orgInfo.orgid}")'></c:if>
	  <c:if  test="${orgInfo.childnum == 0 }"><img src="/shop/images/share/fabiao.gif" ></c:if></td>
    <td><a href="/shop/pages/org/list.do?parentid=${orgInfo.orgid}">${orgInfo.orgname}</a></td>
	<td>${orgInfo.orgid}</td>
    <td>${orgInfo.orgcode}</td>
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