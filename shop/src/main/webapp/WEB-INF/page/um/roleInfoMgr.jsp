<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/framemenu.inc"%>
<html>
    <head>
        <title>jui type</title>
        <link href="/shop/css/css_v2.css" type="text/css" rel="stylesheet">
        <link href="/shop/css/jackal.css" type="text/css" rel="stylesheet">
        <script language="javascript">
        var _gv_imageroot = "/shop/images/tablemenu/"; 
        </script>        
    </head>
    
    <body topmargin="0">
        <%@ include file="/inc/navigationBar.inc"%>
        <table id="JtsPool_demo" cellSpacing="0" cellPadding="0" width="100%" height="96%">
            <tbody>
                <tr height="1">
                    <td background="/shop/images/tablemenu/top_tab_title_blank.gif" align="right">
                        <div id="JtsTitle_demo">
                            <table id="demo" cellSpacing="0" cellPadding="0"
                                   jui_type="JTabSet"
                                   padding="10"
                                   activeindex="1">
                                <tbody>
                                </tbody>
                            </table>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td align="center">
                        <div id="JtsBody_demo">
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>     
      
		<div id="roleList" tabLebel="<%=CURRENT_MENU_ITEM%>">
            <form name="roleLstForm">
                <table width="100%" height="100%" cellSpacing="0" cellPadding="0" border="0">
                    <tr>
                        <td align="left" height="100%">
        <iframe src="/shop/pages/um/roleinfo/list.do" id="roleList" name="roleList" width="100%" height="100%" marginwidth="0" marginheight="0" frameborder="0" border="0" scrolling="auto" noresize></iframe>
                        </td>
                    </tr>
                </table>
            </form>
        </div>	
        
    </body>
    <script language="javascript">
     setTabSet("demo","roleList");
     achieveJackal();
    </script>    
</html>