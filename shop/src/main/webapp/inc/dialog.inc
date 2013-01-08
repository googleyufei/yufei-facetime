<script language="javascript">
/**
 * strHref: URL
 * strCustom:"width,height,title,bundle"
*/
function OpenModal(strHref,strCustom)
{
  var aryCustom=strCustom.split(",");
  var width=aryCustom.length>0?aryCustom[0]:300
  var height=aryCustom.length>1?aryCustom[1]:300
  var title=aryCustom.length>2?aryCustom[2]:""
  var bundle=aryCustom.length>3?aryCustom[3]:""
  return OpenModal2(strHref,width,height,title,bundle)	
}

function OpenModal2(strHref,width,height,title,bundle)
{
   var strContextPath='<%=request.getContextPath()%>'	
   if(strHref.indexOf('http')<0)
   {
      if(strHref.substr(0,1)=='/')	
        strHref=strContextPath+strHref
      else  
        strHref=strContextPath+"/"+strHref
   }  
   if(title==null)
     title=""
     
   if(bundle==null)
     bundle=""
     
 
   strHref=strContextPath+"/modalDialog.jsp?urlStr="+escape(strHref)+"&title="+title

   if(bundle!="")
   {
    strHref=strHref+"&bundle="+bundle	
   } 	
   
   return window.showModalDialog(strHref,window,"dialogWidth:"+width+"px;dialogHeight:"+height+"px;resizable:no;scroll:0;help:0;status:0")
}

/**
* ����������λ�õ������
*/
function OpenAlt(strHref,strCustom)
{
  var aryCustom=strCustom.split(",");
  var width=aryCustom.length>0?aryCustom[0]:300
  var height=aryCustom.length>1?aryCustom[1]:300
  var title=aryCustom.length>2?aryCustom[2]:""
  var bundle=aryCustom.length>3?aryCustom[3]:""
  
  return OpenAlt2(strHref,width,height,title,bundle)	
}

function OpenAlt2(strHref,width,height,title,bundle)
{
	var strContextPath='<%=request.getContextPath()%>'
	var showx = event.screenX;
	var showy = event.screenY;	
	if(strHref.indexOf('http')<0)
	{
		if(strHref.substr(0,1)=='/')	
			strHref=strContextPath+strHref
		else  
			strHref=strContextPath+"/"+strHref
	}  
	if(title==null)
		title=""
     
	if(bundle==null)
		bundle=""     
 
	strHref=strContextPath+"/modalDialog.jsp?urlStr="+escape(strHref)+"&title="+title

	if(bundle!="")
	{
		strHref=strHref+"&bundle="+bundle	
	} 	
  
	var feather = "dialogWidth:"+width+"px; dialogHeight:"+height+"px; dialogLeft:"+showx+"px; dialogTop:"+showy+"px; status:no;directories:yes;scroll:no;Resizable:no;help:0;"     
   return window.showModalDialog(strHref,this,feather)
}
</script>