/*
* tablesort UI version 0.0.1
*  by ArcoKe
*  2006-06-06
*/
//当前表格对象	
var tableobj;
//当前点击对象
var curObj;
//排序方式，0升序1降序
var sorttype=0;
//排序的起始行,1表示第2行开始排序
var startrow=1;
//k为列的序号，对k列排序
function sortTable(tableid,k) {
	
  return;//add by ljming 2006/07/06
  
  var i;
  tableobj=tableid;
  var theRows=new Array();
  //thetitle为表格的标题
  var thetitle=new Array();
  //如果没有标题,不需要对thetitle付值
  thetitle[0]=new Array(tableobj.rows[0].cells[k].innerText.toLowerCase(),tableobj.rows[0].outerHTML);
  var j=0;;
  for(i=startrow;i<tableobj.rows.length;i++) {
	    theRows[j]=new Array(tableobj.rows[i].cells[k].innerText.toLowerCase(),tableobj.rows[i].outerHTML);
	    j++;
	}
	//首次点击为升序，再次点击降序和升序交替进行
	if(curObj!=k || sorttype==0){
	    theRows.sort(sortRowsUp);
	}else{
      theRows.sort(sortRowsDown);
  }
  sorttype = (sorttype+1)%2;
  curObj=k;
  var str=''
  for(i=0;i<theRows.length;i++) {  	
	    str+=theRows[i][1];
	}
  tableobj.outerHTML='<table id='+tableid.id+' border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" width="100%">'+thetitle[0][1]+str+'</table>'
  newsort(tableid.id);
  //运算时间计算
  //alert("计算共花去时间为："+(new Date()-start))
  return ;
}
//降序判断
function sortRowsDown(x,y) {
	if(x[0]>y[0]) return -1;
	else if(x[0]<y[0]) return 1;
	else return 0;
}
//升序判断
function sortRowsUp(y,x) {
	if(x[0]>y[0]) return -1;
	else if(x[0]<y[0]) return 1;
	else return 0;
}       
//重新对背景设置
function newsort(tableid){
	tableobj=document.getElementById(tableid);
  for(var i=startrow;i<tableobj.rows.length;i++) {
     if(i%2==1){
          tableobj.rows[i].style.backgroundColor="#fffffd";
     }else{
      	  tableobj.rows[i].style.backgroundColor="#F0F0F0";
     }
  }
  try{
     a.onclick();
  }catch(e){}
}
var a;
//点击行事件
function pass(tableid,trid){
  tableobj=tableid;
  a=trid;
  for(var i=startrow;i<tableobj.rows.length;i++) {
      if(i%2==1){
      	  tableobj.rows[i].style.backgroundColor="#fffffd";
      }else{
      	  tableobj.rows[i].style.backgroundColor="#F0F0F0";
      }
  }
	a.style.backgroundColor="#ccccff";
}
//add by ljming 2006/7/6
function TrMove(trObj)
{
  if(trObj.oriClass==null) return true
  if(trObj.className=='trMouseOver')
  {
      trObj.className=trObj.oriClass
  }	  
  else{	   
      trObj.oriClass=trObj.className
      trObj.className='trMouseOver'
  }
}
/*
实例调用：
sortTable(downloadList,0):对id号为downloadList的表格第0列进行排序
pass(downloadList,tr11):对对id号为downloadList的表格中id号为tr11的行点击颜色改变
<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
  <tr>
    <td width="30%" onclick="sortTable(downloadList,0)" class="oracolumncenterheader" style="CURSOR: hand">Name</td>
    <td width="20%" onclick="sortTable(downloadList,1)" class="oracolumncenterheader" style="CURSOR: hand">Type</td>
    <td width="20%" onclick="sortTable(downloadList,2)" class="oracolumncenterheader" style="CURSOR: hand">Money</td>
    <td width="30%" onclick="sortTable(downloadList,3)" class="oracolumncenterheader" style="CURSOR: hand">Date</td>
  </tr>
  <tr id="tr11" class="oracletdone" onclick="pass(downloadList,tr11)">
    <td width="30%"><a href="#">AddCommonInfo.mxp</a></td>
    <td width="20%">MXP File</td>
    <td width="20%">2614</td>
    <td width="30%">2002-12-30 16:45:22,Fri</td>
  </tr>
  <tr id="tr12" class="oracletdtwo" onclick="pass(downloadList,tr12)">
    <td>addtemplateparam.mxp</td>
    <td>MXP File</td>
    <td>3100</td>
    <td>2002-12-05 13:28:24,Sun</td>
  </tr>
  <tr id="tr13" class="oracletdone" onclick="pass(downloadList,tr13)">
    <td>addtemplateparam1.mxp</td>
    <td>MXP File1</td>
    <td>31001</td>
    <td>2002-12-05 13:28:27,Sun</td>
  </tr>
</table>
*/