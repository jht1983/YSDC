package com.timing.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import com.timing.impcl.MantraLog;
import com.timing.impcl.ProcessParameterVO;
import com.yulongtao.db.DBFactory;
import com.timing.impcl.EventCl;
import com.yulongtao.db.FieldEx;
import com.yulongtao.db.Record;
import com.yulongtao.db.TableEx;

public class TimingInit {

	public void init() {
		EventCl.vecGJStatus = initData("T_WXDQGZJH");

	}

	public HashMap<String, String> InsertAttr(HttpServletRequest request) {
		HashMap<String, String> vecAddDate = new HashMap<String, String>(); // ���ص�HashMap
		Map inputDataMap = request.getParameterMap(); // ��ȡ�����������в���
		Iterator entries = inputDataMap.entrySet().iterator(); // ����������
		String _strSplitN = ",";// �и����-����
		try {
			while (entries.hasNext()) {
				Map.Entry entry = (Map.Entry) entries.next(); // �õ���ǰ�� KEYVALUE entry��ʽ
				String sys_PageKey = (String) entry.getKey(); // �õ�KEY
				sys_PageKey.substring(sys_PageKey.indexOf("$") + 1);
				if (sys_PageKey.indexOf("NO_") == 0)
					continue; // ����NO_��ͷ��
				if (sys_PageKey.indexOf("$") == 0)
					continue; // ����$��ͷ��
				StringBuffer sys_PageValue = new StringBuffer();
				for (String a : (String[]) entry.getValue()) { // ��������ظ� ƴ��
					sys_PageValue.append(new String(a.getBytes("iso8859-1"), "GBK"));
					sys_PageValue.append(_strSplitN);
				}
				vecAddDate.put(sys_PageKey, sys_PageValue.substring(0, sys_PageValue.length() - _strSplitN.length())); // �򷵻ص�HashMap
			}
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		}
		return vecAddDate;
	}

	public boolean insertMain(HttpServletRequest request) {

		String pageCode = request.getParameter("SPAGECODE");
		String id = request.getParameter("S_ID");
		switch (pageCode) {
		case "1500428508300":
			EventCl.vecGJStatus.add(getTabelAllData("T_WXDQGZJH", "T_WXDQGZJH.S_ZJ = '"+id+"'"));
			MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "ά�޶�ʱ�������");
			break;
		default:
			MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "��ʱ����ȱʡ�����");
			break;
		}
		return true;
	}

	public Vector<HashMap<String, String>> initData(String _tableName) {
		Vector<HashMap<String, String>> initVecprtData = new Vector<HashMap<String, String>>();
		HashMap<String, String> initMapData = null;
		TableEx tableEx = null;
		DBFactory dbf = new DBFactory();
		Record record = null;
		FieldEx fileEx = null;
		try {
			String sql = " select * from " + _tableName;
			tableEx = dbf.query(sql);
			int recordCount = tableEx.getRecordCount();
			int colCount = tableEx.getColCount();
			for (int i = 0; i < recordCount; i++) {
				initMapData = new HashMap<String, String>();
				record = tableEx.getRecord(i);
				for (int j = 0; j < colCount; j++) {
					fileEx = record.getFieldById(j + 1);
					initMapData.put(fileEx.fieldName.toString(), fileEx.value.toString());
				}
				initVecprtData.add(initMapData);
			}
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			if (tableEx != null)
				tableEx.close();
			if (dbf != null)
				dbf.close();
		}
		return initVecprtData;
	}

	public HashMap<String, String> getTabelAllData(String _tableName, String _queryConditions) {
		HashMap<String, String> dataMap = new HashMap<String, String>();
		TableEx tableEx = null;
		DBFactory dbf = new DBFactory();
		Record record = null;
		FieldEx fileEx = null;
		try {
			String sql = " select * from " + _tableName + " where " + _queryConditions;
			tableEx = dbf.query(sql);
			int recordCount = tableEx.getRecordCount();
			int colCount = tableEx.getColCount();
			for (int i = 0; i < recordCount; i++) {
				record = tableEx.getRecord(i);
				for (int j = 0; j < colCount; j++) {
					fileEx = record.getFieldById(j + 1);
					dataMap.put(fileEx.fieldName.toString(), fileEx.value.toString());
				}
			}
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			if (tableEx != null)
				tableEx.close();
			if (dbf != null)
				dbf.close();
		}
		return dataMap;
	}

}
