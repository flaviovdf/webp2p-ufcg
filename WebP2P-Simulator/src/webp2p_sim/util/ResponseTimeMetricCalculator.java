package webp2p_sim.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

public class ResponseTimeMetricCalculator {

	private static final String REQUESTMADE= "webp2p_sim.proxy.Browser.generateRequest";
	private static final String REQUESTRESPONSE = "webp2p_sim.proxy.Browser.hereIsContent";
			
	private MetricCollector collector;

	public ResponseTimeMetricCalculator() {
		this.collector = new MetricCollector();
	}
	
	public void parseLog(File logFile) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(logFile));

		String line;
		while ((line = reader.readLine()) != null) {
			String[] tokens = line.split("\\s+");
			
			if (line.contains(REQUESTMADE)) {
				
				Long time = Long.parseLong(tokens[7]);
				Long request = Long.parseLong(tokens[14]);
				this.collector.requestMade(request, time);
				
			} else if (line.contains(REQUESTRESPONSE)) {
				
				Long time = Long.parseLong(tokens[7]);
				Long request = Long.parseLong(tokens[14]);

				this.collector.requestFinished(request, time);
				
			}
		}
		
		reader.close();
	}
	
	public DescriptiveStatistics getStats() {
		return collector.getStatistics();
	}
	
	public static void main(String[] args) throws Exception {
		ResponseTimeMetricCalculator calc = new ResponseTimeMetricCalculator();
		calc.parseLog(new File(args[0]));
		
		DescriptiveStatistics stats = calc.getStats();
		System.out.println(stats.getMean() + "\t" + stats.getStandardDeviation());
	}
	
	private class MetricCollector {
	
		private DescriptiveStatistics statistics = DescriptiveStatistics.newInstance();
		
		private Map<Long, Long> requestsStartTime = new HashMap<Long, Long>();
		
		public void requestMade(long requestID, long time) {
			requestsStartTime.put(requestID, time);
		}
		
		public void requestFinished(long requestID, long time) {
			Long startTime = requestsStartTime.remove(requestID);
			
			if (startTime != null) {
				statistics.addValue(time - startTime);
			}
		}
		
		public DescriptiveStatistics getStatistics() {
			return statistics;
		}
	}

}
