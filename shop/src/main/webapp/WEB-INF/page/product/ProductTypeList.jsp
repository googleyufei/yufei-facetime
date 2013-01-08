<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/page/share/taglib.jsp" %>
<html>
<head>
<title>产品类别管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="/shop/css/vip.css" type="text/css">
<script language="JavaScript">
<!--
	//到指定的分页页面
	function topage(page){
		var form = document.forms[0];
		form.page.value=page;
		form.submit();
	}
//-->
</script>
<SCRIPT language=JavaScript src="/js/FoshanRen.js"></SCRIPT>
</head>

<body bgcolor="#FFFFFF" text="#000000" marginwidth="0" marginheight="0">
<form action="/shop/control/product/type/list.do" method="post">
<input type="hidden" name="page"/>
<input type="hidden" name="parentid"/>
<input type="hidden" name="name"/>
<input type="hidden" name="query"/>
  <table width="98%" border="0" cellspacing="1" cellpadding="2" align="center">
    <tr ><td colspan="6"  bgcolor="6f8ac4" align="right">
    	<%@ include file="/WEB-INF/page/share/fenye.jsp" %>
   </td></tr>
    <tr>
      <td width="8%" bgcolor="6f8ac4"> <div align="center"><font color="#FFFFFF">代号</font></div></td>
      <td width="5%" nowrap bgcolor="6f8ac4"> <div align="center"><font color="#FFFFFF">修改</font></div></td>
      <td width="20%" bgcolor="6f8ac4"> <div align="center"><font color="#FFFFFF">产品类别名称</font></div></td>
	  <td width="10%" nowrap bgcolor="6f8ac4"> <div align="center"><font color="#FFFFFF">创建下级类别</font></div></td>
	  <td width="15%" bgcolor="6f8ac4"><div align="center"><font color="#FFFFFF">所属父类</font></div></td>
	  <td nowrap bgcolor="6f8ac4"> <div align="center"><font color="#FFFFFF">备注</font></div></td>
    </tr>
<!---------------------------LOOP START------------------------------>
<c:forEach items="${pageView.records}" var="entry">
    <tr>
      <td bgcolor="f5f5f5"> <div align="center">${entry.typeid }</div></td>
      <td bgcolor="f5f5f5"> 
        <itcast:permission privilege="update" module="productType">
           <div align="center"><a href="/shop/control/product/type/editUI.do?typeid=${entry.typeid }"> <img src="/shop/images/edit.gif" width="15" height="16" border="0"></a></div>
	    </itcast:permission></td>
      <td bgcolor="f5f5f5"> 
        <itcast:permission privilege="view" module="productType">
        <div align="center"> <c:if test="${fn:length(entry.childtypes)>0}">
               <a href='/shop/control/product/type/list.do?parentid=${entry.typeid}'>${entry.name }</a><font color=red>(有${fn:length(entry.childtypes)}个子类)</font></c:if> <c:if test="${fn:length(entry.childtypes)<=0}">${entry.name}</c:if></div>
        </itcast:permission></td>
	  <td bgcolor="f5f5f5"> 
	    <itcast:permission privilege="insert" module="productType">
	       <div align="center"><a href="/shop/control/product/type/addUI.do?parentid=${entry.typeid}">创建子类别</a></div>
	    </itcast:permission></td>
	  <td bgcolor="f5f5f5" align="center">
	     <itcast:permission privilege="view" module="productType">
	       <c:if test="${!empty entry.parent}"><a href="/shop/control/product/type/list.do?parentid=${entry.parent.parent.typeid}">${entry.parent.name}</a></c:if>
	     </itcast:permission></td>
	  <td bgcolor="f5f5f5">${entry.note }</td>
	</tr>
</c:forEach>
    <!----------------------LOOP END------------------------------->
    <tr> 
      <td bgcolor="f5f5f5" colspan="6" align="center"><table width="100%" border="0" cellspacing="1" cellpadding="3">
          <tr> 
            <td width="5%"></td>
              <td width="85%">
              <itcast:permission privilege="insert" module="productType">
              <input name="AddDic" type="button" class="frm_btn" id="AddDic" onClick="javascript:window.location.href='/shop/control/product/type/addUI.do?parentid=${param.parentid}'" value="添加类别"> &nbsp;&nbsp;
              </itcast:permission>
              <itcast:permission privilege="view" module="productType">
			  <input name="query" type="button" class="frm_btn" id="query" onClick="javascript:window.location.href='/shop/control/product/type/queryUI.do'" value=" 查 询 "> &nbsp;&nbsp;
              </itcast:permission>
            </td>
          </tr>
        </table></td>
    </tr>
  </table>
</form>
</body>
</html>