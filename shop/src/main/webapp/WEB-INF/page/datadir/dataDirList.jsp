<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/page/share/taglib.jsp" %>
<%@ include file="/inc/htc.inc"%>
<html>
<head><title>dataDirList</title></head>
<link href="/shop/css/css_v2.css" type="text/css" rel="stylesheet">
<script language="javascript" src="/shop/js/tablesort.js"></script>
<script language="javascript">
function SelAll(chkAll) {
	var chkSet = document.all.tags("input")
	for(var i=0;i<chkSet.length;i++) {
	    if(chkSet[i].name=="chkList") {
		   chkSet[i].checked=chkAll.checked
		}
	 }
}

function ComeIn(itemId) {
   var strUrl="<%=request.getContextPath()%>/pages/datadir/list.do?id="+itemId;
   dataDirForm.action = strUrl
   dataDirForm.submit()
}

function ComeBack(itemId) {
   var strUrl="<%=request.getContextPath()%>/pages/datadir/list.do?mark=back&id="+itemId
   dataDirForm.action = strUrl
   dataDirForm.submit()
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

function add() {
	location.href="/shop/pages/datadir/addUI.do?parentid=${dataDirForm.id}";
}

function modify(){
	var idList
	var chkSet = document.all.tags("input")
	var j=0;
	for(var i=0;i<chkSet.length;i++) {
	    if(chkSet[i].name=="chkList") {
			if(chkSet[i].checked) {
				j++;
				idList=chkSet[i].value
			}
		}
	 }

	if(j<1){
		alert('<spring:message code="operator.update"/> ')
		return false
	} else if (j>1){
		alert('<spring:message code="operator.updateone"/> ')
		return false
	} else {
		ModifyItem(idList)
	}
}

function ModifyItem(itemId) {
	location.href="/shop/pages/datadir/modifyUI.do?id="+itemId;
}

function getMyChild(dataid) {
	var strUrl = "<%=request.getContextPath()%>/pages/datadir/list.do?id=" + dataid;
	document.dataDirForm.action = strUrl;
	document.dataDirForm.submit();
}

function del() {
	var idList=GetSelIds()
	if(idList=="") {
		alert('<spring:message code="del.selectNone"/> ')
		return false
	}
	if(!confirm('<spring:message code="dataDir.del.confirm"/> '))  {
		return false
	}
	location.href="/shop/pages/datadir/delete.do?parentid=${dataDirForm.id}&id="+idList;
}

function Sort() {
	location.href="/shop/pages/datadir/sortUI.do?parentid=${dataDirForm.id}";
}
</script>
<body id="bodyid" leftmargin="0" topmargin="0">
<form name="dataDirForm" action="/shop/pages/datadir/list.do" method="post">
<table width="100%" height="20" border="0" cellspacing="0" cellpadding="0" background="/shop/images/main/bgtop.gif">
  <tr>
    <td width="3%" valign="middle">&nbsp;<img src="/shop/images/share/list.gif" width="14" height="16"></td>
    <td width="56%" class="orarowhead"><spring:message code="dataDir.navigation"/> : 
    <c:if test="${not empty dirNavigation }">${dirNavigation }</c:if>
	</td>
	<td align="right" width="41%"><view:button menuid="menu.menuno" type="session" />
	</td>
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
            <td width="13%"><label for=chkAll><spring:message code="label.select.all"/> </label></td>
    <td width="83%"><spring:message code="page.nav.total"/>${rowNum} </td>
  </tr>
</table>
</td></tr>
</table>
<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066">
<tr class="oracolumncenterheader">
    <td width="4%"><spring:message code="label.select"/> </td>
    <td width="3%"></td>
    <td width="20%"><spring:message code="label.note"/> </td>
    <td width="20%"><spring:message code="label.key"/> </td>
    <td width="53%"><spring:message code="label.value"/> </td>
</tr>
<c:forEach items="${dirList}" var="dirList" varStatus="index">
	<c:if test="${dirList.parentid != 0 && index.index == 0}">
	<tr class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
	    <td height="20"></td>
	    <td></td>
	    <td><a href='javascript:ComeBack("${dirList.parentid}")'><spring:message code="dataDir.list.back"/> </a></td>
	    <td></td>
	    <td></td>
	    </tr>
	</c:if>
	<tr class="trClass${index.index%2}"  oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
	    <td align="center"><input type="checkbox" name="chkList" value='${dirList.id}'>    </td>
	    <td align="center">
		    <c:if test="${dirList.childnum > 0 }">
		      <img src="/shop/images/share/foldericon.gif" style="cursor:hand" onClick='ComeIn("${dirList.id}")'>
		    </c:if>
		    <c:if  test="${dirList.childnum==0 }" >
		      <img src="/shop/images/share/fabiao.gif">
		    </c:if>
		</td>
	    <td title='[<spring:message code="dataDirSaveForm.noteEn"/> ]${dirList.noteEn}'>
	      <a href='#' onclick="getMyChild('${dirList.id}')">${dirList.note}</a>
		</td>
	    <td>${dirList.key}</td>
	    <td>${dirList.value}</td>
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