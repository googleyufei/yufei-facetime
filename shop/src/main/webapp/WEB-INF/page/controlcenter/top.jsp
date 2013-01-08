<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ include file="/WEB-INF/page/share/taglib.jsp" %>
<%
Date dNow = new Date(); 
SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
%>
<HTML><HEAD><TITLE>Untitled Document</TITLE>
<META http-equiv=Content-Type content="text/html; charset=UTF-8">
<STYLE type=text/css>.top {
	FONT-SIZE: 9pt; VERTICAL-ALIGN: bottom; PADDING-TOP: 0px; FONT-FAMILY: "宋体"; 006600: 
}
BODY {
	MARGIN-TOP: 0px; FONT-SIZE: 9pt; MARGIN-LEFT: 0px; COLOR: #ffffff; LINE-HEIGHT: 150%; MARGIN-RIGHT: 0px; FONT-FAMILY: "宋体"; BACKGROUND-COLOR: #ffffff ;
Crollbar-Face-color:#FFCC00;
Scrollbar-Highlight-Color:#FF0000;
Scrollbar-Shadow-Color:#FFFFFF;
Scrollbar-3Dlight-Color:#000000;
Scrollbar-Arrow-Color:#000000;
Scrollbar-Track-Color:#FDEAC4;
Scrollbar-Darkshadow-Color:#FFFF00;
}
TD {
	FONT-SIZE: 9pt; COLOR: #ffffff; LINE-HEIGHT: 150%; FONT-FAMILY: 宋体
}
.My {  font-size: 25px; color:#FFFFFF; font-weight: :bold }
TD.top {
	PADDING-RIGHT: 4px; PADDING-LEFT: 4px; PADDING-BOTTOM: 0px; PADDING-TOP: 0px
}
A:link {
	COLOR: #ffffff; TEXT-DECORATION: none
}
A:visited {
	COLOR: #ffffff; TEXT-DECORATION: none
}
A:hover {
	COLOR: #cebef7; TEXT-DECORATION: none
}
</STYLE>

<SCRIPT language=JavaScript>
<!--

function MM_findObj(n, d) { //v3.0
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document); return x;
}
function MM_nbGroup(event, grpName) { //v3.0
  var i,img,nbArr,args=MM_nbGroup.arguments;
  if (event == "init" && args.length > 2) {
    if ((img = MM_findObj(args[2])) != null && !img.MM_init) {
      img.MM_init = true; img.MM_up = args[3]; img.MM_dn = img.src;
      if ((nbArr = document[grpName]) == null) nbArr = document[grpName] = new Array();
      nbArr[nbArr.length] = img;
      for (i=4; i < args.length-1; i+=2) if ((img = MM_findObj(args[i])) != null) {
        if (!img.MM_up) img.MM_up = img.src;
        img.src = img.MM_dn = args[i+1];
        nbArr[nbArr.length] = img;
    } }
  } else if (event == "over") {
    document.MM_nbOver = nbArr = new Array();
    for (i=1; i < args.length-1; i+=3) if ((img = MM_findObj(args[i])) != null) {
      if (!img.MM_up) img.MM_up = img.src;
      img.src = (img.MM_dn && args[i+2]) ? args[i+2] : args[i+1];
      nbArr[nbArr.length] = img;
    }
  } else if (event == "out" ) {
    for (i=0; i < document.MM_nbOver.length; i++) {
      img = document.MM_nbOver[i]; img.src = (img.MM_dn) ? img.MM_dn : img.MM_up; }
  } else if (event == "down") {
    if ((nbArr = document[grpName]) != null)
      for (i=0; i < nbArr.length; i++) { img=nbArr[i]; img.src = img.MM_up; img.MM_dn = 0; }
    document[grpName] = nbArr = new Array();
    for (i=2; i < args.length-1; i+=2) if ((img = MM_findObj(args[i])) != null) {
      if (!img.MM_up) img.MM_up = img.src;
      img.src = img.MM_dn = args[i+1];
      nbArr[nbArr.length] = img;
  } }
}

function MM_preloadImages() { //v3.0
 var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
   var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
   if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function exitsystem(){
	if (confirm('您确定要退出系统吗？'))
	  window.parent.location="<html:rewrite action='/Administrator/Logout'/>"; 
}
//-->
</SCRIPT>

<META content="MSHTML 6.00.2600.0" name=GENERATOR></HEAD>
<BODY bgColor=#ffffff 
onload="javascript:MM_preloadImages('/shop/images/OAHeader_r1_c3_f2.gif','/shop/images/OAHeader_r1_c3_f3.gif','/shop/images/OAHeader_r1_c5_f2.gif','/shop/images/OAHeader_r1_c5_f3.gif','/shop/images/OAHeader_r1_c7_f2.gif','/shop/images/OAHeader_r1_c7_f3.gif','/shop/images/OAHeader_r1_c9_f2.gif','/shop/images/OAHeader_r1_c9_f3.gif','/shop/images/OAHeader_r1_c11_f2.gif','/shop/images/OAHeader_r1_c11_f3.gif');">
<TABLE style="MARGIN: 0px" cellSpacing=0 cellPadding=0 width="100%" 
bgColor=#6386de border=0 valign="top">
  <TBODY>
  <TR vAlign=top>
      <TD vAlign=top width="550" class="My" Align=center><img src="/shop/images/main/banner.gif" border=0 /></TD>
    </TR></TBODY></TABLE></BODY></HTML>
