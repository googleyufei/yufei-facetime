<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/WEB-INF/page/share/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="/shop/css/global/header01.css" rel="stylesheet" type="text/css"/>
<link href="/shop/css/product/product.css" rel="stylesheet" type="text/css"/>
<link href="/shop/css/global/topcommend.css" rel="stylesheet" type="text/css"/>
<script language="javascript" src="/shop/js/FoshanRen.js"></script>
<script language="javascript" src="/shop/js/xmlhttp.js"></script>
<title>${product.name}-巴巴运动网</title>
<meta name="Keywords" content="${product.name}"/>
<meta name="description" content="${product.description}"/>
  <script language="javascript">
  <!--
	function styleEvent(styleid){
		var productImage = document.getElementById('productImage_'+ styleid);
		if(productImage){
		    var main_image = document.getElementById("main_image");
			main_image.style.background="url(/shop"+ productImage.value+") center center no-repeat";
		}
	}
	
	function bigImageBrowse(){
		var form = document.forms["cart"];
		var stypeid = form.styleid.value;
		var productPrototypeImage = document.getElementById('productPrototypeImage_'+ stypeid);		
		if(productPrototypeImage){
			var path = "/shop/product/switch/showimage.do?path=/shop"+ productPrototypeImage.value;			
			window.open (path ,"显示图片");
		}
	}
	
	function getTopSell(typeid){
		var salespromotion = document.getElementById('commenddetail');		
		if(salespromotion && typeid!=""){
			salespromotion.innerHTML= "数据正在加载...";
			send_request(function(value){salespromotion.innerHTML=value},
					 "/shop/product/switch/topsell.do?typeid="+ typeid, true);
		}
	}
	function getViewHistory(){
		var viewHistoryUI = document.getElementById('historyaccess');		
		if(viewHistoryUI){
			viewHistoryUI.innerHTML= "数据正在加载...";
			send_request(function(value){viewHistoryUI.innerHTML=value},
					 "/shop/product/switch/getViewHistory.do", true);
		}
	}
	function pageInit(){
		getTopSell("${product.type.typeid}");
		getViewHistory();
	}
  //-->
  </script>
</head>

<body onload="javascript:pageInit()">
<jsp:include page="/WEB-INF/page/share/Head.jsp"/>
<div id="ContentBody"><!-- 页面主体 -->
    <c:set var="out" value="&gt;&gt; <em>${product.name}</em>"/><c:forEach items="${stypes}" var="type" varStatus="statu">
		<c:set var="out" value=" &gt;&gt; <a href='/shop/product/list/display.do?typeid=${type.typeid}'>${type.name}</a> ${out}"/>
	</c:forEach>
	 	 <div id="position"> 您现在的位置：<a href="/shop" name="linkHome">巴巴运动网</a> <span id="uc_cat_spnPath"><c:out value="${out}" escapeXml="false"></c:out></span></div>
 <div class="browse_left"><!-- 页面主体 左边 --> 
        <!-- 浏览过的商品 -->
	 <div class="browse">
	      <div class="browse_t">您浏览过的商品</div>
	      <ul><div id="historyaccess"></div></ul>
	 </div>
	<!--精品推荐 start -->
	<div id="topcommend" align="left">
	       <div id="newtop"><img height=13 src="/shop/images/global/sy2.gif" width=192/></div>
	       <div id="newlist">
		  <div id="newmore">
		    <div class="title">精品推荐</div>
		  </div>
			<span id="commenddetail">
			</span>
		</div>
	</div>
</div><!-- 页面主体 左边end -->
	
 <div id="Right" ><!-- 页面主体 右边 -->
<form action="/shop/shopping/cart.do" method="post" name="cart">
<input type="hidden" name="productid" value="${product.id}"/>
<div id="browse_left"><c:set var="currentimage"/><c:set var="imagecount" value="0"/>
<c:forEach items="${product.styles}" var="style">
    <c:if test="${style.visible}">
      <c:set var="currentimage" value="${style}"/><c:set var="imagecount" value="${imagecount+1}"/>
    </c:if>
</c:forEach>
<div class="right_left">
	<div id="main_image" onclick="JavaScript:bigImageBrowse()" style="cursor:hand;background:url(/shop${currentimage.image140FullPath}) center center no-repeat"><img src="/shop/images/global/product_blank.gif" WIDTH="200" HEIGHT="240"/></div>
      <img src="/shop/images/global/zoom+.gif" onclick="JavaScript:bigImageBrowse()" style="cursor:pointer;"/>
	</div>
	<div class="right_right">									
	  <div class="right_title"><b>${product.name}</b></div>
		<div class="right_desc">
		   <ul>
			<li class="li2">商品编号：A${product.id}<font color="#CC0000">（电话订购专用）</font></li>
			<c:if test="${!empty product.brand}"><li>品牌：${product.brand.name} </li>	</c:if>																																		</ul>
	    </div>
		<div class="right_desc">
			<c:if test="${imagecount==1}">
			<input type="hidden" name="styleid" value="${currentimage.id }"/>
			<li>颜色：${currentimage.name }</li>
			<input type="hidden" id="productImage_${currentimage.id }" value="${currentimage.imagename}"/>
			<input type="hidden" id="productPrototypeImage_${currentimage.id }" value="${currentimage.imageFullPath}"/>
			</c:if>
			<c:if test="${imagecount>1}">
				<img src="/shop/images/global/init.gif" width="0" height="0"/>
				<li>颜色：<select name="styleid" onchange="javascript:styleEvent(this.value)">
				        <c:forEach items="${product.styles}" var="style">
				          <c:if test="${style.visible}"><option value="${style.id }" <c:if test="${style.id==currentimage.id}">selected </c:if>>${style.name }</option></c:if></c:forEach>
					    </select></li>
						<c:forEach items="${product.styles}" var="style">
						<c:if test="${style.visible}">
						    <input type="hidden" id="productImage_${style.id }" value="${style.image140FullPath}"/>
						    <input type="hidden" id="productPrototypeImage_${style.id }" value="${style.imageFullPath}"/>
						</c:if>
						</c:forEach>
			</c:if>
		    </div>
				<ul>
				<li>市场价：<s>${product.marketprice}</s> 元 <font color='#ff6f02'>本站价：<b>${product.sellprice} 元</b></font> 节省：<font color='#ff6f02'>${product.savedPrice }</font> 元</li>
			  	<li class="right_img"><input type="image" src="/shop/images/global/sale.gif"/></li>
			    <li class="guopiprice">[ <img src="/shop/images/global/2j4.gif" border="0"/>&nbsp;
			       <a href="http://www.babasport.com/cache/news/6/9.shtml" target="_blank">配送说明</a> ]&nbsp;&nbsp;&nbsp;&nbsp;[ <img src="/shop/images/global/2j4.gif" border="0"/>&nbsp;<a href="http://www.babasport.com/cache/news/4/24.shtml" target="_blank">付款方式</a> ]
			    </li>
				</ul>									
	</div>
</div>
<div id="browse_right">
   <div id="sy_biankuang">
	<div class="sy_xinpintuijian_font">本站尚未开张</div>
    <div class="sy_dianhua" style="line-height:150%"><font color="#FF0000">全国：010-6466 3070</font><br/>MSN在线客服：babasport@sohu.com <br/><font color="#3A8FAF">QQ在线客服：523429525</font></div>
   </div>
</div>
</form>
<div class='right_blank'></div><div class='right_title1'>商品说明</div><div class='right_content'>${product.description}</div></div>
<!-- 页面主体 右边 end -->
</div>
<!-- 页面主体 end -->

<jsp:include page="/WEB-INF/page/share/Foot.jsp" />
</body>
</html>