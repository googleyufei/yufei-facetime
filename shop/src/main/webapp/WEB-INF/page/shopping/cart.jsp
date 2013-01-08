<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/page/share/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>购物车 巴巴运动网</title>
<meta http-equiv=Content-Type content="text/html; charset=UTF-8"/>
<meta http-equiv=Content-Language content=zh-CN/>
<link href="/shop/css/new_cart.css" rel="stylesheet" type="text/css"/>
<link href="/shop/css/global/header01.css" rel="stylesheet" type="text/css"/>
<script language="javascript" src="/shop/js/FoshanRen.js"></script>
<script language="javascript">
<!--
/** 获取以指定字符串为前缀的输入字段集合 **/
/** 数字输入格式是否正确(长度1-4位，第一个数字必须是1-9) **/
function numericFormat(strNumber){   
  var newPar=/^[1-9]\d{0,3}$/;
  return newPar.test(strNumber);
} 

function getInputsByname(name, etype){//
	var inputs = document.getElementsByTagName("input");
	var texts = new Array();
	var y = 0;
	for (var i = 0; i < inputs.length; i++) {
	  if (inputs[i].type == etype && inputs[i].name!=null && inputs[i].name.substring(0, name.length) == name) {
			texts[y] = inputs[i];
			y++;
		}
	}
	return texts;
}
function settleAccounts(){
	if(validateAmount()){		
		var form = document.forms["buycart"];
		form.action=form.action+"/settleAccounts.do";
		form.submit();
	}
}

function modifyAmount(){
	if(validateAmount()){		
		var form = document.forms["buycart"];
		form.action=form.action+"/updateAmount.do";
		form.submit();
	}
}

/** 验证购买数量字段 **/
function validateAmount(){
	var amounts = getInputsByname("amount_", "text");
	if(amounts.length==0){
		alert("您还没有购买商品");
		return false;
	}else{
		for (var i = 0; i < amounts.length; i++) {
			var amount = amounts[i];
			if(amount.value==null || amount.value.trim()==""){
				alert("\n您购买的商品中,有的商品购买数量为空,请填写购买数量");
				amount.focus();
				return false;
			}else if(amount.value=="0"){
				alert("\n您购买的商品中,有的商品购买数量为0,如果您不需要该商品,可以删除它");
				amount.select();
				return false;
			}else if(!numericFormat(amount.value)){
				alert("\n购买数量含有非数字数据,请更正");
				amount.select();
				return false;
			}
		}
	}
	return true;
}
//-->
</script>
</head>
<body>
<jsp:include page="/WEB-INF/page/share/Head.jsp"/>
<br/>
<table cellspacing=0 cellpadding=5 width="98%" border="0" align="center">
  <tr>
    <td><table cellspacing=0 cellpadding=0 width="96%" border=0>
      <tbody>
        <tr>
          <td width="24%"><img height=31 src="/shop/images/buy/shop-cart-header-blue.gif" width="218" border=0/></td>
          <td width="34%">如果您修改了商品数量，请点击 <img style="cursor: pointer; " alt="修改数量" src="/shop/images/buy/update-t-sm.gif" border="0" onclick="javascript:modifyAmount()"/></td>
          <td width="14%" align="left"><a href="/shop/shopping/cart/manage.do?method=deleteAll&directUrl=${param.directUrl}">
          <img style="cursor:pointer;" alt="清空购物车" src="/shop/images/buy/az-empty-shoppingcard.gif" border="0"/></a></td>
          <td width="15%" align=left><a href="/shop"><img src="/shop/images/buy/as-s-continus.gif" width="116" height="22" border="0"/></a></td>
          <td width="13%" align=right><img style="cursor:pointer;" src="/shop/images/buy/az-by-split.gif" width="116" height="22" 
                        onclick="javascript:settleAccounts()"/></td>
        </tr>
      </tbody>
    </table></td>
  </tr>

  <tr>
    <td><form id="buycart" name="buycart" action="/shop/shopping/cart" method="post">
    
     <input type="hidden" name="directUrl" value="${param.directUrl}"/>
     <table cellspacing=0 cellpadding=6 width="100%" border=0> 
      <tr bgcolor=#d7ebff>
        <td width="457"><strong>我的购物车里的商品--马上购买</strong></td>
        <td width=112><div align=center><strong>市场价</strong></div></td>
        <td width=181><div align=center><strong>价格</strong></div></td>
        <td width=73><div align=center><strong>数量</strong></div></td>
        <td width=66>&nbsp;</td>
      </tr>
      
<!-- loop begin -->
<c:forEach items="${buyCart.items}" var="item"> 
       <tr valign="top">
        <td><strong><a href="" target="_blank">${item.product.name}</a></strong> 
            <span class="h3color">[颜色/样式：<c:forEach items="${item.product.styles}" var="style">${style.name}</c:forEach>]</span><br/><br/>
        </td>
        <td width="112" align="center">
          <span class="price" title="￥${item.product.marketprice}元"><font color="black"><s><b>￥${item.product.marketprice}元</b></s></font></span>
        </td>
        <td width="181" align="center">
          <p align="center"><span class="price"><b>￥${item.product.sellprice} 元</b></span> <br/>为您节省：<span class="price">￥${item.product.savedPrice}元 </span><br/> </p></td>
        <td align="center" width="73">
          <input type="text" style="width: 30px;" maxlength="3" value="${item.amount}"  name="amount_${item.product.id}_<c:forEach items="${item.product.styles}" var="style">${style.id}</c:forEach>" onkeypress="javascript:InputIntNumberCheck()"/></td>
        <td align="middle" width="66">
          <a href="/shop/shopping/cart/delete.do?directUrl=${param.directUrl }&buyitemid=${item.product.id}-<c:forEach items="${item.product.styles}" var="style">${style.id}</c:forEach>">
                   <img height="17" src="/shop/images/buy/delete.gif" width="45" border="0"/></a></td>
        </tr>
        <tr valign="top">
           <td colspan="5"><img height=1 src="/shop/images/buy/green-pixel.gif" width="100%" border="0"/></td>
      </tr>
</c:forEach>
<!-- loop end -->	  
    </table>
  </form>
  <table width="96%" border="0" align="left">
        <tr>
          <td width="60%" align="right">如果您修改了商品数量，请点击
          <img style="cursor:pointer;" alt="修改数量" src="/shop/images/buy/update-t-sm.gif"  border="0" onclick="javascript:modifyAmount()"/></td>
          <td width="9%" align="right"><div align="right"><span class="price"><strong><b><font color="black">共计:</font></b></strong></span></div></td>
          <td width="11%" align="right"><div align="center"><span class="price"><strong><b class="price"><font color="black">${buycart.totalsellprice} 元</font></b></strong></span></div></td>
          <td width="8%" align="right"><div align="right"><span class="price"><strong><b><font color="black">节省:</font></b></strong></span></div></td>
          <td width="12%" align="right"><div align="center"><span class="price"><strong><b class="price">${buycart.totalsaveprice} 元</b></strong></span></div></td>
        </tr>
        <tr>
          <td colspan="3" align="right">&nbsp;</td>
          <td colspan="2" align="right"><img style="cursor:pointer;" src="/shop/images/buy/az-by-split.gif" width="116" height="22" onclick="javascript:settleAccounts()"/></td>
        </tr>
      </table></td>
  </tr>
</table>
<br/>
<jsp:include page="/WEB-INF/page/share/Foot.jsp" />
</body>
</html>
