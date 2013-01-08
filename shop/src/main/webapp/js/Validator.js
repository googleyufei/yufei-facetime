
/*************************************************
Validator 表单验证
itemName:表单名
pattern;验证类型
*************************************************/
	function Validate(itemName,pattern){
	 var Require= /.+/;
	 var Email= /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
     var Phone= /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/;
    var Mobile= /^((\(\d{2,3}\))|(\d{3}\-))?13\d{9}$/;
	var Mobiles= /^(((\(\d{2,3}\))|(\d{3}\-))?13\d{9}|[;])+$/;
    var Url= /^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/;
   var IdCard = /^\d{15}(\d{2}[A-Za-z0-9])?$/;
   var Currency = /^\d+(\.\d+)?$/;
   var Number= /^\d+$/;
   var Zip = /^[1-9]\d{5}$/;
   var QQ = /^[1-9]\d{4,10}$/;
   var Integer = /^[-\+]?\d+$/;
   var integer = /^[+]?\d+$/;
   var Double= /^[-\+]?\d+(\.\d+)?$/;
   var double = /^[+]?\d+(\.\d+)?$/;
   var English = /^([A-Za-z]|[,\!\*\.\ \(\)\[\]\{\}<>\?\\\/\'\"])+$/;
   var english = /^([a-z])+$/;
   var Chinese = /^[\u0391-\uFFE5]+$/;
   var BankCard = /^([0-9]|[,]|[;])+([;])+$/;
   var DoFilter=/^.+\.(?=wav)(wav)$/;
   var DoExcelFilter=/^.+\.(?=xls)(xls)$/;
   var Key=/^([A-Za-z0-9]|[-\_])+$/;
   //对日期格式进行验证  格式为 yyyy-mm-dd 
   var VDate=/^\d{4}-((0[1-9]{1})|(1[0-2]{1}))-((0[1-9]{1})|([1-2]{1}[0-9]{1})|(3[0-1]{1}))$/;
   //对日期时间格式进行验证  格式为 yyyy-mm-dd  hh:mm:ss
	var VDateTime=/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/;
	var yesOrNo = /^[YyNn]$/;
    var itemNameValue=document.getElementsByName(itemName)[0].value

	var UnSafe= /^(([A-Z]*|[a-z]*|\d*|[-_\~!@#\$%\^&\*\.\(\)\[\]\{\}<>\?\\\/\'\"]*)|.{0,5})$|\s/;
	//不能是中文
	var noChinese = /[^\x00-\xff]/;
	
   		var flag
	switch(pattern){ 
	 case "Require":
		 flag = Require.test(itemNameValue);
	      break;
	 case "Email":
		 flag = Email.test(itemNameValue);
	      break;
	 case "Phone":
		 flag = Phone.test(itemNameValue);
	      break;
	 case "Mobile":
		 flag = Mobile.test(itemNameValue);
	      break;
	 case "Mobiles":
		 flag = Mobiles.test(itemNameValue);
	      break;
	 case "Url":
		 flag = Url.test(itemNameValue);
	      break;
	 case "IdCard":
		 flag = IdCard.test(itemNameValue);
	      break;
	 case "Currency":
		 flag = Currency.test(itemNameValue);
	      break;
	 case "Number":
		 flag = Number.test(itemNameValue);
		      break;
	 case "Zip":
		 flag = Zip.test(itemNameValue);
	      break;
	 case "QQ":
		 flag = QQ.test(itemNameValue);
	      break;
	 case "integer":
		 flag = integer.test(itemNameValue);
	      break;		  
	 case "Integer":
	// if (itemNameValue.length>0)
		 flag = Integer.test(itemNameValue);
	//else 
	//	flag=true
	      break;
	 case "Double":
		 flag = Double.test(itemNameValue);
	      break;
	 case "double":
			 flag = double.test(itemNameValue);
		      break;
	 case "English":
		 flag = English.test(itemNameValue);
	      break;
	 case "english":
		 flag = english.test(itemNameValue);
	      break;
	 case "Chinese":
		 flag = Chinese.test(itemNameValue);
	      break;
	 case "BankCard":
		 flag = BankCard.test(itemNameValue);
	      break;
	 case "DoFilter":
		 flag = DoFilter.test(itemNameValue);
	      break;
	 case "DoExcelFilter":
		 flag = DoExcelFilter.test(itemNameValue);
	      break;
	 case "Key":
		 flag = Key.test(itemNameValue);
	      break;
	 case "VDate":
		 flag = VDate.test(itemNameValue);
	      break;
	 case "VDateTime":
		 flag = VDateTime.test(itemNameValue);
	      break;		  
	 case "UnSafe":
		 flag = !UnSafe.test(itemNameValue);
	      break;		  
	 case "noChinese":
		 flag = !noChinese.test(itemNameValue);
	      break;
	 case "yesOrNo":
		 flag = yesOrNo.test(itemNameValue);
		 break;
	default :
		flag = false;
		break;
 	}
//	if (!flag){
//	alert(msg);
//	document.getElementsByName(itemName)[0].focus();
//	}
   return flag

}
