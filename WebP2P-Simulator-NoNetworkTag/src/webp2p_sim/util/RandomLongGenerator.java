package webp2p_sim.util;

public class RandomLongGenerator {

	private long nextId;
	
	public RandomLongGenerator() {
		this.nextId = generateRequestID();
	}
	
	private long generateRequestID() {
		return (long) (Math.random() * Long.MAX_VALUE);
	}
	
	public long getNextID() {
		long returnValue = this.nextId;
		this.nextId = generateRequestID();
		return returnValue;
	}
	
	public long peekNextID() {
		return this.nextId;
	}
}
