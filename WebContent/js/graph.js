
var myGraph = {
		objGraphType:{
			"line":"����ͼ",
			"pie":"��״ͼ",
			"bar":"��״ͼ"
			
		},
		pieType:["","radius","area"],//ͼģʽ
		legendObjArr:["graphType","legendWidth","djmc","color","legendGraphWidth"],//ͼ����
		formObjArr:["t_sys_pagemsg$SPAGENAME","t_sys_pagemsg$SQUERYFIELD","t_sys_pagemsg$SGLFIELD","spagemsgSelect","axisField"],
		dataControlClass:"graph",//���ݵ�����
		checkHighColor:"#ccc",
		graphNums:0,//������ͼ�θ���
		isExistParPage:false,//����ҳ���Ƿ����
		allParGraph:"",//���ֵ�ͼ�ζ�Ӧ�ĵ�Ԫ������
		parSpagecode:getUrl("spagecode"),//ҳ��ͼ�α��(���ֵ�ͼ�α��)
		currentSpagecode:"",//��ǰ������ͼ�α��
		currentGraphDatasetCode:"",//��ǰ������ͼ�ε����ݼ����
		currentFieldCodes:"",//��ǰ������ͼ�����ݼ����ֶδ���
		currentFieldNames:"",//��ǰ������ͼ�����ݼ����ֶ�����
		currentGraphLegendObj:"",//��ǰ������ͼ������
		allgraphLegends:"",//����ͼ������
		currentXias:"",//��ǰͼ�ε������ֶ�
		currentTdId:"",//��ǰ�����ĵ�Ԫ���ID
		isNewGraph:false,//˫���ж����½������޸�
		
		newGraph:function(){
			this.currentSpagecode = "";
			this.currentGraphDatasetCode = "";
			this.currentFieldCodes = "";
			this.currentFieldNames = "";
			this.currentGraphLegendObj = "";
			this.allgraphLegends = "";
			this.currentXias = "";
			
			$("t_sys_pagemsg$SPAGENAME").value = "";
			$("t_sys_pagemsg$SQUERYFIELD").value = "";
			$("t_sys_pagemsg$SGLFIELD").value = "";
			$("legend").innerHTML = "";
			$("graphType").innerHTML = "";
			$("djmc").value = "";
			$("legendWidth").value = "5";
			$("legendGraphWidth").value = "22";
			
			var spagemsgSelect = $("spagemsgSelect");
			spagemsgSelect.value='';
			
			var axisField = $("axisField");//�����ֶ�
			axisField.options.length=0;
			
			
		},
		
		initPage:function(){//��ʼ��ҳ������
			var pageData = getTx("method=getPageData&spagecode="+this.parSpagecode, this.dataControlClass);
			if(pageData!=""){
				pageData = pageData.split("{}");
				this.graphNums = parseInt(pageData[0]);
				$("parPageName").value = pageData[5];
				this.isExistParPage = true;
				this.allParGraph =  pageData[1];
			}
			
			var spagemsgSelect = $("spagemsgSelect");
			spagemsgSelect.options.length=0;
			var pageInfo = getTx("method=initPage", this.dataControlClass).split("&");
			var pageType1 = pageInfo[0].split("|");//���ݼ�
			
			//���ݼ�
			for(var i = 0;i<pageType1.length-1;i++){
				var page = pageType1[i].split(":");
				spagemsgSelect.options.add(new Option(page[1],page[0] ));
			}

			
			
		},
		updateGraph:function(_obj){
			if(myGraph.currentSpagecode != ""){//ȷ�����޸�
				var oldPageInfo = getTx("method=getPageData&spagecode="+myGraph.currentSpagecode, myGraph.dataControlClass);
				var oldPageInfoTemp = oldPageInfo.split("{}");
				myGraph.currentGraphDatasetCode = oldPageInfoTemp[0];//���ݼ����
				
				
				myGraph.allgraphLegends = oldPageInfoTemp[2];//ͼ��
				
				myGraph.initGraphXais();//���������ֶ�������
				myGraph.createGraphLegend();//����ͼ����ʼ����
				
				
				$("spagemsgSelect").value=myGraph.currentGraphDatasetCode;
				$("t_sys_pagemsg$SQUERYFIELD").value=oldPageInfoTemp[3];//���
				$("t_sys_pagemsg$SGLFIELD").value=oldPageInfoTemp[4];//�߶�
				$("t_sys_pagemsg$SPAGENAME").value=oldPageInfoTemp[5];//ͼ������
				
			}
		}
		,
		selectData:function(selectSpagemsg){//ѡ�����ݼ�
			//���ݼ������ͼ������Ӧ����������
			if(selectSpagemsg != this.currentGraphDatasetCode){
				this.updateData("t_sys_pagemsg$SFIELDSIZE", "");
				this.updateData("t_sys_pagemsg$SFIELDCODE",selectSpagemsg);
				this.currentGraphLegendObj = "";//��ǰ������ͼ������
				this.allgraphLegends="";//����ͼ������
			}
			
			this.currentGraphDatasetCode = selectSpagemsg;
			this.initGraphXais();//���������ֶ�������
			this.createGraphLegend();//����ͼ����ʼ����

		},
		
		createGraphLegend:function(){//����ͼ����ʼ����
			var checkboxHtml ="";
			checkboxHtml += "<ul>";
			for(var i = 0 ;i < this.currentFieldCodes.length;i++){
				var strChecked = "";
				if(this.allgraphLegends!=""){
					var graphLendedss = this.allgraphLegends.split("~");
					for(j=0;j<graphLendedss.length;j++){
						if(graphLendedss[j].split("$")[2] == this.currentFieldCodes[i] ){
							strChecked = "checked='checked'";
							break;
						}
					}
				}
				
				checkboxHtml += "<li><input name='graphLegend' id='"+this.currentFieldNames[i]+"' type='checkbox' "+strChecked+"  onclick='myGraph.clickGraphLegend(this,false)' style='width:50px' value='"+this.currentFieldCodes[i]+"'/><span onclick='myGraph.clickGraphLegend(this.previousSibling,false)'>"+this.currentFieldNames[i]+"</span></li>";
			}
			checkboxHtml += "</ul>";
			$("legend").innerHTML=checkboxHtml;
			
			this.initGraphType();//����ͼ������������

		}
		,
		
		initGraphXais:function(){//��ʼ�������ֶ�
			var fieldInfo = getTx("method=createGraphCode&spagecode=" + this.currentGraphDatasetCode, this.dataControlClass);//--���õ�ͼ���ֶ�
			var fieldInfos = fieldInfo.split("|");
			this.currentFieldCodes = fieldInfos[0].split(",");
			this.currentFieldNames = fieldInfos[1].split(",");
			var axisField = $("axisField");//�����ֶ�
			axisField.options.length=0;
			
			for(var i = 0 ;i < this.currentFieldCodes.length;i++){
				axisField.options.add(new Option(this.currentFieldNames[i], this.currentFieldCodes[i]));
			}
			
			if(this.currentXias!=""){
				var currentXiass = this.currentXias.split(":");
				axisField.value=currentXiass[1];//�����ֶ�
				var axiss = document.getElementsByName("axis");//X or Y����
				for(var j = 0 ;j < axiss.length;j++){
					if(axiss[j].value == currentXiass[0]){
						axiss[j].checked=true;
					}
				}
			}
		},
		
		initGraphType:function(){//��ʼ��ͼ��������Ϣ--ͼ������
			
			//ͼ������
			var graphTypeTemp = $("graphType");
			graphTypeTemp.options.length=0;
			for(var i in this.objGraphType){
				graphTypeTemp.options.add(new Option(this.objGraphType[i],i ));
			}
			
			var pieTypeTemp = $("pieType");
			pieTypeTemp.options.length=0;
			for(var i=0;i<this.pieType.length;i++){
				pieTypeTemp.options.add(new Option(this.pieType[i],this.pieType[i] ));
			}
		},
		updateData:function (fieldcode,fieldvalue){//���»����������ݵ����ݿ�
			if(this.currentTdId==""){//δѡ��Ԫ��
				alert("����ѡ��Ҫ����ͼ�εĵ�Ԫ��");
				return;
			}
			
			var OPTYPE = "NO_OPTYPE=1&";
			
			
			
			if(this.currentSpagecode==""){//����
				this.currentSpagecode =this.parSpagecode+"_"+(parseInt(this.graphNums)+1);
				OPTYPE = "";
				
				if(this.allParGraph.trim() == ""){
					this.allParGraph += this.currentTdId+":"+this.currentSpagecode;
				}else{
					this.allParGraph += "~" + this.currentTdId+":"+this.currentSpagecode;
				}
				
				var parOPTYPE = "";
				if(!this.isExistParPage){//����ҳ��δ����
					parOPTYPE = "";
				}else{//����ҳ���Ѵ���-����ͼ������
					parOPTYPE = "NO_OPTYPE=1&";
				}
				var str = parOPTYPE+"NO_charset=UTF-8&NO_CON=t_sys_pagemsg$SPAGECODE&t_sys_pagemsg$SFIELDNAME="+this.allParGraph+"&t_sys_pagemsg$SFIELDCODE="+(this.graphNums+1)+"&t_sys_pagemsg$SPAGECODE="+this.parSpagecode;
				getTx(str,"YLWebAction");
				
				this.graphNums = this.graphNums+1;//ͼ������+1
			}
			
			
			var str = "t_sys_pagemsg$SPAGECODE="+this.currentSpagecode;
			var param = OPTYPE+"t_sys_pagemsg$SPAGETYPE=12&NO_charset=UTF-8&NO_CON=t_sys_pagemsg$SPAGECODE&"+str+"&"+fieldcode+"="+fieldvalue;
			
			param = this.replaceAll(param, "%", "��");
			getTx(param,"YLWebAction");
			
			
			this.reload();
		},
		savePage:function (){//���»���������������
			var pageName = $("parPageName").value;
			if(pageName==""){
				alert("����дҳ�����ƣ�");
				return;
			}
			if(!this.isExistParPage){//����ҳ��δ����
				getTx("NO_charset=UTF-8&NO_CON=t_sys_pagemsg$SPAGECODE&t_sys_pagemsg$SFIELDNAME= &t_sys_pagemsg$SPAGETYPE=88&t_sys_pagemsg$SPAGENAME="+pageName+"&t_sys_pagemsg$SFIELDCODE=0&t_sys_pagemsg$SPAGECODE="+this.parSpagecode,"YLWebAction");
			}else{//ҳ���Ѵ���
				getTx("NO_OPTYPE=1&NO_charset=UTF-8&NO_CON=t_sys_pagemsg$SPAGECODE&t_sys_pagemsg$SPAGENAME="+pageName+"&t_sys_pagemsg$SPAGECODE="+this.parSpagecode,"YLWebAction");
			}
			this.isExistParPage = true;
			alert("����ɹ���");
		},
		updateDataAuto:function(field){//�������ݵ����ݿ�,field��������
			if(field.value==""){
				alert("����Ϊ�գ�");
				return;
			}
			this.updateData(field.id, field.value);
		},
		
		deleteData:function(pagecode){//�������
			if(this.graphNums != 0 && pagecode != ""){
				if(confirm("��ȷ��Ҫ���������")){
					var str = "method=delData&from=t_sys_pagemsg&where=SPAGECODE LIKE '"+pagecode+"~'";
					var result = getTx(str,this.dataControlClass);
					if(result == "y"){
						if(pagecode.indexOf("-")){
							var fieldName = this.currentTdId+":"+pagecode+"~";
							this.allParGraph = this.allParGraph.replace(fieldName, "");
							getTx("NO_charset=UTF-8&NO_OPTYPE=1&NO_CON=t_sys_pagemsg$SPAGECODE&t_sys_pagemsg$SFIELDNAME="+this.allParGraph+"&t_sys_pagemsg$SFIELDCODE="+(this.graphNums-1)+"&t_sys_pagemsg$SPAGECODE="+this.parSpagecode,"YLWebAction");
						}
						alert("����ɹ���");
					}else{
						alert("���ʧ�ܣ�");
					}
					
					
					window.location.reload();
				}
			}else{
				alert("����Ҫ���!");
			}
		},
	
		clickGraphLegend:function(obj,isNewData){//���ͼ��
			var lis = document.getElementsByTagName("li");
			for(var i = 0;i<lis.length;i++){
				lis[i].style.backgroundColor="";
			}
			if(obj.checked){
				this.currentGraphLegendObj = obj;//����Ϊ��ǰͼ��
				obj.parentNode.style.backgroundColor=this.checkHighColor;
				
				if(this.allgraphLegends!=""){
					var findFlag = false;//���������У��Ƿ���ڸ�ͼ��
					var allgraphLegendsTemp = this.allgraphLegends.split("~");
					for(var i = 0 ;i < allgraphLegendsTemp.length;i++){
						
						var _cols = allgraphLegendsTemp[i].split("$");
						var _cols0 = _cols[0];
						if(_cols0 == obj.id){
							findFlag = true;
							//�ҵ��˺�϶�Ҫ��ԭֵ��ʾ����
							var graphType = _cols[1];//ͼ������
							var styles = _cols[3].split(",");//��ʽ
							
							$("graphType").value = graphType;
							
							this.generalGraphType();
								
							if(graphType == "pie"){//����Ǳ�״ͼ
								var pieType = styles[0].split(":")[1];
								var pieFieldTemp = _cols[4].split(",");
								$("pieField").value = pieFieldTemp[0];
								if(pieFieldTemp[1] == "y"){
									$("isGlobal").checked=true;
								}else{
									$("isGlobal").checked=false;
								}
								
								
								$("pieType").value = pieType;
								$("piePosition").value = _cols[5];
								
								
							}else{
								var color = styles[0].split(":")[1].replace("#","").replace("'","").replace("'","");//��ɫ
								var barBorderRadius = styles[1].split(":")[1];//ͼ�����
								var djmc = _cols[4];//�ѻ�����
								$("legendWidth").value = barBorderRadius;
								$("djmc").value = djmc;
								$("color").value = color;
								$("color").style.backgroundColor="#"+color;
								$("legendGraphWidth").value = _cols[5];
							}
							break;
						}
					}
				}
				
			}else{//ȥ��
				$("pietb").style.display="none";
				$("nopietb").style.display="none";
				
				var allGraphLegend="";
				this.currentGraphLegendObj = "";
				obj.parentNode.style.backgroundColor="";
				if(this.allgraphLegends!=""){
					var findFlag = false;
					var allgraphLegendsTemp = this.allgraphLegends.split("~");
					for(var i = 0 ;i < allgraphLegendsTemp.length;i++){
						var _cols = allgraphLegendsTemp[i].split("$")[0];
						if(_cols == obj.id){
							findFlag = true;
						}else{
							if(allgraphLegendsTemp[i]!=""){
								allGraphLegend += allgraphLegendsTemp[i]+"~";
							}
						}
					}
				}
				
				this.allgraphLegends = allGraphLegend;
				this.updateData("t_sys_pagemsg$SFIELDSIZE",allGraphLegend);
			}
			
			this.readOnlyLegend();
		},
		
		axisFieldChange:function(objAxisField){
			var axisField = objAxisField;
			var axiss = document.getElementsByName("axis");
			for(var i = 0 ;i < axiss.length;i++){
				if(axiss[i].checked){
					axisField = axiss[i].value+":"+axisField;
				}
			}
			this.updateData("t_sys_pagemsg$SFIELDNAME", axisField);
		},
		axisClick:function(obj){
			this.axisFieldChange($("axisField").value);
		},
		saveLegendPro:function(){
			var graphType = $("graphType");
			var _style = "";
			var thisGraphLegend = "";
			var allGraphLegend = "";
			var pieField = $("pieField").value;
			
			pieField = $("isGlobal").checked?pieField+",y":pieField+",n";
			
			
			if(graphType.value == "pie"){//��״ͼ
				_style = $("pieType").value == ""?"":"roseType:'"+$("pieType").value+"'";
			    thisGraphLegend = this.currentGraphLegendObj.id+"$"+graphType.value+"$"+this.currentGraphLegendObj.value+"$"+_style+"$"+pieField+"$"+$("piePosition").value;
			}else{
				_style = "color:'#"+$("color").value+"'"+",barBorderRadius:"+$("legendWidth").value+"$"+$("djmc").value+"$"+$("legendGraphWidth").value;
				thisGraphLegend = this.currentGraphLegendObj.id+"$"+graphType.value+"$"+this.currentGraphLegendObj.value+"$"+_style;
			}
			
			if(this.allgraphLegends!=""){
				var findFlag = false;//���������У��Ƿ���ڸ�ͼ��
				var allgraphLegendsTemp = this.allgraphLegends.split("~");
				for(var i = 0 ;i < allgraphLegendsTemp.length;i++){
					
					var _cols = allgraphLegendsTemp[i].split("$");
					var _cols0 = _cols[0];
					if(_cols0 == this.currentGraphLegendObj.id){//�ҵ���ͼ��
						findFlag = true;
						allGraphLegend += thisGraphLegend+"~";
					}else{
						if(allgraphLegendsTemp[i]!=""){
							if($("isGlobal").checked){
								allGraphLegend += this.replaceAll(allgraphLegendsTemp[i], ",y", ",n")+"~";
							}else{
								allGraphLegend += allgraphLegendsTemp[i]+"~";
							}
							
						}
					}
				}
				
				if(!findFlag){
					allGraphLegend += thisGraphLegend+"~"
				}
				
			}else{
				allGraphLegend = thisGraphLegend;
			}
			
			this.allgraphLegends = allGraphLegend;
			
			this.updateData("t_sys_pagemsg$SFIELDSIZE",allGraphLegend);
		},
		legendProBlur:function(){
			if(this.currentGraphLegendObj!=""){
				this.saveLegendPro();
			}else{
				alert("����ѡ��ͼ����");
			}
		},
		generalGraphType:function(){
			if($("graphType").value == "pie"){//����Ǳ�״ͼ
				$("pietb").style.display="block";
				$("nopietb").style.display="none";
				
				
				var pieField = $("pieField");
				
				pieField.options.length=0;
				
				for(var i=0;i< this.currentFieldCodes.length;i++){
					if(this.currentGraphLegendObj.value == this.currentFieldCodes[i]){
						continue;
					}
					pieField.options.add(new Option(this.currentFieldNames[i],this.currentFieldCodes[i] ));
				}
			}else{
				$("pietb").style.display="none";
				$("nopietb").style.display="block";
			}
		},
		graphTypeChange:function(){
			this.generalGraphType();
			this.legendProBlur();
		},
		iIndex:0,
		reload:function(){//ˢ��
			getTx("hometype=init","dohome");
			if(this.isNewGraph){
				this.isNewGraph = false;
				contentFrame.sys_insert_Graph(this.currentTdId,this.currentSpagecode,this.parSpagecode);
				contentFrame.location.reload();
			}else{
				contentFrame.sys_refresh(this.currentTdId,this.currentSpagecode+"&iindex="+this.iIndex);
				this.iIndex++;
			}
		},
		doGraph:function(_obj){
		
			this.currentTdId = _obj.id;
			
			var isexist = false;
			alert(this.allParGraph);
			var parGraphs = this.allParGraph.split("~");
			var spagecode;
			
			for(var i = 0 ;i < parGraphs.length;i++){
				var oneGraphs = parGraphs[i].split(":");
				if(oneGraphs[0] == _obj.id){
					isexist = true;
					spagecode = oneGraphs[1];
					break;
				}
			}
			
			if(isexist){//�޸�
				this.isNewGraph = false;
				this.currentSpagecode = spagecode;
				this.updateGraph();
			}else{//�½�
				this.isNewGraph = true;
				this.newGraph();
			}
			
			this.readOnlyLegend();
		}
		,
		readOnlyLegend:function(){
			var isDisabled = this.currentGraphLegendObj!=""?false: "disabled";
			for(var i=0;i<this.legendObjArr.length;i++){
				$(this.legendObjArr[i]).disabled=isDisabled;
			}
			
			isDisabled = this.currentTdId!=""?"relative": "absolute";
			var dis = this.currentTdId!=""?"none": "block";
			$("isdisabled").style.position=isDisabled;
			$("isdisabled").style.display=dis;
		},
		replaceAll:function(obj,str1,str2){
			return obj.replace(eval("/"+str1+"/gi"),str2);      
		}
}
