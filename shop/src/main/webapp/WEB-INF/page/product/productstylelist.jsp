<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/page/share/taglib.jsp" %>
<html>
<head>
<title>产品样式显示</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="/shop/css/vip.css" type="text/css">
<SCRIPT language=JavaScript src="/shop/js/FoshanRen.js"></SCRIPT>
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
	
	function actionEvent(methodname){
		var form = document.forms[0];
		if(validateIsSelect(form.all, form.stylesids)){
			form.action=form.action+methodname+".do";
			form.submit();
		}else{
			alert("请选择要操作的记录");
		}
	}
	/*
	 * 判断是否选择了记录
     */
	function validateIsSelect(allobj,items){
	    var state = allobj.checked;
	    if(items.length){
	    	for(var i=0;i<items.length;i++){
	    		if(items[i].checked) return true;
	    	}
	    }else{
	    	if(items.checked) return true;
	    }
	    return false;
	}
//-->
</script>
</head>

<body bgcolor="#FFFFFF" text="#000000" marginwidth="0" marginheight="0">
<form action="/shop/control/product/style/" method="post">
<input type="hidden" name="method" value="">
<input type="hidden" name="productid" value="${param.productid}"/>
  <table width="98%" border="0" cellspacing="1" cellpadding="2" align="center">
    <tr>
      <td width="8%" nowrap bgcolor="6f8ac4"> <div align="center"><font color="#FFFFFF">选择</font></div></td>
      <td width="8%" nowrap bgcolor="6f8ac4"> <div align="center"><font color="#FFFFFF">修改</font></div></td>
      <td width="37%" bgcolor="6f8ac4"> <div align="center"><font color="#FFFFFF">名称</font></div></td>
      <td width="10%" bgcolor="6f8ac4"> <div align="center"><font color="#FFFFFF">在售状态</font></div></td>
	  <td width="37%" nowrap bgcolor="6f8ac4"> <div align="center"><font color="#FFFFFF">产品图片</font></div></td>
    </tr>
<!---------------------------LOOP START------------------------------>
<c:forEach items="${styles}" var="entry">
    <tr>
      <td bgcolor="f5f5f5"> <div align="center"><INPUT TYPE="checkbox" NAME="stylesids" value=${entry.id}></div></td>
      <td bgcolor="f5f5f5"> <div align="center"><a href="/shop/control/product/style/editUI.do?productstyleid=${entry.id}">
	  <img src="/shop/images/edit.gif" width="15" height="16" border="0"></a></div></td>
      <td bgcolor="f5f5f5"> <div align="center">${entry.name}</div></td>
      <td bgcolor="f5f5f5"> <div align="center"><c:if test="${entry.visible}">在售</c:if><c:if test="${!entry.visible}">停售</c:if></div></td>
	  <td bgcolor="f5f5f5"> <div align="center"><img src="/shop${entry.imageFullPath}" width="50"></div></td>
	</tr>
</c:forEach>
    <!----------------------LOOP END------------------------------->
    <tr> 
      <td bgcolor="f5f5f5" colspan="10" align="center"><table width="100%" border="0" cellspacing="1" cellpadding="3">
          <tr> 
            <td width="8%"><input type="checkbox" <c:if test="${fn:length(styles)<1}">disabled="disabled"</c:if>
             NAME="all" onclick="javascript:allselect(this, this.form.stylesids)">全选
            </td>
            <td width="85%">
              <input type="button" class="frm_btn" onClick="javascript:window.location.href='/shop/control/product/style/addUI.do?productid=${param.productid}'" value="添加产品图片"> &nbsp;&nbsp;
              <input name="visible" type="button" class="frm_btn" <c:if test="${fn:length(styles)<1}">disabled="disabled"</c:if>
                     onClick="javascript:actionEvent('enable')" value=" 上 架 "> &nbsp;&nbsp;
              <input name="disable" type="button" class="frm_btn" <c:if test="${fn:length(styles)<1}">disabled="disabled"</c:if>
                     onClick="javascript:actionEvent('disable')" value=" 下 架 "> &nbsp;&nbsp;
            </td>
          </tr>
        </table></td>
    </tr>
  </table>
</form>
</body>
</html>