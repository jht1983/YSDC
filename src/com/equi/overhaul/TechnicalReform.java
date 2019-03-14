package com.equi.overhaul;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.timing.impcl.MantraLog;
import com.timing.impcl.MantraUtil;
import com.timing.impcl.ProcessParameterVO;
import com.yulongtao.db.DBFactory;
import com.yulongtao.db.FieldEx;
import com.yulongtao.db.Record;
import com.yulongtao.db.TableEx;
import com.yulongtao.util.EString;

public class TechnicalReform {
	ProcessParameterVO proVo = null;//ʵ����
	
	
	/**
	 * ResearchReport �������о�����
	 * */
	public void ResearchReport(HttpServletRequest _request) {
		MantraUtil tool = new MantraUtil();
		proVo = new ProcessParameterVO(_request); // ������׼����
		TableEx tableEx = null;
		Record record = null;
		DBFactory dbf = new DBFactory();
		int recordIndex = 0;
		String sumOver = "";
		try {
			tableEx = new TableEx("T_KXXYJBG.S_SBID S_CZJXJH","T_KXXYJBG","S_ID = '"+proVo.getInpPkey()+"'");
			recordIndex = tableEx.getRecordCount();
			if(recordIndex!=1) {
				MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "[err]:019->OverHaulTheReport->recordIndex="+recordIndex);
			}
			for(int i = 0 ; i < recordIndex ; i++) {
				record = tableEx.getRecord(i);
				if(record.getFieldByName("S_CZJXJH").value == null) {
					MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "[err]:019->OverHaulTheReport->S_CZJXJH=null");
					return ;
				}

				
				sumOver = record.getFieldByName("S_CZJXJH").value.toString();
				
				dbf.exeSqls("update t_xmsb set S_FLAGBG='1' where t_xmsb.S_ID='"+sumOver+"' ", false);
				
				tool.recordRel(proVo.getBranck(), proVo.getSpageCode(),
						proVo.getInpPkey(), "T_KXXYJBG", "1500359881671", sumOver, "T_XMSB");

			}

		}catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		}finally {
			tableEx.close();
			dbf.close();
		}
	}
	 
	/**
	 * ProjectSummary  ��Ŀ����
	 * */
	public void ProjectSummary(HttpServletRequest _request) {
		MantraUtil tool = new MantraUtil();
		proVo = new ProcessParameterVO(_request); // ������׼����
		TableEx tableEx = null;
		Record record = null;
		DBFactory dbf = new DBFactory();
		int recordIndex = 0;
		String sumOver = "";
		String strSid = proVo.getInpPkey();
		String[] basicData = {
				"T_JXXMPFQK.SYS_FLOW_VER,T_JXXMPFQK.S_RUN_ID,T_JXXMPFQK.S_ID,T_JXXMPFQK.S_CJR,T_JXXMPFQK.S_CJRBM,T_JXXMPFQK.S_CJSJ,T_JXXMPFQK.S_DJH,T_JXXMPFQK.S_DJRQ,T_JXXMPFQK.S_JZ,T_JXXMPFQK.S_ND,T_JXXMPFQK.S_ZZ",
				"<<FLOW_ID>>,<<UUID>>,<<SYS_GENER_ID>>,T_XMZH.S_ZDR_NAME,T_XMZH.S_ZDR,T_XMZH.S_ZDSJ,<<SYS_GENER_ID>>,T_XMZH.S_DJRQ,T_XMZH.S_JZ,T_XMZH.S_ND,T_XMZH.S_ZZ",
				 }; //��Ӧ����
		
		try {
			dbf.sqlExe("update T_XMSB set S_FLAGBG='3' where S_DJH in(select S_XH from T_XMZH_F where S_FID='"+strSid+"')",true);
			StringBuffer sbr = new StringBuffer();
			sbr.append("insert into T_JXXMPFQK (");
			sbr.append( basicData[0]);
			sbr.append(" ) select ");
			sbr.append( basicData[1]);
			sbr.append(" from T_XMZH where T_XMZH.S_ID='");
			sbr.append(strSid);
			sbr.append("'");
			
			sbr =new StringBuffer(tool.sqlDisCom(sbr.toString(), "pageCode=1517262373552&bmid=" + proVo.getBranck())) ;//����SQL
			
			dbf.sqlExe(sbr.toString(), true);

			dbf.sqlExe("update T_XMZH_F set S_FIDPF='"+tool.getOrdGreId()+"' where S_FID='"+strSid+"'",true);
			
			tool.recordRel(proVo.getBranck(), "1513347650028", strSid, "T_XMZH", "1517262373552",
					tool.getOrdGreId(), "T_JXXMPFQK");
			
//			tableEx = new TableEx("S_XH","T_XMZH_F","S_FID='"+strSid+"'");
//			
//			for(int i = 0 ,j = tableEx.getRecordCount() ; i < j ; i ++) {
//				record = tableEx.getRecord(i);
//				String  S_XH = record.getFieldByName("S_XH").value.toString();
//				tool.recordRel(proVo.getBranck(), "1513347650028", strSid, "T_XMZH", "1517262373552",
//						S_XH, "T_JXXMPFQK");
//			}
			
			
		}catch(Exception e) {
			MantraLog.fileCreateAndWrite(e);
		}finally {
			tableEx.close();
			dbf.close();
		}
	}
	
	/**
	 * TechnicalReformReply  ��������->������Ŀ����
	 * */
	public void TechnicalReformReply(HttpServletRequest _request) {
		MantraUtil tool = new MantraUtil();
		proVo = new ProcessParameterVO(_request); // ������׼����
		TableEx tableEx = null;
		Record record = null;
		DBFactory dbf = new DBFactory();
		int recordIndex = 0;
		HttpSession session = _request.getSession();
		String uuid = EString.generId();
		String strFlowVersion="";
		String SYS_STRCURUSER = (String) session.getAttribute("SYS_STRCURUSER");
		String SYS_STRCURUSERNAME = (String) session.getAttribute("SYS_STRCURUSERNAME");
		String SYS_STRBRANCHID = (String) session.getAttribute("SYS_STRBRANCHID");
		String CurDate=EString.getCurDate();
		try {
			String exSql = "select * from T_JXXMPFQK where S_ID='"+proVo.getInpPkey()+"'";
			tableEx = dbf.query(exSql);
			Record rd = tableEx.getRecord(0);
			
			StringBuffer sbr = new StringBuffer();
			sbr.append("insert into T_HZJXJH (S_RUN_ID,SYS_FLOW_VER,S_CJR,S_CJRQ,S_XGR,S_XGRQ,S_DJRQ,S_JXLX,S_JHMC,S_JHLX,S_ZDR,S_ZDRQ,S_DJH,S_JHBZBM,S_JHBZBMBM,S_JHBZR,S_JHBZRBM,S_JHBZSJ,S_JZH,S_JHZFY,S_JGDJH,S_JGID,S_ZJ,S_ZZ,S_TJR,S_TJR_NAME) ");//����ID,�ܷ���,����,��������,�ƻ�����,���ܼƻ�ID,���ܼƻ����ݺ�
			sbr.append("values(");
			sbr.append("'").append(tool.getShortUuid()).append("'").append(",");//���к�
			sbr.append("'").append(strFlowVersion).append("'").append(",");//�汾��
			sbr.append("'").append(SYS_STRCURUSER).append("'").append(",");//������
			sbr.append("'").append(CurDate).append("'").append(",");//��������
			sbr.append("'").append(SYS_STRCURUSER).append("'").append(",");//�޸���
			sbr.append("'").append(CurDate).append("'").append(",");//�޸�����
			sbr.append("'").append(CurDate).append("'").append(",");//��������
			sbr.append("'").append("06").append("'").append(",");//��������
			sbr.append("'").append(getColString("S_ND", rd)+"_����").append("'").append(",");//�ƻ�����
			sbr.append("'").append("NDJH").append("'").append(",");//�ƻ�����
			sbr.append("'").append(SYS_STRCURUSER).append("'").append(",");//�Ƶ���
			sbr.append("'").append(CurDate).append("'").append(",");//�Ƶ�����
			sbr.append("'").append(uuid).append("'").append(",");//���ݺ�
			sbr.append("'").append(session.getAttribute("SYS_STRBRANCHNAME")).append("'").append(",");//��������
			sbr.append("'").append(session.getAttribute("SYS_STRBRANCHID")).append("'").append(",");//���ű���
			sbr.append("'").append(SYS_STRCURUSERNAME).append("'").append(",");//�ƻ�����������
			sbr.append("'").append(SYS_STRCURUSER).append("'").append(",");//�����˴���
			sbr.append("'").append(CurDate).append("'").append(",");//����ʱ��
			sbr.append("'").append(getColString("S_JZ", rd)).append("'").append(",");//�����
			sbr.append("'").append("").append("'").append(",");//�ƻ��ܷ���
			sbr.append("'").append(getColString("S_DJH", rd)).append("'").append(",");//�������ݺ�
			sbr.append("'").append(getColString("S_ID", rd)).append("'").append(",");//����ID
			sbr.append("'").append(uuid).append("'").append(",");//����
			sbr.append("'").append(getColString("S_ZZ", rd)).append("'").append(",");//��֯
			sbr.append("'").append(SYS_STRCURUSER).append("'").append(",");//�ύ��
			sbr.append("'").append(SYS_STRCURUSERNAME).append("'");//�ύ������
			sbr.append(");");
			dbf.sqlExe(sbr.toString(), true);
			
			tool.recordRel(proVo.getBranck(), proVo.getSpageCode(), proVo.getInpPkey(), "T_JXXMPFQK",
					"1510924423686", uuid, "T_HZJXJH"); //������ϵ
			
			sbr = new StringBuffer();
			sbr.append("insert into T_HZJXJH_F (S_PID,S_BDHF,S_SBBM,S_SBBJMC,S_JHFY,S_ZY,S_JXXM,S_XMFZR_BM,S_XMFZR,S_ID) ");
			sbr.append("select "+uuid+" AS 'PID',S_XMMC AS 'S_BDHF',S_JGDX AS 'S_SBBM',S_JGDX_NAME AS 'S_SBMC',S_JHZJ AS 'S_JHFY',S_ZY AS 'S_ZY',S_XMMC AS 'S_JXXM',S_FZR AS 'S_XMFZR',S_FZR_NAME AS 'S_XMFZR_NAME',S_ID AS 'S_ID' from T_XMZH_F where T_XMZH_F.S_FIDPF=");
			sbr.append("'");
			sbr.append(proVo.getInpPkey());
			sbr.append("';");
	        MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "SQl:"+sbr.toString());
			dbf.sqlExe(sbr.toString(), true);
			
		}catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		}finally {
			tableEx.close();
			dbf.close();
		}
	}

	public String getColString(String _strCol,Record rd){
		String strReturn = "";
		try {
			FieldEx ex = rd.getFieldByName(_strCol);
		
			Object obj= ((ex==null||"".equals(ex))?"":(ex.value));
			strReturn = (obj==null||"".equals(obj))?"":obj.toString();
		} catch (Exception e) {
			strReturn = "";
			MantraLog.fileCreateAndWrite(e);
			// String[] strArrayFlowLog22 = {"333","","",new Date()+"",_strCol,_strCol,"getColString",getErrorInfoFromException(e)};
			// insertFlowLog("1", strArrayFlowLog22);
			e.printStackTrace();
		}finally{
			
		}
		return strReturn ;
	}
	
}
