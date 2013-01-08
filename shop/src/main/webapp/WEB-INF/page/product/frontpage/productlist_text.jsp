<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/WEB-INF/page/share/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
   <title>户外用品 巴巴运动网</title>
    <link rel="shortcut icon" href="/shop/favicon.ico" type="image/x-icon"/>    
	<link href="/shop/css/global/header01.css" rel="stylesheet" type="text/css"/>
	<link href="/shop/css/product/list.css" rel="stylesheet" type="text/css" />	
	<link href="/shop/css/global/topsell.css" rel="stylesheet" type="text/css"/>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="Keywords" content="户外用品"/>
	<meta name="description" content="中国最大的户外用品网上商城,致力于提供最高品质，时尚的户外用品，让所有热爱生活的人们，能够享受到户外所带来的轻松、惬意和乐趣。"/>
<script language="javascript" src="/shop/js/xmlhttp.js"></script>
<script language="javascript">
<!--
	function getTopSell(typeid){
		var salespromotion = document.getElementById('salespromotion');		
		if(salespromotion && typeid!=""){
			salespromotion.innerHTML= "数据正在加载...";
			send_request(function(value){salespromotion.innerHTML=value},
					 "/shop/product/switch/topsell.do?typeid="+ typeid, true);
		}
	}
	function getViewHistory(){
		var viewHistoryUI = document.getElementById('viewHistory');		
		if(viewHistoryUI){
			viewHistoryUI.innerHTML= "数据正在加载...";
			send_request(function(value){viewHistoryUI.innerHTML=value},
					 "/shop/product/switch/getViewHistory.do", true);
		}
	}
	function pageInit(){
		getTopSell("${producttype.typeid}");
		getViewHistory();
	}
//-->
</script>
</head>

<body class="ProducTypeHome2" onload="javascript:pageInit()">
	<jsp:include page="/WEB-INF/page/share/Head.jsp"/>
	<c:set var="out" value=""/><c:forEach items="${types}" var="type" varStatus="statu">
		<c:if test="${statu.count==1}"><c:set var="out" value=" &gt;&gt; <em>${type.name}</em> ${out}"/></c:if>
		<c:if test="${statu.count>1}"><c:set var="out" value=" &gt;&gt; <a href='/shop/product/list/display.do?typeid=${type.typeid}'>${type.name}</a> ${out}"/></c:if>
	</c:forEach>
    <div id="position">您现在的位置: <a href="/shop" name="linkHome">巴巴运动网</a> 
    <c:out value="${out}" escapeXml="false"></c:out>（${pageView.totalrecord}个）
	</div>

    <!--页面左侧分类浏览部分-->
    <div class="browse_left">
         <div class="browse">
            <div class="browse_t">${producttype.name}</div>
				<h2><span class="gray">浏览下级分类</span></h2>
				<ul><c:forEach items="${producttype.childtypes}" var="childtype">						
				<li class='bj_blue'><a href="/shop/product/list/display.do?typeid=${childtype.typeid}">${childtype.name}</a></li></c:forEach>		    
			</ul>
	     </div>
<div id="sy_biankuang">
        <div class="lanmu_font">最畅销${producttype.name}</div>
        <div style="padding-left: 10px; color: #333333" id="salespromotion"></div>
</div><br/>
<div class="browse">
      <div class="browse_t">您浏览过的商品</div>
        <ul id="viewHistory"></ul>
	  </div>
    </div>
    <!--页面右侧分类列表部分开始-->
    <div class="browse_right">
         <div class="select_reorder">
              <div class="reorder_l">请选择排序方式： 
                <c:if test="${'selldesc'==param.sort}"><strong><em>销量多到少</em></strong></c:if>
                   <c:if test="${'selldesc'!=param.sort}"><a title='按销量降序' href="/shop/product/list/display.do?sort=selldesc&typeid=${param.typeid}&style=${param.style}">销量多到少</a></c:if>
			  | <c:if test="${'sellpricedesc'==param.sort}"><strong><em>价格高到低</em></strong></c:if>
			       <c:if test="${'sellpricedesc'!=param.sort}"><a title='价格高到低' href="/shop/product/list/display.do?sort=sellpricedesc&typeid=${param.typeid}&style=${param.style}">价格高到低</a></c:if>
			  | <c:if test="${'sellpriceasc'==param.sort}"><strong><em>价格低到低</em></strong></c:if>
			       <c:if test="${'sellpriceasc'!=param.sort}"><a title='价格低到高' href="/shop/product/list/display.do?sort=sellpriceasc&typeid=${param.typeid}&style=${param.style}">价格低到高</a></c:if>
			  | <c:if test="${empty param.sort}"><strong><em>最近上架时间</em></strong></c:if>
			       <c:if test="${!empty param.sort}"><a title='价格低到高' href="/shop/product/list/display.do?sort=&typeid=${param.typeid}&style=${param.style}">最近上架时间</a>
			    </c:if>
			  </div>
		      <div class="reorder_r">显示方式：
		        <c:if test="${param.style=='imagetext'}"><strong><em>图文版</em></strong></c:if>
		           <c:if test="${param.style!='imagetext'}"><a href="/shop/product/list/display.do?sort=${param.sort}&typeid=${param.typeid}&sex=${param.sex }&brandid=${param.brandid}&style=imagetext">图文版</a></c:if> 
		       |<c:if test="${param.style=='imagetext'}"><a href="/shop/product/list/display.do?sort=${param.sort}&typeid=${param.typeid}&sex=${param.sex }&brandid=${param.brandid}&style=image">图片版</a>
		           </c:if><c:if test="${param.style!='imagetext'}"><strong><em>图片版</em></strong></c:if>
		      </div>
			<div class="emptybox"></div>
			 <div class="brand">
				<div class="FindByHint">按<strong>品牌</strong>选择：</div>
				<ul class="CategoryListTableLevel1">
				  <c:forEach items="${brands}" var="brand">
				   <li><a href="/shop/product/list/display.do?sort=${param.sort}&typeid=${param.typeid}&brandid=${brand.code}&sex=${param.sex}">${brand.name}</a></li>
				  </c:forEach>
				</ul>
			 </div>
			 <div class="SubCategoryBox">
				<div class="FindByHint">按<strong>男女款</strong>选择：</div>
				<ul class="CategoryListTableLevel1">
				<li><a  href="/shop/product/list/display.do?sort=sellpriceasc&typeid=${param.typeid}&sex=MAN&brandid=${param.brandid}&style=${param.style}">男款</a></li>
				<li><a  href="/shop/product/list/display.do?sort=sellpriceasc&typeid=${param.typeid}&sex=WOMEN&brandid=${param.brandid}&style=${param.style}">女款</a></li>
				<li><a  href="/shop/product/list/display.do?sort=sellpriceasc&typeid=${param.typeid}&sex=NONE&brandid=${param.brandid}&style=${param.style}">男女均可</a></li>
				<li><a class="red" href="/shop/product/list/display.do?sort=sellpriceasc&typeid=${param.typeid}">全部</a></li>
				</ul>
			 </div>
		</div>
	     <div id="divNaviTop" class="number">
	          <div class="number_l">以下<span class='number_white'>${pageView.totalrecord}</span>条结果按<span class="number_white">
				<c:choose>
				  <c:when test="${'selldesc'==param.sort}">销量多到少</c:when>
				  <c:when test="${'sellpricedesc'==param.sort}">价格高到低</c:when>
				  <c:when test="${'sellpriceasc'==param.sort}">价格低到高</c:when>
				  <c:otherwise>最近上架时间</c:otherwise>
				</c:choose>
			  </span>排列，显示方式是<span class="number_white"><c:if test="${param.style=='imagetext'}">图文版</c:if><c:if test="${param.style!='imagetext'}">图片版</c:if></span>　每页显示<span class="number_white">${pageView.maxresult}</span>条</div>
		      <div class="turnpage">
                <div><em>第${pageView.currentpage}页</em></div>
		      </div>
	     </div>
<!---------------------------LOOP START------------------------------>
<c:forEach items="${pageView.records}" var="entry">	
		<div class="goodslist">
          <div class="goods" style="cursor:hand;background:url(<c:forEach items="${entry.styles}" var="pic">/shop${pic.image140FullPath}</c:forEach>) center center no-repeat"><a href="/shop/product/view.do?productid=${entry.id}" target="_blank">
            <img src="/shop/images/global/product_blank.gif" alt="${entry.name}" width="140" height="168"  border="0"/></a></div>
          <div class="goods_right">
                <h2><a href="/shop/product/view.do?productid=${entry.id}" target="_blank" title="${entry.name}">${entry.name}</a></h2>
	           <div class="message"><ul>
			  <c:if test="${!empty entry.brand}"> <li>品牌：${entry.brand.name}</li></c:if>
			   </ul></div>
	           <div class="content">&nbsp;&nbsp;&nbsp;<c:out value="${fn:substring(entry.description,0,200)}"/></div>
	           <div class="message_bottom">
	                <div class="save"><s>￥${entry.marketprice}</s>　<strong><em>￥${entry.sellprice}</em></strong>　节省：${entry.savedPrice}</div>
			        <div class="buy"><a href="/shop/product/view.do?productid=${entry.id}"><img src='/shop/images/sale.gif' width='84' height='24' border='0' /></a></div>
	           </div>
          </div>
          <div class="empty_box"></div>
        </div>
</c:forEach>
<!----------------------LOOP END------------------------------->	
	     <div id="divNaviBottom" class="page_number">
	     <div class="turnpage turnpage_bottom">	
	     <c:forEach begin="${pageView.pageindex.startindex}" end="${pageView.pageindex.endindex}" var="wp">
		    <c:if test="${pageView.currentpage==wp}"><div class='red'>${wp}</div></c:if>
		    <c:if test="${pageView.currentpage!=wp}"><div class="page"><a href="/shop/product/list/display.do?page=${wp}&brandid=${param.brandid}&sex=${param.sex}&sort=selldesc&style=${param.style}&typeid=${param.typeid}">[${wp}]</a></div></c:if>
		</c:forEach>
		<div>&nbsp;&nbsp;</div>跳转到第
		  <select name="selectPage" class="kuang" onchange="javaScript:topage(this.value)">
		     <c:forEach begin="1" end="${pageView.totalpage}" var="wp">
				<option value="${wp}" <c:if test="${pageView.currentpage==wp}">selected</c:if>> ${wp} </option>
			 </c:forEach>
		</select>页
		<script language="javascript">
		<!--
		function topage(pagenum){
			window.location.href='/shop/product/list/display.do?sort=selldesc&style=${param.style}&brandid=${param.brandid}&sex=${param.sex}&typeid=${param.typeid}&page='+ pagenum;
		}
		//-->
		</script>
       </div>
     </div>
    </div>
	<jsp:include page="/WEB-INF/page/share/Foot.jsp" />
</body>
</html>