<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<html>
<head><title>dp log query</title></head>
<link href="/shop/css/css_v2.css" rel="stylesheet" type="text/css">
<script language="javascript" src="/shop/js/tablesort.js"></script>
<script language="javascript" type="text/javascript">
var selectValue = "";
function SelAll(chkAll) {
     var chkSet = document.all.tags("input")
     for(var i=0;i<chkSet.length;i++) {
	    if(chkSet[i].name=="chkList") {
		   chkSet[i].checked=chkAll.checked
		}   
	 }
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

function query() {
	if(selectValue==""){
		dpLogForm.selValue.value = document.getElementById("selectName").value;		
	}else{
		dpLogForm.selValue.value = selectValue;
	}
	dpLogForm.toPage.value = formList.toPage.value
	dpLogForm.pageSize.value = formList.pageSize.value
	dpLogForm.submit()
}

function del() {
	var idList = GetSelIds();
	if(idList == "") {
		alert('<spring:message code="del.selectNone"/> ')
		return false
	}

	if (confirm('<bean:message bundle="logmgr" key="confirmMsg.deleteTermType"/>')) {
		var idList = GetSelIds()
		strUrl="/pages/logmgr/dplog.do?action=del&idList="+idList
		var returnValue = OpenModal(strUrl,"500,400,union.subject.delete,busn")
		query()   
	}
}

function clearAll() {
	if (confirm('<spring:message code="operator.clearalllog"/> ')) {
		dpLogForm.action = "<%=request.getContextPath()%>/pages/logmgr/dplog.do?action=clear"
		dpLogForm.submit()
	}	
}
<!-- start of own updting by wswei at 20100510 -->
function showprocess(){
	var idList = ""
	var chkSet = document.all.tags("input")
	var j=0;
	var strMark;
	var str;
	for(var i=0; i<chkSet.length; i++) {
		if (chkSet[i].name == "radio") {
			if(chkSet[i].checked) {
				str = chkSet[i].value.split(",")
				j++;
				if (str[4] == "99") {
					alert('<spring:message code="msg.noError"/> ')
					return false
				}
				idList = chkSet[i].value;
				strMark = chkSet[i].value.split(",");
			}    
		}   
	}
	if(j<1) {
		alert('<spring:message code="msg.process.null"/> ')
		return false
	} else if (j>1) {
		alert('<spring:message code="msg.process.only"/> ')
		return false
	}else{
			if(strMark[strMark.length-1] == ""){
				alert('<spring:message code="msg.noError"/> ')
			}else{
				var strUrl="<%=request.getContextPath()%>/pages/logmgr/dplog.do?action=showProc&id="+idList
				window.open(strUrl,"","scrollbars=yes,width=900,height=600")
			}
		}		
}
function itemSelectChange(value){
	setValue(value);
}
function setValue(value){
	selectValue = value;
}
<!-- end of own updting by wswei at 20100510 -->
</script>
<body id="bodyid" leftmargin="0" topmargin="2">
<form action="/pages/logmgr/dplog.do?action=query" method="post">
<input type="hidden" name="selValue">
<input type="hidden" name="selId">
<input type="hidden" id="ui" value="<%=request.getSession().getAttribute("userid") %>">
<table width="100%" cellspacing="0" cellpadding="0">
<tr> 
    <td> 
	<fieldset class="jui_fieldset" width="100%">
	<legend><spring:message code="label.condition"/> </legend> 
	<table width="100%">
	<tr>
	  <td width="9%" align="right"><spring:message code="clear.account.unionorgid" bundle="liquidata"/>:  </td>
	  <td width="17%">
	  <bean:define id="sunionorgid" name="liquiddata.loginUser" property="unionorgid" />
	  <view:viewSelect name="unionorgid" id="unionorgid" style="width:100%"	selType="unionorgid" path='<%=String.valueOf(sunionorgid)%>' />
	  <logic:notEmpty name="dpLogForm" property="unionorgid">
        <script language="javascript">
			dpLogForm.unionorgid.value='${dpLogForm.unionorgid}';
		</script>
      </logic:notEmpty>
	 	</td>
	 	<td width="8%" align="right"><spring:message code="label.user.name"/>:  </td>
	 	<td width="8%" align="left">
	 		<select id="selectName" style="width:100%" onchange=itemSelectChange(this.options[this.selectedIndex].value)>
				<c:forEach items="userName" var="user" varStatus="index"><option value="<view:EnToZh name="user" property="userid" hashMap="" type="dataDir"/>"><view:EnToZh name="user" property="userid" hashMap="BusnDataDir.operInfoMap" type="dataDir"/></option>
				</logic:iterate>
			</select>
	 	</td>
		<td width="9%" align="right"><spring:message code="clear.account.type" bundle="liquidata"/>:  </td>
		  <td width="14%"><view:viewSelect name="type" id="type" style="width:100%" selType="dataDir" path="log.processType" beforeOption="all" />
		    <logic:notEmpty name="dpLogForm" property="type">
		  	<script language="javascript">
				dpLogForm.type.value='${dpLogForm.type}';
			</script>
		    </logic:notEmpty>
		  </td>
	  <td width="9%" align="right"><spring:message code="clear.account.date" bundle="liquidata"/>:  </td>
	  <td width="34%">
        <input name="createBeginDate" type="text" value='${dpLogForm" property="createBeginDate"/>'  class="MyInput" issel="true" isdate="true" dofun="ShowDate(this)" size="12">&nbsp;<spring:message code="calendar.to"/> &nbsp;<input name="createEndDate" type="text" value='<bean:write name="dpLogForm.createEndDate}'  class="MyInput" issel="true" isdate="true" dofun="ShowDate(this)" size="12">
      </td>
	</tr>
		</table>
	  </fieldset>
	</td>
  </tr>
</table>
<input type="hidden" name="pageSize" value="20">
<input type="hidden" name="toPage" value="1">
<input type="hidden" name="idList">
<input type="hidden" name="menuno" value='${dplog.menuno}'>
</form>
<form name="formList" method="post">
<table width="100%" height="20" border="0" cellspacing="0" cellpadding="0" background="/shop/images/main/bgtop.gif">
  <tr>
    <td width="3%" valign="middle">&nbsp;<img src="/shop/images/share/list.gif" width="14" height="16"></td>
    <td width="15%" class="orarowhead"><spring:message code="label.query.result"/> </td>
	<td align="right"><view:button menuid="dplog.menuno" type="session"/></td>
  </tr>
</table>
<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066">
  <tr bgcolor="#FFFFFF"> 
  	<td>
       <table width="100%" cellSpacing="0" cellPadding="0">
          <tr>
            <td width="5%" align="center">&nbsp;</td>
          	<td width="12%"></td>
    <td width="83%"><view:pagination pageName="currPage" formName="dpLogForm" /></td>
	</tr>
	</table>
	  </td>
	</tr>
</table>
<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066">
<tr class="oracolumncenterheader"> 
	<td width="5%" align="center"><spring:message code="label.select"/> </td>
	<td width="8%"><spring:message code="label.user.name"/> </td>	  
	<td width="11%"><bean:message bundle="liquidata" key="clear.account.cleardate" /></td>
	<td width="11%"><bean:message bundle="liquidata" key="clear.account.createdate" /></td>
	<td width="11%"><bean:message bundle="liquidata" key="clear.account.unionorgid" /></td>
	<td width="9%"><bean:message bundle="liquidata" key="clear.account.type" /></td>
	<td width="10%"><bean:message bundle="liquidata" key="clear.account.hostip" /></td>
	<td width="10%"><bean:message bundle="liquidata" key="clear.account.doneflag" /></td>
	<%-- start of updating by wswei at 20100507 --%>
	<td width="35%"><bean:message bundle="liquidata" key="clear.account.errReas" /></td>
	<%-- end of updating by wswei at 20100507 --%>
</tr>
<logic:iterate id="dpLog" name="uploadList" indexId="index">
<bean:define id="cleardate" property="cleardate" name="dpLog" />
<tr class="trClass<%=(index.intValue()%2)%>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)> 
	<td align="center">
		<input name="radio" type="radio" id="radio" value='${dpLog" property="cleardate" format="yyyy-MM-dd"/>,${dpLog" property="unionorgid" />,${dpLog" property="type},<bean:write name="dpLog.id},<bean:write name="dpLog.errReason}'>
	</td>
	<td align="center"><view:EnToZh name="dpLog" property="userid" hashMap="BusnDataDir.operInfoMap" type="dataDir"/></td>
	<td align="center">
	<%
		String str=String.valueOf(cleardate);
		out.print(str.substring(0,4)+"-"+str.substring(4,6)+"-"+str.substring(6,8));
	%>
	</td>
	<td align="center">
		${dpLog.createdate" format="yyyy-MM-dd HH:mm:ss}
	</td>
	<td align="center"><view:EnToZh name="dpLog" property="unionorgid" hashMap="BusnDataDir.unionOrgInfoMap" type="dataDir"/></td>
	<td align="center">
		<view:EnToZh name="dpLog" property="type" hashMap="log.processType" type="dataDir"/>
	</td>
	<td align="center">${dpLog.hostip}</td>
	<td align="center"><view:EnToZh name="dpLog" property="flag" hashMap="log.doneflag" type="dataDir"/></td>
	<%-- start of updating by wswei at 20100507 --%>
	<td align="center"><view:EnToZh name="dpLog" hashMap="accchk.errorRessonMap" type="dataDir" property="errReason"/></td>
	<%-- end of updating by wswei at 20100507 --%>
</tr>	
</c:forEach>	
<%
	if (request.getAttribute("rowNum")!=null){
		int rownum = ((Integer)request.getAttribute("rowNum")).intValue();
		if (rownum>0){
			for(int i=0;i<rownum;i++){
%>
<tr class="trClass<%=(i%2)%>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
	<td height="20"></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
</tr>		
<%		}
	}
}
%>  
</table>
</form>
</body>
</html>