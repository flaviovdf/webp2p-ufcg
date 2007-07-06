package webp2p_sim.core;


public abstract class Simulator {

	private final long maxTicks;

	public Simulator(long maxTicks) {
		this.maxTicks = maxTicks;
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
