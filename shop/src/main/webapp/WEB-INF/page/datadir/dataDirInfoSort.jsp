<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/page/share/taglib.jsp" %>
<%@ include file="/inc/htc.inc"%>
<html>
<head><title></title></head>
<link href="/shop/css/css_v2.css" type=text/css rel=stylesheet>
<script language="JavaScript">
function MoveUp() {
    for (i=1; i<dataDirForm.lstItem.all.length; i++) {
        if (dataDirForm.lstItem.item(i).selected) {

          st = dataDirForm.lstItem.item(i-1).value ;
          dataDirForm.lstItem.item(i-1).value = dataDirForm.lstItem.item(i).value;
          dataDirForm.lstItem.item(i).value = st;

          st = dataDirForm.lstItem.item(i-1).text ;
          dataDirForm.lstItem.item(i-1).text = dataDirForm.lstItem.item(i).text;
          dataDirForm.lstItem.item(i).text = st;

          dataDirForm.lstItem.item(i-1).selected = true;
          dataDirForm.lstItem.item(i).selected = false;
        }
    }
}

function MoveDown() {
    for (i=dataDirForm.lstItem.all.length-2; i>=0; i--) {
        if (dataDirForm.lstItem.item(i).selected) {

          st = dataDirForm.lstItem.item(i+1).value ;
          dataDirForm.lstItem.item(i+1).value = dataDirForm.lstItem.item(i).value;
          dataDirForm.lstItem.item(i).value = st;

          st = dataDirForm.lstItem.item(i+1).text ;
          dataDirForm.lstItem.item(i+1).text = dataDirForm.lstItem.item(i).text;
          dataDirForm.lstItem.item(i).text = st;

          dataDirForm.lstItem.item(i+1).selected = true;
          dataDirForm.lstItem.item(i).selected = false;
        }
    }
}

function SubmitForm() {
    var st = "";
    for (i=0; i<dataDirForm.lstItem.all.length; i++) {
        st += dataDirForm.lstItem.item(i).value + ",";
    }

	if(st.length>0) {
		st=st.substring(0,st.length-1);
		dataDirForm.idList.value = st;
		parent.returnValue=true
		return true;
	} else {
	     alert("<spring:message code="sort.save.noItems"/> ");
	     return false;
     }
}
</script>
<body id="bodyid" leftmargin="10" topmargin="10">
<form name="dataDirForm" action="/shop/pages/datadir/sort.do" method="post" onsubmit="return SubmitForm()">
<input type="hidden" name="parentid" value="${dataDirForm.parentid}"/>
<table border="0" cellpadding="1" width="100%" cellspacing="1" class="tablebg" align="center">
<tr>
	<td class="oracolumncenterheader" colspan="2" height="24"><spring:message code="menuInfo.title.sort"/> 
	</td>
</tr>
<tr>
	<td width="18%" class="oracletdtwo" align="center">
		<input type="hidden" name="idList" value="">
		<input type="button" name="btnup" value='<spring:message code="button.up"/> ' class="MyButton" image="/shop/images/share/up.png" onClick="MoveUp()"><br><br>
		<input type="button" name="btndown" value='<spring:message code="button.down"/> ' class="MyButton" image="/shop/images/share/down.png" onClick="MoveDown()">
	</td>
	<td width="82%" class="oracletdone" align="center">
		<select name="lstItem" multiple style="width:98%;height:200;">
		 <c:forEach items="${dataDirInfoLst}" var="dir">
			<option value="${dir.id}" selected>${dir.note}</option>
		 </c:forEach>
        </select>
	</td>
</tr>
<tr>
	<td colspan="2" bgcolor="#FFFFFF" height="30" align="center">
		<input type="submit" name="btnSave" value='<spring:message code="button.save"/> ' class="MyButton" accesskey="S" image="/shop/images/share/save.gif">
        <input type="button" name="btnClose" value='<spring:message code="button.close"/> ' class="MyButton" AccessKey="C" image="/shop/images/share/f_closed.gif" onClick="window.close()">
	</td>
</tr>
<tr>
	<td bgcolor="#F5F5F5" align="center">
			<table width="93%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td align="center"><img src="/shop/images/share/lamp.gif"></td>
				</tr>
			</table>
	</td>
	<td bgcolor="#FFFFFF" valign="top">&nbsp;
		<table width="99%" border="0" align="center" cellpadding="0" cellspacing="2" height="100%">
				<tr>
					<td valign="bottom">
						<font color="#FF0000"><spring:message code="tips.title"/> </font>
					</td>
				</tr>
				<tr>
					<td valign="top"><spring:message code="sort.tips.content"/> </td>
				</tr>
			</table>
	</td>
</tr>
</table>
</form>
</body>
</html>