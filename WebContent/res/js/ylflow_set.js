ylt.flow.updateForm = function (_obj,num){
    miniWin('\u9009\u62e9\u8868\u5355','','View?SPAGECODE=1501320329106&type=flowTableName&flowsId='+num,1000,630,'','');
};
/*
\u9009\u62e9\u8868\u5355  流程选择
ylt.flow.updateForm = function (_obj,num){
    miniWin('?????','','View?SPAGECODE=1501320802541&type=flowTableName&flowsId='+num,1000,630,'','');
};

*/
function getAjaxActive(){
	var xmlHttp;
 if (window.ActiveXObject) { 
  xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
 } 
 else if (window.XMLHttpRequest) { 
  xmlHttp = new XMLHttpRequest();
 }
 return xmlHttp;
}

function getTx(param,aStrUrl){
		var xml=getAjaxActive();
		xml.open("POST",aStrUrl,false);
		xml.setRequestHeader("content-length",param.length);  
		xml.setRequestHeader("content-type","application/x-www-form-urlencoded");  
		xml.setRequestHeader("charset", "UTF-8");
		xml.send(param);  
		var res = xml.responseText;
		return res;
	}

ylt.flow.arrStrAttrNames=["S_AUDIT_TABLECONTROL", "S_AUDIT_TYPE", "S_AUDIT_DESC", "S_AUDIT_TZXX", "S_AUDIT_ROLE", "S_AUDIT_BRANCH", "S_AUDIT_USER", "S_AUDIT_YQTS", "S_AUDIT_YQTSCL", "S_AUDIT_THJD", "NO_AUDIT_SHOW", "NO_AUDIT_USER","S_AUDIT_PREEMPTION","S_AUD_VAL","S_AUDIT_DEF","S_AUDIT_SEL","NO_AUDIT_FLOWNAME","S_FLOW_SON","NO_AUDIT_FSPJ","S_AUDIT_FSPJ","NO_AUDIT_THJDZD","S_AUDIT_SQRY","S_AUDIT_SQRYATTR","S_AUDIT_THJDZD","S_AUDIT_THDJR","S_CHILD_TYPE","S_CHILD_TRANSQL","S_CHILD_TRANCON","S_AUDIT_CLASSNAME","S_AUDIT_PAGENAME"];
ylt.flow.arrStrAttrIds=["S_AUDIT_TABLECONTROL", "S_AUDIT_TYPE", "S_AUDIT_DESC", "S_AUDIT_TZXX", "S_AUDIT_ROLE", "S_AUDIT_BRANCH", "S_AUDIT_USER", "S_AUDIT_YQTS", "S_AUDIT_YQTSCL", "S_AUDIT_THJD", "NO_AUDIT_SHOW", "NO_AUDIT_USER","S_AUDIT_PREEMPTION","S_AUD_VAL","S_AUDIT_DEF","S_AUDIT_SEL","NO_AUDIT_FLOWNAME","S_FLOW_SON","NO_AUDIT_FSPJ","S_AUDIT_FSPJ","NO_AUDIT_THJDZD","S_AUDIT_SQRY","S_AUDIT_SQRYATTR","S_AUDIT_THJDZD","S_AUDIT_THDJR","S_CHILD_TYPE","S_CHILD_TRANSQL","S_CHILD_TRANCON","S_AUDIT_CLASSNAME","S_AUDIT_PAGENAME"];
function getQueryString(name) {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
    var r = parent.window.location.search.substr(1).match(reg);

    if (r != null) {
        return unescape(r[2]);
    }
    return null;
};
function BgetQueryString(name) {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return unescape(r[2]);
    }
    return null;
};
	var bmidCode =  getQueryString("bmid");	
ylt.flow.showAttrs=function () {
		var b, c, d, e, f, g, h, i, j, k, a = this;
		for(this.objCurSelGraph = a, b = "&flowid=" + ylt.flow.strFlowId + "&nodeid=" + a.data("id") + "&caption=" + a.data("text") + "&nodetype=" + a.data("iType"), c = a.data("child"), null == c && (c = ""), d = c.split(","), e = d.length, b += "&nodechilds=" + c, f = "&nodechilds=" + c, g = 0; e > g; g++) h = d[g], "" != h && (f += "&nodechild_" + h + "=" + ylt.flow.objCurGraph[h].data("text"));
		for(i = ylt.flow.arrStrAttrIds.length, g = 0; i > g; g++) j = a.data("attr_" + ylt.flow.arrStrAttrIds[g]), j || (j = ""), b += "&" + ylt.flow.arrStrAttrIds[g] + "=" + j;
		k = this.objCurSelGraph.data("iType"), "1" == k ? miniWin("\u8bbe\u7f6e\u6d3b\u52a8", "", "flow-select.v?formid=" + str_Sys_FormId + "&bmid="+bmidCode, 1440, 800, "", "") : "2" == k ? miniWin("\u7f51\u5173\u8bbe\u7f6e", "", "flow-gate.v?formid=" + str_Sys_FormId + f+ "&bmid="+bmidCode, 1440, 800, "", "") : "4" == k ? miniWin("\u8bbe\u7f6e\u7ed3\u675f\u8282\u70b9", "", "flow-end.v?formid=" + str_Sys_FormId+ "&bmid="+bmidCode, 1440, 800, "", "") : "5" == k ? miniWin("\u8bbe\u7f6e\u5b50\u6d41\u7a0b", "", "flow-child.v?formid=" + str_Sys_FormId+ "&bmid="+bmidCode, 1440, 800, "", "") : miniWin("\u8bbe\u7f6e\u5f00\u59cb\u8282\u70b9", "", "flow-start.v?formid=" + str_Sys_FormId+ "&bmid="+bmidCode, 1440, 800, "", "")
};     //设置活动

var S_FLOW_ID=BgetQueryString('flowid');
var S_AUDIT_VERSION=BgetQueryString('flowversion');
var S_RUN_ID=BgetQueryString('runid');

var ajaxreturn = getTx("S_FLOW_ID="+S_FLOW_ID+"&S_AUDIT_VERSION="+S_AUDIT_VERSION+"&S_RUN_ID="+S_RUN_ID,"ajax.v");
function picDissolveProcess(_obj, nowRunNum, lastRunNum) {

   var nextNodeArr =  getNextNode_Arr(_obj);
    for(key in nextNodeArr){
        var Ele = ylt.flow.objCurGraph[nextNodeArr[key]];
        if(lastRunNum == nextNodeArr[key]){
            _obj.attr('fill', '#90ed7d');
            Ele.attr('fill', '#90ed7d');
            return true;
        }
    }
   
    if(nextNodeArr.length==1){
       _obj.attr('fill', '#90ed7d');
        if(picDissolveProcess(Ele, nextNodeArr[key], lastRunNum)){
            Ele.attr('fill', '#90ed7d');
        } 
    }
    
    for(key in nextNodeArr){
        var Ele = ylt.flow.objCurGraph[nextNodeArr[key]];
        if(Ele.type=="circle"){
            if(picDissolveProcess(Ele, nextNodeArr[key], lastRunNum)){
                _obj.attr('fill', '#90ed7d');
            } 
        }else if(Ele.type=="path"){
            if(picDissolveProcess(Ele, nextNodeArr[key], lastRunNum)){
                _obj.attr('fill', '#90ed7d');
            }  
        }
    }
}

function getNextNode_jstOne(_obj){
    _obj = _obj.data('graphStartLine')
    for(var key in _obj){
      return key
    }
}
function getNextNode_Arr(_obj){
   
    _obj = _obj.data('graphStartLine')
    var arrayObj = new Array();
    for(var key in _obj){
      arrayObj.push(key);
    }
    return arrayObj
}

function Fillgr(_str) {
			//得到当前流程所有节点
			_str = _str.substr(_str.indexOf("-*-") + 3);
			_str = _str.substr(0, _str.indexOf("-*-"));
		
			var flowRunData =  _str.split(',');
			var newRunNum = flowRunData[1];
			var oldRunArr = flowRunData[0].split('|');
			var oldRunNum="";
			var bool = false;
		
			//判断当前节点只运行一次
			if(flowRunData.indexOf("|" + newRunNum + "|") == flowRunData.lastIndexOf("|" + newRunNum + "|")) {
				bool = true;
			}
			//绘制路线
			for(var i = 0, j = oldRunArr.length; i < j; i++) {
				oldRunNum = oldRunArr[i];
				var objCurGrap = ylt.flow.objCurGraph[oldRunNum];
				// objCurGrap.attr('fill', '#90ed7d');
				if(bool && oldRunNum == newRunNum) {
					break;
				} else {
				    //中间有网关
					picDissolveProcess(objCurGrap, oldRunNum, oldRunArr[i + 1]);
				}
			}
			//绘制当前节点
			ylt.flow.objCurGraph[newRunNum].attr('fill', '#f96665');
		}
		
		
		
ylt.flow.init=function(a, b, c) {

		var e, f, g, h, i, j, k;
		$("treepanel"), this.drawPaper = Raphael(treepanel);
		for(e in a) {
			for(f = a[e], 
				g = f.iIndexColor, 
				null != c 
				&& (this.strAudNodeId = c.nodeId, this.strAudUser = c.audUser, g = e != this.strAudNodeId ? 6 : 5),
				h = this.addNode(e, f.x, f.y, this.iNodeWidth, this.iNodeHeight, f.text, f.type, g),
				i = this.arrStrAttrIds.length, j = 0; i > j; j++) k = this.arrStrAttrIds[j], h.data("attr_" + k, f[k]);
			h.data("attr_childcon", f.S_CONDITION), this.generPointCoordinate(h)
		}
		this.drawDataLine(a), this.iNodeId = b, document.body.onmousemove = function() {
			ylt.flow.dragBodyPanel()
		}, document.body.onmousedown = function() {
			ylt.flow.bodyDragStart()
		}, document.body.onmouseup = function() {
			ylt.flow.bodyDragEnd()
		}, document.body.oncontextmenu = function() {
			return ylt.flow.clearAddNodeStatus(), ylt.flow.bIsDel = !1, !1
		}, document.body.onselectstart = function() {
			return !1
		}, document.body.onselect = function() {
			document.selection.empty()
		}
		Fillgr(ajaxreturn);
	};
