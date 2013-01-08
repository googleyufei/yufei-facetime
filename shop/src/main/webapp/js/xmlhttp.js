/*
var i = 0,j=0;
window.onload = function() {
	var urlXml 	= "/ECS/data/order/retriveOrderItem.xsql?orderno=B05101000007";
	var urlXsl 	= "/ECS/data/order/transform/orderBaseInfo.xsl";
	try {
		//演示一: 根据xsl和xml以及渲染参数 生成html
		for(;i<=1000;i++) {
			var renderParam	= {'payment':'true'};//会在xml数据里加入参数名：payment，值为：true
			var r =	new liquiddata.TransformXmlXsl(urlXml,urlXsl,mycallbackHtml);
			r.transform(renderParam); 
		}
		//演示二: 发请求(异步)
		//var xmlhttp = new liquiddata.XMLHttpRequest("POST",urlXml,true,mycallbackXml);
		//xmlhttp.send("Body");
		
		//演示三: 发请求(同步)
		//xmlhttp	 = new liquiddata.XMLHttpRequest("GET",urlXml,false);
		//var retDom = xmlhttp.send();
		//alert("同步调用"+retDom.xml);
	} catch(e) {
		alert(e.description)
	}
}
function mycallbackHtml(html) {
	if(window.ActiveXObject) {
		document.getElementById("result").innerHTML = (j++)+"<br>"+html;
	} else if(window.XMLHttpRequest) {
		document.getElementById("result").innerHTML = "(j++)"+"<br>";
		document.getElementById("result").appendChild(html);
	}
}
function mycallbackXml(xmlDom,responseText) {
	alert("异步调用"+xmlDom.xml);
}
*/
// namespace object
if(typeof(liquiddata)=='undefined') liquiddata = {};
/**
 * method: 提交的方式：GET POST
 * url：请求URL
 * async:true:异步 false:同步
 * callback:数据读取完毕后自动回调的函数，一般在异步方式的时候才用到，
            该函数有两个参数，用于保存this.xmlhttp.responseXML和this.xmlhttp.responseText
            该函数也可以只定义一个参数，不过就只能读到this.xmlhttp.responseXML
            例如定义：function mycallbackXml(xmlDom) 或者function mycallbackXml(xmlDom,responseText)
**/
liquiddata.XMLHttpRequest = function(method,url,async,callback) {
	this.url				=	url;
	this.method				= 	method;
	this.async				=	async;
	this.init				=	function() {	
		this.async = async ? true : false;
		this.method = method || "POST";
		var xmlhttp = null;
		if(window.ActiveXObject) { 
			xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
		} else if(window.XMLHttpRequest) { 
			xmlhttp = new XMLHttpRequest();
		} else {
			throw new Error("Could not create xmlhttp on this browser");
		}	
		return xmlhttp;
	}
	this.xmlhttp			=	this.init();
	this.callback			= 	callback;
	this.requestHeaderName	=	"Content-Type";
	this.requestHeaderValue	=	"text/xml";
	this.context			=	this;
}
liquiddata.XMLHttpRequest.prototype.setRequestHeader	= function(name,value) {
	this.requestHeaderName	= name;
	this.requestHeaderValue	= value;
}
liquiddata.XMLHttpRequest.prototype.onreadystatechange	= function() {
	
	if(this.xmlhttp==null) return;
	if(this.xmlhttp.readyState != 4) return;
	if(this.xmlhttp.status == 200 || this.xmlhttp.status == 304) {
		if(this.callback == null) {
			return;
		} else if(this.callback instanceof Function) {
			// 有回调函数
			var retXmlDom = this.xmlhttp.responseXML;
			if(retXmlDom.xml=="") {
				retXmlDom = new ActiveXObject("Microsoft.XMLDOM");
				retXmlDom.loadXML(this.xmlhttp.responseText);
			}
			//-->
			this.callback.call(this.context,retXmlDom,this.xmlhttp.responseText);
		} else {
			eval(this.callback.call+'(this.context,this.xmlhttp.responseXML,this.xmlhttp.responseText)');
		}
		this.context = null;
		this.xmlhttp = null;
	} else {
		if(typeof(processException) != "undefined") {
			processException(this.xmlhttp.status,this.xmlhttp.statusText);
		} else {
			alert(this.xmlhttp.status+": "+this.xmlhttp.statusText);
		}
	}
}

liquiddata.XMLHttpRequest.prototype.send = function(httpBody) {
	this.xmlhttp.open(this.method,this.url,this.async);
	this.xmlhttp.setRequestHeader(this.requestHeaderName,this.requestHeaderValue);
	if(this.async) {
		var content = this;
		this.xmlhttp.onreadystatechange = function() {
			content.onreadystatechange.call(content);
		}
	}
	this.xmlhttp.send(httpBody);
	if(!this.async) {
		return this.xmlhttp.responseXML;
	}
}

liquiddata.TransformXmlXsl	= function(xmlUrl, xslUrl, callbackName,xmlMethod, httpBody) {

	this.xmlUrl			=	xmlUrl;
	this.xslUrl			=	xslUrl;
	this.callbackName	=	callbackName;
	this.xmlMethod		=	xmlMethod	||	"POST"
	this.httpBody		=	httpBody;
	this.args			=	null;
	this.xmlDom			= 	null;
	this.xslDom			=	null;
	this.validateXMLDOM	=	null;
}
liquiddata.TransformXmlXsl.prototype.transform	=	function(args) {
	this.args		=	args || null;
	var context		=	this;
	if(typeof(this.xmlUrl.xml) != "undefined")
		this.xmlDom	= this.xmlUrl;
	else {
		var xmlhttpXML = new liquiddata.XMLHttpRequest(this.xmlMethod,this.xmlUrl,true,this.xmlCallback);
		xmlhttpXML.context	=	context;
		xmlhttpXML.send(this.httpBody);
		xmlhttpXML = null;
	}
	
	if(typeof(this.xslUrl.xml) != "undefined") {
		this.xslDom	= this.xmlUrl;
	} else {
		var xmlhttpXSL = new liquiddata.XMLHttpRequest("GET",this.xslUrl,true,this.xslCallback);
		xmlhttpXSL.context	=	context;
		xmlhttpXSL.send();	
		xmlhttpXSL = null;
	}
	
	if(typeof(this.xmlUrl.xml) != "undefined" && typeof(this.xslUrl.xml) != "undefined") {
		this.onDataReady();
	}
}

liquiddata.TransformXmlXsl.prototype.xmlCallback	= function(xmlDom) {
	this.xmlDom = xmlDom;
	this.onDataReady();	
}
liquiddata.TransformXmlXsl.prototype.xslCallback	= function(xslDom) {
	this.xslDom = xslDom;
	this.onDataReady();
}

liquiddata.TransformXmlXsl.prototype.onDataReady = function() {
	if(this.xmlDom == null || this.xslDom == null)
		return;
		
	if(this.validateXMLDOM!=null) {
		if(!this.validateXMLDOM(this.xmlDom)) {
			if(loadingHide)loadingHide();
			return;
		}
	}	
	var html = this.transformNode();
	this.callbackName(html);
}	

liquiddata.TransformXmlXsl.prototype.transformNode = function() {
	var result = null;
	try {
		if(window.ActiveXObject) {
			if(this.args == null) {
				result = this.xmlDom.transformNode(this.xslDom);
			} else {
				var xslDoc=new ActiveXObject("MSXML2.FreeThreadedDOMDocument");
				this.xslDom.save(xslDoc);
				this.xslDom = null;
				var xslTemplate=new ActiveXObject("MSXML2.XSLTemplate");
				xslTemplate.stylesheet=xslDoc;
				var xslProcessor=xslTemplate.createProcessor();
				if(this.args!=null) {
					for(var name in this.args) {
						xslProcessor.addParameter(name,this.args[name]);
					}
				}
				xslProcessor.input=this.xmlDom;
		    	xslProcessor.transform();
		    	result = xslProcessor.output;
		    	
		    	xslDoc = null;
		    	xslTemplate = null;
		    	xslProcessor = null;
			}
		} else if(window.XMLHttpRequest) {
				var xslProcessor = new XSLTProcessor();
				xslProcessor.importStylesheet(this.xslDom);
				if(this.args!=null) {
					for(var name in this.args) {
						xslProcessor.setParameter(null,name,this.args[name]);
					}
				}
				result = xslProcessor.transformToFragment(this.xmlDom,document);
				xslProcessor = null;
		} else {
			throw new Error("Could not transform xml on this browser");
		}
	} catch(e) {
		throw e;
	} finally {
		this.xmlDom = null;
		this.xslDom = null;
		this.args 	= null;
	}
	return result;
}