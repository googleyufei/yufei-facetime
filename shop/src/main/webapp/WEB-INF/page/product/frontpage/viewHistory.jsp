<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/page/share/taglib.jsp" %>
<c:forEach items="${viewHistory}" var="viewproduct" varStatus="statu">
<li class="bj_blue"><a href="/shop/product/view.do?productid=${viewproduct.id}" target="_blank" title="${viewproduct.name}">${viewproduct.name}</a>
</li>
</c:forEach>			