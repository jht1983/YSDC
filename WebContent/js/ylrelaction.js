function Htb()
    {
        this._hash = new Object();
        this.put   = function(key,value){
                   if(typeof(key)!="undefined"){
                     if(this.contains(key)==false){
                                    this._hash[key]=typeof(value)=="undefined"?null:value;
                                    return true;
                                } else {
                                    return false;
                                }
                            } else {
                                return false;
                            }
                        }
//�¼ӵ�
       this.getValues = function () {
         var values = new Array();
         for(var prop in this._hash){
             values.push(this._hash[prop]);
           }
         return values;
            }
 
     this.getKeys = function () {
         var keys = new Array();
         for(var prop in this._hash){
             keys.push(prop);
         }
         return keys;
     }
	 //end 
     this.getKeysByValue=function (value){
    	 var keys=new Array();
    	 var keys = new Array();
         for(var prop in this._hash){
            if(this._hash[prop]==value)
            	keys.push(prop);
         }
         return keys;
    	 
     }
        this.remove      = function(key){delete this._hash[key];}
        this.count       = function(){var i=0;for(var k in this._hash){i++;} return i;}
        this.get         = function(key){return this._hash[key];}
        this.contains    = function(key){return typeof(this._hash[key])!="undefined";}
        this.clear       = function(){for(var k in this._hash){delete this._hash[k];}}
      
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	var x0=0,y0=0,x1=0,y1=0;
var divs="";
var offx=0,offy=0;
var moveable=false;
var hover='orange',normal='#336699';//color;
var index=100;//z-index;
var movedXY={x:0,y:0};
//��ʼ�϶�;
function startDrag(obj)
{
if(event.button==1)
{
   //����������;
   obj.setCapture();
   //�������;
   var win = obj.parentNode;
   var sha = win.nextSibling;
   //��¼���Ͳ�λ��;
   x0 = event.clientX;
   y0 = event.clientY;
   x1 = parseInt(win.style.left);
   y1 = parseInt(win.style.top);
   //��¼��ɫ;
   normal = obj.style.backgroundColor;
   //�ı���;
   obj.style.backgroundColor = hover;
   win.style.borderColor = hover;
   obj.nextSibling.style.color = hover;
   sha.style.left = x1 + offx;
   sha.style.top   = y1 + offy;
   moveable = true;
}
}
//�϶�;
function drag(obj,aId)
{
if(moveable)
{ // alert("move"+aId);
   var win = obj.parentNode;
   var sha = win.nextSibling;
   win.style.left = x1 + event.clientX - x0;
   win.style.top   = y1 + event.clientY - y0;
   sha.style.left = parseInt(win.style.left) + offx;
   sha.style.top   = parseInt(win.style.top) + offy;
   movedXY.x=event.clientX-x0;
   movedXY.y=event.clientY-y0;
  changeLinePoint(aId,event.clientX-x0,event.clientY-y0);
}
}
//ֹͣ�϶�;
function stopDrag(obj,aId)
{
if(moveable)
{ 
   setLintPosition(aId);//���������ߵ������յ�
   var win = obj.parentNode;
   var sha = win.nextSibling;
   var msg = obj.nextSibling;
   win.style.borderColor      = normal;
   obj.style.backgroundColor = normal;
   msg.style.color            = normal;
   sha.style.left = obj.parentNode.style.left;
   sha.style.top   = obj.parentNode.style.top;
   obj.releaseCapture();
   moveable = false;
}
}
//��ý���;
function getFocus(obj)
{
if(obj.style.zIndex!=index)
{
   index = index + 2;
   var idx = index;
   obj.style.zIndex=idx;
   obj.nextSibling.style.zIndex=idx-1;
}
}
//��С��;
function min(obj)
{
var win = obj.parentNode.parentNode;
var sha = win.nextSibling;
var tit = obj.parentNode;
var msg = tit.nextSibling;
var flg = msg.style.display=="none";
if(flg)
{
   win.style.height   = parseInt(msg.style.height) + parseInt(tit.style.height) + 2*2;
   sha.style.height   = win.style.height;
   msg.style.display = "block";
   obj.innerHTML = "0";
}
else
{
   win.style.height   = parseInt(tit.style.height) + 2*2;
   sha.style.height   = win.style.height;
   obj.innerHTML = "2";
   msg.style.display = "none";
}
}
//����һ������;
function xWin(id,w,h,l,t,tit,msg)
{
index = index+2;
this.id       = id;
this.width    = w;
this.height   = h;
this.left     = l;
this.top      = t;
this.zIndex   = index;
this.title    = tit;
this.message = msg;
this.obj      = null;
this.bulid    = bulid;
this.bulid();
}
//��ʼ��;
function bulid()
{
	
	
var str = ""
   + "<div id=xMsg" + this.id + " "
   + "style='"
   + "z-index:" + this.zIndex + ";"
   + "width:" + this.width + ";"
   + "height:" + this.height + ";"
   + "left:" + this.left + ";"
   + "top:" + this.top + ";"
   + "background-color:" + normal + ";"
   + "color:" + normal + ";"
   + "font-size:8pt;"
   + "font-family:Tahoma;"
   + "position:absolute;"
   + "cursor:default;"
   + "border:2px solid " + normal + ";"
   + "' "
   + "onmousedown='getFocus(this)'>"
    + "<div  ' "
    + "style='"
    + "background-color:" + normal + ";"
    + "width:" + (this.width-2*2) + ";"
    + "height:20;"
    + "color:white;"
    + "' "
    + "onmousedown='startDrag(this)' "
    + "onmouseup='stopDrag(this,\""+this.id+"\")' "
    + "onmousemove='drag(this,\""+this.id+"\")' "
    + "ondblclick='min(this.childNodes[1])'"
    + ">"
	+ "<span style='width:12;border-width:0px;color:white;font-family:webdings;' onclick='min(this)'>0</span>"
     + "<span style='padding-left:3px;'><font color=red>" + this.title + "</font></span>"
    + "</div>"
     + "<div style='"
     + "width:100%;"
     + "height:" + (this.height-20-4) + ";"
     + "background-color:white;"
     + "line-height:14px;"
     + "word-break:break-all;"
     + "padding:3px;"
     + "'>" + this.message + "</div>"
   + "</div>"
   + "<div id=xMsg" + this.id + "bg style='"
   + "width:" + this.width + ";"
   + "height:" + this.height + ";"
   + "top:" + this.top + ";"
   + "left:" + this.left + ";"
   + "z-index:" + (this.zIndex-1) + ";"
   + "position:absolute;"
   + "background-color:black;"
   + "filter:alpha(opacity=40);"
   + "'></div>";

document.body.insertAdjacentHTML("beforeEnd",str);
 divs+=str+"\r\n\r\n";
}
//���ش���
function ShowHide(id,dis){
	deleteLine(id);
	document.body.removeChild(document.getElementById("xMsg"+id));
	document.body.removeChild(document.getElementById("xMsg"+id+"bg"));
}

 function deleteLine(aTableId){
	 if(tabHt){//�������tabHt,�����ڻ���
	 tabHt.remove(aTableId);
	 //ɾ������
	 var outLines=outLineHt.getKeysByValue(aTableId);//����
	 for(var i=0,iLen=outLines.length;i<iLen;i++){
		 document.body.removeChild(document.getElementById(outLines[i]));
		 outLineHt.remove(outLines[i]);
		 inLineHt.remove(outLines[i]);
	 }
	 //ɾ������
	 var inLines=inLineHt.getKeysByValue(aTableId);//����
	 for(var i=0,iLen=inLines.length;i<iLen;i++){
		 document.body.removeChild(document.getElementById(inLines[i]));
		 outLineHt.remove(inLines[i]);
		 inLineHt.remove(inLines[i]);
	 }
	 } 
 }
 
// add by Simple_joy
  function changeLinePoint(aTableId,aLeft,aTop){
	 var vLine=null;
	  //�ı���ߵ����
	 if(outLineHt){//�������outLineHt,�����ڻ���
	var outLines=outLineHt.getKeysByValue(aTableId);//����
	 for(var i=0,iLen=outLines.length;i<iLen;i++){
		 vLine=document.getElementById(outLines[i]);
		//��λ��һ�� vml pt ��event.x px����, 1pt=0.75px
		 document.getElementById(outLines[i]).from=(vLine.startX+aLeft)+","+(vLine.startY+aTop);
	 }
	 //�ı����ߵ��յ�
	 var inLines=inLineHt.getKeysByValue(aTableId);//����
	 for(var i=0,iLen=inLines.length;i<iLen;i++){
		 vLine=document.getElementById(inLines[i]);
		 document.getElementById(inLines[i]).to=(vLine.endX+aLeft)+","+(vLine.endY+aTop);
	 }
	 }
  }
  function setLintPosition(aTableId){
	  var vLine=null;
	  //�ı���ߵ����
	  if(outLineHt){//�������outLineHt,�����ڻ���
	var outLines=outLineHt.getKeysByValue(aTableId);//����
	 for(var i=0,iLen=outLines.length;i<iLen;i++){
		 vLine=document.getElementById(outLines[i]);
		//��λ��һ�� vml pt ��event.x px����, 1pt=0.75px
		 document.getElementById(outLines[i]).startX=(vLine.startX+movedXY.x);
		 document.getElementById(outLines[i]).startY=(vLine.startY+movedXY.y);
	 }
	 //�ı����ߵ��յ�
	 var inLines=inLineHt.getKeysByValue(aTableId);//����
	 for(var i=0,iLen=inLines.length;i<iLen;i++){
		 vLine=document.getElementById(inLines[i]);
		 document.getElementById(inLines[i]).endX=(vLine.endX+movedXY.x);//vLine.endX+aLeft��Ĳ���
	     document.getElementById(inLines[i]).endY=(vLine.endY+movedXY.y);
		
	 }
	 movedXY.x=0;
	 movedXY.y=0;
	  }
  }


  
  
  
  
  
  
  
  
  
  
  	var showLeft=0;
	var showTop=0;
	var flags={isDraw:false};
	var point={startX:0,startY:0,endX:0,endY:0,starttablename:"",startcolname:"",endtablename:"",endcolname:"",startcheck:false,endcheck:false};//��¼���,�յ�����,�����ƣ�������
    var databaseLineHt=new Htb();//���ݿ������еĹ�ϵ�ߣ����ڱ���ʱ���ظ��ж�
	var inLineHt=new Htb();//���� ���������
	var outLineHt=new Htb();//����
	//var	LineNameHt=new Htb();//������
    var flags={isDraw:false};
    var objLine=null;//��ǰ�ڻ�����
    var chooseLine=null;//��ǰѡ�й�ϵ�߶���
    var tabHt= new Htb();//ҳ�������еı�
    
    function f_clear(){
    	point.startX=0;
    	point.startY=0;
    	point.endX=0;
    	point.endY=0;
    	point.starttablename="";
    	point.startcolname="";
    	point.endtablename="";
    	point.endcolname="";
    	point.startcheck=false;
    	point.endcheck=false;
    	
    }
    function f_creatediv(aId,aWidth,aHight,aLeft,aTop,aTitle,aContent){
	       if(tabHt.contains(aId))
	    	   return false;//�� ����Ѿ�����  
	       else{
			 new xWin(aId,aWidth,aHight,aLeft,aTop,aTitle,aContent);
			 tabHt.put(aId,aId);
	       }
	}

	function f_setStartPoint(){
 	 	var objSrc=event.srcElement;
 	 	if(!flags.isDraw&&objSrc.tagName=="INPUT"){
 	 		point.startcheck=objSrc.checked;
 	 		objSrc.checked=true;
 	 		flags.isDraw=true;
 	 		//point.startX=event.x;
 	 		//point.startY=event.y;
 	 		//point.startX=objSrc.getBoundingClientRect().left;
 	 		//point.startY=objSrc.getBoundingClientRect().top;
 	 		point.starttablename=objSrc.tname;
 	 		point.startcolname=objSrc.colname;
 	 		
 	 	}
	}
	function f_drawline(){
		point.endX=event.x;
	 	point.endY=event.y;
				if(flags.isDraw&&objLine!=null){
					objLine.to.x=event.x;
					objLine.to.y=event.y;
					objLine.style.display="none";
				}
				//lineposition.value=point.startX+","+point.startY+":"+event.x+","+event.y;
		}
	function dragTip()
    {
			startObj=event.srcElement;
			
			var b = document.body;
			
			startx=event.clientX + b.scrollLeft ;
			starty=event.clientY + b.scrollTop;
			
		point.startX=startx;
 	 	point.startY=starty-30;
			
			
			
            var mm = function()
            { 
                    if(event.button == 1)
                    {		 if (typeof(dragtext)=='undefined')
                    {
                    	alert('dragtext');
                    	 }
							dragtext.style.display="";
							dragtext.innerHTML="<font color='red' size='5'>"+startObj.colname+"</font>";
							
							var iL = event.clientX + b.scrollLeft;
							var iT = event.clientY + b.scrollTop;
							
							
                            dragtext.style.pixelLeft = iL+10;
                            dragtext.style.pixelTop = iT;
                    }
                    else{
						document.detachEvent("onmousemove", mm)
						dragtext.style.display="none";
					}
            }
            document.attachEvent("onmousemove", mm)
            
    }
	var aLine=null;
	function f_setEndPoint(){
		var objSrc=event.srcElement;
		if(flags.isDraw){
			if(objSrc.tagName=="INPUT"){
				point.endcheck=objSrc.checked;
				objSrc.checked=true;
				
				
				var b = document.body;
				
				point.endX=event.clientX + b.scrollLeft ;
	 	 		point.endY=event.clientY + b.scrollTop-30;
	 	 		point.endtablename=objSrc.tname;
	 	 		point.endcolname=objSrc.colname;
	 	 		if(point.starttablename==point.endtablename){
	 	 			//ͬһ����
	 	 			document.getElementById(point.starttablename+"."+point.startcolname).checked=point.startcheck;
	 	 			document.getElementById(point.endtablename+"."+point.endcolname).checked=point.endcheck;
	 	 			f_clear();
	 	 			flags.isDraw=false;
	 	 			return false;
	 	 		}
	 	 		var lineId=point.starttablename+"$"+point.startcolname+"-"+point.endtablename+"$"+point.endcolname;
	 	 		var aMark=lineId.indexOf("-");
	 	 		var lineIdAlias=lineId.substring(aMark+1,lineId.length)+"-"+lineId.substring(0,aMark);//ͬһ��ϵ�ߣ�ֻ�Ƿ����෴
	 	 		if(inLineHt.contains(lineId)||inLineHt.contains(lineIdAlias)){
	 	 			alert("�˹�ϵ�Ѵ���!");
	 	 			flags.isDraw=false;
	 	 			return ;
	 	 		}
	 	 		 aLine=f_creatLine(lineId,point.startX,point.startY,point.endX,point.endY);
	 	 		outLineHt.put(lineId,point.starttablename);
	 	 		inLineHt.put(lineId,point.endtablename);
			 	document.body.appendChild(aLine);
			 	
			 flags.isDraw=false;
			}else {
				flags.isDraw=false;
				var isChecked=document.getElementById(point.starttablename+"."+point.startcolname).checked;
				document.getElementById(point.starttablename+"."+point.startcolname).checked=isChecked;
				f_clear();
				}
		}
		
	}
	
	
   
    
    
	//�����һ����
  function f_creatLine(aId,startX,startY,endX,endY){
	  var aLine=document.createElement("v:line");
	  	aLine.setAttribute('id',aId);
	  	aLine.setAttribute('startX',startX);
	  	aLine.setAttribute('startY',startY);
	  	aLine.setAttribute('endX',endX);
	  	aLine.setAttribute('endY',endY);
		aLine.style.position="absolute";
		
		aLine.onclick=function (){
			if(chooseLine!=null){
				chooseLine.strokecolor='red';
				chooseLine=this;
				chooseLine.strokecolor='black';
			}else{
				chooseLine=this;
				chooseLine.strokecolor='black';
			}

					}
		
		aLine.setAttribute('strokecolor','red');
		aLine.setAttribute('from',startX+","+startY);
		aLine.setAttribute('to',endX+","+endY);
	
	var arrowobj=document.createElement("v:stroke");
		arrowobj.setAttribute('EndArrow','Classic');
		aLine.appendChild(arrowobj);
		aLine.style.zIndex = "10000";
		aLine.setAttribute('alt',aId);
		
		//aLine.alt=aId;
		return aLine;
  }
  function f_deldatabase(aId){//�����ݿ���ɾ���ù�ϵ
	    var xml = new ActiveXObject("Microsoft.XMLHTTP"); 
		var param="srelation="+aId+"&stype=del";
		xml.open("POST","view.do?id=108",false);//��ͬ����ʽͨ��  
		xml.setrequestheader("content-length",param.length);  
		xml.setrequestheader("content-type","application/x-www-form-urlencoded");  
		xml.setRequestHeader("charset", "GBK");
		xml.send(param);  
  }
  function f_delLine(){//ɾ�� ��ǰѡ�й��߶���
	  if(chooseLine!=null){
			if(!confirm("ȷ��Ҫɾ���������� ")) return false;
			f_deldatabase(chooseLine.id);
			document.body.removeChild(chooseLine);
			databaseLineHt.remove(chooseLine.id);
			outLineHt.remove(chooseLine.id);
			inLineHt.remove(chooseLine.id);
			chooseLine=null;
	  }else{
		  alert("��ѡ��Ҫɾ���Ĺ�ϵ��");
		  return false;
	  }
		  }
  function f_update_package(aRelation,atype,tables){
	  	var packageId="00000000000000000000";
		var xml = new ActiveXObject("Microsoft.XMLHTTP"); 
		var param="srelation="+aRelation+"&stype="+atype+"&packageId="+packageId+"&tables="+tables;
		xml.open("POST","view.do?id=108",false);//��ͬ����ʽͨ��  
		xml.setrequestheader("content-length",param.length);  
		xml.setrequestheader("content-type","application/x-www-form-urlencoded");  
		xml.setRequestHeader("charset", "GBK");
		xml.send(param);  
		var res = xml.responseText;
		return res;
  }
  function f_save(){//�����ϵ��
	  var sResult="";
	  var aType="add";
	  var iCount=inLineHt.count();
	  var sLineId=inLineHt.getKeys();
	  var tables="";
	 if(iCount>0)
	   tables=inLineHt.getValues().join(",")+","+outLineHt.getValues().join(",");
	 // alert(tables);
	 // return false;
	  var snewLineInfo="";
	  var aId=null;
	  var i=0;
	  while(i<iCount){
		  i++;
		  aId=sLineId.shift();
		  if(!databaseLineHt.contains(aId)){//�жϴ˹�ϵ�Ƿ��Ѵ���,������,���ʾΪ������ϵ�ߣ�׷�ӵ�sLineInfo
			  snewLineInfo+=aId;//�ߵ�ID
			  snewLineInfo+=",";
		  }
	  }
	  if(snewLineInfo==""){
		 // alert("����ɹ�");//��ʵû������µĹ�ϵ��
			  alert("û���µı��ϵ������Ҫ���棡");
			  return;
	  }else{
		 snewLineInfo=snewLineInfo.substring(0,snewLineInfo.length-1);
	  	 sResult= f_update_package(snewLineInfo,aType,tables);
	  	if(sResult.indexOf("success")!=-1){
				alert("����ɹ�");
				//snewLineInfo="";
		}else 
			alert("����ʧ��");
	 }
  }
  
//��ҳ���ϻ��ѵ����֮��Ĺ�ϵ��
function f_showLine(relation){
	var lineId=relation.replace(".","$").replace(".","$").replace("=","-");
	if(inLineHt.contains(lineId)||outLineHt.contains(lineId))return false;

	var aLine=null;
	var iMark=relation.indexOf("=");
	var startId=relation.substring(0,iMark);
	var endId=relation.substring(iMark+1,relation.length);
	//if(!(tabHt.contains(startId)&&tabHt.contains(endId)))return false;
	var startElement=document.getElementById(startId);
	
	if(startElement){
		startElement.checked=true;
		point.startX=startElement.getBoundingClientRect().left-showLeft+6;
		point.startY=startElement.getBoundingClientRect().top-showTop-24;
	}
	
	var endElement=document.getElementById(endId);
	if(endElement==null) return false;
    if(endElement){
    	endElement.checked=true;
    	point.endX=endElement.getBoundingClientRect().left-showLeft+6;
    	point.endY=endElement.getBoundingClientRect().top-showTop-24;
    }
	if(point.endX!=0&&point.endY!=0&&point.startX!=0&&point.startY!=0){//ȷ����������ҳ����
		
		databaseLineHt.put(lineId,lineId);
		outLineHt.put(lineId,startId.substring(0,startId.indexOf(".")));
		inLineHt.put(lineId,endId.substring(0,endId.indexOf(".")));
		aLine=f_creatLine(lineId,point.startX,point.startY,point.endX,point.endY);
		document.body.appendChild(aLine);
		aLine=null;
	}
	point.startX=0;
	point.startY=0;
	point.endX=0;
	point.endY=0;
	
}