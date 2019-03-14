package com.bfkc.process;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;

import com.yulongtao.db.DBFactory;
import com.yulongtao.db.Record;
import com.yulongtao.db.TableEx;

public class ProcessRunOperation {
	
	public static SimpleDateFormat strSdfYmd =  new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat strSdfYmdHms =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public StringBuffer sb = new StringBuffer();

	/**
	 * 发起节点
	 * @param request
	 * @return
	 */
	public Boolean processStart(HttpServletRequest request,StringBuffer _sb){
		TableEx tableEx =null;
		Boolean b = true;
		try{
			/**接收数据*/
			String strStartUser = request.getSession().getAttribute("SYS_STRCURUSER").toString();//发起�?
			String strStartUserRole = request.getSession().getAttribute("SYS_STRROLECODE").toString();//发起人角�?
			String strStartUserBranch = request.getSession().getAttribute("SYS_STRBRANCHID").toString();//发起人组�?
			String strFlowId = request.getParameter("NO_sys_flow_id");//流程ID
			String strVersion = request.getParameter("NO_sys_flow_Ver");//版本�?
			String strFlowRunId = request.getParameter("NO_sys_S_RUN_ID");//运行
			
				/**0 查询流程节点*/
				String strStartNode = "";//发起节点NODE
				String strStartNodeBak = "";//发起节点NODE
				tableEx = queryFlowNodeInfo(strFlowId,strVersion,"");
				int iCount = tableEx.getRecordCount();
				//_sb.append(" iCount: "+iCount);
				//_sb.append("sql:"+sb.toString());
				if(iCount<1){
					return false;
				}
				/**1 查找�?始节�?*/
				Record record = null;
				for(int i=0;i<iCount;i++){
					record =  tableEx.getRecord(i);//1 动作 3 �?�?   2网关  4结束
					if("3".equals(getColString("I_TYPE", record))){//�?始数�?
						strStartNodeBak = getColString("I_NODE_ID", record);
						//多个�?始节点判断是否发节点
						if(queryFlowStartPerson(strStartUser,strStartUserRole,strStartUserBranch,getColString("S_AUDIT_BRANCH", record),getColString("S_AUDIT_ROLE", record),getColString("S_AUDIT_USER", record))==true){
							strStartNode = strStartNodeBak;	
						}
					}
				}
				strStartNode= ("".equals(strStartNode)?strStartNodeBak:strStartNode);//找不到开始节点则任意�?�?
				/**2 �?始节点赋�?*/
				String strAuditArrayyq=",";//逾期
				String strAuditNodes=strStartNode;//�?有节�?
				String strAuditState="3";//运行状�?? 3：提�?
				String strNodeIdNext="";//运行节点
				String strNextAuditUser="";//节点审批�?
				String strAuditOther =",,,";//其他
				String strNownewDate = strSdfYmdHms.format(new Date());//发起日期
				int strNextAuditUserIndex =1;//下一审批节点索引,发起人默认为1
				String strAuditUsers = strStartUser;//�?有审批人
				String strAuditMsgs = "";//�?有消息模�?
//				String strAuditTg = "";//是否跳过
				String strIsOver = "0";//是否结束
				Record rd = null;//获取下一节点对象
				String strEndFlag = "";
				/**3 循环拼接参数*/
				while(!"4".equals(strEndFlag)){
						rd = getNextNodeByCondition(request,strStartNode,tableEx);
						if(rd==null){break;}
						String strNodeAudit = queryAuditPerson(strStartUser,strStartUserBranch,getColString("S_AUDIT_BRANCH", rd),getColString("S_AUDIT_ROLE", rd),getColString("S_AUDIT_USER", rd));
						strAuditArrayyq =(strAuditArrayyq+"|") +getColString("S_AUDIT_YQTS", rd)+","+getColString("S_AUDIT_YQTSCL", rd);//�?有�?�期  �?有�?�期操作
						strAuditMsgs =(strAuditMsgs+"|")+getColString("S_AUDIT_TZXX", rd);//�?有消息模�?
						strAuditNodes = (strAuditNodes+"|")+getColString("I_NODE_ID", rd);
						//是否跳过 �?有审批驳�? �?有审批驳回处�? 子流�?
						strAuditOther =(strAuditOther+"|") +getColString("S_AUDIT_TG", rd)+","+getColString("S_AUDIT_THJD", rd)+","+getColString("S_AUDIT_THJDZD", rd)+","+getColString("S_TZLC", rd)+","+getColString("S_AUDIT_PREEMPTION ", rd);
						strStartNode = getColString("S_CHILD_ID", rd);
						if("".equals(strNodeAudit)){
							strNodeAudit = ("1".equals(getColString("S_AUDIT_TG", rd)))?"T":strNodeAudit;//是否跳岗
						}
						strAuditUsers = (strAuditUsers+"|")+strNodeAudit;
						strEndFlag = getColString("I_TYPE", rd);
				}
								_sb.append("sql:"+sb.toString());
					/**4 跳岗 1:�?*/
					strNextAuditUserIndex = getNodesInfoRun(strAuditUsers,strAuditOther,strNextAuditUserIndex,strStartUser,strAuditMsgs.split("\\|",-1),strFlowId,strVersion,strFlowRunId,strAuditNodes.split("\\|",-1)[strNextAuditUserIndex]);
					strNextAuditUser =strAuditUsers.split("\\|",-1)[strNextAuditUserIndex];
					strNodeIdNext =strAuditNodes.split("\\|",-1)[strNextAuditUserIndex];
					/**5 插入运行�?*/
					String[] strArrayFlowRun = {strFlowId,strFlowRunId,strNodeIdNext,strVersion,strNownewDate,strStartUser,strNextAuditUser,strNextAuditUserIndex+"",strAuditMsgs,strStartUserBranch,strAuditArrayyq,strAuditUsers,strAuditNodes,strIsOver,strAuditOther};
					updateFlowRun(strArrayFlowRun,"1");
				
					String strDate = strSdfYmdHms.format(new Date());
					String  strAuditComment = "";
					/**插入流程日志*/
					String[] strArrayFlowLog = {strFlowId,strFlowRunId,strAuditNodes.split("\\|",-1)[0],strDate,strVersion,strStartUser,strAuditState,strAuditComment};
					insertFlowLog("1", strArrayFlowLog);
					/**发�?�消�?*/
					sendMsg(strAuditMsgs.split("\\|",-1)[strNextAuditUserIndex],strNextAuditUser,strAuditState,strIsOver,strFlowId,strVersion,strFlowRunId,strNodeIdNext,request);
		}catch (Exception e) {
			b=false;
			_sb.append(e);
			e.printStackTrace();
		}finally{
			tableEx.close();
		}
		return b;
	}
	
	/**
	 * 节点运行
	 * @param request
	 * @return
	 */
	@SuppressWarnings("finally")
	public Boolean processRun(HttpServletRequest request,StringBuffer _sb){
		TableEx exRun =null;
		Boolean b=true;
		try{
			/**接收数据*/
			String strAuditUser = request.getSession().getAttribute("SYS_STRCURUSER").toString();//审批�?
			String strAuditState = request.getParameter("NO_sys_flow_state");//审核状�?? 审核状�??:0驳回1通过2作废3提交4逾期5逾期作废6逾期�?�?
			String strAuditComment = request.getParameter("strAuditComment");//备注
			String strAuditChoiceNode = request.getParameter("NO_sys_flow_choicenode");//审批人指定节�?
			//doFlowRun_sflowid_srunid_sversion_state_statecomment_choicenode
			String strFlowId = request.getParameter("NO_sys_flow_id");
            String strFlowRunId = request.getParameter("NO_sys_S_RUN_ID");
            String strVersion = request.getParameter("NO_sys_flow_Ver");
            
            
            String strAuditUserId = request.getParameter("auditUserId");//多个审核�?,指定审核�?

            
			
			//String strFlowId = request.getParameter("NO_sys_flow_id");//流程ID
			//String strVersion = request.getParameter("NO_sys_flow_Ver");//版本�?
			//String strFlowRunId = request.getParameter("NO_sys_S_RUN_ID");//运行
			
			String strIsOver = "0";
			/**查询流程运行信息*/
			exRun = queryFlowRun(strFlowId,strVersion,strFlowRunId);
			//_sb.append(strFlowId+"    "+strVersion+"    "+strFlowRunId);
			String strMsgs=exRun.getRecord(0).getFieldByName("S_AUDIT_MSG").value.toString();
			String strYqs=exRun.getRecord(0).getFieldByName("S_AUDIT_ARRAYYQ").value.toString();
			String strAuditUsers=exRun.getRecord(0).getFieldByName("S_AUDIT_ARRAY").value.toString();
			String strNodes=exRun.getRecord(0).getFieldByName("S_AUDIT_NODES").value.toString();
			String strLaunchUser = exRun.getRecord(0).getFieldByName("S_LAUNCH_USER").value.toString();
//			String strLaunchBranch = exRun.getRecord(0).getFieldByName("S_LAUNCH_BRANCH").value.toString();
			String strLaunchDate = exRun.getRecord(0).getFieldByName("S_LAUNCH_DATE").value.toString();
			String strNodeIdNow = exRun.getRecord(0).getFieldByName("S_NODE_CODE").value.toString();//当前节点
			String strOther = exRun.getRecord(0).getFieldByName("S_AUDIT_OTHER").value.toString();//其他
			int index = Integer.parseInt(exRun.getRecord(0).getFieldByName("S_AUDIT_INDEX").value.toString());//索引
			String strIsOverRun = exRun.getRecord(0).getFieldByName("I_ISOVER").value.toString();//是否完成
			
			if("1".equals(strIsOverRun)){//判断完成返回
				return b;
			}
		
			String[] strArrayAuditUsers =strAuditUsers.split("\\|",-1);//审批人数�?
			String[] strArrayNodes = strNodes.split("\\|",-1);//节点人数�?
			String[] strArrayMsgs = strMsgs.split("\\|",-1);//消息数组
			
			/**判断当前登录人是否包含运行节点审批人*/
			boolean flag = false;
			String[] strArrayAuditUsersNow = strArrayAuditUsers[index].split(",");
			for(int i=0,j=strArrayAuditUsersNow.length;i<j;i++){
				if(strAuditUser.equals(strArrayAuditUsersNow[i])){
					flag = true;
				}
			}
			if(flag==false){
				return b; 
			}
			
			
			
			String strNodeIdNext = "";//下一节点
			String strNextAuditUser = "";//下一审批�?
			int iNextAuditUserIndex = index;//下一审批人索�?
			String strMsgId = "";//节点消息ＩＤ
			String strAudMod="";//审批 指定/  抢占模式
			String[] strOtherArrayNow = strOther.split("\\|",-1)[index].split(",",-1);
			/**是否逾期*/
			if(!isYuQi(strLaunchDate, strYqs,index)){
				String strYqOpt = strYqs.split("\\|",-1)[index].split(",",-1)[1];
				switch (strYqOpt) {
					case "ZF"://作废
						strAuditState = "5";
						strIsOver ="1";//流程结束
						break;
					case "TGJD"://自动跳过
						index = index +1;
						/**跳岗*/
						iNextAuditUserIndex = getNodesInfoRun(strAuditUsers,strOther,index,strLaunchUser,strArrayMsgs,strFlowId,strVersion,strFlowRunId,strArrayNodes[index]);					
						break;
					case "ZDTH"://自动�?回初始节�?
						index = 0;
						strAuditState = "6";
						strIsOver="1";
						iNextAuditUserIndex = 0;				
						break;
				}
			}else{
				/**判断是否审核通过*/
				switch (strAuditState) {
					case "1"://审核通过
						strAudMod = strOtherArrayNow[4];//抢占      //指定
						index = index+1;
						/**跳岗*/
							_sb.append("   index   ").append(index).append("      ");
						iNextAuditUserIndex = getNodesInfoRun(strAuditUsers,strOther,index,strLaunchUser,strArrayMsgs,strFlowId,strVersion,strFlowRunId,strArrayNodes[index]);
						switch (strAudMod) {
						case "QZ"://抢占
							break;
						case "ZD"://指定
							break;
						case "HQ":
							break;
						}
								
								
								
								
								break;
					case "2"://会签
						
						break;
					case "0"://驳回
								/**查询当前节点信息*/
								String strAuditReject = strOtherArrayNow[1];//驳回
								switch (strAuditReject) {
									case "1"://上一节点
										String strBeforeNodeId = queryFlowLogBeforeNodeId(strFlowId,strVersion, strFlowRunId, strNodeIdNow);
										index = getChoiceNode(strArrayNodes,strBeforeNodeId);
										iNextAuditUserIndex = index;
										break;
									case "2"://初始节点
										index = 0;
										iNextAuditUserIndex=0;
										break;
									case "3"://指定节点
										String strAuditRejectChoiceNode = strOtherArrayNow[2];//驳回节点
										index = getChoiceNode(strArrayNodes,strAuditRejectChoiceNode);
										/**跳岗*///TODO
										iNextAuditUserIndex = getNodesInfoRun(strAuditUsers,strOther,index,strLaunchUser,strArrayMsgs,strFlowId,strVersion,strFlowRunId,strArrayNodes[index]);
										break;
									case "4"://审批人指�?
										index = getChoiceNode(strArrayNodes,strAuditChoiceNode);
										iNextAuditUserIndex = index;
										break;
									case "5"://作废
										strAuditState = "2";
										strIsOver = "1";						
										break;
								}
						break;
				}
			}
			strNodeIdNext = strArrayNodes[iNextAuditUserIndex];
			strNextAuditUser = strArrayAuditUsers[iNextAuditUserIndex];
			strMsgId = strArrayMsgs[iNextAuditUserIndex];
			
			/**流程是否结束*/
			strIsOver = ((iNextAuditUserIndex+1) == strArrayNodes.length)?"1":strIsOver;
			/**更新流程运行信息*/
			String[] strArrayFlowRun = {strFlowId,strFlowRunId,strVersion,strNodeIdNext,strNextAuditUser,iNextAuditUserIndex+"",strIsOver};
			updateFlowRun(strArrayFlowRun,"2");
			/**插入流程日志*/
			String strNowDate = strSdfYmdHms.format(new Date());
			String[] strArrayFlowLog = {strFlowId,strFlowRunId,strNodeIdNow,strNowDate,strVersion,strAuditUser,strAuditState,strAuditComment};
			insertFlowLog("1", strArrayFlowLog);
			sendMsg(strMsgId,strNextAuditUser,strAuditState,strIsOver,strFlowId,strVersion,strFlowRunId,strNodeIdNext,request);
	
		}catch (Exception e) {
			b = false;
			_sb.append(e);
			e.printStackTrace();
		}finally{
			exRun.close();
			return b;
		}

	}

	/**
	 * 表单修改重新初始化流�?
	 * @param request
	 * @return
	 */
	@SuppressWarnings("finally")
	public Boolean processSave(HttpServletRequest request){
		Boolean b = true;
		TableEx exRun =null;
		TableEx tableEx =null;
		try{
			/**1接收数据*/
			String strFlowId = request.getParameter("NO_sys_flow_id");//流程ID
			String strVersion = request.getParameter("NO_sys_flow_Ver");//版本�?
			String strFlowRunId = request.getParameter("NO_sys_S_RUN_ID");//运行
			
				/**2查询运行�?*/
				exRun = queryFlowRun(strFlowId,strVersion,strFlowRunId);
				String strIsOverRun = exRun.getRecord(0).getFieldByName("I_ISOVER").value.toString();//是否完成
				if("1".equals(strIsOverRun)){//判断完成返回
					return b;
				}
				String strMsgs=exRun.getRecord(0).getFieldByName("S_AUDIT_MSG").value.toString();
				String strYqs=exRun.getRecord(0).getFieldByName("S_AUDIT_ARRAYYQ").value.toString();
				String strAuditUsersRun=exRun.getRecord(0).getFieldByName("S_AUDIT_ARRAY").value.toString();
				String strNodes=exRun.getRecord(0).getFieldByName("S_AUDIT_NODES").value.toString();
				String strLaunchUser = exRun.getRecord(0).getFieldByName("S_LAUNCH_USER").value.toString();
				String strLaunchBranch = exRun.getRecord(0).getFieldByName("S_LAUNCH_BRANCH").value.toString();
				String strStartNode = exRun.getRecord(0).getFieldByName("S_NODE_CODE").value.toString();//当前节点
				String strOther = exRun.getRecord(0).getFieldByName("S_AUDIT_OTHER").value.toString();//其他
				int index = Integer.parseInt(exRun.getRecord(0).getFieldByName("S_AUDIT_INDEX").value.toString());//索引
				/**3 查询流程节点*/
				tableEx = queryFlowNodeInfo(strFlowId,strVersion,"");
				
				/**4 循环拼接参数*/
				String strAuditArrayyq="";//逾期
				String strAuditNodes="";//�?有节�?
				String strAuditOther ="";//其他
				int strNextAuditUserIndex = index+1;//下一审批节点索引,发起人默认为1
				String strAuditUsers = "";//�?有审批人
				String strAuditMsgs = "";//�?有消息模�?
				String strIsOver = "0";//是否结束
				Record rd = null;//获取下一节点对象
				String strEndFlag = "";
				int icount = 0;
				while(!"4".equals(strEndFlag)){
						rd = getNextNodeByCondition(request,strStartNode,tableEx);
						if(rd==null){break;}
						String strNodeAudit = queryAuditPerson(strLaunchUser,strLaunchBranch,getColString("S_AUDIT_BRANCH", rd),getColString("S_AUDIT_ROLE", rd),getColString("S_AUDIT_USER", rd));
						strAuditArrayyq =(icount==0?"":(strAuditArrayyq+"|")) +getColString("S_AUDIT_YQTS", rd)+","+getColString("S_AUDIT_YQTSCL", rd);//�?有�?�期  �?有�?�期操作
						strAuditMsgs =(icount==0?"":(strAuditMsgs+"|"))+getColString("S_AUDIT_TZXX", rd);//�?有消息模�?
						strAuditNodes = (icount==0?"":(strAuditNodes+"|"))+getColString("I_NODE_ID", rd);
						strAuditOther =(icount==0?"":(strAuditOther+"|")) +getColString("S_AUDIT_TG", rd)+","+getColString("S_AUDIT_THJD", rd)+","+getColString("S_AUDIT_THJDZD", rd)+","+getColString("S_TZLC", rd)+","+getColString("S_AUDIT_PREEMPTION ", rd);
						strStartNode = getColString("S_CHILD_ID", rd);
						//是否跳过 �?有审批驳�? �?有审批驳回处�? 子流�?
						if("".equals(strNodeAudit)){
							strNodeAudit = ("1".equals(getColString("S_AUDIT_TG", rd)))?"T":strNodeAudit;//跳岗
						}
						strAuditUsers = (icount==0?"":(strAuditUsers+"|"))+strNodeAudit;
						strEndFlag = getColString("I_TYPE", rd);
						icount=1;
				}
				/**5组装参数,中间保存从当前节点修改审批信�?*/
				strAuditUsersRun = getAuditStrArry(strAuditUsersRun,strAuditUsers,index);
				strYqs = getAuditStrArry(strYqs,strAuditArrayyq,index);
				strMsgs = getAuditStrArry(strMsgs,strAuditMsgs,index);
				strNodes = getAuditStrArry(strNodes,strAuditNodes,index);
				strOther= getAuditStrArry(strOther,strAuditOther,index);
				    
					/**6 跳岗 1:�?*/
//					strNextAuditUserIndex = getNodesInfo(strAuditUsersRun,strOther,strNextAuditUserIndex);
//					strNextAuditUser =strAuditUsersRun.split("\\|",-1)[strNextAuditUserIndex];
//					strNodeIdNext =strNodes.split("\\|")[strNextAuditUserIndex];
					/**流程是否结束*/
					strIsOver = ((strNextAuditUserIndex+1) == strNodes.split("\\|",-1).length)?"1":strIsOver;
					/**7 更新运行�? 4个数�?*/
					String[] strArrayFlowRun = {strFlowId,strFlowRunId,strVersion,strMsgs,strYqs,strAuditUsersRun,strNodes,strOther,strIsOver};
					updateFlowRun(strArrayFlowRun,"3");
		}catch (Exception e) {
			b = false;
			e.printStackTrace();
		}finally{
			exRun.close();
			tableEx.close();
			return b;
		}
	}
	/**
	 * 审批驳回选择审批�?
	 * @param request
	 * @return
	 */
	@SuppressWarnings("finally")
	public String processAuditSelectNode(HttpServletRequest request){
		TableEx exRun =null;
		String strResult = "";
		try{
			/**接收数据*/
			String strFlowId = request.getParameter("sflowid");
            String strFlowRunId = request.getParameter("srunid");
            String strVersion = request.getParameter("sversion");
			
			/**查询流程运行信息*/
			exRun = queryFlowRun(strFlowId==null?"":strFlowId,strVersion==null?"":strVersion,strFlowRunId==null?"":strFlowRunId);
			if(exRun.getRecordCount()>0){
				String strRunNode = exRun.getRecord(0).getFieldByName("S_NODE_CODE").value.toString();
				strResult = queryFlowLogBeforeAll(strFlowId, strVersion, strFlowRunId,strRunNode);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			exRun.close();
			return strResult;
		}
	}
	
	/**
	 * 当前节点指定审批�?-多人
	 * @param request
	 * @return
	 */
	@SuppressWarnings("finally")
	public String processNodeAudit(HttpServletRequest request){
		TableEx exRun =null;
		TableEx exTRGXX =null;
		StringBuffer strResult  = new StringBuffer();
		try{
			/**接收数据*/
			String strFlowId = request.getParameter("sflowid");
            String strFlowRunId = request.getParameter("srunid");
            String strVersion = request.getParameter("sversion");
			
			/**查询流程运行信息*/
			exRun = queryFlowRun(strFlowId==null?"":strFlowId,strVersion==null?"":strVersion,strFlowRunId==null?"":strFlowRunId);
			if(exRun.getRecordCount()>0){
				//S_AUDIT_OTHER S_AUDIT_ARRAYYQ S_AUDIT_INDEX
				String[] strArrayAud = exRun.getRecord(0).getFieldByName("S_AUDIT_ARRAY").value.toString().split("\\|",-1);
				int index = Integer.parseInt(exRun.getRecord(0).getFieldByName("S_AUDIT_INDEX").value.toString());
				
				//iNextAuditUserIndex = getNodesInfoRun(strAuditUsers,strOther,index,strLaunchUser,strArrayMsgs,strFlowId,strVersion,strFlowRunId,strArrayNodes[index]);
				
				index++;
				if(strArrayAud[index].split(",").length>1){//多个审批�?
					String strOther = exRun.getRecord(0).getFieldByName("S_AUDIT_OTHER").value.toString();
					if("ZD".equals(strOther.split("\\|",-1)[index].split(",",-1)[4])){//指定模式
						exTRGXX = new TableEx("SYGZW,SYGMC","T_RGXX","1=1  and SYGZW in("+strArrayAud[index]+")");//and SROLECODE in("+_strRoleIds+")
						Record  rd = null;
						for(int i=0,j=exTRGXX.getRecordCount();i<j;i++){
							rd = exTRGXX.getRecord(i);
							strResult.append(getColString("SYGZW", rd)).append(",").append(getColString("SYGMC", rd)).append("|");
						}
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			exRun.close();
			exTRGXX.close();
			return strResult.toString();
		}
	}	

	/**
	 * 发�?�消�?
	 * @param _strArrayMsgIds
	 * @param _strArrayMsgUsers
	 * @param _strType
	 * @param _strIsOver
	 * @param _strFlowId
	 * @param _strVersion
	 * @param _strFlowRunId
	 * @param _strNodeId
	 */
	public void sendMsg(String _strArrayMsgIds,String _strArrayUserIds,String _strType,String _strIsOver,String _strFlowId,String _strVersion,String _strFlowRunId,String _strNodeId,HttpServletRequest request){
		//发起/结束/作废/�?�?
		//建表:消息记录表t_msg_records:ID(32) 流程ID(32) 运行ID(64) 版本�?(16) 节点ID(8) 发�?�人(32) 接收�?(32) 消息ID(32) 消息类型(32) 消息内容(512)  发�?�时�?(128) 状�??(已读未读)(4) 删除标识(4)
		//insert方法(流程ID 运行ID 版本�? 发�?�人,接收�?,消息ID,内容,时间,未读,未删�?)
		//查询(接收人ID查询,未删�?) 只读未读排序 时间排序
		//更新:已读 �? 删除状�?�修�?
		String strLoginUserName = request.getSession().getAttribute("SYS_STRCURUSERNAME").toString();//登录人名�?
		String strPageCode = request.getParameter("NO_sys_S_spagecode");
//		String strLoginBranchName = request.getSession().getAttribute("SYS_STRBRANCHNAME");//登录人部门名�?
		switch (_strType) {// 审核状�??:0�?1�?2作废3提交4逾期5逾期作废6逾期�?�?7跳岗
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
				_strType="逾期�?�?";
				break;
			case "7":
				_strType="跳岗";
				break;
		}
		TableEx exRun = queryFlowRun(_strFlowId, _strVersion, _strFlowRunId);
		exRun.close();
		String strMsgContent = queryMsgTemplet(_strArrayMsgIds);
//		String[] strArrayUserIds = _strArrayUserIds.split(",");
//		for(int i=0,j=strArrayUserIds.length;i<j;i++){
//			if("".equals(strArrayUserIds[i])){continue;}
			String strNumberId = System.currentTimeMillis()+"";
			strMsgContent = strMsgContent.replace("${username}", strLoginUserName);//${username} ${active}单据,单据运行�?:${numberid} ${branchname}
			strMsgContent = strMsgContent.replace("${active}", _strType);
			strMsgContent = strMsgContent.replace("${numberid}", _strFlowRunId);
//			strMsgContent = strMsgContent.replace("${branchname}", strLoginBranchName);
			String[] strArrayValues={strPageCode,_strVersion,"system",strSdfYmdHms.format(new Date()),strNumberId,_strNodeId,_strArrayUserIds,_strFlowId,"0",_strArrayMsgIds,"system",strMsgContent,_strFlowRunId,"0"};
			updateMsgs("1",strArrayValues);
//		}
	}
	
	public void updateMsgs(String _strType,String[] _strArrayValues){
		DBFactory dbf = new DBFactory();
		try {
			if("1".equals(_strType)){
				String strTabCol ="(S_PAGECODE,S_BBH,S_FSR,S_FSSJ,S_ID,S_JDID,S_JSR,S_LCID,S_SCBS,S_XXID,S_XXLX,S_XXNR,S_YXID,S_ZT)";
				_strArrayValues = arrayAddSingleQuotes(_strArrayValues);
				String strTabVal = Arrays.toString(_strArrayValues);
				strTabVal = strTabVal.substring(1,strTabVal.length()-1);
				dbf.sqlExe("insert into T_MSG_RECORDS "+strTabCol+" values("+strTabVal+")", true);
			}else{
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbf.close();
		}
	}
	
	
	/**
	 * 查询消息模版
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
			e.printStackTrace();
		}finally{
			ex.close();
		}
		return strMsgTem;
	}
	
	private String getRequestParam(HttpServletRequest _request, String _strReplaceStr) {
		Enumeration enu = _request.getParameterNames();
		while (enu.hasMoreElements()) {
			String paraName = (String) enu.nextElement();
			_strReplaceStr = _strReplaceStr.replace(paraName, "'"+_request.getParameter(paraName)+"'");
		}
		return _strReplaceStr;
	}
	
	/**
	 * 流程条件判断
	 * @param _strCon
	 * @return
	 */
	public String appendConditionSql(HttpServletRequest _request,String _strCon){
//		2:  T_table1.qjts_2 <  3  $6:  T_table1.qjts_2 >=  3 
//		节点代码:  条件  分割�?$ ......
		_strCon = _strCon.replace("-*-", "'");
		_strCon = _strCon.replace("^", "%");
		String[] strNodeArray = _strCon.split("\\$");
		String strNodeId = "";
		String strTable = "";
		String sql = "";
		String sqlCon = "";
		for(int i=0,j=strNodeArray.length;i<j;i++){
			if(strNodeArray[i].trim().length()==0){continue;}
			strNodeId = strNodeArray[i].substring(0,strNodeArray[i].indexOf(":")).trim();//节点ＩＤ
			strNodeArray[i] = strNodeArray[i].substring(strNodeArray[i].indexOf(":")+1,strNodeArray[i].length());//去掉'冒号'
			strTable = strNodeArray[i].substring(0,strNodeArray[i].indexOf("."));//得到表名
			strNodeArray[i] = strNodeArray[i].replace(strTable+".", strTable+"$");//.替换�?$
			sql = getRequestParam(_request,strNodeArray[i]);
			
			sqlCon = queryConditionSql(sql);
			if("1".equals(sqlCon)){//1:true
				break;
			}
 			sb.append("sql:"+sql+  "  sqlCon  "+sqlCon);
		}
		return strNodeId;
	}
	private String queryConditionSql(String _sql){
		String strResult = "";
		TableEx ex = null;
		try {
			ex = new TableEx(_sql +" as 'xx'", "T_CONDITION","1=1");
			strResult = ex.getRecord(0).getFieldByName("xx").value.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			ex.close();
		}
		return strResult;
	}
	/**
	 * true:正常 false:逾期
	 * @param strLaunchDate
	 * @param strYqs
	 * @param index
	 * @return
	 */
	public boolean isYuQi(String strLaunchDate,String strYqs,int index){
		boolean b = true;
		String[] strArrayYq = strYqs.split("\\|",-1)[index].split(",",-1);
		if(!"".equals(strArrayYq[0])){//逾期
			if(new Date().getTime()>dateCal(strLaunchDate,Integer.parseInt(strArrayYq[0])).getTime()){
				b = false;
			}
		}
		return b;
	}
	//2017-08-18 15:06:44 ,|1,TGJD|1,TGJD|1,ZDTH|1,TGJD|,TGJD|,  2
	/**
	 * �?始节点判断是否发起人
	 * @param _strStartUserId 前台传入
	 * @param _strStartRole
	 * @param _strStartBranchId
	 * @param _strBranchIds 节点取�??
	 * @param _strRoleIds
	 * @param _strUserIds
	 * @return
	 */
	public boolean queryFlowStartPerson(String _strStartUserId,String _strStartRole,String _strStartBranchId,String _strBranchIds,String _strRoleIds,String _strUserIds){
		if(compareArrayRepeat(_strStartRole,_strRoleIds)){//---发起人角色与节点角色�?�?
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 查找节点审批�?
	 * @param _strLanuchUserId发起�?
	 * @param _strLanuchRole �?
	 * @param _strLanuchBranchId
	 * @param _strBranchIds 节点
	 * @param _strRoleIds
	 * @param _strUserIds
	 * @return
	 */
	public String queryAuditPerson(String _strLanuchUserId,String _strLanuchBranchId,String _strBranchIds,String _strRoleIds,String _strUserIds){
		String strAuditIds = "";
		//选择了人------------直接返回�?
		if(!"".equals(_strUserIds)){
			strAuditIds =_strUserIds;
		}else if(("".equals(_strBranchIds))&&("".equals(_strUserIds))&&_strRoleIds!=null){
			//选择了角色（可能多个�?,机构、人为空
			//查询人所在部门，_strLanuchBranchId, 根据发起人所在部门ID，依次向上查询部门下有此角色的人
//			t_SYS_ROLE SROLECODE(角色代码)
//			t_SYS_BRANCH S_CODE(部门编号)
//			t_RGXX SYGZW(账号) SROLECODE（角色代码） SROLEBH（角色编号） SBRANCHID(组织编号)
			strAuditIds = queryUserIdByRoles(_strRoleIds,_strLanuchBranchId);
		}else if((!"".equals(_strBranchIds))&&("".equals(_strUserIds))&&_strRoleIds!=null){
			//选择了机�?(可能多个�?  角色   人为�?,根据机构和角色查询所在人
			TableEx exTRGXX = null ;
			try {
				exTRGXX = new TableEx("SROLECODE,SYGZW,SBRANCHID","T_RGXX","1=1  and SBRANCHID in("+_strBranchIds+")");//and SROLECODE in("+_strRoleIds+")
				if(exTRGXX!=null&&!"".equals(exTRGXX)){
					int iCount = exTRGXX.getRecordCount();
					for(int i=0;i<iCount;i++){
						Record rd = exTRGXX.getRecord(i);
						if(compareArrayRepeat(_strRoleIds,getColString("SROLECODE", rd))){//包含角色
							strAuditIds = ("".equals(strAuditIds)?"":(strAuditIds+","))+getColString("SYGZW", rd);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				exTRGXX.close();
			}
		}
		return strAuditIds;
	}
	
	/**
	 * 查询用户组织树角�?
	 * @param _strRoleIds
	 * @param _strLanuchBranchId
	 * @return
	 */
	public String queryUserIdByRoles(String _strRoleIds,String _strLanuchBranchId){
		Map<String,String> map = new HashMap<String, String>();
		String strAuditIds= "";
		String strBranchCodes = getParentBranchCode(_strLanuchBranchId);
		TableEx exTRGXX = null;
		//查询人所在父级部门的�?有人,并�?�过角色筛�?�得到map< 角色代码，人ID>
		try {
			exTRGXX = new TableEx("SROLECODE,SYGZW,SBRANCHID","t_RGXX","1=1 and SBRANCHID in("+strBranchCodes+")  order by SBRANCHID");//and SROLECODE in("+_strRoles+")
			int iCount = exTRGXX.getRecordCount();
			Record rd = null;
			for(int i=0;i<iCount;i++){
				rd = exTRGXX.getRecord(i);
				if(compareArrayRepeat(_strRoleIds,getColString("SROLECODE",rd))){//角色包含
					String strBranchId = rd.getFieldByName("SBRANCHID").value.toString();
					String strUserId =rd.getFieldByName("SYGZW").value.toString();
					map.put(strBranchId, map.get(strBranchId)==null?strUserId:map.get(strBranchId)+","+strUserId);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			exTRGXX.close();
		}
		//获取�?近的部门
		 for (Map.Entry<String, String> entry : map.entrySet()) {
			 strAuditIds = entry.getValue();
			if(strAuditIds!=null&&!"".equals(strAuditIds)){
				break;
			}
		}
		return strAuditIds;
	}
	
	/**
	 * 获取当前编号的所有上级节�?
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
	 * 查询�?有节点审批人
	 * @param _strFlowId
	 * @param _strVersion
	 * @param _strFlowRunId
	 * @param _strNodeId
	 * @param _strStartUser
	 * @param _strStartRole
	 * @param _strStartBranch
	 * @return
	 */
	public Map<String,String> queryAllNodesAudit(String _strFlowId,String _strVersion,String _strFlowRunId,String _strNodeId,String _strLaunchhUser,String _strLaunchRole,String _strLaunchBranch){
		Map<String,String> mapNodes = new HashMap<String, String>();//节点ID，审批人
		TableEx tableEx =null;
		try {
			tableEx =queryFlowNodeInfo(_strFlowId,_strVersion,"");
			int iCount = tableEx.getRecordCount();
			//判断�?始节点数�?
			for(int i=0;i<iCount;i++){
				Record rd = tableEx.getRecord(i);//1 动作 3 �?�?   2网关  4结束
				String strNodeId = rd.getFieldByName("I_NODE_ID").value.toString();
				if("1".equals(rd.getFieldByName("I_TYPE").value.toString())){//动作
					String strNodeAudit = queryAuditPerson(_strLaunchhUser,_strLaunchBranch,rd.getFieldByName("S_AUDIT_BRANCH").value.toString(),rd.getFieldByName("S_AUDIT_ROLE").value.toString(),rd.getFieldByName("S_AUDIT_USER").value.toString());
					mapNodes.put(strNodeId, strNodeAudit);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			tableEx.close();
		}
		return mapNodes;
	}


	/**
	 * 运行-跳岗
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
		//审批人为�?&&跳岗�?1，则++
		while("T".equals(strArrayUsers[index])){
			/**发�?�消�?*/
			index++;
			System.out.println("此节点跳�?");
		}
		return index;
	}
	
	/**
	 * 获取下一节点
	 * @param request
	 * @param _strNowNode
	 * @param _map
	 * @return
	 */
	public Record getNextNodeByCondition(HttpServletRequest request,String _strNowNode,TableEx _tabEx){
		Record record =null;
		try {
			// 获取当前记录
			Record objNowRd = getRecordByNodeId(_strNowNode,_tabEx);
			//判断节点类型
			String strNodeType = getColString("I_TYPE", objNowRd);
			System.out.println("nodeID:"+getColString("I_NODE_ID", objNowRd));
			if(("3").equals(strNodeType)){//�?�?-找到子节�?-自调�?
				record = getNextNodeByCondition(request,getColString("S_CHILD_ID", objNowRd),_tabEx);
			}else if("1".equals(strNodeType)){//动作-赋�??-返回record
				record = objNowRd;
			}else if("2".equals(strNodeType)){//网关-根据网关条件判断获取下一节点-自调�?
			   // String _strCon = "4:   T_GJFLGZ.S_ZDR  =  '888'   $5:   T_GJFLGZ.S_ZDR  =  'gzy'   $";
			   // String strNextNodeId = appendConditionSql(request,_strCon);
				String strNextNodeId = appendConditionSql(request,getColString("S_CONDITION", objNowRd));
				record = getNextNodeByCondition(request,strNextNodeId,_tabEx);
			}else if("4".equals(strNodeType)){//结束-赋�??-返回record
				record = objNowRd;
			}
		} catch (Exception e) {
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
			e.printStackTrace();
		}
		return rd;
	}



	/**
	 * 查询流程节点信息
	 * @param _strFlowId
	 * @param _strFlowRunId
	 * @param _strNodeId
	 * @return
	 */
	public TableEx queryFlowNodeInfo(String _strFlowId,String _strVersion,String _strNodeId){
		TableEx tableEx = null;
		try {
			String strWhere = " 1=1";
			strWhere = strWhere+((_strFlowId==null||"".equals(_strFlowId))?" ":(" and S_FLOW_ID='"+_strFlowId+"' "));
			strWhere = strWhere+((_strVersion==null||"".equals(_strVersion))?" ":(" and S_AUDIT_VERSION='"+_strVersion+"' "));
			strWhere = strWhere+((_strNodeId==null||"".equals(_strNodeId))?" ":(" and I_NODE_ID='"+_strNodeId+"' "));
			tableEx = new TableEx("*", "t_sys_flow_node", strWhere);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tableEx;
	}
	
	/**
	 * 查询运行表T_SYS_FLOW_RUN
	 * @param _strFlowId
	 * @param _strVersion
	 * @param _strFlowRunId
	 * @return
	 */
	public TableEx queryFlowRun(String _strFlowId,String _strVersion,String _strFlowRunId){
		TableEx ex = null;
		try {
			ex = new TableEx("*", "T_SYS_FLOW_RUN", " 1=1 and S_FLOW_ID='"+_strFlowId+"' and S_AUDIT_VERSION ='"+_strVersion+"' and S_RUN_ID ='"+_strFlowRunId+"'");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ex;
	}
	
	/**
	 * 更新运行日志T_SYS_FLOW_RUN
	 * @param _strArrayFlowRun
	 * @param _strType 1:插入 2:更新 3:更新
	 */
	public void updateFlowRun(String[] _strArrayFlowRunVal,String _strType){
		DBFactory dbf = new DBFactory();
		try {
			if("1".equals(_strType)){
				String strTabCol ="(S_FLOW_ID,S_RUN_ID,S_NODE_CODE,S_AUDIT_VERSION,S_LAUNCH_DATE,S_LAUNCH_USER,S_AUD_USER,S_AUDIT_INDEX,S_AUDIT_MSG,S_LAUNCH_BRANCH,S_AUDIT_ARRAYYQ,S_AUDIT_ARRAY,S_AUDIT_NODES,I_ISOVER,S_AUDIT_OTHER)";
				_strArrayFlowRunVal = arrayAddSingleQuotes(_strArrayFlowRunVal);
				String strTabVal = Arrays.toString(_strArrayFlowRunVal);
				System.out.println("strTabVal"+strTabVal);
				strTabVal = strTabVal.substring(1,strTabVal.length()-1);
				//重新发起流程:删除----插入
				dbf.sqlExe("delete from T_SYS_FLOW_RUN where S_FLOW_ID="+_strArrayFlowRunVal[0] +" and S_RUN_ID="+_strArrayFlowRunVal[1]+" and S_AUDIT_VERSION="+_strArrayFlowRunVal[3]+"",true);
				dbf.sqlExe("insert into T_SYS_FLOW_RUN "+strTabCol+" values("+strTabVal+")", true);
			}else if("2".equals(_strType)){
				String[] strArrayTabCols ={"S_FLOW_ID","S_RUN_ID","S_AUDIT_VERSION","S_NODE_CODE","S_AUD_USER","S_AUDIT_INDEX","I_ISOVER"};
				String strTabVal = "";
				for(int i=3,j=strArrayTabCols.length;i<j;i++){
					strTabVal = ("".equals(strTabVal)?"":(strTabVal+","))+strArrayTabCols[i]+"='"+_strArrayFlowRunVal[i]+"' ";
				}
				dbf.sqlExe("update T_SYS_FLOW_RUN set "+strTabVal+"  where S_FLOW_ID='"+_strArrayFlowRunVal[0]+"' and S_RUN_ID='"+_strArrayFlowRunVal[1]+"' and S_AUDIT_VERSION='"+_strArrayFlowRunVal[2]+"'", true);
			}else if("3".equals(_strType)){
				String strTabVal = "";
				String[] strArrayTabCols ={"S_FLOW_ID","S_RUN_ID","S_AUDIT_VERSION","S_AUDIT_MSG","S_AUDIT_ARRAYYQ","S_AUDIT_ARRAY","S_AUDIT_NODES","S_AUDIT_OTHER","I_ISOVER"};
				for(int i=3,j=strArrayTabCols.length;i<j;i++){
					strTabVal = ("".equals(strTabVal)?"":(strTabVal+","))+strArrayTabCols[i]+"='"+_strArrayFlowRunVal[i]+"' ";
				}
				dbf.sqlExe("update T_SYS_FLOW_RUN set "+strTabVal+"  where S_FLOW_ID='"+_strArrayFlowRunVal[0]+"' and S_RUN_ID='"+_strArrayFlowRunVal[1]+"' and S_AUDIT_VERSION='"+_strArrayFlowRunVal[2]+"'", true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbf.close();
		}
	}
	
	/**
	 * 插入运行日志表T_SYS_FLOW_LOG
	 * @param _type 1:插入 2:更新
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
			e.printStackTrace();
		} finally {
			dbf.close();
		}
	}
	
	/**
	 * 审批人�?�择节点-查询当前节点之前�?有节�?
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
			sb.append(" and T_SYS_FLOW_LOG.S_FLOW_ID='"+_strFlowId+"'");
			sb.append(" and T_SYS_FLOW_LOG.S_AUDIT_VERSION='"+_strVersion+"'");
			sb.append(" and T_SYS_FLOW_LOG.S_RUN_ID='"+_strFlowRunId+"'");
			sb.append(" and (T_SYS_FLOW_LOG.S_AUD_STAUS='1' or T_SYS_FLOW_LOG.S_AUD_STAUS='3')");
			sb.append(" GROUP BY S_NODE_ID");
//			sb.append(" order by T_SYS_FLOW_LOG.S_AUD_DATE desc");//审批节点可能重复,不影响功�?,放开则审批人节点可能丢失
			ex = new TableEx("T_SYS_FLOW_LOG.S_NODE_ID,t_rgxx.SYGMC,T_SYS_FLOW_NODE.S_NODE_NAME", "T_SYS_FLOW_LOG ,T_SYS_FLOW_NODE ,T_RGXX",sb.toString());
			int iCount = ex.getRecordCount(); 
			int flag = -1;
			for(int i=0;i<iCount;i++){
				strReturn = ("".equals(strReturn)?"":(strReturn+"|"))+getColString("S_NODE_ID",  ex.getRecord(i))+","+getColString("SYGMC",  ex.getRecord(i))+","+getColString("S_NODE_NAME",  ex.getRecord(i));
				if(_strNodeId.equals(getColString("S_NODE_ID",  ex.getRecord(i)))){
					flag = i;
				}
			}
			if(flag > -1){//�?
				String[] strArrayReturn = strReturn.split("\\|",-1);
				String strReturnTemp = strArrayReturn[0];
				for(int i=1,j=strArrayReturn.length-1;i<j;i++){
					strReturnTemp =strReturnTemp+"|"+strArrayReturn[i];
				}
				strReturn = strReturnTemp;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			ex.close();
		}
		return strReturn;
	}
	
	/**
	 * 查询上一节点
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
			if(flag == -1){//�?
				strBeforeNodeId =exFlowLog.getRecord(0).getFieldByName("S_NODE_ID").value.toString();
			}else{//�?
				strBeforeNodeId =exFlowLog.getRecord(flag+1).getFieldByName("S_NODE_ID").value.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			exFlowLog.close();
		}
		return strBeforeNodeId;
	}
	
	/**
	 * 流程日志之前节点
	 * @param _strFlowId
	 * @param _strVersion
	 * @param _strFlowRunId
	 * @return
	 */
	public TableEx queryFlowLogBefore(String _strFlowId,String _strVersion,String _strFlowRunId,String _strNodeId){
		TableEx ex = null;
		try {
			//1,查询日志 状�?�为1:审核通过 3:提交 是否有数�?
			String strWhere = " 1=1 and (S_AUD_STAUS='1' or S_AUD_STAUS='3') ";
			strWhere = strWhere+((_strFlowId==null||"".equals(_strFlowId))?" ":(" and S_FLOW_ID='"+_strFlowId+"' "));
			strWhere = strWhere+((_strFlowRunId==null||"".equals(_strFlowRunId))?" ":(" and S_RUN_ID='"+_strFlowRunId+"' "));
			strWhere = strWhere+((_strVersion==null||"".equals(_strVersion))?" ":(" and S_AUDIT_VERSION='"+_strVersion+"' "));
			strWhere = strWhere+((_strNodeId==null||"".equals(_strNodeId))?" ":(" and I_NODE_ID='"+_strNodeId+"' "));
			strWhere = strWhere+" order by T_SYS_FLOW_LOG.S_AUD_DATE desc";
			ex = new TableEx("*", "T_SYS_FLOW_LOG",strWhere);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			ex.close();
		}
		return ex;
	}
	/**
	 * 流程日志查询
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
			e.printStackTrace();
		}
		return ex;
	}
	/**
	 * 判断节点ID在数组中的索引位�?
	 * @param _strArrayNodeIds
	 * @param _strNodeId
	 * @return
	 */
	public int getChoiceNode(String[] _strArrayNodeIds,String _strNodeId){
		int index = -1;
		for(int i=0,j=_strArrayNodeIds.length;i<j;i++){
			if(_strNodeId.equals(_strArrayNodeIds[i])){
				index = i;
				break;
			}
		}
		return index;
	}
	
	/**
	 * 字符串数组是否有交集
	 * @param _str1
	 * @param _str2
	 * @return
	 */
	public boolean compareArrayRepeat(String _str1,String _str2){
		Set<String> set1 = new HashSet<String>(Arrays.asList(_str1.split(",")));
		Set<String> set2 = new HashSet<String>(Arrays.asList(_str2.split(",")));
		set1.retainAll(set2);
		return (set1.size()>0);
	}
	
	/**
	 * 当前日期+day
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
	 * 字符串截取，重新拼接
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
	public String getColString(String _strCol,Record rd){
		String strReturn = "";
		try {
			strReturn= (rd.getFieldByName(_strCol)==null||"".equals(rd.getFieldByName(_strCol)))?"":rd.getFieldByName(_strCol).value.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strReturn;
	}
	
	/**
	 * 数组元素添加单引�?---拼接sql语句
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
		System.out.println(_str);
		Object result = null;
		try {
			result = engine.eval(_str);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		System.out.println("结果类型:" + result.getClass().getName() + ",计算结果:"+ result);
		return result;
	}  
	 
	public void shanchutable(String _strTable){
		DBFactory dbf = new DBFactory();
		try {
				dbf.sqlExe("delete from T_SYS_FLOW_RUN where S_FLOW_ID='"+_strTable+"'",true);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbf.close();
		}
	}
}
