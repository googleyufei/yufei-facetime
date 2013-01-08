<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.facetime.core.utils.HtmlUtils,java.util.*"%>
<%@ page import="com.facetime.mgr.bean.SortRequestForm"%>
<%@ page import="com.facetime.mgr.domain.MenuInfo"%>
<%
    SortRequestForm reqForm=(SortRequestForm)request.getAttribute("sortRequestForm");
    List childList=(List)request.getAttribute("menuInfoLst");
%>
<html>
<head><title></title></head>
<link href="/shop/css/css_v2.css" type="text/css" rel="stylesheet">
<script language="javascript" src="/shop/js/jquery.min.1.7.js"></script>
<script type="text/javascript">
$(function(){
	$("input[name=btnup]").bind("click",upMove);
	$("input[name=btndown]").bind("click",downMove);
});

function upMove() {
    for (var i=1; i<menuInfoSortForm.lstItem.all.length; i++) {
		if (menuInfoSortForm.lstItem.item(i).selected) {
			st = menuInfoSortForm.lstItem.item(i-1).value ;
			menuInfoSortForm.lstItem.item(i-1).value = menuInfoSortForm.lstItem.item(i).value;
			menuInfoSortForm.lstItem.item(i).value = st;

			st = menuInfoSortForm.lstItem.item(i-1).text ;
			menuInfoSortForm.lstItem.item(i-1).text = menuInfoSortForm.lstItem.item(i).text;
			menuInfoSortForm.lstItem.item(i).text = st;

			menuInfoSortForm.lstItem.item(i-1).selected = true;
			menuInfoSortForm.lstItem.item(i).selected = false;
		}
    }
}

function downMove() {
	for (var i=menuInfoSortForm.lstItem.all.length-2; i>=0; i--) {
		if (menuInfoSortForm.lstItem.item(i).selected) {
			st = menuInfoSortForm.lstItem.item(i+1).value ;
			menuInfoSortForm.lstItem.item(i+1).value = menuInfoSortForm.lstItem.item(i).value;
			menuInfoSortForm.lstItem.item(i).value = st;

			st = menuInfoSortForm.lstItem.item(i+1).text ;
			menuInfoSortForm.lstItem.item(i+1).text = menuInfoSortForm.lstItem.item(i).text;
			menuInfoSortForm.lstItem.item(i).text = st;

			menuInfoSortForm.lstItem.item(i+1).selected = true;
			menuInfoSortForm.lstItem.item(i).selected = false;
		}
    }
}

function SubmitForm() {
	var st = "";
    for (i=0; i<menuInfoSortForm.lstItem.all.length; i++) {
        st += menuInfoSortForm.lstItem.item(i).value + ",";
    }
	if(st.length>0) {
		st=st.substring(0,st.length-1);
	  	menuInfoSortForm.idList.value = st;
		window.returnValue=true;
		return true;
	} else {
		alert("<spring:message code='sort.save.noItems'/>");
		return false;
	}
}
</script>
<body id="bodyid" leftmargin="10" topmargin="10">
<form name="menuInfoSortForm" action="/shop/pages/menu/sort.do" method="post" onsubmit="return SubmitForm()">
<table border="0" cellpadding="1" width="100%" cellspacing="1" class="tablebg" align="center">
<tr>
	<td class="oracolumncenterheader" colspan="2" height="24"><spring:message code="menuInfo.title.sort"/>  
	</td>
</tr>
<tr>
	<td width="18%" class="oracletdtwo" align="center">
		<input type="hidden" name="idList" value="">
		<input type="button" name="btnup" value='向上' class="MyButton" image="/shop/images/share/up.png"><br><br>
		<input type="button" name="btndown" value='向下' class="MyButton" image="/shop/images/share/down.png" >
	</td>
	<td width="82%" class="oracletdone" align="center">
		<select name="lstItem" multiple style="width:98%;height:200;">
		<%
			int size=childList.size();
			MenuInfo menuinfo;
			Iterator itRec=childList.iterator();
			if(itRec.hasNext()) {
				menuinfo = (MenuInfo)itRec.next();
		%>
		<option value="<%=menuinfo.getMenuid()%>" selected>
			<%=HtmlUtils.htmlEncode(menuinfo.getMenuitem())%>
		</option>
		<%
			}
			while(itRec.hasNext()) {
				menuinfo=(MenuInfo)itRec.next();
		%>
		<option value="<%=menuinfo.getMenuid()%>">
			<%=HtmlUtils.htmlEncode(menuinfo.getMenuitem())%>
		</option>
		<%
			}
		%>
		</select>
	</td>
</tr>
<tr>
	<td colspan="2" bgcolor="#FFFFFF" height="30" align="center">
		<input type="submit" name="btnSave" value='保存' class="MyButton" accesskey="S" image="/shop/images/share/save.gif">
        <input type="button" name="btnClose" value='关闭' class="MyButton" AccessKey="C" image="/shop/images/share/f_closed.gif" onClick="window.close()">
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
					<td valign="top"><spring:message code="sort.tips.content"/>  </td>
				</tr>
			</table>
	</td>
</tr>
</table>
</form>
</body>
</html>