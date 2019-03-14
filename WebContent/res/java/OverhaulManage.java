package com.equi.overhaul;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import com.timing.impcl.ProcessParameterVO;
import com.timing.impcl.RelationVO;
import com.yulongtao.db.DBFactory;
import com.yulongtao.db.Record;
import com.yulongtao.db.TableEx;
import com.timing.impcl.MantraLog;
import com.timing.impcl.MantraUtil;
import com.yulongtao.util.EString;

public class OverhaulManage {
	ProcessParameterVO proVo = null;

	/*
	 * StandardOverHaul:���ޱ�׼������
	 * 
	 */
	public boolean StandardOverHaul(HttpServletRequest _request) {
		proVo = new ProcessParameterVO(_request); // ������׼����

		return true;
	}

	/*
	 * SummaryOverhaulPlan:���ܼ��޼ƻ�
	 * 
	 */
	public boolean SummaryOverhaulPlan(HttpServletRequest _request) {
		proVo = new ProcessParameterVO(_request); // ������׼����
		HashMap<String, String> breakUp = new HashMap<String, String>(); // ���Hash
		MantraUtil tool = new MantraUtil(); // ������
		TableEx tableEx = null;
		Record record = null;
		DBFactory dbf = new DBFactory();
		String splitCon = "T_HZJXJH_F.S_BDHF Mantra_splitCondition"; // �������
		String[] basicData = {
				"T_HZJXJH.S_ZJ,T_HZJXJH.S_JHMC,T_HZJXJH.S_JZH,T_HZJXJH.S_JXLX,T_HZJXJH.S_JHLX,T_HZJXJH.S_JHZFY,<<FLOW_ID>>,<<UUID>>,<<SYS_GENER_ID>>,T_HZJXJH.S_ZZ",
				"T_JXZJHFJ.S_GLID,T_JXZJHFJ.S_MC,T_JXZJHFJ.S_JZH,T_JXZJHFJ.S_JXLX,T_JXZJHFJ.S_JHLX,T_JXZJHFJ.S_JHZFY,T_JXZJHFJ.SYS_FLOW_VER,T_JXZJHFJ.S_RUN_ID,T_JXZJHFJ.S_ZJ,T_JXZJHFJ.S_ZZ",
				"<<SYS_GENER_ID>>,<<S_FID>>,T_HZJXJH_F.S_BZ,T_HZJXJH_F.S_JHFY,T_HZJXJH_F.S_JXXM,T_HZJXJH_F.S_SBBM,T_HZJXJH_F.S_SBBJMC,T_HZJXJH_F.S_XMFZR,T_HZJXJH_F.S_XMFZR_BM,T_HZJXJH_F.S_ZLBZ,T_HZJXJH_F.S_ZY,T_HZJXJH_F.S_ID",
				"T_JXZJHFJSB.S_ZJ,T_JXZJHFJSB.S_PID,T_JXZJHFJSB.S_BZ,T_JXZJHFJSB.S_JHFY,T_JXZJHFJSB.S_JXXM,T_JXZJHFJSB.S_SBBM,T_JXZJHFJSB.S_SBMC,T_JXZJHFJSB.S_XMFZR,T_JXZJHFJSB.S_XMFZR_BM,T_JXZJHFJSB.S_ZLBZHYQ,T_JXZJHFJSB.S_ZY,T_JXZJHFJSB.S_BM",
				"T_HZJXJH.S_ZJ = T_HZJXJH_F.S_PID", "T_JXZJHFJ.S_ID = T_JXZJHFJSB.S_PID" }; // ��Ӧ����
		try {

			tableEx = new TableEx(splitCon + ",T_HZJXJH_F.S_ID Mantra_SON_S_ID ",
					"T_HZJXJH left join T_HZJXJH_F on " + basicData[4],
					" T_HZJXJH.S_ZJ='" + proVo.getInpPkey() + "' order by T_HZJXJH_F.S_ID;");

			int recordIndex = tableEx.getRecordCount(); // ��ȡ���ݿ��ж�Ӧ�����������в���
			for (int i = 0; i < recordIndex; i++) {
				record = tableEx.getRecord(i);
				String splitCond = record.getFieldByName("Mantra_splitCondition").value.toString();
				String insertSql = ""; // ������ӵ�SQL ����
				splitCond = "".equals(splitCond)?"sys_NULLBYdefault":splitCond;
				if (breakUp.get(splitCond) == null) { // �ж��Ƿ���Ҫ���

					insertSql = "insert into T_JXZJHFJ (" + basicData[1] + ") select " + basicData[0]
							+ " from  T_HZJXJH  where T_HZJXJH.S_ZJ='" + proVo.getInpPkey() + "'";

					insertSql = tool.sqlDisCom(insertSql, "pageCode=1516168904786&bmid=" + proVo.getBranck());// ����SQL

					dbf.sqlExe(insertSql, true);// ����SQL��������

					tool.recordRel(proVo.getBranck(), proVo.getSpageCode(), proVo.getInpPkey(), "T_HZJXJH",
							"1516168904786", tool.getOrdGreId(), "T_JXZJHFJ"); // ������ϵ

					breakUp.put(splitCond, tool.getOrdGreId()); // ���ò������
				}
				String SPLITCONDID = breakUp.get(splitCond);

				String sonId = record.getFieldByName("Mantra_SON_S_ID").value.toString();

				insertSql = "insert into T_JXZJHFJSB (" + basicData[3] + ") select " + basicData[2]
						+ " from  T_HZJXJH_F where T_HZJXJH_F.S_ID='" + sonId + "' and  T_HZJXJH_F.S_PID  = '"
						+ proVo.getInpPkey() + "';";

				insertSql = tool.sqlDisCom(insertSql, "S_FID=" + SPLITCONDID + "&pageCode=&bmid="); // ����SQL

				tool.recordRel(proVo.getBranck(), "", sonId, "T_HZJXJH_F", "", tool.getOrdGreId(), "T_JXZJHFJSB");// ������ϵ

				if (i + 1 == recordIndex) {
					dbf.sqlExe(insertSql, false);
				} else {
					dbf.sqlExe(insertSql, true);
				}

			}

		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			tableEx.close();
			dbf.close();
		}

		return true;
	}

	/*
	 * ProfessionalOverhaulPlan:רҵ���޼ƻ�
	 * 
	 */
	public boolean ProfessionalOverhaulPlan(HttpServletRequest _request) {
		MantraUtil tool = new MantraUtil();
		proVo = new ProcessParameterVO(_request); // ������׼����
		TableEx tableEx = null;
		Record record = null;
		DBFactory dbf = new DBFactory();
		if ("".equalsIgnoreCase(proVo.getInpPkey()))
			return false;
		String sql = "";
		String strSumOverPlanCode = "";
		String S_ZY = "";
		int sumNum = 0;

		try {
			tableEx = new TableEx("T_ZYJXJH.S_CZJXJHID S_CZJXJH , T_ZYJXJHF.S_ID S_ID ,T_ZYJXJH.S_ZY S_ZYL ",
					"T_ZYJXJH left join T_ZYJXJHF on T_ZYJXJH.S_ID = T_ZYJXJHF.S_FID",
					" T_ZYJXJH.S_ID='" + proVo.getInpPkey() + "' order by T_ZYJXJHF.S_ID ");
			int recordIndex = tableEx.getRecordCount();
			for (int i = 0; i < recordIndex; i++) {
				record = tableEx.getRecord(i);
				if (sumNum == 0 && i == 0) {
					strSumOverPlanCode = record.getFieldByName("S_CZJXJH").value.toString();
					sumNum = tool.getDateCont("T_HZJXJH_F", "T_HZJXJH_F.S_PID='" + strSumOverPlanCode + "' ");// BUG
					S_ZY = record.getFieldByName("S_ZYL").value.toString();
				}

				sql = "insert into T_HZJXJH_F (T_HZJXJH_F.S_ID,T_HZJXJH_F.S_PID,T_HZJXJH_F.S_ZY,T_HZJXJH_F.S_BDHF,T_HZJXJH_F.S_BZ,T_HZJXJH_F.S_FXGR,T_HZJXJH_F.S_JHFY,T_HZJXJH_F.S_JXXM,T_HZJXJH_F.S_SBBM,T_HZJXJH_F.S_SBBJMC,T_HZJXJH_F.S_XMFZR_BM,T_HZJXJH_F.S_XMFZR,T_HZJXJH_F.S_ZLBZ,T_HZJXJH_F.S_KSRQ,T_HZJXJH_F.S_JSRQ) select "
						+ "'" + (i + sumNum) + "','" + strSumOverPlanCode + "','" + S_ZY
						+ "',T_ZYJXJHF.S_BDHF,T_ZYJXJHF.S_BZFBZ,T_ZYJXJHF.S_FXGR,T_ZYJXJHF.S_JHFY,T_ZYJXJHF.S_JXXM,T_ZYJXJHF.S_SBBM,T_ZYJXJHF.S_SBMC,T_ZYJXJHF.S_XMFZR,T_ZYJXJHF.S_XMFZR_NAME,T_ZYJXJHF.S_ZLBZHYQ,T_ZYJXJHF.S_KSRQ,T_ZYJXJHF.S_JSQR from T_ZYJXJHF where T_ZYJXJHF.S_FID='"
						+ proVo.getInpPkey() + "' and T_ZYJXJHF.S_ID='" + record.getFieldByName("S_ID").value.toString()
						+ "';";
				if (i + 1 == recordIndex) {
					dbf.sqlExe(sql, false);
				} else {
					dbf.sqlExe(sql, true);
				}
			}

			tool.recordRel(proVo.getBranck(), "1510924423686", strSumOverPlanCode, "T_HZJXJH", proVo.getSpageCode(),
					proVo.getInpPkey(), "T_ZYJXJH");

		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			tableEx.close();
			dbf.close();
		}
		return true;
	}

	/*
	 * OverHaulTheReport:����ǰ�豸���۱���
	 * 
	 */
	public boolean OverHaulTheReport(HttpServletRequest _request) {
		proVo = new ProcessParameterVO(_request); // ������׼����
		MantraUtil tool = new MantraUtil();
		TableEx tableEx = null;
		Record record = null;
		DBFactory dbf = new DBFactory();
		int recordIndex = 0;
		String sumOver = "";
		try {
			tableEx = new TableEx("S_CZJXJHID S_CZJXJH", "T_JXQSBPJBG", "S_ZJ = '" + proVo.getInpPkey() + "'");
			recordIndex = tableEx.getRecordCount();
			if (recordIndex != 1) {
				MantraLog.WriteProgress(MantraLog.LOG_PROGRESS,
						"[err]:019->OverHaulTheReport->recordIndex=" + recordIndex);
			}
			for (int i = 0; i < recordIndex; i++) {
				record = tableEx.getRecord(i);
				if (record.getFieldByName("S_CZJXJH").value == null) {
					MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "[err]:019->OverHaulTheReport->S_CZJXJH=null");
					return false;
				}
				sumOver = record.getFieldByName("S_CZJXJH").value.toString();

				tool.recordRel(proVo.getBranck(), "1510924423686", sumOver, "T_HZJXJH", proVo.getSpageCode(),
						proVo.getInpPkey(), "T_JXQSBPJBG");

			}

		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			tableEx.close();
			dbf.close();
		}

		return true;
	}

	/*
	 * Fileverification:[��ί]�ļ��˲�
	 * 
	 */
	public boolean FileVerification(HttpServletRequest _request) {
		proVo = new ProcessParameterVO(_request); // ������׼����
		MantraUtil tool = new MantraUtil();
		TableEx tableEx = null;
		Record record = null;
		DBFactory dbf = new DBFactory();
		int recordIndex = 0;
		String sumOver = "";
		try {
			tableEx = new TableEx("T_JXWJHCB.S_CZJHID S_CZJXJH", "T_JXWJHCB", "S_ZJ = '" + proVo.getInpPkey() + "'");
			recordIndex = tableEx.getRecordCount();
			if (recordIndex != 1) {
				MantraLog.WriteProgress(MantraLog.LOG_PROGRESS,
						"[err]:019->OverHaulTheReport->recordIndex=" + recordIndex);
			}
			for (int i = 0; i < recordIndex; i++) {
				record = tableEx.getRecord(i);
				if (record.getFieldByName("S_CZJXJH").value == null) {
					MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "[err]:019->OverHaulTheReport->S_CZJXJH=null");
					return false;
				}
				sumOver = record.getFieldByName("S_CZJXJH").value.toString();

				tool.recordRel(proVo.getBranck(), "1516168904786", sumOver, "T_JXZJHFJ", proVo.getSpageCode(),
						proVo.getInpPkey(), "T_JXWJHCB");

			}

		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			tableEx.close();
			dbf.close();
		}

		return true;
	}

	/*
	 * SecurityClarificaiton:��ȫ����
	 * 
	 */
	public boolean SecurityClarificaiton(HttpServletRequest _request) {
		proVo = new ProcessParameterVO(_request); // ������׼����
		MantraUtil tool = new MantraUtil();
		TableEx tableEx = null;
		Record record = null;
		DBFactory dbf = new DBFactory();
		int recordIndex = 0;
		String sumOver = "";
		// T_XCAQCSJDK$S_CZJHDJH
		try {
			tableEx = new TableEx("T_XCAQCSJDK.S_CZJHID S_CZJXJH", "T_XCAQCSJDK",
					"S_ZJ = '" + proVo.getInpPkey() + "'");
			recordIndex = tableEx.getRecordCount();
			if (recordIndex != 1) {
				MantraLog.WriteProgress(MantraLog.LOG_PROGRESS,
						"[err]:019->OverHaulTheReport->recordIndex=" + recordIndex);
			}
			for (int i = 0; i < recordIndex; i++) {
				record = tableEx.getRecord(i);
				if (record.getFieldByName("S_CZJXJH").value == null) {
					MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "[err]:019->OverHaulTheReport->S_CZJXJH=null");
					return false;
				}
				sumOver = record.getFieldByName("S_CZJXJH").value.toString();

				tool.recordRel(proVo.getBranck(), "1516168904786", sumOver, "T_JXZJHFJ", proVo.getSpageCode(),
						proVo.getInpPkey(), "T_XCAQCSJDK");

			}

		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			tableEx.close();
			dbf.close();
		}

		return true;

	}

	/*
	 * OverhaulStartReport:���޿�������
	 * 
	 */
	public boolean OverhaulStartReport(HttpServletRequest _request) {
		proVo = new ProcessParameterVO(_request); // ������׼����
		MantraUtil tool = new MantraUtil();
		TableEx tableEx = null;
		Record record = null;
		DBFactory dbf = new DBFactory();
		int recordIndex = 0;
		String runSql = "";
		String sumOver = "";
		String S_DJH = "";
		// T_JXKGBG$S_CZZJHID
		try {
			tableEx = new TableEx("T_JXKGBG.S_CZZJHID S_CZJXJH,T_JXKGBG.S_DJH S_DJH", "T_JXKGBG",
					"S_ZJ = '" + proVo.getInpPkey() + "'");
			recordIndex = tableEx.getRecordCount();
			if (recordIndex != 1) {
				MantraLog.WriteProgress(MantraLog.LOG_PROGRESS,
						"[err]:019->OverHaulTheReport->recordIndex=" + recordIndex);
			}
			for (int i = 0; i < recordIndex; i++) {
				record = tableEx.getRecord(i);
				if (record.getFieldByName("S_CZJXJH").value == null) {
					MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "[err]:019->OverHaulTheReport->S_CZJXJH=null");
					return false;
				}

				sumOver = record.getFieldByName("S_CZJXJH").value.toString();

				S_DJH = record.getFieldByName("S_DJH").value.toString();

				tool.recordRel(proVo.getBranck(), "1516168904786", sumOver, "T_JXZJHFJ", proVo.getSpageCode(),
						proVo.getInpPkey(), "T_JXKGBG");


				RelationVO rel = tool.getDataByRel("RIGHT", sumOver, "T_JXZJHFJ", 2).get(0); // ���һ��ܼ��޼ƻ�,����׼������

				String RenoRepl = getRenovationReplyById(sumOver);
				
				runSql = "update T_XMZH_F set S_KG='"+S_DJH+"',S_KGBG_ID='"+proVo.getInpPkey()+"' where s_fidpf='"+rel.getRelationVOs().get(0).getS_LEFT_ID()+"' and S_ID in ("+RenoRepl+")";
				
				dbf.sqlExe(runSql, false);
				
			}

		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			tableEx.close();
			dbf.close();
		}

		return true;

	}

	public String getRenovationReplyById(String _jgid) {
		StringBuffer retStr = new StringBuffer();
		proVo = new ProcessParameterVO(); // ������׼����
		MantraUtil tool = new MantraUtil();
		TableEx tableEx = null;
		Record record = null;
		DBFactory dbf = new DBFactory();

		try {
			// String NNN = "select S_ZJ from T_JXZJHFJSB where S_PID = '"+sumOver+"'";
			tableEx = new TableEx("T_JXZJHFJSB.S_BM S_BM", "T_JXZJHFJSB", "S_PID = '" + _jgid + "'");

			int recordCount = tableEx.getRecordCount();
			for (int i = 0 ; i < recordCount; i++) {
				record=tableEx.getRecord(i);
				retStr.append("'");
				retStr.append(record.getFieldByName("S_BM").value.toString());
				if(i+1==recordCount) {
					retStr.append("'");
				}else {
					retStr.append("',");
				}
			}
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			tableEx.close();
			dbf.close();
		}

		return retStr.toString();
	}

	/*
	 * OverhaulPerform:����ִ��
	 * 
	 */
	public boolean OverhaulPerform(HttpServletRequest _request) {
		proVo = new ProcessParameterVO(_request); // ������׼����
		MantraUtil tool = new MantraUtil();
		TableEx tableEx = null;
		Record record = null;
		DBFactory dbf = new DBFactory();
		int recordIndex = 0;
		String sumOver = "";
		// T_JXZX$S_JXBGID
		try {
			tableEx = new TableEx("T_JXZX.S_JXBGID S_CZJXJH", "T_JXZX", "S_ZJ = '" + proVo.getInpPkey() + "'");
			recordIndex = tableEx.getRecordCount();
			if (recordIndex != 1) {
				MantraLog.WriteProgress(MantraLog.LOG_PROGRESS,
						"[err]:019->OverHaulTheReport->recordIndex=" + recordIndex);
			}
			for (int i = 0; i < recordIndex; i++) {
				record = tableEx.getRecord(i);
				if (record.getFieldByName("S_CZJXJH").value == null) {
					MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "[err]:019->OverHaulTheReport->S_CZJXJH=null");
					return false;
				}
				sumOver = record.getFieldByName("S_CZJXJH").value.toString();

				tool.recordRel(proVo.getBranck(), "1506343680243", sumOver, "T_JXKGBG", proVo.getSpageCode(),
						proVo.getInpPkey(), "T_JXZX");

			}

		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			tableEx.close();
			dbf.close();
		}

		return true;

	}

	/*
	 * OverhaulQualityCheckPerform:�����������ձ���
	 * 
	 */
	public boolean OverhaulQualityCheckPerform(HttpServletRequest _request) {
		proVo = new ProcessParameterVO(_request); // ������׼����
		MantraUtil tool = new MantraUtil();
		TableEx tableEx = null;
		Record record = null;
		DBFactory dbf = new DBFactory();
		int recordIndex = 0;
		String runSql = "";
		String sumOver = "";
		String S_DJH = "";
		try {

			tableEx = new TableEx("T_JXZLYSBG.S_JHFJID S_CZJXJH,T_JXZLYSBG.S_DJH S_DJH", "T_JXZLYSBG", "S_ZJ = '" + proVo.getInpPkey() + "'");
			recordIndex = tableEx.getRecordCount();
			if (recordIndex != 1) {
				MantraLog.WriteProgress(MantraLog.LOG_PROGRESS,
						"[err]:019->OverHaulTheReport->recordIndex=" + recordIndex);
			}
			for (int i = 0; i < recordIndex; i++) {
				record = tableEx.getRecord(i);
				if (record.getFieldByName("S_CZJXJH").value == null) {
					MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "[err]:019->OverHaulTheReport->S_CZJXJH=null");
					return false;
				}
				sumOver = record.getFieldByName("S_CZJXJH").value.toString();
				S_DJH = record.getFieldByName("S_DJH").value.toString();
				tool.recordRel(proVo.getBranck(), "1506343680243", sumOver, "T_JXKGBG", proVo.getSpageCode(),
						proVo.getInpPkey(), "T_JXZLYSBG");

				
				RelationVO rel = tool.getDataByRel("RIGHT", sumOver, "T_JXKGBG", 3).get(0); // ���һ��ܼ��޼ƻ�,����׼������

				String RenoRepl = getRenovationReplyById(rel.getS_LEFT_ID());
				
				MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, RenoRepl);
				
				rel=rel.getRelationVOs().get(0);
				runSql = "update T_XMZH_F set S_ZLBG='"+S_DJH+"',S_ZLYSBG_ID='"+proVo.getInpPkey()+"' where s_fidpf='"+rel.getRelationVOs().get(0).getS_LEFT_ID()+"' and S_ID in ("+RenoRepl+")";
				
				MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, runSql);
				
				dbf.sqlExe(runSql, false);
				
				
			}

		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			tableEx.close();
			dbf.close();
		}

		return true;

	}

	/*
	 * ProjectAccPerform:��Ŀ����
	 * 
	 */
	public boolean ProjectAccPerform(HttpServletRequest _request) {
		proVo = new ProcessParameterVO(_request); // ������׼����
		MantraUtil tool = new MantraUtil();
		TableEx tableEx = null;
		Record record = null;
		DBFactory dbf = new DBFactory();
		int recordIndex = 0;
		String sumOver = "";

		try {
			tableEx = new TableEx("T_XMJY.S_GLJHID S_CZJXJH", "T_XMJY", "S_ZJ = '" + proVo.getInpPkey() + "'");
			recordIndex = tableEx.getRecordCount();
			if (recordIndex != 1) {
				MantraLog.WriteProgress(MantraLog.LOG_PROGRESS,
						"[err]:019->OverHaulTheReport->recordIndex=" + recordIndex);
			}
			for (int i = 0; i < recordIndex; i++) {
				record = tableEx.getRecord(i);
				if (record.getFieldByName("S_CZJXJH").value == null) {
					MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "[err]:019->OverHaulTheReport->S_CZJXJH=null");
					return false;
				}
				sumOver = record.getFieldByName("S_CZJXJH").value.toString();

				tool.recordRel(proVo.getBranck(), "1510924423686", sumOver, "T_HZJXJH", proVo.getSpageCode(),
						proVo.getInpPkey(), "T_XMJY");

			}

		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			tableEx.close();
			dbf.close();
		}

		return true;

	}

	/*
	 * OverhaulTestRunPerform:������������
	 * 
	 */
	public boolean OverhaulTestRunPerform(HttpServletRequest _request) {
		proVo = new ProcessParameterVO(_request); // ������׼����
		MantraUtil tool = new MantraUtil();
		TableEx tableEx = null;
		Record record = null;
		DBFactory dbf = new DBFactory();
		int recordIndex = 0;
		String sumOver = "";
		// T_JXSYPJBG$S_JXZJHID
		try {
			tableEx = new TableEx("T_JXSYPJBG.S_JXZJHID S_CZJXJH", "T_JXSYPJBG", "S_ID = '" + proVo.getInpPkey() + "'");
			recordIndex = tableEx.getRecordCount();
			if (recordIndex != 1) {
				MantraLog.WriteProgress(MantraLog.LOG_PROGRESS,
						"[err]:019->OverHaulTheReport->recordIndex=" + recordIndex);
			}
			for (int i = 0; i < recordIndex; i++) {
				record = tableEx.getRecord(i);
				if (record.getFieldByName("S_CZJXJH").value == null) {
					MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "[err]:019->OverHaulTheReport->S_CZJXJH=null");
					return false;
				}
				sumOver = record.getFieldByName("S_CZJXJH").value.toString();

				tool.recordRel(proVo.getBranck(), "1510924423686", sumOver, "T_HZJXJH", proVo.getSpageCode(),
						proVo.getInpPkey(), "T_JXSYPJBG");

			}

		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			tableEx.close();
			dbf.close();
		}

		return true;

	}

	/*
	 * OverhaulCompletePerform:���޿����ܽᱨ��
	 * 
	 */
	public boolean OverhaulCompletePerform(HttpServletRequest _request) {
		proVo = new ProcessParameterVO(_request); // ������׼����
		MantraUtil tool = new MantraUtil();
		TableEx tableEx = null;
		Record record = null;
		DBFactory dbf = new DBFactory();
		int recordIndex = 0;
		String sumOver = "";
		// T_JXJGZJBG$S_JXZJHID
		try {
			tableEx = new TableEx("T_JXJGZJBG.S_JXZJHID S_CZJXJH", "T_JXJGZJBG", "S_ID = '" + proVo.getInpPkey() + "'");

			recordIndex = tableEx.getRecordCount();
			if (recordIndex != 1) {
				MantraLog.WriteProgress(MantraLog.LOG_PROGRESS,
						"[err]:019->OverHaulTheReport->recordIndex=" + recordIndex);
			}
			for (int i = 0; i < recordIndex; i++) {
				record = tableEx.getRecord(i);
				if (record.getFieldByName("S_CZJXJH").value == null) {
					MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "[err]:019->OverHaulTheReport->S_CZJXJH=null");
					return false;
				}
				sumOver = record.getFieldByName("S_CZJXJH").value.toString();

				tool.recordRel(proVo.getBranck(), "1510924423686", sumOver, "T_HZJXJH", proVo.getSpageCode(),
						proVo.getInpPkey(), "T_JXJGZJBG");

			}

		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			tableEx.close();
			dbf.close();
		}

		return true;

	}

	/*
	 * OverhaulReviewPerform:���޹���ȫ���������󱨸�
	 * 
	 */
	public boolean OverhaulReviewPerform(HttpServletRequest _request) {
		proVo = new ProcessParameterVO(_request); // ������׼����
		MantraUtil tool = new MantraUtil();
		TableEx tableEx = null;
		Record record = null;
		DBFactory dbf = new DBFactory();
		int recordIndex = 0;
		String sumOver = "";
		// T_JXGCQMZLPSBG$S_JXZJHID
		try {
			tableEx = new TableEx("T_JXGCQMZLPSBG.S_JXZJHID S_CZJXJH", "T_JXGCQMZLPSBG",
					"S_ID = '" + proVo.getInpPkey() + "'");

			recordIndex = tableEx.getRecordCount();
			if (recordIndex != 1) {
				MantraLog.WriteProgress(MantraLog.LOG_PROGRESS,
						"[err]:019->OverHaulTheReport->recordIndex=" + recordIndex);
			}
			for (int i = 0; i < recordIndex; i++) {
				record = tableEx.getRecord(i);
				if (record.getFieldByName("S_CZJXJH").value == null) {
					MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "[err]:019->OverHaulTheReport->S_CZJXJH=null");
					return false;
				}
				sumOver = record.getFieldByName("S_CZJXJH").value.toString();

				tool.recordRel(proVo.getBranck(), "1506482323524", sumOver, "T_JXJGZJBG", proVo.getSpageCode(),
						proVo.getInpPkey(), "T_JXGCQMZLPSBG");

			}

		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			tableEx.close();
			dbf.close();
		}

		return true;

	}
	
	/*
	 * OverhaulDecomp:���޷ֽ�
	 * 
	 */
	public boolean OverhaulDecomp(HttpServletRequest _request) {
		proVo = new ProcessParameterVO(_request); // ������׼����
		MantraUtil tool = new MantraUtil();
		TableEx tableEx = null;
		Record record = null;
		DBFactory dbf = new DBFactory();
		int recordIndex = 0;

		// T_JXSYPJBG$S_JXZJHID
		try {
			tableEx = new TableEx("*", "T_JXZJHFJ", "T_JXZJHFJ.S_ZJ = '" + proVo.getInpPkey() + "'");
			recordIndex = tableEx.getRecordCount();
			if (recordIndex != 1) {
				MantraLog.WriteProgress(MantraLog.LOG_PROGRESS,
						"[err]:019->OverHaulTheReport->recordIndex=" + recordIndex);
			}
			for (int i = 0; i < recordIndex; i++) {
				record = tableEx.getRecord(i);
				if("true".equals(tool.getStrByRecord(record, "S_SFWW"))){//��ί
					String generId = EString.generId();
					String strJxlx =tool.getStrByRecord(record,"S_JXLX");//��������
					StringBuffer sbr = new StringBuffer();
					String strFlowVersion = tool.getFlowVer(proVo.getSpageCode(),tool.getStrByRecord(record,"S_ZZ")); //���а汾
					sbr.append("insert into T_WWXM (SYS_FLOW_VER,S_RUN_ID,S_HTH,S_XMMC,S_GQKSSJ,S_GQJSSJ,S_WWDW,S_WWDWID,S_YFFZR,S_JFFZR,S_JXJG,S_ZZ,S_BGRQ,S_ZDR,S_ZDSJ,S_XGR,S_XGSJ,S_BS_JD,S_ID,S_FJID) ");
					sbr.append("select '"+strFlowVersion+"' AS 'SYS_FLOW_VER', '"+EString.generId()+"' AS 'S_RUN_ID','' AS 'S_HTH',S_MC  AS 'S_XMMC',S_KSSJ  AS 'S_GQKSSJ',S_JSSJ AS 'S_GQJSSJ',S_WWDW AS 'S_WWDW',S_WWDWID AS 'S_WWDWID',S_YFZR AS 'S_YFFZR',S_FZR AS 'S_JFFZR','true' AS 'S_JXJG',S_ZZ AS 'S_ZZ','"+EString.getCurDate()+"'  AS 'S_BGRQ',S_ZDR AS 'S_ZDR','"+EString.getCurDate()+"' AS 'S_ZDSJ',S_ZHXGR AS 'S_XGR','"+EString.getCurDate()+"' AS 'S_XGSJ','' AS 'S_BS_JD','"+generId+"' AS 'S_ID',S_ZJ AS 'S_FJID' from T_JXZJHFJ where T_JXZJHFJ.S_ZJ='");
					sbr.append( proVo.getInpPkey());
					sbr.append("';");
					dbf.sqlExe(sbr.toString(), true);
					sbr = new StringBuffer();
					sbr.append("insert into T_WWXMFB (S_CBXM,S_SGSD,S_Z,S_ID,S_RID,S_CBDW,S_LXFS,S_XTLL) ");
					sbr.append("select S_JXXM AS 'S_CBXM',S_KSRQ AS 'S_SGSD',S_JSRQ AS 'S_Z',S_BM AS 'S_ID','"+generId+"' AS 'S_RID','"+tool.getStrByRecord(record,"S_WWDW")+"' AS 'S_CBDW','"+tool.getStrByRecord(record,"S_LXFS")+"' AS 'S_LXFS','"+tool.getStrByRecord(record,"S_XTLXT")+"' AS 'S_XTLL' from T_JXZJHFJSB where T_JXZJHFJSB.S_PID='");
					sbr.append( proVo.getInpPkey());
					sbr.append("';");
					MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, sbr.toString());
					dbf.sqlExe(sbr.toString(), false);
					tool.recordRel(proVo.getBranck(), proVo.getSpageCode(),proVo.getInpPkey(), "T_JXZJHFJ","1506310525794",generId, "T_WWXM");
				}

			}

		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			tableEx.close();
			dbf.close();
		}

		return true;

	}
}
