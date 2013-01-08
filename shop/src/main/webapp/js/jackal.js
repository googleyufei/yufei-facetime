/*
* Jackal UI version 0.3.1
*  by JackieChen
*  2005-05-21
*/

// global variable
var _gv_tmp_checkbox_array = new Array();
var _gv_dataform_array = new Array();
var _gv_active_editor = null;//上次被激活的 editor
var _gv_dropdown_array = new Array();// 页面中所有的 dropdown 组成的数组
var _gv_is_active_control_short = null; // 判断动态 control 是否被 缩短
var _gv_is_drop_btn_show = null;
var _gv_is_drop_box_show = null;
var _gv_current_drop_obj = null;// 当前激活的 dropdown对象
var _gv_drop_content_row = null;// 当前dropdownbox 的 行
var _gv_drop_now_select_row_index = null;// 当前 dropdown.control 中的值对应的 box 中的行index

//

function setTabSet(_tabset_id,_tabs){
    var _tab_array = _tabs.split(",");
    var _tabset = document.getElementById(_tabset_id);
    _tabset.tabArray = _tab_array;
    
    //now set selectArray for each tab page
    var _tab;
    for(var i=0;i<_tab_array.length;i++){
        _tab = document.getElementById(_tab_array[i]);
        _init_tab_checkbox_array(_tab,_tab);
        _tab.checkboxArray = _gv_tmp_checkbox_array;
        _gv_tmp_checkbox_array = new Array();
    }
    
    function _init_tab_checkbox_array(_element,_tab){
    	var _jui_type = _element.getAttribute("jui_type");
        if((_jui_type == "JEditor")&&(_element.tagName.toLowerCase() == "input")&&(_element.type == "checkbox")){
            _element.inTab = _tab;
            var _i = _gv_tmp_checkbox_array.length;
            var _obj_checkbox = new Object();
            _obj_checkbox._control = _element;
            _obj_checkbox._checked = false;
            
            _gv_tmp_checkbox_array[_i] = _obj_checkbox;
            _gv_tmp_checkbox_array["_index_of_"+_element.id] = _i;
        }
        
        for(var j=0;j<_element.children.length;j++){
            _init_tab_checkbox_array(_element.children[j],_tab);
        }
    }
    
}

function achieveJackal(){
    initElementGroup(document.body);
    initDatas();
    
    document.onpropertychange = _document_property_change;
    
    _finishInit();
}

function initElementGroup(_element){
    
    if(!_element) return;
    initElement(_element);
    
    for(var i=0;i<_element.children.length;i++){
    	initElementGroup(_element.children[i]);
    }
}

function initElement(_element){ // 初始化界面中的元素，按照 jui_type 确定 样式 一个分发器
    var _jui_type;
    _jui_type = _element.getAttribute("jui_type");
    
    switch(_jui_type){
        case "JTabSet":{
            initTabSet(_element);
            break;
        }
        case "JEditor":{
            break;
        }
        case "JCalCurCell":{
            initCalCurCell(_element);
            break;
        }
        case "JCalPreCell":{
            initCalPreCell(_element);
            break;
        }
        case "JDateInput":{
            initDateInput(_element);
            break;
        }
        case "JDateButton":{
            initDateButton(_element);
            break;
        }
        default:{
            break;
        }
    }
}

function initDateInput(_input){
    _input.className = "jui_date_input";
    //_input.style.width = "100%";
    _input.style.height = "18";
}

function initDateButton(_button){
    _button.className = "jui_date_button";
    _button.style.width = "20";
    _button.style.height = "20";
}

function initCalCurCell(_cell){
    _cell.className = "jui_cur_cell";
}

function initCalPreCell(_cell){
    _cell.className = "jui_pre_cell";
}


function initTabSet(_tabset){

    var _placement = _tabset.getAttribute("placement");
    if((!_placement)||(_placement!="bot")) _placement = "top";
    _tabset.placement = _placement;
    
    // initBodyDiv first
    initBodyDiv(_tabset);
    // initTitle
    initTabSetTitle(_tabset);
}

function initBodyDiv(_tabset){
    var _placement = _tabset.placement;
    var _tabset_id = _tabset.getAttribute("id");
    var _tabset_body;
    
    var _body_class = _tabset.getAttribute("bodyclass");
    if(!_body_class) _body_class = "title" + _placement + "_tabbody";
    // the default _body_class must defined is "titleup_tabbody" and "titledown_tabbody"
    
    _tabset_body = document.getElementById("JtsBody_" + _tabset_id);  
    _tabset.body = _tabset_body;
    _tabset_body.className = _body_class;
    // set body div class
}

function initTabSetTitle(_tabset){
    var _placement = _tabset.placement;
    
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
    _title_cell.innerHTML = "<image src='"+_gv_imageroot+_placement+"_start_tabtitle.gif"+"'>";
    
    var _title_label,_active_label;
    
    var _active_index = _tabset.getAttribute("activeindex");
    if(!_active_index) _active_index = "1";
    _active_index = parseInt(_active_index);
    // if did not define the _active_index of tabset _active_index is "1"

    for(var i=0;i<_tab_array.length;i++){
    	_title_cell = _title_row.insertCell();
    	_title_cell.firstCell = false;
    	
    	_title_cell.background = _gv_imageroot+_placement+"_tab_title_back.gif";
    	
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
    	
    	_title_cell.appendChild(_title_label);
    	
    	_title_cell = _title_row.insertCell();
    	
    	if(i == _tab_array.length-1){
    	    _title_cell.innerHTML = "<image src='"+_gv_imageroot+_placement+"_end_tabtitle.gif"+"'>";
        } else {
            _title_cell.innerHTML = "<image src='"+_gv_imageroot+_placement+"_between_tabtitle.gif"+"'>";
	}
    	
    	if(_active_index == i+1) _active_label = _title_label;
    	
    	_tab_div.style.visibility = "hidden";
    	_tab_div.style.overflow="hidden";
    	_tab_div.style.position="absolute";
    	_tab_div.style.left=0;
    	_tab_div.style.top=0;
    	_tab_div.style.width = _tabset.body.clientWidth-_tab_padding*2;
    	_tab_div.style.height = _tabset.body.clientHeight-_tab_padding;
    	_tab_div.style.margin=2;
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
    }
    
    function setLabelNormal(_label){
    
        var _tabset = _label.tabset;
    
        var _id = _label.labelId;
        var _cell_id = getCellIdByLabelId(_id);
        var _placement = _tabset.placement;
    
        if(_id == 1){
            _tabset.cells[_cell_id-1].innerHTML = "<image src='"+_gv_imageroot+_placement+"_start_tabtitle.gif"+"'>";
        } else {
            _tabset.cells[_cell_id-1].innerHTML = "<image src='"+_gv_imageroot+_placement+"_between_tabtitle.gif"+"'>";
        }
    
        if(_id == _tabset.tabArray.length){
    	    _tabset.cells[_cell_id+1].innerHTML = "<image src='"+_gv_imageroot+_placement+"_end_tabtitle.gif"+"'>";
        } else {
            _tabset.cells[_cell_id+1].innerHTML = "<image src='"+_gv_imageroot+_placement+"_between_tabtitle.gif"+"'>";
        }
    
        _tabset.cells[_cell_id].background = _gv_imageroot+_placement+"_tab_title_back.gif";

        //now save the select value
        saveTabSelectValue(_label.tabDiv);
        _label.tabDiv.style.position = "absolute";
        _label.tabDiv.style.visibility = "hidden";
    }
    
    function setLabelActive(_label){
    
        var _tabset = _label.tabset;
        var _id = _label.labelId;
        var _cell_id = getCellIdByLabelId(_id);
        var _placement = _tabset.placement;
    
        if(_id == 1){
            _tabset.cells[_cell_id-1].innerHTML = "<image src='"+_gv_imageroot+_placement+"_active_start_tabtitle.gif"+"'>";
        } else {
            _tabset.cells[_cell_id-1].innerHTML = "<image src='"+_gv_imageroot+_placement+"_active_between_tabtitle1.gif"+"'>";
        }
    
        if(_id == _tabset.tabArray.length){
            _tabset.cells[_cell_id+1].innerHTML = "<image src='"+_gv_imageroot+_placement+"_active_end_tabtitle.gif"+"'>";
        } else {
            _tabset.cells[_cell_id+1].innerHTML = "<image src='"+_gv_imageroot+_placement+"_active_between_tabtitle2.gif"+"'>";
        }
    
        _tabset.cells[_cell_id].background = _gv_imageroot+_placement+"_active_tab_title_back.gif";
    
    
        _label.tabset._active_label = _label;

        _tabset.body.appendChild(_label.tabDiv);
    
        setTabSelectValue(_label.tabDiv);
        _label.tabDiv.style.position = "";
        _label.tabDiv.style.visibility = "visible";
    
        var _active_div = _label.tabDiv;
    }
    
    function getCellIdByLabelId(_labelId){
        return _labelId*2-1;
    }
    
    function setTabSelectValue(_tab){
    
        var _tab_select_array = _tab.checkboxArray;
    
        for(var i=0;i<_tab_select_array.length;i++){
    	    var _selectbox = _tab_select_array[i]._control;
    	    var _checked = _tab_select_array[i]._checked;
    	    _selectbox.checked = _checked;
        }
    }
    
    function saveTabSelectValue(_tab){
        var _tab_select_array = _tab.checkboxArray;
        for(var i=0;i<_tab_select_array.length;i++){
    	    var _selectbox = _tab_select_array[i]._control;
    	    _tab_select_array[i]._checked = _selectbox.checked;
        }
    }
    
}

function initDatas(){
    for(var i=0;i<_gv_dataform_array.length;i++){
    	initData(_gv_dataform_array[i],_gv_dataform_array[i].data.firstNode);
    }
}

function initData(_dataform,_result_set){//初始化数据，也就是一个按照 datatype 进行初始化的分发器
    var _fields = _dataform.fields;
    var _dataform_id = _dataform.id;
    var _control,_data_type;
    
    for(var i=0;i<_fields.length;i++){
    	_control = document.getElementById(_dataform_id+"_control_"+_fields[i].fieldName);
    	_control.dataform = _dataform;
    	_control.field = _fields[i];
    	
    	_data_type = _fields[i].dataType;
    	
    	switch(_data_type){
    	    case "Boolean":{
    	    	var _b_object = eval("_"+_dataform.id+"_"+_fields[i].fieldName);
    	    	var _checkbox_intab = _control.inTab;
    	    	
    	    	if(_b_object._isTrue(_result_set.getValueById(i+1))){
    	            _control.checked = true;
    	        } else {
    	            _control.checked = false;
    	        }
    	    	
    	        break;
    	    }
    	    case "String":{
    	    	_control.value = _result_set.getValueById(i+1);
    	    	break;
    	    }
    	    case "Int":{
    	    	_control.value = _result_set.getValueById(i+1);
    	    	break;
    	    }
    	    case "Date":{
    	    	_control.value = _result_set.getValueById(i+1);
    	    	break;
    	    }
    	    default:{
    	        break;
    	    }
    	}
    }
}

function _document_property_change(){//页面属性发生变化时触发
	try{
    set_element_property(document.activeElement);
  }catch(e){}
}

function set_element_property(_element){
    //根据当前被激活的 element 的类型，判定当前动态效果，属于分发器
    if(!_element) return;
    
    var _element_type = _element.getAttribute("jui_type");
    
    switch(_element_type){
    	case "JEditor":{
    	    if ((_gv_is_drop_btn_show)&&(_element == jui_drop_btn.control)) return;
    	    if(_gv_active_editor) {
    	        set_normal_editor(_gv_active_editor);
    	    }
    	    set_active_editor(_element);

    	    break;
    	}
    	case "drop_btn":{
    	    break;
    	}
    	case "drop_box":{
            break;
    	}
    	default:{
    	    if(_gv_active_editor) set_normal_editor(_gv_active_editor);
    	    break;
    	}
    }
}

function set_normal_editor(_element){
    if(!_element) return;
    var _jui_type = _element.getAttribute("jui_type");
    
    var _normal_class = _element.getAttribute("class");
    if(_element.type != "checkbox"){
        if(!_normal_class) _normal_class = "jui_input";
        _element.className = _normal_class;
    } else {
        if(!_normal_class) _normal_class = "jui_select";
        _element.className = _normal_class;
    }
    if(_gv_is_drop_btn_show){// 如果当前 btn is show
    	jui_drop_btn.style.visibility = "hidden";
    	_gv_is_drop_btn_show = false;
    }
    
    if(_gv_is_active_control_short){// 如果active 被缩短
    	_element.style.width = _element.offsetWidth + 18;
    	_gv_is_active_control_short = false;
    }
    
    if(_gv_is_drop_box_show){//如果当前dropdown box show
    	jui_drop_box.style.visibility = "hidden";
    	_gv_is_drop_box_show = false;
    }
}

function set_active_editor(_element){
    var _jui_type = _element.getAttribute("jui_type");
    var _active_class = _element.getAttribute("activeclass");
    
    if(_element.type != "checkbox"){
        // 设定动态样式
        if(!_active_class) _active_class = "jui_input_active";
        _element.className = _active_class;
        
        //处理动态操作
        
        processEditorActive(_element);
        
        _element.select();
    } else {
    	if(!_active_class) _active_class = "jui_select_active";
    	_element.className = _active_class;
    }
    
    
    _gv_active_editor = _element;
}

function processEditorActive(_element){
    // editor 被激活的动作，基本上就是显示 dropdown_button
    
    var _data_type = _element.getAttribute("dataType");
    var _drop_down_id,_drop_down,_drop_down_index;
    
    if(_data_type == "Date"){
    	var _dropdown_index = _gv_dropdown_array["_index_of_jui_date"];
    	var _drop_down = _gv_dropdown_array[_dropdown_index];
    	_element._dropdown = _drop_down;
    	showDropdownBtn(_element);
    	
    } else {
    	_drop_down_id = _element.getAttribute("dropdown");
    	if(!_drop_down_id) return;
    	var _dropdown_index = _gv_dropdown_array["_index_of_"+_drop_down_id];
    	var _drop_down = _gv_dropdown_array[_dropdown_index];
    	_element._dropdown = _drop_down;
    	showDropdownBtn(_element);
    	//下面判断是不是要自动下拉
    	var _if_auto_drop = _element.getAttribute("autodropdown");
    	if(_if_auto_drop=="true"){
    	    jui_drop_btn.click();
    	} else {
    	    // if auto drop false, do nothing;
    	}
    }
}

function showDropdownBtn(_element){
    
    createDropdownBtn(_element);
    jui_drop_btn.value = "6";
    setDropDownBtnPos(_element);
    _gv_is_drop_btn_show = true;
}

function createDropdownBtn(_element){
    if(typeof(jui_drop_btn)=="undefined"){ //如果没有创建 jui_drop_btn 按钮
    	var _btn = document.createElement("<input id='jui_drop_btn' jui_type='drop_btn' type='button' class='jui_btn' value='6' tabindex=-1 hidefocus=true onclick='clickDropDown_btn(this)'>");
        document.body.appendChild(_btn);
    }
    
    jui_drop_btn.style.visibility="visible";
    jui_drop_btn.control = _element;
}

function setDropDownBtnPos(_element){
    var _pos = new Array();
    _pos = getAbsPos(_element);
    
    jui_drop_btn.style.height=_element.offsetHeight;
    jui_drop_btn.style.width=16;
    jui_drop_btn.style.posLeft=_pos[0]+_element.offsetWidth-jui_drop_btn.offsetWidth-1;
    jui_drop_btn.style.posTop=_pos[1];
    
    _element.style.width = parseInt(_element.offsetWidth)-18;
    _gv_is_active_control_short = true;
}

function getAbsPos(_element){
    var _left = _element.offsetLeft;
    var _top = _element.offsetTop;
    var _tmp_element = _element.offsetParent;
    
    while((_tmp_element!=document.body)&&(_tmp_element)){
    	_left += (_tmp_element.offsetLeft - _tmp_element.scrollLeft + _tmp_element.clientLeft);
    	_top += (_tmp_element.offsetTop - _tmp_element.scrollTop + _tmp_element.clientTop);
    	_tmp_element = _tmp_element.offsetParent;
    }
    var _array_pos = new Array();
    _array_pos[0] = _left;
    _array_pos[1] = _top;
    
    return (_array_pos);
}

function clickDropDown_btn(_btn){
    //判断 _btn 的 control 的控件类型，根据 control._dropdown 类型 判断如何处理 dropdown, 分发器
    // 修正，暂不进行分发，直到 dropdown box 填充 content 再分发  2005-5-25
    var _control = _btn.control;
    var _data_type = _control.getAttribute("dataType");
    if(_data_type == "Date"){
    	if(_gv_is_drop_box_show){
    	    hideDropdownBox();
    	} else {
    	    showDateDropdownBox(_control);
    	}
    } else {
    	if(_gv_is_drop_box_show){
    	    hideDropdownBox();
    	} else {
    	    showDropdownBox(_control);
    	}
    }
}

function showDateDropdownBox(_control){
    jui_drop_btn.value = "5";
    createDropdownBox(_control);
    setDateDropdownBox(_control);
    initElementGroup(jui_drop_box);
    _gv_is_drop_box_show = true;
    _gv_current_drop_obj = _control._dropdown; // when dropbox show the gv_drop_obj 赋值
    _gv_current_drop_obj.control = _control;  // drop object .control 属性赋值

}

function setDateDropdownBox(_control){
    setDateDropdownBoxContent(_control);
    setDropdownBoxPos(_control);
}

function setDateDropdownBoxContent(_control){
    
    var _date_content = getDateDropDownContent(_control);
    jui_drop_box.innerHTML = _date_content;
    jui_drop_box._contentObj = date_content_table;
}

function getDateDropDownContent(_control){
    var _result_str;
    
    _result_str = "<table id='date_content_table' width='250' cellspacing='0' cellpadding='0' border='0'><tbody>";
    var _today = new Date();
    var _today_calendar = getCalendarByDate(_today);
    _result_str += _today_calendar;
    
    _result_str += "</tdoby></table>";
    return _result_str;
}

function getCalendarByDate(_tmp_date){
    var _result_str;
    var _year = _tmp_date.getYear();
    var _month = _tmp_date.getMonth()+1;
    var _date = _tmp_date.getDate();
    var _weekday = _tmp_date.getDay();
    
    var _calendar_title_tr = getCalendarTitle(_tmp_date);
    var _calendar_content_tr = getCalendarContent(_tmp_date);
    var _calendar_bot_tr = getCalendarBot(_tmp_date);
    
    _result_str = _calendar_title_tr + _calendar_content_tr + _calendar_bot_tr;
    
    return _result_str;
}

function getCalendarBot(_tmp_date){
    var _result_str;
    _result_str = "";
    
    return _result_str;
}

function getCalendarTitle(_tmp_date){
    var _result_str;
    _result_str = "<tr height='30'><td><table cellpadding='0' cellspacing='0'><tr>";
    _result_str += "<td width='30' align='right'><input class='jui_date_button' type='button' value='3' jui_type='JDateButton'></td>";
    _result_str += "<td width='40'><input class='jui_date_input' type='text' jui_type='JDateInput' value='2005'></td>"
    _result_str += "<td width='30'><input class='jui_date_button' type='button' value='4' jui_type='JDateButton'></td>";
    _result_str += "<td width='30' align='right'><input class='jui_date_button' type='button' value='3' jui_type='JDateButton'></td>";
    _result_str += "<td width='40'><input class='jui_date_input' type='text' jui_type='JDateInput' value='05'></td>"
    _result_str += "<td width='30'><input class='jui_date_button' type='button' value='4' jui_type='JDateButton'></td>";
    _result_str += "</tr></table></td></tr>";
    
    return _result_str;
}

function getCalendarContent(_tmp_date){
    var _result_str;
    _result_str = "<tr><td><table width='100%' cellpadding='0' cellspacing='1'><tbody>";
    _result_str += "<tr class='jui_calendar_title'><td>日</td><td>一</td><td>二</td><td>三</td><td>四</td><td>五</td><td>六</td></tr>"
    
    var _total_date_arr = new Array();
    _total_date_arr = getTotalDateArr(_tmp_date);
    
    
    for(var i=0;i<6;i++){
    	_result_str += "<tr>";
    	
    	for(var j=0;j<7;j++){
    	    _result_str += "<td jui_type='"+_total_date_arr[i*7+j]._jui_type+"'>"+_total_date_arr[i*7+j]._date.getDate()+"</td>";
    	}
    	
    	
    	_result_str += "</tr>";
    }
    
    
    
    _result_str += "</tbody></table></td></tr>";
    return _result_str;
}

function getTotalDateArr(_tmp_date){
    var _result_arr = new Array();
    
    var _pre_month_arr = new Array();
    var _cur_month_arr = new Array();
    var _next_month_arr = new Array();
    var _total_day_sum = 7*6;
    var _present_date = _tmp_date;
    _present_date.setDate(1);// 设置 present 为当前月的第一天
    
    var _tmp_pre_month_day = _present_date;//设置上个月的参数
    var _tmp_cur_month_day = _present_date;// 本月参数
    
    var _present_weekday = _present_date.getDay();
    if(_present_weekday == 0) _present_weekday = 7;
    
    
    // pre_month_arr
    for(var i=_present_weekday-1;i>=0;i--){
        _tmp_pre_month_day = _pre_day(_tmp_pre_month_day);
        _pre_month_arr[i] = new Object();
        _pre_month_arr[i]._date = _tmp_pre_month_day;
        _pre_month_arr[i]._jui_type = "JCalPreCell";
    }
    
    for(var i=0;i<_pre_month_arr.length;i++){
    	_result_arr[_result_arr.length] = _pre_month_arr[i];
    	
    }
    
    // end pre_month_arr
    
    
    // cur_month_arr
    while(_tmp_cur_month_day.getMonth() == _present_date.getMonth()){
    	var _length = _cur_month_arr.length;
    	_cur_month_arr[_length] = new Object();
    	_cur_month_arr[_length]._date = _tmp_cur_month_day;
    	_cur_month_arr[_length]._jui_type = "JCalCurCell";
    	_result_arr[_result_arr.length] = _cur_month_arr[_length];
    	
    	_tmp_cur_month_day = _next_day(_tmp_cur_month_day);
    }
    
    // end cur_month_arr
    
    // next_month_arr
    var _next_month_day_sum = _total_day_sum - _result_arr.length;
    var _tmp_next_month_day = _tmp_cur_month_day;
    
    for(var i=0;i<_next_month_day_sum;i++){
    	_next_month_arr[i] = new Object();
    	_next_month_arr[i]._date = _tmp_next_month_day;
    	_next_month_arr[i]._jui_type = "JCalPreCell";
    	
    	_result_arr[_result_arr.length] = _next_month_arr[i];
    	
    	_tmp_next_month_day = _next_day(_tmp_next_month_day);
    }
    // end next_month_arr
    
    
    
    
    function _pre_day(__tmp_date){
    	var _date = __tmp_date.getDate();
    	var _pre_date = _date - 1;
    	var _return_date = new Date();
    	_return_date.setDate(_pre_date);
    	return _return_date;
    }
    
    function _next_day(__tmp_date){
    	var _date = __tmp_date.getDate();
    	var _next_date = _date + 1;
    	var _return_date = new Date();
    	_return_date.setDate(_next_date);
    	return _return_date;
    }
    
    
    return _result_arr;
}

function hideDropdownBox(){
    jui_drop_btn.value = "6";
    jui_drop_box.style.visibility = "hidden";
    _gv_is_drop_box_show = false;
    _gv_current_drop_obj = null;  // dropbox hidden 设置当前 drop_obj 为 null
    _gv_drop_content_row = null;
}

function showDropdownBox(_control){
    jui_drop_btn.value = "5";
    createDropdownBox(_control);
    setDropdownBox(_control);
    _gv_is_drop_box_show = true;
    _gv_current_drop_obj = _control._dropdown; // when dropbox show the gv_drop_obj 赋值
    _gv_current_drop_obj.control = _control;  // drop object .control 属性赋值
}

function createDropdownBox(_control){
    if(typeof(jui_drop_box)=="undefined"){
        var _box = document.createElement("<div id='jui_drop_box' jui_type='drop_box' class='jui_dropdown_box'></div>");
        document.body.appendChild(_box);
    }
    
    if(jui_drop_box.filters.blendTrans.status!=2){
        jui_drop_box.filters.blendTrans.apply();
        jui_drop_box.style.visibility = "visible";
    	jui_drop_box.filters.blendTrans.play();
    }

    jui_drop_box.control = _control;

}

function setDropdownBox(_control){
    setDropdownBoxContent(_control);
    setDropdownBoxPos(_control);
}

function setDropdownBoxContent(_control){
    var _dropdown = _control._dropdown;
    var _dropdown_type = _dropdown.type;
    // 判断 dropdown 类型，根据 control._dropdown 类型 判断如何处理 dropdown.content, 分发器
    var _content;
    switch (_dropdown_type) {
    	case "List":{//如果是 静态 dropdown 类型
    	    _content = getListDropDownContent(_control);
    	    _gv_drop_now_select_row_index = getListSelectIndex(_control);
    	    
    	    jui_drop_box.innerHTML = _content;
    	    jui_drop_box._contentObj = content_table;
    	    if(typeof(content_table.rows[_gv_drop_now_select_row_index])=="undefined"){
    	    } else {
    	        content_table.rows[_gv_drop_now_select_row_index].className = "selected_dropdown_row";

    	    }
    	        	    
            break;
        }
        default:{
	    break;
	}
    }
}

function getListSelectIndex(_control){
    
    var _dropdown = _control._dropdown;
    var _control_value = _control.value;
    var _selectIndex;
    
    if(!_control_value){
    	_selectIndex = 0;
    } else {
    	_selectIndex = _dropdown.resultValueArray["_index_of_"+_control_value];
    }
    return _selectIndex;
}

function getListDropDownContent(_control){
    var _labelStr = _control._dropdown.itemLabelArr;
    var _dropdown = _control._dropdown;
    var _label_arr = _labelStr.split(";");
    _dropdown.LabelArray = _label_arr;
    
    var _valueStr = _control._dropdown.itemValueArr;
    var _value_arr = _valueStr.split(";");
    _dropdown.ValueArray = _value_arr;
    
    var _map_type = _control.getAttribute("dropMaptype");
    if(!_map_type) _map_type = "Label";//如果没有定义 数据映射方式，定义映射方式为 Label
    
    var _map_label_no = _control.getAttribute("MapLabelNo");
    if(!_map_label_no) _map_label_no = 1;//默认 _map_label_no 为显示label表格的第一个 td

    
    var _strResult = "<table id='content_table' cellpadding='3' cellspacing='1'><tbody>";
    for(var i=0;i<_label_arr.length;i++){
    	_strResult += "<tr class='jui_dropdown_content' onmousemove=_dropdown_mousemove(this) onmousedown=_dropdown_mousedown(this)>";
  
    	var _label_tr_arr = _label_arr[i].split(",");//_label_tr_arr 表示 dropdown 中的一个 tr 中的所有数据
    	for(var j=0;j<_label_tr_arr.length;j++){
    	    _strResult += "<td nowrap>";
    	    _strResult += _label_tr_arr[j];
    	    _strResult += "</td>";

            if(_map_type == "Label"){
    	         _dropdown.resultValueArray[i] = _label_tr_arr[_map_label_no-1];
    	         _dropdown.resultValueArray["_index_of_"+_label_tr_arr[_map_label_no-1]] = i;
            } else {//映射方式为 value
    	         _dropdown.resultValueArray[i] = _value_arr[i];
    	         _dropdown.resultValueArray["_index_of_"+_value_arr[i]] = i;
            }
    	}
    	_strResult += "</tr>";
    }
    _strResult += "</tbody></table>";
    return _strResult;
}

function _dropdown_mousemove(_tr){
    if(_tr.rowIndex == _gv_drop_now_select_row_index) return;//_gv_drop_now_select_row_index 表示当前值对应的index
    if(_gv_drop_content_row){
        setDropdownRowNormal(_gv_drop_content_row);
    }
    setDropdownRowActive(_tr);
    //alert();
    _gv_drop_content_row = _tr;   
}

function setDropdownRowNormal(_tr){
    _tr.className = "jui_dropdown_content";
}
function setDropdownRowActive(_tr){
    _tr.className = "current_dropdown_row";
}

function _dropdown_mousedown(_tr){
    var _dropdown = _gv_current_drop_obj;
    var _tr_index = _tr.rowIndex;
    var _control = _dropdown.control;
    var _map_type = _control.getAttribute("dropMaptype");
    if(!_map_type) _map_type = "Label";//_map_type 默认值为 "Label"
    var _result,_label_no,tmp_label_cell_arr;
    
    if(_map_type == "Label"){
    	_label_no = _control.getAttribute("MapLabelNo");

    	if(_label_no){
    	    _label_no = parseInt(_label_no);
    	    tmp_label_cell_arr = _dropdown.LabelArray[_tr_index].split(",");
    	    _result = tmp_label_cell_arr[_label_no-1];
    	} else {
    	    _result = _dropdown.LabelArray[_tr_index];
    	}
    	
    } else if(_map_type == "Value"){
    	_result = _dropdown.ValueArray[_tr_index];
    }
    
    _control.value = _result;
}

function setDropdownBoxPos(_control){
    var _pos = new Array();
    _pos = getAbsPos(_control);
    var _box_width,_box_height;
    
    var _content_obj= jui_drop_box._contentObj;
    var _box_cur_width = _content_obj.offsetWidth;
    var _box_cur_height = _content_obj.offsetHeight;
    
    var _standard_width = _control.offsetWidth + 18;
    _box_width = (_box_cur_width > _standard_width)?_box_cur_width:_standard_width;
    _box_height = _box_cur_height;
    
    _content_obj.style.width = _box_width;
    _content_obj.style.height = _box_height;
    _control._posX = _pos[0];
    _control._posY = _pos[1];
    var _box_pos_arr = getPerfectPos(_control,_content_obj);
    
    jui_drop_box.style.posLeft = _box_pos_arr[0];
    jui_drop_box.style.posTop = _box_pos_arr[1];
    
}

function getPerfectPos(_control,_content){
    var _result_left = 0;
    var _pannel_height = document.body.clientHeight;
    var _pannel_width = document.body.clientWidth;
    var _result_arr = new Array();
        
    var _pannel_to_top = _control._posY - document.body.scrollTop;
    var _pannel_to_bot = document.body.clientHeight + document.body.scrollTop - _control._posY - _control.offsetHeight;
    var _pannel_to_left = _control._posX - document.body.scrollLeft;
    var _pannel_to_right = document.body.clientWidth + document.body.scrollLeft - _control._posX - _control.offsetWidth;
    
    if(_pannel_to_bot > _content.offsetHeight+5){
        if((_pannel_to_left > 0)&&((_pannel_to_right + _control.offsetWidth) > (_content.offsetWidth+2)) ){//normal down
            _result_left = _control._posX;
            _result_top = _control._posY + _control.offsetHeight+2;
        } else if(_pannel_to_left < 0){
            _result_left = document.body.scrollLeft+2;
            _result_top = _control._posY + _control.offsetHeight+2;
        } else {
            _result_left = document.body.clientWidth + document.body.scrollLeft - _content.offsetWidth-4;
	    _result_top = _control._posY + _control.offsetHeight+2;
	}
    } else if(_pannel_to_top > _content.offsetHeight+5){
        if((_pannel_to_left > 0)&&((_pannel_to_right + _control.offsetWidth) > _content.offsetWidth+2)){//normal up
            _result_left = _control._posX;
            _result_top = _control._posY - _content.offsetHeight-4;
        } else if(_pannel_to_left < 0){
            _result_left = document.body.scrollLeft+2;
            _result_top = _control._posY - _content.offsetHeight-4;
        } else {
            _result_left = document.body.clientWidth + document.body.scrollLeft - _content.offsetWidth-4;
            _result_top = _control._posY - _content.offsetHeight-4;
	}
    } else {
	if(_pannel_to_right > _content.offsetWidth){
            _result_left = _control._posX + _control.offsetWidth;
            if((_pannel_to_bot + _control.offsetHeight) > _content.offsetHeight){
                _result_top = _control._posY;
            } else {
                _result_top = document.body.clientHeight + document.body.scrollTop - _content.offsetHeight;
            }
        } else {
	    _result_left = _control._posX - _content.offsetWidth-4;
            if((_pannel_to_bot + _control.offsetHeight) > _content.offsetHeight){
                _result_top = _control._posY-4;
            } else {
                _result_top = document.body.clientHeight + document.body.scrollTop - _content.offsetHeight-4;
            }
	}
    }

    _result_arr[0] = _result_left;
    _result_arr[1] = _result_top;
    return _result_arr;
}


function createDateDropDown(){
    
}

function createDropdown(_id){
// 创建 dropdown 的时候，只是添加 id 和 dropdown object
    var _dropdown = new Object;
    _dropdown.id = _id;
    _dropdown.type;
    _dropdown.LabelArray = new Array();// dropdown 的 label 数组
    _dropdown.valueArray = new Array();// dropdown 的 value 数组
    _dropdown.control;
    _dropdown.resultValueArray = new Array();//dropdown 中真正用来和control 交互的 array
    
    
    
    var _index = _gv_dropdown_array.length;
    
    _gv_dropdown_array[_index] = _dropdown;
    _gv_dropdown_array["_index_of_"+_id] = _index;
    
    return _dropdown;
}




function booleanObject(_true,_false){
    var _true_array,_false_array,_result;
    this._isTrue = _is_true;
    
    if(_true){
    	_true_array = _true.split(",");
    }
    if(_false){
    	_false_array = _false.split(",");
    }
    
    function _is_true(_value){
    	if(!_true){
            for(var i=0;i<_false_array.length;i++){
                if(_false_array[i] == _value){
                    _result = false;
                    return _result;
                }
            }
            _result = true;
            return _result;
        } else {
            for(var j=0;j<_true_array.length;j++){
                if(_true_array[j] == _value){
                    _value = true;
                    return _value;
                }
            }
            _value = false;
            return _value;
	}
    }
}



















function createDataForm(_id,_datastr){
    var _data_form = new JDataForm(_id);
    var _dataform_array = _datastr.split(";");
    
    for(var i=0;i<_dataform_array.length;i++){
    	var _resultset_array = _dataform_array[i].split(",");
    	var _result_set = new JResultSet();
    	
    	for(var j=0;j<_resultset_array.length;j++){
    	    var _jdata = new JData(_resultset_array[j]);
    	    _jdata.cId = j+1;
    	    _jdata.rId = i;
    	    _jdata.dataForm = _data_form;
    	    _jdata.resultSet = _result_set;
    	    _result_set.data.insertNode(_jdata);
    	    
    	}
    	_result_set.rId = i+1;
    	_result_set.dataForm = _data_form;
    	_data_form.data.insertNode(_result_set);
    }
    
    _gv_dataform_array[_gv_dataform_array.length] = _data_form;
    
    
    return _data_form;
    
}

function JDataForm(_id){
    this.id = _id;
    this.data = new JArray();
    this.fields = new Array();
    
    this.addField = _add_field;
    this.getCellValueById = _get_cell_value_by_id;
    this.getResultSetById = _get_resultset_by_id;
    
    function _get_resultset_by_id(_id){
    	return this.data.getNodeById(_id);
    }
    
    function _get_cell_value_by_id(_rid,_cid){
    	return get_cell_value_by_id(this,_rid,_cid);
    }
    
    function get_cell_value_by_id(_dataform,_rid,_cid){
    	var _result;
    	_result = _dataform.data.getNodeById(_rid).data.getNodeById(_cid).data;
    	return _result;
    }
    
    function _add_field(_field_name,_data_type){
        return add_field(this,_field_name,_data_type);
    }
    
    function add_field(_jdata_form,_field_name,_data_type){
        var _field = new JField(_field_name,_data_type);
        _field.dataForm = _jdata_form;
        var _fields = _jdata_form.fields;
        var _index = _fields.length;
        _fields[_index] = _field;
        _fields["_index_of_"+_field_name] = _index;
        var _fields_count = _fields.length;
        
        var _result_set_count = _jdata_form.data.arrayLength;

        for(var i=0;i<_result_set_count;i++){
            var _node = _jdata_form.getResultSetById(i+1).data.getNodeById(_fields_count);
            _node.jField = _field;
        }
        return _field;
    }
}


function JField(_field_name,_data_type){
    this.fieldName = _field_name;
    this.dataType = _data_type;
    this.dataForm;
}


function JData(_value){
    this.data = _value;
    this.cId = 0;
    this.dataForm;
    this.jField;
    this.ResultSet;
    this.rId;
}
    
function JResultSet(){
    this.data = new JArray();
    this.rId = 0;
    this.dataForm;
    
    this.getValue = _get_value;
    this.getValueById = _get_value_by_id;
    this.getDataObjById = _get_data_obj_by_id;
    
    function _get_data_obj_by_id(_id){
        return this.data.getNodeById(_id);
    }
    
    function _get_value_by_id(_id){
    	var _dataform = this.dataForm;
    	var _field = _dataform.fields[_id-1];
    	var _result = this.getValue(_field.fieldName);
    	return _result;
    }
    
    function _get_value(_field){
    	return get_value(this,_field);
    }
    
    function get_value(_resultset,_field){
    	var _result;
    	var _data_array = _resultset.data;
    	
        for(var i=0;i<_data_array.arrayLength;i++){
            var _dataObject = _data_array.getNodeById(i+1);
    	    if(_dataObject.jField.fieldName == _field){
    	        _result = _dataObject.data;
    	        return _result;
    	    }
    	}
    	return _result;
    }
} 


function JArray(){
    this.firstNode = null;
    this.lastNode = null;
    this.arrayLength = 0;
    
    this.insertNode = _insert_node;
    this.getNodeById = _get_node_by_id;
    
    
    function _insert_node(_new_node){
        return insert_node(this,_new_node);
    }
    
    function insert_node(_array,_new_node){
        var _node1,_node2;
        
        _node1 = _array.lastNode;
        _node2 = null;
        
        _new_node.preNode = _node1;
        _new_node.nextNode = _node2;
        
        if(_node1){
            _node1.nextNode = _new_node;
            _new_node.id = _node1.id + 1;
        } else {
            _array.firstNode = _new_node;
            _new_node.id = 1;
        }
        
        if(_node2){
            _node2.preNode = _new_node;
        } else {
            _array.lastNode = _new_node;
        }
        
        _array.arrayLength ++;
    }
    
    function _get_node_by_id(_id){
    	return get_node_by_id(this,_id);
    }
    
    function get_node_by_id(_jarry,_id){
        var _node = _jarry.firstNode;
        
        while(_node){
            if(_node.id == _id) break;
            _node = _node.nextNode;
        }
        return _node;
    }
}


















function _finishInit(){
    document.body.style.visibility = "visible";
}