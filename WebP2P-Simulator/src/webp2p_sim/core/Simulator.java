package webp2p_sim.core;


public abstract class Simulator {

	private final long maxTicks;

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
}
