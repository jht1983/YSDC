function setStart(){
		if(inputnextnode.value==""){
			alert("����д�¼��ڵ�����!");
			inputnextnode.focus();
			return;
		}
		objOpenPage.ylt.workflow.setNode(inputnextnode.value,obj_sys_startNode);
		closeWin();
	}
function changeType(){
	if(rfztj.checked||rfzhq.checked){//����
			addnodename.style.display="";
			overnode.style.display="none";
	}else{
			overnode.style.display="";
			addnodename.style.display="none";
	}
}
	function createNode(objStartNode,_iType){		
		var strName=addnodename.value;
		if(strName==""&&_iType==2)
			return;
		if(strName==""&&_iType==1&&overnode.value=="")
			return;
		var strId="";
		if(overnode.value!="")
				strId=overnode.value;
			else
				strId=objOpenPage.ylt.workflow.generId();
		//alert(strId);
		if(strId=="wkn2"){//�����ڵ�
		//alert("����");
			var objLine=objOpenPage.ylt.workflow.drawLine();
			objOpenPage.ylt.workflow.setLine(objStartNode,objOpenPage.wkn2,objLine);
		}else
			if(objOpenPage.document.getElementById(strId)==null){//���û������
				var objCurNode=objOpenPage.ylt.workflow.addNode(strId,strName,objStartNode,_iType);
			}else{//���нڵ�
				var objLine=objOpenPage.ylt.workflow.drawLine();
				objLine.bIsHave=true;//���нڵ�
				objOpenPage.ylt.workflow.setLine(objStartNode,objOpenPage.document.getElementById(strId),objLine);
				}
		return strId;
	}
	function finishConNode(){
		var strName=addnodename.value;
		var objStartNode=obj_sys_startNode;
		var ObjStartNodeName=obj_sys_startNodeNm;
		
		ObjStartNodeName.innerText=hdmc.value;
		objStartNode.nodename=hdmc.value;
		
		
		
		if(strName==""&&overnode.value==""){
			closeWin();
		}else{
			if(textdocon.innerHTML==""&&!bismrcf.checked){
				alert("��ѡ������!");
				return;
			}
			var strWinId=createNode(objStartNode,1);
			
			if(bismrcf.checked){
				objStartNode.objCons[strWinId]=nodedefault.value;
				objStartNode.objCN_Cons[strWinId]="Ĭ�ϴ���";
			}else{			
				objStartNode.objCons[strWinId]=nodeconid.value;
				objStartNode.objCN_Cons[strWinId]=textdocon.innerHTML;
				}
		}
		
		closeWin();
	}
	function clickMrCf(){
		if(bHsDefault){
			bisxztj.checked=true;
			alert("һ����������ֻ����һ��Ĭ�ϳ��ڣ�");
		}
	}
	function finishNode(){
		var objStartNode=obj_sys_startNode;
		var ObjStartNodeName=obj_sys_startNodeNm;
		
		if(hdmc.value==""){
			alert("����Ʋ���Ϊ��!");
			hdmc.focus();
			return;
		}
		if(rolecodes.value==""&&usercodes.value==""){
			alert("��ѡ�������!");
			rolenames.focus();
			return;
		}
		
		
		if (isNaN(indays.value)||indays.value==""||indays.value.indexOf(".")!=-1){
			alert("������������Ϊ������");
			return;
		}
		if (isNaN(inhous.value)||inhous.value==""||inhous.value.indexOf(".")!=-1){
			alert("����Сʱ����Ϊ������");
			return;
		}
		
		
		
		
		
		ObjStartNodeName.innerText=hdmc.value;
		objStartNode.nodename=hdmc.value;
		objStartNode.noderole=rolecodes.value;
		objStartNode.noderolename=rolenames.value;
		
		objStartNode.nodeusers=usercodes.value;
		objStartNode.nodenames=usernames.value;
		
		/***********************************�¼�*******************************************/
		if(checkishtqr.checked)
			objStartNode.checkishtqr="0";//����ȷ��
		else
			objStartNode.checkishtqr="1";//����ȷ��
		
		if(checkisqzht.checked)
			objStartNode.checkisqzht="0";//ǿ�ƻ���
		else
			objStartNode.checkisqzht="1";//ǿ�ƻ���
			
	
		objStartNode.selfhfs=selfhfs.value;//���ط�ʽ
		objStartNode.selhtfw=selhtfw.value;//���˷�Χ
		
		if(checklz.checked)
			objStartNode.checklz="0";//��תʱ֪ͨ
		else
			objStartNode.checklz="1";//��תʱ֪ͨ
		
		if(checkht.checked)
			objStartNode.checkht="0";//����ʱ֪ͨ
		else
			objStartNode.checkht="1";//����ʱ֪ͨ
		
		if(checkjs.checked)
			objStartNode.checkjs="0";//����ʱ֪ͨ
		else
			objStartNode.checkjs="1";//����ʱ֪ͨ
		
		if(checkzz.checked)
			objStartNode.checkzz="0";//��ֹʱ֪ͨ
		else
			objStartNode.checkzz="1";//��ֹʱ֪ͨ
		
	
		objStartNode.indays=indays.value;//��������
		objStartNode.inhous=inhous.value;//����Сʱ
		
		objStartNode.selclfs=selclfs.value;//����ʽ
		
		objStartNode.inmmgz=inmmgz.value;//��������
		
		var objBackNodes=document.getElementsByName("zdfwbacknode");
		var iLength=objBackNodes.length;
		objStartNode.strbacknodes="";
		for(var i=0;i<iLength;i++)
			if(objBackNodes[i].checked)
				objStartNode.strbacknodes+=objBackNodes[i].value+",";
		
		/***********************************�¼�*******************************************/
		
		if(rfztj.checked)//����
			createNode(objStartNode,2);
		else 
			if(rfzhq.checked)//����
				createNode(objStartNode,3);
			else
				createNode(objStartNode,1);
		
		closeWin();
	}
	function getConNode(){
		var objStartNode=obj_sys_startNode;
		var strParentId=objStartNode.parentnode;
		
		addHaveNode();//�������л
		getChildNodes();
	}
	function getParentForm(){
		var strParentId=obj_sys_startNode.parentnode;
		var objParent=objOpenPage.document.getElementById(strParentId);
		return objParent.selforms;
	}
	function setUpDateCN(_strWinId,_strCnExp){
		obj_sys_startNode.objCN_Cons[_strWinId]=_strCnExp;
	}
	function updateCon(){
		var strFormId=getParentForm();
		var _strConId=event.srcElement.sConId;
		var strWinId=event.srcElement.strWinId;
		winBox('ѡ������','flowform/addcon.jsp?winid='+strWinId+'&cn_exp=s'+_strConId+'&sconid='+_strConId+'&sformid='+strFormId,850,350);
	}
	function addCon(){
		var strFormId=getParentForm();
		var _strConId=nodeconid.value;
		winBox('ѡ������','flowform/addcon.jsp?cn_exp=textdocon&sconid='+_strConId+'&sformid='+strFormId,850,350);
	}
	var bHsDefault=false;
	function getChildNodes(){
		var fromLine=obj_sys_startNode.fromline.substr(1);	
		if(fromLine!=""){
		var strArrNodeId=fromLine.split(",");
			for(var i in strArrNodeId){
					var objLine=objOpenPage.document.getElementById(strArrNodeId[i]);
					var objNode=objOpenPage.document.getElementById(objLine.strToId);
					
					var strConId=obj_sys_startNode.objCons[objNode.id];
					
					
			
					var newTr=childnodesmsg.insertRow(1);
					newTr.className="tr1";					
					
					
					var newTd = newTr.insertCell();
					newTd.className="td1";
					newTd.innerHTML=objNode.id;
					
					newTd = newTr.insertCell();
					newTd.className="td1";
					newTd.innerHTML=objNode.nodename;
					
					newTd = newTr.insertCell();
					newTd.className="td1";
					newTd.id="s"+strConId;
					newTd.sConId=strConId;
					newTd.strWinId=objNode.id;
					if(strConId!="SYS_DEFAULT_FLOW_NODE")
						newTd.attachEvent("onclick",updateCon);
					else{
						bHsDefault=true;
						newTd.style.color="green";
						}
					newTd.innerHTML=obj_sys_startNode.objCN_Cons[objNode.id];
				}
	}
	
	
	
	}
	function setcheckValue(_strValue,_objChecked){
		if(_strValue=="1")
			_objChecked.checked=false;
		else
			_objChecked.checked=true;
	}
	var strBackNodes="";
	function setzdfw(_obj){
		if(_obj.value=="2"){
			strBackNodes="";
			generBackNode(obj_sys_startNode);
			tdzdfw.innerHTML=strBackNodes;
		}
	}
	//wkn1
	function generBackNode(_objNode){
		//
		var strToLine=_objNode.toline.substr(1);
		var strArrNodeId=strToLine.split(",");
			for(var i in strArrNodeId){
				var objToLine=objOpenPage.document.getElementById(strArrNodeId[i]);
				var strParentId=objToLine.strFromId;
				var objParent=objOpenPage.document.getElementById(strParentId);
				if(_objNode.strbacknodes.indexOf(strParentId+",")!=-1)
					strBackNodes+="<input name='zdfwbacknode' checked type='checkbox' id='sback"+strParentId+"' value='"+strParentId+"'><label for='sback"+strParentId+"'>"+objParent.nodename+"</a>";
				else
					strBackNodes+="<input name='zdfwbacknode' type='checkbox' id='sback"+strParentId+"' value='"+strParentId+"'><label for='sback"+strParentId+"'>"+objParent.nodename+"</a>";
				
			}
	}
	function getBackNode(){//�ڵ��ʱ��ʼ���ڵ�����
		var objStartNode=obj_sys_startNode;
		var strParentId=objStartNode.parentnode;
		var newoption;
		
		
		
		
		/***********************************�¼�*******************************************/
		
		setcheckValue(objStartNode.checkishtqr,checkishtqr);//����ȷ��
		setcheckValue(objStartNode.checkisqzht,checkisqzht);//ǿ�ƻ���
		setcheckValue(objStartNode.checklz,checklz);//��תʱ֪ͨ
		setcheckValue(objStartNode.checkht,checkht);//����ʱ֪ͨ
		setcheckValue(objStartNode.checkjs,checkjs);//����ʱ֪ͨ
		setcheckValue(objStartNode.checkzz,checkzz);//��ֹʱ֪ͨ
	
			
		if(objStartNode.selfhfs!=null)
			selfhfs.value=objStartNode.selfhfs;//���ط�ʽ
		if(objStartNode.selhtfw!=null)
			selhtfw.value=objStartNode.selhtfw;//���˷�Χ
		if(objStartNode.indays!=null)
			indays.value=objStartNode.indays;//��������
		if(objStartNode.inhous!=null)
			inhous.value=objStartNode.inhous;//����Сʱ
		if(objStartNode.selclfs!=null)
			selclfs.value=objStartNode.selclfs;//����ʽ
		
		if(objStartNode.inmmgz!=null)		
			inmmgz.value=objStartNode.inmmgz;//��������
		
		if(selhtfw.value=="2"){
			strBackNodes="";
			generBackNode(obj_sys_startNode);
			tdzdfw.innerHTML=strBackNodes;
		}
		/***********************************�¼�*******************************************/
		
		
		if(!!objStartNode.noderole){//���ò�����
			rolecodes.value=objStartNode.noderole;
			rolenames.value=objStartNode.noderolename;
			
			usercodes.value=objStartNode.nodeusers;
			usernames.value=objStartNode.nodenames;			
		}else{
			rolecodes.value="";
			rolenames.value="";
			usercodes.value="";
			usernames.value="";	
		}

		addHaveNode();//�������л
	}
	
	
	
function getHQNode(){
		var objStartNode=obj_sys_startNode;
		
		if(!!objStartNode.selforms)//���ñ�
			selforms.value=objStartNode.selforms;
		if(objStartNode.selupdateforms!="")
			if(document.getElementById("selupdateforms")!=null)
				selupdateforms.value=objStartNode.selupdateforms;
		
		
		
		if(!!objStartNode.noderole){//���ò�����
			rolecodes.value=objStartNode.noderole;
			rolenames.value=objStartNode.noderolename;
	
		}else{
			rolecodes.value="SYS_ALL_ROLES";
			rolenames.value="���и�λ";
		}
		if(objStartNode.inmmgz!=null)		
			inmmgz.value=objStartNode.inmmgz;//��������
		addHaveNode();//�������л
	}	
function finishHQNode(){
		var objStartNode=obj_sys_startNode;
		var ObjStartNodeName=obj_sys_startNodeNm;
		
		if(hdmc.value==""){
			alert("����Ʋ���Ϊ��!");
			hdmc.focus();
			return;
		}
		if(rolecodes.value==""){
			alert("��ѡ�������!");
			rolenames.focus();
			return;
		}
		ObjStartNodeName.innerText=hdmc.value;
		objStartNode.nodename=hdmc.value;
		objStartNode.noderole=rolecodes.value;
		objStartNode.noderolename=rolenames.value;
		alert(objStartNode.noderole+":"+objStartNode.noderolename);
		objStartNode.selforms=selforms.value;	

		objStartNode.inmmgz=inmmgz.value;//��������
		
		
		if(rfztj.checked)//����
			createNode(objStartNode,2);
		else if(rfzhq.checked)//����
				createNode(objStartNode,3);
			else
			createNode(objStartNode,1);
		
		closeWin();
	}	
	
function addHaveNode(){	
	var arrNodes=objOpenPage.document.getElementsByName("sysflownode");
	var iSize=arrNodes.length;
	var objOption=new Option("---ѡ�����л---","");
	overnode.options.add(objOption);
	for(var i =0;i<iSize;i++){
		var strNodeId=arrNodes[i].value;
		if(strNodeId=="wkn1"||strNodeId==obj_sys_startNode.id)
			continue;
		
		var objNode=objOpenPage.document.getElementById(strNodeId);
		if(objNode.parentnode==obj_sys_startNode.id)//�ǵ�ǰ�ڵ���ӽڵ�
			continue;
		
		if(objNode.nodetype=="2")//������������
			continue;
		
		objOption=new Option(objNode.nodename,strNodeId);
		overnode.options.add(objOption);
	}
	}