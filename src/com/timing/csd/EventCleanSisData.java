/**
 * 
 */
package com.timing.csd;

import java.util.HashMap;
import java.util.Vector;
import com.timing.util.TimingTaskTool;

import com.timing.impcl.MantraLog;
import com.timing.impcl.MantraUtil;
import com.yulongtao.web.event.Event;


import java.text.SimpleDateFormat;
import java.util.Date;
import com.sis.util.TJSisData;



import com.yulongtao.db.DBFactory;

public class EventCleanSisData extends Event {

	public static Vector<HashMap<String, String>> vecGJStatus = new Vector<HashMap<String, String>>();
	public Vector<HashMap<String, String>> runGJStatus = new Vector<HashMap<String, String>>();

	public static int sys_index = 0;
	// private static final long timeInterval = 10 * 60 * 1000L;
	// super.lFLAGTIME = 60000L;
	private int index = 0;

	TimingTaskTool timTool = new TimingTaskTool();
	
	//--SIS Data--start
    static String _getSisDate = "";
    
    static boolean isRun=false;
    public static boolean isStartSis=false;
    //--SIS Data--end

	public boolean isRun() {
	    
	    long sys_now_tim=System.currentTimeMillis();
try {
        if(isStartSis==true){
                     SimpleDateFormat sdf_ymd = new SimpleDateFormat("yyyy-MM-dd");
                		    	
                        	MantraUtil tool = new MantraUtil();
                        	
                        	MantraLog.WriteProgress(MantraLog.LOG_PROGRESS,
                    						"[:019]->SisDataTiming->isRun:");
                    
                    		tool = new MantraUtil(); // ������
                    		String strNowDate = sdf_ymd.format(new Date()); // ��ǰʱ��
                    		if("".equals(_getSisDate)) {  //��ʼ��
                    			_getSisDate=strNowDate;
                    		}else if(strNowDate.equals(_getSisDate)){  //���

                    		}else {	//�����
                    			int dataRow = tool.getDateCont("t_sis_date_day", "DATETIME='" + _getSisDate + "'"); // �ж����ݿ��Ƿ��е��������
                    			if (dataRow == 0) {
                    				isRun = true;
                    			}
                    		}
                			
                		
                            if(isRun==true){
                                TJSisData sisTool = new TJSisData();
                    		    boolean retBool = sisTool.obtainData(_getSisDate); // �����������е�����
                    		    if (retBool = true) {
                    			    _getSisDate = sdf_ymd.format(new Date());
                    	    	}
                    	    	isRun=false;
                            }
                }

	} catch (Exception e) {
	    	MantraLog.WriteProgress(MantraLog.LOG_PROGRESS,
                    					"[:019]->SisDataTiming->isRun:ERR");
			MantraLog.fileCreateAndWrite(e);
		}
		try {
//-----------��ȡSIS����	
        
		   
  
		    
//--------��ʱ����
			if (vecGJStatus.size() != 0) { // �ǿ�����
				HashMap<String, String> runMap = null;
  
				for (int i = 0, j = vecGJStatus.size(); i < j; i++) { // ��������
				
					runMap = vecGJStatus.get(i);
					String SYS_LASTDATE = runMap.get("SYS_LASTDATE"); // ��һ����ʱ��
					String SYS_NEXTDATE = runMap.get("SYS_NEXTDATE"); // ��һ����ʱ��
					String SYS_TYPE = runMap.get("SYS_TYPE"); // ���
					String SYS_START = runMap.get("SYS_START"); // ��ʼʱ��
					String SYS_AHEADDay = runMap.get("SYS_AHEADDay"); // ��ǰ����
					String cycles = runMap.get("cycles"); // ��ǰ����
					String S_QYZT = runMap.get("S_QYZT"); // ����״̬
					
					// 01_YQY is on, 02_TY is off
					if ("01_YQY".equals(S_QYZT) && timTool.runTimeCheck(SYS_NEXTDATE, SYS_AHEADDay)) { // �������ʱ���������ʱ��
						String[] cdit = timTool.nextTimeCompute(SYS_NEXTDATE, SYS_TYPE, cycles);

						runMap.put("SYS_LASTDATE", cdit[0]);
						runMap.put("SYS_NEXTDATE", cdit[1]);

						runGJStatus.add(runMap);
					}

				}
			} else {
				timTool.initEventClParameter();
			}
			if (runGJStatus.size() > 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
		    	MantraLog.WriteProgress(MantraLog.LOG_PROGRESS,
                    					"[:019]->EventCl->isRun:ERR");
			MantraLog.fileCreateAndWrite(e);
		}
		return false;
	}

	public void run() {

		HashMap<String, String> hmp = null;
		MantraUtil tool = new MantraUtil();
        MantraLog.WriteProgress(MantraLog.LOG_PROGRESS,
                    					"[:019]->EventCl->isRun:"+runGJStatus.size());
		for (int z = 0; z < runGJStatus.size(); z++) {
			hmp = runGJStatus.get(z);
			timTool.runningStartEvent(hmp);
		}
		
		if (runGJStatus.size() != 0) {
			runGJStatus.clear();
		}
	}

	public static Vector<HashMap<String, String>> getVecGJStatus() {
		return vecGJStatus;
	}

	public static void setVecGJStatus(HashMap<String, String> vecGJStatus) {
		EventCleanSisData.vecGJStatus.add(vecGJStatus);
	}

	// SPAGECODE='+arr[1]+'&sys_bed=true&S_ID='+arr[2]+'&bmid='+arr[3]

}
