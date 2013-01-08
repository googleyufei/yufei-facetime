<script language='JavaScript' src='<%=request.getContextPath()%>/js/calendar/fixDate.js'></script>
<script language="javascript">

function ShowDateTime(ctrlobj)
{ 
  ShowCalendar(ctrlobj,1)	
}
function ShowDate(ctrlobj)
{ 
  ShowCalendar(ctrlobj,2)	
}
function ShowTime(ctrlobj)
{ 
  ShowCalendar(ctrlobj,3)	
}

function ShowCalendar(ctrlobj,ptype)
{
  var RaletiveJsurl="<%=request.getContextPath()%>/js/calendar"	
  var showx = event.screenX;
  var showy = event.screenY;
  if(ptype == 1){
	  url=RaletiveJsurl+"/datetime.jsp";
	  width = 310;
	  height = 240;
	}else if(ptype == 2){
      url=RaletiveJsurl+"/date.jsp";
	  width = 310;
	  height = 220;
  }else if(ptype == 3){
      url=RaletiveJsurl+"/time.jsp";
	  width = 310;
	  height = 60;
  }
	if(showx + width >screen.width){
		showx = showx - width;
	}
	if(showy + height + 10 >screen.height){
		showy = showy -height -10;
	}else{
		showy = showy + 10;
	}
   var feather = "dialogWidth:"+width+"px; dialogHeight:"+height+"px; dialogLeft:"+showx+"px; dialogTop:"+showy+"px; status:no; directories:yes;scroll:no;Resizable:no;help:0;"  
   window.showModalDialog(url,ctrlobj,feather );
}
</script>