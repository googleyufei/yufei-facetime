<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%
  response.setHeader("Cache-Control","no-store");
  response.setHeader("Pragma","no-cache");
  response.setDateHeader("Expires",0);
%>
<HTML>
<Head>
<title><spring:message code="selectDate.title"/></title>
<script language="JavaScript" src="selectDate.js"></script>
<script language="javascript">
months = new Array(<spring:message code="calendar.months"/>);
weekDays = new Array(<spring:message code="calendar.weekDays"/>);
yearLabel="<spring:message code="calendar.year"/>"
hourLabel="<spring:message code="calendar.hour"/>"
minuteLabel="<spring:message code="calendar.minute"/>"
secondLabel="<spring:message code="calendar.second"/>"
okLabel="<spring:message code="calendar.okLabel"/>"
clearLabel="<spring:message code="calendar.clearLabel"/>"
cancelLabel="<spring:message code="calendar.cancelLabel"/>"
</script>
<STYLE type=text/css>
A {
	COLOR: #000000;
	FONT-SIZE: 10pt;
	TEXT-DECORATION: none
}

.country {
	FONT-SIZE: 9pt;
	LETTER-SPACING: 2px;
	LINE-HEIGHT: 12pt
}

A:hover {
	COLOR: #367d89;
	TEXT-DECORATION: none
}

.style {
	FONT-SIZE: 9pt;
	LINE-HEIGHT: 12px
}

.font1 {
	FONT-SIZE: 10pt
}
</STYLE>
</HEAD>
<BODY onload=load()>
	<SCRIPT>
function fkeydown()
{
	if(event.keyCode==27){
		event.returnValue = null;
		window.returnValue = null;
		window.close();
	}
}
document.onkeydown=fkeydown;
function load(){
toggleDatePicker('daysOfMonth',2);
}
</SCRIPT>
	<div id='daysOfMonth'
		style="POSITION: absolute; left: 0px; top: 0px; width: 300px; height: 180px"></div>
</BODY>
</HTML>