/*
* framemenu UI version 0.0.1
*  by ArcoKe
*  2006-06-08
*/

// global variable
var _gv_tmp_checkbox_array = new Array();

//add by ljming 2006/07/05
var  title_label_array=new Array();
var  tab_id_array;
var  simulate_label_click;

function ActiveTab(tabId)
{
  if(simulate_label_click==null) return;
  var index=0;
  for(var i=0;i<tab_id_array.length;i++)
  {
     if(tab_id_array[i]==tabId)
     {
        index=i;	
        break;	
     }  
  }	
  simulate_label_click(title_label_array[index]);	
}

function setTabSet(_tabset_id,_tabs){
    var _tab_array = _tabs.split(",");
    tab_id_array=_tab_array;
    var _tabset = document.getElementById(_tabset_id);
    _tabset.tabArray = _tab_array;
    
    //now set selectArray for each tab page
    var _tab;
    for(var i=0;i<_tab_array.length;i++){
        _tab = document.getElementById(_tab_array[i]);
        _tab.checkboxArray = _gv_tmp_checkbox_array;
        _gv_tmp_checkbox_array = new Array();
    }
    
}

function achieveJackal(){
    initElementGroup(document.body);
    //document.onpropertychange = _document_property_change;    
    _finishInit();
}

function initElementGroup(_element){
    
    if(!_element) return;
    initElement(_element);
    
    for(var i=0;i<_element.children.length;i++){
    	initElementGroup(_element.children[i]);
    }
}



function initTabSet(_tabset){
    // initBodyDiv first
    initBodyDiv(_tabset);
    // initTitle
    initTabSetTitle(_tabset);
}


function initElement(_element){ // 初始化界面中的元素，按照 jui_type 确定 样式 一个分发器
    var _jui_type;
    _jui_type = _element.getAttribute("jui_type");
    //alert(_jui_type);
    switch(_jui_type){
        case "JTabSet":{
            initTabSet(_element);
            break;
        }
        default:{
            break;
        }
    }
}

function initBodyDiv(_tabset){
    var _tabset_id = _tabset.getAttribute("id");
    var _tabset_body;
    
    var _body_class = _tabset.getAttribute("bodyclass");
    if(!_body_class) _body_class = "titletop_tabbody";
    // the default _body_class must defined is "titleup_tabbody" and "titledown_tabbody"
    
    _tabset_body = document.getElementById("JtsBody_" + _tabset_id);  
    _tabset.body = _tabset_body;
    _tabset_body.className = _body_class;
    // set body div class
}

function initTabSetTitle(_tabset){
    var _tab_array = _tabset.tabArray;
    var _title_row = _tabset.tBodies[0].insertRow();
    var _title_cell = _title_row.insertCell();
    
    var _label_class = _tabset.getAttribute("labelclass");
    if(!_label_class) _label_class = "tab_title";
    // if did not define the labelclass of tabset labelclass is "tab_title"
    
    var _tab_padding = _tabset.getAttribute("padding");
    if(!_tab_padding) _tab_padding = "10";
    _tab_padding = parseInt(_tab_padding);
    // if did not define the _tab_padding of tabset _tab_padding is "10"
    
    _title_cell.firstCell = true;
    _title_cell.innerHTML = "<image src='"+_gv_imageroot+"top_start_tabtitle.gif"+"'>";
    
    var _title_label,_active_label;
    
    var _active_index = _tabset.getAttribute("activeindex");
    if(!_active_index) _active_index = "1";
    _active_index = parseInt(_active_index);
    // if did not define the _active_index of tabset _active_index is "1"
    
    title_label_array=new Array(_tab_array.length); //add by ljming
    for(var i=0;i<_tab_array.length;i++){
    	_title_cell = _title_row.insertCell();
    	_title_cell.firstCell = false;
    	
    	_title_cell.background = _gv_imageroot+"top_tab_title_back.gif";
    	
    	_title_label = document.createElement("<div nowrap class='"+_label_class+"'></div>");
    	_title_label.labelClass = _label_class;
    	_title_label.tabset = _tabset;
    	_title_label.labelId = i+1;
    	
    	_tab_div = document.getElementById(_tab_array[i]);
    	_title_label.tabDiv = _tab_div;
    	
    	_title_label.innerText = _tab_div.getAttribute("tabLebel");
    	_title_label.onmouseover = _label_mouse_over;
    	_title_label.onmouseout = _label_mouse_out;
    	_title_label.onclick = _label_click;
    	_title_label.frameid = _tab_div.getAttribute("frameid");
    	_title_label.tourl = _tab_div.getAttribute("tourl");
    	_title_label.doFun = _tab_div.getAttribute("doFun");
    	
    	_title_cell.appendChild(_title_label);
    	
    	_title_cell = _title_row.insertCell();
    	
    	if(i == _tab_array.length-1){
    	    _title_cell.innerHTML = "<image src='"+_gv_imageroot+"top_end_tabtitle.gif"+"'>";
        } else {
            _title_cell.innerHTML = "<image src='"+_gv_imageroot+"top_between_tabtitle.gif"+"'>";
	    }
    	
    	if(_active_index == i+1) _active_label = _title_label;
    	
    	_tab_div.style.visibility = "hidden";
    	_tab_div.style.overflow="hidden";
    	_tab_div.style.position="absolute";
    	_tab_div.style.left=0;
    	_tab_div.style.top=0;
    	//_tab_div.style.width = _tabset.body.clientWidth-_tab_padding*2;
    	_tab_div.style.height = _tabset.body.clientHeight-_tab_padding;
    	_tab_div.style.margin=2;
    	
    	title_label_array[i]=_title_label; //add by ljming   
    }
    
    setLabelActive(_active_label);
    
    function _label_mouse_over(){
        return label_mouse_over(this);
    }
    function label_mouse_over(_element){
        _element.className = _element.labelClass + "_mouseover";
    }
    function _label_mouse_out(){
        return label_mouse_out(this);
    }
    function label_mouse_out(_element){
        _element.className = _element.labelClass + "_mouseout";
    }

    function _label_click(){
        return label_click(this);
    }

    function label_click(_label){
        var _tabset = _label.tabset;
        // when click set native active cell normal
        if(_tabset._active_label == _label) return;
    
        setLabelNormal(_tabset._active_label);
        setLabelActive(_label);
        try{
        doURL(_label.frameid,_label.tourl);
        }catch(e){}
        try{        	
        eval(_label.doFun);
        }catch(e){}
        	
    }
    
    simulate_label_click=function(_label)
    {
    	label_click(_label)
    }
    
    function doURL(frameid,tourl){
    	var p=document.getElementById(frameid);
    	if(p.src==null || p.src=='' || p.src!=tourl){	
        p.src = tourl;
      }
    }
    
    function setLabelNormal(_label){
    
        var _tabset = _label.tabset;
    
        var _id = _label.labelId;
        var _cell_id = getCellIdByLabelId(_id);
    
        if(_id == 1){
            _tabset.cells[_cell_id-1].innerHTML = "<image src='"+_gv_imageroot+"top_start_tabtitle.gif"+"'>";
        } else {
            _tabset.cells[_cell_id-1].innerHTML = "<image src='"+_gv_imageroot+"top_between_tabtitle.gif"+"'>";
        }
    
        if(_id == _tabset.tabArray.length){
    	    _tabset.cells[_cell_id+1].innerHTML = "<image src='"+_gv_imageroot+"top_end_tabtitle.gif"+"'>";
        } else {
            _tabset.cells[_cell_id+1].innerHTML = "<image src='"+_gv_imageroot+"top_between_tabtitle.gif"+"'>";
        }
    
        _tabset.cells[_cell_id].background = _gv_imageroot+"top_tab_title_back.gif";

        //now save the select value
        _label.tabDiv.style.position = "absolute";
        _label.tabDiv.style.visibility = "hidden";
    }
    
    function setLabelActive(_label){
    
        var _tabset = _label.tabset;
        var _id = _label.labelId;
        var _cell_id = getCellIdByLabelId(_id);
        if(_id == 1){
            _tabset.cells[_cell_id-1].innerHTML = "<image src='"+_gv_imageroot+"top_active_start_tabtitle.gif"+"'>";
        } else {
            _tabset.cells[_cell_id-1].innerHTML = "<image src='"+_gv_imageroot+"top_active_between_tabtitle1.gif"+"'>";
        }
    
        if(_id == _tabset.tabArray.length){
            _tabset.cells[_cell_id+1].innerHTML = "<image src='"+_gv_imageroot+"top_active_end_tabtitle.gif"+"'>";
        } else {
            _tabset.cells[_cell_id+1].innerHTML = "<image src='"+_gv_imageroot+"top_active_between_tabtitle2.gif"+"'>";
        }
    
        _tabset.cells[_cell_id].background = _gv_imageroot+"top_active_tab_title_back.gif";
    
    
        _label.tabset._active_label = _label;

        _tabset.body.appendChild(_label.tabDiv);
    
        _label.tabDiv.style.position = "";
        _label.tabDiv.style.visibility = "visible";
        
        viewChild(_label.tabDiv);
    }
    
function viewChild(_element){
    
    if(!_element) return;
    //alert(_element.id +"|"+_element.tagName + _element.children.length);
    if(_element.tagName.toLowerCase() == "iframe"){
    	try{
    		//alert(eval(_element.id).bodyid.getElementsByTagName("select"));
    		var selectobj = eval(_element.id).bodyid.getElementsByTagName("select");
        for(var isel=0;isel<selectobj.length;isel++){
        	  selectobj[isel].style.visibility="hidden";
        		selectobj[isel].style.visibility="visible";
        }
    		}catch(e){}
    }
    try{
    for(var i=0;i<_element.children.length;i++){
    	viewChild(_element.children[i]);
    }}catch(e){}
}

    function getCellIdByLabelId(_labelId){
        return _labelId*2-1;
    }
            
}


function _finishInit(){
    document.body.style.visibility = "visible";
}