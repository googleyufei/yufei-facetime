<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/page/share/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<base target="mainframe">
<html>
<head>
<title>menuset</title>
</head>
<style type="text/css">
body {
	scrollbar-base-color: #F8F8F8;
	scrollbar-arrow-color: #6E99F3;
	background-image: url(/shop/images/main/leftbg.gif)
}

a {
	color: #000000;
	text-decoration: none;
}
</style>
<body topmargin="0" leftmargin="0">
	<table align="center" valign="top" border="0" cellpadding="0"
		cellspacing="0" background="/shop/menutree/lefttitle.gif" width="173">
		<tr>
			<td width="173" height="35" align="center"></td>
		</tr>
	</table>
	<script type="text/javascript">
/*********************************** /
目录树代码 V1.22
  .无限分类
  .以"-"数目表示层数,清晰明了
  .动态展开&收缩
  .支持 IE , FireFox
  天窗 http://www.faisun.com
  暖阳 faisun@sina.com
  2004 年 11 月

/ **********************************/

var faisunMenu_openedfolderimage_src="/shop/menutree/openedfolder.gif";
var faisunMenu_closedfolderimage_src="/shop/menutree/closedfolder.gif";
var faisunMenu_menufileimage_src="/shop/menutree/menufile.gif";

var faisunMenu_treetext=new Array();
var faisunMenu_treeurl=new Array();
var faisunMenu_treeurltarget=new Array();

var faisunMenu_treeNum=0;

document.write("<style type='text/css'>.blockhide{display:none;} .blockmove{overflow: hidden;height:1px;display:block;} .blockshow{overflow: visible; display:block;} .hideme{overflow: visible;display:none;} .showme{overflow: visible;display:block;} .faisunMenu td{font-size:12px;} </style>");

window.document.body.onselectstart=new Function("return false");

function outinit(itemNo,dir,blockheight){ //缩小一个div的高度

  var subfiles=document.getElementById("item"+itemNo);
  if(blockheight==0){
	subfiles.className="blockshow";
	subfiles.style.height='';
	blockheight=parseInt(subfiles.offsetHeight);
	if(dir>0) subfiles.className="blockmove";
  }
  var outinspeed=blockheight/10;
  var nowheight=parseInt(subfiles.offsetHeight)+outinspeed*dir;
  if(nowheight<=0){
    subfiles.className="blockhide";
	return;
  }else{
    subfiles.className="blockmove";
  }
  if(nowheight>=blockheight && dir>0){
    subfiles.className="blockshow";
	subfiles.style.height='';
	return;
  }
  subfiles.style.height=nowheight;
  subfiles.scrollTop=blockheight;
  parentsresize(subfiles);
  setTimeout("outinit("+itemNo+","+dir+","+blockheight+")",15);
}

function parentsresize(obj){ //缩小父类div的高度

	do{
		if(obj.className=="faisunMenu"){
			break;
		}
		if(obj.className=="blockshow"){
			obj.style.height="";
		}
	}while(obj=obj.parentElement);
}

function showhideit(itemNo){
  var showfolder=document.images["openedfolderimage"+itemNo];
  var hidefolder=document.images["closedfolderimage"+itemNo];
  var subfiles=document.getElementById("item"+itemNo);
  if(subfiles.className=="blockmove") {return;}

  if(showfolder.className=="hideme") {
	hidefolder.className="hideme";
    showfolder.className="showme";
	outinit(itemNo,1,0);
  }
  else {
	showfolder.className="hideme";
    hidefolder.className="showme";
	outinit(itemNo,-1,0);
  }
}

function addtree(text,url,target){
  faisunMenu_treetext.push(text);
  faisunMenu_treeurl.push(url?url:"");
  faisunMenu_treeurltarget.push(target?target:"mainframe");
}

function getsubnum(text){ //算出前面有几个"-"号

  var newtext=text.replace(/^-*/,"");
  return text.length-newtext.length;
}


function createtree(){

  faisunMenu_treeNum++;
  var treestatus=new Array();
  var treeendlayer=new Array();
  var openedlayer=new Array();

  var next_subnum=0;
  for(i=faisunMenu_treetext.length-1;i>=0;i--){ //从后面分析起,是否有为结束位置或有子树枝

    var subnum=getsubnum(faisunMenu_treetext[i]);
	treestatus[i]=0;
	if(subnum<next_subnum){  //有子目录
	   treeendlayer[next_subnum]=0;
	   treestatus[i]+=1;
	}
	if(!treeendlayer[subnum]){ //结束位置
	   treeendlayer[subnum]=1;
	   treestatus[i]+=2;
	}
	//显然地,既有子目录又是结束位置时 treestatus[i]=3;
	next_subnum=subnum;
  }
  
  var echo="<div class='faisunMenu'>";
  for(i=0;i<faisunMenu_treetext.length;i++){
     if(!faisunMenu_treetext[i]) continue;
	 var subnum=getsubnum(faisunMenu_treetext[i]);
	 var newtext=faisunMenu_treetext[i].replace(/^-*\*?/,"");
	 
	 if(treestatus[i]==1||treestatus[i]==3){
	   var havechild=1;
	 }else{
	   var havechild=0;
	 }

	 if(treestatus[i]==2||treestatus[i]==3){
	   openedlayer[subnum]=0;
	   var barstatus=2;
	 }else{
	   openedlayer[subnum]=1;
	   var barstatus=1;		 
	 }

	 var showme=faisunMenu_treetext[i].match(/^-*\*/);
	 var openfold=(i==faisunMenu_treetext.length-1?0:faisunMenu_treetext[i+1].match(/^-*\*/));
	 var li=i-1;
     if(i>0&&(treestatus[li]==1||treestatus[li]==3)){
	   echo += "<div id='item"+faisunMenu_treeNum+li+"' class='"+(showme?"blockshow":"blockhide")+"'>";
	 }
	 echo += "<table width='153' border='0' cellspacing='0' cellpadding='0' "+(subnum==0?"height=25":"")+"><tr>\n";
	for(j=1;j<subnum;j++){
	  echo += "<td width='20' valign=bottom><img src='/shop/menutree/"+(openedlayer[j]?"bar3.gif":"spacer.gif")+"' width='20' height='20'></td>\n";
	}
	if(subnum>0){
	  echo += "<td width='20' valign=bottom><img src='/shop/menutree/bar"+barstatus+".gif' width='20' height='20'></td>";
	}
	var clicktoshowhide=(havechild?" onclick='showhideit("+faisunMenu_treeNum+""+i+")' style='cursor:pointer;' ":"");
    echo += "<td width='22' valign=bottom id='tdid"+i+"' "+clicktoshowhide+"><img name='openedfolderimage"+faisunMenu_treeNum+""+i+"' src='"+(havechild?faisunMenu_openedfolderimage_src:faisunMenu_menufileimage_src)+"' class='"+(openfold?"showme":"hideme")+"' width='20' height='20'><img name='closedfolderimage"+faisunMenu_treeNum+""+i+"' src='"+(havechild?faisunMenu_closedfolderimage_src:faisunMenu_menufileimage_src)+"' class='"+(openfold?"hideme":"showme")+"' width='20' height='20'></td>";
	echo += "<td nowrap valign=bottom><a onmousedown='return false;' "+clicktoshowhide+" "+(faisunMenu_treeurl[i]?"href='"+faisunMenu_treeurl[i]+"'":"name='#'")+" target='"+faisunMenu_treeurltarget[i]+"'>"+newtext+"</a></td></tr></table>\n\n";	

     if(barstatus==2&&!havechild){
	   for(j=subnum;j>=0;j--){
	     if(!openedlayer[j]) echo += "</div>";else break;
	   }
	 }
  }
  echo += "</div>";
  document.write(echo);

  //清空列表以接受下一个菜单

  faisunMenu_treetext=new Array();
  faisunMenu_treeurl=new Array();
  faisunMenu_treeurltarget=new Array();

}
/************************* /

例:
addtree("标题一","title1.htm");
addtree("-子目录一","sunmenu1.htm");
addtree("-子目录二","sunmenu1.htm");
createtree();

.可以建立多个目录树,重复上面的代码即可.

.addtree()三个参数: 
  (1) 显示的文本,有几个"-"号表位于几层,"-"号后紧跟"*"号表该层默认展开
  (2) URL;
  (3) target,URL的打开目标窗口,默认"forummainframe"
createtree();生成一个目录树,可以生成多个

/ ************************/
</script>
<script language="JavaScript">
		addtree('<B>控制面板</B>');
	<%String menulist = (String) request.getAttribute("lefttree");
			out.print(menulist);%>
		createtree();
	</script>
</body>
</html>