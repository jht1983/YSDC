package org.timing.SisDitDat;

import com.timing.impcl.MantraLog;
import com.yulongtao.web.event.Event;

public class EveCleanSisData extends Event {
	

		@Override
		public boolean isRun() {

			MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "EventCleanSisData.run()");
			return true;
		}

		@Override
		public void run() {
			MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "EventCleanSisData.run()");
			//clean the sis table data
		}

	

}
