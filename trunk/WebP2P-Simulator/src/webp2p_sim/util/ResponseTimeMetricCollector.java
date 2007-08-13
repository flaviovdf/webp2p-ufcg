package webp2p_sim.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

import webp2p_sim.core.Clock;

public class ResponseTimeMetricCollector {

	private DescriptiveStatistics statistics = DescriptiveStatistics.newInstance();
	
	private Map<Long, Long> requestsStartTime = new HashMap<Long, Long>();
	
	public void requestMade(long requestID) {
		requestsStartTime.put(requestID, Clock.getInstance().getCurrentTick());
	}
	
	public void requestFinished(long requestID) {
		Long startTime = requestsStartTime.remove(requestID);
		
		if (startTime != null) {
			statistics.addValue(Clock.getInstance().getCurrentTick() - startTime);
		}
	}
	
	public DescriptiveStatistics getStatistics() {
		return statistics;
	}
}
