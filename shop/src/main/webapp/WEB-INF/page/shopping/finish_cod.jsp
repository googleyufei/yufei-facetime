<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/page/share/taglib.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<HEAD>
<TITLE>订单完成 巴巴运动网</TITLE>
<META http-equiv=Content-Type content="text/html; charset=UTF-8">
<META http-equiv=Content-Language content=zh-CN/>
<LINK href="/shop/css/new_cart.css" rel="stylesheet" type="text/css"/>
<link href="/shop/css/global/header01.css" rel="stylesheet" type="text/css"/>
<SCRIPT language=JavaScript src="/shop/js/FoshanRen.js"></SCRIPT>
</HEAD>
<BODY>
<jsp:include page="/WEB-INF/page/share/Head.jsp"/>
<br/>
<h1>订单号:${param.orderid },应付金额:${param.payablefee }元</h1>
<br/>
你选择的付款方式为"货到付款",在未收到商品的这段时间,请保持你的电话畅通.
<br/>
<jsp:include page="/WEB-INF/page/share/Foot.jsp" />
</BODY>
</HTML>
