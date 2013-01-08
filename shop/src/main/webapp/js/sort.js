var dom = (document.getElementsByTagName) ? true : false;
var ie5 = (document.getElementsByTagName && document.all) ? true : false;
var arrowUp, arrowDown;
if (ie5 || dom)
 initSortTable();
function initSortTable() {
 arrowUp = document.createElement("SPAN");
 var tn = document.createTextNode("↓");
 arrowUp.appendChild(tn);
 arrowUp.className = "arrow";
 arrowDown = document.createElement("SPAN");
 var tn = document.createTextNode("↑");
 arrowDown.appendChild(tn);
 arrowDown.className = "arrow";
}

function sortTable(tableNode, nCol, bDesc, sType) {
 var tBody = tableNode.tBodies[0];
 var trs = tBody.rows;
 var a = new Array();
 
 for (var i=0; i<trs.length; i++) {
  a[i] = trs[i];
 }
 
 a.sort(compareByColumn(nCol,bDesc,sType));
 
 for (var i=0; i<a.length; i++) {
  tBody.appendChild(a[i]);
 }
}
function CaseInsensitiveString(s) {
 return String(s).toUpperCase();
}
function parseDate(s) {
 return Date.parse(s.replace(/\-/g, '/'));
}
function toNumber(s) {
    return Number(s.replace(/[^0-9\.]/g, ""));
}
function compareByColumn(nCol, bDescending, sType) {
 var c = nCol;
 var d = bDescending;
 
 var fTypeCast = String;
 
 if (sType == "Number")
  fTypeCast = Number;
 else if (sType == "Date")
  fTypeCast = parseDate;
 else if (sType == "CaseInsensitiveString")
  fTypeCast = CaseInsensitiveString;
 return function (n1, n2) {
  if (fTypeCast(getInnerText(n1.cells[c])) < fTypeCast(getInnerText(n2.cells[c])))
   return d ? -1 : +1;
  if (fTypeCast(getInnerText(n1.cells[c])) > fTypeCast(getInnerText(n2.cells[c])))
   return d ? +1 : -1;
  return 0;
 };
}

function sortColumn(e) {
 var tmp, el, tHeadParent;
 if (ie5)
  tmp = e.srcElement;
 else if (dom)
  tmp = e.target;
 tHeadParent = getParent(tmp, "THEAD");
 el = getParent(tmp, "TH");
 if (tHeadParent == null)
  return;
  
 if (el != null) {
  var p = el.parentNode;
  var i;
  if (el._descending) // catch the null
   el._descending = false;
  else
   el._descending = true;
  
  if (tHeadParent.arrow != null) {
   if (tHeadParent.arrow.parentNode != el) {
    tHeadParent.arrow.parentNode._descending = null;   
   }
   tHeadParent.arrow.parentNode.removeChild(tHeadParent.arrow);
  }
  if (el._descending)
   tHeadParent.arrow = arrowDown.cloneNode(true);
  else
   tHeadParent.arrow = arrowUp.cloneNode(true);
  el.appendChild(tHeadParent.arrow);
   
  // get the index of the td
  for (i=0; i<p.cells.length; i++) {
   if (p.cells[i] == el) break;
  }
  var table = getParent(el, "TABLE");
  // can't fail
  
  sortTable(table,i,el._descending, el.getAttribute("type"));
 }
}

function getInnerText(el) {
 if (ie5) return el.innerText; //Not needed but it is faster
 
 var str = "";
 
 for (var i=0; i<el.childNodes.length; i++) {
  switch (el.childNodes.item(i).nodeType) {
   case 1: //ELEMENT_NODE
    str += getInnerText(el.childNodes.item(i));
    break;
   case 3: //TEXT_NODE
    str += el.childNodes.item(i).nodeValue;
    break;
  }
  
 }
 
 return str;
}
function getParent(el, pTagName) {
 if (el == null) return null;
 else if (el.nodeType == 1 && el.tagName.toLowerCase() == pTagName.toLowerCase())
  return el;
 else
  return getParent(el.parentNode, pTagName);
}



/**
<html>
  <head>
    <title>商品库存查询</title>     
  </head>  
  <body leftmargin="8" topmargin="8">
      单击标题头的列就可以升降序
<table onclick="sortColumn(event)" width="100%" border="0" align="center" cellpadding="3" cellspacing="1" bgcolor="#BBBBBB">
  <THEAD> 
  <tr> 
    <th  title="按商品名称排序" style="cursor:hand">商品名称</th>
    <th  title="按商品类别排序" style="cursor:hand">商品类别</th>
    <th  title="按规  格排序" style="cursor:hand">规格</th>
    <th  title="按单  位排序" style="cursor:hand">单位</th>
    <th  title="按单  价排序" style="cursor:hand" >单价(元)</th>
    <th  title="按库存数量排序" style="cursor:hand">库存数量</th>
  </tr>
  </THEAD> 
  <tr onmouseout="this.style.backgroundColor=''" onmouseover="this.style.backgroundColor='#ACC7FF'" bgcolor=#FFFFFF> 
    <td>妇科千金片</td>
    <td>中成药</td>
    <td>72s*150盒</td>
    <td>盒</td>
    <td>15</td>
    <td>20</td>
  </tr>
  <tr onmouseout="this.style.backgroundColor=''" onmouseover="this.style.backgroundColor='#ACC7FF'" bgcolor=#EEEEEE> 
    <td>复方地塞米松乳膏（皮炎平）</td>
    <td>化学药制剂</td>
    <td>10g*20支*40中包</td>
    <td>支</td>
    <td>12</td>
    <td>36</td>
  </tr>
  <tr onmouseout="this.style.backgroundColor=''" onmouseover="this.style.backgroundColor='#ACC7FF'" bgcolor=#FFFFFF> 
    <td>安神补脑液</td>
    <td>中成药</td>
    <td>10ml*10支*50盒</td>
    <td>盒</td>
    <td>11.5</td>
    <td>86</td>
  </tr>
  <tr onmouseout="this.style.backgroundColor=''" onmouseover="this.style.backgroundColor='#ACC7FF'" bgcolor=#EEEEEE> 
    <td>复方地巴唑氢氯噻嗪胶囊</td>
    <td>化学药制剂</td>
    <td>60s*200瓶</td>
    <td>瓶</td>
    <td>13.5</td>
    <td>76</td>
  </tr>
  <tr onmouseout="this.style.backgroundColor=''" onmouseover="this.style.backgroundColor='#ACC7FF'" bgcolor=#FFFFFF> 
    <td>枫蓼肠胃康片</td>
    <td>中成药</td>
    <td>0.24g*12s*2板*10盒*20中盒</td>
    <td>盒</td>
    <td>11.58</td>
    <td>77</td>
  </tr>
  <tr onmouseout="this.style.backgroundColor=''" onmouseover="this.style.backgroundColor='#ACC7FF'" bgcolor=#EEEEEE> 
    <td>乌鸡白凤丸(水蜜丸)</td>
    <td>中成药</td>
    <td>6g*10袋*40盒</td>
    <td>盒</td>
    <td>152.4</td>
    <td>688</td>
  </tr>
  <tr onmouseout="this.style.backgroundColor=''" onmouseover="this.style.backgroundColor='#ACC7FF'" bgcolor=#FFFFFF> 
    <td>复方利血平氨苯蝶啶片</td>
    <td>化学药制剂</td>
    <td>10s*300盒</td>
    <td>盒</td>
    <td>15.4</td>
    <td>255</td>
  </tr>
  <tr onmouseout="this.style.backgroundColor=''" onmouseover="this.style.backgroundColor='#ACC7FF'" bgcolor=#EEEEEE> 
    <td>双料喉风散</td>
    <td>中成药</td>
    <td>1g*6瓶*60盒</td>
    <td>盒</td>
    <td>18.6</td>
    <td>100</td>
  </tr>
</table>
  </body>
</html>
*/