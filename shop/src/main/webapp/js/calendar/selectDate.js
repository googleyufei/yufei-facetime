/**
 * 注意：前面的黑点不能删,请保存为UTF-8格式 只需要date.jsp,datetime.jsp引用
 */
/*
 * //在date.jsp,datetime.jsp里面定义以下跟国际化相关的变量 var months = new Array("一月", "二月",
 * "三月", "四月", "五月", "六月","七月", "八月", "九月", "十月", "十一月","十二月"); var weekDays =
 * new Array("星期日", "星期一", "星期二","星期三", "星期四", "星期五", "星期六"); var yearLabel="年"
 * var hourLabel="时" var minuteLabel="分" var okLabel="确定" var clearLabel="清空"
 * var cancelLabel="取消"
 */
/*
 * var weekDays = new Array("Sun", "Mon", "Tue","Wed", "Thu", "Fri", "Sat"); var
 * months = new Array("01", "02", "03","04", "05", "06", "07", "08", "09","10",
 * "11", "12"); var yearLabel="Year" var hourLabel="Hour" var
 * minuteLabel="Minute"
 * 
 * var okLabel="OK" var clearLabel="CLEAR" var cancelLabel="CANCEL"
 */

// 定义全程变量
var isIE = (document.all ? true : false);
var daysInMonth = new Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
var displayMonth;
var displayYear;
var displayDay;
var displayHours;
var displayMinutes;
var displaySeconds = 0;
var displayDivName;
var oldValue = "";
var today = new getToday();

function getXBrowserRef(eltname) {
	return (isIE ? document.all[eltname].style : document.layers[eltname]);
}

function hideElement(eltname) {
	getXBrowserRef(eltname).visibility = 'hidden';
	window.close();
}

function toggleVisible(eltname) {
	elt = getXBrowserRef(eltname);
	if (elt.visibility == 'visible' || elt.visibility == 'show') {
		elt.visibility = 'hidden';
	} else {
		// fixPosition(eltname);
		elt.visibility = 'visible';
	}
}

function getDays(month, year) {
	// check if it is special year
	if (1 == month)
		return ((0 == year % 4) && (0 != (year % 100))) || (0 == year % 400) ? 29
				: 28;
	else
		return daysInMonth[month];
}

function newCalendar(eltName, type) {

	today = new getToday();
	var newCal;
	var parseHours;
	var parseMinutes;
	var parseSeconds;
	var parseYear = parseIntNum(displayYear + '');
	if (type == 1) {
		parseHours = parseIntNum(displayHours + '');
		parseMinutes = parseIntNum(displayMinutes + '');
		parseSeconds = parseIntNum(displaySeconds + '');
	}
	if (type == 1) {
		newCal = new Date(parseYear, displayMonth, 1, parseHours, parseMinutes,
				displaySeconds);
	} else {
		newCal = new Date(parseYear, displayMonth, 1);
	}

	var day = -1;
	var startDayOfWeek = newCal.getDay();
	if ((today.year == newCal.getFullYear())
			&& (today.month == newCal.getMonth())) {
		day = today.day;
	}
	var intDaysInMonth = getDays(newCal.getMonth(), newCal.getFullYear());
	var daysGrid = makeDaysGrid(startDayOfWeek, day, intDaysInMonth, newCal,
			eltName, type)
	if (isIE) {
		var elt = document.all[eltName];
		elt.innerHTML = daysGrid;
	} else {
		var elt = document.layers[eltName].document;
		elt.open();
		elt.write(daysGrid);
		elt.close();
	}
}

function incMonth(delta, eltName, type) {
	displayMonth += delta;
	if (displayMonth >= 12) {
		displayMonth = 0;
		incYear(1, eltName);
	} else if (displayMonth <= -1) {
		displayMonth = 11;
		incYear(-1, eltName, type);
	}
	newCalendar(eltName, type);
	setDay(eltName, type);
}

function incYear(delta, eltName, type) {
	displayYear = parseIntNum(displayYear + '') + delta;
	newCalendar(eltName, type);
	setDay(eltName, type);
}

function incHours(delta, eltName, type) {
	displayHours = parseIntNum(displayHours + '') + delta;
	if (displayHours >= 24) {
		displayHours = 0;
	} else if (displayHours <= -1) {
		displayHours = 23;
	}
	document.all.hour.value = displayHours;
	setDay(eltName, type);
}

function incMinutes(delta, eltName, type) {
	displayMinutes = parseIntNum(displayMinutes + '') + delta;
	if (displayMinutes >= 60) {
		displayMinutes = 0;
	} else if (displayMinutes <= -1) {
		displayMinutes = 59;
	}
	document.all.minute.value = displayMinutes;
	setDay(eltName, type);
}

function incSeconds(delta, eltName, type) {
	displaySeconds = parseIntNum(displaySeconds + '') + delta;
	if (displaySeconds >= 60) {
		displaySeconds = 0;
	} else if (displaySeconds <= -1) {
		displaySeconds = 59;
	}
	document.all.second.value = displaySeconds;
	setDay(eltName, type);
}

function makeDaysGrid(startDay, day, intDaysInMonth, newCal, eltName, type) {
	var daysGrid;
	var month = newCal.getMonth();
	var year = newCal.getFullYear();
	if (type == 1) {
		var hours = newCal.getHours();
		var minutes = newCal.getMinutes();
		var seconds = newCal.getSeconds();
	}

	var isThisYear = (year == new Date().getFullYear());
	var isThisMonth = (day > -1)
	// daysGrid = '<font face="courier new, courier" size=2>';
	daysGrid = '<table border="1" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111">'
	daysGrid += '<tr><td><table border=0 width=300  style="font-size:9pt" bgcolor=white>';
	daysGrid += '<tr><td bgcolor=#ccddff  colspan=7 height=20>';
	daysGrid += '&nbsp;&nbsp;&nbsp;&nbsp;'
	daysGrid += '<a style="cursor:hand" onClick="incYear(-10,\'' + eltName
			+ '\', \'' + type + '\')"><font size=1>&#9668;&#9668;</font> </a>';
	daysGrid += '<a style="cursor:hand" onClick="incYear(-1,\'' + eltName
			+ '\', \'' + type
			+ '\')"><font size=1>&nbsp;&nbsp;&#9668;</font> </a>';
	daysGrid += '<input type=text id=year size=4  maxlength=4 style="BACKGROUND-color:transparent;border:0;color:red;font:9pt" value='
			+ year;
	daysGrid += ' onkeypress=\"return dtp_KeyFilter(\'number\');\" onfocus=\"return dtp_focus(\'year\');\" onblur=\"setYear(\''
			+ eltName
			+ '\',\''
			+ type
			+ '\');\"><font color=red>'
			+ yearLabel
			+ '</font>';
	daysGrid += '<a style="cursor:hand" onClick="incYear(1,\'' + eltName
			+ '\', \'' + type + '\')"><font size=1>&#9658;</font> </a>';
	daysGrid += '<a style="cursor:hand" onClick="incYear(10,\'' + eltName
			+ '\', \'' + type
			+ '\')"><font size=1>&nbsp;&nbsp;&#9658;&#9658;</font></a>';
	daysGrid += '&nbsp;&nbsp;&nbsp;&nbsp;'
	daysGrid += '<a style="cursor:hand" onClick="incMonth(-1,\'' + eltName
			+ '\', \'' + type + '\')"><font size=1>&#9668;</font> </a>';
	daysGrid += '<font color=red><select id=month onchange=\"setMonth(\''
			+ eltName + '\', \'' + type
			+ '\')" style="font-size:9pt;color:red" >';
	for (i = 0; i < months.length; i++) {
		daysGrid += '<option value=' + i;
		if (i == month) {
			daysGrid += ' selected '
		}
		daysGrid += '>' + months[i] + '</option>';
	}
	daysGrid += '</select>'
	daysGrid += '<a style="cursor:hand" onClick="incMonth(1,\'' + eltName
			+ '\', \'' + type + '\')"><font size=1>&#9658;</font></a>';
	daysGrid += '</td></tr><tr>';
	for (i = 0; i < weekDays.length; i++) {
		daysGrid += '<td>' + weekDays[i] + '</td>'
	}
	daysGrid += '</tr></table></td></tr><tr><td><table border=0 width=300 style="font-size:9pt;LINE-HEIGHT: 12px" bgcolor=white>';
	var dayOfMonthOfFirstSunday = (7 - startDay + 1);
	for ( var intWeek = 0; intWeek < 6; intWeek++) {
		var dayOfMonth;
		daysGrid += '<tr>'
		for ( var intDay = 0; intDay < 7; intDay++) {

			dayOfMonth = (intWeek * 7) + intDay + dayOfMonthOfFirstSunday - 7;
			if (dayOfMonth <= 0) {
				daysGrid += "<td>&nbsp;</td> ";
			} else if (dayOfMonth <= intDaysInMonth) {
				var color = "black";
				if (day == dayOfMonth)
					color = "red";
				daysGrid += '<td align=center ';
				if (dayOfMonth == displayDay)
					daysGrid += 'bgcolor = yellow';
				daysGrid += '><a style="cursor:hand" onMouseDown="selectDay('
						+ dayOfMonth + ',\'' + eltName + '\', \'' + type
						+ '\')" ';
				daysGrid += 'onMouseUp="onSelectDay()" ';
				daysGrid += 'style="color:' + color + '">';
				var dayString = dayOfMonth + "</a>";
				if (dayString.length == 5)
					dayString = '0' + dayString;
				daysGrid += dayString + '</td>';
			} else {
				daysGrid += "<td>&nbsp;</td> "
			}
		}
		daysGrid += '</tr>';
		if (dayOfMonth < intDaysInMonth)
			daysGrid += "</tr>";
	}
	daysGrid += '</table></td></tr><tr><td><table border=0 width=300 style="font-size:9pt" bgcolor=white>';
	if (type == 1) {
		daysGrid += '<tr><td colspan=7 align=center><a style="cursor:hand" onClick="incHours(-1,\''
				+ eltName
				+ '\', \''
				+ type
				+ '\')"><font size=1>&#9668;</font></a>';
		daysGrid += '<input type=text size=1 id=hour maxlength=2 style="border:0;color:red;font:9pt;width:18" value='
				+ hours;
		daysGrid += ' onkeypress=\"return dtp_KeyFilter(\'number\');\" onfocus=\"return dtp_focus(\'hour\');\" onblur=\"setHours(\''
				+ eltName + '\',\'' + type + '\');\"></font>';
		daysGrid += '<a style="cursor:hand" onClick="incHours(1,\'' + eltName
				+ '\', \'' + type + '\')"><font size=1>&#9658;</font></a>'
				+ hourLabel + '&nbsp;&nbsp;&nbsp;';
		daysGrid += '<a style="cursor:hand" onClick="incMinutes(-10,\''
				+ eltName
				+ '\', \''
				+ type
				+ '\')"><font size=1>&#9668;&#9668;&nbsp;&nbsp;&nbsp;</font></a>';
		daysGrid += '<a style="cursor:hand" onClick="incMinutes(-1,\''
				+ eltName + '\', \'' + type
				+ '\')"><font size=1>&#9668;</font></a>';
		daysGrid += '<input type=text size=1  id=minute maxlength=2 style="border:0;color:red;font:9pt;width:18" value='
				+ minutes;
		daysGrid += ' onkeypress=\"return dtp_KeyFilter(\'number\');\"  onfocus=\"return dtp_focus(\'minute\');\" onblur=\"setMinutes(\''
				+ eltName + '\',\'' + type + '\');\"></font>';
		daysGrid += '<a style="cursor:hand"  onClick="incMinutes(1,\''
				+ eltName + '\', \'' + type
				+ '\')"><font size=1>&#9658;</font></a>'
		daysGrid += '<a style="cursor:hand" onClick="incMinutes(10,\''
				+ eltName
				+ '\', \''
				+ type
				+ '\')"><font size=1>&nbsp;&nbsp;&nbsp;&#9658;&#9658;</font></a>'
				+ minuteLabel + '';
		daysGrid += '&nbsp;&nbsp;&nbsp;';

		daysGrid += '<a style="cursor:hand" onClick="incSeconds(-10,\''
				+ eltName
				+ '\', \''
				+ type
				+ '\')"><font size=1>&#9668;&#9668;&nbsp;&nbsp;&nbsp;</font></a>';
		daysGrid += '<a style="cursor:hand" onClick="incSeconds(-1,\''
				+ eltName + '\', \'' + type
				+ '\')"><font size=1>&#9668;</font></a>';
		daysGrid += '<input type=text size=1  id=second maxlength=2 style="border:0;color:red;font:9pt;width:18" value='
				+ seconds;
		daysGrid += ' onkeypress=\"return dtp_KeyFilter(\'number\');\"  onfocus=\"return dtp_focus(\'second\');\" onblur=\"setSeconds(\''
				+ eltName + '\',\'' + type + '\');\"></font>';
		daysGrid += '<a style="cursor:hand"  onClick="incSeconds(1,\''
				+ eltName + '\', \'' + type
				+ '\')"><font size=1>&#9658;</font></a>'
		daysGrid += '<a style="cursor:hand" onClick="incSeconds(10,\''
				+ eltName
				+ '\', \''
				+ type
				+ '\')"><font size=1>&nbsp;&nbsp;&nbsp;&#9658;&#9658;</font></a>'
				+ secondLabel + '</td>';

	}

	daysGrid += '<tr><td  bgcolor=#ccddff colspan=7><div align=\"center\">'
	daysGrid += '<a id="okbutton" href="#" onClick="hideElement(\'' + eltName
			+ '\')">' + okLabel;
	daysGrid += '</a>&nbsp;&nbsp;<a href="#" onClick="clearAll(\'' + eltName
			+ '\')">' + clearLabel;
	daysGrid += '</a>&nbsp;&nbsp;<a href="#" onClick="cancle(\'' + eltName
			+ '\')">' + cancelLabel;
	daysGrid += '</a></div></td></tr>';

	return daysGrid + "</table></td></tr></table>";
}

function setHours(eltName, type) {
	displayHours = parseIntNum(document.all.hour.value);
	if (displayHours >= 24) {
		displayHours = 0;
	} else if (displayHours <= -1) {
		displayHours = 0;
	}
	document.all.hour.value = displayHours;
	setDay(eltName, type);

}

function setMinutes(eltName, type) {
	displayMinutes = parseIntNum(document.all.minute.value);
	if (displayMinutes >= 60) {
		displayMinutes = 59;
	} else if (displayMinutes <= -1) {
		displayMinutes = 0;
	}
	document.all.minute.value = displayMinutes;
	setDay(eltName, type);
}

function setSeconds(eltName, type) {
	displaySeconds = parseIntNum(document.all.second.value);
	if (displaySeconds >= 60) {
		displaySeconds = 59;
	} else if (displaySeconds <= -1) {
		displaySeconds = 0;
	}
	document.all.second.value = displaySeconds;
	setDay(eltName, type);
}

function setYear(eltName, type) {
	document.all.month.focus();
	displayYear = parseIntNum(document.all.year.value);
	if (displayYear < 1900)
		displayYear = 1900;
	if (displayYear > 3000)
		displayYear = 3000;
	newCalendar(eltName, type);
	setDay(eltName, type);
}

function setMonth(eltName, type) {
	displayMonth = parseIntNum(document.all.month.value);
	newCalendar(eltName, type);
	setDay(eltName, type);
}

function selectDay(day, eltName, type) {
	displayDay = day;
	newCalendar(eltName, type);
	setDay(eltName, type);
}

function onSelectDay() {
	okbutton.onclick();
}

function setDay(eltName, type) {
	var strMonthEx = displayMonth + 1;
	var strDayEx = displayDay;
	var strHoursEx;
	var strMinutesEx;
	var strSecondsEx;
	if (type == 1) {
		strHoursEx = displayHours;
		strMinutesEx = displayMinutes;
		strSecondsEx = displaySeconds;
	}

	if (strMonthEx < 10) {
		strMonthEx = "0" + strMonthEx;
	}
	if (strDayEx < 10) {
		strDayEx = "0" + strDayEx;
	}
	if (type == 1) {
		if (strHoursEx < 10) {
			strHoursEx = "0" + strHoursEx;
		}
		if (strMinutesEx < 10) {
			strMinutesEx = "0" + strMinutesEx;
		}
		if (strSecondsEx < 10) {
			strSecondsEx = "0" + strSecondsEx;
		}
	}

	window.dialogArguments.value = displayYear + "-" + strMonthEx + "-"
			+ strDayEx;
	if (type == 1) {
		window.dialogArguments.value += " " + strHoursEx + ":" + strMinutesEx
				+ ":" + strSecondsEx;
	}
	// hideElement(eltName);

}

function clearAll(eltName) {
	window.dialogArguments.value = "";
	hideElement(eltName);
	window.close();
}

function cancle(eltName) {
	window.dialogArguments.value = oldValue;
	hideElement(eltName);
	window.close();
}
function toggleDatePicker(eltName, type) {
	init(eltName, type);
	newCalendar(eltName, type);
	toggleVisible(eltName);
}

// 根据目标文本框的值初始化参数的值，画出datetime下拉框。保存文本框的值
function init(eltName, type) {

	if (displayDivName && displayDivName != eltName)
		hideElement(displayDivName);
	displayDivName = eltName;
	oldValue = window.dialogArguments.value;
	if (oldValue == "") {
		displayYear = new Date().getFullYear();
		displayMonth = new Date().getMonth();
		displayDay = new Date().getDate();
		;
		if (type == 1) {
			displayHours = new Date().getHours();
			displayMinutes = new Date().getMinutes();
			displaySeconds = new Date().getSeconds();
		}
		setDay(eltName, type);
	} else {
		displayYear = parseIntNum(oldValue.substring(0, 4));
		if (parseIntNum(oldValue.substring(5, 7)) == 0)
			displayMonth = parseIntNum(oldValue.substring(6, 7)) - 1;
		else
			displayMonth = parseIntNum(oldValue.substring(5, 7)) - 1;
		if (parseIntNum(oldValue.substring(8, 10)) == 0)
			displayDay = parseIntNum(oldValue.substring(9, 10));
		else
			displayDay = parseIntNum(oldValue.substring(8, 10));
		if (type == 1) {
			if (parseIntNum(oldValue.substring(11, 13)) == 0)
				displayHours = parseIntNum(oldValue.substring(12, 13));
			else
				displayHours = parseIntNum(oldValue.substring(11, 13));

			if (parseIntNum(oldValue.substring(14, 16)) == 0)
				displayMinutes = parseIntNum(oldValue.substring(15, 16));
			else
				displayMinutes = parseIntNum(oldValue.substring(14, 16));

			if (parseIntNum(oldValue.substring(17, 19)) == 0)
				displaySeconds = parseIntNum(oldValue.substring(18, 19));
			else
				displaySeconds = parseIntNum(oldValue.substring(17, 19));
			if (isNaN(displaySeconds)) {
				displaySeconds = 0;
			}
		}
	}
}

function getToday() {
	this.now = new Date();
	this.year = this.now.getFullYear();
	this.month = this.now.getMonth();
	this.day = this.now.getDate();
	this.hours = this.now.getHours();
	this.getMinutes = this.now.getMinutes();
	this.getSeconds = this.now.getSeconds();
}

function dtp_KeyFilter(dk_type) {
	var berr = false;
	switch (dk_type) {
	case 'date':
		if (!(event.keyCode == 45 || event.keyCode == 47 || event.keyCode == 32
				|| event.keyCode == 58 || (event.keyCode >= 48 && event.keyCode <= 57)))
			berr = true;
		break;
	case 'number':
		if (!(event.keyCode >= 48 && event.keyCode <= 57))
			berr = true;
		break;
	case 'cy':
		if (!(event.keyCode == 46 || (event.keyCode >= 48 && event.keyCode <= 57)))
			berr = true;
		break;
	case 'long':
		if (!(event.keyCode == 45 || (event.keyCode >= 48 && event.keyCode <= 57)))
			berr = true;
		break;
	case 'double':
		if (!(event.keyCode == 45 || event.keyCode == 46 || (event.keyCode >= 48 && event.keyCode <= 57)))
			berr = true;
		break;
	default:
		if (event.keyCode == 35 || event.keyCode == 37 || event.keyCode == 38)
			berr = true;
	}
	return !berr;
}

// 时间字符串转换为整数
function parseIntNum(str) {
	if (str.charAt(0) == '0' && str.length > 1) {
		str = str.substring(1, str.length);
	}
	return parseInt(str);
}

function dtp_focus(df_type) {
	var src = event.srcElement;
	if (src && src.tagName == "INPUT") {
		switch (df_type) {
		case 'year':
			break;
		case 'month':
			break;
		case 'day':
			break;
		case 'hour':
			break;
		case 'minute':
			break;
		case 'second':
			break;
		default:
			;
		}
		src.select();
	}
	return true;
}