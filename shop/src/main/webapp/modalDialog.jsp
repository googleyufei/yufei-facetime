<%@ page contentType="text/html; charset=UTF-8" %>
<%
 String urlStr=request.getParameter("urlStr");
 String title=request.getParameter("title");
 String bundle=request.getParameter("bundle");
 if(urlStr==null)
   urlStr="";
 else
   urlStr=java.net.URLDecoder.decode(urlStr);
 if(title==null||title.equals(""))
   title="modalDialog.defTitle";
%>
<html>
<head>
<title><%=title %></title>
</head>
  <frameset framespacing="0" border="0" frameborder="0">
     <frame name="dgFrame" scrolling="auto" noresize  src="<%=urlStr%>">
  </frameset>
</html>
