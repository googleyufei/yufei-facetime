<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/page/share/taglib.jsp"%>
<html>
<head><title>menuInfoList</title></head>
<link href="/shop/css/css_v2.css" type="text/css" rel="stylesheet">
<script language="javascript" src="/shop/js/tablesort.js"></script>
<script language="javascript" src="/shop/js/jquery.min.1.7.js"></script>
<script language="javascript">
function SelAll(chkAll) {
	var chkSet = document.all.tags("input");
	for(var i=0;i<chkSet.length;i++) {
		if(chkSet[i].name=="chkList") {
			chkSet[i].checked=chkAll.checked;
		}   
	 }
}

function ComeIn(itemId) {
	var strUrl="<%=request.getContextPath()%>/pages/menu/list.do?control=back&menuid="+itemId;
	menuInfoForm.action = strUrl;
	menuInfoForm.submit();
}

function GetSelIds() {
     var idList="";
     var chkSet = document.all.tags("input");
     for(var i=0;i<chkSet.length;i++) {
	    if(chkSet[i].name=="chkList") {
		   if(chkSet[i].checked) {
		      idList+=","+chkSet[i].value;
		   }    
		}   
	 }	 
	 if(idList=="")
	    return "";
	 return idList.substring(1);
}

function getMyChild(menuid) {
	var strUrl = "<%=request.getContextPath()%>/pages/menu/list.do?menuid=" + menuid;
	document.menuInfoForm.action = strUrl;
	document.menuInfoForm.submit();
}

function add() {
	var strUrl="/shop/pages/menu/addUI.do?parentid=${menuInfoForm.parentid}&floor=${menuInfoForm.floor}";
	location.href=strUrl;
}

function ModifyItem(itemId) {
	var strUrl="/shop/pages/menu/updateUI.do?menuid="+itemId;
	location.href = strUrl;
}

function test(){
	window.showModalDialog(strHref,window,"dialogWidth:"+width+"px;dialogHeight:"+height+"px;resizable:no;scroll:0;help:0;status:0");
}

function del() {
	var idList=GetSelIds();
	if(idList=="") {
		alert('<spring:message code="del.selectNone"/>  ');
		return false
	}
	if(!confirm('<spring:message code="menuOperate.del.confirm" />  ')) {
		return false;
	}
	var strUrl="/shop/pages/menu/delete.do?menuid="+idList;
	location.href=strUrl;
}

function Sort() {
	location.href="/shop/pages/menu/sortUI.do?menuid=${menuInfoForm.parentid}";
}

function setfunMenu(itemId) {
	location.href="/shop/pages/menu/setting.do?&menuid="+itemId;
}

function modify(){
	var idList;
	var chkSet = document.all.tags("input");
	var j=0;
	for(var i=0;i<chkSet.length;i++) {
	    if(chkSet[i].name=="chkList") {
			if(chkSet[i].checked) {
				j++;
				idList=chkSet[i].value;
			}    
		}   
	 }

	if(j<1){
		alert('<spring:message code="operator.update"/>  ');
		return false;
	} else if (j>1) {
		alert('<spring:message code="operator.updateone"/>  ');
		return false;
	} else {
		ModifyItem(idList);
	}   		
}

function setfun(){
	var idList;
	var chkSet = document.all.tags("input");
	var j=0;
	for(var i=0;i<chkSet.length;i++) {
	    if(chkSet[i].name=="chkList") {
			if(chkSet[i].checked) {
				j++;
				idList=chkSet[i].value;
			}    
		}   
	 }

	if(j<1) {
		alert('<spring:message code="operator.set"/>  ');
		return false;
	} else if (j>1){
		alert('<spring:message code="operator.setone"/>  ');
		return false;
	} else {
		setfunMenu(idList);
	}
}
</script>

<body id="bodyid" leftmargin="0" topmargin="0">
<form action="/shop/pages/menu/list" method="post" name="menuInfoForm" id="menuInfoForm">
<table width="100%" height="20" border="0" cellspacing="0" cellpadding="0" background="/images/main/bgtop.gif">
<tr>
    <td width="3%" valign="middle">&nbsp;<img src="/shop/images/share/list.gif" width="14" height="16"></td>
    <td width="41%" class="orarowhead">
    <spring:message code="menuInfo.navigation"/>  :
    	<c:if test="${menuNavigation !=null}">
    		${menuNavigation}
    	</c:if>
	</td>
	<td width="56%" align="right"><view:button menuid="menu.menuno" type="session"/></td>
  </tr>
</table>
<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066">
<tr bgcolor="#FFFFFF"> 
  	<td>
      <table width="100%" cellSpacing="0" cellPadding="0">
      <tr>
		<td width="4%" align="center">
          <input type="checkbox" name="chkAll" id="chkAll" value="all" onClick="SelAll(this)">
		</td>
		<td width="13%"><label for=chkAll><spring:message code="label.select.all"/>  </label></td>
		<td width="83%"><spring:message code="page.nav.total"/>  ${rowNum}</td>
	</tr>
    </table>
    </td>
  </tr>
</table>
<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066">
<tr class="oracolumncenterheader"> 
	<td width="4%"><spring:message code="label.select"/>  </td>
	<td width="3%"></td>
	<td width="12%"><spring:message code="menuInfoForm.menuname"/>  </td>
	<td width="26%"><spring:message code="menuInfoForm.menuname.English"/>  </td>
	<td width="45%"><spring:message code="menuInfoForm.setorder" />  </td>
	<td width="10%"><spring:message code="menuInfoForm.isExistImage" />  </td>
</tr>
<c:if test="${menuInfoForm.parentid != 0 }">
<tr class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)> 
	<td height="20"></td>
	<td></td>
	<td><a href='javascript:ComeIn("${menuInfoForm.parentid}")'><spring:message code="menuInfo.list.back" />  </a>
	</td>
	<td></td>
	<td></td>
	<td></td>
</tr>
</c:if>

<c:forEach items="${menuList}" var="menu" varStatus="index">
<tr class="trClass${index.index%2}" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)> 
	<td align="center">
		<input type="checkbox" name="chkList" value='${menu.menuid}'>
	</td>
	<td align="center">
	<c:if test="${menu.childnum >0}">
        <img src="/shop/images/share/foldericon.gif" style="CURSOR: hand"  onClick="getMyChild('${menu.menuid}');"> 
	</c:if>
	<c:if test="${menu.childnum == 0}">
        <img src="/shop/images/share/fabiao.gif"> 
	</c:if>
	</td>
    <td><a href='#' onclick="getMyChild('${menu.menuid}')">${menu.menuitem}</a>
	</td>
	<td>${menu.menuitemEn}</td>
	<td>${menu.actionto }</td>
	<td align="center">
	  <c:if test="${not empty  menu.image}">
			<img src="/shop/images/share/button_ok.gif">
	  </c:if>
	</td>
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