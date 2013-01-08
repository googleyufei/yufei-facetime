<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/page/share/taglib.jsp" %>
<html>
<head>
<title>修改产品品牌</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="/shop/css/vip.css" type="text/css">
<SCRIPT language=JavaScript src="/shop/js/FoshanRen.js"></SCRIPT>
<script language="javascript">
function checkfm(form){
	if (trim(form.name.value)==""){
		alert("品牌名称不能为空！");
		form.name.focus();
		return false;
	}
	var logofileVal = form.logofile.value;
	if(trim(logofileVal) == ""){
		alert("品牌图片不能为空!");
		form.logofile.focus();
		return false;
	}
	if(trim(logofileVal)!=""){
		var ext = logofileVal.substring(logofileVal.length-3).toLowerCase();
		if (ext!="jpg" && ext!="gif" && ext!="bmp" && ext!="png"){
			alert("只允许上传gif、jpg、bmp、png！");
			return false; 
		}
	}
	return true;
}
</script>
</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<form action="/shop/control/product/brand/edit.do" method="post" enctype="multipart/form-data" onsubmit="return checkfm(this);">
<input type="hidden" name="code" value="${param.code}"/>
<br>
  <table width="90%" border="0" cellspacing="2" cellpadding="3" align="center">
    <tr bgcolor="6f8ac4"><td colspan="2"  > <font color="#FFFFFF">修改品牌：</font></td>
    </tr>
    <tr bgcolor="f5f5f5"> 
      <td width="22%" > <div align="right">品牌名称：</div></td>
      <td width="78%"> <input type="text" name="name" value="${brandBean.name}" size="50" maxlength="40"/>
        <font color="#FF0000">*</font></td>
    </tr>
	<tr bgcolor="f5f5f5"> 
      <td width="22%" > <div align="right">Logo图片：</div></td>
      <td width="78%"> <input type="file" name="logofile" size="50"><br/>
     <c:if test="${!empty brandBean.logoimagepath}"><img src="/shop/${brandBean.logoimagepath}" width="100">
     </c:if> </td>
    </tr>
    <tr bgcolor="f5f5f5"> 
      <td colspan="2"> <div align="center"> 
          <input type="submit" name="SYS_SET" value=" 确 定 " class="frm_btn">
        </div></td>
    </tr>
  </table>
</form>
<br>
</body>
</html>