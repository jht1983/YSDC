function setServices(_strFlowId,_strFunId,_strWid){
	var strParam="NO_OPTYPE=1&NO_CON=T_FLOWMAIN$SFLOWID&T_FLOWMAIN$SFLOWID="+_strFlowId+"&T_FLOWMAIN$SFUN="+_strFunId;
	getTx(strParam,"YLWebAction");
	parent.closeWinById(_strWid);
}
function selUsers(_iRoleCount){
		var strSelRole="";
		var strSelRoleName="";
		for(var i=0;i<_iRoleCount;i++){
			if(document.getElementById("selcheck"+i).checked){
				strSelRole+=","+document.getElementById("tdrolecode"+i).innerText;
				strSelRoleName+=","+document.getElementById("tdrolename"+i).innerText;
			}
		}
		objOpenPage.usernames.value=strSelRoleName.substr(1);
		objOpenPage.usercodes.value=strSelRole.substr(1);	
		objOpenPage.rolenames.value=" ";
		objOpenPage.rolecodes.value=" ";
		closeWin();
	}
function selRole(_iRoleCount){
		var strSelRole="";
		var strSelRoleName="";
		for(var i=0;i<_iRoleCount;i++){
			if(document.getElementById("selcheck"+i).checked){
				strSelRole+=","+document.getElementById("tdrolecode"+i).innerText;
				strSelRoleName+=","+document.getElementById("tdrolename"+i).innerText;
			}
		}
		objOpenPage.rolenames.value=strSelRoleName.substr(1);
		objOpenPage.rolecodes.value=strSelRole.substr(1);
		objOpenPage.usernames.value=" ";
		objOpenPage.usercodes.value=" ";		
		closeWin();
	}
var testCard=function(idcard){ 
var Errors=new Array("0","����֤����λ������!","����֤����������ڳ�����Χ���зǷ��ַ�!","����֤����У�����!","����֤�����Ƿ�!"); 
var area={11:"����",12:"���",13:"�ӱ�",14:"ɽ��",15:"���ɹ�",21:"����",22:"����",23:"������",31:"�Ϻ�",32:"����",33:"�㽭",34:"����",35:"����",36:"����",37:"ɽ��",41:"����",42:"����",43:"����",44:"�㶫",45:"����",46:"����",50:"����",51:"�Ĵ�",52:"����",53:"����",54:"����",61:"����",62:"����",63:"�ຣ",64:"����",65:"xinjiang",71:"̨��",81:"���",82:"����",91:"����"} 
var idcard,Y,JYM; 
var S,M; 
var idcard_array = new Array(); 
idcard_array = idcard.split(""); 
if(area[parseInt(idcard.substr(0,2))]==null) return Errors[4]; 
switch(idcard.length){ 
case 15: 
if ((parseInt(idcard.substr(6,2))+1900) % 4 == 0 || ((parseInt(idcard.substr(6,2))+1900) % 100 == 0 && (parseInt(idcard.substr(6,2))+1900) % 4 == 0 )){ 
ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/;//���Գ������ڵĺϷ��� 
} 
else{ 
ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/;//���Գ������ڵĺϷ��� 
} 
if(ereg.test(idcard)) 
return Errors[0]; 
else 
return Errors[2]; 
break; 
case 18: 
if( parseInt(idcard.substr(6,4)) % 4 == 0 || ( parseInt(idcard.substr(6,4)) % 100 == 0 && parseInt(idcard.substr(6,4))%4 == 0 )){ 
ereg = /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/;//����������ڵĺϷ����������ʽ 
} 
else{ 
ereg = /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/;//ƽ��������ڵĺϷ����������ʽ 
} 
if(ereg.test(idcard)){ 
S = (parseInt(idcard_array[0]) + parseInt(idcard_array[10])) * 7 + (parseInt(idcard_array[1]) + parseInt(idcard_array[11])) * 9 + (parseInt(idcard_array[2]) + parseInt(idcard_array[12])) * 10 + (parseInt(idcard_array[3]) + parseInt(idcard_array[13])) * 5 + (parseInt(idcard_array[4]) + parseInt(idcard_array[14])) * 8 + (parseInt(idcard_array[5]) + parseInt(idcard_array[15])) * 4 + (parseInt(idcard_array[6]) + parseInt(idcard_array[16])) * 2 + parseInt(idcard_array[7]) * 1 + parseInt(idcard_array[8]) * 6 + parseInt(idcard_array[9]) * 3 ; 
Y = S % 11; 
M = "F"; 
JYM = "10X98765432"; 
M = JYM.substr(Y,1); 
if(M == idcard_array[17]) 
return Errors[0]; 
else 
return Errors[3]; 
} 
else 
return Errors[2]; 
break; 
default: 
return Errors[1]; 
break; 
} 
} 

function check(aForm,aAction){

	var iFormSize=aForm.elements.length;
	var iRule="";
	
	for(var i=0;i<iFormSize;i++){
		var objRule=aForm.elements[i].attributes.rule;
		
		if(objRule!=null){
			iRule=objRule.value;
			switch(iRule){
			case "bxtx":
				if(aForm.elements[i].value==""){
				
					alert(aForm.elements[i].attributes.ruleTip.value+",������д��");
					aForm.elements[i].focus();
					return false;
				}
				break;
			case "bxsz":
				if (isNaN(aForm.elements[i].value)||aForm.elements[i].value==""){
					alert(aForm.elements[i].attributes.ruleTip.value+",���������֣�");
					aForm.elements[i].focus();
					return false;
				}
				break;
			case "bxsj":
				var bIs=isSj(aForm.elements[i].value);
				if(!bIs){
					alert(aForm.elements[i].attributes.ruleTip.value+",�������ֻ���");
					aForm.elements[i].focus();
					return false;
				}
				break;

			case "bxdh":
				var bIs=isDh(aForm.elements[i].value);
				if(!bIs){
					alert(aForm.elements[i].attributes.ruleTip.value+",�����ǵ绰��");
					aForm.elements[i].focus();
					return false;
				}
				break;
			
			case "bxem":
				var bIs=isYx(aForm.elements[i].value);
				if(!bIs){
					alert(aForm.elements[i].attributes.ruleTip.value+",�����ǵ����ʼ���");
					aForm.elements[i].focus();
					return false;
				}
				break;

			case "bxsf":
				var msg=testCard(aForm.elements[i].value)
				
					if(msg!='0'){
						alert(msg);
						aForm.elements[i].focus();
						return false;
					}
				break;

			case "bxyb":
				//return zdizk();
				break;
			case "glrwmc":
				strTaskValue();
				break;
			case "bncf":
				if(aForm.elements[i].value==""){
					alert("����д"+aForm.elements[i].attributes.ruleTip.value);
					aForm.elements[i].focus();
					return false;
				}
				
				var strIsCf=getTx("O_SYS_TYPE=refieldiscf&sys_tables="+aForm.elements[i].name+"&sys_tabvalue="+aForm.elements[i].value,"Menu");
				
				if(strIsCf=="ok"){
					alert(aForm.elements[i].attributes.ruleTip.value+"�Ѿ���ռ��,����������!");
					aForm.elements[i].focus();
					return false;
				}
				break;
			}
		}

	}

/**
	if(!!ylt.check){
		var bOther=ylt.check.checked();
		if(!bOther)
			return false;
	}
**/	
	aForm.action=aAction;
	var objSubBttn=$("sys_form_display_bttn");
	if(objSubBttn!=null){
		objSubBttn.style.background="#cccccc";
		objSubBttn.style.border="1px solid #c3c3c3";
		objSubBttn.innerHTML="�ύ��...";
		objSubBttn.href="#";
		
	}
	objSubBttn=$("sys_form_display_laun_bttn");
	if(objSubBttn!=null){
		objSubBttn.style.background="#cccccc";
		objSubBttn.style.border="1px solid #c3c3c3";
		objSubBttn.innerHTML="������...";
		objSubBttn.href="#";
		
	}
	//alert("ok");
	//generBg("aaa");
	return true;	
}





function checkFrom(aForm){
	var iFormSize=aForm.elements.length;
	var iRule="";
	for(var i=0;i<iFormSize;i++){
		iRule=aForm.elements[i].rule;
		if(!!!iRule)
			iRule="";
		if(iRule!=null){
			switch(iRule){
			case "bxtx":
				if(aForm.elements[i].value==""){
					alert(aForm.elements[i].ruleTip);
					aForm.elements[i].focus();
					return false;
				}
				break;
			case "bxsz":
				if (isNaN(aForm.elements[i].value)||aForm.elements[i].value==""){
					alert(aForm.elements[i].ruleTip);
					aForm.elements[i].focus();
					return false;
				}
				break;

			
			case "bxsj":
				var bIs=isSj(aForm.elements[i].value);
				if(!bIs){
					//alert(aForm.elements[i].ruleTip);
					aForm.elements[i].focus();
					return false;
				}
				break;

			case "bxdh":
				var bIs=isDh(aForm.elements[i].value);
				if(!bIs){
					//alert(aForm.elements[i].ruleTip);
					aForm.elements[i].focus();
					return false;
				}
				break;
			
			case "bxem":
				var bIs=isYx(aForm.elements[i].value);
				if(!bIs){
					//alert(aForm.elements[i].ruleTip);
					aForm.elements[i].focus();
					return false;
				}
				break;

			case "bxsf":
				if (isNaN(aForm.elements[i].value)||aForm.elements[i].value==""){
					//alert(aForm.elements[i].ruleTip);
					aForm.elements[i].focus();
					return false;
				}
				break;

			case "bxyb":
				//return zdizk();
				break;
			case "bncf":
				if(aForm.elements[i].value==""){
					alert("����д"+aForm.elements[i].ruleTip);
					return false;
				}
				break;
			}
		}

	}
	
	return true;	
}


//////////////////////////////////////////////////////////////////////////////////////////////




	
function sys_getListValues(_strListId){
	var _objList=$(_strListId);
	var iCount = _objList.length;
	var strValues="";
		for (var i = iCount - 1; i >= 0 ; i--){
			strValues+=","+_objList.options[i].value;
		}
	if(iCount>0)
		strValues=strValues.substring(1);
	return strValues;
}


function sys_listMutMove(_fromList,_toList){
	var objFromList=$(_fromList);
	var objToList=$(_toList);
	var bh;
	var count = objFromList.length;
		for (var i = 0; i < count; i++)
		if(objFromList.options[i].selected == true)
		{	
			bh = objFromList.options[i].value;
			if (sys_listHaveOption(bh,objToList)) continue;
			newoption = new Option(objFromList.options[i].text,bh,false,false);
			objToList.options[objToList.options.length] = newoption;
		}
	sys_listDel(objFromList);
}
function sys_listHaveOption(_strValue,_objToList)
{
	var count = _objToList.length;
	for (var i = 0; i < count; i++)
	{		
	
		if (_objToList.options[i].value == _strValue)
			return true;
	}
	return false;
}
function sys_listDel(_objList)
{		
	var count = _objList.length;
		for (var i = count - 1; i >= 0 ; i--){
		if(_objList.options[i].selected == true)_objList.options[i] = null;
	}
}

function sys_plaSetTables(_strWinId){
	var objCheckChilds=document.getElementsByName('syscheckbox');
	var iCheckCount=objCheckChilds.length;
	var _objTable=$('tb');
	var strSplit='';
	var strSelCodes="";
	for(var i=0;i<iCheckCount;i++){
			 if(objCheckChilds[i].checked){
				var _objRow=_objTable.rows[parseInt(objCheckChilds[i].value)+1];
				strSelCodes+=strSplit+_objRow.cells[1].innerText;
				strSplit=",";
			 }
	}	
	parent.getOpenPage(_strWinId).addTable(strSelCodes);
}
function sys_getDataWin(){
	var objWin=window;
	while(objWin!=objWin.parent)
		objWin=objWin.parent;
	return objWin;
}
function sys_setHaveSelect(_objTr, _strWid,_strViewCellIndex){
	
	var objParent=sys_getDataWin();
	var arrViewCellIndex=_strViewCellIndex.split("-");
	var objViewDiv=objParent.document.getElementById("divselect"+_strWid);
	var objCheckBox=_objTr.cells[0].childNodes[0];
	var bIsCheck=objCheckBox.checked;
	
	
	
	var iSetValueIndexCount=arrViewCellIndex.length;
	var strSelValue=_objTr.cells[parseInt(arrViewCellIndex[0])].innerText;
	var strSelName=_objTr.cells[parseInt(arrViewCellIndex[1])].innerText;
	

	var strId="sys_issel"+strSelValue;
	if(bIsCheck){
		var arrTemp=new Array();
		for(var i=0;i<iSetValueIndexCount;i++){
			arrTemp.push( _objTr.cells[parseInt(arrViewCellIndex[i])].innerText);
		}
		arrTemp.push(arrViewCellIndex[0]);
		objParent.objSelValues[_strWid][strSelValue]=arrTemp;
		
		objViewDiv.innerHTML+="<font id='"+
							   strId+
							   "' class='mutselfont'><img class='mutselimg' onclick=\"sys_delSelValues('"+
							   strId+"','"+
							   _strWid+"');\" src='images/eve/gbred.png'>"+
							   strSelName+
							   "&nbsp;&nbsp;</font>";
	}else{
		delete objParent.objSelValues[_strWid][strSelValue];

		var objViewLabel=objParent.document.getElementById(strId);
		if(objViewLabel!=null)
			objViewDiv.removeChild(objViewLabel);
	
	}
}
function sys_HaveSelect_ReView(_strWid,_strViewCellIndex){
	var objParentData=sys_getDataWin().objSelValues[_strWid];
	var objTab=document.getElementById("tb");	
	var arrRows=objTab.rows;
	var iRowCount=arrRows.length;
	var iSelDataCell=parseInt(_strViewCellIndex.split("-")[0]);
	for(var i=0;i<iRowCount;i++){
		var objRow=arrRows[i];
		var objCell=objRow.cells[iSelDataCell];
		if(objCell==null)
			continue;
		if(!!objParentData[objCell.innerText]){
			objRow.cells[0].childNodes[0].checked=true;
			objRow.className="tr1over";
		}
	}
}
function sys_getOpenSelValues(_strWid){
	var objParentData=sys_getDataWin().objSelValues[_strWid];
	if(!!window.setReturn)
		setReturn(objParentData);
	sys_getDataWin().objSelValues[_strWid]={};
}
function sys_SetOpenSinSelValuesToForm(objValues,_iIndex,_strReturn,_strWid){
	var objParentData=sys_getDataWin().objSelValues[_strWid];
	if(_iIndex==null)_iIndex=0;
	if(!!window.setReturn)
		setReturn(_iIndex,objValues);
	else{

		var arrStrReturns=_strReturn.replace(/add./g,"").split(";");
		var iReturnLength=arrStrReturns.length;
		var objOpenPage=sys_getDataWin().getOpenPage(_strWid);
		
		for(var j=0;j<iReturnLength;j++){
			if(arrStrReturns[j]=="")
				continue;
			var arrInputMsg=arrStrReturns[j].split("=");
			var strInputName=arrInputMsg[0];
			var arrElementInputs=objOpenPage.document.getElementsByName(strInputName);
			
			arrElementInputs[_iIndex].value=objValues[arrInputMsg[1]].innerText.trim();
		}
	}
	sys_getDataWin().objSelValues[_strWid]={};
}
function sys_SetOpenSelValuesToForm(_iIndex,_strReturn,_strWid){
	var objParentData=sys_getDataWin().objSelValues[_strWid];
	if(!!window.setReturn)
		setReturn(_iIndex,objParentData);
	else{
		var strSplit="";
		var arrStrReturns=_strReturn.replace(/add./g,"").split(";");
		var iReturnLength=arrStrReturns.length;
		var arrValues=new Array(iReturnLength);
		for(var j=0;j<iReturnLength;j++){
			arrValues[j]="";
		}
		for(var i in objParentData){
			for(var j=0;j<iReturnLength;j++){
				arrValues[j]=arrValues[j]+strSplit+objParentData[i][j];
			}
			strSplit=",";
		}
		var objOpenPage=sys_getDataWin().getOpenPage(_strWid);
		for(var j=0;j<iReturnLength;j++){
			var strInputName=arrStrReturns[j].split("=")[0];
			var arrElementInputs=objOpenPage.document.getElementsByName(strInputName);
			arrElementInputs[_iIndex].value=arrValues[j].trim();
		}
	}
	sys_getDataWin().objSelValues[_strWid]={};
}
function getParentByTagName(_obj,_strTagName){
	var obj=_obj.parentNode;
	if(obj.tagName==_strTagName)
		return obj;
	else
		return getParentByTagName(obj,_strTagName)
	
}
function sys_SetOpenSelValuesToMutSplitForm(_iIndex,_strReturn,_strWid){
	
	var arrStrReturns=_strReturn.replace(/add./g,"").split(";");
	var iReturnLength=arrStrReturns.length;
	var objDataWin=sys_getDataWin();
	var objOpenPage=objDataWin.getOpenPage(_strWid);
	var objParentData=objDataWin.objSelValues[_strWid];	
	var objInput=null;
	var bIsChange=false;
	
	var objHaveValues={};
	if(iReturnLength>0){
		var arrElementInputs=objOpenPage.document.getElementsByName(arrStrReturns[0].split("=")[0]);
		var iInputCount=arrElementInputs.length;
		for(var i=0;i<iInputCount;i++){
			var inputValue=arrElementInputs[i].value;
			if(inputValue!="")
				objHaveValues[inputValue]="ok";
		}
	}
	
	
	
	
		for(var i in objParentData){
			
			if(objHaveValues[i]!=null){
				continue;
			}
			var strTempName=arrStrReturns[0].split("=")[0];
			var objTempInput=objOpenPage.document.getElementsByName(strTempName)[_iIndex];
			if(objParentData[objTempInput.value]!=null)
				objInput=objTempInput;
			
			if(objInput!=null){				
				var objTr=getParentByTagName(objInput,"TR");
				var objTab=getParentByTagName(objTr,"TABLE");
				var strTBId=objTab.id.replace("sys_form_content","");
				var newTr = objTab.insertRow(objTr.rowIndex);
				
				newTr.onmouseover=function(){this.style.background='#f1f1f1';};
				newTr.onmouseout=function(){this.style.background='#ffffff';};
				newTr.innerHTML=objOpenPage.SYS_ChangeSelId(objOpenPage.$("NO_SYS_BATCH_ROW"+strTBId).value,strTBId);
				objOpenPage.SYS_InitBatchSelId(strTBId);
			}
			
			
			for(var j=0;j<iReturnLength;j++){
				var strInputName=arrStrReturns[j].split("=")[0];
				var arrElementInputs=objOpenPage.document.getElementsByName(strInputName);
				var objInput=arrElementInputs[_iIndex];
				var objData=objParentData[i];
				objInput.value=objData[j];
			}
		}
		
		
		
		
		
	
	/**	var strTBId=objTab.id.replace("sys_form_content","");
		alert(strTBId);
		newTr.onmouseover=function(){this.style.background='#f1f1f1';};
		newTr.onmouseout=function(){this.style.background='#ffffff';};
		newTr.innerHTML=SYS_ChangeSelId($("NO_SYS_BATCH_ROW"+strTBId).value,strTBId);
		SYS_InitBatchSelId(strTBId);
	}

	function SYS_INSERT_BATCH_ROW(_obj,_strTBId){
	var objTab=$("sys_form_content"+_strTBId);
	var objTr=_obj.parentNode.parentNode;
	var newTr = objTab.insertRow(objTr.rowIndex);
	newTr.onmouseover=function(){this.style.background='#f1f1f1';};
	newTr.onmouseout=function(){this.style.background='#ffffff';};
	newTr.innerHTML=SYS_ChangeSelId($("NO_SYS_BATCH_ROW"+_strTBId).value,_strTBId);
	SYS_InitBatchSelId(_strTBId);
	**/
	
}
function getCellData(_strId){
	var vResult=$(_strId);
	var iData=0;
	if(vResult!=null){
		var objData=vResult.value;
		if(objData!="")
			iData=parseInt(objData);
	}
	return iData;
}
function to(_iValue,_iNM){
	if(!!!_iValue)
		return "0";
	var strValue=_iValue+'';
	var iIndex=strValue.indexOf(".");
	if(iIndex==-1)
		return strValue;
	else{
		var strInt=strValue.substring(0,iIndex);
		var strTo=strValue.substring(iIndex);
		if(strTo.length<=_iNM)
			return strInt+strTo;
		else
			return strInt+strTo.substring(0,(_iNM+1));
	}

}
function formatData(_strValue){
	if(!!!_strValue)
		_strValue="0";
	else if(_strValue=="Infinity")
		_strValue="0";
	_strValue=_strValue+"";
	_strValue=_strValue.replace(/\ /g,"&nbsp;");
	
	return _strValue;
}
function getSumData(_strName){
	var arrData=document.getElementsByName(_strName);
	var iData=0;
	var iLenth=arrData.length;
	//alert(arrData[0]);
	for(var i=0;i<iLenth;i++){
			var objData=arrData[i].value;
			
			if(objData!="")
				iData=iData+parseFloat(objData);
		}
	return iData;
}
function getSys_CharData(_strCell,_iRow,_iCol,_bISFWConst){
	//alert("ok");
	var strCell="in"+_strCell+"_"+_iRow+"_"+_iCol;

	if(_bISFWConst){//��Χ��̬
		var arrCell=_strCell.split("$");
		strCell="in"+(parseInt(arrCell[0])+_iRow)+"$"+arrCell[1]+"_0_"+_iCol;
	}
	var iValueData="0";	
	var objData=document.getElementById(strCell);
	if(objData!=null)
		iValueData=objData.value;
	if(iValueData=="")
		iValueData="0";
	return iValueData;
}
function getCharData(strTJFW,strTJKJ,strSJMC,strTJSJ){	
	var strXmlData="<chart palette='2' caption='ͳ��ͼ' shownames='1' >";
	var strXmlFW="";
	var strXmlKJ="";
	var arrTJSJ=strTJSJ.split(",");
	var arrSJMC=strSJMC.split(",");
	var i_DATA_LEN=arrTJSJ.length;
	
	var arr_FW_DATA;
	var bISFWConst=false;
	if(strTJFW.indexOf("const.")!=-1){
		strTJFW=strTJFW.substr(6);
		arr_FW_DATA=strTJFW.split(",");
		bISFWConst=true;
	}else
		arr_FW_DATA=document.getElementsByName("in"+strTJFW);
	
	
	
	var i_FW_LEN=arr_FW_DATA.length;
	
	strXmlFW+="<categories>";
	for(var i=0;i<i_FW_LEN;i++)
		if(bISFWConst)
			strXmlFW+="<category label='"+arr_FW_DATA[i]+"' />";
		else
			strXmlFW+="<category label='"+arr_FW_DATA[i].value+"' />";
	strXmlFW+="</categories>";
	
	var arr_KJ_DATA=document.getElementsByName("in"+strTJKJ);
	var bIsKJConst=false;
	if(strTJKJ.indexOf("const.")!=-1){
		strTJKJ=strTJKJ.substr(6);
		arr_KJ_DATA=strTJKJ.split(",");
		bIsKJConst=true;
	}else
		arr_FW_DATA=document.getElementsByName("in"+strTJFW);
	
	
	
	
	//alert(arr_KJ_DATA);
	
	var i_KJ_LEN=arr_KJ_DATA.length;
	
	for(var i=0;i<i_KJ_LEN;i++){//��Χ���У��ھ�����		
		for(var k=0;k<i_DATA_LEN;k++){
			if(bIsKJConst)
				strXmlKJ+="<dataset seriesName='"+arr_KJ_DATA[i]+arrSJMC[k]+"'  showValues='0'>";
			else
				strXmlKJ+="<dataset seriesName='"+arr_KJ_DATA[i].value+arrSJMC[k]+"'  showValues='0'>";
			for(var j=0;j<i_FW_LEN;j++){//��	
				
				strXmlKJ+="<set value='"+getSys_CharData(arrTJSJ[k],j,i,bISFWConst)+"' />";
			}
			strXmlKJ+="</dataset>";
		}		
	}
	
	strXmlData+=strXmlFW+strXmlKJ+"</chart>";
	//alert(strXmlData);
	return strXmlData;
}
var objCurGraph=null;
function changeGraph(_strType){
	if(objCurGraph!=null)
		objCurGraph.style.display="none";
	else
		repframe.dragtable.style.display="none";
	if(_strType=="viewrep")
		objCurGraph=repframe.dragtable;
	else
		objCurGraph=repframe.document.getElementById('ylChart'+_strType+'Div');
	objCurGraph.style.display='';
}





var gs_upl_kc="";
/*****************************/
var gs_root="";
/****************************/
function init(aForm){
	aForm.onsubmit=function()
		{
			if(gs_upl_kc=="")
				return check(aForm,gs_root+"/YLWebAction");
			else
				return check(aForm,gs_root+"/YLWebAction?NO_UPL_KC="+gs_upl_kc);
		}
}
function initBatch(aForm,aPage,aOpt){
	aForm.onsubmit=function()
		{
			
			if(gs_upl_kc=="")
				return check(aForm,gs_root+"/YLWebAction?NO_BATCH_PAGE="+aPage+"&NO_OPTYPE="+aOpt);
			else
				return check(aForm,gs_root+"/YLWebAction?NO_UPL_KC="+gs_upl_kc+"&NO_BATCH_PAGE="+aPage+"&NO_OPTYPE="+aOpt);
		}
}

function initUpdate(aForm,aCon){
	aForm.onsubmit=function()
		{
			if(gs_upl_kc=="")
				return check(aForm,gs_root+"/YLWebAction?NO_OPTYPE=1&NO_CON="+aCon);
			else
				return check(aForm,gs_root+"/YLWebAction?NO_UPL_KC="+gs_upl_kc+"&NO_OPTYPE=1&NO_CON="+aCon);
		}

}
function initOther(aForm,aOpt){
	aForm.onsubmit=function()
		{
			if(gs_upl_kc=="")
				return check(aForm,gs_root+"/YLWebAction?NO_OPTYPE="+aOpt);
			else
				return check(aForm,gs_root+"/YLWebAction?NO_UPL_KC="+gs_upl_kc+"&NO_OPTYPE="+aOpt);
		}

}




function initForm(aForm){
	aForm.onsubmit=function()
		{
		
			return checkFrom(aForm);
		}
}




var iQueryOldHeight=-1;
function initPage(){
	  SetGrid("100%",300);
}

var  bHaveDrag=false;
var  bIsStartDrag=false;
var objCurTh=null;
var iCurXPos=0;
var iCurThWidth=0;
var bIsDraging=false;

function doMouseClick(){
	var objEvent=event.srcElement;
	if(objEvent.className=="th1over"){
		objEvent.childNodes[1].innerHTML="<font color='white'>&nbsp;&nbsp;����</font>";
	}
}

function doMouseOver(){
	var objEvent=event.srcElement;
	if(objEvent.className=="th1"){
		objEvent.className="th1over";
	}
}
function doMouseOut(){
	var objEvent=event.srcElement;
	if(objEvent.className=="th1over")
		objEvent.className="th1";
}
function doMouseDown(){
	var objEvent=event.srcElement;
	if(bHaveDrag){
		bIsStartDrag=true;
		objCurTh=objEvent;
		iCurXPos=event.x;
		//iCurThWidth=objCurTh.offsetWidth;
		iCurThWidth=parseInt(objCurTh.style.width.replace("px",""));
		document.body.onselectstart =function(){return false;};
	}
}
function doMouseUp(){
	 bHaveDrag=false;
	bIsStartDrag=false;
	objCurTh=null;
	iCurXPos=0;
	iCurThWidth=0;
	bIsDraging=false;
	document.body.style.cursor='';
	document.body.onselectstart =function(){return true;};
}
function doMouseMove(){
	if(bIsStartDrag){
		var iCurWidth=iCurThWidth+event.x-iCurXPos;
		if(iCurWidth<0)
			iCurWidth=0;
		objCurTh.style.width=iCurWidth;
		bIsDraging=true;
		}
	var objEvent=event.srcElement;
	if(objEvent.className=="th1over"){
		var ox=event.offsetX+5;
		var offset_x =event.srcElement.offsetWidth;
		if(offset_x<ox||bIsDraging){
			document.body.style.cursor='col-resize';
			bHaveDrag=true;
		}else
			document.body.style.cursor='';
		
		}
}
function treeRedirect(_objNode,_strUrl){
	parent.lxmain.location=_strUrl;

}

function sysGetAttr(_obj,_strAttr){
	return _obj.attributes[_strAttr].value;
}
function sysSetAttr(_obj,_strAttr,_strValue){
	_obj.attributes[_strAttr].value=_strValue;
}
var sys_iStrCurPageIndex=0;
function doPageClick(_obj,_objCom,_iIndex){
	var objOldPage=$(_objCom.id+"pg_"+sysGetAttr(_objCom,'icurtarget'));

	$(_objCom.id+"class_"+sysGetAttr(_objCom,'icurtarget')+"_rt").className='sys_tabs_right';//ԭѡ��ҳ�ұ߷��
	$(_objCom.id+"class_"+sysGetAttr(_objCom,'icurtarget')+"_lt").className='sys_tabs_left';//ԭѡ��ҳ�ұ߷��
	$(_objCom.id+"class_"+_iIndex+"_rt").className='sys_tabs_right_current';//ԭѡ��ҳ�ұ߷��
	$(_objCom.id+"class_"+_iIndex+"_lt").className='sys_tabs_left_current';//ԭѡ��ҳ�ұ߷��
	_obj.className='tabbed-pane-tab current';
	objOldPage.style.display="none";
	var objCurPage=$(_objCom.id+"pg_"+_iIndex);
	objCurPage.style.display="";
	sysSetAttr(_objCom,'icurtarget',_iIndex);
	
	var objFrame=$(_objCom.id+"pgfm_"+_iIndex);
	if(objFrame!=null)
		if(objFrame.autor==null){
			objFrame.autor="yulongtao";
			window.frames[_objCom.id+"pgfm_"+_iIndex].location.reload();
			//document.frames(_objCom.id+"pgfm_"+_iIndex).initPage();
		}
	
	sys_iStrCurPageIndex=_iIndex;
}
function sys_init_tabs_size(_objTabs){
	var iCurBodyWidth=document.body.clientWidth;
	var iCurBodyHeight=document.body.clientHeight;
	var objTabsContent=$(_objTabs.id+"_content");
	objTabsContent.style.height=(_objTabs.clientHeight-30)+"px";
}
function sys_extends_parent_size(_objFrame){
	var objChildForm=$("div_childform");
	if(objChildForm!=null)
		_objFrame.style.height=(objChildForm.offsetHeight-35)+"px";
	else
		_objFrame.style.height=_objFrame.parentNode.offsetHeight+"px";
}


function doTableClickTree(_objRows,_strId,strStyle){
	var iRows=_objRows.length;
	for(var i=0;i<iRows;i++){
		
		var objAttrParCode=_objRows[i].attributes.parcode;
		if(objAttrParCode==null)
			continue;
		if(objAttrParCode.value==_strId.substring(2)){
			_objRows[i].style.display=strStyle;
			doTableClickTree(_objRows,_objRows[i].id,strStyle);
		}
	}
}
function tableTreeClick(_obj){
	var objcurTr=_obj.parentElement.parentElement;
	var objTable=objcurTr.parentElement.parentElement;
	var objRows=objTable.rows;
	var objAttrTrIsClose=objcurTr.attributes.isClose;
	var strAttrIsClose="false";
	if(objAttrTrIsClose!=null)
		strAttrIsClose=objAttrTrIsClose.value;
	else
		objcurTr.setAttribute("isClose","false");
	var strStyle="";
		if(strAttrIsClose=="true"){
			strStyle="";
			objcurTr.attributes["isClose"].value="false";
		}else{
			strStyle="none";
			objcurTr.attributes["isClose"].value="true";
			}
	doTableClickTree(objRows,objcurTr.id,strStyle);
	
	
}

function selAllCheckBox(_objCheckMain,_strCheckChildName){
	
	var objCheckChilds=document.getElementsByName(_strCheckChildName);
	var iCheckCount=objCheckChilds.length;
	for(var i=0;i<iCheckCount;i++){
		objCheckChilds[i].checked=_objCheckMain.checked;
		
		var objCheckRow=objCheckChilds[i].parentElement.parentElement;
		
		if(objCheckChilds[i].checked){
			if(!!!objCheckRow.sys_cur_query_trclass)
				objCheckRow.sys_cur_query_trclass=objCheckRow.className;
			objCheckRow.className="tr1select";
		}else{
			objCheckRow.className=objCheckRow.sys_cur_query_trclass;
		}
		
		
		
		
	}
}

function delSysAll(){
	if(confirm("ȷ��Ҫɾ����ѡ������")){
	var objCheckChilds=document.getElementsByName("syscheckbox");
	var strDelParam="";
		var iCheckCount=objCheckChilds.length;
		for(var i=0;i<iCheckCount;i++){
			if(objCheckChilds[i].checked){
				strDelParam=$("sysdelvalue"+objCheckChilds[i].value).value;
				getTx(strDelParam,"YLDel");
			}
		}
		if(strDelParam.indexOf("NO_flush=true")!=-1)
			parent.lxleft.yltTree.prototype.fushTree('testtree0');
		location.reload();
	}
}

function sys_synVScroll(){
	var iScrollTop=vbar.scrollTop+25;
		if(iScrollTop<=25)
			iScrollTop=0;
		$("tablediv").scrollTop=iScrollTop;
}

 function SetGrid(awidth,aheight){

	var queryTable=$("tb");
	var iCurBodyWidth=document.body.clientWidth;
	var iCurBodyHeight=document.body.clientHeight;
	var objOpPanelTable=$("sysbttoparea");
	var objFormPanelTable=$("sys_form_opbttn");
	var objFormWinTable=$("sys_form_frame");
	var objFormWinDiv=$("sysformwindiv");
		
	if(objFormWinTable!=null){
			objFormWinTable.style.width=iCurBodyWidth+"px";
	}
	if(objFormWinDiv!=null){
		var objFormSizeDiv=$("sys_form_size_panel");
		if(objFormSizeDiv!=null)
			objFormWinDiv.style.height=objFormSizeDiv.style.height;
		else{
			if($("sys_form_flow_panel")==null)
				objFormWinDiv.style.height=(iCurBodyHeight-32)+"px";
			}
	}

	if(objFormPanelTable!=null)
		objFormPanelTable.style.width=iCurBodyWidth+"px";
	if(queryTable!=null){
		var queryTableTitle=$("tbtitle");
		var queryTableDiv=$("tablediv");
		var queryTableTitleDiv=$("titlediv");
		var objSplit=$("fytable");
		
		
		var iSplit=0;
		var iScroll=0;
		if(objSplit!=null)//�з�ҳ
			iSplit=30;
		//�ȷ���	
			if(queryTable.width.indexOf("%")==-1){
			if(parseFloat(queryTable.width)<queryTableDiv.clientWidth){
				queryTable.width="100%";	
				queryTableTitle.width="100%";
			}
		}
		iCurBodyWidth=document.body.clientWidth;
		
		if(queryTable.offsetWidth>iCurBodyWidth)//�к��������
			iScroll=8;
			
		var iQueryTop=queryTableDiv.offsetTop;
		var iOldHeight=queryTable.offsetHeight+iScroll;
		var iNewHeight=document.body.clientHeight-iQueryTop-iSplit;
		
		//iNewHeight=iNewHeight-5;////////��ʱ
		
		//if(iNewHeight>iOldHeight)
		//	iNewHeight=iOldHeight;
		//queryTableDiv.style.backgroundColor='red';

		queryTableDiv.style.height=iNewHeight+"px";
	
		vbar.childNodes[0].style.height =(queryTable.offsetHeight+queryTableTitleDiv.offsetHeight)+"px";
		vbar.style.height=(iNewHeight+35)+"px";
		
		iCurBodyWidth=document.body.clientWidth;
		
		if(vbar.offsetHeight>=vbar.childNodes[0].offsetHeight){
			vbartd.style.display="none";
		}else
			iCurBodyWidth=iCurBodyWidth-18;
			
	
		queryTableTitleDiv.style.width=queryTableDiv.style.width=(iCurBodyWidth-15)+"px";
		
	
		//queryTableDiv.style.border="1px solid red";
		//alert(queryTableDiv.style.width);
		//��ǰ����
		if(queryTable.width.indexOf("%")==-1){
			if(parseFloat(queryTable.width)<queryTableDiv.clientWidth){
				queryTable.width="100%";	
				queryTableTitle.width="100%";
			}
		}
		
		 
		document.body.onmousemove=sys_columDragMove;
		document.body.onmouseup=sys_columDragUp;
	}
  }  

   ////////////////======================
   
   function delSelAllSelect(_objRow){
	
	var objCheckChilds=document.getElementsByName("syscheckbox");
	var iCheckCount=objCheckChilds.length;
	for(var i=0;i<iCheckCount;i++){
		var objCheckRow=objCheckChilds[i].parentElement.parentElement;
		//alert(!!!objCheckRow.sys_cur_query_trclass);
			if(!!objCheckRow.sys_cur_query_trclass)
				objCheckRow.className=objCheckRow.sys_cur_query_trclass;
		if(_objRow!=objCheckRow)
			objCheckChilds[i].checked=false;
	}
}
 
  function sysTableRowClick(_objRow,_event){
	_event=_event||window.event;
	var objSrcElement=_event.srcElement||_event.target;
	if(objSrcElement.name=="syscheckbox")
		return;
	var objCurCheckBox=_objRow.cells[0].childNodes[0];
	var bIsCheckBox=false;
	if(objCurCheckBox!=null&&objCurCheckBox.type!=null&&objCurCheckBox.type=="checkbox")
		bIsCheckBox=true;
 
	if(bIsCheckBox){
		if(!sys_Ctrl_IS_DOWN){
				delSelAllSelect(_objRow);
		}
		objCurCheckBox.checked=!objCurCheckBox.checked;
		if(objCurCheckBox.checked){
			if(!!!_objRow.sys_cur_query_trclass)
				_objRow.sys_cur_query_trclass=_objRow.className;
			_objRow.className="tr1select";
			
		}else
			_objRow.className=_objRow.sys_cur_query_trclass;
	
	}
  }
 
 function synTitle(obj){
     $("titlediv").scrollLeft=obj.scrollLeft;
  } 
  function sys_getCurSelRow(){
	var arrCurSelRow=new Array();
	var objCheckChilds=document.getElementsByName("syscheckbox");
		var iCheckCount=objCheckChilds.length;
		for(var i=0;i<iCheckCount;i++){
			if(objCheckChilds[i].checked){
				arrCurSelRow[arrCurSelRow.length]=objCheckChilds[i].parentElement.parentElement;
				}
		}
		return arrCurSelRow;
  }
  function sys_addMod(){
    var arrCurSelRow=sys_getCurSelRow();
	if(arrCurSelRow.length>0)
		if(arrCurSelRow.length>1)
			alert("���ֻ��ѡ��һ��ģ�飡");
		else{
			miniWin('����ģ��','','ModMain?STROPTYPE=1&SMODCODE='+arrCurSelRow[0].cells[2].innerText,700,200,'','');
			}
	else
		miniWin('����ģ��','','ModMain?STROPTYPE=1&SMODCODE=',700,200,'','');
  } 
  function sys_getCurSelCheckBox(){
	var arrCurSelCheckBox=new Array();
	var objCheckChilds=document.getElementsByName("syscheckbox");
		var iCheckCount=objCheckChilds.length;
		for(var i=0;i<iCheckCount;i++){
			if(objCheckChilds[i].checked){
				arrCurSelCheckBox[arrCurSelCheckBox.length]=objCheckChilds[i];
				}
		}
		return arrCurSelCheckBox;
  }
  function sys_EditForm(_iWidth,_iHeight){
    var arrCurSelRow=sys_getCurSelCheckBox();
	if(arrCurSelRow.length>0)
		if(arrCurSelRow.length>1)
			alert("���ֻ��ѡ��һ����¼�����޸ģ�");
		else{
			var strUrl=$("syseditvalue"+arrCurSelRow[0].value).value;
			miniWin('�޸���Ϣ','',strUrl,_iWidth,_iHeight,'','');
			}
	else
		alert("��ѡ��һ���޸��");
  }
function sys_CopyForm(_iWidth,_iHeight){
    var arrCurSelRow=sys_getCurSelCheckBox();
	if(arrCurSelRow.length>0)
		if(arrCurSelRow.length>1)
			alert("���ֻ��ѡ��һ����¼���и��ƣ�");
		else{
			var strUrl=$("syseditvalue"+arrCurSelRow[0].value).value+"&sys_copy=true";
			miniWin('������Ϣ','',strUrl,_iWidth,_iHeight,'','');
			}
	else
		alert("��ѡ��һ�������");
  }  
  var isMSIE = (navigator.appName == "Microsoft Internet Explorer");
  function sys_columStatus(e,obj){
		var px = navigator.userAgent.indexOf('Firefox')<0?e.offsetX:e.layerX-obj.offsetLeft;
		if(px>obj.offsetWidth-8 && px<obj.offsetWidth){
			obj.style.cursor = "col-resize";
		}else{
			obj.style.cursor = "default";
		}
	}
	
	
	
	
	
	
	
	
	
var obj_sys_curDragTh=null;
var obj_sys_curSortCol=null;	
var i_sys_curEventX=0;
var obj_sys_CurTdWidth=0;
var isys_curTableWidth=0;


function sys_setCookie(_strName,_strValue){
		var Days = 30;
		var exp = new Date();
		exp.setTime(exp.getTime() + Days*24*60*60*1000);
		document.cookie = _strName+"="+_strValue+";expires=" + exp.toGMTString();
	}


var sys_Cur_Change_Size_Field="";
var sys_Cur_Select_th_col=null;
var sys_Cur_Select_th_col_class="th1";
var sys_cur_Select_th_msg=null;

function sys_columDown(e,obj,_strCode){
		//var px = isMSIE?e.offsetX:e.layerX-obj.offsetLeft;
		var px = navigator.userAgent.indexOf('Firefox')<0?e.offsetX:e.layerX-obj.offsetLeft;
		if(px>obj.offsetWidth-8 && px<obj.offsetWidth){
			e=e||window.event;
			obj=obj||this;
			
			isys_curTableWidth=tb.offsetWidth;
			
			i_sys_curEventX = e.clientX;
			obj_sys_CurTdWidth = obj.offsetWidth;
			obj_sys_curDragTh = obj;
			sys_Cur_Change_Size_Field=_strCode;
			if(obj.setCapture){
				obj.setCapture();
			}else{
				e.preventDefault();
			}
		}else{
			e=e||window.event;
			if(_strCode=="NOOP")
				return;
		
			if(e.button==2)
				return;
		
			var objSrcElement=e.srcElement||e.target;
			var objSortType=objSrcElement.attributes.sorttype;	
			if(objSortType!=null){
				sys_do_query_sort(_strCode,objSortType.value);
				return;
			}
			if(sys_Cur_Select_th_col!=null)
				sys_Cur_Select_th_col.className=sys_Cur_Select_th_col_class;
			sys_Cur_Select_th_col_class=obj.className;
			obj.className="th1over";
			sys_Cur_Select_th_col=obj;
			
			var objCurQueryTableRows=$("tb").rows;
			//alert(objCurQueryTableRows[3].cells[obj.cellIndex].innerHTML);
			
			
			
			destroElement();
			sys_query_select_div.innerHTML="";
			var objTrigerTd=objCurQueryTableRows[1].cells[obj.cellIndex];
			i_sys_msd_status=objTrigerTd.cellIndex;
			i_sys_msd_cells_index=i_sys_msd_status;
			sys_query_select_div.style.display="none";
			i_sys_msd_sel_top=objTrigerTd.offsetTop;
			sys_query_select_div.style.top=i_sys_msd_sel_top+"px";
			sys_query_select_div.style.left=objTrigerTd.offsetLeft+"px";
			sys_query_select_div.style.width=objTrigerTd.clientWidth+"px";
			i_sys_msd_start_row=1;
			i_sys_msd_end_row=objCurQueryTableRows.length-1;
			var objCurQueryTableCell=objCurQueryTableRows[i_sys_msd_end_row].cells[obj.cellIndex];
			var iCurQueryDivHeight=objCurQueryTableCell.offsetTop+objCurQueryTableCell.clientHeight-i_sys_msd_sel_top;
			
			sys_query_select_div.style.height=iCurQueryDivHeight+"px";
			sys_query_select_div.style.display="";
			 $_sys_sel_tip();
			 if(sys_cur_Select_th_msg!=null)
				sys_cur_Select_th_msg.innerHTML="";
			sys_cur_Select_th_msg=obj.childNodes[0].childNodes[1];
			sys_cur_Select_th_msg.innerHTML = "<span sorttype='ASC' onmouseover=\"this.style.color='red';\" onmouseout=\"this.style.color='white';\">&nbsp;��</span>&nbsp;&nbsp;<span sorttype='DESC' onmouseover=\"this.style.color='red';\" onmouseout=\"this.style.color='white';\">&nbsp;��</span>";
			
			/**
			if(obj.childNodes[0].childNodes[1]!=null){
			
			
			if(obj_sys_curSortCol!=null)
				obj_sys_curSortCol.childNodes[1].innerHTML = "";
			if(obj.getAttribute("sort")==null){
				obj.setAttribute("sort",0);
			}
			
			var sort = obj.getAttribute("sort");
			if(sort==1){//����
				sys_columSort(obj,true);
				obj.setAttribute("sort",0);
				sys_form_fiter_con.NO_SORT_CONDITION.value=" ORDER BY "+_strCode+" ASC";
			}else{//����
				sys_columSort(obj,false);
				obj.setAttribute("sort",1);
				sys_form_fiter_con.NO_SORT_CONDITION.value=" ORDER BY "+_strCode+ " DESC";
			}
			obj_sys_curSortCol=obj.childNodes[0];
			//alert(sys_form_fiter_con.NO_SORT_CONDITION.value);
			sys_setCookie(spagecode+"_NO_SORT_CONDITION",encodeURIComponent(sys_form_fiter_con.NO_SORT_CONDITION.value));
			
			sys_form_fiter_con.submit();
			}
			**/
		}
	}
	function sys_do_query_sort(_strCode,_strSort){
		sys_form_fiter_con.NO_SORT_CONDITION.value=" ORDER BY "+_strCode+" "+_strSort;
		//sys_setCookie(spagecode+"_NO_SORT_CONDITION",encodeURIComponent(sys_form_fiter_con.NO_SORT_CONDITION.value));
		sys_form_fiter_con.submit();
	}
	function sys_columSort(obj,asc){		
		if(asc){
			obj.childNodes[0].childNodes[1].innerHTML = "&nbsp;��";
		}else{
			obj.childNodes[0].childNodes[1].innerHTML = "&nbsp;��";
		}
	}
	
	function sys_getCookie(_strName) { 
		var arr,reg=new RegExp("(^| )"+_strName+"=([^;]*)(;|$)");
		if(arr=document.cookie.match(reg))
			return unescape(arr[2]); 
		else 
			return null; 
	}

/////////////////////////�϶����	
  function sys_columDragUp(e){
		if(obj_sys_curDragTh!=null){
			if(obj_sys_curDragTh.releaseCapture){
				obj_sys_curDragTh.releaseCapture();
			}
			var strWidthMsg=sys_Cur_Change_Size_Field+"="+obj_sys_curDragTh.clientWidth;
			var strCookieColName=spagecode+"_NO_PAGE_COL_WIDTH";
			var strPageColWidth=sys_getCookie(strCookieColName);
			if(strPageColWidth==null)
				sys_setCookie(strCookieColName,encodeURIComponent(strWidthMsg));
			else{
				strPageColWidth=decodeURIComponent(strPageColWidth);
				var iIndex=strPageColWidth.indexOf(sys_Cur_Change_Size_Field);
				if(iIndex!=-1){
					var strParamLength=strPageColWidth.length;
					var strTemp=strPageColWidth.substring(iIndex,strParamLength);
					var iEndIndex=strTemp.indexOf(":");

					if(iEndIndex==-1)
						iEndIndex=strTemp.length;
	
					//alert(strPageColWidth.substring(0,iIndex)+strWidthMsg+strPageColWidth.substring(iEndIndex,strParamLength));
					
					sys_setCookie(strCookieColName,encodeURIComponent(strPageColWidth.substring(0,iIndex)+strWidthMsg+strTemp.substring(iEndIndex,strTemp.length)));
				}else
					sys_setCookie(strCookieColName,encodeURIComponent(strPageColWidth+":"+strWidthMsg));
				
			}
			//alert(decodeURIComponent(sys_getCookie(strCookieColName)));
			i_sys_curEventX = 0;
			obj_sys_CurTdWidth = 0;
			obj_sys_curDragTh = null;
			 SetGrid("100%",300);
			 
		}
	}
  function sys_columDragMove(e){
		if(obj_sys_curDragTh!=null){
			e=e||window.event;
			var newwidth = obj_sys_CurTdWidth-(i_sys_curEventX-e.clientX);
			if(newwidth>5){
				obj_sys_curDragTh.style.width = newwidth+"px";
				tb.rows[0].cells[obj_sys_curDragTh.cellIndex].style.width = newwidth+"px";
				var iDivCaption=newwidth-15;
				if(iDivCaption<0)iDivCaption=0;
				obj_sys_curDragTh.childNodes[0].style.width=iDivCaption+"px";
				//tb.rows[0].cells[obj_sys_curDragTh.cellIndex].childNodes[0].style.width = newwidth;
			}else{
				//alert(newwidth);
				obj_sys_curDragTh.style.width = "5px";
				obj_sys_curDragTh.childNodes[0].style.width="3px";
				tb.rows[0].cells[obj_sys_curDragTh.cellIndex].style.width = "5px";
				//tb.rows[0].cells[obj_sys_curDragTh.cellIndex].childNodes[0].style.width = 5;
			}
		}
	}




var arrObjEvent=new Array();
function addInitEvent(_objFun){
	arrObjEvent[arrObjEvent.length]=_objFun;
}
function loadpageEvent(){
	//init_Sys_Date();
	initPage();
	yltTree.prototype.initTree();
	yltSelect.init();
	var iLength=arrObjEvent.length;
	for(var i=0;i<iLength;i++)
		arrObjEvent[i]();
}
function login(_strUser,_strPassword,_strUrl){
	var strVer="";
	if($("ver")!=null)
		strVer=ver.value;
	var param="user="+_strUser+"&password="+_strPassword+"&ver="+strVer;
	
	if($('ckjzmm').checked)
		param+="&jzmm=true";
	else
		param+="&jzmm=false";
	
	
	var strMsg=getTx(param,"Menu?O_SYS_TYPE=login");

	if(strMsg=="OK"){
			window.location.href=_strUrl+"?width="+iScreen_Width+"&height="+iScreen_Height;
		}else
		{
			//imgcode.src='DisplayImage?id=a'+Math.random();
			eval(strMsg);
		}
}
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
function $(_strId){
		return document.getElementById(_strId);
	}
function $p(_strId){
		return document.getElementById(_strId+"_panel");
	}
function $$(_strId){
		return $(_strId+"_$");
	}
function $$$(_strId){
		return $(_strId+"_$$");
	}
function generElement(_strTaget,_strId,_iWidth,_iHeight){
		var bgObj=document.createElement(_strTaget);		
		bgObj.setAttribute('id',_strId);
		if(_iWidth!=0){
			if((_iWidth+"").indexOf("%")!=-1)
				bgObj.style.width=_iWidth;
			else
				bgObj.style.width=_iWidth+"px";
		}
		if(_iHeight!=0)
			bgObj.style.height=_iHeight+"px";
		return bgObj;
	}
function getElementsByClassName(node,classname) {
		if (node.getElementsByClassName) {
			return node.getElementsByClassName(classname);
		} else {
			return (function getElementsByClass(searchClass,node) {
						if ( node == null )
							node = document;
						var classElements = [],
						els = node.getElementsByTagName("INPUT"),
						elsLen = els.length,
						pattern = new RegExp("(^|\\s)"+searchClass+"(\\s|$)"), i, j;

						for (i = 0, j = 0; i < elsLen; i++) {
							if ( pattern.test(els[i].className) ) {
								classElements[j] = els[i];
								j++;
							}
						}
					return classElements;
    })(classname, node);
		}
	}
	
function $_dbl(_obj){
	var objDoFun=yltPub["DBL_"+spagecode];
	if(!!objDoFun){
		_obj.className="trDBL";
		objDoFun(_obj.childNodes,_obj.rowIndex);
	}
}


var objOldAddRow=null;
function generNextRow(_strAddRowId,_iRowMumber,_iCols,_strContent,_strUrl){		
		_iRowMumber++;
		if(objOldAddRow!=null&&objOldAddRow.id!=_strAddRowId)
			objOldAddRow.style.display="none";
		var objAddRow=$(_strAddRowId);
		//alert(objAddRow);
if(objAddRow==null){		
		var newTr= $("tb").insertRow(_iRowMumber);var newTd = newTr.insertCell();
		newTr.id=_strAddRowId;
		newTr.className="tr1";
		newTd.colSpan=_iCols;
		if(_strContent!=""){
			newTd.innerHTML=_strContent;
		}else{
			newTd.innerHTML="<iframe name='frmaeadd"+_strAddRowId+"' frameborder='no' border='0' marginwidth='0' marginheight='0' width='100%' height='300'  src='"+_strUrl+"'></iframe>";
		}
		objOldAddRow=newTr;
}else{
	if(objAddRow.style.display=="none"){
		objAddRow.style.display="";
		window.frames["frmaeadd"+_strAddRowId].location.reload();
		//frmaeadd.myChart.render("chartdiv");
	}else
		objAddRow.style.display="none";
		objOldAddRow=objAddRow;
}	
}


function SetWinHeight(obj) 
{ 
var win=obj; 
if (document.getElementById) 
{ 
if (win && !window.opera) 
{ 
if (win.contentDocument && win.contentDocument.body.offsetHeight) 




win.height = win.contentDocument.body.offsetHeight; 
else if(win.Document && win.Document.body.scrollHeight) 
win.height = win.Document.body.scrollHeight; 
loadpageEvent();
} 
} 
}

var sys_singer_col_fiter_Cur_Field="";
function sys_rClick_queryhead(_event,_ojbTh,_StrFilterField,_iFilterFieldIndex){
	
	var strFiterFrameUrl=window.location+"&NO_FILTER_FIELD="+_StrFilterField+"&NO_FILTER_FIELD_INDEX="+_iFilterFieldIndex;
	if(strFiterFrameUrl.indexOf("SPAGECODE")==-1)
		strFiterFrameUrl+=sys_StrFiterParam;
	//sys_frame_singer_col.location=strFiterFrameUrl;
	_event=_event||window.event;
	query_list_pop_menu.style.width='200px';
	query_list_pop_menu.style.left=_event.clientX+"px";
	query_list_pop_menu.style.top=_event.clientY+"px";
	query_list_pop_menu.style.display="";
	sys_singer_col_fiter_Cur_Field=_StrFilterField;
	
	sys_form_fiter_con_view_col.action=strFiterFrameUrl;
	//alert(strFiterFrameUrl);
	sys_form_fiter_con_view_col.submit();
	
	return false;
}
function sys_singer_col_fiter_queryAll(_obj){
	var objCheckChilds=document.getElementsByName('syscheckbox');
	var iCheckCount=objCheckChilds.length;
	for(var i=0;i<iCheckCount;i++){
			 objCheckChilds[i].checked=_obj.checked;
	}	
}
function sys_singer_col_fiter_query(){
	var objCheckChilds=sys_frame_singer_col.document.getElementsByName('syscheckbox');
	var iCheckCount=objCheckChilds.length;
	var strSplit='';
	var strSelCodes="";
	var bIsCheck=false;
	var bIsAllCheck=true;
	for(var i=0;i<iCheckCount;i++){
			 if(objCheckChilds[i].checked){
				strSelCodes+=strSplit+objCheckChilds[i].value;
				strSplit="','";
				bIsCheck=true;
			 }else
				bIsAllCheck=false;
	}	
	if(bIsCheck){
		strSelCodes=" "+sys_singer_col_fiter_Cur_Field+ " in('"+strSelCodes+"')";
		var strCurFiterCondition=sys_form_fiter_con.NO_FITER_CONDITION.value;
		if(strCurFiterCondition==""){
			if(bIsAllCheck)
				return;
			else
				sys_form_fiter_con.NO_FITER_CONDITION.value=strSelCodes;
		}else{
			var strCurDoFiterCondition=sys_singer_col_fiter_Cur_Field+" in(";
			if(strCurFiterCondition.indexOf(strCurDoFiterCondition)!=-1){
				var arrStrFiterValue=strCurFiterCondition.split(" and ");
				var iConditionFieldCount=arrStrFiterValue.length;
				for(var j=0;j<iConditionFieldCount;j++){
					//alert(arrStrFiterValue[j].indexOf(strCurDoFiterCondition));
					if(arrStrFiterValue[j].indexOf(strCurDoFiterCondition)!=-1){
						if(bIsAllCheck){//ȫѡ
							sys_form_fiter_con.NO_FITER_CONDITION.value=sys_form_fiter_con.NO_FITER_CONDITION.value.replace(" and "+arrStrFiterValue[j],"");
							sys_form_fiter_con.NO_FITER_CONDITION.value=sys_form_fiter_con.NO_FITER_CONDITION.value.replace(arrStrFiterValue[j],"");
							if(sys_form_fiter_con.NO_FITER_CONDITION.value.substring(0,5)==" and ")
								sys_form_fiter_con.NO_FITER_CONDITION.value=sys_form_fiter_con.NO_FITER_CONDITION.value.substring(5);
						}else
							sys_form_fiter_con.NO_FITER_CONDITION.value=sys_form_fiter_con.NO_FITER_CONDITION.value.replace(arrStrFiterValue[j],strSelCodes);
						}
				}
			}else
				if(bIsAllCheck)
					return;
				else
					sys_form_fiter_con.NO_FITER_CONDITION.value+=" and "+strSelCodes;
			}
	}else
		sys_form_fiter_con.NO_FITER_CONDITION.value="";
	sys_form_fiter_con.submit();
	
	
	
}

//////////==========

function sys_singer_col_fiter_query_Clear(){
	sys_form_fiter_con.NO_FITER_CONDITION.value="";
	sys_form_fiter_con.submit();	
}
function sys_spit_mouseover(_obj,_iType){
	switch(_iType){
		case 1:
			_obj.src="images/split/syo.png";
			break;
		case 2:
			_obj.src="images/split/wyo.png";
			break;
		case 3:
			_obj.src="images/split/syyo.png";
			break;
		case 4:
			_obj.src="images/split/xyyo.png";
			break;
	}
}
function sys_spit_mouseout(_obj,_iType){
	switch(_iType){
		case 1:
			_obj.src="images/split/sy.png";
			break;
		case 2:
			_obj.src="images/split/wy.png";
			break;
		case 3:
			_obj.src="images/split/syy.png";
			break;
		case 4:
			_obj.src="images/split/xyy.png";
			break;
	}
}


function sytScrollGL(_e){
	var e=_e||window.event;
		if(e.wheelDelta<=0 || e.detail>0){
			vbar.scrollTop += 18;
		}else {
			vbar.scrollTop -= 18;
		}
}

var sys_iAddConRowIndex=1;
function sys_AddCondition(_obj){
	var strCurSelValue=_obj.value;
	var iRowCount=sys_tabop.rows.length;
	
	if(strCurSelValue!=')'){
		var iCurRowIndex=_obj.parentElement.parentElement.rowIndex+1;
		if(iCurRowIndex==iRowCount){
			var objNewRow=sys_tabop.insertRow();
			sys_iAddConRowIndex++;
			objNewRow.className='tr1';
			objNewRow.setAttribute("strFieldIndex",sys_iAddConRowIndex);
			sys_generCell(objNewRow,"kh"+sys_iAddConRowIndex,add.sys_kuohaodata);//����	
			sys_generCell(objNewRow,"zd"+sys_iAddConRowIndex,add.sys_fielddata);//�ֶ�
			sys_generCell(objNewRow,"tj"+sys_iAddConRowIndex,add.sys_tiaojiandata);//����
			objNewCell=objNewRow.insertCell();
			objNewCell.className='td1';
			objNewCell.innerHTML="<input type='text' id='tjz"+sys_iAddConRowIndex+"' name='tjz"+sys_iAddConRowIndex+
			"' onclick='sys_doConditionSelValue(this);' style='width:160px;'><input id='tjzdate"+sys_iAddConRowIndex+"'  type='hidden'>";//����ֵ
			sys_generCell(objNewRow,"ljf"+sys_iAddConRowIndex,add.sys_luojifudata);//�߼���
			objNewCell=objNewRow.insertCell();
			objNewCell.className='td1';
			objNewCell.innerHTML="<img src='images/eve/rgb.png?v=1' style='cursor:hand;' onclick='sys_del_row(this);'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+
			"<img src='images/eve/xzrq.png' onclick=\"WdatePicker({el:'tjzdate"+sys_iAddConRowIndex+"',onpicked:function() {$dp.$('tjz"+sys_iAddConRowIndex+"').value=$dp.cal.getDateStr();}})\">";
			sys_doConditionSel($('zd'+sys_iAddConRowIndex));
		}
	}
}
function sys_doMutSetConValue(_obj){
	if(event.srcElement.tagName=="INPUT")
		return;
	var objCheckBox=_obj.cells[0].childNodes[0];
	objCheckBox.checked=!objCheckBox.checked;
}
function sys_do_dic_mutSel(_strV0,_strV1){
	var objTab=$("sys_dic_mutSelTab");
	var iRows=sys_dic_mutSelTab.rows.length;
	var strCods="";
	var strText="";
	var strSplit="";
	for(var i=1;i<iRows;i++){
		if(objTab.rows[i].cells[0].childNodes[0].checked){
			strCods+=strSplit+objTab.rows[i].cells[1].innerHTML;
			strText+=strSplit+objTab.rows[i].cells[2].innerHTML;
			strSplit=",";
		}
	}
	 var objParentWin = parent.getOpenPage(strWinId);
    objParentWin.$(_strV0).value = strText;
    objParentWin.$(_strV1).value = strCods;
    closeWin();
}
function sys_queryFilter(_i){
	var objDicTable=$("sys_dic_mutSelTab");
	var iRows=sys_dic_mutSelTab.rows.length;
	var strCodeValue=$("qinputcode").value.toUpperCase();
	var strNameValue=$("qinputname").value.toUpperCase();

	for(var i=2;i<iRows;i++){
		var strCode=objDicTable.rows[i].cells[_i+0].innerHTML.toUpperCase();
		var strName=objDicTable.rows[i].cells[_i+1].innerHTML.toUpperCase();
		if(strCodeValue!=""||strNameValue!=""){
			if(strCodeValue!=""&&strCode.indexOf(strCodeValue)!=-1){
				objDicTable.rows[i].style.display="";
			}else{
				if(strNameValue!=""&&strName.indexOf(strNameValue)!=-1)
					objDicTable.rows[i].style.display="";
				else
					objDicTable.rows[i].style.display="none";
			}
		}else
			objDicTable.rows[i].style.display="";
	}
}
function sys_generCell(_objNewRow,_strName,_strValueSrc){
	var objNewCell=_objNewRow.insertCell();
	objNewCell.className='td1';
	objNewCell.innerHTML=_strValueSrc.value+"id='"+_strName+"' name='"+_strName+"'>";//����
	yltSelect.initSel(_strName);
}
function sys_del_row(_obj){
	var iIndex=_obj.parentElement.parentElement.rowIndex;
	sys_tabop.deleteRow(iIndex);
}
function sys_clear_Often_con(){
	var objConTb=$("tbconcontainer");
	var iRowCount=objConTb.rows.length;
	for(var i=0;i<iRowCount;i++)
		objConTb.deleteRow(0);
	iIdCount=0;
}
function sys_GetNormalQueryCondition(){
	var strCondition="";
	var strSplit="";
	for(var i=0;i<iIdCount;i++){
		var objFieldCode=$("f"+i);
		if(objFieldCode!=null){
			var strFieldCode=objFieldCode.value;
			var strLogic=$("l"+i).value;
			var strFieldValue=$("fv"+i).value;
			var strFieldValueCode=$("fvh"+i).value;
			
			if(strFieldValueCode!="")
				strFieldValue=strFieldValueCode;
		

		if(strLogic=="are"){			
			strFieldValue=strFieldCode+">='"+$("ds"+i).value+"' and "+strFieldCode+"<='"+$("de"+i).value+" 23:59:59'";
			strFieldCode="";strLogic="";
		}else if(strLogic=="in"||strLogic=="not in")
			strFieldValue=" ('"+strFieldValue.replace(/,/g,"','")+"') ";
		else if(strLogic==">"||strLogic=="<"||strLogic==">="||strLogic=="<="){
				if(isNaN(strFieldValue)){
					strFieldValue=" '"+strFieldValue+"' ";
				}else
				strFieldValue=strFieldValue+" ";
		}else if(strLogic=="like"||strLogic=="not like")
			strFieldValue=" '%"+strFieldValue+"%' ";
		else
			strFieldValue=" '"+strFieldValue+"' ";
		
		strCondition+=strSplit+strFieldCode+' '+strLogic+" "+strFieldValue;
		strSplit=" and ";	
		}
	}
	return strCondition;
}
function sys_doNormalCondition(){
	var strCondition=sys_GetNormalQueryCondition();
	if(strCondition!=""){
		var objParent=parent.getOpenPage(strWinId);		
		objParent.sys_form_fiter_con.NO_FITER_CONDITION.value=strCondition;
		objParent.sys_form_fiter_con.submit();
		parent.closeWinById(strWinId);
	}
}
function sys_doSaveNormalCondition(){
	if($("t_sys_user_condition$S_USER").value==""){
		alert("���ȵ�¼��");
		return;
	}
	var strConName=$("inputcycxnm").value;
	if(strConName==""){
		alert("��ѯ���Ʋ���Ϊ�գ�");
		return;
	}
	
	for(var i=0;i<iIdCount;i++){
		var objFieldCode=$("f"+i);
		if(objFieldCode!=null){
			var objLogic=$("l"+i);
			var objFieldValue=$("fv"+i);
			var objFieldValueCode=$("fvh"+i);

		
        for (j = 0; j < objLogic.options.length; j++) {
			if (objLogic.options[j].selected) 
                     objLogic.options[j].setAttribute("selected","selected");
        }
			
			if(objFieldValue.value!='')
				objFieldValue.setAttribute("value",objFieldValue.value);
			if(objFieldValueCode.value!='')
				objFieldValueCode.setAttribute("value",objFieldValueCode.value);
			                      
			
		}
	}
	
	
	
	
	
	var strCondition=$("tbconcontainer").innerHTML;
	if(strCondition!=""){
		$("t_sys_user_condition$S_CONDITION").value=strCondition;
		$("t_sys_user_condition$S_CON_NM").value=strConName;
		$("t_sys_user_condition$I_CON_COUNT").value=iIdCount;
		$("formsavecon").submit();
	}
}
function sys_do_NormalQueryCYCX(_strQueryId,_iConCount,_strConName){
	$("inputcycxnm").value=_strConName;
	$("tbconcontainer").innerHTML=$("con_"+_strQueryId).value;
	iIdCount=_iConCount;
	sys_str_Cur_Con_Id=_strQueryId;
	
}
function sys_GetQueryCondition(){
	var iRowCount=sys_tabop.rows.length;
	var strCondition="";
	var iKHCount=0;
	for(var i=1;i<iRowCount;i++){

		var strFieldIndex=sys_tabop.rows[i].getAttribute("strFieldIndex");
		//alert(strFieldIndex);
		var strKuoHaoValue=$('kh'+strFieldIndex).value;
		var strLuoJiFuValue=$('ljf'+strFieldIndex).value;
		if(strKuoHaoValue=="(")
			iKHCount++;
		if(strLuoJiFuValue==")" || strLuoJiFuValue==")AND" || strLuoJiFuValue==")OR")
			iKHCount--;
		
		var strTJ= $('tj'+strFieldIndex).value;
		var strTJZ;
		if(strTJ=="IN"||strTJ=="NOT IN")
			strTJZ=" ('"+$('tjz'+strFieldIndex).value+"') ";
		else if(strTJ==">"||strTJ=="<"||strTJ==">="||strTJ=="<="){
			var strFieldValue=$('tjz'+strFieldIndex).value;
			if(isNaN(strFieldValue)){
				strTJZ=" '"+strFieldValue+"' ";
			}else
				strTJZ=strFieldValue+" ";
		}else if(strTJ=="LIKE"||strTJ=="NOT LIKE")
			strTJZ=" '%"+$('tjz'+strFieldIndex).value+"%' ";
		else
			strTJZ=" '"+$('tjz'+strFieldIndex).value+"' ";
		
		strCondition+=strKuoHaoValue+$('zd'+strFieldIndex).value+' '+strTJ+strTJZ+strLuoJiFuValue+' ';
	}
	if(iKHCount!=0){
		alert("�������ô������Ų��Գƣ�");
		return "";
	}else{
		return strCondition
	}
}
function sys_doCondition(){
	var strCondition=sys_GetQueryCondition();
	if(strCondition!=""){
		var objParent=parent.getOpenPage(strWinId);		
		objParent.sys_form_fiter_con.NO_FITER_CONDITION.value=strCondition;
		objParent.sys_form_fiter_con.submit();
		parent.closeWinById(strWinId);
	}
}
function sys_doSaveCondition(){
	if($("t_sys_user_condition$S_USER").value==""){
		alert("���ȵ�¼��");
		return;
	}
	var strConName=$("inputcycxnm").value;
	if(strConName==""){
		alert("��ѯ���Ʋ���Ϊ�գ�");
		return;
	}
	var strCondition=sys_GetQueryCondition();
	if(strCondition!=""){
		$("t_sys_user_condition$S_CONDITION").value=strCondition;
		$("t_sys_user_condition$S_CON_NM").value=strConName;
		$("formsavecon").submit();
	}
}
function sys_do_QueryCYCX(_strQueryId){
	var objParent=parent.getOpenPage(strWinId);		
	objParent.sys_form_fiter_con.NO_FITER_CONDITION.value=$("con_"+_strQueryId).value;
	objParent.sys_form_fiter_con.submit();
	parent.closeWinById(strWinId);
	
}
var sys_Ctrl_IS_DOWN=false;
var sys_Shift_IS_DOWN=false;
function sys_Key_Down_Listener(){
	switch(window.event.keyCode){
		case 17:
			sys_Ctrl_IS_DOWN=true;
			break;
		case 16:
			sys_Shift_IS_DOWN=true;
			break;
	}
}
function sys_Key_Up_Listener(){
	switch(window.event.keyCode){
		case 17:
			sys_Ctrl_IS_DOWN=false;
			break;
		case 16:
			sys_Shift_IS_DOWN=false;
			break;
	}
}

function sys_Enter_Down_Listener(){
	if(window.event.keyCode==13)
		login(user.value,pwd.value,'dohome');
}
function sys_FilterUrl(_strUrl){
	_strUrl=_strUrl.replace(/#/g, "%23");
	return _strUrl;
}

// var sys_cur_query_trclass='';
  function sysMoseOverTr(_objRow){
  if(i_sys_msd_status==-1){
	
	if(!!!_objRow.sys_cur_query_trclass)
		_objRow.sys_cur_query_trclass=_objRow.className;
	_objRow.className="tr1over";
  }
  }
  function sysMoseOutTr(_objRow){
  if(i_sys_msd_status==-1){
	//if(objSysCurSelectRow!=_objRow)
	var objCurCheckBox=_objRow.cells[0].childNodes[0];
	var bIsHaveCheckBox=false;
	if(objCurCheckBox!=null&&objCurCheckBox.type!=null&&objCurCheckBox.type=="checkbox")
		bIsHaveCheckBox=true;
	
	if(!bIsHaveCheckBox||!objCurCheckBox.checked)
			_objRow.className=_objRow.sys_cur_query_trclass;
  }
  }

var i_sys_msd_status=-1;
var i_sys_msd_cells_index=-1;
var i_sys_msd_sel_top=-1;
var i_sys_msd_start_row=-1;
var i_sys_msd_end_row=-1;
function destroElement(){
	var objChildNodes=sys_query_select_div.childNodes;
	var iChildLength=objChildNodes.length;
	for(var i=0;i<iChildLength;i++){
		var objChild=objChildNodes[i];
		var strClass="";
		if(objChild.attributes["class"])
			strClass=objChild.attributes["class"].nodeValue;
		if(strClass=="ylselect")
			yltSelect.hiddenSelWin(objChild.id);
	}
	
}
var i_sys_msd_move_start_y=-1;
function $_sysMSDown(_obj,_event){

	document.body.onselectstart = new Function("return false"); 
	if(sys_query_select_div.style.display==""){
		if(sys_Shift_IS_DOWN){
			var objTrigerTd=_obj;
			var iCurDivHeight=objTrigerTd.offsetTop+objTrigerTd.clientHeight-i_sys_msd_sel_top;
			sys_query_select_div.style.height=iCurDivHeight+"px";
			i_sys_msd_end_row=_obj.rowIndex;
			$_sys_sel_tip();
			return;
		}
	
	}
	
	
	_event=window.event||_event;
	
	destroElement();
	sys_query_select_div.innerHTML="";
	var objTrigerTd=_event.srcElement;
	if(objTrigerTd==null)
		objTrigerTd=_event.target;
	
	i_sys_msd_status=objTrigerTd.cellIndex;
	i_sys_msd_cells_index=i_sys_msd_status;
	sys_query_select_div.style.display="none";
	//objTrigerTd.style.background="red";
	//alert(objTrigerTd.cellIndex);
	
	i_sys_msd_sel_top=objTrigerTd.offsetTop;
	
	sys_query_select_div.style.top=i_sys_msd_sel_top+"px";
	sys_query_select_div.style.left=objTrigerTd.offsetLeft+"px";
	sys_query_select_div.style.width=objTrigerTd.clientWidth+"px";
	sys_query_select_div.style.height=objTrigerTd.clientHeight+"px";
	i_sys_msd_start_row=_obj.rowIndex;
	i_sys_msd_move_start_y=_event.clientY;
	//alert(window.event.button);
	//sys_table_op_right_msg.innerHTML=event.clientY;
}
function $_sysDoScroll(){
	vbar.scrollTop=vbar.scrollTop+100;
}
function yl_sys_leftMouseIsDown(_event){
	var iLeftButton=0;
	if(_event.which==null)
		iLeftButton=window.event.button;
	else
		iLeftButton=_event.which;
	if(iLeftButton==1)
		return true;
	else
		return false;
}
function $_sysMSMove(_obj,_event){
	
	_event=window.event||_event;
	//sys_table_op_right_msg.innerHTML=yl_sys_leftMouseIsDown(_event);
	if(!yl_sys_leftMouseIsDown(_event))
		i_sys_msd_status=-1;
	
	
	
	if(i_sys_msd_status!=-1){
		
		var objTrigerTd=_obj.cells[i_sys_msd_status];
		if(objTrigerTd==null)return;
		var iCurDivHeight=objTrigerTd.offsetTop+objTrigerTd.clientHeight-i_sys_msd_sel_top;
		if((_event.clientY-i_sys_msd_move_start_y)>3){
			sys_query_select_div.style.display="";
			sys_query_select_div.style.height=iCurDivHeight+"px";
			i_sys_msd_end_row=_obj.rowIndex;
			//sys_table_op_right_msg.innerHTML=iCurDivHeight+"px";
		}
		if(sys_Ctrl_IS_DOWN)
			vbar.scrollTop=vbar.scrollTop+25;
		
		 $_sys_sel_tip();
		
	}

}
function $_sys_sel_tip(){
	var objsys_table_label_other_msg=$("sys_table_label_other_msg");
	if(objsys_table_label_other_msg!=null){
			

	var objDataTable=document.getElementById("tb");
	var result="����:&nbsp;&nbsp;<font color='blue'><b>"+(i_sys_msd_end_row-i_sys_msd_start_row+1)+"</b></font>";
	var fTotal=0;
	var iIndex=0;
	for(var i=i_sys_msd_start_row;i<=i_sys_msd_end_row;i++){
		var fCellValue= parseFloat(objDataTable.rows[i].cells[i_sys_msd_cells_index].innerText.replace(/,/g,""));
		if(!isNaN(fCellValue)){
			fTotal=fTotal+fCellValue;
			iIndex++;
		}
	}
	fTotal=fTotal.toFixed(2);
	if(fTotal!=0)
		result+="&nbsp;&nbsp;���:&nbsp;&nbsp;<font color='blue'><b>"+fTotal+"</b></font>&nbsp;&nbsp;ƽ��ֵ:&nbsp;&nbsp;<font color='blue'><b>"+(fTotal/iIndex).toFixed(2)+"</b></font>";
	objsys_table_label_other_msg.innerHTML=result+"&nbsp;&nbsp;";
	}
}
function sys_update_sel_cells(){
	var objDataTable=document.getElementById("tb");
	var strSelValue=sys_sel_branche_tree.value;
	var strSelTerxt=sys_sel_branche_tree.text;
	for(var i=i_sys_msd_start_row;i<=i_sys_msd_end_row;i++){
		getTx("NO_OPTYPE=1&t_component$S_GJH="+objDataTable.rows[i].cells[0].innerText+"&t_component$S_BRANCHID="+strSelValue+"&NO_CON=t_component$S_GJH","YLWebAction");
		objDataTable.rows[i].cells[i_sys_msd_cells_index].innerHTML=strSelTerxt;
	}
}


function sys_update_Allsel_Cells(_iConCol,_strTb,_strF,_strCF){
	var objDataTable=document.getElementById("tb");
	var strSelValue=selflownode.value;
	var strSelTerxt=selflownode.text;
	var strUpdateWhere="";
	for(var i=i_sys_msd_start_row;i<=i_sys_msd_end_row;i++){
		//getTx("NO_OPTYPE=1&t_component$S_GJH="+objDataTable.rows[i].cells[0].innerText+"&t_component$S_BRANCHID="+strSelValue+"&NO_CON=t_component$S_GJH","YLWebAction");
		strUpdateWhere+=","+objDataTable.rows[i].cells[_iConCol].innerText;
		objDataTable.rows[i].cells[i_sys_msd_cells_index].innerHTML=strSelTerxt;
	}
	if(strUpdateWhere!=""){
		strUpdateWhere=strUpdateWhere.substring(1);
		strUpdateWhere="'"+strUpdateWhere.replace(/\,/g,"','")+"'";
		//update <<s_t>> set <<s_f>>='<<s_f_v>>' where <<s_c>> in(<<s_c_c>>)
		getTx("comid=001&s_t="+_strTb+"&s_f="+_strF+"&s_c="+_strCF+"&s_f_v="+strSelValue+"&s_c_c="+strUpdateWhere,"docommand");
	}
	//alert(strUpdateWhere);
}


function $_sys_MS_simpleUpdate(){
	var objDoFun=yltPub["doView_Simple_Update"];
	if(!!objDoFun){
		objDoFun();
	}
}
function $_sysMSUp(_obj){
	i_sys_msd_status=-1;
	//sys_query_select_div.innerHTML="<input type='text'>";
	
}
//'T_001003$1426730944237'
function sys_EditChildTable(_strPageCode){
	var arrPageCode=_strPageCode.split("$");
	miniWin(arrPageCode[2],'','View?SPAGECODE=T'+arrPageCode[0]+"&S_ID="+arrPageCode[1],parent.parent.parent.iScreen_Width-150,parent.parent.parent.iScreen_Height-150,'','')
}
function sys_openQueryTable(_strPageCode){
	var arrPageCode=_strPageCode.split("$");
	miniWin(arrPageCode[1],'','View?SPAGECODE='+arrPageCode[0],parent.parent.parent.iScreen_Width-150,parent.parent.parent.iScreen_Height-150,'','')
}
function sys_showMaxWin(_strPageCode){
	var arrPageCode=_strPageCode.split("$");
	if(arrPageCode.length<2)
		arrPageCode=_strPageCode.split("-");
	miniWinAll(arrPageCode[0],'',arrPageCode[1],parent.parent.parent.iScreen_Width,parent.parent.parent.iScreen_Height-60,'','',0,60,0);
}
/**
String.prototype.endWith=function(str){     
  var reg=new RegExp(str+"$");     
  return reg.test(this);        
}
**/
function sys_open_file(_obj,_strUrl){
	miniWinAll('�ĵ��鿴','',_strUrl,parent.parent.parent.iScreen_Width,parent.parent.parent.iScreen_Height-60,'','',0,60,0);
}
var sys_obj_cur_int_input=null;
var sys_obj_cur_int_input_value="0";
function sys_gener_IntInput(_obj,_iSubValue){
	var objIntInputDiv=$("sys_int_input_div");
	if(objIntInputDiv!=null){
		
		var objPos=_obj.getBoundingClientRect();
		var iScrollTop=Math.max(document.documentElement.scrollTop,document.body.scrollTop);
		var iScrollLeft=Math.max(document.documentElement.scrollLeft,document.body.scrollLeft);
	
	
		objIntInputDiv.style.left=(objPos.left+iScrollLeft)+"px";
		objIntInputDiv.style.top=(objPos.bottom+iScrollTop+1)+"px";
		objIntInputDiv.style.display="";
		
		if(sys_obj_cur_int_input!=null)
			sys_obj_cur_int_input.className="sys_int_input";
		
		sys_obj_cur_int_input=_obj;
		sys_obj_cur_int_input_value=_iSubValue;
		_obj.className="sys_int_input_active";
	}

}
function doCommand(_strCommand){
	getTx(_strCommand,'docommand');
}
function sys_gener_IntInput_Over(_obj){
	_obj.className="sys_int_input_div_span_over";
}
function sys_gener_IntInput_Out(_obj){
	_obj.className="sys_int_input_div_span";
}
function sys_gener_IntInput_Out_Clear(_obj){
	_obj.className="sys_int_input_div_span_clear";
}
function sys_gener_IntInput_Out_Ok(_obj){
	_obj.className="sys_int_input_div_span_ok";
}
function sys_gener_IntInputDown(_obj){

	_obj.className="sys_int_input_div_span_click";
}
function sys_gener_IntInputClick(_obj,_iSubValue){
	if(sys_obj_cur_int_input!=null){
		if(sys_obj_cur_int_input.value==sys_obj_cur_int_input_value)
			sys_obj_cur_int_input.value=_iSubValue;
		else
			sys_obj_cur_int_input.value+=_iSubValue;
	}
	_obj.className="sys_int_input_div_span_over";
}
function sys_gener_IntInputClick_Clear(_obj){
	if(sys_obj_cur_int_input!=null){
		sys_obj_cur_int_input.value="";
	}
	_obj.className="sys_int_input_div_span_over";
}
function sys_gener_IntInputClick_Ok(_obj){
	_obj.className="sys_int_input_div_span_over";
	if(sys_obj_cur_int_input!=null)
		sys_gener_Int_Change(sys_obj_cur_int_input.value,"t_001","jey_7","S_ID='1430883100465'");
}
function sys_gener_Int_Change(_iChangeValue,_strTb,_strFd,_strCd){
	var vResult=getTx("comid=003&sys_tb="+_strTb+"&sys_fd="+_strFd+"&sys_vl="+_iChangeValue+"&sys_cd="+_strCd,"docommand");
				if(vResult=="true")
					alert("����ɹ���");
				else
					alert("����ʧ�ܣ�");
}
document.onkeydown=sys_Key_Down_Listener;
document.onkeyup=sys_Key_Up_Listener;
document.onclick=function(event){var objListPop=$("query_list_pop_menu");if(objListPop!=null)objListPop.style.display="none";
							var objTreePop=$("sys_tree_pop_menu");if(objTreePop!=null)objTreePop.style.display="none";
							var objIntInputDiv=$("sys_int_input_div");
							if(objIntInputDiv!=null){
								if(event.srcElement.attributes["intinpuspanivigr"]==null){
									if(sys_obj_cur_int_input!=null)
										sys_obj_cur_int_input.className="sys_int_input";
									objIntInputDiv.style.display="none";
								}
							}
							var objMainMenu=parent.parent.parent.parent.document.getElementById("sys_div_msg");
							if(objMainMenu!=null)
								objMainMenu.style.display="none";
						   };
window.onload=loadpageEvent;




function getNowFormatDate() 
{ 
var day = new Date(); 
var Year = 0; 
var Month = 0; 
var Day = 0; 
var CurrentDate = ""; 
//��ʼ��ʱ�� 
//Year= day.getYear();//�л����2008����ʾ108��bug 
Year= day.getFullYear();//ie����¶����� 
Month= day.getMonth()+1; 
Day = day.getDate(); 
//Hour = day.getHours(); 
// Minute = day.getMinutes(); 
// Second = day.getSeconds(); 
CurrentDate += Year + "-"; 
if (Month >= 10 ) 
{ 
CurrentDate += Month + "-"; 
} 
else 
{ 
CurrentDate += "0" + Month + "-"; 
} 
if (Day >= 10 ) 
{ 
CurrentDate += Day ; 
} 
else 
{ 
CurrentDate += "0" + Day ; 
} 
return CurrentDate; 
} 






function sys_do_queryUpdateExe(_obj,_iRow,_strGJH,_strPartId,_strPId,_strDate,_strOpTb,_strOpField,_strUserName){
    var _upValue=_obj.value;
    if(_upValue=='')
        return;
    if(isNaN(_upValue)){
        alert("���������֣�");
        _obj.value="";
        return;
    }
    
    var objRow=$('tb').rows[_iRow];
    var arrZslzwc=objRow.cells[3].innerText.split('/');
    
    var iYwc=parseInt(arrZslzwc[0]);
    var iZsl=parseInt(arrZslzwc[1]);
    var iDrwc=parseInt(_upValue);
    
    var parvalue=_obj.getAttribute('parchangevalue');
        if(parvalue=='')
            parvalue=0;
    
    var iDrZwc=iYwc-parseInt(parvalue)+iDrwc;
    if(iDrZwc>iZsl){
        alert('��������������Χ�����������룡');
        return;
    }
    
    
    //var strDate='<<v3>>';
	var vResult="";
	if(_strGJH=="")
		vResult=getTx('comid=011&sdate='+_strDate+'&partid='+_strPartId+'&pid='+_strPId+'&sl='+_upValue+'&optb='+_strOpTb+'&opcount='+_strOpField,'docommand');
	else{
		if(_strGJH.charAt(0)=="$"){
			_strGJH=_strGJH.substring(1);
			vResult=getTx('comid=013&gjh='+_strGJH+'&sdate='+_strDate+'&partid='+_strPartId+'&pid='+_strPId+'&sl='+_upValue+'&optb='+_strOpTb+'&opcount='+_strOpField,'docommand');
		}else
			vResult=getTx('comid=006&gjh='+_strGJH+'&sdate='+_strDate+'&partid='+_strPartId+'&pid='+_strPId+'&sl='+_upValue+'&optb='+_strOpTb+'&opcount='+_strOpField,'docommand');
    }
	if(vResult=='true'){
        _obj.setAttribute('parchangevalue',_upValue);
        objRow.cells[6].innerText=_strUserName;//'<<SYS_STRCURUSERNAME>>';
        objRow.cells[5].innerText=_strDate;
        objRow.cells[3].innerText=iDrZwc+'/'+iZsl;
    }else{
        alert('����ʧ�ܣ�');
    }
        
}


function sys_do_queryUpdateGjExe(_obj,_iRow,_strGJH,_strPId,_strDate,_strOpTb,_strOpField,_strUserName){
    var _upValue=_obj.value;
    if(_upValue=='')
        return;
    if(isNaN(_upValue)){
        alert("���������֣�");
        _obj.value="";
        return;
    }
	
	var objRow=$('tb').rows[_iRow];
    var arrZslzwc=objRow.cells[3].innerText.split('/');
    
    var iYwc=parseInt(arrZslzwc[0]);
    var iZsl=parseInt(arrZslzwc[1]);
    var iDrwc=parseInt(_upValue);
    
    var parvalue=_obj.getAttribute('parchangevalue');
        if(parvalue=='')
            parvalue=0;
    
    var iDrZwc=iYwc-parseInt(parvalue)+iDrwc;
    if(iDrZwc>iZsl){
        alert('��������������Χ�����������룡');
		if(parvalue==0)
			_obj.value="";
		else
			_obj.value=parvalue;
		_obj.focus();
        return;
    }
	var strComId="007";
	if(_strOpTb=="t_mr_gjzz")
		strComId="014";
    var vResult=getTx('comid='+strComId+'&gjh='+_strGJH+'&sdate='+_strDate+'&pid='+_strPId+'&sl='+_upValue+'&optb='+_strOpTb+'&opcount='+_strOpField,'docommand');
    if(vResult=='true'){
		_obj.setAttribute('parchangevalue',_upValue);
        objRow.cells[5].innerText=_strUserName;
        objRow.cells[6].innerText=_strDate;
		objRow.cells[3].innerText=iDrZwc+'/'+iZsl;
    }else{
        alert('����ʧ�ܣ�');
    }
        
}

function importTreeSelRow(bIsIndex,_arrStartCell,_arrSelCellId,_arrComId,_arrStyle,_strWinId,_sId,_strIdInput){
    var objTable=document.getElementById("tb");
    var objCheckChilds=document.getElementsByName("syscheckbox");
	var objOpenPage;
	if(_strWinId=="")
		objOpenPage=parent.parent.getOpenPage(parent.lxleft.gs_upl_kc);
	else
		objOpenPage=parent.getOpenPage(_strWinId);
    var objOpenTable=objOpenPage.$('tablebatch');
    var iSelCellCount=_arrSelCellId.length;
	var iStartCellCount=_arrStartCell.length;
	var iCheckCount=objCheckChilds.length;
	var newTr;
	var newTd;
	var strSID=objOpenPage.add.elements[_sId].value;
		for(var i=0;i<iCheckCount;i++){
			if(objCheckChilds[i].checked){
				newTr = objOpenTable.insertRow();
				objSelRow=objTable.rows[parseInt(objCheckChilds[i].value)+1];
				if(bIsIndex){
					newTd= newTr.insertCell();
					newTd.className="td1";
					newTd.innerText=i;
				}
				var iEndCellCount=iStartCellCount-1;
				for(var j=0;j<iStartCellCount;j++){
					newTd= newTr.insertCell();	
					newTd.className="td1";
					if(j==iEndCellCount)
						newTd.innerHTML=_arrStartCell[j]+" value='"+strSID+"'>";
					else
						newTd.innerHTML=_arrStartCell[j];
				}
				var bIsSIdInput=false;
				var strMainKeyInput="";
				if(iStartCellCount==0){
					strMainKeyInput=_strIdInput+" value='"+strSID+"'>";
					bIsSIdInput=true;
				}
				for(var j=0;j<iSelCellCount;j++){
					newTd= newTr.insertCell();	
					newTd.className="td1";
					if(_arrStyle[j]!=""){
						if(bIsSIdInput){
							newTd.innerHTML=strMainKeyInput+"<input type='text' "+_arrStyle[j]+" value='"+objSelRow.cells[_arrSelCellId[j]].innerText+"' name='"+_arrComId[j]+"'>";
							bIsSIdInput=false;
						}else
							newTd.innerHTML="<input type='text' "+_arrStyle[j]+" value='"+objSelRow.cells[_arrSelCellId[j]].innerText+"' name='"+_arrComId[j]+"'>";
					}else
						newTd.innerHTML=objSelRow.cells[_arrSelCellId[j]].innerText;
				}
			}
		}
	//alert(strSelHtml);
	if(_strWinId=="")
		parent.parent.closeWinById(parent.lxleft.gs_upl_kc);
	else
		parent.closeWinById(_strWinId);
}
function SYS_ADD_BATCH_ROW(_strTBId){
	var objTab=$("sys_form_content"+_strTBId);
	var objRow=document.createElement("tr");
	objRow.onmouseover=function(){this.style.background='#f1f1f1';};
	objRow.onmouseout=function(){this.style.background='#ffffff';};
	
	strUploadInintScript="";
	objRow.innerHTML=SYS_ChangeUploadId(SYS_ChangeSelId($("NO_SYS_BATCH_ROW"+_strTBId).value,_strTBId));
	var objRowArea = document.createDocumentFragment();
	objTab.appendChild(objRowArea.appendChild(objRow));
	SYS_InitBatchSelId(_strTBId);
	eval(strUploadInintScript);
}
function SYS_InitBatchSelId(_strTBId){
	var arrTreeId=eval("arrInitSelTreeId"+_strTBId);
	var iLength=arrTreeId.length;
	for(var i=0;i<iLength;i++)
		yltSelect.initSel(arrTreeId[i]+"_"+iBatchFormTreeIdIndex);
} 
var iBatchFormTreeIdIndex=0;
var iUploadId_Index=0;
var strUploadInintScript="";
function SYS_ChangeUploadId(_strContent){
	var iLength=arrUploadIds.length;
	
	for(var i=0;i<iLength;i++){
		var arrUploadId=arrUploadIds[i].split(",");
		var strUploadId=arrUploadId[0]+"_"+iUploadId_Index;
		var strUPloadInputId=arrUploadId[1]+"_"+iUploadId_Index;
		_strContent=_strContent.replace(new RegExp(arrUploadId[0],'gm'),strUploadId);
		_strContent=_strContent.replace(" id='"+arrUploadId[1]+"' "," id='"+strUPloadInputId+"' ");
		strUploadInintScript="initSingFile('"+strUploadId+"','"+strUPloadInputId+"');";
	}
	iUploadId_Index++;
	return _strContent;
}
function SYS_ChangeSelId(_strContent,_strTBId){
	var arrTreeId=eval("arrInitSelTreeId"+_strTBId);
	var iLength=arrTreeId.length;
	iBatchFormTreeIdIndex++;
	for(var i=0;i<iLength;i++)
		_strContent=_strContent.replace(
				"id=\""+arrTreeId[i]+"\"",
				"id=\""+arrTreeId[i]+"_"+iBatchFormTreeIdIndex+"\"");
	return _strContent;
}
function SYS_INSERT_BATCH_ROW(_obj,_strTBId){
	var objTab=$("sys_form_content"+_strTBId);
	var objTr=_obj.parentNode.parentNode;
	var newTr = objTab.insertRow(objTr.rowIndex);
	newTr.onmouseover=function(){this.style.background='#f1f1f1';};
	newTr.onmouseout=function(){this.style.background='#ffffff';};
	
	strUploadInintScript="";
	
	newTr.innerHTML=SYS_ChangeUploadId(SYS_ChangeSelId($("NO_SYS_BATCH_ROW"+_strTBId).value,_strTBId));
	SYS_InitBatchSelId(_strTBId);
	eval(strUploadInintScript);
}
function SYS_DEL_BATCH_ROW(_obj,_strTBId){
	var objTr=_obj.parentNode.parentNode
	objTr.parentNode.removeChild(objTr);
}
function SYS_CHANGE_BATCH_TAB(){
	var objMainForm=$("div_mainform");
	var objChildForm=$("div_childform");
	
	if(objMainForm.style.display==""){
		objMainForm.style.display="none";
		objChildForm.style.height="100%";
	}else{
		objMainForm.style.display="";
		objChildForm.style.height="40%";
	}
	initTabPageSize();
}