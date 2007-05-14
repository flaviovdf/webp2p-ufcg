package common;

public class InfinitTimeToLive extends TimeToLive {

	public InfinitTimeToLive() {
		super(1);
	}
	
	@Override
	public int decrease() {
		return ttl;
	}

}
