<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/page/share/taglib.jsp" %>
<html>
<head>
<title>自助设备经营管理系统</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script language="javascript" src="/shop/js/jquery.min.1.7.js"></script>
</head>

<frameset framespacing="0" border="0" rows="60,*,15" frameborder="0">
  <frame name="banner" scrolling="no" noresize target="contents" src="/shop/control/center/top.do" marginwidth="0" marginheight="0">
  <frameset cols="168,*">
    <frame name="menuframe" target="mainframe" src="/shop/control/center/left.do" scrolling="no" marginwidth="0" marginheight="0">
    <frame name="mainframe" scrolling="auto" noresize src="/shop/control/center/right.do" marginwidth="0" marginheight="0">
  </frameset>
  <frame name="menuframe" target="mainframe" src="/shop/control/center/end.do" scrolling="no" marginwidth="0" marginheight="0">
</frameset><noframes></noframes>
</html>