<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="cn.grgbanking.phosphorframework.webapp.struts.action.WebConstants"%>
<%@ include file="/inc/htc.inc"%>
<html>
<head>
<title>msgBox</title>
<script language="JavaScript">
 var backUrl="/"
 var target="SELF"
 var buttonType="RETURN"
 
 <logic:present name="MSGBOX.KEY">
     backUrl='${MSGBOX.KEY.backUrl}'
	 target='${MSGBOX.KEY.target}'
	 buttonType='${MSGBOX.KEY.buttonType}'
 </logic:present>
 
 <logic:present name="<%=WebConstants.LINK_URL%>">
     backUrl='<%=request.getAttribute(WebConstants.LINK_URL).toString()%>'
 </logic:present>

 function BackTo()
 {
    if(buttonType=="CLOSE")
    {
	    if(window==window.parent) 
		{
           window.close();
		}else{
		   parent.close();  
		}    
        return;
    }
    if(buttonType=="RETURN")
    {
       if(document.parentWindow.history.length==0)
         window.close();
       else
         window.history.back();
       return;
    }
	if(backUrl=="<%=request.getContextPath()%>/")
	{
	   top.location=backUrl
	   return
	}

    if(target=="PARENT")
    {
        parent.location=backUrl;
    }else
    {
	   if(target=="TOP")
	   {
	       top.location=backUrl;
	   } else
	   {
          window.location=backUrl;
		}
    }
 }
 function ShowErrDetail()
 {
    var  divDetail=document.getElementById('divDetail')
	if(divDetail==null)
	   return;
	if(divDetail.style.visibility=='hidden')
	{
    	divDetail.style.visibility = 'visible'  
	}	
	else
	{
	    divDetail.style.visibility = 'hidden'
	}	
 } 
</script>
</head>
<body   bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table WIDTH=100% height="100%">
  <tr>
    <td >
      <table width=450 border=0 cellpadding=0 cellspacing=0 align="center" height="304" background="/shop/images/msg/message.gif" >
        <tr>
          <td colspan="4" height="52">&nbsp; </td>
        </tr>
        <tr>
          <td width="97" align="left" valign="top">&nbsp; </td>
          <td colspan=2 rowspan=2>
            <p align="center">
			  <logic:messagesPresent message="true" >
				<html:messages id="msg" message="true" >
				   ${msg"  filter="false}
				</html:messages>
			  </logic:messagesPresent>
			  
			  <logic:present name="MSGBOX.KEY">
                   <logic:notEmpty name="MSGBOX.KEY"  property="msgInfo">
					      ${MSGBOX.KEY.msgInfo}   
				   </logic:notEmpty>				  				  
		      </logic:present>
            </p>
            <logic:present name="MSGBOX.KEY"> 
               <logic:notEmpty name="MSGBOX.KEY"  property="errorStack">
                   <div id = "divDetail"  style="width=330;height:80;visibility:hidden;overflow:scroll">
				     ${MSGBOX.KEY.errorStack}
			       </div>
			   </logic:notEmpty>				  
			 </logic:present> 
			 <br/>
               <div align="center">
			     <logic:equal  name="MSGBOX.KEY" property="buttonType" value="OK" >
			       <input type="button" name="btnOk" value='<spring:message code="button.ok"/>' class="MyButton"  onClick = "BackTo()" image="<%=request.getContextPath()%>/images/msg/ok.gif">
			     </logic:equal>
			     <logic:equal  name="MSGBOX.KEY" property="buttonType" value="RETURN" >
			       <input type="button" name="btnOk" value='<spring:message code="button.return"/>' class="MyButton"  onClick = "BackTo()" image="<%=request.getContextPath()%>/images/msg/return.gif">
			     </logic:equal>
			     <logic:equal  name="MSGBOX.KEY" property="buttonType" value="CLOSE" >
			       <input type="button" name="btnOk" value='<spring:message code="button.close"/>' class="MyButton"  onClick = "BackTo()" image="<%=request.getContextPath()%>/images/msg/close.gif">
			     </logic:equal>
              </div>
          </td>
          <td width="9">&nbsp; </td>
        </tr>
        <tr>
          <td width="97" height="193" valign="top"> 
            <table width="91" border="0" cellspacing="0" cellpadding="0" height="67">
              <tr> 
                <td height="79"> 
                  <div align="center"><img src="/shop/images/msg/warn.gif" onclick="ShowErrDetail()" style="cursor:hand" width="50" height="42"></div>
                </td>
              </tr>
            </table>
          </td>
          <td width="9" height="193">&nbsp; </td>
        </tr>
        <tr>
          <td colspan="4">&nbsp; </td>
        </tr>
      </table>
    </td>
</tr>
</table>
</body>
</html>
<script language=javascript>
try{
   btnOk.focus()
}catch(e){
  //alert(e.description)
}
</script>