package webp2p_sim.util;


public class InfinitTimeToLive extends TimeToLive {

	public InfinitTimeToLive() {
		super(Integer.MAX_VALUE);
	}
	
	@Override
	public int decrease() {
		return ttl;
	}

}
