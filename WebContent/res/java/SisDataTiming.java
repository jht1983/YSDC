package com.timing.impcl;

import com.yulongtao.web.event.Event;
import com.timing.impcl.MantraUtil;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.sis.util.TJSisData;

public class SisDataTiming extends Event {
	static String _getSisDate = "";
	MantraUtil tool = null;
	SimpleDateFormat sdf_ymd = new SimpleDateFormat("yyyy-MM-dd");
	int index = 0;

	@Override
	public boolean isRun() {
	    	MantraLog.WriteProgress(MantraLog.LOG_PROGRESS,
						"[:019]->SisDataTiming->isRun:");
		boolean isRun = false; // �ж��Ƿ�����
		if (index == 60) {
			tool = new MantraUtil(); // ������
			String strNowDate = sdf_ymd.format(new Date()); // ��ǰʱ��
			if("".equals(_getSisDate)) {  //��ʼ��
				_getSisDate=strNowDate;
			}else if(strNowDate.equals(_getSisDate)){  //���
				MantraLog.WriteProgress(MantraLog.LOG_PROGRESS,
						"[:019]->SisDataTiming->isRun:"+strNowDate);
			}else {	//�����
				int dataRow = tool.getDateCont("t_sis_date_day", "DATETIME='" + _getSisDate + "'"); // �ж����ݿ��Ƿ��е��������
				if (dataRow == 0) {
					isRun = true;
				}
			}
			index = 0;
		} else {
			index++;
		}

		return isRun;
	}

	@Override
	public void run() {
		TJSisData sisTool = new TJSisData();
		boolean retBool = sisTool.obtainData(_getSisDate); // �����������е�����
		if (retBool = true) {
			_getSisDate = sdf_ymd.format(new Date());
		}
	}
}
