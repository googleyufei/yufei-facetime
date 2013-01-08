<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/page/share/taglib.jsp" %>
<UL>
	<c:forEach items="${topsellproducts}" var="topsellproduct" varStatus="statu">
		<li class="bx">${statu.count}.<a href="/shop/product/view.do?productid=${topsellproduct.id}" target="_blank" >${topsellproduct.name}</a></li></c:forEach>			
</UL>