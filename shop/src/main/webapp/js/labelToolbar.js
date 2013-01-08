/*
create toolbar
@name 样式的前缀
@folder 存放按钮图片的目录
@num 样式总数目
@row 每一行样式数
@numNum  那个按钮位置留空。从1开始，多个项目用:分隔
*/
function GetToolsPic(name,folder,num,row,nullNum)
{
	var szHtm="";
	var count,i,j;
	var CreateFlag=true;
	var nullAry=nullNum.split(":");
	szHtm +='<table border="0" cellspacing="0" cellpadding="0" align="center"><tr>';
	for(i=0;i<num;i++)
	{
		CreateFlag = true;
		if((i+1)<10) count = "0"+(i+1);
		else count = (i+1);
  		szHtm += '<td width="62" height="50" ';
		for(j=0;j<nullAry.length;j++)
		{
			if(parseInt(nullAry[j])==(i+1))
			{
				CreateFlag = false;
			}
		}
		if(CreateFlag)
		{
			szHtm += 'background="../../images/geog/toolsicon/box_up.gif" onmousedown="ToolsMouseDown(this);ToolSeleced(\''+count+'\');" ';
			szHtm += ' style="cursor:hand" id=TD_b'+i+'>';
			szHtm += '<img src=../../images/geog/'+folder+"/"+name+count+'.gif ';
			//szHtm += " onerror=HandleImageError('TD_b',"+i+")";
		}
		szHtm += '></td>';
		if((i+1)%row==0) szHtm +="</tr><tr>";
	}
	szHtm +="</tr></table>";

	return szHtm;
}

/*
function HandleImageError(keywrod,num)
{
  var Tab_LoadImages=eval("document.all."+keywrod+num);
  Tab_LoadImages.innerHTML="";
}
*/

//call ToolSeleced function of parent window
function ToolSeleced(toolsNum)
{
  dialogArguments.parentWin.ToolSeleced(toolsNum)
}

//global variable of parent window which accessing through dialogArguments
//dialogArguments.parentWin.curDownToolsId

var ToolsBG1 = "../../images/geog/toolsicon/box_up.gif";
var ToolsBGImg = new Image();
ToolsBGImg.src="../../images/geog/toolsicon/box_dw.gif";
var ToolsBG2 = ToolsBGImg.src;
var TdObject;

//定义那些一按下去就马上起来的按钮的序号，1开始
var NotDownTools =[];
NotDownTools[0]=4;

function ResumeBg()
{
   TdObject.background = ToolsBG1;
}
function ToolsMouseDown(Tdname,notMouse)
{
	if(notMouse==null)
	  if ( event.button!=1 ) { return false; }

	var n1 = ToolsBG1;
	var n2 = ToolsBG2;
	TdObject = Tdname;

	if ( dialogArguments.parentWin.curDownToolsId != "" ) {
		eval(dialogArguments.parentWin.curDownToolsId).background = n1;
	}
	
	Tdname.background = n2;

	for(i=0;i<NotDownTools.length;i++)
	{
		if ( Tdname.id=="TD_b"+(NotDownTools[i]-1) ) 
		{
			dialogArguments.parentWin.curDownToolsId ="";	
				
			//raise up automatically after 200 millisecond
			setTimeout("ResumeBg()",200);
			return false;
		}
	}
	dialogArguments.parentWin.curDownToolsId = Tdname.id;
}

function ResumeLook()
{
   if(dialogArguments.parentWin.curDownToolsId!=null)
   {
     if(dialogArguments.parentWin.curDownToolsId!="")
     {
     	var Tdname=eval(dialogArguments.parentWin.curDownToolsId)
     	if(Tdname!=null)
          ToolsMouseDown(Tdname,true)	
     }	
   }	
}