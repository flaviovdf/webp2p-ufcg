package webp2p_sim.core;

import webp2p_sim.util.ResponseTimeMetricCollector;


public class Simulator {

	private final long maxTicks;
	protected ResponseTimeMetricCollector collector = new ResponseTimeMetricCollector();
	
	public Simulator(Params params) {
		this.maxTicks = params.getSimTime();
	}
	
	public final long getNumberOfTicks() {
		return maxTicks;
	}
	
	public final void simulate() {
		while (Clock.getInstance().getCurrentTick() <= maxTicks) {
			Clock.getInstance().countTick();
		}
	}

	public String printResults() {
		double mean = collector.getStatistics().getMean();
		return "Mean Browser Response Time = " + mean;
	}
}
