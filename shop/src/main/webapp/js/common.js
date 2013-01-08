//MOUSE MOVE IN                                      OK

function showmouseover(object)
{
  object.style.backgroundColor='#DBE2F0'
}

//MOUSE MOVE OUT                                     OK

function showmouseout(object)
{
  object.style.backgroundColor='#ffffff'
}


//IN button                                     OK

function showmouseover_button(object)
{
  //object.style.backgroundColor='#FFCC66'
  object.className='buttstyle1'
}

//OUT button                                      OK

function showmouseout_button(object)
{
   //object.style.backgroundColor='#BDDBFF'
   object.className='buttstyle'
}


//                                 OK

function showmouseover_back(object)
{
  object.className='back1'
}

//                                   OK

function showmouseout_back(object)
{
   object.className='back'
}

//    buttonauto                                     OK
function showmouseover_buttonauto(object){object.className='buttstyleauto1'}
//    buttonauto                                  OK
function showmouseout_buttonauto(object) {object.className='buttstyleauto'}




var index_count = 0;
var title_string = 'Feel-View 3.0';
var title_length = title_string.length;
var cmon;
var kill_length = 0;
function loopTheScroll()
{
    scrollTheTitle();
    if(kill_length > title_length)
     {
     clearTimeout(cmon);
     }

    kill_length++;
    cmon = setTimeout("loopTheScroll();",100)
}

function scrollTheTitle()
{
    var doc_title = title_string.substring((title_length - index_count - 1),title_length);
    document.title = doc_title;

    index_count++;
}

loopTheScroll();




function validateTwoDate( startDate, endDate){
  if(parseDate(startDate)>parseDate(endDate)){
    alert('<bean:message  key="form.startNotGreatEnd"/>');
    return false;
  }
  return true;
  
}

function parseDate( date){
  if(!date ){
    return 0;
  }
  var sDate = date.substring(0,4)+date.substring(5,7)+date.substring(8,10);
  return parseInt(sDate);
  
}
function StringLength(strInput)                 

{

  return strInput.replace(/[^\x00-\xff]/g,"**").length;

}

function CheckInputLength(object, message, maxlength)  //    
{
if (message == null) message = "";
  if (StringLength(trim(object.value)) > maxlength) {

     alert(message + '<bean:message  key="oper.lengthNotGreat"/>:' + maxlength + "  " + maxlength/2);

	 object.focus();

	 return false;  

  }

  object.value = trim(object.value);

  return true;

}


//
function checkSelect(frmObj){
	//var frmObj = document.listAgent;
	var elObj,intlocal;
	var iCount = 0;
	var strSelectd ="";
	for(var i=0;i<frmObj.elements.length;i++){
		elObj = frmObj.elements[i];
		strObjName = elObj.name;

		intlocal= strObjName.indexOf("checkbox");
		if (intlocal>=0){
			if (elObj.checked == true){
				++iCount;
			}
		}
	}
	return (iCount)
}

//
function getSelectId(frmObj){
   //var frmObj = document.listAgent;
	var elObj,intlocal;
	var strSelectd ="";
	for(var i=0;i<frmObj.elements.length;i++){
		elObj = frmObj.elements[i];
		strObjName = elObj.name;
		intlocal= strObjName.indexOf("checkbox");
		if (intlocal>=0){
			if (elObj.checked == true){
				strSelectd = elObj.value;
			}
		}
	}
	return (strSelectd)
}
//
function selectAll(obj,frmObj){
	var coll=frmObj.tags("input");
	 for (i=0;i<coll.length;i++){
	    if (coll.item(i).name=="checkbox") {
			    coll.item(i).checked=obj.checked;
		 }
	}
}

function closeWin() {
 //
 if(window.opener!=null)
 {
	 window.close();
 }  
}

//
function winsize(){
  var x=640;
  var y=480;
  var xx=(window.screen.width-x)/2;
  var yy=(window.screen.height-y)/2;
  //window.resizeTo(x,y);
  window.moveTo(xx,yy);
  setTimeout("winsize()",50);
}


function TrMove(moveIn, which)
{
  if (moveIn == 1)
    which.className = "tLine1"
  else
    which.className= "tLine3"
}

function modify(id){
 parent.window.modify(id); 
}

//
/********************************** chinese ***************************************/
/**
*check input string is chinese
*return value£º
*if string is chinese      then return false
if string isn't chinese then reuturn true 
*/
function checkHasChinese(str)
{
    //if string is "" then password
    if (str == "")
        return true;
    var pattern = /[^\x00-\xff]/;
    if (pattern.test(str))
        return false;
    else
        return true;
}

/***************************compute string's length**************************/
/**
*compute input string's length(a chinese characotor 's length is 2)
*/
function computeStringLength(str){
   return str.replace(/[^\x00-\xff]/g,"**").length;
}