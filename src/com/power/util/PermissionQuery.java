package com.power.util;

import java.util.HashMap;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import com.yulongtao.pub.Pub;
import com.yulongtao.db.Record;
import com.yulongtao.db.TableEx;
import com.timing.impcl.MantraLog;
import com.yulongtao.db.DBFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PermissionQuery {

	private static String _tableName = "T_SYS_MOD";
	private static String _tableCol = "T_ASSFILED assFiled";

	public StringBuffer ModPowerMeter(String _modId, HttpServletRequest _request) {
		Pub pub = new Pub(); //pub�෽��
		DBFactory dbFactory = new DBFactory();
		TableEx tableEx = null;
		Record record;
		HttpSession sesion = _request.getSession();
		StringBuffer retStrBuf = new StringBuffer(); //���ص�����
		JSONObject jsonObj = new JSONObject();  //��JSON����
		JSONArray jsonArray = new JSONArray();  //������ҳ��İ�ťȨ��
		String pageCode = "";
		String sql = " SELECT " + _tableCol + " FROM " + _tableName + " WHERE SMODCODE = '" + _modId + "' ";

		        	 
		try {

			tableEx = dbFactory.query(sql);

			if (tableEx.getRecordCount() == 0 || tableEx.getRecordCount() > 1) { // �޲˵���˵�����
				retStrBuf.append("<div class='errorCode'>error: check the menu code is " + _modId+"</div>");
				retStrBuf.append("<div class='errorName'>管理员未配置当前节点按钮权限</div>");
				return retStrBuf;
			}

			record = tableEx.getRecord(0);
			
            if (record.getFieldByName("assFiled").value==null ) { // �޲˵���˵�����
			    retStrBuf.append("<div class='errorCode'>error: check the menu code is " + _modId+"</div>");
				retStrBuf.append("<div class='errorName'>管理员未配置当前节点按钮权限</div>");
				return retStrBuf;
			}
			String assFiled = record.getFieldByName("assFiled").value.toString();

            if (assFiled==null || assFiled.length() ==0 ) { // �޲˵���˵�����
				retStrBuf.append("<div class='errorCode'>error: check the menu code is " + _modId+"</div>");
				retStrBuf.append("<div class='errorName'>管理员未配置当前节点按钮权限</div>");
				return retStrBuf;
			}
			String[] assFiledPageArr = assFiled.split(","); // ����ҳ��

			jsonObj.put("code", _modId);// ��ǰ���ܶ���
			jsonObj.put("text", _modId);// ��ǰ���ܶ�����

			for (int i = 0, j = assFiledPageArr.length; i < j; i++) {
				pageCode = assFiledPageArr[i];//ҳ�����
				
				JSONObject childJsonData = new JSONObject();
				
				childJsonData.put("text", pub.getPageName(pageCode));	// ҳ������
				childJsonData.put("code", pageCode);					// ҳ�����
				
				HashMap<String, Vector> map = pub._getPageMsgByPIdRights(pageCode, sesion);

				Vector vec = map.get("FIELDCODES");// �ֶδ���
				Vector vecName = map.get("FIELDNAMES");// �ֶ�����
				childJsonData.put("FIELD", pageMsg(vec,vecName));
				
				vec = map.get("BTTNCODES");// ��ť����
				vecName = map.get("BTTNNAMES");// ��ť����
				childJsonData.put("BTTN", pageMsg(vec,vecName));
				
				jsonArray.put(childJsonData);
			}
			jsonObj.put("child", jsonArray);// NAME
			
			
			retStrBuf.append("<textarea id=\"modPowerModel\" style=\"display: none\">");
			retStrBuf.append(jsonObj.toString());
			retStrBuf.append("</textarea><textarea  id=\"modPowerModel_get\" style=\"display: none\">");
			retStrBuf.append(getModPowerByRoleAndPageCode( _request.getParameter("S_ROLECODE"),assFiled).toString());
			retStrBuf.append("</textarea>");
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			dbFactory.close();
			tableEx.close();
		}
		
		return retStrBuf;
	}
    public boolean UpdateModPowerMeter(HttpServletRequest _request) {
	String roleCode="";
		String fileCode="";
		String pageCode="";
			String SYS_ROLE_PARA =  _request.getParameter("SYS_ROLE_PARA");
		DBFactory dbFactory = new DBFactory();
		try {
			JSONObject jsStr = new JSONObject(SYS_ROLE_PARA);
			JSONArray jay=new JSONArray();
	         Iterator keys = jsStr.keys(); 
	         String key ="";
	            while(keys.hasNext()){  
	        	 
	        	 key= keys.next().toString();
	        	 
	        	 dbFactory.sqlExe("delete from t_sys_rightdetail where SROLECODE=\""+_request.getParameter("S_ROLECODE")+"\" and SPAGECODE=\""+key+"\" ",true);

	        	 jay = new JSONArray(jsStr.get(key).toString());
	        	 
	        	 for(int i = 0 ,j = jay.length() ; i< j ;i++) {
	        		 roleCode=((JSONObject)jay.get(i)).get("roleCode").toString();
	        		 fileCode= new String(((JSONObject)jay.get(i)).get("fileCode").toString().replace("\"", "\\\"").getBytes("iso8859-1"),"UTF-8");  
	        		 pageCode=((JSONObject)jay.get(i)).get("pageCode").toString();

	        		 dbFactory.sqlExe("insert into t_sys_rightdetail (SROLECODE,SPAGECODE,SFIELDCODE) values (\""+roleCode+"\",\""+pageCode+"\",\""+fileCode+"\")",true);
	        		
	        	 }
	        	 
	         }
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			MantraLog.fileCreateAndWrite(e);
		} finally {
			dbFactory.close();
		
		}
		
		return true;
	}
	public StringBuffer getModPowerByRoleAndPageCode(String _roleCode,String _pageCode) {
		DBFactory dbFactory = new DBFactory();
		TableEx tableEx = null;
		Record record;
		_pageCode= "'"+_pageCode.replace(",", "','")+"'";
		String sql = " SELECT SFIELDCODE sfileCode ,SPAGECODE spageCode FROM t_sys_rightdetail WHERE SROLECODE=\""+_roleCode+"\" and SPAGECODE in ("+_pageCode+")";
		StringBuffer retStrBuf = new StringBuffer();
		JSONObject jsonObj = new JSONObject();
		try {

			tableEx = dbFactory.query(sql);
			int recordCount = tableEx.getRecordCount();
			
			for(int i = 0 ; i < recordCount ; i++) {
				record = tableEx.getRecord(i);
				
				jsonObj.put(record.getFieldByName("sfileCode").value.toString(),record.getFieldByName("spageCode").value.toString());
				
				
			}

		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			dbFactory.close();
			tableEx.close();
		}
		
		
		return retStrBuf.append(jsonObj.toString());
	}
	public JSONObject pageMsg(Vector<String> vec,Vector<String> vecName) throws JSONException{

		JSONObject jsonObj = new JSONObject();
		if (vec.size() > 0) {
			for (int i = 0, j = vec.size(); i < j; i++) {
				jsonObj.put(vecName.get(i), vec.get(i));
			}
		}
		return jsonObj;
	}
}
