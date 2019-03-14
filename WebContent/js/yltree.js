var str_Sys_NewItemUrl="";
function initSysPramFunction() {
	//for old browsers
	window["undefined"] = window["undefined"];
	//String class extend
	String.isInstance = function (_string) {
		return (typeof (_string) === "string");
	};
	String.isEmpty = function (str) {
		return (typeof (str) === "undefined" || str === null || (str.length === 0));
	};
	String.isNotEmpty = function (str) {
		return (!String.isEmpty(str));
	};
	String.prototype.trim = function () {
		return this.replace(/(^[\s]*)|([\s]*$)/g, "");
	};
	String.prototype.getAttribute = function (name) {
		var reg = new RegExp("(^|;|\\s)" + name + "\\s*:\\s*([^;]*)(\\s|;|$)", "i");
		if (reg.test(this)) {
			return RegExp.$2.replace(/[\x0f]/g, ";");
		}
		return null;
	};
	String.prototype.cnLength = function () {
		return ((this.replace(/[^x00-xFF]/g, "**")).length);
	};
	//Array class extend
	Array.isInstance = function (obj) {
		return Object.prototype.toString.call(obj) === '[object Array]';
	};
	Array.prototype.add = function (o) {
		this.push(o);
	};
	Array.prototype.indexOf = function (o) {
		for (var i = 0, len = this.length; i < len; i = i + 1) {
			if (this[i] != null && typeof(this[i].equals)=='function' && this[i].equals(o)) {
				return i;
			}
			if (this[i] == o) {
				return i;
			}
		}
		return -1;
	};
	Array.prototype.equals = function (_array) {
		if(this == _array){
			return true;
		}
		if(!Array.isInstance(_array)){
			return false;
		}
		if(this.length != _array.length){
			return false;
		}
		for (var i = 0, len = this.length; i < len; i = i + 1) {
			var o1 = this[i];
			var o2 = _array[i];
			if(o1 != o2){
				if(!(typeof(o1.equals)=='function' && o1.equals(o2))){
					return false;
				}
			}
		}
		return true;
	};	
	Array.prototype.remove = function (o) {
		var index = this.indexOf(o);
		if (index != -1) {
			this.splice(index, 1);
			return true;
		} else {
			return false;
		}
	};
	Array.prototype.contains = function (o) {
		return this.indexOf(o) != -1;
	};
	Array.prototype.containsAll = function (oArray) {
		if (this == oArray) {
			return true;
		}
		for (var i = 0; i < oArray.length; i = i + 1) {
			var o = oArray[i];
			if (!this.contains(o)) {
				return false;
			}
		}
		return true;
	};
	//Date class extend
	Date.isInstance = function (obj) {
		return	(Object.prototype.toString.call(obj) === '[object Date]');
	};
	//Function class extend
	Function.isInstance = function (obj) {
		return Object.prototype.toString.call(obj) === '[object Function]';
	};
	//Number class extend
	Number.isInstance = function (obj) {
		return Object.prototype.toString.call(obj) === '[object Number]';
	};
	//Boolean class extend
	Boolean.isInstance = function (obj) {
		return Object.prototype.toString.call(obj) === '[object Boolean]';
	};
}
var bSysParamInitialize=false;
getUniqueID = function(){
	return ('_' + (new Date().getTime()) + '_' + parseInt(Math.random()*10000));
};
var ylt= ylt ||{};
ylt.Tree = ylt.Tree || {};
yltTree =ylt.Tree=function(config){
	if(!bSysParamInitialize){
		initSysPramFunction();
		bSysParamInitialize=true;
	}
	this.nodeHash = {};
	this.root = null;
	this._id = getUniqueID();
	this.iconPath = gs_root+'/images/tree/';
	this.clickListeners = [];
	this.clickCheckListeners=[];
	this.element = document.createElement('div');
	this.element.className='ylTree';
	this.container = null;
	this.focusNode = null;
	this.on=this.addListener;
	this.onCheck=this.addCheckListener;
	this.initialize.apply(this, arguments);
	
};

yltTree.prototype={
	initialize : function(config){
		var renderTo = config['renderTo'];
		this.container = (String.isInstance(renderTo) ? document.getElementById(renderTo) : renderTo ) || document.body;
		var handler= config['handler'];
		if(Function.isInstance(handler)){
			this.addListener('click',handler);
		}
		var iconPath = config['iconPath'];
		if(String.isInstance(iconPath)){
			this.iconPath = iconPath;
		}
		
		var node = new TreeNode(config.root);
		this.setRootNode(node);
	},
	isSelectTree:false,
	pathSeparator: "/",
	getRootNode : function(){
		return this.root;
	},
	setRootNode : function(node){
		this.root = node;
		node.ownerTree = this;
		this.registerNode(node);
		node.cascade((function(node){
			this.registerNode(node);
		}),this);
	},
	getNodeById : function(id){
		return this.nodeHash[id];
	},
	registerNode : function(node){
		this.nodeHash[node.id] = node;
	},
	unregisterNode : function(node){
		delete this.nodeHash[node.id];
	},
	fushTree:function(_strDiv){
		var strScript="";
		
		var url = location.href; 
		var paraString = url.substring(url.indexOf("?")+1,url.length);
		if(paraString!="")paraString="&"+paraString;
		
	
			var e =$(_strDiv);
			if(e.className=="yltree"){
			
			var strClick="";
			if(e.attributes["treeclick"]!=null)
				strClick="&sys_click="+e.attributes["treeclick"].nodeValue;
			
			var strTreeName="";
			if(e.attributes["treename"]!=null)
				strTreeName="&sys_treename="+e.attributes["treename"].nodeValue;
			
			strScript+=getTx("sys_type=singtree&sys_dataid="+
							e.attributes["dataid"].nodeValue+"&sys_nodeid="+
							e.attributes["nodecode"].nodeValue+"&sys_nodepid="+
							e.attributes["nodepcode"].nodeValue+"&sys_nodenm="+
							e.attributes["nodename"].nodeValue+"&sys_randerid="+
							e.id+strClick+strTreeName+paraString,gs_root+"/comp");
			}
		
		e.innerHTML="";
		if(strScript!=""){			
			eval(strScript);
		}
		
	},
	initTree:function(){
	
		var arrSels=document.getElementsByTagName("div");
		var n = arrSels.length;
		var strScript="";
		
		var url = location.href; 
		var paraString = url.substring(url.indexOf("?")+1,url.length);
		if(paraString!="")paraString="&"+paraString;
		
		for (var i = 0; i < n; i++) {
			var e = arrSels[i];
			if(e.className=="yltree"){
			
			var strClick="";
			if(e.attributes["treeclick"]!=null)
				strClick="&sys_click="+e.attributes["treeclick"].nodeValue;
			
			var strTreeName="";
			if(e.attributes["treename"]!=null)
				strTreeName="&sys_treename="+e.attributes["treename"].nodeValue;
			//gs_root="/yldebug";
			strScript+=getTx("sys_type=singtree&sys_dataid="+
							e.attributes["dataid"].nodeValue+"&sys_nodeid="+
							e.attributes["nodecode"].nodeValue+"&sys_nodepid="+
							e.attributes["nodepcode"].nodeValue+"&sys_nodenm="+
							e.attributes["nodename"].nodeValue+"&sys_randerid="+
							e.id+strClick+strTreeName+paraString,gs_root+"/comp");
			}
		}
		if(strScript!=""){			
			eval(strScript);
		}
		
	},
	render : function(){
		this.element.innerHTML = '';
		this.root.render();
		if(this.container){
			this.container.appendChild(this.element);
		}
		this.initEvent();
	},
	getIcon : function(icontype){
		return this.iconPath + this.icon[icontype]
	},
	getIconByType : function(type){
		return type;
	},
	initEvent : function(){
		var _this = this;
		if(str_Sys_NewItemUrl!=""){
		this.element.oncontextmenu=function(event){
			var event = event || window.event;
			
			var elem=(event.srcElement || event.target);
			var _type = elem['_type_'];
			if(typeof(_type) === undefined){
				return;
			}
			elem = elem.parentNode || elem.parentElement;
			var node = _this.nodeHash[ elem.indexId ];
			
			//alert(node.attributes.nodeCode);
			var objTreeMenu=$("sys_tree_pop_menu");
			if(objTreeMenu==null){
				objTreeMenu=document.createElement("div");
				objTreeMenu.setAttribute('id',"sys_tree_pop_menu");
				objTreeMenu.style.width="100px";	
				objTreeMenu.style.background="white";
				objTreeMenu.style.border="1px solid gray";
				objTreeMenu.style.position="absolute";
				objTreeMenu.style.left=event.x+"px";
				objTreeMenu.style.top=event.y+"px";
				objTreeMenu.innerHTML="<table width='100%' border='0'>"+
				"<tr><td onmouseover=\"this.style.background='#6c6c6c';this.style.color='red';\"  onmouseout=\"this.style.background='white';this.style.color='black';\" onclick=\"miniWin('��������','','View?SPAGECODE=1426495400035&pcode="+node.attributes.nodeCode+"',400,150,'','');\">�½�</td></tr>"+
				"<tr><td>�޸�</td></tr>"+
				"<tr><td>ɾ��</td></tr>"+
				//"<tr><td><input id='sys_tree_pop_menu_input' onblur=\"$('sys_tree_pop_menu').style.display='none';\" type='text' style='width:0px;height:0px;border:0px;'></td></tr>"+
				"</table>";
				//alert(this.element.attributes.nodeCode);
				document.body.appendChild(objTreeMenu);
			}else{
				objTreeMenu.style.display="";
				objTreeMenu.style.left=event.x+"px";
				objTreeMenu.style.top=event.y+"px";
			}
			_this.setFocusNode(node);
			return false;
		};
		}
		this.element.setAttribute("isSelectTree",this.isSelectTree);
		this.element.onclick=function(event){
			var event = event || window.event;
			var elem=(event.srcElement || event.target);
			var _type = elem['_type_'];
			if(typeof(_type) === undefined){
				return;
			}
			elem = elem.parentNode || elem.parentElement;
			if(_type == 'clip'){
				if(elem.indexId!=null){
					var node = _this.nodeHash[ elem.indexId ];
					if(node.isExpand){
						node.collapse();
					}else{
						node.expand();
					}
				}
			}else if(_type == 'icon' || _type == 'text'){
				var node = _this.nodeHash[elem.indexId];
				for(var i=0; i < _this.clickListeners.length; i++){
					_this.clickListeners[i](node);
				}
				_this.setFocusNode(node);
			}else if(_type == 'checked'){
				var node = _this.nodeHash[elem.indexId];
				if(this.getAttribute("isSelectTree")=="true"){
					node.onCheck_sing();
					for(var i=0; i < _this.clickCheckListeners.length; i++){
					_this.clickCheckListeners[i](node);
				}
				}else
					node.onCheck();
			}
		};
	},
	
	getChecked : function(name){
		var checkeds = [];
		name = name||'id';
		for(var k in this.nodeHash){
			var node = this.nodeHash[k];
			if(node.checked==1){
				var value = node.attributes[name];
				if(value != null){
					checkeds.push(value);
				}
			}
		}
		return checkeds;
	},
	addListener : function(type,handler){
		if(Function.isInstance(type)){
			handler=type;
			type === 'click'
		}
		this.clickListeners.push(handler);	
	},
	addCheckListener:function(type,handler){
		if(Function.isInstance(type)){
			handler=type;
			type === 'click'
		}
		this.clickCheckListeners.push(handler);	
	},
	setFocusNode : function(node){
		if(this.focusNode){
			this.focusNode.unselect();
		}
		this.focusNode = node;
		if(node.parentNode){
			node.parentNode.expand();
		}
		node.select();
	},
	toString : function(){
		return "[Tree"+(this.id?" "+this.id:"")+"]";
	},
	collapseAll : function(){
		if(this.root){
			this.root.collapseChildNodes(true);
		}
	},
	expandAll : function(){
		if(this.root){
			this.root.expand(true);
		}
	}
};


yltTree.prototype.icon = {
	root				: 'root.gif',
	folder			: 'folder.gif',
	folderOpen	: 'folderopen.gif',
	node				: 'page.gif',
	empty				: 'empty.gif',
	line				: 'line.gif',
	join				: 'join.gif',
	joinBottom	: 'joinbottom.gif',
	plus				: 'plus.gif',
	plusBottom	: 'plusbottom.gif',
	minus				: 'minus.gif',
	minusBottom	: 'minusbottom.gif',
	nlPlus			: 'nolines_plus.gif',
	nlMinus			: 'nolines_minus.gif',
	checkbox0		:	'checkbox_0.gif',
	checkbox1		:	'checkbox_1.gif',
	checkbox2		:	'checkbox_2.gif',
	org					: 'org.gif',
	edp					: 'edp.gif',
	emp					: 'emp.gif'
};


TreeNode=function(attributes) {
	this['attributes'] = attributes || {};
	this['html-element'] = false;//null
	if(!attributes.id){
		attributes.id = getUniqueID();
	}
	this.id = attributes.id;
	this.parentNode = null;
  this.childNodes = [];
	this.parentNode = null;
	this.lastChild = null;
	this.firstChild = null;
	this.previousSibling = null;
	this.nextSibling = null;
	this.childrenRendered = false;
	this.isExpand = false;

	this.checked = this['attributes']['checked'];
	this.checked = this.checked==null ? false : this.checked;

	this.leaf = this.attributes.leaf;
	var children = attributes.children || [];
	for(var i=0,j=children.length;i<j;i++){
		var node = new TreeNode(children[i]);
		this.appendChild(node);
	}
};

TreeNode.prototype={
	initEl : function(){
		this['html-element']={};
		this['html-element']['element'] = document.createElement('div');
		this['html-element']['line']	 = document.createElement('span');
		this['html-element']['clip']	 = document.createElement('img');
		this['html-element']['icon']	 = document.createElement('img');
		this['html-element']['text']	 = document.createElement('span');
 		this['html-element']['checkbox'] = document.createElement('img');
		this['html-element']['child'] = document.createElement('div');
		this['html-element']['element'].appendChild(this['html-element']['line']);
		this['html-element']['element'].appendChild(this['html-element']['clip']);
		this['html-element']['element'].appendChild(this['html-element']['icon']);
		this['html-element']['element'].appendChild(this['html-element']['checkbox']);	
		this['html-element']['element'].appendChild(this['html-element']['text']);
		this['html-element']['element'].appendChild(this['html-element']['child']);
		this['html-element']['text'].className='TreeNode';
		this['html-element']['element'].noWrap='true';
		this['html-element']['line']['_type_'] ='line';
		this['html-element']['clip']['_type_'] ='clip';
		this['html-element']['icon']['_type_'] ='icon';
		this['html-element']['text']['_type_'] ='text';
		this['html-element']['checkbox']['_type_'] ='checked';
		this['html-element']['child'].style.display='none';
		if(this.checked===false){
			this['html-element']['checkbox'].style.display='none';
		}
	},
	render : function(){
		if(!this['html-element']){
			this.initEl();
		}
		if(this.isRoot()){
			this.ownerTree.element.appendChild(this['html-element']['element']);
			this.expand();
		}else{
			this.parentNode['html-element']['child'].appendChild(this['html-element']['element']);
		}
		this.paintPrefix();
		this['html-element']['element'].indexId = this.id;
	},
	paintPrefix : function(){
		this.paintLine();
		this.paintClipIcoImg();
		this.paintCheckboxImg();
		this.paintIconImg();
		this.paintText();
	},
	paintLine : function(){
		var ownerTree = this.getOwnerTree();
		this['html-element']['line'].innerHTML = '';
		var pathNodes = this.getPathNodes();
		for(var i = 1 ,count = pathNodes.length-1 ; i < count ; i++){
				var node = pathNodes[i];
				var img = document.createElement('img');
				if( node.isLast()){
					img.src = ownerTree.getIcon('empty');
				}else{
					img.src = ownerTree.getIcon('line');
				}
				this['html-element']['line'].appendChild(img);
		}
	},
	paintClipIcoImg : function(){
		if(this.isRoot()){
			this['html-element']['clip'].style.display='none';//����ʾ���ڵ��clip
			return;
		}
		var ownerTree = this.getOwnerTree();
		var icon = 'empty';
		if(this.isRoot()){
			icon = this.isExpand ? 'nlMinus':'nlPlus';
		}else{
			if(this.isLeaf()){ //��Ҷ�ڵ�
				if(this.isLast()){
					icon = 'joinBottom';
				}else{
					icon = 'join';
				}
			}else{ //��Ҷ�ڵ�
				if(this.isExpand){ //չ��
					if(this.isLast()){
						icon = 'minusBottom';
					}else{
						icon = 'minus';
					}
				}else{ //�۵�
					if(this.isLast()){
						icon = 'plusBottom';
					}else{
						icon = 'plus';
					}
				}
			}
		};
		this['html-element']['clip'].src = ownerTree.getIcon(icon);
	},
	paintIconImg : function(){
		var ownerTree = this.getOwnerTree();
		var icon = this['attributes']['icon'];
		if(!icon){
			var type = this['attributes']['type'];
			if(type){
				icon = ownerTree.getIconByType(type);
			}
			if(!icon){
				if(this.isRoot()){
					icon = 'root';
				}else if(this.isLeaf()){
					icon = 'node';
				}else if(this.isExpand){
					icon = 'folderOpen';
				}else{
					icon = 'folder';
				}
			}
		}
		if(!!this.attributes['sys_ico_style'])
			this['html-element']['icon'].src=ownerTree.iconPath + this.attributes['sys_ico_style']+ownerTree.icon[icon];
		else
		this['html-element']['icon'].src = ownerTree.getIcon(icon);
	if(this.isRoot())
		this['html-element']['icon'].id="treerootnodeico";
	},
	paintCheckboxImg : function(){
		var ownerTree = this.getOwnerTree();
		var checked = this.checked;
		if(this['html-element']){
			this['html-element']['checkbox'].src = ownerTree.getIcon(((checked==2)?'checkbox2':(checked==1)?'checkbox1':'checkbox0'));
		}
	},	
	paintText : function(){
		var text = 	this['attributes']['text'];
		this['html-element']['text'].style.cursor='hand';
		this['html-element']['text'].title=text;
		this['html-element']['text'].innerText=text;
		this['html-element']['text'].textContent=text;
		if(this.isRoot())
			this['html-element']['text'].id="treerootnode";
	},
	paintChildren : function(){
		if(!this.childrenRendered){
			this['html-element']['child'].innerHTML = '';
			this.childrenRendered = true;
			var childNodes = this.childNodes;
			for(var i=0;i < childNodes.length;i++){
				childNodes[i].render();
			}
		};
	},
	collapse : function(deep){
		this.isExpand=false;
		this['html-element']['child'].style.display='none';
		this.paintIconImg();
		this.paintClipIcoImg();
		if(deep){
			this.collapseChildNodes(deep);
		}
	},
	//private
	collapseChildNodes : function(deep){
		var cs = this.childNodes;
		for(var i = 0, len = cs.length; i < len; i++) {
			cs[i].collapse(deep);
		}
	},
	expand : function(deep){
		if(!this.isLeaf()&&this.childNodes.length>0){
			this.isExpand=true;
			this.paintChildren();
			this['html-element']['child'].style.display='block';
		}
		this.paintIconImg();
		this.paintClipIcoImg();
		if(deep){
			this.expandChildNodes(deep);
		}
	},
	//private
	expandChildNodes : function(deep){
		var cs = this.childNodes;
		for(var i = 0, len = cs.length; i < len; i++) {
			cs[i].expand(deep);
		}
	},
	select : function(){
		this.isSelect = true;
		this['html-element']['text'].style.backgroundColor='#CCCCFF';
	},
	unselect : function(){
		this.isSelect = false;
		this['html-element']['text'].style.backgroundColor='';
	},
	getEl :  function(){
		return this['html-element'];
	},
	setCheck : function(checked){
		if(checked==2||checked==3){
			var childNodes = this.childNodes;
			var count = childNodes.length;
			if(count==0){
				this.checked=checked==2?0:1;
			}else{
				var checked1 = 0;
				var checked2 = 0;
				for(var i=0;i<count;i++){
				var checked = childNodes[i].checked;
					if(checked==1){
						checked1++;
					}else if(checked==2){
						checked2++;
					}
				}
				this.checked = (childNodes.length==checked1) ? 1 : (checked1>0||checked2>0) ? 2 : 0;
			}
		}else{
			this.checked=checked;
		}
		this.paintCheckboxImg();
	},
	onCheck_sing : function(){
		if(this.checked!==false){
			if(this.checked==1){
			
					this.setCheck(0);
			
	
			}else{
				this.setCheck(1);

			}
		}
	},
	
	onCheck : function(){
		if(this.checked!==false){
			if(this.checked==1){
				this.cascade((function(checked){
					this.setCheck(checked);
				}),null,0);
				this.bubble((function(checked){
					this.setCheck(checked);
				}),null,2);
			}else{
				this.cascade((function(checked){
					this.setCheck(checked);
				}),null,1);
				this.bubble((function(checked){
					this.setCheck(checked);
				}),null,3);
			}
		}
		if(!!window.do_sysTreeCheckEvent)
			do_sysTreeCheckEvent();
	},
	isRoot : function(){
		return (this.ownerTree!=null) && (this.ownerTree.root === this);
	},
	isLeaf : function(){
		return this.childNodes.length===0;
		//return this.leaf === true;
  },
	isLast : function(){
		return (!this.parentNode ? true : this.parentNode.lastChild == this);
	},
	isFirst : function(){
		return (!this.parentNode ? true : this.parentNode.firstChild == this);
	},
	hasChildNodes : function(){
		return !this.isLeaf() && this.childNodes.length > 0;
	},
	// private
	setFirstChild : function(node){
		this.firstChild = node;
	},
	//private
	setLastChild : function(node){
		this.lastChild = node;
	},
	appendChild : function(node){
		var multi = false;
		if(Array.isInstance(node)){
			multi = node;
		}else if(arguments.length > 1){
			multi = arguments;
		}
		if(multi){
    	for(var i = 0, len = multi.length; i < len; i++) {
				this.appendChild(multi[i]);
			}
		}else{
			//>>beforeappend
      var oldParent = node.parentNode;
      //>>beforemove
      if(oldParent){
				oldParent.removeChild(node);
			}
			var index = this.childNodes.length;
      if(index == 0){
				this.setFirstChild(node);
      }
			this.childNodes.push(node);
			node.parentNode = this;
			//
			var ps = this.childNodes[index-1];
			if(ps){
				node.previousSibling = ps;
				ps.nextSibling = node;
			}else{
				node.previousSibling = null;
			}
			node.nextSibling = null;
      this.setLastChild(node);
			node.setOwnerTree(this.getOwnerTree());
			//>>append
			//if(oldParent) >>move

			if(node && this.childrenRendered){
				node.render();
				if(node.previousSibling){
					node.previousSibling.paintPrefix();//paintLine();
				}
      }
      if(this['html-element']){
      	this.paintPrefix();
      }
			return node;//true
		}
	},
	removeChild : function(node){
	  var index = this.childNodes.indexOf(node);
	  if(index == -1){
	      return false;
	  }
		//>>beforeremove
		this.childNodes.splice(index, 1);
		var previousSibling = node.previousSibling;
		if(node.previousSibling){
	  	node.previousSibling.nextSibling = node.nextSibling;
		}
		if(node.nextSibling){
	  	node.nextSibling.previousSibling = node.previousSibling;
		}
		if(this.firstChild == node){
	  	this.setFirstChild(node.nextSibling);
		}
		if(this.lastChild == node){
	  	this.setLastChild(node.previousSibling);
		}
		node.setOwnerTree(null);
		//clear
		node.parentNode = null;
		node.previousSibling = null;
		node.nextSibling = null;
		//>>remove UI
		if(this.childrenRendered){
			if(node['html-element']&&node['html-element']['element']){
				this['html-element']['child'].removeChild(node['html-element']['element'])	
			}
			if(this.childNodes.length==0){
				this.collapse();
			}
    }
    if(this['html-element']){
    	this.paintPrefix();
    }
    if(previousSibling){
    	if(previousSibling['html-element']){
				previousSibling.paintPrefix();
			}
    	previousSibling.cascade((function(){
    		if(this['html-element']){
					this.paintPrefix();
				}
			}));
		}
		return node;
	},
	insertBefore : function(node, refNode){
		if(!refNode){
			return this.appendChild(node);
		}
		//�ƶ�λ��������λ��(����Ҫ�ƶ�)
		if(node == refNode){
			return false;
		}
		var index = this.childNodes.indexOf(refNode);
		var oldParent = node.parentNode;
		var refIndex = index;
		//���ӽڵ㣬����������ƶ�
		if(oldParent == this && this.childNodes.indexOf(node) < index){
			refIndex--;
		}
		if(oldParent){
			oldParent.removeChild(node);
		}
		//���ýڵ���ϵ
		if(refIndex == 0){
			this.setFirstChild(node);
		}
		this.childNodes.splice(refIndex, 0, node);
		node.parentNode = this;
		var ps = this.childNodes[refIndex-1];
		if(ps){
			node.previousSibling = ps;
			ps.nextSibling = node;
		}else{
			node.previousSibling = null;
		}
		node.nextSibling = refNode;
		refNode.previousSibling = node;
		node.setOwnerTree(this.getOwnerTree());
		return node;
	},
	replaceChild : function(newChild, oldChild){
		this.insertBefore(newChild, oldChild);
		this.removeChild(oldChild);
		return oldChild;
	},
	indexOf : function(child){
		return this.childNodes.indexOf(child);
	},
	getOwnerTree : function(){
		if(!this.ownerTree){
			var p = this;
			while(p){
				if(p.ownerTree){
					this.ownerTree = p.ownerTree;
					break;
				}
				p = p.parentNode;
			}
		}
		return this.ownerTree;
	},
	//��ýڵ����
	getDepth : function(){
  	var depth = 0;
		var p = this;
		while(p.parentNode){
			depth++;
			p = p.parentNode;
		}
		return depth;
	},
  //private
	setOwnerTree : function(tree){
		if(tree != this.ownerTree){
			if(this.ownerTree){
				this.ownerTree.unregisterNode(this);
			}
			this.ownerTree = tree;
			var cs = this.childNodes;
			for(var i = 0, len = cs.length; i < len; i++) {
				cs[i].setOwnerTree(tree);
			}
			if(tree){
				tree.registerNode(this);
			}
		}
	},
	getPathNodes : function(){
		var nodes = [];
		for(var parent=this; parent!=null; parent=parent.parentNode){nodes.push(parent);};
		return nodes.reverse();
	},
	getPath : function(attr){
		attr = attr || "id";
		var p = this.parentNode;
		var b = [this['attributes'][attr]];
		while(p){
			b.unshift(p.attributes[attr]);
			p = p.parentNode;
    }
		var sep = this.getOwnerTree().pathSeparator;
		return sep + b.join(sep);
	},
	//ð��(�������и��ڵ�)
	bubble : function(fn, scope, args){
  	var p = this;
		while(p){
			if(fn.call(scope || p, (args==null)?p:args) === false){
	    	break;
			}
	    p = p.parentNode;
		}
	},
	//�ٲ�(���������ӽڵ�)
	cascade : function(fn, scope, args){
		if(fn.call(scope || this, (args==null)?this:args) !== false){
			var cs = this.childNodes;
			for(var i = 0, len = cs.length; i < len; i++) {
				cs[i].cascade(fn, scope, args);
			}
    }
	},
	//����
	findChild : function(attribute, value){
		var cs = this.childNodes;
    for(var i = 0, len = cs.length; i < len; i++) {
			if(cs[i].attributes[attribute] == value){
      return cs[i];
     }
		}
		return null;
	},
  findChildBy : function(fn, scope){
      var cs = this.childNodes;
      for(var i = 0, len = cs.length; i < len; i++) {
      	if(fn.call(scope||cs[i], cs[i]) === true){
      	    return cs[i];
      	}
      }
      return null;
  },
  //�÷���δ��ȫʵ��
	sort : function(fn, scope){
		var cs = this.childNodes;
		var len = cs.length;
    if(len > 0){
			var sortFn = scope ? function(){fn.apply(scope, arguments);} : fn;
			cs.sort(sortFn);
			for(var i = 0; i < len; i++){
				var n = cs[i];
        n.previousSibling = cs[i-1];
        n.nextSibling = cs[i+1];
        if(i == 0){
        	this.setFirstChild(n);
         }
        if(i == len-1){
         	this.setLastChild(n);
				}
			}
		}
	},
  contains : function(node){
     var p = node.parentNode;
      while(p){
          if(p == this){
              return true;
          }
          p = p.parentNode;
      }
      return false;
  },
  toString : function(){
      return "[Node"+(this.id?" "+this.id:"")+"]";
  }
};
function sys_DoTreeOp(){
	  var objTreeOp=$("sys_tree_op");
	  if(objTreeOp!=null){
		  objTreeOp.className="bttoparea";
		  var objTree=$("testtree0");
		  if(objTree!=null)
			objTreeOp.style.width=objTree.offsetWidth+"px";
		  $("treerootnode").style.display="inline-block";
		  $("treerootnode").innerHTML="";
		  $("treerootnode").appendChild(objTreeOp);
		  $("treerootnodeico").style.display="none";
	  }
  }