<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/page/share/taglib.jsp" %>
<html>
<head>
<title>上传文件显示</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="/shop/css/vip.css" type="text/css">
<script language=JavaScript src="/shop/js/FoshanRen.js"></script>
<script language="JavaScript">
<!--
	//到指定的分页页面
	function topage(page){
		var form = document.forms[0];
		form.page.value=page;
		form.submit();
	}
	
	function allselect(allobj,items){
	    var state = allobj.checked;
	    if(items.length){
	    	for(var i=0;i<items.length;i++){
	    		if(!items[i].disabled) items[i].checked=state;
	    	}
	    }else{
	    	if(!items.disabled) items.checked=state;
	    }
	}
	
	function deleteFiles(objform,methodName){
		objform.action='/shop/control/uploadfile/'+methodName+".do";
		objform.submit();
	}
//-->
</script>
</head>

<body bgcolor="#FFFFFF" text="#000000" marginwidth="0" marginheight="0">
<form action="/shop/control/uploadfile/list.do" method="post">
  <input type="hidden" name="page" value="${pageView.currentpage}"/>
  <table width="98%" border="0" cellspacing="1" cellpadding="2" align="center">
    <tr ><td colspan="4" bgcolor="6f8ac4" align="right">
    	<%@ include file="/WEB-INF/page/share/fenye.jsp" %>
   </td></tr>
    <tr>
      <td width="6%" bgcolor="6f8ac4"> <div align="center"><font color="#FFFFFF">选择</font></div></td>
      <td width="24%" bgcolor="6f8ac4"> <div align="center"><font color="#FFFFFF">代号</font></div></td>
      <td width="50%" bgcolor="6f8ac4"> <div align="center"><font color="#FFFFFF">文件</font></div></td>
	  <td width="20%" nowrap bgcolor="6f8ac4"> <div align="center"><font color="#FFFFFF">上传时间</font></div></td>
    </tr>
<!---------------------------LOOP START------------------------------>
<c:forEach items="${pageView.records}" var="entry">
    <tr>
      <td bgcolor="f5f5f5"> <div align="center"><input type="checkbox" name="fileids" value="${entry.id}"></div></td>
      <td bgcolor="f5f5f5"> <div align="center">${entry.id}</div></td>
      <td bgcolor="f5f5f5"> <div align="center"><a href="/shop${entry.filepath}" target="_blank">${entry.filepath}</a></div></td>
	  <td bgcolor="f5f5f5"> <div align="center">${entry.uploadtime}</div></td>
	</tr>
</c:forEach>
    <!----------------------LOOP END------------------------------->
    <tr>
      <td bgcolor="f5f5f5" colspan="4" align="center"><table width="100%" border="0" cellspacing="1" cellpadding="3">
          <tr> 
            <td width="10%"><INPUT TYPE="checkbox" NAME="all" onclick="javascript:allselect(this, this.form.fileids)">全选</td>
            <td width="85%">
             <input type="button" class="frm_btn" onClick="javascript:deleteFiles(this.form,'addUI')" value=" 上传文件 "> &nbsp;&nbsp;
             <input type="button" class="frm_btn" onClick="javascript:deleteFiles(this.form,'delete')" value=" 删 除 "> &nbsp;&nbsp;
            </td>
          </tr>
        </table></td>
    </tr>
  </table>
</form>
</body>
</html>