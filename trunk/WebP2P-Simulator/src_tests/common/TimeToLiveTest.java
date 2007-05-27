package common;

import util.SmartTestCase;

public class TimeToLiveTest extends SmartTestCase {

	public void testTTL() {
		int initialTTL = 10;
		TimeToLive ttl = new TimeToLive(initialTTL);
		
		for (int i = initialTTL; i >= 1; i--) {
			assertEquals(i, ttl.remaining());
			assertEquals(i - 1, ttl.decrease());
		}
		
		assertEquals(0, ttl.decrease());
		assertEquals(0, ttl.remaining());
		
		//must never be bellow zero
		assertEquals(0, ttl.decrease());
		assertEquals(0, ttl.remaining());
	}
	
}
