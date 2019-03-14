	package com.bfkc.process;
	
	import java.io.PrintWriter;
	import java.io.StringWriter;
	import java.lang.reflect.InvocationTargetException;
	import java.lang.reflect.Method;
	import java.text.ParseException;
	import java.text.SimpleDateFormat;
	import java.util.Arrays;
	import java.util.Calendar;
	import java.util.Date;
	import java.util.Enumeration;
	import java.util.HashMap;
	import java.util.HashSet;
	import java.util.LinkedHashMap;
	import java.util.Map;
	import java.util.Set;
	import java.util.UUID;
	
	import javax.script.ScriptEngine;
	import javax.script.ScriptException;
	import javax.servlet.http.HttpServletRequest;
	import javax.servlet.http.HttpSession;
	
	import com.timing.impcl.MantraLog;
	import com.yulongtao.db.DBFactory;
	import com.yulongtao.db.FieldEx;
	import com.yulongtao.db.Record;
	import com.yulongtao.db.TableEx;
	import com.yulongtao.util.EString;
	import com.timing.impcl.MantraUtil;
	
	public class ProcessRunOperation {
		
		public static SimpleDateFormat strSdfYmd =  new SimpleDateFormat("yyyy-MM-dd");
		public static SimpleDateFormat strSdfYmdHms =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		private static String[] arrVarNames=new String[]{"username","user","role","branchid","date","dataset","username","splitbranchid","usercount","userip","userlogindate","userlogindatehm"};
		private static String[] arrVarValues=new String[]{"SYS_STRCURUSERNAME","SYS_STRCURUSER","SYS_STRROLECODE","SYS_STRBRANCHID","SYS_CURDATE","DATASET","SYS_STRCURUSERNAME","SYS_BRANCHID_SPLIT","SYS_USER_COUNT","SYS_STRCURUSER_IP","SYS_USER_LOGIN_DATE","SYS_CURDATE"};
		
	//	SYS_STRCURUSER //�û�����
	//	SYS_STRCURUSERNAME //�û�����
	//	SYS_STRBRANCHID //��֯����
	//	SYS_BRANCHID_SPLIT //�粿��
	//	SYS_STRROLECODE //�û���ɫ
	//	SYS_USER_COUNT //��¼����
	//	SYS_STRCURUSER_IP //��¼IP
	//	SYS_USER_LOGIN_DATE //��¼���� ��-��-��
	//	SYS_CURDATE //��¼ʱ�� ʱ:��
		/**
		 * �滻session,requestֵ
		 * @param _request
		 * @param _str
		 */
		public String replaceRequestVal(HttpServletRequest _request,String _str){
			HttpSession session = _request.getSession();
			Object strVal = session.getAttribute(_str);
	//		for(int i=0,j=arrVarValues.length;i<j;i++){
	//			if(_str.equals(arrVarValues[i])){
	//				strVal = session.getAttribute(arrVarValues[i]);
	//				break;
	//			}
	//		}
			return strVal==null?_str:strVal.toString();
		}
		
		public StringBuffer sb = new StringBuffer();
		
		//���鶨��:�ڵ�����,�ڵ��滻ֵ
		public String getNodeReplaceVal(HttpServletRequest _request,String _strkey,String _strField){
			HttpSession session = _request.getSession();
			int iLength=arrVarNames.length;
			Object strValue = "";
			for(int i=0;i<iLength;i++){
				if("date".equals(arrVarNames[i])){
					strValue=EString.getCurDateHH();
				}else {
					strValue=session.getAttribute(arrVarValues[i]);
				}
				strValue = (strValue==null||"".equals(strValue))?"":strValue;
				_strField=_strField.replace("{"+arrVarNames[i]+"}",strValue.toString());
			}
			return _strField;
		}
		
		/**
		 * ���½ڵ�����ֵ
		 * @param _request
		 * @param _strkey
		 * @param _strField
		 * @param _strRunId
		 */
	//	1500260373394$true$T_DQYZGZP.S_GLYQM,true,false,{user}|T_DQYZGZP.S_GLYQMSJ,true,false,{date}|T_DQYZGZP.S_ZHXGSJ,true,false,{date}|T_DQYZGZP.S_ZHXGR,true,false,{user}
		public void updateTabByFlowSet(HttpServletRequest _request,String _strkey,String _strField,String _strRunId,StringBuffer _sr){
			DBFactory dbf = new DBFactory();
			String strField = getNodeReplaceVal(_request, _strkey, _strField);
			Map<String,String> map = new HashMap<String, String>();
			Map<String,String> mapCon = new HashMap<String, String>();

			if("".equals(_strField)){return;}
			String[] strArrayTable = strField.split("`");
			
			for(int a=0,b=strArrayTable.length;a<b;a++){
				String strTemp = strArrayTable[a];
	//			strTemp = strTemp.substring(strTemp.lastIndexOf("$")+1,strTemp.length());
				strTemp = strTemp.substring(strTemp.indexOf("e$")+2,strTemp.length());
				if("".equals(strTemp))
				{
					continue;
				}
				String[] strArrayField = strTemp.split("\\|");
			
				for(int i=0,j=strArrayField.length;i<j;i++){
					String[] strArrayItem  = strArrayField[i].split(",",-1);
					String strWhere = "";
					
					String strTemp1 = strArrayItem[0];
					int ind = strTemp1.indexOf(".");
					if(ind<0){
						continue;
					}
					String strCou = strTemp1.substring(ind+1,strTemp1.length());//�ֶ�����
					String strTabName =strTemp1.substring(0, ind);//����
					
	//				String strTabName =strArrayItem[0].substring(0, strArrayItem[0].indexOf("."));//����
	//				String strCou = strArrayItem[0].substring(strArrayItem[0].indexOf("."),strArrayItem[0].length());//�ֶ�����
					String strVal = strArrayItem[4];
					if(strVal==null||"".equals(strVal)){
						continue;
					}
				
					String strAuditState = _request.getParameter("NO_sys_flow_state");//0����1ͨ��
					if("0".equals(strAuditState)){//����ȡֵ
						if(strVal.indexOf("{no:")>-1){
							strVal = strVal.substring(strVal.indexOf("{no:")+4,strVal.indexOf(":end}"));
						}else if(strVal.indexOf("{request:")>-1){
							strVal = strVal.replace("{request:", "");//strAuditComment {request:strAuditComment}
							strVal = strVal.replace("}", "");
							
							Object _obj = _request.getParameter(strVal+"");
							strVal = (_obj==null?"":_obj.toString());
							
						}else{
							continue;
						}
					}else{
						if(strVal.indexOf("{no:")>-1){
							String _strValTemp= strVal.substring(strVal.indexOf("{no:")+4,strVal.indexOf(":end}"));
							strVal = strVal.replace("{no:"+_strValTemp+":end}", "");
							if("".equals(strVal)){
								continue;
							}
						}
						
						if(strVal.indexOf("{number:")>-1){
							strVal = strVal.replace("{", "");
							strVal = strVal.replace("}", "");
							String[] strArrayNum = strVal.split(":",-1);
							if(!"".equals(strArrayNum[3])){//{number:143214235:����:����}
	//							T_DQYZGZP.S_GZPBH,true,false,{number:1504603191000:����:����}
	//							{number:1504603191000:����:����}
								strWhere =strWhere+ " and "+strCou +"=''";
								strVal = com.yulongtao.util.SerialUtil.getSerialNum(strArrayNum[1],_request);//TODO  ���к�
							}else{//{number:143214235:����:}
								strWhere =strWhere+  "and "+strCou+" like '%" +strArrayNum[2]+"'";
								strVal = com.yulongtao.util.SerialUtil.getSerialNum(strArrayNum[1],_request);//TODO  ���к�
							}
						}else if(strVal.indexOf("{request:")>-1){
							strVal = strVal.replace("{request:", "");//strAuditComment {request:strAuditComment}
							strVal = strVal.replace("}", "");
							
							Object _obj = _request.getParameter(strVal+"");
	//						new String(_obj.toString().getBytes("iso8859-1"),"UTF-8");
							String str = _obj.toString();
							try{
								strVal = (_obj==null?"":(new String(str.getBytes("iso8859-1"),"UTF-8")));
							}catch (Exception e) {
							 //   MantraLog.fileCreateAndWrite(e);
							}
						}else if(strVal.indexOf("{dataset:")>-1){
							strVal = strVal.replace("{dataset:", "");
							strVal = strVal.replace("}", "");
							String[] strValArry = strVal.split("\\|");
							for(int a1=0,b1=strValArry.length;a1<b1;a1++){
	//							strValArry[a1];
							}
							//ִ�����ݼ�
						}
					}
					mapCon.put(strTabName, strWhere);
					map.put(strTabName,(map.get(strTabName)==null?"":(map.get(strTabName).toString()+" , "))+strCou+" = '"+strVal+"' ");//�ֶ���
				
				}
			}
			
		
			try {
			
    			for (String key : map.keySet()) {
    			    	if(dbf==null){
    			    	    dbf=new DBFactory();
    			    	}
    			    //	update T_DQYZGZP set S_GZPZT = 'GZPZT022' , S_GZXKRQM_NAME = '��С��' , S_GZXKRQM = 'liuxiaofeng' where S_RUN_ID='5NwQQiikQlq7Dk4PVReLoQ'
    				dbf.sqlExe("update " + key + " set " + map.get(key) + " where S_RUN_ID='" + _strRunId + "' "+mapCon.get(key), true);
    				//dbf.sqlExe("update T_DQYZGZP set S_GZPZT = 'GZPZT022' , S_GZXKRQM_NAME = '��С��' , S_GZXKRQM = 'liuxiaofeng' where S_RUN_ID='5NwQQiikQlq7Dk4PVReLoQ'", true);
    			}
    			
    		} catch (Exception e) {
    		  //  MantraLog.fileCreateAndWrite(e);
    // 			String[] strArrayFlowLog22 = {"333","","",new Date()+"","","","updateTabByFlowSet",getErrorInfoFromException(e)};
    // 			insertFlowLog("1", strArrayFlowLog22);
    			e.printStackTrace();
    		}finally{
    			if(dbf!=null){dbf.close();}
    		}
		}
		public Boolean processStartNoAudit(String _strFlowId,String _strRunId,String _strVersion,String _strPageCode){
			TableEx tableEx =null;
			TableEx tableEx1 =null;
			Boolean b = true;
			String strFlowRunId = "";
			String strFlowId ="";
			String strVersion="";
			try{
				/**��������*/
				String strStartUser = "system";//������
				String strStartUserRole = "";//�����˽�ɫ
				String strStartUserBranch = "";//��������֯
				String strPageCode=_strPageCode;
				
				strFlowId = _strFlowId;//����ID
				strVersion = _strVersion;//�汾��
				 strFlowRunId = _strRunId;//����
				String strFlowType = "1";//0:������ 1:������ Ĭ��������
				
					/**0 ��ѯ���̽ڵ�*/
					String strStartNode = "";//����ڵ�NODE
					String strStartNodeBak = "";//����ڵ�NODE
					String strEndNodes = ",";//���н����ڵ�
					String strAuditMsgs = "";//������Ϣģ��
					String strTab="";//����
					tableEx = queryFlowNodeInfo(strFlowId,strVersion,"");
					int iCount = tableEx.getRecordCount();
					if(iCount<1){
						return false;
					}
					/**1 ���ҿ�ʼ�ڵ�*/
					Record record = null;
					for(int i=0;i<iCount;i++){
						record =  tableEx.getRecord(i);//1 ���� 3 ��ʼ   2����  4����
						if("3".equals(getColString("I_TYPE", record))){//��ʼ����
							strAuditMsgs = getColString("S_AUDIT_TZXX", record);
							strStartNodeBak = getColString("I_NODE_ID", record);
							//�����ʼ�ڵ��ж��Ƿ񷢽ڵ�
							if(queryFlowStartPerson(strStartUser,strStartUserRole,strStartUserBranch,getColString("S_AUDIT_BRANCH", record),getColString("S_AUDIT_ROLE", record),getColString("S_AUDIT_USER", record))==true){
								strStartNode = strStartNodeBak;
								strTab = getColString("S_TAB", record);
							}else if(querySqRole(getColString("S_AUDIT_SQRYATTR", record),getColString("S_AUDIT_SQRY", record), null, strFlowRunId,strStartUserRole)){
								strStartNode = strStartNodeBak;
								strTab = getColString("S_TAB", record);
							}
						}else if("4".equals(getColString("I_TYPE", record))){
							strEndNodes=strEndNodes+getColString("I_NODE_ID", record)+",";
						}
					}
					strStartNode= ("".equals(strStartNode)?strStartNodeBak:strStartNode);//�Ҳ�����ʼ�ڵ�������һ��
					if("".equals(strStartNode)){
						return false;
					}
					String strNodeIdNow = strStartNode;//�Ҳ����ڵ�,�ܳ��쳣
					
					/**2 ��ʼ�ڵ㸳ֵ*/
					String strAuditArrayyq=",";//����
					String strAuditNodes=strStartNode;//���нڵ�
					String strAuditState="3";//����״̬ 3���ύ
					String strNodeIdNext="";//���нڵ�
					String strNextAuditUser="";//�ڵ�������
					String strAuditOther =",,,,,,,";//����
					String strNownewDate = strSdfYmdHms.format(new Date());//��������
					int strNextAuditUserIndex =1;//��һ�����ڵ�����,������Ĭ��Ϊ1
					String strAuditUsers = strStartUser;//����������
					String strSonFlow = "";
	//				String strAuditTg = "";//�Ƿ�����
					String strIsOver = "0";//�Ƿ����
					Record rd = null;//��ȡ��һ�ڵ����
					String strEndFlag = "";
					String strAudSel = "";//��ѡ���̽ڵ�
					String strFlowPj = "";//����Ʊ��
					/**3 ѭ��ƴ�Ӳ���*/
					while(!"4".equals(strEndFlag)){
							rd = getNextNodeByCondition(null,strStartNode,tableEx,"2",strFlowRunId);
							if(rd==null){break;}
							strEndFlag = getColString("I_TYPE", rd);
							String strCustomNodeIds = getColString("S_AUDIT_SEL", rd);//�ֶ�ѡ��ڵ�
							if("2".equals(strEndFlag)&&strCustomNodeIds!=null&&!"".equals(strCustomNodeIds)){//�����ж��Ƿ��ֶ�ѡ��ڵ�
								strAudSel = strAudSel+strCustomNodeIds;
								break;					
							}
							strAudSel = strAudSel+"|"+"";
							String strNodeAudit = queryAuditPerson(strStartUser,strStartUserBranch,getColString("S_AUDIT_BRANCH", rd),getColString("S_AUDIT_ROLE", rd),getColString("S_AUDIT_USER", rd),getColString("S_AUDIT_SQRYATTR", rd),getColString("S_AUDIT_SQRY", rd),null,rd,strFlowRunId);
							if("5".equals(strEndFlag)){//������,�洢���������̺�/�汾�����к�/��ID
								strNodeAudit = "S";
							}
							strSonFlow = (strSonFlow+"|")+getColString("S_FLOW_SON", rd);
							strAuditArrayyq =(strAuditArrayyq+"|") +getColString("S_AUDIT_YQTS", rd)+","+getColString("S_AUDIT_YQTSCL", rd);//��������  �������ڲ���
							strAuditMsgs =(strAuditMsgs+"|")+getColString("S_AUDIT_TZXX", rd);//������Ϣģ��
							strAuditNodes = (strAuditNodes+"|")+getColString("I_NODE_ID", rd);
							strFlowPj =(strFlowPj+"|")+getColString("S_AUDIT_FSPJ", rd);//����Ʊ��
							//�Ƿ����� ������������ �����������ش��� ������
							//����,ֵ5,ͨ������6,��������7|
							strAuditOther =(strAuditOther+"|") +getColString("S_AUDIT_TG", rd)+","+getColString("S_AUDIT_THJD", rd)+","+getColString("S_AUDIT_THJDZD", rd)+","+getColString("S_TZLC", rd)+","+getColString("S_AUDIT_PREEMPTION", rd)+","+getColString("S_AUD_VAL", rd)+","+",";
							strStartNode = getColString("S_CHILD_ID", rd);
							if("".equals(strNodeAudit)){
								strNodeAudit = ("1".equals(getColString("S_AUDIT_TG", rd)))?"T":strNodeAudit;//�Ƿ�����
							}
							strAuditUsers = (strAuditUsers+"|")+strNodeAudit;
	
					}
						/**4 ���� 1:��*/
						strNextAuditUserIndex = getNodesInfoRun(strAuditUsers,strAuditOther,strNextAuditUserIndex,strStartUser,strAuditMsgs.split("\\|",-1),strFlowId,strVersion,strFlowRunId,strAuditNodes.split("\\|",-1)[strNextAuditUserIndex]);
						strNextAuditUser =strAuditUsers.split("\\|",-1)[strNextAuditUserIndex];
						strNodeIdNext =strAuditNodes.split("\\|",-1)[strNextAuditUserIndex];
						/**�жϵ�ǰ�ڵ��������Ƿ��ȡֵ*/
						strNextAuditUser = queryAuditPersonIsColumn(strNextAuditUser, strFlowRunId, strFlowId, strVersion, strNodeIdNext);
						
						String[] strArryAuditUsers = strAuditUsers.split("\\|",-1);
						strArryAuditUsers[strNextAuditUserIndex]=strNextAuditUser;
						strAuditUsers = getStringArryToString(strArryAuditUsers);
						/**5 �������б�*/
						String[] strArrayFlowRun = {strFlowId,strFlowRunId,strNodeIdNext,strVersion,strNownewDate,strStartUser,strNextAuditUser,strNextAuditUserIndex+"",strAuditMsgs,strStartUserBranch,strAuditArrayyq,strAuditUsers,strAuditNodes,strIsOver,strAuditOther,strAudSel,strEndNodes,strSonFlow,strFlowType,"",strFlowPj,strTab};
						updateFlowRun(strArrayFlowRun,"1");
					
						String strDate = strSdfYmdHms.format(new Date());
						String  strAuditComment = "";
						/**����������־*/
						String[] strArrayFlowLog = {strFlowId,strFlowRunId,strAuditNodes.split("\\|",-1)[0],strDate,strVersion,strStartUser,strAuditState,strAuditComment};
						insertFlowLog("1", strArrayFlowLog);
						/**������Ϣ*/
						sendMsg(strAuditMsgs.split("\\|",-1)[0],strNextAuditUser,strAuditState,strIsOver,strFlowId,strVersion,strFlowRunId,strNodeIdNext,null,strFlowType,strPageCode,strTab);
	//					if(strClassName!=null&&strMethodName!=null&&!"".equals(strMethodName)&&!"".equals(strClassName)){
	//						reflectMothedInvoke(strClassName, strMethodName, request);
	//					}
			}catch (Exception e) {
			 //   MantraLog.fileCreateAndWrite(e);
				b=false;
				// String[] strArrayFlowLog22 = {"333",strFlowRunId,strFlowId,new Date()+"",strVersion,"","start",getErrorInfoFromException(e)};
				// insertFlowLog("1", strArrayFlowLog22);
				e.printStackTrace();
			}finally{
				tableEx.close();
				tableEx1.close();
			}
			return b;
		}
		
		/**
		 * ����ڵ�
		 * @param request
		 * @return
		 */
		public Boolean processStart(HttpServletRequest request,StringBuffer _sb,String _strSonFlowId,String _strFlowParentId){

			TableEx tableEx =null;
			TableEx tableEx1 =null;
			Boolean b = true;
			String strFlowRunId = "";
			String strFlowId ="";
			String strVersion="";
			try{
				/*
				insertFlowLog("1", strArrayFlowLog1);
				*/
	//			DBFactory dbf = new DBFactory("orcl","extop.tpddns.cn" , "gnnczs", 2, "gnnczs", "1521");
	//			DBFactory db = new DBFactory(aStrDBName, aStrDBIP, aDBUser, iDBTypeTemp, aStrPassword, aStrDBPort);
				/**��������*/
				String strStartUser = request.getSession().getAttribute("SYS_STRCURUSER").toString();//������
				String strStartUserRole = request.getSession().getAttribute("SYS_STRROLECODE").toString();//�����˽�ɫ
				String strStartUserBranch = request.getSession().getAttribute("SYS_STRBRANCHID").toString();//��������֯
				
				strFlowId = request.getParameter("NO_sys_flow_id");//����ID
				strVersion = request.getParameter("NO_sys_flow_Ver");//�汾��
				 strFlowRunId = request.getParameter("NO_sys_S_RUN_ID");//����
				String strFlowType = request.getParameter("NO_sys_S_flow_type");//0:������ 1:������ Ĭ��������

				//ɾ��֪ͨ��Ϣ
				DelMsg( strFlowRunId);
				
				/**������*/
				strFlowType = "1";
				
				String strPageCode = "";//ҳ�����
				if(_strSonFlowId!=null&&!"".equals(_strSonFlowId)){
					strFlowId = _strSonFlowId;
					String[] strArraySon = queryFlowMaiByFlowId(_strSonFlowId,"").split(",",-1);
					strVersion = strArraySon[0];
					strPageCode=strArraySon[1];
					strFlowType = "0";
				}

				
					/**0 ��ѯ���̽ڵ�*/
					String strStartNode = "";//����ڵ�NODE
					String strStartNodeBak = "";//����ڵ�NODE
					String strEndNodes = ",";//���н����ڵ�
					String strAuditMsgs = "";//������Ϣģ��
					String strTab="";//����
					tableEx = queryFlowNodeInfo(strFlowId,strVersion,"");
					int iCount = tableEx.getRecordCount();
					//_sb.append(" iCount: "+iCount);
					//_sb.append("sql:"+sb.toString());
					if(iCount<1){
						return false;
					}
					/**1 ���ҿ�ʼ�ڵ�*/
					Record record = null;
					for(int i=0;i<iCount;i++){
						record =  tableEx.getRecord(i);//1 ���� 3 ��ʼ   2����  4����
						if("3".equals(getColString("I_TYPE", record))){//��ʼ����
							strAuditMsgs = getColString("S_AUDIT_TZXX", record);
							strStartNodeBak = getColString("I_NODE_ID", record);
							//�����ʼ�ڵ��ж��Ƿ񷢽ڵ�
							if(queryFlowStartPerson(strStartUser,strStartUserRole,strStartUserBranch,getColString("S_AUDIT_BRANCH", record),getColString("S_AUDIT_ROLE", record),getColString("S_AUDIT_USER", record))==true){
								strStartNode = strStartNodeBak;
								strTab = getColString("S_TAB", record);
								if(strPageCode==null||"".equals(strPageCode)){
									strPageCode = getColString("S_PAGECODE", record);
								}
							}else if(querySqRole(getColString("S_AUDIT_SQRYATTR", record),getColString("S_AUDIT_SQRY", record), request, strFlowRunId,strStartUserRole)){
								strStartNode = strStartNodeBak;
								strTab = getColString("S_TAB", record);
								if(strPageCode==null||"".equals(strPageCode)){
									strPageCode = getColString("S_PAGECODE", record);
								}
							}
	//						if("".equals(strStartNode)||strStartNode==null){
	//							strStartNodeBak = getColString("I_NODE_ID", record);
	//						}
						}else if("4".equals(getColString("I_TYPE", record))){
							strEndNodes=strEndNodes+getColString("I_NODE_ID", record)+",";
						}
					}
					strStartNode= ("".equals(strStartNode)?strStartNodeBak:strStartNode);//�Ҳ�����ʼ�ڵ�������һ��
					if("".equals(strStartNode)){
						_sb.append("��ǰ��ɫ��Ȩ�޷�������");
						return false;
					}
					String strNodeIdNow = strStartNode;//�Ҳ����ڵ�,�ܳ��쳣
					
					/**�ύ����������*/
					tableEx1 = queryFlowNodeInfo(strFlowId, strVersion, strNodeIdNow);
					String strClassName = getColString("S_AUDIT_PAGENAME", tableEx1.getRecord(0));
					String strMethodName = getColString("S_AUDIT_CLASSNAME", tableEx1.getRecord(0));
					String strField = getColString("S_AUDIT_TABLECONTROL", tableEx1.getRecord(0));
					 _sb.append(strField);
					if(strField!=null&&!"".equals(strField)){
						updateTabByFlowSet(request, "", strField, strFlowRunId,_sb);//strNodeIdNow
					}
					
					
					
					/**2 ��ʼ�ڵ㸳ֵ*/
					String strAuditArrayyq=",";//����
					String strAuditNodes=strStartNode;//���нڵ�
					String strAuditState="3";//����״̬ 3���ύ
					String strNodeIdNext="";//���нڵ�
					String strNextAuditUser="";//�ڵ�������
					String strAuditOther =",,,,,,,";//����
					String strNownewDate = strSdfYmdHms.format(new Date());//��������
					int strNextAuditUserIndex =1;//��һ�����ڵ�����,������Ĭ��Ϊ1
					String strAuditUsers = strStartUser;//����������
					String strSonFlow = "";
	//				String strAuditTg = "";//�Ƿ�����
					String strIsOver = "0";//�Ƿ����
					Record rd = null;//��ȡ��һ�ڵ����
					String strEndFlag = "";
					String strAudSel = "";//��ѡ���̽ڵ�
					String strFlowPj = "";//����Ʊ��
					/**3 ѭ��ƴ�Ӳ���*/
					while(!"4".equals(strEndFlag)){
							rd = getNextNodeByCondition(request,strStartNode,tableEx,"1",strFlowRunId);
							if(rd==null){break;}
							strEndFlag = getColString("I_TYPE", rd);
							String strCustomNodeIds = getColString("S_AUDIT_SEL", rd);//�ֶ�ѡ��ڵ�
							if("2".equals(strEndFlag)&&strCustomNodeIds!=null&&!"".equals(strCustomNodeIds)){//�����ж��Ƿ��ֶ�ѡ��ڵ�
								strAudSel = strAudSel+strCustomNodeIds;
								break;					
							}
							strAudSel = strAudSel+"|"+"";
							String strNodeAudit = queryAuditPerson(strStartUser,strStartUserBranch,getColString("S_AUDIT_BRANCH", rd),getColString("S_AUDIT_ROLE", rd),getColString("S_AUDIT_USER", rd),getColString("S_AUDIT_SQRYATTR", rd),getColString("S_AUDIT_SQRY", rd),request,rd,strFlowRunId);
							if("5".equals(strEndFlag)){//������,�洢���������̺�/�汾�����к�/��ID
								strNodeAudit = "S";
							}
							strSonFlow = (strSonFlow+"|")+getColString("S_FLOW_SON", rd);
							strAuditArrayyq =(strAuditArrayyq+"|") +getColString("S_AUDIT_YQTS", rd)+","+getColString("S_AUDIT_YQTSCL", rd);//��������  �������ڲ���
							strAuditMsgs =(strAuditMsgs+"|")+getColString("S_AUDIT_TZXX", rd);//������Ϣģ��
							strAuditNodes = (strAuditNodes+"|")+getColString("I_NODE_ID", rd);
							strFlowPj =(strFlowPj+"|")+getColString("S_AUDIT_FSPJ", rd);//����Ʊ��
							//�Ƿ����� ������������ �����������ش��� ������
							//����,ֵ5,ͨ������6,��������7|
							strAuditOther =(strAuditOther+"|") +getColString("S_AUDIT_TG", rd)+","+getColString("S_AUDIT_THJD", rd)+","+getColString("S_AUDIT_THJDZD", rd)+","+getColString("S_TZLC", rd)+","+getColString("S_AUDIT_PREEMPTION", rd)+","+getColString("S_AUD_VAL", rd)+","+",";
							strStartNode = getColString("S_CHILD_ID", rd);
							if("".equals(strNodeAudit)){
								strNodeAudit = ("1".equals(getColString("S_AUDIT_TG", rd)))?"T":strNodeAudit;//�Ƿ�����
							}
							strAuditUsers = (strAuditUsers+"|")+strNodeAudit;
	
					}
									_sb.append("sql:"+sb.toString());
						/**4 ���� 1:��*/
						strNextAuditUserIndex = getNodesInfoRun(strAuditUsers,strAuditOther,strNextAuditUserIndex,strStartUser,strAuditMsgs.split("\\|",-1),strFlowId,strVersion,strFlowRunId,strAuditNodes.split("\\|",-1)[strNextAuditUserIndex]);
						strNextAuditUser =strAuditUsers.split("\\|",-1)[strNextAuditUserIndex];
						strNodeIdNext =strAuditNodes.split("\\|",-1)[strNextAuditUserIndex];
						/**�жϵ�ǰ�ڵ��������Ƿ��ȡֵ*/
						strNextAuditUser = queryAuditPersonIsColumn(strNextAuditUser, strFlowRunId, strFlowId, strVersion, strNodeIdNext);
						
						String[] strArryAuditUsers = strAuditUsers.split("\\|",-1);
						strArryAuditUsers[strNextAuditUserIndex]=strNextAuditUser;
						strAuditUsers = getStringArryToString(strArryAuditUsers);
						/**5 �������б�*/
						String[] strArrayFlowRun = {strFlowId,strFlowRunId,strNodeIdNext,strVersion,strNownewDate,strStartUser,strNextAuditUser,strNextAuditUserIndex+"",strAuditMsgs,strStartUserBranch,strAuditArrayyq,strAuditUsers,strAuditNodes,strIsOver,strAuditOther,strAudSel,strEndNodes,strSonFlow,strFlowType,_strFlowParentId,strFlowPj,strTab};
						updateFlowRun(strArrayFlowRun,"1");
					
						String strDate = strSdfYmdHms.format(new Date());
						String  strAuditComment = "";
						/**����������־*/
						String[] strArrayFlowLog = {strFlowId,strFlowRunId,strAuditNodes.split("\\|",-1)[0],strDate,strVersion,strStartUser,strAuditState,strAuditComment};
						insertFlowLog("1", strArrayFlowLog);
						/**������Ϣ*/
						sendMsg(strAuditMsgs.split("\\|",-1)[0],strNextAuditUser,strAuditState,strIsOver,strFlowId,strVersion,strFlowRunId,strNodeIdNext,request,strFlowType,strPageCode,strTab);
						if("S".equals(strNextAuditUser)){//�ж��Ƿ�������---��ǰ�ڵ��������Ƿ�S
							//����������
	//						processStart(request, _sb,strSonFlow.split("\\|",-1)[strNextAuditUserIndex+1],strFlowId);
						}
						//�޸�ֵ
	//					String strField1 = getColString("S_AUDIT_TABLECONTROL", queryFlowNodeInfo(strFlowId, strVersion, strNodeIdNow).getRecord(0));
	//					if(strField1!=null&&!"".equals(strField1)){
	//						updateTabByFlowSet(request, "", strField1, strFlowRunId,_sb);//strNodeIdNow
	//						processSave(request);
	//					}
						if(strClassName!=null&&strMethodName!=null&&!"".equals(strMethodName)&&!"".equals(strClassName)){
							reflectMothedInvoke(strClassName, strMethodName, request);
						}
			}catch (Exception e) {
				// MantraLog.fileCreateAndWrite(e);
				b=false;
				// String[] strArrayFlowLog22 = {"333",strFlowRunId,strFlowId,new Date()+"",strVersion,"","start",getErrorInfoFromException(e)};
				// insertFlowLog("1", strArrayFlowLog22);
				_sb.append(e);
				e.printStackTrace();
			}finally{
				tableEx.close();
				tableEx1.close();
			}

			return b;
		}
		
		/**
		 * ��ѯ��ǰ�ڵ��������Ƿ�ȡ�Ա�
		 * @param _strNextAuditUser
		 * @param _strFlowRunId
		 * @param _strFlowId
		 * @param _strVersion
		 * @param _strNodeIdNext
		 * @return
		 */
		private String queryAuditPersonIsColumn(String _strNextAuditUser,String _strFlowRunId,String _strFlowId,String _strVersion,String _strNodeIdNext){
			TableEx exFlowNode = null;
			String strAuditEx = "";
	//		if("".equals(_strNextAuditUser)||_strNextAuditUser==null){
				try {
					exFlowNode = queryFlowNodeInfo(_strFlowId, _strVersion, _strNodeIdNext);
					String strExCon="";
					strExCon = getColString("S_AUDIT_THDJR", exFlowNode.getRecord(0));
					if(strExCon!=null&&!"".equals(strExCon)){
						strAuditEx = queryBusinessDataByCon(strExCon, "",_strFlowRunId);
					}
					if(strAuditEx!=null&&!"".equals(strAuditEx)){
						_strNextAuditUser = strAuditEx;
					}
				} catch (Exception e) {
				    // MantraLog.fileCreateAndWrite(e);
				    
					e.printStackTrace();
				}finally{
					exFlowNode.close();
				}
	//		}
			return _strNextAuditUser;
		}
		
		/**
		 * �������ò�ѯҵ�������
		 * @param strRolePd:����
		 * @param _strZj ����ֵ,δ��
		 * * @param _strFlowRunId����ID
		 * @return
		 */
		private String queryBusinessDataByCon(String strRolePd, String _strZj,String _strFlowRunId) {
			DBFactory dbf = new DBFactory();
			TableEx ex = null;
			String strRole= "";
			try {
	//			strRolePd ����|�ֶ���|��������
				String[] strArrayPd = strRolePd.split("\\|",-1);
				ex = dbf.query(new StringBuffer().append(" select ").append(strArrayPd[1]).append(" from ").append(strArrayPd[0]).append(" where 1=1 and ").append("S_RUN_ID").append("='").append(_strFlowRunId).append("'").toString());
				strRole = getColString(strArrayPd[1], ex.getRecord(0));
			} catch (Exception e) {
			 //   MantraLog.fileCreateAndWrite(e);
				e.printStackTrace();
			}finally{
				ex.close();
				dbf.close();
			}
			return strRole;
		}
	
		/**
		 * �ڵ�����
		 * @param request
		 * @return
		 */
		/**
		 * @param request
		 * @param _sb
		 * @return
		 */
		@SuppressWarnings("finally")
		public Boolean processRun(HttpServletRequest request,StringBuffer _sb){

			TableEx exRun =null;
			TableEx exRun1 =null;
			TableEx exRun2 =null;
			TableEx exNode = null;
			TableEx exHZ = null;
			TableEx exParent = null;//��ǰ���̸����̶���
			Boolean b=true;
			DBFactory dbf  = new DBFactory();
			TableEx exForm= null;
			String strFlowRunId = "";
			String strFlowId ="";
			String strVersion="";
			String strCustomNodeId = "";
			String strAuditUser = "";
			try{
				request.setCharacterEncoding("UTF-8");
				/**��������*/
				strAuditUser = request.getSession().getAttribute("SYS_STRCURUSER").toString();//������
				String strAuditState = request.getParameter("NO_sys_flow_state");//���״̬ ���״̬:0����1ͨ��2����3�ύ4����5��������6�����˻�
				String strAuditComment = request.getParameter("strAuditComment");//��ע

    			
				if(strAuditComment!=null){
					 strAuditComment = new String(strAuditComment.getBytes("iso8859-1"),"UTF-8");
				}
				/*** ������ָ���ڵ�*/
				String strAuditChoiceNode = request.getParameter("NO_sys_flow_choicenode");
				strFlowId = request.getParameter("NO_sys_flow_id");
	            strFlowRunId = request.getParameter("NO_sys_S_RUN_ID");
	            strVersion = request.getParameter("NO_sys_flow_Ver");
	            
				
	            _sb.append("---������ָ���˻ؽڵ�:"+strAuditChoiceNode);
	            String strAuditUserId = request.getParameter("auditUserId");//��������,ָ�������
	            _sb.append("---ָ����һ�ڵ�������:"+strAuditUserId);
	            strCustomNodeId = request.getParameter("NO_custom_node_id");//�û��ֶ�ѡ��ڵ�
	            _sb.append("---�û�ѡ��ڵ�:"+strCustomNodeId);
	            
	          
	            if(strCustomNodeId!=null&&!"".equals(strCustomNodeId)){
	                
	            	processSave(request);
	            }
	            _sb.append("replay init");
				String strIsOver = "0";
				/**��ѯ����������Ϣ*/
				exRun = queryFlowRun(strFlowId,strVersion,strFlowRunId);
				//_sb.append(strFlowId+"    "+strVersion+"    "+strFlowRunId);
				Record record = exRun.getRecord(0);
				String strMsgs=getColString("S_AUDIT_MSG",record);
				String strYqs=record.getFieldByName("S_AUDIT_ARRAYYQ").value.toString();
				String strAuditUsers=record.getFieldByName("S_AUDIT_ARRAY").value.toString();
				String strNodes=record.getFieldByName("S_AUDIT_NODES").value.toString();
				int index = Integer.parseInt(record.getFieldByName("S_AUDIT_INDEX").value.toString());//����
				String strLaunchUser = record.getFieldByName("S_LAUNCH_USER").value.toString();
	//			String strLaunchBranch = record.getFieldByName("S_LAUNCH_BRANCH").value.toString();
				String strLaunchDate = record.getFieldByName("S_LAUNCH_DATE").value.toString();
				String strNodeIdNow = record.getFieldByName("S_NODE_CODE").value.toString();//��ǰ�ڵ�
				String strOther = record.getFieldByName("S_AUDIT_OTHER").value.toString();//����
				String strIsOverRun = record.getFieldByName("I_ISOVER").value.toString();//�Ƿ����
				String strAudSel = record.getFieldByName("S_AUDIT_SEL").value.toString();//�ֶ�ѡ���ӽڵ�
				String strAudOver = record.getFieldByName("S_AUD_OVER").value.toString();//�����ڵ�
				String strFlowSon = record.getFieldByName("S_FLOW_SON").value.toString();//������
				String strFlowtype = record.getFieldByName("S_FLOW_TYPE").value.toString();//��������
				String strTab = getColString("S_TAB", record);//����
				boolean bTranFlowSonFlag=false;
				
		
				
				if("1".equals(strIsOverRun)){//�ж���ɷ���
					return b;
				}
				
				/**���±�*/
				exRun1 = queryFlowNodeInfo(strFlowId, strVersion, strNodeIdNow);
				String strClassName = getColString("S_AUDIT_PAGENAME", exRun1.getRecord(0));
				String strMethodName = getColString("S_AUDIT_CLASSNAME", exRun1.getRecord(0));
				String strAuditStateBak = request.getParameter("NO_sys_flow_state");
				String strField = getColString("S_AUDIT_TABLECONTROL", exRun1.getRecord(0));
	//			String[] strArrayFlowLog1 = {"333","333","1","1","1","1","1",strField};
	//			insertFlowLog("1", strArrayFlowLog1);
	
				if(strField!=null&&!"".equals(strField)){
					updateTabByFlowSet(request, "", strField, strFlowRunId,_sb);//strNodeIdNow
					if(strCustomNodeId==null||"".equals(strCustomNodeId)){
		            	processSave(request);
		            	TableEx exRunGet =queryFlowRun(strFlowId,strVersion,strFlowRunId);
		            	strAuditUsers=exRunGet.getRecord(0).getFieldByName("S_AUDIT_ARRAY").value.toString();
		            	if(exRunGet!=null){
		            	    exRunGet.close();
		            	}
					    
		            }
				}
				
				if(strCustomNodeId!=null&&!"".equals(strCustomNodeId)){
					exRun2 = queryFlowNodeInfo(strFlowId, strVersion, strCustomNodeId);
	           	 //�ڵ�������ͬ-����Ϊ2,�ֶ�ѡ��ڵ�
	if(strCustomNodeId.equals(getColString("I_NODE_ID", exRun2.getRecord(0)))&&"2".equals(getColString("I_TYPE", exRun2.getRecord(0)))&&!"".equals(getColString("S_AUDIT_SEL", exRun2.getRecord(0)))){
						 //�������б�
						 String strDef = getColString("S_AUDIT_SEL",exRun2.getRecord(0));
						 String[] strRunAudSelArry = strAudSel.split("\\|",-1);
						 strRunAudSelArry[index]=strDef;
						 String[] strArrayFlowRunVal = {strFlowId,strFlowRunId,getStringArryToString(strRunAudSelArry)};
						 updateFlowRun(strArrayFlowRunVal, "6");
						 return true;
				 }
	           }            
				
				 _sb.append("form end");
				String[] strArrayAuditUsers =strAuditUsers.split("\\|",-1);//����������
				String[] strArrayNodes = strNodes.split("\\|",-1);//�ڵ�����
				String[] strArrayMsgs = strMsgs.split("\\|",-1);//��Ϣ����
				
				/**�жϵ�ǰ��¼���Ƿ�������нڵ�������*/
				String[] strArrayAuditUsersNow = strArrayAuditUsers[index].split(",");
				boolean flag = false;
				for(int i=0,j=strArrayAuditUsersNow.length;i<j;i++){
					if(strAuditUser.equals(strArrayAuditUsersNow[i])){
						flag = true;
						break;
					}
				}
				if(flag==false){
					return b; 
				}
				
				String strNodeIdNext = "";//��һ�ڵ�
				String strNextAuditUser = "";//��һ������
				int iNextAuditUserIndex = index;//��һ����������
				String strMsgId = strArrayMsgs[index];//�ڵ���Ϣ�ɣ�
				String strAudMod="";//���� ָ��/  ��ռģʽ
				String[] strOtherArrayNow = strOther.split("\\|",-1)[index].split(",",-1);
				boolean bFlag=false;//����&��������
				/**�Ƿ�����*/
				if(!isYuQi(strLaunchDate, strYqs,index)){
					String strYqOpt = strYqs.split("\\|",-1)[index].split(",",-1)[1];
					switch (strYqOpt) {
						case "ZF"://����
							strAuditState = "5";
							strIsOver ="1";//���̽���
							break;
						case "TGJD"://�Զ�����
							index = index +1;
							/**����*/
							iNextAuditUserIndex = getNodesInfoRun(strAuditUsers,strOther,index,strLaunchUser,strArrayMsgs,strFlowId,strVersion,strFlowRunId,strArrayNodes[index]);					
							break;
						case "ZDTH"://�Զ��˻س�ʼ�ڵ�
							index = 0;
							strAuditState = "6";
							strIsOver="1";
							iNextAuditUserIndex = 0;				
							break;
						case "JS"://����
							index = 0;
							strAuditState = "0";
							strIsOver="1";
							iNextAuditUserIndex = 0;				
							break;
					}
				}else{
					 _sb.append("��ǰ�����˳���:"+strArrayAuditUsersNow.length+"---strAuditState---"+strAuditState);
				
					/**�ж��Ƿ����ͨ��*/
	
					switch (strAuditState) {
						case "1"://���ͨ��
							if("S".equals(strArrayAuditUsers[index+1])){//�ж��Ƿ�������---��ǰ�ڵ��������Ƿ�S
								if(exRun1!=null){exRun1.close();}
								exNode = queryFlowNodeInfo(strFlowId, strVersion, strArrayNodes[index+1]);
								Record rd = exNode.getRecord(0);
	//							String strType ="T";
	//							String strSql ="update T_DQEZGZP set T_DQEZGZP.S_SPJG = -*-BHG-*- where T_DQEZGZP.S_ID=-*-<<T_DQEZGZP.S_ID>>-*- ";
	//							String strCon ="T_DQEZGZP.S_ID";
								String strType =getColString("S_CHILD_TYPE", rd).trim();
								String strSql =getColString("S_CHILD_TRANSQL", rd).trim();
								String strCon =getColString("S_CHILD_TRANCON", rd).trim();
								
	//							strSql = strSql.replace("-*-", "'");
	//							dbf = new DBFactory();
	//							dbf.sqlExe("update T_DQEZGZP set T_DQEZGZP.S_SPJG = 'BHG' where T_DQEZGZP.S_RUN_ID='"+strFlowRunId+"'", true);
								
								if("T".equals(strType)){//����
									bTranFlowSonFlag = true;
									index = index+1;
									while("S".equals(strArrayAuditUsers[index])&&"T".equals(strType)){
										strSql = strSql.replace("-*-", "'");
										updateFormSql(strSql,strCon,strType,strFlowRunId);
										index++;
										if(exNode!=null){exNode.close();exNode = null;}
										exNode = queryFlowNodeInfo(strFlowId, strVersion, strArrayNodes[index]);
										strSql = getColString("S_CHILD_TRANSQL", exNode.getRecord(0)).trim();
										strCon = getColString("S_CHILD_TRANCON", exNode.getRecord(0)).trim();
										strType = getColString("S_CHILD_TYPE", exNode.getRecord(0)).trim();
									}
									iNextAuditUserIndex = getNodesInfoRun(strAuditUsers,strOther,index,strLaunchUser,strArrayMsgs,strFlowId,strVersion,strFlowRunId,strArrayNodes[index]);
									strAuditUser = strArrayAuditUsers[iNextAuditUserIndex];
								}else{//������
									index++;
									iNextAuditUserIndex = index;
									strAuditUser = "����������";
									//����������
	//							processStart(request, _sb,strFlowSon.split("\\|",-1)[index],strFlowId);
									//�Ƿ���������-��������iNextAuditUserIndex++,û�������̽�����һ�ڵ�
									if(!queryFlowRunSon(strFlowId, "",strFlowRunId)){
										index++;
										//����
										iNextAuditUserIndex = getNodesInfoRun(strAuditUsers,strOther,index,strLaunchUser,strArrayMsgs,strFlowId,strVersion,strFlowRunId,strArrayNodes[index]);
										strAuditUser = strArrayAuditUsers[iNextAuditUserIndex];
									}
									//�����̽���,����������
								}
							}else if(strArrayAuditUsersNow.length>1){//��ǰΪ���û�����,�ж�����ģʽ
								String strAudModNow = strOtherArrayNow[4];//��ǰ�ڵ�ģʽ
								//����,ֵ5,ͨ������6,��������7|
								_sb.append("��ǰ�ڵ�strother:"+strOther.split("\\|",-1)[index]);
								double dHQ = Double.parseDouble((strOtherArrayNow[5]==null||"".equals(strOtherArrayNow[5])?"0":strOtherArrayNow[5]));//��ǩֵ
								int iHQCount = strArrayAuditUsersNow.length;//������
								int iPasscount = Integer.parseInt("".equals(strOtherArrayNow[6])?"0":strOtherArrayNow[6]);//ͨ������
								//�޸����� strOther
								_sb.append("�޸�strOther���� :"+strOther);
								_sb.append("�޸�strOtherArrayNow���� :"+strOtherArrayNow);
								_sb.append("�޸�index���� :"+index);
								_sb.append("�޸�iPasscount���� :"+iPasscount);
								strOther = getAuditOtherPass(strOther,strOtherArrayNow,index,iPasscount);
								 _sb.append("��ǰģʽ:"+strAudModNow);
								switch (strAudModNow) {
									case "QZ"://��ռ--����+1,�жϽڵ�����,��һ�ڵ�ģʽ�ж�
										index = index+1;
										iNextAuditUserIndex = getNodesInfoRun(strAuditUsers,strOther,index,strLaunchUser,strArrayMsgs,strFlowId,strVersion,strFlowRunId,strArrayNodes[index]);
										strAudMod = strOther.split("\\|",-1)[iNextAuditUserIndex].split(",",-1)[4];//��һ�ڵ�ģʽ:��ռ?ָ��
										break;
									case "ZD"://ָ��--����+1,�жϽڵ�����,��һ�ڵ�ģʽ�ж�
										index = index+1;
										iNextAuditUserIndex = getNodesInfoRun(strAuditUsers,strOther,index,strLaunchUser,strArrayMsgs,strFlowId,strVersion,strFlowRunId,strArrayNodes[index]);
										strAudMod = strOther.split("\\|",-1)[iNextAuditUserIndex].split(",",-1)[4];//��һ�ڵ�ģʽ:��ռ?ָ��
										break;
									case "HQ"://��ǩ
										//ͨ������5--��������6,��ǩֵ1,ͨ������0.5,
										if(((iPasscount+1)*100/iHQCount)>=dHQ){//ͨ������>=¼��ֵ,ִ��ͨ������
											index = index+1;
											iNextAuditUserIndex = getNodesInfoRun(strAuditUsers,strOther,index,strLaunchUser,strArrayMsgs,strFlowId,strVersion,strFlowRunId,strArrayNodes[index]);
											strAudMod = strOther.split("\\|",-1)[iNextAuditUserIndex].split(",",-1)[4];//��һ�ڵ�ģʽ:��ռ?ָ��
										}
										break;
									default:
										index = index+1;
										iNextAuditUserIndex = getNodesInfoRun(strAuditUsers,strOther,index,strLaunchUser,strArrayMsgs,strFlowId,strVersion,strFlowRunId,strArrayNodes[index]);
										strAudMod = strOther.split("\\|",-1)[iNextAuditUserIndex].split(",",-1)[4];//��һ�ڵ�ģʽ:��ռ?ָ��
										break;
								}
							}else{//һ���û�,��������
								index = index+1;
								iNextAuditUserIndex = getNodesInfoRun(strAuditUsers,strOther,index,strLaunchUser,strArrayMsgs,strFlowId,strVersion,strFlowRunId,strArrayNodes[index]);
								strAudMod = strOther.split("\\|",-1)[iNextAuditUserIndex].split(",",-1)[4];//��һ�ڵ�ģʽ:��ռ?ָ��
							}
							/**����*/
							//��ǩ---,2���ֶ�  ��ǩ״̬,,|,,|,,|,,
							break;
						case "2"://��ǩ
							break;
						case "0"://����
							String strAudModNow = strOtherArrayNow[4];//��ǰ�ڵ�ģʽ
							System.out.println("�ڵ�ģʽ:"+strAudModNow);
							_sb.append("�ڵ�ģʽ:"+strAudModNow);
							if(strArrayAuditUsersNow.length>1){//��ǰΪ���û�����,�ж�����ģʽ
								String strAuditReject = strOtherArrayNow[1];//����
								System.out.println("���ش���:"+strAuditReject);
								double dHQ = Double.parseDouble((strOtherArrayNow[5]==null||"".equals(strOtherArrayNow[5])?"0":strOtherArrayNow[5]));//��ǩֵ
								int iHQCount = strArrayAuditUsersNow.length;//������
								int iRejectcount = Integer.parseInt("".equals(strOtherArrayNow[7])?"0":strOtherArrayNow[7]);//���ش���
								_sb.append("���ؽڵ�ģʽ:"+strAudModNow);
								switch (strAudModNow) {
									case "QZ"://��ռģʽ
										/**��ѯ��ǰ�ڵ���Ϣ*/
										switch (strAuditReject) {
										case "1"://��һ�ڵ�
											String strBeforeNodeId = queryFlowLogBeforeNodeId(strFlowId,strVersion, strFlowRunId, strNodeIdNow);
											_sb.append("��һ�ڵ�:"+strBeforeNodeId+"---strArrayNodes---"+strArrayNodes);
											index = getChoiceNode(strArrayNodes,strBeforeNodeId);
											iNextAuditUserIndex = index;
											break;
										case "2"://��ʼ�ڵ�
											index = 0;
											iNextAuditUserIndex=0;
											break;
										case "3"://ָ���ڵ�
	//										strAuditOther =(strAuditOther+"|") +getColString("S_AUDIT_TG", rd)+","+getColString("S_AUDIT_THJD", rd)+","+getColString("S_AUDIT_THJDZD", rd)+","+getColString("S_TZLC", rd)+","+getColString("S_AUDIT_PREEMPTION", rd)+","+getColString("S_AUD_VAL", rd)+","+",";
	
											String strAuditRejectChoiceNode = strOtherArrayNow[2];//���ؽڵ�
											index = getChoiceNode(strArrayNodes,strAuditRejectChoiceNode);
											/**����*///TODO
											iNextAuditUserIndex = getNodesInfoRun(strAuditUsers,strOther,index,strLaunchUser,strArrayMsgs,strFlowId,strVersion,strFlowRunId,strArrayNodes[index]);
											break;
										case "4"://������ָ��
											index = getChoiceNode(strArrayNodes,strAuditChoiceNode);
											iNextAuditUserIndex = index;
											strAuditState = "0";
											break;
										case "5"://����
											strAuditState = "2";
											strIsOver = "1";						
											break;
										case "6"://������ָ��-��������
											strAuditRejectChoiceNode = strOtherArrayNow[2];//���ؽڵ�
											index = getChoiceNode(strArrayNodes,strAuditRejectChoiceNode);
											strAuditState = "0";
											strIsOver = "1";
											iNextAuditUserIndex  = index;
											bFlag =  true;
											break;
										}
										break;
									case "ZD"://ָ��ģʽ
										/**��ѯ��ǰ�ڵ���Ϣ*/
										switch (strAuditReject) {
										case "1"://��һ�ڵ�
											String strBeforeNodeId = queryFlowLogBeforeNodeId(strFlowId,strVersion, strFlowRunId, strNodeIdNow);
											index = getChoiceNode(strArrayNodes,strBeforeNodeId);
											iNextAuditUserIndex = index;
											break;
										case "2"://��ʼ�ڵ�
											index = 0;
											iNextAuditUserIndex=0;
											break;
										case "3"://ָ���ڵ�
											String strAuditRejectChoiceNode = strOtherArrayNow[2];//���ؽڵ�
											index = getChoiceNode(strArrayNodes,strAuditRejectChoiceNode);
											/**����*///TODO
											iNextAuditUserIndex = getNodesInfoRun(strAuditUsers,strOther,index,strLaunchUser,strArrayMsgs,strFlowId,strVersion,strFlowRunId,strArrayNodes[index]);
											break;
										case "4"://������ָ��
											index = getChoiceNode(strArrayNodes,strAuditChoiceNode);
											iNextAuditUserIndex = index;
											strAuditState = "0";
											break;
										case "5"://����
											strAuditState = "2";
											strIsOver = "1";						
											break;
										case "6"://������ָ��-��������
											strAuditRejectChoiceNode = strOtherArrayNow[2];//���ؽڵ�
											index = getChoiceNode(strArrayNodes,strAuditRejectChoiceNode);
											strAuditState = "0";
											strIsOver = "1";
											iNextAuditUserIndex  = index;
											bFlag =  true;
											break;
										}
										break;
									case "HQ"://��ǩ
										//ͨ������5--��������6,��ǩֵ1,ͨ������0.4,
										if(((iRejectcount+1)*100/iHQCount)>=(100-dHQ)){//���ر���>=1-¼��ֵ,ִ�в��ز���
											/**��ѯ��ǰ�ڵ���Ϣ*/
	//										String strAuditReject = strOtherArrayNow[1];//����
											switch (strAuditReject) {
											case "1"://��һ�ڵ�
												String strBeforeNodeId = queryFlowLogBeforeNodeId(strFlowId,strVersion, strFlowRunId, strNodeIdNow);
												index = getChoiceNode(strArrayNodes,strBeforeNodeId);
												strOther = getAuditStrArry(strOther,  getAuditStrArryDefault(strOther, index), index);//index-1,index
												iNextAuditUserIndex = index;
												break;
											case "2"://��ʼ�ڵ�
												index = 0;
												iNextAuditUserIndex=0;
												strOther = getAuditStrArry(strOther,  getAuditStrArryDefault(strOther, index), index);
												break;
											case "3"://ָ���ڵ�
												String strAuditRejectChoiceNode = strOtherArrayNow[2];//���ؽڵ�
												index = getChoiceNode(strArrayNodes,strAuditRejectChoiceNode);
												/**����*///TODO
												iNextAuditUserIndex = getNodesInfoRun(strAuditUsers,strOther,index,strLaunchUser,strArrayMsgs,strFlowId,strVersion,strFlowRunId,strArrayNodes[index]);
												strOther = getAuditStrArry(strOther,  getAuditStrArryDefault(strOther, index), index);
												break;
											case "4"://������ָ��
												index = getChoiceNode(strArrayNodes,strAuditChoiceNode);
												iNextAuditUserIndex = index;
												strOther = getAuditStrArry(strOther,  getAuditStrArryDefault(strOther, index), index);
												strAuditState = "0";
												break;
											case "5"://����
												strOther = getAuditOtherReject(strOther,strOtherArrayNow,index,iRejectcount);
												strAuditState = "2";
												strIsOver = "1";						
												break;
											case "6"://������ָ��-��������
												strAuditRejectChoiceNode = strOtherArrayNow[2];//���ؽڵ�
												index = getChoiceNode(strArrayNodes,strAuditRejectChoiceNode);
												strAuditState = "0";
												strIsOver = "1";
												bFlag =  true;
												iNextAuditUserIndex  = index;
												break;
											}
										}else{
											strOther = getAuditOtherReject(strOther, strOtherArrayNow, iNextAuditUserIndex, iRejectcount);
										}
										break;
								}
							}else{//��������
								/**��ѯ��ǰ�ڵ���Ϣ*/ //����,��ǰ�ڵ�֮����� ,,,,,,|,,,,HQ,,|,,,,,,|,,,,,, //����,ֵ5,ͨ������6,��������7|
								String strAuditReject = strOtherArrayNow[1];//����
								switch (strAuditReject) {
								case "1"://��һ�ڵ�
									String strBeforeNodeId = queryFlowLogBeforeNodeId(strFlowId,strVersion, strFlowRunId, strNodeIdNow);
									index = getChoiceNode(strArrayNodes,strBeforeNodeId);
									iNextAuditUserIndex = index;
									strOther = getAuditStrArry(strOther,  getAuditStrArryDefault(strOther, index), index);
									break;
								case "2"://��ʼ�ڵ�
									index = 0;
									iNextAuditUserIndex=0;
									strOther = getAuditStrArry(strOther,  getAuditStrArryDefault(strOther, index), index);
									break;
								case "3"://ָ���ڵ�
									String strAuditRejectChoiceNode = strOtherArrayNow[2];//���ؽڵ�
									index = getChoiceNode(strArrayNodes,strAuditRejectChoiceNode);
									/**����*///TODO
									iNextAuditUserIndex = getNodesInfoRun(strAuditUsers,strOther,index,strLaunchUser,strArrayMsgs,strFlowId,strVersion,strFlowRunId,strArrayNodes[index]);
									strOther = getAuditStrArry(strOther,  getAuditStrArryDefault(strOther, index), index);
									break;
								case "4"://������ָ��
									index = getChoiceNode(strArrayNodes,strAuditChoiceNode);
									iNextAuditUserIndex = index;
									strOther = getAuditStrArry(strOther,  getAuditStrArryDefault(strOther, index), index);
									strAuditState = "0";
									break;
								case "5"://����
									strAuditState = "2";
									strIsOver = "1";						
									break;
								case "6"://������ָ��-��������
									strAuditRejectChoiceNode = strOtherArrayNow[2];//���ؽڵ�
									index = getChoiceNode(strArrayNodes,strAuditRejectChoiceNode);
									strAuditState = "0";
									strIsOver = "1";
									bFlag =  true;
									iNextAuditUserIndex  = index;
									break;
								}
							}
							break;
					}
				}
				strNodeIdNext = strArrayNodes[iNextAuditUserIndex];
				strNextAuditUser = "ZD".equals(strAudMod)?strAuditUserId:strArrayAuditUsers[iNextAuditUserIndex];//ָ����һ�ڵ�������
				/**�����Ƿ����*/
				if(strAudOver.indexOf(","+strNodeIdNext+",")>-1){
					strIsOver = "1";
				}
				/**�жϵ�ǰ�ڵ��������Ƿ��ȡֵ*/
				strNextAuditUser = queryAuditPersonIsColumn(strNextAuditUser, strFlowRunId, strFlowId, strVersion, strNodeIdNext);
				
			
				
				String[] strArryAuditUsers = strAuditUsers.split("\\|",-1);
				strArryAuditUsers[iNextAuditUserIndex]=strNextAuditUser;
				strAuditUsers = getStringArryToString(strArryAuditUsers);
				
				/**���ؽ���*/
				if(bFlag){
					strNextAuditUser = queryFlowLogBeforeNodeAuditUser(strFlowId,strVersion, strFlowRunId, strNodeIdNext);
				}
				
			
	           
				
				/**��������������Ϣ*/
				String[] strArrayFlowRun = {strFlowId,strFlowRunId,strVersion,strNodeIdNext,"2".equals(strAuditState)?"":strNextAuditUser,iNextAuditUserIndex+"",strIsOver,strOther,strAuditUsers};
				updateFlowRun(strArrayFlowRun,"2");
				/**����������־*/
				String strNowDate = strSdfYmdHms.format(new Date());
				if(bTranFlowSonFlag){
					strAuditUser =request.getSession().getAttribute("SYS_STRCURUSER").toString();
				}
				String[] strArrayFlowLog = {strFlowId,strFlowRunId,strNodeIdNow,strNowDate,strVersion,strAuditUser,strAuditState,strAuditComment};
				insertFlowLog("1", strArrayFlowLog);
				/**���µ�ǰ������־Ϊ��*/
				updateSendMsgZt(dbf,strFlowRunId,strAuditUser,strFlowId);
				String strPageCode="";
				if("1".equals(strIsOver)){
					String strFlowParentId= getColString("S_FLOW_PARENT_ID", exRun.getRecord(0)); //��ǰ�����̸�����ID
					if(strFlowParentId!=null&&!"".equals(strFlowParentId)){//�����̽���,0:������ 1:������ Ĭ��������
						//��ѯ����ID��,�����̺���ȵ������������Ƿ�ȫ�����,���ȫ�����,�����̽�����һ��,����,���ֲ���
						boolean bIsOverSameFlow = queryFlowRunIsOverSameLevel(strFlowRunId,strFlowParentId);//true:��� false:δ���
						String strIsOverParent = "0";
						if(bIsOverSameFlow){
							//��ѯ������,�жϸ������Ƿ����,���͸�������Ϣ
							exParent = new TableEx("*","T_SYS_FLOW_RUN"," S_RUN_ID='"+strFlowRunId+"' and S_FLOW_ID='"+strFlowParentId+"'");
							String[] strAuditUsersParent=exParent.getRecord(0).getFieldByName("S_AUDIT_ARRAY").value.toString().split("\\|",-1);
							String[] strNodesParent=exParent.getRecord(0).getFieldByName("S_AUDIT_NODES").value.toString().split("\\|",-1);
							int indexParent = Integer.parseInt(exParent.getRecord(0).getFieldByName("S_AUDIT_INDEX").value.toString())+1;				
							String strAudOverParent = exParent.getRecord(0).getFieldByName("S_AUD_OVER").value.toString();//�����ڵ�
							String strParentVersion = exParent.getRecord(0).getFieldByName("S_AUDIT_VERSION").value.toString();//�汾��
							strTab = exParent.getRecord(0).getFieldByName("S_TAB").value.toString();//����
							strNextAuditUser = strAuditUsersParent[indexParent];
							//�ж�����TODO
							//�жϸ������Ƿ����
							if(strAudOverParent.indexOf(","+strNodesParent[indexParent]+",")>-1){
								strIsOverParent = "1";
								strNextAuditUser = "";
							}
							strFlowtype = "1";
							/**�жϵ�ǰ�ڵ��������Ƿ��ȡֵ*/
							strNextAuditUser = queryAuditPersonIsColumn(strNextAuditUser, strFlowRunId, strFlowParentId, strParentVersion, strNodesParent[indexParent]);
							strAuditUsersParent[indexParent]=strNextAuditUser;
							//���¸�����
							String[] strArrayFlowRunParent ={strFlowParentId,strFlowRunId,strNextAuditUser,strNodesParent[indexParent],indexParent+"",strIsOverParent,getStringArryToString(strAuditUsersParent)};
							updateFlowRun(strArrayFlowRunParent,"4");
	//						���͸�������Ϣ
							strPageCode  = sendMsg(strMsgId,strNextAuditUser,strAuditState,strIsOverParent,strFlowId,strVersion,strFlowRunId,strNodesParent[indexParent],request,strFlowtype,"",strTab);
						}else{
							//���������,����������δ���
							strNextAuditUser = strArrayAuditUsers[0];//���������,����������δ���,������Ϣ��������
							strFlowtype = "0";//��ǰ����Ϊ������
							strPageCode = sendMsg(strMsgId,strNextAuditUser,strAuditState,strIsOverParent,strFlowId,strVersion,strFlowRunId,"",request,strFlowtype,"",strTab);
						}
	
					}else{//�����̽���
						strNextAuditUser=strArrayAuditUsers[0];//��������,������
						strPageCode = sendMsg(strMsgId,strNextAuditUser,strAuditState,strIsOver,strFlowId,strVersion,strFlowRunId,strNodeIdNext,request,strFlowtype,"",strTab);					
					}
				}else{
					strPageCode = sendMsg(strMsgId,strNextAuditUser,strAuditState,strIsOver,strFlowId,strVersion,strFlowRunId,strNodeIdNext,request,strFlowtype,"",strTab);
				}
				
				if("1".equals(strAuditStateBak)&&strClassName!=null&&strMethodName!=null&&!"".equals(strMethodName)&&!"".equals(strClassName)){
					reflectMothedInvoke(strClassName, strMethodName, request);
				}
				
				/**����ͨ��&���̽���ִ�в���*/
				if("1".equals(strIsOver)&"1".equals(strAuditState)){
					DBFactory db = new DBFactory();
					flowOverDelMsg(strFlowRunId,strFlowId,strVersion);
	            
					if("1510196651437".equals(strPageCode)){
						new com.page.method.Fun().MeasuresToolEntr(request);
					}else if("1513048527561".equals(strPageCode)){
						new com.page.method.Fun().MeasuresToolEntr(request);
					}else if("1505896107531".equals(strPageCode)){//���ƻ�
						new com.page.method.Fun().MeasuresToolEntr(request);
					}else if("15175860658900".equals(strPageCode)){//����-�����Ŀ�ȼ����»���-��
						db.sqlExe("update T_XMZH_F set S_ZT='1' where S_ID=(select S_FZFBID from T_WCXMDJB where S_ID='"+request.getParameter("S_ID")+"')",true);
						if(db==null){db = new DBFactory();}
						db.sqlExe("update T_WCXMDJB set S_FLAG='1' where S_ID='"+request.getParameter("S_ID")+"'",true);
					}else if("1500457059214".equals(strPageCode)){//����-�����Ŀ����S_DJID
						new DBFactory().sqlExe("update T_XMPFB set S_FLAG='1' where S_ID='"+request.getParameter("S_ID")+"'",true);
						new DBFactory().sqlExe("update T_WCXMDJB set S_FLAG='2' where S_ID=(select S_DJID from T_XMPFB where S_ID='"+request.getParameter("S_ID")+"')",true);
					}else if("1500460289462".equals(strPageCode)){//����-��Ŀ�����۱���
						db.sqlExe("update T_XMPFB set S_FLAG='2' where S_ID=(select S_PFID from T_XMHPJBG where S_ID='"+request.getParameter("S_ID")+"')",true);
					}else if("1516247158225".equals(strPageCode)){
						new com.page.method.Fun().MeasuresToolEntr(request);
					}else if("151766214254010023".equals(strPageCode)){
						db.sqlExe("update T_JGGH set S_FLAG='2' where S_ID=(select S_PFID from T_XMHPJBG where S_ID='"+request.getParameter("S_ID")+"')",true);
					}else if("1516166904515".equals(strPageCode)){
						new com.page.method.Fun().MeasuresToolEntr(request);
					}
// else if("1516168904786".equals(strPageCode)){//���޼ƻ��ֽ�-��ί
// 						String strSid = request.getParameter("S_ID");
// 						DBFactory df = new DBFactory();
// 						TableEx wwEx = df.query("select * from T_JXZJHFJ  where S_ZJ='"+strSid+"'");
// 						Record rd = wwEx.getRecord(0);
// 						if("true".equals(getColString("S_SFWW", rd))){//��ί
// 							String uuid = EString.generId();
// 							String strJxlx =getColString("S_JXLX", rd);//��������
// 							StringBuffer sbr = new StringBuffer();
// 							String strFlowVersion = new com.timing.impcl.MantraUtil().getFlowVer(strPageCode,getColString("S_ZZ", rd));
// 	sbr.append("insert into T_WWXM (SYS_FLOW_VER,S_RUN_ID,S_HTH,S_XMMC,S_GQKSSJ,S_GQJSSJ,S_WWDW,S_WWDWID,S_YFFZR,S_JFFZR,S_JXJG,S_ZZ,S_BGRQ,S_ZDR,S_ZDSJ,S_XGR,S_XGSJ,S_BS_JD,S_ID,S_FJID) ");
// 	sbr.append("select '"+strFlowVersion+"' AS 'SYS_FLOW_VER', '"+EString.generId()+"' AS 'S_RUN_ID','' AS 'S_HTH',S_MC  AS 'S_XMMC',S_KSSJ  AS 'S_GQKSSJ',S_JSSJ AS 'S_GQJSSJ',S_WWDW AS 'S_WWDW',S_WWDWID AS 'S_WWDWID',S_YFZR AS 'S_YFFZR',S_FZR AS 'S_JFFZR','true' AS 'S_JXJG',S_ZZ AS 'S_ZZ','"+EString.getCurDate()+"'  AS 'S_BGRQ',S_ZDR AS 'S_ZDR','"+EString.getCurDate()+"' AS 'S_ZDSJ',S_ZHXGR AS 'S_XGR','"+EString.getCurDate()+"' AS 'S_XGSJ','' AS 'S_BS_JD','"+uuid+"' AS 'S_ID',S_ZJ AS 'S_FJID' from T_JXZJHFJ where T_JXZJHFJ.S_ZJ='");
// 	sbr.append(strSid);
// 	sbr.append("';");
// 	df.sqlExe(sbr.toString(), true);
// 	sbr = new StringBuffer();
// 	sbr.append("insert into T_WWXMFB (S_CBXM,S_SGSD,S_Z,S_ID,S_RID,S_CBDW,S_LXFS,S_XTLL) ");
// 	sbr.append("select S_JXXM AS 'S_CBXM',S_KSRQ AS 'S_SGSD',S_JSQR AS 'S_Z',S_ID AS 'S_ID','"+uuid+"' AS 'S_RID','"+getColString("S_WWDW", rd)+"' AS 'S_CBDW','"+getColString("S_LXFS", rd)+"' AS 'S_LXFS','"+getColString("S_XTLXT", rd)+"' AS 'S_XTLL' from T_ZYJXJHF where T_ZYJXJHF.S_FIDHZFJ='");
// 	sbr.append(strSid);
// 	sbr.append("';");
// 							df.sqlExe(sbr.toString(), true);
// 							wwEx.close();
// 							df.close();
// 						}
// 					}
                    else if("1516587805543".equals(strPageCode)){
						new com.page.method.Fun().MeasuresToolEntr(request);
					}else if("1516167932917".equals(strPageCode)){
						new com.page.method.Fun().MeasuresToolEntr(request);
					}else if("1516602563575".equals(strPageCode)){//�ظ�
						new com.page.method.Fun().MeasuresToolEntr(request);
					}else if("1516606174518".equals(strPageCode)){//�ظ�
						new com.page.method.Fun().MeasuresToolEntr(request);
					}else if("1516613463357".equals(strPageCode)){//�ظ�
						new com.page.method.Fun().MeasuresToolEntr(request);
					}else if("1516587886146".equals(strPageCode)){
						new com.page.method.Fun().MeasuresToolEntr(request);
					}else if("15175538437610".equals(strPageCode)){
						new com.page.method.Fun().MeasuresToolEntr(request);
					}else if("1516602563575".equals(strPageCode)){//�ظ�
						new com.page.method.Fun().MeasuresToolEntr(request);
					}else if("1516606174518".equals(strPageCode)){//�ظ�
						new com.page.method.Fun().MeasuresToolEntr(request);
					}else if("1516613463357".equals(strPageCode)){//�ظ�
						new com.page.method.Fun().MeasuresToolEntr(request);
					}else if("1506310525794".equals(strPageCode)){
						new com.page.method.Fun().MeasuresToolEntr(request);
					}
				// 	else if("1515723789958".equals(strPageCode)){
				// 		new com.page.method.Fun().MeasuresToolEntr(request);
				// 	}
					else if("1522727526758".equals(strPageCode)){//��������ͻ�������ϱ�
						new com.page.method.Fun().MeasuresToolEntr(request);
					}else if("1522732741869".equals(strPageCode)){//���㼼�Ĵ��³ɹ��ϱ�
						new com.page.method.Fun().MeasuresToolEntr(request);
					}else if("1522719345443".equals(strPageCode)){//���������ϱ�
						new com.page.method.Fun().MeasuresToolEntr(request);
					}


					if(db!=null){db.close();}
				}
				
			}catch (Exception e) {
			 //   MantraLog.fileCreateAndWrite(e);
	
				b = false;
				
				// String[] strArrayFlowLog22 = {"333",strFlowRunId,strFlowId,new Date()+"",strVersion,strAuditUser,"run",getErrorInfoFromException(e)};
				// insertFlowLog("1", strArrayFlowLog22);
				e.printStackTrace();
			}finally{
				if(exRun!=null){exRun.close();}
				if(exRun1!=null){exRun1.close();}
				if(exRun2!=null){exRun2.close();}
				if(exParent!=null){exParent.close();}
				if(exForm!=null){exForm.close();}
				if(exNode!=null){exNode.close();}
				if(exHZ!=null){exHZ.close();}
				if(dbf!=null){dbf.close();}
			
				return b;
			}
	         
		}
		/**
		 * ������Ϣ״̬
		 * @param _dbf
		 * @param _strFlowRunId
		 * @param _strAuditUser
		 */
		private void updateSendMsgZt(DBFactory _dbf, String _strFlowRunId,String _strAuditUser,String _strFlowId) {
			try {
				//ɾ����Ϣ��:��������ID,����ID,�汾��
				_dbf.sqlExe("delete from t_msg_records where S_YXID='"+_strFlowRunId+"'", true);
	//			_dbf.sqlExe("update t_msg_records set S_ZT=1 where S_YXID='"+_strFlowRunId+"'", true);
			} catch (Exception e) {
			 //   MantraLog.fileCreateAndWrite(e);
	//			if(_dbf!=null){_dbf.close();}
				// String[] strArrayFlowLog22 = {"333","333","updateSendMsgZt","updateSendMsgZt","updateSendMsgZt","_strFlowRunId",_strFlowRunId,getErrorInfoFromException(e)};
				// insertFlowLog("1", strArrayFlowLog22);
				e.printStackTrace();
			}
		}
	
		public void updateFormSql(String strSql, String strCon, String strType,String strFlowRunId) {
			//��ѯ��
			DBFactory dbf = new DBFactory();
			TableEx exForm = null;
			String sql = "";
			try {
	//			T_DQEZGZP.S_ID
				strCon = strCon.trim();
				String strTable = strCon.substring(0, strCon.indexOf("."));
				String strConTem = strCon.replace(strTable+".", "");
				exForm = dbf.query("select "+strConTem +" from "+strTable+" where S_RUN_ID='"+strFlowRunId+"'");
				//�滻����
				String[] strArrayCon = strConTem.split(",");
				for(int s1 = 0,s2 = strArrayCon.length;s1<s2;s1++){
					strSql = strSql.replace("<<"+strTable+"."+strArrayCon[s1]+">>", getColString(strArrayCon[s1].trim(),exForm.getRecord(0)));
				}
				//ִ��sql
				String[] strArryCon = strSql.split(";");
				for(int s3=0,s4=strArryCon.length;s3<s4;s3++){
					sql = strArryCon[s3];
					if(!"".equals(sql)){
						dbf.sqlExe(sql, true);
					}
				}
				String[] strArrayFlowLog22 = {"333","333","",new Date()+"","����ID"+strFlowRunId,"","updateFormSql",sql};
				insertFlowLog("1", strArrayFlowLog22);
				
			} catch (Exception e) {
			 //   MantraLog.fileCreateAndWrite(e);
				// String[] strArrayFlowLog22 = {"333","333","",new Date()+"","����ID"+strFlowRunId,"","updateFormSql",getErrorInfoFromException(e)};
				// insertFlowLog("1", strArrayFlowLog22);
				e.printStackTrace();
			}finally{
				if(exForm!=null){exForm.close();}
				dbf.close();
			}
		}
	
		public String getErrorInfoFromException(Exception e) {
			String sr = "";
			try {
	            StringWriter sw = new StringWriter();
	            PrintWriter pw = new PrintWriter(sw);
	            e.printStackTrace(pw);
	            sr = "\r\n" + sw.toString() + "\r\n";
	            sw.close();
	            pw.close();
	        } catch (Exception e2) {
	            return "ErrorInfoFromException";
	        }finally{
	        	return sr;
	        }
	    }
		public String getStringArryToString(String[] _arry){
			StringBuffer sr = new StringBuffer();
			for(int i=0,j=_arry.length;i<j;i++){
				sr.append(_arry[i]).append("|");
			}
			return sr.deleteCharAt(sr.length()-1).toString();
		}
		
		/**
		 * ��ѯ����������
		 * @param strFlowRunId
		 * @param strFlowParentId
		 * @return
		 */
		private boolean queryFlowRunIsOverSameLevel(String strFlowRunId, String strFlowParentId) {
			TableEx exParent = null;
			boolean bIsOver = true;
			try {
				exParent = new TableEx("I_ISOVER","T_SYS_FLOW_RUN"," S_RUN_ID='"+strFlowRunId+"' and S_FLOW_PARENT_ID='"+strFlowParentId+"'");
				int iCount = exParent.getRecordCount();
				for(int i=0;i<iCount;i++){
					if("0".equals(getColString("I_ISOVER", exParent.getRecord(i)))){
						bIsOver = false;
					}
				}
			} catch (Exception e) {
			 //   MantraLog.fileCreateAndWrite(e);
				e.printStackTrace();
			}finally{
				if(exParent!=null)
					exParent.close();
			}
			return bIsOver;
		}
	
		private String getAuditOtherReject(String strOther, String[] strOtherArrayNow, int index, int iRejectcount) {
			StringBuffer sbOther = new StringBuffer();
			sbOther.append(strOtherArrayNow[0]).append(",").append(strOtherArrayNow[1]).append(",").append(strOtherArrayNow[2]).append(",").append(strOtherArrayNow[3]).append(",").append(strOtherArrayNow[4]).append(",");
			sbOther.append(strOtherArrayNow[5]).append(",");
			sbOther.append(strOtherArrayNow[6]).append(",").append(iRejectcount+1);
			System.out.println("getAuditOtherReject�޸ĺ��strOther:"+sbOther);
			String[] strOtherArray = strOther.split("\\|",-1);
			strOtherArray[index]=sbOther.toString();
			String strOterTemp =strOtherArray[0];
			for(int i=1,j=strOtherArray.length;i<j;i++){
				strOterTemp=strOterTemp+"|"+strOtherArray[i];
			}
			return strOterTemp;
		}
	
		//,,,,,,,|,1,,,,,1,|,1,,,,,,|,1,,,,,,|,1,,,,,|,1,,,,,|,1,,,,,
		public String getAuditOtherPass(String strOther,String[] strOtherArrayNow,int index,int iCount) {//,1,,,,,
			StringBuffer sbOther = new StringBuffer();
			sbOther.append(strOtherArrayNow[0]).append(",").append(strOtherArrayNow[1]).append(",").append(strOtherArrayNow[2]).append(",").append(strOtherArrayNow[3]).append(",").append(strOtherArrayNow[4]).append(",");
			sbOther.append(strOtherArrayNow[5]).append(",");
			sbOther.append(iCount+1).append(",").append(strOtherArrayNow[6]);
			System.out.println("getAuditOtherPass�޸ĺ��strOther:"+sbOther);
			String[] strOtherArray = strOther.split("\\|",-1);
			strOtherArray[index]=sbOther.toString();
			String strOterTemp =strOtherArray[0];
			for(int i=1,j=strOtherArray.length;i<j;i++){
				strOterTemp=strOterTemp+"|"+strOtherArray[i];
			}
			return strOterTemp;
		}
	
		/**
		 * ��ǰ�ַ���ָ������֮���ΪĬ��
		 * @param strOther
		 * @param index
		 * @return
		 */
		public String getAuditStrArryDefault(String strOther, int index) {
			StringBuffer sbOther = new StringBuffer();
			String[] strOtherArray = strOther.split("\\|",-1);
			for(int i=index,j=strOtherArray.length;i<j;i++){
				String[] strOtherArraySon = strOtherArray[i].split(",",-1);
				sbOther.append(strOtherArraySon[0]).append(",");
				sbOther.append(strOtherArraySon[1]).append(",").append(strOtherArraySon[2]).append(",");
				sbOther.append(strOtherArraySon[3]).append(",").append(strOtherArraySon[4]).append(",");
				sbOther.append(strOtherArraySon[5]).append(",").append(",");
				sbOther.append(i==strOtherArray.length-1?"":"|");
			}		
			return sbOther.toString();
		}
	
		/**
		 * ���޸����³�ʼ������
		 * @param request
		 * @return
		 */
		@SuppressWarnings("finally")
		public Boolean processSave(HttpServletRequest request){
			Boolean b = true;
			TableEx exRun =null;
			TableEx tableEx =null;
			String strFlowRunId = "";
			String strFlowId ="";
			String strVersion="";
			try{
				/**1��������*/
				strFlowId = request.getParameter("NO_sys_flow_id");//����ID
				strVersion = request.getParameter("NO_sys_flow_Ver");//�汾��
				strFlowRunId = request.getParameter("NO_sys_S_RUN_ID");//����
				
					/**2��ѯ���б�*/
					exRun = queryFlowRun(strFlowId,strVersion,strFlowRunId);
					String strIsOverRun = exRun.getRecord(0).getFieldByName("I_ISOVER").value.toString();//�Ƿ����
					if("1".equals(strIsOverRun)){//�ж���ɷ���
						return b;
					}
					
					String strMsgs=exRun.getRecord(0).getFieldByName("S_AUDIT_MSG").value.toString();
					String strYqs=exRun.getRecord(0).getFieldByName("S_AUDIT_ARRAYYQ").value.toString();
					String strAuditUsersRun=exRun.getRecord(0).getFieldByName("S_AUDIT_ARRAY").value.toString();
					String strNodes=exRun.getRecord(0).getFieldByName("S_AUDIT_NODES").value.toString();
					String strLaunchUser = exRun.getRecord(0).getFieldByName("S_LAUNCH_USER").value.toString();
					String strLaunchBranch = exRun.getRecord(0).getFieldByName("S_LAUNCH_BRANCH").value.toString();
					String strStartNode = exRun.getRecord(0).getFieldByName("S_NODE_CODE").value.toString();//��ǰ�ڵ�
					String strOther = exRun.getRecord(0).getFieldByName("S_AUDIT_OTHER").value.toString();//����
					int index = Integer.parseInt(exRun.getRecord(0).getFieldByName("S_AUDIT_INDEX").value.toString());//����
					String strRunAudSel = exRun.getRecord(0).getFieldByName("S_AUDIT_SEL").value.toString();//�ֶ�ѡ���ӽڵ�
					String strSonFlow =  exRun.getRecord(0).getFieldByName("S_FLOW_SON").value.toString();//������
					String strFlowPj =  exRun.getRecord(0).getFieldByName("S_AUDIT_FSPJ").value.toString();//����Ʊ��
					/**3 ��ѯ���̽ڵ�*/
					tableEx = queryFlowNodeInfo(strFlowId,strVersion,"");
					
					/**4 ѭ��ƴ�Ӳ���*/
					String strAuditArrayyq="";//����
					String strAuditNodes="";//���нڵ�
					String strAuditOther ="";//����
					int strNextAuditUserIndex = index+1;//��һ�����ڵ�����,������Ĭ��Ϊ1
					String strAuditUsers = "";//����������
					String strAuditMsgs = "";//������Ϣģ��
					String strIsOver = "0";//�Ƿ����
					Record rd = null;//��ȡ��һ�ڵ����
					String strEndFlag = "";
					String strAudSel = "";//�Զ���ڵ�
					String strAudFlowPj = "";
					int icount = 0;
					String strAudSonFlow ="";//������
					String[] strNodesArray = strNodes.split("\\|",-1);
					String strCustomNodeId = request.getParameter("NO_custom_node_id");//�û��ֶ�ѡ��ڵ�
					boolean bFlag = true;
					 if(strCustomNodeId!=null&&!"".equals(strCustomNodeId)){//
						 strStartNode =strCustomNodeId;
						 //��ǰ�ڵ����������ֶ�ѡ��
						 //��ѯ���̽ڵ�
	//					 int iCount = tableEx.getRecordCount();
	//					 Record rd1 = null;
	//					 for(int i=0;i<iCount;i++){
	//						 rd1 = tableEx.getRecord(i);
	//						 //�ڵ�������ͬ-����Ϊ2,�ֶ�ѡ��ڵ�
	//						 if(strCustomNodeId.equals(getColString("I_NODE_ID", rd1))&&"2".equals(getColString("I_TYPE", rd1))&&!"".equals(getColString("S_AUDIT_SEL", rd1))){
	//							 //�������б�
	//							 String strDef = getColString("S_AUDIT_SEL",rd1);
	//							 String[] strRunAudSelArry = strRunAudSel.split("\\|",-1);
	//							 strRunAudSelArry[index]=strDef;
	//							 String[] strArrayFlowRunVal = {strFlowId,strFlowRunId,getStringArryToString(strRunAudSelArry)};
	//							 exRun.close();
	//							 updateFlowRun(strArrayFlowRunVal, "6");
	//							 bFlag = false;
	//							 break;
	//						 }
	//					 }
					 }else{
				            strStartNode = strNodesArray[index+1];
					 }
	//				 if(!bFlag){
	//					 return false;
	//				 }
					 //��һ��ѡ��ڵ�,û���� |||7,6,8||||12,13,14,15
					 //��һ��ѡ�����˽ڵ�,����������
					 //|||
					while(!"4".equals(strEndFlag)){
							rd = getNextNodeByCondition(request,strStartNode,tableEx,"2",strFlowRunId);
							if(rd==null){break;}
	
							strEndFlag = getColString("I_TYPE", rd);
							String strCustomNodeIds = getColString("S_AUDIT_SEL", rd);//�ֶ�ѡ��ڵ�
							if("2".equals(strEndFlag)&&strCustomNodeIds!=null&&!"".equals(strCustomNodeIds)){//�����ж��Ƿ��ֶ�ѡ��ڵ�
								strAudSel = strAudSel+strCustomNodeIds;
								break;
							}
							String strNodeAudit = queryAuditPerson(strLaunchUser,strLaunchBranch,getColString("S_AUDIT_BRANCH", rd),getColString("S_AUDIT_ROLE", rd),getColString("S_AUDIT_USER", rd),getColString("S_AUDIT_SQRYATTR", rd),getColString("S_AUDIT_SQRY", rd),request,rd,strFlowRunId);

							if("5".equals(strEndFlag)){//������,�洢���������̺�/�汾�����к�/��ID
								strNodeAudit = "S";
							}
							strAudSonFlow = (icount==0?"":(strAudSonFlow+"|"))+getColString("S_FLOW_SON", rd);
							strAudSel = (icount==0?"":(strAudSel+"|"))+strCustomNodeIds;
							strAuditArrayyq =(icount==0?"":(strAuditArrayyq+"|")) +getColString("S_AUDIT_YQTS", rd)+","+getColString("S_AUDIT_YQTSCL", rd);//��������  �������ڲ���
							strAuditMsgs =(icount==0?"":(strAuditMsgs+"|"))+getColString("S_AUDIT_TZXX", rd);//������Ϣģ��
							strAuditNodes = (icount==0?"":(strAuditNodes+"|"))+getColString("I_NODE_ID", rd);
							strAuditOther =(icount==0?"":(strAuditOther+"|")) +getColString("S_AUDIT_TG", rd)+","+getColString("S_AUDIT_THJD", rd)+","+getColString("S_AUDIT_THJDZD", rd)+","+getColString("S_TZLC", rd)+","+getColString("S_AUDIT_PREEMPTION", rd)+","+getColString("S_AUD_VAL", rd)+","+",";;
							strStartNode = getColString("S_CHILD_ID", rd);
							strAudFlowPj =(icount==0?"":(strAudFlowPj+"|"))+getColString("S_AUDIT_FSPJ", rd);//����Ʊ��
							//�Ƿ����� ������������ �����������ش��� ������
							if("".equals(strNodeAudit)){
								strNodeAudit = ("1".equals(getColString("S_AUDIT_TG", rd)))?"T":strNodeAudit;//����
							}
							strAuditUsers = (icount==0?"":(strAuditUsers+"|"))+strNodeAudit;

							icount=1;
					}
					/**5��װ����,�м䱣��ӵ�ǰ�ڵ��޸�������Ϣ*/
					strAuditUsersRun = getAuditStrArrySave(strAuditUsersRun,strAuditUsers,index);//0,1,2,3,4,5  ����index:2 ,����index3,��ǰ�ڵ�֮ǰֵ(������ǰ)+ ��ǰ�ڵ�֮��ֵ(������ǰ)
						
					strYqs = getAuditStrArrySave(strYqs,strAuditArrayyq,index);
					strMsgs = getAuditStrArrySave(strMsgs,strAuditMsgs,index);
					strSonFlow = getAuditStrArrySave(strSonFlow,strAudSonFlow,index);
					strNodes = getAuditStrArrySave(strNodes,strAuditNodes,index);
					strOther= getAuditStrArrySave(strOther,strAuditOther,index);
					strRunAudSel = getAuditStrArrySave(strRunAudSel,strAudSel,index);
					strFlowPj = getAuditStrArrySave(strFlowPj,strAudFlowPj,index);
					System.out.println(strRunAudSel);
						/**6 ���� 1:��*/
	//					strNextAuditUserIndex = getNodesInfo(strAuditUsersRun,strOther,strNextAuditUserIndex);
	//					strNextAuditUser =strAuditUsersRun.split("\\|",-1)[strNextAuditUserIndex];
	//					strNodeIdNext =strNodes.split("\\|")[strNextAuditUserIndex];
						/**�����Ƿ����*/
	//					if("".equals(strAudSel.split("\\|",-1)[index])){//���ӽڵ�ͣ��,�ж��Ƿ����
	//						strIsOver = ((strNextAuditUserIndex+1) == strNodesArray.length)?"1":strIsOver;
	//					}
					/**�жϵ�ǰ�ڵ��������Ƿ��ȡֵ*/
					String strNextAuditUser = queryAuditPersonIsColumn(strAuditUsersRun.split("\\|",-1)[index+1], strFlowRunId, strFlowId, strVersion, strNodes.split("\\|",-1)[index+1]);
					String[] strArryAuditUsers = strAuditUsersRun.split("\\|",-1);
					

					strArryAuditUsers[index+1]=strNextAuditUser;
					strAuditUsersRun = getStringArryToString(strArryAuditUsers);

						/**7 �������б� 4������*/
						String[] strArrayFlowRun = {strFlowId,strFlowRunId,strVersion,strMsgs,strYqs,strAuditUsersRun,strNodes,strOther,strIsOver,strRunAudSel,strSonFlow,strFlowPj};
						updateFlowRun(strArrayFlowRun,"3");
			}catch (Exception e) {
			 //   MantraLog.fileCreateAndWrite(e);
				b = false;
				// String[] strArrayFlowLog22 = {"333",strFlowRunId,strFlowId,new Date()+"",strVersion,"","save",getErrorInfoFromException(e)};
				// insertFlowLog("1", strArrayFlowLog22);
				
				e.printStackTrace();
			}finally{
				exRun.close();
				if(tableEx!=null){tableEx.close();}
				return b;
			}
		}
		
		
		public TableEx queryFlowMainTableEx(String _strFlowId,String _strOrgId){
			TableEx exFlowMain =null;
			try {
				exFlowMain = new TableEx("S_AUDIT_VERSION,S_FORMS", "T_SYS_FLOW_MAIN", "S_FLOWID='"+_strFlowId+"' and I_FLOWSTATUS='0' "+((_strOrgId==null||"".equals(_strOrgId))?"":(" and S_ORG_ID='"+_strOrgId+"'"))+" ORDER BY S_AUDIT_VERSION DESC");
			} catch (Exception e) {
			 //   MantraLog.fileCreateAndWrite(e);
				if(exFlowMain!=null){exFlowMain.close();}
				e.printStackTrace();
			}
			return exFlowMain;
		}
	
		/**
		 * ��������ID��ѯ��������������̰汾
		 * @param _strFlowId
		 * @param _strOrgId
		 * @return
		 */
		public String queryFlowMaiByFlowId(String _strFlowId,String _strOrgId){
			String strVersion = "";
			TableEx exFlowMain =null;
			try {
				exFlowMain = queryFlowMainTableEx(_strFlowId, _strOrgId);
				Record rd = exFlowMain.getRecord(0);
				strVersion = getColString("S_AUDIT_VERSION", rd)+","+getColString("S_FORMS", rd);
			} catch (Exception e) {
			 //   MantraLog.fileCreateAndWrite(e);
				e.printStackTrace();
			}finally{
				exFlowMain.close();
			}
			return strVersion;
		}
		
		public Map<String,String> processAudAll(HttpServletRequest request){
			
			Map<String,String> map = new HashMap<String, String>();
			String processAudCustomNodeIds = "";
			String processAuditSelectNode = "";
			String processNodeAudit = "";
			String strFlowPj = "";
			TableEx exRun =null;
			TableEx exRunNowNode =null;
			String nowNodeName="";
			String nowNodeId="";
			String strAuditFlag = "";
			/**��������*/
			String strFlowId = request.getParameter("s_flow_id");
	        String strFlowRunId = request.getParameter("sys_flow_run_id");
	        String strVersion = request.getParameter("flow_ver");
			
			/**��ѯ����������Ϣ*/
			try {
				exRun = queryFlowRun(strFlowId==null?"":strFlowId,strVersion==null?"":strVersion,strFlowRunId==null?"":strFlowRunId);
				processAudCustomNodeIds = processAudCustomNodeIds(request, exRun);
				processAuditSelectNode = processAuditSelectNode(request, exRun);
				processNodeAudit = processNodeAudit(request, exRun);
				int index = Integer.parseInt(exRun.getRecord(0).getFieldByName("S_AUDIT_INDEX").value.toString());//����
				strFlowPj = exRun.getRecord(0).getFieldByName("S_AUDIT_FSPJ").value.toString().split("\\|",-1)[index];//����Ʊ��;
				
				nowNodeId = exRun.getRecord(0).getFieldByName("S_NODE_CODE").value.toString();
				strAuditFlag = exRun.getRecord(0).getFieldByName("S_AUDIT_OTHER").value.toString().split("\\|",-1)[index].split(",",-1)[1];
				exRunNowNode = new TableEx("S_NODE_NAME","t_sys_flow_node"," s_flow_id='"+strFlowId+"' and S_AUDIT_VERSION='"+strVersion+"' and I_NODE_ID='"+nowNodeId+"'");
				nowNodeName = exRunNowNode.getRecord(0).getFieldByName("S_NODE_NAME").value.toString();
			} catch (Exception e) {
			 //   MantraLog.fileCreateAndWrite(e);
				e.printStackTrace();
			}finally{
				exRun.close();
				exRunNowNode.close();
			}
			
	//		("\r|\n", "");
			map.put("processAudCustomNodeIds", processAudCustomNodeIds.replaceAll("\r|\n", ""));
			map.put("processAuditSelectNode", processAuditSelectNode.replaceAll("\r|\n", ""));
			map.put("processNodeAudit", processNodeAudit.replaceAll("\r|\n", ""));
			map.put("strFlowPj", strFlowPj+"|");
			map.put("nowNodeName", nowNodeName.replaceAll("\r|\n", ""));
			map.put("auditFlag",("".equals(strAuditFlag)||"7".equals(strAuditFlag)||"0".equals(strAuditFlag))?"":"1");
			return map;
		}
		
		/**
		 * �Զ���ѡ��ڵ�
		 * @param request
		 * @return
		 */
		public String processAudCustomNodeIds(HttpServletRequest request,TableEx exRun){
			TableEx exTRGXX =null;
			String strResult = "";
			try{
				/**��������*/
				String strFlowId = request.getParameter("s_flow_id");
	            String strFlowRunId = request.getParameter("sys_flow_run_id");
	            String strVersion = request.getParameter("flow_ver");
				
				/**��ѯ����������Ϣ*/
	//			exRun = queryFlowRun(strFlowId==null?"":strFlowId,strVersion==null?"":strVersion,strFlowRunId==null?"":strFlowRunId);
				if(exRun.getRecordCount()>0){
					String strCustomNodes = getColString("S_AUDIT_SEL", exRun.getRecord(0));
					String strNodeIds = getColString("S_AUDIT_NODES", exRun.getRecord(0));
					int index = Integer.parseInt(getColString("S_AUDIT_INDEX", exRun.getRecord(0)));
					strCustomNodes = strCustomNodes.split("\\|",-1)[index];
					if(!"".equals(strCustomNodes)){
						exTRGXX = new TableEx("I_NODE_ID,S_NODE_NAME,I_TYPE","T_SYS_FLOW_NODE","1=1  and I_NODE_ID in("+strCustomNodes+")" +"and S_FLOW_ID='"+strFlowId+"' and S_AUDIT_VERSION ='"+strVersion+"'");
						Record rd = null;
						for(int i=0,j=exTRGXX.getRecordCount();i<j;i++){
							rd = exTRGXX.getRecord(i);
							strResult = ("".equals(strResult)?"":(strResult+"|"))+getColString("I_NODE_ID", rd)+","+getColString("S_NODE_NAME", rd)+","+getColString("I_TYPE", rd);
						}
					}
	//				else if("S".equals(strNodeIds.split("\\|",-1)[index+1])){
	//					String[] strFlowSonArray = getColString("S_FLOW_SON", exRun.getRecord(0)).split("\\|",-1);
	//					index++;
	//					strResult = "SON"+"|"+strFlowSonArray[index]+"|"+strFlowRunId+"|"+queryFlowMaiByFlowId(strFlowSonArray[index],request.getParameter("strOrgId"));
	//				}
				}
			}catch (Exception e) {
			 //   MantraLog.fileCreateAndWrite(e);
				e.printStackTrace();
			}finally{
				if(exTRGXX!=null){exTRGXX.close();}
				return strResult+"|";
			}
		}
		/**
		 * ��������ѡ��ڵ�
		 * @param request
		 * @return
		 */
		@SuppressWarnings("finally")
		public String processAuditSelectNode(HttpServletRequest request,TableEx exRun){
			String strResult = "";
			try{
				/**��������*/
				String strFlowId = request.getParameter("s_flow_id");
	            String strFlowRunId = request.getParameter("sys_flow_run_id");
	            String strVersion = request.getParameter("flow_ver");
				
				/**��ѯ����������Ϣ*/
	//			exRun = queryFlowRun(strFlowId==null?"":strFlowId,strVersion==null?"":strVersion,strFlowRunId==null?"":strFlowRunId);
				if(exRun.getRecordCount()>0){
				    
				    if (exRun.getRecord(0).getFieldByName("S_NODE_CODE").value==null ||exRun.getRecord(0).getFieldByName("S_AUDIT_INDEX").value==null) { // ���˿�ָ��

        				return strResult;
        			}
        			
					String strRunNode = exRun.getRecord(0).getFieldByName("S_NODE_CODE").value.toString();
					int index = Integer.parseInt(exRun.getRecord(0).getFieldByName("S_AUDIT_INDEX").value.toString());
					
					String strReject ="";
					if (exRun.getRecord(0).getFieldByName("S_AUDIT_OTHER").value==null ) { // ���˿�ָ��
        				strReject="";
        			}else{
        			    strReject = exRun.getRecord(0).getFieldByName("S_AUDIT_OTHER").value.toString().split("\\|",-1)[index].split(",")[1];
        			}
					if("4".equals(strReject)){
						strResult = queryFlowLogBeforeAll(strFlowId, strVersion, strFlowRunId,strRunNode);
					}
				}
			}catch (Exception e) {
			 //   MantraLog.fileCreateAndWrite(e);
				e.printStackTrace();
			}finally{
				return strResult;
			}
		}
		
		/**
		 * ��ǰ�ڵ�ָ��������-����
		 * @param request
		 * @return
		 */
		@SuppressWarnings("finally")
		public String processNodeAudit(HttpServletRequest request,TableEx exRun){
			TableEx exTRGXX =null;
			StringBuffer strResult  = new StringBuffer();
			try{
				/**��������*/
				String strFlowId = request.getParameter("s_flow_id");
	            String strFlowRunId = request.getParameter("sys_flow_run_id");
	            String strVersion = request.getParameter("flow_ver");
				/**��ѯ����������Ϣ*/
	//			exRun = queryFlowRun(strFlowId==null?"":strFlowId,strVersion==null?"":strVersion,strFlowRunId==null?"":strFlowRunId);
				if(exRun.getRecordCount()>0){
					//S_AUDIT_OTHER S_AUDIT_ARRAYYQ S_AUDIT_INDEX
					int index = Integer.parseInt(exRun.getRecord(0).getFieldByName("S_AUDIT_INDEX").value.toString());
					String strMsgs=exRun.getRecord(0).getFieldByName("S_AUDIT_MSG").value.toString();
					String strAuditUsers=exRun.getRecord(0).getFieldByName("S_AUDIT_ARRAY").value.toString();
					String strLaunchUser = exRun.getRecord(0).getFieldByName("S_LAUNCH_USER").value.toString();
	//				String strLaunchBranch = exRun.getRecord(0).getFieldByName("S_LAUNCH_BRANCH").value.toString();
					String strNodeIdNow = exRun.getRecord(0).getFieldByName("S_NODE_CODE").value.toString();//��ǰ�ڵ�
					String strOther = exRun.getRecord(0).getFieldByName("S_AUDIT_OTHER").value.toString();//����
	//				strFlowPj = exRun.getRecord(0).getFieldByName("S_AUDIT_FSPJ").value.toString();//����Ʊ��
					String[] strArrayAud = strAuditUsers.split("\\|",-1);
					
					int iNextAuditUserIndex = getNodesInfoRun(strAuditUsers,strOther,index,strLaunchUser,strMsgs.split("\\|",-1),strFlowId,strVersion,strFlowRunId,strNodeIdNow);
					
					if(strArrayAud[iNextAuditUserIndex].split(",").length>1){//���������
						if("ZD".equals(strOther.split("\\|",-1)[iNextAuditUserIndex].split(",",-1)[4])){//ָ��ģʽ
							exTRGXX = new TableEx("SYGZW,SYGMC","T_RGXX","1=1  and SYGZW in("+strArrayAud[iNextAuditUserIndex]+")");//and SROLECODE in("+_strRoleIds+")
							Record  rd = null;
							for(int i=0,j=exTRGXX.getRecordCount();i<j;i++){
								rd = exTRGXX.getRecord(i);
								strResult.append(getColString("SYGZW", rd)).append(",").append(getColString("SYGMC", rd)).append("|");
							}
						}
					}
				}
			}catch (Exception e) {
			 //   MantraLog.fileCreateAndWrite(e);
				e.printStackTrace();
			}finally{
				if(exTRGXX!=null){exTRGXX.close();}
				return strResult.toString();
			}
		}	
	
	
		/**
		 * ������Ϣ
		 * @param _strArrayMsgIds
		 * @param _strArrayUserIds
		 * @param _strType
		 * @param _strIsOver
		 * @param _strFlowId
		 * @param _strVersion
		 * @param _strFlowRunId
		 * @param _strNodeId
		 * @param request
		 * @param _strFlowType
		 */
		public String sendMsg(String _strArrayMsgIds,String _strArrayUserIds,String _strType,String _strIsOver,String _strFlowId,String _strVersion,String _strFlowRunId,String _strNodeId,HttpServletRequest request,String _strFlowType,String _strPageCode,String _strTab){
			
			DBFactory dbf = new DBFactory();
			String strLoginUserName ="";
			String strPageCode  ="";
			Object strLoginBranchName  ="";
		
			if(request==null){
				strPageCode =_strPageCode;
				strLoginUserName="system";
				strLoginBranchName="ϵͳ";
			}else{
				strLoginUserName = request.getSession().getAttribute("SYS_STRCURUSERNAME").toString();//��¼������
				strPageCode = request.getParameter("SPAGECODE");//ҳ�����
				strLoginBranchName = request.getSession().getAttribute("SYS_STRBRANCHNAME");//��¼�˲�������
//				_strFlowRunId=request.getParameter("NO_sys_flow_id");
			}
	//		SYS_STRBRANCHID�����ɣ�
// 		switch (_strType) {// ���״̬:0��1��2����3�ύ4����5��������6�����˻�7����
// 			case "0":
// 				_strType="����";
// 				break;
// 			case "1":	
// 				_strType="���ͨ��";
// 				break;
// 			case "2":
// 				_strType="����";
// 				break;
// 			case "3":
// 				_strType="�ύ";
// 				break;
// 			case "4":
// 				_strType="��������";
// 				break;
// 			case "5":
// 				_strType="��������";
// 				break;
// 			case "6":
// 				_strType="�����˻�";
// 				break;
// 			case "7":
// 				_strType="����";
// 				break;
// 			case "8":
// 				_strType="����";
// 				break;
// 		}
// 		if("1".equals(_strIsOver)){
// 			_strType="���̽���";
// 		}
switch (_strType) {// 审核状�&#65533;&#65533;:0�&#65533;1�&#65533;2作废3提交4逾期5逾期作废6逾期�&#65533;�&#65533;7跳岗
			case "0":
				_strType="驳回";
				break;
			case "1":	
				_strType="审核通过";
				break;
			case "2":
				_strType="作废";
				break;
			case "3":
				_strType="提交";
				break;
			case "4":
				_strType="逾期审批";
				break;
			case "5":
				_strType="逾期作废";
				break;
			case "6":
				_strType="逾期�&#65533;�&#65533;";
				break;
			case "7":
				_strType="跳岗";
				break;
			case "8":
				_strType="结束";
				break;
		}
		if("1".equals(_strIsOver)){
			_strType="流程结束";
		}

        
		String strMsgContent = queryMsgTemplet(_strArrayMsgIds);
//���Ա��� 
        
       
	
			TableEx exRun = queryFlowRun(_strFlowId, _strVersion, _strFlowRunId);
			exRun.close();
			
	//		String[] strArrayUserIds = _strArrayUserIds.split(",");
	//		for(int i=0,j=strArrayUserIds.length;i<j;i++){
	//			if("".equals(strArrayUserIds[i])){continue;}
			String sid  = ""; // request.getParameter("S_ID");
			String bmid  = ""; // request.getParameter("BMID");
			String stype  = ""; // request.getParameter("STYPE");
			String djh = ""; //request.getParameter("DJH");
				String strNumberId = System.currentTimeMillis()+"";
				
			
				
				strMsgContent = strMsgContent.replace("${username}", strLoginUserName);//${username} ${active}����,�������к�:${numberid} ${branchname}
				strMsgContent = strMsgContent.replace("${active}", _strType);
				strMsgContent = strMsgContent.replace("${numberid}", _strFlowRunId);
				strMsgContent = strMsgContent.replace("${branchname}", (strLoginBranchName==null||"".equals(strLoginBranchName))?"":strLoginBranchName.toString());
				strPageCode = strPageCode==null?_strPageCode:strPageCode;
				if(strPageCode==null||"".equals(strPageCode)){

					String[] strArraySon = queryFlowMaiByFlowId(_strFlowId,"").split(",",-1);
	//				String[] strArraySon = queryFlowMaiByFlowId(request.getParameter("NO_sys_flow_id"),"").split(",",-1);
					strPageCode=strArraySon[1];
				}
				String[] strArray = setMsgParVal(sid, bmid, stype, djh, strPageCode, _strFlowRunId,dbf,request,_strIsOver);
				sid = strArray[0];
				bmid = strArray[1];
				djh = strArray[2];

				String[] strArrayValues={strPageCode,_strVersion,"system",strSdfYmdHms.format(new Date()),strNumberId,_strNodeId,_strArrayUserIds,_strFlowId,"0",_strArrayMsgIds,"system",strMsgContent,_strFlowRunId,"0",_strFlowType,sid,bmid,stype,djh};

				updateMsgs("1",strArrayValues);

				dbf.close();
				return strPageCode;

		}
		private String[] setMsgParVal(String _strSid,String _strBmid,String _strType,String _strDjh,String _strPageCode,String _strRunId,DBFactory _dbf,HttpServletRequest request,String _strIsOver){
			TableEx ex = null;
			TableEx exForm = null;
			String strTableName = "";
			String[] strArray=new String[3];
			try{
	//			ex = _dbf.query("select S_SID,S_ZZ,S_TYPE,S_DJH,S_TABLE from T_SYS_FLOW_PAR where S_SPAGECODE='"+_strPageCode+"'");
				ex = _dbf.query("select * from T_SYS_FLOW_PAR where S_SPAGECODE='"+_strPageCode+"'");
	//			ex = new TableEx("S_SID,S_ZZ,S_TYPE,S_DJH,S_TABLE", "T_SYS_FLOW_PAR", "S_SPAGECODE='"+_strPageCode+"'");
				Record rd = null;
				String strSidF = "";
				String strTableNameF="";
				String strDjhF="";
				String strBz="";
				String strBzF="";
				String strClass="";
				String strMethod="";
				String strRelation="";
				if(ex.getRecordCount()>0){
					rd = ex.getRecord(0);
					_strSid = getColString("S_SID", rd);
					_strBmid = getColString("S_ZZ", rd);
	//				_strType = getColString("S_TYPE", rd);
					_strDjh = getColString("S_DJH", rd);
					strTableName = getColString("S_TABLE", rd);
					strSidF = getColString("S_IDF", rd);
					strTableNameF = getColString("S_TABLEF", rd);
					strDjhF = getColString("S_DJHF", rd);
					strBz = getColString("S_BZ", rd);
					strBzF = getColString("S_BZF", rd);
					strClass = getColString("S_CLASSPATH", rd);
					strMethod = getColString("S_METHOD", rd);
					strRelation = getColString("S_OTHER", rd);
				}
				String strAuditState = "1";//���״̬ ���״̬:0����1ͨ��2����3�ύ4����5��������6�����˻�
				if(request!=null){
					strAuditState = request.getParameter("NO_sys_flow_state");//���״̬ ���״̬:0����1ͨ��2����3�ύ4����5��������6�����˻�
				}
//				MantraLog.WriteProgress(MantraLog.LOG_PROGRESS ,"--------"+request+"---------"+request.getParameter("NO_sys_flow_state"));
				if(!"".equals(strTableName)){
					if(!"".equals(strTableNameF)){
						String strCol = _strSid+","+_strBmid+","+_strDjh+","+strTableNameF+"."+strSidF+" AS 'sidf',"+strTableNameF+"."+strDjhF+" AS 'djhf' ";
//						MantraLog.WriteProgress(MantraLog.LOG_PROGRESS ,"--------"+strCol+"---------");
						exForm = _dbf.query("select "+strCol+" from "+ strTableName+","+strTableNameF+" where S_RUN_ID='"+_strRunId+"' and "+strRelation);
					}else{
//						MantraLog.WriteProgress(MantraLog.LOG_PROGRESS ,"--------"+strTableName+"---------"+_strRunId+"---");
						exForm = new TableEx("*",strTableName, "S_RUN_ID='"+_strRunId+"'");
					}
	//				exForm = _dbf.query("select * from "+ strTableName+" where S_RUN_ID='"+_strRunId+"'");
					if(exForm.getRecordCount()>0){
						Record rd1 = exForm.getRecord(0);
						strArray[0] = getColString(_strSid, rd1);
						strArray[1] = getColString(_strBmid, rd1);
	//					_strType = getColString(_strType, rd);
						strArray[2] = getColString(_strDjh, rd1);
//						MantraLog.WriteProgress(MantraLog.LOG_PROGRESS ,"--------"+strArray[0] +"---------"+strArray[1]+"---"+strArray[2]);
						if("1".equals(_strIsOver)){
						    //recordRel(String S_ORGANISATION,String S_LEFT_PAGECODE,String LEFT_ID,String LEFT_NAME,String S_RIGHT_PAGECODE,String RIGHT_ID,String RIGHT_NAME)
							//new com.timing.impcl.MantraUtil().recordRel(strArray[0],strTableName,"".equals(strSidF)?"":getColString("sidf", rd1),"".equals(strTableNameF)?"":getColString(strTableNameF, rd1));
						}
					}
				}
				if(!"".equals(strMethod)&&!"".equals(strClass)&&"1".equals(_strIsOver)&&request!=null){
					reflectMothedInvoke(strClass, strMethod, request);
				}
			}catch (Exception e) {
				// MantraLog.fileCreateAndWrite(e);
				// String[] strArrayFlowLog22 = {"333",_strRunId,_strSid,new Date()+"",_strPageCode,"","setMsgParVal",getErrorInfoFromException(e)};
				// insertFlowLog("1", strArrayFlowLog22);
				if(ex!=null){ex.close();}
				if(exForm!=null){exForm.close();}
				e.printStackTrace();
			}finally{
				if(ex!=null){ex.close();}
				if(exForm!=null){exForm.close();}
				return strArray;
			}
		}
		public void updateMsgs(String _strType,String[] _strArrayValues){
			DBFactory dbf = new DBFactory();
			try {
				if("1".equals(_strType)){
					
					String strTabCol ="(S_PAGECODE,S_BBH,S_FSR,S_FSSJ,S_ID,S_JDID,S_JSR,S_LCID,S_SCBS,S_XXID,S_XXLX,S_XXNR,S_YXID,S_ZT,S_FLOW_TYPE,S_SID,S_BMID,S_TYPE,S_DJH)";
					_strArrayValues = arrayAddSingleQuotes(_strArrayValues);
					String strTabVal = Arrays.toString(_strArrayValues);
					strTabVal = strTabVal.substring(1,strTabVal.length()-1);
					//2018-04-19 16:26:04   ע��
					//MantraLog.WriteProgress(MantraLog.LOG_PROGRESS ,"insert into T_MSG_RECORDS "+strTabCol+" values("+strTabVal+")");
					dbf.sqlExe("insert into T_MSG_RECORDS "+strTabCol+" values("+strTabVal+")", true);
				}else{
				}
			} catch (Exception e) {
				// MantraLog.fileCreateAndWrite(e);
				// String[] strArrayFlowLog22 = {"333",_strType,_strType,new Date()+"",_strType,"","updateMsgs",getErrorInfoFromException(e)};
				// insertFlowLog("1", strArrayFlowLog22);
				e.printStackTrace();
			} finally {
				dbf.close();
			}
		}
		
		
		/**
		 * ��ѯ��Ϣģ��
		 * @param _strMsgIds
		 * @return
		 */
		public String queryMsgTemplet(String _strMsgIds){
			TableEx ex = null;
			String strMsgTem = "";
			try {
				ex = new TableEx("S_MBNR", "T_XXGL"," 1=1 and S_ID ='"+_strMsgIds+"'");
				if(ex.getRecordCount()>0){
					strMsgTem= ex.getRecord(0).getFieldByName("S_MBNR").value.toString();
				}
			} catch (Exception e) {
			 //   MantraLog.fileCreateAndWrite(e);
				e.printStackTrace();
			}finally{
				ex.close();
			}
			return strMsgTem;
		}
		
		public String getRequestParam(HttpServletRequest _request, String _strReplaceStr) {
			Enumeration enu = _request.getParameterNames();
			while (enu.hasMoreElements()) {
				String paraName = (String) enu.nextElement();
				_strReplaceStr = _strReplaceStr.replace(paraName, "'"+_request.getParameter(paraName)+"'");
			}
			return _strReplaceStr;
		}
		
		/**
		 * ���������ж�
		 * @param _strCon
		 * @return
		 */
		public String appendConditionSql(HttpServletRequest _request,String _strCon,String _strType,String _strRunId){//���������ж�,û���������ؿ�
	//		2:  T_table1.qjts_2 <  3  $6:  T_table1.qjts_2 >=  3 
	//		30:   T_DQEZGZP.S_QF_SMGLYQM  <>  -*--*-   $29:   T_DQEZGZP.S_QF_SMGLYQM  =  -*--*-    $
	//		25:   T_DQEZGZP.S_SPJG  =  -*-BHG-*-   $24:   T_DQEZGZP.S_SPJG  =  ��HG��   $
	//		108:   T_DQYZGZP.S_GLYQM_NAME  <>  -*--*-   $118:   T_DQYZGZP.S_GLYQM  =  -*--*-   $
	//		�ڵ����:  ����  �ָ��$ ......
			DBFactory dbf = new DBFactory();
			_strCon = _strCon.replace("-*-", "'");
			_strCon = _strCon.replace("^", "%");
			String[] strNodeArray = _strCon.split("\\$");
			
			String strTable = "";
			String sql = "";
			String sqlCon = "";
			String strNode="";
			String st="";
			TableEx ex = null;
			for(int i=0,j=strNodeArray.length;i<j;i++){
				if(strNodeArray[i].trim().length()==0){continue;}
				String strNodeId = strNodeArray[i].substring(0,strNodeArray[i].indexOf(":")).trim();//�ڵ�ɣ�
				strNodeArray[i] = strNodeArray[i].substring(strNodeArray[i].indexOf(":")+1,strNodeArray[i].length());//ȥ��'ð��'
				strTable = strNodeArray[i].substring(0,strNodeArray[i].indexOf("."));//�õ�����
				strNodeArray[i] = strNodeArray[i].replace(strTable+".", strTable+"$");//.�滻Ϊ$
				if("2".equals(_strType)){
					String str = strNodeArray[i].trim();
					strTable = strTable.trim();
					try {
						String strCol = str.substring(str.indexOf(strTable)+strTable.length()+1,str.indexOf(" "));
						if(_request!=null){
							_strRunId=_request.getParameter("NO_sys_S_RUN_ID");
						}
						st = "select "+strCol+" from "+strTable+" where S_RUN_ID='"+_strRunId+"'";
						ex = dbf.query("select "+strCol+" from "+strTable+" where S_RUN_ID='"+_strRunId+"'");
						sql =strNodeArray[i].replace(strTable+"$"+strCol,"'"+getColString(strCol, ex.getRecord(0))+"'"); 
					} catch (Exception e) {
					   // MantraLog.fileCreateAndWrite(e);
						e.printStackTrace();
				// 		String[] strArrayFlowLog22 = {"333","333","appendConditionSql","appendConditionSql",_strCon,"appendConditionSql",_strType,st+"----"+getErrorInfoFromException(e)};
				// 		insertFlowLog("1", strArrayFlowLog22);
						if(ex!=null){ex.close();}
					}finally{
						if(ex!=null){ex.close();}
					}
				}else{
					sql = getRequestParam(_request,strNodeArray[i]);
				}
				
				sqlCon = queryConditionSql(sql);
				if("1".equals(sqlCon)){//1:true
					strNode = strNodeId;
					break;
				}
	 			sb.append("sql:"+sql+  "  sqlCon  "+sqlCon);
			}
			dbf.close();
			return strNode;
		}
		private String queryConditionSql(String _sql){
			String strResult = "";
			TableEx ex = null;
			try {
				ex = new TableEx(_sql +" as 'xx'", "T_CONDITION","1=1");
				strResult = ex.getRecord(0).getFieldByName("xx").value.toString();
			} catch (Exception e) {
			 //   MantraLog.fileCreateAndWrite(e);
				e.printStackTrace();
			}finally{
				ex.close();
			}
			return strResult;
		}
		/**
		 * true:���� false:����
		 * @param strLaunchDate
		 * @param strYqs
		 * @param index
		 * @return
		 */
		public boolean isYuQi(String strLaunchDate,String strYqs,int index){
			boolean b = true;
			String[] strArrayYq = strYqs.split("\\|",-1)[index].split(",",-1);
			if(!"".equals(strArrayYq[0])){//����
				if(new Date().getTime()>dateCal(strLaunchDate,Integer.parseInt(strArrayYq[0])).getTime()){
					b = false;
				}
			}
			return b;
		}
		//2017-08-18 15:06:44 ,|1,TGJD|1,TGJD|1,ZDTH|1,TGJD|,TGJD|,  2
		/**
		 * ��ʼ�ڵ��ж��Ƿ�����
		 * @param _strStartUserId ǰ̨����
		 * @param _strStartRole
		 * @param _strStartBranchId
		 * @param _strBranchIds �ڵ�ȡֵ
		 * @param _strRoleIds
		 * @param _strUserIds
		 * @return
		 */
		public boolean queryFlowStartPerson(String _strStartUserId,String _strStartRole,String _strStartBranchId,String _strBranchIds,String _strRoleIds,String _strUserIds){
			if(compareArrayRepeat(_strStartRole,_strRoleIds)){//---�����˽�ɫ��ڵ��ɫһ��
				return true;
			}else {
				return false;
			}
		}
		
		/**
		 * ���ҽڵ�������
		 * @param _strLanuchUserId������
		 * @param _strLanuchBranchId
		 * @param _strBranchIds �ڵ�
		 * @param _strRoleIds
		 * @param _strUserIds
		 * @return
		 */
		public String queryAuditPerson(String _strLanuchUserId,String _strLanuchBranchId,String _strBranchIds,String _strRoleIds,String _strUserIds,String _strAttr,String _strZj,HttpServletRequest _request,Record rd,String _strFlowRunId){
			String strAuditIds = "";
			//ѡ������------------ֱ�ӷ�����
			String strGw = "";
			strGw = getColString("S_AUDIT_GW", rd);
	//		String strGw = getColString("S_AUDIT_YHZ", rd);
			if(!"".equals(_strUserIds)){
				strAuditIds =_strUserIds;
			}
	//		else if(strGw!=null&&!"".equals(strGw)){
	//			strAuditIds = queryUserByGw(strGw);
	//		}
			else if(_strAttr!=null&&!"".equals(_strAttr)){
				//ѡ������Ȩ��Ա
				//��ѯ�����ڲ��ţ�_strLanuchBranchId, ���ݷ��������ڲ���ID���������ϲ�ѯ�������д˽�ɫ����
	//			t_SYS_ROLE SROLECODE(��ɫ����)
	//			t_SYS_BRANCH S_CODE(���ű��)
	//			t_RGXX SYGZW(�˺�) SROLECODE����ɫ���룩 SROLEBH����ɫ��ţ� SBRANCHID(��֯���)
	         
				strAuditIds = queryUserIdBySqRoles(_strAttr,_strLanuchBranchId,_strZj,_request,_strFlowRunId);
		
			}else if(("".equals(_strBranchIds))&&("".equals(_strUserIds))&&(_strRoleIds!=null&&!"".equals(_strRoleIds))){
				//ѡ���˽�ɫ�����ܶ����,��������Ϊ��
				//��ѯ�����ڲ��ţ�_strLanuchBranchId, ���ݷ��������ڲ���ID���������ϲ�ѯ�������д˽�ɫ����
	//			t_SYS_ROLE SROLECODE(��ɫ����)
	//			t_SYS_BRANCH S_CODE(���ű��)
	//			t_RGXX SYGZW(�˺�) SROLECODE����ɫ���룩 SROLEBH����ɫ��ţ� SBRANCHID(��֯���)
				strAuditIds = queryUserIdByRoles(_strRoleIds,_strLanuchBranchId);
			}else if((!"".equals(_strBranchIds))&&("".equals(_strUserIds))&&(_strRoleIds!=null&&!"".equals(_strRoleIds))){
				//ѡ���˻���(���ܶ����  ��ɫ   ��Ϊ��,���ݻ����ͽ�ɫ��ѯ������
				TableEx exTRGXX = null ;
				try {
					exTRGXX = new TableEx("SROLECODE,SYGZW,SBRANCHID","T_RGXX","1=1  and SBRANCHID in("+_strBranchIds+")");//and SROLECODE in("+_strRoleIds+")
					if(exTRGXX!=null&&!"".equals(exTRGXX)){
						int iCount = exTRGXX.getRecordCount();
						for(int i=0;i<iCount;i++){
							Record rd1 = exTRGXX.getRecord(i);
							if(compareArrayRepeat(_strRoleIds,getColString("SROLECODE", rd1))){//������ɫ
								strAuditIds = ("".equals(strAuditIds)?"":(strAuditIds+","))+getColString("SYGZW", rd1);
							}
						}
					}
				} catch (Exception e) {
				    // MantraLog.fileCreateAndWrite(e);
					e.printStackTrace();
				}finally{
					exTRGXX.close();
				}
			}
			return strAuditIds;
		}
		
		private String queryUserByGw(String strGw) {
			TableEx exTRGXX = null;
			StringBuffer sr =new StringBuffer();
			//��ѯ�����ڸ������ŵ�������,��ͨ����ɫɸѡ�õ�map< ��ɫ���룬��ID>
			try {
				exTRGXX = new TableEx("SYGZW","t_RGXX","1=1 and S_GW in("+strGw+") ");//and SROLECODE in("+_strRoles+")
				int iCount = exTRGXX.getRecordCount();
				Record rd = null;
				for(int i=0;i<iCount;i++){
					rd = exTRGXX.getRecord(i);
					sr.append(getColString("SYGZW", rd)).append(",");
				}
			} catch (Exception e) {
			    MantraLog.fileCreateAndWrite(e);
				e.printStackTrace();
			}finally{
				exTRGXX.close();
			}
			return sr.deleteCharAt(sr.length()-1).toString();
		}
		/**
		 * ���������жϿ�ʼ�ڵ�
		 * @param _strAttr
		 * @param _strZj
		 * @param _request
		 * @param _strFlowRunId
		 * @return
		 */
		public boolean querySqRole(String _strAttr,String _strZj,HttpServletRequest _request,String _strFlowRunId,String _strStartRole){
			/**��ӱ��ֶ������ö�Ӧ start*/
			//����,�ֶ���|����'
			boolean b = false;
			String sql ="";
			if(_strAttr.indexOf("CL:")>-1){//����
				_strAttr = _strAttr.replace("CL:", "");
				sql = "select S_JSDM,S_BMID from T_JSYHZ left join T_JSYHZRIGHT on T_JSYHZRIGHT.S_FID=T_JSYHZ.S_ID where S_ATTR='"+_strAttr+"' and T_JSYHZ.S_ID='"+_strZj+"'";
			}else if(_strAttr.indexOf("BL:")>-1){
				_strAttr = _strAttr.replace("BL:", "");
				_strAttr = queryBusinessDataByCon(_strAttr,"",_strFlowRunId);
				sql = "select S_JSDM,S_BMID from T_JSYHZ left join T_JSYHZRIGHT on T_JSYHZRIGHT.S_FID=T_JSYHZ.S_ID where S_ATTR='"+_strAttr+"' and T_JSYHZ.S_ID='"+_strZj+"'";
			}else if(_strAttr.indexOf("AL:")>-1){
				_strAttr = _strAttr.replace("AL:", "");
				String[] strArryAttr = _strAttr.split("-");
				String strFormVal = queryBusinessDataByCon(strArryAttr[1],"",_strFlowRunId);
				sql = "select S_JSDM,S_BMID from T_JSYHZ left join T_JSYHZRIGHT on T_JSYHZRIGHT.S_FID=T_JSYHZ.S_ID where S_ATTR='"+strArryAttr[0]+"' and S_ATTR2='"+strFormVal+"' and T_JSYHZ.S_ID='"+_strZj+"'";
			}
			
			/**��ӱ��ֶ������ö�Ӧ end*/
			StringBuffer _strRoleIds = new StringBuffer();
			DBFactory dbf = new DBFactory();
			TableEx exSq = null;
			try {
				exSq = dbf.query(sql);
				Record rd = null;
				int j = exSq.getRecordCount();
				if(j==0){
					b=false;
				}else{
					for(int i=0;i<j;i++){
						rd = exSq.getRecord(i);
						_strRoleIds.append(getColString("S_JSDM", rd)).append(",");
					}
					if(_strRoleIds.length()==0){
						
					}else{
						_strRoleIds = _strRoleIds.deleteCharAt(_strRoleIds.length()-1);
					}
					if(compareArrayRepeat(_strRoleIds.toString(), _strStartRole)){
						b = true;
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}finally{
				try{
						exSq.close();
						dbf.close();
				}catch(Exception e){
					
				}
				return b;
			}
		}
		
		public String queryUserIdBySqRoles(String _strAttr,String _strLanuchBranchId,String _strZj,HttpServletRequest _request,String _strFlowRunId){
			/**��ӱ��ֶ������ö�Ӧ start*/
			//����,�ֶ���|����
			String sql = "";
			String strBchId = _strLanuchBranchId;
			
			//CL: �� AL: Ϊ��Ȩ ��Ҫ����Ȩ��ѡ����Ա;
			if(_strAttr.indexOf("CL:")>-1){//����
				_strAttr = _strAttr.replace("CL:", "");
				sql = "select S_JSDM,S_BMID from T_JSYHZ left join T_JSYHZRIGHT on T_JSYHZRIGHT.S_FID=T_JSYHZ.S_ID where S_ATTR='"+_strAttr+"' and T_JSYHZ.S_ID='"+_strZj+"'";
			}else if(_strAttr.indexOf("BL:")>-1){
				_strAttr = _strAttr.replace("BL:", "");
				_strAttr = queryBusinessDataByCon(_strAttr,"",_strFlowRunId);
				sql = "select S_JSDM,S_BMID from T_JSYHZ left join T_JSYHZRIGHT on T_JSYHZRIGHT.S_FID=T_JSYHZ.S_ID where S_ATTR='"+_strAttr+"' and T_JSYHZ.S_ID='"+_strZj+"'";
			}else if(_strAttr.indexOf("AL:")>-1){
				_strAttr = _strAttr.replace("AL:", "");
				String[] strArryAttr = _strAttr.split("-");
	//			CBL:XKR-T_DQYZGZP|S_BPDD|S_ID
				String strSttr = strArryAttr[0];
				String strFormVal = queryBusinessDataByCon(strArryAttr[1],"",_strFlowRunId);
				sql = "select S_JSDM,S_BMID from T_JSYHZ left join T_JSYHZRIGHT on T_JSYHZRIGHT.S_FID=T_JSYHZ.S_ID where S_ATTR='"+strSttr+"' and S_ATTR2='"+strFormVal+"' and T_JSYHZ.S_ID='"+_strZj+"'";
			}
			
			
			/**��ӱ��ֶ������ö�Ӧ end*/
			StringBuffer _strRoleIds = new StringBuffer();
			DBFactory dbf = new DBFactory();
			TableEx exSq = null;
			try {
				exSq = dbf.query(sql);
	//			exSq = dbf.query("select S_JSDM,S_BMID from T_JSYHZ left join T_JSYHZRIGHT on T_JSYHZRIGHT.S_FID=T_JSYHZ.S_ID where S_ATTR='"+_strSqRole+"' and S_BMID='"+_strLanuchBranchId+"'");
				Record rd = null;
				for(int i=0,j=exSq.getRecordCount();i<j;i++){
					rd = exSq.getRecord(i);
					_strRoleIds.append(getColString("S_JSDM", rd)).append(",");
					_strLanuchBranchId = getColString("S_BMID", rd);
				}
				if(_strRoleIds.length()==0){
					
				}else{
					_strRoleIds = _strRoleIds.deleteCharAt(_strRoleIds.length()-1);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}finally{
				try{
						exSq.close();
						dbf.close();
				}catch(Exception e){
					
				}
			}
			
			String strRole = _strRoleIds.toString();
			
			Map<String,String> map = new LinkedHashMap<String, String>();
			String strAuditIds= "";//001001001---001,001001,001001001
	//		String strBranchCodes = _strLanuchBranchId;
			String strBranchCodes = getParentBranchCode(_strLanuchBranchId);
			TableEx exTRGXX = null;
			//��ѯ�����ڸ������ŵ�������,��ͨ����ɫɸѡ�õ�map< ��ɫ���룬��ID>
			try {
	//			exTRGXX = new TableEx("SROLECODE,SYGZW,SBRANCHID","t_RGXX","1=1 and SROLECODE!='' and SBRANCHID like '"+strBranchCodes+"%'  order by length(SBRANCHID) desc");//and SROLECODE in("+_strRoles+")
				
				//old Code  2018-07-08 15:44:34
				//sanquanrenyuan
				//exTRGXX = new TableEx("SROLECODE,SYGZW,SBRANCHID","t_RGXX","1=1 and SROLECODE!='' and SBRANCHID like '"+strBranchCodes.substring(0,3)+"%' ");//and SROLECODE in("+_strRoles+")
	            String Sqyy = selDAteRet("S_TSSX","T_JSYHZ","S_ID='"+_strZj+"'");
	            
	           // MantraLog.WriteProgress(MantraLog.LOG_PROGRESS ,"---Sqyy---"+Sqyy);
	           // MantraLog.WriteProgress(MantraLog.LOG_PROGRESS ,"---Sqyy---"+strRole);
	            
	            if("SQYY".equals(Sqyy)){
		            exTRGXX = new TableEx("t_rgxx.SROLECODE SROLECODE,t_rgxx.SYGZW SYGZW,t_rgxx.SBRANCHID SBRANCHID","T_SQRYDJB LEFT JOIN t_rgxx ON T_SQRYDJB.S_RYBM=t_rgxx.sygzw "," 1=1 and SROLECODE!='' and SBRANCHID like '"+strBranchCodes.substring(0,3)+"%' ");//and SROLECODE in("+_strRoles+")
		        }else{
		            exTRGXX = new TableEx("SROLECODE,SYGZW,SBRANCHID","t_RGXX","1=1 and SROLECODE!='' and SBRANCHID like '"+strBranchCodes.substring(0,3)+"%' ");//and SROLECODE in("+_strRoles+")
		        }
		 
				
				
				int iCount = exTRGXX.getRecordCount();
				Record rd = null;
				for(int i=0;i<iCount;i++){
					rd = exTRGXX.getRecord(i);
					if(compareArrayRepeat(strRole.toString(),getColString("SROLECODE",rd))){//��ɫ����
						String strBranchId = rd.getFieldByName("SBRANCHID").value.toString();
						String strUserId =rd.getFieldByName("SYGZW").value.toString();
	
						map.put(strBranchId, map.get(strBranchId)==null?strUserId:map.get(strBranchId)+","+strUserId);
					}
				}
			} catch (Exception e) {
			    MantraLog.fileCreateAndWrite(e);
				e.printStackTrace();
			}finally{
				exTRGXX.close();
			}
			//��ȡ����Ĳ���

			StringBuffer sr = new StringBuffer();
			 for (Map.Entry<String, String> entry : map.entrySet()) {
				 String strAuditIds1 = entry.getValue();
				 sr.append(strAuditIds1).append(",");
				if(strAuditIds!=null&&!"".equals(strAuditIds)){
	//				break;
				}
			}
			
	
			
				
	//		 return strAuditIds;
			 if(sr.length()==0){
				 strAuditIds = "";
			 }else{
				 strAuditIds =sr.deleteCharAt(sr.length()-1).toString();
			 }
			 
			 
			return strAuditIds;
		}
		/**
		 * ��ѯ�û���֯����ɫ
		 * @param _strRoleIds
		 * @param _strLanuchBranchId
		 * @return
		 */
		public String queryUserIdByRoles(String _strRoleIds,String _strLanuchBranchId){
			String strBchId = _strLanuchBranchId;
			Map<String,String> map = new LinkedHashMap<String, String>();
			String strAuditIds= "";//001001001---001,001001,001001001
			String strBranchCodes = getParentBranchCode(_strLanuchBranchId);
			TableEx exTRGXX = null;
			//��ѯ�����ڸ������ŵ�������,��ͨ����ɫɸѡ�õ�map< ��ɫ���룬��ID>
			try {
				/**���̶���,�¼����Ų�������*/
				//exTRGXX = new TableEx("SROLECODE,SYGZW,SBRANCHID","t_RGXX","1=1 and SROLECODE!='' and SBRANCHID in("+strBranchCodes+")  order by length(SBRANCHID) desc");//and SROLECODE in("+_strRoles+")
				/**���̶��屾��֯ȫ����������*/
				exTRGXX = new TableEx("SROLECODE,SYGZW,SBRANCHID","t_RGXX","1=1 and SROLECODE!='' and SBRANCHID like '"+strBranchCodes.substring(0,3)+"%'  order by length(SBRANCHID) desc");//and SROLECODE in("+_strRoles+")
	//			exTRGXX = new TableEx("SROLECODE,SYGZW,SBRANCHID","t_RGXX","1=1 and SROLECODE!='' and ( SBRANCHID like '"+_strLanuchBranchId+"%' or SBRANCHID in("+strBranchCodes+"))  order by length(SBRANCHID) desc");//and SROLECODE in("+_strRoles+")
				int iCount = exTRGXX.getRecordCount();
				Record rd = null;
				for(int i=0;i<iCount;i++){
					rd = exTRGXX.getRecord(i);
					if(compareArrayRepeat(_strRoleIds,getColString("SROLECODE",rd))){//��ɫ����
						String strBranchId = rd.getFieldByName("SBRANCHID").value.toString();
						String strUserId =rd.getFieldByName("SYGZW").value.toString();
						map.put(strBranchId, map.get(strBranchId)==null?strUserId:map.get(strBranchId)+","+strUserId);
					}
				}
			} catch (Exception e) {
			 //   MantraLog.fileCreateAndWrite(e);
				e.printStackTrace();
			}finally{
				exTRGXX.close();
			}
			//��ȡ����Ĳ���
			StringBuffer sr = new StringBuffer();
			 for (Map.Entry<String, String> entry : map.entrySet()) {
				 String strAuditIds1 = entry.getValue();
				 sr.append(strAuditIds1).append(",");
				if(strAuditIds!=null&&!"".equals(strAuditIds)){
	//				break;
				}
			}
			 if(sr.length()==0){
				 strAuditIds = "";
			 }else{
				 strAuditIds =sr.deleteCharAt(sr.length()-1).toString();
			 }
			return strAuditIds;
		}
		
		/**
		 * ��ȡ��ǰ��ŵ������ϼ��ڵ�
		 * @param _strBranchCodes
		 * @return
		 */
		public String getParentBranchCode(String _strBranchCodes){
			String str = _strBranchCodes;
			while(_strBranchCodes.length()>3){
				_strBranchCodes = _strBranchCodes.substring(0,_strBranchCodes.length()-3);
				str = str+","+_strBranchCodes;
			}
			return str;
		}
		
		/**
		 * ��ѯ���нڵ�������
		 * @param _strFlowId
		 * @param _strVersion
		 * @param _strFlowRunId
		 * @param _strNodeId
		 * @param _strStartUser
		 * @param _strStartRole
		 * @param _strStartBranch
		 * @return
		 */
	//	public Map<String,String> queryAllNodesAudit(String _strFlowId,String _strVersion,String _strFlowRunId,String _strNodeId,String _strLaunchhUser,String _strLaunchRole,String _strLaunchBranch){
	//		Map<String,String> mapNodes = new HashMap<String, String>();//�ڵ�ID��������
	//		TableEx tableEx =null;
	//		try {
	//			tableEx =queryFlowNodeInfo(_strFlowId,_strVersion,"");
	//			int iCount = tableEx.getRecordCount();
	//			//�жϿ�ʼ�ڵ�����
	//			for(int i=0;i<iCount;i++){
	//				Record rd = tableEx.getRecord(i);//1 ���� 3 ��ʼ   2����  4����
	//				String strNodeId = rd.getFieldByName("I_NODE_ID").value.toString();
	//				if("1".equals(rd.getFieldByName("I_TYPE").value.toString())){//����
	//					String strNodeAudit = queryAuditPerson(_strLaunchhUser,_strLaunchBranch,rd.getFieldByName("S_AUDIT_BRANCH").value.toString(),rd.getFieldByName("S_AUDIT_ROLE").value.toString(),rd.getFieldByName("S_AUDIT_USER").value.toString(),getColString("S_AUDIT_SQRYATTR", rd),getColString("S_AUDIT_SQRY", rd),request);
	//					mapNodes.put(strNodeId, strNodeAudit);
	//				}
	//			}
	//			
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		}finally{
	//			tableEx.close();
	//		}
	//		return mapNodes;
	//	}
	
	
		/**
		 * ����-����
		 * @param _strUsers
		 * @param _strOther
		 * @param index
		 * @param _strLaunchUser
		 * @param _strArrayMsg
		 * @param _strFlowId
		 * @param _strVersion
		 * @param _strFlowRunId
		 * @param _strNodeId
		 * @return
		 */
		public int getNodesInfoRun(String _strUsers,String _strOther,int index,String _strLaunchUser,String[] _strArrayMsg,String _strFlowId,String _strVersion,String _strFlowRunId,String _strNodeId){
			String[] strArrayUsers = _strUsers.split("\\|",-1);
			//������Ϊ��&&����Ϊ1����++
			while("T".equals(strArrayUsers[index])){
				/**������Ϣ*/
				index++;
			}
			return index;
		}
		
		/**
		 * ��ȡ��һ�ڵ�
		 * @param request
		 * @param _strNowNode
		 * @param _map
		 * @return
		 */
		public Record getNextNodeByCondition(HttpServletRequest request,String _strNowNode,TableEx _tabEx,String _strType,String _strRunId){
			Record record =null;
			try {
				// ��ȡ��ǰ��¼
				Record objNowRd = getRecordByNodeId(_strNowNode,_tabEx);
				//�жϽڵ�����
				String strNodeType = getColString("I_TYPE", objNowRd);
				if(("3").equals(strNodeType)){//��ʼ-�ҵ��ӽڵ�-�Ե���
					record = getNextNodeByCondition(request,getColString("S_CHILD_ID", objNowRd),_tabEx,_strType,_strRunId);
				}else if("1".equals(strNodeType)){//����-��ֵ-����record
					record = objNowRd;
				}else if("5".equals(strNodeType)){//������
					record = objNowRd;
	//				getColString("S_FLOW_TYPE", objNowRd);//
				}else if("2".equals(strNodeType)){//����-�������������жϻ�ȡ��һ�ڵ�-�Ե���
					String strCustomNodeIds = getColString("S_AUDIT_SEL", objNowRd);//�ֶ�ѡ��ڵ�
					if("".equals(strCustomNodeIds)){
						//����ҳ��,��ѯ��һ�ڵ��Ƿ�����&�����ڵ㲻Ϊ��	
						String strNextNodeId = appendConditionSql(request,getColString("S_CONDITION", objNowRd),_strType,_strRunId);
						//Ĭ���ֶ�
						strNextNodeId = ("".equals(strNextNodeId)?getColString("S_AUDIT_DEF", objNowRd):strNextNodeId);
						//�ֶ�ѡ��--��ѯ��ǰ�ڵ����һ�ڵ��Ƿ�������&�ֶ�ѡ��
						record = getNextNodeByCondition(request,strNextNodeId,_tabEx,_strType,_strRunId);
					}else{//�ֶ�ѡ��ڵ�
						record = objNowRd;
					}
	
				}else if("4".equals(strNodeType)){//����-��ֵ-����record
					record = objNowRd;
				}
			} catch (Exception e) {
			 //   MantraLog.fileCreateAndWrite(e);
				// String[] strArrayFlowLog22 = {"333",_strNowNode,"",new Date()+"",_strNowNode,"","getNextNodeByCondition",getErrorInfoFromException(e)};
				// insertFlowLog("1", strArrayFlowLog22);
				e.printStackTrace();
			}
			return record;
		}
	
		
		public Record getRecordByNodeId(String _strNodeId, TableEx _tabEx) {
			Record rd = null;
			try {
				int iCount = _tabEx.getRecordCount();
				for(int i=0;i<iCount;i++){
					if(_strNodeId.equals(_tabEx.getRecord(i).getFieldByName("I_NODE_ID").value.toString())){
						rd = _tabEx.getRecord(i);
						break;
					}
				}
			} catch (Exception e) {
			    MantraLog.fileCreateAndWrite(e);
				e.printStackTrace();
			}
			return rd;
		}
	
	
	
		/**
		 * ��ѯ���̽ڵ���Ϣ
		 * @param _strFlowId
		 * @param _strFlowRunId
		 * @param _strNodeId
		 * @return
		 */
		public TableEx queryFlowNodeInfo(String _strFlowId,String _strVersion,String _strNodeId){
			TableEx tableEx = null;
			try {
				String strWhere = " 1=1 ";
				strWhere = strWhere+((_strFlowId==null||"".equals(_strFlowId))?" ":(" and S_FLOW_ID='"+_strFlowId+"' "));
				strWhere = strWhere+((_strVersion==null||"".equals(_strVersion))?" ":(" and S_AUDIT_VERSION='"+_strVersion+"' "));
				strWhere = strWhere+((_strNodeId==null||"".equals(_strNodeId))?" ":(" and I_NODE_ID='"+_strNodeId+"' "));
				tableEx = new TableEx("*", "t_sys_flow_node", strWhere);
			} catch (Exception e) {
				String[] strArrayFlowLog22 = {"333","333","queryFlowNodeInfo","queryFlowNodeInfo","����:"+_strFlowId,"�ڵ�:"+_strNodeId,"�汾:"+_strVersion,getErrorInfoFromException(e)};
				insertFlowLog("1", strArrayFlowLog22);
				e.printStackTrace();
			}
			return tableEx;
		}
		
		/**
		 * ��ѯ���б�T_SYS_FLOW_RUN
		 * @param _strFlowId
		 * @param _strVersion
		 * @param _strFlowRunId
		 * @return
		 */
		public TableEx queryFlowRun(String _strFlowId,String _strVersion,String _strFlowRunId){
			TableEx ex = null;
			try {
				StringBuffer sr = new StringBuffer();
				sr.append(" 1=1");
				sr.append((_strFlowId==null||"".equals(_strFlowId))?"":(" and S_FLOW_ID='"+_strFlowId+"'"));
				sr.append((_strVersion==null||"".equals(_strVersion))?"":(" and S_AUDIT_VERSION ='"+_strVersion+"'"));
				sr.append((_strFlowRunId==null||"".equals(_strFlowRunId))?"":(" and S_RUN_ID ='"+_strFlowRunId+"'"));
				ex = new TableEx("*", "T_SYS_FLOW_RUN", sr.toString());
			} catch (Exception e) {
			 //   MantraLog.fileCreateAndWrite(e);
			    
				e.printStackTrace();
			}
			return ex;
		}
		/**
		 * true:��������  false:��������
		 * @param _strFlowId
		 * @param _strVersion
		 * @param _strFlowRunId
		 * @param _strParentFlowId
		 * @return
		 */
		public boolean queryFlowRunSon(String _strFlowId,String _strVersion,String _strFlowRunId){
			TableEx ex = null;
			boolean bSonFlow = false;
			try {
				StringBuffer sr = new StringBuffer();
				sr.append((_strVersion==null||"".equals(_strVersion))?"":(" and S_AUDIT_VERSION ='"+_strVersion+"'"));
				sr.append((_strFlowRunId==null||"".equals(_strFlowRunId))?"":(" and S_RUN_ID ='"+_strFlowRunId+"'"));
				sr.append((_strFlowId==null||"".equals(_strFlowId))?"":(" and S_FLOW_PARENT_ID ='"+_strFlowId+"'"));
				ex = new TableEx("*", "T_SYS_FLOW_RUN", sr.toString());
				if(ex.getRecordCount()>0){
					bSonFlow = true;
				}
			} catch (Exception e) {
			 //   MantraLog.fileCreateAndWrite(e);
				e.printStackTrace();
				
			}finally{
				if(ex!=null){ex.close();}
			}
			return bSonFlow;
		}
		
		/**
		 * �������ύ,��δ����,���ز���
		 * @param _strFlowId
		 * @param _strFlowRunId
		 * @param _strVersion
		 * @param _userCode
		 * @return
		 */
		public boolean backFlowRun(String _strFlowId,String _strFlowRunId,String _strVersion,String _userCode){
			TableEx exRun = null;
			boolean bFlag = false;
			try {
				exRun = queryFlowRun(_strFlowId, _strVersion, _strFlowRunId);
				int index =Integer.parseInt(exRun.getRecord(0).getFieldByName("S_AUDIT_INDEX").value.toString());
				if(index==0)return bFlag;
				String strNodes=exRun.getRecord(0).getFieldByName("S_AUDIT_NODES").value.toString().split("\\|",-1)[index-1];
				String strNexAuditUser = queryFlowLogBeforeNodeAuditUser(_strFlowId,_strVersion, _strFlowRunId, strNodes);
				//2018-07-09 13:20:18  addcode
				String strNexUserArr = exRun.getRecord(0).getFieldByName("S_AUDIT_ARRAY").value.toString().split("\\|",-1)[index-1];
				if(strNexAuditUser.equals(_userCode)){
				    //String[] strArrayFlowRunVal = {_strFlowId,_strFlowRunId,strNexAuditUser,strNodes,(index-1)+""};
				    //2018-07-09 13:20:18  addcode
					String[] strArrayFlowRunVal = {_strFlowId,_strFlowRunId,strNexUserArr,strNodes,(index-1)+""};
					updateFlowRun(strArrayFlowRunVal, "5");
					bFlag = true;
				}
			} catch (NumberFormatException e) {
			 //   MantraLog.fileCreateAndWrite(e);	
				e.printStackTrace();
			} catch (Exception e) {
			 //   MantraLog.fileCreateAndWrite(e);
				e.printStackTrace();
			}finally{
				exRun.close();
				return bFlag;
			}
		}
		
		/**
		 * ��������
		 * @param _strFlowId ���̺�
		 * @param _strFlowRunId �ڵ��
		 * @param _strVersion �汾��
		 * @param _strType 1:���� 0:���� 3:����
		 * @return
		 */
		public boolean processFlowHand(String _strFlowId,String _strFlowRunId,String _strVersion,String _strType){
			DBFactory dbf = new DBFactory();
			TableEx exRun = null;
			try {
				exRun = queryFlowRun(_strFlowId,_strVersion,_strFlowRunId);
				String strAuditUsers=exRun.getRecord(0).getFieldByName("S_AUDIT_ARRAY").value.toString();
				int index = Integer.parseInt(exRun.getRecord(0).getFieldByName("S_AUDIT_INDEX").value.toString());//����
				if("1".equals(_strType)){//���� ������Ϊ��,״̬Ϊ2
					dbf.sqlExe("update t_sys_flow_run set I_ISOVER='2',S_AUD_USER='' where s_run_id='"+_strFlowRunId+"' and s_flow_id='"+_strFlowId+"'", true);
				}else if("0".equals(_strType)){//�ظ���,״̬Ϊ0 
					dbf.sqlExe("update t_sys_flow_run set I_ISOVER='0',S_AUD_USER='"+strAuditUsers.split("\\|",-1)[index]+"' where s_run_id='"+_strFlowRunId+"' and s_flow_id='"+_strFlowId+"'", true);
				}else if("3".equals(_strType)){
					dbf.sqlExe("update t_sys_flow_run set I_ISOVER='3',S_AUD_USER='' where s_run_id='"+_strFlowRunId+"' and s_flow_id='"+_strFlowId+"'", true);
				}
			} catch (Exception e) {
			 //   MantraLog.fileCreateAndWrite(e);
				dbf.close();
				if(exRun!=null){exRun.close();}
				e.printStackTrace();
			}finally{
				dbf.close();
				if(exRun!=null){exRun.close();}
				return true;
			}
		}
		
		/**
		 * ����������־T_SYS_FLOW_RUN
		 * @param _strArrayFlowRun
		 * @param _strType 1:���� 2:���� 3:���� 4:���¸�
		 */
		public void updateFlowRun(String[] _strArrayFlowRunVal,String _strType){
			DBFactory dbf = new DBFactory();
			try {
				if("1".equals(_strType)){
					String strTabCol ="(S_FLOW_ID,S_RUN_ID,S_NODE_CODE,S_AUDIT_VERSION,S_LAUNCH_DATE,S_LAUNCH_USER,S_AUD_USER,S_AUDIT_INDEX,S_AUDIT_MSG,S_LAUNCH_BRANCH,S_AUDIT_ARRAYYQ,S_AUDIT_ARRAY,S_AUDIT_NODES,I_ISOVER,S_AUDIT_OTHER,S_AUDIT_SEL,S_AUD_OVER,S_FLOW_SON,S_FLOW_TYPE,S_FLOW_PARENT_ID,S_AUDIT_FSPJ,S_TAB)";
					_strArrayFlowRunVal = arrayAddSingleQuotes(_strArrayFlowRunVal);
					String strTabVal = Arrays.toString(_strArrayFlowRunVal);
					strTabVal = strTabVal.substring(1,strTabVal.length()-1);
					//���·�������:ɾ��----����
					MantraLog.WriteProgress(MantraLog.LOG_PROGRESS,
                    						"updateFlowRun->Del:T_SYS_FLOW_RUN");
					dbf.sqlExe("delete from T_SYS_FLOW_RUN where S_FLOW_ID="+_strArrayFlowRunVal[0] +" and S_RUN_ID="+_strArrayFlowRunVal[1]+" and S_AUDIT_VERSION="+_strArrayFlowRunVal[3]+"",false);
					dbf.sqlExe("insert into T_SYS_FLOW_RUN "+strTabCol+" values("+strTabVal+")", true);
				}else if("2".equals(_strType)){
	
					String[] strArrayTabCols ={"S_FLOW_ID","S_RUN_ID","S_AUDIT_VERSION","S_NODE_CODE","S_AUD_USER","S_AUDIT_INDEX","I_ISOVER","S_AUDIT_OTHER","S_AUDIT_ARRAY"};
					String strTabVal = "";
					for(int i=3,j=strArrayTabCols.length;i<j;i++){
						strTabVal = ("".equals(strTabVal)?"":(strTabVal+","))+strArrayTabCols[i]+"='"+_strArrayFlowRunVal[i]+"' ";
					}
					dbf.sqlExe("update T_SYS_FLOW_RUN set "+strTabVal+"  where S_FLOW_ID='"+_strArrayFlowRunVal[0]+"' and S_RUN_ID='"+_strArrayFlowRunVal[1]+"' and S_AUDIT_VERSION='"+_strArrayFlowRunVal[2]+"'", false);
				
				 	}else if("3".equals(_strType)){
					String strTabVal = "";
					String[] strArrayTabCols ={"S_FLOW_ID","S_RUN_ID","S_AUDIT_VERSION","S_AUDIT_MSG","S_AUDIT_ARRAYYQ","S_AUDIT_ARRAY","S_AUDIT_NODES","S_AUDIT_OTHER","I_ISOVER","S_AUDIT_SEL","S_FLOW_SON","S_AUDIT_FSPJ"};
					for(int i=3,j=strArrayTabCols.length;i<j;i++){
						strTabVal = ("".equals(strTabVal)?"":(strTabVal+","))+strArrayTabCols[i]+"='"+_strArrayFlowRunVal[i]+"' ";
					}
					dbf.sqlExe("update T_SYS_FLOW_RUN set "+strTabVal+"  where S_FLOW_ID='"+_strArrayFlowRunVal[0]+"' and S_RUN_ID='"+_strArrayFlowRunVal[1]+"' and S_AUDIT_VERSION='"+_strArrayFlowRunVal[2]+"'", false);
			        
			       
				}else if("4".equals(_strType)){
	//				{strFlowParentId,strFlowRunId,strNextAuditUser,strNodesParent[indexParent],indexParent+"",strIsOverParent}
					String strTabVal = "";
					String[] strArrayTabCols ={"S_FLOW_ID","S_RUN_ID","S_AUD_USER","S_NODE_CODE","S_AUDIT_INDEX","I_ISOVER","S_AUDIT_ARRAY"};
					for(int i=2,j=strArrayTabCols.length;i<j;i++){
						strTabVal = ("".equals(strTabVal)?"":(strTabVal+","))+strArrayTabCols[i]+"='"+_strArrayFlowRunVal[i]+"' ";
					}
					dbf.sqlExe("update T_SYS_FLOW_RUN set "+strTabVal+"  where S_FLOW_ID='"+_strArrayFlowRunVal[0]+"' and S_RUN_ID='"+_strArrayFlowRunVal[1]+"'", false);
				}else if("5".equals(_strType)){
					String strTabVal = "";
					String[] strArrayTabCols ={"S_FLOW_ID","S_RUN_ID","S_AUD_USER","S_NODE_CODE","S_AUDIT_INDEX"};
					for(int i=2,j=strArrayTabCols.length;i<j;i++){
						strTabVal = ("".equals(strTabVal)?"":(strTabVal+","))+strArrayTabCols[i]+"='"+_strArrayFlowRunVal[i]+"' ";
					}
					dbf.sqlExe("update T_SYS_FLOW_RUN set "+strTabVal+"  where S_FLOW_ID='"+_strArrayFlowRunVal[0]+"' and S_RUN_ID='"+_strArrayFlowRunVal[1]+"'", false);
				}else if("6".equals(_strType)){
					String strTabVal = "";
					String[] strArrayTabCols ={"S_FLOW_ID","S_RUN_ID","S_AUDIT_SEL"};
					for(int i=2,j=strArrayTabCols.length;i<j;i++){
						strTabVal = ("".equals(strTabVal)?"":(strTabVal+","))+strArrayTabCols[i]+"='"+_strArrayFlowRunVal[i]+"' ";
					}
					
					dbf.sqlExe("update T_SYS_FLOW_RUN set "+strTabVal+"  where S_FLOW_ID='"+_strArrayFlowRunVal[0]+"' and S_RUN_ID='"+_strArrayFlowRunVal[1]+"'", false);
				}
			} catch (Exception e) {
			 //   MantraLog.fileCreateAndWrite(e);
				// String[] strArrayFlowLog22 = {"333","333","flowrun","flowrun","flowrun","flowrun",_strType,getErrorInfoFromException(e)};
				// insertFlowLog("1", strArrayFlowLog22);
				
				e.printStackTrace();
			} finally {
				if(dbf!=null){dbf.close();}
			}
		}
		
		/**
		 * ����������־��T_SYS_FLOW_LOG
		 * @param _type 1:���� 2:����
		 * @param _strArrayValues
		 */
		public void insertFlowLog(String _type,String[] _strArrayValues){
			DBFactory dbf = new DBFactory();
			try {
				if("1".equals(_type)){
					String strTabCol ="(S_FLOW_ID,S_RUN_ID,S_NODE_ID,S_AUD_DATE,S_AUDIT_VERSION,S_AUD_USER,S_AUD_STAUS,S_AUD_COMMENT)";
					_strArrayValues = arrayAddSingleQuotes(_strArrayValues);
					String strTabVal = Arrays.toString(_strArrayValues);
					strTabVal = strTabVal.substring(1,strTabVal.length()-1);
					dbf.sqlExe("insert into T_SYS_FLOW_LOG "+strTabCol+" values("+strTabVal+")", true);
				}else{
				}
			} catch (Exception e) {
			 //   MantraLog.fileCreateAndWrite(e);
				e.printStackTrace();
			} finally {
				dbf.close();
			}
		}
		
		/**
		 * ������ѡ��ڵ�-��ѯ��ǰ�ڵ�֮ǰ���нڵ�
		 * @param _strFlowId
		 * @param _strVersion
		 * @param _strFlowRunId
		 * @param _strNodeIds
		 * @return
		 */
		public String queryFlowLogBeforeAll(String _strFlowId,String _strVersion,String _strFlowRunId,String _strNodeId){
			String strReturn = "";
			TableEx ex = null;
			try {
				StringBuffer sb = new StringBuffer();
				sb.append(" 1=1 ").append("  and T_SYS_FLOW_LOG.S_NODE_ID=T_SYS_FLOW_NODE.I_NODE_ID ").append(" and T_SYS_FLOW_LOG.S_AUD_USER=t_rgxx.SYGZW ");
				sb.append(" and T_SYS_FLOW_LOG.S_FLOW_ID=T_SYS_FLOW_NODE.S_FLOW_ID and T_SYS_FLOW_LOG.S_AUDIT_VERSION=T_SYS_FLOW_NODE.S_AUDIT_VERSION");
				sb.append(" and T_SYS_FLOW_LOG.S_FLOW_ID='"+_strFlowId+"'");
				sb.append(" and T_SYS_FLOW_LOG.S_AUDIT_VERSION='"+_strVersion+"'");
				sb.append(" and T_SYS_FLOW_LOG.S_RUN_ID='"+_strFlowRunId+"'");
				sb.append(" and (T_SYS_FLOW_LOG.S_AUD_STAUS='1' or T_SYS_FLOW_LOG.S_AUD_STAUS='3')");
				sb.append(" GROUP BY S_NODE_ID");
	//			sb.append(" order by T_SYS_FLOW_LOG.S_AUD_DATE desc");//�����ڵ�����ظ�,��Ӱ�칦��,�ſ��������˽ڵ���ܶ�ʧ
				ex = new TableEx("T_SYS_FLOW_LOG.S_NODE_ID,t_rgxx.SYGMC,T_SYS_FLOW_NODE.S_NODE_NAME", "T_SYS_FLOW_LOG ,T_SYS_FLOW_NODE ,T_RGXX",sb.toString());
				int iCount = ex.getRecordCount(); 
				int flag = -1;
				for(int i=0;i<iCount;i++){
					strReturn = ("".equals(strReturn)?"":(strReturn+"|"))+getColString("S_NODE_ID",  ex.getRecord(i))+","+getColString("SYGMC",  ex.getRecord(i))+","+getColString("S_NODE_NAME",  ex.getRecord(i));
					if(_strNodeId.equals(getColString("S_NODE_ID",  ex.getRecord(i)))){
						flag = i;
					}
				}
				if(flag > -1){//��
					String[] strArrayReturn = strReturn.split("\\|",-1);
					String strReturnTemp = strArrayReturn[0];
					for(int i=1,j=strArrayReturn.length-1;i<j;i++){
						strReturnTemp =strReturnTemp+"|"+strArrayReturn[i];
					}
					strReturn = strReturnTemp;
				}
			} catch (Exception e) {
			 //   MantraLog.fileCreateAndWrite(e);
				e.printStackTrace();
			}finally{
				ex.close();
			}
			return strReturn+"|";
		}
		
		/**
		 * ��ѯ��һ�ڵ�
		 * @param _strFlowId
		 * @param _strVersion
		 * @param _strFlowRunId
		 * @param _strNodeId
		 * @return
		 */
		public String queryFlowLogBeforeNodeId(String _strFlowId,String _strVersion,String _strFlowRunId,String _strNodeId){
			String strBeforeNodeId = "";
			TableEx exFlowLog = null;
			try {
				exFlowLog = queryFlowLog(_strFlowId,_strVersion,_strFlowRunId,_strNodeId);
				int iCount = exFlowLog.getRecordCount(); 
				int flag = -1;
				Record rd = null;
				for(int i=0;i<iCount;i++){
					rd =  exFlowLog.getRecord(i);
					if(_strNodeId.equals(getColString("S_NODE_ID",  rd))){
						flag = i;
					}
				}
				if(flag == -1){//��
					strBeforeNodeId =exFlowLog.getRecord(0).getFieldByName("S_NODE_ID").value.toString();
				}else{//��
					strBeforeNodeId =exFlowLog.getRecord(flag+1).getFieldByName("S_NODE_ID").value.toString();
				}
			} catch (Exception e) {
			 //   MantraLog.fileCreateAndWrite(e);
				e.printStackTrace();
			}finally{
				exFlowLog.close();
			}
			return strBeforeNodeId;
		}
		public String queryFlowLogBeforeNodeAuditUser(String _strFlowId,String _strVersion,String _strFlowRunId,String _strNodeId){//��ѯ�˳�������
			String strBeforeUser = "";
			TableEx exFlowLog = null;
			try {
				exFlowLog = queryFlowLog(_strFlowId,_strVersion,_strFlowRunId,_strNodeId);
				strBeforeUser = getColString("S_AUD_USER", exFlowLog.getRecord(0));
			} catch (Exception e) {
			 //   MantraLog.fileCreateAndWrite(e);
				e.printStackTrace();
			}finally{
				exFlowLog.close();
			}
			return strBeforeUser;
		}
		
		/**
		 * ������־֮ǰ�ڵ�
		 * @param _strFlowId
		 * @param _strVersion
		 * @param _strFlowRunId
		 * @return
		 */
		public TableEx queryFlowLogBefore(String _strFlowId,String _strVersion,String _strFlowRunId,String _strNodeId){
			TableEx ex = null;
			try {
				//1,��ѯ��־ ״̬Ϊ1:���ͨ�� 3:�ύ �Ƿ�������
				String strWhere = " 1=1 and (S_AUD_STAUS='1' or S_AUD_STAUS='3') ";
				strWhere = strWhere+((_strFlowId==null||"".equals(_strFlowId))?" ":(" and S_FLOW_ID='"+_strFlowId+"' "));
				strWhere = strWhere+((_strFlowRunId==null||"".equals(_strFlowRunId))?" ":(" and S_RUN_ID='"+_strFlowRunId+"' "));
				strWhere = strWhere+((_strVersion==null||"".equals(_strVersion))?" ":(" and S_AUDIT_VERSION='"+_strVersion+"' "));
				strWhere = strWhere+((_strNodeId==null||"".equals(_strNodeId))?" ":(" and I_NODE_ID='"+_strNodeId+"' "));
				strWhere = strWhere+" order by T_SYS_FLOW_LOG.S_AUD_DATE desc";
				ex = new TableEx("*", "T_SYS_FLOW_LOG",strWhere);
			} catch (Exception e) {
			 //   MantraLog.fileCreateAndWrite(e);
				e.printStackTrace();
			}finally{
				ex.close();
			}
			return ex;
		}
		/**
		 * ������־��ѯ
		 * @param _strFlowId
		 * @param _strVersion
		 * @param _strFlowRunId
		 * @return
		 */
		public TableEx queryFlowLog(String _strFlowId,String _strVersion,String _strFlowRunId,String _strNodeId){
			TableEx ex = null;
			try {
				StringBuffer sb = new StringBuffer();
				sb.append("1=1 ");
				sb.append((_strFlowId==null||"".equals(_strFlowId))?"":(" and S_FLOW_ID='"+_strFlowId+"' "));
				sb.append((_strFlowRunId==null||"".equals(_strFlowRunId))?"":(" and S_RUN_ID='"+_strFlowRunId+"' "));
				sb.append((_strVersion==null||"".equals(_strVersion))?"":(" and S_AUDIT_VERSION='"+_strVersion+"' "));
				sb.append(" and (T_SYS_FLOW_LOG.S_AUD_STAUS='1' or T_SYS_FLOW_LOG.S_AUD_STAUS='3')");
	//			sb.append("".equals(_strNodeId)?"":" GROUP BY S_NODE_ID  ");
				sb.append(" order by T_SYS_FLOW_LOG.S_AUD_DATE desc");
				ex = new TableEx("*", "T_SYS_FLOW_LOG",sb.toString());
			} catch (Exception e) {
			 //   MantraLog.fileCreateAndWrite(e);
				e.printStackTrace();
			}
			return ex;
		}
		/**
		 * �жϽڵ�ID�������е�����λ��
		 * @param _strArrayNodeIds
		 * @param _strNodeId
		 * @return
		 */
		public int getChoiceNode(String[] _strArrayNodeIds,String _strNodeId){
			int index = -1;
			String[] strArrayNode = _strNodeId.split("-");
			for(int a=strArrayNode.length-1,b=0;a>=b;a--){
				if("".equals(strArrayNode[a])){
					continue;
				}
				for(int i=0,j=_strArrayNodeIds.length;i<j;i++){
					if(strArrayNode[a].equals(_strArrayNodeIds[i])){
						index = i;
						break;
					}
				}
			}
			return index;
		}
		
		/**
		 * �ַ��������Ƿ��н���
		 * @param _str1
		 * @param _str2
		 * @return
		 */
		public boolean compareArrayRepeat(String _str1,String _str2){
			if(_str1==null||"".equals(_str1)||_str2==null||"".equals(_str2)){
				return false;
			}else{
				Set<String> set1 = new HashSet<String>(Arrays.asList(_str1.split(",")));
				Set<String> set2 = new HashSet<String>(Arrays.asList(_str2.split(",")));
				set1.retainAll(set2);
				return (set1.size()>0);
			}
		}
		
		/**
		 * ��ǰ����+day
		 * 
		 * @param date
		 * @param day
		 * @param format
		 * @return
		 */
		public static Date dateCal(String date, int day) {
	
			Date d = null;
			try {
				d = strSdfYmd.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal.add(Calendar.DATE, day);
			return cal.getTime();
		}
		
		/**
		 * �ַ�����ȡ������ƴ��
		 * @param _str
		 * @param _strReplace
		 * @param index
		 * @return
		 */
		public String getAuditStrArry(String _str,String _strReplace,int index){
			String[] strArray = _str.split("\\|", -1);
			String strResult = strArray[0];
			for(int i=1;i<index;i++){
				strResult = strResult +"|"+strArray[i];
			}
			
			return strResult+"|"+_strReplace;
		}
		
		public String getAuditStrArrySave(String _str,String _strReplace,int index){
			String[] strArray = _str.split("\\|", -1);
			String strResult = strArray[0];
			for(int i=1;i<=index;i++){
				strResult = strResult +"|"+strArray[i];
			}
			
			return strResult+"|"+_strReplace;
		}
		public String getColString(String _strCol,Record rd){
			String strReturn = "";
			try {
				FieldEx ex = rd.getFieldByName(_strCol);
				Object obj= ((ex==null||"".equals(ex))?"":(ex.value));
				strReturn = (obj==null||"".equals(obj))?"":obj.toString();
			} catch (Exception e) {
				strReturn = "";
				// MantraLog.fileCreateAndWrite(e);
				// String[] strArrayFlowLog22 = {"333","","",new Date()+"",_strCol,_strCol,"getColString",getErrorInfoFromException(e)};
				// insertFlowLog("1", strArrayFlowLog22);
				e.printStackTrace();
			}finally{
				return strReturn ;
			}
		}
		
		/**
		 * ����Ԫ����ӵ�����---ƴ��sql���
		 * @param _array
		 * @return
		 */
		public String[] arrayAddSingleQuotes(String[] _array){
			for(int i=0,j=_array.length;i<j;i++){
				_array[i]="'"+_array[i]+"'";
			}
			return _array;
		}
		
		public Object convertToCode(ScriptEngine engine, String _str) {
			Object result = null;
			try {
				result = engine.eval(_str);
			} catch (ScriptException e) {
				e.printStackTrace();
			}
			System.out.println("�������:" + result.getClass().getName() + ",������:"+ result);
			return result;
		}  
		 
		public void shanchutable(String _strTable){
			DBFactory dbf = new DBFactory();
			try {
					dbf.sqlExe("delete from T_SYS_FLOW_RUN where S_FLOW_ID='"+_strTable+"'",true);
			} catch (Exception e) {
			 //   MantraLog.fileCreateAndWrite(e);
				e.printStackTrace();
			} finally {
				dbf.close();
			}
		}
		public static void reflectMothedInvoke(String strClassName,String strMethodName,Object... obj){
	        try {
				Class<?> class1 = null;
				class1 = Class.forName(strClassName);
				Method[] methods = class1.getDeclaredMethods();  
				Class<?>[] class2 =null;
				for (int i = 0; i < methods.length; i++) {  
					if(strMethodName.equals(methods[i].getName())){
						int l = methods[i].getParameterTypes().length;
						class2 = new Class<?>[l] ;
						for(int j=0;j<l;j++){
							class2[j] = methods[i].getParameterTypes()[j];
						}
					}
				} 
				Method method = class1.getMethod(strMethodName,class2); 
				method.invoke(class1.newInstance(),obj);  
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			}  
		}
		public void getTest(HttpServletRequest req){
			System.out.println("*****************");
			String[] strArrayFlowLog22 = {"333","xx","xx",new Date()+"","xx","","start","xx"};
			insertFlowLog("1", strArrayFlowLog22);
		}
		public void flowOverDelMsg( String S_RUN_ID, String S_FLOW_ID, String S_AUTO_VER) {
    		DBFactory dbf = new DBFactory();
    		try {
    			dbf.sqlExe("delete from T_MSG_RECORDS where S_LCID='"+S_FLOW_ID+"' AND S_YXID='"+S_RUN_ID+"' ;", false);
    		}catch (Exception e) {
    // 			MantraLog.fileCreateAndWrite(e);
    		}finally {
    			if(dbf!=null) {
    				dbf.close();
    			}
    		}
    	}
    	
		public void DelMsg( String S_RUN_ID) {
    		DBFactory dbf = new DBFactory();
    		try {
    			dbf.sqlExe("delete from T_MSG_RECORDS where   S_YXID='"+S_RUN_ID+"' ;", false);
    		}catch (Exception e) {
    // 			MantraLog.fileCreateAndWrite(e);
    		}finally {
    			if(dbf!=null) {
    				dbf.close();
    			}
    		}
    	}
        public String selDAteRet(String col,String tableName,String cond) {
    		String retStr="";
    		TableEx tb = null;
    		Record record=null;
    		try {
    			tb = new TableEx(col,tableName,cond);
    			if(tb.getRecordCount()>0) {
    				record = tb.getRecord(0);
    				 if (record.getFieldByName(col).value != null) {
    					 retStr = record.getFieldByName(col).value.toString();
    					}
    			}
    		}catch (Exception e) {
    // 			MantraLog.fileCreateAndWrite(e);
    		}finally {
    			tb.close();
    		}
    		return retStr;
    	}
        
    	
		public static void main(String[] args) {
			HttpServletRequest req = null;
			reflectMothedInvoke("com.bfkc.process.ProcessRunOperation","getTest",req);
		}	
	}
