<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/page/share/taglib.jsp" %>
<html>
<head>
<title>修改订单的配送方式</title>
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
<form action="/shop/control/order/modifyDeliverWay.do" method="post" onsubmit="return checkfm(this)">
<input type="hidden" name="orderid" value="${param.orderid}"/>
  <table width="90%" border="0" cellspacing="2" cellpadding="3" align="center">
    <tr bgcolor="6f8ac4"><td > <font color="#FFFFFF">修改订单的配送方式：</font></td>
    </tr>
 <c:if test="${orderForm.paymentWay!='COD'}">
    <tr bgcolor="f5f5f5"> 
      <td><input type="radio" name="deliverWay" value="GENERALPOST"/>平邮</td>
    </tr>
    <tr bgcolor="f5f5f5"> 
      <td><input type="radio" name="deliverWay" value="EMS"/>国内特快专递EMS</td>
    </tr>
 </c:if>
     <tr bgcolor="f5f5f5"> 
      <td><input type="radio" name="deliverWay" value="EXPRESSDELIVERY"/>快递送货上门</td>
    </tr>
    <tr bgcolor="f5f5f5"> 
      <td><input type="radio" name="deliverWay" value="EXIGENCEEXPRESSDELIVERY"/>加急快递送货上门</td>
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