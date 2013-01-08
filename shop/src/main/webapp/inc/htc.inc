<style>
.MyCheckBox{
	behavior:url(<%=request.getContextPath()%>/htc/checkbox.htc);
}
.MyButton{
	behavior:url(<%=request.getContextPath()%>/htc/button.htc);
}
.MyInput{
  behavior:url(<%=request.getContextPath()%>/htc/input.htc);
  border:1px solid #dddddd;
  backgroundColor:"#FEFEFE";
  color:"#666666";
}
.MySelect{
  border:1px solid #dddddd;
  backgroundColor:"#FEFEFE";
  color:"#666666";
  visibility: visible;
}

.CheckBoxTable{
	display:inline;
	cursor: hand;
	height:16;
}

.txtSel{
   font-size:9pt;
   color:green;
}
.txtNoSel{
   font-size:9pt;
   color:black;
}
.checkBoxImg{

}
.checkBoxDisable{
    font-size:9pt;
	color:#000000;cursor:default;
}
.ButtonTable{
	display:inline;
	cursor: hand;
	height:19;
}
.ButtonTable_td{
	border-top:1pt solid #000000;border-bottom:1pt solid #000000;
	padding-top:0;
	background-image: url(<%=request.getContextPath()%>/images/btbak.gif);
	background-repeat: repeat-x;
	cursor:hand;
	color:#000000;
}
.ButtonDisable{
	filter:dropshadow(color=#FFFFFF,offx=1,offy=1);color:#b2b2b2;cursor:default;
}
.ButtonValue{
	color:#222222;
}
</style>