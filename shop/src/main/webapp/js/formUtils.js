/**
 本文件必须保存成utf-8格式,否则会乱码引起解释错误！
 需要stringUtils.js,cooke.js
**/
//------------------------------------------------------------------------ 
//把表单可见元素(text,textarea,select-one,radio,checkbox,password)内容保存到cookie
function SaveFormLook(fm,cookieName,expireDate)
{
   var cookie=""
   var len=fm.elements.length
   for(var i=0;i<len;i++)
   {
        if(fm.elements[i].type=="text"||fm.elements[i].type=="textarea"||fm.elements[i].type=="password")
        {
           cookie+=fm.elements[i].name+"="+fm.elements[i].value+"w3u7"
        }
        else
        {
          if(fm.elements[i].type=="select-one")
          {
              cookie+=fm.elements[i].name+"="+fm.elements[i].selectedIndex+"w3u7"
          }else
          {
              if(fm.elements[i].type=="radio"||fm.elements[i].type=="checkbox")
              {
                   var c=fm.elements[i]
                   if(c.checked)
                          cookie+=fm.elements[i].name+"=1w3u7"
                   else
                          cookie+=fm.elements[i].name+"=0w3u7"
              }//radio or checkbox
          }
       }//else
  }
  if(expireDate!=null)  
    SetCookie(cookieName,cookie,expireDate,"/",null,false)
  else
    SetCookie(cookieName,cookie)  
}
//恢复表单可见控件的内容
function FillFormLook(fm,cookieName)
{
   var cookie=GetCookie(cookieName)
   if(cookie==null||cookie=="")
   {
       return false
   }
   var len=fm.elements.length
   for(var i=0;i<len;i++)
   {
        if(fm.elements[i].type=="text"||fm.elements[i].type=="textarea"||fm.elements[i].type=="password")
        {
          fm.elements[i].value=GetByName(cookie,fm.elements[i].name,"w3u7")
        }else
        {
             if(fm.elements[i].type=="select-one")
             {
                  var selectIndex=GetByName(cookie,fm.elements[i].name,"w3u7");
                  if(selectIndex!="")
                       fm.elements[i].options[parseInt(selectIndex)].selected=true
             }else
             {
                 if(fm.elements[i].type=="radio"||fm.elements[i].type=="checkbox")
                 {
                    var c=fm.elements[i]
                    if(GetByName(cookie,fm.elements[i].name,"w3u7")=="1")
                        c.checked=true
                    else
                        c.checked=false
                 }
            }//else
         }//else
   }//for
  return true
}
//------------------------------------------------------------------------