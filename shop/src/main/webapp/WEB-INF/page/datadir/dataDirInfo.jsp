<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/page/share/taglib.jsp" %>
<%@ include file="/inc/htc.inc"%>
<html>
<head><title>addOrModify</title></head>
<link href="/shop/css/css_v2.css" type="text/css" rel="stylesheet">
<script language="javascript" src="/shop/js/Validator.js"></script>
<body id="bodyid" leftmargin="10" topmargin="10">
<form action="/shop/pages/datadir/save.do" name="dataDirForm" method="post">
<table border="0" cellpadding="1" width="100%" cellspacing="1" class="tablebg" align="center">
<tr>
	<td class="oracolumncenterheader" colspan="2" height="24">
		<c:if test="${empty dataDirForm.id}">
		   <spring:message code="dataDir.title.add"/> 
		</c:if>
        <c:if test="${not empty dataDirForm.id }" >
            <spring:message code="dataDir.title.modify"/> 
            <script type="text/javascript">dataDirForm.action="/shop/pages/datadir/modify.do"</script>
		</c:if>
	</td>
</tr>
<tr>
	<td height="24" width="29%" class="oracletdtwo" align="right"><spring:message code="dataDir.current.path"/> : 
	</td>
	<td width="71%" class="oracletdone">
	<c:if test="${empty dirPath}">
		<font color="#FF0099">${dirPath}</font>
	</c:if>
	</td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right">
		<font color="#FF0000">**</font><spring:message code="label.note"/> : 
	</td>
	<td class="oracletdone"><input type="text" name="note" value="${dataDirForm.note}" style="width:95%"/></td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right"><spring:message code="label.note.en"/> : 
	</td>
	<td class="oracletdone"><input type="text" name="noteEn" value="${dataDirForm.noteEn }" style="width:95%"/></td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right">
		<font color="#FF0000">**</font><spring:message code="label.key"/> : 
	</td>
	<td class="oracletdone"><input type="text" name="key" value="${dataDirForm.key }" style="width:95%"/></td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right"><spring:message code="label.value"/>:</td>
	<td class="oracletdone">
		<input type="text" name="value" value="${dataDirForm.value }" style="width:95%" />
		<input type="hidden" name="id" value="${dataDirForm.id }"/>
		<input type="hidden" name="parentid" value="${dataDirForm.parentid }"/>
	</td>
</tr>
<tr>
	<td height="24" class="oracletdtwo" align="right" colspan="2"></td>
</tr>
<tr>
	<td colspan="2" bgcolor="#FFFFFF" height="24" align="center">
		<input type="submit" name="btnSave" value='<spring:message code="button.save"/> ' class="MyButton" accesskey="S" image="/shop/images/share/save.gif">
        <input type="button" name="btnClose" value='<spring:message code="button.close"/> ' class="MyButton" AccessKey="C" image="/shop/images/share/f_closed.gif" onClick="window.close()">
		<c:if test="${empty dataDirForm.id }"> 
		<input type="checkbox" name="newAnOther" id="newAnOther" value="1" checked><spring:message code="dataDir.label.addAnOther"/> 
		</c:if>
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
			<td valign="top"><spring:message code="dataDirInfo.tips.content"/> </td>
		</tr>
	</table>
	</td>
</tr>
</table>
</form>
</body>
</html>