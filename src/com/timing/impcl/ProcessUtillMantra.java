package com.timing.impcl;

import com.yulongtao.db.TableEx;
import com.yulongtao.db.DBFactory;
import com.yulongtao.db.Record;

public class ProcessUtillMantra {
	TableEx table = null;
	DBFactory dbf = null;

	
	/**
	 * @param _flowId
	 * @param _runId
	 * @param _flowVer
	 * @param _nowUserCode
	 * @return boolean
	 */
	public final boolean delRunLog(String _flowId, String _runId, String _flowVer, String _nowUserCode) {
		Record record = null;
		String lastAuditUser = ""; // 最后处理人
		int rowCount = 0;
		int colCount = 0;
		StringBuffer doSql = new StringBuffer();
		dbf = new DBFactory();
		try {
			table = new TableEx("*", "T_SYS_FLOW_LOG", "S_RUN_ID='" + _runId + "' AND S_FLOW_ID='" + _flowId
					+ "' AND S_AUDIT_VERSION='" + _flowVer + "' order by S_AUD_DATE desc");
			
			rowCount = table.getRecordCount();
			
			colCount = table.getColCount();

			if (rowCount == 0) return false;
	
			record = table.getRecord(0);
			
			lastAuditUser = record.getFieldByName("S_AUD_USER").value.toString();
			
			if (_nowUserCode.equals(lastAuditUser)) {// 删除 数据 , 由于表中无独立主键, 故全条匹配
				
				doSql.append("DELETE FROM T_SYS_FLOW_LOG WHERE ");
				String strBakSql="INSERT INTO T_SYS_FLOWLOG_BAK (S_FLOW_ID, S_RUN_ID, S_NODE_ID, S_AUD_USER, S_AUD_DATE, S_AUD_STAUS, S_AUD_COMMENT, S_AUDIT_VERSION) VALUES ('"+
				record.getFieldByName("S_FLOW_ID").value+"', '"+
				record.getFieldByName("S_RUN_ID").value+"', '"+
				record.getFieldByName("S_NODE_ID").value+"', '"+
				record.getFieldByName("S_AUD_USER").value+"', '"+
				record.getFieldByName("S_AUD_DATE").value+"', '"+
				record.getFieldByName("S_AUD_STAUS").value+"', '"+
				record.getFieldByName("S_AUD_COMMENT").value+"', '"+
				record.getFieldByName("S_AUDIT_VERSION").value+"')";
				for(int i = 1 ; i <= colCount ; i ++){ 
					
					doSql.append(record.getFieldName(i)+"='"+record.getFieldByName(record.getFieldName(i)).value.toString()+"' ");
					if(i!=colCount) {
						doSql.append("and ");
					}
					
				}

				dbf.sqlExe(doSql.toString(), false);
				dbf.sqlExe(strBakSql, true);
				
				return true;
			}
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		}finally {
			table.close();
			dbf.close();
		}
		return false;
	}
}
