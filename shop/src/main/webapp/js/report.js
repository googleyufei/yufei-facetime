function OperBrWindow(strUrl,windowid){
	var childw=window.open(strUrl,windowid,"top=0,left=0,menubar=no,status=no,scrollbars=yes,resizable=yes,width="+screen.Width+",height="+screen.Height/2)
   try{
      childw.focus();
   }catch(e){}

}
