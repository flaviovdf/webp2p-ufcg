package webp2p_sim.log;

import java.text.DateFormat;
import java.util.Date;

import org.apache.log4j.Layout;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;

import webp2p_sim.core.Clock;

public class WebP2PLayout extends Layout {

	@Override
	public void activateOptions() {
		// TODO Auto-generated method stub

	}

	@Override
	public String format(LoggingEvent event) {
		String date = DateFormat.getInstance().format(new Date(event.timeStamp));
		
		StringBuffer st = new StringBuffer();
		st.append("[REALTIME: " + date  +" - ");
		st.append("SYSTEMTIME: " + Clock.getInstance().getCurrentTick()+"] ");
		st.append("{" + event.getLevel().toString() + "} * ");
		LocationInfo info = event.getLocationInformation();
		st.append(info.getClassName() + "." + info.getMethodName());
		st.append(" ==> "+event.getMessage()+"\n");
		return st.toString();
	}

	@Override
	public boolean ignoresThrowable() {
		// TODO Auto-generated method stub
		return false;
	}

}
