<input type='text' class='underLine'
	   readOnly id='${nameTxt}' 
	   name='${nameTxt}' size='${size}'><input type='hidden' name='${name}' id='${name}'><img src='../../images/share/button_display.gif' width='18' height='17' onClick='display()' style='cursor:hand'>

<%@ include file="/inc/dialog.inc"%>
<script language='javascript' src='../../js/cookie.js'></script>
<script language=''>
	initpage();
	
	function display(){
		var key = document.getElementById('${name}').value;
		var scode = document.getElementById('${name}');
		var stxt = document.getElementById('${nameTxt}');
		var strUrl = "/pages/common/displayTermStatus.jsp?key="+key+"&dirpath=${path}";
		var features="350,310,title.tmlAlarm.state";
		var a = OpenModal(strUrl,features);
		
		if(a != null){
			scode.value = a[0];
			stxt.value = a[1];
			SaveCookie();
		}
	}
	
	function SaveCookie(){
		var expireDate=new Date();
		expireDate.setTime(expireDate.getTime()+(24*60*60*1000*365));
		SetCookie("${name}",document.getElementsByName('${name}')[0].value,expireDate,"/",null,false);
	}
	
	function GetCookies(){
		var strTxt=GetCookie('${name}');
		var strCode=GetCookie('${nameTxt}');
	}
	
	function initpage(){
		var hiddenValue = GetCookie('${name}');
		var txtValue = GetCookie('${nameTxt}');
		var scode = document.getElementById('${name}');
		var stxt = document.getElementById('${nameTxt}');
		
		if(txtValue!=null && txtValue.length!=0 && hiddenValue !=null && hiddenValue.length != 0 ){
			scode.value=hiddenValue;
			stxt.value=txtValue;
		}else{
			scode.value="${dirCode}";		
			stxt.value="${dirTxt}";
		}
	}
</script>