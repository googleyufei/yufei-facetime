<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/page/share/taglib.jsp" %>
<html>
<head>
<title>修改订单的联系人信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="/shop/css/vip.css" type="text/css">
<SCRIPT language=JavaScript src="/shop/js/FoshanRen.js"></SCRIPT>
<script language="JavaScript">
function checkfm(form){
	return true;
}
</script>
</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<form action="/shop/control/order/modifyContactInfo.do" method="post" onsubmit="return checkfm(this)">
<input type="hidden" name="contactid" value="${param.contactid}"/>
  <table width="90%" border="0" cellspacing="2" cellpadding="3" align="center">
    <tr bgcolor="6f8ac4"><td colspan="2"  > <font color="#FFFFFF">修改订单的联系人信息：</font></td>
    </tr>
    <tr bgcolor="f5f5f5"> 
      <td width="22%" > <div align="right">联系人名称：</div></td>
      <td width="78%"> <input type="text" name="buyer" value="${contactInfo.buyerName}" size="20" maxlength="30"/>
        <input type="radio" name="buyer_gender" <c:if test="${contactInfo.gender == 'MAN'}"> checked </c:if> value="MAN"  />先生
        <input type="radio" name="buyer_gender" <c:if test="${contactInfo.gender == 'WOMEN'}"> checked</c:if> value="WOMEN" />女士
        <font color="#FF0000">*</font></td>
    </tr>
    <tr bgcolor="f5f5f5"> 
      <td width="22%" > <div align="right">联系人地址：</div></td>
      <td width="78%"> <input type="text" name="buyer_address"value="${contactInfo.address}" size="50" maxlength="100"/>
        <font color="#FF0000">*</font></td>
    </tr>
        <tr bgcolor="f5f5f5"> 
      <td width="22%" > <div align="right">邮编：</div></td>
      <td width="78%"> <input type="text" name="buyer_postalcode" value="${contactInfo.postalcode}" size="8" maxlength="6"/></td>
    </tr>
    <tr bgcolor="f5f5f5"> 
      <td width="22%" > <div align="right">座机：</div></td>
      <td width="78%"> <input type="text" name="buyer_tel" value="${contactInfo.tel}" size="20" maxlength="20"/></td>
    </tr>
        <tr bgcolor="f5f5f5"> 
      <td width="22%" > <div align="right">手机：</div></td>
      <td width="78%"> <input type="text" name="buyer_mobile" value="${contactInfo.mobile}" size="20" maxlength="11"/></td>
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