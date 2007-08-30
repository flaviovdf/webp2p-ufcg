package webp2p_sim.core;



public class Simulator {

	private final long maxTicks;
	
	public Simulator(Params params) {
		this.maxTicks = params.getSimTime();
	}
	
	public final long getNumberOfTicks() {
		return maxTicks;
	}
	
	public final void simulate() {
		Clock.getInstance().countToTick(maxTicks);
	}
}
