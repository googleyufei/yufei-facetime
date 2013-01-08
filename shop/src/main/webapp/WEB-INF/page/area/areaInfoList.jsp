<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/page/share/taglib.jsp"%>
<%@ page import="com.facetime.mgr.common.Constants"%>
<html>
<head><title>test</title></head>
<link href="/shop/css/css_v2.css" type="text/css" rel="stylesheet">
<script language="javascript" src="/shop/js/tablesort.js"></script>
<script language="javascript" type="text/javascript">
function SelAll(chkAll) {
     var chkSet = document.all.tags("input")
     for(var i=0;i<chkSet.length;i++) {
	    if(chkSet[i].name=="deleteAreaid") {
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
	var theURL="/pages/geog/areaMgr.do?action=add&parentid=${areaInfoForm.parentid}"
	ShowTitleModal(theURL,"500,410,geogarea.addinfo,geog")
}

function modify(){
	var idList
	var chkSet = document.all.tags("input")
	var j=0;
   for(var i=0;i<chkSet.length;i++) {
	    if(chkSet[i].name=="deleteAreaid") {
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
		var theURL="/pages/geog/areaMgr.do?action=modify&areaid="+idList
		ShowTitleModal(theURL,"500,410,operator.modify,org")
	}
}

function del() {
	if(IsDeleteCheckValues('deleteAreaid')){
		doyou = confirm('<spring:message code="back.deleteinfo"/> ');
		if (doyou == true){
			var strUrl="<%=request.getContextPath()%>/pages/geog/areaMgr.do?action=delete&parentid="+areaInfoForm.parentid.value
			areaInfoForm.action = strUrl
			areaInfoForm.submit()
		}
	} else {
		alert('<spring:message code="check.deleteinfo"/> ')
	}
}

function ShowTitleModal(theURL,features){
	var strReturn = OpenModal(theURL,features)
	if (strReturn) {
		getChild(areaInfoForm.parentid.value)
	}
}

function getChild(areaid) {
	var strUrl = "<%=request.getContextPath()%>/pages/geog/areaMgr.do?action=list&parentid="+areaid
	areaInfoForm.action = strUrl
	areaInfoForm.submit()
}
</script>
<body id="bodyid" leftmargin="0" topmargin="0">
<form name="areaInfoForm" action="/shop/pages/area/list.do" method="post">
<table width="100%" height="20" border="0" cellpadding="0" cellspacing="0" background="/shop/images/main/bgtop.gif">
  <tr>
    <td width="3%" valign="middle">&nbsp;<img src="/shop/images/share/list.gif" width="14" height="16"></td>
    <td width="30%" class="orarowhead"><spring:message code="geogarea.postiton"/>: ${parentName}</td>
	<td width="67%" align="right">
	<input type="hidden"  name="parentid" value="${areaInfoForm.parentid }"/>
	<c:if test="${areaInfoForm.parentid != hasChild }">
		<view:button  menuid="menu.menuno" type="session"/>
	</c:if>
	<c:if test="${areaInfoForm.parentid == hasChild }">
	<view:purview operId="<%=Constants.OPER_MODIFY_VALUE%>" imageUrl="/shop/images/share/modify.gif" controlType="button" labelValue="button.modify" controlName="btnModify" shortcutKey="M" visible="false">
		 <input type="button" name="btnModify" onClick="modify()" value='<spring:message code="button.modify"/> ' class="MyButton" accesskey="M" image="/shop/images/share/modify.gif" >
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
	<td width="83%"><spring:message code="geogarea.areanum" />:  ${countAllNum}</td>
  </tr>
</table>
	</td>
  </tr>
</table>
<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066">
  <tr class="oracolumncenterheader">
    <td width="5%"><spring:message code="button.select"/> </td>
    <td width="3%"></td>
    <td width="30%"><spring:message code="geogarea.areaname"/> </td>
    <td width="29%"><spring:message code="geogarea.areaid"/> </td>
    <td width="33%"><spring:message code="geogarea.note"/> </td>
  </tr>
  <c:if test="${areaInfoForm.parentid != hasChild }">
  <tr class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
    <td></td>
    <td></td>
    <td height="20">
      <a href="/shop/pages/area/back.do?parentid=${areaInfoForm.parentid }"><spring:message code="operator.backup"/> </a>
    </td>
    <td></td>
    <td></td>
  </tr>
  </c:if>
<c:forEach var="geogArea" items="${areaInfoList}" varStatus="index">
<tr class="trClass${index.index%2 }" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
    <td align="center"><input name="deleteAreaid" type="checkbox" id="deleteAreaid" value='${geogArea.areaid}'></td>
    <td align="center">
		<c:if test="${geogArea.childnum > 0}"> <img src="/shop/images/share/foldericon.gif" style="cursor:hand" onClick='getChild("${geogArea.areaid}")'></c:if>
		<c:if test="${geogArea.childnum == 0}"><img src="/shop/images/share/fabiao.gif"> </c:if> </td>
    <td><a href="/shop/pages/area/list.do?parentid=${geogArea.areaid }">${geogArea.name}</a></td>
    <td>${geogArea.areaid}</td>
    <td>${geogArea.note}</td>
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