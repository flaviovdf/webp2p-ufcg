package webp2p_sim.util;

public class TimeToLive {

	protected int ttl;

	public TimeToLive(int ttl) {
		this.ttl = ttl;
		
	}
	
	public int decrease() {
		ttl = Math.max(0, ttl - 1);
		return ttl;
	}
	
	public int remaining() {
		return ttl;
	}
	
}
