package com.security.commission;

import javax.servlet.http.HttpServletRequest;

import com.timing.impcl.MantraLog;
import com.timing.impcl.ProcessParameterVO;
import com.yulongtao.db.DBFactory;
import com.yulongtao.db.Record;
import com.yulongtao.db.TableEx;
import com.timing.impcl.MantraUtil;

public class CommissionBusiness {
	ProcessParameterVO pro = null;

	public boolean CommissionProject(HttpServletRequest _request) {
		pro = new ProcessParameterVO(_request);

		return true;
	}

	/**
	 * 
	 * SecurityCheck:��ȫ�������
	 * 
	 */
	public boolean SecurityCheck(HttpServletRequest _request) {
		
		MantraUtil tool = new MantraUtil();
		pro = new ProcessParameterVO(_request);
		TableEx tableEx = null;
		Record record = null;
		DBFactory dbf = new DBFactory();
		String S_ID, S_GCXMID = "";
		try {
			tableEx = dbf.query("select S_ID,S_GCXMID from T_AQZZSC where S_ID='" + pro.getInpPkey() + "'");
			int recordCount = tableEx.getRecordCount();
			for (int i = 0; i < recordCount; i++) {
				
				record = tableEx.getRecord(i);
				
				S_GCXMID = tool.getStrByRecord(record, "S_GCXMID");
				
				S_ID = tool.getStrByRecord(record, "S_ID");
				
				tool.recordRel(pro.getBranck(), "1506310525794", S_GCXMID, "T_WWXM", pro.getSpageCode(), S_ID,
						"T_AQZZSC");
				
				dbf.sqlExe("update T_WWXM set S_BS='true' where S_ID='" + S_GCXMID + "';", false);
				
			}
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			tableEx.close();
			dbf.close();
		}
		return true;
	}
	
		/**
	 * 
	 * EducationTraining:��ί������ѵ
	 * 
	 */

	public boolean EducationTraining(HttpServletRequest _request) {
		pro = new ProcessParameterVO(_request);
		MantraUtil tool = new MantraUtil();
		Record record = null;
		DBFactory dbf = new DBFactory();
		TableEx tableEx = null;
		// T_WWJYPX$S_JXJH_ID
		try {
			tableEx = dbf.query(
					"select T_WWJYPX.S_JXJH_ID ZJHID from T_WWJYPX where T_WWJYPX.S_ID='" + pro.getInpPkey() + "'");
			MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "select T_WWJYPX.S_JXJH_ID ZJHID from T_WWJYPX where T_WWJYPX.S_ID='" + pro.getInpPkey() + "'");		
			int recordCount = tableEx.getRecordCount();
			String ZJHID = "";
			for (int i = 0; i < recordCount; i++) {
				record = tableEx.getRecord(i);
				ZJHID = tool.getStrByRecord(record, "ZJHID");
				
				tool.recordRel(pro.getBranck(), "1506310525794", ZJHID, "T_WWXM", pro.getSpageCode(),pro.getInpPkey(),
						"T_WWJYPX");
				
			}

		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			dbf.close();
			if (tableEx != null)
				tableEx.close();
		}
		return true;
	}

	/**
	 * 
	 * SecurityClarificaiton:��ί��ȫ����
	 * 
	 */

	public boolean SecurityClarificaiton(HttpServletRequest _request) {
		pro = new ProcessParameterVO(_request);
		MantraUtil tool = new MantraUtil();
		Record record = null;
		DBFactory dbf = new DBFactory();
		TableEx tableEx = null;
		// T_WWAQJD$S_XMMCID
		try {
			tableEx = dbf.query(
					"select T_WWAQJD.S_XMMCID ZJHID from T_WWAQJD where T_WWAQJD.S_ID='" + pro.getInpPkey() + "'");
			int recordCount = tableEx.getRecordCount();
			String ZJHID = "";
			for (int i = 0; i < recordCount; i++) {
				record = tableEx.getRecord(i);
				ZJHID = tool.getStrByRecord(record, "ZJHID");
				
				tool.recordRel(pro.getBranck(), "1506310525794", ZJHID, "T_WWXM", pro.getSpageCode(),pro.getInpPkey(),
						"T_WWAQJD");
				
			}

		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			dbf.close();
			if (tableEx != null)
				tableEx.close();
		}
		return true;
	}
	
	
	/**
	 * 
	 * WorkPermit:�������֤
	 * 
	 */

	public boolean WorkPermit(HttpServletRequest _request) {
		pro = new ProcessParameterVO(_request);
		MantraUtil tool = new MantraUtil();
		Record record = null;
		DBFactory dbf = new DBFactory();
		TableEx tableEx = null;
		// T_KGXKZ.S_XKXMBM
		try {
			tableEx = dbf.query(
					"select T_KGXKZ.S_XKXMBM ZJHID from T_KGXKZ where T_KGXKZ.S_ID='" + pro.getInpPkey() + "'");
			MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "select T_WWJYPX.S_JXJH_ID ZJHID from T_WWJYPX where T_WWJYPX.S_ID='" + pro.getInpPkey() + "'");
			int recordCount = tableEx.getRecordCount();
			String ZJHID = "";
			for (int i = 0; i < recordCount; i++) {
				record = tableEx.getRecord(i);
				ZJHID = tool.getStrByRecord(record, "ZJHID");
				tool.recordRel(pro.getBranck(), "1514363785380", ZJHID, "T_WWAQJD", pro.getSpageCode(),pro.getInpPkey(),
						"T_KGXKZ");
			}

		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			dbf.close();
			if (tableEx != null)
				tableEx.close();
		}
		return true;
	}
	
	
	/**
	 * 
	 * EducationAcceptance:��ί��������
	 * 
	 */

	public boolean EducationAcceptance(HttpServletRequest _request) {
		pro = new ProcessParameterVO(_request);
		MantraUtil tool = new MantraUtil();
		Record record = null;
		DBFactory dbf = new DBFactory();
		TableEx tableEx = null;
		// T_WWJGYS$S_KGXGZ_ID
		try {
			tableEx = dbf.query(
					"select T_WWJGYS.S_KGXGZ_ID ZJHID from T_WWJGYS where T_WWJGYS.S_ID='" + pro.getInpPkey() + "'");
			MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "select T_WWJYPX.S_JXJH_ID ZJHID from T_WWJYPX where T_WWJYPX.S_ID='" + pro.getInpPkey() + "'");
			int recordCount = tableEx.getRecordCount();
			String ZJHID = "";
			for (int i = 0; i < recordCount; i++) {
				record = tableEx.getRecord(i);
				ZJHID = tool.getStrByRecord(record, "ZJHID");
				
				tool.recordRel(pro.getBranck(), "1516176894007", ZJHID, "T_KGXKZ", pro.getSpageCode(),pro.getInpPkey(),
						"T_WWJGYS");
				
			}

		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			dbf.close();
			if (tableEx != null)
				tableEx.close();
		}
		return true;
	}
}
