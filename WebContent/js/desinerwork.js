var curObj=null;
var objNode=new Object();
var ylt= ylt ||{};
var sColor="black";
var sFont="����";
ylt.workflow = ylt.workflow || {};
yltWorkFlow=ylt.workflow={
	iWorkNodeId:1,
	iWorkLineId:1,
	iPanelWidth:800,
	iPanelHeight:600,
	createNode:function(_strName,_strId,_iLeft,_iTop,_iWidth,_iHeight,_sPic,_iType){
		var iWidth=600;
		var iHeight=350;
		if(_iType==0){
			iWidth=350;
			iHeight=100;
		}
		objNewNode=document.createElement("<div issave='true' id='"+_strId+
		"' onmousedown='ylt.workflow.dragNode(this);' ondblclick='winBox(\""+_strName+"\",\"FlowFactory?O_SYS_TYPE=node&itype="+_iType+"&name="+_strName+"&id="+_strId+"\","+iWidth+","+iHeight+");'></div>");
		objNewNode.setAttribute("nodewidth",_iWidth);
		objNewNode.setAttribute("nodeheight",_iHeight);
		objNewNode.setAttribute("fromline","");
		objNewNode.setAttribute("toline","");
		objNewNode.setAttribute("nodename",_strName);
		
		objNewNode.setAttribute("selupdateforms","");
		

		objNewNode.setAttribute("iskbm",false);
		objNewNode.setAttribute("noderole","");
		objNewNode.setAttribute("backnode","");
		
		objNewNode.setAttribute("nodetype",""+_iType);
		
		objNewNode.objCons=new Object();
		objNewNode.objCN_Cons=new Object();
		objNewNode.objCN_ConName=new Object();
		//objNewNode.setAttribute("parothernode","");
		

		objNewNode.style.position="absolute";
		objNewNode.style.top=_iTop;
		objNewNode.style.left=_iLeft;
		objNewNode.style.zIndex = "10000";
		objNewNode.innerHTML="<input type='hidden' name='sysflownode' value='"+_strId+"'><table id='pic"+_strId+"' style=\"cursor:hand;width:"+_iWidth+";height:"+_iHeight+";background:url("+_sPic+")\"><tr><td align=\"center\"><font style='color:"+sColor+";font-family:"+sFont+";font-size:12;' id='nm"+_strId+"'>"+_strName+"</font></td></tr></table><font style='text-align:center;width:100;color:"+sColor+";font-family:"+sFont+";font-size:10;'  id='time"+_strId+"'></font>";
		document.body.appendChild(objNewNode);
		return objNewNode;
	},
	createWorkNode:function(_strName,_strId,_strNodeType,_iX,_iY){
		var objNode=document.getElementById(_strId);
		if(objNode!=null)
			return objNode;
		switch (_strNodeType)
		{
		case "start" :
		case "end" :
			objNode=ylt.workflow.createNode(_strName,_strId,_iX,_iY,100,46,'images/workflow/'+_strNodeType+'.png',0);
			break;
		case "event" :
			sColor="black";
			objNode=ylt.workflow.createNode(_strName,_strId,_iX,_iY,100,56,'images/workflow/'+_strNodeType+'.png',1);
			break;
		case "work" :				
			objNode=ylt.workflow.createNode(_strName,_strId,_iX,_iY,100,64,'images/workflow/'+_strNodeType+'.png',2);
			sColor="black";
			break;
		case "hq" :				
			objNode=ylt.workflow.createNode(_strName,_strId,_iX,_iY,65,65,'images/workflow/'+_strNodeType+'.png',3);
			sColor="black";
			break;
		
		}
		return objNode;
	},
	setLine:function(_objFromNode,_objToNode,_objLine){		
		_objFromNode.fromline+=","+_objLine.id;
		_objToNode.toline+=","+_objLine.id;
		_objLine.strFromId=_objFromNode.id;
		_objLine.strToId=_objToNode.id;
		if(_objFromNode.nodetype==2){
			var lineText=_objFromNode.objCN_ConName[_objToNode.id];
			if(lineText!=null)
				this.insertText(_objLine.id,lineText);
			}
		claLinePix(_objFromNode,_objToNode,_objLine);
	},
	drawLine1:function(){
		var lineObj=document.createElement("v:line");
				lineObj.setAttribute('id',this.generLineId());
				lineObj.style.position="absolute";
				lineObj.setAttribute('from',"0,0");
				lineObj.setAttribute('to',"0,0");
				lineObj.setAttribute('strokecolor',"#be7b5e");
				lineObj.setAttribute('strokeweight',"1");
				lineObj.setAttribute('title',"3");
				
				lineObj.setAttribute('bIsHave',false);//�����ڵĽڵ�
				
			var arrowobj=document.createElement("v:stroke");
				arrowobj.setAttribute('EndArrow','Classic');				
				lineObj.appendChild(arrowobj);
				document.body.appendChild(lineObj);	
				return lineObj;
	},
	insertText:function(_strId,_strContent){
		var lineText=document.createElement("<font style='color:green;font-family:"+sFont+";font-size:10;'></font>");
		lineText.setAttribute('id',"text_"+_strId);
		lineText.style.position="absolute";
		lineText.style.top=100;
		lineText.style.left=200;
		lineText.innerHTML=_strContent;
		document.body.appendChild(lineText);
	},
	drawLine:function(){
		var lineObj=document.createElement("v:polyline");
		var strLineId=this.generLineId();
				lineObj.setAttribute('id',strLineId);
				lineObj.style.position="absolute";
				lineObj.setAttribute('strokecolor',"gray");
				lineObj.setAttribute('strokeweight',"0.5");
				lineObj.setAttribute('title',"3");
				lineObj.setAttribute('filled','false');	
				
				lineObj.setAttribute('bIsHave',false);//�����ڵĽڵ�
				
				var arrowobj=document.createElement("v:stroke");
				arrowobj.setAttribute('EndArrow','Classic');				
				lineObj.appendChild(arrowobj);
				document.body.appendChild(lineObj);				
				return lineObj;
	},
	initWorkPanle:function(){//��ʼ�����������
		iPanelWidth = window.screen.availWidth-200; 
		iPanelHeight = window.screen.availHeight;	
		var objStart=this.createWorkNode("��ʼ",this.generId(),"start",100,iPanelHeight/3);
		var objend=	this.createWorkNode("����",this.generId(),"end",iPanelWidth-100,iPanelHeight/3);
	},
	addNode:function(_strId,_strName,_objStart,_iType){
		var strNodeType="event";
		if(_iType==2)
			strNodeType="work";
		else if(_iType==3)
			strNodeType="hq";
		var iCount=_objStart.fromline.split(",").length-1;
		var iYpix=_objStart.style.pixelTop;//iPanelHeight/3;
		var iViewCount=(iCount/2).toFixed(0);
		if(iViewCount==0)
			iViewCount=1;
		if(iCount>0){
			if(iCount%2==0)
				iYpix+=100*iViewCount;
			else
				iYpix-=100*iViewCount;
		}
		
		var objend=	this.createWorkNode(_strName,_strId,strNodeType,_objStart.style.pixelLeft+200,iYpix);
		//objend.backnode=_objStart.id;
		objend.parentnode=_objStart.id;
		var objLine=this.drawLine();
		this.setLine(_objStart,objend,objLine);
		return objend

	},
	setNode:function(_strName,_objStart){		
		if(_objStart.fromline==""){
			var objend=	this.createWorkNode(_strName,this.generId(),"event",_objStart.style.pixelLeft+200,iPanelHeight/3);
			objend.parentnode=_objStart.id;
			var objLine=this.drawLine();
			this.setLine(_objStart,objend,objLine);
		}

	},
	generId:function(){
		var strId="wkn"+this.iWorkNodeId;
		this.iWorkNodeId++;
		return strId;
	},
	generLineId:function(){
		var strId="wline"+this.iWorkLineId;
		this.iWorkLineId++;
		return strId;
	},
		
	dragNode:function(aObj){
	//if(curObj!=null)
	//	curObj.style.zIndex=100002;
	var obj=aObj;
	//alert(obj.innerHTML);
	curObj=obj;
	//obj.style.zIndex=100003;
    var s = obj.style;
    var b = document.body;
    var x = event.clientX + b.scrollLeft - s.pixelLeft;
    var y = event.clientY + b.scrollTop - s.pixelTop;

	var strArrFromLines=null;
	if(obj.fromline!="")
		strArrFromLines=obj.fromline.substr(1).split(",");
	var strArrToLines=null;
	if(obj.toline!="")
		strArrToLines=obj.toline.substr(1).split(",");

    var m = function()
    {
            if (event.button == 1)
            {							
                    s.pixelLeft = event.clientX + b.scrollLeft - x
                    s.pixelTop = event.clientY + b.scrollTop - y
					if(strArrFromLines!=null){
						for(var i in strArrFromLines){
							var objLine=$(strArrFromLines[i]);
							claLinePix($(objLine.strFromId),$(objLine.strToId),objLine);
						}
					}
					if(strArrToLines!=null){
						for(var i in strArrToLines){
							var objLine=$(strArrToLines[i]);
							claLinePix($(objLine.strFromId),$(objLine.strToId),objLine);
						}
					}
            }
            else {
					document.detachEvent("onmousemove", m);
			}
    }
    document.attachEvent("onmousemove", m);				
}
};
function $(_strId){
	return document.getElementById(_strId);
}

function claLinePix1(_objFromNode,_objToNode,_objLine){
		var startPosX=_objFromNode.style.pixelLeft+_objFromNode.nodewidth/2;
		var startPosY=_objFromNode.style.pixelTop+_objFromNode.nodeheight/2;
		var endPosX=_objToNode.style.pixelLeft+_objToNode.nodewidth/2;
		var endPosY=_objToNode.style.pixelTop+_objToNode.nodeheight/2;
		var fAngle=Math.atan2(endPosX-startPosX,endPosY-startPosY)*180/Math.PI;
		
		if(fAngle<45&&fAngle>=-45){
			startPosY=startPosY+_objFromNode.nodeheight/2;
			endPosY=endPosY-_objToNode.nodeheight/2;
		}else if(fAngle>=45&&fAngle<135){
			startPosX=startPosX+_objFromNode.nodewidth/2;
			endPosX=endPosX-_objToNode.nodewidth/2;
		}else if(fAngle>=135||fAngle<-135){
			startPosY=startPosY-_objFromNode.nodeheight/2;
			endPosY=endPosY+_objToNode.nodeheight/2;
		}else{
			startPosX=startPosX-_objFromNode.nodewidth/2;
			endPosX=endPosX+_objToNode.nodewidth/2;
		}
		_objLine.from=startPosX+","+startPosY;
		_objLine.to=endPosX+","+endPosY;
	}

function claTextPix(_strId,_iLength,_iPosX,_iPosY){
	var objText=document.getElementById("text_"+_strId);
	if(objText!=null){
			objText.style.left=_iPosX;
			objText.style.top=_iPosY;
			objText.style.width=_iLength;
	}
}	
function claLinePix(_objFromNode,_objToNode,_objLine){
		var startPosX=_objFromNode.style.pixelLeft+_objFromNode.nodewidth/2;
		var startPosY=_objFromNode.style.pixelTop+_objFromNode.nodeheight/2;
		var endPosX=_objToNode.style.pixelLeft+_objToNode.nodewidth/2;
		var endPosY=_objToNode.style.pixelTop+_objToNode.nodeheight/2;
		var strPoints="";
		
		if(endPosX>(startPosX+_objFromNode.nodewidth/2)){//�ұ�
			if(endPosY<_objFromNode.style.pixelTop){//����
				strPoints=startPosX+","+_objFromNode.style.pixelTop+" "+startPosX+","+endPosY+" "+_objToNode.style.pixelLeft+","+endPosY;				
			}else if(endPosY>(_objFromNode.style.pixelTop+_objFromNode.nodeheight)){//����
				strPoints=startPosX+","+(_objFromNode.style.pixelTop+_objFromNode.nodeheight)+" "+startPosX+","+endPosY+" "+_objToNode.style.pixelLeft+","+endPosY;
			}else{//ƽ��
				strPoints=startPosX+","+endPosY+" "+_objToNode.style.pixelLeft+","+endPosY;
			}
				var _iStartX=_objFromNode.style.pixelLeft+_objFromNode.nodewidth;
				var iXLength=_objToNode.style.pixelLeft-_iStartX;
				if(iXLength<0)
					iXLength=0;
				claTextPix(_objLine.id,iXLength,_iStartX,endPosY-10);
					
		}else if(endPosX<_objFromNode.style.pixelLeft){//���
			if(endPosY<_objFromNode.style.pixelTop){//����
				strPoints=startPosX+","+_objFromNode.style.pixelTop+" "+startPosX+","+endPosY+" "+(_objToNode.style.pixelLeft+_objToNode.nodewidth)+","+endPosY;
			}else if(endPosY>(_objFromNode.style.pixelTop+_objFromNode.nodeheight)){//����
				strPoints=startPosX+","+(_objFromNode.style.pixelTop+_objFromNode.nodeheight)+" "+startPosX+","+endPosY+" "+(_objToNode.style.pixelLeft+_objToNode.nodewidth)+","+endPosY;
			}else{//ƽ��
				strPoints=startPosX+","+endPosY+" "+(_objToNode.style.pixelLeft+_objToNode.nodewidth)+","+endPosY;
			}
			
			var _iEndX=_objToNode.style.pixelLeft+_objToNode.nodewidth;
			var iXLength=startPosX-_iEndX;
				if(iXLength<0)
					iXLength=0;
				claTextPix(_objLine.id,iXLength,_iEndX,endPosY-10);
			
		}else{//����
			if(endPosY<_objFromNode.style.pixelTop){//����
				strPoints=startPosX+","+startPosY+" "+startPosX+","+(_objToNode.style.pixelTop+_objToNode.nodeheight);
			}else{//����
				strPoints=startPosX+","+startPosY+" "+startPosX+","+_objToNode.style.pixelTop;
			}
		
		}
		_objLine.Points.value=strPoints;
	}
ylt.workflow.initWorkPanle();
var strParam="";
var strErr="";

var strFormLineIds="";


function save(_strFlowId){
	var objNodes=document.getElementsByTagName("div");
	var iLength=objNodes.length;
	strErr="";
	strParam="O_SYS_TYPE=save&fid="+_strFlowId;//��ʼ��	
	for(var i=0;i<iLength;i++)
		saveNode(objNodes[i]);
	
	
	if(strErr==""){
			//alert(strParam);
			//return;
			var strOpMsg=getTx(strParam,"FlowFactory");
			if(strOpMsg=="ok")
				messageBox("��ʾ��Ϣ!��","����ɹ���");
			else
				messageBox("����","����ʧ�ܣ�");
		}else
			messageBox("����",strErr);
	
}
function generParams(_bIsHave,_iCount,_strParentId,objNode){
	var strId=objNode.id;
	if(_bIsHave)
		strId="$"+_iCount+"_"+strId;
	strParam+="&id="+strId;
	strParam+="&"+strId+"_name="+objNode.nodename;
	strParam+="&"+strId+"_par="+_strParentId;
	strParam+="&"+strId+"_forms="+objNode.selforms;
	
	strParam+="&"+strId+"_selupdateforms="+objNode.selupdateforms;
	
	strParam+="&"+strId+"_roles="+objNode.noderole;
	strParam+="&"+strId+"_users="+objNode.nodeusers;
	strParam+="&"+strId+"_cons="+objNode.flowCon;
	strParam+="&"+strId+"_split="+objNode.iskbm;
	strParam+="&"+strId+"_fx="+objNode.style.left;
	strParam+="&"+strId+"_fy="+objNode.style.top;
	strParam+="&"+strId+"_rolenames="+objNode.noderolename;
	strParam+="&"+strId+"_usernames="+objNode.nodenames;
	strParam+="&"+strId+"_nodetype="+objNode.nodetype;
	
	generBackAndPublish(objNode.checkishtqr,"_checkishtqr",strId);
	generBackAndPublish(objNode.checkisqzht,"_checkisqzht",strId);
	generBackAndPublish(objNode.checklz,"_checklz",strId);
	generBackAndPublish(objNode.checkht,"_checkht",strId);
	generBackAndPublish(objNode.checkjs,"_checkjs",strId);
	generBackAndPublish(objNode.checkzz,"_checkzz",strId);
	generBackAndPublish(objNode.selfhfs,"_selfhfs",strId);
	generBackAndPublish(objNode.selhtfw,"_selhtfw",strId);
	generBackAndPublish(objNode.indays,"_indays",strId);
	generBackAndPublish(objNode.inhous,"_inhous",strId);
	generBackAndPublish(objNode.selclfs,"_selclfs",strId);
	
	if(objNode.strbacknodes==null)
		objNode.strbacknodes="";
	strParam+="&"+strId+"_strbacknodes="+objNode.strbacknodes;
	
	if(objNode.inmmgz==null)
		objNode.inmmgz="";
	strParam+="&"+strId+"_inmmgz="+objNode.inmmgz;
}

function generBackAndPublish(_strValues,_strParamNames,_strId){
	if(_strValues==null)
		_strValues="0";
	strParam+="&"+_strId+_strParamNames+"="+_strValues;

}



function saveNode(objNode){
	if(objNode.id=="wkn1"||objNode.id=="framediv")
		return;
	var strToLine=objNode.toline.substr(1);
	if(objNode.id=="wkn2"){
		var strArrNodeId=strToLine.split(",");
			for(var i in strArrNodeId){
				var objToLine=document.getElementById(strArrNodeId[i]);
				if(objToLine==null)
					strErr+="<font color='green'></font>û��<font color='red'>һ����ȫ�պϵ�����</font>!<br>";
				else
					strParam+="&"+objToLine.strFromId+"_over=1";
			}
		
		
		return;
	}
	if(objNode.fromline=="")
		strErr+="<font color='green'>"+objNode.nodename+"</font>û��<font color='red'>�رսڵ�</font>!<br>";

	if(objNode.nodetype==1||objNode.nodetype==3){
		if(objNode.noderole=="")
			strErr+="<font color='green'>"+objNode.nodename+"</font>û��<font color='red'>������</font>!<br>";
	}else{
		objNode.noderolename="";
		objNode.nodenames="";
		objNode.noderole="";
		objNode.nodeusers="";
	}
	var strId=objNode.id;
	objNode.flowCon="";
	if(objNode.nodetype==2){//��������
		for (var k in objNode.objCons){
			objNode.flowCon+=","+k+":"+objNode.objCons[k];
		}
		if(objNode.flowCon!="")
			objNode.flowCon=objNode.flowCon.substring(1);
	}
	if(strToLine=="")
		strErr+="<font color='green'>"+objNode.nodename+"</font>û��<font color='red'>��ʼ�ڵ�</font>!<br>";
	else{
		var strArrNodeId=strToLine.split(",");
			for(var i in strArrNodeId){
				var objToLine=document.getElementById(strArrNodeId[i]);
				generParams(objToLine.bIsHave,i,objToLine.strFromId,objNode);
			}
	}
}

function save1(_strFlowId){
	strFormLineIds="";
	var fromLine=wkn1.fromline.substr(1);
	var fromLine=objNode.fromline.substr(1);	
	
	
	if(fromLine!=""){

		strParam="O_SYS_TYPE=save&fid="+_strFlowId;//��ʼ��
		strErr="";
		var strArrNodeId=fromLine.split(",");
		for(var i in strArrNodeId)
			saveNode(strArrNodeId[i]);
		if(strErr==""){
			var strOpMsg=getTx(strParam,"FlowFactory");
			//var strOpMsg="";//alert(strParam);
			if(strOpMsg=="ok")
				messageBox("��ʾ��Ϣ!��","����ɹ���");
			else
				messageBox("����","����ʧ�ܣ�");
		}else
			messageBox("����",strErr);
		//��ʼ����

	}else{
		alert("����Ҫ���棡");
	}
}
function saveNode1(_strLineId){
if(strFormLineIds.indexOf(_strLineId)!=-1)
	return;
	strFormLineIds+=_strLineId+",";
	var objLine=document.getElementById(_strLineId);
	
	
	var objNode=document.getElementById(objLine.strToId);
	if(objNode.id=="wkn2"){
		strParam+="&"+objLine.strFromId+"_over=1";
		return;
	}
	if(objNode.fromline=="")
		strErr+="<font color='green'>"+objNode.nodename+"</font>û��<font color='red'>�رսڵ�</font>!<br>";
	if(objNode.nodetype==1||objNode.nodetype==3){
		if(objNode.noderole=="")
			strErr+="<font color='green'>"+objNode.nodename+"</font>û��<font color='red'>������</font>!<br>";
	}else{
		objNode.noderolename="";
		objNode.nodenames="";
		objNode.noderole="";
		objNode.nodeusers="";
	}

	var strId=objNode.id;
	
	if(objLine.bIsHave)
		strId="$"+strId;
		
	strParam+="&id="+strId;
	strParam+="&"+strId+"_name="+objNode.nodename;
	
	
	strParam+="&"+strId+"_par="+objLine.strFromId;
	
/**	if(objLine.bIsHave){
		alert(objLine.strFromId);
		strParam+="&"+strId+"_par="+objLine.strFromId;
	}else
		strParam+="&"+strId+"_par="+objNode.parentnode;**/
		
	strParam+="&"+strId+"_forms="+objNode.selforms;
	strParam+="&"+strId+"_back="+objNode.backnode;
	strParam+="&"+strId+"_roles="+objNode.noderole;
	strParam+="&"+strId+"_users="+objNode.nodeusers;
	
	
		objNode.flowCon="";

	if(objNode.nodetype==2){
		for (var k in objNode.objCons){
			objNode.flowCon+=","+k+":"+objNode.objCons[k];
		}
		if(objNode.flowCon!="")
			objNode.flowCon=objNode.flowCon.substring(1);
	}
	//alert(objNode.flowCon);
	strParam+="&"+strId+"_cons="+objNode.flowCon;

	strParam+="&"+strId+"_split="+objNode.iskbm;
	strParam+="&"+strId+"_fx="+objNode.style.left;
	strParam+="&"+strId+"_fy="+objNode.style.top;
	strParam+="&"+strId+"_rolenames="+objNode.noderolename;
	strParam+="&"+strId+"_usernames="+objNode.nodenames;
	
	strParam+="&"+strId+"_nodetype="+objNode.nodetype;
	
	









	var fromLine=objNode.fromline.substr(1);	
	if(fromLine!=""){
		var strArrNodeId=fromLine.split(",");
			for(var i in strArrNodeId)
				saveNode(strArrNodeId[i]);
	}
}
