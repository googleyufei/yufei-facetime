<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ page import="com.facetime.mgr.common.BusnDataDir"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.*"%>
<html>
<link href="/shop/css/css_v2.css" type="text/css" rel="stylesheet">
<body leftMargin="0" topMargin="10" marginheight="10"  marginwidth="0">
<%
	HashMap<String,String> errorMessage = new HashMap<String,String>();
	errorMessage = (HashMap<String,String>)request.getAttribute("logErrorString");
	Iterator iterator = errorMessage.entrySet().iterator();
	while(iterator.hasNext()){
		Map.Entry entry = (Map.Entry)iterator.next();
%>
<table border="0" cellpadding="1" width="100%" cellspacing="1"
				class="tablebg" bgcolor="#000084" align="center">
	<tr> 
		<td width="5%" class="oracletdtwo" style="vertical-align:middle;" height="11"><img src="/shop/images/share/arrow-4.gif"><font size=2  style="font-weight:bold"><%=entry.getKey()%></font>
		</td>
	</tr>
	<tr>
		<td  width="100%" class="oracletdone">
			<table>
				<tr>
					<td style="word-break:break-all;offsettop:10" ><%=entry.getValue()%></td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<%
	}
%>
</body>
</html>