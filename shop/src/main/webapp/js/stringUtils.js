/*
got rid of blank of string
*/
function Trim(str1)
{
  return str1.replace(new RegExp("^ {1,}| {1,}$", "ig"),'');
}
/*
got rid of blank from textfield of form
*/
function TrimFormText(fm)
{
   var len=fm.elements.length
   for(var i=0;i<len;i++)
   {
        if(fm.elements[i].type=="text"||fm.elements[i].type=="textarea")
        {
           fm.elements[i].value=Trim(fm.elements[i].value)
        }
   }//for        
}
/*
encoding url before submit
*/
function EscapeUrl(str1)
{
  var re;
  var str1;

  re = new RegExp("^ {1,}| {1,}$", "ig");
  str1=str1.replace(re,'');

  re=/</gi;
  str1=str1.replace(re,"&lt;");

  re=/>/gi;
  str1=str1.replace(re,"&gt;");

  re=/\"/gi;
  str1=str1.replace(re,"&quot;");

  //re=/\'/gi;
  //str1=str1.replace(re,"‘");

  re=/%/gi;
  str1=str1.replace(re,"%25");
  re=/&/gi;
  str1=str1.replace(re,"%26");
  re=/\$/gi;
  str1=str1.replace(re,"%24");
  re=/#/gi;
  str1=str1.replace(re,"%23");

  return str1;
}
/*
 whether  legal number string
*/
 function IsNumber(strData)
 {
       if(strData=="") return false
       var ch
       for(var i=0;i<strData.length;i++)
       {
       	  ch=strData.charAt(i)
          if(ch<"0"||ch>"9")
          {
             return false
          }
       }//for
       return true
 }
/*
 get by name from string
*/
function GetByName(src,name,symbol)
{
   name=name+"="
   var i=src.indexOf(name)
   if(i<0) return ""
   var j=src.indexOf(symbol,i)
   if(j<0)
       return src.substring(i+name.length)
   else
       return src.substring(i+name.length,j)
}