/**
注意：前面的黑点不能删,请保存为UTF-8格式
**/

//定义全程变量
  var isIE = (document.all ? true : false);
  var displayHours;
  var displayMinutes;
  var displaySeconds = 0;
  var displayDivName;
  var oldValue;
  var today = new getToday();

function getXBrowserRef(eltname) {
 return (isIE ? document.all[eltname].style : document.layers[eltname]);
}

function hideElement(eltname) { getXBrowserRef(eltname).visibility = 'hidden'; window.close();}

function toggleVisible(eltname) {
 elt = getXBrowserRef(eltname);
 if (elt.visibility == 'visible' || elt.visibility == 'show') {
   elt.visibility = 'hidden';
 } else {
   elt.visibility = 'visible';
 }
}

   function newCalendar(eltName,type) {

      today = new getToday();
			var newCal;
			var parseHours;
      var parseMinutes;
      var parseSeconds;
			 parseHours = parseIntNum(displayHours + '');
			 parseMinutes = parseIntNum(displayMinutes + '');
			 parseSeconds = parseIntNum(displaySeconds + '');
      newCal = new Date(2006,1,1,parseHours,parseMinutes,displaySeconds);
			
      var day = -1;
      var startDayOfWeek = newCal.getDay();
      if ((today.year == newCal.getFullYear()) &&
                  (today.month == newCal.getMonth()))
     {
               day = today.day;
     }
     var daysGrid = makeDaysGrid(startDayOfWeek,day,newCal,eltName,type)
     if (isIE) {
        var elt = document.all[eltName];
        elt.innerHTML = daysGrid;
     } else {
        var elt = document.layers[eltName].document;
        elt.open();
        elt.write(daysGrid);
        elt.close();
     }
  }

  function incHours(delta,eltName,type){
  displayHours = parseIntNum(displayHours + '') + delta;
  if (displayHours >= 24) {
    displayHours = 0;
	}else if (displayHours<=-1){
	  displayHours = 23;
	}
    document.all.hour.value=displayHours;
	  setDay(eltName,type);
  }

  function incMinutes(delta,eltName,type){
  displayMinutes = parseIntNum(displayMinutes + '') + delta;
  if (displayMinutes >= 60) {
    displayMinutes = 0;
	}else if (displayMinutes<=-1){
	  displayMinutes = 59;
	}
	document.all.minute.value=displayMinutes;
	setDay(eltName,type);
  }	
	
  function incSeconds(delta,eltName,type){
  displaySeconds = parseIntNum(displaySeconds + '') + delta;
  if (displaySeconds >= 60) {
    displaySeconds = 0;
	}else if (displaySeconds<=-1){
	  displaySeconds = 59;
	}
	document.all.second.value=displaySeconds;
	setDay(eltName,type);
  }

  function makeDaysGrid(startDay,day,newCal,eltName,type) {
    var daysGrid;
		var hours = newCal.getHours();
	  var minutes = newCal.getMinutes();
	  var seconds = newCal.getSeconds();

   daysGrid = '<table border="1" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111">'
	 daysGrid += '<tr><td><table border=0 width=300 style="font-size:9pt" bgcolor=white>';
	 
	 daysGrid += '<tr><td colspan=7 align=center><a style="cursor:hand" onClick="incHours(-1,\'' + eltName + '\', \'' +type+'\')"><font size=1>&#9668;</font></a>';
	 daysGrid += '<input type=text size=1 id=hour maxlength=2 style="border:0;color:red;font:9pt;width:18" value='+ hours;
	 daysGrid += ' onkeypress=\"return dtp_KeyFilter(\'number\');\" onfocus=\"return dtp_focus(\'hour\');\" onblur=\"setHours(\'' +eltName+'\',\'' + type + '\');\"></font>';
	 daysGrid +='<a style="cursor:hand" onClick="incHours(1,\'' + eltName + '\', \'' +type+'\')"><font size=1>&#9658;</font></a>'+hourLabel+'&nbsp;&nbsp;&nbsp;';
	 daysGrid +='<a style="cursor:hand" onClick="incMinutes(-10,\'' + eltName + '\', \'' +type+'\')"><font size=1>&#9668;&#9668;&nbsp;&nbsp;&nbsp;</font></a>';
	 daysGrid +='<a style="cursor:hand" onClick="incMinutes(-1,\'' + eltName + '\', \'' +type+'\')"><font size=1>&#9668;</font></a>';
   daysGrid += '<input type=text size=1  id=minute maxlength=2 style="border:0;color:red;font:9pt;width:18" value='+ minutes;
	 daysGrid += ' onkeypress=\"return dtp_KeyFilter(\'number\');\"  onfocus=\"return dtp_focus(\'minute\');\" onblur=\"setMinutes(\'' +eltName+'\',\'' + type + '\');\"></font>';
	 daysGrid +='<a style="cursor:hand"  onClick="incMinutes(1,\'' + eltName + '\', \'' +type+'\')"><font size=1>&#9658;</font></a>'
	 daysGrid +='<a style="cursor:hand" onClick="incMinutes(10,\'' + eltName + '\', \'' +type+'\')"><font size=1>&nbsp;&nbsp;&nbsp;&#9658;&#9658;</font></a>'+minuteLabel+'';
	 daysGrid +='&nbsp;&nbsp;&nbsp;';
	 
	 daysGrid +='<a style="cursor:hand" onClick="incSeconds(-10,\'' + eltName + '\', \'' +type+'\')"><font size=1>&#9668;&#9668;&nbsp;&nbsp;&nbsp;</font></a>';
	 daysGrid +='<a style="cursor:hand" onClick="incSeconds(-1,\'' + eltName + '\', \'' +type+'\')"><font size=1>&#9668;</font></a>';
   daysGrid += '<input type=text size=1  id=second maxlength=2 style="border:0;color:red;font:9pt;width:18" value='+ seconds;
	 daysGrid += ' onkeypress=\"return dtp_KeyFilter(\'number\');\"  onfocus=\"return dtp_focus(\'second\');\" onblur=\"setSeconds(\'' +eltName+'\',\'' + type + '\');\"></font>';
	 daysGrid +='<a style="cursor:hand"  onClick="incSeconds(1,\'' + eltName + '\', \'' +type+'\')"><font size=1>&#9658;</font></a>'
	 daysGrid +='<a style="cursor:hand" onClick="incSeconds(10,\'' + eltName + '\', \'' +type+'\')"><font size=1>&nbsp;&nbsp;&nbsp;&#9658;&#9658;</font></a>'+secondLabel+'</td>';
	 	 	  
	 daysGrid +='<tr><td  bgcolor=#ccddff colspan=7><div align=\"center\">'
	 daysGrid +='<a id="okbutton" href="#" onClick="hideElement(\'' + eltName + '\')">'+okLabel;
	 daysGrid +='</a>&nbsp;&nbsp;<a href="#" onClick="clearAll(\'' + eltName + '\')">'+clearLabel;
	 daysGrid +='</a>&nbsp;&nbsp;<a href="#" onClick="cancle(\'' + eltName + '\')">'+cancelLabel;
	 daysGrid +='</a></div></td></tr>';
	 
	 return daysGrid + "</table></td></tr></table>";
  }

function setHours(eltName,type)
{
	 displayHours = parseIntNum(document.all.hour.value);
   if (displayHours >= 24) {
      displayHours = 0;
	 }else if (displayHours<=-1){
	    displayHours = 0;
	 }
   document.all.hour.value=displayHours;
	 setDay(eltName,type);   
}

function setMinutes(eltName,type)
{
	displayMinutes = parseIntNum(document.all.minute.value);
  if (displayMinutes >= 60) {
    displayMinutes = 59;
	}else if (displayMinutes<=-1){
	  displayMinutes = 0;
	}
	document.all.minute.value=displayMinutes;
	setDay(eltName,type);
}

function setSeconds(eltName,type)
{
	displaySeconds = parseIntNum(document.all.second.value);
  if (displaySeconds >= 60) {
    displaySeconds = 59;
	}else if (displaySeconds<=-1){
	  displaySeconds = 0;
	}
	document.all.second.value=displaySeconds;
	setDay(eltName,type);
}

function onSelectDay()
{
	okbutton.onclick();
}

  function setDay(eltName,type)
  {
	var strHoursEx;
	var strMinutesEx;
	var strSecondsEx;
		strHoursEx=displayHours;
	  strMinutesEx=displayMinutes;
	  strSecondsEx=displaySeconds;
	
	if(strHoursEx<10)
    {
       strHoursEx="0"+strHoursEx;
    }
	if(strMinutesEx<10)
    {
       strMinutesEx="0"+strMinutesEx;
    }
  if(strSecondsEx<10)
    {
       strSecondsEx="0"+strSecondsEx;
    }	    
	window.dialogArguments.value = strHoursEx +":"+strMinutesEx +":"+strSecondsEx;	
  }

  function clearAll(eltName)
  {
	  window.dialogArguments.value ="";
	  hideElement(eltName);
	  window.close();
  }

  function cancle(eltName)
  {
	  window.dialogArguments.value =oldValue;
	  hideElement(eltName);
	  window.close();
  }
  
function toggleDatePicker(eltName,type) {
  init(eltName,type);
  newCalendar(eltName,type);
  toggleVisible(eltName);
}

//根据目标文本框的值初始化参数的值，画出datetime下拉框。保存文本框的值
function init(eltName,type)
{	
  if (displayDivName && displayDivName != eltName) hideElement(displayDivName);
     displayDivName = eltName;
     oldValue = window.dialogArguments.value;
  if(oldValue == "")
	{
	     displayHours = new Date().getHours();
	     displayMinutes = new Date().getMinutes();
	     displaySeconds = new Date().getSeconds();
	   setDay(eltName,type);
	} else {
   
	   if(parseIntNum(oldValue.substring(0,2))==0)
	     displayHours = parseIntNum(oldValue.substring(1,2));
	   else
       displayHours = parseIntNum(oldValue.substring(0,2));
		 if(isNaN(displayHours)){
		 	 displayHours = 0;
		 }
       
	   if(parseIntNum(oldValue.substring(3,5))==0)
	     displayMinutes = parseIntNum(oldValue.substring(4,5));
	   else
			 displayMinutes = parseIntNum(oldValue.substring(3,5));
		 if(isNaN(displayMinutes)){
		 	 displayMinutes = 0;
		 }
		 
		 if(parseIntNum(oldValue.substring(6,8))==0)
		   displaySeconds = parseIntNum(oldValue.substring(7,8));
		 else
		 	 displaySeconds = parseIntNum(oldValue.substring(6,8));
		 if(isNaN(displaySeconds)){
		 	 displaySeconds = 0;
		 }
	}
}

function getToday() {
      this.now = new Date();
			this.hours = this.now.getHours();
			this.getMinutes = this.now.getMinutes();
			this.getSeconds = this.now.getSeconds();
}


function dtp_KeyFilter(dk_type) {
	var berr=false;
	switch(dk_type)	{
		case 'date':
			if (!(event.keyCode == 45 || event.keyCode == 47 || event.keyCode == 32 || event.keyCode == 58 || (event.keyCode>=48 && event.keyCode<=57)))
				berr=true;
			break;
		case 'number':
			if (!(event.keyCode>=48 && event.keyCode<=57))
				berr=true;
			break;
		case 'cy':
			if (!(event.keyCode == 46 || (event.keyCode>=48 && event.keyCode<=57)))
				berr=true;
			break;
		case 'long':
			if (!(event.keyCode == 45 || (event.keyCode>=48 && event.keyCode<=57)))
				berr=true;
			break;
		case 'double':
			if (!(event.keyCode == 45 || event.keyCode == 46 || (event.keyCode>=48 && event.keyCode<=57)))
				berr=true;
			break;
		default:
			if (event.keyCode == 35 || event.keyCode == 37 || event.keyCode==38)
				berr=true;
	}
	return !berr;
}

//时间字符串转换为整数
function parseIntNum(str)
{
	if(str.charAt(0) == '0' && str.length>1){
		str=str.substring(1,str.length);
	}	
	return parseInt(str);
}

function dtp_focus(df_type) 
{
  var src=event.srcElement;
  if(src && src.tagName=="INPUT")
  {
	 switch(df_type)	{
		case 'hour':	break;
		case 'minute':	break;
		case 'second':	break;
		default:;
	 }
    src.select();
  }
  return true;
}