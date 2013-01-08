<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/page/share/taglib.jsp" %>
<html>
<head><title><spring:message code="menuOperate.title.add" />  </title></head>
<link href="/shop/css/css_v2.css" type="text/css" rel="stylesheet">
<script language="javascript" src="/shop/js/Validator.js"></script>
<script language="javascript">
function closeform() {
	if (!parent.returnValue) {
		parent.returnValue = false
	}
	window.close()
}
</script>
<body id="bodyid" leftmargin="10" topmargin="10"">
<form action="/shop/pages/menuoperate/save.do" name="menuOperateForm" method="post">
<table border="0" cellpadding="1" width="100%" cellspacing="1" class="tablebg" align="center">
<c:if test="${not empty menuOperateForm.operid}">
  <script type="text/javascript">menuOperateForm.action="/shop/pages/menuoperate/update.do"</script>
</c:if>
<tr>
	<td class="oracolumncenterheader" colspan="2" height="24"><spring:message code="menuOperateForm.title"/> </td>
</tr>
<tr>
	<td height="24" width="28%" class="oracletdtwo" align="right">
		<font color="#FF0000">**</font>操作ID:
	</td>
	<td width="72%" class="oracletdone">
		<input type="text" name="operid" value="${menuOperateForm.operid}" style="width:97%"/>
	</td>
</tr>
<tr>
	<td height="24" width="28%" class="oracletdtwo" align="right">
		<font color="#FF0000">**</font><spring:message code="menuOperateForm.opername"/>  
	</td>
	<td width="72%" class="oracletdone">
		<input type="text" name="opername" value="${menuOperateForm.opername}" style="width:97%"/>
	</td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right">
		<font color="#FF0000">**</font><spring:message code="menuOperateInfoForm.clickname"/>  :
	</td>
	<td class="oracletdone">
		<input type="text" name="clickname" value="${menuOperateForm.clickname }" style="width:97%"/>
	</td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right"><spring:message code="menuOperateInfoForm.picpath"/>  :
	</td>
	<td class="oracletdone">
		<input type="text" name="picpath" value="${menuOperateForm.picpath}" style="width:97%"/>
	</td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right"><spring:message code="menuOperateInfoForm.types"/>  :
	</td>
	<td class="oracletdone">	
		<select name="types" style="width:97%">
		<option value="button" />button
		<option  value="submit" />submit
		<option value="reset" />reset
		</select>
	</td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right"><spring:message code="menuOperateInfoForm.keys"/>  :
	</td>
	<td class="oracletdone"><input type="text" name="keys" value="${menuOperateForm.keys }" style="width:97%"/></td>
</tr>
<tr>
	<td colspan="2" bgcolor="#FFFFFF" height="24" align="center">
		<input type="submit" name="submit" value='<spring:message code="button.save"/>  ' class="MyButton" AccessKey="O" image="/shop/images/share/save.gif">
         <input type="button" name="btnClose" value='<spring:message code="button.close"/>  ' class="MyButton" AccessKey="C" onClick="closeform()" image="/shop/images/share/f_closed.gif">
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
			<td valign="top"><spring:message code="label.content"/>  </td>
		</tr>
	</table>
	</td>
</tr>
</table>
</form>
</body>
</html>